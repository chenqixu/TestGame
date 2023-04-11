package com.cqx.coasterrider.model;

/**
 * Piece
 *
 * @author chenqixu
 */
public class Piece {
    private PieceType pieceType;
    private int x;
    private int y;

    public Piece(PieceType pieceType) {
        this.pieceType = pieceType;
    }

    public Piece(PieceType pieceType, int x, int y) {
        this.pieceType = pieceType;
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("[%s]pieceType=%s, x=%s, y=%s", super.toString(), pieceType, x, y);
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
