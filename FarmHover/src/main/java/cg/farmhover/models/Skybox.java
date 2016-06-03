package cg.farmhover.models;

import cg.farmhover.gl.util.Shader;
import cg.farmhover.gl.util.Texture;
import cg.farmhover.gl.util.TextureLoader;
import com.jogamp.common.nio.Buffers;
import javax.media.opengl.GL;
import javax.media.opengl.GL3;
import java.io.IOException;


public class Skybox {

    protected GL3 gl;
    private int vertex_positions_handle;
    private int texture_handle;
    private int[] vao;
    private TextureLoader texLoader;
    private Texture texture;
    private static final float SIZE = 1200;
    private String[] TEXTURE_FILES = {"right","left","top","bottom","back","front"};
    protected float[] vertex_buffer;

    public Skybox() {

        vertex_buffer = new float[]{
                -SIZE,  SIZE, -SIZE,
                -SIZE, -SIZE, -SIZE,
                SIZE, -SIZE, -SIZE,
                SIZE, -SIZE, -SIZE,
                SIZE,  SIZE, -SIZE,
                -SIZE,  SIZE, -SIZE,

                -SIZE, -SIZE,  SIZE,
                -SIZE, -SIZE, -SIZE,
                -SIZE,  SIZE, -SIZE,
                -SIZE,  SIZE, -SIZE,
                -SIZE,  SIZE,  SIZE,
                -SIZE, -SIZE,  SIZE,

                SIZE, -SIZE, -SIZE,
                SIZE, -SIZE,  SIZE,
                SIZE,  SIZE,  SIZE,
                SIZE,  SIZE,  SIZE,
                SIZE,  SIZE, -SIZE,
                SIZE, -SIZE, -SIZE,

                -SIZE, -SIZE,  SIZE,
                -SIZE,  SIZE,  SIZE,
                SIZE,  SIZE,  SIZE,
                SIZE,  SIZE,  SIZE,
                SIZE, -SIZE,  SIZE,
                -SIZE, -SIZE,  SIZE,

                -SIZE,  SIZE, -SIZE,
                SIZE,  SIZE, -SIZE,
                SIZE,  SIZE,  SIZE,
                SIZE,  SIZE,  SIZE,
                -SIZE,  SIZE,  SIZE,
                -SIZE,  SIZE, -SIZE,

                -SIZE, -SIZE, -SIZE,
                -SIZE, -SIZE,  SIZE,
                SIZE, -SIZE, -SIZE,
                SIZE, -SIZE, -SIZE,
                -SIZE, -SIZE,  SIZE,
                SIZE, -SIZE,  SIZE
        };
    }
    public void draw() {
        // Desenha o buffer carregado em memória (triangulos)
        gl.glDrawArrays(GL.GL_TRIANGLES, 0, vertex_buffer.length / 3);
        gl.glBindVertexArray(0);

    }

    public void init(GL3 gl, Shader shader) {
        this.gl = gl;
        this.vertex_positions_handle = shader.getAttribLocation("a_position");
        this.texture_handle = shader.getUniformLocation("u_texture");
        texLoader = new TextureLoader(gl);
        create_object(gl);
    }

    public void bind() {
        //gl.glActiveTexture(GL.GL_TEXTURE0);
        gl.glUniform1i(texture_handle,0);
        texture.bind();
        gl.glBindVertexArray(vao[0]);
    }

    public void loadTexture(String[] textureFiles, String extension) throws IOException {
        texture = texLoader.getTexture(textureFiles, GL.GL_TEXTURE_CUBE_MAP, GL.GL_RGBA, GL.GL_LINEAR, GL.GL_LINEAR, extension);
    }

    private void create_object(GL3 gl) {
        vao = new int[1];
        gl.glGenVertexArrays(1, vao, 0);
        gl.glBindVertexArray(vao[0]);

        int vbo[] = new int[1];
        gl.glGenBuffers(1, vbo, 0);

        //the positions buffer
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, vbo[0]); // Bind vertex buffer
        gl.glBufferData(GL3.GL_ARRAY_BUFFER, vertex_buffer.length * Buffers.SIZEOF_FLOAT,
                Buffers.newDirectFloatBuffer(vertex_buffer), GL3.GL_STATIC_DRAW);
        gl.glEnableVertexAttribArray(vertex_positions_handle);
        gl.glVertexAttribPointer(vertex_positions_handle, 3, GL3.GL_FLOAT, false, 0, 0);
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
        gl.glBindVertexArray(0);

    }


    public String[] getTextureFiles() {
        return TEXTURE_FILES;
    }

    public GL3 getGl() {
        return gl;
    }

    public Texture getTexture() {
        return texture;
    }
}
