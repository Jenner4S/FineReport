// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.renderer;

import java.awt.Component;
import java.beans.PropertyEditor;

// Referenced classes of package com.fr.design.mainframe.widget.renderer:
//            GenericCellRenderer

public class PropertyCellRenderer extends GenericCellRenderer
{

    private PropertyEditor editor;

    public PropertyCellRenderer(PropertyEditor propertyeditor)
    {
        editor = propertyeditor;
    }

    public void setValue(Object obj)
    {
        editor.setValue(obj);
    }

    public Component getRendererComponent()
    {
        return editor.getCustomEditor();
    }
}
