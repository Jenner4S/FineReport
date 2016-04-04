// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style;

import com.fr.base.Formula;
import com.fr.base.Utils;
import com.fr.chart.chartattr.CategoryAxis;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.editor.*;
import com.fr.design.formula.FormulaFactory;
import com.fr.design.formula.UIFormula;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.style.FormatBox;
import com.fr.general.*;
import com.fr.stable.StringUtils;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class DateAxisValuePane extends FurtherBasicBeanPane
{

    private static String TYPES[] = {
        Inter.getLocText("Year"), Inter.getLocText("Month"), Inter.getLocText("Sun"), Inter.getLocText("Sche-Hour"), Inter.getLocText("Sche-Minute"), Inter.getLocText("Sche-Second")
    };
    private static Map VALUES;
    private static Map INTS;
    private UICheckBox maxCheckBox;
    private ValueEditorPane maxValueField;
    private UICheckBox minCheckBox;
    private ValueEditorPane minValueField;
    private UICheckBox mainTickBox;
    private UITextField mainUnitField;
    private UIComboBox mainType;
    private FormatBox formatBox;

    public FormatBox getFormatBox()
    {
        return formatBox;
    }

    public DateAxisValuePane()
    {
        initComponents();
    }

    private void initMin()
    {
        minCheckBox = new UICheckBox(Inter.getLocText("Min_Value"));
        Date date = null;
        DateEditor dateeditor = new DateEditor(date, true, Inter.getLocText("Date"), 3);
        FormulaEditor formulaeditor = new FormulaEditor(Inter.getLocText("Parameter-Formula"));
        Editor aeditor[] = {
            dateeditor, formulaeditor
        };
        minValueField = new ValueEditorPane(aeditor);
        minValueField.setEnabled(false);
        minCheckBox.addActionListener(new ActionListener() {

            final DateAxisValuePane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                checkBoxUse();
            }

            
            {
                this$0 = DateAxisValuePane.this;
                super();
            }
        }
);
    }

    private void initMax()
    {
        maxCheckBox = new UICheckBox(Inter.getLocText("Max_Value"));
        Date date = null;
        DateEditor dateeditor = new DateEditor(date, true, Inter.getLocText("Date"), 3);
        FormulaEditor formulaeditor = new FormulaEditor(Inter.getLocText("Parameter-Formula"));
        Editor aeditor[] = {
            dateeditor, formulaeditor
        };
        maxValueField = new ValueEditorPane(aeditor);
        maxValueField.setEnabled(false);
        maxCheckBox.addActionListener(new ActionListener() {

            final DateAxisValuePane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                checkBoxUse();
            }

            
            {
                this$0 = DateAxisValuePane.this;
                super();
            }
        }
);
    }

    private void initMain()
    {
        mainTickBox = new UICheckBox(Inter.getLocText("MainGraduationUnit"));
        mainUnitField = new UITextField();
        mainUnitField.setPreferredSize(new Dimension(30, 20));
        mainUnitField.setEditable(false);
        mainType = new UIComboBox(TYPES);
        mainType.setEnabled(false);
        mainTickBox.addActionListener(new ActionListener() {

            final DateAxisValuePane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                checkBoxUse();
            }

            
            {
                this$0 = DateAxisValuePane.this;
                super();
            }
        }
);
        addListener(mainTickBox, mainUnitField);
    }

    private void initComponents()
    {
        setLayout(FRGUIPaneFactory.createBorderLayout());
        formatBox = new FormatBox();
        JPanel jpanel = FRGUIPaneFactory.createY_AXISBoxInnerContainer_S_Pane();
        add(jpanel, "North");
        initMin();
        initMax();
        initMain();
        JPanel jpanel1 = new JPanel();
        jpanel1.setLayout(new FlowLayout(1, 4, 0));
        jpanel1.add(mainUnitField);
        jpanel1.add(mainType);
        JPanel jpanel2 = new JPanel();
        jpanel2.setLayout(new FlowLayout(1, 4, 0));
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d, d, d
        };
        double ad1[] = {
            d, d1
        };
        Component acomponent[][] = {
            {
                minCheckBox, minValueField
            }, {
                maxCheckBox, maxValueField
            }
        };
        JPanel jpanel3 = TableLayoutHelper.createTableLayoutPane(acomponent, ad, ad1);
        Component acomponent1[][] = {
            {
                jpanel3, null
            }, {
                mainTickBox, jpanel1
            }
        };
        jpanel.add(TableLayoutHelper.createTableLayoutPane(acomponent1, ad, ad1));
    }

    private void addListener(final UICheckBox box, final UITextField textField)
    {
        textField.addMouseListener(new MouseAdapter() {

            final UICheckBox val$box;
            final UITextField val$textField;
            final DateAxisValuePane this$0;

            public void mousePressed(MouseEvent mouseevent)
            {
                if(box.isSelected())
                    showFormulaPane(textField);
            }

            
            {
                this$0 = DateAxisValuePane.this;
                box = uicheckbox;
                textField = uitextfield;
                super();
            }
        }
);
        textField.addKeyListener(new KeyAdapter() {

            final UICheckBox val$box;
            final UITextField val$textField;
            final DateAxisValuePane this$0;

            public void keyTyped(KeyEvent keyevent)
            {
                if(box.isSelected())
                {
                    keyevent.consume();
                    showFormulaPane(textField);
                }
            }

            
            {
                this$0 = DateAxisValuePane.this;
                box = uicheckbox;
                textField = uitextfield;
                super();
            }
        }
);
    }

    private void showFormulaPane(final UITextField jTextField)
    {
        final UIFormula formulaPane = FormulaFactory.createFormulaPane();
        formulaPane.populate(new Formula(jTextField.getText()));
        BasicDialog basicdialog = formulaPane.showLargeWindow(SwingUtilities.getWindowAncestor(this), new DialogActionAdapter() {

            final UITextField val$jTextField;
            final UIFormula val$formulaPane;
            final DateAxisValuePane this$0;

            public void doOk()
            {
                jTextField.setText(Utils.objectToString(formulaPane.update()));
            }

            
            {
                this$0 = DateAxisValuePane.this;
                jTextField = uitextfield;
                formulaPane = uiformula;
                super();
            }
        }
);
        basicdialog.setVisible(true);
    }

    private void populateMain(CategoryAxis categoryaxis)
    {
        if(categoryaxis.isCustomMainUnit() && categoryaxis.getMainUnit() != null)
        {
            mainTickBox.setSelected(true);
            mainUnitField.setText(Utils.objectToString(categoryaxis.getMainUnit()));
            mainType.setSelectedItem(INTS.get(Integer.valueOf(categoryaxis.getMainType())));
        }
    }

    private void updateMain(CategoryAxis categoryaxis)
    {
        if(mainTickBox.isSelected() && StringUtils.isNotEmpty(mainUnitField.getText()))
        {
            categoryaxis.setCustomMainUnit(true);
            categoryaxis.setMainUnit(new Formula(mainUnitField.getText()));
            categoryaxis.setMainType(((Integer)VALUES.get(mainType.getSelectedItem())).intValue());
        } else
        {
            categoryaxis.setCustomMainUnit(false);
        }
    }

    public boolean accept(Object obj)
    {
        return obj instanceof CategoryAxis;
    }

    public void reset()
    {
    }

    public String title4PopupWindow()
    {
        return Inter.getLocText("Chart_Date_Axis");
    }

    private void checkBoxUse()
    {
        minValueField.setEnabled(minCheckBox.isSelected());
        maxValueField.setEnabled(maxCheckBox.isSelected());
        if(minValueField.getCurrentEditor() instanceof FormulaEditor)
        {
            FormulaEditor formulaeditor = (FormulaEditor)minValueField.getCurrentEditor();
            formulaeditor.enableEditor(minCheckBox.isSelected());
        }
        if(maxValueField.getCurrentEditor() instanceof FormulaEditor)
        {
            FormulaEditor formulaeditor1 = (FormulaEditor)maxValueField.getCurrentEditor();
            formulaeditor1.enableEditor(maxCheckBox.isSelected());
        }
        mainType.setEnabled(mainTickBox.isSelected());
        mainUnitField.setEnabled(mainTickBox.isSelected());
    }

    public void populateBean(CategoryAxis categoryaxis)
    {
        if(categoryaxis == null)
            return;
        if(!categoryaxis.isDate())
            return;
        if(categoryaxis.isCustomMinValue() && categoryaxis.getMinValue() != null)
        {
            minCheckBox.setSelected(true);
            String s = categoryaxis.getMinValue().getPureContent();
            if(!isDateForm(s))
            {
                minValueField.populate(categoryaxis.getMinValue());
            } else
            {
                Date date = getDateFromFormula(categoryaxis.getMinValue());
                minValueField.populate(date);
            }
        }
        if(categoryaxis.isCustomMaxValue() && categoryaxis.getMaxValue() != null)
        {
            maxCheckBox.setSelected(true);
            String s1 = categoryaxis.getMaxValue().getPureContent();
            if(!isDateForm(s1))
            {
                maxValueField.populate(categoryaxis.getMaxValue());
            } else
            {
                Date date1 = getDateFromFormula(categoryaxis.getMaxValue());
                maxValueField.populate(date1);
            }
        }
        populateMain(categoryaxis);
        checkBoxUse();
    }

    private boolean isDateForm(String s)
    {
        s = Pattern.compile("\"").matcher(s).replaceAll("");
        if(s.matches("^[+-]?[0-9]*[0-9]$"))
            return false;
        else
            return DateUtils.string2Date(s, true) != null;
    }

    public void updateBean(CategoryAxis categoryaxis)
    {
        updateMain(categoryaxis);
        if(minCheckBox.isSelected())
        {
            if(minValueField.getCurrentEditor() instanceof FormulaEditor)
            {
                Formula formula = (Formula)minValueField.update();
                categoryaxis.setMinValue(formula);
                categoryaxis.setCustomMinValue(!StringUtils.isEmpty(formula.getPureContent()));
            } else
            {
                Date date = (Date)minValueField.update();
                DateEditor dateeditor = (DateEditor)minValueField.getCurrentEditor();
                String s = dateeditor.getUIDatePickerFormat().format(date);
                categoryaxis.setCustomMinValue(!StringUtils.isEmpty(s));
                categoryaxis.setMinValue(new Formula(s));
            }
        } else
        {
            categoryaxis.setCustomMinValue(false);
        }
        if(maxCheckBox.isSelected())
        {
            if(maxValueField.getCurrentEditor() instanceof FormulaEditor)
            {
                Formula formula1 = (Formula)maxValueField.update();
                categoryaxis.setMaxValue(formula1);
                categoryaxis.setCustomMaxValue(!StringUtils.isEmpty(formula1.getPureContent()));
            } else
            {
                Date date1 = (Date)maxValueField.update();
                DateEditor dateeditor1 = (DateEditor)maxValueField.getCurrentEditor();
                String s1 = dateeditor1.getUIDatePickerFormat().format(date1);
                categoryaxis.setCustomMaxValue(!StringUtils.isEmpty(s1));
                categoryaxis.setMaxValue(new Formula(s1));
            }
        } else
        {
            categoryaxis.setCustomMaxValue(false);
        }
        checkBoxUse();
    }

    public CategoryAxis updateBean()
    {
        return null;
    }

    private static final Date getDateFromFormula(Formula formula)
    {
        String s = formula.getPureContent();
        s = Pattern.compile("\"").matcher(s).replaceAll("");
        Date date = DateUtils.string2Date(s, true);
        try
        {
            String s1 = DateUtils.getDate2LStr(date);
            date = DateUtils.DATETIMEFORMAT2.parse(s1);
        }
        catch(ParseException parseexception)
        {
            FRLogger.getLogger().error(Inter.getLocText("Cannot_Get_Date"));
        }
        return date;
    }

    public volatile void updateBean(Object obj)
    {
        updateBean((CategoryAxis)obj);
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((CategoryAxis)obj);
    }

    static 
    {
        VALUES = new HashMap();
        VALUES.put(Inter.getLocText("Year"), Integer.valueOf(3));
        VALUES.put(Inter.getLocText("Month"), Integer.valueOf(2));
        VALUES.put(Inter.getLocText("Sun"), Integer.valueOf(1));
        VALUES.put(Inter.getLocText("Sche-Hour"), Integer.valueOf(4));
        VALUES.put(Inter.getLocText("Sche-Minute"), Integer.valueOf(5));
        VALUES.put(Inter.getLocText("Sche-Second"), Integer.valueOf(6));
        INTS = new HashMap();
        INTS.put(Integer.valueOf(3), Inter.getLocText("Year"));
        INTS.put(Integer.valueOf(2), Inter.getLocText("Month"));
        INTS.put(Integer.valueOf(1), Inter.getLocText("Sun"));
        INTS.put(Integer.valueOf(4), Inter.getLocText("Sche-Hour"));
        INTS.put(Integer.valueOf(5), Inter.getLocText("Sche-Minute"));
        INTS.put(Integer.valueOf(6), Inter.getLocText("Sche-Second"));
    }


}
