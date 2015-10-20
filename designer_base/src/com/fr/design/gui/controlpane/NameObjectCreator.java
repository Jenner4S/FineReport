package com.fr.design.gui.controlpane;

import com.fr.general.NameObject;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.ilist.ListModelElement;
import com.fr.stable.Nameable;


public class NameObjectCreator extends AbstractNameableCreator {
	
	/*
	 * alex:����Ĺ��캯��Ϊʲô��Class�����Ǵ�ʵ����?
	 * ��Ϊ��Ӧ��NameObjectCreator��ʼ����ʱ��Ͱ�BasicBeanPane��ʼ������Ӧ������Ҫ�õ��������ʱ������BasicBeanPane�ĳ�ʼ������
	 */
	public NameObjectCreator(String menuName, Class clazz, Class<? extends BasicBeanPane> updatePane) {
		super(menuName, clazz, updatePane);
	}

	public NameObjectCreator(String menuName, String iconPath, Class clazz) {
		super(menuName, iconPath, clazz);
	}
	
	public NameObjectCreator(String menuName, String iconPath, Class clazz, Class<? extends BasicBeanPane> updatePane) {
		super(menuName, iconPath, clazz, updatePane);
	}
	
	public NameObjectCreator(String menuName, String iconPath, Class clazz, Class clazz4Init, Class<? extends BasicBeanPane> updatePane) {
		super(menuName, iconPath, clazz, clazz4Init, updatePane);
	}

	/**
	 * create Nameable
	 * @param helper
	 * @return
	 */
	public Nameable createNameable(UnrepeatedNameHelper helper) {
		try {
			return new NameObject(helper.createUnrepeatedName(this.menuName()), clazzOfInitCase.newInstance());
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * save update bean
	 * @param wrapper
	 * @param bean
	 */
	public void saveUpdatedBean(ListModelElement wrapper, Object bean) {
		((NameObject)wrapper.wrapper).setObject(bean);
	}
}
