// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style.series;

import com.fr.chart.base.*;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartattr.XYScatterPlot;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.chart.chartglyph.Marker;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.LineComboBox;
import com.fr.design.gui.xcombox.MarkerComboBox;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.general.ComparatorUtils;

// Referenced classes of package com.fr.design.mainframe.chart.gui.style.series:
//            LineSeriesPane

public class XYScatterSeriesPane extends LineSeriesPane
{

    public XYScatterSeriesPane(ChartStylePane chartstylepane, Plot plot)
    {
        super(chartstylepane, plot);
    }

    public void populateBean(Plot plot)
    {
        super.populateBean(plot);
        isCurve.setSelected(((XYScatterPlot)plot).isCurve());
    }

    public void updateBean(Plot plot)
    {
        super.updateBean(plot);
        ((XYScatterPlot)plot).setCurve(isCurve.isSelected());
    }

    protected void updateAttrCondition(ConditionAttr conditionattr)
    {
        DataSeriesCondition dataseriescondition = conditionattr.getExisted(com/fr/chart/base/AttrLineStyle);
        if(dataseriescondition != null)
            conditionattr.remove(dataseriescondition);
        conditionattr.addDataSeriesCondition(new AttrLineStyle(lineStyle.getSelectedLineStyle()));
        dataseriescondition = conditionattr.getExisted(com/fr/chart/base/AttrColor);
        if(dataseriescondition != null)
            conditionattr.remove(dataseriescondition);
        dataseriescondition = conditionattr.getExisted(com/fr/chart/base/AttrMarkerType);
        if(dataseriescondition != null)
            conditionattr.remove(dataseriescondition);
        if(!ComparatorUtils.equals(markerPane.getSelectedMarkder().getMarkerType(), "NullMarker"))
            conditionattr.addDataSeriesCondition(new AttrMarkerType(markerPane.getSelectedMarkder().getMarkerType()));
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
