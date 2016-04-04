package com.fr.plugin.chart.glyph;

import java.awt.geom.Rectangle2D;

/**
 * �����µ�ͼ������
 */
public interface VanChartPlotGlyphInterface {

    /**
     *  �Ի�ͼ���е������Ჿ�ֽ��в���,����ȥͼ��߽������������
     * @param chartOriginalBounds ԭʼͼ��߽磨������������ʱ�õ���
     * @param resolution �ֱ���
     */
    public void layoutAxisGlyph(Rectangle2D chartOriginalBounds, int resolution);

    /**
     * �Ƿ��Ǽ��ˢ��
     * @return �Ƿ��Ǽ��ˢ��
     */
    public boolean isMonitorRefresh();
}
