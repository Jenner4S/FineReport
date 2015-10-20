package com.fr.design.mainframe;

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComponentUI;

import com.fr.base.BaseUtils;
import com.fr.base.GraphHelper;
import com.fr.base.Utils;
import com.fr.design.constants.UIConstants;
import com.fr.design.designer.beans.AdapterBus;
import com.fr.design.designer.beans.ComponentAdapter;
import com.fr.design.designer.beans.location.Direction;
import com.fr.design.designer.beans.models.AddingModel;
import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWFitLayout;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.design.roleAuthority.ReportAndFSManagePane;
import com.fr.design.utils.ComponentUtils;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import com.fr.stable.Constants;

/**
 * FormDesigner��UI�࣬��һ����״̬��UI�࣬������FormDesigner�ĵ�ǰ״̬����
 * �������������õ���ƽ��棬�Լ���ǰ��ƽ����һЩ����״̬������ѡ���ʶ���϶����� �Լ���ǰ������ӵ����
 */
public class FormDesignerUI extends ComponentUI {

    // ��ǰ�������
    private FormDesigner designer;
    private SelectionModel selectionModel;
    private Rectangle2D.Double back_or_selection_rect = new Rectangle2D.Double(0, 0, 0, 0);

    public FormDesignerUI() {
    }

    /**
     *  ��ʼ������
     * @param c      ���
     */
    public void installUI(JComponent c) {
        designer = (FormDesigner) c;
        selectionModel = designer.getSelectionModel();
    }

    /**
     * ��Ⱦ��ǰ����ƽ����Լ���Ƹ���״̬
     * @param g ��ͼ��
     * @param c ���
     */
    @Override
    public void paint(Graphics g, JComponent c) {
        XCreator rootComponent = designer.getRootComponent();
        if (rootComponent != null) {
            // �������Ӧ����
            repaintFit(g, rootComponent, c);
        }
        XCreator paraComponent = designer.getParaComponent();
        if (paraComponent != null) {
            // ��Ʋ������
            repaintPara(g, paraComponent, c);
        }

        if (designer.isDrawLineMode() && designer.getDrawLineHelper().drawLining()) {
            designer.getDrawLineHelper().drawAuxiliaryLine(g);
            return;
        }

        paintSelection(g);

        if (BaseUtils.isAuthorityEditing()) {
            paintAuthorityDetails(g, designer.getRootComponent());
        }

        Rectangle hotspot_bounds = selectionModel.getHotspotBounds();

        if (hotspot_bounds != null) {
            // ��ǰ����ѡ���
            g.setColor(XCreatorConstants.SELECTION_COLOR);
            g.drawRect(hotspot_bounds.x - designer.getArea().getHorizontalValue(), hotspot_bounds.y
                    - designer.getArea().getVerticalValue(), hotspot_bounds.width, hotspot_bounds.height);
        }

        if (designer.getPainter() != null) {
            // ComponentAdapter��LayoutAdapter�ṩ�Ķ����Painter����Painterһ��������ʾ���ã�
            // �൱��һ��������
            designer.getPainter().paint(g, designer.getArea().getHorizontalValue(),
                    designer.getArea().getVerticalValue());
        }
        AddingModel addingModel = designer.getAddingModel();

        if ((addingModel != null) && (addingModel.getXCreator() != null)) {
            // ��ǰ������ӵ����
            paintAddingBean(g, addingModel);
        }
    }

    private int[] getActualLine(int i) {
        int j[];
        switch (i) {
            case Direction.TOP:
                j = new int[]{Direction.TOP};
                break;
            case Direction.BOTTOM:
                j = new int[]{Direction.BOTTOM};
                break;
            case Direction.LEFT:
                j = new int[]{Direction.LEFT};
                break;
            case Direction.RIGHT:
                j = new int[]{Direction.RIGHT};
                break;
            case Direction.LEFT_TOP:
                j = new int[]{Direction.TOP, Direction.LEFT};
                break;
            case Direction.LEFT_BOTTOM:
                j = new int[]{Direction.BOTTOM, Direction.LEFT};
                break;
            case Direction.RIGHT_TOP:
                j = new int[]{Direction.TOP, Direction.RIGHT};
                break;
            case Direction.RIGHT_BOTTOM:
                j = new int[]{Direction.BOTTOM, Direction.RIGHT};
                break;
            default:
                j = new int[]{Direction.TOP, Direction.LEFT};
                break;
        }
        return j;
    }

    private void paintPositionLine(Graphics g, Rectangle bounds, int l[]) {
        Graphics2D g2d = (Graphics2D) g.create();
        int x1, y1, x2, y2;
        String text;
        for (int k : l) {
            if (k == 1 || k == 2) {
                x1 = 0;
                x2 = 6;
                y2 = y1 = bounds.y - designer.getArea().getVerticalValue() + (k == 1 ? 0 : bounds.height);
                text = Utils.objectToString(y1 + designer.getArea().getVerticalValue());
            } else {
                y1 = 0;
                y2 = 6;
                x1 = x2 = bounds.x - designer.getArea().getHorizontalValue() + (k == 3 ? 0 : bounds.width);
                text = Utils.objectToString(x1 + designer.getArea().getHorizontalValue());
            }
            text += Inter.getLocText("FR-Designer_Indent-Pixel");
            g2d.setColor(XCreatorConstants.RESIZE_BOX_BORDER_COLOR);
            GraphHelper.drawString(g2d, text, x1 + 3, y1 + 10);
            GraphHelper.drawLine(g2d, x1, y1, x2, y2);
        }
        g2d.dispose();
    }

    /**
     * ��Ⱦ��ǰ������ӵ����������Rendererԭ��
     */
    private void paintAddingBean(Graphics g, final AddingModel addingModel) {
        XCreator bean = addingModel.getXCreator();
        int x = addingModel.getCurrentX();
        int y = addingModel.getCurrentY();
        int width = bean.getWidth();
        int height = bean.getHeight();
        Graphics clipg = g.create(x, y, width, height);
        ArrayList<JComponent> dbcomponents = new ArrayList<JComponent>();
        // ��ֹ˫������Ϊ
        ComponentUtils.disableBuffer(bean, dbcomponents);

        ComponentAdapter adapter = AdapterBus.getComponentAdapter(designer, bean);
        // ����ComponentAdapter��paintComponentMascot������Ⱦ����������ʾ
        adapter.paintComponentMascot(clipg);
        clipg.dispose();
        // �ָ�˫����
        ComponentUtils.resetBuffer(dbcomponents);
    }


    private void paintAuthorityCreator(Graphics2D g2d, Rectangle creatorBounds) {
        back_or_selection_rect.setRect(creatorBounds.getX(), creatorBounds.getY(),
                creatorBounds.getWidth(), creatorBounds.getHeight());
        Area borderLineArea = new Area(back_or_selection_rect);
        GraphHelper.fill(g2d, borderLineArea);
    }

    /**
     *  ��Ȩ�ޱ༭��
     * @param g             ��ͼ��
     * @param xCreator  ���
     */
    public void paintAuthorityDetails(Graphics g, XCreator xCreator) {
        String selectedRoles = ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName();
        if (selectedRoles == null) {
            return;
        }
        Graphics2D g2d = (Graphics2D) g;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
        g2d.setPaint(UIConstants.AUTHORITY_COLOR);
        int count = xCreator.getComponentCount();
        for (int i = 0; i < count; i++) {
            XCreator subCreator = (XCreator) xCreator.getComponent(i);
            if (subCreator instanceof XLayoutContainer) {
                paintAuthorityDetails(g, subCreator);
            } else {
            	if (subCreator.toData().isDirtyWidget(selectedRoles)) {
                    Rectangle creatorBounds = ComponentUtils.getRelativeBounds(subCreator);
                    creatorBounds.x -= designer.getArea().getHorizontalValue();
                    creatorBounds.y -= designer.getArea().getVerticalValue();
                    paintAuthorityCreator(g2d, creatorBounds);
                }
            }
        }
        g2d.setPaintMode();
    }

    /**
     * ��ѡ�з�Χ
     * @param g ��ͼ
     */
    public void paintSelection(Graphics g) {
        if (!selectionModel.hasSelectionComponent()) {
            return;
        }
        Rectangle bounds = designer.getTopContainer().getBounds();
        bounds.x = -designer.getArea().getHorizontalValue();
        bounds.y = -designer.getArea().getVerticalValue();
        Graphics clipg = g.create();
        clipg.clipRect(bounds.x, bounds.y, bounds.width + 1, bounds.height + 1);
        paintResizing(clipg);
        clipg.dispose();
    }

    /**
     * ������ǰѡ����ק״̬��
     *
     * @param g  ͼ��
     */
    private void paintResizing(Graphics g) {
        Rectangle bounds = selectionModel.getSelection().getRelativeBounds();
        if (designer.hasWAbsoluteLayout() && designer.getStateModel().getDirection() != null) {
            int[] actualline = getActualLine(designer.getStateModel().getDirection().getActual());
            paintPositionLine(g, bounds, actualline);
        }
        if (designer.getStateModel().isDragging()) {
            designer.getStateModel().paintAbsorptionline(g);
        }

        bounds.x -= designer.getArea().getHorizontalValue();
        bounds.y -= designer.getArea().getVerticalValue();

        drawResizingThumbs(g, selectionModel.getSelection().getDirections(), bounds.x, bounds.y, bounds.width, bounds.height);
        g.setColor(XCreatorConstants.SELECTION_COLOR);

        for (XCreator creator : selectionModel.getSelection().getSelectedCreators()) {
            Rectangle creatorBounds = ComponentUtils.getRelativeBounds(creator);
            creatorBounds.x -= designer.getArea().getHorizontalValue();
            creatorBounds.y -= designer.getArea().getVerticalValue();
            if (creator.acceptType(XWFitLayout.class)) {
            	resetFitlayoutBounds(creatorBounds);
            } else if (designer.getRootComponent().acceptType(XWFitLayout.class)) {
            	resetCreatorBounds(creatorBounds);
            }
            GraphHelper.draw(g, creatorBounds, Constants.LINE_MEDIUM);
        }
    }
    
    /**
     * ��ʼΪ����Ӧʱ������ѡ�еķ�Χ
     * @param bound
     */
    private void resetFitlayoutBounds( Rectangle bound) {
		bound.x ++;
		bound.width -= 2;
		bound.y ++;
		bound.height -= 2;
    }
    
    private void resetCreatorBounds( Rectangle bound) {
    	Rectangle rec = bound;
    	if (rec.x == 0) {
    		bound.x ++;
    		bound.width --;
    	}
    	if (rec.y == 0) {
    		bound.y ++;
    		bound.height --;
    	}
    	if (rec.x+rec.width == designer.getWidth()) {
    		bound.width --;
    	}
    	if (rec.y+rec.height == designer.getHeight()) {
    		bound.height --;
    	}
    }
    

    /**
     * �����˸���ק��
     */
    private void drawResizingThumbs(Graphics g, int[] directions, int x, int y, int w, int h) {
        int bx = x - XCreatorConstants.RESIZE_BOX_SIZ;
        int by = y - XCreatorConstants.RESIZE_BOX_SIZ;

        if (ArrayUtils.contains(directions, Direction.LEFT_TOP)) {
            drawBox(g, bx, by);
        }
        if (ArrayUtils.contains(directions, Direction.TOP)) {
            bx = x + ((w - XCreatorConstants.RESIZE_BOX_SIZ) / 2);
            drawBox(g, bx, by);
        }
        if (ArrayUtils.contains(directions, Direction.RIGHT_TOP)) {
            bx = x + w;
            drawBox(g, bx, by);
        }
        if (ArrayUtils.contains(directions, Direction.LEFT)) {
            bx = x - XCreatorConstants.RESIZE_BOX_SIZ;
            by = y + ((h - XCreatorConstants.RESIZE_BOX_SIZ) / 2);
            drawBox(g, bx, by);
        }
        if (ArrayUtils.contains(directions, Direction.LEFT_BOTTOM)) {
            bx = x - XCreatorConstants.RESIZE_BOX_SIZ;
            by = y + h;
            drawBox(g, bx, by);
        }
        if (ArrayUtils.contains(directions, Direction.BOTTOM)) {
            bx = x + ((w - XCreatorConstants.RESIZE_BOX_SIZ) / 2);
            by = y + h;
            drawBox(g, bx, by);
        }
        if (ArrayUtils.contains(directions, Direction.RIGHT_BOTTOM)) {
            bx = x + w;
            by = y + h;
            drawBox(g, bx, by);
        }
        if (ArrayUtils.contains(directions, Direction.RIGHT)) {
            bx = x + w;
            by = y + ((h - XCreatorConstants.RESIZE_BOX_SIZ) / 2);
            drawBox(g, bx, by);
        }
    }

    /**
     * ��ÿһ��С��ק��
     */
    private void drawBox(Graphics g, int x, int y) {
        g.setColor(XCreatorConstants.RESIZE_BOX_INNER_COLOR);
        g.fillRect(x, y, XCreatorConstants.RESIZE_BOX_SIZ, XCreatorConstants.RESIZE_BOX_SIZ);
        g.setColor(XCreatorConstants.RESIZE_BOX_BORDER_COLOR);
        g.drawRect(x, y, XCreatorConstants.RESIZE_BOX_SIZ, XCreatorConstants.RESIZE_BOX_SIZ);
    }

    /**
     * ������Ӧ����
     */
    private void repaintFit(Graphics g, Component component, Component parent) {
        try {
            SwingUtilities.updateComponentTreeUI(component);
        } catch (Exception ex) {
        }
        ArrayList<JComponent> dbcomponents = new ArrayList<JComponent>();
        // ��ֹ˫����
        ComponentUtils.disableBuffer(component, dbcomponents);
        Graphics clipg;
        clipg = g.create(-designer.getArea().getHorizontalValue(), -designer.getArea().getVerticalValue() + designer.getParaHeight(), parent
                .getSize().width + designer.getArea().getHorizontalValue(), parent.getSize().height
                + designer.getArea().getVerticalValue());

        designer.paintContent(clipg);
        clipg.dispose();

        // �ָ�˫����
        ComponentUtils.resetBuffer(dbcomponents);
        designer.resetEditorComponentBounds();
    }
    
    /**
     * ���������
     */
    private void repaintPara(Graphics g, Component component, Component parent) {
        try {
            SwingUtilities.updateComponentTreeUI(component);
        } catch (Exception ex) {
        }
        ArrayList<JComponent> dbcomponents = new ArrayList<JComponent>();
        // ��ֹ˫����
        ComponentUtils.disableBuffer(component, dbcomponents);
        Graphics clipg1;
        clipg1 = g.create(-designer.getArea().getHorizontalValue(), -designer.getArea().getVerticalValue() , parent
                .getSize().width + designer.getArea().getHorizontalValue(), designer.getParaHeight()
                + designer.getArea().getVerticalValue());

        designer.paintPara(clipg1);
        clipg1.dispose();

        // �ָ�˫����
        ComponentUtils.resetBuffer(dbcomponents);
    }
    
}
