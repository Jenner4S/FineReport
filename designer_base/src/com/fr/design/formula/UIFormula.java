package com.fr.design.formula;

import com.fr.base.Formula;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionListener;

import java.awt.*;

/**
 * @author richie
 * @date 2015-06-24
 * @since 8.0
 */
public interface UIFormula {

    /**
     * �����еĹ�ʽ��ԭ��ʽ���
     * @param formula ��ʽ
     */
    void populate(Formula formula);

    /**
     * ����ָ���ı�������͹�ʽ��ԭ��ʽ���
     * @param formula ��ʽ
     * @param variableResolver ����������
     */
    void populate(Formula formula, VariableResolver variableResolver);

    /**
     * ��ȡ��ʽ���Ĳ���
     * @return ��ʽ
     */
    Formula update();

    /**
     * ��ʾ����
     * @param window ����
     * @param l �Ի��������
     * @return �Ի���
     */
    BasicDialog showLargeWindow(Window window, DialogActionListener l);
}
