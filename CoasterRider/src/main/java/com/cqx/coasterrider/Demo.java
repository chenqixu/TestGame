package com.cqx.coasterrider;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Demo
 *
 * @author chenqixu
 */
public class Demo extends JFrame implements KeyListener {
    private DemoPanel demoPanel;

    public Demo() {
        setSize(1300, 1000);
        demoPanel = buildDemoPanel();
        add(demoPanel);
        setLocation(0, 0);//设置窗口初始化位置
        setVisible(true);//设置窗口可见
        addKeyListener(this);
        requestFocus();//请求焦点
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new Demo();
    }

    public DemoPanel buildDemoPanel() {
        return new DemoPanel();
    }

    @Override
    public void keyTyped(KeyEvent e) {
//        System.out.println("[keyTyped]" + e.getKeyChar());
    }

    @Override
    public void keyPressed(KeyEvent e) {
//        System.out.println("[keyPressed]" + e.getKeyChar());
        demoPanel.setKey(e.getKeyChar());
        demoPanel.repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
//        System.out.println("[keyReleased]" + e.getKeyChar());
    }

    /**
     * 以左上角的顶点为0,0，横坐标轴为x，纵坐标轴为y<br>
     * 假设坦克的长宽一样<br>
     * 假设坦克的炮塔长50，宽20，在坦克的中间<br>
     * 向左，坐标轴为x-50，y+40
     * 向右，坐标轴为x+长，y+40
     * 向上，坐标轴为x+40，y-50
     * 向下，坐标轴为x+40，y+长
     */
    class DemoPanel extends JPanel {
        int x = 0;
        int y = 0;
        int width = 100;
        int heigth = 100;
        int pg_width = 50;
        int pg_heigth = 20;
        private Graphics g;
        private char key;
        private char lastKey;

        void setKey(char key) {
            if (this.lastKey == 0) {
                this.lastKey = key;
            } else {
                this.lastKey = this.key;
            }
            this.key = key;
        }

        boolean isMove() {
            return this.key == this.lastKey;
        }

        void move(char key) {
            if (isMove()) {// 移动，每次移动10
                switch (key) {
                    case 'w':
                        // 向上

                        break;
                    case 's':
                        // 向下
                        break;
                    case 'a':
                        // 向左
                        break;
                    case 'd':
                    default:
                        // 向右
                        break;
                }
            } else {// 改变方向
                switch (key) {
                    case 'w':
                        // 炮管向上
                        g.fillRect(x + heigth / 2 - pg_heigth / 2, y - pg_width, pg_width, pg_heigth);
                        g.drawString(String.format("[炮管向上]x=%s, y=%s", x + heigth / 2 - pg_heigth / 2, y - pg_width), 400, 400);
                        break;
                    case 's':
                        // 炮管向下
                        g.fillRect(x + heigth / 2 - pg_heigth / 2, y + width, pg_heigth, pg_width);
                        g.drawString(String.format("[炮管向下]x=%s, y=%s", x + heigth / 2 - pg_heigth / 2, y + width), 400, 400);
                        break;
                    case 'a':
                        // 炮管向左
                        g.fillRect(x - pg_width, y + heigth / 2 - pg_heigth / 2, pg_width, pg_heigth);
                        g.drawString(String.format("[炮管向左]x=%s, y=%s", x - pg_width, y + heigth / 2 - pg_heigth / 2), 400, 400);
                        break;
                    case 'd':
                    default:
                        // 炮管向右
                        g.fillRect(x + width, y + heigth / 2 - pg_heigth / 2, pg_width, pg_heigth);
                        g.drawString(String.format("[炮管向右]x=%s, y=%s", x + width, y + heigth / 2 - pg_heigth / 2), 400, 400);
                        break;
                }
            }
        }

        public void paint(Graphics g) {
            this.g = g;
            g.clearRect(0, 0, 1300, 1000);
            g.setColor(Color.GRAY);
            // 坦克
            g.fillRect(x, y, width, heigth);
            // 炮管
            move(this.key);
//            // 直线
//            g.drawLine(100, 50, 100, 100);
//            // 内容
//            g.drawString("Hello World !", 400, 400);
//            // 改变颜色
//            g.setColor(Color.green);
//            // 矩形
//            g.drawRect(100, 100, 200, 100);
//            // 填充矩形
//            g.fillRect(350, 100, 200, 100);
//            // 圆角矩形
//            g.drawRoundRect(600, 100, 200, 100, 50, 50);
//            // 填充圆角矩形
//            g.fillRoundRect(850, 100, 200, 100, 20, 100);
//            // 椭圆
//            g.drawOval(100, 250, 100, 100);
//            // 填充椭圆
//            g.fillOval(250, 250, 80, 100);
//            // 圆弧[矩形|椭圆]
//            g.drawArc(350, 250, 100, 100, 0, 90);
//            // 填充圆弧
//            g.fillArc(450, 250, 100, 100, 0, 90);
//            g.fillArc(550, 250, 100, 100, 10, 80);
        }
    }
}