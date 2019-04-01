package com.cqx.tetris.control;

//import java.util.Random;

//import com.cqx.tetris.block.BaseBlock;
//import com.cqx.tetris.block.L1Block;
//import com.cqx.tetris.block.LBlock;
//import com.cqx.tetris.block.OBlock;
//import com.cqx.tetris.block.TBlock;
//import com.cqx.tetris.block.Z1Block;
//import com.cqx.tetris.block.ZBlock;
//import com.cqx.tetris.block.OneBlock;
import com.cqx.tetris.panel.PanelNextFactory;

/**
 * 模块自动往下，以及清除和计分
 * */
public class BlockThread implements Runnable {
	private BlockControl bc;
	private boolean status = false;
	private BlockStep bs;
//	private Random r1 = new Random();
	
	public BlockThread(BlockControl _bc){
		bc = _bc;		
//		bc.initBaseBlock(new OneBlock(), 0, 4);
//		bc.initBaseBlock(new ZBlock(), 0, 4);
//		bc.initBaseBlock(random(), 0, 4);
		//从队列中获取当前
		bc.initBaseBlock(BlockQueue.getInstance().getNow(), 0, bc.getHalfCols());
		bs = new BlockStep();
		bc.setBs(bs);
	}
	
//	private BaseBlock random(){
//		BaseBlock bb;
//		int i = r1.nextInt(6);
//		switch (i) {
//		case 0:
//			bb = new LBlock();
//			break;
//		case 1:
//			bb = new OBlock();
//			break;
//		case 2:
//			bb = new TBlock();
//			break;
//		case 3:
//			bb = new ZBlock();
//			break;
//		case 4:
//			bb = new Z1Block();
//			break;
//		case 5:
//			bb = new L1Block();
//			break;
//		default:
//			bb = new TBlock();
//			break;
//		}
//		return bb;
//	}

	public boolean getStatus() {
		return status;
	}

	public void run() {
		//下一个模块设置
		PanelNextFactory.getInstance().getBlockControl().cleanAll();
		PanelNextFactory.getInstance().getBlockControl().initBaseBlock(BlockQueue.getInstance().getNext()
				, 0, 1);
		while(!bc.isEnd()){			
			try {
				status = true;
				Thread.sleep(Score.getInstance().getLevel());
				//往下移动
				bc.downBaseBlock();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//清除完成的行，每行记1分
		bc.cleanCompleteBlock();
		status = false;
		//Game Over
		if(bs.getStep_num()==0){
			//停止ThreadFactory
			ThreadFactory.getInstance().stop();			
		}else{
			//队列下一个
			BlockQueue.getInstance().next();
			//计步器清空
			bs.clean();
		}
	}
}
