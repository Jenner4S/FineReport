package com.fr.plugin.widget.speedtree;

import com.fr.design.fun.ParameterWidgetOptionProvider;
import com.fr.design.fun.impl.AbstractFormWidgetOptionProvider;
import com.fr.form.ui.Widget;
import com.fr.general.Inter;

/**
 * @author richie
 * @date 2015-03-16
 * @since 8.0
 */
public class SpeedTreeImpl extends AbstractFormWidgetOptionProvider implements ParameterWidgetOptionProvider {
    /**
     * 新下拉树对应类
     * @return 对应的Editor类
     */
    @Override
    public Class<? extends Widget> classForWidget() {
        return SpeedTreeEditor.class;
    }

    /**
     * 下拉树控件对应的界面（右侧属性面板）
     * @return 对应的面板界面类
     */
    @Override
    public Class<?> appearanceForWidget() {
        return XSpeedTreeEditor.class;
    }

    /**
     * 控件图标路径
     * @return 路径
     */
    @Override
    public String iconPathForWidget() {
        return "/com/fr/plugin/widget/speedtree/images/speedtree.png";
    }

    /**
     * 新下拉树名字
     * @return 控件名
     */
    @Override
    public String nameForWidget() {
        return Inter.getLocText("FR-Widget-Plugin-SpeedTreeEditor_Name");
    }
}