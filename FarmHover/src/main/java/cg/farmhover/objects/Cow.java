package cg.farmhover.objects;

import cg.farmhover.TestScene;
import cg.farmhover.gl.jWaveFront.JWavefrontObject;
import cg.farmhover.gl.util.Shader;

import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;
import java.io.File;
import java.io.IOException;

public class Cow {

    private JWavefrontObject model;
    private float x,y,z;
    public boolean rising;
    float fallHeight;
    
    public Cow(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        // descomentar a linha abaixo e comentar a seguinte para usar a vaca com textura
        //model = new JWavefrontObject(new File(".\\models\\cow.obj"));
        model = new JWavefrontObject(new File(".\\models\\newCow.obj"));
        rising = false;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getZ() {
        return this.z;
    }

    public void init(GLAutoDrawable glad, Shader shader) {
        // Get pipeline
        GL3 gl = glad.getGL().getGL3();

        try {
            model.init(gl, shader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        model.unitize();
    }

    public JWavefrontObject getModel() {
        return model;
    }

    public void move(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
    }
   
    public void uprise(Ufo ufo){
        rising = true;
        fallHeight = ufo.getY();
        if (y < ufo.getY()-2){
            y += 0.02f;
        } else {
            TestScene.cows.remove(this);
        }
    }
    
    public void applyGravity(){
        if(rising==false && y>1){
            y -= (fallHeight-y)*0.02f; // pra acelerar com o tempo 
        }
    }
}
