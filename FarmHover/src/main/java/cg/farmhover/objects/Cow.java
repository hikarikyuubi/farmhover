package cg.farmhover.objects;

import cg.farmhover.Main;
import cg.farmhover.Model;
import cg.farmhover.TestScene;
import static cg.farmhover.TestScene.terrain;
import static cg.farmhover.objects.SceneObject.ObjectType.COW;
import java.io.File;


public class Cow extends SceneObject {
    public boolean rising;
    float fallHeight;
    public static Model cow_model;
    public static Integer n_abducted_cows = 0;
    public Cow(float x, float y, float z) {    
        super(COW, x, y, z, 1.5f, 1.5f, 1.5f);
        rising = false;
    }

    public void uprise(Ufo ufo) {
        rising = true;
        fallHeight = ufo.getY();
        if (y < ufo.getY() - 2) {
            y += 0.09f;
            ry += 5;
        } else {
            TestScene.cows.remove(this);
        }
    }
    
    public void applyGravity(Ufo ufo) {
        if(rising && !this.isUnderUFO(ufo)){
            rising = false;
        }
        if(!rising && y > (terrain.getHeightofTerrain(this.getX(), this.getZ())+ this.getHeight()*this.getScalez())) {
            y -= (fallHeight - y) * 0.03f; // pra acelerar com o tempo 
        }
    }

    public boolean isUnderUFO(Ufo ufo) {
        float ufoxmin = ufo.getX() - ufo.getWidth() * ufo.getScalex()/2;
        float ufoxmax = ufo.getX() + ufo.getWidth() * ufo.getScalex()/2;
        float ufozmin = ufo.getZ() - ufo.getDepth() * ufo.getScalez()/2;
        float ufozmax = ufo.getZ() + ufo.getDepth() * ufo.getScalez()/2;

        return this.getX() >= ufoxmin && this.getX() <= ufoxmax 
                && this.getZ() >= ufozmin && this.getZ() <= ufozmax;
    }
}
