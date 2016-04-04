// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.edit.order;

import com.fr.base.BaseUtils;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.general.Inter;
import com.fr.report.cell.FloatElement;
import com.fr.report.elementcase.ElementCase;

// Referenced classes of package com.fr.design.actions.edit.order:
//            AbstractFloatElementOrderAction

public class SendFloatElementToBackAction extends AbstractFloatElementOrderAction
{

    public SendFloatElementToBackAction(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
        setName(Inter.getLocText("M_Edit-Send_to_Back"));
        setMnemonic('K');
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_edit/sendToBack.png"));
    }

    public void orderWithSelectedFloatElement(ElementCase elementcase, FloatElement floatelement)
    {
        elementcase.sendFloatElementToBack(floatelement);
    }
}
