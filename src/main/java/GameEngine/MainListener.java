package GameEngine;

import java.awt.event.*;

public class MainListener implements KeyListener{

    public boolean right, left, up, down, attack;
    private GameWindow gw;
    private GameName thisGame;
    private Player player;
    private int[][] map;

    public MainListener(GameWindow gw, GameName thisGame){
        this.gw = gw;
        this.thisGame = thisGame;
        right = false;
        left = false;
        up = false;
        down = false;
        attack = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        keyEvents(e.getKeyCode(),true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyEvents(e.getKeyCode(),false);
    }


    public void keyEvents(int keyCode, boolean keyPressed){
        if(keyCode == KeyEvent.VK_W){
            up = keyPressed;
        }
        if(keyCode == KeyEvent.VK_A){
            left = keyPressed;
        }
        if(keyCode == KeyEvent.VK_S){
            down = keyPressed;
        }
        if(keyCode == KeyEvent.VK_D){
            right = keyPressed;
        }
        if(keyCode == KeyEvent.VK_SPACE){
            attack = keyPressed;
        }
    }

}
