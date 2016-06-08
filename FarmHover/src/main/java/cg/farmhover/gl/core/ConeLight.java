package cg.farmhover.gl.core;


import cg.farmhover.gl.util.Shader;
import com.jogamp.common.nio.Buffers;

import javax.media.opengl.GL3;
import java.util.Arrays;

public class ConeLight {
    private GL3 gl;

    private float[] ambientColor;
    private float[] position;
    private float attenuation;
    private float coneAngle;
    private float[] coneDirection;

    private int positionHandle;
    private int ambientColorHandle;
    private int attenuationHandle;
    private int coneAngleHandle;
    private int coneDirectionHandle;


    public ConeLight() {
        setPosition(new float[]{50.0f, 50.0f, 50.0f});
        setAmbientColor(new float[]{0.1f, 1.0f, 0.1f});

    }

    public final void setPosition(float[] position) {
        this.position = Arrays.copyOf(position, position.length);
    }

    public final void setAmbientColor(float[] ambientColor) {
        this.ambientColor = Arrays.copyOf(ambientColor, ambientColor.length);
    }
    public final void setConeDirection(float[] coneDirection) {
        this.coneDirection = Arrays.copyOf(coneDirection, coneDirection.length);
    }
    public final void setAttenuation(float attenuation) {
        this.attenuation = attenuation;
    }

    public final void setConeAngle(float coneAngle) {
        this.coneAngle = coneAngle;
    }


    public void init(GL3 gl) {
        this.gl = gl;
    }
    public void bind(Shader shader) {
        this.positionHandle = shader.getUniformLocation("coneLightPosition");
        this.ambientColorHandle = shader.getUniformLocation("conelight.lightColour");
        this.attenuationHandle = shader.getUniformLocation("conelight.attenuation");
        this.coneAngleHandle = shader.getUniformLocation("conelight.coneAngle");
        this.coneDirectionHandle = shader.getUniformLocation("conelight.coneDirection");
        System.out.println(positionHandle + " " + ambientColorHandle + " " + attenuationHandle + " " + coneAngleHandle + " " +
                coneDirectionHandle + " AQUIIIII");
    }

    public void draw() {
        gl.glUniform3fv(positionHandle, 1, Buffers.newDirectFloatBuffer(position));
        gl.glUniform3fv(ambientColorHandle, 1, Buffers.newDirectFloatBuffer(ambientColor));
        gl.glUniform3fv(coneDirectionHandle, 1, Buffers.newDirectFloatBuffer(coneDirection));

        gl.glUniform1f(attenuationHandle, attenuation);
        gl.glUniform1f(coneAngleHandle, coneAngle);
    }

    public float[] getConeDirection() {
        return coneDirection;
    }

    public float getConeAngle() {
        return coneAngle;
    }

    public float getAttenuation() {
        return attenuation;
    }

    public float[] getPosition() {
        return position;
    }

    public float[] getAmbientColor() {
        return ambientColor;
    }
}
