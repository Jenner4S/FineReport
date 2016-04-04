// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.dscolumn;

import com.fr.data.TableDataSource;
import com.fr.design.condition.DSColumnLiteConditionPane;
import com.fr.design.condition.DSColumnSimpleLiteConditionPane;
import com.fr.design.data.DesignTableDataManager;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.general.data.Condition;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.cellattr.core.group.DSColumn;
import javax.swing.JPanel;

public class DSColumnConditionsPane extends BasicPane
{

    private DSColumnLiteConditionPane liteConditionPane;
    private UICheckBox reselectExpandCheckBox;
    private static final String INSET_TEXT = "      ";

    public DSColumnConditionsPane()
    {
        this(2);
    }

    public DSColumnConditionsPane(int i)
    {
        setLayout(FRGUIPaneFactory.createM_BorderLayout());
        if(i > 1)
            liteConditionPane = new DSColumnLiteConditionPane() {

                final DSColumnConditionsPane this$0;

                protected boolean needDoWithCondition(Condition condition)
                {
                    return condition != null;
                }

            
            {
                this$0 = DSColumnConditionsPane.this;
                super();
            }
            }
;
        else
            liteConditionPane = new DSColumnSimpleLiteConditionPane();
        add(liteConditionPane, "Center");
        if(i > 1)
        {
            JPanel jpanel = FRGUIPaneFactory.createX_AXISBoxInnerContainer_S_Pane();
            jpanel.add(new UILabel("      "));
            reselectExpandCheckBox = new UICheckBox(Inter.getLocText("BindColumn-Extend_the_conditions_of_fatherCell(Applied_to_the_data_contains_other_data)"), false);
            jpanel.add(reselectExpandCheckBox);
            reselectExpandCheckBox.setSelected(true);
            JPanel jpanel1 = GUICoreUtils.createFlowPane(jpanel, 0);
            add(jpanel1, "North");
            jpanel1.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("BindColumn-The_Conditions_of_FatherCell"), null));
        }
    }

    public String title4PopupWindow()
    {
        return Inter.getLocText("Filter");
    }

    public void populate(TableDataSource tabledatasource, CellElement cellelement)
    {
        if(cellelement == null)
            return;
        Object obj = cellelement.getValue();
        if(obj == null || !(obj instanceof DSColumn))
            return;
        DSColumn dscolumn = (DSColumn)obj;
        if(reselectExpandCheckBox != null)
            reselectExpandCheckBox.setSelected(!dscolumn.isReselect());
        String as[] = DesignTableDataManager.getSelectedColumnNames(tabledatasource, dscolumn.getDSName());
        liteConditionPane.populateColumns(as);
        liteConditionPane.populateBean(dscolumn.getCondition());
    }

    public void update(CellElement cellelement)
    {
        if(cellelement == null)
            return;
        Object obj = cellelement.getValue();
        if(obj == null || !(obj instanceof DSColumn))
            return;
        DSColumn dscolumn = (DSColumn)obj;
        dscolumn.setCondition(liteConditionPane.updateBean());
        if(reselectExpandCheckBox != null)
            dscolumn.setReselect(!reselectExpandCheckBox.isSelected());
    }
}
