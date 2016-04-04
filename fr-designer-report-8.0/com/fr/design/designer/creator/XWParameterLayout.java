// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator;

import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.adapters.layout.FRParameterLayoutAdapter;
import com.fr.design.mainframe.widget.editors.*;
import com.fr.design.mainframe.widget.renderer.BackgroundRenderer;
import com.fr.design.mainframe.widget.renderer.WidgetDisplayPositionRender;
import com.fr.form.ui.AbstractBorderStyleWidget;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.*;
import com.fr.general.Background;
import com.fr.general.Inter;
import java.awt.Dimension;
import java.beans.IntrospectionException;

// Referenced classes of package com.fr.design.designer.creator:
//            XWAbsoluteLayout, CRPropertyDescriptor

public class XWParameterLayout extends XWAbsoluteLayout
{

    public XWParameterLayout()
    {
        this(new WParameterLayout(), new Dimension());
    }

    public XWParameterLayout(WParameterLayout wparameterlayout)
    {
        this(wparameterlayout, new Dimension());
    }

    public XWParameterLayout(WParameterLayout wparameterlayout, Dimension dimension)
    {
        super(wparameterlayout, dimension);
    }

    public Dimension initEditorSize()
    {
        return new Dimension(960, 65);
    }

    public CRPropertyDescriptor[] supportedDescriptor()
        throws IntrospectionException
    {
        return (new CRPropertyDescriptor[] {
            (new CRPropertyDescriptor("widgetName", data.getClass())).setI18NName(Inter.getLocText("FR-Designer_Form-Widget_Name")), (new CRPropertyDescriptor("background", data.getClass())).setEditorClass(com/fr/design/mainframe/widget/editors/BackgroundEditor).setRendererClass(com/fr/design/mainframe/widget/renderer/BackgroundRenderer).setI18NName(Inter.getLocText("Background")).putKeyValue("category", "Advanced"), (new CRPropertyDescriptor("display", data.getClass())).setEditorClass(com/fr/design/mainframe/widget/editors/BooleanEditor).setI18NName(Inter.getLocText("ParameterD-Show_Parameter_Window")).putKeyValue("category", "Advanced"), (new CRPropertyDescriptor("delayDisplayContent", data.getClass())).setEditorClass(com/fr/design/mainframe/widget/editors/BooleanEditor).setI18NName(Inter.getLocText("FR-Designer_DisplayNothingBeforeQuery")).putKeyValue("category", "Advanced"), (new CRPropertyDescriptor("position", data.getClass())).setEditorClass(com/fr/design/mainframe/widget/editors/WidgetDisplayPosition).setRendererClass(com/fr/design/mainframe/widget/renderer/WidgetDisplayPositionRender).setI18NName(Inter.getLocText("FR-Designer_WidgetDisplyPosition")).putKeyValue("category", "Advanced")
        });
    }

    public LayoutAdapter getLayoutAdapter()
    {
        return new FRParameterLayoutAdapter(this);
    }

    public boolean canEnterIntoParaPane()
    {
        return false;
    }

    public boolean isSupportDrag()
    {
        return false;
    }

    public WParameterLayout toData()
    {
        return (WParameterLayout)data;
    }

    public String createDefaultName()
    {
        return "para";
    }

    public boolean isDelayDisplayContent()
    {
        return toData().isDelayDisplayContent();
    }

    public boolean isDisplay()
    {
        return toData().isDisplay();
    }

    public int getDesignWidth()
    {
        return toData().getDesignWidth();
    }

    public int getPosition()
    {
        return toData().getPosition();
    }

    public void setDelayDisplayContent(boolean flag)
    {
        toData().setDelayDisplayContent(flag);
    }

    public void setPosition(int i)
    {
        toData().setPosition(i);
    }

    public void setDisplay(boolean flag)
    {
        toData().setDisplay(flag);
    }

    public void setBackground(Background background)
    {
        toData().setBackground(background);
    }

    public volatile WAbsoluteLayout toData()
    {
        return toData();
    }

    public volatile WLayout toData()
    {
        return toData();
    }

    public volatile AbstractBorderStyleWidget toData()
    {
        return toData();
    }

    public volatile Widget toData()
    {
        return toData();
    }
}
