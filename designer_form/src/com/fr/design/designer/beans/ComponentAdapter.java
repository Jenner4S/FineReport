
package com.fr.design.designer.beans;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;

import com.fr.design.beans.GroupModel;
import com.fr.design.designer.beans.events.DesignerEditor;

/**
 * ����������ӿ�
 * ��ҪĿ����Ϊ��������ṩ���������Ϊ
 */
public interface ComponentAdapter {

    /**
     * �����ѡ�������ѡ����������ͺ�����ƽ����ϸ�������ƶ���������ǰҪ��������ͼ��
     * һ��ʹ����������ͼ�δ��档
     *
     * @param component Ҫ��ӵ����
     * @param g ��ǰ�������ͼ�������Ķ���
     */
    void paintComponentMascot(Graphics g);

    /**
     * ������ڴ����������Ҽ����ʱ���÷������������ĺ���������ṩ������Ӧ�Ĳ˵�
     *
     * @param ���������˵�������¼�
     *
     * @return �����˵�
     */
    JPopupMenu getContextPopupMenu(MouseEvent e);

    /**
     * Ϊ��ǰ��������������Ա��model, ���鷵��
     * @return BeanPropertyModel
     */
    ArrayList<GroupModel> getXCreatorPropertyModel();

	/**
     * �ṩ˫��������ı༭��
     * @param bean ���˫���ı�������
     * @return ����Ƶı༭��
     */
    public DesignerEditor<? extends JComponent> getDesignerEditor();
    
    /**
     * ʵ�������������������������г�ʼ��
     */
    void initialize();    
}
