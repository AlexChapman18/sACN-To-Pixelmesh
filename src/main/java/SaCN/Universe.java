package SaCN;

import org.pcap4j.core.*;
import org.pcap4j.packet.Packet;

import java.io.EOFException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

public class Universe {

    InetAddress addr = InetAddress.getByName("192.168.0.19");
    PcapNetworkInterface nif = Pcaps.getDevByAddress(addr);

    int snapLen = 65536;
    PcapNetworkInterface.PromiscuousMode mode = PcapNetworkInterface.PromiscuousMode.PROMISCUOUS;
    int timeout = 10;
    PcapHandle handle = nif.openLive(snapLen, mode, timeout);

    public Universe() throws PcapNativeException, UnknownHostException {
    }

    public Packet getPacket() throws NotOpenException, EOFException, PcapNativeException, TimeoutException {
        Packet packet = handle.getNextPacketEx();
        handle.close();
        return packet;
    }

    PacketListener listener = new PacketListener() {
        @Override
        public void gotPacket(PcapPacket packet) {
            // Override the default gotPacket() function and process packet
            System.out.println(packet);
        }
    };
}
