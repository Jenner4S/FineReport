// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.dscolumn;

import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icombobox.FunctionComboBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.cell.cellattr.CellExpandAttr;
import com.fr.report.cell.cellattr.core.group.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.dscolumn:
//            ResultSetGroupPane

public class ResultSetGroupDockingPane extends ResultSetGroupPane
{

    private static final int BIND_GROUP = 0;
    private static final int BIND_SELECTED = 1;
    private static final int BIND_SUMMARY = 2;
    private UIButton advancedButton;
    private FunctionComboBox functionComboBox;
    private JPanel cardPane;
    private CardLayout cardLayout;
    private UIComboBox goBox;
    ItemListener l;

    public ResultSetGroupDockingPane(ElementCasePane elementcasepane)
    {
        initComponents(elementcasepane);
    }

    public void initComponents(ElementCasePane elementcasepane)
    {
        goBox = new UIComboBox(new String[] {
            Inter.getLocText("BindColumn-Group"), Inter.getLocText("BindColumn-Select"), Inter.getLocText("BindColumn-Summary")
        });
        initCardPane();
        JPanel jpanel = layoutPane();
        setLayout(new BorderLayout());
        add(jpanel, "Center");
    }

    private JPanel layoutPane()
    {
        double d = 4D;
        double d1 = 6D;
        double d2 = -2D;
        double d3 = -1D;
        Component acomponent[][] = {
            {
                new UILabel(Inter.getLocText("Data_Setting")), goBox
            }, {
                cardPane, null
            }
        };
        goBox.addItemListener(new ItemListener() {

            final ResultSetGroupDockingPane this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                checkButtonEnabled();
                int i = goBox.getSelectedIndex();
                if(i == 0)
                    cardLayout.show(cardPane, "groupPane");
                else
                if(i == 1)
                    cardLayout.show(cardPane, "listPane");
                else
                if(i == 2)
                {
                    cardLayout.show(cardPane, "summaryPane");
                    CellExpandAttr cellexpandattr = cellElement.getCellExpandAttr();
                    cellexpandattr.setDirection((byte)2);
                }
            }

            
            {
                this$0 = ResultSetGroupDockingPane.this;
                super();
            }
        }
);
        double ad[] = {
            d2, d3
        };
        double ad1[] = {
            d2, d2
        };
        return TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
    }

    private void initCardPane()
    {
        cardPane = FRGUIPaneFactory.createCardLayout_S_Pane();
        cardLayout = new CardLayout();
        cardPane.setLayout(cardLayout);
        JPanel jpanel = new JPanel(new BorderLayout(3, 0));
        groupComboBox.addItemListener(new ItemListener() {

            final ResultSetGroupDockingPane this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                checkButtonEnabled();
            }

            
            {
                this$0 = ResultSetGroupDockingPane.this;
                super();
            }
        }
);
        advancedButton = new UIButton(Inter.getLocText("Custom"));
        advancedButton.addActionListener(groupAdvancedListener);
        jpanel.add(groupComboBox, "West");
        jpanel.add(advancedButton, "Center");
        cardPane.add(jpanel, "groupPane");
        cardPane.add(new JPanel(), "listPane");
        cardPane.add(functionComboBox = new FunctionComboBox(GUICoreUtils.getFunctionArray()), "summaryPane");
    }

    public void populate(TemplateCellElement templatecellelement)
    {
        cellElement = templatecellelement;
        if(isNPE(templatecellelement))
            return;
        DSColumn dscolumn = (DSColumn)templatecellelement.getValue();
        recordGrouper = dscolumn.getGrouper();
        if((recordGrouper instanceof FunctionGrouper) && !((FunctionGrouper)recordGrouper).isCustom())
        {
            int i = ((FunctionGrouper)recordGrouper).getDivideMode();
            if(i == 0)
            {
                cardLayout.show(cardPane, "groupPane");
                goBox.setSelectedIndex(0);
                groupComboBox.setSelectedIndex(0);
            } else
            if(i == 2)
            {
                cardLayout.show(cardPane, "groupPane");
                goBox.setSelectedIndex(0);
                groupComboBox.setSelectedIndex(1);
            } else
            if(i == 1)
            {
                cardLayout.show(cardPane, "listPane");
                goBox.setSelectedIndex(1);
            }
        } else
        if((recordGrouper instanceof FunctionGrouper) && ((FunctionGrouper)recordGrouper).isCustom())
        {
            cardLayout.show(cardPane, "groupPane");
            goBox.setSelectedIndex(0);
            groupComboBox.setSelectedIndex(2);
        } else
        if(recordGrouper instanceof SummaryGrouper)
        {
            cardLayout.show(cardPane, "summaryPane");
            goBox.setSelectedIndex(2);
            functionComboBox.setFunction(((SummaryGrouper)recordGrouper).getFunction());
        } else
        if(recordGrouper instanceof CustomGrouper)
        {
            cardLayout.show(cardPane, "groupPane");
            goBox.setSelectedIndex(0);
            groupComboBox.setSelectedIndex(2);
        }
        checkButtonEnabled();
    }

    public void update()
    {
        if(isNPE(cellElement))
            return;
        DSColumn dscolumn = (DSColumn)cellElement.getValue();
        if(goBox.getSelectedIndex() == 0)
            recordGrouper = updateGroupCombox();
        else
        if(goBox.getSelectedIndex() == 1)
        {
            FunctionGrouper functiongrouper = new FunctionGrouper();
            functiongrouper.setDivideMode(1);
            functiongrouper.setCustom(false);
            recordGrouper = functiongrouper;
        } else
        if(goBox.getSelectedIndex() == 2)
        {
            SummaryGrouper summarygrouper = new SummaryGrouper();
            summarygrouper.setFunction(functionComboBox.getFunction());
            recordGrouper = summarygrouper;
        }
        dscolumn.setGrouper(recordGrouper);
    }

    private void checkButtonEnabled()
    {
        advancedButton.setEnabled(false);
        functionComboBox.setEnabled(false);
        groupComboBox.setEnabled(false);
        if(goBox.getSelectedIndex() == 2)
            functionComboBox.setEnabled(true);
        if(goBox.getSelectedIndex() == 0)
        {
            groupComboBox.setEnabled(true);
            if(groupComboBox.getSelectedIndex() == 2)
                advancedButton.setEnabled(true);
        }
    }

    public void addListener(ItemListener itemlistener)
    {
        goBox.addItemListener(itemlistener);
        groupComboBox.addItemListener(itemlistener);
        functionComboBox.addItemListener(itemlistener);
        l = itemlistener;
    }

    void fireTargetChanged()
    {
        l.itemStateChanged(null);
    }

    public void setRecordGrouper(RecordGrouper recordgrouper)
    {
        recordGrouper = recordgrouper;
    }




}
