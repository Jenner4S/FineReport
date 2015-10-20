package com.fr.design.chart;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.dialog.JWizardPanel;

/**
* @author kunsnat E-mail:kunsnat@gmail.com
* @version ����ʱ�䣺2012-4-23 ����03:07:52
* ��˵��: ͼ���򵼽���
 */
public abstract class ChartWizardPane extends JWizardPanel {

	public ChartWizardPane() {
		super();
	}
	
	public abstract void update(ChartCollection cc);
	
	public abstract void populate(ChartCollection cc);
}
