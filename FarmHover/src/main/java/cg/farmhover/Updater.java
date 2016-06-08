package cg.farmhover;

import static cg.farmhover.Main.animator;
import static cg.farmhover.TestScene.cows;
import static cg.farmhover.TestScene.objects;
import static cg.farmhover.TestScene.terrain;

import cg.farmhover.gl.core.ConeLight;
import cg.farmhover.models.LightCone;
import cg.farmhover.objects.Camera;
import cg.farmhover.objects.Cow;
import cg.farmhover.objects.Particle;
import cg.farmhover.objects.ParticleSystem;
import cg.farmhover.objects.SceneObject;
import cg.farmhover.objects.Ufo;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;


public class Updater {
    
    public Updater () {
    }
    public volatile static boolean playMoo = false;
    public volatile static boolean playLoop = false;
    public volatile static boolean playHit = false;
    public void movementApplier(BitSet keyBits, Ufo ufo, Camera cam, LightCone cone) {
        int direction = 0, moveY = 0, rX = 0, rY = 0, rZ = 0;
        
        if (keyBits.get(KeyEvent.VK_SPACE)){
            System.out.println(ufo.getX()+":"+ufo.getZ());
            float ux = ufo.getX();
            float uz = ufo.getZ();
            for(Cow cow : cows){
                if(cow.isUnderUFO(ufo)){
                    if(TestScene.risingCow!=cow){
                        playMoo = true;
                    }
                    playLoop = true;
                    cow.uprise(ufo);
                    TestScene.risingCow = cow;
                    break;
                }
            }
            cone.setDrawCone(true);
        }
        if(keyBits.get(KeyEvent.VK_ESCAPE)){ // ESC pra fechar o programa
             new Thread(new Runnable() {
                    @Override
                    public void run() {
                        animator.stop();
                        System.exit(0);
                    }
                }).start();
        } 
        if (keyBits.get(KeyEvent.VK_W)){
            moveY = 1; // arise
            rX = 1; 
            ufo.movingY = true;
        }
        if (keyBits.get(KeyEvent.VK_S)){
            moveY = -1; // descend
            rX = -1;
            ufo.movingY = true;
        }
        if (keyBits.get(KeyEvent.VK_A) || keyBits.get(KeyEvent.VK_LEFT)){
            rY = -1; // turn left
//            direction = 1;
            rZ = 1; 
            ufo.turning = true;
        }
        if (keyBits.get(KeyEvent.VK_D) || keyBits.get(KeyEvent.VK_RIGHT)){
            rY = 1; // turn right
//            direction = 1;
            rZ = -1;
            ufo.turning = true;
        }
            
        if (keyBits.get(KeyEvent.VK_Q))
            rZ = 1; // flip to left
        if (keyBits.get(KeyEvent.VK_E))
            rZ = -1; // flip to right
        if (keyBits.get(KeyEvent.VK_2))
            rX = 1; // flip frontward
        if (keyBits.get(KeyEvent.VK_X))
            rX = -1; // flip backward
        if (keyBits.get(KeyEvent.VK_UP)){
            direction = 1; // Move front
            ufo.movingXZ = true;
        }
        if (keyBits.get(KeyEvent.VK_DOWN)){
            direction = -1; // Move back
            ufo.movingXZ = true;
        }
       
        //====================================================
        ufo.rotate(rX, rY, rZ);
        ufo.move(direction, moveY);
        if(checkUFOCollision(ufo)|| (ufo.getY()-ufo.getHeight()*ufo.getScaley()/2)<= terrain.getHeightofTerrain(ufo.getX(), ufo.getZ())){
            ufo.move(-direction, -moveY);
        }
        cam.updatePosition(ufo);
    }
    public ArrayList<Particle> particleUpdater(BitSet keyBits, ParticleSystem psys, ArrayList<Particle> particles, Ufo ufo) {
            int aux = (keyBits.get(KeyEvent.VK_UP))? 1 : 0;
            aux = (keyBits.get(KeyEvent.VK_DOWN)) ? -1 : aux;
            if (aux != 0) {
                
                float[] pos = new float[3];
                pos[0] = ufo.getX();
                pos[1] = ufo.getY();
                pos[2] = ufo.getZ();
                float[] rot = new float[3];
                rot[0] = ufo.getRx();
                rot[1] = ufo.getRy();
                rot[2] = ufo.getRz();
                particles = psys.generateParticles(1, pos, rot, particles);
            }

            Iterator<Particle> iterator = particles.iterator();
           while(iterator.hasNext()) {
               Particle p = iterator.next();
               if(!p.update(ufo)) {
                   iterator.remove();
               }
           }
           if (keyBits.get(KeyEvent.VK_1) || Util.EXPLODE) {
               Util.EXPLODE = false;
               float[] pos = new float[3];
                pos[0] = ufo.getX();
                pos[1] = ufo.getY();
                pos[2] = ufo.getZ();
                ParticleSystem p = new ParticleSystem(100, 0.5f, 0, 60);
                // todo: change texture
               particles = p.explosion(pos, particles);
           }
           return particles;
    }
    
    boolean checkUFOCollision(Ufo ufo){
        for(SceneObject obj : objects){
            if(ufo.isColliding(obj)){
                return true;
            }
        }
        for(Cow cow : cows){
            if(ufo.isColliding(cow)){
                return true;
            }
        }
        return false;
    } 
 
}
