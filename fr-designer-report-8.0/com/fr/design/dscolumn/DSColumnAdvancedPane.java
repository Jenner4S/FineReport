// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.dscolumn;

import com.fr.base.Formula;
import com.fr.data.util.SortOrder;
import com.fr.design.data.DesignTableDataManager;
import com.fr.design.dialog.*;
import com.fr.design.formula.*;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.SortOrderComboBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.cell.cellattr.CellExpandAttr;
import com.fr.report.cell.cellattr.core.group.DSColumn;
import com.fr.report.cell.cellattr.core.group.SelectCount;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import javax.swing.*;

public class DSColumnAdvancedPane extends BasicPane
{
    private static class ValuePane extends JPanel
    {

        private JFormulaField formulaField;

        public void populate(CellElement cellelement)
        {
            if(cellelement == null)
                return;
            Object obj = cellelement.getValue();
            if(obj == null || !(obj instanceof DSColumn))
                return;
            DSColumn dscolumn = (DSColumn)obj;
            String s = dscolumn.getResult();
            if(s == null)
                s = "$$$";
            formulaField.populateElement(cellelement);
            formulaField.populate(s);
        }

        public void update(CellElement cellelement)
        {
            if(cellelement == null)
                return;
            Object obj = cellelement.getValue();
            if(obj == null || !(obj instanceof DSColumn))
            {
                return;
            } else
            {
                DSColumn dscolumn = (DSColumn)(DSColumn)cellelement.getValue();
                dscolumn.setResult(formulaField.getFormulaText());
                return;
            }
        }

        public ValuePane()
        {
            setLayout(FRGUIPaneFactory.createBoxFlowLayout());
            add(new UILabel((new StringBuilder()).append(" ").append(Inter.getLocText("Value")).append(":").toString()));
            add(Box.createHorizontalStrut(2));
            add(formulaField = new JFormulaField("$$$"));
        }
    }

    private static class JFormulaField extends JPanel
    {

        private CellElement cellElement;
        private UITextField formulaTextField;
        private String defaultValue;
        private ActionListener formulaButtonActionListener;

        public void populate(String s)
        {
            formulaTextField.setText(s);
        }

        public void populateElement(CellElement cellelement)
        {
            cellElement = cellelement;
        }

        public String getFormulaText()
        {
            return formulaTextField.getText();
        }




        public JFormulaField(String s)
        {
            formulaButtonActionListener = new ActionListener() {

                final JFormulaField this$0;

                public void actionPerformed(ActionEvent actionevent)
                {
                    Formula formula = new Formula();
                    String s1 = formulaTextField.getText();
                    if(s1 == null || s1.length() <= 0)
                        formula.setContent(defaultValue);
                    else
                        formula.setContent(s1);
                    final UIFormula formulaPane = FormulaFactory.createFormulaPaneWhenReserveFormula();
                    if(cellElement == null)
                        return;
                    Object obj = cellElement.getValue();
                    if(obj == null || !(obj instanceof DSColumn))
                    {
                        return;
                    } else
                    {
                        DSColumn dscolumn = (DSColumn)obj;
                        String as[] = DesignTableDataManager.getSelectedColumnNames(DesignTableDataManager.getEditingTableDataSource(), dscolumn.getDSName());
                        formulaPane.populate(formula, new CustomVariableResolver(as, true));
                        formulaPane.showLargeWindow(SwingUtilities.getWindowAncestor(JFormulaField.this), new DialogActionAdapter() {

                            final UIFormula val$formulaPane;
                            final _cls1 this$1;

                            public void doOk()
                            {
                                Formula formula1 = formulaPane.update();
                                if(formula1.getContent().length() <= 1)
                                    formulaTextField.setText(defaultValue);
                                else
                                    formulaTextField.setText(formula1.getContent().substring(1));
                            }

                        
                        {
                            this$1 = _cls1.this;
                            formulaPane = uiformula;
                            super();
                        }
                        }
).setVisible(true);
                        return;
                    }
                }

                
                {
                    this$0 = JFormulaField.this;
                    super();
                }
            }
;
            defaultValue = s;
            setLayout(FRGUIPaneFactory.createBoxFlowLayout());
            UILabel uilabel = new UILabel("=");
            uilabel.setFont(new Font("Dialog", 1, 12));
            add(uilabel);
            formulaTextField = new UITextField(24);
            add(formulaTextField);
            formulaTextField.setText(s);
            UIButton uibutton = new UIButton("...");
            add(uibutton);
            uibutton.setToolTipText((new StringBuilder()).append(Inter.getLocText("Formula")).append("...").toString());
            uibutton.setPreferredSize(new Dimension(25, formulaTextField.getPreferredSize().height));
            uibutton.addActionListener(formulaButtonActionListener);
        }
    }

    private static class SelectCountPane extends JPanel
    {

        CellElement cellElement;
        private UIComboBox selectCountComboBox;
        private JPanel selectCountCardPane;
        private UITextField serialTextField;
        JFormulaField topFormulaPane;
        JFormulaField bottomFormulaPane;

        public void populate(CellElement cellelement)
        {
            if(cellelement == null)
                return;
            cellElement = cellelement;
            Object obj = cellelement.getValue();
            if(obj == null || !(obj instanceof DSColumn))
                return;
            DSColumn dscolumn = (DSColumn)(DSColumn)cellelement.getValue();
            SelectCount selectcount = dscolumn.getSelectCount();
            topFormulaPane.populateElement(cellelement);
            bottomFormulaPane.populateElement(cellelement);
            if(selectcount != null)
            {
                int i = selectcount.getType();
                selectCountComboBox.setSelectedIndex(i);
                if(i == 1)
                    topFormulaPane.populate(selectcount.getFormulaCount());
                else
                if(i == 2)
                    bottomFormulaPane.populate(selectcount.getFormulaCount());
                else
                if(i == 5)
                    serialTextField.setText(selectcount.getSerial());
            }
        }

        public void update(CellElement cellelement)
        {
            if(cellelement == null)
                return;
            Object obj = cellelement.getValue();
            if(obj == null || !(obj instanceof DSColumn))
                return;
            DSColumn dscolumn = (DSColumn)(DSColumn)cellelement.getValue();
            int i = selectCountComboBox.getSelectedIndex();
            if(i == 0)
            {
                dscolumn.setSelectCount(null);
            } else
            {
                SelectCount selectcount = new SelectCount();
                dscolumn.setSelectCount(selectcount);
                selectcount.setType(i);
                if(i == 1)
                    selectcount.setFormulaCount(topFormulaPane.getFormulaText());
                else
                if(i == 2)
                    selectcount.setFormulaCount(bottomFormulaPane.getFormulaText());
                else
                if(i == 5)
                    selectcount.setSerial(serialTextField.getText());
            }
        }

        private JFormattedTextField getTextField(JSpinner jspinner)
        {
            JComponent jcomponent = jspinner.getEditor();
            if(jcomponent instanceof javax.swing.JSpinner.DefaultEditor)
            {
                return ((javax.swing.JSpinner.DefaultEditor)jcomponent).getTextField();
            } else
            {
                System.err.println((new StringBuilder()).append("Unexpected editor type: ").append(jspinner.getEditor().getClass()).append(" isn't a descendant of DefaultEditor").toString());
                return null;
            }
        }



        public SelectCountPane()
        {
            setLayout(FRGUIPaneFactory.createBorderLayout());
            selectCountComboBox = new UIComboBox(new String[] {
                Inter.getLocText("Undefined"), Inter.getLocText("BindColumn-Top_N"), Inter.getLocText("BindColumn-Bottom_N"), Inter.getLocText("Odd"), Inter.getLocText("Even"), Inter.getLocText("Specify")
            });
            selectCountComboBox.addActionListener(new ActionListener() {

                final SelectCountPane this$0;

                public void actionPerformed(ActionEvent actionevent)
                {
                    int i = selectCountComboBox.getSelectedIndex();
                    CardLayout cardlayout = (CardLayout)selectCountCardPane.getLayout();
                    if(i == 1)
                        cardlayout.show(selectCountCardPane, "TOP");
                    else
                    if(i == 2)
                        cardlayout.show(selectCountCardPane, "BOTTOM");
                    else
                    if(i == 3)
                        cardlayout.show(selectCountCardPane, "ODD");
                    else
                    if(i == 4)
                        cardlayout.show(selectCountCardPane, "EVEN");
                    else
                    if(i == 5)
                        cardlayout.show(selectCountCardPane, "SPECIFY");
                    else
                        cardlayout.show(selectCountCardPane, "UNDEFINE");
                }

                
                {
                    this$0 = SelectCountPane.this;
                    super();
                }
            }
);
            selectCountCardPane = FRGUIPaneFactory.createCardLayout_S_Pane();
            add(GUICoreUtils.createFlowPane(new JComponent[] {
                new UILabel(" "), selectCountComboBox, new UILabel(" "), selectCountCardPane
            }, 0), "West");
            JPanel jpanel = GUICoreUtils.createFlowPane(new UILabel(Inter.getLocText("Undefined")), 0);
            topFormulaPane = new JFormulaField("-1");
            bottomFormulaPane = new JFormulaField("-1");
            serialTextField = new UITextField(18);
            JPanel jpanel1 = GUICoreUtils.createFlowPane(new UILabel((new StringBuilder()).append(Inter.getLocText("BindColumn-Result_Serial_Number_Start_From_1")).append("  ").append(Inter.getLocText("BindColumn-Odd_Selected_(1,3,5...)")).toString()), 0);
            JPanel jpanel2 = GUICoreUtils.createFlowPane(new UILabel((new StringBuilder()).append(Inter.getLocText("BindColumn-Result_Serial_Number_Start_From_1")).append("  ").append(Inter.getLocText("BindColumn-Even_Selected_(2,4,6...)")).toString()), 0);
            JPanel jpanel3 = GUICoreUtils.createFlowPane(new JComponent[] {
                serialTextField, new UILabel(Inter.getLocText(new String[] {
                    "Format", "BindColumn-Result_Serial_Number_Start_From_1", "Inner_Parameter", "Group_Count"
                }, new String[] {
                    ": 1,2-3,5,8  ", ",", "$__count__"
                }))
            }, 0);
            serialTextField.setToolTipText((new StringBuilder()).append(Inter.getLocText("StyleFormat-Sample")).append(":=JOINARRAY(GREPARRAY(RANGE($__count__), item!=4), \",\")").toString());
            selectCountCardPane.add(jpanel, "UNDEFINE");
            selectCountCardPane.add(topFormulaPane, "TOP");
            selectCountCardPane.add(bottomFormulaPane, "BOTTOM");
            selectCountCardPane.add(jpanel1, "ODD");
            selectCountCardPane.add(jpanel2, "EVEN");
            selectCountCardPane.add(jpanel3, "SPECIFY");
        }
    }

    private static class SortPane extends SortFormulaPane
    {

        private CellElement cellElement;

        public void formulaAction()
        {
            if(cellElement == null)
                return;
            Object obj = cellElement.getValue();
            if(obj == null || !(obj instanceof DSColumn))
            {
                return;
            } else
            {
                String as[] = DesignTableDataManager.getSelectedColumnNames(DesignTableDataManager.getEditingTableDataSource(), ((DSColumn)obj).getDSName());
                showFormulaDialog(as);
                return;
            }
        }

        void populate(CellElement cellelement)
        {
            if(cellelement == null)
                return;
            cellElement = cellelement;
            Object obj = cellelement.getValue();
            if(obj == null || !(obj instanceof DSColumn))
                return;
            DSColumn dscolumn = (DSColumn)obj;
            int i = dscolumn.getOrder();
            sortOrderComboBox.setSortOrder(new SortOrder(i));
            String s = dscolumn.getSortFormula();
            if(s != null && s.length() >= 1)
                sortFormulaTextField.setText(s);
            else
                sortFormulaTextField.setText(s);
        }

        public void update(CellElement cellelement)
        {
            if(cellelement == null)
                return;
            Object obj = cellelement.getValue();
            if(obj == null || !(obj instanceof DSColumn))
                return;
            DSColumn dscolumn = (DSColumn)(DSColumn)cellelement.getValue();
            dscolumn.setOrder(sortOrderComboBox.getSortOrder().getOrder());
            String s = null;
            if(sortFormulaTextField.getText() != null && !sortFormulaTextField.getText().trim().equals("") && !sortFormulaTextField.getText().trim().equals("$$$"))
                s = new String(sortFormulaTextField.getText());
            if(s != null && s.length() >= 1)
                dscolumn.setSortFormula(s);
            else
                dscolumn.setSortFormula(null);
        }

        private SortPane()
        {
        }

    }


    private static final String InsetText = " ";
    private SortPane sortPane;
    private SelectCountPane selectCountPane;
    private ValuePane valuePane;
    private UICheckBox horizontalExtendableCheckBox;
    private UICheckBox verticalExtendableCheckBox;
    private UICheckBox useMultiplyNumCheckBox;
    private UISpinner multiNumSpinner;

    public DSColumnAdvancedPane()
    {
        this(2);
    }

    public DSColumnAdvancedPane(int i)
    {
        setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        setLayout(FRGUIPaneFactory.createBorderLayout());
        sortPane = new SortPane();
        sortPane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("Sort-Sort_Order"), null));
        if(i > 1)
        {
            selectCountPane = new SelectCountPane();
            selectCountPane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("BindColumn-Results_Filter"), null));
        }
        valuePane = new ValuePane();
        valuePane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("BindColumn-Custom_Data_Appearance"), null));
        JPanel jpanel = null;
        if(i > 1)
        {
            JPanel jpanel1 = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
            jpanel1.add(horizontalExtendableCheckBox = new UICheckBox(Inter.getLocText("ExpandD-Horizontal_Extendable")));
            jpanel1.add(verticalExtendableCheckBox = new UICheckBox(Inter.getLocText("ExpandD-Vertical_Extendable")));
            jpanel = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("ExpandD-Expandable"));
            jpanel.setLayout(new BorderLayout());
            jpanel.add(jpanel1, "Center");
        }
        JPanel jpanel2 = null;
        if(i > 1)
        {
            jpanel2 = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("Fill_blank_Data"));
            useMultiplyNumCheckBox = new UICheckBox(Inter.getLocText("Column_Multiple"));
            jpanel2.add(useMultiplyNumCheckBox);
            jpanel2.add(new UILabel(" "));
            multiNumSpinner = new UISpinner(1.0D, 10000D, 1.0D, 1.0D);
            jpanel2.add(multiNumSpinner);
            useMultiplyNumCheckBox.addActionListener(new ActionListener() {

                final DSColumnAdvancedPane this$0;

                public void actionPerformed(ActionEvent actionevent)
                {
                    checkButtonEnabled();
                }

            
            {
                this$0 = DSColumnAdvancedPane.this;
                super();
            }
            }
);
        }
        double ad[] = {
            -2D, -2D, -2D, -2D, -2D, -2D
        };
        double ad1[] = {
            -1D
        };
        Component acomponent[][] = (Component[][])null;
        if(i > 1)
            acomponent = (new Component[][] {
                new Component[] {
                    sortPane
                }, new Component[] {
                    selectCountPane
                }, new Component[] {
                    valuePane
                }, new Component[] {
                    jpanel
                }, new Component[] {
                    jpanel2
                }
            });
        else
            acomponent = (new Component[][] {
                new Component[] {
                    sortPane
                }, new Component[] {
                    valuePane
                }
            });
        add(TableLayoutHelper.createTableLayoutPane(acomponent, ad, ad1), "Center");
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("Advanced");
    }

    public void populate(TemplateCellElement templatecellelement)
    {
        if(templatecellelement == null)
            return;
        sortPane.populate(templatecellelement);
        valuePane.populate(templatecellelement);
        if(selectCountPane != null)
        {
            selectCountPane.populate(templatecellelement);
            CellExpandAttr cellexpandattr = templatecellelement.getCellExpandAttr();
            if(cellexpandattr == null)
            {
                cellexpandattr = new CellExpandAttr();
                templatecellelement.setCellExpandAttr(cellexpandattr);
            }
            switch(cellexpandattr.getExtendable())
            {
            case 0: // '\0'
                horizontalExtendableCheckBox.setSelected(true);
                verticalExtendableCheckBox.setSelected(true);
                break;

            case 1: // '\001'
                horizontalExtendableCheckBox.setSelected(false);
                verticalExtendableCheckBox.setSelected(true);
                break;

            case 2: // '\002'
                horizontalExtendableCheckBox.setSelected(true);
                verticalExtendableCheckBox.setSelected(false);
                break;

            default:
                horizontalExtendableCheckBox.setSelected(false);
                verticalExtendableCheckBox.setSelected(false);
                break;
            }
            if(cellexpandattr.getMultipleNumber() == -1)
            {
                useMultiplyNumCheckBox.setSelected(false);
            } else
            {
                useMultiplyNumCheckBox.setSelected(true);
                multiNumSpinner.setValue(cellexpandattr.getMultipleNumber());
            }
            checkButtonEnabled();
        }
    }

    public void update(TemplateCellElement templatecellelement)
    {
        if(templatecellelement == null)
            return;
        sortPane.update(templatecellelement);
        valuePane.update(templatecellelement);
        if(selectCountPane != null)
        {
            selectCountPane.update(templatecellelement);
            CellExpandAttr cellexpandattr = templatecellelement.getCellExpandAttr();
            if(cellexpandattr == null)
            {
                cellexpandattr = new CellExpandAttr();
                templatecellelement.setCellExpandAttr(cellexpandattr);
            }
            if(horizontalExtendableCheckBox.isSelected())
            {
                if(verticalExtendableCheckBox.isSelected())
                    cellexpandattr.setExtendable((byte)0);
                else
                    cellexpandattr.setExtendable((byte)2);
            } else
            if(verticalExtendableCheckBox.isSelected())
                cellexpandattr.setExtendable((byte)1);
            else
                cellexpandattr.setExtendable((byte)3);
            if(useMultiplyNumCheckBox.isSelected())
                cellexpandattr.setMultipleNumber((int)multiNumSpinner.getValue());
            else
                cellexpandattr.setMultipleNumber(-1);
        }
    }

    private void checkButtonEnabled()
    {
        if(useMultiplyNumCheckBox.isSelected())
            multiNumSpinner.setEnabled(true);
        else
            multiNumSpinner.setEnabled(false);
    }

}
