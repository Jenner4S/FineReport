// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.poly.hanlder;

import com.fr.base.*;
import com.fr.base.chart.BaseChart;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.general.FRLogger;
import com.fr.grid.Grid;
import com.fr.poly.PolyDesigner;
import com.fr.poly.PolyUtils;
import com.fr.poly.creator.BlockCreator;
import com.fr.poly.creator.PolyElementCasePane;
import com.fr.poly.model.AddingData;
import com.fr.report.poly.TemplateBlock;
import com.fr.stable.unit.UnitRectangle;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import javax.swing.JScrollBar;

// Referenced classes of package com.fr.poly.hanlder:
//            BlockForbiddenWindow

public class PolyDesignerDropTarget extends DropTargetAdapter
{

    private static final double SCROLL_POINT = 100D;
    private static final int SCROLL_DISTANCE = 15;
    private PolyDesigner designer;
    private AddingData addingData;
    private int resolution;
    private BlockForbiddenWindow forbiddenWindow;

    public PolyDesignerDropTarget(PolyDesigner polydesigner)
    {
        resolution = ScreenResolution.getScreenResolution();
        forbiddenWindow = new BlockForbiddenWindow();
        designer = polydesigner;
        new DropTarget(polydesigner, this);
    }

    public void dragEnter(DropTargetDragEvent droptargetdragevent)
    {
        if(BaseUtils.isAuthorityEditing())
            return;
        if(designer.getAddingData() != null)
            return;
        Transferable transferable;
        java.awt.datatransfer.DataFlavor adataflavor[];
        int i;
        transferable = droptargetdragevent.getTransferable();
        adataflavor = transferable.getTransferDataFlavors();
        i = 0;
_L2:
        BlockCreator blockcreator;
        if(i >= adataflavor.length)
            break MISSING_BLOCK_LABEL_165;
        if(!transferable.isDataFlavorSupported(adataflavor[i]))
            break MISSING_BLOCK_LABEL_144;
        Object obj = transferable.getTransferData(adataflavor[i]);
        blockcreator = null;
        if(obj instanceof Class)
        {
            Class class1 = (Class)obj;
            blockcreator = PolyUtils.createCreator(class1);
        } else
        if(obj instanceof BaseChart)
            blockcreator = PolyUtils.createCreator((BaseChart)obj);
        if(blockcreator == null)
            return;
        addingData = new AddingData(blockcreator);
        designer.setAddingData(addingData);
        i++;
        if(true) goto _L2; else goto _L1
_L1:
        Exception exception;
        exception;
        FRContext.getLogger().error(exception.getMessage(), exception);
        Point point = droptargetdragevent.getLocation();
        addingData.moveTo(point);
        designer.repaint();
        return;
    }

    public void dragOver(DropTargetDragEvent droptargetdragevent)
    {
        if(addingData != null)
        {
            Point point = droptargetdragevent.getLocation();
            addingData.moveTo(point);
            setForbiddenWindowVisibility(point);
            scrollWhileDropCorner(droptargetdragevent);
            designer.repaint();
        }
    }

    private void scrollWhileDropCorner(final DropTargetDragEvent dtde)
    {
        Thread thread = new Thread(new Runnable() {

            final DropTargetDragEvent val$dtde;
            final PolyDesignerDropTarget this$0;

            public void run()
            {
                Point point = dtde.getLocation();
                if((double)point.x > (double)designer.getWidth() - 100D)
                {
                    JScrollBar jscrollbar = designer.getHorizontalScrollBar();
                    jscrollbar.setValue(jscrollbar.getValue() + 15);
                }
                if((double)point.y > (double)designer.getHeight() - 100D)
                {
                    JScrollBar jscrollbar1 = designer.getVerticalScrollBar();
                    jscrollbar1.setValue(jscrollbar1.getValue() + 15);
                }
            }

            
            {
                this$0 = PolyDesignerDropTarget.this;
                dtde = droptargetdragevent;
                super();
            }
        }
);
        thread.start();
    }

    private void setForbiddenWindowVisibility(Point point)
    {
        BlockCreator blockcreator = addingData.getCreator();
        Rectangle rectangle = getCreatorPixRectangle(blockcreator, point);
        UnitRectangle unitrectangle = new UnitRectangle(rectangle, resolution);
        if(designer.intersectsAllBlock(unitrectangle, blockcreator.getValue().getBlockName()))
        {
            int i = (int)((designer.getAreaLocationX() + rectangle.getCenterX()) - (double)designer.getHorizontalValue());
            int j = (int)((designer.getAreaLocationY() + rectangle.getCenterY()) - (double)designer.getVerticalValue());
            forbiddenWindow.showWindow(i, j);
        } else
        {
            forbiddenWindow.hideWindow();
        }
    }

    public void drop(DropTargetDropEvent droptargetdropevent)
    {
        if(addingData != null)
        {
            designer.stopAddingState();
            BlockCreator blockcreator = addingData.getCreator();
            Point point = droptargetdropevent.getLocation();
            Rectangle rectangle = getCreatorPixRectangle(blockcreator, point);
            if(!intersectLocation(rectangle, blockcreator))
                return;
            designer.addBlockCreator(blockcreator);
            designer.stopEditing();
            designer.setSelection(blockcreator);
            DesignerContext.getDesignerFrame().resetToolkitByPlus(DesignerContext.getDesignerFrame().getSelectedJTemplate());
            focusOnSelection();
            designer.fireTargetModified();
            addingData = null;
        }
    }

    private void focusOnSelection()
    {
        if(designer.getSelection().getEditingElementCasePane() == null)
            return;
        Grid grid = designer.getSelection().getEditingElementCasePane().getGrid();
        if(!grid.hasFocus() && grid.isRequestFocusEnabled())
            grid.requestFocus();
    }

    private boolean intersectLocation(Rectangle rectangle, BlockCreator blockcreator)
    {
        if(rectangle.getX() < 0.0D || rectangle.getY() < 0.0D)
        {
            forbiddenWindow.hideWindow();
            designer.repaint();
            return false;
        }
        UnitRectangle unitrectangle = new UnitRectangle(rectangle, resolution);
        if(designer.intersectsAllBlock(unitrectangle, blockcreator.getValue().getBlockName()))
        {
            forbiddenWindow.hideWindow();
            designer.repaint();
            return false;
        } else
        {
            blockcreator.getValue().setBounds(unitrectangle);
            return true;
        }
    }

    private Rectangle getCreatorPixRectangle(BlockCreator blockcreator, Point point)
    {
        int i = blockcreator.getWidth();
        int j = blockcreator.getHeight();
        int k = (point.x - i / 2) + designer.getHorizontalValue();
        int l = (point.y - j / 2) + designer.getVerticalValue();
        return new Rectangle(k, l, i, j);
    }

    public void dragExit(DropTargetEvent droptargetevent)
    {
        if(addingData != null)
        {
            addingData.reset();
            designer.repaint();
        }
        forbiddenWindow.hideWindow();
    }

}
