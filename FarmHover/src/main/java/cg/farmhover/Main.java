package cg.farmhover;

import com.jogamp.opengl.util.AnimatorBase;
import com.jogamp.opengl.util.FPSAnimator;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;

public class Main {
    
    public static AnimatorBase animator;
    static boolean stillRunning;
    public static void main(String[] args) {
        // Get GL3 profile (to work with OpenGL 4.0)
        GLProfile profile = GLProfile.get(GLProfile.GL3);
        stillRunning = true;
        // Configurations
        GLCapabilities glcaps = new GLCapabilities(profile);
        glcaps.setDoubleBuffered(true);
        glcaps.setHardwareAccelerated(true);

        // Create canvas
        GLCanvas glCanvas = new GLCanvas(glcaps);

        // Add listener to panel
        TestScene listener = new TestScene();
        glCanvas.addGLEventListener(listener);

        Frame frame = new Frame("Test");
        frame.setSize(1024,768);
        frame.add(glCanvas);
        frame.addKeyListener(listener);
        Main.animator = new FPSAnimator(glCanvas, 60);
        final AudioPlayer player = new AudioPlayer ();
        final AudioPlayer player2 = new AudioPlayer ();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        stillRunning = false;
                        animator.stop();
                        System.exit(0);
                    }
                }).start();
            }
        });
//        new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        while(stillRunning){
////                            System.out.println("bla");
//                            player2.play(".\\audio\\moo.wav");
//                            if(Updater.playMoo){
////                                System.out.println("bli<<<<?<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
//                                player2.play(".\\audio\\moo.wav");
//                                Updater.playMoo = false;
//                            }
////                            System.out.println("blu");
//                        }
//                    }
//                }).start();
        frame.setVisible(true);
        animator.start();
        while(stillRunning){
            player.play(".\\audio\\farm.ogg");
        }
        
  }
}
