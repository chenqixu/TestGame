package com.cqx.coasterrider;

import com.cqx.coasterrider.model.LimitedQueue;
import com.cqx.common.utils.system.SleepUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;

/**
 * 屏幕抽象类
 *
 * @author chenqixu
 */
public abstract class AbstractScreen extends JFrame {
    public static final String FromLeftToRight = "FromLeftToRight";
    public static final String FromRightToLeft = "FromRightToLeft";
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected List<List<JButton>> jbList;
    /**
     * 排
     */
    protected int rows = 16;
    /**
     * 列
     */
    protected int cols = 128;
    protected int jbLen = 12;
    protected int LocationX;
    protected int LocationY;
    protected String title;
    protected LimitedQueue<Integer> limitedQueue;
    /**
     * 空列
     */
    protected List<Integer> spaceList;
    protected String type;
    /**
     * 移动速度[1000-0]<br>
     * 越大越慢，建议300
     */
    protected int speed;

    public AbstractScreen() {
    }

    public AbstractScreen(String type, int speed, int LocationX, int LocationY, String title) {
        this(12, 16, 128, type, speed, LocationX, LocationY, title);
    }

    public AbstractScreen(int jbLen, int rows, int cols, String type, int speed, int LocationX, int LocationY, String title) {
        // 参数
        this.jbLen = jbLen;
        this.rows = rows;
        this.cols = cols;
        this.type = type;
        this.speed = speed;
        this.LocationX = LocationX;
        this.LocationY = LocationY;
        this.title = title;

        // 移动速度校验
        if (speed > 1000 || speed < 0) {
            this.speed = 300;
            logger.warn("移动速度只能在0-1000之间，越大越慢，目前调整为默认值300");
        }
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
            jb.setPreferredSize(new Dimension(jbLen, jbLen));
            rowList.add(jb);
            panelContent.add(jb);
        }

        Container panel = getContentPane();// 主布局容器
        panel.add(panelContent);

        pack();// 根据控件占据总大小设置JFrame窗口大小
        setLocation(LocationX, LocationY);// 设置窗口初始化位置
        setVisible(true);// 设置窗口可见
        setTitle(title);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // 空列
        spaceList = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            spaceList.add(Color.WHITE.getRGB());
        }
        // 初始化 rows * cols 的数据队列
        limitedQueue = new LimitedQueue<>(cols, type, spaceList);
    }

    public void run() {
        // 解析内容并刷入数据队列
        buildData();
    }

    /**
     * 在容器上绘图
     */
    protected void paint() {
        List<List<Integer>> datas = limitedQueue.getAllData();
        for (int i = 0; i < datas.size(); i++) {
            List<JButton> jButtonList = jbList.get(i);
            for (int j = 0; j < datas.get(i).size(); j++) {
                jButtonList.get(j).setBackground(new Color(datas.get(i).get(j)));
            }
        }
        // 解决刷新的残影问题
        setVisible(true);
        update(getGraphics());
    }

    /**
     * 解析内容并刷入数据队列
     */
    protected void buildData() {
        BlockingDeque<List<Integer>> deque = buildDataQueue();
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

    protected abstract BlockingDeque<List<Integer>> buildDataQueue();
}
