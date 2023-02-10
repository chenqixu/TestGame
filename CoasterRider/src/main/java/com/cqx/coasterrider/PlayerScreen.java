package com.cqx.coasterrider;

import com.cqx.coasterrider.util.ImageUtil;
import com.cqx.common.utils.file.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 大屏播放器--点阵方式
 *
 * @author chenqixu
 */
public class PlayerScreen extends AbstractScreen {
    private String playerPath;

    public PlayerScreen(String playerPath) {
        super(3, 112, 200, Replace
                , 100, 200, 200, "灯光秀");
        this.playerPath = playerPath;
    }

    public static void main(String[] args) {
        new PlayerScreen("d:\\tmp\\data\\player\\").run();
    }

    @Override
    protected BlockingDeque<List<Integer>> buildDataQueue() {
        logger.info("{}", playerPath);
        BlockingDeque<List<Integer>> deque = new LinkedBlockingDeque<>();
        // 读取所有的图片，写入队列
        List<Integer> list = new ArrayList<>();
        for (File image : FileUtil.listFilesEndWith(playerPath, ".jpg")) {
            list.add(Integer.valueOf(image.getName().replace(".jpg", "")));
        }
        Collections.sort(list);
        logger.info("{}", list);
        for (int img : list) {
            String imagePath = playerPath + img + ".jpg";
            logger.info("imagePath={}", imagePath);
            int[][] imgArray = ImageUtil.getPicArrayData(imagePath);
            for (int i = 0; i < imgArray.length; i++) {
                int[] _imgArray = imgArray[i];
                List<Integer> column = new ArrayList<>();
                for (int _img : _imgArray) {
                    column.add(_img);
                }
                // 加入图片的单列
                deque.offer(column);
            }
        }
        return deque;
    }
}
