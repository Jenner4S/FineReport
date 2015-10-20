package com.fr.design.fun;

import com.fr.design.mainframe.JTemplate;

import java.util.Map;

/**
 * @author richie
 * @date 2015-03-19
 * @since 8.0
 * �Զ���Ԥ����ʽ�ӿ�
 */
public interface PreviewProvider {

    public static final String MARK_STRING = "PreviewProvider";

    /**
     * ���������˵�������
     * @return �����˵�����
     */
    public String nameForPopupItem();

    /**
     * ���������˵���ͼ��·��
     * @return ͼ��·��
     */
    public String iconPathForPopupItem();

    /**
     * ��ͼ��·��
     * @return ��ͼ��·��
     */
    public String iconPathForLarge();

    /**
     * ��������˵�ʱ�������¼�
     * @param jt ��ǰ��ģ�����
     */
    public void onClick(JTemplate<?, ?> jt);

    /**
     * ���ڱ��Ԥ�����͵�����
     * @return Ԥ������
     */
    public int previewTypeCode();

    /**
     * ����Ԥ����ʽ��Я����Ĭ�ϲ�������
     * @return ��������
     */
    public Map<String, Object> parametersForPreview();

}
