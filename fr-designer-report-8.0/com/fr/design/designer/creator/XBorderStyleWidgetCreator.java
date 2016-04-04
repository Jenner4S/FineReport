// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator;

import com.fr.base.Formula;
import com.fr.design.border.UIRoundedBorder;
import com.fr.form.ui.*;
import com.fr.form.ui.container.WAbsoluteLayout;
import com.fr.form.ui.container.WTitleLayout;
import com.fr.general.ComparatorUtils;
import java.awt.*;
import java.io.Serializable;
import javax.swing.JComponent;

// Referenced classes of package com.fr.design.designer.creator:
//            XWidgetCreator, XWFitLayout, XWTitleLayout, XLabel

public class XBorderStyleWidgetCreator extends XWidgetCreator
{

    private int cornerSize;
    private int noneSize;

    public XBorderStyleWidgetCreator(Widget widget, Dimension dimension)
    {
        super(widget, dimension);
        cornerSize = 15;
        noneSize = 0;
    }

    public AbstractBorderStyleWidget toData()
    {
        return (AbstractBorderStyleWidget)data;
    }

    protected void initStyle()
    {
        LayoutBorderStyle layoutborderstyle = toData().getBorderStyle();
        initBorderStyle();
        if(ComparatorUtils.equals(Integer.valueOf(layoutborderstyle.getType()), Integer.valueOf(1)))
            initTitleStyle(layoutborderstyle);
        else
            clearTitleWidget();
    }

    protected void initBorderStyle()
    {
        LayoutBorderStyle layoutborderstyle = toData().getBorderStyle();
        if(layoutborderstyle != null && layoutborderstyle.getBorder() != 0)
            setBorder(new UIRoundedBorder(layoutborderstyle.getBorder(), layoutborderstyle.getColor(), layoutborderstyle.isCorner() ? cornerSize : noneSize));
        else
            setBorder(DEFALUTBORDER);
    }

    private void clearTitleWidget()
    {
        if(acceptType(new Class[] {
    com/fr/design/designer/creator/XWFitLayout
}))
            return;
        XWTitleLayout xwtitlelayout = (XWTitleLayout)getParent();
        if(xwtitlelayout.getComponentCount() > 1)
            xwtitlelayout.remove(xwtitlelayout.getTitleCreator());
    }

    protected void initTitleStyle(LayoutBorderStyle layoutborderstyle)
    {
        if(layoutborderstyle.getTitle() == null)
            return;
        XWTitleLayout xwtitlelayout = (XWTitleLayout)getParent();
        if(xwtitlelayout.getComponentCount() > 1)
        {
            XLabel xlabel = (XLabel)xwtitlelayout.getTitleCreator();
            Label label = xlabel.toData();
            updateTitleWidgetStyle(label, layoutborderstyle);
            xlabel.initXCreatorProperties();
            return;
        } else
        {
            XLabel xlabel1 = new XLabel(new Label(), new Dimension());
            Label label1 = xlabel1.toData();
            updateTitleWidgetStyle(label1, layoutborderstyle);
            xwtitlelayout.add(xlabel1, "Title");
            xlabel1.initXCreatorProperties();
            WTitleLayout wtitlelayout = xwtitlelayout.toData();
            wtitlelayout.updateChildBounds(wtitlelayout.getBodyBoundsWidget().getBounds());
            return;
        }
    }

    private void updateTitleWidgetStyle(Label label, LayoutBorderStyle layoutborderstyle)
    {
        label.setBorder(layoutborderstyle.getBorder());
        label.setColor(layoutborderstyle.getColor());
        label.setCorner(layoutborderstyle.isCorner());
        WidgetTitle widgettitle = layoutborderstyle.getTitle();
        WidgetTitle _tmp = widgettitle;
        label.setWidgetName((new StringBuilder()).append("Title_").append(toData().getWidgetName()).toString());
        label.setWidgetValue(getTitleValue(widgettitle));
        label.setFont(widgettitle.getFrFont());
        label.setTextalign(widgettitle.getPosition());
        label.setBackground(widgettitle.getBackground());
    }

    private WidgetValue getTitleValue(WidgetTitle widgettitle)
    {
        String s = String.valueOf(widgettitle.getTextObject());
        Object obj = s.startsWith("=") ? ((Object) (new Formula(s))) : ((Object) (s));
        return new WidgetValue(obj);
    }

    protected String getIconName()
    {
        return "";
    }

    protected JComponent initEditor()
    {
        return this;
    }

    public Insets getInsets()
    {
        PaddingMargin paddingmargin = toData().getMargin();
        if(paddingmargin == null)
            return new Insets(0, 0, 0, 0);
        else
            return new Insets(paddingmargin.getTop(), paddingmargin.getLeft(), paddingmargin.getBottom(), paddingmargin.getRight());
    }

    public volatile Widget toData()
    {
        return toData();
    }
}
