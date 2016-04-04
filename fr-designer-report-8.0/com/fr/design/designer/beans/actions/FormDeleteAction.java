// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans.actions;

import com.fr.base.BaseUtils;
import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.FormSelection;
import com.fr.general.Inter;
import javax.swing.KeyStroke;

// Referenced classes of package com.fr.design.designer.beans.actions:
//            FormUndoableAction

public class FormDeleteAction extends FormUndoableAction
{

    public FormDeleteAction(FormDesigner formdesigner)
    {
        super(formdesigner);
        setName(Inter.getLocText("M_Edit-Delete"));
        setMnemonic('D');
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_report/delete.png"));
        setAccelerator(KeyStroke.getKeyStroke(127, 0));
    }

    public boolean executeActionReturnUndoRecordNeeded()
    {
        FormDesigner formdesigner = (FormDesigner)getEditingComponent();
        if(formdesigner == null)
        {
            return false;
        } else
        {
            FormSelection formselection = formdesigner.getSelectionModel().getSelection();
            XCreator xcreator = formselection.getSelectedCreator();
            formdesigner.getSelectionModel().deleteSelection();
            xcreator.deleteRelatedComponent(xcreator, formdesigner);
            return false;
        }
    }

    public void update()
    {
        setEnabled(true);
    }
}
