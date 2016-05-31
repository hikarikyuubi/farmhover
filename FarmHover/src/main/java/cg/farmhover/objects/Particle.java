package cg.farmhover.objects;

import cg.farmhover.Util;

public class Particle {
    private float[] position, velocity;
    private float gravityEffect, lifeLength, rotation, scale, elapsedTime;

    public Particle(float[] position, float[] velocity, float gravityEffect, float lifeLength, float rotation, float scale) {
        this.position = position;
        this.velocity = velocity;
        this.gravityEffect = gravityEffect;
        this.lifeLength = lifeLength;
        this.rotation = rotation;
        this.scale = scale;
        this.elapsedTime = 0;
    }

    public float[] getPosition() {
        return position;
    }

    public float getRotation() {
        return rotation;
    }

    public float getScale() {
        return scale;
    }
    
    public boolean update(Ufo ufo) {
        //velocity[1] += 0.005f * gravityEffect;
        velocity[2] += 0.0025f * gravityEffect * Util.cos(ufo.getRy());
        velocity[0] += 0.0025f * gravityEffect * -Util.sin(ufo.getRy());
        scale *= 1.01;
       float[] change = velocity.clone();
        position[0] += change[0]*scale;
        position[1] += change[1]*scale;
        position[2] += change[2]*scale;
        elapsedTime += 1;
        return (elapsedTime < lifeLength);
    }
    

}
