package com.fr.design.mainframe.chart.gui.style.axis;

import com.fr.chart.chartattr.Axis;
import com.fr.chart.chartattr.ValueAxis;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

/**
 * ���Ա�, ������. �ڶ�ֵ�����.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2013-1-4 ����05:41:11
 */
public class ChartSecondValuePane extends ChartValuePane {

	private UICheckBox isAlignZeroValue;

	protected JPanel aliagnZero4Second() {// ��� 0ֵ����
		JPanel pane = new JPanel();
		pane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		pane.add(isAlignZeroValue = new UICheckBox(Inter.getLocText("Chart_AxisAlignZeroValueLine"), false));
		return pane;
	}

    public void populateBean(Axis axis) {
        if (axis instanceof ValueAxis) {
            super.populateBean(axis);
            isAlignZeroValue.setSelected(((ValueAxis)axis).isAlignZeroValue());
        }
    }

    public void updateBean(Axis axis) {
    	if(axis instanceof ValueAxis) {
    		ValueAxis valueAxis = (ValueAxis)axis;
    		super.updateBean(valueAxis);
    		valueAxis.setAlignZeroValue(isAlignZeroValue.isSelected());
    	}
    }

    /**
     * ������� �ڶ�ֵ��
     * @return �ڶ�ֵ��
     */
	public String title4PopupWindow() {
		return Inter.getLocText(new String[]{"Second", "Chart_F_Radar_Axis"});
	}
}
