// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.cell;

import com.fr.base.BaseUtils;
import com.fr.design.dialog.BasicPane;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.menu.MenuKeySet;
import com.fr.design.present.ConditionAttributesGroupPane;
import com.fr.report.cell.TemplateCellElement;

// Referenced classes of package com.fr.design.actions.cell:
//            AbstractCellElementAction

public class ConditionAttributesAction extends AbstractCellElementAction
{

    public ConditionAttributesAction(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
        setMenuKeySet(KeySetUtils.CONDITION_ATTR);
        setName(getMenuKeySet().getMenuKeySetName());
        setMnemonic(getMenuKeySet().getMnemonic());
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_format/highlight.png"));
    }

    protected BasicPane populateBasicPane(TemplateCellElement templatecellelement)
    {
        ConditionAttributesGroupPane conditionattributesgrouppane = new ConditionAttributesGroupPane();
        conditionattributesgrouppane.populate(templatecellelement.getHighlightGroup());
        return conditionattributesgrouppane;
    }

    protected void updateBasicPane(BasicPane basicpane, TemplateCellElement templatecellelement)
    {
        templatecellelement.setHighlightGroup(((ConditionAttributesGroupPane)basicpane).updateHighlightGroup());
    }
}
