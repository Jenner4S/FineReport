// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.design.designer.beans.events.CreatorEventListenerTable;
import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.designer.creator.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

// Referenced classes of package com.fr.design.mainframe:
//            FormDesigner, FormSelection

public class FormEditorKeyListener extends KeyAdapter
{

    private FormDesigner designer;
    private boolean moved;

    public FormEditorKeyListener(FormDesigner formdesigner)
    {
        designer = formdesigner;
    }

    public void keyPressed(KeyEvent keyevent)
    {
        int i = keyevent.getKeyCode();
        XCreator xcreator = designer.getSelectionModel().getSelection().getSelectedCreator();
        XLayoutContainer xlayoutcontainer;
        if(xcreator == null || (xlayoutcontainer = XCreatorUtils.getParentXLayoutContainer(xcreator)) == null || !(xlayoutcontainer instanceof XWAbsoluteLayout))
            return;
        moved = true;
        switch(i)
        {
        case 37: // '%'
            designer.getSelectionModel().move(-1, 0);
            break;

        case 39: // '\''
            designer.getSelectionModel().move(1, 0);
            break;

        case 38: // '&'
            designer.getSelectionModel().move(0, -1);
            break;

        case 40: // '('
            designer.getSelectionModel().move(0, 1);
            break;

        default:
            moved = false;
            break;
        }
    }

    public void keyReleased(KeyEvent keyevent)
    {
        if(moved)
        {
            designer.getEditListenerTable().fireCreatorModified(designer.getSelectionModel().getSelection().getSelectedCreator(), 6);
            moved = false;
        }
    }
}
