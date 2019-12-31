package GameEngine;

import Graphics.Spritesheet;

import java.awt.*;

public class Bullet extends Entity{

    public final int DAMAGE = 25;
    public final int MAX_DISTANCE = 10;

    public Bullet( Spritesheet sprite) {
        super(sprite);
    }

    public void update(){
        super.update();
    }

    @Override
    public void render(Graphics g) {

    }
}
