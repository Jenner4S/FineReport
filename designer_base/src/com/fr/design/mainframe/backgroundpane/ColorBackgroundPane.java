package com.fr.design.mainframe.backgroundpane;

import com.fr.base.background.ColorBackground;
import com.fr.design.event.UIObserverListener;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Background;
import com.fr.general.Inter;
import com.fr.design.style.color.NewColorSelectPane;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * @author zhou
 * @since 2012-5-29����1:12:14
 */
public class ColorBackgroundPane extends BackgroundSettingPane {

	private NewColorSelectPane detailColorSelectPane;

	public ColorBackgroundPane() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());

		detailColorSelectPane = new NewColorSelectPane();
		this.add(detailColorSelectPane, BorderLayout.NORTH);
	}

	public void populateBean(Background background) {
		ColorBackground colorBackgroud = (ColorBackground) background;
		this.detailColorSelectPane.setColor(colorBackgroud.getColor());
	}

	public Background updateBean() {
		this.detailColorSelectPane.updateUsedColor();
		return ColorBackground.getInstance(this.detailColorSelectPane.getNotNoneColor());
	}

	/**
	 * ������Ǽ�һ���۲��߼����¼�
	 *
	 * @param listener �۲��߼����¼�
	 */
	public void registerChangeListener(final UIObserverListener listener) {
		detailColorSelectPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				listener.doChange();
			}
		});
	}

	@Override
	/**
	 * �Ƿ�ΪColorBackground ����
	 * 
	 * @param background ����
	 * @return ͬ��
	 * 
	 */
	public boolean accept(Background background) {
		return background instanceof ColorBackground;
	}

	@Override
	/**
	 * ��������
	 * @return ͬ��
	 */
	public String title4PopupWindow() {
		return Inter.getLocText("Color");
	}
}
