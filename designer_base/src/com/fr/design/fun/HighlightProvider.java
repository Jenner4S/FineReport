package com.fr.design.fun;

import com.fr.design.condition.ConditionAttrSingleConditionPane;
import com.fr.design.condition.ConditionAttributesPane;

/**
 * @author richie
 * @date 2015-03-26
 * @since 8.0
 * 条件属性接口
 */
public interface HighlightProvider {

    public static final String MARK_STRING = "HighlightProvider";

    /**
     * 条件属性的实现类
     * @return 实现类
     */
    public Class<?> classForHighlightAction();

    /**
     * 条件属性的界面
     * @param conditionAttributesPane 条件界面
     * @return 设置界面
     */
    public ConditionAttrSingleConditionPane appearanceForCondition(ConditionAttributesPane conditionAttributesPane);
}