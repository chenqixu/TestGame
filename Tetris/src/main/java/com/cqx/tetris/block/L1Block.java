package com.cqx.tetris.block;

public class L1Block extends BaseBlock {
	//下层左
	private static int[] argsL1DownLeft = {1,4,6,7};
	private static int[] argsL2DownLeft = {3,6,7,8};
	private static int[] argsL3DownLeft = {0,1,3,6};
	private static int[] argsL4DownLeft = {3,4,5,8};
	
	//下层右
	private static int[] argsL1DownRigth = {2,5,7,8};
	private static int[] argsL2DownRigth = {3,6,7,8};
	private static int[] argsL3DownRigth = {1,2,4,7};
	private static int[] argsL4DownRigth = {3,4,5,8};

	@Override
	public void setChangelist() {
		addBaseBlockList(argsL1DownLeft, argsL2DownLeft, argsL3DownLeft, argsL4DownLeft);
		addBaseBlockList(argsL1DownRigth, argsL2DownRigth, argsL3DownRigth, argsL4DownRigth);
	}

	@Override
	public void setNotMove() {
		notmove = new int[]{0,2};
	}
}
