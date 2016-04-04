// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style;

import com.fr.base.Utils;
import com.fr.chart.base.AttrContents;
import com.fr.chart.base.ChartConstants;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.general.FRFont;
import com.fr.general.Inter;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.mainframe.chart.gui.style:
//            ChartDatapointLabelPane, ChartTextAttrPane

public class ChartDataPointLabel4MapPane extends ChartDatapointLabelPane
{

    public ChartDataPointLabel4MapPane(ChartStylePane chartstylepane)
    {
        parent = chartstylepane;
        isLabelShow = new UICheckBox(Inter.getLocText("FR-Chart-Chart_Label"));
        isCategory = new UICheckBox(Inter.getLocText("FR-Chart-Area_Name"));
        isValue = new UICheckBox(Inter.getLocText("FR-Chart-Area_Value"));
        valueFormatButton = new UIButton(Inter.getLocText("Chart-Use_Format"));
        isValuePercent = new UICheckBox(Inter.getLocText("Chart-Value_Percent"));
        valuePercentFormatButton = new UIButton(Inter.getLocText("Chart-Use_Format"));
        divideComoBox = new UIComboBox(ChartConstants.DELIMITERS);
        textFontPane = new ChartTextAttrPane();
        initFormatListener();
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d, d1
        };
        double ad1[] = {
            d, d, d, d, d, d, d
        };
        Component acomponent[][] = new Component[9][3];
        JPanel jpanel = null;
        acomponent[1] = (new Component[] {
            isCategory, null
        });
        acomponent[3] = (new Component[] {
            isValue, valueFormatButton
        });
        acomponent[4] = (new Component[] {
            isValuePercent, valuePercentFormatButton
        });
        JPanel jpanel1 = new JPanel(new BorderLayout(6, 6));
        jpanel1.add(new BoldFontTextLabel(Inter.getLocText("FR-Chart-Delimiter_Symbol")), "West");
        jpanel1.add(divideComoBox, "Center");
        acomponent[5] = (new Component[] {
            jpanel1, null
        });
        acomponent[6] = (new Component[] {
            textFontPane, null
        });
        labelPane = TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
        double ad2[] = {
            d, d
        };
        double ad3[] = {
            46D, d1
        };
        jpanel = TableLayoutHelper.createTableLayoutPane(new Component[][] {
            new Component[] {
                isLabelShow, null
            }, new Component[] {
                null, labelPane
            }
        }, ad2, ad3);
        setLayout(new BorderLayout());
        add(jpanel, "Center");
        isLabelShow.addActionListener(new ActionListener() {

            final ChartDataPointLabel4MapPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                checkBoxUse();
            }

            
            {
                this$0 = ChartDataPointLabel4MapPane.this;
                super();
            }
        }
);
    }

    public void populate(AttrContents attrcontents)
    {
        isLabelShow.setSelected(true);
        String s = attrcontents.getSeriesLabel();
        if(s != null)
        {
            int i = 0;
            do
            {
                if(i >= ChartConstants.DELIMITERS.length)
                    break;
                String s1 = ChartConstants.DELIMITERS[i];
                if(divideComoBox != null && s.contains(s1))
                {
                    divideComoBox.setSelectedItem(s1);
                    break;
                }
                i++;
            } while(true);
            if(divideComoBox != null && s.contains("${BR}"))
                divideComoBox.setSelectedItem(ChartConstants.DELIMITERS[3]);
            if(isCategory != null)
                isCategory.setSelected(s.contains("${CATEGORY}"));
            if(isValue != null)
                isValue.setSelected(s.contains("${VALUE}"));
            if(isValuePercent != null)
                isValuePercent.setSelected(s.contains("${PERCENT}"));
        } else
        {
            if(isCategory != null)
                isCategory.setSelected(false);
            if(isValue != null)
                isValue.setSelected(false);
            if(isValuePercent != null)
                isValuePercent.setSelected(false);
        }
        valueFormat = attrcontents.getFormat();
        percentFormat = attrcontents.getPercentFormat();
        if(textFontPane != null)
            if(attrcontents != null)
                textFontPane.populate(attrcontents.getTextAttr());
            else
                textFontPane.populate(FRFont.getInstance());
    }

    public AttrContents update()
    {
        if(!isLabelShow.isSelected())
            return null;
        AttrContents attrcontents = new AttrContents();
        String s = "";
        String s1 = Utils.objectToString(divideComoBox.getSelectedItem());
        if(s1.contains(ChartConstants.DELIMITERS[3]))
            s1 = "${BR}";
        else
        if(s1.contains(ChartConstants.DELIMITERS[4]))
            s1 = " ";
        if(isCategory != null && isCategory.isSelected())
            s = (new StringBuilder()).append(s).append("${CATEGORY}").append(s1).toString();
        if(isValue != null && isValue.isSelected())
            s = (new StringBuilder()).append(s).append("${VALUE}").append(s1).toString();
        if(isValuePercent != null && isValuePercent.isSelected())
            s = (new StringBuilder()).append(s).append("${PERCENT}").append(s1).toString();
        if(s.contains(s1))
            s = s.substring(0, s.lastIndexOf(s1));
        attrcontents.setSeriesLabel(s);
        if(valueFormat != null)
            attrcontents.setFormat(valueFormat);
        if(percentFormat != null)
            attrcontents.setPercentFormat(percentFormat);
        if(textFontPane != null)
            attrcontents.setTextAttr(textFontPane.update());
        updatePercentFormatpane();
        return attrcontents;
    }
}
