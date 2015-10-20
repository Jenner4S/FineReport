/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.gui.frpane;

import com.fr.base.Utils;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.design.gui.itextfield.UINumberField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.stable.StringUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;

/**
 * For input Number.
 */
public abstract class UnitInputPane extends BasicPane {
    private static final double NUM_POINT =  0.000001;
	private int scale = -1;
	String title;

	public UnitInputPane(int scale, String name) {
		this.scale = scale;
		title = name;
		initComponents();
	}

	protected void initComponents() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		JPanel centerPane = FRGUIPaneFactory.createTitledBorderPane("");
		this.add(centerPane, BorderLayout.CENTER);
		centerPane.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 30));
		UILabel titleLabel = new UILabel(title + ":");
		centerPane.add(titleLabel);

		// Denny���ڶԻ����м���JSpinner����
		numberFieldSpinner = new UIBasicSpinner(new SpinnerNumberModel(0, 0, 999, 1));
		GUICoreUtils.setColumnForSpinner(numberFieldSpinner, 24);
		numberFieldSpinner.setPreferredSize(new Dimension(60, 20));
		numberFieldSpinner.setMinimumSize(new Dimension(60, 20));
		centerPane.add(numberFieldSpinner);
		numberFieldSpinner.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				if (Math.abs((((Double) numberFieldSpinner.getValue()).floatValue() - popValue)) > NUM_POINT) {
					changed = true;
				}
			}
		});

		unitLabel = new UILabel("");
		centerPane.add(unitLabel);
	}

	public void setUnitText(String unit) {
		this.unitLabel.setText("(" + unit + ")");
	}

	public void populate(float floatValue) {
		popValue = floatValue;
		numberFieldSpinner.setModel(new SpinnerNumberModel(0.00, 0.00, 999.00, 0.01));

		JFormattedTextField temp = GUICoreUtils.getSpinnerTextField(numberFieldSpinner);
		addChangeListener(temp);
		BigDecimal de = new BigDecimal(floatValue + "");
		if (scale > 0) {
			floatValue = de.setScale(scale, BigDecimal.ROUND_DOWN).floatValue();
		} else {
			floatValue = de.floatValue();
		}
        //ѡ�ж���, �����п���ȫһ�µĻ�, �Ͳ���ʾֵ��.
		temp.setText(floatValue == 0 ? StringUtils.EMPTY : Utils.convertNumberStringToString(new Float(floatValue)));

		// denny:Ĭ��Ӧ��Ϊѡ�У������û��޸�
		temp.selectAll();
	}

	private void addChangeListener(JFormattedTextField temp) {
		temp.removeKeyListener(kl);
		temp.addKeyListener(kl);
		temp.removeMouseListener(ml);
		temp.addMouseListener(ml);
	}

	// �׸���ֻ��Ϊ��֪ͨ����ֵû��
	public double update() throws ValueNotChangeException {
		// ֵû��Ͳ���
		if (!changed) {
            throw vncExp;
        }
		// Denny: get numberFieldSpinner �� TextField
		JFormattedTextField temp = GUICoreUtils.getSpinnerTextField(numberFieldSpinner);

		try {
			if (temp.getText().length() == 0) {
				return 0;
			}
			BigDecimal de = new BigDecimal(temp.getText());
			if (scale > 0) {
				return de.setScale(scale, BigDecimal.ROUND_DOWN).floatValue();
			} else {
				return de.floatValue();
			}
		} catch (NumberFormatException numberFormatException) {

			return UINumberField.ERROR_VALUE;
		}
	}

	public static class ValueNotChangeException extends Exception {
	}

	private UILabel unitLabel;
	private UIBasicSpinner numberFieldSpinner;
	private float popValue;
	private boolean changed = false;
	private ValueNotChangeException vncExp = new ValueNotChangeException();

	private KeyListener kl = new KeyListener() {

		@Override
		public void keyTyped(KeyEvent arg0) {
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
		}

		@Override
		public void keyPressed(KeyEvent arg0) {
			changed = true;
		}
	};

	private MouseListener ml = new MouseListener() {

		@Override
		public void mouseReleased(MouseEvent arg0) {
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			changed = true;
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
		}

		@Override
		public void mouseClicked(MouseEvent arg0) {
		}
	};
}
