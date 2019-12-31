package GameEngine;

public class GameName {


    public GameWindow gw;

    public static void main(String[] args) {
        new GameName().run();
    }

    public void run(){
        gw = new GameWindow(this);
        gw.init();
    }

}
