/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cg.farmhover;

import cg.farmhover.gl.core.Light;
import cg.farmhover.gl.jWaveFront.JWavefrontObject;
import cg.farmhover.gl.util.Matrix4;
import cg.farmhover.gl.util.Shader;
import cg.farmhover.gl.util.ShaderFactory;
import cg.farmhover.gl.util.ShaderFactory.ShaderType;
import cg.farmhover.objects.Camera;
import cg.farmhover.objects.Cow;
import cg.farmhover.objects.Ufo;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.BitSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GL;
import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

/**
 *
 * @author Hikari Kyuubi
 */
public class TestScene extends KeyAdapter implements GLEventListener { 
    
    private final Shader shader; // Gerenciador dos shaders
    private final Matrix4 modelMatrix;
    private final Matrix4 projectionMatrix;
    private final Matrix4 viewMatrix;
    private final Light light;
    private final JWavefrontObject farm;
    private float delta, aspectRatio;
    private Ufo ufo;
    private Camera cam;
    public static ArrayList<Cow> cows;
    public static Cow risingCow;
    private JWavefrontObject shadow;
    private float floatingSpeed;
    private BitSet keyBits;
    private Updater updater;
    

    
    public TestScene() {
        keyBits = new BitSet(256);
        updater = new Updater();
        shader = ShaderFactory.getInstance(ShaderType.COMPLETE_SHADER);
        modelMatrix = new Matrix4();
        projectionMatrix = new Matrix4();
        viewMatrix = new Matrix4();
        light = new Light();
        cows = new ArrayList();
        Random rand = new Random();
        Cow cow;
        for(int i = 0; i<15; ++i){
            cow = new Cow(rand.nextInt(50)-25,1,rand.nextInt(50)-25); // -25 pra ir de -25 a +25, já que a fazenda tá w=50 h=50
            cow.ry = rand.nextInt(360);
            cows.add(cow); 
            
        }
        ufo = new Ufo();
        cam = new Camera(ufo);
        farm = new JWavefrontObject(new File(".\\models\\cube.obj"));
        shadow = new JWavefrontObject(new File(".\\models\\shadow.obj"));
        delta = 5f;

        
        //========================== depois consertar =================
        ufo.setScalex(3);
        ufo.setScaley(3);
        ufo.setScalez(3);

        floatingSpeed = 0f;

    }
    
    @Override // Configura a inicialização
    public void init(GLAutoDrawable glad) {
        // Get pipeline
        GL3 gl = glad.getGL().getGL3();

        // Print OpenGL version
        System.out.println("OpenGL Version: " + gl.glGetString(GL.GL_VERSION) + "\n");

        gl.glClearColor(0.075f, 0.027f, 0.227f, 1.0f);
        gl.glClearDepth(1.0f);

        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glEnable(GL.GL_CULL_FACE);

        //inicializa os shaders
        shader.init(gl);

        //ativa os shaders
        shader.bind();

        //inicializa a matrix Model and Projection
        modelMatrix.init(gl, shader.getUniformLocation("u_modelMatrix"));
        projectionMatrix.init(gl, shader.getUniformLocation("u_projectionMatrix"));
        viewMatrix.init(gl, shader.getUniformLocation("u_viewMatrix"));
        
        //init the light
        light.setPosition(new float[]{0, 50, -50, 1.0f});
        light.setAmbientColor(new float[]{0.1f, 0.1f, 0.1f, 1.0f});
        light.setDiffuseColor(new float[]{0.75f, 0.75f, 0.75f, 1.0f});
        light.setSpecularColor(new float[]{0.7f, 0.7f, 0.7f, 1.0f});
        light.init(gl, shader);


        ufo.init(glad, shader);
         for(int i = 0; i<cows.size(); ++i){
            cows.get(i).init(glad, shader);
            cows.get(i).getModel().unitize();
        }

        try {
            //init the model
            farm.init(gl, shader);
            farm.unitize();
//            shadow.init(gl, shader);
//            shadow.unitize();
            //init the model

        } catch (IOException ex) {
            Logger.getLogger(TestScene.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        }

        light.bind();
    }
    
    @Override // Chamado pelo animator
    public void display(GLAutoDrawable glad) {
        GL3 gl = glad.getGL().getGL3(); // Contexto de desenho
        
        // A cada atualização, limpa de acordo com a cor do buffer
        gl.glClear(GL3.GL_COLOR_BUFFER_BIT | GL3.GL_DEPTH_BUFFER_BIT);
        updater.movementApplier(keyBits, ufo, cam);
        
        /* Projeção e View */
        
        // Carrega a matriz de projeção perspectiva
        projectionMatrix.loadIdentity();
        projectionMatrix.perspective(45.0f, aspectRatio, 0.1f, 100.0f);
        projectionMatrix.bind();

        // Carrega a camera para acompanhar o OVNI
        viewMatrix.loadIdentity();
        viewMatrix.lookAt(
                cam.getX(), cam.getY(), cam.getZ(),
                ufo.getX(), ufo.getY(), ufo.getZ(),
                cam.getLookUpX(), cam.getLookUpY(), cam.getLookUpZ());
        viewMatrix.bind();
        
        /* Elementos fixos do cenário */
        
        /* Desenho da fazenda */
        modelMatrix.loadIdentity();
        modelMatrix.translate(0, -1f, 0);
        modelMatrix.scale(50, 1, 50);
        modelMatrix.bind();
        farm.draw();
        
        // sombra
//        modelMatrix.loadIdentity();
//        modelMatrix.translate(ufo.getX(),2,ufo.getZ());
//        modelMatrix.scale(3f,3f,3f);
//        modelMatrix.bind();
//        shadow.draw();

        /* Desenho das vacas */
        
        for (Cow cow : cows) {
            cow.applyGravity();
            modelMatrix.loadIdentity();         

            modelMatrix.translate(cow.getX(), cow.getY(), cow.getZ());
            modelMatrix.rotate(cow.getRy(), 0, 1, 0);

            modelMatrix.bind();
            cow.getModel().draw();
        }

        /* Desenho do OVNI */
        
        modelMatrix.loadIdentity();
        modelMatrix.translate(0,0.1f*(float)Math.sin(Math.toRadians(floatingSpeed)),0);
        modelMatrix.translate(ufo.getX(),ufo.getY(),ufo.getZ());
        modelMatrix.rotate(-1*ufo.getRy(), 0, 1, 0);
        modelMatrix.scale(2,2,3);
        modelMatrix.rotate(ufo.getRx(), 1, 0, 0);
        modelMatrix.rotate(ufo.getRz(), 0, 0, 1);
        modelMatrix.bind();
        ufo.getModel().draw();
        floatingSpeed += 2;
    }

    @Override
    public void keyPressed(KeyEvent event) {
        int keyCode = event.getKeyCode();
        keyBits.set(keyCode);
    }

    @Override
    public void keyReleased(KeyEvent event) {
        int keyCode = event.getKeyCode();
       
        if (keyCode == KeyEvent.VK_SPACE) {
            if(risingCow != null){
                risingCow.rising = false;
                risingCow = null;
            }
        }
        
        keyBits.clear(keyCode);       
    }
    
    @Override
    public void dispose(GLAutoDrawable glad) {
        ufo.getModel().dispose();
        for(int i = 0; i < cows.size(); ++i){
            cows.get(i).getModel().dispose();
        }        
    }

    @Override
    public void reshape(GLAutoDrawable glad, int x, int y, int width, int height) {
        GL3 gl = glad.getGL().getGL3();
        gl.glViewport(0, 0, width, height); // update the viewport
        aspectRatio = (float)width/(float)(height);//uptdate aspectratio para usar na matriz de projecao
    }
}