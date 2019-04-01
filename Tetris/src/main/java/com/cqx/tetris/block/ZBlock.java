package com.cqx.tetris.block;

public class ZBlock extends BaseBlock {
//	private static int[] argsL1 = {0,3,4,7};//Z
//	private static int[] argsL2 = {0,1,2,4};
	
	//ÏÂ²ã×ó
	private static int[] argsL1DownLeft = {1,3,4,6};//Z
	private static int[] argsL2DownLeft = {3,4,7,8};
	
	//ÏÂ²ãÓÒ
	private static int[] argsL1DownRigth = {2,4,5,7};//Z
	private static int[] argsL2DownRigth = {3,4,7,8};

	@Override
	public void setChangelist() {	
//		changelist.add(argsL1);
//		changelist.add(argsL2);
//		addBaseBlockList(argsL1, argsL2);
		addBaseBlockList(argsL1DownLeft, argsL2DownLeft);
		addBaseBlockList(argsL1DownRigth, argsL2DownRigth);
	}

	@Override
	public void setNotMove() {
		notmove = new int[]{0};
	}
}
