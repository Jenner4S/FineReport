package com.fr.plugin.widget.speedtree;

import com.fr.design.widget.ui.TreeComboBoxEditorDefinePane;

/**
 * Created by zack on 2015/10/8.
 */
public class SpeedTreeDefinePane extends TreeComboBoxEditorDefinePane {

    public SpeedTreeDefinePane() {
        super.initComponents();
    }

    @Override
    protected String title4PopupWindow() {
        return "SpeedTree";
    }

}