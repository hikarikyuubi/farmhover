package cg.farmhover.models;

import cg.farmhover.gl.core.ConeLight;

public class LightCone {

    private SimpleModel cone;
    private ConeLight conelight;
    private float speed;
    private float maxLimit;
    private float minLimit;
    private float yPosition;
    private float scale;
    private float scaleFactor;
    private boolean drawCone;
    public LightCone() {
        cone = new MultiCircle();
        speed = 0.005f;
        maxLimit = 1f;
        minLimit = 0f;
        yPosition = 1f;
        scale = 0.5f;
        scaleFactor = 0.05f;
    }


    public void activate() {

        yPosition += speed;
        if (yPosition > maxLimit || yPosition < minLimit) {
            speed *= -1;
        }
        if (scale < 1) {
            scale += scaleFactor;
        }
        conelight.draw();
    }

    public SimpleModel getCone() {
        return cone;
    }

    public void setCone(SimpleModel cone) {
        this.cone = cone;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(float maxLimit) {
        this.maxLimit = maxLimit;
    }

    public float getMinLimit() {
        return minLimit;
    }

    public void setMinLimit(float minLimit) {
        this.minLimit = minLimit;
    }

    public float getyPosition() {
        return yPosition;
    }

    public void setyPosition(float yPosition) {
        this.yPosition = yPosition;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public float getScaleFactor() {
        return scaleFactor;
    }

    public void setScaleFactor(float scaleFactor) {
        this.scaleFactor = scaleFactor;
    }

    public ConeLight getConelight() {
        return conelight;
    }

    public void setConelight(ConeLight conelight) {
        this.conelight = conelight;
    }

    public boolean isDrawCone() {
        return drawCone;
    }

    public void setDrawCone(boolean drawCone) {
        this.drawCone = drawCone;
    }
}