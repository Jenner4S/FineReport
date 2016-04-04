// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.condition;

import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.gui.ilable.UILabel;
import com.fr.general.Inter;
import com.fr.report.cell.cellattr.highlight.HighlightAction;
import com.fr.report.cell.cellattr.highlight.ValueHighlightAction;

// Referenced classes of package com.fr.design.condition:
//            ConditionAttrSingleConditionPane, ConditionAttributesPane

public class NewRealValuePane extends ConditionAttrSingleConditionPane
{

    private ValueEditorPane valueEditor;

    public NewRealValuePane(ConditionAttributesPane conditionattributespane)
    {
        super(conditionattributespane);
        add(new UILabel((new StringBuilder()).append(Inter.getLocText("FR-Designer_New_Value")).append(":").toString()));
        valueEditor = ValueEditorPaneFactory.createBasicValueEditorPane();
        add(valueEditor);
    }

    public String nameForPopupMenuItem()
    {
        return Inter.getLocText("FR-Designer_New_Value");
    }

    protected String title4PopupWindow()
    {
        return nameForPopupMenuItem();
    }

    public void populate(HighlightAction highlightaction)
    {
        valueEditor.populate(((ValueHighlightAction)highlightaction).getValue());
    }

    public HighlightAction update()
    {
        return new ValueHighlightAction(valueEditor.update());
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
