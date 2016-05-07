package cg.farmhover.objects;

import cg.farmhover.Model;
import cg.farmhover.Util;
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
    
    
    public void move(int direction, int moveY) {
        this.y += (moveY * this.moveVel);
        if(direction!=0){
            // A ser ajustado ainda
            float front = Util.frontXW(direction, this.rx) * this.moveVel;
            this.x += (Util.roundDec(Math.sin(Math.toRadians(this.ry)), 3) * front);
            this.z += -1*(Util.roundDec(Math.cos(Math.toRadians(this.ry)), 3) * front);
            this.y += (Util.roundDec(Math.sin(Math.toRadians(this.rx)), 3) * front);
        }
    }

    public void rotate(int x, int y, int z) {
        this.rx = (this.rx + x * this.flipDeg) % 360;
        this.ry = (this.ry + y * this.flipDeg) % 360;
        this.rz = (this.rz + z * this.flipDeg) % 360;
    }

}