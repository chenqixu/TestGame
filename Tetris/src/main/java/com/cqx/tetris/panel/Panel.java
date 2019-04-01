package com.cqx.tetris.panel;

import java.util.List;

import com.cqx.tetris.block.BaseBlock;
import com.cqx.tetris.block.Block;

public interface Panel {
	public int getRows();
	public int getCols();
	public int getSize();
	public void setJblist(List<Block> jblist);
	public void initBaseBlock(BaseBlock bb, int row_index, int col_index);
	public int getRownum(int index);
	public int getColnum(int index);
}
