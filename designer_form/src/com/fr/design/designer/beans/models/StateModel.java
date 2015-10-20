package com.fr.design.designer.beans.models;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import com.fr.design.beans.location.Absorptionline;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.FormSelectionUtils;
import com.fr.design.designer.beans.AdapterBus;
import com.fr.design.designer.beans.HoverPainter;
import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.beans.location.Direction;
import com.fr.design.designer.beans.location.Location;
import com.fr.design.designer.creator.XConnector;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWAbsoluteLayout;
import com.fr.design.utils.ComponentUtils;

/**
 * ��ͨģʽ�µ�״̬model
 */
public class StateModel {
	// ��Ӧ��selection model

	private SelectionModel selectionModel;
	// ��ǰ��������ק�����λ������
	private Direction driection;

	// ��ǰ��ק����ʼλ��
	private int current_x;
	private int current_y;

	private Point startPoint = new Point();
	private Point currentPoint = new Point();

	private Absorptionline lineInX;
	private Absorptionline lineInY;

	// ��ǰ�Ƿ�����קѡ��״̬
	private boolean selecting;
	private boolean dragging;
	
	private boolean addable;

	private FormDesigner designer;

	public StateModel(FormDesigner designer) {
		this.designer = designer;
		selectionModel = designer.getSelectionModel();
	}

	/**
	 * ����direction
	 * @return direction����
	 */
	public Direction getDirection() {
		return driection;
	}

	/**
	 * �Ƿ����������ѡ��
	 * 
	 * @return true �������һ�������ѡ��
	 */
	public boolean isSelecting() {
		return selecting;
	}

    /**
     *�Ƿ�����ק
     * @return ��outer��ѡ��Ϊ��
     */
	public boolean dragable() {
		return ((driection != Location.outer) && !selecting);
	}

	/**
	 * ��ק���Ƿ����ת��Ϊ���ģʽ��
	 * �����ק���ֻ��һ������굱ǰ����λ�õ���ײ����������������������ͬ��
	 * �����ק���Ϊ�������굱ǰ����λ�õ���ײ����������Ҫ��Ҫ����Щ�����������ͬ�⣬�������Ǿ��Զ�λ����
    */
	private void checkAddable(MouseEvent e) {
		addable = false;
		designer.setPainter(null);

		if (driection != Location.inner) {
			return;
		}

		XCreator comp = designer.getComponentAt(e.getX(), e.getY(), selectionModel.getSelection().getSelectedCreators());
		XLayoutContainer container = XCreatorUtils.getHotspotContainer(comp);
		XCreator creator = selectionModel.getSelection().getSelectedCreator();
		Component creatorContainer = XCreatorUtils.getParentXLayoutContainer(creator);
		if (creatorContainer != null && creatorContainer != container
				&& (selectionModel.getSelection().size() == 1 || container instanceof XWAbsoluteLayout)) {
			HoverPainter painter = AdapterBus.getContainerPainter(designer, container);
			designer.setPainter(painter);
			if (painter != null) {
				Rectangle rect = ComponentUtils.getRelativeBounds(container);
				rect.x -= designer.getArea().getHorizontalValue();
				rect.y -= designer.getArea().getVerticalValue();
				painter.setRenderingBounds(rect);
				painter.setHotspot(new Point(e.getX(), e.getY()));
				painter.setCreator(creator);
			}
			addable = true;
		}
	}

	private boolean addBean(XLayoutContainer container, int x, int y) {
		LayoutAdapter adapter = container.getLayoutAdapter();
		Rectangle r = ComponentUtils.getRelativeBounds(container);
		if (selectionModel.getSelection().size() == 1) {
			return adapter.addBean(selectionModel.getSelection().getSelectedCreator(), x
					+ designer.getArea().getHorizontalValue() - r.x, y + designer.getArea().getVerticalValue() - r.y);
		}
		for (XCreator creator : selectionModel.getSelection().getSelectedCreators()) {
			adapter.addBean(creator, x + designer.getArea().getHorizontalValue() - r.x, y + designer.getArea().getVerticalValue()- r.y);
		}
		return true;
	}
	
	private void adding(int x, int y) {
        // ��ǰ������ڵ����
		XCreator hoveredComponent = designer.getComponentAt(x, y, selectionModel.getSelection().getSelectedCreators());

        // ��ȡ��������ڵĽ�������
        XLayoutContainer container = XCreatorUtils.getHotspotContainer(hoveredComponent);

        boolean success = false;

		if (container != null) {
			// ������������������acceptComponent�������
			success = addBean(container, x, y);
		}

		if (success) {
			FormSelectionUtils.rebuildSelection(designer);
			designer.getEditListenerTable().fireCreatorModified(
					selectionModel.getSelection().getSelectedCreator(), DesignerEvent.CREATOR_ADDED);
		} else {
			Toolkit.getDefaultToolkit().beep();
		}

        // ȡ����ʾ
        designer.setPainter(null);
    }

    /**
     *�Ƿ���ק
     * @return dragging״̬
     */
	public boolean isDragging() {
		return dragging;
	}

    /**
     *�Ƿ���Կ�ʼ����
     * @return startPoint��Ϊ�շ���true
     */
	public boolean prepareForDrawLining() {
		return startPoint != null;
	}

    /**
     *���ÿ�ʼλ��
     * @param p pointλ��
     */
	public void setStartPoint(Point p) {
		this.startPoint = p;
	}

    /**
     *���ؿ�ʼλ��
     * @return ��λ��
     */
	public Point getStartPoint() {
		return startPoint;
	}

    /**
     *���ص�ǰ��λ��
     * @return ��λ��
     */
    public Point getEndPoint() {
		return currentPoint;
	}

    /**
     *��ǰѡ�����
     * @param e ����¼�
     */
	public void startSelecting(MouseEvent e) {
		selecting = true;
		selectionModel.setHotspotBounds(new Rectangle());
		current_x = getMouseXY(e).x;
		current_y = getMouseXY(e).y;
	}

    /**
     *��ǰ����xy
     * @param e ����¼�
     */
	public void startResizing(MouseEvent e) {
		if (!selectionModel.getSelection().isEmpty()) {
			driection.backupBounds(designer);
		}
		current_x = getMouseXY(e).x;
		current_y = getMouseXY(e).y;
	}

    /**
     *��ʼ�㿪ʼDrawLine
     * @param p ��λ��
     */
	public void startDrawLine(Point p) {
		this.startPoint = p;
		if(p != null) {
			try {
				designer.setCursor(XConnector.connectorCursor);
			} catch (Exception e) {
			}
		} else {
			designer.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

    /**
     *����ͷ�ʱ���ڵ�����Ȧ�е����
     * @param e ����¼�
     */
	public void selectCreators(MouseEvent e) {
		int x = getMouseXY(e).x;
		int y = getMouseXY(e).y;

		Rectangle bounds = createCurrentBounds(x, y);

		if ((x != current_x) || (y != current_y)) {
			selectionModel.setSelectedCreators(getHotspotCreators(bounds, designer.getRootComponent()));
		}
		selectionModel.setHotspotBounds(null);
	}

    /**
     *������������
     * @param e ����¼�
     */
	public void drawLine(MouseEvent e) {
		designer.getDrawLineHelper().setDrawLine(true);
		Point p = designer.getDrawLineHelper().getNearWidgetPoint(e);
		if (p != null) {
			currentPoint = p;
		} else {
			currentPoint.x = e.getX() + designer.getArea().getHorizontalValue();
			currentPoint.y = e.getY() + designer.getArea().getVerticalValue();
		}
	}

	private Rectangle createCurrentBounds(int x, int y) {
		Rectangle bounds = new Rectangle();

		bounds.x = Math.min(x, current_x);
		bounds.y = Math.min(y, current_y);
		bounds.width = Math.max(x, current_x) - bounds.x;
		bounds.height = Math.max(y, current_y) - bounds.y;

		return bounds;
	}

	private ArrayList<XCreator> getHotspotCreators(Rectangle selection, XCreator root) {
		ArrayList<XCreator> creators = new ArrayList<XCreator>();

		if (!root.isVisible() && !designer.isRoot(root)) {
			return creators;
		}

		if (root instanceof XLayoutContainer) {
			XLayoutContainer container = (XLayoutContainer) root;
			int count = container.getXCreatorCount();
			Rectangle clipped = new Rectangle(selection);

			for (int i = count - 1; i >= 0; i--) {
				XCreator child = container.getXCreator(i);

				if (selection.contains(child.getBounds())) {
					creators.add(child);
				} else {
					clipped.x = selection.x - child.getX();
					clipped.y = selection.y - child.getY();
					creators.addAll(getHotspotCreators(clipped, child));
				}
			}
		}

		return creators;
	}


    /**
     *����model
     */
	public void resetModel() {
		dragging = false;
		selecting = false;
	}

    /**
     *����
     */
	public void reset() {
		driection = Location.outer;
		dragging = false;
		selecting = false;
	}

    /**
     *ȡ����ק
     */
	public void draggingCancel() {
		designer.repaint();
		reset();
	}

    /**
     *���ÿ����췽��
     * @param dir ���췽��
     */
	public void setDirection(Direction dir) {
		if(driection != dir) {
			this.driection = dir;
			driection.updateCursor(designer);
		}
	}

    /**
     *x�����߸�ֵ
     * @param line ��
     */
	public void setXAbsorptionline(Absorptionline line) {
		this.lineInX = line;
	}

    /**
     *y�����߸�ֵ
     * @param line ��
     */
	public void setYAbsorptionline(Absorptionline line) {
		this.lineInY = line;
	}

    /**
     *��������
     * @param g Graphics��
     */
	public void paintAbsorptionline(Graphics g) {
		if(lineInX != null) {
			lineInX.paint(g,designer.getArea());
		}
		if(lineInY != null) {
			lineInY.paint(g,designer.getArea());
		}
	}

    /**
     *��ק
     * @param e ����¼�
     */
	public void dragging(MouseEvent e) {
		checkAddable(e);
		setDependLinePainter(e);
		driection.drag(getMouseXY(e).x-current_x, getMouseXY(e).y-current_y, designer);
		this.dragging = true;
	}
	
	// ��קʱ���������õ���painter
	private void setDependLinePainter(MouseEvent e){
		XCreator comp = designer.getComponentAt(e.getX(), e.getY(), selectionModel.getSelection().getSelectedCreators());
		XLayoutContainer container = XCreatorUtils.getHotspotContainer(comp);
		XCreator creator = selectionModel.getSelection().getSelectedCreator();
		HoverPainter painter = AdapterBus.getContainerPainter(designer, container);
		designer.setPainter(painter);
		if (painter != null) {
			painter.setHotspot(new Point(e.getX(), e.getY()));
			painter.setCreator(creator);
		}
	}

    /**
     *�ͷŲ���
     * @param e ����¼�
     */
	public void releaseDragging(MouseEvent e) {
		this.dragging = false;
		if (addable) {
			adding(e.getX(), e.getY());
		} else if (!selectionModel.getSelection().isEmpty()) {
			selectionModel.releaseDragging();
		}
		designer.repaint();
	}

    /**
     *�ı�ѡ������
     *
     * @param e  ����¼�
     */
	public void changeSelection(MouseEvent e) {
		Rectangle bounds = createCurrentBounds(getMouseXY(e).x, getMouseXY(e).y);
		selectionModel.setHotspotBounds(bounds);
	}

    /**
     *����������ڵ�x��y  ���ǹ�������ֵ
     *
     * @param e ����¼�
     * @return xyֵ
     */
	public Point getMouseXY(MouseEvent e) {
		Point p1 = new Point(e.getX() + designer.getArea().getHorizontalValue(), e.getY()
				+ designer.getArea().getVerticalValue());
		return p1;
	}
	
}
