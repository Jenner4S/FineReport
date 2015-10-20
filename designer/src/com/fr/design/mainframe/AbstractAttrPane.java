package com.fr.design.mainframe;

import com.fr.design.gui.frpane.AbstractAttrNoScrollPane;
import com.fr.design.gui.iscrollbar.UIScrollBar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 * ��Ԫ�����Ա��ÿ��tab��Ӧ��pane���Ѿ�ʵ���˹��������ܣ����ҳ��װ���£����Զ�����
 * ���㣺���಻��Ҫд���캯���ˣ����е����������createContentPane()�������ɵ�pane����,������Ҫ����border��
 */
public abstract class AbstractAttrPane extends AbstractAttrNoScrollPane {
	private static final int MAXVALUE = 100;
    private static final int TITLE_HEIGHT = 50;
    private static final int MOUSE_WHEEL_SPEED = 5;
    private static final int CONTENTPANE_WIDTH_GAP = 4;
    private static final int SCROLLBAR_WIDTH = 8;
	private int maxHeight = 280;
	private int beginY = 0;

	private UIScrollBar scrollBar;

	public AbstractAttrPane() {
		this.setLayout(new BarLayout());
		scrollBar = new UIScrollBar(UIScrollBar.VERTICAL) {

			@Override
			public int getVisibleAmount() {
				int preferredHeight = leftContentPane.getPreferredSize().height;
				int e = MAXVALUE * (maxHeight) / preferredHeight;
				setVisibleAmount(e);
				return e;
			}

			@Override
			public int getMaximum() {
				return MAXVALUE;
			}

		};
		this.add(scrollBar);
		this.add(leftContentPane);

		scrollBar.addAdjustmentListener(new AdjustmentListener() {

			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				adjustValues();
			}
		});
		// august:�����ֻ����¼�
		this.addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				int value = scrollBar.getValue();
				value += MOUSE_WHEEL_SPEED * e.getWheelRotation();
				scrollBar.setValue(value);
				adjustValues();
			}
		});
	}

	/**
	 * �������Ĺ���������������Ĳ��ֹ�����
	 */
	protected class BarLayout implements LayoutManager {

		@Override
		public void addLayoutComponent(String name, Component comp) {

		}

		@Override
		public void removeLayoutComponent(Component comp) {

		}

		@Override
		public Dimension preferredLayoutSize(Container parent) {
			return leftContentPane.getPreferredSize();
		}

		@Override
		public Dimension minimumLayoutSize(Container parent) {
			return leftContentPane.getMinimumSize();
		}

		@Override
		public void layoutContainer(Container parent) {
			maxHeight = CellElementPropertyPane.getInstance().getHeight() - TITLE_HEIGHT;
			if ((MAXVALUE - scrollBar.getVisibleAmount()) == 0) {
				beginY = 0;
			} else {
				int preferredHeight = leftContentPane.getPreferredSize().height;
				int value = scrollBar.getValue();
				beginY = value * (preferredHeight - maxHeight) / (MAXVALUE - scrollBar.getVisibleAmount());
			}
			int width = parent.getWidth();
			int height = parent.getHeight();
			if (leftContentPane.getPreferredSize().height > maxHeight) {
				leftContentPane.setBounds(0, -beginY, width - scrollBar.getWidth() - CONTENTPANE_WIDTH_GAP, height + beginY);
				scrollBar.setBounds(width - scrollBar.getWidth() - 1, 0, scrollBar.getWidth(), height);
			} else {
				leftContentPane.setBounds(0, 0, width - SCROLLBAR_WIDTH - CONTENTPANE_WIDTH_GAP, height);
			}
			leftContentPane.validate();
		}
	}
}
