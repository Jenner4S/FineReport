// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style.series;

import com.fr.chart.base.*;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartattr.RadarPlot;
import com.fr.chart.chartglyph.*;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.LineComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.xcombox.MarkerComboBox;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.general.Inter;
import com.fr.stable.CoreConstants;
import java.awt.Component;
import java.util.Iterator;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.mainframe.chart.gui.style.series:
//            AbstractPlotSeriesPane

public class RadarSeriesPane extends AbstractPlotSeriesPane
{

    protected UIButtonGroup isNullValueBreak;
    protected UICheckBox isCurve;
    private LineComboBox lineStyle;
    private MarkerComboBox markerPane;

    public RadarSeriesPane(ChartStylePane chartstylepane, Plot plot)
    {
        super(chartstylepane, plot, false);
    }

    protected JPanel getContentInPlotType()
    {
        isCurve = new UICheckBox(Inter.getLocText("ChartF-Fill"));
        lineStyle = new LineComboBox(CoreConstants.STRIKE_LINE_STYLE_ARRAY_4_CHART);
        markerPane = new MarkerComboBox(MarkerFactory.getMarkerArray());
        String as[] = {
            Inter.getLocText("Chart_Null_Value_Break"), Inter.getLocText("Chart_Null_Value_Continue")
        };
        Boolean aboolean[] = {
            Boolean.valueOf(true), Boolean.valueOf(false)
        };
        isNullValueBreak = new UIButtonGroup(as, aboolean);
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d, d1
        };
        double ad1[] = {
            d, d, d, d, d
        };
        Component acomponent[][] = {
            {
                new UILabel(Inter.getLocText("Chart_Line_Style")), isCurve
            }, {
                new UILabel(Inter.getLocText(new String[] {
                    "Chart_Line", "Line-Style"
                })), lineStyle
            }, {
                new UILabel(Inter.getLocText(new String[] {
                    "ChartF-Marker", "FS_Report_Type"
                })), markerPane
            }, {
                new UILabel(Inter.getLocText("Null_Value_Show")), isNullValueBreak
            }
        };
        return TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
    }

    protected void populateAttrCondition(Iterator iterator)
    {
        do
        {
            if(!iterator.hasNext())
                break;
            DataSeriesCondition dataseriescondition = (DataSeriesCondition)iterator.next();
            if(dataseriescondition instanceof AttrLineStyle)
            {
                int i = ((AttrLineStyle)dataseriescondition).getLineStyle();
                if(i != 5 && i != 1 && i != 2 && i != 0)
                    i = 1;
                lineStyle.setSelectedLineStyle(i);
            } else
            if(dataseriescondition instanceof AttrMarkerType)
                markerPane.setSelectedMarker(MarkerFactory.createMarker(((AttrMarkerType)dataseriescondition).getMarkerType()));
        } while(true);
    }

    protected void updateAttrCondition(ConditionAttr conditionattr)
    {
        DataSeriesCondition dataseriescondition = conditionattr.getExisted(com/fr/chart/base/AttrLineStyle);
        if(dataseriescondition != null)
            conditionattr.remove(dataseriescondition);
        conditionattr.addDataSeriesCondition(new AttrLineStyle(lineStyle.getSelectedLineStyle()));
        dataseriescondition = conditionattr.getExisted(com/fr/chart/base/AttrMarkerType);
        if(dataseriescondition != null)
            conditionattr.remove(dataseriescondition);
        conditionattr.addDataSeriesCondition(new AttrMarkerType(markerPane.getSelectedMarkder().getMarkerType()));
        dataseriescondition = conditionattr.getExisted(com/fr/chart/base/AttrColor);
        if(dataseriescondition != null)
            conditionattr.remove(dataseriescondition);
    }

    public void populateBean(Plot plot)
    {
        super.populateBean(plot);
        isCurve.setSelected(((RadarPlot)plot).isFilled());
        ConditionAttr conditionattr = plot.getConditionCollection().getDefaultAttr();
        populateAttrCondition(conditionattr.getConditionIterator());
        isNullValueBreak.setSelectedItem(Boolean.valueOf(plot.isNullValueBreak()));
    }

    public void updateBean(Plot plot)
    {
        super.updateBean(plot);
        ((RadarPlot)plot).setIsFilled(isCurve.isSelected());
        plot.setNullValueBreak(((Boolean)isNullValueBreak.getSelectedItem()).booleanValue());
        ConditionAttr conditionattr = plot.getConditionCollection().getDefaultAttr();
        updateAttrCondition(conditionattr);
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
