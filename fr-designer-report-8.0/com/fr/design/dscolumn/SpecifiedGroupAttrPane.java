// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.dscolumn;

import com.fr.base.Formula;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.condition.DSColumnLiteConditionPane;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.formula.CustomVariableResolver;
import com.fr.design.formula.FormulaFactory;
import com.fr.design.formula.UIFormula;
import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.gui.controlpane.ObjectJControlPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ilist.ModNameActionListener;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.general.NameObject;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.cellattr.core.group.ConditionGroup;
import com.fr.report.cell.cellattr.core.group.CustomGrouper;
import com.fr.report.cell.cellattr.core.group.FunctionGrouper;
import com.fr.report.cell.cellattr.core.group.RecordGrouper;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class SpecifiedGroupAttrPane extends BasicPane
{
    private class FormulaGroupPane extends JPanel
    {

        private String displayModeNames[] = {
            Inter.getLocText("GROUPING_MODE"), Inter.getLocText("LIST_MODE"), Inter.getLocText("CONTINUUM_MODE")
        };
        private String InsertText;
        private UIComboBox modeComboBox;
        private UITextField valueField;
        private JPanel southPane;
        final SpecifiedGroupAttrPane this$0;

        public void populate(RecordGrouper recordgrouper)
        {
            if(recordgrouper instanceof FunctionGrouper)
            {
                int i = ((FunctionGrouper)recordgrouper).getDivideMode();
                if(i == 0)
                    modeComboBox.setSelectedIndex(0);
                else
                if(i == 1)
                    modeComboBox.setSelectedIndex(1);
                else
                    modeComboBox.setSelectedIndex(2);
                String s = ((FunctionGrouper)recordgrouper).getFormulaContent();
                if(s == null)
                    valueField.setText("$$$");
                else
                    valueField.setText(s);
            }
        }

        public RecordGrouper update()
        {
            FunctionGrouper functiongrouper = new FunctionGrouper();
            functiongrouper.setCustom(true);
            if(modeComboBox.getSelectedIndex() == 0)
                functiongrouper.setDivideMode(0);
            else
            if(modeComboBox.getSelectedIndex() == 1)
                functiongrouper.setDivideMode(1);
            else
            if(modeComboBox.getSelectedIndex() == 2)
                functiongrouper.setDivideMode(2);
            functiongrouper.setFormulaContent(valueField.getText());
            return functiongrouper;
        }


        public FormulaGroupPane()
        {
            this$0 = SpecifiedGroupAttrPane.this;
            super();
            InsertText = "    ";
            setBorder(BorderFactory.createTitledBorder(Inter.getLocText("D-Dispaly_Divide_Result_Set_into_Groups")));
            setLayout(FRGUIPaneFactory.createM_BorderLayout());
            JPanel jpanel = FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(1);
            add(jpanel, "North");
            JPanel jpanel1 = FRGUIPaneFactory.createBorderLayout_S_Pane();
            jpanel1.setLayout(FRGUIPaneFactory.createBorderLayout());
            jpanel.add(jpanel1);
            modeComboBox = new UIComboBox(displayModeNames);
            jpanel1.add(GUICoreUtils.createFlowPane(new JComponent[] {
                new UILabel(InsertText), new UILabel((new StringBuilder()).append(Inter.getLocText("Display_Modes")).append(":  ").toString()), modeComboBox
            }, 0), "West");
            UILabel uilabel = new UILabel("=");
            uilabel.setFont(new Font("Dialog", 1, 12));
            valueField = new UITextField(16);
            valueField.setText("$$$");
            UIButton uibutton = new UIButton("...");
            uibutton.setToolTipText((new StringBuilder()).append(Inter.getLocText("Formula")).append("...").toString());
            uibutton.setPreferredSize(new Dimension(25, valueField.getPreferredSize().height));
            uibutton.addActionListener(new ActionListener() {

                final SpecifiedGroupAttrPane val$this$0;
                final FormulaGroupPane this$1;

                public void actionPerformed(ActionEvent actionevent)
                {
                    Formula formula = new Formula();
                    String s = valueField.getText();
                    if(s == null || s.length() <= 0)
                        formula.setContent("$$$");
                    else
                        formula.setContent(s);
                    final UIFormula formulaPane = FormulaFactory.createFormulaPane();
                    formulaPane.populate(formula, new CustomVariableResolver(displayNames != null ? displayNames : new String[0], true));
                    formulaPane.showLargeWindow(SwingUtilities.getWindowAncestor(FormulaGroupPane.this), new DialogActionAdapter() {

                        final UIFormula val$formulaPane;
                        final _cls1 this$2;

                        public void doOk()
                        {
                            Formula formula1 = formulaPane.update();
                            if(formula1.getContent().length() <= 1)
                                valueField.setText("$$$");
                            else
                                valueField.setText(formula1.getContent().substring(1));
                        }

                        
                        {
                            this$2 = _cls1.this;
                            formulaPane = uiformula;
                            super();
                        }
                    }
).setVisible(true);
                }


// JavaClassFileOutputException: Invalid index accessing method local variables table of <init>
            }
);
            southPane = GUICoreUtils.createFlowPane(new JComponent[] {
                new UILabel(InsertText), new UILabel((new StringBuilder()).append(Inter.getLocText(new String[] {
                    "Custom", "Value"
                })).append(": ").toString()), uilabel, valueField, uibutton
            }, 0);
            jpanel.add(southPane);
        }
    }

    public static class ConditionGroupDetailsPane extends BasicBeanPane
    {

        private ConditionGroup editing;
        private DSColumnLiteConditionPane liteConditionPane;

        protected String title4PopupWindow()
        {
            return Inter.getLocText("SpecifiedG-Specified_Group");
        }

        public ConditionGroup updateBean()
        {
            editing.setCondition(liteConditionPane.updateBean());
            return editing;
        }

        public void populateBean(ConditionGroup conditiongroup)
        {
            editing = conditiongroup;
            liteConditionPane.populateBean(conditiongroup.getCondition());
        }

        public volatile Object updateBean()
        {
            return updateBean();
        }

        public volatile void populateBean(Object obj)
        {
            populateBean((ConditionGroup)obj);
        }

        public ConditionGroupDetailsPane(String as[])
        {
            setLayout(FRGUIPaneFactory.createBorderLayout());
            setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
            liteConditionPane = new DSColumnLiteConditionPane();
            add(liteConditionPane, "Center");
            if(as != null)
                liteConditionPane.populateColumns(as);
        }
    }

    public class SpecifiedGroupControlPane extends ObjectJControlPane
    {

        final SpecifiedGroupAttrPane this$0;

        public NameableCreator[] createNameableCreators()
        {
            return (new NameableCreator[] {
                new NameObjectCreator(Inter.getLocText("Condition"), com/fr/report/cell/cellattr/core/group/ConditionGroup, com/fr/design/dscolumn/SpecifiedGroupAttrPane$ConditionGroupDetailsPane)
            });
        }

        protected String title4PopupWindow()
        {
            return Inter.getLocText("SpecifiedG-Specified_Group");
        }


        public SpecifiedGroupControlPane(String as[])
        {
            this$0 = SpecifiedGroupAttrPane.this;
            super(as);
            addModNameActionListener(new ModNameActionListener() {

                final SpecifiedGroupAttrPane val$this$0;
                final SpecifiedGroupControlPane this$1;

                public void nameModed(int i, String s, String s1)
                {
                    populateSelectedValue();
                }


// JavaClassFileOutputException: Invalid index accessing method local variables table of <init>
            }
);
        }
    }


    private CardLayout cardLayout;
    private JPanel centerCardPane;
    private JPanel conditionsGroupPane;
    private FormulaGroupPane formulaGroupPane;
    private UIComboBox specifiedComboBox;
    private SpecifiedGroupControlPane specifiedControlPane;
    private String displayNames[];
    private UICheckBox forceCheckBox;
    private UICheckBox moreCheckBox;
    private UIComboBox otherComboBox;
    private UITextField otherTextField;
    ItemListener otherItemListener;

    public SpecifiedGroupAttrPane(String as[])
    {
        otherItemListener = new ItemListener() {

            final SpecifiedGroupAttrPane this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                if(otherComboBox.getSelectedIndex() == 2)
                    otherTextField.setEnabled(true);
                else
                    otherTextField.setEnabled(false);
            }

            
            {
                this$0 = SpecifiedGroupAttrPane.this;
                super();
            }
        }
;
        displayNames = as;
        initComponents();
    }

    protected void initComponents()
    {
        setLayout(FRGUIPaneFactory.createBorderLayout());
        specifiedComboBox = new UIComboBox(new String[] {
            Inter.getLocText(new String[] {
                "Condition", "Group"
            }), Inter.getLocText(new String[] {
                "Formula", "Group"
            })
        });
        specifiedComboBox.addItemListener(new ItemListener() {

            final SpecifiedGroupAttrPane this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                if(specifiedComboBox.getSelectedIndex() == 0)
                    cardLayout.show(centerCardPane, "Condition");
                else
                    cardLayout.show(centerCardPane, "Formula");
            }

            
            {
                this$0 = SpecifiedGroupAttrPane.this;
                super();
            }
        }
);
        JPanel jpanel = GUICoreUtils.createFlowPane(new JComponent[] {
            new UILabel((new StringBuilder()).append(Inter.getLocText("Select_Specified_Grouping")).append(":").toString()), specifiedComboBox
        }, 0);
        add(jpanel, "North");
        cardLayout = new CardLayout();
        centerCardPane = FRGUIPaneFactory.createCardLayout_S_Pane();
        centerCardPane.setLayout(cardLayout);
        conditionsGroupPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        specifiedControlPane = new SpecifiedGroupControlPane(displayNames);
        conditionsGroupPane.add(specifiedControlPane, "Center");
        JPanel jpanel1 = FRGUIPaneFactory.createMediumHGapFlowInnerContainer_M_Pane();
        conditionsGroupPane.add(jpanel1, "South");
        forceCheckBox = new UICheckBox(Inter.getLocText("SpecifiedG-Force_Group"));
        moreCheckBox = new UICheckBox(Inter.getLocText("one_record_exists_in_many_groups"));
        jpanel1.add(forceCheckBox);
        jpanel1.add(moreCheckBox);
        otherComboBox = new UIComboBox(new String[] {
            Inter.getLocText("SpecifiedG-Discard_all_others"), Inter.getLocText("SpecifiedG-Leave_in_their_own_groups"), Inter.getLocText("SpecifiedG-Put_all_others_together")
        });
        otherComboBox.addItemListener(otherItemListener);
        UILabel uilabel = new UILabel((new StringBuilder()).append(Inter.getLocText("OtherGroup_Name")).append(":").toString());
        otherTextField = new UITextField(8);
        jpanel1.add(otherComboBox);
        jpanel1.add(GUICoreUtils.createFlowPane(new Component[] {
            uilabel, otherTextField
        }, 0));
        formulaGroupPane = new FormulaGroupPane();
        centerCardPane.add("Condition", conditionsGroupPane);
        centerCardPane.add("Formula", formulaGroupPane);
        add(centerCardPane, "Center");
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("SpecifiedG-Specified_Group");
    }

    public void checkValid()
        throws Exception
    {
    }

    public void populate(RecordGrouper recordgrouper)
    {
        if(recordgrouper == null)
            return;
        if(recordgrouper instanceof CustomGrouper)
        {
            specifiedComboBox.setSelectedIndex(0);
            cardLayout.show(centerCardPane, "Condition");
            CustomGrouper customgrouper = (CustomGrouper)recordgrouper;
            boolean flag = customgrouper.isForce();
            ConditionGroup aconditiongroup[] = customgrouper.getConditionGroups();
            boolean flag1 = customgrouper.isMore();
            int i = customgrouper.getOther();
            String s = customgrouper.getOtherdisplay();
            forceCheckBox.setSelected(flag);
            moreCheckBox.setSelected(flag1);
            if(i == 0)
            {
                otherComboBox.setSelectedIndex(2);
                otherTextField.setEnabled(true);
            } else
            if(i == 1)
            {
                otherComboBox.setSelectedIndex(0);
                otherTextField.setEnabled(false);
            } else
            {
                otherComboBox.setSelectedIndex(1);
                otherTextField.setEnabled(false);
            }
            if(s != null)
                otherTextField.setText(s);
            if(aconditiongroup != null)
            {
                ArrayList arraylist = new ArrayList();
                for(int j = 0; j < aconditiongroup.length; j++)
                    arraylist.add(new NameObject(aconditiongroup[j].getDisplay(), aconditiongroup[j]));

                specifiedControlPane.populate((com.fr.stable.Nameable[])arraylist.toArray(new NameObject[arraylist.size()]));
            }
        } else
        if((recordgrouper instanceof FunctionGrouper) && ((FunctionGrouper)recordgrouper).isCustom())
        {
            specifiedComboBox.setSelectedIndex(1);
            cardLayout.show(centerCardPane, "Formula");
            formulaGroupPane.populate(recordgrouper);
        }
        if(otherComboBox.getSelectedIndex() == 2)
            otherTextField.setEnabled(true);
        else
            otherTextField.setEnabled(false);
    }

    public RecordGrouper update(CellElement cellelement, RecordGrouper recordgrouper)
    {
        if(specifiedComboBox.getSelectedIndex() == 0)
        {
            CustomGrouper customgrouper = new CustomGrouper();
            if(forceCheckBox.isSelected())
                customgrouper.setForce(true);
            if(!moreCheckBox.isSelected())
                customgrouper.setMore(false);
            if(otherComboBox.getSelectedIndex() == 2)
                customgrouper.setOther(0);
            else
            if(otherComboBox.getSelectedIndex() == 0)
                customgrouper.setOther(1);
            else
                customgrouper.setOther(2);
            customgrouper.setOdisplay(otherTextField.getText());
            com.fr.stable.Nameable anameable[] = specifiedControlPane.update();
            NameObject anameobject[] = new NameObject[anameable.length];
            Arrays.asList(anameable).toArray(anameobject);
            ConditionGroup aconditiongroup[] = new ConditionGroup[anameable.length];
            for(int i = 0; i < anameable.length; i++)
            {
                aconditiongroup[i] = (ConditionGroup)anameobject[i].getObject();
                aconditiongroup[i].setDisplay(anameobject[i].getName());
            }

            customgrouper.setConditionGroups(aconditiongroup);
            recordgrouper = customgrouper;
        } else
        {
            recordgrouper = formulaGroupPane.update();
        }
        return recordgrouper;
    }






}
