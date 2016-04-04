// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.roleAuthority.*;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.main.impl.WorkBook;
import com.fr.privilege.finegrain.WorkSheetPrivilegeControl;
import com.fr.report.report.TemplateReport;
import com.fr.report.worksheet.WorkSheet;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.mainframe:
//            AuthorityEditPane, JTemplate

public class SheetAuthorityEditPane extends AuthorityEditPane
{

    private static final int TOP_GAP = 11;
    private static final int LEFT_GAP = 8;
    private static final int ALIGNMENT_GAP = -3;
    private UICheckBox sheetVisible;
    private WorkBook workBook;
    private int selectedIndex;
    private ItemListener itemListener;

    public SheetAuthorityEditPane(WorkBook workbook, int i)
    {
        super(HistoryTemplateListPane.getInstance().getCurrentEditingTemplate());
        sheetVisible = new UICheckBox(Inter.getLocText("Widget-Visible"));
        workBook = null;
        selectedIndex = -1;
        itemListener = new ItemListener() {

            final SheetAuthorityEditPane this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                String s = ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName();
                if(ComparatorUtils.equals(s, Inter.getLocText("Role")))
                    return;
                if(s == null)
                    return;
                WorkSheetPrivilegeControl worksheetprivilegecontrol = workBook.getTemplateReport(selectedIndex).getWorkSheetPrivilegeControl();
                if(sheetVisible.isSelected())
                    worksheetprivilegecontrol.removeInvisibleRole(s);
                else
                    worksheetprivilegecontrol.addInvisibleRole(s);
                JTemplate jtemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
                jtemplate.setSheetCovered(!sheetVisible.isSelected());
                jtemplate.fireTargetModified();
                jtemplate.refreshContainer();
                RolesAlreadyEditedPane.getInstance().refreshDockingView();
            }

            
            {
                this$0 = SheetAuthorityEditPane.this;
                super();
            }
        }
;
        setLayout(new BorderLayout());
        type = new UILabel();
        name = new UILabel();
        checkPane = new JPanel();
        checkPane.setLayout(new BorderLayout());
        add(layoutText(), "West");
        add(layoutPane(), "Center");
        setBorder(BorderFactory.createEmptyBorder(11, 8, 0, 0));
        workBook = workbook;
        sheetVisible.addItemListener(itemListener);
        selectedIndex = i;
    }

    private JPanel layoutText()
    {
        double d = -2D;
        Component acomponent[][] = {
            {
                new UILabel((new StringBuilder()).append(Inter.getLocText("Type")).append(":").toString(), 4)
            }, {
                new UILabel((new StringBuilder()).append(Inter.getLocText("WF-Name")).append(":").toString(), 4)
            }, {
                new UILabel((new StringBuilder()).append(Inter.getLocText("DashBoard-Potence")).append(":").toString(), 4)
            }
        };
        double ad[] = {
            d, d, d
        };
        double ad1[] = {
            d
        };
        int ai[][] = {
            {
                1
            }, {
                1
            }, {
                1
            }
        };
        return TableLayoutHelper.createGapTableLayoutPane(acomponent, ad, ad1, ai, 6D, 6D);
    }

    private JPanel layoutPane()
    {
        double d = -1D;
        double d1 = -2D;
        Component acomponent[][] = {
            {
                type
            }, {
                name
            }, {
                checkPane
            }
        };
        double ad[] = {
            d1, d1, d1
        };
        double ad1[] = {
            d
        };
        int ai[][] = {
            {
                1
            }, {
                1
            }, {
                1
            }
        };
        return TableLayoutHelper.createGapTableLayoutPane(acomponent, ad, ad1, ai, 6D, 6D);
    }

    public void populateDetials()
    {
        if(workBook.getTemplateReport(selectedIndex) instanceof WorkSheet)
            ((WorkSheet)workBook.getTemplateReport(selectedIndex)).setPaintSelection(false);
        populateName();
        populateType();
        checkPane.removeAll();
        if(name.getText() == "")
        {
            return;
        } else
        {
            checkPane.add(populateCheckPane(), "Center");
            checkPane.setBorder(BorderFactory.createEmptyBorder(-3, 0, 0, 0));
            checkVisibleCheckBoxes();
            return;
        }
    }

    public void populateType()
    {
        type.setText("sheet");
    }

    public void populateName()
    {
        name.setText(workBook.getReportName(selectedIndex));
    }

    public JPanel populateCheckPane()
    {
        double d = -1D;
        double d1 = -2D;
        Component acomponent[][] = {
            {
                sheetVisible
            }
        };
        double ad[] = {
            d1
        };
        double ad1[] = {
            d
        };
        int ai[][] = {
            {
                1
            }
        };
        return TableLayoutHelper.createGapTableLayoutPane(acomponent, ad, ad1, ai, 6D, 6D);
    }

    private void checkVisibleCheckBoxes()
    {
        sheetVisible.removeItemListener(itemListener);
        String s = ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName();
        if(s == null)
            sheetVisible.setSelected(true);
        sheetVisible.setSelected(!workBook.getTemplateReport(selectedIndex).getWorkSheetPrivilegeControl().checkInvisible(s));
        sheetVisible.addItemListener(itemListener);
    }



}
