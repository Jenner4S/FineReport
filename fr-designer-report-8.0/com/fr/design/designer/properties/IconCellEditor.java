// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.properties;

import com.fr.design.mainframe.widget.accessibles.AccessibleIconEditor;

// Referenced classes of package com.fr.design.designer.properties:
//            DelegateEditor

public class IconCellEditor extends DelegateEditor
{

    public IconCellEditor()
    {
        final AccessibleIconEditor iconEditor = new AccessibleIconEditor();
        editorComponent = iconEditor;
        _flddelegate = new DelegateEditor.EditorDelegate() {

            final AccessibleIconEditor val$iconEditor;
            final IconCellEditor this$0;

            public void setValue(Object obj)
            {
                iconEditor.setValue(obj);
            }

            public Object getCellEditorValue()
            {
                return iconEditor.getValue();
            }

            
            {
                this$0 = IconCellEditor.this;
                iconEditor = accessibleiconeditor;
                super(IconCellEditor.this);
            }
        }
;
        iconEditor.addChangeListener(_flddelegate);
    }
}
