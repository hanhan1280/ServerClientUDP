package Network.Packets;

import Network.Client;
import Network.Packets.Packet;
import Network.Server;

public class AttackPacket extends Packet {

    private boolean isAttack;
    private String username;

    public AttackPacket(byte[] data) {
        super(03);
        String[] dataMessage = new String(data).trim().split("#");
        this.username = dataMessage[1];
        this.isAttack = Integer.parseInt(dataMessage[2]) == 1;
    }

    public AttackPacket(String username, boolean isAttack) {
        super(03);
        this.username = username;
        this.isAttack = isAttack;
    }

    public boolean isAttack() {
        return isAttack;
    }


    public String getUsername() {
        return username;
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
        return ("" + this.id + "#" + this.username + "#" + (this.isAttack? 1:0)).getBytes();
    }
}
