package com.fr.design.fun;

/**
 * Created by zhouping on 2015/9/20.
 */
public interface IndentationUnitProcessor {
    String MARK_STRING = "IndentationProcessor";

    int getIndentationUnit();

    void setIndentationUnit(int value);

    /**
     * ����paddingunit
     * @param value ����
     * @return ���
     */
    int paddingUnitProcessor(int value);

    /**
     * ��spinner�������ֵ�����������paddingunit
     * @param value ����ֵ
     * @return ���
     */
    int paddingUnitGainFromSpinner(int value);
}
