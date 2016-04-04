// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.grid.selection;

import com.fr.base.FRContext;
import com.fr.design.cell.clipboard.*;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.selection.SelectableElement;
import com.fr.general.FRLogger;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.stable.ColumnRow;
import com.fr.stable.FCloneable;
import java.io.Serializable;
import javax.swing.JPopupMenu;

// Referenced classes of package com.fr.grid.selection:
//            FloatSelection

public abstract class Selection
    implements FCloneable, Serializable, SelectableElement
{

    public Selection()
    {
    }

    public abstract boolean isSelectedOneCell(ElementCasePane elementcasepane);

    public abstract void asTransferable(ElementsTransferable elementstransferable, ElementCasePane elementcasepane);

    public boolean pasteFloatElementClip(FloatElementsClip floatelementsclip, ElementCasePane elementcasepane)
    {
        FloatElementsClip floatelementsclip1;
        try
        {
            floatelementsclip1 = (FloatElementsClip)floatelementsclip.clone();
        }
        catch(CloneNotSupportedException clonenotsupportedexception)
        {
            FRContext.getLogger().error(clonenotsupportedexception.getMessage(), clonenotsupportedexception);
            return false;
        }
        TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        FloatSelection floatselection = floatelementsclip1.pasteAt(templateelementcase);
        if(floatselection != null)
            elementcasepane.setSelection(floatselection);
        return true;
    }

    public abstract boolean pasteCellElementsClip(CellElementsClip cellelementsclip, ElementCasePane elementcasepane);

    public abstract boolean pasteString(String s, ElementCasePane elementcasepane);

    public abstract boolean pasteOtherType(Object obj, ElementCasePane elementcasepane);

    public abstract boolean canMergeCells(ElementCasePane elementcasepane);

    public abstract boolean mergeCells(ElementCasePane elementcasepane);

    public abstract boolean canUnMergeCells(ElementCasePane elementcasepane);

    public abstract boolean unMergeCells(ElementCasePane elementcasepane);

    public abstract JPopupMenu createPopupMenu(ElementCasePane elementcasepane);

    public abstract boolean clear(com.fr.design.mainframe.ElementCasePane.Clear clear1, ElementCasePane elementcasepane);

    public abstract int[] getSelectedRows();

    public abstract int[] getSelectedColumns();

    public abstract void moveLeft(ElementCasePane elementcasepane);

    public abstract void moveRight(ElementCasePane elementcasepane);

    public abstract void moveUp(ElementCasePane elementcasepane);

    public abstract void moveDown(ElementCasePane elementcasepane);

    public abstract boolean triggerDeleteAction(ElementCasePane elementcasepane);

    public abstract boolean containsColumnRow(ColumnRow columnrow);

    public Selection clone()
        throws CloneNotSupportedException
    {
        return (Selection)super.clone();
    }

    public volatile Object clone()
        throws CloneNotSupportedException
    {
        return clone();
    }
}
