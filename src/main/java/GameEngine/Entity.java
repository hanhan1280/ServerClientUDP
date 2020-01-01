package GameEngine;

import Graphics.Animation;
import Graphics.Sprite;
import Graphics.Spritesheet;

import java.awt.*;

public abstract class Entity {

    public int xPosIdx, yPosIdx;
    protected final int DOWN = 0, LEFT = 1, UP=2, RIGHT = 3;
    //protected boolean up,right,down,left;
    protected Animation ani;
    public boolean attack, isMoving;
    public int currentDir, prevDir;

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    protected int w, h;

    private Spritesheet sprite;

    public Entity(Spritesheet sprite){
        this.sprite = sprite;
        this.w = sprite.spriteW;
        this.h = sprite.spriteH;
        this.ani = new Animation();
        setAnimation(DOWN, sprite.getSpriteAnimation(DOWN), 10);
    }

    public void setAnimation(int i, Sprite[] frames, int delay){
        prevDir = i;
        ani.delay = delay;
        ani.setFrames(frames);
    }

    public void update(){
        animate();
        ani.update();
    }

    public void animate() {
        if (ani.delay == -1) {
            return;
        }
        if(isMoving){
            if(prevDir!=currentDir){
                setAnimation(currentDir,sprite.getSpriteAnimation(currentDir),10);
            }
        }else{
            setAnimation(currentDir,sprite.getSpriteAnimation(currentDir),-1);
        }
    }

    abstract public void render(Graphics g);
}
