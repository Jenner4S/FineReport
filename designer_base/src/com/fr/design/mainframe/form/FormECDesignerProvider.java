package com.fr.design.mainframe.form;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import com.fr.design.menu.MenuDef;
import com.fr.design.menu.ShortCut;
import com.fr.design.menu.ToolBarDef;
import com.fr.form.FormElementCaseProvider;

public interface FormECDesignerProvider {
	
	public static final String XML_TAG = "FormElementCaseDesigner";

    /**
     * ѡ��Ŀ��� ��ӦMenu
     * @return ����MenuDef����.
     */
	public MenuDef[] menus4Target();

    /**
     * ��ȡ����
     */
    public void requestFocus() ;

    /**
     * �������˵������ֺŵ�
     * @return   �������˵�����
     */
	public ToolBarDef[] toolbars4Target();

    /**
     * ���ı����Ĺ��߰�ť���Ƽ�������
     * @return ���߰�ť
     */
    public JComponent[] toolBarButton4Form();

    /**
     * ģ��˵�
     * @return    ���ز˵�
     */
	public ShortCut[] shortcut4TemplateMenu();

    /**
     *��ǰ���ڱ༭��elementcase
     * @return   ��ǰ���ڱ༭��elementcase
     */
	public FormElementCaseProvider getEditingElementCase();

    /**
     *  ���Ͻ����Ա�
     * @return     �������
     */
	public JComponent getEastDownPane();

    /**
     *   ���½ǿؼ���������չ����
     * @return    �������
     */
	public JComponent getEastUpPane();

    /**
     * ��ȡ��ǰElementCase������ͼ
     * @param elementCaseContainerSize ����ͼ�Ĵ�С
     * @return    ͼ
     */
	public BufferedImage getElementCaseImage(Dimension elementCaseContainerSize);

}
