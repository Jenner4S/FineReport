package com.fr.design.mainframe;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;

import javax.swing.BorderFactory;
import javax.swing.JWindow;

import com.fr.base.BaseUtils;
import com.fr.design.designer.beans.AdapterBus;
import com.fr.design.designer.beans.HoverPainter;
import com.fr.design.designer.beans.Painter;
import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.beans.models.AddingModel;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWFitLayout;
import com.fr.design.designer.creator.XWParameterLayout;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.icon.IconPathConstants;
import com.fr.design.utils.ComponentUtils;
import com.fr.form.ui.Widget;
import com.fr.general.Inter;
import com.fr.stable.Constants;

/**
 * 添加模式下鼠标事件处理器。
 */
public class FormCreatorDropTarget extends DropTarget {

	private FormDesigner designer;
	/**
	 * 当前鼠标的设计组件
	 */
	private Component current;
	/**
	 * 当前添加模式对应的model
	 */
	private AddingModel addingModel;
    private final static int GAP = 30;

    private JWindow promptWindow = new JWindow();
    private UIButton promptButton = new UIButton("", BaseUtils.readIcon(IconPathConstants.FORBID_ICON_PATH));

	public FormCreatorDropTarget(FormDesigner designer) {
		this.designer = designer;
		this.addingModel = designer.getAddingModel();
        this.promptWindow.add(promptButton);
	}

	private void adding(int x, int y) {
		// 当前鼠标所在的组件
		XCreator hoveredComponent = designer.getComponentAt(x, y);

		// 获取该组件所在的焦点容器
		XLayoutContainer container = XCreatorUtils.getHotspotContainer(hoveredComponent);
		
		//cardTagLayout里用到
		container.stopAddingState(designer);

		boolean success = false;

		if (container != null) {
			// 如果是容器，则调用其acceptComponent接受组件
			AddingModel model = designer.getAddingModel();

            boolean chartEnter2Para =!addingModel.getXCreator().canEnterIntoParaPane() && container.acceptType(XWParameterLayout.class);
            boolean formSubmit2Adapt = !addingModel.getXCreator().canEnterIntoAdaptPane() && container.acceptType(XWFitLayout.class);

			if (model != null && !chartEnter2Para && !formSubmit2Adapt) {
				success = model.add2Container(designer, container, x, y);
			}
            cancelPromptWidgetForbidEnter();
		}

		if (success) {
			// 如果添加成功，则触发相应事件
            XCreator xCreator = container.acceptType(XWParameterLayout.class) ? designer.getParaComponent() : designer.getRootComponent();
			designer.getSelectionModel().setSelectedCreators(
					FormSelectionUtils.rebuildSelection(xCreator, new Widget[]{addingModel.getXCreator().toData()}));
			designer.getEditListenerTable().fireCreatorModified(addingModel.getXCreator(), DesignerEvent.CREATOR_ADDED);
		} else {
			Toolkit.getDefaultToolkit().beep();
		}

		// 取消提示
		designer.setPainter(null);
		// 切换添加状态到普通状态
		designer.stopAddingState();
	}

	private void entering(int x, int y) {
		// 将要添加的组件图标移动到鼠标下的位置
		addingModel.moveTo(x, y);
		designer.repaint();
	}

	private void exiting() {
        cancelPromptWidgetForbidEnter();
		// 隐藏组件图标
		addingModel.reset();
		designer.setPainter(null);
		designer.repaint();
	}

	private void hovering(int x, int y) {
		// 当前位置移植鼠标e所在的位置
		addingModel.moveTo(x, y);
		// 获取e所在的焦点组件
		XCreator hotspot = designer.getComponentAt(x, y);
		// 获取焦点组件所在的焦点容器
		XLayoutContainer container = XCreatorUtils.getHotspotContainer(hotspot);
        //提示组件是否可以拖入
        promptUser(x, y, container);
		if (container != null) {
			HoverPainter painter = null;

			if (container != current || designer.getPainter() == null) {
				// 如果焦点容器不是当前容器
				if (current != null) {
					// 取消前一个焦点容器的提示渲染器
					designer.setPainter(null);
				}

				painter = AdapterBus.getContainerPainter(designer, container);

				// 为界面设计器设置提示渲染提示器
				designer.setPainter(painter);

				// 将当前容器更新为新的容器
				current = container;
			} else {
				// 获取当前设计界面的提示渲染器
				Painter p = designer.getPainter();
				if (p instanceof HoverPainter) {
					painter = (HoverPainter) p;
				}
			}

			if (painter != null) {
				// 为提示渲染器设置焦点位置、区域等渲染参数
				Rectangle rect = ComponentUtils.getRelativeBounds(container);
				rect.x -= designer.getArea().getHorizontalValue();
				rect.y -= designer.getArea().getVerticalValue();
				painter.setRenderingBounds(rect);
				painter.setHotspot(new Point(x, y));
				painter.setCreator(addingModel.getXCreator());
			}
		} else {
			// 如果鼠标不在任何组件上，则取消提示器
			designer.setPainter(null);
			current = null;
		}
		designer.repaint();
	}

    private void promptUser(int x, int y, XLayoutContainer container){
        if (!addingModel.getXCreator().canEnterIntoParaPane() && container.acceptType(XWParameterLayout.class)){
            promptButton.setText(Inter.getLocText("FR-Designer_Forbid_Drag_into_Para_Pane"));
            promptWidgetForbidEnter(x ,y ,container);
        } else if (!addingModel.getXCreator().canEnterIntoAdaptPane() && container.acceptType(XWFitLayout.class)){
            promptButton.setText(Inter.getLocText("FR-Designer_Forbid_Drag_into_Adapt_Pane"));
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
        promptWindow.setLocation( screen_X + x + GAP ,screen_Y + y + GAP);
        promptWindow.setVisible(true);
    }

    private void cancelPromptWidgetForbidEnter(){
        if (designer.getParaComponent() != null){
            designer.getParaComponent().setBorder(BorderFactory.createLineBorder(XCreatorConstants.LAYOUT_SEP_COLOR, Constants.LINE_THIN));
        }
        designer.getRootComponent().setBorder(BorderFactory.createLineBorder(XCreatorConstants.LAYOUT_SEP_COLOR, Constants.LINE_THIN));
        promptWindow.setVisible(false);
    }

    /**
     * 拖拽进入
     * @param dtde     事件
     */
	public void dragEnter(DropTargetDragEvent dtde) {
		Point loc = dtde.getLocation();
		this.entering(loc.x, loc.y);
	}

    /**
     * 拖拽移动经过
     * @param dtde     事件
     */
	public void dragOver(DropTargetDragEvent dtde) {
		Point loc = dtde.getLocation();
		hovering(loc.x, loc.y);
	}

    /**
     * 拖拽事件
     * @param dtde     事件
     */
	public void dropActionChanged(DropTargetDragEvent dtde) {
	}

    /**
     * 拖拽离开
     * @param dte     事件
     */
	public void dragExit(DropTargetEvent dte) {
		this.exiting();
	}

    /**
     * 拖拽释放
     * @param dtde     事件
     */
	public void drop(DropTargetDropEvent dtde) {
		Point loc = dtde.getLocation();
		this.adding(loc.x, loc.y);
        //针对在表单中拖入一个控件直接ctrl+s无反应
        designer.requestFocus();
	}
}