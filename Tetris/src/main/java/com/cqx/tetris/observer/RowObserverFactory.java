package com.cqx.tetris.observer;

import java.util.ArrayList;
import java.util.List;

import com.cqx.tetris.control.BlockControl;
import com.cqx.tetris.control.Score;

/**
 * 行观察者工厂
 * */
public class RowObserverFactory {
	private List<RowObserver> roblist = new ArrayList<RowObserver>();
	private static RowObserverFactory rof = new RowObserverFactory();
	private int index = 0;
	
	private RowObserverFactory(){
		roblist.add(new RowObserver());
	}
	
	public static RowObserverFactory getInstance(){
		if(rof==null)rof = new RowObserverFactory();
		return rof;
	}
	
	/**
	 * 返回当前行观察者
	 * */
	public RowObserver getNowObserver(){
		if(roblist.size()<=index)
			roblist.add(new RowObserver());
		return roblist.get(index);
	}
	
	/**
	 * 新建一个行观察者
	 * */
	public void next(){
		index++;
	}
	
	/**
	 * 处理行观察者
	 * */
	public void deal(BlockControl bc){
		boolean isClean = false;
		// 逐行处理行观察者，只处理有值的，从下往上
		for(int i=(roblist.size()-1);i>=0;i--){
			if(isClean){
				if(roblist.get(i).hasRowInit()){
					// 有值才能下移一格
					roblist.get(i).downRow(bc);
				}else{
					break;
				}
			}else if(roblist.get(i).hasRowInit() && roblist.get(i).hasClean()){
				// 清除一行
				roblist.get(i).cleanRow();
				// 标记为清除
				isClean = true;
				// 得一分
				Score.getInstance().addScore();
			}
		}
	}
}
