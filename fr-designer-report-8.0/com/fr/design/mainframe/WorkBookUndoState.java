// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.design.designer.EditingState;
import com.fr.main.impl.WorkBook;

// Referenced classes of package com.fr.design.mainframe:
//            BaseUndoState, JWorkBook

public class WorkBookUndoState extends BaseUndoState
{

    private WorkBook wb;
    private int selectedReportIndex;
    private EditingState editingState;

    public WorkBookUndoState(JWorkBook jworkbook, int i, EditingState editingstate)
    {
        super(jworkbook);
        try
        {
            wb = (WorkBook)((WorkBook)jworkbook.getTarget()).clone();
        }
        catch(CloneNotSupportedException clonenotsupportedexception)
        {
            throw new RuntimeException(clonenotsupportedexception);
        }
        selectedReportIndex = i;
        editingState = editingstate;
    }

    public WorkBook getWorkBook()
    {
        return wb;
    }

    public int getSelectedReportIndex()
    {
        return selectedReportIndex;
    }

    public EditingState getSelectedEditingState()
    {
        return editingState;
    }

    public void applyState()
    {
        ((JWorkBook)getApplyTarget()).applyUndoState(this);
    }
}
