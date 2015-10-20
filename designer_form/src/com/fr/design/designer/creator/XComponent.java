package com.fr.design.designer.creator;

import java.awt.Rectangle;

import javax.swing.JComponent;

import com.fr.design.mainframe.BaseJForm;
import com.fr.design.mainframe.FormDesigner;

public interface XComponent {

	/**
	 * ���������λ�ô�С
	 * @return ����bound
	 */
	Rectangle getBounds();

	/**
	 * ���������λ�ô�С
	 * @param oldbounds  bound��С
	 */
	void setBounds(Rectangle oldbounds);

	/**
	 * ���ɹ��߲˵�����
	 * @param jform BaseJForm��
	 * @param formeditor ��ƽ������
	 * @return ���ع��߽���
	 */
	JComponent createToolPane(BaseJForm jform, FormDesigner formeditor);
	
}
