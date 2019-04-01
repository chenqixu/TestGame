package com.cqx.tetris.panel;

import java.util.ArrayList;
import java.util.List;

import com.cqx.tetris.block.BaseBlock;
import com.cqx.tetris.block.Block;
import com.cqx.tetris.control.BlockControl;
import com.cqx.tetris.control.BlockStep;
import com.cqx.tetris.observer.RowObserverFactory;

/**
 * 方块面板
 * */
public class BlockPanel implements BlockControl, Panel {
	private int rows;//行
	private int cols;//列
	private List<Block> jblist;//元素列表
	private int now_index;//当前元素位置
	private int now_block_row;//当前模块的列位置
	private int now_block_col;//当前模块的行位置
	private BaseBlock bbmodel;//当前模块引用
	private int[] now_indexs;//当前模块的位置数组
	private boolean isEnd = false;
	private BlockStep bs;//计步器
	
	public BlockPanel(int _rows, int _cols){
		rows = _rows;
		cols = _cols;
	}
	
	/**
	 * 判断是否左边
	 * */
	private boolean isLeft(){		
		return now_block_col<=(cols/2);
	}
	
	/**
	 * 判断是否右边
	 * */
	private boolean isRigth(){
		return now_block_col>(cols/2);
		
	}
	
	private synchronized void initEnd(){
		isEnd = false;
	}
	
	private synchronized void closeEnd(){
		isEnd = true;
	}
	
	public int getRows() {
		return rows;
	}

	public int getCols() {
		return cols;
	}
	
	/**
	 * 获取一半列+1
	 * */
	public int getHalfCols(){
		return cols/2-1;
	}
	
	/**
	 * 设置计步器
	 * */
	public void setBs(BlockStep bs) {
		this.bs = bs;
	}

	/**
	 * 设置块列表
	 * */
	public void setJblist(List<Block> _jblist){
		jblist = _jblist;
	}
	
	/**
	 * 返回行*列
	 * */
	public int getSize(){
		return rows*cols;
	}
	
	/**
	 * 根据传入的计算行号
	 * */
	public int getRownum(int index){
		return index/cols;
	}
	
	/**
	 * 根据传入的计算列号
	 * */
	public int getColnum(int index){
		//先获取当前行数
		int now_row = index/cols;
		//获取当前行数所在的列位置
		return index-now_row*cols;
	}
	
	private void clean(int index){
		jblist.get(index).clean();
	}
	
	/**
	 * 清除当前模块的位置数组，已经不在使用，现在使用新的缓存功能
	 * */
	@Deprecated 
	public void cleans(){
		if(now_indexs!=null)
			for(int t : now_indexs){
				jblist.get(t).clean();
			}
	}
	
	/**
	 * 清屏
	 * */
	public void cleanAll(){
		for(Block b : jblist)b.clean();
	}
	
	/**
	 * 能否移动的判断
	 * */
	private boolean hasInit(int index){
		if(jblist.get(index).isActive())return false;
		return true;
	}
	
	private void initBlock(int index){
		if(hasInit(index))
			jblist.get(index).init();
	}
	
	/**
	 * 初始化单个块
	 * */
	public void initBlockSingle(int index){
		if(index<0)return;
		BlockCache bcache = new BlockCache();
		//清除加入缓存
		bcache.addCleanCache(now_index);
		//进行初始化
		bcache.addInitCache(index);
		//预执行
		if(bcache.prepare()){
			bcache.exec();
			now_index = index;
		}
	}
	
	public boolean initBlockSome(int... indexs){
		BlockCache bcache = new BlockCache();
		if(now_indexs!=null)
			for(int c : now_indexs){
				bcache.addCleanCache(c);
			}
		for(int t : indexs){
			bcache.addInitCache(t);
		}
		//预执行
		if(bcache.prepare()){
			bcache.exec();
			now_indexs = indexs;
		}
		return bcache.getFlag();
	}
	
	/**
	 * 初始化方块模块
	 * */
	public void initBaseBlock(BaseBlock bb, int row_index, int col_index){
		now_indexs = null;
		initEnd();
		//传入初始化位置，面板大小，获取转换后的位置，一个一个初始化
		if(initBlockSome(bb.getPanelIndexs(row_index, col_index, rows, cols))){
			now_block_row = row_index;
			now_block_col = col_index;
			bbmodel = bb;
		}
	}
	
	/**
	 * 重绘模块位置
	 * */
	private boolean repaintBaseBlock(){
		return initBlockSome(bbmodel.getPanelIndexs(now_block_row, now_block_col, rows, cols));
	}
	
	/**
	 * 方块模块向上移动
	 * */
	public synchronized void upBaseBlock(){
		if((now_block_row-1)<0)return;
		now_block_row--;
		if(!repaintBaseBlock())
			now_block_row++;
	}
	
	/**
	 * 方块模块向下移动
	 * */
	public synchronized boolean downBaseBlock(){
		if((now_block_row+bbmodel.getRow())>=rows){
			closeEnd();
			return false;
		}
		now_block_row++;
		if(repaintBaseBlock()){
			//移动成功，还能下移，置isEnd状态false
			initEnd();
			//计步器加一
			if(bs!=null)bs.add();
			return true;
		}else{
			//移动失败，不能下移，置isEnd状态true，回退now_block_row
			closeEnd();
			now_block_row--;
			return false;
		}
	}
	
	/**
	 * 方块模块向左移动
	 * */
	public synchronized void leftBaseBlock(){		
		//无法到达，返回
		if((now_block_col-1)<0)return;
		//判断在左边，使用左九宫格
		if(isLeft()){
//		//到达最左，使用左九宫格
//		if((now_block_col-1)==0){
			if(!bbmodel.isLeft()){
				bbmodel.useLeft();
				//重绘一次
				repaintBaseBlock();
			}
			//判断能否移动
			if(bbmodel.hasMuNotMove())return;
		}
		now_block_col--;
		if(!repaintBaseBlock())
			now_block_col++;
	}
	
	/**
	 * 方块模块向右移动
	 * */
	public synchronized void rightBaseBlock(){
		//无法到达，返回
		if((now_block_col+bbmodel.getCol())>=cols)return;
		//判断在右边，使用右九宫格
		if(isRigth()){
//		//到达最右，使用右九宫格
//		if((now_block_col+bbmodel.getCol()+1)==cols){
			if(!bbmodel.isRight()){
				bbmodel.useRight();
				//重绘一次
				repaintBaseBlock();
			}
			//判断能否移动
			if(bbmodel.hasMuNotMove())return;
		}
		now_block_col++;
		if(!repaintBaseBlock())
			now_block_col--;
	}
	
	/**
	 * 方块模块变形
	 * */
	public synchronized void chagenBaseBlock(){
		//清理
//		cleans();
		if(!isEnd()){
			//变形
			bbmodel.next();
			if(!initBlockSome(bbmodel.getPanelIndexs(now_block_row, now_block_col, rows, cols)))
				bbmodel.back();
		}
	}
	
	/**
	 * 判断当前模块是否到底
	 * */
	public synchronized boolean isEnd(){
//		if((now_block_row+bbmodel.getRow())>=rows)return true;
//		return false;
		return isEnd;
	}
	
	/**
	 * 清除完成的行，每行记1分
	 * */
	public void cleanCompleteBlock(){
		//初始化的时候初始化行观察者
		//触发观察者动作
		RowObserverFactory.getInstance().deal(this);
	}
	
	/**
	 * 传入元素位置，判断上面是否有空位置
	 * */
	public boolean isUp(int index){
		//先获取当前行数
		int now_row = index/cols;
		//如果是顶层，就返回false
		if(now_row==0)return false;
		//否则返回true
		return true;
	}
	
	/**
	 * 传入元素位置，获取上面位置
	 * */
	public int getUp(int index){
		//先获取当前行数
		int now_row = index/cols;
		//如果是顶层，就返回-1
		if(now_row==0){
			return -1;
		}else{
			//如果不是顶层，获取当前行数所在的列位置
			int now_column = index-now_row*cols;
			return (now_row-1)*cols+now_column;
		}
	}
	
	/**
	 * 传入元素位置，获取下面位置
	 * */
	public int getDown(int index){
		//先获取当前行数
		int now_row = index/cols;
		//如果是底层，就返回-1
		if((now_row+1)==rows){
			return -1;
		}else{
			//如果不是底层，获取当前行数所在的列位置
			int now_column = index-now_row*cols;
			return (now_row+1)*cols+now_column;
		}
	}

	/**
	 * 传入元素位置，获取左边位置
	 * */
	public int getLeft(int index){
		//先获取当前行数
		int now_row = index/cols;
		//获取当前行数所在的列位置
		int now_column = index-now_row*cols;
		//如果是开始，就返回-1
		if(now_column==0){
			return -1;
		}else{
			return index-1;
		}
	}
	
	/**
	 * 传入元素位置，获取右边位置
	 * */
	public int getRight(int index){
		int now_row = index/cols;
		//获取当前行数所在的列位置
		int now_column = index-now_row*cols;
		//如果到底，就返回-1
		if(now_column==(cols-1)){
			return -1;
		}else{
			return index+1;
		}
	}
	
	public int getDown(){
		return getDown(now_index);
	}
	
	public int getUp(){
		return getUp(now_index);
	}
	
	public int getLeft(){
		return getLeft(now_index);
	}
	
	public int getRight(){
		return getRight(now_index);
	}
	
	public void Down(){
		initBlock(getDown(now_index));
	}
	
	public void Up(){
		initBlock(getUp(now_index));
	}
	
	public void Left(){
		initBlock(getLeft(now_index));
	}
	
	public void Right(){
		initBlock(getRight(now_index));
	}
	
	/**
	 * 根据List索引获得块，位置下移
	 * */
	public void Down(int index){
		initBlock(getDown(index));
		clean(index);
	}
	
	/**
	 * 块缓存
	 * */
	public class BlockCache {
		private List<Integer> cleanCache;
		private List<Integer> initCache;
		private boolean flag = false;
		public BlockCache(){
			cleanCache = new ArrayList<Integer>();
			initCache = new ArrayList<Integer>();
		}
		public void addCleanCache(int cleanCache) {
			this.cleanCache.add(cleanCache);
		}
		public void addInitCache(int initCache) {
			this.initCache.add(initCache);
		}
		public boolean getFlag() {
			return flag;
		}
		//预执行，需要剔除cleanCache
		public boolean prepare(){
			List<Integer> preInitCache = new ArrayList<Integer>();
			preInitCache.addAll(initCache);
			preInitCache.removeAll(cleanCache);
			for(int t : preInitCache){
				if(!hasInit(t)){
					flag = false;
					return false;
				}
			}
			flag = true;
			return true;
		}
		public void exec(){
			//do clean
			if(cleanCache!=null)
				for(int c : cleanCache){
					clean(c);
				}			
			//do init
			if(initCache!=null)
				for(int i : initCache){
					initBlock(i);
				}
		}
	}
}
