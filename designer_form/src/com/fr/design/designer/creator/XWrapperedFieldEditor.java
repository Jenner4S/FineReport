/**
 * 
 */
package com.fr.design.designer.creator;

import java.awt.Dimension;

import javax.swing.JComponent;

import com.fr.form.ui.FieldEditor;

/**
 *
 *
 * @date: 2014-11-25-����5:08:06
 */
public abstract class XWrapperedFieldEditor extends XFieldEditor {

	/**
	 * ���캯��
	 */
	public XWrapperedFieldEditor(FieldEditor widget, Dimension initSize) {
		super(widget, initSize);
	}

	protected abstract JComponent initEditor();

	protected abstract String getIconName();

    
	/**
	 * ��ȡ��ǰXCreator��һ����װ������
	 * 
	 * @param widgetName ��ǰ�����
	 * 
	 * @return ��װ�ĸ�����
	 * 
	 *
	 * @date 2014-11-25-����4:47:23
	 * 
	 */
	protected XLayoutContainer getCreatorWrapper(String widgetName){
		return new XWScaleLayout();
	}
	
	/**
	 * ����ǰ������ӵ���������
	 * 
	 * @param parentPanel ���������
	 * 
	 *
	 * @date 2014-11-25-����4:57:55
	 * 
	 */
	protected void addToWrapper(XLayoutContainer parentPanel, int width, int minHeight){			
		this.setSize(width, minHeight);
		parentPanel.add(this);
	}
    
    /**
	 * �˿ؼ�������Ӧ����Ҫ����ԭ���߶�
	 * 
	 * @return ���򷵻�true
	 */
	@Override
	public boolean shouldScaleCreator() {
		return true;
	}
}
