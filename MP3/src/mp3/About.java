//  About.java
// For About dialog box

package mp3;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class About extends Dialog implements ActionListener {

    JLabel []l = new JLabel[7];

    public About(Frame parent, String title) {
        // Make the about dialog box 'modal' (true).
        super(parent, title, true);
        setBackground(new Color(150, 150, 150));
        setForeground(Color.green);
        setLayout(new FlowLayout());
        setSize(230, 230);

        l[0] = new JLabel("          RudMP3 v0.4.14 b (Lily)      ");
        l[1] = new JLabel("                                 ");
        l[2] = new JLabel("             Authors:            ");
        l[3] = new JLabel("Prashant Khajotia     0081152707");
        l[4] = new JLabel("Skand Gupt            0131152707");
        l[5] = new JLabel("Ravi Singh            0161152707");
        l[6] = new JLabel("Saurabh Jain          0281152707");

        l[0].setForeground(Color.GREEN);
        l[2].setForeground(Color.DARK_GRAY);
        l[4].setToolTipText("Skand = Zhand!!");
                
        for (int i=0; i<7; i++) {
            add(l[i]);
            if (i>=3 && i<=6)
                l[i].setForeground(Color.white);
        }

        JButton ok = new JButton("OK");
        add(ok);
        ok.addActionListener(this);
        
        addWindowListener(new AbtAdapt(this));
    }

    public void actionPerformed(ActionEvent ae) {
        dispose();  // Dispose (end) the Dialog box.
    }
}

class AbtAdapt extends WindowAdapter {

    About abt;

    AbtAdapt(About abt) {
        this.abt = abt;
    }

    public void windowClosing (WindowEvent we) {
        abt.setVisible(false);
    }
}

