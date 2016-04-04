// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.chart.series.SeriesCondition;

import com.fr.base.FRContext;
import com.fr.base.Utils;
import com.fr.chart.base.*;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.chart.comp.BorderAttriPane;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import javax.swing.*;

public class ConditionTrendLinePane extends BasicBeanPane
{

    private static final long serialVersionUID = 0x35aaed788ee6f48dL;
    private ConditionTrendLine editing;
    private UITextField nameLabel;
    private BorderAttriPane linePane;
    private UIRadioButton linearButton;
    private UIRadioButton polynomialButton;
    private UIRadioButton logButton;
    private UIRadioButton exponentButton;
    private UIRadioButton powerButton;
    private UIRadioButton maButton;
    private UIBasicSpinner maSpinner;
    private UITextField forwardLabel;
    private UITextField backwardLabel;
    ActionListener listener;

    public ConditionTrendLinePane()
    {
        listener = new ActionListener() {

            final ConditionTrendLinePane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                if(actionevent.getSource() instanceof JRadioButton)
                {
                    JRadioButton jradiobutton = (JRadioButton)actionevent.getSource();
                    if(jradiobutton.isSelected())
                        maSpinner.setEnabled(false);
                }
            }

            
            {
                this$0 = ConditionTrendLinePane.this;
                super();
            }
        }
;
        setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel jpanel = FRGUIPaneFactory.createY_AXISBoxInnerContainer_S_Pane();
        add(jpanel, "North");
        JPanel jpanel1 = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        jpanel.add(jpanel1);
        jpanel1.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText(new String[] {
            "Chart_TrendLine", "WF-Name"
        }), null));
        jpanel1.add(new UILabel(Inter.getLocText(new String[] {
            "Define", "WF-Name"
        })));
        jpanel1.add(nameLabel = new UITextField("", 6));
        jpanel.add(linePane = new BorderAttriPane());
        linePane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText(new String[] {
            "Chart_Trend", "Line-Style"
        }), null));
        JPanel jpanel2 = FRGUIPaneFactory.createY_AXISBoxInnerContainer_S_Pane();
        jpanel.add(jpanel2);
        jpanel2.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText(new String[] {
            "Chart_Trend", "Type"
        }), null));
        JPanel jpanel3 = FRGUIPaneFactory.createBoxFlowInnerContainer_S_Pane();
        jpanel2.add(jpanel3);
        jpanel3.add(exponentButton = new UIRadioButton(Inter.getLocText("Chart_Exponent")));
        jpanel3.add(linearButton = new UIRadioButton(Inter.getLocText("Chart_Linear")));
        jpanel3.add(logButton = new UIRadioButton(Inter.getLocText("Chart_Log")));
        jpanel3.add(polynomialButton = new UIRadioButton(Inter.getLocText("Chart_Polynomial")));
        jpanel3.add(powerButton = new UIRadioButton(Inter.getLocText("Chart_Power")));
        JPanel jpanel4 = FRGUIPaneFactory.createBoxFlowInnerContainer_S_Pane();
        jpanel2.add(jpanel4);
        jpanel4.add(maButton = new UIRadioButton(Inter.getLocText("Chart_Move_Average")));
        jpanel4.add(new UILabel((new StringBuilder()).append(Inter.getLocText("cycle")).append(":").toString()));
        jpanel4.add(maSpinner = new UIBasicSpinner(new SpinnerNumberModel(2, 1, 999, 1)));
        maSpinner.setEnabled(false);
        ButtonGroup buttongroup = new ButtonGroup();
        buttongroup.add(exponentButton);
        buttongroup.add(linearButton);
        buttongroup.add(logButton);
        buttongroup.add(polynomialButton);
        buttongroup.add(powerButton);
        buttongroup.add(maButton);
        jpanel.add(initExtendsPane());
        initListeners();
    }

    private JPanel initExtendsPane()
    {
        JPanel jpanel = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        jpanel.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText(new String[] {
            "Chart_Trend", "Forecast"
        }), null));
        jpanel.add(new UILabel((new StringBuilder()).append(Inter.getLocText("Chart_TrendLine_Forward")).append(":").toString()));
        jpanel.add(forwardLabel = new UITextField("0", 5));
        jpanel.add(new UILabel(Inter.getLocText("cycle")));
        jpanel.add(new UILabel((new StringBuilder()).append(Inter.getLocText("Chart_TrendLine_Backward")).append(":").toString()));
        jpanel.add(backwardLabel = new UITextField("0", 5));
        jpanel.add(new UILabel(Inter.getLocText("cycle")));
        return jpanel;
    }

    private void initListeners()
    {
        exponentButton.addActionListener(listener);
        linearButton.addActionListener(listener);
        logButton.addActionListener(listener);
        polynomialButton.addActionListener(listener);
        powerButton.addActionListener(listener);
        maButton.addActionListener(new ActionListener() {

            final ConditionTrendLinePane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                if(maButton.isSelected())
                    maSpinner.setEnabled(true);
            }

            
            {
                this$0 = ConditionTrendLinePane.this;
                super();
            }
        }
);
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("Chart_TrendLine");
    }

    public void populateBean(ConditionTrendLine conditiontrendline)
    {
        editing = conditiontrendline;
        nameLabel.setText(conditiontrendline.getLine().getTrendLineName());
        forwardLabel.setText(Integer.toString(conditiontrendline.getForward()));
        backwardLabel.setText(Integer.toString(conditiontrendline.getBackward()));
        if(conditiontrendline.getLine() != null)
        {
            linePane.setLineStyle(conditiontrendline.getLine().getLineStyleInfo().getAttrLineStyle().getLineStyle());
            Color color = conditiontrendline.getLine().getLineStyleInfo().getAttrLineColor().getSeriesColor();
            color = color != null ? color : new Color(128, 128, 128);
            linePane.setLineColor(color);
            ChartEquationType chartequationtype = conditiontrendline.getLine().getEquation();
            maSpinner.setEnabled(false);
            static class _cls3
            {

                static final int $SwitchMap$com$fr$chart$base$ChartEquationType[];

                static 
                {
                    $SwitchMap$com$fr$chart$base$ChartEquationType = new int[ChartEquationType.values().length];
                    try
                    {
                        $SwitchMap$com$fr$chart$base$ChartEquationType[ChartEquationType.EXPONENT.ordinal()] = 1;
                    }
                    catch(NoSuchFieldError nosuchfielderror) { }
                    try
                    {
                        $SwitchMap$com$fr$chart$base$ChartEquationType[ChartEquationType.LINEAR.ordinal()] = 2;
                    }
                    catch(NoSuchFieldError nosuchfielderror1) { }
                    try
                    {
                        $SwitchMap$com$fr$chart$base$ChartEquationType[ChartEquationType.LOG.ordinal()] = 3;
                    }
                    catch(NoSuchFieldError nosuchfielderror2) { }
                    try
                    {
                        $SwitchMap$com$fr$chart$base$ChartEquationType[ChartEquationType.POLYNOMIAL.ordinal()] = 4;
                    }
                    catch(NoSuchFieldError nosuchfielderror3) { }
                    try
                    {
                        $SwitchMap$com$fr$chart$base$ChartEquationType[ChartEquationType.POWER.ordinal()] = 5;
                    }
                    catch(NoSuchFieldError nosuchfielderror4) { }
                    try
                    {
                        $SwitchMap$com$fr$chart$base$ChartEquationType[ChartEquationType.MA.ordinal()] = 6;
                    }
                    catch(NoSuchFieldError nosuchfielderror5) { }
                }
            }

            switch(_cls3..SwitchMap.com.fr.chart.base.ChartEquationType[chartequationtype.ordinal()])
            {
            case 1: // '\001'
                exponentButton.setSelected(true);
                break;

            case 2: // '\002'
                linearButton.setSelected(true);
                break;

            case 3: // '\003'
                logButton.setSelected(true);
                break;

            case 4: // '\004'
                polynomialButton.setSelected(true);
                break;

            case 5: // '\005'
                powerButton.setSelected(true);
                break;

            case 6: // '\006'
                maButton.setSelected(true);
                maSpinner.setEnabled(true);
                maSpinner.setValue(Integer.valueOf(conditiontrendline.getLine().getMoveAverage()));
                break;
            }
        }
    }

    public ConditionTrendLine updateBean()
    {
        updateBean(editing);
        return editing;
    }

    public void updateBean(ConditionTrendLine conditiontrendline)
    {
        try
        {
            maSpinner.commitEdit();
        }
        catch(ParseException parseexception)
        {
            FRContext.getLogger().error(parseexception.getMessage(), parseexception);
        }
        conditiontrendline.getLine().setTrendLineName(nameLabel.getText());
        conditiontrendline.setForward(Utils.string2Number(forwardLabel.getText()).intValue());
        conditiontrendline.setBackward(Utils.string2Number(backwardLabel.getText()).intValue());
        if(conditiontrendline.getLine() != null)
        {
            if(exponentButton.isSelected())
                conditiontrendline.getLine().setEquation(ChartEquationType.EXPONENT);
            else
            if(linearButton.isSelected())
                conditiontrendline.getLine().setEquation(ChartEquationType.LINEAR);
            else
            if(logButton.isSelected())
                conditiontrendline.getLine().setEquation(ChartEquationType.LOG);
            else
            if(polynomialButton.isSelected())
                conditiontrendline.getLine().setEquation(ChartEquationType.POLYNOMIAL);
            else
            if(powerButton.isSelected())
                conditiontrendline.getLine().setEquation(ChartEquationType.POWER);
            else
            if(maButton.isSelected())
            {
                conditiontrendline.getLine().setEquation(ChartEquationType.MA);
                conditiontrendline.getLine().setMoveAverage(Utils.string2Number(Utils.objectToString(maSpinner.getValue())).intValue());
            }
            conditiontrendline.getLine().getLineStyleInfo().getAttrLineColor().setSeriesColor(linePane.getLineColor());
            conditiontrendline.getLine().getLineStyleInfo().getAttrLineStyle().setLineStyle(linePane.getLineStyle());
        }
    }

    public volatile void updateBean(Object obj)
    {
        updateBean((ConditionTrendLine)obj);
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((ConditionTrendLine)obj);
    }


}
