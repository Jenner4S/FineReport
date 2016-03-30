package com.fr.plugin.widget.scancodewidget;

import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.widget.ui.AbstractDataModify;

import javax.swing.*;

/**
 * Created by joyxu on 2016/3/20.
 */
public class CellScanPane extends AbstractDataModify<Scan> {

    public CellScanPane(){
        initComponents();
    }

    private void initComponents() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(0, 13, 0, 0));
    }

    @Override
    public void populateBean(Scan scan) {

    }

    @Override
    public Scan updateBean() {
        return null;
    }

    @Override
    protected String title4PopupWindow() {
        return "scan";
    }
}
