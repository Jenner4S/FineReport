/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.creator;

import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.adapters.layout.FRParameterLayoutAdapter;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.design.mainframe.widget.editors.BackgroundEditor;
import com.fr.design.mainframe.widget.editors.BooleanEditor;
import com.fr.design.mainframe.widget.editors.WidgetDisplayPosition;
import com.fr.design.mainframe.widget.renderer.BackgroundRenderer;
import com.fr.design.mainframe.widget.renderer.WidgetDisplayPositionRender;
import com.fr.form.ui.container.WBorderLayout;
import com.fr.form.ui.container.WFitLayout;
import com.fr.form.ui.container.WParameterLayout;
import com.fr.general.Background;
import com.fr.general.Inter;

import java.awt.*;
import java.beans.IntrospectionException;

/**
 * ����������container
 */
public class XWParameterLayout extends XWAbsoluteLayout {
	
	public XWParameterLayout() {
		this(new WParameterLayout(), new Dimension());
	}
	
	public XWParameterLayout(WParameterLayout widget) {
		this(widget,new Dimension());
	}

	public XWParameterLayout(WParameterLayout widget, Dimension initSize) {
		super(widget, initSize);
	}

    /**
     * ��ʼ���ߴ�
     * @return    �ߴ�
     */
    public Dimension initEditorSize() {
        return new Dimension(WFitLayout.DEFAULT_WIDTH, WBorderLayout.DEFAULT_SIZE);
    }

    /**
     * ����������Ա�
     * @return ����
     * @throws java.beans.IntrospectionException
     */
    public CRPropertyDescriptor[] supportedDescriptor() throws IntrospectionException {
        return  new CRPropertyDescriptor[]{
                new CRPropertyDescriptor("widgetName", this.data.getClass()).setI18NName(Inter
                        .getLocText("FR-Designer_Form-Widget_Name")),
                new CRPropertyDescriptor("background", this.data.getClass()).setEditorClass(BackgroundEditor.class)
                        .setRendererClass(BackgroundRenderer.class).setI18NName(Inter.getLocText("Background"))
                        .putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Advanced"),        
                new CRPropertyDescriptor("display", this.data.getClass()).setEditorClass(BooleanEditor.class)
                        .setI18NName(Inter.getLocText("ParameterD-Show_Parameter_Window"))
                        .putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Advanced"),
                new CRPropertyDescriptor("delayDisplayContent", this.data.getClass()).setEditorClass(BooleanEditor.class)
                        .setI18NName(Inter.getLocText("FR-Designer_DisplayNothingBeforeQuery"))
                        .putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Advanced"),
                new CRPropertyDescriptor("position", this.data.getClass()).setEditorClass(WidgetDisplayPosition.class)
                     .setRendererClass(WidgetDisplayPositionRender.class).setI18NName(Inter.getLocText("FR-Designer_WidgetDisplyPosition"))
                        .putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Advanced"),
        };
    }
    
	@Override
	public LayoutAdapter getLayoutAdapter() {
		return new FRParameterLayoutAdapter(this);
	}

    /**
     * ������Ƿ��������������
     * @return ���򷵻�true
     */
    public boolean canEnterIntoParaPane(){
        return false;
    }

    /**
     * ������Ƿ������ק(���в�����������Ӧ���ֲ�������ק)
     * @return ���򷵻�true
     */
    public boolean isSupportDrag(){
        return false;
    }


    /**
     *  ���ض�Ӧ��widget����
     * @return   ��Ӧ����
     */
    public WParameterLayout toData() {
        return (WParameterLayout) data;
    }

    /**
     * �ؼ�Ĭ������
     * @return   ����
     */
    public String createDefaultName() {
        return "para";
    }

    /**
     * �Ƿ��ӳ�չʾ�������ݣ�Ҳ����˵�Ƿ�Ҫ�ȵ���˲�ѯ֮���ִ�б���
     * @return �����true�����ʾ���֮��ſ�ʼ���㣬false���ʾ����ݲ���Ĭ��ֱֵ�Ӽ��㱨��չ��
     */
    public boolean isDelayDisplayContent() {
        return toData().isDelayDisplayContent();
    }

    /**
     * �Ƿ���ʾ��������
     * @return ��ʾ���������򷵻�true�����򷵻�false
     */
    public boolean isDisplay() {
        return toData().isDisplay();
    }

    /**
     * ��ȡ��������Ŀ��
     * @return ���
     */
    public int getDesignWidth() {
        return toData().getDesignWidth();
    }

    /**
     * ��ȡ�������Ķ��뷽ʽ
     * @return ���������ֶ��뷽ʽ
     */
    public int getPosition() {
        return toData().getPosition();
    }

    public void setDelayDisplayContent(boolean delayPlaying){
        this.toData().setDelayDisplayContent(delayPlaying);
    }

    public void setPosition(int align){
        this.toData().setPosition(align);
    }

    public void setDisplay(boolean showWindow){
        this.toData().setDisplay(showWindow);
    }

    public void setBackground(Background background){
        this.toData().setBackground(background);
    }
}
