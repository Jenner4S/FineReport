package com.fr.design.fun;

/**
 * @author richie
 * @date 2015-03-23
 * @since 8.0
 * ���ؼ�
 */
public interface FormWidgetOptionProvider extends ParameterWidgetOptionProvider {

    public static final String XML_TAG = "FormWidgetOptionProvider";

    /**
     * ����Ƿ��ǲ�������
     * @return �ǲ��������򷵻�true�����򷵻�false
     */
    public boolean isContainer();

}
