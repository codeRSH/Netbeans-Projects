package mp3;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.media.*;

public class Player2 extends JFrame  {

    // media player
    Player player = null;
    
    JButton stop, open, prev, next, forw, revw;  // Player JButtons
    JButton b[] = new JButton[7];   // To get reference for above Buttons
    JLabel display, errDisplay, rateDisplay;  // For display purposes
    JMenuItem about, miOpen;   // Menu Items
    JMenu skColor;      // Skin Sub Menu.
    JCheckBoxMenuItem system, motif, sdefault;   // Different skin items.
    JCheckBoxMenuItem autoLoop;   // To on/off autoloop
    JList plist;    // Intended Swing Playlist
    JScrollPane scrlp;  // Scrollpane Used with the playlist
    JPanel panel1;   // Panel to add buttons
    JPanel panel2;   // Panel to add labels
    JPanel panel;   // Panel to above 2 panels, craeting a hierarchy of containers.

    List plList;    // Used AWT Playlist

    // To change the background and foreground colors of different
    // components in Player's GUI.
    Color bclr, fclr;
    
    File fil;   // To encapsulate the file representing the MP3 files directory
    String dir; // To remember the name of directory as selected by the user.
    // Holds the complete name of current song with complete path.
    String curSng;
    String str[];   // To hold the MP3 file names shown in playlist.
    int sngNo;  // To remember the currently playing song number in playlist.
    // To check that autoLoop checkbox or stop buttons has been pressed or not.
    boolean alFlag, stpFlg;

    public Player2() {

        // Initialise various items..

        str = null;
        // Default Song when Player starts. Loads from current location of file.
        //curSng = Player2.class.getResource("1.mp3").toString();
        curSng = null;
        alFlag = stpFlg = false;  // AutoLoop and Stop is initially not pressed.
        bclr = null;    // Background color default initially.
        fclr = null;    // Foreground color default initially.
        
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
       // scrlp.setVisible(false);


        // Set the Layout of Player (JFrame) to BorderLayout
        setLayout(new BorderLayout());
        // Player background doen't change somehow.
        // But change of color (greyish) reflects in 'Open' File Dialog box.
        // But not under Windows OS.
        setBackground(new Color(200, 200, 200));

        // JMenubar and its items
        JMenuBar mb1 = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenu view = new JMenu("View");
        JMenu help = new JMenu("Help");

        // Set Shortcut keys to access menu items.
        file.setMnemonic(KeyEvent.VK_F);
        view.setMnemonic(KeyEvent.VK_V);
        help.setMnemonic(KeyEvent.VK_H);
        
        miOpen = new JMenuItem("Open..");
        skColor = new JMenu("Skin");
        about = new JMenuItem("About");
        
        miOpen.setMnemonic(KeyEvent.VK_P);
        skColor.setMnemonic(KeyEvent.VK_C);
        about.setMnemonic(KeyEvent.VK_A);

        // Add CheckBoxMenuItems to change skin of components.
        // 'Default' checked initially.
        sdefault = new JCheckBoxMenuItem("Default", true);
        system = new JCheckBoxMenuItem("System", false);
        motif = new JCheckBoxMenuItem("Motif", false);
        autoLoop = new JCheckBoxMenuItem("AutoLoop", false);

        // Add Menu Items to 'Skin' Menu
        skColor.add(sdefault);
        skColor.add(system);
        skColor.add(motif);

        // Add Menu Items and Submenus to their respective Menus.
        file.add(miOpen);
        view.add(skColor);  // Submenu
        view.add(autoLoop);
        help.add(about);

        // Add Menus to MenuBar
        mb1.add(file);
        mb1.add(view);
        mb1.add(help);

        setJMenuBar(mb1);    // set Menu Bar on Player

        panel1 = new JPanel();
        // Create and add Player's Button.
        // b[i] holds reference of all buttons
        panel1.add(b[0] = open = new JButton("Open"));
        panel1.add(b[1] = prev = new JButton("|<"));
        panel1.add(b[2] = revw = new JButton("<<<"));
       // panel.add(b[3] = plps = new JButton("Play "));
        panel1.add(b[4] = stop = new JButton("Stop"));
        panel1.add(b[5] = forw = new JButton(">>>"));
        panel1.add(b[6] = next = new JButton(">|"));

        // Make the 'Open' button a bit larger than its normal size.
        open.setPreferredSize(new Dimension(80, 40));
        open.setForeground(Color.RED);  // You see 'Open' in red color.

        // SetMnemonics so as to access the Buttons through Alt+<key> shortcut
        open.setMnemonic(KeyEvent.VK_O);
        prev.setMnemonic(KeyEvent.VK_P);
        stop.setMnemonic(KeyEvent.VK_S);
        next.setMnemonic(KeyEvent.VK_N);
        
        // Create and add Labels to panel..
        // Used to show various information including errors if any.
        panel2 = new JPanel();
        panel2.add(display = new JLabel("                             "));
        panel2.add(errDisplay = new JLabel());
        panel2.add(rateDisplay = new JLabel());

        // Parent panel to hold sub panels.
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(panel1, "North");
        panel.add(panel2, "South");
        
        // Add ScrollPanes in Center of player.
        add(scrlp, "Center");

        // Add the panel to the contentPane of main JFrame of application
        add( panel, "North");

        // Sets the tool tips. Will show up when mouse over any button.
        open.setToolTipText("Open the file selection dialog box");
        prev.setToolTipText("Play previous song in playlist");
        revw.setToolTipText("Decrease Play Rate");
        //plps.setToolTipText("Play/Pause the current song");
        stop.setToolTipText("Stops the current song");
        forw.setToolTipText("Increase PlayRate");
        next.setToolTipText("Play next song in playlist");

        // Set the fore color of displays. Better Colors?
        display.setForeground(new Color(150, 50, 250));
        errDisplay.setForeground(new Color(250, 25, 125));
        rateDisplay.setForeground(Color.ORANGE);

        //  Action Event Handler for all JButtons
        // 'this' passes the reference of Player to ActionHandler1 class.
        ActionHandler1 actnHndlr = new ActionHandler1(this);
        about.addActionListener(actnHndlr);
        miOpen.addActionListener(actnHndlr);
//        plps.addActionListener(actnHndlr);
        stop.addActionListener(actnHndlr);
        open.addActionListener(actnHndlr);
        prev.addActionListener(actnHndlr);
        next.addActionListener(actnHndlr);
        forw.addActionListener(actnHndlr);
        revw.addActionListener(actnHndlr);

        // Seperate Action Event Handler for playlist (List).
        // 'this' used to access all the components (eg. Buttons) of Player.
        ActionHandler2 listActnHndlr = new ActionHandler2(this);
        plList.addActionListener(listActnHndlr);

        // Item Event Handler for CheckBox Menu Item.
        ItemHandler itmHndlr = new ItemHandler(this);
        sdefault.addItemListener(itmHndlr);
        system.addItemListener(itmHndlr);
        motif.addItemListener(itmHndlr);
        autoLoop.addItemListener(itmHndlr);     
     
        //  Window Event handler for closing the Player.
        MyWinAdapt mAdapt = new MyWinAdapt(this);
        addWindowListener(mAdapt);
    }
}
