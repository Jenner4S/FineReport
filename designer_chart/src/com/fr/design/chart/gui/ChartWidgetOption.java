package com.fr.design.chart.gui;

import com.fr.base.FRContext;
import com.fr.chart.chartattr.Chart;
import com.fr.design.gui.core.WidgetOption;
import com.fr.form.ui.ChartEditor;
import com.fr.form.ui.Widget;

import javax.swing.*;

/**
 * ���� ͼ��ؼ���Ϣ, ����, class, form��ͨ��XcreatorUtils������ ʵ��XChartEditor�ĳ�ʼ��.
 *
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2013-7-5 ����09:59:39
 */
public class ChartWidgetOption extends WidgetOption {
	private static final long serialVersionUID = -6576352405047132226L;
	private String optionName;
	private Icon optionIcon;
	private Class<? extends ChartEditor> widgetClass;
	private Chart chart;

	public ChartWidgetOption(String optionName, Icon optionIcon, Class<? extends ChartEditor> widgetClass, Chart chart) {
		this.optionName = optionName;
		this.optionIcon = optionIcon;
		this.widgetClass = widgetClass;
		this.chart = chart;
	}

	/**
	 * ͨ���� ��Ϣ�� ������Ӧ�Ŀؼ��༭��.
	 *
	 * @return ���ؿؼ��༭��.
	 */
	public Widget createWidget() {
		Class<? extends ChartEditor> clz = widgetClass();
		try {
			ChartEditor widget = clz.newInstance();
			widget.addChart((Chart) chart.clone());
			return widget;
		} catch (InstantiationException e) {
			FRContext.getLogger().error(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			FRContext.getLogger().error(e.getMessage(), e);
		} catch (CloneNotSupportedException e) {
			FRContext.getLogger().error(e.getMessage(), e);
		}

		return null;
	}

	/**
	 * ��Ϣ����, һ����ͼ�����������.
	 *
	 * @return ��������.
	 */
	public String optionName() {
		return this.optionName;
	}

	/**
	 * ��ϢͼƬ, һ����ͼ��ť������ͼ.
	 *
	 * @return ��ϢͼƬ.
	 */
	public Icon optionIcon() {
		return this.optionIcon;
	}

	/**
	 * ͼ��ؼ���ӦEditor����.
	 *
	 * @return ������.
	 */
	public Class<? extends ChartEditor> widgetClass() {
		return this.widgetClass;
	}
}
