/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.cell;

import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fr.base.BaseUtils;
import com.fr.base.CellBorderStyle;
import com.fr.design.actions.ElementCaseAction;
import com.fr.design.actions.core.ActionUtils;
import com.fr.design.style.BorderUtils;
import com.fr.general.Inter;
import com.fr.design.mainframe.ElementCasePane;

/**
 * Border.
 */
public class BorderAction extends ElementCaseAction implements ChangeListener {

    private CellBorderStyle oldCellBorderStyle;

    public BorderAction(ElementCasePane t) {
        super(t);

        this.setName(Inter.getLocText("M_Format_A-Border"));
    }

    public void stateChanged(ChangeEvent e) {
        this.actionPerformedUndoable();
    }

    @Override
    public boolean executeActionReturnUndoRecordNeeded() {
        Object object = this.getValue(UIToolbarBorderButton.class.getName());
        ElementCasePane reportPane = this.getEditingComponent();
        if (reportPane == null) {
            return false;
        }
        if (object != null && object instanceof UIToolbarBorderButton) {
            oldCellBorderStyle = ((UIToolbarBorderButton) object).getCellBorderStyle();

            return this.update(reportPane);
        }
        return false;
    }

    @Override
    public JComponent createToolBarComponent() {
        Object object = this.getValue(UIToolbarBorderButton.class.getName());
        if (object == null || !(object instanceof UIToolbarBorderButton)) {
            UIToolbarBorderButton borderStylePane = new UIToolbarBorderButton(BaseUtils.readIcon("/com/fr/design/images/m_format/noboder.png"), this.getEditingComponent());
            this.putValue(UIToolbarBorderButton.class.getName(), borderStylePane);
            borderStylePane.setEnabled(this.isEnabled());
            borderStylePane.set4Toolbar();
            // peter:产生tooltip
            borderStylePane.setToolTipText(ActionUtils.createButtonToolTipText(this));
            borderStylePane.setCellBorderStyle(new CellBorderStyle());
            borderStylePane.addStyleChangeListener(this);
            return borderStylePane;
        }

        return (JComponent) object;
    }

    public boolean update(ElementCasePane elementCasePane) {
        if (oldCellBorderStyle.isNoneBorderStyle()) {
            //无边框格式
            return BorderUtils.updateCellBorderStyle(elementCasePane, oldCellBorderStyle);
        }
        return BorderUtils.update(elementCasePane, oldCellBorderStyle);
    }

    @Override
    public void update() {
        this.setEnabled(true);
        ElementCasePane reportPane = this.getEditingComponent();
        if (reportPane == null) {
            this.setEnabled(false);
            return;
        }
    }
}