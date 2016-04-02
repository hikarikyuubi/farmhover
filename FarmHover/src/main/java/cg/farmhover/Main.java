/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cg.farmhover;

import com.jogamp.opengl.util.AnimatorBase;
import com.jogamp.opengl.util.FPSAnimator;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;

/**
 *
 * @author Hikari Kyuubi
 */
public class Main {
    
    public static void main(String args[]) {
        
        // Define qual versão da OpenGL que será usado
        GLProfile profile = GLProfile.get(GLProfile.GL3);
        
        // Estabelece configurações para opengl (por padrão já costuma vir assim)
        GLCapabilities cap = new GLCapabilities(profile);
        cap.setDoubleBuffered(true);
        cap.setHardwareAccelerated(true);
        
        // Criar Frame Buffer com dada configuração
        GLCanvas canvas = new GLCanvas(cap); 
        // Define quem desenhará no canvas
        canvas.addGLEventListener(new TestScene()); 
        // Tentará atualizar à taxa de 30 fps
        AnimatorBase animator = new FPSAnimator(canvas, 30);
        
        // Cria Janela
        JFrame frame = new JFrame();
        // Estabele relação entre canvas e janela (não fazer diretamente) 
        frame.getContentPane().add(canvas); 
        // Configura Janela
        frame.setSize(800,800); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        // Inicializa loop de animação
        animator.start();
    }
}
