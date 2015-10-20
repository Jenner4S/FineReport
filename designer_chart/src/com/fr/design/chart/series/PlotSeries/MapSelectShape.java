package com.fr.design.chart.series.PlotSeries;

import java.awt.Point;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.List;

/**
 * ��ͼѡ�е�shape, ��װѡ�еĵ�, GeneralPath ������
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2012-11-12 ����05:00:34
 */
public class MapSelectShape {
	
	private List<Point> selectPoint = new ArrayList<Point>();
	private List<Shape> selectShape = new ArrayList<Shape>();
	
	private int selectType;
	
	public MapSelectShape(Shape[] shape, Point[] point, int selectType) {
		
		for(int i = 0; i < shape.length; i++) {
			this.selectShape.add(shape[i]);
		}
		for(int i = 0; i < point.length; i++) {
			this.selectPoint.add(point[i]);
		}
		this.selectType = selectType;
	}
	
	/**
	 * �ж϶�������� �Ƿ����Point
	 */
	public boolean containsPoint(Point point) {
		boolean contains = false;
		for(int i = 0; selectShape != null && i < selectShape.size(); i++) {
			Shape shape = (Shape)selectShape.get(i);
			contains = shape.contains(point);
			if(contains) {
				break;
			}
		}
		
		return contains;
	}
	
	/**
	 * ��Ӷ�Ӧ��ѡ�е��ѡ�е�����
	 */
	public void addSelectValue(Point point, Shape shape) {
		if(!selectShape.contains(shape)) {
			this.selectShape.add(shape);
		}
		if(!this.selectPoint.contains(point)) {
			this.selectPoint.add(point);
		}
	}
	
	/**
	 * ����ѡ�еĵ� ����
	 */
	public Point[] getSelectPoints() {
		return (Point[])this.selectPoint.toArray(new Point[selectPoint.size()]);
	}
	
	/**
	 * ����ѡ�е����� ����
	 */
	public Shape[] getSelectShapes() {
		return (Shape[])this.selectShape.toArray(new Shape[selectShape.size()]);
	}
	
	/**
	 * ����ѡ�е�����: ���� ���ߵ�
	 */
	public void setSelectType(int selectType) {
		this.selectType = selectType;
	}

	/**
	 * ����ѡ�е�����: ���� ���ߵ�
	 */
	public int getSelectType() {
		return selectType;
	}
}
