// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.widget.ui;

import com.fr.data.Dictionary;
import com.fr.design.data.DataCreatorUI;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.present.dict.DictionaryPane;
import com.fr.form.ui.ListEditor;
import com.fr.form.ui.WriteUnableRepeatEditor;
import com.fr.general.Inter;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.widget.ui:
//            WriteUnableRepeatEditorPane

public class ListEditorDefinePane extends WriteUnableRepeatEditorPane
{

    private UICheckBox needHeadCheckBox;
    private DictionaryPane dictPane;

    public ListEditorDefinePane()
    {
        initComponents();
    }

    protected void initComponents()
    {
        super.initComponents();
        dictPane = new DictionaryPane();
    }

    protected JPanel setThirdContentPane()
    {
        JPanel jpanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        JPanel jpanel1 = FRGUIPaneFactory.createMediumHGapFlowInnerContainer_M_Pane();
        jpanel1.add(needHeadCheckBox = new UICheckBox(Inter.getLocText("List-Need_Head")));
        jpanel.add(jpanel1);
        return jpanel;
    }

    protected String title4PopupWindow()
    {
        return "List";
    }

    protected void populateSubWriteUnableRepeatBean(ListEditor listeditor)
    {
        needHeadCheckBox.setSelected(listeditor.isNeedHead());
        dictPane.populateBean(listeditor.getDictionary());
    }

    protected ListEditor updateSubWriteUnableRepeatBean()
    {
        ListEditor listeditor = new ListEditor();
        listeditor.setNeedHead(needHeadCheckBox.isSelected());
        listeditor.setDictionary((Dictionary)dictPane.updateBean());
        return listeditor;
    }

    public DataCreatorUI dataUI()
    {
        return dictPane;
    }

    protected volatile WriteUnableRepeatEditor updateSubWriteUnableRepeatBean()
    {
        return updateSubWriteUnableRepeatBean();
    }

    protected volatile void populateSubWriteUnableRepeatBean(WriteUnableRepeatEditor writeunablerepeateditor)
    {
        populateSubWriteUnableRepeatBean((ListEditor)writeunablerepeateditor);
    }
}
