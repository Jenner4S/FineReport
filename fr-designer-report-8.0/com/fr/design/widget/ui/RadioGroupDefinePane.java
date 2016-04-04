// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.widget.ui;

import com.fr.data.Dictionary;
import com.fr.design.data.DataCreatorUI;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.present.dict.DictionaryPane;
import com.fr.form.ui.FieldEditor;
import com.fr.form.ui.RadioGroup;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.widget.ui:
//            FieldEditorDefinePane, ButtonGroupDictPane

public class RadioGroupDefinePane extends FieldEditorDefinePane
{

    private DictionaryPane dictPane;
    private ButtonGroupDictPane buttonGroupDictPane;

    public RadioGroupDefinePane()
    {
        initComponents();
    }

    protected void initComponents()
    {
        super.initComponents();
        dictPane = new DictionaryPane();
    }

    protected JPanel setFirstContentPane()
    {
        JPanel jpanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        jpanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        JPanel jpanel1 = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        buttonGroupDictPane = new ButtonGroupDictPane();
        buttonGroupDictPane.setLayout(new FlowLayout(0, 5, 0));
        jpanel1.add(buttonGroupDictPane);
        jpanel.add(jpanel1, "Center");
        return jpanel;
    }

    protected RadioGroup updateSubFieldEditorBean()
    {
        RadioGroup radiogroup = new RadioGroup();
        radiogroup.setDictionary((Dictionary)dictPane.updateBean());
        buttonGroupDictPane.update(radiogroup);
        return radiogroup;
    }

    protected String title4PopupWindow()
    {
        return "radiogroup";
    }

    protected void populateSubFieldEditorBean(RadioGroup radiogroup)
    {
        dictPane.populateBean(radiogroup.getDictionary());
        buttonGroupDictPane.populate(radiogroup);
    }

    public DataCreatorUI dataUI()
    {
        return dictPane;
    }

    protected volatile FieldEditor updateSubFieldEditorBean()
    {
        return updateSubFieldEditorBean();
    }

    protected volatile void populateSubFieldEditorBean(FieldEditor fieldeditor)
    {
        populateSubFieldEditorBean((RadioGroup)fieldeditor);
    }
}
