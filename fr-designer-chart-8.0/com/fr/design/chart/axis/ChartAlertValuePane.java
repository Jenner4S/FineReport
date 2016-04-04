// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.axis;

import com.fr.base.Formula;
import com.fr.base.Utils;
import com.fr.chart.base.AttrColor;
import com.fr.chart.base.AttrLineStyle;
import com.fr.chart.chartattr.ChartAlertValue;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.formula.FormulaFactory;
import com.fr.design.formula.UIFormula;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.icombobox.LineComboBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.style.AlphaPane;
import com.fr.design.style.FRFontPane;
import com.fr.design.style.color.ColorSelectBox;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.FRFont;
import com.fr.general.Inter;
import com.fr.stable.CoreConstants;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.*;

public class ChartAlertValuePane extends BasicBeanPane
{

    private static final int DE_FONT = 9;
    private static final double ALPH_PER = 100D;
    private UITextField textField;
    private LineComboBox lineCombo;
    private ColorSelectBox colorBox;
    private AlphaPane alphaPane;
    private UITextField contentField;
    private UIComboBox fontNameBox;
    private UIComboBox fontSizeBox;
    private UIRadioButton leftButton;
    private UIRadioButton rightButton;

    public ChartAlertValuePane()
    {
        setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel jpanel = FRGUIPaneFactory.createY_AXISBoxInnerContainer_S_Pane();
        add(jpanel, "Center");
        JPanel jpanel1 = FRGUIPaneFactory.createY_AXISBoxInnerContainer_S_Pane();
        jpanel.add(jpanel1);
        jpanel1.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText(new String[] {
            "ChartF-Alert-Line", "Set"
        })));
        JPanel jpanel2 = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        jpanel1.add(jpanel2);
        jpanel2.add(new UILabel((new StringBuilder()).append(Inter.getLocText(new String[] {
            "ChartF_Alert", "Value"
        })).append(":").toString()));
        textField = new UITextField();
        textField.setColumns(4);
        jpanel2.add(textField);
        textField.addMouseListener(new MouseAdapter() {

            final ChartAlertValuePane this$0;

            public void mousePressed(MouseEvent mouseevent)
            {
                showFormulaPane();
            }

            
            {
                this$0 = ChartAlertValuePane.this;
                super();
            }
        }
);
        textField.addKeyListener(new KeyAdapter() {

            final ChartAlertValuePane this$0;

            public void keyTyped(KeyEvent keyevent)
            {
                keyevent.consume();
                showFormulaPane();
            }

            
            {
                this$0 = ChartAlertValuePane.this;
                super();
            }
        }
);
        JPanel jpanel3 = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        jpanel1.add(jpanel3);
        jpanel3.add(new UILabel((new StringBuilder()).append(Inter.getLocText("Line-Style")).append(":").toString()));
        lineCombo = new LineComboBox(CoreConstants.STRIKE_LINE_STYLE_ARRAY_4_CHART);
        jpanel3.add(lineCombo);
        JPanel jpanel4 = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        jpanel1.add(jpanel4);
        jpanel4.add(new UILabel((new StringBuilder()).append(Inter.getLocText(new String[] {
            "ChartF-Alert-Line", "Color"
        })).append(":").toString()));
        colorBox = new ColorSelectBox(80);
        jpanel4.add(colorBox);
        alphaPane = new AlphaPane();
        jpanel1.add(alphaPane);
        JPanel jpanel5 = FRGUIPaneFactory.createY_AXISBoxInnerContainer_S_Pane();
        jpanel.add(jpanel5);
        jpanel5.setBorder(GUICoreUtils.createTitledBorder((new StringBuilder()).append(Inter.getLocText("Chart_Alert_Tip")).append(":").toString(), null));
        JPanel jpanel6 = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        jpanel5.add(jpanel6);
        fontNameBox = new UIComboBox();
        fontNameBox.setPreferredSize(new Dimension(80, 20));
        fontNameBox.addItem("SimSun");
        String as[] = Utils.getAvailableFontFamilyNames4Report();
        for(int i = 0; i < as.length; i++)
            fontNameBox.addItem(as[i]);

        fontSizeBox = new UIComboBox();
        fontSizeBox.setPreferredSize(new Dimension(80, 20));
        Integer ainteger[] = FRFontPane.Font_Sizes;
        for(int j = 0; j < ainteger.length; j++)
            fontSizeBox.addItem(ainteger[j]);

        double d = -1D;
        double ad[] = {
            d, d, d, d, d
        };
        double ad1[] = {
            0.10000000000000001D, 0.20000000000000001D, 0.5D, 0.20000000000000001D
        };
        Component acomponent[][] = {
            {
                null, new UILabel((new StringBuilder()).append(Inter.getLocText("Content")).append(":").toString()), contentField = new UITextField(3)
            }, {
                null, new UILabel((new StringBuilder()).append(Inter.getLocText("FRFont")).append(":").toString()), fontNameBox
            }, {
                null, new UILabel((new StringBuilder()).append(Inter.getLocText("FRFont-Size")).append(":").toString()), fontSizeBox
            }, {
                null, new UILabel((new StringBuilder()).append(Inter.getLocText("Layout")).append(": ").toString()), leftButton = new UIRadioButton(getLeftName())
            }, {
                null, null, rightButton = new UIRadioButton(getRightName())
            }
        };
        JPanel jpanel7 = TableLayoutHelper.createTableLayoutPane(acomponent, ad, ad1);
        jpanel6.add(jpanel7);
        ButtonGroup buttongroup = new ButtonGroup();
        buttongroup.add(leftButton);
        buttongroup.add(rightButton);
        leftButton.setSelected(true);
    }

    protected String getLeftName()
    {
        return Inter.getLocText("Chart_Alert_Left");
    }

    protected String getRightName()
    {
        return Inter.getLocText("Chart_Alert_Right");
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("ChartF-Alert-Line");
    }

    private void showFormulaPane()
    {
        final UIFormula formulaPane = FormulaFactory.createFormulaPane();
        formulaPane.populate(new Formula(textField.getText()));
        formulaPane.showLargeWindow(SwingUtilities.getWindowAncestor(this), new DialogActionAdapter() {

            final UIFormula val$formulaPane;
            final ChartAlertValuePane this$0;

            public void doOk()
            {
                Formula formula = formulaPane.update();
                textField.setText(Utils.objectToString(formula));
            }

            
            {
                this$0 = ChartAlertValuePane.this;
                formulaPane = uiformula;
                super();
            }
        }
).setVisible(true);
    }

    public void populateBean(ChartAlertValue chartalertvalue)
    {
        if(chartalertvalue.getLineStyle().getLineStyle() != 0 && chartalertvalue.getLineStyle().getLineStyle() != 1 && chartalertvalue.getLineStyle().getLineStyle() != 2 && chartalertvalue.getLineStyle().getLineStyle() != 5)
            chartalertvalue.getLineStyle().setLineStyle(1);
        textField.setText(Utils.objectToString(chartalertvalue.getAlertValueFormula()));
        lineCombo.setSelectedLineStyle(chartalertvalue.getLineStyle().getLineStyle());
        colorBox.setSelectObject(chartalertvalue.getLineColor().getSeriesColor());
        alphaPane.populate((int)((double)chartalertvalue.getAlertLineAlpha() * 100D));
        contentField.setText(chartalertvalue.getAlertContent());
        fontNameBox.setSelectedItem(chartalertvalue.getAlertFont().getName());
        fontSizeBox.setSelectedItem(Integer.valueOf(chartalertvalue.getAlertFont().getSize()));
        if(chartalertvalue.getAlertPosition() == 2)
            leftButton.setSelected(true);
        else
            rightButton.setSelected(true);
    }

    public ChartAlertValue updateBean()
    {
        ChartAlertValue chartalertvalue = new ChartAlertValue();
        updateBean(chartalertvalue);
        return chartalertvalue;
    }

    public void updateBean(ChartAlertValue chartalertvalue)
    {
        chartalertvalue.setAlertValueFormula(new Formula(textField.getText()));
        chartalertvalue.getLineColor().setSeriesColor(colorBox.getSelectObject());
        chartalertvalue.getLineStyle().setLineStyle(lineCombo.getSelectedLineStyle());
        chartalertvalue.setAlertLineAlpha(alphaPane.update());
        chartalertvalue.setAlertContent(contentField.getText());
        String s = Utils.objectToString(fontNameBox.getSelectedItem());
        int i = 9;
        Number number = Utils.objectToNumber(fontSizeBox.getSelectedItem(), true);
        if(number != null)
            i = number.intValue();
        chartalertvalue.setAlertFont(FRFont.getInstance(s, 0, i));
        if(leftButton.isSelected())
            chartalertvalue.setAlertPosition(2);
        else
        if(rightButton.isSelected())
            chartalertvalue.setAlertPosition(4);
    }

    public volatile void updateBean(Object obj)
    {
        updateBean((ChartAlertValue)obj);
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((ChartAlertValue)obj);
    }


}
