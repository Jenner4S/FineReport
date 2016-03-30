package com.fr.plugin.cell.highlight;

import com.fr.design.condition.ConditionAttrSingleConditionPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.icombobox.UIDictionaryComboBox;
import com.fr.general.Inter;
import com.fr.report.cell.cellattr.highlight.HighlightAction;
import com.fr.stable.Constants;

import javax.swing.*;

/**
 * @author richie
 * @date 2015-03-26
 * @since 8.0
 */
public class MyHighlightPane extends ConditionAttrSingleConditionPane<HighlightAction> {
    private UIDictionaryComboBox<Integer> alignComboBox;
    private JComboBox scopeComboBox;

    public MyHighlightPane(ConditionAttributesPane conditionAttributesPane) {
        super(conditionAttributesPane);
        this.alignComboBox = new UIDictionaryComboBox<Integer>(
                new Integer[]{Constants.LEFT, Constants.CENTER, Constants.RIGHT},
                new String[]{"左对齐","居中对齐","右对齐"}
        );
        add(alignComboBox);
        this.scopeComboBox = new UIComboBox(new String[] {
                Inter.getLocText("Utils-Current_Cell"),
                Inter.getLocText("Utils-Current_Row"),
                Inter.getLocText("Utils-Current_Column") });

        this.add(this.scopeComboBox);
    }

    @Override
    public String nameForPopupMenuItem() {
        return "我的条件属性";
    }

    @Override
    public void populate(HighlightAction condition) {
        MyHighlightAction action = (MyHighlightAction) condition;
        scopeComboBox.setSelectedIndex(action.getScope());
        alignComboBox.setSelectedItem(action.getAlign());
    }

    @Override
    public HighlightAction update() {
        return new MyHighlightAction(alignComboBox.getSelectedItem(), scopeComboBox.getSelectedIndex());
    }

}
