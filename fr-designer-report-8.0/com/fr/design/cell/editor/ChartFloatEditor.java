// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.cell.editor;

import com.fr.base.chart.BaseChartCollection;
import com.fr.design.dialog.DialogActionListener;
import com.fr.design.gui.chart.MiddleChartComponent;
import com.fr.design.gui.chart.MiddleChartDialog;
import com.fr.design.module.DesignModuleFactory;
import com.fr.grid.Grid;
import com.fr.report.cell.FloatElement;
import com.fr.stable.bridge.StableFactory;
import com.fr.stable.core.PropertyChangeAdapter;
import java.awt.Component;
import javax.swing.SwingUtilities;

// Referenced classes of package com.fr.design.cell.editor:
//            AbstractFloatEditor

public class ChartFloatEditor extends AbstractFloatEditor
    implements DialogActionListener
{

    private MiddleChartDialog chartFloatEditorDialog;
    private MiddleChartComponent glyphComponent;

    public ChartFloatEditor()
    {
        chartFloatEditorDialog = null;
        glyphComponent = null;
    }

    public Object getFloatEditorValue()
        throws Exception
    {
        if(glyphComponent != null)
        {
            MiddleChartComponent middlechartcomponent = glyphComponent;
            return middlechartcomponent.update();
        }
        BaseChartCollection basechartcollection = chartFloatEditorDialog.getChartCollection();
        if(basechartcollection != null)
            return basechartcollection;
        else
            return "";
    }

    public Component getFloatEditorComponent(final Grid grid, FloatElement floatelement, int i)
    {
        Object obj = floatelement.getValue();
        if(obj instanceof BaseChartCollection)
        {
            chartFloatEditorDialog = null;
            if(glyphComponent == null)
            {
                glyphComponent = DesignModuleFactory.getChartComponent((BaseChartCollection)obj);
                glyphComponent.addStopEditingListener(new PropertyChangeAdapter() {

                    final Grid val$grid;
                    final ChartFloatEditor this$0;

                    public void propertyChange()
                    {
                        stopFloatEditing();
                        grid.requestFocus();
                    }

            
            {
                this$0 = ChartFloatEditor.this;
                grid = grid1;
                super();
            }
                }
);
            } else
            {
                glyphComponent.populate((BaseChartCollection)obj);
            }
            return glyphComponent;
        } else
        {
            chartFloatEditorDialog = DesignModuleFactory.getChartDialog(SwingUtilities.getWindowAncestor(grid));
            chartFloatEditorDialog.addDialogActionListener(this);
            BaseChartCollection basechartcollection = (BaseChartCollection)StableFactory.createXmlObject("CC");
            chartFloatEditorDialog.populate(basechartcollection);
            glyphComponent = null;
            return chartFloatEditorDialog;
        }
    }

    public void doOk()
    {
        stopFloatEditing();
    }

    public void doCancel()
    {
        cancelFloatEditing();
    }
}
