package com.cqx.tetris.control;

import com.cqx.tetris.block.BlockGameOver;
import com.cqx.tetris.panel.PanelFactory;
import com.cqx.tetris.panel.PanelNextFactory;

/**
 * 线程工厂，用于启动FrameThread
 * */
public class ThreadFactory {
	private static ThreadFactory btf = new ThreadFactory();
	private BlockControl bc;
	private FrameThread bt;
	private Thread t;
	
	private ThreadFactory(){
	}
	
	public void setBc(BlockControl bc) {
		this.bc = bc;
	}

	public static ThreadFactory getInstance(){
		if(btf==null)btf = new ThreadFactory();
		return btf;
	}
	
	public boolean isStop(){
		if(bc!=null){
			return !bt.isStatus();
		}
		return false;
	}
	
	/**
	 * 启动
	 * */
	public synchronized void start(){
		if(bc!=null){
			if(bt==null)bt = new FrameThread(bc);
			bc.cleanAll();//清空面板
			bt.start(); //启动BlockThread线程：模块自动往下，以及清除和计分
			t = new Thread(bt);
			t.start(); //启动FrameThread线程：循环BlockThread
		}
	}
	
	/**
	 * 停止
	 * */
	public synchronized void stop(){
		//如果bc不为空
		if(bc!=null){
			//清空PanelFactory
			System.out.println("clean PanelFactory");
			bc.cleanAll();
			//清空分数
			System.out.println("clean score");
			Score.getInstance().cleanScore();
			//通知标签刷新
			System.out.println("notice label flush");
			Score.getInstance().flush();
			//清空PanelNextFactory
			System.out.println("clean PanelNextFactory");
			PanelNextFactory.getInstance().getBlockControl().cleanAll();
			//显示Game Over画面
			PanelFactory.getInstance().getBlockControl().initBlockSome(BlockGameOver.getGameover2Array());
			//停止FrameThread
			System.out.println("stop FrameThread");
			bt.stop();
		}
	}
	
}
