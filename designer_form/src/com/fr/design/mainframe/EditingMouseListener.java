package com.fr.design.mainframe;

import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;

import com.fr.base.BaseUtils;
import com.fr.design.designer.beans.AdapterBus;
import com.fr.design.designer.beans.ComponentAdapter;
import com.fr.design.designer.beans.events.DesignerEditor;
import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.beans.location.Direction;
import com.fr.design.designer.beans.location.Location;
import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.designer.beans.models.StateModel;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.designer.creator.XEditorHolder;
import com.fr.design.designer.creator.XElementCase;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWFitLayout;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.xpane.ToolTipEditor;
import com.fr.design.icon.IconPathConstants;
import com.fr.design.utils.ComponentUtils;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.general.Inter;
import com.fr.stable.Constants;

/**
 * 普通模式下的鼠标点击、位置处理器
 */
public class EditingMouseListener extends MouseInputAdapter {

	private static final int INDEX = 0;
	private FormDesigner designer;

	/**
	 * 普通模式下对应的model
	 */
	private StateModel stateModel;
	
	/**
	 * 获取表单设计器
	 * 
	 * @return 表单设计器
	 */
	public FormDesigner getDesigner() {
		return designer;
	}

	/**
	 * 选择模型，存储当前选择的组件和剪切板
	 */
	private SelectionModel selectionModel;
	/**
	 * 获取选择模型
	 * 
	 * @return 选择 
	 */
	public SelectionModel getSelectionModel() {
		return selectionModel;
	}

	private XCreator last_creator;
	private MouseEvent lastPressEvent;
	private DesignerEditor<? extends JComponent> current_editor;
	private XCreator current_creator;

	/**
	 * 获取最小移动距离
	 * 
	 * @return 最小移动距离
	 */
	public int getMinMoveSize() {
		return minMoveSize;
	}

	private int minDragSize = 5;
	private int minMoveSize = 8;
    //报表块的编辑按钮不灵敏，范围扩大一点
    private static final int GAP = 10;

    private XElementCase xElementCase;

    private JWindow promptWindow = new JWindow();

	public EditingMouseListener(FormDesigner designer) {
		this.designer = designer;
		stateModel = designer.getStateModel();
		selectionModel = designer.getSelectionModel();
        UIButton promptButton = new UIButton(Inter.getLocText("FR-Designer_Forbid_Drag_into_Adapt_Pane"), BaseUtils.readIcon(IconPathConstants.FORBID_ICON_PATH));
        this.promptWindow.add(promptButton);
	}

    private void promptUser(int x, int y, XLayoutContainer container){
        if (!selectionModel.getSelection().getSelectedCreator().canEnterIntoAdaptPane() && container.acceptType(XWFitLayout.class)){
            promptWidgetForbidEnter(x ,y , container);
        } else {
            cancelPromptWidgetForbidEnter();
        }
    }

    private void promptWidgetForbidEnter(int x,int y, XLayoutContainer container){
        container.setBorder(BorderFactory.createLineBorder(Color.RED, Constants.LINE_MEDIUM));
        int screen_X = (int)designer.getArea().getLocationOnScreen().getX();
        int screen_Y = (int)designer.getArea().getLocationOnScreen().getY();
        this.promptWindow.setSize(promptWindow.getPreferredSize());
        this.promptWindow.setPreferredSize(promptWindow.getPreferredSize());
        promptWindow.setLocation(screen_X + x + GAP, screen_Y + y + GAP);
        promptWindow.setVisible(true);
    }

    private void cancelPromptWidgetForbidEnter(){
        designer.getRootComponent().setBorder(BorderFactory.createLineBorder(XCreatorConstants.LAYOUT_SEP_COLOR, Constants.LINE_THIN));
        promptWindow.setVisible(false);
    }


    /**
     * 按下
     * @param e    鼠标事件
     */
	public void mousePressed(MouseEvent e) {
		if (!stopEditing()) {
			return;
		}
		if (!designer.isFocusOwner()) {
			// 获取焦点，以便获取热键
			designer.requestFocus();
		}
		if (e.isPopupTrigger()) {
			// 为触发上下文菜单预留
		} else if (e.getButton() == MouseEvent.BUTTON1) {

			Direction dir = selectionModel.getDirectionAt(e);
			if (!BaseUtils.isAuthorityEditing()) {
				stateModel.setDirection(dir);
			}

			if (dir == Location.outer) {
				if (designer.isDrawLineMode()) {
					designer.updateDrawLineMode(e);
				} else {
					if (selectionModel.hasSelectionComponent()
							&& selectionModel.getSelection().getRelativeBounds().contains(
							designer.getArea().getHorizontalValue() + e.getX(),
							designer.getArea().getVerticalValue() + e.getY())) {
						lastPressEvent = e;
						last_creator = selectionModel.getSelection().getSelectedCreator();
					} else {
						stateModel.startSelecting(e);
					}
				}
			} else {
				stateModel.startResizing(e);
			}
		}
	}

    /**
     * 释放
     * @param e    鼠标事件
     */
	public void mouseReleased(MouseEvent e) {
		if (e.isPopupTrigger()) {
			if (stateModel.isDragging()) {
				stateModel.draggingCancel();
			}
		} else {
			if (designer.isDrawLineMode()) {
				if (stateModel.prepareForDrawLining()) {
					designer.getDrawLineHelper().setDrawLine(false);
					designer.getDrawLineHelper().createDefalutLine();
				}
			} else if (stateModel.isSelecting()) {
				// 如果当前是区域选择状态，则选择该区域所含的组件
				designer.selectComponents(e);
			}
			if (stateModel.isDragging()) {
                // 当前鼠标所在的组件
                XCreator hoveredComponent = designer.getComponentAt(e.getX(), e.getY());
                // 拉伸时鼠标拖动过快，导致所在组件获取会为空
                if (hoveredComponent == null && e.getY() < 0) {
                	// bug63538
                	// 不是拖动过快导致的，而是纵坐标为负值导致的，这时参照横坐标为负值时的做法，取边界位置的组件，为鼠标所在点的组件
                	// 如果直接return，界面上已经进行了拖拽不能恢复
                	hoveredComponent = designer.getComponentAt(0, 0);
                }
                // 获取该组件所在的焦点容器
                XLayoutContainer container = XCreatorUtils.getHotspotContainer(hoveredComponent);

                if (container != null) {
                    boolean formSubmit2Adapt = !selectionModel.getSelection().getSelectedCreator().canEnterIntoAdaptPane() 
                    					&& container.acceptType(XWFitLayout.class);
                    if ( !formSubmit2Adapt) {
                        // 如果是处于拖拽状态，则释放组件
                        stateModel.releaseDragging(e);
                    } else {
                        selectionModel.deleteSelection();
                        designer.setPainter(null);
                    }
                    cancelPromptWidgetForbidEnter();
                }

			}
		}
		lastPressEvent = null;
		last_creator = null;
	}

	/**
	 * 激活上下文菜单，待完善
	 * 6.56暂时不支持右键 bugid 8777
	 */
	private void trigger_popup(MouseEvent e) {

		XCreator creator = selectionModel.getSelection().getSelectedCreator();

		if (creator == null) {
			return;
		}

		JPopupMenu popupMenu = null;
		ComponentAdapter adapter = AdapterBus.getComponentAdapter(designer, creator);
		popupMenu = adapter.getContextPopupMenu(e);

		if (popupMenu != null) {
			popupMenu.show(designer, e.getX(), e.getY());
		}
		// 通知组件已经被选择了
		designer.getEditListenerTable().fireCreatorModified(creator, DesignerEvent.CREATOR_SELECTED);
	}

    /**
     * 移动
     * @param e    鼠标事件
     */
	public void mouseMoved(MouseEvent e) {
		XCreator component = designer.getComponentAt(e);
		if (component instanceof XEditorHolder) {
			XEditorHolder xcreator = (XEditorHolder) component;
			Rectangle rect = xcreator.getBounds();
			int min = rect.x + rect.width / 2 - minMoveSize;
			int max = rect.x + rect.width / 2 + minMoveSize;
			if (e.getX() > min && e.getX() < max) {
				if (designer.getCursor().getType() != Cursor.HAND_CURSOR) {
					designer.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				}
				return;
			} else {
				if (designer.getCursor().getType() == Cursor.HAND_CURSOR) {
					designer.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
			}
		}
		Direction dir = selectionModel.getDirectionAt(e);
		if (designer.isDrawLineMode() && stateModel.getDirection() == Location.outer) {
			designer.updateDrawLineMode(e);
		}
		if (!BaseUtils.isAuthorityEditing()) {
			stateModel.setDirection(dir);
		}
        if(xElementCase != null){
            xElementCase.displayCoverPane(false);
        }
        if (component.isReport()) {
            xElementCase = (XElementCase)component;
            UIButton button = (UIButton)xElementCase.getCoverPane().getComponent(0);
            if(designer.getCursor().getType() ==Cursor.HAND_CURSOR) {
                designer.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            } // component.getParent() 是报表块所在的XWTitleLayout
            int minX = button.getX() + component.getX() + component.getParent().getX() - designer.getArea().getHorizontalValue();
            int minY = button.getY() + component.getY() + component.getParent().getY() + designer.getParaHeight() - designer.getArea().getVerticalValue();
            if(e.getX() + GAP >  minX && e.getX() - GAP < minX + button.getWidth()){
                if( e.getY() + GAP > minY && e.getY() - GAP < minY + button.getHeight()){
                    designer.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                }
            }
            xElementCase.displayCoverPane(true);
            xElementCase.setDirections(Direction.TOP_BOTTOM_LEFT_RIGHT);
            designer.repaint();
        }  else {
            if(xElementCase != null){
            xElementCase.displayCoverPane(false);
            designer.repaint();
            }
        }
	}

    /**
     * 拖拽
     * @param e    鼠标事件
     */
	public void mouseDragged(MouseEvent e) {
		if (BaseUtils.isAuthorityEditing()) {
			return;
		}
		// 如果当前是左键拖拽状态，拖拽组件
		if (stateModel.dragable()) {
			if (SwingUtilities.isRightMouseButton(e)) {
				return;
			} else {
				stateModel.dragging(e);
				// 获取e所在的焦点组件
				XCreator hotspot = designer.getComponentAt(e.getX(), e.getY());
				// 拉伸时鼠标拖动过快，导致所在组件获取会为空
				if (hotspot == null) {
					return;
				}
				// 获取焦点组件所在的焦点容器
				XLayoutContainer container = XCreatorUtils.getHotspotContainer(hotspot);
				//提示组件是否可以拖入
				promptUser(e.getX(), e.getY(), container);
				}
		} else if (designer.isDrawLineMode()) {
			if (stateModel.prepareForDrawLining()) {
				stateModel.drawLine(e);
			}
		} else if (stateModel.isSelecting() && (selectionModel.getHotspotBounds() != null)) {
			// 如果是拖拽选择区域状态，则更新选择区域
			stateModel.changeSelection(e);
		} else {
			if ((lastPressEvent == null) || (last_creator == null)) {
				return;
			}
			if (e.getPoint().distance(lastPressEvent.getPoint()) > minDragSize) {
                //参数面板和自适应布局不支持拖拽
                if (last_creator.isSupportDrag()){
                    designer.startDraggingComponent(last_creator, lastPressEvent, e.getX(), e.getY());
                }
				e.consume();
				lastPressEvent = null;
			}
		}
		designer.repaint();
	}

    /**
     * 点击
     * @param e    鼠标事件
     */
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() != MouseEvent.BUTTON1) {
			return;
		}
		XCreator creator = designer.getComponentAt(e);
		if(creator != null){
			creator.respondClick(this, e);
		}
		creator.doLayout();
		LayoutUtils.layoutRootContainer(designer.getRootComponent());
	}
	


    /**
     * 离开
     * @param e    鼠标事件
     */
	public void mouseExited(MouseEvent e) {
		if (designer.getCursor().getType() != Cursor.DEFAULT_CURSOR) {
			designer.setCursor(Cursor.getDefaultCursor());
		}
        if (xElementCase != null){
            xElementCase.displayCoverPane(false);
            designer.repaint();
        }
        cancelPromptWidgetForbidEnter();
	}

	/**
	 * 开始编辑
	 * @param creator 容器
	 * @param designerEditor 设计器
	 * @param adapter 适配器
	 */
	public void startEditing(XCreator creator, DesignerEditor<? extends JComponent> designerEditor, ComponentAdapter adapter) {
		if (designerEditor != null) {
			Rectangle rect = ComponentUtils.getRelativeBounds(creator);
			current_editor = designerEditor;
			current_creator = creator;
			Rectangle bounds = new Rectangle(1, 1, creator.getWidth() - 2, creator.getHeight() - 2);
			bounds.x += (rect.x - designer.getArea().getHorizontalValue());
			bounds.y += (rect.y - designer.getArea().getVerticalValue());
			designerEditor.getEditorTarget().setBounds(bounds);
			designer.add(designerEditor.getEditorTarget());
			designer.invalidate();
			designer.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			designerEditor.getEditorTarget().requestFocus();
			designer.repaint();
		}
	}

    /**
     * 停止编辑
     * @return     是否编辑成功
     */
	public boolean stopEditing() {
		if (current_editor != null) {
			designer.remove(current_editor.getEditorTarget());
			current_editor.fireEditStoped();

			Container container = current_creator.getParent();

			if (container != null) {
				LayoutUtils.layoutRootContainer(container);
			}
			designer.invalidate();
			designer.repaint();
			current_creator = null;
			current_editor = null;
			return true;
		}
		return true;
	}

    /**
     * 重置编辑控件大小
     */
	public void resetEditorComponentBounds() {
		if (current_editor == null) {
			return;
		}

		if (current_creator.getParent() == null) {
			stopEditing();
			return;
		}

		Rectangle rect = ComponentUtils.getRelativeBounds(current_creator);
		Rectangle bounds = new Rectangle(1, 1, current_creator.getWidth() - 2, current_creator.getHeight() - 2);
		bounds.x += (rect.x - designer.getArea().getHorizontalValue());
		bounds.y += (rect.y - designer.getArea().getVerticalValue());
		if (current_creator instanceof XEditorHolder) {
			ToolTipEditor.getInstance().resetBounds((XEditorHolder) current_creator, bounds, current_editor.getEditorTarget().getBounds());
		}
		current_editor.getEditorTarget().setBounds(bounds);
	}
}