package cg.farmhover.objects;

import cg.farmhover.gl.jWaveFront.JWavefrontObject;
import cg.farmhover.gl.util.Shader;

import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;
import java.io.File;
import java.io.IOException;

public class Ufo {

    private JWavefrontObject model;
    private float x,y,z; // posição
    private float rx, ry, rz; // rotação
    private float moveVel, flipDeg; // bases
    
    public Ufo() {
        rx = ry = rz = x = y = z = 0f;
        moveVel = 0.25f;
        flipDeg = 3f;
        model = new JWavefrontObject(new File(".\\models\\UFO.obj"));
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

    public void move(int x, int y, int z) {
        this.x += x*moveVel;
        this.y += y*moveVel;
        this.z += z*moveVel;
    }

    public void rotate(int x, int y, int z) {
        this.rx += x*flipDeg;
        this.ry += y*flipDeg;
        this.rz += z*flipDeg;
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
    
    public float getRx() {
        return this.rx;
    }

    public float getRy() {
        return this.ry;
    }

    public float getRz() {
        return this.rz;
    }
}
