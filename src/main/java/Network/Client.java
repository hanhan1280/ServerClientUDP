/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Network;

import GameEngine.GameWindow;
import GameEngine.Player.Player;
import Graphics.Spritesheet;
import Network.Packets.*;

import javax.swing.*;
import java.io.IOException;
import java.net.*;

/**
 * @author hanan
 */
public class Client implements Runnable {
    private InetAddress inetAddress;
    private DatagramSocket socket;
    private GameWindow gw;
    private int port;

    public Client(GameWindow gw, String address, int port) {
        this.gw = gw;
        try {
            this.socket = new DatagramSocket();
            this.inetAddress = InetAddress.getByName(address);
            this.port = port;
        } catch (UnknownHostException e) {
            JOptionPane.showMessageDialog(null, "NO Host at this IP Address");
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private void communicate(byte[] data, InetAddress inetAddress, int port) {
        String message = new String(data).trim();
        String[] dataReceived = message.split("#");
        int packetType = Integer.parseInt(dataReceived[0]);
        Packet packet;
        switch (packetType) {
            default:
                break;
            case Packet.CONNECT_PACKET:
                packet = new ConnectPacket(data);
                connectPacketMethod((ConnectPacket) packet, inetAddress, port);
                break;
            case Packet.DISCONNECT_PACKET:
                packet = new DisconnectPacket(data);
                disconnectPacketMethod((DisconnectPacket) packet, inetAddress,port);
                break;
            case Packet.GAME_PACKET:
                packet = new GamePacket(data);
                gamePacketMethod((GamePacket) packet, inetAddress, port);
                break;
            case Packet.ATTACK_PACKET:
                packet = new AttackPacket(data);
                attackPacketMethod((AttackPacket) packet);
                break;
        }
    }

    public void sendData(byte[] data) {
        DatagramPacket packet = new DatagramPacket(data, data.length, this.inetAddress, this.port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,"IP Address at this Server is unreachable");
            System.exit(-1);
        }
    }

    private void connectPacketMethod(ConnectPacket packet, InetAddress inetAddress, int port) {
        Player instance = new Player(new Spritesheet("blueSprite.png", 4, 6), gw, packet.getxPos(),packet.getyPos(),inetAddress, port);
        instance.setUsername(packet.getUsername());
        gw.map.addEntity(instance);
    }

    private void disconnectPacketMethod(DisconnectPacket packet, InetAddress inetAddress, int port) {
        gw.map.removeEntity(packet.getUsername());
    }

    private void gamePacketMethod(GamePacket packet, InetAddress inetAddress, int port) {
        gw.map.playersMove(packet.getUsername(), packet.getxPos(), packet.getyPos(), packet.getCurrentDir(), packet.isMoving());
    }

    private void attackPacketMethod(AttackPacket packet) {
        gw.map.playersAttack(packet.getUsername(), packet.isAttack());
    }


    @Override
    public void run() {
        while (true) {
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.communicate(packet.getData(), packet.getAddress(), packet.getPort());
        }
    }
}
