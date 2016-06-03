package cg.farmhover.objects;

import cg.farmhover.Model;
import cg.farmhover.gl.util.Matrix4;
import cg.farmhover.gl.util.Shader;
import java.io.File;
import java.io.IOException;
import static java.lang.Math.abs;
import static java.lang.Math.sqrt;
import javax.media.opengl.GL3;

public class SceneObject {

    public SceneObject(String filepath, float x, float y, float z,
            float scalex, float scaley, float scalez) {
        this.model = new Model(new File(filepath));
        this.x = x;
        this.y = y;
        this.z = z;
        this.scalex = scalex;
        this.scaley = scaley;
        this.scalez = scalez;
    }

    
    Model model;
    private Matrix4 inverseModelMatrix;

    
    float x,y,z;
    public float rx, ry, rz; // rotação ------------ depois mudar pra getters e setters (ou não)
    private float width, height, depth;
    private float scalex, scaley, scalez;

    public void init(GL3 gl, Shader shader) {
        // Get pipeline

        try {
            model.init(gl, shader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.model.unitize(this);
        this.inverseModelMatrix = new Matrix4();
 
        System.err.println("======================> w:" + this.width 
                           + " h:" + this.height + " d:"+ this.depth);
    }
    
    public void resetInverseModelMatrix(){
        inverseModelMatrix.loadIdentity();
        inverseModelMatrix.getStack().clear();
    }
    
    private void findInverseModelMatrix(){
        inverseModelMatrix.loadIdentity();
        while(!inverseModelMatrix.getStack().empty()){
            inverseModelMatrix.multiply(inverseModelMatrix.getStack().pop());
        }
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
        float maxDist = (Math.max(Math.max(this.width*this.scalex, this.depth*this.scalez), this.height*this.scaley) +
                    Math.max(Math.max(other.getDepth()*other.getScalez(), other.getHeight()*other.getScaley()), other.getWidth()*other.getScalex()))/2;
  
        if(findDist(other)>=maxDist){ // se a distância entre os centros for maior que a soma dos 'raios' dos objetos       
            return false;
        }
        //System.out.println("maxdist = "+maxDist + "ufo: "+this.x+", "+this.y+", "+this.z+" | cow: "+other.x+", "+other.y+", "+other.z);
        findInverseModelMatrix();
        float realwidth = other.getWidth()*other.getScalex();
        float realheight = other.getHeight()*other.getScaley();
        float realdepth = other.getDepth()*other.getScalez();
        float ox = other.getX();
        float oy = other.getY();
        float oz = other.getZ();
        // pegar cantos do 'other' no novo sistema de coordenadas
        // extremidades da 'frente' do cubo
        float[] topleft1 = multiplyPos4Matrix(ox-realwidth/2, oy+realheight/2, oz+realdepth/2, this.inverseModelMatrix.getMatrix());
        float[] topright1 = multiplyPos4Matrix(ox+realwidth/2, oy+realheight/2, oz+realdepth/2, this.inverseModelMatrix.getMatrix());
        float[] bottomleft1 = multiplyPos4Matrix(ox-realwidth/2, oy-realheight/2, oz+realdepth/2, this.inverseModelMatrix.getMatrix());
        float[] bottomright1 = multiplyPos4Matrix(ox+realwidth/2, oy-realheight/2, oz+realdepth/2, this.inverseModelMatrix.getMatrix());
        // extremidades de 'trás' do cubo
        float[] topleft2 = multiplyPos4Matrix(ox-realwidth/2, oy+realheight/2, oz-realdepth/2, this.inverseModelMatrix.getMatrix());
        float[] topright2 = multiplyPos4Matrix(ox+realwidth/2, oy+realheight/2, oz-realdepth/2, this.inverseModelMatrix.getMatrix());
        float[] bottomleft2 = multiplyPos4Matrix(ox-realwidth/2, oy-realheight/2, oz-realdepth/2, this.inverseModelMatrix.getMatrix());
        float[] bottomright2 = multiplyPos4Matrix(ox+realwidth/2, oy-realheight/2, oz-realdepth/2, this.inverseModelMatrix.getMatrix());
        
        float[] pos0  = multiplyPos4Matrix(this.x, this.y, this.z, this.inverseModelMatrix.getMatrix()); // posição pré-transformações 
        return (isInsideCube(topleft1, pos0, this.width*this.scalex, this.height*this.scaley, this.depth*this.scalez)
                || isInsideCube(topright1, pos0, this.width*this.scalex, this.height*this.scaley, this.depth*this.scalez)
                || isInsideCube(bottomleft1, pos0, this.width*this.scalex, this.height*this.scaley, this.depth*this.scalez)
                || isInsideCube(bottomright1, pos0, this.width*this.scalex, this.height*this.scaley, this.depth*this.scalez)
                || isInsideCube(topleft2, pos0, this.width*this.scalex, this.height*this.scaley, this.depth*this.scalez)
                || isInsideCube(topright2, pos0, this.width*this.scalex, this.height*this.scaley, this.depth*this.scalez)
                || isInsideCube(bottomleft2, pos0, this.width*this.scalex, this.height*this.scaley, this.depth*this.scalez)
                || isInsideCube(bottomright2, pos0, this.width*this.scalex, this.height*this.scaley, this.depth*this.scalez)
                );
    }
    
    static private boolean isInsideCube(float[] pos, float[] center, float w, float h, float d){
        //System.out.println(pos[0]+" "+pos[1]+" "+pos[2]);
        return (pos[0]>=(center[0]-w/2) && pos[0]<=(center[0]+w/2)) 
                && (pos[1]>=(center[1]-h/2) && pos[1]<=(center[1]+h/2))
                && (pos[2]>=(center[2]-d/2) && pos[2]<=(center[2]+d/2));
    }
    
    float findDist(SceneObject other){
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
    
    public Matrix4 getInverseModelMatrix() {
        return inverseModelMatrix;
    }
    
    public void setModel(Model model) {
        this.model = model;
    }
}


