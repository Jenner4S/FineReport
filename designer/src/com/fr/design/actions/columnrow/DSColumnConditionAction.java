package com.fr.design.actions.columnrow;

import com.fr.base.BaseUtils;
import com.fr.design.data.DesignTableDataManager;
import com.fr.design.actions.cell.AbstractCellElementAction;
import com.fr.design.dscolumn.DSColumnConditionsPane;
import com.fr.design.dialog.BasicPane;
import com.fr.general.Inter;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.report.cell.TemplateCellElement;

public class DSColumnConditionAction extends AbstractCellElementAction {

    private boolean returnValue = false;
    private TemplateCellElement editCellElement;

    public DSColumnConditionAction(ElementCasePane t) {
		super(t);
		
        this.setName(Inter.getLocText("Filter"));
        this.setMnemonic('E');
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/expand/cellAttr.gif"));
    }

    @Override
	protected BasicPane populateBasicPane(TemplateCellElement cellElement) {
        DSColumnConditionsPane dSColumnConditionsPane = new DSColumnConditionsPane();
        dSColumnConditionsPane.populate(DesignTableDataManager.getEditingTableDataSource(), cellElement);
        return dSColumnConditionsPane;
    }

    @Override
	protected void updateBasicPane(BasicPane bp, TemplateCellElement cellElement) {
        ((DSColumnConditionsPane) bp).update(cellElement);
    }
}