package com.bit.plugin.combox;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.fun.impl.AbstractCellWidgetOptionProvider;
import com.fr.form.ui.Widget;
import com.fr.general.Inter;

public class CellCombox extends AbstractCellWidgetOptionProvider {

    @Override
    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    @Override
    public Class<? extends Widget> classForWidget() {
        return MyCombox.class;
    }

    @Override
    public Class<? extends BasicBeanPane<? extends Widget>> appearanceForWidget() {
        return CellComboBoxUI.class;
    }

    @Override
    public String iconPathForWidget() {
        return "/com/fr/plugin/widget/ztree/images/drop_down_tree.png";
    }

    @Override
    public String nameForWidget() {
        return Inter.getLocText("Myplugin-combox_name");
    }



}