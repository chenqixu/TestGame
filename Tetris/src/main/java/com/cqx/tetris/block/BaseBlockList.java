package com.cqx.tetris.block;

import java.util.ArrayList;
import java.util.List;

public class BaseBlockList {
	protected List<int[]> changelist = null;
	
	public BaseBlockList(){
		changelist = new ArrayList<int[]>();
	}

	public int[] get(int index) {
		return changelist.get(index);
	}

	public void add(int[] change) {
		this.changelist.add(change);
	}
	
	public int size(){
		return changelist.size();
	}
}
