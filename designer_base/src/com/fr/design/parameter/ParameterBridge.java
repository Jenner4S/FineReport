package com.fr.design.parameter;


import com.fr.general.Background;

/**
 * @author richie
 * @date 2014/11/06
 * @since 8.0
 */
public interface ParameterBridge {

    /**
     * �Ƿ��ӳ�չʾ�������ݣ�Ҳ����˵�Ƿ�Ҫ�ȵ���˲�ѯ֮���ִ�б���
     * @return �����true�����ʾ���֮��ſ�ʼ���㣬false���ʾ����ݲ���Ĭ��ֱֵ�Ӽ��㱨��չ��
     */
    public boolean isDelayDisplayContent();

    /**
     * �Ƿ���ʾ��������
     * @return ��ʾ���������򷵻�true�����򷵻�false
     */
    public boolean isDisplay();

    /**
     * ��ȡ�������汳��
     * @return �������汳��
     *
     */
    public Background getDataBackground();

    /**
     * ��ȡ��������Ŀ��
     * @return ���
     */
    public int getDesignWidth();

    /**
     * ��ȡ�������Ķ��뷽ʽ
     * @return ���������ֶ��뷽ʽ
     */
    public int getPosition();

    public void setDelayDisplayContent(boolean delayPlaying);

    public void setPosition(int align);

    public void setDisplay(boolean showWindow);

    public void setBackground(Background background);
}
