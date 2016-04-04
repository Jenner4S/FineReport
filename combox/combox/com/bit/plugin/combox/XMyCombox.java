package com.bit.plugin.combox;

import java.awt.Dimension;
import java.beans.IntrospectionException;

import com.fr.design.designer.creator.CRPropertyDescriptor;
import com.fr.design.designer.creator.XComboBox;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.design.mainframe.widget.editors.InChangeBooleanEditor;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;

public class XMyCombox extends XComboBox {

    public XMyCombox(MyCombox widget, Dimension initSize) {
        super(widget, initSize);
    }
    
    @Override
	public CRPropertyDescriptor[] supportedDescriptor() throws IntrospectionException {
//    	System.out.println("MMMM::"+((MyCombox) this.toData()).isMutiSelect());
//    	System.out.println("MM11::"+((MyCombox) this.toData()).isReturnString());
//    	System.out.println("_______");
		return (CRPropertyDescriptor[]) ArrayUtils.addAll(super.supportedDescriptor(), 
				getAttrUI()
				
				);
	}

	private CRPropertyDescriptor[] getAttrUI() throws IntrospectionException {
		return //				!((ComboCheckBox) this.toData()/
		((MyCombox) this.toData()).isMutiSelect()?getMutiUI():getMutiUIno();
	}

	private CRPropertyDescriptor[] getMutiUIno() throws IntrospectionException {
		return new  CRPropertyDescriptor[]{
				new CRPropertyDescriptor("mutiSelect", this.data.getClass()).setI18NName(
						Inter.getLocText("Myplugin-combox_duoxuan")/*Inter.getLocText("Form-SupportTag")*/).setEditorClass(InChangeBooleanEditor.class).putKeyValue(XCreatorConstants.PROPERTY_CATEGORY,
						Inter.getLocText("Myplugin-combox_leixing"))
				};
	}

	private CRPropertyDescriptor[] getMutiUI() throws IntrospectionException {
		return !((MyCombox) this.toData())
		.isReturnString() ? getReturnStringUIno() : getReturnStringUIyes();
	}

	private CRPropertyDescriptor[] getReturnStringUIyes()
			throws IntrospectionException {
		return new CRPropertyDescriptor[] {
				new CRPropertyDescriptor("mutiSelect", this.data.getClass()).setI18NName(
						Inter.getLocText("Myplugin-combox_duoxuan")/*Inter.getLocText("Form-SupportTag")*/).setEditorClass(InChangeBooleanEditor.class).putKeyValue(XCreatorConstants.PROPERTY_CATEGORY,
						Inter.getLocText("Myplugin-combox_leixing")),
		new CRPropertyDescriptor("delimiter", this.data.getClass()).setI18NName(
				Inter.getLocText("Form-Delimiter")).putKeyValue(XCreatorConstants.PROPERTY_CATEGORY,
				"Advanced"),
		new CRPropertyDescriptor("returnString", this.data.getClass()).setI18NName(
				Inter.getLocText("Return-String")).setEditorClass(InChangeBooleanEditor.class).putKeyValue(
				XCreatorConstants.PROPERTY_CATEGORY, "Return-Value"),
		new CRPropertyDescriptor("startSymbol", this.data.getClass()).setI18NName(
				Inter.getLocText("ComboCheckBox-Start_Symbol")).putKeyValue(
				XCreatorConstants.PROPERTY_CATEGORY, "Return-Value"),
		new CRPropertyDescriptor("endSymbol", this.data.getClass()).setI18NName(
				Inter.getLocText("ComboCheckBox-End_Symbol")).putKeyValue(XCreatorConstants.PROPERTY_CATEGORY,
				"Return-Value") };
	}

	private CRPropertyDescriptor[] getReturnStringUIno() throws IntrospectionException {
		return new CRPropertyDescriptor[] {
				new CRPropertyDescriptor("mutiSelect", this.data.getClass()).setI18NName(
						Inter.getLocText("Myplugin-combox_duoxuan")/*Inter.getLocText("Form-SupportTag")*/).setEditorClass(InChangeBooleanEditor.class).putKeyValue(XCreatorConstants.PROPERTY_CATEGORY,
						Inter.getLocText("Myplugin-combox_leixing")),


		new CRPropertyDescriptor("supportTag", this.data.getClass()).setI18NName(
		        Inter.getLocText("Form-SupportTag")).putKeyValue(XCreatorConstants.PROPERTY_CATEGORY,
		        "Advanced"),
		new CRPropertyDescriptor("delimiter", this.data.getClass()).setI18NName(
				Inter.getLocText("Form-Delimiter")).putKeyValue(XCreatorConstants.PROPERTY_CATEGORY,
				"Advanced"),
		new CRPropertyDescriptor("returnString", this.data.getClass()).setEditorClass(
				InChangeBooleanEditor.class).setI18NName(Inter.getLocText("Return-String")).putKeyValue(
				XCreatorConstants.PROPERTY_CATEGORY, "Return-Value") };
	}

    @Override
    protected String getIconName() {
        return "combo_check_16.png";
    }
}