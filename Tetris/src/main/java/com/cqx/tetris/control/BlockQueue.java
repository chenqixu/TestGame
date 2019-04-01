package com.cqx.tetris.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.cqx.tetris.block.BaseBlock;
import com.cqx.tetris.block.L1Block;
import com.cqx.tetris.block.LBlock;
import com.cqx.tetris.block.OBlock;
import com.cqx.tetris.block.OneBlock;
import com.cqx.tetris.block.TBlock;
import com.cqx.tetris.block.Z1Block;
import com.cqx.tetris.block.ZBlock;

/**
 * 随机生成N个模块
 * 提供获取当前和下一个模块方法
 * */
public class BlockQueue {
	private Random r1 = new Random();
	private static BlockQueue bq = new BlockQueue();
	private List<BaseBlock> bblist = new ArrayList<BaseBlock>();
	private int index = 0;
	
	private BlockQueue(){
		random(100);
	}
	
	public static BlockQueue getInstance(){
		if(bq==null)bq = new BlockQueue();
		return bq;
	}
	
	public void random(int index){
		for(int i=0;i<index;i++){
			bblist.add(random());
		}
	}
	
	public synchronized BaseBlock getNext(){
		if(bblist.size()<=(index+1))random(100);
		return bblist.get(index+1);
	}
	
	public BaseBlock getNow(){
		return bblist.get(index);
	}
	
	public synchronized void next(){
		index++;
	}
	
	private BaseBlock random(){
		BaseBlock bb;
		int i = r1.nextInt(7);
		switch (i) {
		case 0:
			bb = new LBlock();
			break;
		case 1:
			bb = new OBlock();
			break;
		case 2:
			bb = new TBlock();
			break;
		case 3:
			bb = new ZBlock();
			break;
		case 4:
			bb = new Z1Block();
			break;
		case 5:
			bb = new L1Block();
			break;
		case 6:
			bb = new OneBlock();
			break;
		default:
			bb = new TBlock();
			break;
		}
		return bb;
	}
}
