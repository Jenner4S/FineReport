// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.design.designer.beans.AdapterBus;
import com.fr.design.designer.beans.HoverPainter;
import com.fr.design.designer.beans.events.CreatorEventListenerTable;
import com.fr.design.designer.beans.models.AddingModel;
import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.designer.creator.*;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.utils.ComponentUtils;
import com.fr.form.ui.Widget;
import com.fr.general.Inter;
import java.awt.*;
import java.awt.dnd.*;
import javax.swing.BorderFactory;
import javax.swing.JWindow;

// Referenced classes of package com.fr.design.mainframe:
//            FormDesigner, FormSelectionUtils, FormArea

public class FormCreatorDropTarget extends DropTarget
{

    private FormDesigner designer;
    private Component current;
    private AddingModel addingModel;
    private static final int GAP = 30;
    private JWindow promptWindow;
    private UIButton promptButton;

    public FormCreatorDropTarget(FormDesigner formdesigner)
    {
        promptWindow = new JWindow();
        promptButton = new UIButton("", BaseUtils.readIcon("/com/fr/web/images/form/forbid.png"));
        designer = formdesigner;
        addingModel = formdesigner.getAddingModel();
        promptWindow.add(promptButton);
    }

    private void adding(int i, int j)
    {
        XCreator xcreator = designer.getComponentAt(i, j);
        XLayoutContainer xlayoutcontainer = XCreatorUtils.getHotspotContainer(xcreator);
        xlayoutcontainer.stopAddingState(designer);
        boolean flag = false;
        if(xlayoutcontainer != null)
        {
            AddingModel addingmodel = designer.getAddingModel();
            boolean flag1 = !addingModel.getXCreator().canEnterIntoParaPane() && xlayoutcontainer.acceptType(new Class[] {
                com/fr/design/designer/creator/XWParameterLayout
            });
            boolean flag2 = !addingModel.getXCreator().canEnterIntoAdaptPane() && xlayoutcontainer.acceptType(new Class[] {
                com/fr/design/designer/creator/XWFitLayout
            });
            if(addingmodel != null && !flag1 && !flag2)
                flag = addingmodel.add2Container(designer, xlayoutcontainer, i, j);
            cancelPromptWidgetForbidEnter();
        }
        if(flag)
        {
            XLayoutContainer xlayoutcontainer1 = xlayoutcontainer.acceptType(new Class[] {
                com/fr/design/designer/creator/XWParameterLayout
            }) ? designer.getParaComponent() : designer.getRootComponent();
            designer.getSelectionModel().setSelectedCreators(FormSelectionUtils.rebuildSelection(xlayoutcontainer1, new Widget[] {
                addingModel.getXCreator().toData()
            }));
            designer.getEditListenerTable().fireCreatorModified(addingModel.getXCreator(), 1);
        } else
        {
            Toolkit.getDefaultToolkit().beep();
        }
        designer.setPainter(null);
        designer.stopAddingState();
    }

    private void entering(int i, int j)
    {
        addingModel.moveTo(i, j);
        designer.repaint();
    }

    private void exiting()
    {
        cancelPromptWidgetForbidEnter();
        addingModel.reset();
        designer.setPainter(null);
        designer.repaint();
    }

    private void hovering(int i, int j)
    {
        addingModel.moveTo(i, j);
        XCreator xcreator = designer.getComponentAt(i, j);
        XLayoutContainer xlayoutcontainer = XCreatorUtils.getHotspotContainer(xcreator);
        promptUser(i, j, xlayoutcontainer);
        if(xlayoutcontainer != null)
        {
            HoverPainter hoverpainter = null;
            if(xlayoutcontainer != current || designer.getPainter() == null)
            {
                if(current != null)
                    designer.setPainter(null);
                hoverpainter = AdapterBus.getContainerPainter(designer, xlayoutcontainer);
                designer.setPainter(hoverpainter);
                current = xlayoutcontainer;
            } else
            {
                com.fr.design.designer.beans.Painter painter = designer.getPainter();
                if(painter instanceof HoverPainter)
                    hoverpainter = (HoverPainter)painter;
            }
            if(hoverpainter != null)
            {
                Rectangle rectangle = ComponentUtils.getRelativeBounds(xlayoutcontainer);
                rectangle.x -= designer.getArea().getHorizontalValue();
                rectangle.y -= designer.getArea().getVerticalValue();
                hoverpainter.setRenderingBounds(rectangle);
                hoverpainter.setHotspot(new Point(i, j));
                hoverpainter.setCreator(addingModel.getXCreator());
            }
        } else
        {
            designer.setPainter(null);
            current = null;
        }
        designer.repaint();
    }

    private void promptUser(int i, int j, XLayoutContainer xlayoutcontainer)
    {
        if(!addingModel.getXCreator().canEnterIntoParaPane() && xlayoutcontainer.acceptType(new Class[] {
    com/fr/design/designer/creator/XWParameterLayout
}))
        {
            promptButton.setText(Inter.getLocText("FR-Designer_Forbid_Drag_into_Para_Pane"));
            promptWidgetForbidEnter(i, j, xlayoutcontainer);
        } else
        if(!addingModel.getXCreator().canEnterIntoAdaptPane() && xlayoutcontainer.acceptType(new Class[] {
    com/fr/design/designer/creator/XWFitLayout
}))
        {
            promptButton.setText(Inter.getLocText("FR-Designer_Forbid_Drag_into_Adapt_Pane"));
            promptWidgetForbidEnter(i, j, xlayoutcontainer);
        } else
        {
            cancelPromptWidgetForbidEnter();
        }
    }

    private void promptWidgetForbidEnter(int i, int j, XLayoutContainer xlayoutcontainer)
    {
        xlayoutcontainer.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        int k = (int)designer.getArea().getLocationOnScreen().getX();
        int l = (int)designer.getArea().getLocationOnScreen().getY();
        promptWindow.setSize(promptWindow.getPreferredSize());
        promptWindow.setPreferredSize(promptWindow.getPreferredSize());
        promptWindow.setLocation(k + i + 30, l + j + 30);
        promptWindow.setVisible(true);
    }

    private void cancelPromptWidgetForbidEnter()
    {
        if(designer.getParaComponent() != null)
            designer.getParaComponent().setBorder(BorderFactory.createLineBorder(XCreatorConstants.LAYOUT_SEP_COLOR, 1));
        designer.getRootComponent().setBorder(BorderFactory.createLineBorder(XCreatorConstants.LAYOUT_SEP_COLOR, 1));
        promptWindow.setVisible(false);
    }

    public void dragEnter(DropTargetDragEvent droptargetdragevent)
    {
        Point point = droptargetdragevent.getLocation();
        entering(point.x, point.y);
    }

    public void dragOver(DropTargetDragEvent droptargetdragevent)
    {
        Point point = droptargetdragevent.getLocation();
        hovering(point.x, point.y);
    }

    public void dropActionChanged(DropTargetDragEvent droptargetdragevent)
    {
    }

    public void dragExit(DropTargetEvent droptargetevent)
    {
        exiting();
    }

    public void drop(DropTargetDropEvent droptargetdropevent)
    {
        Point point = droptargetdropevent.getLocation();
        adding(point.x, point.y);
        designer.requestFocus();
    }
}
