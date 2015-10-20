package com.fr.design.designer.beans.models;

import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import com.fr.design.designer.beans.AdapterBus;
import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.beans.location.Direction;
import com.fr.design.designer.beans.location.Location;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWFitLayout;
import com.fr.design.designer.creator.XWParameterLayout;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.FormSelection;
import com.fr.design.mainframe.FormSelectionUtils;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.stable.ArrayUtils;

/**
 * ��model���浱ǰѡ�������ͼ��а���Ϣ
 */
public class SelectionModel {
    private static final int DELTA_X_Y = 20; //ճ��ʱ���ƫ�ƾ���
	private static FormSelection CLIP_BOARD = new FormSelection();
	private FormDesigner designer;
	private FormSelection selection;
	private Rectangle hotspot_bounds;

	public SelectionModel(FormDesigner designer) {
		this.designer = designer;
		selection = new FormSelection();
	}

	/**
	 * ���á����formSelction�Լ�ѡ������
	 */
	public void reset() {
		selection.reset();
		hotspot_bounds = null;
	}
	
	/**
	 * formSelction�Ƿ�Ϊ��
	 * @return �Ƿ�Ϊ��
	 */
	public static boolean isEmpty(){
		return CLIP_BOARD.isEmpty();
	}

	/**
	 * �����һ�£���ѡ�еĵ������������Ctrl����shift��ʱ�����Խ��ж�ѡ
	 * @param e ����¼�
	 */
	public void selectACreatorAtMouseEvent(MouseEvent e) {
		if (!e.isControlDown() && !e.isShiftDown()) {
			// ���Ctrl����Shift����û�а��£�������Ѿ�ѡ������
			selection.reset();
		}

		// ��ȡe���ڵ����
		XCreator comp = designer.getComponentAt(e);
		// ���������scale��title����ר�������������丸�㣬��������ǲ��ñ�ѡ�е�
		if (comp != designer.getRootComponent() && comp != designer.getParaComponent()) {
			XCreator parentContainer = (XCreator) comp.getParent();
			comp = parentContainer.isDedicateContainer() ? parentContainer : comp;
		}
		if (selection.removeSelectedCreator(comp) || selection.addSelectedCreator(comp)) {
			designer.getEditListenerTable().fireCreatorModified(comp, DesignerEvent.CREATOR_SELECTED);
			designer.repaint();
		}
	}

	/**
	 * ����ѡ������е����а���
	 */
	public void cutSelectedCreator2ClipBoard() {
		if (hasSelectionComponent()) {
			selection.cut2ClipBoard(CLIP_BOARD);
			designer.getEditListenerTable().fireCreatorModified(DesignerEvent.CREATOR_CUTED);
			designer.repaint();
		}
	}

	/**
	 * ���Ƶ�ǰѡ�е���������а�
	 */
	public void copySelectedCreator2ClipBoard() {
		if (!selection.isEmpty()) {
			selection.copy2ClipBoard(CLIP_BOARD);
		}
	}

	/**
	 * �Ӽ��а�ճ�����
	 * @return ��
	 */
	public boolean pasteFromClipBoard() {
		if (!CLIP_BOARD.isEmpty()) {
			XLayoutContainer parent = null;
			if (!hasSelectionComponent()) {
				FormSelectionUtils.paste2Container(designer, designer.getRootComponent(),CLIP_BOARD, DELTA_X_Y, DELTA_X_Y);
			} else {
				parent = XCreatorUtils.getParentXLayoutContainer(selection.getSelectedCreator());
				if (parent != null) {
					Rectangle rec = selection.getSelctionBounds();
					FormSelectionUtils.paste2Container(designer, parent,CLIP_BOARD, rec.x + DELTA_X_Y, rec.y + DELTA_X_Y);
				}
			}
		} else {
			Toolkit.getDefaultToolkit().beep();
		}
		return false;
	}

	public FormSelection getSelection() {
		return selection;
	}

	/**
	 * ɾ����ǰ����ѡ������
	 */
	public void deleteSelection() {
		XCreator[] roots = selection.getSelectedCreators();

		if (roots.length > 0) {
			for (XCreator creator : roots) {
                if(creator.acceptType(XWParameterLayout.class)){
                    designer.removeParaComponent();
                }
                
				removeCreatorFromContainer(creator, creator.getWidth(), creator.getHeight());
				creator.removeAll();
				// �����ѡ�е����
                selection.reset();
			}
            setSelectedCreator(designer.getRootComponent());
			// �����¼�
			designer.getEditListenerTable().fireCreatorModified(DesignerEvent.CREATOR_DELETED);
			designer.repaint();
		}
	}

	/**
	 * ��ѡ�������ɾ��ĳ���
	 * 
	 * @param creator ���
	 * @param creatorWidth ���֮ǰ���
	 * @param creatorHeight ���֮ǰ�߶�
	 */
	public void removeCreator(XCreator creator, int creatorWidth, int creatorHeight) {
		selection.removeCreator(creator);
		removeCreatorFromContainer(creator, creatorWidth, creatorHeight);
		designer.repaint();
	}

	/**
	 * ����ѡ������
	 */
	public void setHotspotBounds(Rectangle rect) {
		hotspot_bounds = rect;
	}

	/**
	 * ��õ�ǰѡ������
	 */
	public Rectangle getHotspotBounds() {
		return hotspot_bounds;
	}

	private void removeCreatorFromContainer(XCreator creator, int creatorWidth, int creatorHeight) {
		XLayoutContainer parent = XCreatorUtils.getParentXLayoutContainer(creator);
		if (parent == null) {
			return;
		}
		boolean changeCreator = creator.shouldScaleCreator() || creator.hasTitleStyle();
		if (parent.acceptType(XWFitLayout.class) && changeCreator) {
			creator = (XCreator) creator.getParent();
		}
		parent.getLayoutAdapter().removeBean(creator, creatorWidth, creatorHeight);
		// ɾ����������ͬʱ��ɾ����ͬʱ��ѡ���Ҷ�����
		parent.remove(creator);
		LayoutManager layout = parent.getLayout();

		if (layout != null) {
			// ˢ����������Ĳ���
			LayoutUtils.layoutContainer(parent);
		}
	}

	/**
	 * �Ƿ��������ѡ�������ѡ�������ײ�������Ҳ��Ϊ��ѡ��
	 * @return ���򷵻�true
	 */
	public boolean hasSelectionComponent() {
		return !selection.isEmpty() && selection.getSelectedCreator().getParent() != null;
	}

	/**
	 * �ƶ������ָ��λ��
	 * @param x ����x
	 * @param y ����y
	 */
	public void move(int x, int y) {
		for (XCreator creator : selection.getSelectedCreators()) {
				creator.setLocation(creator.getX() + x, creator.getY() + y);
				LayoutAdapter layoutAdapter = AdapterBus.searchLayoutAdapter(designer, creator);
				if (layoutAdapter != null) {
					layoutAdapter.fix(creator);
				}
		}
		designer.getEditListenerTable().fireCreatorModified(selection.getSelectedCreator(),
					DesignerEvent.CREATOR_SELECTED);
	}

	/**
	 * �ͷŲ���
	 */
	public void releaseDragging() {
		designer.setPainter(null);
		selection.fixCreator(designer);
		designer.getEditListenerTable().fireCreatorModified(selection.getSelectedCreator(),
				DesignerEvent.CREATOR_RESIZED);
	}

	public Direction getDirectionAt(MouseEvent e) {
		Direction dir;
		if (e.isControlDown() || e.isShiftDown()) {
			XCreator creator = designer.getComponentAt(e.getX(), e.getY(), selection.getSelectedCreators());
			if (creator != designer.getRootComponent() && selection.addedable(creator)) {
				return Location.add;
			}
		}
		if (hasSelectionComponent()) {
			int x = e.getX() + designer.getArea().getHorizontalValue();
			int y = e.getY() + designer.getArea().getVerticalValue();
			dir = getDirection(selection.getRelativeBounds(), x, y);
			if (selection.size() == 1) {
				if (!ArrayUtils.contains(selection.getSelectedCreator().getDirections(), dir.getActual())) {
					dir = Location.outer;
				}
			}
		} else {
			dir = Location.outer;
		}

		if (designer.getDesignerMode().isFormParameterEditor() && dir == Location.outer) {
			dir = designer.getLoc2Root(e);
		}
		return dir;
	}

	private Direction getDirection(Rectangle bounds, int x, int y) {
		if (x < (bounds.x - XCreatorConstants.RESIZE_BOX_SIZ)) {
			return Location.outer;
		} else if ((x >= (bounds.x - XCreatorConstants.RESIZE_BOX_SIZ)) && (x <= bounds.x)) {
			if (y < (bounds.y - XCreatorConstants.RESIZE_BOX_SIZ)) {
				return Location.outer;
			} else if ((y >= (bounds.y - XCreatorConstants.RESIZE_BOX_SIZ)) && (y <= bounds.y)) {
				return Location.left_top;
			} else if ((y > bounds.y) && (y < (bounds.y + bounds.height))) {
				return Location.left;
			} else if ((y >= (bounds.y + bounds.height))
					&& (y <= (bounds.y + bounds.height + XCreatorConstants.RESIZE_BOX_SIZ))) {
				return Location.left_bottom;
			} else {
				return Location.outer;
			}
		} else if ((x > bounds.x) && (x < (bounds.x + bounds.width))) {
			if (y < (bounds.y - XCreatorConstants.RESIZE_BOX_SIZ)) {
				return Location.outer;
			} else if ((y >= (bounds.y - XCreatorConstants.RESIZE_BOX_SIZ)) && (y <= bounds.y)) {
				return Location.top;
			} else if ((y > bounds.y) && (y < (bounds.y + bounds.height))) {
				return Location.inner;
			} else if ((y >= (bounds.y + bounds.height))
					&& (y <= (bounds.y + bounds.height + XCreatorConstants.RESIZE_BOX_SIZ))) {
				return Location.bottom;
			} else {
				return Location.outer;
			}
		} else if ((x >= (bounds.x + bounds.width))
				&& (x <= (bounds.x + bounds.width + XCreatorConstants.RESIZE_BOX_SIZ))) {
			if (y < (bounds.y - XCreatorConstants.RESIZE_BOX_SIZ)) {
				return Location.outer;
			} else if ((y >= (bounds.y - XCreatorConstants.RESIZE_BOX_SIZ)) && (y <= bounds.y)) {
				return Location.right_top;
			} else if ((y > bounds.y) && (y < (bounds.y + bounds.height))) {
				return Location.right;
			} else if ((y >= (bounds.y + bounds.height))
					&& (y <= (bounds.y + bounds.height + XCreatorConstants.RESIZE_BOX_SIZ))) {
				return Location.right_bottom;
			} else {
				return Location.outer;
			}
		} else {
			return Location.outer;
		}
	}

	private void fireCreatorSelected() {
		designer.getEditListenerTable().fireCreatorModified(selection.getSelectedCreator(),
				DesignerEvent.CREATOR_SELECTED);
	}

	public void setSelectedCreator(XCreator rootComponent) {
		selection.setSelectedCreator(rootComponent);
		fireCreatorSelected();
	}

	public void setSelectedCreators(ArrayList<XCreator> rebuildSelection) {
		selection.setSelectedCreators(rebuildSelection);
		fireCreatorSelected();
	}
}
