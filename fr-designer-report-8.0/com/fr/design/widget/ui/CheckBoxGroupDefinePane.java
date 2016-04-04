// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.widget.ui;

import com.fr.data.Dictionary;
import com.fr.design.data.DataCreatorUI;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.present.dict.DictionaryPane;
import com.fr.form.ui.CheckBoxGroup;
import com.fr.form.ui.FieldEditor;
import com.fr.general.Inter;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.widget.ui:
//            FieldEditorDefinePane, CheckBoxDictPane, ButtonGroupDictPane

public class CheckBoxGroupDefinePane extends FieldEditorDefinePane
{

    private DictionaryPane dictPane;
    CheckBoxDictPane checkBoxDictPane;
    private UICheckBox checkbox;
    private ButtonGroupDictPane buttonGroupDictPane;

    public CheckBoxGroupDefinePane()
    {
        initComponents();
    }

    protected void initComponents()
    {
        super.initComponents();
        dictPane = new DictionaryPane();
    }

    protected String title4PopupWindow()
    {
        return "CheckBoxGroup";
    }

    protected JPanel setFirstContentPane()
    {
        JPanel jpanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        jpanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        JPanel jpanel1 = FRGUIPaneFactory.createBorderLayout_S_Pane();
        jpanel1.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        checkBoxDictPane = new CheckBoxDictPane();
        checkBoxDictPane.setLayout(new FlowLayout(0, 5, 0));
        jpanel1.add(checkBoxDictPane, "North");
        JPanel jpanel2 = new JPanel();
        checkbox = new UICheckBox(Inter.getLocText(new String[] {
            "Provide", "Choose_All"
        }));
        jpanel2.add(checkbox);
        jpanel2.setLayout(new FlowLayout(0, 8, 0));
        jpanel1.add(jpanel2, "Center");
        jpanel.add(jpanel1, "North");
        JPanel jpanel3 = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        buttonGroupDictPane = new ButtonGroupDictPane();
        buttonGroupDictPane.setLayout(new FlowLayout(0, 3, 0));
        jpanel3.add(buttonGroupDictPane);
        jpanel.add(jpanel3, "Center");
        return jpanel;
    }

    protected void populateSubFieldEditorBean(CheckBoxGroup checkboxgroup)
    {
        dictPane.populateBean(checkboxgroup.getDictionary());
        checkBoxDictPane.populate(checkboxgroup);
        checkbox.setSelected(checkboxgroup.isChooseAll());
        buttonGroupDictPane.populate(checkboxgroup);
    }

    protected CheckBoxGroup updateSubFieldEditorBean()
    {
        CheckBoxGroup checkboxgroup = new CheckBoxGroup();
        checkboxgroup.setDictionary((Dictionary)dictPane.updateBean());
        checkBoxDictPane.update(checkboxgroup);
        checkboxgroup.setChooseAll(checkbox.isSelected());
        buttonGroupDictPane.update(checkboxgroup);
        return checkboxgroup;
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
        populateSubFieldEditorBean((CheckBoxGroup)fieldeditor);
    }
}
