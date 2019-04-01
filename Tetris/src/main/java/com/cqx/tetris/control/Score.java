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
	 * 加分
	 * */
	public synchronized void addScore(){
		score++;
		flush();
	}
	
	/**
	 * 通知标签刷新
	 * */
	public synchronized void flush(){
		// 通知刷新分数
		jlabelscore.setText(""+getScore());
		// 通知刷新级别
		jlabellevel.setText(""+getLevelDesc());
	}
	
	/**
	 * 获取分数
	 * */
	public synchronized int getScore(){
		return score;
	}
	
	/**
	 * 清零
	 * */
	public synchronized void cleanScore(){
		score = 0;
	}
}
