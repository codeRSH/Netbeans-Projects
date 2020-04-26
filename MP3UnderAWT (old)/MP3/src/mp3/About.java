//  About.java
package mp3;

import java.awt.*;
import java.awt.event.*;

public class About extends Dialog implements ActionListener {

    public About(Frame parent, String title) {
        super(parent, title, true);
        setBackground(Color.blue);
        setForeground(Color.red);
        setLayout(new GridLayout(3, 4));
        setSize(300, 200);

        Label l1 = new Label("Authors:  ");
        Label l2 = new Label("RavS  ");
        Label l3 = new Label();
        Label l4, l5, l6, l7, l8, l9, l10, l11;
        l4 = l5 = l6 = l7 = l8 = l9 = l10 = l11 = l3;
        Button ok = new Button("OK");
        // Labels added to position the button!!
        add(l1);
        add(l2);
        add(l3);
        add(l4);
        add(l5);
        add(l6);
        add(l7);
        add(l8);
        add(l9);
        add(l10);
        add(l11);

        add(ok);
        ok.addActionListener(this);
        addWindowListener(new AbtAdapt(this));
    }

    public void actionPerformed(ActionEvent ae) {
        dispose();
    }
}

class AbtAdapt extends WindowAdapter {

    About abt;

    AbtAdapt(About abt) {
        this.abt = abt;
    }

    public void windowClosing(WindowEvent we) {
        abt.setVisible(false);
    }
}

