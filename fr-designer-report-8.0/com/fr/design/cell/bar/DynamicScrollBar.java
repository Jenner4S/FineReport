// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.cell.bar;

import com.fr.base.DynamicUnitList;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.Grid;
import com.fr.grid.GridUtils;
import com.fr.report.ReportHelper;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.stable.unit.FU;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.accessibility.AccessibleContext;
import javax.swing.BoundedRangeModel;
import javax.swing.JScrollBar;

// Referenced classes of package com.fr.design.cell.bar:
//            DynamicScrollBarUI, BarHelper

public class DynamicScrollBar extends JScrollBar
{
    private abstract class DynamicScrollBarHelper extends BarHelper
    {

        final DynamicScrollBar this$0;

        protected abstract DynamicUnitList getSizeList();

        protected abstract int getLastIndex();

        public int getExtent(int i)
        {
            return GridUtils.getExtentValue(i, getSizeList(), getVisibleSize(), dpi) - i;
        }

        private int adjustNewValue(int i, int j, DynamicUnitList dynamicunitlist)
        {
            if(i < 0)
                i = 0;
            if(j < i)
            {
                int k = i - j;
                int i1 = 0;
                int k1 = j + 1;
                do
                {
                    if(dynamicunitlist.get(k1).more_than_zero() && ++i1 == k)
                    {
                        i = k1;
                        break;
                    }
                    k1++;
                } while(true);
            } else
            if(j > i)
            {
                int l = j - i;
                int j1 = 0;
                int l1 = j - 1;
                do
                {
                    if(l1 < 0)
                        break;
                    if(dynamicunitlist.get(l1).more_than_zero() && ++j1 == l)
                    {
                        i = l1;
                        break;
                    }
                    l1--;
                } while(true);
            }
            return i;
        }

        public void setValue(int i)
        {
            int j = getValue();
            DynamicUnitList dynamicunitlist = getSizeList();
            int k = getLastIndex();
            i = adjustNewValue(i, j, dynamicunitlist);
            int l = getExtent(i);
            int i1 = Math.max(k, l) + 1;
            i1 = Math.max(i + l, i1);
            BoundedRangeModel boundedrangemodel = getModel();
            if(i != boundedrangemodel.getValue() || l != boundedrangemodel.getExtent() || i1 != boundedrangemodel.getMaximum())
            {
                boundedrangemodel.setRangeProperties(i, l, 0, i1, true);
                resetBeginValue(i);
                resetExtentValue(l);
                reportPane.repaint(40L);
                if(DynamicScrollBarHelper != null)
                    DynamicScrollBarHelper.firePropertyChange("AccessibleValue", new Integer(j), new Integer(getValue()));
            }
        }

        private DynamicScrollBarHelper()
        {
            this$0 = DynamicScrollBar.this;
            super();
        }

    }


    private ElementCasePane reportPane;
    private boolean isSupportHide;
    private int dpi;
    private DynamicScrollBarHelper verticalBarHelper;
    private DynamicScrollBarHelper horizontalBarHelper;

    public DynamicScrollBar(int i, ElementCasePane elementcasepane, int j)
    {
        super(i);
        isSupportHide = true;
        verticalBarHelper = new DynamicScrollBarHelper() {

            final DynamicScrollBar this$0;

            public int getLastIndex()
            {
                return reportPane.getEditingElementCase().getRowCount() - 1;
            }

            public DynamicUnitList getSizeList()
            {
                return ReportHelper.getRowHeightList(reportPane.getEditingElementCase());
            }

            public double getVisibleSize()
            {
                return (double)reportPane.getGrid().getHeight();
            }

            public void resetBeginValue(int k)
            {
                reportPane.getGrid().setVerticalValue(k);
            }

            public void resetExtentValue(int k)
            {
                reportPane.getGrid().setVerticalExtent(k);
            }

            
            {
                this$0 = DynamicScrollBar.this;
                super();
            }
        }
;
        horizontalBarHelper = new DynamicScrollBarHelper() {

            final DynamicScrollBar this$0;

            public int getLastIndex()
            {
                return reportPane.getEditingElementCase().getColumnCount() - 1;
            }

            public DynamicUnitList getSizeList()
            {
                return ReportHelper.getColumnWidthList(reportPane.getEditingElementCase());
            }

            public double getVisibleSize()
            {
                return (double)reportPane.getGrid().getWidth();
            }

            public void resetBeginValue(int k)
            {
                reportPane.getGrid().setHorizontalValue(k);
            }

            public void resetExtentValue(int k)
            {
                reportPane.getGrid().setHorizontalExtent(k);
            }

            
            {
                this$0 = DynamicScrollBar.this;
                super();
            }
        }
;
        reportPane = elementcasepane;
        dpi = j;
        setMinimum(0);
        setUnitIncrement(1);
        setBlockIncrement(3);
        addComponentListener(new ComponentListener() {

            final DynamicScrollBar this$0;

            public void componentResized(ComponentEvent componentevent)
            {
                ajustValues();
            }

            public void componentMoved(ComponentEvent componentevent)
            {
                ajustValues();
            }

            public void componentShown(ComponentEvent componentevent)
            {
                ajustValues();
            }

            public void componentHidden(ComponentEvent componentevent)
            {
                ajustValues();
            }

            private void ajustValues()
            {
                setValue(getValue());
            }

            
            {
                this$0 = DynamicScrollBar.this;
                super();
            }
        }
);
    }

    public ElementCasePane getReportPane()
    {
        return reportPane;
    }

    public void setReportPane(ElementCasePane elementcasepane)
    {
        if(elementcasepane == null)
            return;
        reportPane = elementcasepane;
        if(orientation == 1)
            verticalBarHelper.setValue(reportPane.getGrid().getVerticalValue());
        else
            horizontalBarHelper.setValue(reportPane.getGrid().getHorizontalValue());
    }

    public int getVisibleAmount()
    {
        if(reportPane == null)
            return 0;
        if(orientation == 1)
            return verticalBarHelper.getExtent(getValue());
        else
            return horizontalBarHelper.getExtent(getValue());
    }

    public void updateUI()
    {
        setUI(new DynamicScrollBarUI());
    }

    public void setValue(int i)
    {
        if(reportPane == null)
            return;
        if(orientation == 1)
            verticalBarHelper.setValue(i);
        else
            horizontalBarHelper.setValue(i);
    }

    public boolean isSupportHide()
    {
        return isSupportHide;
    }

    public void setSupportHide(boolean flag)
    {
        isSupportHide = flag;
    }

    public Dimension getPreferredSize()
    {
        if(isSupportHide)
            if(getOrientation() == 0)
            {
                if(reportPane != null && !reportPane.isHorizontalScrollBarVisible())
                    return new Dimension(0, 0);
            } else
            if(getOrientation() == 1 && reportPane != null && !reportPane.isVerticalScrollBarVisible())
                return new Dimension(0, 0);
        return super.getPreferredSize();
    }




}
