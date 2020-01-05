package Graphics;

import java.awt.*;

public class Fonts {

    public static String characters = " !\"#$%&'()*+,-." +
            "/0123456789:;<=" +
            ">?@ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "[" +
            "\\]^_           ";

    private static Spritesheet spritesheet;
    private static Sprite[][] charArray;

    public Fonts(Spritesheet spritesheet){
        this.spritesheet = spritesheet;
        charArray = spritesheet.spriteArray;
    }

    public static void render(Graphics g, String msg, int xPos, int yPos, float scale) {
        msg = msg.toUpperCase();
        xPos -= ((msg.length()+1)/2)*spritesheet.spriteW*scale;
        for (int i = 0; i < msg.length(); i++) {
            int charIndex = characters.indexOf(msg.charAt(i));
            int x = charIndex%spritesheet.tileX;
            int y = charIndex/spritesheet.tileX;
            if (charIndex >= 0){
                g.drawImage(charArray[x][y].image, (int)(xPos + i*spritesheet.spriteW*scale),yPos,(int)(spritesheet.spriteW*scale),(int)(spritesheet.spriteH*scale),null);
            }
        }
    }
}
