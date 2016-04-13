package cg.farmhover.objects;

import cg.farmhover.gl.jWaveFront.JWavefrontObject;
import cg.farmhover.gl.util.Shader;

import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;
import java.io.File;
import java.io.IOException;

public class Ufo {

    private JWavefrontObject model;
    private float x, y, z; // posição
    private float rx, ry, rz; // rotação
    private float moveVel, flipDeg; // bases
    
    public Ufo() {
        rx = ry = rz = x = y = z = 0f;
        moveVel = 0.25f;
        flipDeg = 5f;
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
    
    private float roundDec(double number, int decimal) {
        int roundPoint = 1;
        for (int i = 0; i < decimal; i++) {
            roundPoint *= 10;
        }
        return ((float) Math.round(number * roundPoint))/((float)roundPoint);
    }

    public void move(int forward, int moveY) {
        this.y += (moveY * this.moveVel);
        if (forward == 1) {
            this.x += (roundDec(Math.sin(Math.toRadians(ry)), 3) * this.moveVel);
            this.z += -1*(roundDec(Math.cos(Math.toRadians(ry)), 3) * this.moveVel);
        }
    }
    
    public void move(int x, int y, int z) {
        this.x += x * this.moveVel;
        this.y += y * this.moveVel;
        this.z += z * this.moveVel;
    }

    public void rotate(int x, int y, int z) {
        this.rx = (this.rx + x * this.flipDeg) % 360;
        this.ry = (this.ry + y * this.flipDeg) % 360;
        this.rz = (this.rz + z * this.flipDeg) % 360;
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
    
    public float getLookat(char axis) {
        if (axis == 'x') return -1*roundDec(Math.sin(Math.toRadians(ry)), 3);
        return roundDec(Math.cos(Math.toRadians(ry)), 3);
    }
}
