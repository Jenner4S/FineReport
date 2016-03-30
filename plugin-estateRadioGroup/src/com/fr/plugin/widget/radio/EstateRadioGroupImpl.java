package com.fr.plugin.widget.radio;

import com.fr.design.fun.FormWidgetOptionProvider;
import com.fr.design.fun.ParameterWidgetOptionProvider;
import com.fr.design.fun.impl.AbstractFormWidgetOptionProvider;
import com.fr.form.ui.Widget;
import com.fr.general.Inter;

/**
 * �ز���ҵ�ؼ���ѡ��ť��ʵ����
 * @author focus
 * @date Jun 17, 2015
 * @since 8.0
 */
public class EstateRadioGroupImpl extends AbstractFormWidgetOptionProvider implements ParameterWidgetOptionProvider,FormWidgetOptionProvider{
    /**
     * ��˵��
     * @return ͬ��
     */
	@Override
    public Class<? extends Widget> classForWidget() {
        return EstateRadioGroup.class;
    }

	/**
	 * ��˵��
	 * @return ͬ��
	 */
    @Override
    public Class<?> appearanceForWidget() {
        return XEstateRadioGroup.class;
    }

    /**
     * ͼƬ·��
     * @return String ͬ��
     */
    @Override
    public String iconPathForWidget() {
        return "/com/fr/plugin/widget/radio/images/estate.png";
    }

    /**
     * �ؼ�����
     * @return String ͬ��
     */
    @Override
    public String nameForWidget() {
        return Inter.getLocText("FR-Designer-Estate_Radio-Group");
    }
}
