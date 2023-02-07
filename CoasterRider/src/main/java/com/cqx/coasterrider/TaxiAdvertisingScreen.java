package com.cqx.coasterrider;

import com.cqx.coasterrider.model.LimitedQueue;
import com.cqx.common.utils.system.SleepUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 出租车广告屏“跑马灯”
 *
 * @author chenqixu
 */
public class TaxiAdvertisingScreen extends JFrame {
    public static final String FromLeftToRight = "FromLeftToRight";
    public static final String FromRightToLeft = "FromRightToLeft";
    private static final Logger logger = LoggerFactory.getLogger(TaxiAdvertisingScreen.class);
    /**
     * 字库路径，格式：路径/%s.png
     */
    private String fontPath;
    private List<List<JButton>> jbList;
    private int rows = 16;
    private int cols = 128;
    private LimitedQueue<Integer> limitedQueue;
    /**
     * 空列
     */
    private List<Integer> spaceList;
    /**
     * 移动速度[1000-0]<br>
     * 越大越慢，建议300
     */
    private int speed;

    public TaxiAdvertisingScreen() {
    }

    public TaxiAdvertisingScreen(String fontPath, String dataStr) {
        this(fontPath, dataStr, FromRightToLeft, 500);
    }

    public TaxiAdvertisingScreen(String fontPath, String dataStr, String type, int speed) {
        // 构造字库
        buildFont(fontPath, dataStr, rows);

        // 移动速度校验
        if (speed > 1000 || speed < 0) {
            speed = 300;
            logger.warn("移动速度只能在0-1000之间，越大越慢，目前调整为默认值300");
        }
        this.speed = speed;
        //主面板
        JPanel panelContent = new JPanel(new GridLayout(rows, cols));// 网格布局
        /**
         * 0 1 2 3
         * 4 5 6 7
         * 8 9 10 11
         * 所以0 4 8是一组
         * 1 5 9是一组
         * 规律就是0, 0+列数, 0+列数+列数
         */
        jbList = new ArrayList<>();
        for (int i = 0; i < cols; i++) {
            jbList.add(new ArrayList<>());
        }
        for (int i = 0; i < cols * rows; i++) {
            List<JButton> rowList = jbList.get(i % cols);
            JButton jb = new JButton();
            jb.setPreferredSize(new Dimension(12, 12));
            rowList.add(jb);
            panelContent.add(jb);
        }

        Container panel = getContentPane();// 主布局容器
        panel.add(panelContent);

        pack();// 根据控件占据总大小设置JFrame窗口大小
        setLocation(200, 500);// 设置窗口初始化位置
        setVisible(true);// 设置窗口可见
        setTitle("跑马灯");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // 空列
        spaceList = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            spaceList.add(0);
        }
        // 初始化 rows * cols 的数据队列
        limitedQueue = new LimitedQueue<>(cols, type, spaceList);
        // 解析内容并刷入数据队列
        buildData(dataStr, type);
    }

    public static void main(String[] args) {
        new TaxiAdvertisingScreen("d:\\tmp\\data\\font\\"
                , "福州欢迎你！欢迎来到有福之州，祝您旅途愉快！"
                , FromRightToLeft
                , 100);
    }

    /**
     * 通过getRGB()方式获得像素矩阵，矩阵是倒过来的
     *
     * @param path
     * @return
     */
    public static int[][] getPicArrayData(String path) {
        try {
            BufferedImage bimg = ImageIO.read(new File(path));
            int[][] data = new int[bimg.getWidth()][bimg.getHeight()];
            for (int i = 0; i < bimg.getWidth(); i++) {
                for (int j = 0; j < bimg.getHeight(); j++) {
                    data[i][j] = bimg.getRGB(i, j);
                }
            }
            return data;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return new int[][]{};
    }

    /**
     * 获得像素矩阵，矩阵是正向的
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static int[][] convertTo2DWithoutUsingGetRGB(String path) throws IOException {
        BufferedImage image = ImageIO.read(new File(path));

        final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        final int width = image.getWidth();
        final int height = image.getHeight();
        final boolean hasAlphaChannel = image.getAlphaRaster() != null;

        int[][] result = new int[height][width];
        if (hasAlphaChannel) {
            final int pixelLength = 4;
            for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
                argb += ((int) pixels[pixel + 1] & 0xff); // blue
                argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
                result[row][col] = argb;
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        } else {
            final int pixelLength = 3;
            for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += -16777216; // 255 alpha
                argb += ((int) pixels[pixel] & 0xff); // blue
                argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
                result[row][col] = argb;
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        }

        return result;
    }

    public String getFontPath() {
        return fontPath;
    }

    public void setFontPath(String fontPath) {
        this.fontPath = fontPath;
    }

    /**
     * 构造字库
     *
     * @param fontPath
     * @param dataStr
     * @param size
     */
    public void buildFont(String fontPath, String dataStr, int size) {
        File file = new File(fontPath);
        if (!file.exists() || !file.isDirectory()) {
            throw new NullPointerException(String.format("[路径]%s不存在！", fontPath));
        }
        if (!fontPath.endsWith(File.separator)) {
            fontPath += File.separator;
        }
        Font yahei = new Font("微软雅黑", Font.PLAIN, size);
        setFontPath(fontPath + "%s.png");
        logger.info("fontPath: {}", getFontPath());
        for (char c : dataStr.toCharArray()) {
            String s = "" + c;
            String _tmpFontPath = String.format(getFontPath(), s);
            File fontFile = new File(_tmpFontPath);
            if (!fontFile.exists() || !fontFile.isFile()) {
                logger.info("构造={}, 构造到路径={}", s, _tmpFontPath);
                try {
                    FontDemo.createImage(s, yahei, new File(_tmpFontPath)
                            , size, size, 0, 0);
                } catch (Exception e) {
                    throw new NullPointerException(String.format("构造=%s, 异常信息=%s", s, e.getMessage()));
                }
            } else {
                logger.info("构造={}, 已经存在, 不用构造", s, _tmpFontPath);
            }
        }
    }

    /**
     * 在容器上绘图
     */
    public void paint() {
        List<List<Integer>> datas = limitedQueue.getAllData();
        for (int i = 0; i < datas.size(); i++) {
            List<JButton> jButtonList = jbList.get(i);
            for (int j = 0; j < datas.get(i).size(); j++) {
                jButtonList.get(j).setBackground(datas.get(i).get(j) == 1 ? Color.BLACK : Color.WHITE);
            }
        }
        // 解决刷新的残影问题
        setVisible(true);
        update(getGraphics());
    }

    /**
     * 解析内容并刷入数据队列
     *
     * @param dataStr
     * @param type
     */
    public void buildData(String dataStr, String type) {
        BlockingDeque<List<Integer>> deque = new LinkedBlockingDeque<>();
        for (char c : dataStr.toCharArray()) {
            String s = "" + c;
            String _path = String.format(getFontPath(), s);
            int[][] imgArray = TaxiAdvertisingScreen.getPicArrayData(_path);
            for (int i = 0; i < imgArray.length; i++) {
                int[] _imgArray = imgArray[i];
                List<Integer> list1 = new ArrayList<>();
                for (int _img : _imgArray) {
                    list1.add(_img != 0 ? 1 : 0);
                }
                // 加入字段的单列
                deque.offer(list1);
            }
            // 加入空列
            deque.offer(spaceList);
        }

        List<Integer> last = null;
        while (true) {
            switch (type) {
                case FromLeftToRight:
                    last = deque.pollLast();
                    // 加到队头
                    if (last != null) deque.offer(last);
                    break;
                case FromRightToLeft:
                    last = deque.poll();
                    // 加到队尾
                    if (last != null) deque.offerLast(last);
                    break;
                default:
                    break;
            }
            if (last == null) {
                break;
            } else {
                limitedQueue.add(last);
                paint();
                SleepUtil.sleepMilliSecond(speed);
            }
        }
    }
}
