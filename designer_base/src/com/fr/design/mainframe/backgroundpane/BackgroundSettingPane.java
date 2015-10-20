package com.fr.design.mainframe.backgroundpane;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.event.UIObserver;
import com.fr.general.Background;

/**
 * @author zhou
 * @since 2012-5-29����1:12:28
 */
public abstract class BackgroundSettingPane extends BasicBeanPane<Background> implements UIObserver {

	public abstract boolean accept(Background background);

	@Override
	public abstract void populateBean(Background background);

	@Override
	public abstract Background updateBean();

	@Override
	public abstract String title4PopupWindow();

	/**
	 * ����Ƿ���Ҫ��Ӧ��ӵĹ۲����¼�
	 *
	 * @return �����Ҫ��Ӧ�۲����¼��򷵻�true�����򷵻�false
	 */
	public boolean shouldResponseChangeListener() {

		return true;
	}
}
