package com.mouse.mineSweeping;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GamePanel extends JPanel {
    // 行数
    private int rows;
    // 列数
    private int cols;
    // 炸弹数
    private int bombCount;

    // 每个方格宽度
    private final int GRID_WIDTH = 20;
    // 每个方格高度
    private final int GRID_HEIGHT = 20;

    //存储每一个方格的绘制信息
    private JLabel[][] labels;
    private MyButton[][] buttons;

    private final int[][] offset = {{-1, -1}, {0, -1}, {1, -1}, {1, 0},
            {1, 1}, {0, 1}, {-1, 1}, {-1, 0}};

    /**
     * 无参构造函数
     */
    public GamePanel() {
        this.rows = 9;
        this.cols = 9;
        this.bombCount = rows * cols / 10;
        this.labels = new JLabel[rows][cols];
        this.buttons = new MyButton[rows][cols];
        this.setLayout(null);

        initButtons();
        initLabels();
    }

    /**
     * 有参构造函数
     *
     * @param rows
     * @param cols
     */
    public GamePanel(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.bombCount = rows * cols / 10;
        this.labels = new JLabel[rows][cols];
        this.buttons = new MyButton[rows][cols];
        this.setLayout(null);

        initButtons();
        initLabels();
    }

    /**
     * 初始化界面
     */
    private void initLabels() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                JLabel label = new JLabel("", JLabel.CENTER);
                // 设置每个小方格的边界
                label.setBounds(j * GRID_WIDTH, i * GRID_HEIGHT,
                        GRID_WIDTH, GRID_HEIGHT);
                // 绘制方格边框
                label.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                // 设置方格为透明
                label.setOpaque(true);
                // 背景填充颜色为绿色
                label.setBackground(Color.GREEN);
                // 将方格放入JPanel
                this.add(label);
                // 将变量存入数组
                labels[i][j] = label;
            }
        }
        randomBomb();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                numberAndSpace(i, j);
            }
        }

    }

    /**
     * 初始化按钮，将JLabel盖住
     */
    private void initButtons() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                MyButton button = new MyButton();
                button.setBounds(j * GRID_WIDTH, i * GRID_HEIGHT,
                        GRID_WIDTH, GRID_HEIGHT);
                this.add(button);
                buttons[i][j] = button;
                button.setRow(i);
                button.setCol(j);

                button.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        int tempBtn = e.getButton();
                        if (tempBtn == MouseEvent.BUTTON1) {
                            open((MyButton) e.getSource());
                        } else if (tempBtn == MouseEvent.BUTTON3) {
//                            button.setText("🚩");
//                            Font f = new Font("微软雅黑", Font.BOLD, 10);
//                            button.setFont(f);
                            button.setIcon(new ImageIcon("./pic/flag.jpg"));
                        }
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {

                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {

                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {

                    }

                    @Override
                    public void mouseExited(MouseEvent e) {

                    }

                });
            }
        }
    }

    /**
     * 计算并长度和高度
     *
     * @return
     */
    public Dimension getSize() {
        return new Dimension(cols * GRID_WIDTH + 7,
                rows * GRID_HEIGHT + 36);
    }

    /**
     * 随机产生炸弹
     */
    private void randomBomb() {

        for (int i = 0; i < bombCount; i++) {
            // 随机生成一个行坐标
            int row = (int) (Math.random() * rows);
            // 随机生成一个列坐标
            int col = (int) (Math.random() * cols);
            while (labels[row][col].getText() != "") {
                row = (int) (Math.random() * rows);
                col = (int) (Math.random() * cols);
            }
            labels[row][col].setText("💣");
            labels[row][col].setBackground(Color.GRAY);
            labels[row][col].setForeground(Color.RED);
        }
    }

    /**
     * 填上数字和空格 int row, int col
     */
    private void numberAndSpace(int row, int col) {
        if (labels[row][col].getText() != "💣") {
            // 炸弹的数目
            int count = 0;
            for (int i = 0; i < offset.length; i++) {
                int newRow = row + offset[i][0];
                int newCol = col + offset[i][1];
                if (verify(newRow, newCol)) {
                    if (labels[newRow][newCol].getText() == "💣") {
                        count++;
                    }
                }
            }
            if (count == 0) {
                labels[row][col].setText("");
            } else {
                labels[row][col].setText(String.valueOf(count));
            }
        }
    }

    /**
     * 判断坐标是否越界
     *
     * @param row
     * @param col
     * @return
     */
    private boolean verify(int row, int col) {
        boolean flag = row >= 0 && row < this.rows &&
                col >= 0 && col < this.cols;
        return flag;
    }

    /**
     * 单击按钮是打开或成片打开
     *
     * @param button
     */
    private void open(MyButton button) {
        // 先将当前按钮设置为不可见，即打开了按钮
        button.setVisible(false);
        if (isWin()) {
            int choice = JOptionPane.showConfirmDialog(null,
                    "你赢了！再玩一局？",
                    "提示", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                // 使用SwingUtilities.getWindowAncestor(…)方法，将返回一个可以投射到顶级类型的窗口
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
                // 关闭当前窗口
                frame.dispose();
                // 打开新的窗口
                new GameFrame();
            }
            return;
        }
        // 判断按钮中为数字还是空
        switch (labels[button.getRow()][button.getCol()].getText()) {
            // 如果是炸弹，则全部按钮打开，GG
            case "💣":
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        buttons[i][j].setVisible(false);
                    }
                }
                int choice = JOptionPane.showConfirmDialog(null,
                        "踩到雷，你GG啦！再玩一局？",
                        "提示", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    // 使用SwingUtilities.getWindowAncestor(…)方法，将返回一个可以投射到顶级类型的窗口
                    JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
                    // 关闭当前窗口
                    frame.dispose();
                    // 打开新的窗口
                    new GameFrame();
                }
                break;
            // 如果为空，则将它周围的按钮都打开，进入递归
            case "":
                for (int[] off : offset) {
                    int newRow = button.getRow() + off[0];
                    int newCol = button.getCol() + off[1];
                    if (verify(newRow, newCol)) {
                        MyButton newButton = buttons[newRow][newCol];
                        if (newButton.isVisible()) {
                            open(newButton);
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 判断游戏是否胜利
     *
     * @return
     */
    public boolean isWin() {
        int count = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (buttons[i][j].isVisible()) {
                    count++;
                }
            }
        }
        if (count == bombCount) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    buttons[i][j].setVisible(false);
                }
            }
            return true;
        }
        return false;
    }
}

