// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator;

import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.ui.TreeComboBoxEditor;
import com.fr.form.ui.Widget;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import java.awt.Color;
import java.awt.Dimension;
import java.beans.IntrospectionException;
import javax.swing.JComponent;

// Referenced classes of package com.fr.design.designer.creator:
//            XTreeEditor, CRPropertyDescriptor, XWScaleLayout, XWidgetCreator, 
//            XLayoutContainer

public class XTreeComboBoxEditor extends XTreeEditor
{

    XWidgetCreator.LimpidButton btn;

    public XTreeComboBoxEditor(TreeComboBoxEditor treecomboboxeditor, Dimension dimension)
    {
        super(treecomboboxeditor, dimension);
    }

    protected JComponent initEditor()
    {
        if(editor == null)
        {
            editor = FRGUIPaneFactory.createBorderLayout_S_Pane();
            UITextField uitextfield = new UITextField(5);
            uitextfield.setOpaque(false);
            editor.add(uitextfield, "Center");
            btn = new XWidgetCreator.LimpidButton(this, "", getIconPath(), toData().isVisible() ? 1.0F : 0.4F);
            btn.setPreferredSize(new Dimension(21, 21));
            btn.setOpaque(true);
            editor.add(btn, "East");
            editor.setBackground(Color.WHITE);
        }
        return editor;
    }

    protected CRPropertyDescriptor[] addWaterMark(CRPropertyDescriptor acrpropertydescriptor[])
        throws IntrospectionException
    {
        return (CRPropertyDescriptor[])(CRPropertyDescriptor[])ArrayUtils.add(acrpropertydescriptor, (new CRPropertyDescriptor("waterMark", data.getClass())).setI18NName(Inter.getLocText("FR-Designer_WaterMark")).putKeyValue("category", "Advanced"));
    }

    protected CRPropertyDescriptor[] addAllowEdit(CRPropertyDescriptor acrpropertydescriptor[])
        throws IntrospectionException
    {
        return (CRPropertyDescriptor[])(CRPropertyDescriptor[])ArrayUtils.add(acrpropertydescriptor, (new CRPropertyDescriptor("directEdit", data.getClass())).setI18NName(Inter.getLocText("Form-Allow_Edit")).putKeyValue("category", "Advanced"));
    }

    protected CRPropertyDescriptor[] addCustomData(CRPropertyDescriptor acrpropertydescriptor[])
        throws IntrospectionException
    {
        return (CRPropertyDescriptor[])(CRPropertyDescriptor[])ArrayUtils.add(acrpropertydescriptor, (new CRPropertyDescriptor("customData", data.getClass())).setI18NName(Inter.getLocText("Form-Allow_CustomData")).putKeyValue("category", "Advanced"));
    }

    protected String getIconName()
    {
        return "comboboxtree.png";
    }

    protected void makeVisible(boolean flag)
    {
        btn.makeVisible(flag);
    }

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
