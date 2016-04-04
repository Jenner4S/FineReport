// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.condition;

import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.style.color.ColorSelectBox;
import com.fr.general.Inter;
import com.fr.report.cell.cellattr.highlight.ForegroundHighlightAction;
import com.fr.report.cell.cellattr.highlight.HighlightAction;
import java.awt.Color;

// Referenced classes of package com.fr.design.condition:
//            ConditionAttrSingleConditionPane, ConditionAttributesPane

public class ForeGroundPane extends ConditionAttrSingleConditionPane
{

    private UILabel foregroundLabel;
    private ColorSelectBox foregroundColorPane;
    private UIComboBox foreScopeComboBox;

    public ForeGroundPane(ConditionAttributesPane conditionattributespane)
    {
        super(conditionattributespane);
        foregroundLabel = new UILabel((new StringBuilder()).append(Inter.getLocText("FR-Designer_Foreground")).append(":").toString());
        foregroundColorPane = new ColorSelectBox(80);
        add(foregroundLabel);
        add(foregroundColorPane);
        foreScopeComboBox = new UIComboBox(new String[] {
            Inter.getLocText("Utils-Current_Cell"), Inter.getLocText("Utils-Current_Row"), Inter.getLocText("Utils-Current_Column")
        });
        add(foreScopeComboBox);
        foregroundColorPane.setSelectObject(Color.black);
    }

    public String nameForPopupMenuItem()
    {
        return Inter.getLocText("FR-Designer_Foreground");
    }

    protected String title4PopupWindow()
    {
        return nameForPopupMenuItem();
    }

    public void populate(HighlightAction highlightaction)
    {
        foregroundColorPane.setSelectObject(((ForegroundHighlightAction)highlightaction).getForegroundColor());
        foreScopeComboBox.setSelectedIndex(((ForegroundHighlightAction)highlightaction).getScope());
    }

    public HighlightAction update()
    {
        return new ForegroundHighlightAction(foregroundColorPane.getSelectObject(), foreScopeComboBox.getSelectedIndex());
    }

    public volatile Object update()
    {
        return update();
    }

    public volatile void populate(Object obj)
    {
        populate((HighlightAction)obj);
    }
}
