package com.fr.design.widget;

import com.fr.base.FRContext;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.core.WidgetConstants;
import com.fr.design.gui.frpane.TreeSettingPane;
import com.fr.design.present.dict.DictionaryPane;
import com.fr.design.widget.ui.*;
import com.fr.form.ui.*;
import com.fr.report.web.button.form.TreeNodeToggleButton;
import com.fr.report.web.button.write.AppendRowButton;
import com.fr.report.web.button.write.DeleteRowButton;
import com.fr.stable.bridge.BridgeMark;
import com.fr.stable.bridge.StableFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-11-14
 * Time   : 上午11:17
 */
public class WidgetDefinePaneFactory {
    private static Map<Class<? extends Widget>, Appearance> defineMap = new HashMap<Class<? extends Widget>, Appearance>();

    static {
        defineMap.put(NumberEditor.class, new Appearance(NumberEditorDefinePane.class, WidgetConstants.NUMBER + ""));
        defineMap.put(DateEditor.class, new Appearance(DateEditorDefinePane.class, WidgetConstants.DATE + ""));
        defineMap.put(ComboCheckBox.class, new Appearance(ComboCheckBoxDefinePane.class, WidgetConstants.COMBOCHECKBOX + ""));
        defineMap.put(Radio.class, new Appearance(RadioDefinePane.class, WidgetConstants.RADIO + ""));
        defineMap.put(CheckBox.class, new Appearance(CheckBoxDefinePane.class, WidgetConstants.CHECKBOX + ""));
        defineMap.put(TreeComboBoxEditor.class, new Appearance(TreeComboBoxEditorDefinePane.class, WidgetConstants.TREECOMBOBOX + ""));
//        defineMap.put(TreeEditor.class.getName(), new DN(TreeComboBoxEditorDefinePane.class, WidgetConstants.TREE + ""));
        // shoc:没看懂为什么这么搞
        defineMap.put(TreeEditor.class, new Appearance(TreeEditorDefinePane.class, WidgetConstants.TREE + ""));
        defineMap.put(MultiFileEditor.class, new Appearance(MultiFileEditorPane.class, WidgetConstants.MULTI_FILE + ""));
        defineMap.put(TextArea.class, new Appearance(TextAreaDefinePane.class, WidgetConstants.TEXTAREA + ""));
        defineMap.put(Password.class, new Appearance(PasswordDefinePane.class, WidgetConstants.PASSWORD + ""));
        defineMap.put(IframeEditor.class, new Appearance(IframeEditorDefinePane.class, WidgetConstants.IFRAME + ""));
        defineMap.put(TextEditor.class, new Appearance(TextFieldEditorDefinePane.class, WidgetConstants.TEXT + ""));
        defineMap.put(NameWidget.class, new Appearance(UserEditorDefinePane.class, "UserDefine"));
        defineMap.put(ComboCheckBox.class, new Appearance(ComboCheckBoxDefinePane.class, WidgetConstants.COMBOCHECKBOX + ""));
        defineMap.put(ListEditor.class, new Appearance(ListEditorDefinePane.class, WidgetConstants.LIST + ""));
        defineMap.put(ComboBox.class, new Appearance(ComboBoxDefinePane.class, WidgetConstants.COMBOBOX + ""));
        defineMap.put(RadioGroup.class, new Appearance(RadioGroupDefinePane.class, WidgetConstants.RADIOGROUP + ""));
        defineMap.put(CheckBoxGroup.class, new Appearance(CheckBoxGroupDefinePane.class, WidgetConstants.CHECKBOXGROUP + ""));
        //AUGUST：按需求说表格树控件要删除 先屏蔽之
//        defineMap.put(TableTree.class.getName(), new DN(TableTreeDefinePane.class, WidgetConstants.TABLETREE + ""));
        defineMap.put(NoneWidget.class, new Appearance(NoneWidgetDefinePane.class, WidgetConstants.NONE + ""));
        defineMap.put(Button.class, new Appearance(ButtonDefinePane.class, WidgetConstants.BUTTON + ""));
        defineMap.put(FreeButton.class, new Appearance(ButtonDefinePane.class, WidgetConstants.BUTTON + ""));
        if (StableFactory.getMarkedClass(BridgeMark.SUBMIT_BUTTON, Widget.class) != null) {
            defineMap.put(StableFactory.getMarkedClass(BridgeMark.SUBMIT_BUTTON, Widget.class), new Appearance(ButtonDefinePane.class, WidgetConstants.BUTTON + ""));
        }
        defineMap.put(AppendRowButton.class, new Appearance(ButtonDefinePane.class, WidgetConstants.BUTTON + ""));
        defineMap.put(DeleteRowButton.class, new Appearance(ButtonDefinePane.class, WidgetConstants.BUTTON + ""));
        defineMap.put(TreeNodeToggleButton.class, new Appearance(ButtonDefinePane.class, WidgetConstants.BUTTON + ""));
        defineMap.putAll(ExtraDesignClassManager.getInstance().getCellWidgetOptionsMap());
    }

    private WidgetDefinePaneFactory() {

    }

    public static RN createWidgetDefinePane(Widget widget) {
        Appearance dn = defineMap.get(widget.getClass());
        BasicBeanPane<Widget> definePane = null;
        DictionaryPane dictionaryPane = null;
        TreeSettingPane treeSettingPane = null;
        try {
            definePane = (BasicBeanPane<Widget>) dn.getDefineClass().newInstance();

            definePane.populateBean(widget);
            dictionaryPane = ((DicPaneAndTreePaneCreator)definePane).getDictionaryPane();
            treeSettingPane = ((DicPaneAndTreePaneCreator)definePane).getTreeSettingPane();
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage(), e);
        }
        if (treeSettingPane != null) {
            return new RN(definePane, dn.getDisplayName(), treeSettingPane);
        } else {
            return new RN(definePane, dn.getDisplayName(), dictionaryPane);
        }
    }

    public static class RN {
        private BasicBeanPane<? extends Widget> definePane;
        private String cardName;
        private DictionaryPane dictionaryPane;
        private TreeSettingPane treeSettingPane;

        public RN(BasicBeanPane<? extends Widget> definePane, String cardName, DictionaryPane dictionaryPane) {
            this.definePane = definePane;
            this.cardName = cardName;
            this.dictionaryPane = dictionaryPane;
        }

        public RN(BasicBeanPane<? extends Widget> definePane, String cardName, TreeSettingPane treeSettingPane) {
            this.definePane = definePane;
            this.cardName = cardName;
            this.treeSettingPane = treeSettingPane;
        }

        public BasicBeanPane<? extends Widget> getDefinePane() {
            return definePane;
        }

        public String getCardName() {
            return cardName;
        }

        public DictionaryPane getDictionaryPane() {
            return dictionaryPane;
        }

        public TreeSettingPane getTreeSettingPane() {
            return treeSettingPane;
        }
    }

}
