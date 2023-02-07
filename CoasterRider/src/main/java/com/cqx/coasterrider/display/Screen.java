package com.cqx.coasterrider.display;

import javax.swing.*;
import java.awt.*;

/**
 * 屏幕
 *
 * @author chenqixu
 */
public class Screen extends JFrame {
    private JPanel panelContent;
    private int row;
    private int col;

    public void init() {
        //主面板
        panelContent = new JPanel(new GridLayout(row, col));//网格布局
    }

    public void loadMap() {

    }
}
