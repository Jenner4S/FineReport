package com.fr.design.widget;

import com.fr.design.gui.frpane.TreeSettingPane;
import com.fr.design.present.dict.DictionaryPane;

/**
 * Created with IntelliJ IDEA.
 * User: С�һ�
 * Date: 13-8-23
 * Time: ����11:35
 * To change this template use File | Settings | File Templates.
 */
public interface DicPaneAndTreePaneCreator {

    public DictionaryPane getDictionaryPane();


    public TreeSettingPane getTreeSettingPane();

}
