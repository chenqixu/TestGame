package com.cqx.coasterrider.util;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * CV工具
 *
 * @author chenqixu
 */
public class CVUtil {
    private static final Logger logger = LoggerFactory.getLogger(CVUtil.class);

    /**
     * 获取指定视频的帧并保存为图片至指定目录
     *
     * @param videofile 源视频文件路径
     * @param framefile 截取帧的图片存放路径
     * @param frameSize 截取几帧
     * @throws Exception
     */
    public static void fetchFrame(String videofile, String framefile, int frameSize) throws Exception {
        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(videofile);
        ff.start();
        int length = ff.getLengthInFrames();
        int i = 0;
        Frame f;
        while (i < length) {
            // 获取帧
            f = ff.grabFrame();
            // 帧的图片
            opencv_core.IplImage img = f.image;
            if (img != null) {
                i++;
                int owidth = img.width();
                int oheight = img.height();
                // 对截取的帧进行等比例缩放
                int width = 200;
                int height = (int) (((double) width / owidth) * oheight);
                BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
                bi.getGraphics().drawImage(f.image.getBufferedImage().getScaledInstance(width, height, Image.SCALE_SMOOTH),
                        0, 0, null);
                File targetFile = new File(String.format(framefile, i + ""));
                ImageIO.write(bi, "jpg", targetFile);
            }

            if ((i > frameSize) && (f.image != null)) {
                break;
            }
        }
        // ff.flush();
        ff.stop();
    }
}
