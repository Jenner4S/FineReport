// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.poly.creator;

import com.fr.design.designer.TargetComponent;
import com.fr.design.menu.*;
import com.fr.design.selection.QuickEditor;
import com.fr.poly.JPolyBlockPane;
import com.fr.poly.PolyDesigner;
import com.fr.report.poly.*;
import com.fr.stable.unit.UnitRectangle;
import javax.swing.JComponent;

// Referenced classes of package com.fr.poly.creator:
//            BlockCreator, ECBlockEditor, ECBlockPane, BlockEditor, 
//            PolyElementCasePane

public class ECBlockCreator extends BlockCreator
{

    private ECBlockEditor editor;

    public ECBlockCreator()
    {
    }

    public ECBlockCreator(PolyECBlock polyecblock)
    {
        super(polyecblock);
    }

    protected JComponent initMonitor()
    {
        return new JPolyBlockPane((PolyECBlock)block);
    }

    public PolyECBlock getValue()
    {
        return (PolyECBlock)block;
    }

    public PolyDesigner getDesigner()
    {
        return designer;
    }

    public void setDesigner(PolyDesigner polydesigner)
    {
        designer = polydesigner;
    }

    public UnitRectangle getDefaultBlockBounds()
    {
        return PolyCoreUtils.getDefaultBlockBounds();
    }

    public BlockEditor getEditor()
    {
        if(editor == null)
            editor = new ECBlockEditor(designer, this);
        return editor;
    }

    public void checkButtonEnable()
    {
    }

    public void setValue(PolyECBlock polyecblock)
    {
        polyecblock.setWorksheet((PolyWorkSheet)designer.getTarget());
        cal(polyecblock);
        repaint();
    }

    public ToolBarDef[] toolbars4Target()
    {
        return editor.createEffective().toolbars4Target();
    }

    public JComponent[] toolBarButton4Form()
    {
        return editor.createEffective().toolBarButton4Form();
    }

    public MenuDef[] menus4Target()
    {
        return editor.createEffective().menus4Target();
    }

    public int getMenuState()
    {
        return 0;
    }

    public ShortCut[] shortcut4TemplateMenu()
    {
        return editor.createEffective().shortcut4TemplateMenu();
    }

    public PolyElementCasePane getEditingElementCasePane()
    {
        return editor.createEffective();
    }

    public QuickEditor getQuickEditor(TargetComponent targetcomponent)
    {
        return editor.createEffective().getCurrentEditor();
    }

    public volatile void setValue(TemplateBlock templateblock)
    {
        setValue((PolyECBlock)templateblock);
    }

    public volatile TemplateBlock getValue()
    {
        return getValue();
    }
}
