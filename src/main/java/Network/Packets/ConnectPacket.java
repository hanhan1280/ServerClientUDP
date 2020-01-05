package Network.Packets;

import Network.Client;
import Network.Packets.Packet;
import Network.Server;

public class ConnectPacket extends Packet {

    private String username = null;
    private int xPos,yPos;

    public ConnectPacket(byte[] data) {
        super(00);
        String[] dataMessage = new String(data).trim().split("#");
        this.username = dataMessage[1];
        this.xPos = Integer.parseInt(dataMessage[2]);
        this.yPos = Integer.parseInt(dataMessage[3]);
    }

    public ConnectPacket(String username, int xPos, int yPos) {
        super(00);
        this.xPos = xPos;
        this.yPos = yPos;
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
        return ("" + id + "#" + this.username + "#" + this.xPos + "#" + this.yPos).getBytes();
    }

    public String getUsername() {
        return username;
    }

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }

}
