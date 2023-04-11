package com.cqx.coasterrider;

import com.cqx.coasterrider.model.EnumDirection;
import com.cqx.coasterrider.model.PieceType;
import com.cqx.coasterrider.model.TankeModel;
import com.cqx.coasterrider.model.TkContainer;
import com.cqx.coasterrider.util.BuildMapUtil;
import com.cqx.common.utils.system.SleepUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * TankeDemo
 *
 * @author chenqixu
 */
public class TankeDemo extends JFrame implements KeyListener {
    private static final Logger logger = LoggerFactory.getLogger(TankeDemo.class);
    private int width = 520;
    private int height = 540;
    private TankeModel play01;

    private TkContainer tkContainer;

    public TankeDemo() {
        BuildMapUtil buildMapUtil = new BuildMapUtil();
        try {
            tkContainer = buildMapUtil.buildTanke("d:\\tmp\\data\\show\\示意图.xlsx", width, height);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        MainPanel mainPanel = buildMainPanel();
        add(mainPanel);
        pack();// 根据控件占据总大小设置JFrame窗口大小
        setLocation(400, 150);// 设置窗口初始化位置
        setVisible(true);// 设置窗口可见
        setTitle("TankeDemo");
        addKeyListener(this);
        requestFocus();// 请求焦点
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    SleepUtil.sleepMilliSecond(20);
                    // 面板重新绘制
                    mainPanel.repaint();
                }
            }
        }).start();
        tkContainer.aiStart();
    }

    public static void main(String[] args) {
        new TankeDemo();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyChar()) {
            case 'w':// 上
                tkContainer.move(PieceType.Tanke, play01, EnumDirection.UP);
                break;
            case 's':// 下
                tkContainer.move(PieceType.Tanke, play01, EnumDirection.DOWN);
                break;
            case 'a':// 左
                tkContainer.move(PieceType.Tanke, play01, EnumDirection.LEFT);
                break;
            case 'd':// 右
                tkContainer.move(PieceType.Tanke, play01, EnumDirection.RIGHT);
                break;
            case 'j':
                // 子弹只有40*20
                // 砖块是40*40
                // 一辆坦克一次只能发射一枚炮弹
                play01.bulletSend(tkContainer);
                break;
            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    MainPanel buildMainPanel() {
        return new MainPanel();
    }

    class MainPanel extends JPanel {

        MainPanel() {
            init();
        }

        /**
         * 设置大小
         *
         * @return
         */
        @Override
        public Dimension getPreferredSize() {
            // 画布
            return new Dimension(width, height);
        }

        private void init() {
            play01 = tkContainer.getPlayerList().get(0);
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            setBackground(Color.BLACK);
            // 画炮弹
            for (Map.Entry<String, TankeModel> entry : tkContainer.getTankeBulletMap().entrySet()) {
                TankeModel bullet = entry.getValue();
                g.drawImage(bullet.getBufferedImage(), bullet.getDrawX(), bullet.getDrawY(), null);
            }
            // 基地
            for (TankeModel brick : tkContainer.getHomeList()) {
                g.drawImage(brick.getBufferedImage(), brick.getDrawX(), brick.getDrawY(), null);
            }
            // 玩家-坦克
            for (TankeModel brick : tkContainer.getPlayerList()) {
                g.drawImage(brick.getBufferedImage(), brick.getDrawX(), brick.getDrawY(), null);
            }
            // AI-坦克
            for (TankeModel brick : tkContainer.getAiList()) {
                g.drawImage(brick.getBufferedImage(), brick.getDrawX(), brick.getDrawY(), null);
            }
            // 画砖块
            for (TankeModel brick : tkContainer.getBrickList()) {
                g.drawImage(brick.getBufferedImage(), brick.getDrawX(), brick.getDrawY(), null);
            }
        }
    }
}
