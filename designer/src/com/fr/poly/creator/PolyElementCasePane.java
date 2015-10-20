/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.poly.creator;

import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.selection.CellSelection;
import com.fr.report.poly.PolyECBlock;

/**
 * @author richer
 * @since 6.5.4 ������2011-4-6
 */
public abstract class PolyElementCasePane extends ElementCasePane<PolyECBlock> {

	public PolyElementCasePane(PolyECBlock block) {
		super(block);
        setSelection(new CellSelection(0, 0, 1, 1));
        // �������������������Ȼ����Ҳ��û�ɼ�����
		setHorizontalScrollBarVisible(false);
		setVerticalScrollBarVisible(false);
	}

    @Override
    protected boolean supportRepeatedHeaderFooter() {
    	return false;
    }
}
