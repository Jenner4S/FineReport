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
 * ���ģʽ������¼���������
 */
public class FormCreatorDropTarget extends DropTarget {

	private FormDesigner designer;
	/**
	 * ��ǰ����������
	 */
	private Component current;
	/**
	 * ��ǰ���ģʽ��Ӧ��model
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
		// ��ǰ������ڵ����
		XCreator hoveredComponent = designer.getComponentAt(x, y);

		// ��ȡ��������ڵĽ�������
		XLayoutContainer container = XCreatorUtils.getHotspotContainer(hoveredComponent);
		
		//cardTagLayout���õ�
		container.stopAddingState(designer);

		boolean success = false;

		if (container != null) {
			// ������������������acceptComponent�������
			AddingModel model = designer.getAddingModel();

            boolean chartEnter2Para =!addingModel.getXCreator().canEnterIntoParaPane() && container.acceptType(XWParameterLayout.class);
            boolean formSubmit2Adapt = !addingModel.getXCreator().canEnterIntoAdaptPane() && container.acceptType(XWFitLayout.class);

			if (model != null && !chartEnter2Para && !formSubmit2Adapt) {
				success = model.add2Container(designer, container, x, y);
			}
            cancelPromptWidgetForbidEnter();
		}

		if (success) {
			// �����ӳɹ����򴥷���Ӧ�¼�
            XCreator xCreator = container.acceptType(XWParameterLayout.class) ? designer.getParaComponent() : designer.getRootComponent();
			designer.getSelectionModel().setSelectedCreators(
					FormSelectionUtils.rebuildSelection(xCreator, new Widget[]{addingModel.getXCreator().toData()}));
			designer.getEditListenerTable().fireCreatorModified(addingModel.getXCreator(), DesignerEvent.CREATOR_ADDED);
		} else {
			Toolkit.getDefaultToolkit().beep();
		}

		// ȡ����ʾ
		designer.setPainter(null);
		// �л����״̬����ͨ״̬
		designer.stopAddingState();
	}

	private void entering(int x, int y) {
		// ��Ҫ��ӵ����ͼ���ƶ�������µ�λ��
		addingModel.moveTo(x, y);
		designer.repaint();
	}

	private void exiting() {
        cancelPromptWidgetForbidEnter();
		// �������ͼ��
		addingModel.reset();
		designer.setPainter(null);
		designer.repaint();
	}

	private void hovering(int x, int y) {
		// ��ǰλ����ֲ���e���ڵ�λ��
		addingModel.moveTo(x, y);
		// ��ȡe���ڵĽ������
		XCreator hotspot = designer.getComponentAt(x, y);
		// ��ȡ����������ڵĽ�������
		XLayoutContainer container = XCreatorUtils.getHotspotContainer(hotspot);
        //��ʾ����Ƿ��������
        promptUser(x, y, container);
		if (container != null) {
			HoverPainter painter = null;

			if (container != current || designer.getPainter() == null) {
				// ��������������ǵ�ǰ����
				if (current != null) {
					// ȡ��ǰһ��������������ʾ��Ⱦ��
					designer.setPainter(null);
				}

				painter = AdapterBus.getContainerPainter(designer, container);

				// Ϊ���������������ʾ��Ⱦ��ʾ��
				designer.setPainter(painter);

				// ����ǰ��������Ϊ�µ�����
				current = container;
			} else {
				// ��ȡ��ǰ��ƽ������ʾ��Ⱦ��
				Painter p = designer.getPainter();
				if (p instanceof HoverPainter) {
					painter = (HoverPainter) p;
				}
			}

			if (painter != null) {
				// Ϊ��ʾ��Ⱦ�����ý���λ�á��������Ⱦ����
				Rectangle rect = ComponentUtils.getRelativeBounds(container);
				rect.x -= designer.getArea().getHorizontalValue();
				rect.y -= designer.getArea().getVerticalValue();
				painter.setRenderingBounds(rect);
				painter.setHotspot(new Point(x, y));
				painter.setCreator(addingModel.getXCreator());
			}
		} else {
			// �����겻���κ�����ϣ���ȡ����ʾ��
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
     * ��ק����
     * @param dtde     �¼�
     */
	public void dragEnter(DropTargetDragEvent dtde) {
		Point loc = dtde.getLocation();
		this.entering(loc.x, loc.y);
	}

    /**
     * ��ק�ƶ�����
     * @param dtde     �¼�
     */
	public void dragOver(DropTargetDragEvent dtde) {
		Point loc = dtde.getLocation();
		hovering(loc.x, loc.y);
	}

    /**
     * ��ק�¼�
     * @param dtde     �¼�
     */
	public void dropActionChanged(DropTargetDragEvent dtde) {
	}

    /**
     * ��ק�뿪
     * @param dte     �¼�
     */
	public void dragExit(DropTargetEvent dte) {
		this.exiting();
	}

    /**
     * ��ק�ͷ�
     * @param dtde     �¼�
     */
	public void drop(DropTargetDropEvent dtde) {
		Point loc = dtde.getLocation();
		this.adding(loc.x, loc.y);
        //����ڱ�������һ���ؼ�ֱ��ctrl+s�޷�Ӧ
        designer.requestFocus();
	}
}
