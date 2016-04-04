// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.widget.ui;

import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.ui.FieldEditor;
import com.fr.form.ui.WriteUnableRepeatEditor;
import com.fr.general.Inter;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.widget.ui:
//            FieldEditorDefinePane

public abstract class WriteUnableRepeatEditorPane extends FieldEditorDefinePane
{

    protected UICheckBox removeRepeatCheckBox;

    public WriteUnableRepeatEditorPane()
    {
        initComponents();
    }

    protected JPanel setFirstContentPane()
    {
        JPanel jpanel = FRGUIPaneFactory.createY_AXISBoxInnerContainer_L_Pane();
        jpanel.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));
        JPanel jpanel1 = FRGUIPaneFactory.createMediumHGapFlowInnerContainer_M_Pane();
        removeRepeatCheckBox = new UICheckBox(Inter.getLocText("Form-Remove_Repeat_Data"), false);
        jpanel.add(jpanel1);
        jpanel1.add(removeRepeatCheckBox);
        JPanel jpanel2 = setThirdContentPane();
        if(jpanel2 != null)
            jpanel.add(jpanel2, "Center");
        return jpanel;
    }

    protected abstract JPanel setThirdContentPane();

    protected void populateSubFieldEditorBean(WriteUnableRepeatEditor writeunablerepeateditor)
    {
        removeRepeatCheckBox.setSelected(writeunablerepeateditor.isRemoveRepeat());
        populateSubWriteUnableRepeatBean(writeunablerepeateditor);
    }

    protected abstract void populateSubWriteUnableRepeatBean(WriteUnableRepeatEditor writeunablerepeateditor);

    protected WriteUnableRepeatEditor updateSubFieldEditorBean()
    {
        WriteUnableRepeatEditor writeunablerepeateditor = updateSubWriteUnableRepeatBean();
        writeunablerepeateditor.setRemoveRepeat(removeRepeatCheckBox.isSelected());
        return writeunablerepeateditor;
    }

    protected abstract WriteUnableRepeatEditor updateSubWriteUnableRepeatBean();

    protected volatile FieldEditor updateSubFieldEditorBean()
    {
        return updateSubFieldEditorBean();
    }

    protected volatile void populateSubFieldEditorBean(FieldEditor fieldeditor)
    {
        populateSubFieldEditorBean((WriteUnableRepeatEditor)fieldeditor);
    }
}
