// PlaySong.java
// Implements startPlay() method.
package mp3;

import java.io.*;
import java.awt.*;
import java.net.*;
import javax.media.*;

// ############################################################################
class PlaySong implements ControllerListener {

    Player2 plr;

    // Controls gain, position, start, stop
    Component controlComponent = null;
    
    PlaySong(Player2 plr) {
        this.plr = plr;
    }

// ****************************************************************************
    void startPlay() {

        // Prepare the string to make url.
        String mediaFile = "file:///"+ plr.curSng;
        // URL for our media file
        URL url = null;
        
        if (plr.player != null) {
            // Remove the control component of any previous decoder first.
            plr.remove(plr.player.getControlPanelComponent());
            plr.validate(); // Verify if player has been removed.
            plr.player.stop();  // Stop player from playing.
            plr.player.deallocate();    // Deallocate any system resources.
            plr.player.close(); // Close the Player
        }

        try {
            // Create an url from the file name
            if ((url = new URL(mediaFile)) == null) {
                System.err.println("What the hell");
                Fatal("Can't build URL for " + mediaFile);
            }
            
            // Create an instance of a player for this media
            // Using the url and the Manager class.
            if ((plr.player = Manager.createPlayer(url)) == null) {
                Fatal("Could not create player for " + url);
            }

            // Add ourselves as a listener for player's events
            plr.player.addControllerListener(this);
        }
        catch (IOException i) {
            Fatal("IO exception creating player for " + url);
        }
        catch (Exception e) {
            Fatal("Invalid media file URL!" + e);
        }

        // Call start() to prefetch and start the player.
        if (plr.player != null) {
            plr.player.start();
        }
    }

    /*
     * This controllerUpdate function must be defined in order
     * to implement a ControllerListener interface.  This
     * function will be called whenever there is a media event.
     */
    public synchronized void controllerUpdate(ControllerEvent event) {
        // If we're getting messages from a dead player, just leave
        if (plr.player == null) {
            return;
        }

        // When the player is Realized, get the control components and add it
        // to the 'body'.
        if (event instanceof RealizeCompleteEvent) {
            if ((controlComponent = plr.player.getControlPanelComponent()) != null) {
                // Set the size so as to fit the screen.
                controlComponent.setPreferredSize(new Dimension(400, 20));

                // Add at the bottom of screen.
                plr.add(controlComponent, "South");
            }
            // Force the application to draw the components
            plr.validate();
        }

        // We've reached the end of the media.
        else if (event instanceof EndOfMediaEvent) {
            if (plr.alFlag == true) {   // If auto loop has been enabled.
                // Rewind and start over
                plr.player.setMediaTime(new Time(0));
                plr.player.start();
            }
        }

        else if (event instanceof ControllerErrorEvent) {
            // Tell TypicalPlayerApplet.start() to call it a day

            plr.player = null;
            Fatal(((ControllerErrorEvent) event).getMessage());
        }
    }

    void Fatal(String s) {
        // Applications will make various choices about what
        // to do here.  We simply print a message and then exit.

        System.err.println("FATAL ERROR: " + s);
        System.exit(0);  // Exit from system.
    }
}


