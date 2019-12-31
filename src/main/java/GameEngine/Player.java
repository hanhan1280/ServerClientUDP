package GameEngine;


import Graphics.*;
import Map.Box;
import Map.Camera;
import Network.GamePacket;
import java.awt.*;
import java.net.InetAddress;

public class Player extends Entity{

    private int health;
    private String username;
    private final float MOVEMENT_V = 50f;
    private MainListener input;
    public InetAddress inetAddress;
    public int port;
    private Camera camera;

    public Player(Spritesheet sprite, GameWindow gw, InetAddress inetAddress, int port, Camera c, MainListener mainListener) {
        super(sprite);
        this.inetAddress = inetAddress;
        this.port = port;
        this.camera = c;
        this.input = mainListener;
        xPos = gw.WIDTH/2;
        yPos = gw.HEIGHT/2;
    }

    public Player(Spritesheet sprite, GameWindow gw, InetAddress inetAddress, int port) {
        super(sprite);
        this.inetAddress = inetAddress;
        this.port = port;
        xPos = gw.WIDTH/2;
        yPos = gw.HEIGHT/2;
    }

    public void update(){
        super.update();
        if(camera!= null){
            camera.entityMove(this);
        }
            move();

    }

    public void render(Graphics g){
        if(camera!=null){
            Fonts.render(g,username,GameWindow.gameWindow.WIDTH/2, GameWindow.gameWindow.HEIGHT/2 + 200, 1.6f);
            g.drawImage(ani.getImage().image, xPos-camera.xOffset, yPos-camera.yOffset, w, h, null);
        }else{
            Fonts.render(g,username,xPos - GameWindow.gameWindow.camera.xOffset, yPos - GameWindow.gameWindow.camera.yOffset-h, 1);
            g.drawImage(ani.getImage().image, xPos - GameWindow.gameWindow.camera.xOffset, yPos - GameWindow.gameWindow.camera.yOffset, w, h, null);
        }
    }

    public void move(){
        int xa= 0 ,ya =0;
        if(input!= null){
            if(input.attack){
                attack = true;
            }else {
                attack = false;
            }
            if(input.up){
                currentDir = UP;
                ya--;
            }
            if(input.right){
                currentDir = RIGHT;
                xa++;
            }
            if(input.left){
                currentDir = LEFT;
                xa--;
            }
            if(input.down){
                currentDir = DOWN;
                ya++;
            }
        }
        if (xa != 0 || ya != 0) {
            xPos += (xa * MOVEMENT_V/Box.BOX_SIZE);
            yPos += (ya * MOVEMENT_V/Box.BOX_SIZE);
            isMoving = true;
            GamePacket packet = new GamePacket(username,xPos,yPos,currentDir,attack,true);
            packet.writeData(GameWindow.gameWindow.client);
        } else {
            isMoving = false;
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

}
