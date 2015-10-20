package com.fr.design.object;

import com.fr.base.BaseUtils;
import com.fr.general.ComparatorUtils;

/**
 * 
 * @author zhou
 * @since 2012-5-29����1:25:42
 */
public class NameObject<T> {
	private String name;
	private T object;

	public NameObject(String name, T object) {
		super();
		this.name = name;
		this.object = object;
	}

	/**
	 * ��ȡ���������
	 */
	public String getName() {
		return name;
	}

	/**
	 * ���ö��������
	 * 
	 * @param name
	 *            ���������
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * ��ȡ������һ�����
	 */
	public T getObject() {
		return this.object;
	}

	/**
	 * ���ð�����һ�����
	 */
	public void setObject(T object) {
		this.object = object;
	}

	public boolean equals(Object obj) {
		return obj instanceof NameObject && ComparatorUtils.equals(((NameObject<?>)obj).name, name) && ComparatorUtils.equals(((NameObject<?>)obj).object, object);
	}

	/**
	 * toString.
	 */
	public String toString() {
		return "Name:" + this.getName() + "\tObject:" + this.getObject();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object clone() throws CloneNotSupportedException {
		NameObject newNameObject = (NameObject)super.clone();
		newNameObject.object = BaseUtils.cloneObject(this.object);
		return newNameObject;
	}

}
