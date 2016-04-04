// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans.actions;

import com.fr.base.BaseUtils;
import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.designer.creator.XWidgetCreator;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.FormSelection;
import com.fr.general.Inter;

// Referenced classes of package com.fr.design.designer.beans.actions:
//            FormUndoableAction

public class ChangeNameAction extends FormUndoableAction
{

    public ChangeNameAction(FormDesigner formdesigner)
    {
        super(formdesigner);
        setName(Inter.getLocText("Form-Change_Widget_Name"));
        setMnemonic('G');
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/control/refresh.png"));
    }

    public boolean executeActionReturnUndoRecordNeeded()
    {
        FormDesigner formdesigner = (FormDesigner)getEditingComponent();
        if(formdesigner == null)
            return false;
        XWidgetCreator xwidgetcreator = (XWidgetCreator)formdesigner.getSelectionModel().getSelection().getSelectedCreator();
        if(xwidgetcreator == null)
        {
            return false;
        } else
        {
            xwidgetcreator.ChangeCreatorName(formdesigner, xwidgetcreator);
            return false;
        }
    }
}
