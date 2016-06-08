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
    static public volatile JTextField text;
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
        frame.setUndecorated(true);
        frame.setSize(1024,768);
        frame.add(glCanvas);
//        text = new JTextField("Hello World!");
//        text.setText("bla");
        text = new javax.swing.JTextField();
        text.setEditable(false);
        text.setBackground(new java.awt.Color(37,63,112));
        text.setFont(new java.awt.Font("Segoe UI Emoji", 1, 36)); // NOI18N
        text.setForeground(new java.awt.Color(123, 123, 123));
        text.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        text.setText("0");
        text.setAutoscrolls(false);
        text.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        text.setMargin(new java.awt.Insets(2, 2, 2, 20));
        frame.add(text);
        frame.setBackground(java.awt.Color.black);
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(frame.getContentPane());
        frame.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(text, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(text, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                )
        );

        
        
        glCanvas.setBounds(0, 60, 1950, 1020);
        glCanvas.addKeyListener(listener);
        frame.pack();
        //frame.addKeyListener(listener);
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
                            } else {
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
            player.play(".\\audio\\farm.ogg");
        }
        
  }
}
