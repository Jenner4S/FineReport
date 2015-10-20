package com.fr.design.gui.style;

import com.fr.design.fun.impl.AbstractIndentationUnitProcessor;

/**
 * Created by zhouping on 2015/9/21.
 */
public class DefaultIndentationUnitProcessor extends AbstractIndentationUnitProcessor {
    private static final int INDENTATION_PADDING_ARG = 1;

    @Override
    public int getIndentationUnit() {
        return INDENTATION_PADDING_ARG;
    }

    /**
     * Ĭ�ϴ���ϵ����1
     * @param value ����
     * @return ���
     */
    @Override
    public int paddingUnitProcessor(int value){
        return (value / INDENTATION_PADDING_ARG);
    }

    /**
     * Ĭ�ϴ���ϵ����1������spinner�ľ���ptֵ
     * @param value ����ֵ
     * @return ���
     */
    @Override
    public int paddingUnitGainFromSpinner(int value) {
        return (value * INDENTATION_PADDING_ARG);
    }

}
