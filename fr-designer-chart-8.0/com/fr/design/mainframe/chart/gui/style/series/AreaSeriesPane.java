// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style.series;

import com.fr.chart.base.AttrMarkerType;
import com.fr.chart.base.DataSeriesCondition;
import com.fr.chart.chartattr.AreaPlot;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.*;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.xcombox.MarkerComboBox;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.style.ChartBeautyPane;
import com.fr.general.Inter;
import java.awt.Component;
import java.util.Iterator;
import javax.swing.JPanel;
import javax.swing.JSeparator;

// Referenced classes of package com.fr.design.mainframe.chart.gui.style.series:
//            AbstractPlotSeriesPane

public class AreaSeriesPane extends AbstractPlotSeriesPane
{

    private UICheckBox isCurve;
    protected MarkerComboBox markerPane;
    private ChartBeautyPane stylePane;

    public AreaSeriesPane(ChartStylePane chartstylepane, Plot plot)
    {
        super(chartstylepane, plot, false);
    }

    public AreaSeriesPane(ChartStylePane chartstylepane, Plot plot, boolean flag)
    {
        super(chartstylepane, plot, true);
    }

    protected JPanel getContentInPlotType()
    {
        stylePane = new ChartBeautyPane();
        isCurve = new UICheckBox(Inter.getLocText("FR-Chart-Curve_Line"));
        markerPane = new MarkerComboBox(MarkerFactory.getMarkerArray());
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d, d1
        };
        double ad1[] = {
            d, d, d, d
        };
        Component acomponent[][] = {
            {
                stylePane, null
            }, {
                new JSeparator(), null
            }, {
                new UILabel(Inter.getLocText("FR-Chart-Line_Style")), isCurve
            }, {
                new BoldFontTextLabel(Inter.getLocText("FR-Chart-Marker_Type")), markerPane
            }
        };
        return TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
    }

    public void populateBean(Plot plot)
    {
        super.populateBean(plot);
        if(stylePane != null)
            stylePane.populateBean(Integer.valueOf(plot.getPlotStyle()));
        isCurve.setSelected(((AreaPlot)plot).isCurve());
        ConditionAttr conditionattr = plot.getConditionCollection().getDefaultAttr();
        populateAttrCondition(conditionattr.getConditionIterator());
    }

    protected void populateAttrCondition(Iterator iterator)
    {
        do
        {
            if(!iterator.hasNext())
                break;
            DataSeriesCondition dataseriescondition = (DataSeriesCondition)iterator.next();
            if(dataseriescondition instanceof AttrMarkerType)
                markerPane.setSelectedMarker(MarkerFactory.createMarker(((AttrMarkerType)dataseriescondition).getMarkerType()));
        } while(true);
    }

    public void updateBean(Plot plot)
    {
        super.updateBean(plot);
        if(stylePane != null)
            plot.setPlotStyle(stylePane.updateBean().intValue());
        ((AreaPlot)plot).setCurve(isCurve.isSelected());
        ConditionAttr conditionattr = plot.getConditionCollection().getDefaultAttr();
        updateAttrCondition(conditionattr);
    }

    protected void updateAttrCondition(ConditionAttr conditionattr)
    {
        DataSeriesCondition dataseriescondition = conditionattr.getExisted(com/fr/chart/base/AttrMarkerType);
        if(dataseriescondition != null)
            conditionattr.remove(dataseriescondition);
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
