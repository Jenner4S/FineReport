// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style;

import com.fr.base.Utils;
import com.fr.chart.base.*;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.general.FRFont;
import com.fr.general.Inter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.mainframe.chart.gui.style:
//            ChartDatapointLabelPane, ChartTextAttrPane

public class ChartDataPointLabel4GisPane extends ChartDatapointLabelPane
{

    private UICheckBox isAddress;
    private UICheckBox isAddressName;
    private UICheckBox isAddressTittle;
    private UICheckBox isDatapointValue;

    public ChartDataPointLabel4GisPane(ChartStylePane chartstylepane)
    {
        parent = chartstylepane;
        isLabelShow = new UICheckBox(Inter.getLocText("FR-Chart-Chart_Label"));
        isAddressTittle = new UICheckBox(Inter.getLocText("Chart-Area_Title"));
        isAddress = new UICheckBox(Inter.getLocText("Chart-Gis_Address"));
        isAddress.setSelected(true);
        isAddressName = new UICheckBox(Inter.getLocText("Chart-Address_Name"));
        isDatapointValue = new UICheckBox(Inter.getLocText("Chart-Use_Value"));
        valueFormatButton = new UIButton(Inter.getLocText("Chart-Use_Format"));
        divideComoBox = new UIComboBox(ChartConstants.DELIMITERS);
        textFontPane = new ChartTextAttrPane();
        initFormatListener();
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d, d1
        };
        double ad1[] = {
            d, d, d, d, d, d
        };
        JPanel jpanel = new JPanel(new BorderLayout(6, 6));
        jpanel.add(new BoldFontTextLabel(Inter.getLocText("FR-Chart-Delimiter_Symbol")), "West");
        jpanel.add(divideComoBox, "Center");
        Component acomponent[][] = {
            {
                isAddress, null
            }, {
                isAddressName, null
            }, {
                isAddressTittle, null
            }, {
                isDatapointValue, valueFormatButton
            }, {
                jpanel, null
            }, {
                textFontPane, null
            }
        };
        labelPane = TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
        double ad2[] = {
            d, d
        };
        double ad3[] = {
            46D, d1
        };
        JPanel jpanel1 = TableLayoutHelper.createTableLayoutPane(new Component[][] {
            new Component[] {
                isLabelShow, null
            }, new Component[] {
                null, labelPane
            }
        }, ad2, ad3);
        setLayout(new BorderLayout());
        add(jpanel1, "Center");
        isLabelShow.addActionListener(new ActionListener() {

            final ChartDataPointLabel4GisPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                checkBoxUse();
            }

            
            {
                this$0 = ChartDataPointLabel4GisPane.this;
                super();
            }
        }
);
    }

    public void populate(DataSeriesCondition dataseriescondition)
    {
        if(dataseriescondition == null)
            isLabelShow.setSelected(false);
        else
        if(dataseriescondition instanceof AttrContents)
        {
            AttrContents attrcontents = (AttrContents)dataseriescondition;
            populate(attrcontents);
        }
        if(textFontPane != null)
            if(dataseriescondition != null)
            {
                textFontPane.populate(((AttrContents)dataseriescondition).getTextAttr());
            } else
            {
                FRFont frfont = FRFont.getInstance();
                frfont.setForeground(Color.white);
                textFontPane.populate(frfont);
            }
        checkBoxUse();
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
            isAddressTittle.setSelected(s.contains("${AREA_TITTLE}"));
            isAddress.setSelected(s.contains("${ADDRESS}"));
            isAddressName.setSelected(s.contains("${ADDRESS_NAME}"));
            isDatapointValue.setSelected(s.contains("${VALUE}"));
        } else
        {
            isAddressTittle.setSelected(false);
            isAddress.setSelected(false);
            isAddressName.setSelected(false);
            isDatapointValue.setSelected(false);
        }
        valueFormat = attrcontents.getFormat();
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
        if(isDatapointValue.isSelected())
            s = (new StringBuilder()).append(s).append("${VALUE}").append(s1).toString();
        if(isAddressTittle.isSelected())
            s = (new StringBuilder()).append(s).append("${AREA_TITTLE}").append(s1).toString();
        if(isAddress.isSelected())
            s = (new StringBuilder()).append(s).append("${ADDRESS}").append(s1).toString();
        if(isAddressName.isSelected())
            s = (new StringBuilder()).append(s).append("${ADDRESS_NAME}").append(s1).toString();
        attrcontents.setSeriesLabel(s);
        if(valueFormat != null)
            attrcontents.setFormat(valueFormat);
        if(textFontPane != null)
            attrcontents.setTextAttr(textFontPane.update());
        return attrcontents;
    }
}
