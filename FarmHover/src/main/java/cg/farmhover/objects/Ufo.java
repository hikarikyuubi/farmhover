package cg.farmhover.objects;

import cg.farmhover.Model;
import static cg.farmhover.TestScene.ORIGIN;
import cg.farmhover.Util;
import static cg.farmhover.objects.SceneObject.ObjectType.UFO;
import java.io.File;

public class Ufo extends SceneObject {
    private float moveVel, flipDeg; // bases
    public boolean turning, movingY, movingXZ;
    
    
    public Ufo() {
        super(UFO, ORIGIN + 0f, 10f, ORIGIN + 0f, 2.5f, 2.5f, 2.5f);
        rx = ry = rz = 0f;
        moveVel = 0.35f;
        flipDeg = 1f;
        turning = false;
        movingY = false; 
        movingXZ = false;
    }
    
    public void undoXRotation(){
        if(!this.movingY && this.rx!=0){
            if(this.rx>0){
                this.rx = this.rx - z * 0.001f;
                if(this.rx<0)   this.rx = 0;
            } else {
                this.rx = this.rx + z * 0.001f;
                if(this.rx>0)   this.rx = 0;
            }
        }
    }
    
    public void undoTurningRotation(){
        if(!this.turning && this.rz!=0){
            if(this.rz>0){
                this.rz = this.rz - z * 0.003f;
                if(this.rz<0)   this.rz = 0;
            } else {
                this.rz = this.rz + z * 0.003f;
                if(this.rz>0)   this.rz = 0;
            }
        }
    }
    
    public void move(int direction, int moveY) {
        this.y += (moveY * this.moveVel);
        if(direction!=0){
            // A ser ajustado ainda
//            float front = direction * Util.cos(this.rx) * this.moveVel;
            float front = direction * Util.cos(this.rx) * this.moveVel*1.5f; 
            this.x += Util.sin(this.ry) * front;
            this.z += -1 * Util.cos(this.ry) * front;
            this.y += Util.sin(this.rx) * front;
        }
    }

    public void rotate(int x, int y, int z) {
        this.rx = this.rx + x * this.flipDeg*2f;
        if ( this.rx > 25) this.rx = 25;
        else if ( this.rx < -25) this.rx = -25;
        
        this.rz = this.rz + z * this.flipDeg;
        if (this.rz > 35) this.rz = 35;
        else if (this.rz < -35) this.rz = -35;
        
        this.ry = (this.ry + y * this.flipDeg) % 360;
    }

}