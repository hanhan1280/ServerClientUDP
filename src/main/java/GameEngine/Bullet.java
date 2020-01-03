package GameEngine;

import Graphics.*;

import java.awt.*;

public class Bullet{

    public static Spritesheet sheet = new Spritesheet("fireballSheet.png",4,3);
    public final int DAMAGE = 25;
    public final int MAX_DISTANCE = 10;
    Animation ani;
    int initX,initY;
    int x, y, xPos, yPos, currentDir, prevDir;
    final int DOWN = 0, LEFT = 1, UP=2, RIGHT = 3;

    public Bullet(int x, int y, int currentDir) {
        this.ani = new Animation();
        this.x = x;
        this.y = y;
        this.initX = x;
        this.initY = y;
        this.xPos = indexToLoc(this.x);
        this.yPos = indexToLoc(this.y);
        this.currentDir = currentDir;
        setAnimation(currentDir,sheet.getSpriteAnimation(currentDir),1000);
    }

    public void update(){
        ani.update();
        move();
    }

    public void move(){
        if(xPos == indexToLoc(x) && yPos == indexToLoc(y)){
            int xa = 0, ya =0;
            if(currentDir == UP){
                ya = -1;
            }
            else if (currentDir == RIGHT){
                xa = 1;
            }
            else if (currentDir == LEFT){
                xa = -1;
            }
            else if (currentDir == DOWN){
                ya = 1;
            }
            if(xa != 0 || ya != 0){
                x+=xa;
                y+=ya;
            }
        }else{
            int targetX = indexToLoc(x);
            int targetY = indexToLoc(y);
            if(targetX -xPos > 0){
                xPos+=8;
            }
            else if(targetX - xPos < 0){
                xPos-=8;
            }
            if(targetY -yPos > 0){
                yPos+=8;
            }
            else if(targetY - yPos < 0){
                yPos-=8;
            }
        }

    }

    public void setAnimation(int i, Sprite[] frames, int delay){
        prevDir = i;
        ani.delay = delay;
        ani.setFrames(frames);
    }

    public int indexToLoc(int index){
        return index * 32 - 16;
    }

    public void render(Graphics g) {
        g.drawImage(ani.getImage().image, xPos - GameWindow.gameWindow.camera.xOffset, yPos - GameWindow.gameWindow.camera.yOffset + 5, null);
    }
}
