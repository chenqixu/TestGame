package com.cqx.tetris;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import com.cqx.tetris.block.Block;
import com.cqx.tetris.control.BlockControl;
import com.cqx.tetris.control.Score;
import com.cqx.tetris.control.ThreadFactory;
import com.cqx.tetris.panel.Panel;
import com.cqx.tetris.panel.PanelFactory;
import com.cqx.tetris.panel.PanelNextFactory;

public class TetrisFrame extends JFrame implements KeyListener {
	private static final long serialVersionUID = 1L;
	
	private JPanel panelContent;
	private JPanel panelStatus;
	private List<Block> jblist;
	private JLabel jlabelnextdesc;
	private JPanel panelnext;
	private List<Block> nextlist;
	private JLabel jlabelleveldesc;
	private JLabel jlabellevel;
	private JLabel jlabelscoredesc;
	private JLabel jlabelscore;
	private JLabel jlabelhelpdesc;
	private JLabel jlabelhelp;
	private Panel bp;
	private Panel nextbp;
	private BlockControl bc;

	public TetrisFrame(){
		PanelFactory.getInstance().init(25, 13);
		bp = PanelFactory.getInstance().getPanel();
		bc = PanelFactory.getInstance().getBlockControl();
		
		//主面板
		panelContent = new JPanel(new GridLayout(bp.getRows(),
				bp.getCols()));//网格布局
		jblist = new ArrayList<Block>();
		for(int i=0;i<bp.getSize();i++){
			jblist.add(new Block(bp.getRownum(i), bp.getColnum(i), i));
			panelContent.add(jblist.get(i));
		}
		
		//次面板，网格布局,4行2列
		//布局
		//下一个：下一个图形
		//等级：1
		//分数：0
		//帮助：1-重置，a-左移，s-右移，s-下移，k-变形
		panelStatus = new JPanel(new GridLayout(4, 2));//网格布局,4行2列
		jlabelnextdesc = new JLabel("下一个：");
		jlabelnextdesc.setBackground(Color.BLACK);
		panelStatus.add(jlabelnextdesc);
		
		PanelNextFactory.getInstance().init(4, 4);
		nextbp = PanelNextFactory.getInstance().getPanel();
		panelnext = new JPanel(new GridLayout(nextbp.getRows(), nextbp.getCols()));//网格布局,4行4列
		nextlist = new ArrayList<Block>();
		for(int i=0;i<nextbp.getSize();i++){
			nextlist.add(new Block(nextbp.getRownum(i), nextbp.getColnum(i), i));
			panelnext.add(nextlist.get(i));
		}
		panelStatus.add(panelnext);
		PanelNextFactory.getInstance().setJblist(nextlist);
		
		jlabelleveldesc = new JLabel("等级：");
		jlabelleveldesc.setBackground(Color.BLACK);
		panelStatus.add(jlabelleveldesc);
		
		jlabellevel = new JLabel("1");
		jlabellevel.setBackground(Color.BLACK);
		panelStatus.add(jlabellevel);
		
		jlabelscoredesc = new JLabel("分数：");
		jlabelscoredesc.setBackground(Color.BLACK);
		panelStatus.add(jlabelscoredesc);
		
		jlabelscore = new JLabel("0");
		jlabelscore.setBackground(Color.BLACK);
		panelStatus.add(jlabelscore);
		
		jlabelhelpdesc = new JLabel("帮助：");
		jlabelhelpdesc.setBackground(Color.BLACK);
		panelStatus.add(jlabelhelpdesc);
		
		jlabelhelp = new JLabel("<html><body>1-再来一局<br>a-左移<br>s-右移<br>s-下移<br>k-变形</body></html>");
		jlabelhelp.setBackground(Color.BLACK);
		panelStatus.add(jlabelhelp);
		
		//标签加入计分对象，方便通知
		Score.getInstance().addLevelLabel(jlabellevel);
		Score.getInstance().addScoreLabel(jlabelscore);
		
		Container panel = getContentPane();//主布局容器
		panel.setLayout(new BorderLayout());
		panel.add(panelStatus, BorderLayout.WEST);
		panel.add(panelContent, BorderLayout.EAST);		
		
		pack();//根据控件占据总大小设置JFrame窗口大小
		setLocation(400,200);//设置窗口初始化位置
		setVisible(true);//设置窗口可见
		addKeyListener(this);
		requestFocus();//请求焦点
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		//设置jblist
		PanelFactory.getInstance().setJblist(jblist);
		//设置bc
		ThreadFactory.getInstance().setBc(bc);
		//启动线程
		ThreadFactory.getInstance().start();
	}

	public void keyTyped(KeyEvent e) {
		if (e.getKeyChar()=='S'||e.getKeyChar()=='s') {
			bc.downBaseBlock();
        } else if (e.getKeyChar()=='W'||e.getKeyChar()=='w') {
//        	bc.upBaseBlock();
        } else if (e.getKeyChar()=='A'||e.getKeyChar()=='a') {
        	bc.leftBaseBlock();
        } else if (e.getKeyChar()=='D'||e.getKeyChar()=='d') {
        	bc.rightBaseBlock();
        } else if (e.getKeyChar()=='K'||e.getKeyChar()=='k') {
        	bc.chagenBaseBlock();
        } else if (e.getKeyChar()=='1') {
        	//重新开始，停止活动线程，计分清0，bc清空，启动线程
//        	ThreadFactory.getInstance().stop();
        	//如果不是Ending，不能重新开始
        	if(ThreadFactory.getInstance().isStop()){
        		System.out.println("restart……");
        		ThreadFactory.getInstance().start();
        	}
        }
	}

	public void keyPressed(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
	}
}
