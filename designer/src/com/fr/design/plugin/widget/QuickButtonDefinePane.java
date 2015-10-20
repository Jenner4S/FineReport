package com.fr.design.plugin.widget;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.frpane.TreeSettingPane;
import com.fr.design.present.dict.DictionaryPane;
import com.fr.design.widget.DicPaneAndTreePaneCreator;

/**
 * @author richie
 * @date 2015-01-29
 * @since 8.0
 */
public class QuickButtonDefinePane extends BasicBeanPane<QuickButton> implements DicPaneAndTreePaneCreator {

    public QuickButtonDefinePane() {

    }
    @Override
    public void populateBean(QuickButton ob) {

    }

    @Override
    public QuickButton updateBean() {
        QuickButton btn =  new QuickButton();
        btn.setText("sddsds");

        return btn;
    }

    @Override
    protected String title4PopupWindow() {
        return null;
    }

    @Override
    public DictionaryPane getDictionaryPane() {
        return null;
    }

    @Override
    public TreeSettingPane getTreeSettingPane() {
        return null;
    }
}
