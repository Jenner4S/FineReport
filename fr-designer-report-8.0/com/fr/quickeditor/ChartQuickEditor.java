// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.quickeditor;

import com.fr.base.chart.BaseChartCollection;
import com.fr.design.gui.chart.BaseChartPropertyPane;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.module.DesignModuleFactory;
import com.fr.design.selection.QuickEditor;
import com.fr.grid.selection.*;
import com.fr.poly.PolyDesigner;
import com.fr.poly.creator.BlockCreator;
import com.fr.poly.creator.ChartBlockEditor;
import com.fr.report.cell.Elem;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.report.poly.PolyChartBlock;
import java.awt.BorderLayout;

public class ChartQuickEditor extends QuickEditor
{

    private static ChartQuickEditor THIS;
    private BaseChartPropertyPane editingPropertyPane;

    public static final ChartQuickEditor getInstance()
    {
        if(THIS == null)
            THIS = new ChartQuickEditor();
        return THIS;
    }

    private ChartQuickEditor()
    {
        editingPropertyPane = null;
        setLayout(new BorderLayout());
        setBorder(null);
    }

    protected void refresh()
    {
        if(editingPropertyPane != null)
            remove(editingPropertyPane);
        BaseChartCollection basechartcollection = null;
        if(tc instanceof PolyDesigner)
        {
            ChartBlockEditor chartblockeditor = (ChartBlockEditor)((PolyDesigner)tc).getSelection().getEditor();
            basechartcollection = chartblockeditor.getValue().getChartCollection();
            add(editingPropertyPane = DesignModuleFactory.getChartPropertyPane(), "Center");
            editingPropertyPane.setSupportCellData(false);
        } else
        {
            Selection selection = ((ElementCasePane)tc).getSelection();
            Object obj = null;
            if(selection instanceof CellSelection)
            {
                CellSelection cellselection = (CellSelection)selection;
                obj = ((ElementCasePane)tc).getEditingElementCase().getCellElement(cellselection.getColumn(), cellselection.getRow());
            } else
            if(selection instanceof FloatSelection)
            {
                FloatSelection floatselection = (FloatSelection)selection;
                obj = ((ElementCasePane)tc).getEditingElementCase().getFloatElement(floatselection.getSelectedFloatName());
            }
            basechartcollection = (BaseChartCollection)((Elem) (obj)).getValue();
            add(editingPropertyPane = DesignModuleFactory.getChartPropertyPane(), "Center");
            editingPropertyPane.setSupportCellData(true);
        }
        editingPropertyPane.populateChartPropertyPane(basechartcollection, tc);
    }
}
