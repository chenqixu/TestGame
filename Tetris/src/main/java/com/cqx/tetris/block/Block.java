package com.cqx.tetris.block;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;

public class Block extends JButton{
	private static final long serialVersionUID = 1L;
	private boolean status = false;
	private int rownum = 0;//行号
	private int colnum = 0;//列号
	private int listindex = 0;//list中的序号

	public Block(){
		super();
		setFocusable(false);//设置焦点失效
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(30, 30));
	}
	
	public Block(int _rownum, int _colnum, int _listindex){
		super();
		setFocusable(false);//设置焦点失效
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(30, 30));
		rownum = _rownum;
		colnum = _colnum;
		listindex = _listindex;
	}
	
	/**
	 * 初始化块
	 * */
	public void init(){
		status = true;
		setBackground(Color.BLACK);
	}
	
	/**
	 * 清理块
	 * */
	public void clean(){
		status = false;
		setBackground(Color.WHITE);
	}
	
	/**
	 * 块是否活动
	 * */
	public boolean isActive(){
		return status;
	}

	/**
	 * 获得行号
	 * */
	public int getRownum() {
		return rownum;
	}

	/**
	 * 获得列号
	 * */
	public int getColnum() {
		return colnum;
	}

	/**
	 * 获得在List中的索引位置
	 * */
	public int getListindex() {
		return listindex;
	}
}
