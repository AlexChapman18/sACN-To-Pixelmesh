import SaCN.Universes;
import VideoOutput.Calculations;
import VideoOutput.Display;
import org.pcap4j.core.PcapNativeException;

import javax.swing.*;
import java.awt.*;
import java.net.UnknownHostException;
import java.util.Locale;

public class run {



    public static void main(String[] args) throws InterruptedException, UnknownHostException, PcapNativeException {

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
//        display.setTestCard();
//        display.drawSegments();
//        display.testSegments();

        Universes universe = new Universes(display);
        universe.listen();
        universe.handle.close();

        System.out.println("Finished!");


    }
}
