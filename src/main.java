import VideoOutput.Display;

import javax.swing.*;
import java.awt.*;

public class main {
    public static void main(String[] args) {

        int screenNum = Integer.parseInt(args[0]);

        Display display = new Display(screenNum);
        display.initialiseScreen();


    }
}
