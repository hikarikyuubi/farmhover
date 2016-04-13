/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cg.farmhover.models;

import javax.media.opengl.GL3;

public class Plan extends SimpleModel {

    public Plan() {
        vertex_buffer = new float[]{
            // Front face
           -1f, -1f, 1f, 
            1f, -1f, 1f, 
            1f, -1f, 1f, 
            1f, 1f, 1f, 
            1f, 1f, 1f, 
            -1f, 1f, 1f,
            -1f, 1f, 1f,
            -1f, -1f, 1f,

        };
    }

    @Override
    public void draw() {
        draw(GL3.GL_LINES);
    }

}
