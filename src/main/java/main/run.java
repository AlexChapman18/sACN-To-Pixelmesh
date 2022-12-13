package main;

import SaCN.Universes;
import VideoOutput.Calculations;
import VideoOutput.Display;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.pcap4j.core.PcapNativeException;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.Properties;

public class run {

    public static Properties allSettings;

    public static void main(String[] args) throws IOException, PcapNativeException {

//        Gets rid of log4j error message
        Logger.getRootLogger().setLevel(Level.OFF);

        allSettings = Utils.getSettings();

        assert allSettings != null;
        int displayNum = Integer.parseInt(allSettings.getProperty("Display_number"));
        int mode = Integer.parseInt(allSettings.getProperty("Mode"));
        int numPanelsWide = Integer.parseInt(allSettings.getProperty("Panels_wide"));
        int numPanelsTall = Integer.parseInt(allSettings.getProperty("Panels_tall"));
        int numPanels = numPanelsWide * numPanelsTall;
        String adapterAddress = allSettings.getProperty("Adapter_ip_address");


        Display display = new Display(displayNum);
        display.initialiseScreen();
        System.out.println("Screen initialised successfully");
        Calculations calculations = new Calculations(mode, numPanelsWide, numPanelsTall);
        display.makeSegments(numPanelsWide, numPanelsTall, calculations.getMode());
        System.out.println("Segments created successfully");


//        All of this is printing the current instances information
        int segmentsPerPanel = ((display.getNumSegments() / numPanels));
        int channelsPerPanel = (segmentsPerPanel * 3);
        System.out.println("\n\n---------- PANEL INFO ----------");
        System.out.println("Number of panels used: " + numPanels);
        System.out.println("Number of universes used: " + ((display.getNumSegments() * 3) / 512 + 1));
        System.out.println("Number of channels per panels: " + channelsPerPanel);
        System.out.println("Number of RGB fixtures per panels: " + segmentsPerPanel);
        System.out.println("Number of segments total: " + display.getNumSegments());

        int numSegmentsWide = (numPanelsWide * display.getPIXELS_WIDE())/calculations.getMode()[0];
        int numSegmentsTall = (numPanelsTall * display.getPIXELS_TALL())/calculations.getMode()[1];
        System.out.println("Number of segments wide: " + numSegmentsWide + ". Number of segments tall: " + numSegmentsTall);

        int currentAddress = 0;
        int totalChannels = 0;
        int currentUniverse = 0;
        for (int i = 0; i < numPanels; i++){
            totalChannels += channelsPerPanel;
            System.out.println("Panel: " + (i + 1) +
                    ", is at: " + (currentUniverse + 1) + "." + (currentAddress + 1) +
                    " -> " + ((totalChannels / 512) + 1) + "." + ((totalChannels % 510) -2)
            );
            currentAddress = (totalChannels % 510);
            currentUniverse = (totalChannels / 512);

        }

//        Set listening for sACN data
        Universes universe = new Universes(adapterAddress, display);
        universe.listen();
        universe.handle.close();
    }
}
