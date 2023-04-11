package com.cqx.coasterrider.model;

/**
 * 方向
 *
 * @author chenqixu
 */
public enum EnumDirection {
    UP("up"), DOWN("down"), LEFT("left"), RIGHT("right");

    private String code;

    EnumDirection(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
