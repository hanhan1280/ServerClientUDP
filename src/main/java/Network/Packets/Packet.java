package Network.Packets;

import Network.Client;
import Network.Server;

public abstract class Packet {

    public static final int CONNECT_PACKET = 00, DISCONNECT_PACKET = 01, GAME_PACKET = 02, ATTACK_PACKET = 03;
    protected byte id;

    public Packet(int id) {
        this.id = (byte) id;
    }

    public abstract void writeData(Client client);

    public abstract void writeData(Server server);

    public abstract byte[] getData();

}
