// NewThread.java
// Used to create new threads for displaying dynamic content.

package mp3;

// ############################################################################
class NewThread implements Runnable {

    MPlayer plr;

    NewThread(MPlayer plr) {
        this.plr = plr;     // Get reference Player

        plr.stpFlg = false;
        // Create new thread 'Play Thread'
        plr.thrd = new Thread(this, "Play Thread");
       // System.out.println(t);  // Print name of thread in console.
        plr.thrd.start();  // Start the new thread.
    }

    public void run() {
        try {
            // Infinite loop.
            while (true) {
                if (plr.stpFlg) {   // If Stop flag is on.
                    break;  // break out of loop.
                }

                // Keep reading the messages passed by decoder into 'read'
                // if message doesn't starts with '@P'
                // (which means player has been paused)
                if (!(plr.read = plr.bufr.readLine()).startsWith("@P")) {
                    // Print the message
                    System.out.println(plr.read);
                    plr.jta.append(plr.read + "\n");

                    if (plr.read.startsWith("@F")) {
                        // Code below uses the message starting with '@F'
                        // to print the Time elapsed and left in current song.

                        // Get last occurence of <space>.
                        int lio = plr.read.lastIndexOf(' ');

                        String tmr = plr.read.substring(lio + 1);
                        String tmp = plr.read.substring(0, lio);
                        lio = tmp.lastIndexOf(' ');
                        String tms = tmp.substring(lio + 1);
                        //System.out.println("out " + tmp);

                        int secr = (int) Double.parseDouble(tmr);
                        int secs = (int) Double.parseDouble(tms);
                        plr.display1.setText((secs/60) + ":" + (secs % 60) +
                                " / " +(secr/60) + ":" + (secr%60));
                    }
                }
                System.out.println(plr.read);
            }
        }
        catch(NumberFormatException nfe) {
            System.out.println("Erorr in parsing");
            plr.display1.setText("Error in parsing");
        }
        catch (Exception e) {
            System.out.println("Error " + e);
        }
    }
}