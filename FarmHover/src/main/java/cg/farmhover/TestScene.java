/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cg.farmhover;

import cg.farmhover.gl.core.Light;
import cg.farmhover.gl.core.SimpleLight;
import cg.farmhover.gl.jWaveFront.JWavefrontObject;
import cg.farmhover.gl.util.Matrix4;
import cg.farmhover.gl.util.Shader;
import cg.farmhover.gl.util.ShaderFactory;
import cg.farmhover.gl.util.ShaderFactory.ShaderType;
import cg.farmhover.models.Skybox;
import cg.farmhover.models.Terrain.Terrain;
import cg.farmhover.objects.*;

import cg.farmhover.objects.Camera;
import cg.farmhover.objects.Cow;
import cg.farmhover.objects.SceneObjectFactory.ObjectType;
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

public class TestScene extends KeyAdapter implements GLEventListener { 
    
    private final Shader shader; // Gerenciador dos shaders
    private final Matrix4 modelMatrix;
    private final Matrix4 projectionMatrix;
    private final Matrix4 viewMatrix;
    private final Light light;
    private final SimpleLight simpleLight;
    private float delta, aspectRatio;
    private Ufo ufo;
    private Camera cam;
    public static ArrayList<Cow> cows;
    public static Cow risingCow, cowaux;
    private JWavefrontObject shadow;
    private float floatingSpeed;
    private BitSet keyBits;
    private Updater updater;
    private final Skybox skybox;
    private final Shader skyboxShader;
    private final Shader terrainShader;
    private final Shader particleShader;
    private int[] shaderHandles;
    private int[] skyboxShaderHandles;
    private int[] particleShaderHandles;
    private ArrayList<Particle> particles;
    //private SimpleModel quad;
    private JWavefrontObject quad;
    private SceneObject farmhouse, barn,shelter;
    private SceneObject fence;
    private SceneObject tractor,harvester;
    private SceneObject corn;
    private SceneObject wind;
    private SceneObject tree;
    private ParticleSystem psys;
    private int[] terrainShaderHandles;
    public static Terrain terrain;
    public static ArrayList<SceneObject> objects;
    public static float ORIGIN = 500f;
    public TestScene() {
        SceneObjectFactory.init();
        cows = new ArrayList();
        objects = new ArrayList();
        keyBits = new BitSet(256);
        updater = new Updater();
        shader = ShaderFactory.getInstance(ShaderType.COMPLETE_SHADER);

        modelMatrix = new Matrix4();
        projectionMatrix = new Matrix4();
        viewMatrix = new Matrix4();
        light = new Light();
        simpleLight = new SimpleLight();
        terrain = new Terrain("heightmap",".png");
        //quad = new Cube();
        // OBS.: 
        //     - Objetos que sofrem colisão = SceneObject 
        //     - Demais = JWavefrontObject
        // Atenção: Todos os objetos, exceto o UFO e as vacas, devem ser adicionados a "objects"
        // ------------- Objetos sem colisão -------------
        quad = new JWavefrontObject(new File(".\\models\\Poof.obj")); // vai continuar como JWavefront pois não tem colisão
        // ------------- Objetos com colisão -------------
       
            //farm
            
            farmhouse = SceneObjectFactory.getInstance(ObjectType.FARMHOUSE, ORIGIN + 15f, ORIGIN +10f);
           //metade do shelter esta enterrado no relevo
            shelter = SceneObjectFactory.getInstance(ObjectType.SHELTER, ORIGIN + 0f, ORIGIN +30f);
            shelter.setY(5f);
            System.out.println("shelter y:"+shelter.getY());
            barn = SceneObjectFactory.getInstance(ObjectType.BARN, ORIGIN + 0f, ORIGIN - 20f);
            barn.ry = 90;
            fence = SceneObjectFactory.getInstance(ObjectType.FENCE, ORIGIN + -5f, ORIGIN + 37.5f);
            harvester = SceneObjectFactory.getInstance(ObjectType.HARVESTER, ORIGIN - 8f, ORIGIN + 32f);
            tractor = SceneObjectFactory.getInstance(ObjectType.TRACTOR, ORIGIN + -8f, ORIGIN + 26f);
            tractor.ry = 180;
            wind = SceneObjectFactory.getInstance(ObjectType.WINDMILL, ORIGIN + 0f, ORIGIN + 10f);
            tree = SceneObjectFactory.getInstance(ObjectType.TREE, ORIGIN + 10f, ORIGIN + 10f);
            for(int k =0; k<4;k++){

                for(int j=0;j<4; j++){
                    corn = SceneObjectFactory.getInstance(ObjectType.CORN, ORIGIN-16+ 4*k, ORIGIN + 42f+4*j);
                    objects.add(corn);
                }

            }

            objects.add(barn);
            objects.add(farmhouse);
            objects.add(shelter);
            objects.add(harvester);
            objects.add(tractor);
            objects.add(wind);
            //objects.add(tree);
            
        

        ufo = new Ufo();
        cam = new Camera(ufo);
        shadow = new JWavefrontObject(new File(".\\models\\shadow.obj"));
        delta = 5f;
        
        skybox = new Skybox();
        skyboxShader = ShaderFactory.getInstance(ShaderType.SKYBOX_SHADER);
        terrainShader = ShaderFactory.getInstance(ShaderType.TERRAIN_SHADER);
        particles = new ArrayList<>();
        psys = new ParticleSystem(5,0.1f,0.01f, 40);
        particleShader = ShaderFactory.getInstance(ShaderType.PARTICLE_SHADER);
        floatingSpeed = 0f;
        
        shaderHandles = new int[3];
        skyboxShaderHandles = new int[3];
        particleShaderHandles = new int[3];
        terrainShaderHandles = new int[3];
        //aspectRatio = 1.0f;
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
        skyboxShader.init(gl);
        terrainShader.init(gl);
        particleShader.init(gl);

        // inicializa as matrizes
        modelMatrix.init(gl);
        projectionMatrix.init(gl);
        viewMatrix.init(gl);
        //quad.init(gl, shader);
        
        // pega os indices das matrizes no Complete Shader
        shaderHandles[0] = shader.getUniformLocation("u_modelMatrix");
        shaderHandles[1] = shader.getUniformLocation("u_projectionMatrix");
        shaderHandles[2] = shader.getUniformLocation("u_viewMatrix");
        // pega os indices das matrizes no Skybox Shader
        skyboxShaderHandles[0] = skyboxShader.getUniformLocation("u_modelMatrix");
        skyboxShaderHandles[1] = skyboxShader.getUniformLocation("u_projectionMatrix");
        skyboxShaderHandles[2] = skyboxShader.getUniformLocation("u_viewMatrix");
        // pega os indices das matrizes no Terrain Shader
        terrainShaderHandles[0] = terrainShader.getUniformLocation("u_modelMatrix");
        terrainShaderHandles[1] = terrainShader.getUniformLocation("u_projectionMatrix");
        terrainShaderHandles[2] = terrainShader.getUniformLocation("u_viewMatrix");
        // pega os indices das matrizes no Particle Shader
        particleShaderHandles[0] = particleShader.getUniformLocation("u_modelMatrix");
        particleShaderHandles[1] = particleShader.getUniformLocation("u_projectionMatrix");
        particleShaderHandles[2] = particleShader.getUniformLocation("u_viewMatrix");

        //init the light
        light.init(gl,shader);
        light.setPosition(new float[]{1000, 800, 0, 1.0f});
        light.setAmbientColor(new float[]{0.1f, 0.1f, 0.1f, 1.0f});
        light.setDiffuseColor(new float[]{0.7f, 0.7f, 0.7f, 1.0f});
        light.setSpecularColor(new float[]{0.7f, 0.7f, 0.7f, 1.0f});

        simpleLight.init(gl);
        simpleLight.bind(terrainShader);

        simpleLight.setPosition(new float[]{1000, 800,0});
        simpleLight.setAmbientColor(new float[]{1.0f, 1.0f, 1.0f});


        ufo.init(gl, shader);

        Random rand = new Random();
        Cow cow;
        for(int i = 0; i<10; ++i){
            float x = rand.nextInt(50)+ Terrain.SIZE /2;
            float z = -rand.nextInt(50)+ Terrain.SIZE /2;
            cow = new Cow(x,1,z);
            cows.add(cow);
            cow.init(gl, shader);
            cow.setY(terrain.getHeightofTerrain(x,z) + cow.getHeight()/2);
            //tudo começa em (500,500) translado de até -50 ou até +50
            cow.ry = rand.nextInt(360);
            //System.out.println("cow " + cow.getY());

        }
        /*
        for(Cow cow2 : cows){
            cow2.init(gl, shader);
        }*/

        for(SceneObject obj : objects){
            obj.init(gl, shader);
            obj.setY(terrain.getHeightofTerrain(obj.getX(),obj.getZ()) + obj.getHeight()/2+2);
        }
        
        try {
            //init the model
            quad.init(gl, shader);
            quad.unitize();
        } catch (IOException ex) {
            Logger.getLogger(TestScene.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        }
        
        // carrega a textura do skybox
        skybox.init(gl, skyboxShader);
        try {
            skybox.loadTexture(skybox.getTextureFiles(),".jpg");
        } catch (IOException ex) {
        Logger.getLogger(TestScene.class.getName()).log(Level.SEVERE, null, ex);
        }


        terrain.init(gl, terrainShader);
        String[] textures = {"blendMap","mud","grassFlowers","mud","nightGrass"};
        try {
            terrain.loadTexture(textures);
        } catch (IOException ex) {
        Logger.getLogger(TestScene.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    @Override // Chamado pelo animator
    public void display(GLAutoDrawable glad) {
        GL3 gl = glad.getGL().getGL3(); // Contexto de desenho
        
        shader.bind();
        light.bind();
        // A cada atualização, limpa de acordo com a cor do buffer
        gl.glClear(GL3.GL_COLOR_BUFFER_BIT | GL3.GL_DEPTH_BUFFER_BIT);
        updater.movementApplier(keyBits, ufo, cam);
        particles = updater.particleUpdater(keyBits, psys, particles, ufo);
        
        /* Projeção e View */
        // Carrega a matriz de projeção perspectiva
        projectionMatrix.loadIdentity();
        projectionMatrix.perspective(80.0f, aspectRatio, 0.1f, 2000.0f);
        projectionMatrix.bind(shaderHandles[1]);

        // Carrega a camera para acompanhar o OVNI

        viewMatrix.loadIdentity();
        viewMatrix.translate(0, -5, -7);
        viewMatrix.rotate(40,1,0,0);
        viewMatrix.lookAt(
                cam.getX(), cam.getY(), cam.getZ(),
                ufo.getX(), ufo.getY()+1, ufo.getZ(),
                cam.getLookUpX(), cam.getLookUpY(), cam.getLookUpZ());
        viewMatrix.bind(shaderHandles[2]);

        /* Elementos fixos do cenário */
        for(SceneObject obj : objects){
            obj.resetInverseModelMatrix();
            modelMatrix.loadIdentity();
            modelMatrix.translate(obj,obj.getX(), obj.getY(), obj.getZ());
            modelMatrix.scale(obj,obj.getScalex(), obj.getScaley(), obj.getScalez());
            modelMatrix.rotate(obj, obj.getRy(), 0, 1, 0);
            modelMatrix.bind(shaderHandles[0]);
            obj.getModel().draw();
        }
       /* Desenho da fazenda */    
        
        
        // sombra
//        modelMatrix.loadIdentity();
//        modelMatrix.translate(ufo.getX(),2,ufo.getZ());
//        modelMatrix.scale(3f,3f,3f);
//        modelMatrix.bind();
//        shadow.draw();

        /* Desenho das vacas */
       
        for (Cow cow : cows) {
            cow.resetInverseModelMatrix();
            cow.applyGravity(ufo);
            modelMatrix.loadIdentity();         
            modelMatrix.translate(cow, cow.getX(), cow.getY(), cow.getZ());
            modelMatrix.rotate(cow, cow.getRy(), 0, 1, 0);
            modelMatrix.bind(shaderHandles[0]);
            cow.getModel().draw();
        }

        /* Desenho do OVNI */
        ufo.resetInverseModelMatrix();
        modelMatrix.loadIdentity();
        //modelMatrix.translate(0,0.5f*(float)Math.sin(Math.toRadians(floatingSpeed)),0);
        modelMatrix.translate(ufo, ufo.getX(),ufo.getY(),ufo.getZ());
        modelMatrix.rotate(ufo, -1*ufo.getRy(), 0, 1, 0);
        modelMatrix.rotate(ufo, ufo.getRx(), 1, 0, 0);
        modelMatrix.rotate(ufo, ufo.getRz(), 0, 0, 1);
        modelMatrix.scale(ufo.getScalex(), ufo.getScaley(), ufo.getScalez()); // !!! tá sem o primeiro argumento !!!
        modelMatrix.bind(shaderHandles[0]);
        ufo.getModel().draw();
        floatingSpeed += 2;

        terrainShader.bind();
        modelMatrix.loadIdentity();
        //modelMatrix.translate(-Terrain.SIZE /2,0,-Terrain.SIZE /2);
        projectionMatrix.bind(terrainShaderHandles[1]);
        viewMatrix.bind(terrainShaderHandles[2]);
        modelMatrix.bind(terrainShaderHandles[0]);
        simpleLight.draw();
        terrain.bind();
        terrain.draw();

        
        /* Desenho das Particulas */
        //particleShader.bind();
       // particleShader.bind();
        //gl.glBlendFunc(gl.GL_SRC_ALPHA, gl.GL_ONE_MINUS_SRC_ALPHA);

        //gl.glEnable(gl.GL_BLEND);
        //gl.glEnableVertexAttribArray(0);
        //gl.glBindVertexArray(quad.getVaoID());
        //gl.glDepthMask(false);
       //projectionMatrix.bind(particleShaderHandles[1]);
        //viewMatrix.bind(particleShaderHandles[2]);
        shader.bind();
        for (Particle p : particles) {
            modelMatrix.loadIdentity();
            modelMatrix.translate(p.getPosition()[0], p.getPosition()[1], p.getPosition()[2]);
            modelMatrix.rotate(-1*ufo.getRy(), 0, 1, 0);
            modelMatrix.rotate(ufo.getRx(), 1, 0, 0);
            modelMatrix.rotate(ufo.getRz(), 0, 0, 1);
            modelMatrix.scale(p.getScale()*0.5f, p.getScale()*0.5f, p.getScale()*0.5f);
//modelMatrix.updateModelViewMatrix(p.getPosition(), p.getRotation(), p.getScale(), viewMatrix.getMatrix()); ;
            modelMatrix.bind(shaderHandles[0]);
        //quad.bind();
            quad.draw();
        }
        //gl.glDepthMask(true);
        //gl.glDisable(gl.GL_BLEND);
        //gl.glDisableVertexAttribArray(0);
        //gl.glBindVertexArray(0);
        /* Desenho do Skybox */
        skyboxShader.bind();
        gl.glDepthFunc(GL.GL_LEQUAL);
        gl.glDepthMask(false);
        projectionMatrix.bind(skyboxShaderHandles[1]);
        viewMatrix.bind(skyboxShaderHandles[2]);
        modelMatrix.loadIdentity();
        modelMatrix.translate(ufo.getX(),ufo.getY(),ufo.getZ());
        modelMatrix.bind(skyboxShaderHandles[0]);
        // skybox cube
        skybox.bind();
        skybox.draw();
        gl.glDepthMask(true);
        gl.glDepthFunc(GL.GL_LESS);

        gl.glFlush();
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
        for(Cow cow : cows){
            cow.getModel().dispose();
        }
        for(SceneObject obj: objects){
            obj.getModel().dispose();
        }
    }

    @Override
    public void reshape(GLAutoDrawable glad, int x, int y, int width, int height) {
        GL3 gl = glad.getGL().getGL3();
        gl.glViewport(0, 0, width, height); // update the viewport
        aspectRatio = (float)width/(float)(height);//uptdate aspectratio para usar na matriz de projecao
    }
}