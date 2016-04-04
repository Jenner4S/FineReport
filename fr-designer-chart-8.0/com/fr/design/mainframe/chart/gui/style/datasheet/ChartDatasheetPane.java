// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style.datasheet;

import com.fr.base.FRContext;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.DataSheet;
import com.fr.design.dialog.BasicScrollPane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.style.FormatPane;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.mainframe.chart.gui.style.ChartTextAttrPane;
import com.fr.design.mainframe.chart.gui.style.axis.ChartAxisPane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.*;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.JSeparator;

public class ChartDatasheetPane extends BasicScrollPane
{
    private class ContentPane extends JPanel
    {

        private static final long serialVersionUID = 0x24347d4f59eacfaL;
        final ChartDatasheetPane this$0;

        private void initComponents()
        {
            double d = -2D;
            double d1 = -1D;
            double ad[] = {
                46D, d1
            };
            double ad1[] = {
                d, d, d, d
            };
            isDatasheetVisable = new UICheckBox(Inter.getLocText("Chart_Show_Data_Sheet"));
            textAttrPane = new ChartTextAttrPane();
            formatPane = new FormatPane();
            formatPane.setForDataSheet();
            Component acomponent[][] = {
                {
                    null, textAttrPane
                }, {
                    new JSeparator(), null
                }, {
                    new UILabel(Inter.getLocText("Data_Type")), null
                }, {
                    null, formatPane
                }
            };
            datasheetPane = TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
            double ad2[] = {
                d, d
            };
            double ad3[] = {
                d1
            };
            JPanel jpanel = TableLayoutHelper.createTableLayoutPane(new Component[][] {
                new Component[] {
                    isDatasheetVisable
                }, new Component[] {
                    datasheetPane
                }
            }, ad2, ad3);
            setLayout(new BorderLayout());
            add(jpanel, "Center");
            isDatasheetVisable.addActionListener(new ActionListener() {

                final ContentPane this$1;

                public void actionPerformed(ActionEvent actionevent)
                {
                    checkAxisPaneUse();
                }

                
                {
                    this$1 = ContentPane.this;
                    super();
                }
            }
);
            isDatasheetVisable.addActionListener(new ActionListener() {

                final ContentPane this$1;

                public void actionPerformed(ActionEvent actionevent)
                {
                    checkBoxUse();
                }

                
                {
                    this$1 = ContentPane.this;
                    super();
                }
            }
);
        }

        public ContentPane()
        {
            this$0 = ChartDatasheetPane.this;
            super();
            initComponents();
        }
    }


    private static final long serialVersionUID = 0xbca2e0f31329f05aL;
    private UICheckBox isDatasheetVisable;
    private ChartTextAttrPane textAttrPane;
    private FormatPane formatPane;
    private JPanel datasheetPane;
    private ChartAxisPane axisPane;

    public ChartDatasheetPane()
    {
    }

    public void useWithAxis(ChartAxisPane chartaxispane)
    {
        axisPane = chartaxispane;
    }

    public void checkAxisPaneUse()
    {
        if(axisPane != null)
            axisPane.checkUseWithDataSheet(!isDatasheetVisable.isSelected());
    }

    private void checkBoxUse()
    {
        isDatasheetVisable.setEnabled(true);
        datasheetPane.setVisible(isDatasheetVisable.isSelected());
    }

    public String title4PopupWindow()
    {
        return PaneTitleConstants.CHART_STYLE_DATA_TITLE;
    }

    protected JPanel createContentPane()
    {
        return new ContentPane();
    }

    public void updateBean(Chart chart)
    {
        if(chart == null)
            return;
        Plot plot = chart.getPlot();
        if(plot == null)
            return;
        DataSheet datasheet = plot.getDataSheet();
        if(datasheet == null)
            datasheet = new DataSheet();
        datasheet.setVisible(isDatasheetVisable.isSelected());
        datasheet.setFont(textAttrPane.updateFRFont());
        datasheet.setFormat(formatPane.update());
    }

    public void populateBean(Chart chart)
    {
        if(chart == null || chart.getPlot() == null)
            return;
        DataSheet datasheet = chart.getPlot().getDataSheet();
        if(datasheet != null)
        {
            isDatasheetVisable.setSelected(datasheet.isVisible());
            FRFont frfont = FRContext.getDefaultValues().getFRFont() != null ? FRContext.getDefaultValues().getFRFont() : FRFont.getInstance();
            textAttrPane.populate(datasheet.getFont() != null ? datasheet.getFont() : frfont);
            formatPane.populateBean(datasheet.getFormat());
        }
        checkAxisPaneUse();
        checkBoxUse();
        if(chart.isUseMoreDate())
            GUICoreUtils.setEnabled(this, false);
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
