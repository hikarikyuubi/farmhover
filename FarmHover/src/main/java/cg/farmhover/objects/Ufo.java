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
        moveVel = 1f;
        flipDeg = 45f;
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
        this.y += moveY * moveVel;
        if (forward == 1) {
            this.x += (moveVel * roundDec(Math.cos(Math.toRadians(ry)), 3));
            this.z += (moveVel * roundDec(Math.sin(Math.toRadians(ry)), 3));
            
            System.err.println("Ry = " + ry + "dec, so z is = " 
                    + roundDec(Math.sin(Math.toRadians(ry)), 3) + " and new z = " + this.z + " while x is " + 
                   roundDec(Math.cos(Math.toRadians(ry)), 3) + " and new x = " + this.x);
        }
    }
    
    public void move(int x, int y, int z) {
        this.x += x * moveVel;
        this.y += y * moveVel;
        this.z += z * moveVel;
    }

    public void rotate(int x, int y, int z) {
        this.rx = (this.rx + x * flipDeg) % 360;
        this.ry = (this.ry + y * flipDeg) % 360;
        this.rz = (this.rz + z * flipDeg) % 360;
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
