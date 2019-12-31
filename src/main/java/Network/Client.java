/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Network;

import GameEngine.GameName;
import GameEngine.GameWindow;
import GameEngine.Player;
import Graphics.Spritesheet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

/**
 *
 * @author hanan
 */
public class Client implements Runnable{
    private InetAddress inetAddress;
    private DatagramSocket socket;
    GameWindow gw;

    public Client (GameWindow gw){
        this.gw = gw;
        try {
            this.socket = new DatagramSocket();
            this.inetAddress = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void communicate(byte[] data, InetAddress iNetAddress, int port){
        String message = new String(data).trim();
        String[] dataReceived = message.split("#");
        int packetType = Integer.parseInt(dataReceived[0]);
        Packet packet = null;
        switch (packetType){
            case Packet.LOGIN_PACKET:
                packet = new LoginPacket(data);
                loginPacketMethod((LoginPacket) packet, iNetAddress, port);
                break;
            case Packet.DISCONNECT_PACKET:
                break;
            case Packet.GAME_PACKET:
                packet = new GamePacket(data);
                gamePacketMethod((GamePacket) packet, iNetAddress, port);
                break;
        }
    }

    public void sendData(byte[] data){
        DatagramPacket packet = new DatagramPacket(data,data.length, this.inetAddress, 8888);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loginPacketMethod(LoginPacket packet, InetAddress inetAddress, int port){
        Player instance = new Player(new Spritesheet("testSprint.png",4,6), gw,inetAddress,port);
        instance.setUsername(packet.getUsername());
        instance.xPos = packet.getxPos();
        instance.yPos = packet.getyPos();
        gw.map.addEntity(instance);
    }

    private void disconnectPacketMethod(){

    }

    private void gamePacketMethod(GamePacket packet, InetAddress inetAddress, int port){
        gw.map.playersMove(packet.getUsername(),packet.getxPos(),packet.getyPos(),packet.getCurrentDir(),packet.isAttack(),packet.isMoving());
    }

    @Override
    public void run() {
        while(true){
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
