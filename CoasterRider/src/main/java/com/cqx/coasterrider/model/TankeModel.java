package com.cqx.coasterrider.model;

import com.cqx.common.utils.system.SleepUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * TankeModel
 *
 * @author chenqixu
 */
public class TankeModel {
    private static final Logger logger = LoggerFactory.getLogger(TankeModel.class);
    private String sucai_path = "d:\\tmp\\data\\show\\";
    private int x;
    private int y;
    private EnumDirection direction = EnumDirection.UP;
    private String imagePath;
    private BufferedImage bufferedImage;
    private String name;
    private List<Piece> pieceList = new ArrayList<>();
    // 针对炮弹
    private boolean isStop;
    private PieceType pieceType;
    // 坦克移动速度
    private int speed = 500;
    // ai动作
    private Thread action;
    // 针对坦克
    private boolean isOver;
    // 炮弹的速度
    private int bulletSpeed = 100;
    // 等级
    private int level;
    // 是否AI
    private boolean isAi;
    // 生命
    private int life = 1;

    public TankeModel(PieceType pieceType, String name, String imagePath, int level) {
        this.pieceType = pieceType;
        this.name = name;
        this.imagePath = imagePath;
        this.level = level;
        try {
            this.bufferedImage = ImageIO.read(new File(String.format(imagePath, direction.getCode())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TankeModel(PieceType pieceType, String name, String imagePath) {
        this(pieceType, name, imagePath, 1);
    }

    public void move(Piece c1, Piece c2) {
        Iterator<Piece> it = pieceList.iterator();
        while (it.hasNext()) {
            Piece _p = it.next();
            if (_p.equals(c1) || _p.equals(c2)) {
                it.remove();
                it = pieceList.iterator();
            }
        }
    }

    public void addPiece(Piece piece) {
        pieceList.add(piece);
    }

    public void cleanPiece() {
        pieceList.clear();
    }

    public int getWidth() {
        return this.bufferedImage.getWidth();
    }

    public int getHeight() {
        return this.bufferedImage.getHeight();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getDrawX() {
        return x * 20;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDrawY() {
        return y * 20;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public boolean setDirection(EnumDirection direction) {
        EnumDirection hisDirection = this.direction;
        this.direction = direction;
        if (hisDirection != this.direction) {
            try {
                this.bufferedImage = ImageIO.read(new File(String.format(imagePath, direction.getCode())));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }

    public EnumDirection getDirection() {
        return direction;
    }

    public String getName() {
        return name;
    }

    public boolean isOutOfBorder(int parentWidth, int parentHeight) {
        switch (getDirection()) {
            case UP:// 上，y小于等于0
                return getDrawY() <= 0;
            case DOWN:// 下，y大于等于画布的高
                return getDrawY() + getHeight() >= parentHeight;
            case LEFT:// 左，x小于等于0
                return getDrawX() <= 0;
            case RIGHT:// 右，x大于等于画布的宽
                return getDrawX() + getWidth() >= parentWidth;
            default:
                break;
        }
        return false;
    }

    public List<Piece> getPieceList() {
        return pieceList;
    }

    public boolean isStop() {
        return isStop;
    }

    public void setStop(boolean stop) {
        isStop = stop;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void startAi(TkContainer tkContainer, TankeModel ai) {
        this.action = new Thread(new Runnable() {
            Random random = new Random(System.currentTimeMillis());

            @Override
            public void run() {
                // 策略，一直移动，直到需要转弯
                SleepUtil.sleepMilliSecond(1500);
                while (!isOver()) {
                    // 丢骰子，确定是移动还是攻击
                    // 移动和攻击的概率比
                    int ratio = 4;
                    int move_attach = random.nextInt(ratio);
                    if (move_attach == (ratio - 1)) {// 攻击
                        bulletSend(tkContainer);
                    } else {// 移动
                        // 先判断前面是否有遮挡物
                        boolean mvRet = tkContainer.move(PieceType.Tanke, ai, ai.getDirection());
                        if (!mvRet) {// 需要转向
                            int direction = random.nextInt(4);
                            // 0上, 1下, 2左, 3右
                            int _current_direction = 0;
                            switch (ai.getDirection()) {
                                case DOWN:
                                    _current_direction = 1;
                                    break;
                                case LEFT:
                                    _current_direction = 2;
                                    break;
                                case RIGHT:
                                    _current_direction = 3;
                                    break;
                            }
                            // 剔除当前方向
                            while (_current_direction == direction) {
                                direction = random.nextInt(3);
                            }
                            switch (direction) {
                                case 0:
                                    tkContainer.move(PieceType.Tanke, ai, EnumDirection.UP);
                                    break;
                                case 1:
                                    tkContainer.move(PieceType.Tanke, ai, EnumDirection.DOWN);
                                    break;
                                case 2:
                                    tkContainer.move(PieceType.Tanke, ai, EnumDirection.LEFT);
                                    break;
                                case 3:
                                    tkContainer.move(PieceType.Tanke, ai, EnumDirection.RIGHT);
                                    break;
                            }
                        }
                    }
                    SleepUtil.sleepMilliSecond(ai.getSpeed());
                }
            }
        });
        action.start();
    }

    public boolean isOver() {
        return isOver;
    }

    public void destory() {
        isOver = true;
    }

    public void bulletSend(TkContainer tkContainer) {
        // 子弹只有40*20
        // 砖块是40*40
        // 一辆坦克一次只能发射一枚炮弹
        if (tkContainer.getBulletByTanke(getName()) == null) {
            // 先获取坦克方向，再从那个方向计算炮弹的起始位置
            // 炮弹也有速度
            // 发射炮弹
            TankeModel bullet = new TankeModel(PieceType.Bullet, "bullet", sucai_path + "bullet-%s.jpg");
            bullet.setAi(true);
            bullet.setDirection(getDirection());
            switch (getDirection()) {
                case UP:// 坦克的x，坦克的y-20
                    bullet.setX(getX());
                    bullet.setY(getY() - 1);
                    break;
                case DOWN:// 坦克的x，坦克的y+坦克的高
                    bullet.setX(getX());
                    bullet.setY(getY() + 2);
                    break;
                case LEFT:// 坦克的x-20，坦克的y
                    bullet.setX(getX() - 1);
                    bullet.setY(getY());
                    break;
                case RIGHT:// 坦克的x+塔克的高，坦克的y
                    bullet.setX(getX() + 2);
                    bullet.setY(getY());
                    break;
                default:
                    break;
            }
            // 判断能否发射，发射前先判断炮弹前面有没物体
            if (tkContainer.addBullet(bullet.getX(), bullet.getY(), bullet)) {
                // 加到炮弹map中用于控制炮弹个数
                tkContainer.sendBulletByTanke(getName(), bullet);
                // 炮弹飞行
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (!bullet.isOutOfBorder(tkContainer.getWidth(), tkContainer.getHeight()) && !bullet.isStop()) {
                            switch (bullet.getDirection()) {
                                case UP:
                                    tkContainer.move(PieceType.Bullet, bullet, EnumDirection.UP);
                                    break;
                                case DOWN:
                                    tkContainer.move(PieceType.Bullet, bullet, EnumDirection.DOWN);
                                    break;
                                case LEFT:
                                    tkContainer.move(PieceType.Bullet, bullet, EnumDirection.LEFT);
                                    break;
                                case RIGHT:
                                    tkContainer.move(PieceType.Bullet, bullet, EnumDirection.RIGHT);
                                    break;
                                default:
                                    break;
                            }
                            SleepUtil.sleepMilliSecond(bulletSpeed);
                        }
                        // 清理
                        tkContainer.clean(bullet);
                        // 移除
                        tkContainer.removeBulletByTanke(getName());
                    }
                }).start();
            }
        }
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isAi() {
        return isAi;
    }

    public void setAi(boolean ai) {
        isAi = ai;
    }

    public int getLife() {
        return life;
    }
}
