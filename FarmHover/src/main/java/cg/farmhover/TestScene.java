/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cg.farmhover;

import cg.farmhover.gl.core.Light;
import cg.farmhover.gl.jWaveFront.JWavefrontObject;
import cg.farmhover.gl.util.Matrix4;
import cg.farmhover.models.SimpleModel;
import cg.farmhover.models.WiredCube;
import cg.farmhover.gl.util.Shader;
import cg.farmhover.gl.util.ShaderFactory;
import cg.farmhover.gl.util.ShaderFactory.ShaderType;
import cg.farmhover.objects.Cow;
import cg.farmhover.objects.Ufo;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
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
    public static ArrayList<Cow> cows;
    private Cow risingCow;
    private JWavefrontObject shadow;
    
    public TestScene() {
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
        farm = new JWavefrontObject(new File(".\\models\\cube.obj"));
        shadow = new JWavefrontObject(new File(".\\models\\shadow.obj"));
        delta = 5f;
        
        //========================== depois consertar =================
        ufo.setScalex(3);
        ufo.setScaley(3);
        ufo.setScalez(3);
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

        // Carrega a matriz de projeção perspectiva
        projectionMatrix.loadIdentity();
        //usar o aspectRatio para manter a proporcao dos objetos
        projectionMatrix.perspective(45.0f, aspectRatio, 0.1f, 100.0f);
        projectionMatrix.bind();

        // Carrega a camera para acompanhar o OVNI
        viewMatrix.loadIdentity();
        viewMatrix.lookAt(
                ufo.getX() + 10*ufo.getLookat('x'), ufo.getY() + 10, ufo.getZ() + 10*ufo.getLookat('z'), // Arranjar forma de acompanhar a rotação da nave depois;
                ufo.getX(), ufo.getY(), ufo.getZ(),
                0, 1, 0);
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
        for(int i = 0; i<cows.size(); ++i){
            cows.get(i).applyGravity();
            modelMatrix.loadIdentity();
            modelMatrix.translate(cows.get(i).getX(),cows.get(i).getY(),cows.get(i).getZ());
            modelMatrix.rotate(cows.get(i).getRy(), 0, 1, 0);
            modelMatrix.bind();
            cows.get(i).getModel().draw();
        }

        /* Desenho do OVNI */
        modelMatrix.loadIdentity();
        modelMatrix.translate(ufo.getX(),ufo.getY(),ufo.getZ());
        modelMatrix.scale(3f,3f,3f);
        modelMatrix.rotate(ufo.getRx(), 1, 0, 0);
        //modelMatrix.rotate(ufo.getRy(), 0, 1, 0);
        modelMatrix.rotate(ufo.getRz(), 0, 0, 1);
        modelMatrix.bind();
        ufo.getModel().draw();
        //System.err.println("X, Y, Z, ry= " + (ufo.getX() + 10*ufo.getLookat('x')) + " "+(ufo.getY() + 10) + " " + (ufo.getZ() + 10*ufo.getLookat('z')) + " " + ufo.getRy());
    }
    
    @Override
    public void keyReleased(KeyEvent e){
         switch (e.getKeyCode()) {
             case KeyEvent.VK_SHIFT:
                if(risingCow!=null){
                    risingCow.rising = false;
                    risingCow = null;
                }
                break;
         }
    }
    
    @Override
    public void keyPressed(KeyEvent e) {

        switch (e.getKeyCode()) {
            case KeyEvent.VK_SHIFT: // abduz a vaca
                System.out.println(ufo.getX()+":"+ufo.getZ());
                float ux= ufo.getX();
                float uz = ufo.getZ();
                for(int i = 0; i<cows.size(); ++i){
//                    if(cows.get(i).getX()==ux && cows.get(i).getZ()==uz){
                    if(cows.get(i).isUnderUFO(ufo)){
                        cows.get(i).uprise(ufo);
                        risingCow = cows.get(i);
                        break;
                    }
                }
                break;
            /* UFO navigation */
            case KeyEvent.VK_SPACE:
                ufo.move(1, 0);
                break;
            case KeyEvent.VK_W:
                ufo.move(0, 1);
                break;
            case KeyEvent.VK_S:
                ufo.move(0, -1);
                break;
            /* UFO rotation */
            case KeyEvent.VK_A:
                ufo.rotate(0, -1, 0);
                break;
            case KeyEvent.VK_D:
                ufo.rotate(0, 1, 0);
                break;
            case KeyEvent.VK_Q:
                ufo.rotate(0, 0, 1);
                break;
            case KeyEvent.VK_E:
                ufo.rotate(0, 0, -1);
                break;
            case KeyEvent.VK_2:
                ufo.rotate(1, 0, 0);
                break;
            case KeyEvent.VK_X:
                ufo.rotate(-1, 0, 0);
                break;
            /* old movement */
            case KeyEvent.VK_UP:
                ufo.move(0, 0, -0.25f);
                break;
            case KeyEvent.VK_DOWN:
                ufo.move(0, 0, 0.25f);
                break;
            case KeyEvent.VK_LEFT:
                ufo.move(-0.25f, 0, 0);
                break;
            case KeyEvent.VK_RIGHT:
                ufo.move(0.25f, 0, 0);
                break;
           
           /* -> ideal cenary like bellow -> more than 1 key pressed
           int forward, moveY, rX, rY, rZ;
            if (KeyEvent.VK_SHIFT) // abduz a vaca
                cow.uprise(ufo);
            if (KeyEvent.VK_SPACE)
                forward = 1;
            if (KeyEvent.VK_W)
                moveY = 1;
            if (KeyEvent.VK_S)
                moveY = -1;
            if (KeyEvent.VK_A)
                rY = 1;
            if (KeyEvent.VK_D)
                rY = -1;
            if (KeyEvent.VK_Q)
                rZ = 1;
            if (KeyEvent.VK_E)
                rZ = -1;
            if (KeyEvent.VK_2)
                rX = 1;
            if (KeyEvent.VK_X)
                rX = -1;

            ufo.rotate(rX, rY, rZ);
            ufo.move(forward, moveY);
            */
        }
    }
    
    @Override
    public void dispose(GLAutoDrawable glad) {
        ufo.getModel().dispose();
        for(int i = 0; i<cows.size(); ++i){
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