// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.condition;

import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.general.Inter;
import com.fr.report.cell.cellattr.highlight.HighlightAction;
import com.fr.report.cell.cellattr.highlight.PageHighlightAction;

// Referenced classes of package com.fr.design.condition:
//            ConditionAttrSingleConditionPane, ConditionAttributesPane

public class PagePane extends ConditionAttrSingleConditionPane
{

    private UILabel pageLabel;
    private UIComboBox pageComboBox;

    public PagePane(ConditionAttributesPane conditionattributespane)
    {
        super(conditionattributespane);
        pageLabel = new UILabel((new StringBuilder()).append(Inter.getLocText("FR-Designer_Pagination")).append(":").toString());
        add(pageLabel);
        pageComboBox = new UIComboBox(new String[] {
            Inter.getLocText("Utils-No_Pagination"), Inter.getLocText("CellWrite-Page_After_Row"), Inter.getLocText("CellWrite-Page_Before_Row"), Inter.getLocText("CellWrite-Page_After_Column"), Inter.getLocText("CellWrite-Page_Before_Column")
        });
        add(pageComboBox);
        pageComboBox.setSelectedIndex(0);
    }

    public String nameForPopupMenuItem()
    {
        return Inter.getLocText("FR-Designer_Pagination");
    }

    protected String title4PopupWindow()
    {
        return nameForPopupMenuItem();
    }

    public void populate(HighlightAction highlightaction)
    {
        pageComboBox.setSelectedIndex(((PageHighlightAction)highlightaction).getPage());
    }

    public HighlightAction update()
    {
        return new PageHighlightAction(pageComboBox.getSelectedIndex());
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
