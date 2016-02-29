package com.fr.design.fun;

import com.fr.design.beans.BasicBeanPane;
import com.fr.form.ui.Widget;

/**
 * @author richie
 * @date 2015-01-29
 * @since 8.0
 * 格子中的自定义控件接口
 */
public interface CellWidgetOptionProvider extends ParameterWidgetOptionProvider {

    public static final String XML_TAG = "CellWidgetOptionProvider";

    /**
     * 自定义格子控件的设计界面类
     * @return 控件设计界面类
     */
    public Class<? extends BasicBeanPane<? extends Widget>> appearanceForWidget();

}