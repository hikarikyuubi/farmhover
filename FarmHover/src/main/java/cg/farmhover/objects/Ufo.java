package cg.farmhover.objects;

import cg.farmhover.Model;
import java.io.File;

public class Ufo extends SceneObject {
    private float moveVel, flipDeg; // bases
    
    public Ufo() {
        rx = ry = rz = x = z = 0f;
        y = 8f;
        moveVel = 0.15f;
        flipDeg = 1.5f;
        model = new Model(new File(".\\models\\UFO.obj"));
    }
    
    private float roundDec(double number, int decimal) {
        int roundPoint = 1;
        for (int i = 0; i < decimal; i++) {
            roundPoint *= 10;
        }
        return ((float) Math.round(number * roundPoint))/((float)roundPoint);
    }

    public void move(int direction, int moveY) {
        this.y += (moveY * this.moveVel);
        if(direction!=0){
            this.x += (roundDec(Math.sin(Math.toRadians(ry)), 3) * direction * this.moveVel);
            this.z += -1*(roundDec(Math.cos(Math.toRadians(ry)), 3) * direction * this.moveVel);
        }
    } 

    public void rotate(int x, int y, int z) {
        this.rx = (this.rx + x * this.flipDeg) % 360;
        this.ry = (this.ry + y * this.flipDeg) % 360;
        this.rz = (this.rz + z * this.flipDeg) % 360;
    }
    
  
    
    public float getLookat(char axis) {
        if (axis == 'x') return -1*roundDec(Math.sin(Math.toRadians(ry)), 3);
        return roundDec(Math.cos(Math.toRadians(ry)), 3);
    }
}
