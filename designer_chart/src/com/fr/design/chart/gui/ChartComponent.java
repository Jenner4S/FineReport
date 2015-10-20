package com.fr.design.chart.gui;

import com.fr.base.FRContext;
import com.fr.base.ScreenResolution;
import com.fr.base.chart.BaseChart;
import com.fr.base.chart.BaseChartCollection;
import com.fr.base.chart.BaseChartGlyph;
import com.fr.chart.base.ChartConstants;
import com.fr.chart.chartattr.Axis;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartglyph.AxisGlyph;
import com.fr.design.chart.gui.active.ActiveGlyph;
import com.fr.design.chart.gui.active.ChartActiveGlyph;
import com.fr.design.gui.chart.MiddleChartComponent;
import com.fr.stable.ArrayUtils;
import com.fr.stable.core.PropertyChangeListener;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
* @author kunsnat E-mail:kunsnat@gmail.com
* @version ����ʱ�䣺2012-7-3 ����02:46:45
* ��˵��: �¼�˵��: �������༭--> ��ˢ��ChartComponent Ȼ����Ӧ������ƿ�ĸı��¼�
 				       �Ҽ��༭ ---> ˢ��ChartCompment  ˢ�¶�Ӧ�Ĺ�����(�����¼�) Ȼ����Ӧ������ƿ�ĸı��¼�
 */
public class ChartComponent extends MiddleChartComponent implements MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 744164838619052097L;
	private final List<PropertyChangeListener> listeners = new ArrayList<PropertyChangeListener>();
    private ChartCollection chartCollection4Design;
    private Chart editingChart;
    private BaseChartGlyph chartGlyph;
    private int chartWidth = -1;
    private int chartHeight = -1;
    private Point point;

    private ActiveGlyph activeGlyph;
    
    private boolean supportEdit = true;

    private final int[] resizeCursors = new int[]{
            Cursor.NW_RESIZE_CURSOR, Cursor.N_RESIZE_CURSOR, Cursor.NE_RESIZE_CURSOR,
            Cursor.E_RESIZE_CURSOR, Cursor.W_RESIZE_CURSOR,
            Cursor.SE_RESIZE_CURSOR, Cursor.S_RESIZE_CURSOR, Cursor.SW_RESIZE_CURSOR
    };

    public ChartComponent() {
        super();
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    /**
     *  ������� ��Ӧ�¼�, ֹͣ��ǰ��ѡ��Ԫ�ر༭ && ��ȡ����
     * @author kunsnat E-mail kunsnat@gmail.com
     * @param cc
     */
    public ChartComponent(ChartCollection cc) {
        this();
        populate(cc);
    }
    
    public ChartComponent(BaseChartCollection cc) {
    	this();
    	populate(cc);
    }
    
    public ChartComponent(ChartCollection cc, PropertyChangeListener l) {
    	this();
    	populate(cc);
    	addStopEditingListener(l);
    }


    /**
     * �Ҽ��༭ ͼ��༭��ļ����¼�, ��ֹͣ�༭ʱ ��Ӧ�����༭ģ��(form, sheet)�ĸı�.
     * @param l   �����¼�
     */
    public void addStopEditingListener(PropertyChangeListener l) {
    	 if (!listeners.contains(l)) {
             listeners.add(l);
         }
    }

    private void fireStopEditing() {
        for (int len = listeners.size(), i = len; i > 0; i--) {
            listeners.get(i - 1).propertyChange();
        }
    }

    /**
      * ֹͣ�༭, ֪ͨ����, ˢ�»����½���.
      */
    public void reset() {
        fireStopEditing();

        this.editingChart = null;
        this.chartGlyph = null;
        this.activeGlyph = null;
        this.point = null;
        this.chartHeight = this.chartWidth = -1;

        this.editingChart = this.chartCollection4Design.getSelectedChart();

        refreshChartGlyph();
        this.activeGlyph = ActiveGlyphFactory.createActiveGlyph(this, chartGlyph);

        repaint();
    }

    public void populate(BaseChartCollection cc) { // kunsnat_bug: 5471 ʵ�����õļ�ʱԤ��
    	try {// clone Ϊ���жϱ༭ǰ���ֵ.
    		this.chartCollection4Design = (ChartCollection)cc;
//    		this.chartCollection4Design = (ChartCollection)cc.clone();
		} catch (Exception e) {
			FRContext.getLogger().error("ChartCollection clone is Error");
		}
        reset();
    }

    public BaseChartCollection update() {
        return this.chartCollection4Design;
    }

    /**
     * �����Ƿ�֧�ֱ༭, ��:�����Ի���, �Ҽ�ѡ���б�
     */
    public void setSupportEdit(boolean supportEdit) {
		this.supportEdit = supportEdit;
	}

    /**
     * �����Ƿ�֧�ֱ༭,  ��:�����Ի���, �Ҽ�ѡ���б�
     * @return �����Ƿ�֧�ֱ༭.
     */
	public boolean isSupportEdit() {
		return supportEdit;
	}

	public ChartCollection getChartCollection() {
        return chartCollection4Design;
    }

    public int getChartSize() {
        return (this.chartCollection4Design == null) ? 0 : this.chartCollection4Design.getChartCount();
    }

    public BaseChart getEditingChart() {
        return editingChart;
    }

	public BaseChartGlyph getChartGlyph() {
        return chartGlyph;
    }

    public void paintComponent(Graphics g) {  //
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        Paint oldPaint = g2d.getPaint();

        g2d.setPaint(Color.white);
        g2d.fillRect(0, 0, this.getBounds().width, this.getBounds().height);
        g2d.setPaint(oldPaint);
        
        g2d.translate(ChartConstants.PREGAP4BOUNDS/2, ChartConstants.PREGAP4BOUNDS/2);

        if (needRefreshChartGlyph()) {
            refreshChartGlyph();
        }

        Object lastHint = g2d.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        // �����
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawChartGlyph(g2d);
        
        ActiveGlyph ag = this.getActiveGlyph();
		if (ag != null) {
			ag.paint4ActiveGlyph(g2d, chartGlyph);
		}

        g2d.translate(-ChartConstants.PREGAP4BOUNDS/2, -ChartConstants.PREGAP4BOUNDS/2);
        if (lastHint == null) {
            lastHint = RenderingHints.VALUE_ANTIALIAS_OFF;
        }

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, lastHint);
    }

    /*
      * ChartGlyph�ı��С��ʱ�����Ĳ���
      */
    private void refreshChartGlyph() {
        Dimension d = getBounds().getSize();
        this.editingChart = this.chartCollection4Design.getSelectedChart();// kunsnat: �л�ѡ��ʱ ͬ���л�Plot
        if (editingChart != null) {
            this.chartGlyph = editingChart.createGlyph(editingChart.defaultChartData());
            this.activeGlyph = ActiveGlyphFactory.createActiveGlyph(this, chartGlyph);
        }
        this.chartWidth = d.width - ChartConstants.PREGAP4BOUNDS;
        this.chartHeight = d.height - ChartConstants.PREGAP4BOUNDS;
    }

    private ActiveGlyph getActiveGlyph() {
        if (point == null) {
        	this.activeGlyph = new ChartActiveGlyph(this, chartGlyph);
        } else {
        	this.activeGlyph = new ChartActiveGlyph(this, chartGlyph).findActionGlyphFromChildren(point.x, point.y);
        }
        return this.activeGlyph;
    }

    /**
     * �����
     * @param event    ����¼�
     */
    public void mouseClicked(MouseEvent event) { 
    	 int click = event.getClickCount();
         if (click >= 1 && activeGlyph != null && this.isSupportEdit()) {
             activeGlyph.goRightPane();
         }
    }

    /**
     * ��갴ѹ
     * @param e    ����¼�
     */
    public void mousePressed(MouseEvent e) {
    	point = new Point(e.getX(),e.getY());
       if (!ArrayUtils.contains(resizeCursors, this.getCursor().getType())) {
    	   this.activeGlyph = new ChartActiveGlyph(this, chartGlyph).findActionGlyphFromChildren(point.x, point.y);
        }

       if (this.activeGlyph == null) {
            return;
        }

        repaint();
        
    }

    /**
     * ����ɿ�
     * @param e    ����¼�
     */
	public void mouseReleased(MouseEvent e) { 
	}

    /**
     * ������
     * @param e    ����¼�
     */
    public void mouseEntered(MouseEvent e) {

    }

    /**
     * ����˳�
     * @param e    ����¼�
     */
    public void mouseExited(MouseEvent e) {

    }

    /**
     * �����ק
     * @param e    ����¼�
     */
	public void mouseDragged(MouseEvent e) {
	}

    /**
     * ����ƶ�
     * @param e    ����¼�
     */
    public void mouseMoved(MouseEvent e) {
    	ActiveGlyph ag = this.getActiveGlyph();
		if (ag != null) {
			ag.onMouseMove(e);
		}
    }

    public AxisGlyph getActiveAxisGlyph() {
        return (AxisGlyph) activeGlyph.getGlyph();
    }

    public Axis getActiveAxis() {
        AxisGlyph axisGlyph = getActiveAxisGlyph();
        if (editingChart.getPlot() != null) {
            return editingChart.getPlot().getAxis(axisGlyph.getAxisType());
        }
        return null;
    }

    private boolean needRefreshChartGlyph() {
        return chartGlyph == null || chartWidth != this.getBounds().width || chartHeight != this.getBounds().height;
    }

    private void drawChartGlyph(Graphics2D g2d) {
        if (chartGlyph != null) {
            if (chartGlyph.isRoundBorder()) {
                chartGlyph.setBounds(new RoundRectangle2D.Double(0, 0, chartWidth, chartHeight, 10, 10));
            } else {
                chartGlyph.setBounds(new Rectangle2D.Double(0, 0, chartWidth, chartHeight));
            }
            // chartGlyph.draw(g2d, ScreenResolution.getScreenResolution());
            //��ֱ�ӻ�chartGlyph����image��ԭ���Ǳ�������ͼ�������
            //����ͼ��ok����ʵ�о�Ӧ��������ͼ���Ĳ��ԣ�Ӧ��Ҳ���Ը��Ǳ�
            Image chartImage =  chartGlyph.toImage(chartWidth,chartHeight,ScreenResolution.getScreenResolution());
            g2d.drawImage(chartImage, 0, 0, chartWidth, chartHeight, null);
        }
    }
}
