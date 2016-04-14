package cg.farmhover.objects;

import cg.farmhover.Model;
import cg.farmhover.TestScene;
import java.io.File;

public class Cow extends SceneObject {
    public boolean rising;
    float fallHeight;
    
    public Cow(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        // descomentar a linha abaixo e comentar a seguinte para usar a vaca com textura
        //model = new JWavefrontObject(new File(".\\models\\cow.obj"));
        model = new Model(new File(".\\models\\newCow.obj"));
        rising = false;
    }
    
    public void uprise(Ufo ufo){
        rising = true;
        fallHeight = ufo.getY();
        if (y < ufo.getY()-2){
            y += 0.02f;
        } else {
            TestScene.cows.remove(this);
        }
    }
    
    public void applyGravity(){
        if(rising==false && y>1){
            y -= (fallHeight-y)*0.02f; // pra acelerar com o tempo 
        }
    }
}
