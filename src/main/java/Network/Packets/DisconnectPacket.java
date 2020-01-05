package Network.Packets;

import Network.Client;
import Network.Packets.Packet;
import Network.Server;

public class DisconnectPacket extends Packet {

    private String username;

    public DisconnectPacket(byte[] data) {
        super(01);
        String[] dataMessage = new String(data).trim().split("#");
        this.username = dataMessage[1];
    }

    public DisconnectPacket(String username) {
        super(01);
        this.username = username;
    }

    @Override
    public void writeData(Client client) {
        client.sendData(getData());
    }

    @Override
    public void writeData(Server server) {
        server.sendDataFull(getData());
    }

    @Override
    public byte[] getData() {
        return ("" + this.id + "#" + this.username).getBytes();
    }

    public String getUsername() {
        return username;
    }

}
