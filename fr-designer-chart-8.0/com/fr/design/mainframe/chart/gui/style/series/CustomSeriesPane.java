// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style.series;

import com.fr.chart.chartattr.CustomPlot;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.chart.chartglyph.ConditionCollection;
import com.fr.chart.chartglyph.CustomAttr;
import com.fr.design.gui.frpane.UICorrelationComboBoxPane;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.imenutable.UIMenuNameableCreator;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.style.ChartBeautyPane;
import com.fr.general.Inter;
import java.awt.Component;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JSeparator;

// Referenced classes of package com.fr.design.mainframe.chart.gui.style.series:
//            AbstractPlotSeriesPane, CustomDefaultSeriesPane, CustomTypeConditionSeriesPane

public class CustomSeriesPane extends AbstractPlotSeriesPane
{

    private ChartBeautyPane stylePane;
    private CustomDefaultSeriesPane defaultSeriesStyle;
    private UICorrelationComboBoxPane conditionPane;

    public CustomSeriesPane(ChartStylePane chartstylepane, Plot plot)
    {
        super(chartstylepane, plot, false);
    }

    protected JPanel getContentInPlotType()
    {
        stylePane = new ChartBeautyPane();
        defaultSeriesStyle = new CustomDefaultSeriesPane();
        conditionPane = new UICorrelationComboBoxPane();
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d1
        };
        double ad1[] = {
            d, d, d, d, d, d, d
        };
        Component acomponent[][] = {
            {
                stylePane
            }, {
                new JSeparator()
            }, {
                new BoldFontTextLabel(Inter.getLocText("Series_Use_Default"))
            }, {
                defaultSeriesStyle
            }, {
                new JSeparator()
            }, {
                new BoldFontTextLabel(Inter.getLocText("FR-Chart-Add_Series_Setting"), 2)
            }, {
                conditionPane
            }
        };
        return TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
    }

    public void populateBean(Plot plot)
    {
        super.populateBean(plot);
        if(plot instanceof CustomPlot)
        {
            CustomPlot customplot = (CustomPlot)plot;
            if(stylePane != null)
                stylePane.populateBean(Integer.valueOf(customplot.getPlotStyle()));
            if(defaultSeriesStyle != null)
                defaultSeriesStyle.populateBean((CustomAttr)customplot.getCustomTypeCondition().getDefaultAttr());
            ArrayList arraylist = new ArrayList();
            arraylist.add(new UIMenuNameableCreator(Inter.getLocText("FR-Chart-Series_Setting"), new CustomAttr(), com/fr/design/mainframe/chart/gui/style/series/CustomTypeConditionSeriesPane));
            conditionPane.refreshMenuAndAddMenuAction(arraylist);
            ConditionCollection conditioncollection = customplot.getCustomTypeCondition();
            ArrayList arraylist1 = new ArrayList();
            for(int i = 0; i < conditioncollection.getConditionAttrSize(); i++)
                arraylist1.add(new UIMenuNameableCreator(conditioncollection.getConditionAttr(i).getName(), conditioncollection.getConditionAttr(i), com/fr/design/mainframe/chart/gui/style/series/CustomTypeConditionSeriesPane));

            conditionPane.populateBean(arraylist1);
            conditionPane.doLayout();
        }
    }

    public void updateBean(Plot plot)
    {
        super.updateBean(plot);
        if(plot instanceof CustomPlot)
        {
            CustomPlot customplot = (CustomPlot)plot;
            if(stylePane != null)
                customplot.setPlotStyle(stylePane.updateBean().intValue());
            if(defaultSeriesStyle != null)
                defaultSeriesStyle.updateBean((CustomAttr)customplot.getCustomTypeCondition().getDefaultAttr());
            java.util.List list = conditionPane.updateBean();
            ConditionCollection conditioncollection = customplot.getCustomTypeCondition();
            conditioncollection.clearConditionAttr();
            for(int i = 0; i < list.size(); i++)
            {
                UIMenuNameableCreator uimenunameablecreator = (UIMenuNameableCreator)list.get(i);
                ConditionAttr conditionattr = (ConditionAttr)uimenunameablecreator.getObj();
                conditionattr.setName(uimenunameablecreator.getName());
                conditioncollection.addConditionAttr(conditionattr);
            }

        }
    }

    public volatile void updateBean(Object obj)
    {
        updateBean((Plot)obj);
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((Plot)obj);
    }
}
