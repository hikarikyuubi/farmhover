/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cg.farmhover;
import cg.farmhover.gl.core.ConeLight;
import cg.farmhover.gl.core.Light;
import cg.farmhover.gl.core.SimpleLight;
import cg.farmhover.gl.jWaveFront.JWavefrontObject;
import cg.farmhover.gl.util.Matrix4;
import cg.farmhover.gl.util.Shader;
import cg.farmhover.gl.util.ShaderFactory;
import cg.farmhover.gl.util.ShaderFactory.ShaderType;
import cg.farmhover.models.*;
import cg.farmhover.models.Terrain.Terrain;
import cg.farmhover.objects.*;

import cg.farmhover.objects.Camera;
import cg.farmhover.objects.Cow;
import cg.farmhover.objects.SceneObject.ObjectType;
import cg.farmhover.objects.Ufo;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
    //private final ConeLight coneLight;
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
    private final LightCone cone;
    private JWavefrontObject quad;
    private SceneObject farmhouse, barn,shelter;
    private SceneObject scare;
    private SceneObject tractor,harvester;
    private SceneObject corn;
    private SceneObject wind;
    private SceneObject tree;
    private ParticleSystem psys;
    private int[] terrainShaderHandles;
    public static Terrain terrain;
    public static ArrayList<SceneObject> objects;
    public static float ORIGIN = 500f;
    Model barn_model, farmhouse_model, shelter_model, scare_model, fence_model, corn_model,
            harvester_model, tractor_model, windmill_model, tree_model, ufo_model, cow_model;
    
    //=============================
    
    
    public TestScene() {
        //=============================
       barn_model = new Model(new File(".\\models\\barntest.obj"));
       farmhouse_model = new Model(new File(".\\models\\house01.obj"));
       shelter_model = new Model(new File(".\\models\\shelter.obj"));
       scare_model = new Model(new File(".\\models\\scarecrow.obj"));
       fence_model = new Model(new File(".\\models\\cerca2.obj"));
       //encontrar modelo de plantacao de milho
       corn_model = new  Model(new File(".\\models\\lowpolytree.obj"));
       harvester_model = new Model(new File(".\\models\\Harvester.obj"));
       tractor_model = new Model(new File(".\\models\\tractor.obj"));
       windmill_model = new Model(new File(".\\models\\windtest.obj"));
       tree_model = new Model(new File(".\\models\\tree2.obj"));
       ufo_model = new Model(new File(".\\models\\ufotest2.obj"));
       cow_model = new Model(new File(".\\models\\cow.obj"));
       // cow sem textura:  new Model(new File(".\\models\\newCow.obj"));
        //=============================
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
        cone = new LightCone();
        cone.setConelight(new ConeLight());

        // OBS.: 
        //     - Objetos que sofrem colisão = SceneObject 
        //     - Demais = JWavefrontObject
        // Atenção: Todos os objetos, exceto o UFO e as vacas, devem ser adicionados a "objects"
        // ------------- Objetos sem colisão -------------
        quad = new JWavefrontObject(new File(".\\models\\Poof.obj")); // vai continuar como JWavefront pois não tem colisão
        // ------------- Objetos com colisão -------------
       
        for (int i =-3; i< 4;i++){
            float spaceX = 160f;
            float spaceZ = 200f;
            for(int width =-2;width<3;width++){
                //farm
                farmhouse = new SceneObject(ObjectType.FARMHOUSE, ORIGIN + 15f + spaceX*i,
                        terrain.getHeightofTerrain(ORIGIN+15f, ORIGIN+10f)+5f,
                        ORIGIN + 10f + spaceZ*width, 5f, 5f, 5f);
                farmhouse.rx = -90;
                farmhouse.ry = 90;
               //shelter 
                shelter = new SceneObject(ObjectType.SHELTER, ORIGIN + 0f + spaceX*i,
                        terrain.getHeightofTerrain(ORIGIN, ORIGIN+30f),
                        ORIGIN +30f+spaceZ*width, 5f, 5f, 5f);
    //            shelter.setY(5f);
    //            System.out.println("shelter y:"+shelter.getY());
                barn = new SceneObject(ObjectType.BARN, ORIGIN + 0f + spaceX*i,
                        terrain.getHeightofTerrain(ORIGIN, ORIGIN-20f),
                        ORIGIN - 20f+ spaceZ *width, 5f, 5f, 5f);
                barn.ry = 90;
                harvester = new SceneObject(ObjectType.HARVESTER, ORIGIN - 8f + spaceX*i,
                        terrain.getHeightofTerrain(ORIGIN-8f,ORIGIN+32f),
                        ORIGIN + 32f+spaceZ*width, 3f, 3f, 3f);
                tractor = new SceneObject(ObjectType.TRACTOR, ORIGIN + -8f + spaceX*i,
                        terrain.getHeightofTerrain(ORIGIN-8f,ORIGIN + 26),
                        ORIGIN + 26f+spaceZ*width, 2.5f, 2.5f, 2.5f);
                tractor.ry = 180;
                tractor.rx = -90;
                scare = new SceneObject(ObjectType.SCARE, ORIGIN -20f + spaceX*i,
                        terrain.getHeightofTerrain(ORIGIN-20f, ORIGIN+47f),
                        ORIGIN + 47f+spaceZ*width, 2f, 2f, 2f);
                scare.ry = -90;
                wind = new SceneObject(ObjectType.WINDMILL, ORIGIN + 0f + spaceX*i,
                        terrain.getHeightofTerrain(ORIGIN,ORIGIN+10f),
                        ORIGIN + 10f+spaceZ*width, 5f, 5f, 5f);


                for(int k =0; k<4;k++){

                    for(int j=0;j<4; j++){
                        corn = new SceneObject(ObjectType.CORN, ORIGIN-16+ 4*k + spaceX*i,
                                terrain.getHeightofTerrain(ORIGIN-16+4*k, ORIGIN +42f + 4*j),
                                ORIGIN + 42f+4*j+spaceZ*width, 2f, 2f, 2f);
                        objects.add(corn);
                    }

                }

                objects.add(barn);
                objects.add(farmhouse);
                objects.add(shelter);
                objects.add(harvester);
                objects.add(tractor);
                objects.add(wind);
                objects.add(scare);
            }
        }
        
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

        //gl.glEnable(GL.GL_LINE_SMOOTH);
        //gl.glEnable(GL.GL_BLEND);
        //gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

        //inicializa os shaders
        shader.init(gl);
        skyboxShader.init(gl);
        terrainShader.init(gl);
        particleShader.init(gl);

        // inicializa as matrizes
        modelMatrix.init(gl);
        projectionMatrix.init(gl);
        viewMatrix.init(gl);
        cone.getCone().init(gl,terrainShader);
        
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
        cone.getConelight().init(gl);
        cone.getConelight().bind(terrainShader);

        simpleLight.setPosition(new float[]{1000, 800,0});
        simpleLight.setAmbientColor(new float[]{1.0f, 1.0f, 1.0f});

        cone.getConelight().setPosition(new float[]{500, 10,500});
        cone.getConelight().setAmbientColor(new float[]{0.0f, 1.0f, 0.0f});
        cone.getConelight().setAttenuation(0.01f);
        cone.getConelight().setConeAngle(15.0f);
        cone.getConelight().setConeDirection(new float[]{0, -1,0});



        try {
            barn_model.init(gl, shader);
            farmhouse_model.init(gl, shader);
            shelter_model.init(gl, shader);
            scare_model.init(gl, shader);
            fence_model.init(gl, shader);
            corn_model.init(gl, shader);
            harvester_model.init(gl, shader);
            tractor_model.init(gl, shader);
            windmill_model.init(gl, shader);
            tree_model.init(gl, shader);
            ufo_model.init(gl, shader);
            cow_model.init(gl, shader);
            
            barn_model.unitize(ObjectType.BARN);
            farmhouse_model.unitize(ObjectType.FARMHOUSE);
            shelter_model.unitize(ObjectType.SHELTER);
            scare_model.unitize(ObjectType.SCARE);
            fence_model.unitize(ObjectType.FENCE);
            corn_model.unitize(ObjectType.CORN);
            harvester_model.unitize(ObjectType.HARVESTER);
            tractor_model.unitize(ObjectType.TRACTOR);
            windmill_model.unitize(ObjectType.WINDMILL);
            tree_model.unitize(ObjectType.TREE);
            ufo_model.unitize(ObjectType.UFO);
            cow_model.unitize(ObjectType.COW);
            
        } catch (IOException ex) {
            Logger.getLogger(TestScene.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Random rand = new Random();
        Cow cow;
        for(int i = 0; i<2000; ++i){
            float x = 2*ORIGIN -rand.nextInt((int)(2*ORIGIN));
            float z = 2*ORIGIN -rand.nextInt((int)(2*ORIGIN));
            cow = new Cow(x,1,z);
            cows.add(cow);
            cow.setY(terrain.getHeightofTerrain(x,z) + cow.getHeight()/2);
            cow.ry = rand.nextInt(360);
        }
        
        
        for(SceneObject obj : objects){
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
        updater.movementApplier(keyBits, ufo, cam,cone);
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

        /*
        gl.glDisable(GL.GL_DEPTH_TEST);

        modelMatrix.loadIdentity();
        modelMatrix.scale(50,50,50);
        modelMatrix.translate(500,20,500);
        modelMatrix.bind(shaderHandles[0]);
        cube.bind();
        cube.draw();
        gl.glEnable(GL.GL_DEPTH_TEST);

        /* Elementos fixos do cenário */
        for(SceneObject obj : objects){
            if(ufo.findDist(obj)>200|| backCamera(ufo, cam, obj)){
                continue;
            }
            modelMatrix.loadIdentity();
            modelMatrix.translate(obj,obj.getX(), obj.getY(), obj.getZ());
            modelMatrix.scale(obj,obj.getScalex(), obj.getScaley(), obj.getScalez());
            modelMatrix.rotate(obj, obj.getRy(), 0, 1, 0);
            modelMatrix.rotate(obj, obj.getRx(), 1, 0, 0);
            modelMatrix.rotate(obj, obj.getRz(), 0, 0, 1);
            modelMatrix.bind(shaderHandles[0]);
            switch(obj.type){
                case BARN:{
                    barn_model.draw();
                    break;
                }
                case FARMHOUSE:{
                    farmhouse_model.draw();
                    break;
                }
                case SHELTER:{
                    shelter_model.draw();
                    break;
                }
                case SCARE:{
                    scare_model.draw();
                    break;
                }
                case FENCE:{
                    fence_model.draw();
                    break;
                }
                case CORN:{
                    corn_model.draw();
                    break;
                }
                case TRACTOR:{
                    tractor_model.draw();
                    break;
                }
                case HARVESTER:{
                    harvester_model.draw();
                    break;
                }
                case WINDMILL:{
                    windmill_model.draw();
                    break;
                }
                case TREE:{
                    tree_model.draw();
                    break;
                }
                
            }
        }
        /* Desenho das vacas */
       
        for (Cow cow : cows) {
            if(ufo.findDist(cow)>200 || backCamera(ufo, cam, cow)){
                continue;
            }
            cow.applyGravity(ufo);
            modelMatrix.loadIdentity();         
            modelMatrix.translate(cow, cow.getX(), cow.getY(), cow.getZ());
            modelMatrix.rotate(cow, cow.getRy(), 0, 1, 0);
            modelMatrix.bind(shaderHandles[0]);
            cow_model.draw();
        }

        /* Desenho do OVNI */
        modelMatrix.loadIdentity();
        ufo.undoTurningRotation();
        ufo.undoXRotation();
        if(ufo.getRx()==0 && ufo.getRz()==0 && !ufo.movingXZ){
            modelMatrix.translate(ufo, ufo.getX(),ufo.getY() + 0.5f*(float)Math.sin(Math.toRadians(floatingSpeed)),ufo.getZ());
            floatingSpeed += 2;
        } else {
            modelMatrix.translate(ufo, ufo.getX(),ufo.getY(),ufo.getZ());
            floatingSpeed = 0; // para a transição ficar suave
        }
        modelMatrix.rotate(ufo, -1*ufo.getRy(), 0, 1, 0);
        modelMatrix.rotate(ufo, ufo.getRx(), 1, 0, 0);
        modelMatrix.rotate(ufo, ufo.getRz(), 0, 0, 1);
        modelMatrix.scale(ufo.getScalex(), ufo.getScaley(), ufo.getScalez()); // !!! tá sem o primeiro argumento !!!
        modelMatrix.bind(shaderHandles[0]);
        ufo_model.draw();
        

        /* Terreno */
        terrainShader.bind();
        modelMatrix.loadIdentity();
        //modelMatrix.translate(-Terrain.SIZE /2,0,-Terrain.SIZE /2);
        projectionMatrix.bind(terrainShaderHandles[1]);
        viewMatrix.bind(terrainShaderHandles[2]);
        modelMatrix.bind(terrainShaderHandles[0]);
        simpleLight.draw();
        cone.getConelight().setPosition(new float[]{ufo.getX(),10 +
                terrain.getHeightofTerrain(ufo.getX(),ufo.getZ()),ufo.getZ()});
        cone.getConelight().draw();
        terrain.bind();
        terrain.draw();


        /* Desenho das Particulas */
    
        shader.bind();
        for (Particle p : particles) {
            modelMatrix.loadIdentity();
            modelMatrix.translate(p.getPosition()[0], p.getPosition()[1], p.getPosition()[2]);
            modelMatrix.rotate(-1*ufo.getRy(), 0, 1, 0);
            modelMatrix.rotate(ufo.getRx(), 1, 0, 0);
            modelMatrix.rotate(ufo.getRz(), 0, 0, 1);
            modelMatrix.scale(p.getScale()*0.5f, p.getScale()*0.5f, p.getScale()*0.5f);
            modelMatrix.bind(shaderHandles[0]);
            quad.draw();
        }
        shader.bind();
        /*
        modelMatrix.loadIdentity();
        modelMatrix.translate(ufo.getX(),ufo.getY()-5,ufo.getZ());
        modelMatrix.scale(12,50,12);
        modelMatrix.bind(shaderHandles[0]);
        //holo.bind();
        //holo.draw();*/
        
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
    
    public boolean backCamera(Ufo ufo, Camera cam, SceneObject obj) {
        return ((ufo.getZ() > cam.getZ() && cam.getZ() > obj.getZ())
                || (ufo.getZ() < cam.getZ() && cam.getZ() < obj.getZ()))
                && ((ufo.getX() < cam.getX() && cam.getX() < obj.getX())
                || (ufo.getX() > cam.getX() && cam.getX() > obj.getX()));
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
                Updater.playLoop = false;
            }
            cone.setDrawCone(false);
        } else if(keyCode == KeyEvent.VK_D ||
                keyCode == KeyEvent.VK_RIGHT ||
                keyCode == KeyEvent.VK_A ||
                keyCode == KeyEvent.VK_LEFT){
            ufo.turning = false;
        } else if(keyCode == KeyEvent.VK_W ||
                keyCode == KeyEvent.VK_S ){
            ufo.movingY = false;
        } else if(keyCode == KeyEvent.VK_UP ||
                keyCode == KeyEvent.VK_DOWN){
            ufo.movingXZ = false;
        }
        
        keyBits.clear(keyCode);       
    }
    
    @Override
    public void dispose(GLAutoDrawable glad) {
        barn_model.dispose();
        farmhouse_model.dispose();
        shelter_model.dispose();
        scare_model.dispose();
        fence_model.dispose();
        corn_model.dispose();
        harvester_model.dispose();
        tractor_model.dispose();
        windmill_model.dispose();
        tree_model.dispose();
        ufo_model.dispose();
        cow_model.dispose();
    }

    @Override
    public void reshape(GLAutoDrawable glad, int x, int y, int width, int height) {
        GL3 gl = glad.getGL().getGL3();
        gl.glViewport(0, 0, width, height); // update the viewport
        aspectRatio = (float)width/(float)(height);//uptdate aspectratio para usar na matriz de projecao
    }
}