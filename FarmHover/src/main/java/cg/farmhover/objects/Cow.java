package cg.farmhover.objects;

import cg.farmhover.Model;
import cg.farmhover.TestScene;
import java.io.File;

public class Cow extends SceneObject {
    public boolean rising;
    float fallHeight;
    
    public Cow(float x, float y, float z) {
        super(".\\models\\newCow.obj", x, y, z, 1.5f, 1.5f, 1.5f);
//        this.x = x;
//        this.y = y;
//        this.z = z;
        // descomentar a linha abaixo e comentar a seguinte para usar a vaca com textura
        //model = new Model(new File(".\\models\\cow.obj"));
        //model = new Model(new File(".\\models\\newCow.obj"));
        rising = false;
//        this.setScalex(1.5f);
//        this.setScaley(1.5f);
//        this.setScalez(1.5f);
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
        if(!rising && y > 1) {
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
