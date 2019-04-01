package com.cqx.tetris.observer;

import java.util.ArrayList;
import java.util.List;

import com.cqx.tetris.block.Block;
import com.cqx.tetris.control.BlockControl;

/**
 * 行观察者 定义了所有的行块 当整行的块都完整及触发消除动作
 * */
public class RowObserver {
	private List<Block> rowlist;// 元素列表

	public RowObserver() {
		rowlist = new ArrayList<Block>();
	}

	/**
	 * 增加元素
	 * */
	public void add(Block b) {
		rowlist.add(b);
	}

	/**
	 * 判断能否清除
	 * */
	public boolean hasClean() {
		boolean flag = false;
		for (Block b : rowlist) {
			flag = b.isActive();
			if (!flag)
				break;
		}
		return flag;
	}

	/**
	 * 判断行观察者是否有值
	 * */
	public boolean hasRowInit() {
		boolean flag = false;
		for (Block b : rowlist) {
			flag = b.isActive();
			if (flag)
				break;
		}
		return flag;
	}

	/**
	 * 清除一行
	 * */
	public void cleanRow() {
		for (Block b : rowlist) {
			b.clean();
		}
	}

	/**
	 * 所有块下移一格
	 * */
	public void downRow(BlockControl bc) {
		for (Block b : rowlist) {
			if (b.isActive())
				bc.Down(b.getListindex());
		}
	}
}
