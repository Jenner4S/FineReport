// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator;

import com.fr.form.ui.FieldEditor;
import java.awt.Dimension;
import javax.swing.JComponent;

// Referenced classes of package com.fr.design.designer.creator:
//            XFieldEditor, XWScaleLayout, XLayoutContainer

public abstract class XWrapperedFieldEditor extends XFieldEditor
{

    public XWrapperedFieldEditor(FieldEditor fieldeditor, Dimension dimension)
    {
        super(fieldeditor, dimension);
    }

    protected abstract JComponent initEditor();

    protected abstract String getIconName();

    protected XLayoutContainer getCreatorWrapper(String s)
    {
        return new XWScaleLayout();
    }

    protected void addToWrapper(XLayoutContainer xlayoutcontainer, int i, int j)
    {
        setSize(i, j);
        xlayoutcontainer.add(this);
    }

    public boolean shouldScaleCreator()
    {
        return true;
    }
}
