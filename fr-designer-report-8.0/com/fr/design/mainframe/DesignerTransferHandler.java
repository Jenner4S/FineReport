// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.design.designer.beans.events.CreatorEventListenerTable;
import com.fr.design.designer.beans.models.AddingModel;
import java.awt.datatransfer.Transferable;
import javax.swing.JComponent;
import javax.swing.TransferHandler;

// Referenced classes of package com.fr.design.mainframe:
//            FormDesigner

public class DesignerTransferHandler extends TransferHandler
{

    private FormDesigner designer;
    private AddingModel addingModel;

    public DesignerTransferHandler(FormDesigner formdesigner, AddingModel addingmodel)
    {
        super("rootComponent");
        designer = formdesigner;
        addingModel = addingmodel;
    }

    protected void exportDone(JComponent jcomponent, Transferable transferable, int i)
    {
        if(!addingModel.isCreatorAdded())
            designer.getEditListenerTable().fireCreatorModified(3);
    }
}
