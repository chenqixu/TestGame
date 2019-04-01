package com.cqx.tetris.block;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;

public class Block extends JButton{
	private static final long serialVersionUID = 1L;
	private boolean status = false;
	private int rownum = 0;//�к�
	private int colnum = 0;//�к�
	private int listindex = 0;//list�е����

	public Block(){
		super();
		setFocusable(false);//���ý���ʧЧ
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(30, 30));
	}
	
	public Block(int _rownum, int _colnum, int _listindex){
		super();
		setFocusable(false);//���ý���ʧЧ
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(30, 30));
		rownum = _rownum;
		colnum = _colnum;
		listindex = _listindex;
	}
	
	/**
	 * ��ʼ����
	 * */
	public void init(){
		status = true;
		setBackground(Color.BLACK);
	}
	
	/**
	 * �����
	 * */
	public void clean(){
		status = false;
		setBackground(Color.WHITE);
	}
	
	/**
	 * ���Ƿ�
	 * */
	public boolean isActive(){
		return status;
	}

	/**
	 * ����к�
	 * */
	public int getRownum() {
		return rownum;
	}

	/**
	 * ����к�
	 * */
	public int getColnum() {
		return colnum;
	}

	/**
	 * �����List�е�����λ��
	 * */
	public int getListindex() {
		return listindex;
	}
}
