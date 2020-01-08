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
import java.util.ArrayList;
import java.util.List;

/**
 * @author hanan
 */
public class Server implements Runnable {

    private GameWindow gw;
    private DatagramSocket socket;
    private List<Player> playersOn = new ArrayList<>();

    public Server(GameWindow gw, int port) {
        this.gw = gw;
        try {
            this.socket = new DatagramSocket(port);
        } catch (SocketException e) {
            JOptionPane.showMessageDialog(null, "Server already started here.");
            System.exit(-1);
        }
    }

    private void communicate(byte[] data, InetAddress iNetAddress, int port) {
        String message = new String(data).trim();
        String[] dataReceived = message.split("#");
        int packetType = Integer.parseInt(dataReceived[0]);
        Packet packet = null;
        switch (packetType) {
            default:
                break;
            case Packet.CONNECT_PACKET:
                packet = new ConnectPacket(data);
                System.out.println("[" + iNetAddress.getHostAddress() + ":" + port + "] "
                        + ((ConnectPacket) packet).getUsername() + " has connected...");
                Player instance = new Player(new Spritesheet("testSprint.png", 4, 6), gw, 16,16,iNetAddress, port);
                instance.setUsername(((ConnectPacket) packet).getUsername());
                connectPacketMethod(instance, (ConnectPacket) packet);
                break;
            case Packet.DISCONNECT_PACKET:
                packet = new DisconnectPacket(data);
                System.out.println("[" + iNetAddress.getHostAddress() + ":" + port + "] "
                        + ((DisconnectPacket) packet).getUsername() + " has disconnected...");
                disconnectPacketMethod((DisconnectPacket) packet);
                break;
            case Packet.GAME_PACKET:
                packet = new GamePacket(data);
                gamePacketMethod((GamePacket) packet);
                break;
            case Packet.ATTACK_PACKET:
                packet = new AttackPacket(data);
                attackPacketMethod((AttackPacket) packet);
                break;
        }
    }

    public void connectPacketMethod(Player p, ConnectPacket packet) {
        boolean isConnected = false;
        for (Player player : playersOn) {
            if (p.getUsername().equals(player.getUsername())) {
                player.playerCount++;
                if(player.playerCount >1){
                    p.setUsername(player.playerCount + "." + player.getUsername());
                }
                    isConnected = true;
                    if (player.port == -1) {
                        player.port = p.port;
                    }
                    if (player.inetAddress == null) {
                        player.inetAddress = p.inetAddress;
                    }

            } else {
                sendData(packet.getData(), player.inetAddress, player.port);
                packet = new ConnectPacket(player.getUsername(), player.x, player.y);
                sendData(packet.getData(), p.inetAddress, p.port);
            }
        }
        if (!isConnected) {
            playersOn.add(p);
        }
    }

    private void disconnectPacketMethod(DisconnectPacket packet) {
        this.playersOn.remove(getPlayerByIndex(packet.getUsername()));
        packet.writeData(this);
    }

    private void gamePacketMethod(GamePacket packet) {
        Player p = playersOn.get(getPlayerByIndex(packet.getUsername()));
        p.x = packet.getxPos();
        p.y = packet.getyPos();
        p.currentDir = packet.getCurrentDir();
        p.isMoving = packet.isMoving();
        packet.writeData(this);
    }

    private void attackPacketMethod(AttackPacket packet) {
        Player p = playersOn.get(getPlayerByIndex(packet.getUsername()));
        p.attack = packet.isAttack();
        packet.writeData(this);
    }

    private void sendData(byte[] data, InetAddress inetAddress, int port) {
        DatagramPacket packet = new DatagramPacket(data, data.length, inetAddress, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendDataFull(byte[] data) {
        for (Player p : playersOn) {
            sendData(data, p.inetAddress, p.port);
        }
    }

    private int getPlayerByIndex(String username) {
        int index = 0;
        for (Player p : playersOn) {
            if (p.getUsername().equals(username)) {
                break;
            }
            index++;
        }
        return index;
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