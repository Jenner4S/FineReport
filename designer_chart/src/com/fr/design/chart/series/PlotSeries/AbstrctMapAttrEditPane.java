package com.fr.design.chart.series.PlotSeries;

import com.fr.chart.base.MapSvgAttr;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 7.1.1
 */
public interface AbstrctMapAttrEditPane {

    /**
     * ���½���
     * @param attr  ��ͼ����
     */
    public void populateMapAttr(MapSvgAttr attr);

    /**
     * ����MapSvgAttr
     * @return  ��������
     */
    public MapSvgAttr updateCurrentAttr();
}
