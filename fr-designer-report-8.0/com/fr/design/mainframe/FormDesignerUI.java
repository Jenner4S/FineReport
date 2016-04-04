// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.base.*;
import com.fr.design.constants.UIConstants;
import com.fr.design.designer.beans.*;
import com.fr.design.designer.beans.location.Direction;
import com.fr.design.designer.beans.models.*;
import com.fr.design.designer.creator.*;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.design.roleAuthority.ReportAndFSManagePane;
import com.fr.design.roleAuthority.RoleTree;
import com.fr.design.utils.ComponentUtils;
import com.fr.form.ui.Widget;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComponentUI;

// Referenced classes of package com.fr.design.mainframe:
//            FormDesigner, ConnectorHelper, FormArea, FormSelection

public class FormDesignerUI extends ComponentUI
{

    private FormDesigner designer;
    private SelectionModel selectionModel;
    private java.awt.geom.Rectangle2D.Double back_or_selection_rect;

    public FormDesignerUI()
    {
        back_or_selection_rect = new java.awt.geom.Rectangle2D.Double(0.0D, 0.0D, 0.0D, 0.0D);
    }

    public void installUI(JComponent jcomponent)
    {
        designer = (FormDesigner)jcomponent;
        selectionModel = designer.getSelectionModel();
    }

    public void paint(Graphics g, JComponent jcomponent)
    {
        XLayoutContainer xlayoutcontainer = designer.getRootComponent();
        if(xlayoutcontainer != null)
            repaintFit(g, xlayoutcontainer, jcomponent);
        XLayoutContainer xlayoutcontainer1 = designer.getParaComponent();
        if(xlayoutcontainer1 != null)
            repaintPara(g, xlayoutcontainer1, jcomponent);
        if(designer.isDrawLineMode() && designer.getDrawLineHelper().drawLining())
        {
            designer.getDrawLineHelper().drawAuxiliaryLine(g);
            return;
        }
        paintSelection(g);
        if(BaseUtils.isAuthorityEditing())
            paintAuthorityDetails(g, designer.getRootComponent());
        Rectangle rectangle = selectionModel.getHotspotBounds();
        if(rectangle != null)
        {
            g.setColor(XCreatorConstants.SELECTION_COLOR);
            g.drawRect(rectangle.x - designer.getArea().getHorizontalValue(), rectangle.y - designer.getArea().getVerticalValue(), rectangle.width, rectangle.height);
        }
        if(designer.getPainter() != null)
            designer.getPainter().paint(g, designer.getArea().getHorizontalValue(), designer.getArea().getVerticalValue());
        AddingModel addingmodel = designer.getAddingModel();
        if(addingmodel != null && addingmodel.getXCreator() != null)
            paintAddingBean(g, addingmodel);
    }

    private int[] getActualLine(int i)
    {
        int ai[];
        switch(i)
        {
        case 1: // '\001'
            ai = (new int[] {
                1
            });
            break;

        case 2: // '\002'
            ai = (new int[] {
                2
            });
            break;

        case 3: // '\003'
            ai = (new int[] {
                3
            });
            break;

        case 4: // '\004'
            ai = (new int[] {
                4
            });
            break;

        case 5: // '\005'
            ai = (new int[] {
                1, 3
            });
            break;

        case 6: // '\006'
            ai = (new int[] {
                2, 3
            });
            break;

        case 7: // '\007'
            ai = (new int[] {
                1, 4
            });
            break;

        case 8: // '\b'
            ai = (new int[] {
                2, 4
            });
            break;

        default:
            ai = (new int[] {
                1, 3
            });
            break;
        }
        return ai;
    }

    private void paintPositionLine(Graphics g, Rectangle rectangle, int ai[])
    {
        Graphics2D graphics2d = (Graphics2D)g.create();
        int ai1[] = ai;
        int i1 = ai1.length;
        for(int j1 = 0; j1 < i1; j1++)
        {
            int k1 = ai1[j1];
            int i;
            int j;
            int k;
            int l;
            String s;
            if(k1 == 1 || k1 == 2)
            {
                i = 0;
                k = 6;
                l = j = (rectangle.y - designer.getArea().getVerticalValue()) + (k1 != 1 ? rectangle.height : 0);
                s = Utils.objectToString(Integer.valueOf(j + designer.getArea().getVerticalValue()));
            } else
            {
                j = 0;
                l = 6;
                i = k = (rectangle.x - designer.getArea().getHorizontalValue()) + (k1 != 3 ? rectangle.width : 0);
                s = Utils.objectToString(Integer.valueOf(i + designer.getArea().getHorizontalValue()));
            }
            s = (new StringBuilder()).append(s).append(Inter.getLocText("FR-Designer_Indent-Pixel")).toString();
            graphics2d.setColor(XCreatorConstants.RESIZE_BOX_BORDER_COLOR);
            GraphHelper.drawString(graphics2d, s, i + 3, j + 10);
            GraphHelper.drawLine(graphics2d, i, j, k, l);
        }

        graphics2d.dispose();
    }

    private void paintAddingBean(Graphics g, AddingModel addingmodel)
    {
        XCreator xcreator = addingmodel.getXCreator();
        int i = addingmodel.getCurrentX();
        int j = addingmodel.getCurrentY();
        int k = xcreator.getWidth();
        int l = xcreator.getHeight();
        Graphics g1 = g.create(i, j, k, l);
        ArrayList arraylist = new ArrayList();
        ComponentUtils.disableBuffer(xcreator, arraylist);
        ComponentAdapter componentadapter = AdapterBus.getComponentAdapter(designer, xcreator);
        componentadapter.paintComponentMascot(g1);
        g1.dispose();
        ComponentUtils.resetBuffer(arraylist);
    }

    private void paintAuthorityCreator(Graphics2D graphics2d, Rectangle rectangle)
    {
        back_or_selection_rect.setRect(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
        Area area = new Area(back_or_selection_rect);
        GraphHelper.fill(graphics2d, area);
    }

    public void paintAuthorityDetails(Graphics g, XCreator xcreator)
    {
        String s = ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName();
        if(s == null)
            return;
        Graphics2D graphics2d = (Graphics2D)g;
        graphics2d.setComposite(AlphaComposite.getInstance(3, 0.7F));
        graphics2d.setPaint(UIConstants.AUTHORITY_COLOR);
        int i = xcreator.getComponentCount();
        for(int j = 0; j < i; j++)
        {
            XCreator xcreator1 = (XCreator)xcreator.getComponent(j);
            if(xcreator1 instanceof XLayoutContainer)
            {
                paintAuthorityDetails(g, xcreator1);
                continue;
            }
            if(xcreator1.toData().isDirtyWidget(s))
            {
                Rectangle rectangle = ComponentUtils.getRelativeBounds(xcreator1);
                rectangle.x -= designer.getArea().getHorizontalValue();
                rectangle.y -= designer.getArea().getVerticalValue();
                paintAuthorityCreator(graphics2d, rectangle);
            }
        }

        graphics2d.setPaintMode();
    }

    public void paintSelection(Graphics g)
    {
        if(!selectionModel.hasSelectionComponent())
        {
            return;
        } else
        {
            Rectangle rectangle = designer.getTopContainer().getBounds();
            rectangle.x = -designer.getArea().getHorizontalValue();
            rectangle.y = -designer.getArea().getVerticalValue();
            Graphics g1 = g.create();
            g1.clipRect(rectangle.x, rectangle.y, rectangle.width + 1, rectangle.height + 1);
            paintResizing(g1);
            g1.dispose();
            return;
        }
    }

    private void paintResizing(Graphics g)
    {
        Rectangle rectangle = selectionModel.getSelection().getRelativeBounds();
        if(designer.hasWAbsoluteLayout() && designer.getStateModel().getDirection() != null)
        {
            int ai[] = getActualLine(designer.getStateModel().getDirection().getActual());
            paintPositionLine(g, rectangle, ai);
        }
        if(designer.getStateModel().isDragging())
            designer.getStateModel().paintAbsorptionline(g);
        rectangle.x -= designer.getArea().getHorizontalValue();
        rectangle.y -= designer.getArea().getVerticalValue();
        drawResizingThumbs(g, selectionModel.getSelection().getDirections(), rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        g.setColor(XCreatorConstants.SELECTION_COLOR);
        XCreator axcreator[] = selectionModel.getSelection().getSelectedCreators();
        int i = axcreator.length;
        for(int j = 0; j < i; j++)
        {
            XCreator xcreator = axcreator[j];
            Rectangle rectangle1 = ComponentUtils.getRelativeBounds(xcreator);
            rectangle1.x -= designer.getArea().getHorizontalValue();
            rectangle1.y -= designer.getArea().getVerticalValue();
            if(xcreator.acceptType(new Class[] {
    com/fr/design/designer/creator/XWFitLayout
}))
                resetFitlayoutBounds(rectangle1);
            else
            if(designer.getRootComponent().acceptType(new Class[] {
    com/fr/design/designer/creator/XWFitLayout
}))
                resetCreatorBounds(rectangle1);
            GraphHelper.draw(g, rectangle1, 2);
        }

    }

    private void resetFitlayoutBounds(Rectangle rectangle)
    {
        rectangle.x++;
        rectangle.width -= 2;
        rectangle.y++;
        rectangle.height -= 2;
    }

    private void resetCreatorBounds(Rectangle rectangle)
    {
        Rectangle rectangle1 = rectangle;
        if(rectangle1.x == 0)
        {
            rectangle.x++;
            rectangle.width--;
        }
        if(rectangle1.y == 0)
        {
            rectangle.y++;
            rectangle.height--;
        }
        if(rectangle1.x + rectangle1.width == designer.getWidth())
            rectangle.width--;
        if(rectangle1.y + rectangle1.height == designer.getHeight())
            rectangle.height--;
    }

    private void drawResizingThumbs(Graphics g, int ai[], int i, int j, int k, int l)
    {
        int i1 = i - 5;
        int i3 = j - 5;
        if(ArrayUtils.contains(ai, 5))
            drawBox(g, i1, i3);
        if(ArrayUtils.contains(ai, 1))
        {
            int j1 = i + (k - 5) / 2;
            drawBox(g, j1, i3);
        }
        if(ArrayUtils.contains(ai, 7))
        {
            int k1 = i + k;
            drawBox(g, k1, i3);
        }
        if(ArrayUtils.contains(ai, 3))
        {
            int l1 = i - 5;
            int j3 = j + (l - 5) / 2;
            drawBox(g, l1, j3);
        }
        if(ArrayUtils.contains(ai, 6))
        {
            int i2 = i - 5;
            int k3 = j + l;
            drawBox(g, i2, k3);
        }
        if(ArrayUtils.contains(ai, 2))
        {
            int j2 = i + (k - 5) / 2;
            int l3 = j + l;
            drawBox(g, j2, l3);
        }
        if(ArrayUtils.contains(ai, 8))
        {
            int k2 = i + k;
            int i4 = j + l;
            drawBox(g, k2, i4);
        }
        if(ArrayUtils.contains(ai, 4))
        {
            int l2 = i + k;
            int j4 = j + (l - 5) / 2;
            drawBox(g, l2, j4);
        }
    }

    private void drawBox(Graphics g, int i, int j)
    {
        g.setColor(XCreatorConstants.RESIZE_BOX_INNER_COLOR);
        g.fillRect(i, j, 5, 5);
        g.setColor(XCreatorConstants.RESIZE_BOX_BORDER_COLOR);
        g.drawRect(i, j, 5, 5);
    }

    private void repaintFit(Graphics g, Component component, Component component1)
    {
        try
        {
            SwingUtilities.updateComponentTreeUI(component);
        }
        catch(Exception exception) { }
        ArrayList arraylist = new ArrayList();
        ComponentUtils.disableBuffer(component, arraylist);
        Graphics g1 = g.create(-designer.getArea().getHorizontalValue(), -designer.getArea().getVerticalValue() + designer.getParaHeight(), component1.getSize().width + designer.getArea().getHorizontalValue(), component1.getSize().height + designer.getArea().getVerticalValue());
        designer.paintContent(g1);
        g1.dispose();
        ComponentUtils.resetBuffer(arraylist);
        designer.resetEditorComponentBounds();
    }

    private void repaintPara(Graphics g, Component component, Component component1)
    {
        try
        {
            SwingUtilities.updateComponentTreeUI(component);
        }
        catch(Exception exception) { }
        ArrayList arraylist = new ArrayList();
        ComponentUtils.disableBuffer(component, arraylist);
        Graphics g1 = g.create(-designer.getArea().getHorizontalValue(), -designer.getArea().getVerticalValue(), component1.getSize().width + designer.getArea().getHorizontalValue(), designer.getParaHeight() + designer.getArea().getVerticalValue());
        designer.paintPara(g1);
        g1.dispose();
        ComponentUtils.resetBuffer(arraylist);
    }
}
