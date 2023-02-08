package com.cqx.coasterrider;

import com.cqx.coasterrider.util.ImageUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 图片秀
 *
 * @author chenqixu
 */
public class ImageShowScreen extends AbstractScreen {
    private String imagePath;

    public ImageShowScreen(String imagePath) {
        super(8, 100, 99, FromRightToLeft, 150, 100, 100, "图片秀");
        this.imagePath = imagePath;
    }

    public static void main(String[] args) {
        new ImageShowScreen("d:\\tmp\\data\\show\\fish100-100.jpg").run();
    }

    @Override
    protected BlockingDeque<List<Integer>> buildDataQueue() {
        BlockingDeque<List<Integer>> deque = new LinkedBlockingDeque<>();
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
        return deque;
    }
}
