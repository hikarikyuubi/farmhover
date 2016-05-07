/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cg.farmhover.objects;

import cg.farmhover.Util;

/**
 *
 * @author Hikari Kyuubi
 */
public class Camera {
    private float x, y, z;
    private float lookUpX, lookUpY, lookUpZ;
    private final float fixedDist, fixedDistY;
    
    public Camera (Ufo ufo) {
        this.fixedDistY = 10f;
        this.fixedDist = 8f;
        this.x = ufo.getX();
        this.y = ufo.getY() + fixedDistY;
        this.z = ufo.getZ() + fixedDist;
        
        this.lookUpX = this.lookUpZ = 0;
        this.lookUpY = 1;
    }
    
    public void updatePosition(Ufo ufo) {
        /*this.y = ufo.getY() + fixedDistY;
        this.x = ufo.getX() + fixedDist * 
                (-1 * Util.roundDec(Math.sin(Math.toRadians(ufo.getRy())), 3));
        this.z = ufo.getZ() + fixedDist * 
                Util.roundDec(Math.cos(Math.toRadians(ufo.getRy())), 3);
        */
    /*  
    // ufo.getX() + 10*ufo.getLookat('x')
    public float getLookFromX(float d) {
        //return this.getX() - d * frontXW(1) * roundDec(Math.sin(Math.toRadians(ry)), 3);
        return this.getX() - d * Util.roundDec(Math.sin(Math.toRadians(ry)), 3);
    }

    // ufo.getY() + 10*ufo.getLookat('y')
    public float getLookFromY(float d) {
        return this.getY() + d * (roundDec(Math.sin(Math.toRadians(90 - rx)), 3));
        //return this.getY() + d;
    }
    
    // ufo.getZ() + 10*ufo.getLookat('z')
    public float getLookFromZ(float d) {
        //return this.getZ() + d * frontXW(1) * roundDec(Math.cos(Math.toRadians(ry)), 3);
        return this.getZ() + d * roundDec(Math.cos(Math.toRadians(ry)), 3);
    }
    */
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getLookUpX() {
        return lookUpX;
    }

    public void setLookUpX(float lookUpX) {
        this.lookUpX = lookUpX;
    }

    public float getLookUpY() {
        return lookUpY;
    }

    public void setLookUpY(float lookUpY) {
        this.lookUpY = lookUpY;
    }

    public float getLookUpZ() {
        return lookUpZ;
    }

    public void setLookUpZ(float lookUpZ) {
        this.lookUpZ = lookUpZ;
    }
    
    
}
