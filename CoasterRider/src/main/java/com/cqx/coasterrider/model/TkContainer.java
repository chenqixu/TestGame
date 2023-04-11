package com.cqx.coasterrider.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <pre>
 *     540*520 容器
 *     40*20 炮弹/砖块
 *     40*40 坦克
 *     27*26
 *     27*20=540
 *     26*20=520
 * </pre>
 *
 * @author chenqixu
 */
public class TkContainer {
    private static final Logger logger = LoggerFactory.getLogger(TkContainer.class);
    private int width;
    private int height;
    private int pieceX = 26;
    private int pieceY = 27;
    private Piece[][] pieces = new Piece[pieceX][pieceY];
    private List<TankeModel> playerList = new CopyOnWriteArrayList<>();
    private List<TankeModel> aiList = new CopyOnWriteArrayList<>();
    private List<TankeModel> brickList = new CopyOnWriteArrayList<>();
    private List<TankeModel> homeList = new CopyOnWriteArrayList<>();
    // 一辆坦克只能发射一枚炮弹
    private Map<String, TankeModel> tankeBulletMap = new HashMap<>();
    private Lock w = new ReentrantLock();

    public TkContainer(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void clean(TankeModel tankeModel) {
        for (Piece piece : tankeModel.getPieceList()) {
            cleanPiece(piece);
        }
    }

    public void cleanPiece(Piece piece) {
        pieces[piece.getX()][piece.getY()] = null;
    }

    public void clean(boolean isAi, int level, Piece piece) {
        switch (piece.getPieceType()) {
            case Tanke:// 坦克
                logger.debug("[清理坦克] piece={}", piece);
                // 找到这个坦克的全部
                for (TankeModel tm : aiList) {
                    if (tm.getPieceList().contains(piece)) {
                        // 判断是否同类，同类不能打同类
                        if (tm.isAi() != isAi) {
                            tm.destory();
                            aiList.remove(tm);
                            for (Piece tmPiece : tm.getPieceList()) {
                                cleanPiece(tmPiece);
                            }
                        }
                        break;
                    }
                }
                for (TankeModel pm : playerList) {
                    if (pm.getPieceList().contains(piece)) {
                        // 判断是否同类，同类不能打同类
                        if (pm.isAi() != isAi) {
                            pm.destory();
                            playerList.remove(pm);
                            for (Piece tmPiece : pm.getPieceList()) {
                                cleanPiece(tmPiece);
                            }
                        }
                        break;
                    }
                }
                break;
            case Brick:// 砖块
                logger.debug("[清理砖块] piece={}", piece);
                // 清理对应的List
                for (TankeModel brick : getBrickList()) {
                    if (brick.getX() == piece.getX() && brick.getY() == piece.getY()) {
                        // 判断炮弹的级别能否清理砖块
                        if (level >= brick.getLevel()) {
                            brickList.remove(brick);
                            // 清理对应的二维数组
                            pieces[piece.getX()][piece.getY()] = null;
                        }
                        break;
                    }
                }
                break;
            case Home:// 基地
                logger.debug("[清理基地] piece={}", piece);
                // 找到这个基地的全部
                for (TankeModel tm : homeList) {
                    if (tm.getPieceList().contains(piece)) {
                        homeList.remove(tm);
                        for (Piece tmPiece : tm.getPieceList()) {
                            cleanPiece(tmPiece);
                        }
                        break;
                    }
                }
                break;
            case Bullet:// 炮弹，目前是2个点
                logger.debug("[清理炮弹] piece={}", piece);
                pieces[piece.getX()][piece.getY()] = null;
                break;
            default:
                break;
        }
    }

    public boolean move(PieceType type, TankeModel model, EnumDirection direction) {
        if (w.tryLock()) {
            try {
                switch (type) {
                    case Tanke:// 坦克移动
                        // 越界判断
                        if (model.setDirection(direction) && !model.isOutOfBorder(width, height)) {
                            // 方向判断 & 获取要移动的格子，尝试锁定
                            int preX = 0;
                            int preY = 0;
                            // 历史坐标
                            int oldX = model.getX();
                            int oldY = model.getY();
                            switch (direction) {
                                case UP:
                                    // 预处理坐标，坦克是4个点
                                    preX = model.getX();
                                    preY = model.getY() - 1;
                                    if (pieces[preX][preY] == null
                                            && pieces[preX + 1][preY] == null) {
                                        model.setY(preY);

                                        model.move(pieces[oldX][oldY + 1], pieces[oldX + 1][oldY + 1]);
                                        // 底下两个格子要清除
                                        pieces[oldX][oldY + 1] = null;
                                        pieces[oldX + 1][oldY + 1] = null;
                                        // 上面两个格子要占领
                                        pieces[preX][preY] = new Piece(PieceType.Tanke, preX, preY);
                                        pieces[preX + 1][preY] = new Piece(PieceType.Tanke, preX + 1, preY);
                                        model.addPiece(pieces[preX][preY]);
                                        model.addPiece(pieces[preX + 1][preY]);
                                    } else {
                                        return false;
                                    }
                                    break;
                                case DOWN:
                                    preX = model.getX();
                                    preY = model.getY() + 1;
                                    if (pieces[preX][preY + 1] == null
                                            && pieces[preX + 1][preY + 1] == null) {
                                        model.setY(preY);

                                        model.move(pieces[oldX][oldY], pieces[oldX + 1][oldY]);
                                        // 上面两个格子要清除
                                        pieces[oldX][oldY] = null;
                                        pieces[oldX + 1][oldY] = null;
                                        // 底下两个格子要占领
                                        pieces[preX][preY + 1] = new Piece(PieceType.Tanke, preX, preY + 1);
                                        pieces[preX + 1][preY + 1] = new Piece(PieceType.Tanke, preX + 1, preY + 1);
                                        model.addPiece(pieces[preX][preY + 1]);
                                        model.addPiece(pieces[preX + 1][preY + 1]);
                                    } else {
                                        return false;
                                    }
                                    break;
                                case LEFT:
                                    preX = model.getX() - 1;
                                    preY = model.getY();
                                    if (pieces[preX][preY] == null
                                            && pieces[preX][preY + 1] == null) {
                                        model.setX(preX);

                                        model.move(pieces[oldX + 1][oldY], pieces[oldX + 1][oldY + 1]);
                                        // 右边两个格子要清除
                                        pieces[oldX + 1][oldY] = null;
                                        pieces[oldX + 1][oldY + 1] = null;
                                        // 左边两个格子要占领
                                        pieces[preX][preY] = new Piece(PieceType.Tanke, preX, preY);
                                        pieces[preX][preY + 1] = new Piece(PieceType.Tanke, preX, preY + 1);
                                        model.addPiece(pieces[preX][preY]);
                                        model.addPiece(pieces[preX][preY + 1]);
                                    } else {
                                        return false;
                                    }
                                    break;
                                case RIGHT:
                                    preX = model.getX() + 1;
                                    preY = model.getY();
                                    if (pieces[preX + 1][preY] == null && pieces[preX + 1][preY + 1] == null) {
                                        model.setX(preX);

                                        model.move(pieces[oldX][oldY], pieces[oldX][oldY + 1]);
                                        // 左边两个格子要清除
                                        pieces[oldX][oldY] = null;
                                        pieces[oldX][oldY + 1] = null;
                                        // 右边两个格子要占领
                                        pieces[preX + 1][preY] = new Piece(PieceType.Tanke, preX + 1, preY);
                                        pieces[preX + 1][preY + 1] = new Piece(PieceType.Tanke, preX + 1, preY + 1);
                                        model.addPiece(pieces[preX + 1][preY]);
                                        model.addPiece(pieces[preX + 1][preY + 1]);
                                    } else {
                                        return false;
                                    }
                                    break;
                                default:
                                    return false;
                            }
                        } else {
                            return false;
                        }
                        break;
                    case Bullet:// 炮弹移动
                        // 越界判断
                        if (model.setDirection(direction) && !model.isOutOfBorder(width, height)) {
                            // 方向判断 & 获取要移动的格子，尝试锁定
                            int preX = 0;
                            int preY = 0;
                            // 历史坐标
                            int oldX = model.getX();
                            int oldY = model.getY();
                            switch (direction) {
                                case UP:
                                    // 预处理坐标，炮弹是2个点
                                    preX = model.getX();
                                    preY = model.getY() - 1;
                                    if (pieces[preX][preY] == null
                                            && pieces[preX + 1][preY] == null) {
                                        model.setY(preY);

                                        model.move(pieces[oldX][oldY], pieces[oldX + 1][oldY]);
                                        // 自身两个格子要清除
                                        pieces[oldX][oldY] = null;
                                        pieces[oldX + 1][oldY] = null;
                                        // 上面两个格子要占领
                                        pieces[preX][preY] = new Piece(PieceType.Bullet, preX, preY);
                                        pieces[preX + 1][preY] = new Piece(PieceType.Bullet, preX + 1, preY);
                                        model.addPiece(pieces[preX][preY]);
                                        model.addPiece(pieces[preX + 1][preY]);
                                    } else {
                                        // 撞到
                                        model.setStop(true);
                                        hit(model, pieces[preX][preY], pieces[preX + 1][preY]);
                                    }
                                    break;
                                case DOWN:
                                    preX = model.getX();
                                    preY = model.getY() + 1;
                                    if (pieces[preX][preY] == null
                                            && pieces[preX + 1][preY] == null) {
                                        model.setY(preY);

                                        model.move(pieces[oldX][oldY], pieces[oldX + 1][oldY]);
                                        // 自身两个格子要清除
                                        pieces[oldX][oldY] = null;
                                        pieces[oldX + 1][oldY] = null;
                                        // 底下两个格子要占领
                                        pieces[preX][preY] = new Piece(PieceType.Bullet, preX, preY);
                                        pieces[preX + 1][preY] = new Piece(PieceType.Bullet, preX + 1, preY);
                                        model.addPiece(pieces[preX][preY]);
                                        model.addPiece(pieces[preX + 1][preY]);
                                    } else {
                                        // 撞到
                                        model.setStop(true);
                                        hit(model, pieces[preX][preY], pieces[preX + 1][preY]);
                                    }
                                    break;
                                case LEFT:
                                    preX = model.getX() - 1;
                                    preY = model.getY();
                                    if (pieces[preX][preY] == null
                                            && pieces[preX][preY + 1] == null) {
                                        model.setX(preX);

                                        model.move(pieces[oldX][oldY], pieces[oldX][oldY + 1]);
                                        // 自身两个格子要清除
                                        pieces[oldX][oldY] = null;
                                        pieces[oldX][oldY + 1] = null;
                                        // 左边两个格子要占领
                                        pieces[preX][preY] = new Piece(PieceType.Bullet, preX, preY);
                                        pieces[preX][preY + 1] = new Piece(PieceType.Bullet, preX, preY + 1);
                                        model.addPiece(pieces[preX][preY]);
                                        model.addPiece(pieces[preX][preY + 1]);
                                    } else {
                                        // 撞到
                                        model.setStop(true);
                                        hit(model, pieces[preX][preY], pieces[preX][preY + 1]);
                                    }
                                    break;
                                case RIGHT:
                                    preX = model.getX() + 1;
                                    preY = model.getY();
                                    if (pieces[preX][preY] == null && pieces[preX][preY + 1] == null) {
                                        model.setX(preX);

                                        model.move(pieces[oldX][oldY], pieces[oldX][oldY + 1]);
                                        // 自身两个格子要清除
                                        pieces[oldX][oldY] = null;
                                        pieces[oldX][oldY + 1] = null;
                                        // 右边两个格子要占领
                                        pieces[preX][preY] = new Piece(PieceType.Bullet, preX, preY);
                                        pieces[preX][preY + 1] = new Piece(PieceType.Bullet, preX, preY + 1);
                                        model.addPiece(pieces[preX][preY]);
                                        model.addPiece(pieces[preX][preY + 1]);
                                    } else {
                                        // 撞到
                                        model.setStop(true);
                                        hit(model, pieces[preX][preY], pieces[preX][preY + 1]);
                                    }
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            logger.debug("方向判断不过 或者 越界判断不过");
                        }
                        break;
                    default:
                        return false;
                }
                return true;
            } finally {
                w.unlock();
            }
        }
        return false;
    }

    public void hit(TankeModel src, Piece piece1, Piece piece2) {
        boolean isAi = src.isAi();
        int level = src.getLevel();
        logger.info("[{}]isAi={}, level={}, 撞到目标1={}, 目标2={}", src.getName(), isAi, level, piece1, piece2);
        // 判断撞到什么
        // 如果是砖块，就爆炸砖块的部分
        // 如果是坦克，则需要爆炸坦克的部分
        if (piece1 != null && piece2 != null) {// 两个都不为空
            // 消灭
            clean(isAi, level, piece1);
            clean(isAi, level, piece2);
        } else if (piece1 != null || piece2 != null) {// 其中一个不为空
            Piece notNullPiece = (piece1 != null ? piece1 : piece2);
            // 消灭
            clean(isAi, level, notNullPiece);
        }
    }

    public void aiStart() {
        for (TankeModel ai : aiList) {
            ai.startAi(this, ai);
        }
    }

    public void addPlayer(int x, int y, TankeModel model, EnumDirection direction) {
        pieces[x][y] = new Piece(PieceType.Tanke, x, y);
        pieces[x + 1][y] = new Piece(PieceType.Tanke, x + 1, y);
        pieces[x][y + 1] = new Piece(PieceType.Tanke, x, y + 1);
        pieces[x + 1][y + 1] = new Piece(PieceType.Tanke, x + 1, y + 1);
        model.setX(x);
        model.setY(y);
        model.setDirection(direction);
        model.addPiece(pieces[x][y]);
        model.addPiece(pieces[x + 1][y]);
        model.addPiece(pieces[x][y + 1]);
        model.addPiece(pieces[x + 1][y + 1]);
        playerList.add(model);
    }

    public void addAI(int x, int y, TankeModel model, EnumDirection direction) {
        pieces[x][y] = new Piece(PieceType.Tanke, x, y);
        pieces[x + 1][y] = new Piece(PieceType.Tanke, x + 1, y);
        pieces[x][y + 1] = new Piece(PieceType.Tanke, x, y + 1);
        pieces[x + 1][y + 1] = new Piece(PieceType.Tanke, x + 1, y + 1);
        model.setX(x);
        model.setY(y);
        model.setDirection(direction);
        model.addPiece(pieces[x][y]);
        model.addPiece(pieces[x + 1][y]);
        model.addPiece(pieces[x][y + 1]);
        model.addPiece(pieces[x + 1][y + 1]);
        model.setAi(true);
        aiList.add(model);
    }

    public void addBrick(int x, int y, TankeModel model) {
        pieces[x][y] = new Piece(PieceType.Brick, x, y);
        model.setX(x);
        model.setY(y);
        model.addPiece(pieces[x][y]);
        brickList.add(model);
    }

    public void addHome(int x, int y, TankeModel model) {
        pieces[x][y] = new Piece(PieceType.Home, x, y);
        pieces[x + 1][y] = new Piece(PieceType.Home, x + 1, y);
        pieces[x][y + 1] = new Piece(PieceType.Home, x, y + 1);
        pieces[x + 1][y + 1] = new Piece(PieceType.Home, x + 1, y + 1);
        model.setX(x);
        model.setY(y);
        model.addPiece(pieces[x][y]);
        model.addPiece(pieces[x + 1][y]);
        model.addPiece(pieces[x][y + 1]);
        model.addPiece(pieces[x + 1][y + 1]);
        homeList.add(model);
    }

    public boolean addBullet(int x, int y, TankeModel model) {
        // 命中事件
        switch (model.getDirection()) {
            case UP:
                // 在上边y不能等于-1
                if (y == -1) {
                    logger.debug("越界！y={}", y);
                    return false;
                }
            case DOWN:
                // 在下边y不能等于27
                if (y == pieceY || x + 1 > pieceX - 1) {
                    logger.debug("越界！x+1={}, y={}", x + 1, y);
                    return false;
                } else if (pieces[x][y] != null || pieces[x + 1][y] != null) {
                    logger.debug("[up,down]炮弹刚出来就命中目标！");
                    hit(model, pieces[x][y], pieces[x + 1][y]);
                    return false;
                } else {
                    pieces[x][y] = new Piece(PieceType.Bullet, x, y);
                    pieces[x + 1][y] = new Piece(PieceType.Bullet, x + 1, y);
                    model.addPiece(pieces[x][y]);
                    model.addPiece(pieces[x + 1][y]);
                }
                break;
            case LEFT:
                // 在左边x不能等于-1
                if (x == -1) {
                    logger.debug("越界！x={}", x);
                    return false;
                }
            case RIGHT:
                // 在右边x不能等于26
                if (x == pieceX || y + 1 > pieceY - 1) {
                    logger.debug("越界！x={}, y+1={}", x, y + 1);
                    return false;
                } else if (pieces[x][y] != null || pieces[x][y + 1] != null) {
                    logger.debug("[left,right]炮弹刚出来就命中目标！");
                    hit(model, pieces[x][y], pieces[x][y + 1]);
                    return false;
                } else {
                    pieces[x][y] = new Piece(PieceType.Bullet, x, y);
                    pieces[x][y + 1] = new Piece(PieceType.Bullet, x, y + 1);
                    model.addPiece(pieces[x][y]);
                    model.addPiece(pieces[x][y + 1]);
                }
                break;
            default:
                break;
        }
        return true;
    }

    public TankeModel getBulletByTanke(String tankeName) {
        // 一辆坦克一次只能发射一枚炮弹
        return tankeBulletMap.get(tankeName);
    }

    public void sendBulletByTanke(String tankeName, TankeModel bullet) {
        // 加到炮弹map中用于控制炮弹个数
        tankeBulletMap.put(tankeName, bullet);
    }

    public void removeBulletByTanke(String tankeName) {
        // 移除
        tankeBulletMap.remove(tankeName);
    }

    public List<TankeModel> getBrickList() {
        return brickList;
    }

    public List<TankeModel> getHomeList() {
        return homeList;
    }

    public List<TankeModel> getPlayerList() {
        return playerList;
    }

    public List<TankeModel> getAiList() {
        return aiList;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Map<String, TankeModel> getTankeBulletMap() {
        return tankeBulletMap;
    }
}
