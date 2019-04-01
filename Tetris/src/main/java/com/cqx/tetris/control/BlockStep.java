package com.cqx.tetris.control;

public class BlockStep {
	private int step_num = 0;
	
	public void add(){
		step_num++;
	}
	
	public void clean(){
		step_num = 0;
	}
	
	public int getStep_num() {
		return step_num;
	}
}
