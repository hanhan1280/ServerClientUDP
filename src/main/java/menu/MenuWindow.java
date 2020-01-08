package menu;

import GameEngine.GameWindow;
import GameEngine.ShooterClientServer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MenuWindow{

    public String address, port, username;
    public int port2;
    public boolean isServer;
    private ShooterClientServer thisGame;
    private BufferedImage image;

    public MenuWindow(ShooterClientServer thisGame) {
        this.thisGame = thisGame;
        try {
            image = ImageIO.read(MenuWindow.class.getResourceAsStream("/ShooterLogo (1).png"));
        } catch (IOException e) {
            System.out.println("Image was not found closing");
            System.exit(-1);
        }
        dialog();
    }

    private void dialog() {
        String[] labels = {"Enter Username:","Enter IP Address:","Enter Port:"};
        String[] labels2 = {"Use WASD keys to move","Use spacebar to attack","Do not close window if running the server, even if you are dead"};
        String[] fields = {"Username","localHost","8888"};
        JTextField[] textFields = new JTextField[fields.length];
        do {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel,BoxLayout.PAGE_AXIS));
            panel.setBackground(new Color(60,65,80));
            panel.setForeground(Color.WHITE);
            JLabel logo = new JLabel(new ImageIcon(image));//src/main/res/
            logo.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(logo);
            for (int i = 0; i < labels.length; i++) {
                JLabel label = new JLabel(labels[i]);
                label.setForeground(Color.WHITE);
                label.setAlignmentX(Component.CENTER_ALIGNMENT);
                JTextField field = new MyField(fields[i]);
                textFields[i] = field;
                panel.add(label);
                panel.add(field);
            }
            JCheckBox serverCheck = new JCheckBox("Server");
            serverCheck.setBackground(new Color(60,65,80));
            serverCheck.setForeground(Color.WHITE);
            JButton help = new JButton("Help Screen");
            help.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JPanel panel1 = new JPanel();
                    panel1.setLayout(new BoxLayout(panel1,BoxLayout.Y_AXIS));
                    for (int i = 0; i < labels2.length; i++) {
                        JLabel l = new JLabel(labels2[i]);
                        l.setForeground(Color.WHITE);
                        panel1.add(l);
                    }
                    JOptionPane.showMessageDialog(null,panel1, "INSTRUCTIONS",JOptionPane.PLAIN_MESSAGE);
                }
            });
            serverCheck.setAlignmentX(Component.CENTER_ALIGNMENT);
            help.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(serverCheck);
            panel.add(help);
            UIManager UI = new UIManager();
            UI.put("OptionPane.background", new Color(60,65,80));
            UI.put("Panel.background", new Color(60,65,80));
            UI.put("OptionPane.foreground",Color.WHITE);
            int result = JOptionPane.showConfirmDialog(null, panel,
                    "ShooterClientServer", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                this.username = textFields[0].getText();
                this.address = textFields[1].getText();
                this.port = textFields[2].getText();
                this.isServer = serverCheck.isSelected();
            } else {
                System.exit(0);
            }
        } while (!validateFields(this.username, this.address, this.port));
        int portInt = Integer.parseInt(port);
        port2 = portInt;
        new GameWindow(thisGame, username, address, portInt, isServer).init();
    }

    private boolean validateFields(String username, String address, String port) {
        if (username.replace(" ", "").equalsIgnoreCase("")) {
            JOptionPane.showMessageDialog(null, "No name was entered", "ERROR", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (address.equalsIgnoreCase("")) {
            JOptionPane.showMessageDialog(null, "No address was entered", "ERROR", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            int portNum = Integer.parseInt(port);
            if (portNum <= 4000 || portNum >= 9000) {
                JOptionPane.showMessageDialog(null, "Port must be between 4000 and 9000", "ERROR", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Port must be integer and positive", "ERROR", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}

class MyField extends JTextField {

    private String initMessage;

    MyField(String msg) {
        super(msg);
        initMessage = msg;
        setMaximumSize(new Dimension(200,25));
        init();
    }

    private void init() {
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setBorder(null);
        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
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