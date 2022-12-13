package VideoOutput;

import main.run;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Display extends JFrame{

//    Constants
    final int X_OFFSET = Integer.parseInt(run.allSettings.getProperty("Horizontal_Offset"));
    final int Y_OFFSET = Integer.parseInt(run.allSettings.getProperty("Vertical_Offset"));
    final int PIXELS_WIDE = Integer.parseInt(run.allSettings.getProperty("Panel_pixel_width"));
    final int PIXELS_TALL = Integer.parseInt(run.allSettings.getProperty("Panel_pixel_height"));;

//    Variables
    final int SCREEN_NUMBER;
    int screenWidth;
    int screenHeight;
    List<Segment> segments;
    int totalSegments;

    /**
     * Constructor
     * @param displayNum Display number
     */
    public Display(int displayNum){
        this.SCREEN_NUMBER = displayNum - 1;
    }


    /**
     * Makes the JFrame and sets it to black
     */
    public void initialiseScreen(){


        setTitle("VideoOut");
        int screenNum = this.SCREEN_NUMBER;

        GraphicsEnvironment graphicsEnviroment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] graphicsDevice = graphicsEnviroment.getScreenDevices();

        this.screenWidth = graphicsDevice[screenNum].getDefaultConfiguration().getBounds().width;
        this.screenHeight = graphicsDevice[screenNum].getDefaultConfiguration().getBounds().height;

//        Makes a JFrame on the Display specified in setting.properties and
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.getContentPane().setBackground(Color.BLACK);
        this.setUndecorated(true);
        if((screenNum > -1) && (screenNum < graphicsDevice.length)) {

//            Sets width and height of the "video-wall area" on the 1080 output
            this.setLocation(
                    ((this.screenWidth / 2) - (this.getSize().width / 2)) + graphicsDevice[screenNum].getDefaultConfiguration().getBounds().x,
                    ((this.screenHeight / 2) - (this.getSize().height / 2)) + graphicsDevice[screenNum].getDefaultConfiguration().getBounds().y
            );
            this.setVisible(true);
        } else {
            throw new RuntimeException( "Screen " + screenNum +" not found");
        }
    }

    @Override
    public void paint(Graphics g) {
        for (Segment segment : this.segments){
            g.setColor(segment.getColor());
            g.fillRect(segment.getxOffset(),
                    segment.getyOffset(),
                    segment.getWidth(),
                    segment.getHeight());
        }
    }

    /**
     * Sets the colour of a specific segment
     * @param index index in the list of segments
     * @param color colour the segment should be
     */
    public void setSegment(int index, Color color){
        this.segments.get(index).setColor(color);
    }


    /**
     * Generates a list of segments and sets it into display
     * @param numPanelsWide number of panels wide
     * @param numPanelsTall number of panels tall
     * @param mode mode the program is being used in
     */
    public void makeSegments(int numPanelsWide, int numPanelsTall, int[] mode) {

        int wallWidth = (numPanelsWide * PIXELS_WIDE);
        int wallHeight = (numPanelsTall * PIXELS_TALL);
        int xStepSize = mode[0];
        int yStepSize = mode[1];
        this.totalSegments = (wallWidth / xStepSize) * (wallHeight / yStepSize);

        List<Segment> segmentList = new ArrayList<>();

//        Creates a list of segments
        for (int y = Y_OFFSET; y < (wallHeight + Y_OFFSET); y += yStepSize) {
            for (int x = X_OFFSET; x < (wallWidth + X_OFFSET); x += xStepSize) {
                Segment segment = new Segment(x, y, mode[0], mode[1]);
                segmentList.add(segment);
            }
        }

        this.segments = segmentList;
    }

    /**
     * Draws all the segments onto the screen
     */
    public void drawSegments(){
        this.repaint();
    }

    /**
     * gets the total number of segments
     * @return total number of segments
     */
    public int getNumSegments(){
        return segments.toArray().length;
    }


//    Getters
    public int getPIXELS_WIDE() {
        return PIXELS_WIDE;
    }
    public int getPIXELS_TALL() {
        return PIXELS_TALL;
    }
}
