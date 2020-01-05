package GameEngine;

import menu.MenuWindow;

public class ShooterClientServer {


    public GameWindow gw;

    public static void main(String[] args) {
        new ShooterClientServer().run();
    }

    public void run() {
        new MenuWindow(this);
    }

}
