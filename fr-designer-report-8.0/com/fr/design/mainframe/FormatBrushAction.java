// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.base.Style;
import com.fr.design.actions.ElementCaseAction;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.general.Inter;
import com.fr.grid.Grid;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.Selection;
import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.elementcase.TemplateElementCase;
import java.awt.Rectangle;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

// Referenced classes of package com.fr.design.mainframe:
//            ElementCasePane, DesignerContext

public class FormatBrushAction extends ElementCaseAction
{

    private ElementCasePane ePane;
    private CellSelection oldSelection;

    public FormatBrushAction(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
        setName(Inter.getLocText("M_Edit-FormatBrush"));
        setMnemonic('B');
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_edit/formatBrush.png"));
        setAccelerator(KeyStroke.getKeyStroke(66, 2));
    }

    public boolean executeActionReturnUndoRecordNeeded()
    {
        ePane = (ElementCasePane)getEditingComponent();
        if(ePane != null)
        {
            Selection selection = ePane.getSelection();
            if(!(selection instanceof CellSelection))
                return false;
            oldSelection = ((CellSelection)selection).clone();
            ePane.setFormatReferencedCell(oldSelection);
            int i = oldSelection.getCellRectangleCount();
            if(i > 1 && !isContinueArea())
            {
                JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), Inter.getLocText("Can_not_use_FormatBursh"));
                ePane.setFormatState(0);
                ePane.getFormatBrush().setSelected(false);
                return false;
            } else
            {
                ((ElementCasePane)DesignerContext.getReferencedElementCasePane()).getGrid().setNotShowingTableSelectPane(false);
                ePane.repaint();
                return true;
            }
        } else
        {
            return false;
        }
    }

    private boolean isContinueArea()
    {
        int i = oldSelection.getCellRectangle(1).x;
        int j = 0;
        int k = oldSelection.getCellRectangle(1).y;
        int l = 0;
        int i1 = 0;
        for(int j1 = 0; j1 < oldSelection.getCellRectangleCount(); j1++)
        {
            Rectangle rectangle = oldSelection.getCellRectangle(j1);
            if(rectangle.getX() < (double)i)
                i = rectangle.x;
            if(rectangle.getX() + rectangle.getWidth() > (double)j)
                j = (int)(rectangle.getX() + rectangle.getWidth());
            if(rectangle.getY() < (double)k)
                k = rectangle.y;
            if(rectangle.getY() + rectangle.getHeight() > (double)l)
                l = (int)(rectangle.getY() + rectangle.getHeight());
            i1 += (int)(rectangle.getWidth() * rectangle.getHeight());
        }

        if((j - i) * (l - k) == i1)
        {
            oldSelection = new CellSelection(i, k, j - i, l - k);
            ePane.setSelection(oldSelection);
            ePane.setFormatReferencedCell(oldSelection);
            return true;
        } else
        {
            return false;
        }
    }

    public void updateFormatBrush(Style astyle[][], CellSelection cellselection, ElementCasePane elementcasepane)
    {
        if(astyle == null)
            return;
        int i = astyle[0].length;
        int j = astyle.length;
        TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        int k = cellselection.getRowSpan();
        int l = cellselection.getColumnSpan();
        if(cellselection.getColumnSpan() * cellselection.getRowSpan() == 1)
        {
            k = i;
            l = j;
        }
        for(int i1 = 0; i1 < k; i1++)
        {
            for(int j1 = 0; j1 < l; j1++)
            {
                int k1 = j1 + cellselection.getColumn();
                int l1 = i1 + cellselection.getRow();
                Object obj = templateelementcase.getTemplateCellElement(k1, l1);
                if(obj == null)
                {
                    obj = new DefaultTemplateCellElement(k1, l1);
                    templateelementcase.addCellElement(((TemplateCellElement) (obj)));
                }
                ((TemplateCellElement) (obj)).setStyle(astyle[j1 % j][i1 % i]);
            }

        }

    }

    private Style[][] getOldStyles(CellSelection cellselection)
    {
        Style astyle[][] = new Style[cellselection.getColumnSpan()][cellselection.getRowSpan()];
        int i = cellselection.getCellRectangleCount();
        TemplateElementCase templateelementcase = ePane.getEditingElementCase();
        for(int j = 0; j < i; j++)
        {
            Rectangle rectangle = cellselection.getCellRectangle(j);
            for(int k = 0; k < rectangle.height; k++)
            {
                for(int l = 0; l < rectangle.width; l++)
                {
                    int i1 = l + rectangle.x;
                    int j1 = k + rectangle.y;
                    Object obj = templateelementcase.getTemplateCellElement(i1, j1);
                    if(obj == null)
                    {
                        obj = new DefaultTemplateCellElement(i1, j1);
                        templateelementcase.addCellElement(((TemplateCellElement) (obj)));
                    }
                    Style style = ((TemplateCellElement) (obj)).getStyle();
                    if(style == null)
                    {
                        Style _tmp = style;
                        style = Style.DEFAULT_STYLE;
                    }
                    astyle[l][k] = style;
                }

            }

        }

        return astyle;
    }
}
