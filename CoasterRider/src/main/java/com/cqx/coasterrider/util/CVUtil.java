package com.cqx.coasterrider.util;

import com.cqx.common.utils.file.FileUtil;
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
     * @param videofile
     * @param savePath
     * @param frameNumber
     * @throws Exception
     */
    public static void fetchFrame(String videofile, String savePath, int frameNumber) throws Exception {
        fetchFrame(videofile, savePath, frameNumber, false, 0);
    }

    /**
     * 获取指定视频的帧并保存为图片至指定目录，可对保存的图片进行缩放
     *
     * @param videofile   源视频文件路径
     * @param savePath    截取帧的图片存放路径
     * @param frameNumber 截取几帧
     * @param isScale     是否缩放
     * @param ScaleWidth  缩放比例
     * @throws Exception
     */
    public static void fetchFrame(String videofile, String savePath, int frameNumber
            , boolean isScale, int ScaleWidth) throws Exception {
        if (!FileUtil.isFile(videofile)) {
            throw new NullPointerException(String.format("[源视频文件]%s不存在！", videofile));
        }
        if (!FileUtil.isDirectory(savePath)) {
            throw new NullPointerException(String.format("[截取帧的图片存放路径]%s不存在！", savePath));
        }
        if (!savePath.endsWith(File.separator)) {
            savePath += File.separator;
        }
        savePath += "%s.jpg";
        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(videofile);
        ff.start();
        int length = ff.getLengthInFrames();
        logger.info("视频帧数={}, 图片保存路径={}", length, savePath);
        int i = 0;
        Frame f;
        while (i < length) {
            // 获取帧
            f = ff.grabFrame();
            // 帧的图片
            opencv_core.IplImage img = f.image;
            // 图片不为空
            if (img != null) {
                i++;
                // 内容原始大小
                int owidth = img.width();
                int oheight = img.height();
                // 先设置原始大小，再判断是否要进行等比缩放
                int width = owidth;
                int height = oheight;
                // 对截取的帧进行等比例缩放
                if (isScale && ScaleWidth > 0) {
                    width = ScaleWidth;
                    height = (int) (((double) width / owidth) * oheight);
                }
                BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
                bi.getGraphics().drawImage(f.image.getBufferedImage().getScaledInstance(width, height, Image.SCALE_SMOOTH),
                        0, 0, null);
                String _imagePath = String.format(savePath, i + "");
                File targetFile = new File(_imagePath);
                ImageIO.write(bi, "jpg", targetFile);
                logger.info("保存图片={}", _imagePath);
            }
            // 截取完成，退出
            if (i > frameNumber) {
                break;
            }
        }
        // ff.flush();
        ff.stop();
    }
}
