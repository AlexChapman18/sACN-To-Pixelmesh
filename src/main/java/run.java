import VideoOutput.Calculations;
import VideoOutput.Display;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;

public class run {



    public static void main(String[] args) {

        boolean testCard = false;
        int screenNum = Integer.parseInt(args[0]);
        int mode = Integer.parseInt(args[1]);
        int numPanelsWide = Integer.parseInt(args[2]);
        int numPanelsTall = Integer.parseInt(args[3]);
        if (args[4] != null && args[4].equalsIgnoreCase("true"))
            testCard = true;


        Display display = new Display(screenNum);
        display.initialiseScreen();
        Calculations calculations = new Calculations(mode, numPanelsWide, numPanelsTall);
        display.makeSegments(numPanelsWide, numPanelsTall, calculations.getMode());
        display.setTestCard();
        display.drawSegments();
        System.out.println("Finished!");


    }
}
