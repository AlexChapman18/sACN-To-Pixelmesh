package main;

import SaCN.Universes;
import VideoOutput.Calculations;
import VideoOutput.Display;
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

        int screenNum = Integer.parseInt(args[0]);
        int mode = Integer.parseInt(args[1]);
        int numPanelsWide = Integer.parseInt(args[2]);
        int numPanelsTall = Integer.parseInt(args[3]);
        String adapterAddress = args[4];


        Display display = new Display(screenNum);
        display.initialiseScreen();
        Calculations calculations = new Calculations(mode, numPanelsWide, numPanelsTall);
        display.makeSegments(numPanelsWide, numPanelsTall, calculations.getMode());

        Universes universe = new Universes(adapterAddress, display);
        universe.listen();
        universe.handle.close();
    }
}
