package com.cqx.tetris.panel;

import java.util.List;

import com.cqx.tetris.block.Block;
import com.cqx.tetris.control.BlockControl;

public class PanelNextFactory {
	private static PanelNextFactory pnf = new PanelNextFactory();
	private BlockPanel bp;

	private PanelNextFactory(){}
	
	public static PanelNextFactory getInstance(){
		if(pnf==null)pnf = new PanelNextFactory();
		return pnf;
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
		}
	}

	public Panel getPanel() {
		return bp;
	}

	public BlockControl getBlockControl() {
		return bp;
	}
}
