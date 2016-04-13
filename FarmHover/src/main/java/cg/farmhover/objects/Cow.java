package cg.farmhover.objects;

import cg.farmhover.gl.jWaveFront.JWavefrontObject;
import cg.farmhover.gl.util.Shader;

import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;
import java.io.File;
import java.io.IOException;

public class Cow {

    private JWavefrontObject model;
    private float x,y,z;
    public Cow(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        model = new JWavefrontObject(new File(".\\models\\newCow.obj"));
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
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
        if(y<ufo.getZ()){
            y+=0.01f;
        }
    }
}