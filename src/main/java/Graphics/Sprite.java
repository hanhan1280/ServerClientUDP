package Graphics;

import java.awt.image.BufferedImage;

public class Sprite {

    public BufferedImage image;
    //public int w;
    //public int h;

    public Sprite(BufferedImage image){
        this.image = image;
    }

    public Sprite getSprite(int x, int y, int spriteW, int spriteH){
        return new Sprite(image.getSubimage(x, y, spriteW, spriteH));
    }

}
