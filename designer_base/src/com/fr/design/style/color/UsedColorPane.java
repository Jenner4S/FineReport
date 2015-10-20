package com.fr.design.style.color;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.fr.design.dialog.BasicPane;

public class UsedColorPane extends BasicPane{

	public static final Color DEFAULT_COLOR = new Color(222,222,222);
	
	// ���ʹ���������
	private int columns;
	// ���ʹ���������
	private int rows;
	// ���ʹ����ɫ
	private Object[] colors;
	// ���ʹ�����
	private JPanel pane;
	
	private ColorSelectable selectable;
	
	public JPanel getPane() {
		return pane;
	}

	public void setPane(JPanel pane) {
		this.pane = pane;
	}
	
	public UsedColorPane(){
		
	}
	
	/**
	 * ���캯��
	 * 
	 * @param rows ��
	 * @param columns ��
	 * @param colors ���ʹ�õ���ɫ
	 */
	public UsedColorPane(int rows,int columns,Object[] colors,ColorSelectable selectable){
		this.columns = columns;
		this.rows = rows;
		this.colors = colors;
		this.selectable = selectable;
		initialComponents();
	}
	
	private void initialComponents(){
		int total = columns * rows;
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(rows,columns, 1, 1));
		panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 0, 8));
		Color[] colors = ColorSelectConfigManager.getInstance().getColors();
		int size = colors.length;
		for (int i = 0; i < total; i++) {
			Color color = i < size ? colors[size-1-i]: DEFAULT_COLOR;
			panel.add(new ColorCell(color == null ? DEFAULT_COLOR : color, selectable));
		}
		this.pane = panel;
	}
	
	/**
	 * �������ʹ����ɫ
	 * 
	 */
	public void updateUsedColor(){
		int total = columns * rows;
		Color[] colors = ColorSelectConfigManager.getInstance().getColors();
		int size = colors.length;
		for(int i=0; i<total; i++){
			ColorCell cell = (ColorCell) this.pane.getComponent(i);
			Color color = i < size ? colors[size-1-i]: DEFAULT_COLOR;
			cell.setColor(color == null ? DEFAULT_COLOR : color);
		}
	}

	@Override
	protected String title4PopupWindow() {
		return null;
	}
}
