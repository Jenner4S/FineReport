package com.fr.plugin.cell.highlight;

import com.fr.design.condition.ConditionAttrSingleConditionPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.fun.impl.AbstractHighlightProvider;

/**
 * @author richie
 * @date 2015-03-26
 * @since 8.0
 */
public class MyCellHighlight extends AbstractHighlightProvider {

    @Override
    public Class<?> classForHighlightAction() {
        return MyHighlightAction.class;
    }

    @Override
    public ConditionAttrSingleConditionPane appearanceForCondition(ConditionAttributesPane conditionAttributesPane) {
        return new MyHighlightPane(conditionAttributesPane);
    }
}
