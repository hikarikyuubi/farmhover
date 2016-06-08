package cg.farmhover.models;

import javax.media.opengl.GL;

/**
 * Created by mtgom on 6/6/2016.
 */
public class MultiCircle extends SimpleModel {

    private final int nr_vertices = 30;
    private final float size = 1.0f;

    public MultiCircle() {
    /*
    float inc = 0.02f;
    double circNum = size/inc;
    int a = (int)circNum;


    System.out.println(circNum + " " + a + " " + vertex_buffer.length);
    int k = 0;
    int count = 0;
    float init = 0;
    float decCos = 1.0f;
    for( int y =0; y < a; y++) {
      for (int i = 0; i < nr_vertices; i++, k++) {
        float x = decCos * (float) Math.cos(Math.toRadians((360.0f * i) / nr_vertices));
        //float y = 0;
        float z = decCos * (float) Math.sin(Math.toRadians((360.0f * i) / nr_vertices));;

        vertex_buffer[k] = x;
        vertex_buffer[++k] = init;
        vertex_buffer[++k] = z;
        count++;
        System.out.println(k + " " + x + " " + y + " " + z + " " + count);
      }
      decCos -= 0.01f;
      init += inc;
    }*/
        int k = 0;
        vertex_buffer = new float[4 * nr_vertices * 3];
        for (int i = 0; i < nr_vertices; i++, k++) {
            float x = (float) Math.cos(Math.toRadians((360.0f * i) / nr_vertices));
            float y = 0;
            float z = (float) Math.sin(Math.toRadians((360.0f * i) / nr_vertices));

            vertex_buffer[k] = x;
            vertex_buffer[++k] = y;
            vertex_buffer[++k] = z;
        }
        for (int i = 0; i < nr_vertices; i++, k++) {
            float x = (float) Math.cos(Math.toRadians((360.0f * i) / nr_vertices));
            float y = 0.1f;
            float z = (float) Math.sin(Math.toRadians((360.0f * i) / nr_vertices));

            vertex_buffer[k] = x;
            vertex_buffer[++k] = y;
            vertex_buffer[++k] = z;
        }
        for (int i = 0; i < nr_vertices; i++, k++) {
            float x = (float) Math.cos(Math.toRadians((360.0f * i) / nr_vertices));
            float y = 0.2f;
            float z = (float) Math.sin(Math.toRadians((360.0f * i) / nr_vertices));

            vertex_buffer[k] = x;
            vertex_buffer[++k] = y;
            vertex_buffer[++k] = z;
        }
        for (int i = 0; i < nr_vertices; i++, k++) {
            float x = (float) Math.cos(Math.toRadians((360.0f * i) / nr_vertices));
            float y = 0.3f;
            float z = (float) Math.sin(Math.toRadians((360.0f * i) / nr_vertices));

            vertex_buffer[k] = x;
            vertex_buffer[++k] = y;
            vertex_buffer[++k] = z;
        }
    }

    @Override
    public void draw() {
        draw(GL.GL_LINE_LOOP);
    }
}