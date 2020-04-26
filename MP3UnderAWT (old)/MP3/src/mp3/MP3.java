package mp3;

/**
 *
 * @author ravs
 */
public class MP3 {

    public static void main(String[] args) {
        Player plyr = new Player();    // Starts new instance of Player
        plyr.setSize(300, 300);
        // Better name?
        plyr.setTitle("Dream Player");
        plyr.setVisible(true);
    }
}

