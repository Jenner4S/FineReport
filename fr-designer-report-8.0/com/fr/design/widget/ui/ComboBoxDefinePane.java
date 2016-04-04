// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.widget.ui;

import com.fr.data.Dictionary;
import com.fr.design.data.DataCreatorUI;
import com.fr.design.present.dict.DictionaryPane;
import com.fr.form.ui.ComboBox;
import com.fr.form.ui.CustomWriteAbleRepeatEditor;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.widget.ui:
//            CustomWritableRepeatEditorPane

public class ComboBoxDefinePane extends CustomWritableRepeatEditorPane
{

    protected DictionaryPane dictPane;

    public ComboBoxDefinePane()
    {
        initComponents();
    }

    protected void initComponents()
    {
        super.initComponents();
        dictPane = new DictionaryPane();
    }

    protected JPanel setForthContentPane()
    {
        return null;
    }

    protected void populateSubCustomWritableRepeatEditorBean(ComboBox combobox)
    {
        dictPane.populateBean(combobox.getDictionary());
    }

    protected ComboBox updateSubCustomWritableRepeatEditorBean()
    {
        ComboBox combobox = new ComboBox();
        combobox.setDictionary((Dictionary)dictPane.updateBean());
        return combobox;
    }

    protected String title4PopupWindow()
    {
        return "ComboBox";
    }

    public DataCreatorUI dataUI()
    {
        return dictPane;
    }

    protected volatile CustomWriteAbleRepeatEditor updateSubCustomWritableRepeatEditorBean()
    {
        return updateSubCustomWritableRepeatEditorBean();
    }

    protected volatile void populateSubCustomWritableRepeatEditorBean(CustomWriteAbleRepeatEditor customwriteablerepeateditor)
    {
        populateSubCustomWritableRepeatEditorBean((ComboBox)customwriteablerepeateditor);
    }
}
