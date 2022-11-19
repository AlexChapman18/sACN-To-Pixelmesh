package VideoOutput;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Display extends JFrame{

//    Constants
    final int X_OFFSET = 1;
    final int Y_OFFSET = 1;
    final int PIXELS_WIDE = 32;
    final int PIXELS_TALL = 64;

//    Variables
    final int SCREEN_NUMBER;
    int screenWidth;
    int screenHeight;
    List<Segment> segments;
    int totalSegments;

//    Constructor
    public Display(int screenNum){
        this.SCREEN_NUMBER = screenNum - 1;
    }

//    Sets the specified monitor to black
    public void initialiseScreen(){
        setTitle("VideoOut");
        int screenNum = this.SCREEN_NUMBER;

        GraphicsEnvironment graphicsEnviroment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] graphicsDevice = graphicsEnviroment.getScreenDevices();

        this.screenWidth = graphicsDevice[screenNum].getDefaultConfiguration().getBounds().width;
        this.screenHeight = graphicsDevice[screenNum].getDefaultConfiguration().getBounds().height;

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.getContentPane().setBackground(Color.BLACK);
        this.setUndecorated(true);
        if( screenNum > -1 && screenNum < graphicsDevice.length) {
//            Get width and height of screen and set it to constants
            this.setLocation(
                    ((this.screenWidth / 2) - (this.getSize().width / 2)) + graphicsDevice[screenNum].getDefaultConfiguration().getBounds().x,
                    ((this.screenHeight / 2) - (this.getSize().height / 2)) + graphicsDevice[screenNum].getDefaultConfiguration().getBounds().y
            );
            this.setVisible(true);
            System.out.println("Screen initialised successfully");
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

    public void setSegment(int index, Color color){
        this.segments.get(index).setColor(color);
    }

//    public Segment[] getSegments(){
//        return segments;
//    }

    public void makeSegments(int numPanelsWide, int numPanelsTall, int[] mode) {

        int wallWidth = (numPanelsWide * PIXELS_WIDE);
        int wallHeight = (numPanelsTall * PIXELS_TALL);
        int xStepSize = mode[0];
        int yStepSize = mode[1];
        this.totalSegments = (wallWidth / xStepSize) * (wallHeight / yStepSize);

        List<Segment> segmentList = new ArrayList<>();

        for (int y = Y_OFFSET; y < (wallHeight + Y_OFFSET); y += yStepSize) {
            for (int x = X_OFFSET; x < (wallWidth + X_OFFSET); x += xStepSize) {
                Segment segment = new Segment(x, y, mode[0], mode[1]);
                segmentList.add(segment);
            }
        }

        this.segments = segmentList;
    }

    public void setTestCard(){
        int index = 0;
        for (Segment segment : segments){
            if (index % 2 == 0)
                segment.setColor(Color.GRAY);
            else
                segment.setColor(Color.WHITE);
            index ++;
        }
    }

    public void drawSegments(){
        this.repaint();
    }

    public void setAllSegments(Color color){
        for (Segment segment : segments){
            segment.setColor(color);
        }
    }

    public void testSegments() throws InterruptedException {
        setAllSegments(Color.BLACK);
        drawSegments();
        for (Color color : new Color[]{Color.red, Color.green, Color.blue, Color.white}){
            for (Segment segment : segments){
                segment.setColor(color);
                drawSegments();
                Thread.sleep(500);
                segment.setColor(Color.BLACK);
            }
        }
    }

    public int getNumSegments(){
        return segments.toArray().length;
    }
}
