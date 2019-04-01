package com.cqx.tetris.control;

import com.cqx.tetris.block.BaseBlock;

public interface BlockControl {
	public void initBaseBlock(BaseBlock bb, int row_index, int col_index);
	public boolean downBaseBlock();
	public void upBaseBlock();
	public void leftBaseBlock();
	public void rightBaseBlock();
	public void chagenBaseBlock();
	public boolean isEnd();
	public void cleanCompleteBlock();
	public void Down(int index);
	public void cleanAll();
	public void setBs(BlockStep bs);
	public int getHalfCols();
	public boolean initBlockSome(int... indexs);
}
