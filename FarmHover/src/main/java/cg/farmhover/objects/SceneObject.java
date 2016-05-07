/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cg.farmhover.objects;

import cg.farmhover.Model;
import cg.farmhover.gl.jWaveFront.JWavefrontObject;
import cg.farmhover.gl.util.Shader;
import java.io.IOException;
import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;

/**
 *
 * @author Barbara
 */
public class SceneObject {
    Model model;
    float x,y,z;
    public float rx, ry, rz; // rotação ------------ depois mudar pra getters e setters
    private float width, height, depth;
    private float scalex, scaley, scalez;

    public void init(GLAutoDrawable glad, Shader shader) {
        // Get pipeline
        GL3 gl = glad.getGL().getGL3();

        try {
            model.init(gl, shader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        model.unitize(this);
        
        System.err.println("======================> w:" + width 
                           + " h:" + height + " d:"+depth);
    }
    
    public void move(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
    }
        
    public float getScalex() {
        return scalex;
    }

    public void setScalex(float scalex) {
        //this.width*=scalex;
        this.scalex = scalex;
    }

    public float getScaley() {
        return scaley;
    }

    public void setScaley(float scaley) {
        //this.height*=scaley;
        this.scaley = scaley;
    }

    public float getScalez() {
        return scalez;
    }

    public void setScalez(float scalez) {
        //this.depth*=scalex;
        this.scalez = scalez;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getDepth() {
        return depth;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setDepth(float depth) {
        this.depth = depth;
    }
    
    public Model getModel() {
        return model;
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


