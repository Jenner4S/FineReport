// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.widget.ui;

import com.fr.design.editor.editor.IntegerEditor;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.form.ui.FieldEditor;
import com.fr.form.ui.NumberEditor;
import com.fr.general.Inter;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultFormatter;

// Referenced classes of package com.fr.design.widget.ui:
//            FieldEditorDefinePane, WaterMarkDictPane

public class NumberEditorDefinePane extends FieldEditorDefinePane
{

    private static final long serialVersionUID = 0x6f2da704f60c4295L;
    private UICheckBox allowDecimalsCheckBox;
    private UICheckBox allowNegativeCheckBox;
    private UICheckBox setMaxValueCheckBox;
    private UICheckBox setMinValueCheckBox;
    private UIBasicSpinner maxValueSpinner;
    private SpinnerNumberModel maxValueModel;
    private UIBasicSpinner minValueSpinner;
    private SpinnerNumberModel minValueModel;
    private IntegerEditor decimalLength;
    private JPanel limitNumberPane;
    private WaterMarkDictPane waterMarkDictPane;
    private ActionListener actionListener1;
    private ActionListener actionListener2;
    private ActionListener actionListener3;
    private ActionListener actionListener4;
    private ChangeListener changeListener1;
    private ChangeListener changeListener2;

    public NumberEditorDefinePane()
    {
        actionListener1 = new ActionListener() {

            final NumberEditorDefinePane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                if(allowDecimalsCheckBox.isSelected())
                    limitNumberPane.setVisible(true);
                else
                    limitNumberPane.setVisible(false);
            }

            
            {
                this$0 = NumberEditorDefinePane.this;
                super();
            }
        }
;
        actionListener2 = new ActionListener() {

            final NumberEditorDefinePane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                if(allowNegativeCheckBox.isSelected())
                {
                    minValueModel.setMinimum(Double.valueOf(-1.7976931348623157E+308D));
                    if(!setMinValueCheckBox.isSelected())
                        maxValueModel.setMinimum(Double.valueOf(-1.7976931348623157E+308D));
                } else
                {
                    minValueModel.setMinimum(Double.valueOf(0.0D));
                    if(!setMinValueCheckBox.isSelected())
                        maxValueModel.setMinimum(Double.valueOf(0.0D));
                    Double double1 = Double.valueOf(Double.parseDouble((new StringBuilder()).append("").append(minValueSpinner.getValue()).toString()));
                    Double double2 = Double.valueOf(Double.parseDouble((new StringBuilder()).append("").append(maxValueSpinner.getValue()).toString()));
                    if(double1.doubleValue() < 0.0D)
                        minValueSpinner.setValue(Double.valueOf(0.0D));
                    if(double2.doubleValue() < 0.0D)
                        maxValueSpinner.setValue(Double.valueOf(0.0D));
                }
            }

            
            {
                this$0 = NumberEditorDefinePane.this;
                super();
            }
        }
;
        actionListener3 = new ActionListener() {

            final NumberEditorDefinePane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                if(setMaxValueCheckBox.isSelected())
                {
                    maxValueSpinner.setVisible(true);
                    Double double1 = new Double(0.0D);
                    if(setMinValueCheckBox.isSelected())
                    {
                        Double double2 = Double.valueOf(Double.parseDouble((new StringBuilder()).append("").append(minValueSpinner.getValue()).toString()));
                        if(double2.doubleValue() > double1.doubleValue())
                            double1 = double2;
                    }
                    maxValueSpinner.setValue(double1);
                } else
                {
                    maxValueSpinner.setVisible(false);
                    minValueModel.setMaximum(Double.valueOf(1.7976931348623157E+308D));
                }
            }

            
            {
                this$0 = NumberEditorDefinePane.this;
                super();
            }
        }
;
        actionListener4 = new ActionListener() {

            final NumberEditorDefinePane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                if(setMinValueCheckBox.isSelected())
                {
                    minValueSpinner.setVisible(true);
                    Double double1 = new Double(0.0D);
                    if(setMaxValueCheckBox.isSelected())
                    {
                        Double double2 = Double.valueOf(Double.parseDouble((new StringBuilder()).append("").append(maxValueSpinner.getValue()).toString()));
                        if(double2.doubleValue() < double1.doubleValue())
                            double1 = double2;
                    }
                    minValueSpinner.setValue(double1);
                } else
                {
                    minValueSpinner.setVisible(false);
                    maxValueModel.setMinimum(Double.valueOf(allowNegativeCheckBox.isSelected() ? -1.7976931348623157E+308D : (new Double(0.0D)).doubleValue()));
                }
            }

            
            {
                this$0 = NumberEditorDefinePane.this;
                super();
            }
        }
;
        changeListener1 = new ChangeListener() {

            final NumberEditorDefinePane this$0;

            public void stateChanged(ChangeEvent changeevent)
            {
                if(setMaxValueCheckBox.isSelected() && setMinValueCheckBox.isSelected())
                    minValueModel.setMaximum(Double.valueOf(Double.parseDouble((new StringBuilder()).append("").append(maxValueSpinner.getValue()).toString())));
            }

            
            {
                this$0 = NumberEditorDefinePane.this;
                super();
            }
        }
;
        changeListener2 = new ChangeListener() {

            final NumberEditorDefinePane this$0;

            public void stateChanged(ChangeEvent changeevent)
            {
                if(setMinValueCheckBox.isSelected() && setMaxValueCheckBox.isSelected())
                    maxValueModel.setMinimum(Double.valueOf(Double.parseDouble((new StringBuilder()).append("").append(minValueSpinner.getValue()).toString())));
            }

            
            {
                this$0 = NumberEditorDefinePane.this;
                super();
            }
        }
;
        initComponents();
    }

    protected String title4PopupWindow()
    {
        return "number";
    }

    protected JPanel setFirstContentPane()
    {
        JPanel jpanel = FRGUIPaneFactory.createY_AXISBoxInnerContainer_L_Pane();
        jpanel.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));
        jpanel.setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel jpanel1 = FRGUIPaneFactory.createY_AXISBoxInnerContainer_L_Pane();
        jpanel.add(jpanel1, "North");
        waterMarkDictPane = new WaterMarkDictPane();
        jpanel1.add(waterMarkDictPane);
        allowDecimalsCheckBox = new UICheckBox(Inter.getLocText("Allow_Decimals"));
        decimalLength = new IntegerEditor();
        decimalLength.setColumns(4);
        limitNumberPane = GUICoreUtils.createFlowPane(new JComponent[] {
            new UILabel((new StringBuilder()).append(Inter.getLocText(new String[] {
                "Double", "Numbers"
            })).append(":").toString()), decimalLength
        }, 0, 4);
        jpanel1.add(GUICoreUtils.createFlowPane(new JComponent[] {
            allowDecimalsCheckBox, limitNumberPane
        }, 0, 4));
        allowDecimalsCheckBox.addActionListener(actionListener1);
        allowNegativeCheckBox = new UICheckBox(Inter.getLocText("Allow_Negative"));
        jpanel1.add(GUICoreUtils.createFlowPane(new JComponent[] {
            allowNegativeCheckBox
        }, 0, 4));
        allowNegativeCheckBox.addActionListener(actionListener2);
        setMaxValueCheckBox = new UICheckBox(Inter.getLocText("Need_Max_Value"), false);
        maxValueSpinner = new UIBasicSpinner(maxValueModel = new SpinnerNumberModel(0.0D, -1.7976931348623157E+308D, 1.7976931348623157E+308D, 1.0D));
        maxValueSpinner.setPreferredSize(new Dimension(120, 20));
        setNotAllowsInvalid(maxValueSpinner);
        jpanel1.add(GUICoreUtils.createFlowPane(new JComponent[] {
            setMaxValueCheckBox, maxValueSpinner
        }, 0, 4));
        maxValueSpinner.setVisible(false);
        setMaxValueCheckBox.addActionListener(actionListener3);
        maxValueSpinner.addChangeListener(changeListener1);
        setMinValueCheckBox = new UICheckBox(Inter.getLocText("Need_Min_Value"), false);
        minValueSpinner = new UIBasicSpinner(minValueModel = new SpinnerNumberModel(0.0D, -1.7976931348623157E+308D, 1.7976931348623157E+308D, 1.0D));
        minValueSpinner.setPreferredSize(new Dimension(120, 20));
        setNotAllowsInvalid(minValueSpinner);
        jpanel1.add(GUICoreUtils.createFlowPane(new JComponent[] {
            setMinValueCheckBox, minValueSpinner
        }, 0, 4));
        minValueSpinner.setVisible(false);
        setMinValueCheckBox.addActionListener(actionListener4);
        minValueSpinner.addChangeListener(changeListener2);
        return jpanel;
    }

    protected void populateSubFieldEditorBean(NumberEditor numbereditor)
    {
        allowDecimalsCheckBox.setSelected(numbereditor.isAllowDecimals());
        if(numbereditor.isAllowDecimals())
            decimalLength.setValue(Integer.valueOf(numbereditor.getMaxDecimalLength()));
        else
            limitNumberPane.setVisible(false);
        allowNegativeCheckBox.setSelected(numbereditor.isAllowNegative());
        if(numbereditor.getMaxValue() == 1.7976931348623157E+308D)
        {
            setMaxValueCheckBox.setSelected(false);
            maxValueSpinner.setValue(new Double(1.7976931348623157E+308D));
            maxValueSpinner.setVisible(false);
        } else
        {
            setMaxValueCheckBox.setSelected(true);
            maxValueSpinner.setVisible(true);
            maxValueSpinner.setValue(new Double(numbereditor.getMaxValue()));
        }
        if(numbereditor.getMinValue() == -1.7976931348623157E+308D)
        {
            setMinValueCheckBox.setSelected(false);
            minValueSpinner.setValue(new Double(-1.7976931348623157E+308D));
            minValueSpinner.setVisible(false);
        } else
        {
            setMinValueCheckBox.setSelected(true);
            minValueSpinner.setVisible(true);
            minValueSpinner.setValue(new Double(numbereditor.getMinValue()));
        }
        waterMarkDictPane.populate(numbereditor);
    }

    protected NumberEditor updateSubFieldEditorBean()
    {
        NumberEditor numbereditor = new NumberEditor();
        numbereditor.setAllowDecimals(allowDecimalsCheckBox.isSelected());
        if(allowDecimalsCheckBox.isSelected())
            numbereditor.setMaxDecimalLength(decimalLength.getValue().intValue());
        numbereditor.setAllowNegative(allowNegativeCheckBox.isSelected());
        if(setMaxValueCheckBox.isSelected())
            numbereditor.setMaxValue(Double.parseDouble((new StringBuilder()).append("").append(maxValueSpinner.getValue()).toString()));
        else
            numbereditor.setMaxValue(1.7976931348623157E+308D);
        if(setMinValueCheckBox.isSelected())
            numbereditor.setMinValue(Double.parseDouble((new StringBuilder()).append("").append(minValueSpinner.getValue()).toString()));
        else
            numbereditor.setMinValue(-1.7976931348623157E+308D);
        waterMarkDictPane.update(numbereditor);
        return numbereditor;
    }

    private void checkVisible()
    {
        if(setMinValueCheckBox.isSelected())
            minValueSpinner.setVisible(true);
        else
            minValueSpinner.setVisible(false);
        if(setMinValueCheckBox.isSelected())
            minValueSpinner.setVisible(true);
        else
            minValueSpinner.setVisible(false);
    }

    private void setNotAllowsInvalid(UIBasicSpinner uibasicspinner)
    {
        JComponent jcomponent = uibasicspinner.getEditor();
        if(jcomponent instanceof javax.swing.JSpinner.DefaultEditor)
        {
            JFormattedTextField jformattedtextfield = ((javax.swing.JSpinner.DefaultEditor)jcomponent).getTextField();
            jformattedtextfield.setColumns(10);
            javax.swing.JFormattedTextField.AbstractFormatter abstractformatter = jformattedtextfield.getFormatter();
            DefaultFormatter defaultformatter = (DefaultFormatter)abstractformatter;
            defaultformatter.setAllowsInvalid(false);
        }
    }

    protected volatile FieldEditor updateSubFieldEditorBean()
    {
        return updateSubFieldEditorBean();
    }

    protected volatile void populateSubFieldEditorBean(FieldEditor fieldeditor)
    {
        populateSubFieldEditorBean((NumberEditor)fieldeditor);
    }









}
