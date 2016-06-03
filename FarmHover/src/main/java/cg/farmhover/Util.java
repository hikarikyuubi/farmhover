/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cg.farmhover;

/**
 *
 * @author Hikari Kyuubi
 */
public class Util {
        
    public static float roundDec(double number, int decimal) {
        int roundPoint = 1;
        for (int i = 0; i < decimal; i++) {
            roundPoint *= 10;
        }
        return ((float) Math.round(number * roundPoint))/((float)roundPoint);
    }
    
    public static float sin(float angle) {
        return roundDec(Math.sin(Math.toRadians(angle)), 3);
    }
    
    public static float cos(float angle) {
        return roundDec(Math.cos(Math.toRadians(angle)), 3);
    }
    
    public static float max(float a, float b) {
        if (Math.abs(a) > Math.abs(b)) return a;
        return b;
    }
}
