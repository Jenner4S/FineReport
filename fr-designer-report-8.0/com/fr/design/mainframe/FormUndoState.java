// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.form.main.Form;
import com.fr.form.ui.Widget;
import java.awt.Dimension;

// Referenced classes of package com.fr.design.mainframe:
//            BaseUndoState, BaseJForm, FormArea, FormDesigner, 
//            FormSelection

public class FormUndoState extends BaseUndoState
{

    private Form form;
    private Dimension designerSize;
    private int hValue;
    private int vValue;
    private Widget selectWidgets[];
    private double widthValue;
    private double heightValue;
    private double slideValue;

    public FormUndoState(BaseJForm basejform, FormArea formarea)
    {
        super(basejform);
        try
        {
            form = (Form)((Form)basejform.getTarget()).clone();
        }
        catch(CloneNotSupportedException clonenotsupportedexception)
        {
            throw new RuntimeException(clonenotsupportedexception);
        }
        selectWidgets = formarea.getFormEditor().getSelectionModel().getSelection().getSelectedWidgets();
        hValue = formarea.getHorizontalValue();
        vValue = formarea.getVerticalValue();
        designerSize = formarea.getAreaSize();
        widthValue = formarea.getWidthPaneValue();
        heightValue = formarea.getHeightPaneValue();
        slideValue = formarea.getSlideValue();
    }

    public Form getForm()
    {
        return form;
    }

    public Widget[] getSelectWidgets()
    {
        return selectWidgets;
    }

    public Dimension getAreaSize()
    {
        return designerSize;
    }

    public int getHorizontalValue()
    {
        return hValue;
    }

    public int getVerticalValue()
    {
        return vValue;
    }

    public double getWidthValue()
    {
        return widthValue;
    }

    public double getHeightValue()
    {
        return heightValue;
    }

    public double getSlideValue()
    {
        return slideValue;
    }

    public void applyState()
    {
        ((BaseJForm)getApplyTarget()).applyUndoState4Form(this);
    }
}
