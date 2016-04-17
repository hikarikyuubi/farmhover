/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cg.farmhover;

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
    
    public void movementApplier(BitSet keyBits, Ufo ufo) {
        int forward = 0, moveY = 0, rX = 0, rY = 0, rZ = 0;
        
        if (keyBits.get(KeyEvent.VK_SPACE))
            forward = 1;
        if (keyBits.get(KeyEvent.VK_W))
            moveY = 1;
        if (keyBits.get(KeyEvent.VK_S))
            moveY = -1;
        if (keyBits.get(KeyEvent.VK_A))
            rY = -1;
        if (keyBits.get(KeyEvent.VK_D))
            rY = 1;
        if (keyBits.get(KeyEvent.VK_Q))
            rZ = 1;
        if (keyBits.get(KeyEvent.VK_E))
            rZ = -1;
        if (keyBits.get(KeyEvent.VK_2))
            rX = 1;
        if (keyBits.get(KeyEvent.VK_X))
            rX = -1;

        ufo.rotate(rX, rY, rZ);
        ufo.move(forward, moveY);
    }
}
