package com.cqx.coasterrider;

import com.cqx.coasterrider.util.FontUtil;
import com.cqx.coasterrider.util.ImageUtil;
import com.cqx.common.utils.file.FileUtil;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 出租车广告屏“跑马灯”
 *
 * @author chenqixu
 */
public class TaxiAdvertisingScreen extends AbstractScreen {
    /**
     * 字库路径，格式：路径/%s.png
     */
    private String fontPath;
    private String dataStr;

    public TaxiAdvertisingScreen() {
    }

    public TaxiAdvertisingScreen(String fontPath, String dataStr) {
        super(FromRightToLeft, 150, 200, 500, "跑马灯");
        this.dataStr = dataStr;
        buildFont(fontPath, dataStr, rows);
    }

    public static void main(String[] args) {
        new TaxiAdvertisingScreen("d:\\tmp\\data\\font\\"
                , "福州欢迎你！欢迎来到有福之州，祝您旅途愉快！").run();
    }

    /**
     * 构造字库
     *
     * @param fontPath
     * @param dataStr
     * @param size
     */
    public void buildFont(String fontPath, String dataStr, int size) {
        if (!FileUtil.isDirectory(fontPath)) {
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
                    FontUtil.createImage(s, yahei, new File(_tmpFontPath)
                            , size, size, 0, 0);
                } catch (Exception e) {
                    throw new NullPointerException(String.format("构造=%s, 异常信息=%s", s, e.getMessage()));
                }
            } else {
                logger.info("构造={}, 已经存在, 不用构造", s, _tmpFontPath);
            }
        }
    }

    @Override
    protected BlockingDeque<List<Integer>> buildDataQueue() {
        BlockingDeque<List<Integer>> deque = new LinkedBlockingDeque<>();
        for (char c : dataStr.toCharArray()) {
            String s = "" + c;
            String _path = String.format(getFontPath(), s);
            int[][] imgArray = ImageUtil.getPicArrayData(_path);
            for (int i = 0; i < imgArray.length; i++) {
                int[] _imgArray = imgArray[i];
                List<Integer> list1 = new ArrayList<>();
                for (int _img : _imgArray) {
                    list1.add(_img != 0 ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
                }
                // 加入字段的单列
                deque.offer(list1);
            }
            // 加入空列
            deque.offer(spaceList);
        }
        return deque;
    }

    public String getFontPath() {
        return fontPath;
    }

    public void setFontPath(String fontPath) {
        this.fontPath = fontPath;
    }
}
