package com.fr.design.fun;

/**
 * Created by zhouping on 2015/9/20.
 */
public interface IndentationUnitProcessor {
    String MARK_STRING = "IndentationProcessor";

    int getIndentationUnit();

    void setIndentationUnit(int value);

    /**
     * 处理paddingunit
     * @param value 输入
     * @return 输出
     */
    int paddingUnitProcessor(int value);

    /**
     * 从spinner获得缩进值，并处理后变成paddingunit
     * @param value 输入值
     * @return 输出
     */
    int paddingUnitGainFromSpinner(int value);
}
