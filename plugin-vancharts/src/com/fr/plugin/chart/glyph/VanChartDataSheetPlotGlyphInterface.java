package com.fr.plugin.chart.glyph;

import com.fr.chart.chartglyph.DataSeries;
import com.fr.chart.chartglyph.LegendItem;
import com.fr.plugin.chart.glyph.axis.VanChartBaseAxisGlyph;

import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;

/**
 * Created by Mitisky on 16/1/21.
 * �������ݱ��ͼ�ζ���
 */
public interface VanChartDataSheetPlotGlyphInterface {

    /**
     * �������ݱ��ʱ�����������Ի�ͼ���Ĳü����ֲ������ݱ�ʹ�ã���Ҫ�Ի�ͼ���������ü�,
     * ��Ϊ�����λ�ã�����ֻ��Ҫ�Ҳ��y�����������ƾͺ��ˣ�x��������Ҫ����unitLength�ȡ�
     * @param leftGap ��ͼ����໹��Ҫ�ü�leftGap
     */
    public void adjustAxisGlyphWithLeftGap(double leftGap);

    /**
     * �������ݱ��ʱ����Ҫ�Ի�ͼ���·������ü���
     * �·�x������ҲҪ����ƽ�Ƶȼ��㣬y��������Ҫ����unitLength�ȡ�
     * @param bottomGap ��ͼ���·�����Ҫ�ü�rightGap
     */
    public void adjustAxisGlyphWithBottomGap(double bottomGap);

    /**
     * ���ؾ���Glyph�ı߽�
     */
    public Rectangle2D getBounds();

    /**
     * ���þ���Glyph�ı߽�
     * @param bounds ���α߽�
     */
    public void setBounds(RectangularShape bounds);


    public VanChartBaseAxisGlyph getDefaultXAxisGlyph();

    public int getBottomXAxisCount();

    public LegendItem[] createLegendItems();

    /**
     * �Ѿ���ӵ�ϵ�е�����
     *
     * @return ϵ�е�����
     */
    public int getSeriesSize();

    /**
     * ָ��λ���ϵ�ϵ��, ���������, �򷵻�һ���յ�ϵ��.
     *
     * @param index Ҫ���ص�ϵ�е�����
     * @return ϵ��
     */
    public DataSeries getSeries(int index);
}
