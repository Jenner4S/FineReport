// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style;

import com.fr.chart.base.AttrFillStyle;
import com.fr.chart.base.ChartConstants;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.style.background.gradient.GradientBar;
import com.fr.design.style.background.gradient.SelectColorPointBtn;
import com.fr.general.Inter;
import java.awt.*;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.mainframe.chart.gui.style:
//            ChartFillStylePane, ChartAccColorPane

public class ChartPreFillStylePane extends ChartFillStylePane
{

    public ChartPreFillStylePane()
    {
    }

    protected void initLayout()
    {
        customPane.setPreferredSize(new Dimension(200, 200));
        colorGradient.setPreferredSize(new Dimension(120, 30));
        double d = -2D;
        double ad[] = {
            d, d
        };
        double ad1[] = {
            d, d, d
        };
        Component acomponent[][] = {
            {
                new UILabel((new StringBuilder()).append(" ").append(Inter.getLocText("ColorMatch")).toString()), null
            }, {
                null, customPane
            }
        };
        add(TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad), "West");
    }

    public void populateBean(AttrFillStyle attrfillstyle)
    {
        styleSelectBox.setSelectedIndex(styleSelectBox.getItemCount() - 1);
        if(attrfillstyle == null || attrfillstyle.getColorStyle() == 0)
        {
            colorAcc.populateBean(ChartConstants.CHART_COLOR_ARRAY);
            accButton.setSelected(true);
            gradientButton.setSelected(false);
            cardLayout.show(changeColorSetPane, "acc");
            colorGradient.getSelectColorPointBtnP1().setColorInner(Color.white);
            colorGradient.getSelectColorPointBtnP2().setColorInner(Color.black);
        } else
        {
            int i = attrfillstyle.getColorStyle();
            gradientButton.setSelected(i == 2);
            accButton.setSelected(i == 1);
            int j = attrfillstyle.getColorSize();
            if(j == 2 && gradientButton.isSelected())
            {
                cardLayout.show(changeColorSetPane, "gradient");
                Color color = attrfillstyle.getColorIndex(1);
                Color color1 = attrfillstyle.getColorIndex(0);
                colorGradient.getSelectColorPointBtnP1().setColorInner(color1);
                colorGradient.getSelectColorPointBtnP2().setColorInner(color);
                colorGradient.repaint();
            } else
            if(j > 2 && accButton.isSelected())
            {
                cardLayout.show(changeColorSetPane, "acc");
                Color acolor[] = new Color[j];
                for(int k = 0; k < j; k++)
                    acolor[k] = attrfillstyle.getColorIndex(k);

                colorAcc.populateBean(acolor);
            }
        }
    }

    public AttrFillStyle updateBean()
    {
        AttrFillStyle attrfillstyle = new AttrFillStyle();
        attrfillstyle.clearColors();
        if(gradientButton.isSelected())
        {
            attrfillstyle.setColorStyle(2);
            Color color = colorGradient.getSelectColorPointBtnP1().getColorInner();
            Color color1 = colorGradient.getSelectColorPointBtnP2().getColorInner();
            attrfillstyle.addFillColor(color);
            attrfillstyle.addFillColor(color1);
        } else
        {
            attrfillstyle.setColorStyle(1);
            Color acolor[] = colorAcc.updateBean();
            int i = 0;
            for(int j = acolor.length; i < j; i++)
                attrfillstyle.addFillColor(acolor[i]);

        }
        return attrfillstyle;
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((AttrFillStyle)obj);
    }
}
