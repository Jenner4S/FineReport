// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.widget.ui;

import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.ui.DirectWriteEditor;
import com.fr.form.ui.FieldEditor;
import com.fr.general.Inter;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.widget.ui:
//            FieldEditorDefinePane, WaterMarkDictPane

public abstract class DirectWriteEditorDefinePane extends FieldEditorDefinePane
{

    public UICheckBox directWriteCheckBox;
    private WaterMarkDictPane waterMarkDictPane;

    public DirectWriteEditorDefinePane()
    {
        initComponents();
    }

    protected JPanel setFirstContentPane()
    {
        JPanel jpanel = FRGUIPaneFactory.createY_AXISBoxInnerContainer_L_Pane();
        jpanel.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));
        JPanel jpanel1 = FRGUIPaneFactory.createMediumHGapFlowInnerContainer_M_Pane();
        jpanel.add(jpanel1);
        directWriteCheckBox = new UICheckBox(Inter.getLocText("Form-Allow_Edit"), false);
        jpanel1.add(directWriteCheckBox);
        waterMarkDictPane = new WaterMarkDictPane();
        jpanel1.add(waterMarkDictPane);
        JPanel jpanel2 = setSecondContentPane();
        if(jpanel2 != null)
            jpanel.add(jpanel2);
        return jpanel;
    }

    protected abstract JPanel setSecondContentPane();

    protected void populateSubFieldEditorBean(DirectWriteEditor directwriteeditor)
    {
        directWriteCheckBox.setSelected(directwriteeditor.isDirectEdit());
        waterMarkDictPane.populate(directwriteeditor);
        populateSubDirectWriteEditorBean(directwriteeditor);
    }

    protected abstract void populateSubDirectWriteEditorBean(DirectWriteEditor directwriteeditor);

    protected DirectWriteEditor updateSubFieldEditorBean()
    {
        DirectWriteEditor directwriteeditor = updateSubDirectWriteEditorBean();
        directwriteeditor.setDirectEdit(directWriteCheckBox.isSelected());
        waterMarkDictPane.update(directwriteeditor);
        return directwriteeditor;
    }

    protected abstract DirectWriteEditor updateSubDirectWriteEditorBean();

    protected volatile FieldEditor updateSubFieldEditorBean()
    {
        return updateSubFieldEditorBean();
    }

    protected volatile void populateSubFieldEditorBean(FieldEditor fieldeditor)
    {
        populateSubFieldEditorBean((DirectWriteEditor)fieldeditor);
    }
}
