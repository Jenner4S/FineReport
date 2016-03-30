package com.fr.plugin.widget.radio;

import com.fr.design.designer.creator.CRPropertyDescriptor;
import com.fr.design.designer.creator.XWidgetCreator;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.widget.editors.DictionaryEditor;
import com.fr.design.mainframe.widget.editors.FontEditor;
import com.fr.design.mainframe.widget.editors.InChangeBooleanEditor;
import com.fr.design.mainframe.widget.editors.WidgetValueEditor;
import com.fr.design.mainframe.widget.renderer.DictionaryRenderer;
import com.fr.design.mainframe.widget.renderer.FontCellRenderer;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;

import javax.swing.*;
import java.awt.*;
import java.beans.IntrospectionException;

/**
 * 
 * @author focus
 * @date Jun 17, 2015
 * @since 8.0
 */
public class XEstateRadioGroup extends XWidgetCreator{
	
	private static final int WIDTH = 120;
	private static final int HEIGHT = 21;
	
	/**
	 * 构造函数
	 * @param widget 对应控件
	 * @param initSize 初始大小
	 */
	public XEstateRadioGroup(EstateRadioGroup widget, Dimension initSize) {
		super(widget, initSize);
	}
	
	/**
	 * 返回XCreator的默认大小180x21
	 * @return 默认的最小大小
	 */
	public Dimension initEditorSize() {
		return new Dimension(WIDTH, HEIGHT);
	}

	/**
	 * 对应控件
	 * @return 同上
	 */
	@Override
	public EstateRadioGroup toData() {
		return (EstateRadioGroup) data;
	}

	@Override
	protected JComponent initEditor() {
		if (editor == null) {
			editor = FRGUIPaneFactory.createBorderLayout_S_Pane();
			editor.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
			ButtonGroup bg = new ButtonGroup();
			UIRadioButton radioLeft = new UIRadioButton();
			radioLeft.setSelected(true);
			UIRadioButton radioRight = new UIRadioButton();
			bg.add(radioLeft);
			bg.add(radioRight);
			editor.add(radioLeft, BorderLayout.WEST);
			editor.add(radioRight, BorderLayout.EAST);
		}
		return editor;
	}
	
	/**
	 * 属性组
	 * @return 同上
	 */
	@Override
	public CRPropertyDescriptor[] supportedDescriptor() throws IntrospectionException {
		return (CRPropertyDescriptor[]) ArrayUtils.addAll(super.supportedDescriptor(),this.getCRPropertyDescriptor());
	}
	
	private CRPropertyDescriptor[] getCRPropertyDescriptor() throws IntrospectionException {

		CRPropertyDescriptor dictionaryCrp = new CRPropertyDescriptor("dictionary", this.data.getClass())
				.setI18NName(Inter.getLocText("DS-Dictionary"))
				.setEditorClass(DictionaryEditor.class).setRendererClass(DictionaryRenderer.class);
		CRPropertyDescriptor showDefaultCrp = new CRPropertyDescriptor("showDefault", this.data.getClass())
				.setI18NName(Inter.getLocText("FR-Designer-Estate_Default_Null"))
				.setEditorClass(InChangeBooleanEditor.class);
		CRPropertyDescriptor defaultNullTxtCrp = new CRPropertyDescriptor("defaultTxt", this.data.getClass())
				.setI18NName(Inter.getLocText("FR-Designer-Estate_Parameter_Null_Text"))
				.putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Advanced");
		CRPropertyDescriptor defaultFontCrp = new CRPropertyDescriptor("defaultFont", this.data.getClass())
				.setI18NName(Inter.getLocText("FR-Designer-Estate_Default_Font"))
				.setEditorClass(FontEditor.class).setRendererClass(FontCellRenderer.class).putKeyValue(
						XCreatorConstants.PROPERTY_CATEGORY, "Advanced");
		CRPropertyDescriptor selectFontCrp = new CRPropertyDescriptor("selectedFont", this.data.getClass())
				.setI18NName(Inter.getLocText("FR-Designer-Estate_Selected_Font"))
				.setEditorClass(FontEditor.class).setRendererClass(FontCellRenderer.class).putKeyValue(
						XCreatorConstants.PROPERTY_CATEGORY, "Advanced");
		CRPropertyDescriptor defaultTxtCrp = new CRPropertyDescriptor("widgetValue", this.data.getClass())
				.setI18NName(Inter.getLocText("FR-Designer-Estate_Default_Text"))
				.setEditorClass(WidgetValueEditor.class);
		
		return toData().isShowDefault() ? new CRPropertyDescriptor[] {showDefaultCrp, dictionaryCrp, defaultNullTxtCrp, defaultFontCrp
				, selectFontCrp} : new CRPropertyDescriptor[]{showDefaultCrp, defaultTxtCrp, dictionaryCrp, defaultNullTxtCrp, defaultFontCrp
				, selectFontCrp};
	}
	
	/**
	 * 图片路径
	 * @return String 同上
	 */
    public  String getIconPath() {
    	return "/com/fr/plugin/widget/radio/images/estate.png";
    }


}
