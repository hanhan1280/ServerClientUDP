package GameEngine;

import menu.MenuWindow;

public class ShooterClientServer {

    public static ShooterClientServer thisGame;

    public static void main(String[] args) {
        new ShooterClientServer().run();
    }

    public ShooterClientServer(){
        thisGame = this;
    }

    public void run() {
        new MenuWindow(this);
    }

}
