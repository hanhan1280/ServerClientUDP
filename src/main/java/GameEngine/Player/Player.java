package GameEngine.Player;


import GameEngine.GameWindow;
import GameEngine.MainListener;
import Graphics.*;
import Map.Camera;
import Network.Packets.AttackPacket;
import Network.Packets.DisconnectPacket;
import Network.Packets.GamePacket;
import menu.MenuWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.net.InetAddress;

public class Player extends Entity {

    private int health;
    private String username;
    private Spritesheet healthSheet;
    private MainListener input;
    private boolean isHit, disconnected;
    private GameWindow gw;
    private Camera camera;

    public InetAddress inetAddress;
    public int playerCount = 0;
    public int port;

    public Player(Spritesheet sprite, GameWindow gw, int x, int y, InetAddress inetAddress, int port, Camera c, MainListener mainListener) {
        super(sprite);
        this.camera = c;
        this.input = mainListener;
        init(inetAddress, port, gw, x, y);
    }

    public Player(Spritesheet sprite, GameWindow gw, int x, int y, InetAddress inetAddress, int port) {
        super(sprite);
        init(inetAddress, port, gw, x, y);
    }

    public void init(InetAddress inetAddress, int port, GameWindow gw, int x, int y) {
        this.inetAddress = inetAddress;
        this.port = port;
        this.gw = gw;
        this.x = x;
        this.y = y;
        this.xPos = indexToLoc(x);
        this.yPos = indexToLoc(y);
        this.health = 100;
        this.healthSheet = new Spritesheet("healthSheet.png",1,5);
        this.firingTime = System.nanoTime();
    }

    public void update() {
        super.update();
        if (camera != null) {
            camera.entityMove(this);
            if(health <=0){
                JOptionPane.showMessageDialog(null, "GAMEOVER");
                if(!gw.isServer){
                    gw.dispatchEvent(new WindowEvent(gw, WindowEvent.WINDOW_CLOSING));
                    disconnected = true;
                }
                else{
                    disconnected = true;
                    DisconnectPacket packet = new DisconnectPacket(this.username);
                    packet.writeData(gw.client);
                }
            }
        }
        move();
        attack = false;
        removeBullets();
        isHit = getCollision();
        if (isHit) {
            loseHealth();
        }
    }

    public void render(Graphics g) {
        if (camera != null) {
            Fonts.render(g, username, GameWindow.gameWindow.WIDTH / 2, GameWindow.gameWindow.HEIGHT / 2 + 200, 1.6f);
            g.drawImage(getHealthBar().image,GameWindow.gameWindow.WIDTH / 2 - healthSheet.spriteW/2,GameWindow.gameWindow.HEIGHT / 2 -100, null);
            g.drawImage(ani.getImage().image, xPos - camera.xOffset, yPos - camera.yOffset, w, h, null);
        } else {
            Fonts.render(g, username, xPos - GameWindow.gameWindow.camera.xOffset, yPos - GameWindow.gameWindow.camera.yOffset - h, 1);
            g.drawImage(getHealthBar().image,xPos - GameWindow.gameWindow.camera.xOffset - healthSheet.spriteW/2,yPos - GameWindow.gameWindow.camera.yOffset-h-50, null);
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
                xPos += 4;
            } else if (targetX - xPos < 0) {
                xPos -= 4;
            }
            if (targetY - yPos > 0) {
                yPos += 4;
            } else if (targetY - yPos < 0) {
                yPos -= 4;
            }
            return;
        }
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

    private synchronized boolean getCollision() {
        for (Entity e : gw.map.getEntityList()) {
            if (!e.equals(this)) {
                for (int i = 0; i < e.getBullets().size(); i++) {
                    Bullet b = e.getBullets().get(i);
                    if (b.x == this.x && b.y == this.y) {
                        e.getBullets().remove(i);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private Sprite getHealthBar(){
        if(health<=0){
            return healthSheet.spriteArray[0][4];
        }
        switch (health%100){
            case 0:
                return healthSheet.spriteArray[0][0];
            case 75:
                return healthSheet.spriteArray[0][1];
            case 50:
                return healthSheet.spriteArray[0][2];
            case 25:
                return healthSheet.spriteArray[0][3];
            default:
                return null;
        }
    }

    public boolean isDisconnected(){return this.disconnected;}

    private void loseHealth() {
        this.health -= Bullet.DAMAGE;
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
}
