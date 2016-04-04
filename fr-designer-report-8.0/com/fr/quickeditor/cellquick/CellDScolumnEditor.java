// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.quickeditor.cellquick;

import com.fr.design.actions.columnrow.*;
import com.fr.design.dscolumn.ResultSetGroupDockingPane;
import com.fr.design.dscolumn.SelectedDataColumnPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.quickeditor.CellQuickEditor;
import com.fr.report.cell.TemplateCellElement;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class CellDScolumnEditor extends CellQuickEditor
{

    private JPanel dsColumnRegion;
    private JPanel centerPane;
    private SelectedDataColumnPane dataPane;
    private ResultSetGroupDockingPane groupPane;
    private ItemListener groupListener;
    private ItemListener dataListener;
    private static CellDScolumnEditor THIS;

    public static final CellDScolumnEditor getInstance()
    {
        if(THIS == null)
            THIS = new CellDScolumnEditor();
        return THIS;
    }

    private CellDScolumnEditor()
    {
        groupListener = new ItemListener() {

            final CellDScolumnEditor this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                if(itemevent == null)
                {
                    groupPane.update();
                    fireTargetModified();
                    return;
                }
                if(itemevent.getStateChange() == 2)
                {
                    if(!update)
                        return;
                    groupPane.update();
                    fireTargetModified();
                }
            }

            
            {
                this$0 = CellDScolumnEditor.this;
                super();
            }
        }
;
        dataListener = new ItemListener() {

            final CellDScolumnEditor this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                if(itemevent.getStateChange() == 1)
                {
                    if(!access$400)
                        return;
                    dataPane.update(getStateChange);
                    fireTargetModified();
                }
            }

            
            {
                this$0 = CellDScolumnEditor.this;
                super();
            }
        }
;
    }

    public JComponent createCenterBody()
    {
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d, d1
        };
        double ad1[] = {
            d, d, d, d
        };
        Component acomponent[][] = new Component[0][];
        dsColumnRegion = TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
        centerPane = new JPanel(new BorderLayout(0, 0));
        centerPane.add(dsColumnRegion, "Center");
        return centerPane;
    }

    protected void refreshDetails()
    {
        JPanel jpanel = new JPanel(new BorderLayout(4, 0));
        jpanel.add(new UIButton(new DSColumnConditionAction((ElementCasePane)tc)), "West");
        jpanel.add(new UIButton(new DSColumnAdvancedAction((ElementCasePane)tc)), "Center");
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d, d1
        };
        double ad1[] = {
            d, d, d, d
        };
        Component acomponent[][] = {
            {
                new UIButton(new DSColumnBasicAction((ElementCasePane)tc)), null
            }, {
                jpanel, null
            }, {
                dataPane = new SelectedDataColumnPane(false), null
            }, {
                groupPane = new ResultSetGroupDockingPane((ElementCasePane)tc), null
            }
        };
        centerPane.removeAll();
        dsColumnRegion = TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
        centerPane.add(dsColumnRegion, "Center");
        dataPane.addListener(dataListener);
        groupPane.addListener(groupListener);
        dataPane.populate(null, cellElement);
        groupPane.populate(cellElement);
        validate();
    }








}
