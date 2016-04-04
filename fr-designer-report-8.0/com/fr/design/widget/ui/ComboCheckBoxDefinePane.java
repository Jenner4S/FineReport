// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.widget.ui;

import com.fr.data.Dictionary;
import com.fr.design.data.DataCreatorUI;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.present.dict.DictionaryPane;
import com.fr.form.ui.ComboCheckBox;
import com.fr.form.ui.CustomWriteAbleRepeatEditor;
import com.fr.general.Inter;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.widget.ui:
//            CustomWritableRepeatEditorPane, CheckBoxDictPane

public class ComboCheckBoxDefinePane extends CustomWritableRepeatEditorPane
{

    private CheckBoxDictPane checkBoxDictPane;
    private DictionaryPane dictPane;
    private UICheckBox supportTagCheckBox;

    public ComboCheckBoxDefinePane()
    {
        super.initComponents();
        dictPane = new DictionaryPane();
    }

    protected JPanel setForthContentPane()
    {
        JPanel jpanel = FRGUIPaneFactory.createY_AXISBoxInnerContainer_S_Pane();
        jpanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        JPanel jpanel1 = FRGUIPaneFactory.createBorderLayout_L_Pane();
        jpanel1.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        checkBoxDictPane = new CheckBoxDictPane();
        jpanel.add(jpanel1);
        JPanel jpanel2 = FRGUIPaneFactory.createMediumHGapFlowInnerContainer_M_Pane();
        supportTagCheckBox = new UICheckBox(Inter.getLocText("Form-SupportTag"), true);
        jpanel2.add(supportTagCheckBox);
        jpanel1.add(jpanel2, "North");
        jpanel1.add(checkBoxDictPane, "West");
        return jpanel;
    }

    protected void populateSubCustomWritableRepeatEditorBean(ComboCheckBox combocheckbox)
    {
        dictPane.populateBean(combocheckbox.getDictionary());
        checkBoxDictPane.populate(combocheckbox);
        supportTagCheckBox.setSelected(combocheckbox.isSupportTag());
    }

    protected ComboCheckBox updateSubCustomWritableRepeatEditorBean()
    {
        ComboCheckBox combocheckbox = new ComboCheckBox();
        combocheckbox.setSupportTag(supportTagCheckBox.isSelected());
        combocheckbox.setDictionary((Dictionary)dictPane.updateBean());
        checkBoxDictPane.update(combocheckbox);
        return combocheckbox;
    }

    public DataCreatorUI dataUI()
    {
        return dictPane;
    }

    protected String title4PopupWindow()
    {
        return "ComboCheckBox";
    }

    protected volatile CustomWriteAbleRepeatEditor updateSubCustomWritableRepeatEditorBean()
    {
        return updateSubCustomWritableRepeatEditorBean();
    }

    protected volatile void populateSubCustomWritableRepeatEditorBean(CustomWriteAbleRepeatEditor customwriteablerepeateditor)
    {
        populateSubCustomWritableRepeatEditorBean((ComboCheckBox)customwriteablerepeateditor);
    }
}
