package com.cqx.tetris.block;

public class Z1Block extends BaseBlock {	
	//下层左
	private static int[] argsL1DownLeft = {0,3,4,7};
	private static int[] argsL2DownLeft = {4,5,6,7};
	
	//下层右
	private static int[] argsL1DownRigth = {1,4,5,8};
	private static int[] argsL2DownRigth = {4,5,6,7};

	@Override
	public void setChangelist() {	
		addBaseBlockList(argsL1DownLeft, argsL2DownLeft);
		addBaseBlockList(argsL1DownRigth, argsL2DownRigth);
	}

	@Override
	public void setNotMove() {
		notmove = new int[]{0};
	}
}
