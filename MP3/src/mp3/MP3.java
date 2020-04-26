// MP3.java
// Main file. Starts the player

package mp3;

import javax.swing.*;
import java.awt.*;

public class MP3 {

    public static void main(String[] args) {

        SwingUtilities.invokeLater (new Runnable() {
            public void run() {
                
                // Comment the code below to run in NetBeans..
                final SplashScreen splash = SplashScreen.getSplashScreen();
                if (splash == null) {   // If SplashScreen not found
                    System.out.println("SplashScreen.getSplashScreen() returned null");
                    return;
                }

                try {
                    // Wait 5 secs to display SplashScreen properly.
                    Thread.sleep(5000);
                } catch (Exception e) {
                    System.err.println("OOPS!! Some Error");
                }

                splash.close(); // comment till here.
                
                Player2 plyr = new Player2();    // Starts new instance of Player
                plyr.setSize(450, 350); // Set size of the player.
                // Better name?
                plyr.setTitle("RudMP3 Player");
                plyr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                plyr.setVisible(true);
            }
        });
    }
}

