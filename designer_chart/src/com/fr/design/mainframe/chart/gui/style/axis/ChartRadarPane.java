package com.fr.design.mainframe.chart.gui.style.axis;

import com.fr.design.chart.axis.MinMaxValuePane;
import com.fr.chart.chartattr.Axis;
import com.fr.chart.chartattr.RadarAxis;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * ���Ա�, ������, �״�ͼ����.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2013-1-4 ����02:30:36
 */
public class ChartRadarPane extends ChartValuePane{
	
	private UIButtonGroup<Boolean> allMaxMin;

	// ���������Сֵ����. �״��� ���л���ť.
	protected JPanel initMinMaxValue() {
		
		JPanel valuePane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_S_Pane();
		JPanel buttonPane = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
		valuePane.add(buttonPane);
		
		String[] names = new String[]{Inter.getLocText("Chart_Axis_AutoCount"), Inter.getLocText("Chart_Axis_UnitCount")};
        Boolean[] values = new Boolean[]{Boolean.FALSE, Boolean.TRUE};
		allMaxMin = new UIButtonGroup<Boolean>(names, values);
		buttonPane.add(allMaxMin);
		
		allMaxMin.setSelectedItem(true);
		
		allMaxMin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkMinMax();
			}
		});
		
		minValuePane = new MinMaxValuePane();
		valuePane.add(minValuePane);
		
		return valuePane;
	}
	
	private void checkMinMax() {
		if(minValuePane != null && allMaxMin != null) {
			minValuePane.setPaneEditable(allMaxMin.getSelectedItem());
		}
	}

	protected JPanel addAlertPane() {
		return null;
	}

    protected JPanel getAxisTitlePane() {
        return null;
    }

	@Override
	public void populateBean(Axis axis) {
		if(axis instanceof RadarAxis) {
			RadarAxis radarAxis = (RadarAxis)axis;
			
			allMaxMin.setSelectedItem(radarAxis.isAllMaxMin());
		}
		super.populateBean(axis);
		
		checkMinMax();
	}

	@Override
	public void updateBean(Axis axis) {
		super.updateBean(axis);
		if(axis instanceof RadarAxis) {
			RadarAxis radarAxis = (RadarAxis)axis;
			radarAxis.setAllMaxMin(allMaxMin.getSelectedItem());
		}
	}

}
