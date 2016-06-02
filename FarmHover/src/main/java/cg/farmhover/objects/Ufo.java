package cg.farmhover.objects;

import cg.farmhover.Model;
import cg.farmhover.Util;
import java.io.File;

public class Ufo extends SceneObject {
    private float moveVel, flipDeg; // bases
    
    public Ufo() {
        rx = ry = rz = x = z = 0f;
        y = 10f;
        moveVel = 0.2f;
        flipDeg = 1.5f;
        model = new Model(new File(".\\models\\UFO.obj"));
        this.setScalex(2.5f);
        this.setScaley(2.5f);
        this.setScalez(2.5f);
    }
    
    
    public void move(int direction, int moveY) {
        this.y += (moveY * this.moveVel);
        if(direction!=0){
            // A ser ajustado ainda
            float front = direction * Util.cos(this.rx) * this.moveVel;
            this.x += Util.sin(this.ry) * front;
            this.z += -1 * Util.cos(this.ry) * front;
            this.y += Util.sin(this.rx) * front;
        }
    }

    public void rotate(int x, int y, int z) {
        this.rx = this.rx + x * this.flipDeg;
        if ( this.rx > 45) this.rx = 45;
        else if ( this.rx < -45) this.rx = -45;
        
        this.rz = this.rz + z * this.flipDeg;
        if (this.rz > 80) this.rz = 80;
        else if (this.rz < -80) this.rz = -80;
        
        this.ry = (this.ry + y * this.flipDeg) % 360;
    }

}