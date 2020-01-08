package GameEngine;

import GameEngine.Player.Player;
import Graphics.Fonts;
import Graphics.Spritesheet;
import Map.Camera;
import Map.Map;
import Network.Client;
import Network.Packets.DisconnectPacket;
import Network.Packets.ConnectPacket;
import Network.Server;
import menu.MenuWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class GameWindow extends JFrame implements Runnable {

    public static GameWindow gameWindow;
    public final int WIDTH = 800;
    public final int HEIGHT = WIDTH / 12 * 9;
    public Camera camera;
    public Map map;
    public Client client;
    public boolean isServer;
    private Player player;
    private MainListener mainListener;
    private Server server;
    private ShooterClientServer thisGame = ShooterClientServer.thisGame;
    private String username, address;
    private int port;
    private BufferedImage image;
    private boolean running;
    private Thread clientThread, serverThread, gameThread;

    public GameWindow(ShooterClientServer thisGame, String username, String address, int port, boolean isServer) {
        this.thisGame = thisGame;
        gameWindow = this;
        this.username = username;
        this.address = address;
        this.port = port;
        this.isServer = isServer;
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        setTitle("ShooterClientServer");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        map = new Map(this);
        mainListener = new MainListener();
        camera = new Camera(this, 0, 0);
    }

    public void init() {
        player = new Player(new Spritesheet("testSprint.png", 4, 6), this, 16, 16, null, -1, camera, mainListener);
        player.setUsername(username);
        addKeyListener(mainListener);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                if(!player.isDisconnected()) {
                    DisconnectPacket packet = new DisconnectPacket(player.getUsername());
                    packet.writeData(client);
                }
                new MenuWindow(thisGame);
            }
        });
        setPreferredSize(new Dimension(WIDTH,HEIGHT));
        setMaximumSize(new Dimension(WIDTH,HEIGHT));
        setMinimumSize(new Dimension(WIDTH,HEIGHT));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        start();
    }

    private void setUpThread() {
        map.addEntity(player);
        ConnectPacket packet = new ConnectPacket(player.getUsername(), player.x, player.y);
        if (server != null) {
            server.connectPacketMethod(player, packet);
        }
        packet.writeData(client);
    }

    public synchronized void start() {
        running = true;
        gameThread = new Thread(this);
        if (isServer) {
            server = new Server(this, this.port);
            serverThread = new Thread(server, "Server");
            serverThread.start();
        }
        client = new Client(this,this.address,this.port);
        clientThread = new Thread(client);
        clientThread.start();
        gameThread.start();
    }

    public synchronized void stop() {
        running = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void render() {
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
