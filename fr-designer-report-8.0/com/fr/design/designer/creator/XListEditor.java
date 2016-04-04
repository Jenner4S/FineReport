// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator;

import com.fr.design.mainframe.widget.editors.DictionaryEditor;
import com.fr.design.mainframe.widget.editors.WidgetValueEditor;
import com.fr.design.mainframe.widget.renderer.DictionaryRenderer;
import com.fr.form.ui.ListEditor;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import java.awt.Dimension;
import java.beans.IntrospectionException;
import javax.swing.*;

// Referenced classes of package com.fr.design.designer.creator:
//            XFieldEditor, CRPropertyDescriptor

public class XListEditor extends XFieldEditor
{

    public XListEditor(ListEditor listeditor, Dimension dimension)
    {
        super(listeditor, dimension);
    }

    public CRPropertyDescriptor[] supportedDescriptor()
        throws IntrospectionException
    {
        return (CRPropertyDescriptor[])(CRPropertyDescriptor[])ArrayUtils.addAll(super.supportedDescriptor(), new CRPropertyDescriptor[] {
            (new CRPropertyDescriptor("widgetValue", data.getClass())).setI18NName(Inter.getLocText(new String[] {
                "Widget", "Value"
            })).setEditorClass(com/fr/design/mainframe/widget/editors/WidgetValueEditor), (new CRPropertyDescriptor("dictionary", data.getClass())).setI18NName(Inter.getLocText("DS-Dictionary")).setEditorClass(com/fr/design/mainframe/widget/editors/DictionaryEditor).setRendererClass(com/fr/design/mainframe/widget/renderer/DictionaryRenderer), (new CRPropertyDescriptor("needHead", data.getClass())).setI18NName(Inter.getLocText("List-Need_Head")).putKeyValue("category", "Advanced")
        });
    }

    protected void initXCreatorProperties()
    {
        super.initXCreatorProperties();
        JList jlist = (JList)editor;
        ListEditor listeditor = (ListEditor)data;
        jlist.setSelectedIndex(0);
        jlist.setSelectionBackground(listeditor.getSelectionBackground());
        jlist.setSelectionForeground(listeditor.getSelectionForeground());
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
            editor.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
        }
        return editor;
    }

    public Dimension initEditorSize()
    {
        return new Dimension(120, 80);
    }

    public boolean canEnterIntoParaPane()
    {
        return false;
    }

    protected String getIconName()
    {
        return "list_16.png";
    }
}
