// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.SeriesCondition;

import com.fr.chart.base.AttrContents;
import com.fr.design.dialog.*;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.style.FormatPane;
import com.fr.general.Inter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.Format;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class TooltipContentsPane extends BasicPane
{

    protected UICheckBox showValueCB;
    protected UICheckBox showPercent;
    protected Format format;
    protected Format percentFormat;
    protected JPanel contentPane;
    private ActionListener listener;
    private ActionListener percentListener;

    public TooltipContentsPane()
    {
        listener = new ActionListener() {

            final TooltipContentsPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                FormatPane formatpane = new FormatPane();
                if(format != null)
                    formatpane.populate(format);
                paneActionPerformed(formatpane, false);
            }

            
            {
                this$0 = TooltipContentsPane.this;
                super();
            }
        }
;
        percentListener = new ActionListener() {

            final TooltipContentsPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                FormatPane formatpane = new FormatPane();
                formatpane.percentFormatPane();
                if(percentFormat != null)
                    formatpane.populate(percentFormat);
                paneActionPerformed(formatpane, true);
            }

            
            {
                this$0 = TooltipContentsPane.this;
                super();
            }
        }
;
        setLayout(FRGUIPaneFactory.createBorderLayout());
        contentPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_S_Pane();
        add(contentPane, "Center");
        contentPane.add(createJPanel4Label());
    }

    private JPanel createJPanel4Label()
    {
        return createTableLayoutPane(new Component[][] {
            createComponents4Value(), createComponents4PercentValue(), new Component[0]
        });
    }

    protected Component[] createComponents4Value()
    {
        return (new Component[] {
            null, createJPanel4Value()
        });
    }

    protected JPanel createJPanel4Value()
    {
        if(showValueCB == null)
            showValueCB = new UICheckBox(Inter.getLocText("Value"));
        showValueCB.setSelected(true);
        JPanel jpanel = new JPanel();
        jpanel.setLayout(new FlowLayout(0, 0, 0));
        jpanel.add(showValueCB);
        UIButton uibutton = new UIButton(Inter.getLocText("Format"));
        jpanel.add(uibutton);
        uibutton.addActionListener(listener);
        uibutton.setPreferredSize(new Dimension(60, 20));
        return jpanel;
    }

    protected Component[] createComponents4PercentValue()
    {
        if(showPercent == null)
            showPercent = new UICheckBox(Inter.getLocText("Chart_ValueIntPercent"));
        JPanel jpanel = new JPanel();
        jpanel.setLayout(new FlowLayout(0, 0, 0));
        jpanel.add(showPercent);
        UIButton uibutton = new UIButton(Inter.getLocText("Format"));
        jpanel.add(uibutton);
        uibutton.addActionListener(percentListener);
        uibutton.setPreferredSize(new Dimension(60, 20));
        return (new Component[] {
            null, jpanel
        });
    }

    protected JPanel createTableLayoutPane(Component acomponent[][])
    {
        double ad[] = {
            -2D, -2D, -2D
        };
        double ad1[] = {
            -2D, -2D, -2D, -2D, -2D
        };
        return TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
    }

    public String title4PopupWindow()
    {
        return "DataLabel";
    }

    private void paneActionPerformed(final FormatPane formatPane, final boolean isPercent)
    {
        formatPane.showWindow(SwingUtilities.getWindowAncestor(this), new DialogActionAdapter() {

            final boolean val$isPercent;
            final FormatPane val$formatPane;
            final TooltipContentsPane this$0;

            public void doOk()
            {
                if(isPercent)
                    percentFormat = formatPane.update();
                else
                    format = formatPane.update();
            }

            
            {
                this$0 = TooltipContentsPane.this;
                isPercent = flag;
                formatPane = formatpane;
                super();
            }
        }
).setVisible(true);
    }

    public void populate(AttrContents attrcontents)
    {
        format = attrcontents.getFormat();
        percentFormat = attrcontents.getPercentFormat();
        String s = attrcontents.getSeriesLabel();
        if(s != null)
        {
            if(showValueCB != null)
                showValueCB.setSelected(s.contains("${VALUE}"));
            if(showPercent != null)
                showPercent.setSelected(s.contains("${PERCENT}"));
        } else
        {
            if(showValueCB != null)
                showValueCB.setSelected(false);
            if(showPercent != null)
                showPercent.setSelected(false);
        }
    }

    public void update(AttrContents attrcontents)
    {
        String s = "";
        s = (new StringBuilder()).append(s).append("${SERIES}${BR}${CATEGORY}").toString();
        if(showValueCB.isSelected() && !showPercent.isSelected())
            s = (new StringBuilder()).append(s).append("${BR}${VALUE}").toString();
        else
        if(!showValueCB.isSelected() && showPercent.isSelected())
            s = (new StringBuilder()).append(s).append("${BR}${PERCENT}").toString();
        else
        if(showValueCB.isSelected() && showPercent.isSelected())
            s = (new StringBuilder()).append(s).append("${BR}${VALUE}${BR}${PERCENT}").toString();
        else
            s = null;
        attrcontents.setSeriesLabel(s);
        attrcontents.setFormat(format);
        attrcontents.setPercentFormat(percentFormat);
    }

}
