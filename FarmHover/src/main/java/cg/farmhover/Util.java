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
    public static float barryCentric(float[] p1, float[] p2, float[] p3, float[] pos) {
        float det = (p2[2] - p3[2]) * (p1[0] - p3[0]) + (p3[0] - p2[0]) * (p1[2] - p3[2]);
        float l1 = ((p2[2] - p3[2]) * (pos[0] - p3[0]) + (p3[0] - p2[0]) * (pos[1] - p3[2])) / det;
        float l2 = ((p3[2] - p1[2]) * (pos[0] - p3[0]) + (p1[0] - p3[0]) * (pos[1] - p3[2])) / det;
        float l3 = 1.0f - l1 - l2;
        return l1 * p1[1] + l2 * p2[1] + l3 * p3[1];
    }
}
