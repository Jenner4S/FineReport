package com.fr.design.fun.impl;

import com.fr.design.fun.IndentationUnitProcessor;

/**
 * Created by zhouping on 2015/9/20.
 */
public class AbstractIndentationUnitProcessor implements IndentationUnitProcessor {
    private int indentationUnit = 1;

    public void setIndentationUnit(int value){
        this.indentationUnit = value;
    }

    public int getIndentationUnit(){
        return indentationUnit;
    }

    /**
     * ����paddingunit��ֵ
     * @param value ����
     * @return ���
     */
    public int paddingUnitProcessor(int value){
        return value;
    }

    /**
     * �����spinner����õ�ֵ
     * @param value ����ֵ
     * @return ���
     */
    public int paddingUnitGainFromSpinner(int value){
        return value;
    }
}
