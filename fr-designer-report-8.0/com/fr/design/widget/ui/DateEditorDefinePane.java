// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.widget.ui;

import com.fr.base.FRContext;
import com.fr.base.Formula;
import com.fr.data.core.FormatField;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.ui.DateEditor;
import com.fr.form.ui.DirectWriteEditor;
import com.fr.general.*;
import com.fr.script.Calculator;
import com.fr.stable.*;
import java.awt.Color;
import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.Document;

// Referenced classes of package com.fr.design.widget.ui:
//            DirectWriteEditorDefinePane

public class DateEditorDefinePane extends DirectWriteEditorDefinePane
{

    private UIComboBox returnTypeComboBox;
    private UILabel sampleLabel;
    private UITextField patternTextField;
    private JList patternList;
    private ValueEditorPane startDv;
    private ValueEditorPane endDv;
    private DocumentListener patternTextDocumentListener;
    private ListSelectionListener patternListSelectionListener;

    public DateEditorDefinePane()
    {
        patternTextField = null;
        patternList = null;
        patternTextDocumentListener = new DocumentListener() {

            final DateEditorDefinePane this$0;

            public void insertUpdate(DocumentEvent documentevent)
            {
                refreshPreviewLabel();
            }

            public void removeUpdate(DocumentEvent documentevent)
            {
                refreshPreviewLabel();
            }

            public void changedUpdate(DocumentEvent documentevent)
            {
                refreshPreviewLabel();
            }

            
            {
                this$0 = DateEditorDefinePane.this;
                super();
            }
        }
;
        patternListSelectionListener = new ListSelectionListener() {

            final DateEditorDefinePane this$0;

            public void valueChanged(ListSelectionEvent listselectionevent)
            {
                patternTextField.setText((String)patternList.getSelectedValue());
            }

            
            {
                this$0 = DateEditorDefinePane.this;
                super();
            }
        }
;
        initComponets();
    }

    private void initComponets()
    {
        super.initComponents();
    }

    protected String title4PopupWindow()
    {
        return "Date";
    }

    protected JPanel setSecondContentPane()
    {
        JPanel jpanel = FRGUIPaneFactory.createY_AXISBoxInnerContainer_L_Pane();
        JPanel jpanel1 = FRGUIPaneFactory.createBorderLayout_S_Pane();
        jpanel1.add(new UILabel((new StringBuilder()).append(Inter.getLocText("Widget-Date_Selector_Return_Type")).append(":").toString()), "West");
        returnTypeComboBox = new UIComboBox(new String[] {
            Inter.getLocText("String"), Inter.getLocText("Date")
        });
        jpanel1.add(returnTypeComboBox, "Center");
        jpanel.add(jpanel1);
        JPanel jpanel2 = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("StyleFormat-Sample"));
        jpanel.add(jpanel2, "North");
        sampleLabel = new UILabel("");
        jpanel2.add(sampleLabel, "Center");
        sampleLabel.setBorder(BorderFactory.createEmptyBorder(2, 4, 4, 4));
        sampleLabel.setHorizontalAlignment(0);
        sampleLabel.setFont(FRContext.getDefaultValues().getFRFont());
        JPanel jpanel3 = FRGUIPaneFactory.createBorderLayout_S_Pane();
        jpanel.add(jpanel3, "Center");
        jpanel3.setBorder(BorderFactory.createEmptyBorder(4, 0, 2, 0));
        patternTextField = new UITextField();
        jpanel3.add(patternTextField, "North");
        patternTextField.getDocument().addDocumentListener(patternTextDocumentListener);
        patternList = new JList(new DefaultListModel());
        DefaultListModel defaultlistmodel = (DefaultListModel)patternList.getModel();
        defaultlistmodel.removeAllElements();
        String as[] = getDateFormateArray();
        for(int i = 0; i < as.length; i++)
            defaultlistmodel.addElement(as[i]);

        JScrollPane jscrollpane = new JScrollPane(patternList);
        jscrollpane.setPreferredSize(new Dimension(100, 120));
        jpanel3.add(jscrollpane, "Center");
        patternList.addListSelectionListener(patternListSelectionListener);
        jpanel.add(initStartEndDatePane(), "South");
        return jpanel;
    }

    private String[] getDateFormateArray()
    {
        return FormatField.getInstance().getDateFormatArray();
    }

    protected JPanel initStartEndDatePane()
    {
        JPanel jpanel = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        jpanel.add(new UILabel((new StringBuilder()).append(Inter.getLocText("FS_Start_Date")).append(":").toString()));
        startDv = ValueEditorPaneFactory.createDateValueEditorPane(null, null);
        jpanel.add(startDv);
        jpanel.add(new UILabel((new StringBuilder()).append(Inter.getLocText("FS_End_Date")).append(":").toString()));
        endDv = ValueEditorPaneFactory.createDateValueEditorPane(null, null);
        jpanel.add(endDv);
        return jpanel;
    }

    private void refreshPreviewLabel()
    {
        String s = patternTextField.getText();
        if(s != null && s.length() > 0)
            try
            {
                SimpleDateFormat simpledateformat = new SimpleDateFormat(s);
                String s1 = simpledateformat.format(new Date());
                Color color = Color.black;
                if(!ArrayUtils.contains(FormatField.getInstance().getDateFormatArray(), s))
                {
                    s1 = (new StringBuilder()).append(s1).append(" ").append(Inter.getLocText("DateFormat-Custom_Warning")).toString();
                    color = Color.red;
                }
                sampleLabel.setText(s1);
                sampleLabel.setForeground(color);
            }
            catch(Exception exception)
            {
                sampleLabel.setForeground(Color.red);
                sampleLabel.setText(exception.getMessage());
            }
        else
            sampleLabel.setText((new Date()).toString());
    }

    protected void populateSubDirectWriteEditorBean(DateEditor dateeditor)
    {
        String s = dateeditor.getFormatText();
        patternTextField.setText(s);
        returnTypeComboBox.setSelectedIndex(dateeditor.isReturnDate() ? 1 : 0);
        populateStartEnd(dateeditor);
        DefaultListModel defaultlistmodel = (DefaultListModel)patternList.getModel();
        if(s == null || s.length() <= 0)
        {
            patternList.setSelectedIndex(0);
        } else
        {
            for(int i = 0; i < defaultlistmodel.size(); i++)
                if(ComparatorUtils.equals(defaultlistmodel.getElementAt(i).toString(), s))
                {
                    patternList.setSelectedIndex(i);
                    return;
                }

        }
    }

    protected DateEditor updateSubDirectWriteEditorBean()
    {
        DateEditor dateeditor = new DateEditor();
        dateeditor.setFormatText(getSimpleDateFormat().toPattern());
        dateeditor.setReturnDate(returnTypeComboBox.getSelectedIndex() == 1);
        updateStartEnd(dateeditor);
        return dateeditor;
    }

    public void populateStartEnd(DateEditor dateeditor)
    {
        Formula formula = dateeditor.getStartDateFM();
        Formula formula1 = dateeditor.getEndDateFM();
        if(formula != null)
        {
            startDv.populate(formula);
        } else
        {
            String s = dateeditor.getStartText();
            startDv.populate(StringUtils.isEmpty(s) ? null : ((Object) (DateUtils.string2Date(s, true))));
        }
        if(formula1 != null)
        {
            endDv.populate(formula1);
        } else
        {
            String s1 = dateeditor.getEndText();
            endDv.populate(StringUtils.isEmpty(s1) ? null : ((Object) (DateUtils.string2Date(s1, true))));
        }
    }

    public void updateStartEnd(DateEditor dateeditor)
    {
        Object obj = startDv.update();
        Object obj1 = endDv.update();
        Object obj2 = null;
        if(obj instanceof Formula)
        {
            Calculator calculator = Calculator.createCalculator();
            Formula formula = (Formula)obj;
            try
            {
                formula.setResult(calculator.evalValue(formula.getContent()));
            }
            catch(UtilEvalError utilevalerror)
            {
                FRContext.getLogger().error(utilevalerror.getMessage(), utilevalerror);
            }
            obj = formula.getResult();
            dateeditor.setStartDateFM(formula);
            dateeditor.setStartText(null);
        } else
        {
            try
            {
                dateeditor.setStartText(obj != null ? DateUtils.getDate2Str("MM/dd/yyyy", (Date)obj) : "");
            }
            catch(ClassCastException classcastexception) { }
        }
        if(obj1 instanceof Formula)
        {
            Calculator calculator1 = Calculator.createCalculator();
            Formula formula1 = (Formula)obj1;
            try
            {
                formula1.setResult(calculator1.evalValue(formula1.getContent()));
            }
            catch(UtilEvalError utilevalerror1)
            {
                FRContext.getLogger().error(utilevalerror1.getMessage(), utilevalerror1);
            }
            obj1 = formula1.getResult();
            dateeditor.setEndDateFM(formula1);
            dateeditor.setEndText(null);
        } else
        {
            try
            {
                dateeditor.setEndText(obj1 != null ? DateUtils.getDate2Str("MM/dd/yyyy", (Date)obj1) : "");
            }
            catch(ClassCastException classcastexception1) { }
        }
    }

    private SimpleDateFormat getSimpleDateFormat()
    {
        String s = patternTextField.getText();
        SimpleDateFormat simpledateformat;
        if(s != null && s.length() > 0)
            try
            {
                simpledateformat = new SimpleDateFormat(patternTextField.getText());
                sampleLabel.setText(simpledateformat.format(new Date()));
            }
            catch(Exception exception)
            {
                simpledateformat = new SimpleDateFormat("");
            }
        else
            simpledateformat = new SimpleDateFormat("");
        return simpledateformat;
    }

    protected volatile DirectWriteEditor updateSubDirectWriteEditorBean()
    {
        return updateSubDirectWriteEditorBean();
    }

    protected volatile void populateSubDirectWriteEditorBean(DirectWriteEditor directwriteeditor)
    {
        populateSubDirectWriteEditorBean((DateEditor)directwriteeditor);
    }



}
