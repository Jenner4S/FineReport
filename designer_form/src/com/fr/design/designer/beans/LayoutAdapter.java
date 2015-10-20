package com.fr.design.designer.beans;

import com.fr.design.beans.GroupModel;
import com.fr.design.designer.creator.XCreator;

/**
 * �ýӿ���LayoutManager��BeanInfo�ࡣ��׼Javaƽ̨û���ṩ���ֹ�������BeanInfo�࣬
 * ���ڽ�����ƹ�����˵����һЩ�������Ϊ��
 * @since 6.5.3
 */
public interface LayoutAdapter {

    /**
     * ��������״̬ʱ��������ƶ���ĳ�������Ϸ�ʱ������������в��ֹ������������øò���
     * ������������accept��������ǰλ���Ƿ���Է��ã����ṩ����ı�ʶ�������ɫ�����ʶ����
     * ����BorderLayout�У����ĳ����λ�Ѿ���������������ʱӦ�÷���false��ʶ�����򲻿���
     * ���á�
     *@param creator ���
     *@param x ��ӵ�λ��x����λ���������container��
     *@param y ��ӵ�λ��y����λ���������container��
     *@return �Ƿ���Է���
     */
    boolean accept(XCreator creator, int x, int y);

    /**
     * �еĿؼ�����ק������С����Ҫ���������������¼����µ�ǰ�ĳߴ��Ƿ���ʣ���������ʣ�����Ҫ����fixһ��
     * @param creator ���
     */
    void fix(XCreator creator);

    /**
     * �����ComponentAdapter��������ʱ��������ֲ��ֹ�������Ϊ�գ���̶����øò��ֹ�������
     * addComp�������������ľ�����ӡ��ڸ÷����ڣ����ֹ����������ṩ����Ĺ��ܡ�
     * @param creator ����ӵ������
     * @param x ��ӵ�λ��x����λ���������container��
     * @param y ��ӵ�λ��y����λ���������container��
     * @return �Ƿ���ӳɹ����ɹ�����true������false
     */
    boolean addBean(XCreator creator, int x, int y);

    /**
     * ���ظò��ֹ�����������Painter��Ϊ�����ṩ����λ�õı�ʶ��
     */
    HoverPainter getPainter();

    /**
     * ��ʾparent�������child�����CardLayout����ʾĳ������ʾ������������
     * @param child ���
     */
    void showComponent(XCreator child);

    void addNextComponent(XCreator dragged);

    /**
     * �������˳��ǰ����
     * @param target Ŀ�����
     * @param added �������
     */
    void addBefore(XCreator target, XCreator added);

    /**
     * �������˳������
     * @param target Ŀ�����
     * @param added �������
     */
    void addAfter(XCreator target, XCreator added);

    /**
     * �ܷ���ø������
     * @return ���򷵻�true
     */
    boolean canAcceptMoreComponent();

    ConstraintsGroupModel getLayoutConstraints(XCreator creator);

    GroupModel getLayoutProperties();
    
    /**
     * ɾ�����
     * @param creator ���
     * @param initWidth ���֮ǰ���
     * @param initHeight ���֮ǰ�߶�
     */
    void removeBean(XCreator creator, int initWidth, int initHeight);
}
