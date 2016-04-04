// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator;

import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.widget.editors.DictionaryEditor;
import com.fr.design.mainframe.widget.editors.WidgetValueEditor;
import com.fr.design.mainframe.widget.renderer.DictionaryRenderer;
import com.fr.form.ui.ComboBox;
import com.fr.form.ui.Widget;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import java.awt.Color;
import java.awt.Dimension;
import java.beans.IntrospectionException;
import javax.swing.JComponent;

// Referenced classes of package com.fr.design.designer.creator:
//            XCustomWriteAbleRepeatEditor, CRPropertyDescriptor, XWScaleLayout, XWidgetCreator, 
//            XLayoutContainer

public class XComboBox extends XCustomWriteAbleRepeatEditor
{

    XWidgetCreator.LimpidButton btn;

    public XComboBox(ComboBox combobox, Dimension dimension)
    {
        super(combobox, dimension);
    }

    public CRPropertyDescriptor[] supportedDescriptor()
        throws IntrospectionException
    {
        return (CRPropertyDescriptor[])(CRPropertyDescriptor[])ArrayUtils.addAll(super.supportedDescriptor(), new CRPropertyDescriptor[] {
            (new CRPropertyDescriptor("widgetValue", data.getClass())).setI18NName(Inter.getLocText(new String[] {
                "Widget", "Value"
            })).setEditorClass(com/fr/design/mainframe/widget/editors/WidgetValueEditor), (new CRPropertyDescriptor("dictionary", data.getClass())).setI18NName(Inter.getLocText("FR-Designer_DS-Dictionary")).setEditorClass(com/fr/design/mainframe/widget/editors/DictionaryEditor).setRendererClass(com/fr/design/mainframe/widget/renderer/DictionaryRenderer)
        });
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

    protected String getIconName()
    {
        return "combo_box_16.png";
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
