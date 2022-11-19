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
    static int DST_UNIVERSE_INDEX = DST_ADDRESS_INDEX + 3;
    static int UNIVERSE_INDEX = 168;
    static int MAX_RGB_PER_UNIVERSE = 512 / 3;

    String adapterAddress;
    int requiredChannels;
    int requiredUniverses;
    int[] indexes;
    Display display;


    public PcapHandle handle;



    public Universes(String adapterAddress, Display display) throws PcapNativeException, UnknownHostException {
        int segments = display.getNumSegments();
        this.requiredChannels = segments * 3;
        this.requiredUniverses = (this.requiredChannels / 512) + 1;
        this.display = display;
        this.indexes = IntStream.range(1, segments + 1).toArray();
        this.adapterAddress = adapterAddress;


        InetAddress addr = InetAddress.getByName(adapterAddress);
        PcapNetworkInterface nif = Pcaps.getDevByAddress(addr);
        int snapLen = 65536;
        PcapNetworkInterface.PromiscuousMode mode = PcapNetworkInterface.PromiscuousMode.PROMISCUOUS;
        int timeout = 10;
        this.handle = nif.openLive(snapLen, mode, timeout);

    }

    PacketListener listener = new PacketListener() {
        @Override
        public void gotPacket(PcapPacket packet) {
            byte[] rawPacket = packet.getPacket().getRawData();
//            if (Arrays.equals(Arrays.copyOfRange(rawPacket, SRC_ADDRESS_INDEX, SRC_ADDRESS_INDEX+4), DESK_ADDRESS)){
                if(Arrays.equals(Arrays.copyOfRange(rawPacket, DST_ADDRESS_INDEX, DST_ADDRESS_INDEX + 3), SACN_ADDRESS)){
                    int universeNum = Byte.toUnsignedInt(rawPacket[DST_UNIVERSE_INDEX]);
                    if (universeNum <= requiredUniverses){
                        int index = 0;
                        byte[] byteUniverse = Arrays.copyOfRange(packet.getRawData(), UNIVERSE_INDEX, UNIVERSE_INDEX+512);
                        while (((universeNum -1) * 170 + index) < display.getNumSegments() && (index*3)+2 < 510){

                            int universeOffset = (universeNum -1) * 170;
                            int red = Byte.toUnsignedInt(byteUniverse[index*3]);
                            int green = Byte.toUnsignedInt(byteUniverse[(index*3)+1]);
                            int blue = Byte.toUnsignedInt(byteUniverse[(index*3)+2]);
                            display.setSegment(universeOffset + index, new Color(red, green, blue));
//                            for (int x = 0; x < display.getNumSegments(); x += 1) {
//                            }
                            index++;
                        }
                        display.drawSegments();
                    }
                }
            }
//        }
    };

    public void listen(){
        try {
            handle.loop(-1, listener);
        } catch (InterruptedException | PcapNativeException | NotOpenException e) {
            e.printStackTrace();
        }
    }


}
