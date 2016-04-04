// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator;

import com.fr.design.mainframe.widget.editors.*;
import com.fr.design.mainframe.widget.renderer.TreeModelRenderer;
import com.fr.form.ui.FieldEditor;
import com.fr.form.ui.TreeEditor;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import java.awt.Dimension;
import java.beans.IntrospectionException;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

// Referenced classes of package com.fr.design.designer.creator:
//            XWidgetCreator, CRPropertyDescriptor

public class XTreeEditor extends XWidgetCreator
{

    public XTreeEditor(TreeEditor treeeditor, Dimension dimension)
    {
        super(treeeditor, dimension);
    }

    public CRPropertyDescriptor[] supportedDescriptor()
        throws IntrospectionException
    {
        CRPropertyDescriptor acrpropertydescriptor[] = ((FieldEditor)toData()).isAllowBlank() ? (new CRPropertyDescriptor[] {
            (new CRPropertyDescriptor("widgetValue", data.getClass())).setI18NName(Inter.getLocText(new String[] {
                "FR-Designer_Widget", "Value"
            })).setEditorClass(com/fr/design/mainframe/widget/editors/WidgetValueEditor), (new CRPropertyDescriptor("model", data.getClass(), "getNodeOrDict", "setNodeOrDict")).setI18NName(Inter.getLocText("FR-Designer_DS-Dictionary")).setEditorClass(com/fr/design/mainframe/widget/editors/TreeModelEditor).setRendererClass(com/fr/design/mainframe/widget/renderer/TreeModelRenderer), (new CRPropertyDescriptor("allowBlank", data.getClass())).setI18NName(Inter.getLocText("FR-Designer_Allow_Blank")).setEditorClass(com/fr/design/mainframe/widget/editors/InChangeBooleanEditor).putKeyValue("category", "Advanced")
        }) : (new CRPropertyDescriptor[] {
            (new CRPropertyDescriptor("widgetValue", data.getClass())).setI18NName(Inter.getLocText(new String[] {
                "FR-Designer_Widget", "Value"
            })).setEditorClass(com/fr/design/mainframe/widget/editors/WidgetValueEditor), (new CRPropertyDescriptor("model", data.getClass(), "getNodeOrDict", "setNodeOrDict")).setI18NName(Inter.getLocText("FR-Designer_DS-Dictionary")).setEditorClass(com/fr/design/mainframe/widget/editors/TreeModelEditor).setRendererClass(com/fr/design/mainframe/widget/renderer/TreeModelRenderer), (new CRPropertyDescriptor("allowBlank", data.getClass())).setI18NName(Inter.getLocText("FR-Designer_Allow_Blank")).setEditorClass(com/fr/design/mainframe/widget/editors/InChangeBooleanEditor).putKeyValue("category", "Advanced"), (new CRPropertyDescriptor("errorMessage", data.getClass())).setI18NName(Inter.getLocText("FR-Designer_Verify-Message")).putKeyValue("category", "Advanced")
        });
        acrpropertydescriptor = addWaterMark(acrpropertydescriptor);
        acrpropertydescriptor = (CRPropertyDescriptor[])(CRPropertyDescriptor[])ArrayUtils.add(acrpropertydescriptor, (new CRPropertyDescriptor("fontSize", data.getClass(), "getFontSize", "setFontSize")).setI18NName(Inter.getLocText(new String[] {
            "FR-Designer_Font", "FRFont-Size"
        })).putKeyValue("category", "Advanced"));
        acrpropertydescriptor = (CRPropertyDescriptor[])(CRPropertyDescriptor[])ArrayUtils.add(acrpropertydescriptor, (new CRPropertyDescriptor("multipleSelection", data.getClass())).setI18NName(Inter.getLocText("Tree-Mutiple_Selection_Or_Not")).putKeyValue("category", "Advanced").setEditorClass(com/fr/design/mainframe/widget/editors/InChangeBooleanEditor));
        acrpropertydescriptor = (CRPropertyDescriptor[])(CRPropertyDescriptor[])ArrayUtils.add(acrpropertydescriptor, (new CRPropertyDescriptor("ajax", data.getClass())).setI18NName(Inter.getLocText("Widget-Load_By_Async")).putKeyValue("category", "Advanced"));
        acrpropertydescriptor = addAllowEdit(acrpropertydescriptor);
        acrpropertydescriptor = addCustomData(acrpropertydescriptor);
        acrpropertydescriptor = (CRPropertyDescriptor[])(CRPropertyDescriptor[])ArrayUtils.add(acrpropertydescriptor, (new CRPropertyDescriptor("selectLeafOnly", data.getClass())).setI18NName(Inter.getLocText("Tree-Select_Leaf_Only")).putKeyValue("category", "Advanced"));
        acrpropertydescriptor = (CRPropertyDescriptor[])(CRPropertyDescriptor[])ArrayUtils.add(acrpropertydescriptor, (new CRPropertyDescriptor("returnFullPath", data.getClass())).setI18NName(Inter.getLocText("Tree-Return_Full_Path")).putKeyValue("category", "Advanced"));
        return (CRPropertyDescriptor[])(CRPropertyDescriptor[])ArrayUtils.addAll(super.supportedDescriptor(), acrpropertydescriptor);
    }

    protected CRPropertyDescriptor[] addWaterMark(CRPropertyDescriptor acrpropertydescriptor[])
        throws IntrospectionException
    {
        return acrpropertydescriptor;
    }

    protected CRPropertyDescriptor[] addAllowEdit(CRPropertyDescriptor acrpropertydescriptor[])
        throws IntrospectionException
    {
        return acrpropertydescriptor;
    }

    protected CRPropertyDescriptor[] addCustomData(CRPropertyDescriptor acrpropertydescriptor[])
        throws IntrospectionException
    {
        return acrpropertydescriptor;
    }

    protected JComponent initEditor()
    {
        if(editor == null)
        {
            DefaultMutableTreeNode defaultmutabletreenode = new DefaultMutableTreeNode("Tree");
            defaultmutabletreenode.add(new DefaultMutableTreeNode("Leaf1"));
            defaultmutabletreenode.add(new DefaultMutableTreeNode("Leaf2"));
            editor = new JTree(defaultmutabletreenode);
            editor.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
        }
        return editor;
    }

    public Dimension initEditorSize()
    {
        return SMALL_PREFERRED_SIZE;
    }

    protected String getIconName()
    {
        return "tree_16.png";
    }
}
