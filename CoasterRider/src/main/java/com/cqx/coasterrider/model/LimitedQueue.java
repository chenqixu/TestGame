package com.cqx.coasterrider.model;

import com.cqx.coasterrider.TaxiAdvertisingScreen;

import java.util.ArrayList;
import java.util.List;

/**
 * 有限队列
 *
 * @author chenqixu
 */
public class LimitedQueue<T> {
    private final Object lock = new Object();
    /**
     * 队列大小
     */
    private int size;
    /**
     * 内容移动方向
     */
    private String type;
    private List<List<T>> allData = new ArrayList<>();

    public LimitedQueue(int size, String type, List<T> spaceList) {
        this.size = size;
        this.type = type;
        for (int i = 0; i < size; i++) {
            allData.add(spaceList);
        }
    }

    public void add(List<T> columnData) {
        synchronized (lock) {
            switch (type) {
                case TaxiAdvertisingScreen.FromRightToLeft:
                    if (allData.size() == size) {
                        // 抛弃队头
                        allData.remove(0);
                    }
                    // 加到队尾巴
                    allData.add(columnData);
                    break;
                default:
                    if (allData.size() == size) {
                        // 抛弃队尾
                        allData.remove(size - 1);
                    }
                    // 加到队头
                    allData.add(0, columnData);
                    break;
            }
        }
    }

    public List<List<T>> getAllData() {
        synchronized (lock) {
            return allData;
        }
    }
}
