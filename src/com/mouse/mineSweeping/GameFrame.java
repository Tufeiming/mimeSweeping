package com.mouse.mineSweeping;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    public GameFrame() {
        JFrame frame = new JFrame("小tu的扫雷");
        GamePanel panel = new GamePanel(16, 16);
        Container container = frame.getContentPane();
        container.add(panel);
        frame.setSize(panel.getSize());
        frame.setSize(panel.getSize());
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocation(500, 400);
    }


}
