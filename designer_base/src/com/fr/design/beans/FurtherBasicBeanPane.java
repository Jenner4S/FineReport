package com.fr.design.beans;

/**
 * 
 * @author zhou
 * @since 2012-5-30����12:12:42
 */
public abstract class FurtherBasicBeanPane<T> extends BasicBeanPane<T> {
	/**
	 * �Ƿ���ָ������
	 * @param ob ����
	 * @return �Ƿ���ָ������
	 */
	public abstract boolean accept(Object ob);

	/**
	 * titleӦ����һ�����ԣ���ֻ�ǶԻ���ı���ʱ�õ���������������ʱ��Ҳ���õõ�
	 * @return �绯�����
	 */
	@Override
	public abstract String title4PopupWindow();

	/**
	 * ����
	 */
	public abstract void reset();
	
}
