package VideoOutput;

import javax.swing.*;
import java.awt.*;

public class Display {

    JFrame frame;
    final int SCREEN_NUMBER;
    int width;
    int height;

    public Display(int screenNum){
        this.SCREEN_NUMBER = screenNum - 1;
    }

    public void initialiseScreen(){
        this.frame = new JFrame("VideoOut");
        int screenNum = this.SCREEN_NUMBER;
        JFrame frame = this.frame;

        GraphicsEnvironment graphicsEnviroment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] graphicsDevice = graphicsEnviroment.getScreenDevices();

        this.width = graphicsDevice[screenNum].getDefaultConfiguration().getBounds().width;
        this.height = graphicsDevice[screenNum].getDefaultConfiguration().getBounds().height;

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.getContentPane().setBackground(Color.BLACK);
        frame.setUndecorated(true);
        if( screenNum > -1 && screenNum < graphicsDevice.length) {
//            Get width and height of screen and set it to constants
            frame.setLocation(
                    ((this.width / 2) - (frame.getSize().width / 2)) + graphicsDevice[screenNum].getDefaultConfiguration().getBounds().x,
                    ((this.height / 2) - (frame.getSize().height / 2)) + graphicsDevice[screenNum].getDefaultConfiguration().getBounds().y
            );
            frame.setVisible(true);
            System.out.println("Screen initialised successfully");
        } else {
            throw new RuntimeException( "Screen " + screenNum +" not found");
        }
    }

}
