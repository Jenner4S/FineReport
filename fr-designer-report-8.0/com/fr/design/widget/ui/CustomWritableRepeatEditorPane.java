// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.widget.ui;

import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.ui.CustomWriteAbleRepeatEditor;
import com.fr.form.ui.WriteAbleRepeatEditor;
import com.fr.general.Inter;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.widget.ui:
//            WritableRepeatEditorPane

public abstract class CustomWritableRepeatEditorPane extends WritableRepeatEditorPane
{

    private UICheckBox customDataCheckBox;

    public CustomWritableRepeatEditorPane()
    {
        initComponents();
    }

    protected JPanel setThirdContentPane()
    {
        JPanel jpanel = FRGUIPaneFactory.createBorderLayout_L_Pane();
        jpanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        JPanel jpanel1 = FRGUIPaneFactory.createMediumHGapFlowInnerContainer_M_Pane();
        jpanel.add(jpanel1, "North");
        customDataCheckBox = new UICheckBox(Inter.getLocText("Form-Allow_CustomData"), false);
        jpanel1.add(customDataCheckBox);
        JPanel jpanel2 = setForthContentPane();
        if(jpanel2 != null)
            jpanel.add(jpanel2, "Center");
        return jpanel;
    }

    protected abstract JPanel setForthContentPane();

    protected void populateSubWritableRepeatEditorBean(CustomWriteAbleRepeatEditor customwriteablerepeateditor)
    {
        customDataCheckBox.setSelected(customwriteablerepeateditor.isCustomData());
        populateSubCustomWritableRepeatEditorBean(customwriteablerepeateditor);
    }

    protected abstract void populateSubCustomWritableRepeatEditorBean(CustomWriteAbleRepeatEditor customwriteablerepeateditor);

    protected CustomWriteAbleRepeatEditor updateSubWritableRepeatEditorBean()
    {
        CustomWriteAbleRepeatEditor customwriteablerepeateditor = updateSubCustomWritableRepeatEditorBean();
        customwriteablerepeateditor.setCustomData(customDataCheckBox.isSelected());
        return customwriteablerepeateditor;
    }

    protected abstract CustomWriteAbleRepeatEditor updateSubCustomWritableRepeatEditorBean();

    protected volatile WriteAbleRepeatEditor updateSubWritableRepeatEditorBean()
    {
        return updateSubWritableRepeatEditorBean();
    }

    protected volatile void populateSubWritableRepeatEditorBean(WriteAbleRepeatEditor writeablerepeateditor)
    {
        populateSubWritableRepeatEditorBean((CustomWriteAbleRepeatEditor)writeablerepeateditor);
    }
}
