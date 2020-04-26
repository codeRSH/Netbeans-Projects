// MyWinAdapt.java
// Used for handling WindowEvents on the Player.

package mp3;

import java.awt.event.*;

// ###########################################################################
class MyWinAdapt extends WindowAdapter {
    Player2 plr;

    public MyWinAdapt(Player2 plr) {
        this.plr = plr;
    }

    public void windowClosing (WindowEvent we) {
        // First close the player, if its working.
        if (plr.player != null) {
            plr.remove(plr.player.getControlPanelComponent());
            plr.validate();
            plr.player.stop();
            plr.player.deallocate();
            plr.player.close();
        }

        System.exit(0);     // Close the application all together
    }
}