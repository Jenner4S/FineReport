package com.fr.design.dialog;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.iscrollbar.UIScrollBar;

public abstract class BasicScrollPane<T> extends BasicBeanPane<T>{
	private static final long serialVersionUID = -4293765343535336275L;
	private static final int MAXVALUE = 100;
    private static final int DET_WIDTH_OVER_HEIGHT = 4;
    private static final int DET_HEIGHT = 5;
    private static final int DET_WIDTH = 12;
    private static final int MOUSE_WHELL_SPEED = 5;
	private int maxheight = 280;
	private int beginY = 0;
	protected Color original;

	private UIScrollBar scrollBar;
	protected JPanel leftcontentPane;


	protected abstract JPanel createContentPane();

	protected BasicScrollPane() {
		enableEvents(AWTEvent.MOUSE_WHEEL_EVENT_MASK);
		original = this.getBackground();
		this.setLayout(new BarLayout());
		scrollBar = new UIScrollBar(JScrollBar.VERTICAL) {
			private static final long serialVersionUID = 155777947121777223L;

			@Override
			public int getVisibleAmount() {
				int preferheight = leftcontentPane.getPreferredSize().height;
				if(preferheight <= 0) {
					return 0;
				}
				int e = MAXVALUE * (getHeight() - 1) / preferheight;
				setVisibleAmount(e);
				return e;
			}

			@Override
			public int getMaximum() {
				return MAXVALUE;
			}

		};
		this.add(scrollBar);
		scrollBar.addAdjustmentListener(new AdjustmentListener() {

			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				ajustValues();
			}
		});
		// august:�����ֻ����¼�
		this.addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				int value = scrollBar.getValue();
				value += MOUSE_WHELL_SPEED * e.getWheelRotation();
				scrollBar.setValue(value);
				ajustValues();
			}
		});

		layoutContentPane();
	}
	
	//�ϲ�pane�Ѿ�����scroll����Ҫ���¼����ε�
	protected BasicScrollPane(boolean noScroll) {
		original = this.getBackground();
		this.setLayout(new BarLayout());
		scrollBar = new UIScrollBar(JScrollBar.VERTICAL) {
			private static final long serialVersionUID = 155777947121777223L;

			@Override
			public int getVisibleAmount() {
				int preferheight = leftcontentPane.getPreferredSize().height;
				if(preferheight <= 0) {
					return 0;
				}
				int e = MAXVALUE * (getHeight() - 1) / preferheight;
				setVisibleAmount(e);
				return e;
			}

			@Override
			public int getMaximum() {
				return MAXVALUE;
			}

		};
		this.add(scrollBar);
		layoutContentPane();
	}

	protected void layoutContentPane() {
		leftcontentPane = createContentPane();
		leftcontentPane.setBorder(BorderFactory.createMatteBorder(0, 10, 0, 5, original));
		this.add(leftcontentPane);
	}

	/**
	 * august:�����װ� ��Ҫ�ñʻ�ͼ�������̲ż������
	 * 
	 * @param e
	 */
	protected void ajustValues() {
		doLayout();
	}

	protected class BarLayout implements LayoutManager {

		@Override
		public void addLayoutComponent(String name, Component comp) {

		}

		@Override
		public void removeLayoutComponent(Component comp) {

		}

		@Override
		public Dimension preferredLayoutSize(Container parent) {
			return leftcontentPane.getPreferredSize();
		}

		@Override
		public Dimension minimumLayoutSize(Container parent) {
			return leftcontentPane.getMinimumSize();
		}

		@Override
		public void layoutContainer(Container parent) {
			if(getHeight() >= leftcontentPane.getPreferredSize().height) {
				scrollBar.setEnabled(false);
				scrollBar.setVisible(false);
			} else {
				scrollBar.setEnabled(true);
				scrollBar.setVisible(true);
			}
			maxheight = getHeight() - DET_HEIGHT;
			if ((MAXVALUE - scrollBar.getVisibleAmount()) == 0) {
				beginY = 0;
			} else {
				int preferheight = leftcontentPane.getPreferredSize().height;
				int value = scrollBar.getValue();
				
				int baseValue = MAXVALUE - scrollBar.getVisibleAmount();
				beginY = baseValue == 0 ? 0 : value * (preferheight - maxheight) / baseValue;
				if(MAXVALUE - scrollBar.getVisibleAmount() != 0) {
					beginY = value * (preferheight - maxheight) / (MAXVALUE - scrollBar.getVisibleAmount());
				}
			}
			int width = parent.getWidth();
			int height = parent.getHeight();
			if (leftcontentPane.getPreferredSize().height > maxheight) {
				leftcontentPane.setBounds(0, -beginY, width - scrollBar.getWidth() - DET_WIDTH_OVER_HEIGHT, height + beginY);
				scrollBar.setBounds(width - scrollBar.getWidth() - 1, 0, scrollBar.getWidth(), height);
			} else {
				leftcontentPane.setBounds(0, 0, width - DET_WIDTH, height);
			}
			leftcontentPane.validate();
		}
	}

	@Override
	public T updateBean() {
		return null;
	}
	
	/**
	 * �����ڵ���removeAll�Ժ�ָ�ԭ��pane�Ľṹ�������������ΪBarLayout���ڲ���
	 * @param pane
	 */
	public void reloaPane(JPanel pane){
		this.setLayout(new BarLayout());
		this.add(scrollBar);
		leftcontentPane = pane;
		leftcontentPane.setBorder(BorderFactory.createMatteBorder(0, 10, 0, 5, original));
		this.add(leftcontentPane);
	}
}