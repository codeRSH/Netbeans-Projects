// ActionHandler1.java
// Used for handling Player Buttons

package mp3;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

// ###########################################################################
class ActionHandler1 implements ActionListener {

    Player2 plr;
    PlaySong plSng;

    public ActionHandler1(Player2 plr) {
        // Pass reference of instance of player.
        // Now every component of Player will be referred with respect to 'plr'
        this.plr = plr;
    }

    public void actionPerformed(ActionEvent ae) {
        // Get the name of Button pressed
        String str = ae.getActionCommand();

// ***************************************************************************
        // If 'Open' button or 'Open.." Menu Item has been pressed.
        if (str.equals("Open..") || str.equals("Open")) {
            // Clear the playlist from any previous songs.
            plr.plList.removeAll();

            // Create a new FileDialog Box to select MP3 files to play.
            // Player('plr') is the parent of this Dialog Box
            // Name of DialogBox (shown in Title bar) is 'Open File'.
            FileDialog fd = new FileDialog(plr, "Open File", FileDialog.LOAD);
            fd.setVisible(true);

            // Get the name of the directory in which the user was..
            plr.dir = fd.getDirectory();

// NOTE:-
/*
 FileDialog is an AWT component. Consistency issue with Swing.
 FileDialog doesn't allow multiple selection of files.
 Either a single file can chosen and 'OK' pressed
 Or no file is chosen & 'Ok' pressed(current directory selected in that case.)
 Does nothing if 'Cancel' has been pressed.
*/
            // If a single file has been choosen by the user
            // and file name ends with 'mp3'.
            if ((fd.getFile() != null) && (fd.getFile().endsWith("mp3"))) {
                // 'str' is an array of Strings. Make it an array of one String.
                plr.str = new String[1];
                // Pass the chosen single file name to it.
                plr.str[0] = fd.getFile();

                // println has been used generously throughout the code
                // for debugging purpose. Can be removed safely if required.
                System.out.println("File");
            }

            // If a single file has not been choosen but the whole
            // directory has been choosen (NOT selected but entered into).
            else if (plr.dir != null) {
                // Encapsulate the directory chosen into 'fil'
                plr.fil = new File(plr.dir);

                // Create a FileNameFilter 'ext' that filters out 'mp3' files.
                // See the code of OnlyMP3 inner class
                // at the end of ActionHandler class.
                FilenameFilter ext = new OnlyMP3("mp3");

                // fil.list prepares an Array of Strings containing
                // the filename of mp3 files filtered out using 'ext'
                plr.str = plr.fil.list(ext);

                // Bubble Sort the String array.
                // Mysteriously not already in sorted order.
                for (int i = 1; i < plr.str.length; i++) {
                    for (int j = plr.str.length-1; j>=i; j--) {
                        if (plr.str[j-1].compareTo(plr.str[j]) > 0) {
                            String temp = plr.str[j-1];
                            plr.str[j-1] = plr.str[j];
                            plr.str[j] = temp;
                        }
                    }
                }

                System.out.println("Directory");
            }

            // If there is some mp3 file in the
            // files selected by the user above.
            if ((plr.str != null) && (plr.str.length != 0)) {
                // Current Song no. is 0 (for 1st song).
                plr.sngNo = 0;
                // Get the full address of current song using 'dir'
                plr.curSng = plr.dir + plr.str[plr.sngNo];

                // Loop through all the songs as stored in 'str'
                for (int i=0; i<plr.str.length; i++) {
                    // Print out their names in console.
                    System.out.println(plr.str[i]);
                    // Add all the songs stored in 'str' to plList
                    plr.plList.add(plr.str[i]);

                    // This is where having problem using Swing's JList. Check.
                    //plr.plist.add(plr.str[i], plr.str[i]);
                }
            }
        }

// ****************************************************************************
        else if (str.equals("About")) {
            About abt = new About(plr, "About"); // Start new About dialog box
            abt.setVisible(true);   // show About dialog box
        }

//****************************************************************************
        else if (str.equals("Stop")) {
            plr.stpFlg = true;  // 'Stop' button just pressed

            if (plr.player != null) {
                plr.player.stop();  // Stop the player.
                plr.player.deallocate();    // Deallocate any resources assigned.
            }

            // 'Remove' labels from screen.
            plr.display.setText("");
            plr.rateDisplay.setText("");
            plr.errDisplay.setText("");
        }

// ****************************************************************************
        // If 'Previous' Button has been pressed and there are any song to play.
        else if (str.equals("|<") && (plr.str != null)) {
            // If the currrent song is not the first song in Playlist.
            if ((plr.str.length != 0) && (plr.sngNo-1 >= 0)) {
                plr.sngNo--;    // sngNo now holds previous song's no.
                System.out.println(plr.sngNo);
                // Set the current Song with full path
                plr.curSng = plr.dir + plr.str[plr.sngNo];
                // Display the previous song as the current song
                plr.display.setText(plr.str[plr.sngNo]);
                // Fire up the decoder to play the current song.
                plSng = new PlaySong(plr);
                plSng.startPlay();
            }

        }

// ****************************************************************************
        else if (str.equals(">|") && (plr.str != null)) {
            // If the currrent song is not the last song in Playlist.
            if ((plr.str.length != 0) && (plr.sngNo+1 < plr.str.length)) {
                plr.sngNo++;    // sngNo now holds previous song's no.
                plr.curSng = plr.dir + plr.str[plr.sngNo];
                plr.display.setText(plr.str[plr.sngNo]);

                plSng = new PlaySong(plr);
                plSng.startPlay();
            }

        }

// ****************************************************************************
        // If Forward has been pressed and there is some song playing.
        else if (str.equals(">>>") && (plr.str != null) && (plr.player != null)) {
            try {
                // Get current rate of playing song
                float rate = plr.player.getRate();

                if (rate <= 2.0) { // If rate isn't more than 2.0
                    if (rate < 1)  // If rate below normal.
                        rate = 1; // Get current rate to normal value
                    else
                        rate = rate * 2;  // Half the current rate.

                    plr.player.setRate(rate);   // Set the new rate.
                    
                    // Display the surrent (new) play rate on screen.
                    plr.rateDisplay.setText("  PlayRate: " + plr.player.getRate());
                }
            }
            catch(Exception e) {
              plr.errDisplay.setText("Error!! '>>>'");
            }
        }

// ****************************************************************************
        else if (str.equals("<<<") && (plr.str != null) && (plr.player != null)) {
            try {
                float rate = plr.player.getRate();

                if (rate >= 0.50) { // If rate isn't less than one-fourth
                    if (rate <= 1)  // If rate below normal.
                        rate = rate-(float)0.1; // Decrease play rate by 0.1
                    else
                        rate = rate/2;  // Half the current rate.
                    
                    plr.player.setRate(rate); 
                    plr.rateDisplay.setText("  PlayRate: " + plr.player.getRate());
                }
            }
            catch(Exception e) {
              plr.errDisplay.setText("Error!! '<<'");
            }
        }
    }



// ###########################################################################
    public class OnlyMP3 implements FilenameFilter {
        String ext;

        OnlyMP3(String ext) {
            this.ext = ext;     // 'ext' holds "mp3"
        }

        // 'accept' method checks which files specified in 'dir' end with 'mp3'
        public boolean accept(File dir, String name) {
            return name.endsWith(ext);
        }
    }

}