package com.cqx.tetris.block;

import java.util.ArrayList;
import java.util.List;

/**
 * 九宫格
 * */
public abstract class BaseBlock {
	protected List<BaseBlockList> mulist = new ArrayList<BaseBlockList>();//九宫格列表
	protected int muinde = 0;//九宫格List索引
	protected int index = 0;//变形List索引
	protected int row = 3;//行
	protected int col =3;//列
	protected int[] notmove;//用于切换九宫格的时候不移动
	protected int limitnum = 0;//切换九宫格后能否移动一次，用于处理切换九宫格后的BUG

	public BaseBlock(){
		setChangelist();
		setNotMove();
	}
	
	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}
	
	public void setLimitnum(int limitnum) {
		this.limitnum = limitnum;
	}

	public abstract void setChangelist();
	
	public abstract void setNotMove();
	
	public int getCurrentNowCol(int now_block_col){
		return now_block_col;
	}
	
	public boolean hasMuNotMove(){
		if(notmove!=null)
			for(int m : notmove){
				if(index==m && limitnum==1){
					limitnum = 0;
					return true;
				}
			}
		return false;
	}
	
	public void useLeft(){
		muinde = 0;
		limitnum = 1;
	}
	
	public void useRight(){
		muinde = 1;
		limitnum = 1;
	}
	
	public boolean isLeft(){
		return muinde==0?true:false;
	}
	
	public boolean isRight(){
		return muinde==1?true:false;
	}
	
	public void next(){
		if((index+1)==mulist.get(muinde).size())index=0;
		else index++;
	}
	
	public void back(){
		if(index==0)index=(mulist.get(muinde).size()-1);
		else index--;
	}
	
	public void addBaseBlockList(int[]... args){
		BaseBlockList changelist = new BaseBlockList();
		for(int[] tmp : args){
			changelist.add(tmp);
		}
		mulist.add(changelist);
	}
	
	/**
	 * 传入位置和面板大小，返回最后在面板上的位置
	 * 按行处理，加传入位置即可
	 * */
	protected int[] getBasePanelIndexs(int row_index, int col_index, int rows, int cols){
		//获取当前九宫格
		int[] now_list = mulist.get(muinde).get(index);
		int[] result = new int[now_list.length];
		for(int i=0;i<now_list.length;i++){
			//先获取当前行数
			int now_row = now_list[i]/col;
			//获取当前行数所在的列位置
			int now_col = now_list[i]-now_row * col;
			//判断是否是从第一行开始还是非第一行开始
			//如果都是第一列都特殊处理
			if((now_col+col_index)<=0){
				//特殊处理，按行取值
				result[i] = (row_index+now_row) * cols;
			}else{//要算点差距
				//前差距
				int begin_gap = col_index;
				//真正行数
				int s_index = row_index+now_row;
				//第一行
				if(s_index==0)
					result[i] = begin_gap + now_list[i];
				//非第一行
				else if(s_index>0){
					result[i] = s_index * cols + now_col + begin_gap;
				}
			}
		}
		return result;
	}
	
	/**
	 * 传入位置和面板大小，返回最后在面板上的位置
	 * 按行处理，加传入位置即可
	 * */
	public int[] getPanelIndexs(int row_index, int col_index, int rows, int cols){
		return getBasePanelIndexs(row_index, col_index, rows, cols);
	}
}
