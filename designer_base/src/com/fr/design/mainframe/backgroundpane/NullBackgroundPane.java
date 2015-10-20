package com.fr.design.mainframe.backgroundpane;

import com.fr.design.event.UIObserverListener;
import com.fr.general.Background;
import com.fr.general.Inter;

import java.awt.*;

/**
 * @author zhou
 * @since 2012-5-29����1:12:24
 */
public class NullBackgroundPane extends BackgroundSettingPane {

    public Dimension getPreferredSize(){
        return new Dimension(0,0);
    }

	public void populateBean(Background background) {
		// do nothing.
	}

	public Background updateBean() {
		return null;
	}

	/**
	 * ������Ǽ�һ���۲��߼����¼�
	 *
	 * @param listener �۲��߼����¼�
	 */
	public void registerChangeListener(final UIObserverListener listener) {

	}


    /**
     * �Ƿ����
     * @param background     ����
     * @return    ���򷵻�true
     */
	public boolean accept(Background background) {
		return background == null;
	}

    /**
     * ����
     * @return     ����
     */
	public String title4PopupWindow() {
		return Inter.getLocText("Background-Null");
	}

}