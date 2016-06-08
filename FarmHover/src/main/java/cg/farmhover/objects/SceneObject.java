package cg.farmhover.objects;

import cg.farmhover.Main;
import static cg.farmhover.TestScene.risingCow;
import cg.farmhover.Updater;
import cg.farmhover.Util;
import cg.farmhover.gl.util.Matrix4;
import static cg.farmhover.objects.Cow.n_abducted_cows;
import java.awt.Color;
import static java.lang.Math.abs;
import static java.lang.Math.sqrt;
import java.util.EnumMap;

public class SceneObject {
    public enum ObjectType {
        BARN,
        FARMHOUSE,
        SHELTER,
        SCARE,
        FENCE,
        CORN,
        TRACTOR,
        HARVESTER,
        WINDMILL,
        TREE,
        UFO,
        COW     
  };

    public SceneObject(ObjectType type, float x, float y, float z,
            float scalex, float scaley, float scalez) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
        this.scalex = scalex;
        this.scaley = scaley;
        this.scalez = scalez;
        this.inverseModelMatrix = new Matrix4();
    }
    public static SceneObject collidedObject;
    public static EnumMap<ObjectType, Dimension> dimensions = new EnumMap<>(ObjectType.class);
    private Matrix4 inverseModelMatrix;

    public ObjectType type;
    float x,y,z;
    public float rx, ry, rz; // rotação ------------ depois mudar pra getters e setters (ou não)
    private float scalex, scaley, scalez;
    private int collisionCounter = 0;
    
    public void resetInverseModelMatrix(){
        inverseModelMatrix.loadIdentity();
        inverseModelMatrix.getStack().clear();
    }
    
    static float[] multiplyPos4Matrix(float x, float y, float z, float[] matrix){
        float a00 = matrix[0], a01 = matrix[1], a02 = matrix[2], a03 = matrix[3];
        float a10 = matrix[4], a11 = matrix[5], a12 = matrix[6], a13 = matrix[7];
        float a20 = matrix[8], a21 = matrix[9], a22 = matrix[10], a23 = matrix[11];
        float a30 = matrix[12], a31 = matrix[13], a32 = matrix[14], a33 = matrix[15];
        float[] newPos = new float[3];
        newPos[0] = a00*x + a01*y + a02*z + a03;
        newPos[1] = a10*x + a11*y + a12*z + a03;
        newPos[2] = a20*x + a21*y + a22*z + a03;
        return newPos;
    }
    
    public boolean isColliding(SceneObject other){
        float maxDist = (Math.max(Math.max(this.getWidth()*this.scalex, this.getDepth()*this.scalez), this.getHeight()*this.scaley) +
                    Math.max(Math.max(other.getDepth()*other.getScalez(), other.getHeight()*other.getScaley()), other.getWidth()*other.getScalex()))/2;
        
        if(findDist(other)>=maxDist){ // se a distância entre os centros for maior que a soma dos 'raios' dos objetos       
            collisionCounter++;
            if(collisionCounter>200000){
                collidedObject = null;
                collisionCounter = 0;
            }
            return false;
        } 
        
        if (other != collidedObject) {
            Cow.n_abducted_cows--;
            if (collidedObject != risingCow) Util.EXPLODE = true;
            else {
                Updater.playHit = true;
            }
        }
        collidedObject = other;
        
        return true;
    }
    
    
    public float findDist(SceneObject other){
        float xdif = abs(this.x - other.getX());
        float ydif = abs(this.y - other.getY());
        float zdif = abs(this.z - other.getZ());
        return (float) sqrt(xdif*xdif + ydif*ydif + zdif*zdif);
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
        //this.getWidth()*=scalex;
        this.scalex = scalex;
    }

    public float getScaley() {
        return scaley;
    }

    public void setScaley(float scaley) {
        //this.getHeight()*=scaley;
        this.scaley = scaley;
    }

    public float getScalez() {
        return scalez;
    }

    public void setScalez(float scalez) {
        //this.getDepth()*=scalex;
        this.scalez = scalez;
    }

    public float getWidth() {
        return dimensions.get(this.type).width;
    }

    public float getHeight() {
        return dimensions.get(this.type).height;
    }

    public float getDepth() {
        return dimensions.get(this.type).depth;
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
    
    public Matrix4 getInverseModelMatrix() {
        return inverseModelMatrix;
    }
    

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setZ(float z) {
        this.z = z;
    }
}


