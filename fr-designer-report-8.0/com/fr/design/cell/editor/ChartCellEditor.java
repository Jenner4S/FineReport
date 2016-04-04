// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.cell.editor;

import com.fr.base.chart.BaseChartCollection;
import com.fr.design.dialog.DialogActionListener;
import com.fr.design.gui.chart.MiddleChartComponent;
import com.fr.design.gui.chart.MiddleChartDialog;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.module.DesignModuleFactory;
import com.fr.grid.Grid;
import com.fr.report.cell.TemplateCellElement;
import com.fr.stable.bridge.StableFactory;
import com.fr.stable.core.PropertyChangeAdapter;
import java.awt.Component;
import javax.swing.SwingUtilities;

// Referenced classes of package com.fr.design.cell.editor:
//            AbstractCellEditor

public class ChartCellEditor extends AbstractCellEditor
    implements DialogActionListener
{

    private MiddleChartDialog chartCellEditorDialog;
    private MiddleChartComponent glyphComponent;
    protected ElementCasePane ePane;

    public ChartCellEditor(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
        chartCellEditorDialog = null;
        glyphComponent = null;
    }

    public Object getCellEditorValue()
        throws Exception
    {
        if(glyphComponent != null)
        {
            MiddleChartComponent middlechartcomponent = glyphComponent;
            return middlechartcomponent.update();
        }
        BaseChartCollection basechartcollection = chartCellEditorDialog.getChartCollection();
        if(basechartcollection != null)
            return basechartcollection;
        else
            return "";
    }

    public Component getCellEditorComponent(final Grid grid, TemplateCellElement templatecellelement, int i)
    {
        Object obj = templatecellelement.getValue();
        if(obj instanceof BaseChartCollection)
        {
            if(glyphComponent == null)
            {
                glyphComponent = DesignModuleFactory.getChartComponent((BaseChartCollection)obj);
                glyphComponent.addStopEditingListener(new PropertyChangeAdapter() {

                    final Grid val$grid;
                    final ChartCellEditor this$0;

                    public void propertyChange()
                    {
                        stopCellEditing();
                        grid.requestFocus();
                    }

            
            {
                this$0 = ChartCellEditor.this;
                grid = grid1;
                super();
            }
                }
);
            } else
            {
                glyphComponent.populate((BaseChartCollection)obj);
            }
            chartCellEditorDialog = null;
            return glyphComponent;
        } else
        {
            chartCellEditorDialog = DesignModuleFactory.getChartDialog(SwingUtilities.getWindowAncestor(grid));
            chartCellEditorDialog.addDialogActionListener(this);
            BaseChartCollection basechartcollection = (BaseChartCollection)StableFactory.createXmlObject("CC");
            chartCellEditorDialog.populate(basechartcollection);
            glyphComponent = null;
            return chartCellEditorDialog;
        }
    }

    public void doOk()
    {
        stopCellEditing();
    }

    public void doCancel()
    {
        cancelCellEditing();
    }
}
