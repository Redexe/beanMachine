package objects;

import java.net.InetAddress;

import managers.DataResponse;

public class ServerData {
    public final InetAddress address;
    public final DataResponse packet;

    public ServerData(DataResponse packet, InetAddress address) {
        this.packet = packet;
        this.address = address;
    }

    public DataResponse getPacket() {
        return packet;
    }

    public InetAddress getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return packet.toString();
    }
}
