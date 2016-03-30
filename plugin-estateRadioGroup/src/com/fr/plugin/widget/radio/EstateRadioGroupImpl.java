package com.fr.plugin.widget.radio;

import com.fr.design.fun.FormWidgetOptionProvider;
import com.fr.design.fun.ParameterWidgetOptionProvider;
import com.fr.design.fun.impl.AbstractFormWidgetOptionProvider;
import com.fr.form.ui.Widget;
import com.fr.general.Inter;

/**
 * 地产行业控件单选按钮组实现类
 * @author focus
 * @date Jun 17, 2015
 * @since 8.0
 */
public class EstateRadioGroupImpl extends AbstractFormWidgetOptionProvider implements ParameterWidgetOptionProvider,FormWidgetOptionProvider{
    /**
     * 待说明
     * @return 同上
     */
	@Override
    public Class<? extends Widget> classForWidget() {
        return EstateRadioGroup.class;
    }

	/**
	 * 待说明
	 * @return 同上
	 */
    @Override
    public Class<?> appearanceForWidget() {
        return XEstateRadioGroup.class;
    }

    /**
     * 图片路径
     * @return String 同上
     */
    @Override
    public String iconPathForWidget() {
        return "/com/fr/plugin/widget/radio/images/estate.png";
    }

    /**
     * 控件名称
     * @return String 同上
     */
    @Override
    public String nameForWidget() {
        return Inter.getLocText("FR-Designer-Estate_Radio-Group");
    }
}
