package com.fr.design.fun;

import com.fr.design.condition.ConditionAttrSingleConditionPane;
import com.fr.design.condition.ConditionAttributesPane;

/**
 * @author richie
 * @date 2015-03-26
 * @since 8.0
 * �������Խӿ�
 */
public interface HighlightProvider {

    public static final String MARK_STRING = "HighlightProvider";

    /**
     * �������Ե�ʵ����
     * @return ʵ����
     */
    public Class<?> classForHighlightAction();

    /**
     * �������ԵĽ���
     * @param conditionAttributesPane ��������
     * @return ���ý���
     */
    public ConditionAttrSingleConditionPane appearanceForCondition(ConditionAttributesPane conditionAttributesPane);
}
