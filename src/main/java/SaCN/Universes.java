package SaCN;

import VideoOutput.Display;
import org.pcap4j.core.*;

import java.awt.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.stream.IntStream;

public class Universes {

//    constant
    static byte[] DESK_ADDRESS = new byte[]{2, 0, 0, 12};
    static byte[] SACN_ADDRESS = new byte[]{(byte)239, (byte)255, 0};
    static int SRC_ADDRESS_INDEX = 26;
    static int DST_ADDRESS_INDEX = 30;
    static int DST_UNIVERSE_INDEX = DST_ADDRESS_INDEX + 4;
    static int UNIVERSE_INDEX = 168;

    int requiredChannels;
    int requiredUniverses;
    int[] indexes;
    Display display;

    InetAddress addr = InetAddress.getByName("169.254.140.211");
    PcapNetworkInterface nif = Pcaps.getDevByAddress(addr);
    int snapLen = 65536;
    PcapNetworkInterface.PromiscuousMode mode = PcapNetworkInterface.PromiscuousMode.PROMISCUOUS;
    int timeout = 10;
    public PcapHandle handle = nif.openLive(snapLen, mode, timeout);



    public Universes(Display display) throws PcapNativeException, UnknownHostException {
        int segments = display.getNumSegments();
        this.requiredChannels = segments * 3;
        this.requiredUniverses = (this.requiredChannels % 512) + 1;
        this.display = display;
        this.indexes = IntStream.range(1, segments + 1).toArray();

    }

    PacketListener listener = new PacketListener() {
        @Override
        public void gotPacket(PcapPacket packet) {
            // Override the default gotPacket() function and process packet
            byte[] rawPacket = packet.getPacket().getRawData();
            if (Arrays.equals(Arrays.copyOfRange(rawPacket, SRC_ADDRESS_INDEX, SRC_ADDRESS_INDEX+4), DESK_ADDRESS)){
                if(Arrays.equals(Arrays.copyOfRange(rawPacket, DST_ADDRESS_INDEX, DST_ADDRESS_INDEX + 3), SACN_ADDRESS)){

                    byte[] byteUniverse = Arrays.copyOfRange(packet.getRawData(), UNIVERSE_INDEX, UNIVERSE_INDEX+512);
                    for (int x = 0; x < display.getNumSegments(); x += 1) {
                        int red = Byte.toUnsignedInt(byteUniverse[x*3]);
                        int green = Byte.toUnsignedInt(byteUniverse[(x*3)+1]);
                        int blue = Byte.toUnsignedInt(byteUniverse[(x*3)+2]);
                        display.setSegment(x, new Color(red, green, blue));
//                        display.setSegment(Integer.valueOf(i), new Color(red, green, blue));
//                        System.out.println(red +" "+ green +" "+ blue);
                    }
                    display.drawSegments();
//                    System.out.println(Arrays.toString(Arrays.copyOfRange(packet.getRawData(), UNIVERSE_INDEX, UNIVERSE_INDEX+512)));
                }
            }
        }
    };

    public void listen(){
        try {
            handle.loop(-1, listener);
        } catch (InterruptedException | PcapNativeException | NotOpenException e) {
            e.printStackTrace();
        }
    }


}
