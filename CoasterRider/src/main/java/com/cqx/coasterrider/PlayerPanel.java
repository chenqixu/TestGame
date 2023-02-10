package com.cqx.coasterrider;

import com.cqx.common.utils.file.FileUtil;
import com.cqx.common.utils.system.SleepUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 大屏播放器--替换图片方式
 *
 * @author chenqixu
 */
public class PlayerPanel extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(PlayerPanel.class);
    private String imagePath;
    private int width;
    private int height;
    private int sleep;

    public PlayerPanel(String imagePath, int frameNumber, int width, int height) {
        this.imagePath = imagePath;
        this.width = width;
        this.height = height;
        this.sleep = 1000 / frameNumber;
        add(buildPlayer());// 主面板
        pack();// 根据控件占据总大小设置JFrame窗口大小
        setLocation(0, 0);// 设置窗口初始化位置
        setVisible(true);// 设置窗口可见
        setTitle("图片播放器");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new PlayerPanel("d:\\tmp\\data\\player\\", 29, 200, 112);
    }

    private Player buildPlayer() {
        return new Player();
    }

    /**
     * 播放器面板
     */
    class Player extends Panel {

        /**
         * 设置大小
         *
         * @return
         */
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(width, height);
        }

        /**
         * 绘图
         *
         * @param g
         */
        @Override
        public void paint(Graphics g) {
            super.paint(g);
            logger.info("imagePath={}", imagePath);
            // 读取所有的图片，写入队列
            List<Integer> list = new ArrayList<>();
            for (File image : FileUtil.listFilesEndWith(imagePath, ".jpg")) {
                list.add(Integer.valueOf(image.getName().replace(".jpg", "")));
            }
            Collections.sort(list);
            logger.info("{}", list);
            try {
                for (int img : list) {
                    String _imagePath = imagePath + img + ".jpg";
                    BufferedImage bfImage = ImageIO.read(new File(_imagePath));
                    logger.info("_imagePath={}, width={}, height={}", _imagePath, bfImage.getWidth(), bfImage.getHeight());
                    g.drawImage(bfImage, 0, 0, bfImage.getWidth(), bfImage.getHeight(), null);
                    // 速度，比如1秒28帧，那么休眠时间就是1000/28
                    SleepUtil.sleepMilliSecond(sleep);
                }
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
