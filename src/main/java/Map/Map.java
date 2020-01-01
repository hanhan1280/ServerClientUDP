package Map;

import GameEngine.Entity;
import GameEngine.GameWindow;

import javax.imageio.ImageIO;
import java.awt.*;

import GameEngine.Player;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Map {

    private GameWindow gw;
    private BufferedImage image;
    public final int MAP_SIZE = 32;
    private int[][] map;
    private ArrayList<Entity> entities = new ArrayList<>();

    public Map(GameWindow gw){
        try {
            this.image = ImageIO.read(Box.class.getResourceAsStream("/Texture.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.gw = gw;
        loadMap();
    }

    public void loadMap(){
        map = new int[MAP_SIZE][MAP_SIZE];
        for (int y = 0; y < MAP_SIZE; y++) {
            for (int x = 0; x < MAP_SIZE; x++) {
                map[x][y] = 0;
            }
        }
    }

    public Box getBoxID(int x, int y){
        Box b = Box.boxArr[map[x][y]];
        return b;
    }

    public void render(Graphics g){
        for (int y = 0; y < MAP_SIZE; y++) {
            for (int x = 0; x < MAP_SIZE; x++) {
               new Box(image).render(g,x*Box.BOX_SIZE-gw.camera.xOffset,y*Box.BOX_SIZE-gw.camera.yOffset);
            }
        }
    }

    public void renderEntity(Graphics g){
        for (Entity e : entities) {
            e.render(g);
        }
    }

    public synchronized ArrayList<Entity> getEntityList(){
        return this.entities;
    }
    public synchronized void addEntity(Entity e){
        entities.add(e);
    }

    public void update(){
        for (Entity e : entities) {
            e.update();
        }
    }

    public int getPlayerByIndex(String username){
        int index = 0;
        for (Entity e : entities) {
            if(((Player)e).getUsername().equals(username)){
                break;
            }
            index++;
        }
        return index;
    }

    public synchronized void playersMove(String username, int x ,int y, int currentDir, boolean isAttack, boolean isMoving){
        int index = getPlayerByIndex(username);
        Player player = (Player) getEntityList().get(index);
        player.xPosIdx = reverseMap(x);
        player.yPosIdx = reverseMap(y);
        player.currentDir = currentDir;
        player.attack = isAttack;
        player.isMoving = isMoving;
    }

    int reverseMap(int xPos) {
        return (xPos - 16)/32;
    }

}
