package com.cqx.coasterrider.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * 字体工具
 *
 * @author chenqixu
 */
public class FontUtil {

    /**
     * 根据输入的参数在对应路径生成对应的png格式图片字体，背景为透明
     *
     * @param str     文字
     * @param font    字体类型
     * @param outFile 图片输出路径
     * @param width   图片宽度
     * @param height  图片高度
     * @param x       文字的横坐标（为0代表需要程序自动计算）
     * @param y       文字的纵坐标（为0代表需要程序自动计算）
     * @throws Exception Exception
     */
    public static void createImage(String str, Font font, File outFile,
                                   Integer width, Integer height, int x, int y) throws Exception {
        // 创建BufferedImage对象
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 获取Graphics2D
        Graphics2D g2d = image.createGraphics();

        // ----------  增加下面的代码使得背景透明  -----------------
        image = g2d.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        g2d.dispose();
        g2d = image.createGraphics();
        // ----------  背景透明代码结束  -----------------

        // 设置字体颜色
        g2d.setColor(Color.black);
        // 设置字体类型
        g2d.setFont(font);

        // 用于获得垂直居中
        FontMetrics fm = g2d.getFontMetrics(font);
        int ascent = fm.getAscent();
        int descent = fm.getDescent();
        if (x == 0) {
            x = (width - fm.stringWidth(str)) / 2;
        }
        if (y == 0) {
            y = (height - (ascent + descent)) / 2 + ascent;
        }
        // 将字符写入到图片内
        g2d.drawString(str, x, y);
        // 释放对象
        g2d.dispose();

        // 保存文件
        ImageIO.write(image, "png", outFile);
    }
}
