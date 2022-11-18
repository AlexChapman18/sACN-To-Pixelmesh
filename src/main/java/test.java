import SaCN.Universe;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PacketListener;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapPacket;

import java.io.EOFException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

public class test {
    public static void main(String[] args)
            throws UnknownHostException, PcapNativeException, NotOpenException, EOFException, TimeoutException {

        Universe universe = new Universe();
        PacketListener listener = new PacketListener() {
            @Override
            public void gotPacket(PcapPacket packet) {
                // Override the default gotPacket() function and process packet
                System.out.println(packet);
            }
        };
    }
}
