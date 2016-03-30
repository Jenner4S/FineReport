package com.fr.design.fun;

import com.fr.form.ui.Widget;
import com.fr.stable.fun.Level;

/**
 * @author : richie
 * @since : 8.0
 * 自定义参数界面控件
 */
public interface ParameterWidgetOptionProvider extends Level{

    String XML_TAG = "ParameterWidgetOptionProvider";

    int CURRENT_LEVEL = 1;


    /**
     * 自定义参数控件的实际类，该类需要继承自com.fr.form.ui.Widget
     * @return 控件类
     */
    Class<? extends Widget> classForWidget();

    /**
     * 自定义参数控件的设计界面类，该类需要继承自com.fr.form.designer.creator.XWidgetCreator
     * @return 控件设计界面类
     */
    Class<?> appearanceForWidget();

    /**
     * 自定义参数控件在设计器界面上的图标路径
     * @return 图标所在的路径
     */
    String iconPathForWidget();

    /**
     * 自定义参数控件的名字
     * @return 控件名字
     */
    String nameForWidget();
}