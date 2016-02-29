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
* @version 创建时间：2012-7-3 下午02:46:45
* 类说明: 事件说明: 工具栏编辑--> 是刷新ChartComponent 然后响应整个设计块的改变事件
 				       右键编辑 ---> 刷新ChartCompment  刷新对应的工具栏(加入事件) 然后响应整个设计块的改变事件
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
     *  必须加入 响应事件, 停止当前的选中元素编辑 && 获取焦点
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
     * 右键编辑 图表编辑层的监听事件, 在停止编辑时 响应整个编辑模板(form, sheet)的改变.
     * @param l   监听事件
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
      * 停止编辑, 通知事情, 刷新画出新界面.
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

    public void populate(BaseChartCollection cc) { // kunsnat_bug: 5471 实现设置的即时预览
    	try {// clone 为了判断编辑前后的值.
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
     * 设置是否支持编辑, 如:弹出对话框, 右键选中列表
     */
    public void setSupportEdit(boolean supportEdit) {
		this.supportEdit = supportEdit;
	}

    /**
     * 返回是否支持编辑,  如:弹出对话框, 右键选中列表
     * @return 返回是否支持编辑.
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
        // 反锯齿
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
      * ChartGlyph改变大小的时候做的操作
      */
    private void refreshChartGlyph() {
        Dimension d = getBounds().getSize();
        this.editingChart = this.chartCollection4Design.getSelectedChart();// kunsnat: 切换选中时 同步切换Plot
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
     * 鼠标点击
     * @param event    鼠标事件
     */
    public void mouseClicked(MouseEvent event) { 
    	 int click = event.getClickCount();
         if (click >= 1 && activeGlyph != null && this.isSupportEdit()) {
             activeGlyph.goRightPane();
         }
    }

    /**
     * 鼠标按压
     * @param e    鼠标事件
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
     * 鼠标松开
     * @param e    鼠标事件
     */
	public void mouseReleased(MouseEvent e) { 
	}

    /**
     * 鼠标进入
     * @param e    鼠标事件
     */
    public void mouseEntered(MouseEvent e) {

    }

    /**
     * 鼠标退出
     * @param e    鼠标事件
     */
    public void mouseExited(MouseEvent e) {

    }

    /**
     * 鼠标拖拽
     * @param e    鼠标事件
     */
	public void mouseDragged(MouseEvent e) {
	}

    /**
     * 鼠标移动
     * @param e    鼠标事件
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
            //不直接画chartGlyph而画image的原因是表单的柱形图会溢出表单
            //其他图都ok，其实感觉应该是柱形图画的不对，应该也可以改那边
            Image chartImage =  chartGlyph.toImage(chartWidth,chartHeight,ScreenResolution.getScreenResolution());
            g2d.drawImage(chartImage, 0, 0, chartWidth, chartHeight, null);
        }
    }
}