package com.cqx.tetris.panel;

import java.util.List;

import com.cqx.tetris.block.Block;
import com.cqx.tetris.control.BlockControl;
import com.cqx.tetris.observer.RowObserverFactory;

public class PanelFactory {
	private static PanelFactory pf = new PanelFactory();
	private BlockPanel bp;

	private PanelFactory() {
	}

	public static PanelFactory getInstance() {
		if (pf == null)
			pf = new PanelFactory();
		return pf;
	}

	public void init(int _rows, int _cols) {
		bp = new BlockPanel(_rows, _cols);
	}

	/**
	 * 设置块列表
	 * */
	public void setJblist(List<Block> jblist) {
		if (bp != null) {
			bp.setJblist(jblist);
			// 初始化行观察者
			int nowrow = 0;
			for (Block b : jblist) {
				if (b.getRownum() > nowrow) {
					nowrow = b.getRownum();
					// 新增一个行观察者
					RowObserverFactory.getInstance().next();
					// 加入新的行观察者
					RowObserverFactory.getInstance().getNowObserver().add(b);
				} else {
					// 加入旧的行观察者
					RowObserverFactory.getInstance().getNowObserver().add(b);
				}
			}
		}
	}

	public Panel getPanel() {
		return bp;
	}

	public BlockControl getBlockControl() {
		return bp;
	}
}
