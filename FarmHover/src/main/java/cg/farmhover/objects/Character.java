/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cg.farmhover.objects;

import cg.farmhover.gl.jWaveFront.JWavefrontObject;
import cg.farmhover.gl.util.Shader;
import java.io.IOException;
import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;

/**
 *
 * @author Barbara
 */
public class Character {
    JWavefrontObject model;
    float x,y,z;
    
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
   
    
    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getZ() {
        return this.z;
    }
}


