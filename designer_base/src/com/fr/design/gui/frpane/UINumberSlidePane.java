/**
 * 
 */
package com.fr.design.gui.frpane;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.itextfield.UINumberField;
import com.fr.design.gui.style.NumberDragBar;

/**
 * ��ק�Ļ����Ͷ�Ӧ����ֵ������
 * 
 * @author jim
 * @date 2014-8-20
 */
public class UINumberSlidePane extends BasicBeanPane<Double> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3863821662526890552L;
	private static final double MIN_DIFF_VALUE = 5.0;
	private NumberDragBar dragBar;
	private UINumberField manual;
	private double minValue;
	private double maxValue;
	private double value;

	public UINumberSlidePane(double minValue, double maxValue) {
		this.minValue = minValue;
		this.maxValue = maxValue;
		dragBar = new NumberDragBar((int)minValue, (int)maxValue);
		manual =  new UINumberField(3, 0);
		manual.setHorizontalAlignment(manual.CENTER);
		this.setLayout(new BorderLayout(4, 0));
		this.add(dragBar, BorderLayout.CENTER);
		this.add(manual, BorderLayout.EAST);
		dragBar.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				checkValue(dragBar.getValue());
			}
		});
		manual.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				checkValue(manual.getValue());
			}
		});
	}
	
	
	/**
	 * �ֶ����뻬��ֵ��check��
	 * @param value ֵ
	 */
	public void checkValue(double value) {
		value = Math.max(value, minValue);
		value = Math.min(value, maxValue);
		if (this.value == value) {
			return;
		}
		double diff = Math.abs(value - this.value);
		manual.setValue(value);
		dragBar.setValue((int)value);
		// �϶��ıȽ�С�����Ȳ�����������ʾ��С����ʱ��5px�ż�
		if (diff < MIN_DIFF_VALUE) {
			return;
		}
		this.value = value;
		fireStateChanged();
	}
	
	public double getValue() {
		return this.value;
	}
	
	@Override
	public void populateBean(Double ob) {
		dragBar.setValue(ob.intValue());
		manual.setValue(ob);
		this.value = ob;
	}
	
	@Override
	public Double updateBean() {
		return manual.getValue();
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension dim = new Dimension();
		dim.width = super.getPreferredSize().width;
		dim.height = super.getPreferredSize().height + 2;
		return dim;
	}

	@Override
	protected String title4PopupWindow() {
		return null;
	}
	
	/**
	 * ��Ӹı��¼�
	 * @param l  �仯�¼�
	 */
	public void addChangeListener(ChangeListener l) {
		this.listenerList.add(ChangeListener.class, l);
	}

	/**
	 * remove ChangeListener�¼�
	 * @param l ChangeListener�¼�
	 */
	public void removeChangeListener(ChangeListener l) {
		this.listenerList.remove(ChangeListener.class, l);
	}

	protected void fireStateChanged() {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ChangeListener.class) {
				((ChangeListener) listeners[i + 1]).stateChanged(new ChangeEvent(this));
			}
		}
	}
	
}
