package com.cqx.tetris.block;

public class OneBlock extends BaseBlock {
	//�²���
	private static int[] argsL1DownLeft = {0,1,2,3};//4��1��
	//�²���
	private static int[] argsL1DownRigth = {0,1,2,3};//1��4��
	private boolean changeflag = false;
	
	public OneBlock(){
		super();
		changeRowCol(4, 1);
	}
	
	private void changeRowCol(int _row, int _col){
		row = _row;
		col = _col;
	}

	@Override
	public void setChangelist() {
		addBaseBlockList(argsL1DownLeft);
		addBaseBlockList(argsL1DownRigth);
	}

	@Override
	public void setNotMove() {		
	}

	@Override
	public void next(){
		if(!changeflag){
			changeRowCol(1, 4);
			changeflag = true;
		}else{
			changeRowCol(4, 1);
			changeflag = false;
		}
	}

	@Override
	public int[] getPanelIndexs(int row_index, int col_index, int rows, int cols){
//		if(changeflag && isRight())col_index=col_index-3;
//		if(limitnum==1)col_index=col_index-3;
//		limitnum = 0;//���ñ�־λ���ұ������б�־λ
		return getBasePanelIndexs(row_index, col_index, rows, cols);
	}

//	@Override
//	public int getCurrentNowCol(int now_block_col){
//		int result = now_block_col;
//		if(changeflag && isLeft())result=result+3;
//		else if(changeflag && isRight())result=result-3;
//		return result;
//	}
}
