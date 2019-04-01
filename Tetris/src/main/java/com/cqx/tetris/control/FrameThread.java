package com.cqx.tetris.control;

/**
 * ѭ��BlockThread
 * */
public class FrameThread implements Runnable {
	private Status status = new Status();
	private BlockControl bc;
	private BlockThread bt;
	private Thread t;
	
	public FrameThread(BlockControl _bc){
		bc = _bc;
	}
	
	public boolean isStatus() {
		return status.getFlag();
	}
	
	public void setStatusTrue(){
		synchronized (status) {
			status.setTrue();
		}
	}
	
	public void setStatusFalse(){
		synchronized (status) {
			status.setFalse();
		}
	}

	/**
	 * ����BlockThread�̣߳�ģ���Զ����£��Լ�����ͼƷ�
	 * */
	public void start(){
		setStatusTrue();
		bt = new BlockThread(bc);
		t = new Thread(bt);
		t.start();
	}
	
	@SuppressWarnings("deprecation")
	public void stop(){
		setStatusFalse();
		t.stop();
		bt = null;
		t = null;
	}
	
	public void run() {
		while(isStatus()){
			try {
				Thread.sleep(Score.getInstance().getLevel());
				if(t!=null){
					if(!bt.getStatus() && isStatus()){
						start();
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
