/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cg.farmhover.objects;

import cg.farmhover.Util;
import java.util.ArrayList;

/**
 *
 * @author Hikari Kyuubi
 */
public class ParticleSystem {
    private float pps;
    private float speed;
    private float gravityComplient;
    private float lifeLength;
    
    public ParticleSystem(float pps, float speed, float gravityComplient, float lifeLength) {
        this.pps = pps;
        this.speed = speed;
        this.gravityComplient = gravityComplient;
        this.lifeLength = lifeLength;
    }
     
    public  ArrayList<Particle> generateParticles(int direction, float[] systemCenter, float[] systemRotate, ArrayList<Particle> particles){
        float particlesToCreate = pps;
        int count = (int) Math.floor(particlesToCreate);
        for (int i = 0; i < count; i++){
            particles.add(emitParticle(direction, systemCenter, systemRotate));
        }
        return particles;
    }
     
    private Particle emitParticle(int direction, float[] center, float[] rotation){
        

        float dirX = (float) Math.random() /(-4) * Util.sin(rotation[1])+ ((float) Math.random() *2 - 1)/10;
        float dirZ = (float) Math.random() /4  * Util.cos(rotation[1]) + ((float) Math.random() *2 - 1)/10;
        float dirY = (float) Math.random() * -1/4 *  Util.sin(rotation[0]);
        float[] velocity = new float[3];
        velocity[0] = dirX; velocity[1] = dirY; velocity[2] = dirZ;
        
        //normalize
        float b = (float) Math.sqrt(velocity[0]*velocity[0] + velocity[1]*velocity[1] + velocity[2]*velocity[2]);
        if (b !=0) {
            velocity[0] /= b;
            velocity[1] /= b;
            velocity[2] /= b;
        }
        //scale
        velocity[0] *= speed * direction;
        velocity[1] *= speed * direction;
        velocity[2] *= speed * direction;        
        
        return new Particle(center.clone(), velocity, gravityComplient, lifeLength, (float) Math.random() *360, (float) Math.random()/2);
    }
     
    public ArrayList<Particle> explosion(float[] center, ArrayList<Particle> particles) {
        int count = (int) Math.floor(pps);
        for (int i = 0; i < count; i++){
            float dirX = (float) Math.random() * 2 - 1;
            float dirZ = (float) Math.random() * 2 - 1;
            float dirY = (float) Math.random() * 2 -1;

            float[] velocity = new float[3];
            velocity[0] = dirX; velocity[1] = dirY; velocity[2] = dirZ;

            //normalize
            float b = (float) Math.sqrt(velocity[0]*velocity[0] + velocity[1]*velocity[1] + velocity[2]*velocity[2]);
            if (b !=0) {
                velocity[0] /= b;
                velocity[1] /= b;
                velocity[2] /= b;
            }
            //scale
            velocity[0] *= speed;
            velocity[1] *= speed;
            velocity[2] *= speed;        

            particles.add(new Particle(center.clone(), velocity, gravityComplient, lifeLength, (float) Math.random() *360, (float) Math.random()/2));
            
        }
        return particles;
    }
}
