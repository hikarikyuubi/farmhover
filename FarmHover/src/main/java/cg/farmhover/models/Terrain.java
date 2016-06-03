package cg.farmhover.models;


import cg.farmhover.gl.util.Shader;
import cg.farmhover.gl.util.Texture;
import cg.farmhover.gl.util.TextureLoader;
import com.jogamp.common.nio.Buffers;

import javax.imageio.ImageIO;
import javax.media.opengl.GL;
import javax.media.opengl.GL3;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class Terrain {

    private GL3 gl;
    private int vertex_positions_handle;
    private int vertex_normals_handle;
    private int vertex_texcoords_handle;
    private int texture_handle;
    private int[] vao;
    private Texture texture;
    private TextureLoader loader;
    protected float[] vertex_buffer;
    protected float[] normal_buffer;
    protected int[] indices_buffer;
    protected float[] texture_buffer;


    private String[] TEXTURE = {"nightGrass"};

    public static final float SIZE = 1000;
    public static int VERTEX_COUNT;
    private static final float MAX_HEIGHT = 40;
    private static final float MAX_PIXEL_COLOUR = 256*256*256;

    public Terrain(String heightmapName, String extension) {
        BufferedImage image = null;
        try {
            String ref = "images/"+heightmapName + extension;
            URL url = TextureLoader.class.getClassLoader().getResource("images/"+ heightmapName + extension);
            if (url == null) {
                throw new IOException("Cannot find: "+ref);
            }
                image = ImageIO.read(new BufferedInputStream(getClass().getClassLoader().getResourceAsStream(ref)));
            VERTEX_COUNT = image.getHeight();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int count = VERTEX_COUNT * VERTEX_COUNT;
        vertex_buffer = new float[count * 3];
        normal_buffer = new float[count * 3];
        texture_buffer = new float[count*2];
        indices_buffer = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
        int vertexPointer = 0;
        for(int i=0;i<VERTEX_COUNT;i++){
            for(int j=0;j<VERTEX_COUNT;j++){
                vertex_buffer[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE;
                vertex_buffer[vertexPointer*3+1] = getHeight(j,i,image);
                vertex_buffer[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE;
                float[] normal = calculateNormal(j,i,image);
                normal_buffer[vertexPointer*3] = normal[0];
                normal_buffer[vertexPointer*3+1] = normal[1];
                normal_buffer[vertexPointer*3+2] = normal[2];
                texture_buffer[vertexPointer*2] = (float)j/((float)VERTEX_COUNT - 1);
                texture_buffer[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT - 1);
                vertexPointer++;
            }
        }
        int pointer = 0;
        for(int gz=0;gz<VERTEX_COUNT-1;gz++){
            for(int gx=0;gx<VERTEX_COUNT-1;gx++){
                int topLeft = (gz*VERTEX_COUNT)+gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
                int bottomRight = bottomLeft + 1;
                indices_buffer[pointer++] = topLeft;
                indices_buffer[pointer++] = bottomLeft;
                indices_buffer[pointer++] = topRight;
                indices_buffer[pointer++] = topRight;
                indices_buffer[pointer++] = bottomLeft;
                indices_buffer[pointer++] = bottomRight;
            }
        }
    }

    private float[] calculateNormal(int x, int y,BufferedImage heightmap) {
        float heightL = getHeight(x-1,y,heightmap);
        float heightR = getHeight(x+1,y,heightmap);
        float heightD = getHeight(x,y-1,heightmap);
        float heightU = getHeight(x,y+1,heightmap);
        float[] normal = new float[3];
        normal[0] = heightL-heightR;
        normal[1] = 2f;
        normal[2] = heightD - heightU;
        normalize(normal);
        return normal;
    }
    private void normalize(float[] vector) {
        float norm = (float) Math.sqrt(vector[0] * vector[0]
                + vector[1] * vector[1]
                + vector[2] * vector[2]);
        vector[0] /= norm;
        vector[1] /= norm;
        vector[2] /= norm;
    }
    private float getHeight(int imgCoordX, int imgCoordY, BufferedImage heightmap) {
        if(imgCoordX <= 0 || imgCoordX >= heightmap.getHeight() || imgCoordY <= 0 || imgCoordY >= heightmap.getHeight()) {
            return 0;
        }
        float height = heightmap.getRGB(imgCoordX,imgCoordY);
        height += MAX_PIXEL_COLOUR/2f;
        height /= MAX_PIXEL_COLOUR/2f;
        height *= MAX_HEIGHT;
        return height;
    }
    public void draw() {
        // Desenha o buffer carregado em memória (triangulos)
        gl.glDrawElements(GL.GL_TRIANGLES, indices_buffer.length,
                GL.GL_UNSIGNED_INT, 0);
        gl.glBindVertexArray(0);
    }

    public void init(GL3 gl, Shader shader) {
        this.gl = gl;
        this.vertex_positions_handle = shader.getAttribLocation("a_position");
        this.vertex_normals_handle = shader.getAttribLocation("a_normal");
        this.vertex_texcoords_handle = shader.getAttribLocation("a_texcoord");
        this.texture_handle = shader.getUniformLocation("u_texture");
        loader = new TextureLoader(gl);
        create_object(gl);
    }

    public void bind() {
        //gl.glActiveTexture(GL.GL_TEXTURE0);
        texture.bind();
        gl.glUniform1i(texture_handle,0);
        gl.glBindVertexArray(vao[0]);
    }
    public void dispose() {
    }

    public void loadTexture(String[] filename,String extension) throws IOException {
        texture = loader.getTexture(filename, GL.GL_TEXTURE_2D, GL.GL_RGBA, GL.GL_LINEAR, GL.GL_LINEAR, extension);
    }

    private void create_object(GL3 gl) {
        vao = new int[1];
        gl.glGenVertexArrays(1, vao, 0);
        gl.glBindVertexArray(vao[0]);

        // create vertex positions buffer
        int vbo[] = new int[4];
        gl.glGenBuffers(4, vbo, 0);

        //the positions buffer
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, vbo[0]); // Bind vertex buffer
        gl.glBufferData(GL3.GL_ARRAY_BUFFER, vertex_buffer.length * Buffers.SIZEOF_FLOAT,
                Buffers.newDirectFloatBuffer(vertex_buffer), GL3.GL_STATIC_DRAW);
        gl.glEnableVertexAttribArray(vertex_positions_handle);
        gl.glVertexAttribPointer(vertex_positions_handle, 3, GL3.GL_FLOAT, false, 0, 0);
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);

        if (normal_buffer != null) {
            //the normals buffer
            gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, vbo[1]); // Bind normals buffer
            gl.glBufferData(GL3.GL_ARRAY_BUFFER, normal_buffer.length * Buffers.SIZEOF_FLOAT,
                    Buffers.newDirectFloatBuffer(normal_buffer), GL3.GL_STATIC_DRAW);
            gl.glEnableVertexAttribArray(vertex_normals_handle);
            gl.glVertexAttribPointer(vertex_normals_handle, 3, GL3.GL_FLOAT, false, 0, 0);
            gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
        }
        if (texture_buffer != null) {
            gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo[2]); // Bind normals buffer
            gl.glBufferData(GL3.GL_ARRAY_BUFFER, texture_buffer.length * Buffers.SIZEOF_FLOAT,
                    Buffers.newDirectFloatBuffer(texture_buffer), GL3.GL_STATIC_DRAW);
            gl.glEnableVertexAttribArray(vertex_texcoords_handle);
            gl.glVertexAttribPointer(vertex_texcoords_handle, 2, GL3.GL_FLOAT, false, 0, 0);
            gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
            //gl.glUniform1i(texture_handle, 0);
        }
        if (indices_buffer != null) {
            //the normals buffer
            gl.glBindBuffer(GL3.GL_ELEMENT_ARRAY_BUFFER, vbo[3]); // Bind normals buffer
            gl.glBufferData(GL3.GL_ELEMENT_ARRAY_BUFFER, indices_buffer.length * Buffers.SIZEOF_INT,
                    Buffers.newDirectIntBuffer(indices_buffer), GL3.GL_STATIC_DRAW);
        }
    }

    public String[] getTextureFile() {
        return TEXTURE;
    }
    public void setTextureFile(String[] TEXTURE) {
        this.TEXTURE = TEXTURE;
    }
}
