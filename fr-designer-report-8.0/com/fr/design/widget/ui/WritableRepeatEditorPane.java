// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.widget.ui;

import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.ui.DirectWriteEditor;
import com.fr.form.ui.WriteAbleRepeatEditor;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.widget.ui:
//            DirectWriteEditorDefinePane

public abstract class WritableRepeatEditorPane extends DirectWriteEditorDefinePane
{

    public WritableRepeatEditorPane()
    {
        initComponents();
    }

    protected JPanel setSecondContentPane()
    {
        JPanel jpanel = FRGUIPaneFactory.createBorderLayout_L_Pane();
        jpanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        JPanel jpanel1 = FRGUIPaneFactory.createMediumHGapFlowInnerContainer_M_Pane();
        jpanel.add(jpanel1, "North");
        JPanel jpanel2 = setThirdContentPane();
        if(jpanel2 != null)
            jpanel.add(jpanel2, "Center");
        return jpanel;
    }

    protected abstract JPanel setThirdContentPane();

    protected void populateSubDirectWriteEditorBean(WriteAbleRepeatEditor writeablerepeateditor)
    {
        populateSubWritableRepeatEditorBean(writeablerepeateditor);
    }

    protected abstract void populateSubWritableRepeatEditorBean(WriteAbleRepeatEditor writeablerepeateditor);

    protected WriteAbleRepeatEditor updateSubDirectWriteEditorBean()
    {
        return updateSubWritableRepeatEditorBean();
    }

    protected abstract WriteAbleRepeatEditor updateSubWritableRepeatEditorBean();

    protected volatile DirectWriteEditor updateSubDirectWriteEditorBean()
    {
        return updateSubDirectWriteEditorBean();
    }

    protected volatile void populateSubDirectWriteEditorBean(DirectWriteEditor directwriteeditor)
    {
        populateSubDirectWriteEditorBean((WriteAbleRepeatEditor)directwriteeditor);
    }
}
