// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.poly.hanlder;

import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.poly.PolyDesigner;
import com.fr.poly.PolyUtils;
import com.fr.poly.creator.BlockCreator;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DataEditingListener extends MouseAdapter
{

    private PolyDesigner designer;

    public DataEditingListener(PolyDesigner polydesigner)
    {
        designer = polydesigner;
    }

    public void mousePressed(MouseEvent mouseevent)
    {
        if(!designer.isFocusOwner())
            designer.requestFocus();
        designer.stopEditing();
        int i = mouseevent.getX() + designer.getHorizontalValue();
        int j = mouseevent.getY() + designer.getVerticalValue();
        BlockCreator blockcreator = PolyUtils.searchAt(designer, i, j);
        designer.setSelection(blockcreator);
        DesignerContext.getDesignerFrame().resetToolkitByPlus(DesignerContext.getDesignerFrame().getSelectedJTemplate());
    }

    public void mouseMoved(MouseEvent mouseevent)
    {
        if(designer.getSelection() != null)
            designer.getSelection().checkButtonEnable();
    }
}
