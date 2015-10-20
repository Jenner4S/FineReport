package com.fr.design.fun;

import com.fr.design.formula.UIFormula;

/**
 * @author richie
 * @date 2015-04-17
 * @since 8.0
 * ��ʽ�༭�����洦��ӿ�
 */
public interface UIFormulaProcessor {
    String MARK_STRING = "UIFormulaProcessor";

    /**
     * ��ͨ�Ĺ�ʽ�༭��������
     * @return ��ʽ�༭��������
     */
    UIFormula appearanceFormula();

    /**
     * ����Ҫ��ʾ��������ʽ����ʱ�Ĺ�ʽ�༭��������
     * @return ��ʽ�༭��������
     */
    UIFormula appearanceWhenReserveFormula();
}
