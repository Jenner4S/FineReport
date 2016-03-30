package com.fr.plugin.widget.scancodewidget;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.fun.impl.AbstractCellWidgetOptionProvider;
import com.fr.form.ui.Widget;
import com.fr.general.Inter;

/**
 * Created by joyxu on 2016/3/20.
 */
public class CellScanmpl extends AbstractCellWidgetOptionProvider {

    @Override
    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }
    @Override
    public Class<? extends Widget> classForWidget() {
        return Scan.class;
    }

    @Override
    public Class<? extends BasicBeanPane<? extends Widget>> appearanceForWidget() {
        return CellScanPane.class;
    }

    @Override
    public String iconPathForWidget() {
        return "/com/fr/plugin/widget/scancodewidget/images/scancodeIcon.png";
    }

    @Override
    public String nameForWidget() {
         return Inter.getLocText("FR-Designer-Scan_Scan_Code");
    }
}
