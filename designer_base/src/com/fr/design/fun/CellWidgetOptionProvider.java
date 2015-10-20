package com.fr.design.fun;

import com.fr.design.beans.BasicBeanPane;
import com.fr.form.ui.Widget;

/**
 * @author richie
 * @date 2015-01-29
 * @since 8.0
 * �����е��Զ���ؼ��ӿ�
 */
public interface CellWidgetOptionProvider extends ParameterWidgetOptionProvider {

    public static final String XML_TAG = "CellWidgetOptionProvider";

    /**
     * �Զ�����ӿؼ�����ƽ�����
     * @return �ؼ���ƽ�����
     */
    public Class<? extends BasicBeanPane<? extends Widget>> appearanceForWidget();

}
