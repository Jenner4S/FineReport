// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.data.report;

import com.fr.base.Formula;
import com.fr.base.Utils;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.constants.UIConstants;
import com.fr.design.event.UIObserverListener;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.gui.frpane.UICorrelationPane;
import com.fr.design.gui.itable.UITable;
import com.fr.design.gui.itable.UITableEditor;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayout;
import com.fr.stable.StableUtils;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableModel;

public abstract class AbstractReportDataContentPane extends BasicBeanPane
{
    protected class InnerTableEditor extends UITableEditor
    {

        private TinyFormulaPane editorComponent;
        final AbstractReportDataContentPane this$0;

        public Object getCellEditorValue()
        {
            return editorComponent.getUITextField().getText();
        }

        public java.awt.Component getTableCellEditorComponent(JTable jtable, Object obj, boolean flag, int i, int j)
        {
            if(j == jtable.getModel().getColumnCount())
            {
                return null;
            } else
            {
                seriesPane.stopCellEditing();
                TinyFormulaPane tinyformulapane = getEditorComponent();
                tinyformulapane.getUITextField().setText(Utils.objectToString(obj));
                return tinyformulapane;
            }
        }

        private TinyFormulaPane getEditorComponent()
        {
            editorComponent = null;
            if(editorComponent == null)
            {
                editorComponent = new TinyFormulaPane() {

                    final InnerTableEditor this$1;

                    public void okEvent()
                    {
                        seriesPane.stopCellEditing();
                        seriesPane.fireTargetChanged();
                    }

                
                {
                    this$1 = InnerTableEditor.this;
                    super();
                }
                }
;
                editorComponent.setBackground(UIConstants.FLESH_BLUE);
                editorComponent.getUITextField().registerChangeListener(new UIObserverListener() {

                    final InnerTableEditor this$1;

                    public void doChange()
                    {
                        seriesPane.fireTargetChanged();
                    }

                
                {
                    this$1 = InnerTableEditor.this;
                    super();
                }
                }
);
            }
            return editorComponent;
        }

        protected InnerTableEditor()
        {
            this$0 = AbstractReportDataContentPane.this;
            super();
        }
    }


    private static final double ROW = 6D;
    protected UICorrelationPane seriesPane;

    public AbstractReportDataContentPane()
    {
    }

    protected abstract String[] columnNames();

    protected void initEveryPane()
    {
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d, 4D, d1
        };
        double ad1[] = {
            d, 6D, d, 6D, d, 6D, d
        };
        setLayout(new TableLayout(ad, ad1));
        initSeriesPane();
        add(seriesPane, "0,2,2,2");
    }

    protected void initSeriesPane()
    {
        seriesPane = new UICorrelationPane(columnNames()) {

            final AbstractReportDataContentPane this$0;

            public UITableEditor createUITableEditor()
            {
                return new InnerTableEditor();
            }

            protected UITable initUITable()
            {
                return new UITable(columnCount) {

                    final _cls1 this$1;

                    public UITableEditor createTableEditor()
                    {
                        return createUITableEditor();
                    }

                    public void tableCellEditingStopped(ChangeEvent changeevent)
                    {
                        stopPaneEditing(changeevent);
                    }

                    
                    {
                        this$1 = _cls1.this;
                        super(i);
                    }
                }
;
            }

            transient 
            {
                this$0 = AbstractReportDataContentPane.this;
                super(as);
            }
        }
;
    }

    public void checkBoxUse()
    {
    }

    public ChartCollection updateBean()
    {
        return null;
    }

    protected void populateList(List list)
    {
        seriesPane.populateBean(list);
    }

    protected List updateList()
    {
        return seriesPane.updateBean();
    }

    protected String title4PopupWindow()
    {
        return "";
    }

    protected HashMap createNameValue(List list)
    {
        HashMap hashmap = new HashMap();
        for(int i = 0; i < list.size(); i++)
        {
            Object aobj[] = (Object[])list.get(i);
            hashmap.put(aobj[0], aobj[1]);
        }

        return hashmap;
    }

    protected Object canBeFormula(Object obj)
    {
        if(obj == null)
            return null;
        else
            return StableUtils.canBeFormula(obj) ? new Formula(obj.toString()) : obj.toString();
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }
}
