/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Network;

import GameEngine.Entity;
import GameEngine.GameName;
import GameEngine.GameWindow;
import GameEngine.Player;
import Graphics.Spritesheet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.UUID;

/**
 * @author hanan
 */
public class Server implements Runnable{

    GameWindow gw;
    private DatagramSocket socket;
    private ArrayList<Player> playersOn = new ArrayList<>();

    public Server(GameWindow gw){
        this.gw = gw;
        try {
            this.socket = new DatagramSocket(8888);
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
                System.out.println("[" + iNetAddress.getHostAddress() + ":" + port + "] "
                        + ((LoginPacket) packet).getUsername() + " has connected...");
                Player instance = new Player(new Spritesheet("testSprint.png",4,6), gw,iNetAddress,port);
                instance.setUsername(((LoginPacket) packet).getUsername());
                addPlayer(instance,(LoginPacket) packet);
                break;
            case Packet.DISCONNECT_PACKET:
                break;
            case Packet.GAME_PACKET:
                packet = new GamePacket(data);
                gamePacketMethod((GamePacket) packet);
                break;
        }
    }

    public void addPlayer(Player p, LoginPacket packet){
        boolean isConnected = false;
        for (Player player: playersOn) {
            if(p.getUsername().equals(player.getUsername())){
                if(player.inetAddress == null){
                    player.inetAddress = p.inetAddress;
                }
                if(player.port == -1){
                    player.port = p.port;
                }
                isConnected = true;
            }
            else{
                sendData(packet.getData(),player.inetAddress,player.port);
                packet = new LoginPacket(player.getUsername(),player.xPos,player.yPos);
                sendData(packet.getData(),p.inetAddress,p.port);
            }
        }
        if (!isConnected) {
            playersOn.add(p);
        }
    }

    private void disconnectPacketMethod(){

    }

    private void gamePacketMethod(GamePacket packet){
        Player p = playersOn.get(getPlayerByIndex(packet.getUsername()));
        p.xPos = packet.getxPos();
        p.yPos = packet.getyPos();
        p.currentDir = packet.getCurrentDir();
        p.attack = packet.isAttack();
        p.isMoving = packet.isMoving();
        packet.writeData(this);
    }

    public void sendData(byte[] data, InetAddress inetAddress, int port){
        DatagramPacket packet = new DatagramPacket(data,data.length, inetAddress, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendDataFull(byte[] data){
        for (Player p: playersOn) {
            sendData(data, p.inetAddress, p.port);
        }
    }

    public int getPlayerByIndex(String username){
        int index = 0;
        for (Player p : playersOn) {
            if(p.getUsername().equals(username)){
                break;
            }
            index++;
        }
        return index;
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