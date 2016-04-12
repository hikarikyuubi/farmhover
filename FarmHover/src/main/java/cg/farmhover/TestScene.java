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
    private float delta, dx, dz;
    private Ufo ufo;
    private Cow cow;
    
    public TestScene() {
        shader = ShaderFactory.getInstance(ShaderType.COMPLETE_SHADER);
        modelMatrix = new Matrix4();
        projectionMatrix = new Matrix4();
        viewMatrix = new Matrix4();
        light = new Light();
        cow = new Cow(1,0,1);
        ufo = new Ufo();
        farm = new JWavefrontObject(new File(".\\models\\plain.obj"));
        delta = 5f;
        dx = dz = 0f;
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
        light.setPosition(new float[]{10, 10, 50, 1.0f});
        light.setAmbientColor(new float[]{0.1f, 0.1f, 0.1f, 1.0f});
        light.setDiffuseColor(new float[]{0.75f, 0.75f, 0.75f, 1.0f});
        light.setSpecularColor(new float[]{0.7f, 0.7f, 0.7f, 1.0f});
        light.init(gl, shader);


        ufo.init(glad,shader);
        cow.init(glad, shader);

        try {
            //init the model
            farm.init(gl, shader);
            farm.unitize();
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
        projectionMatrix.perspective(45.0f, 1.0f, 0.1f, 100.0f);
        projectionMatrix.bind();

        // Carrega a camera para acompanhar o OVNI
        viewMatrix.loadIdentity();
        viewMatrix.lookAt(
                dx, 1,dz + 10,
                dx, 0, dz,
                0, 1, 0);
        viewMatrix.bind();

        /* Desenho da fazenda */
        modelMatrix.loadIdentity();
        modelMatrix.translate(0, 0, 0);
        modelMatrix.scale(1000, 1000,1000);
       // modelMatrix.rotate(45, 0, 1, 0);
        modelMatrix.bind();
 
        farm.draw();

        /* Desenho das vacas */

        modelMatrix.loadIdentity();
        modelMatrix.translate(cow.getX(),cow.getY(),cow.getZ());
        modelMatrix.scale(1.5f,1.5f,1.5f);
        modelMatrix.bind();
        cow.getModel().draw();
//        modelMatrix.loadIdentity();
//        modelMatrix.translate(8,0,2);
//        modelMatrix.scale(0.8f,0.8f,0.8f);
//        modelMatrix.rotate(-30, 0, 1, 0);
//        modelMatrix.bind();
//        cow.draw();
//
        /* Desenho do OVNI */

        modelMatrix.loadIdentity();
        modelMatrix.translate(dx,2,dz);
        modelMatrix.scale(1.5f,1.5f,1.5f);
        modelMatrix.rotate(dz*5, 1, 0, 0);
        modelMatrix.rotate(dx*5, 0, 0, 1);
        modelMatrix.bind();
        ufo.getModel().draw();
    }
    
    @Override
    public void keyPressed(KeyEvent e) {

        switch (e.getKeyCode()) {
            case KeyEvent.VK_SPACE: // abduz a vaca
                cow.uprise(ufo);
                break;
            case KeyEvent.VK_UP://gira sobre o eixo-x
                dz -= 1;
                ufo.move(0,0,-1);
                break;
            case KeyEvent.VK_DOWN://gira sobre o eixo-x
                dz += 1;
                ufo.move(0,0,1);
                break;
            case KeyEvent.VK_LEFT://gira sobre o eixo-y
                dx -= 1;
                ufo.move(-1,0,0);
                break;
            case KeyEvent.VK_RIGHT://gira sobre o eixo-y
                dx += 1;
                ufo.move(1,0,0);
                break;
        }
    }
    
    @Override
    public void dispose(GLAutoDrawable glad) {
        
    }


    @Override
    public void reshape(GLAutoDrawable glad, int i, int i1, int i2, int i3) {}
}