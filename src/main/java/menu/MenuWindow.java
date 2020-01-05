package menu;

import GameEngine.GameWindow;
import GameEngine.ShooterClientServer;
import Graphics.Fonts;
import Graphics.Spritesheet;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.InetAddress;

public class MenuWindow extends JFrame {


    public String address, port, username;
    public boolean isServer;

    public final int WIDTH = 800;
    public final int HEIGHT = WIDTH/12 * 9;
    private ShooterClientServer thisGame;

    public MenuWindow(ShooterClientServer thisGame){
        this.thisGame = thisGame;
        init();
    }

    public void init(){
        do {
            JTextField usernameField = new myField("username");
            JTextField addressField = new myField("localHost");
            JTextField portField = new myField("8888");
            JCheckBox serverCheck = new JCheckBox("Server");
            JPanel panel = new JPanel();
            panel.setSize(new Dimension(800,600));
            panel.add(usernameField);
            panel.add(addressField);
            panel.add(portField);
            panel.add(serverCheck);
            int result = JOptionPane.showConfirmDialog(null, panel,
                    "ShooterClientServer", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                this.username = usernameField.getText();
                this.address = addressField.getText();
                this.port = portField.getText();
                this.isServer = serverCheck.isSelected();
            }
            else if(result == JOptionPane.CANCEL_OPTION){
                System.exit(0);
            }
        }while (!validateFields(this.username,this.address,this.port));
        int portInt = Integer.parseInt(port);
        new GameWindow(thisGame, username, address, portInt, isServer).init();
    }

    boolean validateFields(String username, String address, String port){
        if(username.replace(" ", "").equalsIgnoreCase("")){
            JOptionPane.showMessageDialog(null, "No name was entered", "ERROR", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(address.equalsIgnoreCase("")){
            JOptionPane.showMessageDialog(null, "No address was entered", "ERROR", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try{
            int portNum = Integer.parseInt(port);
            if(portNum <= 4000 || portNum >= 9000){
                JOptionPane.showMessageDialog(null,"Port must be between 4000 and 9000", "ERROR", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null,"Port must be integer and positive", "ERROR", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}

class myField extends JTextField{

    String initMessage;

    myField(String msg){
        super(msg);
        initMessage = msg;
        init();
    }

    void init(){
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setSize(new Dimension(600,50));
        setBorder(null);
        addFocusListener(new FocusListener(){
            @Override
            public void focusGained(FocusEvent e){
                if (getText().replace(" ", "").equalsIgnoreCase(initMessage))
                    setText("");
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (getText().replace(" ", "").equalsIgnoreCase(""))
                    setText(initMessage);
            }
        });
    }
}