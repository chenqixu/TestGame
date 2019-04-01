package com.cqx.tetris.control;

import com.cqx.tetris.block.BlockGameOver;
import com.cqx.tetris.panel.PanelFactory;
import com.cqx.tetris.panel.PanelNextFactory;

/**
 * �̹߳�������������FrameThread
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
	 * ����
	 * */
	public synchronized void start(){
		if(bc!=null){
			if(bt==null)bt = new FrameThread(bc);
			bc.cleanAll();//������
			bt.start(); //����BlockThread�̣߳�ģ���Զ����£��Լ�����ͼƷ�
			t = new Thread(bt);
			t.start(); //����FrameThread�̣߳�ѭ��BlockThread
		}
	}
	
	/**
	 * ֹͣ
	 * */
	public synchronized void stop(){
		//���bc��Ϊ��
		if(bc!=null){
			//���PanelFactory
			System.out.println("clean PanelFactory");
			bc.cleanAll();
			//��շ���
			System.out.println("clean score");
			Score.getInstance().cleanScore();
			//֪ͨ��ǩˢ��
			System.out.println("notice label flush");
			Score.getInstance().flush();
			//���PanelNextFactory
			System.out.println("clean PanelNextFactory");
			PanelNextFactory.getInstance().getBlockControl().cleanAll();
			//��ʾGame Over����
			PanelFactory.getInstance().getBlockControl().initBlockSome(BlockGameOver.getGameover2Array());
			//ֹͣFrameThread
			System.out.println("stop FrameThread");
			bt.stop();
		}
	}
	
}
