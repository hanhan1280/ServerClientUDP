package Graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Spritesheet {

    private Sprite sprite;
    public int tileX, tileY;
    public Sprite[][] spriteArray;
    public int spriteW, spriteH;

    public Spritesheet(String path, int tileX, int tileY) {
        this.tileX = tileX;
        this.tileY = tileY;
        this.sprite = new Sprite(loadSprite(path));
        getSpriteArray();
    }

    private BufferedImage loadSprite(String file) {
        BufferedImage sprite = null;
        try {
            sprite = ImageIO.read(getClass().getClassLoader().getResourceAsStream(file));
        } catch (Exception e) {
            System.out.println("ERROR: sprite image was not found");
        }
        this.spriteW = sprite.getWidth() / tileX;
        this.spriteH = sprite.getHeight() / tileY;
        return sprite;
    }

    private void getSpriteArray() {
        spriteArray = new Sprite[tileX][tileY];
        for (int x = 0; x < tileX; x++) {
            for (int y = 0; y < tileY; y++) {
                spriteArray[x][y] = sprite.getSprite(x * spriteW, y * spriteH, spriteW, spriteH);
            }
        }
    }

    public Sprite[] getSpriteAnimation(int i) {
        return spriteArray[i];
    }

}
