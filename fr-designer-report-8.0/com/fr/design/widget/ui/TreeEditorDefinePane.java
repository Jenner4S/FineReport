// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.widget.ui;

import com.fr.design.data.DataCreatorUI;
import com.fr.design.gui.frpane.TreeSettingPane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.itree.refreshabletree.TreeRootPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.ui.FieldEditor;
import com.fr.form.ui.TreeEditor;
import com.fr.general.Inter;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.widget.ui:
//            FieldEditorDefinePane

public class TreeEditorDefinePane extends FieldEditorDefinePane
{

    protected TreeSettingPane treeSettingPane;
    protected TreeRootPane treeRootPane;
    private UICheckBox removeRepeatCheckBox;

    public TreeEditorDefinePane()
    {
        initComponents();
    }

    protected void populateSubFieldEditorBean(TreeEditor treeeditor)
    {
        treeSettingPane.populate(treeeditor);
        treeRootPane.populate(treeeditor.getTreeAttr());
    }

    protected TreeEditor updateSubFieldEditorBean()
    {
        TreeEditor treeeditor = treeSettingPane.updateTreeEditor();
        treeeditor.setTreeAttr(treeRootPane.update());
        return treeeditor;
    }

    protected JPanel setFirstContentPane()
    {
        return setSecondContentPane();
    }

    protected JPanel setSecondContentPane()
    {
        JPanel jpanel = FRGUIPaneFactory.createBorderLayout_L_Pane();
        jpanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        JPanel jpanel1 = FRGUIPaneFactory.createMediumHGapFlowInnerContainer_M_Pane();
        jpanel.add(jpanel1, "North");
        removeRepeatCheckBox = new UICheckBox(Inter.getLocText("Form-Remove_Repeat_Data"), false);
        jpanel1.add(removeRepeatCheckBox);
        JPanel jpanel2 = setThirdContentPane();
        if(jpanel2 != null)
            jpanel.add(jpanel2, "Center");
        return jpanel;
    }

    protected JPanel setThirdContentPane()
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
        return "tree";
    }

    public DataCreatorUI dataUI()
    {
        return treeSettingPane;
    }

    protected volatile FieldEditor updateSubFieldEditorBean()
    {
        return updateSubFieldEditorBean();
    }

    protected volatile void populateSubFieldEditorBean(FieldEditor fieldeditor)
    {
        populateSubFieldEditorBean((TreeEditor)fieldeditor);
    }
}
