package com.bit.plugin.combox;

import com.fr.design.fun.ParameterWidgetOptionProvider;
import com.fr.design.fun.impl.AbstractFormWidgetOptionProvider;
import com.fr.form.ui.Widget;
import com.fr.general.Inter;
import com.bit.plugin.combox.MyCombox;

public class FormCombox extends AbstractFormWidgetOptionProvider implements ParameterWidgetOptionProvider {

    @Override
    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    @Override
    public Class<? extends Widget> classForWidget() {
        return MyCombox.class; 
    }

    @Override
    public Class<?> appearanceForWidget() {
        return XMyCombox.class;
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