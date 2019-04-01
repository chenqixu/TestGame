package com.cqx.tetris.control;

import javax.swing.JLabel;

public class Score {
	private static Score sc = new Score();
	private int score = 0;
	private JLabel jlabellevel;
	private JLabel jlabelscore;
	
	private Score(){}
	
	public static Score getInstance(){
		if(sc==null)sc = new Score();
		return sc;
	}
		
	public synchronized int getLevel(){
		int level = Contant.LEVEL1;
		if(score>20 && score<40)
			level = Contant.LEVEL2;
		else if(score>40 && score<60)
			level = Contant.LEVEL3;
		else if(score>60 && score<80)
			level = Contant.LEVEL4;
		else if(score>80)
			level = Contant.LEVEL5;
		return level;
	}
	
	public synchronized int getLevelDesc(){
		int level = 1;
		switch (getLevel()) {
		case Contant.LEVEL1:
			level = 1;
			break;
		case Contant.LEVEL2:
			level = 2;
			break;
		case Contant.LEVEL3:
			level = 3;
			break;
		case Contant.LEVEL4:
			level = 4;
			break;
		case Contant.LEVEL5:
			level = 5;
			break;
		default:
			break;
		}
		return level;
	}
	
	public void addLevelLabel(JLabel jl){
		jlabellevel = jl;
	}
	
	public void addScoreLabel(JLabel sc){
		jlabelscore = sc;
	}
	
	/**
	 * �ӷ�
	 * */
	public synchronized void addScore(){
		score++;
		flush();
	}
	
	/**
	 * ֪ͨ��ǩˢ��
	 * */
	public synchronized void flush(){
		// ֪ͨˢ�·���
		jlabelscore.setText(""+getScore());
		// ֪ͨˢ�¼���
		jlabellevel.setText(""+getLevelDesc());
	}
	
	/**
	 * ��ȡ����
	 * */
	public synchronized int getScore(){
		return score;
	}
	
	/**
	 * ����
	 * */
	public synchronized void cleanScore(){
		score = 0;
	}
}
