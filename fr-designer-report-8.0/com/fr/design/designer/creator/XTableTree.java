// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator;

import com.fr.design.mainframe.widget.editors.DictionaryEditor;
import com.fr.design.mainframe.widget.renderer.DictionaryRenderer;
import com.fr.form.ui.TableTree;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import java.awt.Dimension;
import java.beans.IntrospectionException;
import javax.swing.*;

// Referenced classes of package com.fr.design.designer.creator:
//            XWidgetCreator, CRPropertyDescriptor

public class XTableTree extends XWidgetCreator
{

    public XTableTree(TableTree tabletree, Dimension dimension)
    {
        super(tabletree, dimension);
    }

    public CRPropertyDescriptor[] supportedDescriptor()
        throws IntrospectionException
    {
        return (CRPropertyDescriptor[])(CRPropertyDescriptor[])ArrayUtils.addAll(super.supportedDescriptor(), new CRPropertyDescriptor[] {
            (new CRPropertyDescriptor("dictionary", data.getClass())).setI18NName(Inter.getLocText("DS-Dictionary")).setEditorClass(com/fr/design/mainframe/widget/editors/DictionaryEditor).setRendererClass(com/fr/design/mainframe/widget/renderer/DictionaryRenderer), new CRPropertyDescriptor("dataUrl", data.getClass())
        });
    }

    protected JComponent initEditor()
    {
        if(editor == null)
        {
            DefaultListModel defaultlistmodel = new DefaultListModel();
            defaultlistmodel.addElement("Item 1");
            defaultlistmodel.addElement("item 2");
            defaultlistmodel.addElement("item 3");
            defaultlistmodel.addElement("item 4");
            editor = new JList(defaultlistmodel);
        }
        return editor;
    }

    public Dimension initEditorSize()
    {
        return new Dimension(120, 80);
    }

    protected String getIconName()
    {
        return "list_16.png";
    }
}
