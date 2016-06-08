package cg.farmhover;

import com.jogamp.opengl.util.AnimatorBase;
import com.jogamp.opengl.util.FPSAnimator;
import java.applet.AudioClip;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;
import javax.swing.JTextField;



public class Main {
    public static AnimatorBase animator;
    static boolean stillRunning;
    static AudioClip music;
    static public volatile JFrame frame;
    
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

        frame = new JFrame("FARM HOVER");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        frame.setSize(1024,768);
        frame.add(glCanvas);
        glCanvas.addKeyListener(listener);
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
//        JPanel p = new JPanel(new GridLayout(1,1));
//        frame.add(p);
//        try {
//            music = Applet.newAudioClip(new File(".\\audio\\moo.wav").toURL());
//        } catch (MalformedURLException ex) {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//        }
        new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while(stillRunning){
//                            System.out.println("bla");
                            if(Updater.playMoo){
//                                music.play();
                                player.play(".\\audio\\moo4.wav");
                                Updater.playMoo = false;
                            }else if(Updater.playHit){
                                player.play(".\\audio\\thump2.wav");
                                Updater.playHit = false;
                            } else{
//                                player.play(".\\audio\\ufo_loop2.wav");
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                              
//                            System.out.println("blu");
                        }
                    }
                }).start();
        
        frame.setVisible(true);
        animator.start();
        while(stillRunning){
            player.play(".\\audio\\farm2.wav");
        }
        
  }
}
