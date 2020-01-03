package Map;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import Graphics.*;

public class Box {

    public static Box[] boxArr = new Box[16];
    public static final int BOX_SIZE = 32;

    public static Spritesheet boxSheet = new Spritesheet("tileSheet.png",4,4);

    public static final Box EDGE_LEFT = new Box(2,true);
    public static final Box EDGE_RIGHT = new Box(10,true);
    public static final Box EDGE_DOWN = new Box(14,true);
    public static final Box EDGE_UP = new Box(6,true);

    public static final Box CORNER_TOPLEFT = new Box(1,true);
    public static final Box CORNER_TOPRIGHT = new Box(5,true);
    public static final Box CORNER_BTMLEFT = new Box(13,true);
    public static final Box CORNER_BTMRIGHT = new Box(9,true);

    public static final Box BOTTOM = new Box(4,true);
    public static final Box NORM = new Box(8, false);


    public int id;
    public boolean isSolid;

    public Box(int id, boolean isSolid){
        this.id = id;
        this.isSolid = isSolid;
        boxArr[id] = this;
    }

    public static void render(Graphics g,int index, int x, int y){
        int xInd = index%boxSheet.tileX;
        int yInd = index/boxSheet.tileX;
        g.drawImage(boxSheet.spriteArray[xInd][yInd].image,x,y,BOX_SIZE,BOX_SIZE,null);
    }

}
