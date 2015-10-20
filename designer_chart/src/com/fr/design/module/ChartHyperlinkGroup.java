package com.fr.design.module;

import com.fr.chart.web.ChartHyperPoplink;
import com.fr.chart.web.ChartHyperRelateCellLink;
import com.fr.chart.web.ChartHyperRelateFloatLink;
import com.fr.design.chart.series.SeriesCondition.impl.ChartHyperPoplinkPane;
import com.fr.design.chart.series.SeriesCondition.impl.ChartHyperRelateCellLinkPane;
import com.fr.design.chart.series.SeriesCondition.impl.ChartHyperRelateFloatLinkPane;
import com.fr.design.chart.series.SeriesCondition.impl.FormHyperlinkPane;
import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.gui.frpane.BaseHyperlinkGroup;
import com.fr.general.Inter;
import com.fr.js.FormHyperlinkProvider;
import com.fr.stable.ArrayUtils;
import com.fr.stable.bridge.StableFactory;

/**
 *  ͼ��ĳ������ӽ���. ��һ���HyperlinkGroupPane����ͼ�����س�������
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2013-6-26 ����04:55:10
 */
public class ChartHyperlinkGroup extends BaseHyperlinkGroup {

	/**
	 * ����֧�ֵĳ�����������
	 * @return NameableCreator[]
	 */
	public NameableCreator[] getHyperlinkCreators() {
		FormHyperlinkProvider formHyperlink = StableFactory.getMarkedInstanceObjectFromClass(FormHyperlinkProvider.XML_TAG, FormHyperlinkProvider.class);
		
		NameableCreator[] realted4CharthyperUse = { 
				new NameObjectCreator(Inter.getLocText("FR-Designer_Chart_Float_chart"), ChartHyperPoplink.class, ChartHyperPoplinkPane.CHART_NO_RENAME.class),
				new NameObjectCreator(Inter.getLocText("FR-Designer_Chart_Cell"), ChartHyperRelateCellLink.class, ChartHyperRelateCellLinkPane.CHART_NO_RENAME.class),
				new NameObjectCreator(Inter.getLocText("FR-Designer_Chart_Float"), ChartHyperRelateFloatLink.class, ChartHyperRelateFloatLinkPane.CHART_NO_RENAME.class),
                new NameObjectCreator(Inter.getLocText("FR-Designer_Hyperlink-Form_link"), formHyperlink.getClass(), FormHyperlinkPane.class)};
		return (NameableCreator[])ArrayUtils.addAll(super.getHyperlinkCreators(), realted4CharthyperUse);
		
	}
}
