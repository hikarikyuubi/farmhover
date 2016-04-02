/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cg.farmhover;

import cg.farmhover.gl.util.Matrix4;
import cg.farmhover.models.SimpleModel;
import cg.farmhover.models.WiredCube;
import cg.farmhover.gl.util.Shader;
import cg.farmhover.gl.util.ShaderFactory;
import javax.media.opengl.GL;
import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

/**
 *
 * @author Hikari Kyuubi
 */
public class TestScene implements GLEventListener { 
    
    private Shader shader; // Configuração de comportamento da GPU
    private SimpleModel cube; // Diretivas de desenho do modelo
    private Matrix4 modelMatrix;
    private float delta;
    private float inc;
    
    public TestScene() {
        // Define Shader a ser usado (como ele funcionará)
        shader = ShaderFactory.getInstance(
                                 ShaderFactory.ShaderType.MODEL_MATRIX_SHADER);
        cube = new WiredCube(); // Define objeto a ser desenhado
        modelMatrix = new Matrix4();
        delta = 0.0f;
        inc = 0.1f;
    }
    
    @Override // Configura a inicialização
    public void init(GLAutoDrawable glad) {
        GL3 gl = glad.getGL().getGL3(); // Contexto de desenho
        
        // "Limpar" buffer com determinada cor (R, G, B, alfa)
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f); 
    
        shader.init(gl); // Manda shader para placa de vídeo
        shader.bind(); // Faz com que a GPU funcione de acordo com o shader 
        
        cube.init(gl, shader); // Associa o objeto à GPU e ao shader
 
        // Associa matriz, e define um nome referente a ele (dentro do shader)
        modelMatrix.init(gl, shader.getUniformLocation("u_modelMatrix"));
    }
    
    @Override // Chamado pelo animator
    public void display(GLAutoDrawable glad) {
        GL3 gl = glad.getGL().getGL3(); // Contexto de desenho
        
        // A cada atualização, limpa de acordo com a cor do buffer
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        
        modelMatrix.loadIdentity(); // Limpa matriz para matriz de identidade
        
        /* Se não utilizar o loadIdentity, a transformação acumulará a
         * cada display. (ex: comente a linha acima)*/
        modelMatrix.translate(0.2f, 0.1f, 0);
        modelMatrix.scale(0.5f, 0.5f, 0.5f);
        modelMatrix.rotate(30, 0, 0, 1);
        modelMatrix.rotate(60, 1, 0, 0);
        modelMatrix.rotate(45, 0, 1, 0);
        /* Processo acima, na ordem de execução: Ry, Rx, Rz, S, T */
         
        modelMatrix.bind(); // Associa matriz à placa
        cube.bind(); // Seleciona objeto a ser ativado
        cube.draw(); // Desenha objeto ativo (deve ser o mesmo)
        
        modelMatrix.loadIdentity(); // Limpa matriz para matriz de identidade
        modelMatrix.translate(-0.2f, -0.1f, 0);
        modelMatrix.scale(0.5f, 0.5f, 0.5f);
        modelMatrix.rotate(-30, 0, 0, 1);
        modelMatrix.rotate(-60, 1, 0, 0);
        modelMatrix.rotate(-45, 0, 1, 0);
            /* Processo acima, na ordem de execução: Ry, Rx, Rz, S, T */

            modelMatrix.bind(); // Associa matriz à placa
            cube.bind(); // Seleciona objeto a ser ativado
            cube.draw(); // Desenha objeto ativo (deve ser o mesmo)
        
        for (int i = 0; i < 5; i++) {
            modelMatrix.loadIdentity(); // Limpa matriz para matriz de identidade
            modelMatrix.translate(i/10, i/10, 0);
            modelMatrix.scale(0.5f/(i+1), 0.5f/(i+1), 0.5f/(i+1));
            modelMatrix.rotate(45, 0, 0, 1);
            modelMatrix.rotate(45, 1, 0, 0);
            modelMatrix.rotate(45, 0, 1, 0);
            /* Processo acima, na ordem de execução: Ry, Rx, Rz, S, T */

            modelMatrix.bind(); // Associa matriz à placa
            cube.bind(); // Seleciona objeto a ser ativado 
            cube.draw(); // Desenha objeto ativo (deve ser o mesmo)
        }
        
        
        if (delta > 0.4 || delta < -0.4) inc *= -1;
        delta += inc;
        modelMatrix.loadIdentity(); // Limpa matriz para matriz de identidade
        modelMatrix.translate(delta, 0.5f, 0.0f);
        modelMatrix.scale(0.5f, 0.5f, 0.5f);
        /* Processo acima, na ordem de execução: Ry, Rx, Rz, S, T */
         
        modelMatrix.bind(); // Associa matriz à placa
        cube.bind(); // Seleciona objeto a ser ativado
        cube.draw(); // Desenha objeto ativo (deve ser o mesmo)
    }
    
    @Override
    public void dispose(GLAutoDrawable glad) {
        
    }


    @Override
    public void reshape(GLAutoDrawable glad, int i, int i1, int i2, int i3) {}
}