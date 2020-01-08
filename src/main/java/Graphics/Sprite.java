package Graphics;

import java.awt.image.BufferedImage;

public class Sprite {

    public BufferedImage image;

    Sprite(BufferedImage image){
        this.image = image;
    }

    Sprite getSprite(int x, int y, int spriteW, int spriteH){
        return new Sprite(image.getSubimage(x, y, spriteW, spriteH));
    }

}
