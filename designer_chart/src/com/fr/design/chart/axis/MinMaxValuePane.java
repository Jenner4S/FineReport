package com.fr.design.chart.axis;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import com.fr.base.Formula;
import com.fr.design.chart.ChartSwingUtils;
import com.fr.chart.base.ChartBaseUtils;
import com.fr.chart.chartattr.Axis;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;

public class MinMaxValuePane extends JPanel {
	private static final long serialVersionUID = 3353266754022091576L;
	private UICheckBox maxCheckBox;
	private UITextField maxValueField;
	private UICheckBox minCheckBox;
	private UITextField minValueField;

	// 主次要刻度单位
	private UICheckBox isCustomMainUnitBox;
	private UITextField mainUnitField;

	private UICheckBox isCustomSecUnitBox;
	private UITextField secUnitField;

	public MinMaxValuePane() {
		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
		double[] columnSize = { p, f };
		double[] rowSize = { p, p, p, p};
		Component[][] components = new Component[][]{
				new Component[]{minCheckBox = new UICheckBox(Inter.getLocText(new String[]{"Custom", "Min_Value"})),minValueField = new UITextField(6)},
				new Component[]{maxCheckBox = new UICheckBox(Inter.getLocText(new String[]{"Custom", "Max_Value"})),maxValueField = new UITextField(6)},
				new Component[]{isCustomMainUnitBox = new UICheckBox(Inter.getLocText("MainGraduationUnit")),mainUnitField = new UITextField(6)},
				new Component[]{isCustomSecUnitBox = new UICheckBox(Inter.getLocText("SecondGraduationUnit")),secUnitField = new UITextField(6)},

		};

		JPanel panel = TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
		this.setLayout(new BorderLayout());
		this.add(panel,BorderLayout.CENTER);

		for(int i = 0; i < components.length; i++) {
			((UICheckBox)components[i][0]).addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					checkBoxUse();
				}
			});
			ChartSwingUtils.addListener((UICheckBox)components[i][0], (UITextField)components[i][1]);	
		}
	}

	private void checkBoxUse() {
		minValueField.setEnabled(minCheckBox.isSelected());
		maxValueField.setEnabled(maxCheckBox.isSelected());

		mainUnitField.setEnabled(isCustomMainUnitBox.isSelected());
		secUnitField.setEnabled(isCustomSecUnitBox.isSelected());
	}

	public void setPaneEditable(boolean isEditable) {
		minCheckBox.setEnabled(isEditable);
		maxCheckBox.setEnabled(isEditable);
		minValueField.setEnabled(isEditable);
		maxValueField.setEnabled(isEditable);

		mainUnitField.setEnabled(isEditable);
		secUnitField.setEnabled(isEditable);
		isCustomMainUnitBox.setEnabled(isEditable);
		isCustomSecUnitBox.setEnabled(isEditable);
		checkBoxUse();
	}

	public void populate(Axis axis) {
		if(axis == null) {
			return;
		}

		if (axis.isCustomMinValue()) {
			minCheckBox.setSelected(true);
			if(axis.getMinValue() != null) {
				minValueField.setText(axis.getMinValue().toString());
			}
		} 

		// 最大值
		if (axis.isCustomMaxValue()) {
			maxCheckBox.setSelected(true);
			if(axis.getMaxValue() != null) {
				maxValueField.setText(axis.getMaxValue().toString());
			}
		} 

		// 主次刻度单位
		if (axis.isCustomMainUnit()) {
			isCustomMainUnitBox.setSelected(true);
			if(axis.getMainUnit() != null) {
				mainUnitField.setText(axis.getMainUnit().toString());
			}
		} 

		if(axis.isCustomSecUnit()) {
			isCustomSecUnitBox.setSelected(true);
			if(axis.getSecUnit() != null) {
				secUnitField.setText(axis.getSecUnit().toString());
			}
		}

		checkBoxUse();
	}

	public void update(Axis axis) {
		if (axis == null) {
			return;
		}
		// 最大最小值
		if (minCheckBox.isSelected()) {
			axis.setCustomMinValue(!StringUtils.isEmpty(minValueField.getText()));
			axis.setMinValue(new Formula(minValueField.getText()));
		} else {
			axis.setCustomMinValue(false);
		}
		if (maxCheckBox.isSelected()) {
			axis.setCustomMaxValue(!StringUtils.isEmpty(maxValueField.getText()));
			axis.setMaxValue(new Formula(maxValueField.getText()));
		} else {
			axis.setCustomMaxValue(false);
		}

		// 主要刻度
		if (isCustomMainUnitBox.isSelected()){
			String increment = mainUnitField.getText();
			if(StringUtils.isEmpty(increment)) {
				axis.setCustomMainUnit(false);
				axis.setMainUnit(null);
			} else {
				axis.setCustomMainUnit(true);
				Formula formula = new Formula(increment);
				Number number = ChartBaseUtils.formula2Number(formula);
				if(number != null && number.doubleValue() < 0) {
					axis.setMainUnit(new Formula("10"));
				} else {
					axis.setMainUnit(formula);
				}
			}
		} else {
			axis.setCustomMainUnit(false);
		}

		if (isCustomSecUnitBox.isSelected()){
			String increment = secUnitField.getText();
			if(StringUtils.isEmpty(increment)) {
				axis.setCustomSecUnit(false);
				axis.setSecUnit(null);
			} else {
				axis.setCustomSecUnit(true);
				Formula formula = new Formula(increment);
				Number number = ChartBaseUtils.formula2Number(formula);
				if(number != null && number.doubleValue() < 0) {
					axis.setSecUnit(new Formula("10"));
				} else {
					axis.setSecUnit(formula);
				}
			}
		} else {
			axis.setCustomSecUnit(false);
		}
	}
}
