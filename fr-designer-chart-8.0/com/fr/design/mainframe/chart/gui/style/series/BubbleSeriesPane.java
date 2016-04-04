// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style.series;

import com.fr.base.Utils;
import com.fr.chart.chartattr.BubblePlot;
import com.fr.chart.chartattr.Plot;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.general.Inter;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JPanel;
import javax.swing.JSeparator;

// Referenced classes of package com.fr.design.mainframe.chart.gui.style.series:
//            AbstractPlotSeriesPane

public class BubbleSeriesPane extends AbstractPlotSeriesPane
{

    protected UIButtonGroup bubbleMean;
    protected UICheckBox isMinus;
    protected UITextField zoomTime;

    public BubbleSeriesPane(ChartStylePane chartstylepane, Plot plot)
    {
        super(chartstylepane, plot, false);
    }

    public void populateBean(Plot plot)
    {
        super.populateBean(plot);
        BubblePlot bubbleplot = (BubblePlot)plot;
        bubbleMean.setSelectedItem(Integer.valueOf(bubbleplot.getSeriesEqualsBubbleInWidthOrArea()));
        zoomTime.setText((new StringBuilder()).append("").append(bubbleplot.getMaxBubblePixel()).toString());
        isMinus.setSelected(bubbleplot.isShowNegativeBubble());
    }

    public void updateBean(Plot plot)
    {
        super.updateBean(plot);
        BubblePlot bubbleplot = (BubblePlot)plot;
        bubbleplot.setSeriesEqualsBubbleInWidthOrArea(((Integer)bubbleMean.getSelectedItem()).intValue());
        bubbleplot.setMaxBubblePixel(Utils.string2Number(zoomTime.getText()).doubleValue());
        bubbleplot.setShowNegativeBubble(isMinus.isSelected());
    }

    protected void initCom()
    {
        String as[] = {
            Inter.getLocText(new String[] {
                "Chart_Bubble", "Chart_Area"
            }), Inter.getLocText("Bubble-Width")
        };
        Integer ainteger[] = {
            Integer.valueOf(2), Integer.valueOf(1)
        };
        bubbleMean = new UIButtonGroup(as, ainteger);
        zoomTime = new UITextField();
        isMinus = new UICheckBox(Inter.getLocText(new String[] {
            "Display", "Chart_Negative_Bubble"
        }));
    }

    protected JPanel getContentInPlotType()
    {
        initCom();
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d, d1
        };
        double ad1[] = {
            d, d, d, d, d, d
        };
        Component acomponent[][] = {
            {
                bubbleMean, null
            }, {
                new JSeparator(), null
            }, {
                new BoldFontTextLabel(Inter.getLocText("Chart_Change_Bubble_Size")), zoomTime
            }, {
                isMinus, null
            }
        };
        JPanel jpanel = TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
        setLayout(new BorderLayout());
        add(jpanel, "Center");
        return jpanel;
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
