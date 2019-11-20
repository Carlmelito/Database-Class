package library;
// Java program that creates the toast message 
//(which is a selectively translucent JWindow) 
import java.awt.*; 
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*; 
public class Toast extends JFrame { 
        
    public static final long DURATION_LONG = 2000;
    public static final long DURATION_MEDIUM = 1000;
    public static final long DURATION_SHORT = 500;

    public static void makeToast(Component parent, String message, long duration) {
        JWindow window = new JWindow();
        AffineTransform affinetransform = new AffineTransform();     
        FontRenderContext frc = new FontRenderContext(affinetransform,true,true);     
        Font font = new Font("Lucida Grande", Font.PLAIN, 13);
        int textwidth = (int)(font.getStringBounds(message, frc).getWidth());
        int textheight = (int)(font.getStringBounds(message, frc).getHeight());

        // make the background transparent 
        window.setBackground(new Color(0, 0, 0, 0));
        window.setOpacity(.75f);
        // create a panel
        JPanel panel = new JPanel() {
            public void paintComponent(Graphics g) {
                //get necessary size
                int wid = g.getFontMetrics().stringWidth(message);
                int hei = g.getFontMetrics().getHeight();

                // draw the boundary of the toast and fill it
                g.setColor(Color.black);
                g.fillRect(10, 10, wid + 30, hei + 10);
                g.setColor(Color.black);
                g.drawRect(10, 10, wid + 30, hei + 10);

                // set the color of text
                g.setColor(new Color(255, 255, 255, 240));
                g.drawString(message, 25, 27);
                int t = 250;
                // draw the shadow of the toast 
                for (int i = 0; i < 4; i++) { 
                        t -= 60; 
                        g.setColor(new Color(0, 0, 0, t)); 
                        g.drawRect(10 - i, 10 - i, wid + 30 + i * 2, 
                                        hei + 10 + i * 2); 
                }
            }
        };

        window.add(panel);
        window.setSize(textwidth+35, textheight+25);
        //get middle of page, lean left if neccesary
        int xLocation = (int) (parent.getX() + parent.getWidth() / 2);
        //move start location over by half the toast window so toast is in middle
        xLocation -= (textwidth / 2);
        //compensate for shadow

        //get bottom of page
        int yLocation = (int) (parent.getY() + parent.getHeight());
        //move up slightly
        yLocation -= 100;

        //set location
        window.setLocation(xLocation, yLocation);

        Thread thread = new Thread(() -> {
            window.setVisible(true);
            try {
                Thread.sleep(duration);
            } catch (InterruptedException ex) {
                Logger.getLogger(Toast.class.getName()).log(Level.SEVERE, null, ex);
            }
            for(float f = .75f; f > 0; f -= .08) {
                window.setOpacity(f);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Toast.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            window.dispose();
        });

        thread.start();
    }        
}

