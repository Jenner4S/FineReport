/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.poly.hanlder;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.fr.design.mainframe.DesignerContext;
import com.fr.poly.PolyDesigner;
import com.fr.poly.PolyUtils;
import com.fr.poly.creator.BlockCreator;

/**
 * @author richer
 * @since 6.5.4 ������2011-4-2 ���ھۺϱ����ı༭���ƶ���ɾ���Ȳ����ļ�����
 */
public class DataEditingListener extends MouseAdapter {

	private PolyDesigner designer;

	public DataEditingListener(PolyDesigner designer) {
		this.designer = designer;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (!designer.isFocusOwner()) {
			// ��ȡ���㣬�Ա��ȡ�ȼ�
			designer.requestFocus();
		}

		designer.stopEditing();
		int x = e.getX() + designer.getHorizontalValue();
		int y = e.getY() + designer.getVerticalValue();
		BlockCreator creator = PolyUtils.searchAt(designer, x, y);
		designer.setSelection(creator);
		DesignerContext.getDesignerFrame().resetToolkitByPlus(DesignerContext.getDesignerFrame().getSelectedJTemplate());
	}


	public void mouseMoved(MouseEvent e) {
		if (designer.getSelection() != null) {
			designer.getSelection().checkButtonEnable();
		}
	}

}
