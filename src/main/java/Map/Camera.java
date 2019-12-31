package Map;

import GameEngine.Entity;
import GameEngine.GameWindow;

public class Camera {

    public int xOffset;
    public int yOffset;
    private GameWindow gw;

    public Camera(GameWindow gw, int xOffset, int yOffset){
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.gw = gw;
    }

    public void entityMove(Entity e){
        xOffset = e.xPos - gw.WIDTH/2 + e.getW()/2;
        yOffset = e.yPos - gw.HEIGHT/2 + e.getH()/2;
    }
}
