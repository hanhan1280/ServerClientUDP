package GameEngine;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.util.Scanner;

import Graphics.*;
import Map.*;
import Network.Client;
import Network.LoginPacket;
import Network.Server;

public class GameWindow extends JFrame implements Runnable {

    Scanner scanner = new Scanner(System.in);
    GameName thisGame;
    public final int WIDTH = 800;
    public final int HEIGHT = WIDTH/12 * 9;
    public Camera camera;
    private int[] pixels;
    public Player player;
    private BufferedImage image;
    public MainListener mainListener;
    private boolean running;
    String username;
    public Map map;
    public static GameWindow gameWindow;
    public Server server;
    public Client client;

    public GameWindow(GameName thisGame){
        this.thisGame = thisGame;
        gameWindow = this;
        image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        setTitle("CPT Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(WIDTH,HEIGHT);
        setResizable(false);
    }

    public void init(){
        System.out.println("Enter username: ");
        username = scanner.nextLine();
        Fonts font = new Fonts(new Spritesheet("fontsheet.png",15,5));
        mainListener = new MainListener(this,thisGame);
        camera = new Camera(this,0,0);
        player = new Player(new Spritesheet("testSprint.png",4,6), this, null, -1, camera,mainListener);
        player.setUsername(username);
        System.out.println(player.getUsername());
        map = new Map(this);
        addKeyListener(mainListener);
        setVisible(true);
        start();
    }

    public void setUpThread(){
        map.addEntity(player);
        LoginPacket packet = new LoginPacket(player.getUsername(), player.xPos, player.yPos);
        if (server != null) {
            server.addPlayer(player, packet);
        }
        packet.writeData(client);
    }

    protected synchronized void start(){
        running = true;
        System.out.println("Server?");
        String s = scanner.nextLine();
        if(s.equalsIgnoreCase("server")){
            server = new Server(this);
            new Thread(server).start();
        }
        client = new Client(this);
        new Thread(client).start();
        new Thread(this).start();
    }

    protected synchronized void stop(){
        running = false;
    }

    public void render(){
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
        //player.update();
        map.update();
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double nsPerTick = 1000000000D / 60D;

        int ticks = 0;
        int frames = 0;

        long lastTimer = System.currentTimeMillis();
        double delta = 0;
        setUpThread();
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;
            boolean shouldRender = true;
            while (delta >= 1) {
                ticks++;
                delta -= 1;
                updateNow();
                shouldRender = true;
            }
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (shouldRender) {
                frames++;
                render();
            }

            if (System.currentTimeMillis() - lastTimer >= 1000) {
                lastTimer += 1000;
                frames = 0;
                ticks = 0;
            }
        }
    }
}
