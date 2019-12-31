package Map;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Box {

    public static Box[] boxArr = new Box[256];
    public static final int BOX_SIZE = 32;
    public int id;
    protected BufferedImage texture;

    public Box(BufferedImage texture){
        this.texture = texture;
    }

    public void render(Graphics g,int x, int y){
        g.drawImage(texture,x,y,BOX_SIZE,BOX_SIZE,null);
    }

}
