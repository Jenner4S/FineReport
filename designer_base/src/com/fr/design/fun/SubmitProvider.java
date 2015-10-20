package com.fr.design.fun;

import com.fr.design.beans.BasicBeanPane;

/**
 * �Զ����ύ�ӿ�
 */
public interface SubmitProvider {

    String MARK_STRING = "SubmitProvider";

    /**
     * ���ý���
     * @return ����
     */
    BasicBeanPane appearanceForSubmit();

    /**
     * ����ѡ��
     * @return �������е��ı�
     */
    String dataForSubmit();

    /**
     * ��
     * @return �ύ�ļ�
     */
    String keyForSubmit();
}
