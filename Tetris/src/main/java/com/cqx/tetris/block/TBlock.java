package com.cqx.tetris.block;

public class TBlock extends BaseBlock {
//	//上层
//	private static int[] argsL1 = {0,1,2,4};//T
//	private static int[] argsL3 = {1,3,4,5};
//	private static int[] argsL2 = {1,3,4,7};
//	private static int[] argsL4 = {1,4,5,7};
	
	//下层左
	private static int[] argsL1DownLeft = {1,3,4,7};
	private static int[] argsL2DownLeft = {4,6,7,8};
	private static int[] argsL3DownLeft = {0,3,4,6};
	private static int[] argsL4DownLeft = {3,4,5,7};
	
	//下层右
	private static int[] argsL1DownRigth = {2,4,5,8};
	private static int[] argsL2DownRigth = {4,6,7,8};
	private static int[] argsL3DownRigth = {1,4,5,7};
	private static int[] argsL4DownRigth = {3,4,5,7};

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
