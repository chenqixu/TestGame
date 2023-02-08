package com.cqx.coasterrider;

import com.cqx.coasterrider.util.ImageUtil;
import com.cqx.common.utils.system.SleepUtil;
import com.cqx.common.utils.thread.ThreadTool;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class TaxiAdvertisingScreenTest {
    private static final Logger logger = LoggerFactory.getLogger(TaxiAdvertisingScreenTest.class);
    private TaxiAdvertisingScreen taxiAdvertisingScreen;
    private String path = "d:\\tmp\\data\\font\\%s.png";

    @Before
    public void setUp() throws Exception {
        taxiAdvertisingScreen = new TaxiAdvertisingScreen();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void buildFont() {
        taxiAdvertisingScreen.buildFont("d:\\tmp\\data\\font\\"
                , "福州欢迎你！", 16);
    }

    @Test
    public void getPicArrayData() {
        int[][] imgArray = ImageUtil.getPicArrayData(path);
        if (imgArray.length > 0) {
            System.out.println(String.format("width: %s, height: %s", imgArray[0].length, imgArray.length));
        }
        for (int[] _imgArray : imgArray) {
            for (int _img : _imgArray) {
                System.out.print(String.format("%s ", _img != 0 ? 1 : 0));
            }
            System.out.println();
        }
    }

    @Test
    public void convertTo2DWithoutUsingGetRGB() throws IOException {
        int[][] imgArray = ImageUtil.convertTo2DWithoutUsingGetRGB(path);
        if (imgArray.length > 0) {
            System.out.println(String.format("width: %s, height: %s", imgArray[0].length, imgArray.length));
        }
        for (int[] _imgArray : imgArray) {
            for (int _img : _imgArray) {
                System.out.print(String.format("%s ", _img != 0 ? 1 : 0));
            }
            System.out.println();
        }
    }

    @Test
    public void listTest() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(0, 2);
        list.add(0, 3);
        logger.info("{}", list);
    }

    /**
     * <pre>
     *     CyclicBarrier(int parties)
     *     创建一个新的 CyclicBarrier ，当给定数量的线程（线程）等待它时，它将跳闸，并且当屏障跳闸时不执行预定义的动作。
     *     CyclicBarrier(int parties, Runnable barrierAction)
     *     创建一个新的 CyclicBarrier ，当给定数量的线程（线程）等待时，它将跳闸，当屏障跳闸时执行给定的屏障动作，由最后一个进入屏障的线程执行。
     *
     *     int await()：等待所有 parties已经在这个障碍上调用了 await 。
     *     int await(long timeout, TimeUnit unit)：等待所有 parties已经在此屏障上调用 await ，或指定的等待时间过去。
     *     int getNumberWaiting()：返回目前正在等待障碍的各方的数量。
     *     int getParties()：返回旅行这个障碍所需的parties数量。
     *     boolean isBroken()：查询这个障碍是否处于破碎状态。
     *     void reset()：将屏障重置为初始状态。
     * </pre>
     */
    @Test
    public void cyclicBarrierTest() {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(5, () -> {
            logger.info("所有人都到场了，大家统一出发");
        });

        ThreadTool threadTool = new ThreadTool(10);
        for (int i = 0; i < 10; i++) {
            final int id = i;
            threadTool.addTask(() -> {
                logger.info(Thread.currentThread().getName() + "，id：" + id + "现前往集合地点");
                try {
                    int sleep = new Random().nextInt(10000);
                    SleepUtil.sleepMilliSecond(sleep);
                    logger.info(Thread.currentThread().getName() + "到了集合地点，开始等待其他人到达");
                    cyclicBarrier.await();
                    logger.info(Thread.currentThread().getName() + "出发了");
                } catch (InterruptedException | BrokenBarrierException e) {
                    logger.error(e.getMessage(), e);
                }
            });
        }
        threadTool.startTask();
    }
}