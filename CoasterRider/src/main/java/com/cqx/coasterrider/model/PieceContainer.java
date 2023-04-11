package com.cqx.coasterrider.model;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 块容器
 *
 * @author chenqixu
 */
public class PieceContainer {
    private int pieceX = 26;
    private int pieceY = 27;
    private Piece[][] pieces = new Piece[pieceX][pieceY];
    private Lock w = new ReentrantLock();

    public void request(int x1, int y1, int x2, int y2) {
        if (w.tryLock()) {
            try {

            } finally {
                w.unlock();
            }
        } else {
        }
    }


}
