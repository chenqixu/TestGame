package com.cqx.tetris.block;

public class OBlock extends BaseBlock {
//	private static int[] argsL1 = {0,1,3,4};//O
	//下层左
	private static int[] argsL1DownLeft = {3,4,6,7};//O
	//下层右
	private static int[] argsL1DownRigth = {4,5,7,8};//O

	@Override
	public void setChangelist() {
//		changelist.add(argsL1);	
//		addBaseBlockList(argsL1);
		addBaseBlockList(argsL1DownLeft);
		addBaseBlockList(argsL1DownRigth);
	}

	@Override
	public void setNotMove() {
		notmove = new int[]{0};
	}
}
