// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style.datalabel;

import com.fr.chart.base.TextAttr;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.design.dialog.BasicScrollPane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.style.ChartTextAttrNoColorPane;
import com.fr.general.Inter;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.JSeparator;

public class ChartLabelFontPane extends BasicScrollPane
{
    private class ContentPane extends JPanel
    {

        private UILabel value;
        private UILabel unit;
        final ChartLabelFontPane this$0;

        private void initComponents()
        {
            setLayout(new BorderLayout());
            add(createValuePane(), "Center");
        }

        private JPanel createValuePane()
        {
            valueTextAttrPane = new ChartTextAttrNoColorPane();
            unitTextAttrPane = new ChartTextAttrNoColorPane();
            cateTextAttrPane = new ChartTextAttrNoColorPane();
            categoryName = new UICheckBox(Inter.getLocText(new String[] {
                "StyleFormat-Category", "WF-Name"
            }));
            categoryName.setSelected(true);
            categoryName.addActionListener(new ActionListener() {

                final ContentPane this$1;

                public void actionPerformed(ActionEvent actionevent)
                {
                    cateTextAttrPane.setEnabled(categoryName.isSelected());
                }

                
                {
                    this$1 = ContentPane.this;
                    super();
                }
            }
);
            double d = -2D;
            double d1 = -1D;
            double ad[] = {
                46D, d1
            };
            double ad1[] = {
                d, d, d, d, d, d, d, d, d
            };
            Component acomponent[][] = {
                {
                    value, null
                }, {
                    null, valueTextAttrPane
                }, {
                    new JSeparator(), null
                }, {
                    unit, null
                }, {
                    null, unitTextAttrPane
                }, {
                    new JSeparator(), null
                }, {
                    categoryName, null
                }, {
                    null, cateTextAttrPane
                }, {
                    new JSeparator(), null
                }
            };
            return TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
        }

        public ContentPane()
        {
            this$0 = ChartLabelFontPane.this;
            super();
            value = new UILabel(Inter.getLocText("Value"));
            unit = new UILabel(Inter.getLocText("ChartF-Units"));
            initComponents();
        }
    }


    private ChartTextAttrNoColorPane valueTextAttrPane;
    private ChartTextAttrNoColorPane unitTextAttrPane;
    private ChartTextAttrNoColorPane cateTextAttrPane;
    private UICheckBox categoryName;

    public ChartLabelFontPane()
    {
    }

    protected JPanel createContentPane()
    {
        return new ContentPane();
    }

    public void populateBean(Chart chart)
    {
        Plot plot = chart.getPlot();
        TextAttr textattr = plot.getValueTextAttr();
        TextAttr textattr1 = plot.getUnitTextAttr();
        TextAttr textattr2 = plot.getCategoryNameTextAttr();
        if(textattr1 == null)
            textattr1 = new TextAttr();
        unitTextAttrPane.populate(textattr1);
        if(textattr == null)
            textattr = new TextAttr();
        valueTextAttrPane.populate(textattr);
        if(textattr2 == null)
            textattr2 = new TextAttr();
        cateTextAttrPane.populate(textattr2);
        categoryName.setSelected(plot.isShowCateName());
    }

    public void updateBean(Chart chart)
    {
        if(chart == null)
            chart = new Chart();
        Plot plot = chart.getPlot();
        TextAttr textattr = plot.getValueTextAttr();
        TextAttr textattr1 = plot.getUnitTextAttr();
        TextAttr textattr2 = plot.getCategoryNameTextAttr();
        if(textattr1 == null)
            textattr1 = new TextAttr();
        unitTextAttrPane.update(textattr1);
        if(textattr == null)
            textattr = new TextAttr();
        valueTextAttrPane.update(textattr);
        if(textattr2 == null)
            textattr2 = new TextAttr();
        cateTextAttrPane.update(textattr2);
        plot.setShowCateName(categoryName.isSelected());
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("Label");
    }

    public volatile void updateBean(Object obj)
    {
        updateBean((Chart)obj);
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((Chart)obj);
    }








}
