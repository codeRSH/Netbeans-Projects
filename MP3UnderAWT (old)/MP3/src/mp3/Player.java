// Player.java
// Main File for Player

package mp3;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
//import java.util.Random;

public class Player extends Frame {

    Button play, stop, open, prev, next;  // Player buttons
   // Label bl1;  // Just for display purpose
    TextField display, display1;    //  Display Screen
    MenuItem about, miOpen; //  For about dialog box
    Runtime r;
    Process p;
    ProcessBuilder proc;
    String dir, str2;
    String str[];
    File fil;
    int sngNo;
    
    public Player() {

        r = Runtime.getRuntime();
        p = null;
        str = null;
        str2 = "";
        
        setLayout(new FlowLayout());
        
        // Menubar and its items
        MenuBar mb1 = new MenuBar();
        Menu file = new Menu("File");
        Menu view = new Menu("View");
        Menu help = new Menu("Help");
        miOpen = new MenuItem("Open..");
        about = new MenuItem("About");

        // Add Menu Items
        file.add(miOpen);
      //  view.add(std);
      //  view.add(sci);
        help.add(about);

        // Add Menus
        mb1.add(file);
        mb1.add(view);
        mb1.add(help);
        setMenuBar(mb1);    // set Menu Bar

        add(open = new Button("Open"));
        add(prev = new Button("<<"));
        add(play = new Button("Play"));
        add(stop = new Button("Stop"));
        add(next = new Button(">>"));
        add(display = new TextField("", 30));
        add(display1 = new TextField("Welcome!!"));

        display.setEditable(false);
        display1.setEditable(false);

        //  Action Event Handler for all buttons and menu items
        ActionHandler1 actnHndlr = new ActionHandler1(this);
        about.addActionListener(actnHndlr);
        miOpen.addActionListener(actnHndlr);
        play.addActionListener(actnHndlr);
        stop.addActionListener(actnHndlr);
        open.addActionListener(actnHndlr);
        prev.addActionListener(actnHndlr);
        next.addActionListener(actnHndlr);

        //  Window Event handler for closing the calculator
        MyWinAdapt mAdapt = new MyWinAdapt(this);
        addWindowListener(mAdapt);
    }
}

class ActionHandler1 implements ActionListener {

    Player plr;

    public ActionHandler1(Player plr) {
        this.plr = plr;   // pass reference of instance of player
    }

    public void actionPerformed(ActionEvent ae) {
        String str = ae.getActionCommand();

        if (str.equals("Open..") || str.equals("Open")) {
            FileDialog fd = new FileDialog(plr, "Open File");
            fd.setVisible(true);

            plr.dir = fd.getDirectory();
            
            if (fd.getFile() != null) {
                plr.str2 = plr.dir + fd.getFile();
                plr.display.setText(fd.getFile());
                System.out.println("File");
            }

            else if(plr.dir != null) {
                plr.fil = new File(plr.dir);
                FilenameFilter ext = new OnlyMP3("mp3");
                plr.str = plr.fil.list(ext);

                if (plr.str.length != 0) {
                    plr.sngNo = 0;
                    plr.str2 = plr.dir + plr.str[plr.sngNo];
                    plr.display.setText(plr.str[plr.sngNo]);

                    for (int i=0; i<plr.str.length; i++) {
                        System.out.println(plr.str[i]);
                    }
                }
                System.out.println("Directory");
            }
        }

        else if (str.equals("About")) {
            About abt = new About(plr, "About"); // Start new About dialog box
            abt.setVisible(true);   // show About dialog box
        }

        else if (str.equals("Play")) {
            try {
/*
        //      This code also does the same job perfectly as below
                plr.proc = new ProcessBuilder("/usr/bin/mpg123", plr.str2);
                plr.proc.start();
                System.out.println(plr.str2);
*/
                String stri[] = {"/usr/bin/mpg321", plr.str2};
                plr.p = plr.r.exec(stri);
                System.out.println(stri[1]);
            }
            
            catch (Exception e) {
                plr.display1.setText("Error executing the player!");
            }
        }

        else if (str.equals("Stop")) {
            if (plr.p != null) {
                plr.p.destroy();    // Close the decoder player
            }
        }

        else if (str.equals("<<") && (plr.str != null)) {
            if ((plr.str.length != 0) && (plr.sngNo-1 >= 0)) {
                plr.sngNo--;
                System.out.println(plr.sngNo);
                plr.str2 = plr.dir + plr.str[plr.sngNo];
                plr.display.setText(plr.str[plr.sngNo]);

                if (plr.p != null) {
                   plr.p.destroy();
                }

                try {
                    String stri[] = {"/usr/bin/mpg123", plr.str2};
                    plr.p = plr.r.exec(stri);
                    System.out.println(stri[1]);
                }
                catch (Exception e) {
                    plr.display1.setText("Error executing the player!");
                }
            }

        }

        else if (str.equals(">>") && (plr.str != null)) {
            if ((plr.str.length != 0) && (plr.sngNo+1 < plr.str.length)) {
                plr.sngNo++;
                plr.str2 = plr.dir + plr.str[plr.sngNo];
                plr.display.setText(plr.str[plr.sngNo]);

                if (plr.p != null) {
                   plr.p.destroy();
                }

                try {
                    String stri[] = {"/usr/bin/mpg123", plr.str2};
                    plr.p = plr.r.exec(stri);
                    System.out.println(stri[1]);
                }
                catch (Exception e) {
                    plr.display1.setText("Error executing the player!");
                }
            }
        }
    }

    public class OnlyMP3 implements FilenameFilter {
        String ext;

        OnlyMP3(String ext) {
            this.ext = ext;
        }

        public boolean accept(File dir, String name) {
            return name.endsWith(ext);
        }
    }
}

class ItemHandler1 implements ItemListener {

    Player plr;

    public ItemHandler1(Player plr) {
        this.plr = plr;
    }

    public void itemStateChanged(ItemEvent ae) {

    }
}

class MyWinAdapt extends WindowAdapter {
    Player plr;

    public MyWinAdapt(Player plr) {
        this.plr = plr;   
    }

    public void windowClosing (WindowEvent we) {
        if (plr.p != null) {
            plr.p.destroy();    // Close the decoder player
        }
        System.exit(0);     // Close the application all together
    }
}
