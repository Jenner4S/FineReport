// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.grid;

import com.fr.base.*;
import com.fr.design.cell.clipboard.CellElementsClip;
import com.fr.design.cell.clipboard.ElementsTransferable;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.general.FRLogger;
import com.fr.general.script.FunctionHelper;
import com.fr.grid.selection.CellSelection;
import com.fr.report.cell.*;
import com.fr.report.cell.cellattr.core.group.DSColumn;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.stable.ColumnRow;
import java.awt.Rectangle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Referenced classes of package com.fr.grid:
//            GridUtils

public class IntelliElements
{
    private abstract class CounterClockwiseDragHelper extends DragHelper
    {

        final IntelliElements this$0;

        public void copy(CellElementsClip cellelementsclip)
        {
            for(int i = getStart(); i > getEnd(); i -= getStep())
            {
                int ai[] = getRect(i);
                cellelementsclip.pasteAtRegion(reportPane.getEditingElementCase(), ai[0], ai[1], ai[2], ai[3], ai[4], ai[5]);
            }

        }

        private CounterClockwiseDragHelper()
        {
            this$0 = IntelliElements.this;
            super();
        }

    }

    private abstract class ClockwiseDragHelper extends DragHelper
    {

        final IntelliElements this$0;

        public void copy(CellElementsClip cellelementsclip)
        {
            for(int i = getStart(); i < getEnd(); i += getStep())
            {
                int ai[] = getRect(i);
                cellelementsclip.pasteAtRegion(reportPane.getEditingElementCase(), ai[0], ai[1], ai[2], ai[3], ai[4], ai[5]);
            }

        }

        private ClockwiseDragHelper()
        {
            this$0 = IntelliElements.this;
            super();
        }

    }

    private abstract class DragHelper
    {

        final IntelliElements this$0;

        protected abstract boolean havetoModify();

        protected abstract void copy(CellElementsClip cellelementsclip);

        public abstract int getStart();

        public abstract int getEnd();

        public abstract int getStep();

        public abstract int[] getRect(int i);

        public void replicate()
        {
            if(havetoModify())
            {
                ElementsTransferable elementstransferable = GridUtils.caculateElementsTransferable(reportPane);
                CellElementsClip cellelementsclip = null;
                Object obj = elementstransferable.getFirstObject();
                if(obj != null && (obj instanceof CellElementsClip))
                    cellelementsclip = (CellElementsClip)obj;
                if(cellelementsclip != null)
                    copy(cellelementsclip);
                reportPane.setSelection(new CellSelection(dragCellRectangle.x, dragCellRectangle.y, dragCellRectangle.width, dragCellRectangle.height));
            }
        }

        public void doIntelliAction()
        {
            int i = getStartColumnIndex();
            for(int j = getEndColumnIndex(); i < j; i++)
            {
                int k = getStartRowIndex();
                for(int l = getEndRowIndex(); k < l; k++)
                {
                    Object obj = getSourceCellElementByColumnRow(i, k);
                    if(obj == null)
                        obj = new DefaultTemplateCellElement();
                    DefaultTemplateCellElement defaulttemplatecellelement = new DefaultTemplateCellElement(i, k);
                    applyStyle(defaulttemplatecellelement, ((CellElement) (obj)));
                    if(((TemplateCellElement) (obj)).getValue() instanceof DSColumn)
                    {
                        DSColumn dscolumn = (DSColumn)((TemplateCellElement) (obj)).getValue();
                        defaulttemplatecellelement.setValue(dscolumn);
                        defaulttemplatecellelement.setCellExpandAttr(((TemplateCellElement) (obj)).getCellExpandAttr());
                    } else
                    if(((TemplateCellElement) (obj)).getValue() instanceof Number)
                        defaulttemplatecellelement.setValue(processNumber((Number)((TemplateCellElement) (obj)).getValue()));
                    else
                    if(((TemplateCellElement) (obj)).getValue() instanceof Formula)
                    {
                        Formula formula = (Formula)((TemplateCellElement) (obj)).getValue();
                        formula = generateSimpleFormula(formula, 1);
                        defaulttemplatecellelement.setValue(formula);
                    } else
                    {
                        try
                        {
                            defaulttemplatecellelement.setValue(BaseUtils.cloneObject(((TemplateCellElement) (obj)).getValue()));
                        }
                        catch(CloneNotSupportedException clonenotsupportedexception)
                        {
                            FRContext.getLogger().error(clonenotsupportedexception.getMessage(), clonenotsupportedexception);
                        }
                    }
                    report.addCellElement(defaulttemplatecellelement);
                }

            }

        }

        protected abstract int getStartColumnIndex();

        protected abstract int getEndColumnIndex();

        protected abstract int getStartRowIndex();

        protected abstract int getEndRowIndex();

        protected abstract TemplateCellElement getSourceCellElementByColumnRow(int i, int j);

        protected abstract Number processNumber(Number number);

        protected abstract ColumnRow processColumnRow(ColumnRow columnrow, int i);

        private Formula generateSimpleFormula(Formula formula, int i)
        {
            Formula formula1;
            try
            {
                formula1 = (Formula)(Formula)formula.clone();
            }
            catch(CloneNotSupportedException clonenotsupportedexception)
            {
                formula1 = new Formula();
                FRContext.getLogger().error(clonenotsupportedexception.getMessage(), clonenotsupportedexception);
            }
            String s = formula.getContent();
            StringBuffer stringbuffer = new StringBuffer();
            String s1 = "[a-z|A-Z]+[0-9]+";
            Pattern pattern = Pattern.compile(s1);
            Matcher matcher = pattern.matcher(s);
            int j = 0;
            do
            {
                if(!matcher.find())
                    break;
                int k = matcher.start();
                int l = matcher.end();
                char c = s.charAt(k - 1);
                if(c != '$')
                {
                    String s2 = s.substring(k, l);
                    ColumnRow columnrow = processColumnRow(BaseUtils.convertCellStringToColumnRow(s2), i);
                    String s3 = BaseUtils.convertColumnRowToCellString(columnrow);
                    stringbuffer.append(s.substring(j, k));
                    stringbuffer.append(s3);
                    j = l;
                }
            } while(true);
            stringbuffer.append(s.substring(j, s.length()));
            formula1.setContent(stringbuffer.toString());
            return formula1;
        }

        private DragHelper()
        {
            this$0 = IntelliElements.this;
            super();
        }

    }


    public static final int DIRECTION_UNDEF = -1;
    public static final int DIRECTION_UP_TO_DOWN = 0;
    public static final int DIRECTION_DOWN_TO_UP = 1;
    public static final int DIRECTION_LEFT_TO_RIGHT = 2;
    public static final int DIRECTION_RIGHT_TO_LEFT = 3;
    public static final int ACTION_SEQUENCING = 0;
    public static final int ACTION_REPLICATION = 1;
    public static final int FORMULA_NONE_PARA_SAME = -1;
    public static final int FORMULA_HOR_PARA_SAME = 0;
    public static final int FORMULA_VER_PARA_SAME = 1;
    private int direction;
    private int action;
    private boolean isStyleSupported;
    private ElementCasePane reportPane;
    private TemplateElementCase report;
    private Rectangle oldCellRectangle;
    private Rectangle dragCellRectangle;
    private DragHelper L2RDHelper;
    private DragHelper R2LDHelper;
    private DragHelper U2DDHelper;
    private DragHelper D2UDHelper;

    public static void iterating(ElementCasePane elementcasepane, Rectangle rectangle, Rectangle rectangle1)
    {
        IntelliElements intellielements = new IntelliElements(elementcasepane, rectangle, rectangle1);
        intellielements.setAction(0);
        intellielements.setStyleSupported(true);
        intellielements.doIntelliAction();
    }

    public IntelliElements(ElementCasePane elementcasepane, Rectangle rectangle, Rectangle rectangle1)
    {
        direction = -1;
        action = 0;
        isStyleSupported = true;
        oldCellRectangle = null;
        dragCellRectangle = null;
        L2RDHelper = new ClockwiseDragHelper() {

            final IntelliElements this$0;

            public int getStart()
            {
                return oldCellRectangle.x + oldCellRectangle.width;
            }

            public int getEnd()
            {
                return dragCellRectangle.x + dragCellRectangle.width;
            }

            public int getStep()
            {
                return oldCellRectangle.width;
            }

            public int[] getRect(int i)
            {
                return (new int[] {
                    i, oldCellRectangle.y, i, oldCellRectangle.y, Math.min(oldCellRectangle.width, (dragCellRectangle.x + dragCellRectangle.width) - i), oldCellRectangle.height
                });
            }

            public boolean havetoModify()
            {
                return dragCellRectangle.width > oldCellRectangle.width;
            }

            public int getStartColumnIndex()
            {
                return oldCellRectangle.x + ((CellSelection)reportPane.getSelection()).getColumnSpan();
            }

            public int getEndColumnIndex()
            {
                return dragCellRectangle.x + dragCellRectangle.width;
            }

            public int getStartRowIndex()
            {
                return oldCellRectangle.y;
            }

            public int getEndRowIndex()
            {
                return oldCellRectangle.y + oldCellRectangle.height;
            }

            public TemplateCellElement getSourceCellElementByColumnRow(int i, int j)
            {
                return report.getTemplateCellElement(i - oldCellRectangle.width, j);
            }

            protected Number processNumber(Number number)
            {
                return FunctionHelper.asNumber(number.doubleValue() + 1.0D);
            }

            protected ColumnRow processColumnRow(ColumnRow columnrow, int i)
            {
                return ColumnRow.valueOf(columnrow.column + i, columnrow.row);
            }

            
            {
                this$0 = IntelliElements.this;
                super();
            }
        }
;
        R2LDHelper = new CounterClockwiseDragHelper() {

            final IntelliElements this$0;

            public int getStart()
            {
                return oldCellRectangle.x - oldCellRectangle.width;
            }

            public int getEnd()
            {
                return dragCellRectangle.x - oldCellRectangle.width;
            }

            public int getStep()
            {
                return oldCellRectangle.width;
            }

            public int[] getRect(int i)
            {
                return (new int[] {
                    i, oldCellRectangle.y, Math.max(i, dragCellRectangle.x), oldCellRectangle.y, Math.min(oldCellRectangle.width, oldCellRectangle.width - (dragCellRectangle.x - i)), oldCellRectangle.height
                });
            }

            public boolean havetoModify()
            {
                return true;
            }

            public int getStartRowIndex()
            {
                return oldCellRectangle.y;
            }

            public int getEndRowIndex()
            {
                return oldCellRectangle.y + oldCellRectangle.height;
            }

            public int getStartColumnIndex()
            {
                return dragCellRectangle.x;
            }

            public int getEndColumnIndex()
            {
                return oldCellRectangle.x;
            }

            public TemplateCellElement getSourceCellElementByColumnRow(int i, int j)
            {
                return report.getTemplateCellElement(oldCellRectangle.x + (i - dragCellRectangle.x) % oldCellRectangle.width, j);
            }

            protected Number processNumber(Number number)
            {
                return number;
            }

            protected ColumnRow processColumnRow(ColumnRow columnrow, int i)
            {
                return ColumnRow.valueOf(Math.max(0, columnrow.column - i), columnrow.row);
            }

            
            {
                this$0 = IntelliElements.this;
                super();
            }
        }
;
        U2DDHelper = new ClockwiseDragHelper() {

            final IntelliElements this$0;

            public int getStart()
            {
                return oldCellRectangle.y + oldCellRectangle.height;
            }

            public int getEnd()
            {
                return dragCellRectangle.y + dragCellRectangle.height;
            }

            public int getStep()
            {
                return oldCellRectangle.height;
            }

            public int[] getRect(int i)
            {
                return (new int[] {
                    oldCellRectangle.x, i, oldCellRectangle.x, i, oldCellRectangle.width, Math.min(oldCellRectangle.height, (dragCellRectangle.y + dragCellRectangle.height) - i)
                });
            }

            public boolean havetoModify()
            {
                return dragCellRectangle.height > oldCellRectangle.height;
            }

            public int getStartColumnIndex()
            {
                return oldCellRectangle.x;
            }

            public int getEndColumnIndex()
            {
                return oldCellRectangle.x + oldCellRectangle.width;
            }

            public int getStartRowIndex()
            {
                return oldCellRectangle.y + ((CellSelection)reportPane.getSelection()).getRowSpan();
            }

            public int getEndRowIndex()
            {
                return dragCellRectangle.y + dragCellRectangle.height;
            }

            public TemplateCellElement getSourceCellElementByColumnRow(int i, int j)
            {
                return report.getTemplateCellElement(i, j - oldCellRectangle.height);
            }

            protected Number processNumber(Number number)
            {
                return FunctionHelper.asNumber(number.doubleValue() + 1.0D);
            }

            protected ColumnRow processColumnRow(ColumnRow columnrow, int i)
            {
                return ColumnRow.valueOf(columnrow.column, columnrow.row + i);
            }

            
            {
                this$0 = IntelliElements.this;
                super();
            }
        }
;
        D2UDHelper = new CounterClockwiseDragHelper() {

            final IntelliElements this$0;

            public int getStart()
            {
                return oldCellRectangle.y - oldCellRectangle.height;
            }

            public int getEnd()
            {
                return dragCellRectangle.y - oldCellRectangle.height;
            }

            public int getStep()
            {
                return oldCellRectangle.height;
            }

            public int[] getRect(int i)
            {
                return (new int[] {
                    oldCellRectangle.x, i, oldCellRectangle.x, Math.max(i, dragCellRectangle.y), oldCellRectangle.width, Math.min(oldCellRectangle.height, oldCellRectangle.height - (dragCellRectangle.y - i))
                });
            }

            public boolean havetoModify()
            {
                return true;
            }

            public int getStartRowIndex()
            {
                return dragCellRectangle.y;
            }

            public int getEndRowIndex()
            {
                return oldCellRectangle.y;
            }

            public int getStartColumnIndex()
            {
                return oldCellRectangle.x;
            }

            public int getEndColumnIndex()
            {
                return oldCellRectangle.x + oldCellRectangle.width;
            }

            public TemplateCellElement getSourceCellElementByColumnRow(int i, int j)
            {
                return report.getTemplateCellElement(i, oldCellRectangle.y + (j - dragCellRectangle.y) % oldCellRectangle.height);
            }

            protected Number processNumber(Number number)
            {
                return number;
            }

            protected ColumnRow processColumnRow(ColumnRow columnrow, int i)
            {
                return ColumnRow.valueOf(columnrow.column, Math.max(0, columnrow.row - i));
            }

            
            {
                this$0 = IntelliElements.this;
                super();
            }
        }
;
        reportPane = elementcasepane;
        report = elementcasepane.getEditingElementCase();
        oldCellRectangle = rectangle;
        dragCellRectangle = rectangle1;
    }

    public void setAction(int i)
    {
        action = i;
    }

    public void setStyleSupported(boolean flag)
    {
        isStyleSupported = flag;
    }

    public void doIntelliAction()
    {
        analyzeDirection();
        if(action == 1)
        {
            doReplication();
            return;
        }
        if(direction == 0)
            U2DDHelper.doIntelliAction();
        else
        if(direction == 1)
            D2UDHelper.doIntelliAction();
        else
        if(direction == 2)
            L2RDHelper.doIntelliAction();
        else
        if(direction == 3)
            R2LDHelper.doIntelliAction();
        reportPane.setSelection(new CellSelection(dragCellRectangle.x, dragCellRectangle.y, dragCellRectangle.width, dragCellRectangle.height));
        reportPane.repaint();
    }

    private void doReplication()
    {
        if(direction == 0)
            U2DDHelper.replicate();
        else
        if(direction == 1)
            D2UDHelper.replicate();
        else
        if(direction == 2)
            L2RDHelper.replicate();
        else
        if(direction == 3)
            R2LDHelper.replicate();
    }

    private void analyzeDirection()
    {
        if(dragCellRectangle.x == oldCellRectangle.x && dragCellRectangle.width == oldCellRectangle.width)
        {
            if(dragCellRectangle.y == oldCellRectangle.y)
                direction = 0;
            else
            if(dragCellRectangle.y < oldCellRectangle.y)
                direction = 1;
        } else
        if(dragCellRectangle.y == oldCellRectangle.y && dragCellRectangle.height == oldCellRectangle.height)
            if(dragCellRectangle.x == oldCellRectangle.x)
                direction = 2;
            else
            if(dragCellRectangle.x < oldCellRectangle.x)
                direction = 3;
    }

    private void applyStyle(CellElement cellelement, CellElement cellelement1)
    {
        if(isStyleSupported)
            cellelement.setStyle(cellelement1.getStyle());
    }





}
