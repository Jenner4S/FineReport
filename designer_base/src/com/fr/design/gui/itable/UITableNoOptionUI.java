package com.fr.design.gui.itable;

import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputListener;

/**
 * ɾ����ť ����ʾ��
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2012-11-14 ����12:57:53
 */
public class UITableNoOptionUI extends UITableUI {
	
	protected MouseInputListener createMouseInputListener() {
		return new MyMouseListener() {
			public void mousePressed(MouseEvent e) {
				if (e.getX() >= table.getWidth() - 20) {
					((UITable)table).removeLine(table.rowAtPoint(e.getPoint()));
					((UITable)table).fireTargetChanged();
					((UITable)table).getParent().doLayout();
				}
			}
		};
	}
}
