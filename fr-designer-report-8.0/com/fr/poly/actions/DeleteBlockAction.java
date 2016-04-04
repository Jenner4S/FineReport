// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.poly.actions;

import com.fr.base.BaseUtils;
import com.fr.design.actions.TemplateComponentActionInterface;
import com.fr.design.actions.UpdateAction;
import com.fr.design.designer.TargetComponent;
import com.fr.general.Inter;
import com.fr.poly.PolyDesigner;
import java.awt.event.ActionEvent;
import javax.swing.KeyStroke;

public class DeleteBlockAction extends UpdateAction
    implements TemplateComponentActionInterface
{

    private PolyDesigner pd;

    public DeleteBlockAction(PolyDesigner polydesigner)
    {
        pd = polydesigner;
        setName(Inter.getLocText("M_Edit-Delete"));
        setMnemonic('D');
        setAccelerator(KeyStroke.getKeyStroke(127, 0));
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_report/delete.png"));
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        if(BaseUtils.isAuthorityEditing())
            return;
        PolyDesigner polydesigner = getEditingComponent();
        if(polydesigner == null)
            return;
        if(polydesigner.delete())
            polydesigner.fireTargetModified();
    }

    public PolyDesigner getEditingComponent()
    {
        return pd;
    }

    public volatile TargetComponent getEditingComponent()
    {
        return getEditingComponent();
    }
}
