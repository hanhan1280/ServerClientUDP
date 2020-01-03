package GameEngine;


import Graphics.*;
import Map.Box;
import Map.Camera;
import Network.AttackPacket;
import Network.GamePacket;
import java.awt.*;
import java.net.InetAddress;
import java.util.ArrayList;

public class Player extends Entity {

    private int health;
    private String username;
    private MainListener input;
    public InetAddress inetAddress;
    public int port;
    private Camera camera;

    public Player(Spritesheet sprite, GameWindow gw, InetAddress inetAddress, int port, Camera c, MainListener mainListener) {
        super(sprite);
        this.camera = c;
        this.input = mainListener;
        init(inetAddress, port, gw);
    }

    public Player(Spritesheet sprite, GameWindow gw, InetAddress inetAddress, int port) {
        super(sprite);
        init(inetAddress, port, gw);
    }

    public void init(InetAddress inetAddress, int port, GameWindow gw) {
        this.inetAddress = inetAddress;
        this.port = port;
        x = gw.map.MAP_SIZE / 2;
        y = gw.map.MAP_SIZE / 2;
        xPos = indexToLoc(x);
        yPos = indexToLoc(y);
        firingTime = System.nanoTime();
    }

    public void update() {
        super.update();
        if (camera != null) {
            camera.entityMove(this);
        }
        move();
        removeBullets();
    }

    public void render(Graphics g) {
        if (camera != null) {
            Fonts.render(g, username, GameWindow.gameWindow.WIDTH / 2, GameWindow.gameWindow.HEIGHT / 2 + 200, 1.6f);
            g.drawImage(ani.getImage().image, xPos - camera.xOffset, yPos - camera.yOffset, w, h, null);
        } else {
            Fonts.render(g, username, xPos - GameWindow.gameWindow.camera.xOffset, yPos - GameWindow.gameWindow.camera.yOffset - h, 1);
            g.drawImage(ani.getImage().image, xPos - GameWindow.gameWindow.camera.xOffset, yPos - GameWindow.gameWindow.camera.yOffset, w, h, null);
        }
        for (Bullet b : this.getBullets()) {
            b.render(g);
        }
    }

    public void move() {
        if (this.xPos == indexToLoc(x) && this.yPos == indexToLoc(y)) {
            int xa = 0, ya = 0;
            if (input != null) {
                if (input.attack) {
                    attack = true;
                    AttackPacket packet = new AttackPacket(this.username, true);
                    packet.writeData(GameWindow.gameWindow.client);
                } else {
                    AttackPacket packet = new AttackPacket(this.username, false);
                    packet.writeData(GameWindow.gameWindow.client);
                }
                if (input.up) {
                    currentDir = UP;
                    ya = -1;
                } else if (input.right) {
                    currentDir = RIGHT;
                    xa = 1;
                } else if (input.left) {
                    currentDir = LEFT;
                    xa = -1;
                } else if (input.down) {
                    currentDir = DOWN;
                    ya = 1;
                }
            }
            if (xa != 0 || ya != 0) {
                x += xa;
                y += ya;
                if (x < 0 || x == 32) {
                    x -= xa;
                }
                if (y < 0 || y == 30) {
                    y -= ya;
                }
                isMoving = true;
                GamePacket packet = new GamePacket(this.username, this.x, this.y, currentDir, true);
                packet.writeData(GameWindow.gameWindow.client);
            } else {
                isMoving = false;
            }
        } else {
            int targetX = indexToLoc(x);
            int targetY = indexToLoc(y);
            if (targetX - xPos > 0) {
                xPos += 2;
            } else if (targetX - xPos < 0) {
                xPos -= 2;
            }
            if (targetY - yPos > 0) {
                yPos += 2;
            } else if (targetY - yPos < 0) {
                yPos -= 2;
            }
            return;
        }
    }

    public int getHealth() {
        return health;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public synchronized void removeBullets() {
        for (int i = 0; i < getBullets().size(); i++) {
            Bullet b = getBullets().get(i);
            if (Math.abs(b.x - b.initX) >= b.MAX_DISTANCE || Math.abs(b.y - b.initY) >= b.MAX_DISTANCE) {
                getBullets().remove(i);
                i--;
            } else {
                b.update();
            }
        }
    }
}
