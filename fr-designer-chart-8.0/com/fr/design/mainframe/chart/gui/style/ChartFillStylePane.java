// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style;

import com.fr.base.*;
import com.fr.chart.base.*;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.style.background.gradient.GradientBar;
import com.fr.design.style.background.gradient.SelectColorPointBtn;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.mainframe.chart.gui.style:
//            ChartAccColorPane

public class ChartFillStylePane extends BasicBeanPane
{

    protected UIComboBox styleSelectBox;
    protected JPanel customPane;
    protected JPanel changeColorSetPane;
    protected GradientBar colorGradient;
    protected CardLayout cardLayout;
    protected UIButton accButton;
    protected UIButton gradientButton;
    protected ChartAccColorPane colorAcc;

    public ChartFillStylePane()
    {
        setLayout(new BorderLayout());
        styleSelectBox = new UIComboBox(getNameObj());
        customPane = new JPanel(FRGUIPaneFactory.createBorderLayout());
        JPanel jpanel = new JPanel();
        jpanel.setLayout(new FlowLayout(0));
        jpanel.add(accButton = new UIButton(Inter.getLocText("FR-Designer_Chart_Acc_Set")));
        jpanel.add(gradientButton = new UIButton(Inter.getLocText("FR-Designer_Gradient-Color")));
        customPane.add(jpanel, "North");
        changeColorSetPane = new JPanel(cardLayout = new CardLayout());
        changeColorSetPane.add(colorGradient = new GradientBar(4, 130), "gradient");
        changeColorSetPane.add(colorAcc = new ChartAccColorPane(), "acc");
        cardLayout.show(changeColorSetPane, "acc");
        customPane.add(changeColorSetPane, "Center");
        customPane.setVisible(false);
        accButton.setSelected(true);
        initListener();
        initLayout();
    }

    private void initListener()
    {
        styleSelectBox.addActionListener(new ActionListener() {

            final ChartFillStylePane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                customPane.setVisible(styleSelectBox.getSelectedIndex() == styleSelectBox.getItemCount() - 1);
            }

            
            {
                this$0 = ChartFillStylePane.this;
                super();
            }
        }
);
        accButton.addActionListener(new ActionListener() {

            final ChartFillStylePane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                accButton.setSelected(true);
                gradientButton.setSelected(false);
                cardLayout.show(changeColorSetPane, "acc");
            }

            
            {
                this$0 = ChartFillStylePane.this;
                super();
            }
        }
);
        gradientButton.addActionListener(new ActionListener() {

            final ChartFillStylePane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                gradientButton.setSelected(true);
                accButton.setSelected(false);
                cardLayout.show(changeColorSetPane, "gradient");
            }

            
            {
                this$0 = ChartFillStylePane.this;
                super();
            }
        }
);
    }

    protected void initLayout()
    {
        customPane.setPreferredSize(new Dimension(200, 130));
        colorGradient.setPreferredSize(new Dimension(120, 30));
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d1
        };
        double ad1[] = {
            d, d
        };
        Component acomponent[][] = {
            {
                styleSelectBox
            }, {
                customPane
            }
        };
        JPanel jpanel = TableLayoutHelper.createTableLayoutPane4Chart(new String[] {
            "ColorMatch"
        }, acomponent, ad1, ad);
        setLayout(new BorderLayout());
        add(jpanel, "Center");
    }

    public Dimension getPreferredSize()
    {
        if(styleSelectBox.getSelectedIndex() != styleSelectBox.getItemCount() - 1)
            return new Dimension(styleSelectBox.getPreferredSize().width, 50);
        else
            return super.getPreferredSize();
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText(new String[] {
            "Chart", "Color"
        });
    }

    private String[] getNameObj()
    {
        ChartPreStyleManagerProvider chartprestylemanagerprovider = ChartPreStyleServerManager.getProviderInstance();
        ArrayList arraylist = new ArrayList();
        arraylist.add(Inter.getLocText("FR-Designer_DEFAULT"));
        Object obj;
        for(Iterator iterator = chartprestylemanagerprovider.names(); iterator.hasNext(); arraylist.add(Utils.objectToString(obj)))
            obj = iterator.next();

        arraylist.add(Inter.getLocText("FR-Designer_Custom"));
        return (String[])arraylist.toArray(new String[arraylist.size()]);
    }

    public void populateBean(AttrFillStyle attrfillstyle)
    {
        String s = attrfillstyle != null ? attrfillstyle.getFillStyleName() : "";
        if(StringUtils.isBlank(s))
        {
            if(attrfillstyle == null || attrfillstyle.getColorStyle() == 0)
            {
                styleSelectBox.setSelectedIndex(0);
                colorAcc.populateBean(ChartConstants.CHART_COLOR_ARRAY);
                accButton.setSelected(true);
                gradientButton.setSelected(false);
                cardLayout.show(changeColorSetPane, "acc");
                colorGradient.getSelectColorPointBtnP1().setColorInner(Color.white);
                colorGradient.getSelectColorPointBtnP2().setColorInner(Color.black);
            } else
            {
                styleSelectBox.setSelectedIndex(styleSelectBox.getItemCount() - 1);
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
        } else
        {
            styleSelectBox.setSelectedItem(s);
        }
    }

    public AttrFillStyle updateBean()
    {
        AttrFillStyle attrfillstyle = new AttrFillStyle();
        attrfillstyle.clearColors();
        if(styleSelectBox.getSelectedIndex() == 0)
            attrfillstyle.setColorStyle(0);
        else
        if(styleSelectBox.getSelectedIndex() < styleSelectBox.getItemCount() - 1)
        {
            ChartPreStyleManagerProvider chartprestylemanagerprovider = ChartPreStyleServerManager.getProviderInstance();
            Object obj = chartprestylemanagerprovider.getPreStyle(styleSelectBox.getSelectedItem());
            if(obj instanceof ChartPreStyle)
            {
                AttrFillStyle attrfillstyle1 = ((ChartPreStyle)obj).getAttrFillStyle();
                attrfillstyle1.setFillStyleName(Utils.objectToString(styleSelectBox.getSelectedItem()));
                return attrfillstyle1;
            }
            attrfillstyle.setColorStyle(0);
        } else
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
