// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.grid.dnd;

import com.fr.base.FRContext;
import com.fr.design.actions.ToggleButtonUpdateAction;
import com.fr.design.actions.UpdateAction;
import com.fr.design.gui.ibutton.UIToggleButton;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.general.data.TableDataColumn;
import com.fr.grid.Grid;
import com.fr.grid.GridUtils;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.Selection;
import com.fr.report.cell.*;
import com.fr.report.cell.cellattr.CellExpandAttr;
import com.fr.report.cell.cellattr.core.group.DSColumn;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.report.poly.PolyECBlock;
import com.fr.report.worksheet.FormElementCase;
import com.fr.report.worksheet.WorkSheet;
import com.fr.stable.ArrayUtils;
import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class ElementCasePaneDropTarget extends DropTargetAdapter
{
    private class SortAction extends UpdateAction
        implements ToggleButtonUpdateAction
    {

        private int direction;
        final ElementCasePaneDropTarget this$0;

        public void actionPerformed(ActionEvent actionevent)
        {
            sortCellElement();
        }

        public UIToggleButton createToolBarComponent()
        {
            return GUICoreUtils.createToolBarComponent(this);
        }

        private void sortCellElement()
        {
            cs = (CellSelection)ePane.getSelection();
            if(doubleArray == null)
                return;
            if(!canMove())
                return;
            int i = doubleArray.length;
            for(int j = 0; j < i; j++)
            {
                if(j != 0)
                {
                    CellElement cellelement = null;
                    CellSelection cellselection = (CellSelection)ePane.getSelection();
                    if(direction == 0)
                    {
                        cellelement = ePane.getEditingElementCase().getCellElement(cellselection.getColumn() + cellselection.getColumnSpan(), cellselection.getRow());
                        if(cellelement == null)
                            ePane.setSelection(new CellSelection(cellselection.getColumn() + cellselection.getColumnSpan(), cellselection.getRow(), 1, 1));
                    } else
                    if(direction == 1)
                    {
                        cellelement = ePane.getEditingElementCase().getCellElement(cellselection.getColumn() - cellselection.getColumnSpan(), cellselection.getRow());
                        if(cellelement == null)
                            ePane.setSelection(new CellSelection(cellselection.getColumn() - cellselection.getColumnSpan(), cellselection.getRow(), 1, 1));
                    } else
                    if(direction == 2)
                    {
                        cellelement = ePane.getEditingElementCase().getCellElement(cellselection.getColumn(), cellselection.getRow() + cellselection.getRowSpan());
                        if(cellelement == null)
                            ePane.setSelection(new CellSelection(cellselection.getColumn(), cellselection.getRow() + cellselection.getRowSpan(), 1, 1));
                    } else
                    if(direction == 3)
                    {
                        cellelement = ePane.getEditingElementCase().getCellElement(cellselection.getColumn(), cellselection.getRow() - cellselection.getRowSpan());
                        if(cellelement == null)
                            ePane.setSelection(new CellSelection(cellselection.getColumn(), cellselection.getRow() - cellselection.getRowSpan(), 1, 1));
                    }
                    if(cellelement != null)
                        ePane.setSelection(new CellSelection(cellelement.getColumn(), cellelement.getRow(), cellelement.getColumnSpan(), cellelement.getRowSpan()));
                }
                paintDropCellElement(j);
            }

        }

        private boolean canMove()
        {
            int i = doubleArray.length;
            if(direction == 1)
            {
                int j = (cs.getColumn() - i) + 1;
                if(j < 0)
                {
                    JOptionPane.showMessageDialog(ePane, Inter.getLocText("Utils-Beyond_the_left_side_of_Border"));
                    return false;
                }
            } else
            if(direction == 3)
            {
                int k = (cs.getRow() - i) + 1;
                if(k < 0)
                {
                    JOptionPane.showMessageDialog(ePane, Inter.getLocText("Utils-Beyond_the_top_side_of_Border"));
                    return false;
                }
            }
            if(ePane.mustInVisibleRange())
                if(direction == 0)
                {
                    if(!GridUtils.canMove(ePane, (cs.getColumn() + i) - 1, cs.getRow()))
                    {
                        JOptionPane.showMessageDialog(ePane, Inter.getLocText("Utils-Beyond_the_right_side_of_Border"));
                        return false;
                    }
                } else
                if(direction == 2 && !GridUtils.canMove(ePane, cs.getRow(), (cs.getColumn() + i) - 1))
                {
                    JOptionPane.showMessageDialog(ePane, Inter.getLocText("Utils-Beyond_the_bottom_side_of_Border"));
                    return false;
                }
            return true;
        }

        public volatile JComponent createToolBarComponent()
        {
            return createToolBarComponent();
        }


        public SortAction(int i)
        {
            this$0 = ElementCasePaneDropTarget.this;
            super();
            direction = i;
            if(i == 0)
                setName(Inter.getLocText("Utils-Left_to_Right_a"));
            else
            if(i == 1)
                setName(Inter.getLocText("Utils-Right_to_Left"));
            else
            if(i == 2)
                setName(Inter.getLocText("Utils-Top_to_Bottom_a"));
            else
            if(i == 3)
                setName(Inter.getLocText("Utils-Bottom_to_Top"));
            setMnemonic('S');
        }
    }


    private static final int LEFT_2_RIGHT = 0;
    private static final int RIGHT_2_LEFT = 1;
    private static final int TOP_2_BOTTOM = 2;
    private static final int BOTTOM_2_TOP = 3;
    private ElementCasePane ePane;
    private CellSelection cs;
    private String doubleArray[][];

    public ElementCasePaneDropTarget(ElementCasePane elementcasepane)
    {
        doubleArray = (String[][])null;
        ePane = elementcasepane;
        new DropTarget(elementcasepane.getGrid(), this);
    }

    public void drop(DropTargetDropEvent droptargetdropevent)
    {
        Point point = droptargetdropevent.getLocation();
        DropTargetContext droptargetcontext = droptargetdropevent.getDropTargetContext();
        Grid grid = (Grid)droptargetcontext.getComponent();
        Selection selection = grid.getElementCasePane().getSelection();
        TemplateElementCase templateelementcase = grid.getElementCasePane().getEditingElementCase();
        if(!(selection instanceof CellSelection) || templateelementcase == null)
        {
            droptargetdropevent.rejectDrop();
            return;
        }
        CellSelection cellselection = (CellSelection)selection;
        try
        {
            Transferable transferable = droptargetdropevent.getTransferable();
            java.awt.datatransfer.DataFlavor adataflavor[] = transferable.getTransferDataFlavors();
            for(int i = 0; i < adataflavor.length; i++)
            {
                if(!transferable.isDataFlavorSupported(adataflavor[i]))
                    continue;
                droptargetdropevent.acceptDrop(droptargetdropevent.getDropAction());
                Object obj = transferable.getTransferData(adataflavor[i]);
                if(obj instanceof Class)
                {
                    Class class1 = (Class)obj;
                    grid.startCellEditingAt_DEC(cellselection.getColumn(), cellselection.getRow(), class1, false);
                } else
                if(obj instanceof String[][])
                {
                    doubleArray = (String[][])(String[][])obj;
                    if(doubleArray.length > 1)
                    {
                        JPopupMenu jpopupmenu = new JPopupMenu();
                        GUICoreUtils.showPopupMenu(createPopupMenu(jpopupmenu), ePane.getGrid(), (int)point.getX() + 1, (int)point.getY() + 1);
                    } else
                    {
                        (new SortAction(0)).sortCellElement();
                    }
                }
                droptargetdropevent.dropComplete(true);
                ePane.fireSelectionChangeListener();
            }

            droptargetdropevent.rejectDrop();
        }
        catch(Exception exception)
        {
            FRContext.getLogger().error(exception.getMessage(), exception);
        }
    }

    private JPopupMenu createPopupMenu(JPopupMenu jpopupmenu)
    {
        jpopupmenu = new JPopupMenu();
        jpopupmenu.add((new SortAction(0)).createMenuItem());
        jpopupmenu.add((new SortAction(1)).createMenuItem());
        jpopupmenu.add((new SortAction(2)).createMenuItem());
        jpopupmenu.add((new SortAction(3)).createMenuItem());
        return jpopupmenu;
    }

    private void paintDropCellElement(int i)
    {
        TemplateElementCase templateelementcase = ePane.getEditingElementCase();
        DefaultTemplateCellElement defaulttemplatecellelement = new DefaultTemplateCellElement();
        if((templateelementcase instanceof WorkSheet) || (templateelementcase instanceof PolyECBlock) || (templateelementcase instanceof FormElementCase))
        {
            String as[] = doubleArray[i];
            if(ArrayUtils.isEmpty(as))
                return;
            DSColumn dscolumn = null;
            DSColumn dscolumn1 = new DSColumn();
            dscolumn1.setDSName(as[0]);
            String s = as[1];
            TableDataColumn tabledatacolumn;
            if(s.length() > 0 && s.charAt(0) == '#')
            {
                int j = Integer.parseInt(s.substring(1));
                tabledatacolumn = TableDataColumn.createColumn(j);
            } else
            {
                tabledatacolumn = TableDataColumn.createColumn(s);
            }
            dscolumn1.setColumn(tabledatacolumn);
            dscolumn = dscolumn1;
            CellSelection cellselection = (CellSelection)ePane.getSelection();
            Object obj = templateelementcase.getTemplateCellElement(cellselection.getColumn(), cellselection.getRow());
            CellExpandAttr cellexpandattr = new CellExpandAttr();
            cellexpandattr.setDirection((byte)0);
            if(obj == null)
            {
                obj = new DefaultTemplateCellElement(cellselection.getColumn(), cellselection.getRow(), cellselection.getColumnSpan(), cellselection.getRowSpan(), dscolumn);
                templateelementcase.addCellElement(((TemplateCellElement) (obj)));
            } else
            {
                ((TemplateCellElement) (obj)).setValue(dscolumn);
                ((TemplateCellElement) (obj)).setCellExpandAttr(cellexpandattr);
            }
            ((TemplateCellElement) (obj)).setCellExpandAttr(cellexpandattr);
        }
        ePane.setSupportDefaultParentCalculate(true);
        ePane.fireTargetModified();
        ePane.setSupportDefaultParentCalculate(false);
    }

    public void dragEnter(DropTargetDragEvent droptargetdragevent)
    {
        droptargetdragevent.acceptDrag(droptargetdragevent.getDropAction());
    }

    public void dragOver(DropTargetDragEvent droptargetdragevent)
    {
        Point point = droptargetdragevent.getLocation();
        DropTargetContext droptargetcontext = droptargetdragevent.getDropTargetContext();
        Grid grid = (Grid)droptargetcontext.getComponent();
        grid.doMousePress(point.getX(), point.getY());
        droptargetdragevent.acceptDrag(droptargetdragevent.getDropAction());
    }





}
