// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.form.ui.ChartBook;
import java.awt.Dimension;

// Referenced classes of package com.fr.design.mainframe:
//            BaseUndoState, JChart, ChartArea

public class ChartUndoState extends BaseUndoState
{

    private ChartBook chartBook;
    private Dimension designerSize;
    private double widthValue;
    private double heightValue;

    public ChartUndoState(JChart jchart, ChartArea chartarea)
    {
        super(jchart);
        try
        {
            chartBook = (ChartBook)((ChartBook)jchart.getTarget()).clone();
        }
        catch(CloneNotSupportedException clonenotsupportedexception)
        {
            throw new RuntimeException(clonenotsupportedexception);
        }
        widthValue = chartarea.getCustomWidth();
        heightValue = chartarea.getCustomHeight();
    }

    public ChartBook getChartBook()
    {
        return chartBook;
    }

    public void applyState()
    {
        ((JChart)getApplyTarget()).applyUndoState(this);
    }
}
