/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cg.farmhover;

import static cg.farmhover.TestScene.cows;
import cg.farmhover.objects.Camera;
import cg.farmhover.objects.Ufo;
import java.awt.event.KeyEvent;
import java.util.BitSet;

/**
 *
 * @author Hikari Kyuubi
 */
public class Updater {
    
    public Updater () {
        
    }
    
    public void movementApplier(BitSet keyBits, Ufo ufo, Camera cam) {
        int direction = 0, moveY = 0, rX = 0, rY = 0, rZ = 0;
        
        if (keyBits.get(KeyEvent.VK_SPACE)){
            System.out.println(ufo.getX()+":"+ufo.getZ());
            float ux = ufo.getX();
            float uz = ufo.getZ();
            for(int i = 0; i < cows.size(); ++i){
                if(cows.get(i).isUnderUFO(ufo)){
                    cows.get(i).uprise(ufo);
                    TestScene.risingCow = cows.get(i);
                    break;
                }
            }
        }
        if (keyBits.get(KeyEvent.VK_W))
            moveY = 1; // arise
        if (keyBits.get(KeyEvent.VK_S))
            moveY = -1; // descend
        if (keyBits.get(KeyEvent.VK_A) || keyBits.get(KeyEvent.VK_LEFT))
            rY = -1; // turn left
        if (keyBits.get(KeyEvent.VK_D) || keyBits.get(KeyEvent.VK_RIGHT))
            rY = 1; // turn right
        if (keyBits.get(KeyEvent.VK_Q))
            rZ = 1; // flip to left
        if (keyBits.get(KeyEvent.VK_E))
            rZ = -1; // flip to right
        if (keyBits.get(KeyEvent.VK_2))
            rX = 1; // flip frontward
        if (keyBits.get(KeyEvent.VK_X))
            rX = -1; // flip backward
        if (keyBits.get(KeyEvent.VK_UP))
            direction = 1; // Move front
        if (keyBits.get(KeyEvent.VK_DOWN))
            direction = -1; // Move back
        //====================================================
        ufo.rotate(rX, rY, rZ);
        ufo.move(direction, moveY);
        cam.updatePosition(ufo);
    }

}
