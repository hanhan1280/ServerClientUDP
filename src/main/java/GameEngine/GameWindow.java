package GameEngine;

import GameEngine.Player.Player;
import Graphics.Fonts;
import Graphics.Spritesheet;
import Map.Camera;
import Map.Map;
import Network.Client;
import Network.Packets.LoginPacket;
import Network.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Scanner;

public class GameWindow extends JFrame implements Runnable {

    public static GameWindow gameWindow;
    public final int WIDTH = 800;
    public final int HEIGHT = WIDTH / 12 * 9;
    public Camera camera;
    public Player player;
    public MainListener mainListener;
    public Map map;
    public Server server;
    public Client client;
    ShooterClientServer thisGame;
    private String username, address;
    private int port;
    Scanner scanner;
    private BufferedImage image;
    private boolean running, isServer;

    public GameWindow(ShooterClientServer thisGame, String username, String address, int port, boolean isServer) {
        this.thisGame = thisGame;
        gameWindow = this;
        this.username = username;
        this.address = address;
        this.port = port;
        this.isServer = isServer;
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        setTitle("ShooterClientServer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        map = new Map(this);
        Fonts font = new Fonts(new Spritesheet("fontsheet.png", 15, 5));
        mainListener = new MainListener(this, thisGame);
        camera = new Camera(this, 0, 0);
    }

    public void init() {
        player = new Player(new Spritesheet("testSprint.png", 4, 6), this, 16, 16, null, -1, camera, mainListener);
        player.setUsername(username);
        addKeyListener(mainListener);
        setVisible(true);
        start();
    }

    public void setUpThread() {
        map.addEntity(player);
        LoginPacket packet = new LoginPacket(player.getUsername(), player.x, player.y);
        if (server != null) {
            server.loginPacketMethod(player, packet);
        }
        packet.writeData(client);
    }

    protected synchronized void start() {
        running = true;
        if (isServer) {
            server = new Server(this, this.port);
            new Thread(server).start();
        }
        client = new Client(this,this.address,this.port);
        new Thread(client).start();
        new Thread(this).start();
    }

    protected synchronized void stop() {
        running = false;
    }

    public void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, WIDTH, HEIGHT, null);
        map.render(g);
        map.renderEntity(g);
        g.dispose();
        bs.show();
    }

    private void updateNow() {
        map.update();
    }

    @Override
    public void run() {
        long current = System.nanoTime(), currentTime = System.currentTimeMillis();
        double nsPerFrame = 1000000000D / 60D, delta = 0;//for 60fps each frame takes:
        setUpThread();
        while (running) {
            long next = System.nanoTime();
            delta += (next - current) / nsPerFrame;
            current = next;
            while (delta >= 1) {
                delta -= 1;
                updateNow();
            }
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            render();
            if (System.currentTimeMillis() - currentTime >= 1000) {
                currentTime += 1000;
            }
        }
    }
}
