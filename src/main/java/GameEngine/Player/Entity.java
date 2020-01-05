package GameEngine.Player;

import GameEngine.Player.Bullet;
import Graphics.*;

import java.awt.Graphics;
import java.util.ArrayList;

public abstract class Entity {

    public int xPos, yPos;
    protected final int DOWN = 0, LEFT = 1, UP = 2, RIGHT = 3, FIRING_DELAY = 500;
    public int x, y; //x and y indexes on map
    protected long firingTime;
    protected Animation ani;
    public boolean attack, isMoving;
    public int currentDir, prevDir;
    public ArrayList<Bullet> bullets = new ArrayList<>();
    protected int w, h;

    private Spritesheet sprite;

    public Entity(Spritesheet sprite) {
        this.sprite = sprite;
        this.w = sprite.spriteW;
        this.h = sprite.spriteH;
        this.ani = new Animation();
        setAnimation(DOWN, sprite.getSpriteAnimation(DOWN), 10);
    }

    public void setAnimation(int i, Sprite[] frames, int delay) {
        prevDir = i;
        ani.delay = delay;
        ani.setFrames(frames);
    }

    public void update() {
        animate();
        ani.update();
    }

    public void animate() {
        if (ani.delay == -1) {
            return;
        }
        if (attack) {
            long elapsed = (System.nanoTime() - firingTime) / 1000000;
            if (elapsed >= FIRING_DELAY) {
                int xB = 0, yB = 0;
                if (this.currentDir == RIGHT) {
                    xB = 1;
                }
                if (this.currentDir == LEFT) {
                    xB = -1;
                }
                if (this.currentDir == UP) {
                    yB = -1;
                }
                if (this.currentDir == DOWN) {
                    yB = 1;
                }
                getBullets().add(new Bullet(this.x + xB, this.y + yB, this.currentDir));
                firingTime = System.nanoTime();
            }
        }
        if (isMoving) {
            if (prevDir != currentDir) {
                setAnimation(currentDir, sprite.getSpriteAnimation(currentDir), 10);
            }
        } else {
            setAnimation(currentDir, sprite.getSpriteAnimation(currentDir), -1);
        }

    }


    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    public int indexToLoc(int index) {
        return index * 32 - 16;
    }

    protected synchronized ArrayList<Bullet> getBullets() {
        return bullets;
    }

    abstract public void render(Graphics g);
}
