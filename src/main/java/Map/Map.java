package Map;

import GameEngine.Player.Entity;
import GameEngine.GameWindow;

import java.awt.*;

import GameEngine.Player.Player;

import java.util.ArrayList;

public class Map {

    private GameWindow gw;
    public static final int MAP_SIZE = 32;
    private int[][] map;
    private ArrayList<Entity> entities = new ArrayList<>();

    public Map(GameWindow gw) {
        this.gw = gw;
        loadMap();
    }

    public void loadMap() {
        map = new int[MAP_SIZE][MAP_SIZE];
        for (int y = 0; y < MAP_SIZE; y++) {
            for (int x = 0; x < MAP_SIZE; x++) {
                if (y == MAP_SIZE - 1) {
                    map[x][y] = Box.BOTTOM.id;
                } else if (x == 0 && y == 0) {
                    map[x][y] = Box.CORNER_TOPLEFT.id;
                } else if (x == MAP_SIZE - 1 && y == 0) {
                    map[x][y] = Box.CORNER_TOPRIGHT.id;
                } else if (x == 0 && y == MAP_SIZE - 2) {
                    map[x][y] = Box.CORNER_BTMLEFT.id;
                } else if (x == MAP_SIZE - 1 && y == MAP_SIZE - 2) {
                    map[x][y] = Box.CORNER_BTMRIGHT.id;
                } else if (x == 0) {
                    map[x][y] = Box.EDGE_LEFT.id;
                } else if (y == 0) {
                    map[x][y] = Box.EDGE_UP.id;
                } else if (x == MAP_SIZE - 1) {
                    map[x][y] = Box.EDGE_RIGHT.id;
                } else if (y == MAP_SIZE - 2) {
                    map[x][y] = Box.EDGE_DOWN.id;
                } else {
                    map[x][y] = Box.NORM.id;
                }
            }
        }
    }

    public Box getBoxID(int x, int y) {
        Box b = Box.boxArr[map[x][y]];
        return b;
    }

    public void render(Graphics g) {
        for (int y = 0; y < MAP_SIZE; y++) {
            for (int x = 0; x < MAP_SIZE; x++) {
                Box.render(g, map[x][y], x * Box.BOX_SIZE - gw.camera.xOffset, y * Box.BOX_SIZE - gw.camera.yOffset);
            }
        }
    }

    public void renderEntity(Graphics g) {
        for (Entity e : getEntityList()) {
            e.render(g);
        }
    }

    public void update() {
        for (Entity e : getEntityList()) {
            e.update();
        }
    }

    public int getPlayerByIndex(String username) {
        int index = 0;
        for (Entity e : getEntityList()) {
            if (((Player) e).getUsername().equals(username)) {
                break;
            }
            index++;
        }
        return index;
    }

    public synchronized ArrayList<Entity> getEntityList() {
        return this.entities;
    }

    public synchronized void addEntity(Entity e) {
        this.getEntityList().add(e);
    }

    public synchronized void removeEntity(String username) {
        for (int i = 0; i < getEntityList().size(); i++) {
            Entity e = getEntityList().get(i);
            if (((Player) e).getUsername().equals(username)) {
                getEntityList().remove(i);
                i--;
            }
        }
    }

    public synchronized void playersMove(String username, int x, int y, int currentDir, boolean isMoving) {
        int index = getPlayerByIndex(username);
        Player player = (Player) getEntityList().get(index);
        player.x = x;
        player.y = y;
        player.currentDir = currentDir;
        player.isMoving = isMoving;
    }

    public synchronized void playersAttack(String username, boolean isAttack) {
        int index = getPlayerByIndex(username);
        Player player = (Player) getEntityList().get(index);
        player.attack = isAttack;
    }
}
