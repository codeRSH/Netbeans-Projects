// ItemHandler.java
// Used for handling JCheckBoxMenuItems

package mp3;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// ############################################################################
class ItemHandler implements ItemListener {

    Player2 plr;

    ItemHandler(Player2 plr) {
        this.plr = plr;
    }

    public void itemStateChanged(ItemEvent ie) {
        // Get refrence to particular CheckBoxMenuItem that caused this event.
        JCheckBoxMenuItem jcmi = (JCheckBoxMenuItem) ie.getItem();

        // If 'System' MenuItem pressed
        if (jcmi.getText().equals("System") && (jcmi.getState() == true)) {
            plr.sdefault.setState(false);
            plr.system.setState(false);

            try {
                // Set "System" Look & Feel
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName());
                // Update immediately.
                SwingUtilities.updateComponentTreeUI(plr);
                plr.pack(); // Adjust according to new L & F.
            } catch (Exception e) {
                System.err.println("Some Error: " + e);
            }

        }

        else if (jcmi.getText().equals("Motif") && (jcmi.getState() == true)) {
            plr.sdefault.setState(false);
            plr.motif.setState(false);
            
            try {
                // Set cross-platform "Motif" L&F
                UIManager.setLookAndFeel(
                        "com.sun.java.swing.plaf.motif.MotifLookAndFeel");
                SwingUtilities.updateComponentTreeUI(plr);
                plr.pack();
            } catch (Exception e) {
                System.err.println("Some Error: " + e);
            }
        }

        else if (jcmi.getText().equals("Default")&&(jcmi.getState() == true)) {
            plr.motif.setState(false);
            plr.system.setState(false);

            try {
                // Set cross-platform Java L&F (also called "Metal")
                UIManager.setLookAndFeel(
                        UIManager.getCrossPlatformLookAndFeelClassName());
                SwingUtilities.updateComponentTreeUI(plr);
                plr.pack();
            } catch (Exception e) {
                System.err.println("Some Error: " + e);
            }
        }

        // If Auto Loop has been enabled.
        if (jcmi.getText().equals("AutoLoop")&&(jcmi.getState() == true)) {
            plr.alFlag = true;
        }

        else if (jcmi.getText().equals("AutoLoop")&&(jcmi.getState() == false)) {
            plr.alFlag = false;
        }
    }
}
