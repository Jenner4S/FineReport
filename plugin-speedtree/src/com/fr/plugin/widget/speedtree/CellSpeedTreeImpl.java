package com.fr.plugin.widget.speedtree;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.fun.CellWidgetOptionProvider;
import com.fr.design.fun.impl.AbstractCellWidgetOptionProvider;
import com.fr.form.ui.Widget;
import com.fr.general.Inter;

/**
 * Created by zack on 2015/9/30.
 */
public class CellSpeedTreeImpl extends AbstractCellWidgetOptionProvider {

    @Override
    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    /**
     * 下拉框控件对应的类(控件设置)
     * @return 对应的Editor类
     */
    @Override
    public Class<? extends Widget> classForWidget() {
        return SpeedTreeEditor.class;
    }

    /**
     * 下拉框控件对应的界面（控件设置）
     * @return 对应的面板界面类
     */
    @Override
    public Class<? extends BasicBeanPane<? extends Widget>> appearanceForWidget() {
        return SpeedTreeDefinePane.class;
    }

    /**
     * 新下拉框的图标
     * @return 图标路径
     */
    @Override
    public String iconPathForWidget() {
        return "/com/fr/plugin/widget/speedtree/images/speedtree.png";
    }

    /**
     * 控件名字
     * @return 控件名
     */
    @Override
    public String nameForWidget() {
        return Inter.getLocText("FR-Widget-Plugin-SpeedTreeEditor_Name");
    }
}