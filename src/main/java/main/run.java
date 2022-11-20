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
    public static void main(String[] args) throws IOException, PcapNativeException {

//        Gets rid of log4j error message
        Logger.getRootLogger().setLevel(Level.OFF);


        int screenNum = Integer.parseInt(args[0]);
        int mode = Integer.parseInt(args[1]);
        int numPanelsWide = Integer.parseInt(args[2]);
        int numPanelsTall = Integer.parseInt(args[3]);
        int numPanels = numPanelsWide * numPanelsTall;
        String adapterAddress = args[4];


        Display display = new Display(screenNum);
        display.initialiseScreen();
        System.out.println("Screen initialised successfully");
        Calculations calculations = new Calculations(mode, numPanelsWide, numPanelsTall);
        display.makeSegments(numPanelsWide, numPanelsTall, calculations.getMode());
        System.out.println("Segments created successfully");


        int segmentsPerPanel = ((display.getNumSegments() / numPanels));
        int channelsPerPanel = (segmentsPerPanel * 3);
        System.out.println("\n\n---------- PANEL INFO ----------");
        System.out.println("Number of panels used: " + numPanels);
        System.out.println("Number of universes used: " + ((display.getNumSegments() * 3) / 512 + 1));
        System.out.println("Number of channels per panels: " + channelsPerPanel);
        System.out.println("Number of RGB fixtures per panels: " + segmentsPerPanel);




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

        Universes universe = new Universes(adapterAddress, display);
        universe.listen();
        universe.handle.close();
    }
}
