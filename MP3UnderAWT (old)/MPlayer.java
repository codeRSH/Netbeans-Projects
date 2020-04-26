// MPlayer.java
// Main File for Player

package mp3;

import java.io.*;
import java.awt.*;
import javax.swing.*;

public class MPlayer extends JFrame {

    JButton plps, stop, open, prev, next, forw, revw;  // Player JButtons
    JButton b[] = new JButton[7];   // To get reference for above Buttons
    JLabel display, display1;  // For display purpose, 'display1' for errors.
    JMenuItem about, miOpen;   // Menu Items
    JMenu skColor;      // Color Sub Menu.
    JCheckBoxMenuItem sred, sgreen, sdefault;   // Different color items.
    JCheckBoxMenuItem visual;   // To on/off details mode.
    JTextArea jta;  // TextArea to show details about currently playing song.

    JList plist;    // Intended Swing Playlist
    JScrollPane scrlp;  // Scrollpane Used with the playlist
    JScrollPane tasp;   // ScrollPane used with TextArea.
    List plList;    // Used AWT Playlist

    // To change the background and froeground colors of different
    // components in Player's GUI.
    Color bclr, fclr;
    
    Runtime r;  // To get reference of JVM
    Process p;  // To control the process of command line decoder player
    Thread thrd;
  //  ProcessBuilder proc;  // Purpose similar to Process p;

    File fil;   // To encapsulate the file representing the MP3 files directory

    // Used to connect to decoder's output stream
    BufferedReader bufr;
    // Used to connect to decoder's input stream (for giving commands)
    BufferedWriter bufw;
    // Used to connect to decoder's error stream, but hasn't actually been used.
    BufferedInputStream bers;

    String dir; // To remember the name of directory as selected by the user.

    // Holds the complete name of current song with complete path.
    String curSng;
    // To hold the message passed by the decoder through BufferedReader
    String read;
    // To hold the command passed to the decoder throgh BufferdWriter.
    String ctlstr;
    
    String str[];   // To hold the MP3 file names shown in playlist.
    int sngNo;  // To remember the currently playing song number in playlist.

    // To check that pause/stop buttons have been pressed or not.
    boolean pausFlg, stpFlg;
    
    public MPlayer() {

        r = Runtime.getRuntime();   // Obtain Java Runtime
        p = null;   // Decoder process hasn't yet started.
        str = null;
        curSng = null;  // No current Song when Player starts.
        pausFlg = stpFlg = false;    // Pause button is initially not pressed.
        bclr = null;    // Background color default initially.
        fclr = null;    // Foreground color default initially.
        
        // The below code is to check out various fonts available on any machine.
/*
        GraphicsEnvironment ge =
                GraphicsEnvironment.getLocalGraphicsEnvironment();
        String fontList[] = ge.getAvailableFontFamilyNames();

        // Print out font names in console.
        for (int i=0; i<fontList.length; i++)
            System.out.println(fontList[i]);
 */

        
        // Having problems adding or removing song names (strings)
        // when using JList as playlist.
//        plist = new JList();
//        plist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // So AWT List has been used to reprsent Playlist.
        // Interface not consistent. JList preferable.

        // Create new list with 5 visible rows and
        // multiple selection not allowed (false)
        plList = new List(5, false);
        // Better font and colors?
        plList.setFont(new Font(Font.SANS_SERIF, Font.ITALIC|Font.BOLD, 12));
        plList.setForeground(new Color(50, 150, 150));
        plList.setBackground(Color.DARK_GRAY);

        // Create a new ScrollPane with Playilist inside it.
        scrlp = new JScrollPane(plList);
        // Set the size of ScrollPane to remain constant.
        scrlp.setPreferredSize(new Dimension(400, 100));
        // Scrollpane not visible till songs added to Playlist.
        scrlp.setVisible(false);

        // TextArea Not working as thought but should after some research.
        jta = new JTextArea(5, 1);  // Create TextArea with 5 ros and 1 column
        jta.setForeground(new Color(150, 50, 50));
        jta.setBackground(Color.GRAY);   // Better Colors?
        jta.setEditable(false);
        
        // Create a new ScrollPane with Playilist inside it.
        tasp = new JScrollPane(jta);
        // Set the size of ScrollPane to remain constant.
        tasp.setPreferredSize(new Dimension(400, 100));
        // Scrollpane not visible till user presses the 'visual' button
        tasp.setVisible(false);
        tasp.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Set the Layout of Player (JFrame) to FlowLayout.
        // Default (BorderLayout) not working properly. Check.
        setLayout(new FlowLayout());
        // Player background doen't change somehow.
        // But change of color (greyish) reflects in 'Open' File Dialog box.
        setBackground(new Color(200, 200, 200));
        
        // JMenubar and its items
        JMenuBar mb1 = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenu view = new JMenu("View");
        JMenu help = new JMenu("Help");
        
        miOpen = new JMenuItem("Open..");
        skColor = new JMenu("Skin");
        about = new JMenuItem("About");

        // Add CheckBoxMenuItems to change color of components.
        // 'Default' checked initially.
        sdefault = new JCheckBoxMenuItem("Default", true);
        sred = new JCheckBoxMenuItem("Red", false);
        sgreen = new JCheckBoxMenuItem("Green", false);
        visual = new JCheckBoxMenuItem("Verbose", false);
        
        // Add Color Menu Items to 'Skin' Menu
        skColor.add(sdefault);
        skColor.add(sred);
        skColor.add(sgreen);

        // Add Menu Items and Submenus to their respective Menus.
        file.add(miOpen);
        view.add(skColor);  // Submenu
        view.add(visual);
        help.add(about);

        // Add Menus to MenuBar
        mb1.add(file);
        mb1.add(view);
        mb1.add(help);
        
        setJMenuBar(mb1);    // set Menu Bar on Player

        // Create and add Player's Button.
        // b[i] holds reference of all buttons
        // so as to change their color simultaneously.
        add(b[0] = open = new JButton("Open"));
        add(b[1] = prev = new JButton("|<"));
        add(b[2] = revw = new JButton("<<"));
        add(b[3] = plps = new JButton("Play "));
        add(b[4] = stop = new JButton("Stop"));
        add(b[5] = forw = new JButton(">>"));
        add(b[6] = next = new JButton(">|"));

        // Create and add Labels to player.
        // Used to show various information including errors if any.
        add(display = new JLabel("                              "));        
        add(display1 = new JLabel());

        // Add ScrollPanes but not visible initially.
        add(scrlp);
        add(tasp);

        // Sets the tool tips. Will show up when mouse over any button.
        open.setToolTipText("Open the file selection dialog box");
        prev.setToolTipText("Play previous song in playlist");
        revw.setToolTipText("Rewind");
        plps.setToolTipText("Play/Pause the current song");
        stop.setToolTipText("Stops the current song");
        forw.setToolTipText("Skip Forward");
        next.setToolTipText("Play next song in playlist");

        // Set the fore color of displays. Better Colors?
        display.setForeground(new Color(150, 50, 250));
        display1.setForeground(new Color(250, 25, 125));
        
        //  Action Event Handler for all JButtons
        // 'this' passes the reference of Player to ActionHandler1 class.
        ActionHandler1 actnHndlr = new ActionHandler1(this);
        about.addActionListener(actnHndlr);
        miOpen.addActionListener(actnHndlr);
        plps.addActionListener(actnHndlr);
        stop.addActionListener(actnHndlr);
        open.addActionListener(actnHndlr);
        prev.addActionListener(actnHndlr);
        next.addActionListener(actnHndlr);
        // Causing problem so got of action for now.
        //forw.addActionListener(actnHndlr);
        //revw.addActionListener(actnHndlr);

        // Seperate Action Event Handler for playlist (List).
        // 'this' used to access all the components (eg. Buttons) of Player.
        ActionHandler2 listActnHndlr = new ActionHandler2(this);
        plList.addActionListener(listActnHndlr);

        // Item Event Handler for CheckBox Menu Item.
        ItemHandler itmHndlr = new ItemHandler(this);
        sdefault.addItemListener(itmHndlr);
        sred.addItemListener(itmHndlr);
        sgreen.addItemListener(itmHndlr);
        visual.addItemListener(itmHndlr);

        //  Window Event handler for closing the Player.
        MyWinAdapt mAdapt = new MyWinAdapt(this);
        addWindowListener(mAdapt);
    }
}

