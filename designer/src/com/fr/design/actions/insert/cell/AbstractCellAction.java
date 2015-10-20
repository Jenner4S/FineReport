/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.insert.cell;

import com.fr.design.actions.CellSelectionAction;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.selection.CellSelection;
import com.fr.report.core.SheetUtils;
import com.fr.report.elementcase.TemplateElementCase;

/**
 * Cell.
 */
public abstract class AbstractCellAction extends CellSelectionAction {
	protected AbstractCellAction(ElementCasePane t) {
		super(t);
	}
	
    public abstract Class getCellValueClass();
    
    /*
     * TODO ALEX_SEP ����ķ���boolean����ʾģ���Ƿ�ı�,���ºܶ�ܶ�ķ�����boolean����ֵ���������˼
     * 
     * �о��е��˷�
     */
    @Override
    protected boolean executeActionReturnUndoRecordNeededWithCellSelection(
    		CellSelection cs) {
    	ElementCasePane ePane = this.getEditingComponent();
        //got simple cell element from column and row.
        TemplateElementCase report = ePane.getEditingElementCase();

        //alex:��������Ա���,��ʱ��Ҫ����һ��Ĭ�ϸ�����ʲô
        if (report != null && this instanceof DSColumnCellAction) {
            SheetUtils.calculateDefaultParent(report);
        }
        
        return ePane.getGrid().startCellEditingAt_DEC(
        		cs.getColumn(),
                cs.getRow(), 
                this.getCellValueClass(), false		
        );
    }
	@Override
	public void update() {
		super.update();
		ElementCasePane ePane = this.getEditingComponent();
		this.setEnabled(ePane.isSelectedOneCell());
	}
}