// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.widget.ui;

import com.fr.design.data.DataCreatorUI;
import com.fr.design.gui.frpane.TreeSettingPane;
import com.fr.design.gui.itree.refreshabletree.TreeRootPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.ui.*;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.widget.ui:
//            CustomWritableRepeatEditorPane

public class TreeComboBoxEditorDefinePane extends CustomWritableRepeatEditorPane
{

    protected TreeSettingPane treeSettingPane;
    protected TreeRootPane treeRootPane;

    public TreeComboBoxEditorDefinePane()
    {
        initComponents();
    }

    protected JPanel setForthContentPane()
    {
        JPanel jpanel = FRGUIPaneFactory.createBorderLayout_L_Pane();
        jpanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        treeRootPane = new TreeRootPane();
        jpanel.add(treeRootPane, "North");
        treeSettingPane = new TreeSettingPane(true);
        return jpanel;
    }

    protected String title4PopupWindow()
    {
        return "treecombobox";
    }

    protected void populateSubCustomWritableRepeatEditorBean(TreeEditor treeeditor)
    {
        treeSettingPane.populate(treeeditor);
        treeRootPane.populate(treeeditor.getTreeAttr());
    }

    protected TreeComboBoxEditor updateSubCustomWritableRepeatEditorBean()
    {
        TreeComboBoxEditor treecomboboxeditor = treeSettingPane.updateTreeComboBox();
        treecomboboxeditor.setTreeAttr(treeRootPane.update());
        return treecomboboxeditor;
    }

    public DataCreatorUI dataUI()
    {
        return treeSettingPane;
    }

    protected volatile CustomWriteAbleRepeatEditor updateSubCustomWritableRepeatEditorBean()
    {
        return updateSubCustomWritableRepeatEditorBean();
    }

    protected volatile void populateSubCustomWritableRepeatEditorBean(CustomWriteAbleRepeatEditor customwriteablerepeateditor)
    {
        populateSubCustomWritableRepeatEditorBean((TreeEditor)customwriteablerepeateditor);
    }
}
