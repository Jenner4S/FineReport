// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.gui;

import com.fr.base.FRContext;
import com.fr.chart.chartattr.Chart;
import com.fr.design.gui.core.WidgetOption;
import com.fr.form.ui.ChartEditor;
import com.fr.form.ui.Widget;
import com.fr.general.FRLogger;
import javax.swing.Icon;

public class ChartWidgetOption extends WidgetOption
{

    private static final long serialVersionUID = 0xa4bc1a54a3613fbeL;
    private String optionName;
    private Icon optionIcon;
    private Class widgetClass;
    private Chart chart;

    public ChartWidgetOption(String s, Icon icon, Class class1, Chart chart1)
    {
        optionName = s;
        optionIcon = icon;
        widgetClass = class1;
        chart = chart1;
    }

    public Widget createWidget()
    {
        Class class1 = widgetClass();
        try
        {
            ChartEditor charteditor = (ChartEditor)class1.newInstance();
            charteditor.addChart((Chart)chart.clone());
            return charteditor;
        }
        catch(InstantiationException instantiationexception)
        {
            FRContext.getLogger().error(instantiationexception.getMessage(), instantiationexception);
        }
        catch(IllegalAccessException illegalaccessexception)
        {
            FRContext.getLogger().error(illegalaccessexception.getMessage(), illegalaccessexception);
        }
        catch(CloneNotSupportedException clonenotsupportedexception)
        {
            FRContext.getLogger().error(clonenotsupportedexception.getMessage(), clonenotsupportedexception);
        }
        return null;
    }

    public String optionName()
    {
        return optionName;
    }

    public Icon optionIcon()
    {
        return optionIcon;
    }

    public Class widgetClass()
    {
        return widgetClass;
    }
}
