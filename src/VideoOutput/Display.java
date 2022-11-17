package VideoOutput;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Display extends JFrame{

//    Constants
    final int PIXELS_WIDE = 32;
    final int PIXELS_TALL = 64;

//    Variables
    final int SCREEN_NUMBER;
    int screenWidth;
    int screenHeight;
    Segment[] segments;
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
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, screenWidth, screenHeight);
        for (Segment segment : this.segments){
            g.setColor(segment.getColor());
            g.fillRect(segment.getxOffset(),
                    segment.getyOffset(),
                    segment.getWidth(),
                    segment.getHeight());
        }
    }

    public void makeSegments(int numPanelsWide, int numPanelsTall, int[] mode){

        int wallWidth = (numPanelsWide * PIXELS_WIDE);
        int wallHeight = (numPanelsTall * PIXELS_TALL);
        int xStepSize = mode[0];
        int yStepSize = mode[1];
        this.totalSegments = (wallWidth / xStepSize) * (wallHeight / yStepSize);

        List<Segment> segmentList = new ArrayList<>();

        for (int y = 0; y < wallHeight; y += yStepSize){
            for (int x = 0; x < wallWidth; x += xStepSize){
                Segment segment = new Segment(x, y, mode[0], mode[1]);
                segmentList.add(segment);
            }
        }

        this.segments =  segmentList.toArray(new Segment[0]);
    }

    public void setTestCard(){
        int index = 0;
        int steps = (int) Math.floor(255 / segments.length);
        for (Segment segment : segments){
            segment.setColor(new Color(0, index, index));
            index += steps;
        }
    }

    public void drawSegments(){
        this.repaint();
    }



}
