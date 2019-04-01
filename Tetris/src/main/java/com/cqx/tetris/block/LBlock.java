package com.cqx.tetris.block;

public class LBlock extends BaseBlock {
//	private static int[] argsL1 = {0,3,6,7};//L
//	private static int[] argsL2 = {5,6,7,8};
//	private static int[] argsL3 = {1,2,5,8};
//	private static int[] argsL4 = {0,1,2,3};
	
	//ÏÂ²ã×ó
	private static int[] argsL1DownLeft = {0,3,6,7};//L
	private static int[] argsL2DownLeft = {5,6,7,8};
	private static int[] argsL3DownLeft = {0,1,4,7};
	private static int[] argsL4DownLeft = {3,4,5,6};
	
	//ÏÂ²ãÓÒ
	private static int[] argsL1DownRigth = {1,4,7,8};//L
	private static int[] argsL2DownRigth = {5,6,7,8};
	private static int[] argsL3DownRigth = {1,2,5,8};
	private static int[] argsL4DownRigth = {3,4,5,6};

	@Override
	public void setChangelist() {
//		changelist.add(argsL1);
//		changelist.add(argsL2);
//		changelist.add(argsL3);
//		changelist.add(argsL4);
//		addBaseBlockList(argsL1, argsL2, argsL3, argsL4);
		addBaseBlockList(argsL1DownLeft, argsL2DownLeft, argsL3DownLeft, argsL4DownLeft);
		addBaseBlockList(argsL1DownRigth, argsL2DownRigth, argsL3DownRigth, argsL4DownRigth);
	}

	@Override
	public void setNotMove() {
		notmove = new int[]{0,2};
	}
}
