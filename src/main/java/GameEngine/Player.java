package GameEngine;


import Graphics.*;
import Map.Camera;
import Network.GamePacket;
import java.awt.*;
import java.net.InetAddress;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class Player extends Entity {

    private int health;
    private String username;
    private final float MOVEMENT_V = 50f;
    private MainListener input;
    public InetAddress inetAddress;
    public int port;
    private Camera camera;
    boolean moved = false;
    public int x, y;

    public int mapIndexToLoc(int idx) {
        return 16 + 32 * idx;
    }

    public int reverseMap(int pos) {
        return (pos - 16)/32;
    }

    public Player(Spritesheet sprite, GameWindow gw, InetAddress inetAddress, int port, Camera c, MainListener mainListener) {
        super(sprite);
        this.inetAddress = inetAddress;
        this.port = port;
        this.camera = c;
        this.input = mainListener;
        xPosIdx = 16;
        yPosIdx = 16;
        this.x = mapIndexToLoc(xPosIdx);
        this.y = mapIndexToLoc(yPosIdx);
    }

    public Player(Spritesheet sprite, GameWindow gw, InetAddress inetAddress, int port) {
        super(sprite);
        this.inetAddress = inetAddress;
        this.port = port;
        xPosIdx = 16;
        yPosIdx = 16;
        this.x = mapIndexToLoc(xPosIdx);
        this.y = mapIndexToLoc(yPosIdx);
    }

    public void update(){
        super.update();

        if(camera!= null){
            camera.entityMove(this);
        }
            move();

    }

//    xPosIdx and yPosIdx are now indices from 0 to 32.
    //    16 + 32*xPosIdx
    //    16 + 32*yPosIdx

    public void render(Graphics g){
        if(camera!=null){
            Fonts.render(g,username,GameWindow.gameWindow.WIDTH/2, GameWindow.gameWindow.HEIGHT/2 + 200, 1.6f);
            g.drawImage(ani.getImage().image, this.x - camera.xOffset, this.y - camera.yOffset, w, h, null);
        }else{
            Fonts.render(g,username, xPosIdx - GameWindow.gameWindow.camera.xOffset, yPosIdx - GameWindow.gameWindow.camera.yOffset-h, 1);
            g.drawImage(ani.getImage().image, mapIndexToLoc(xPosIdx) - GameWindow.gameWindow.camera.xOffset, mapIndexToLoc(yPosIdx) - GameWindow.gameWindow.camera.yOffset, w, h, null);
        }
    }

    public void move(){



        // if we have reached our position
        if (this.x == mapIndexToLoc(this.xPosIdx) && this.y == mapIndexToLoc(this.yPosIdx)) {

            moved = true;
            int xa= 0 ,ya =0;
            if(input!= null){
                if(input.attack){
                    attack = true;
                }else {
                    attack = false;
                }
                if(input.up){
                    currentDir = UP;
                    ya = -1;
                }
                if(input.right){
                    currentDir = RIGHT;
                    xa = 1;
                }
                if(input.left){
                    currentDir = LEFT;
                    xa = -1;
                }
                if(input.down){
                    currentDir = DOWN;
                    ya = 1;
                }
            }

            if (xa != 0 || ya != 0) {

                if (this.xPosIdx >= 0 && this.xPosIdx <= 31 && this.yPosIdx >= 0 && this.yPosIdx <= 31){
                    this.xPosIdx += xa;
                    this.yPosIdx += ya;
                    isMoving = true;
                    GamePacket packet = new GamePacket(this.username, mapIndexToLoc(this.xPosIdx), mapIndexToLoc(this.yPosIdx), this.currentDir, this.attack,true);
                    packet.writeData(GameWindow.gameWindow.client);
                }

            } else {
                isMoving = false;
            }


        } else {

            isMoving = true;

            int targetX = mapIndexToLoc(xPosIdx);
            int targetY = mapIndexToLoc(yPosIdx);

            System.out.println("TARGET X: " + targetX);

            System.out.println("THISSS X: " + this.x);
            int diffX = targetX - this.x;
            System.out.println("DIFFFF X: " + diffX);
            int diffY = targetY - this.y;

            if (diffX > 0) {
                this.x++;
            } else if (diffX < 0) {
                this.x--;
            }
            if (diffY > 0) {
                this.y++;
            } else if (diffY < 0) {
                this.y--;
            }

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
