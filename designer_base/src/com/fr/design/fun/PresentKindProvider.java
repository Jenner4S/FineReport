package com.fr.design.fun;

import com.fr.base.present.Present;
import com.fr.design.beans.FurtherBasicBeanPane;

/**
 * @author richie
 * @date 2015-05-22
 * @since 8.0
 * ��̬���ͽӿ�
 */
public interface PresentKindProvider {

    String MARK_STRING = "PresentKindProvider";

    /**
     * ��̬���ý���
     * @return ��̬���ý���
     */
    FurtherBasicBeanPane<? extends Present> appearanceForPresent();

    /**
     * ����̬�����������ʾ������
     * @return ����
     */
    String title();

    /**
     * ����̬��Ӧ����
     * @return ��
     */
    Class<? extends Present> kindOfPresent();

    /**
     * �˵���ݼ�
     * @return ��ݵ��Ӧ���ַ�
     */
    char mnemonic();
}
