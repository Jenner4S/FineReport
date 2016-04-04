// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.gui.xpane;

import com.fr.base.Formula;
import com.fr.data.impl.NameTableData;
import com.fr.data.impl.RecursionTableData;
import com.fr.data.impl.TableDataDictionary;
import com.fr.design.DesignModelAdapter;
import com.fr.design.data.DesignTableDataManager;
import com.fr.design.data.datapane.EditOrNewLabel;
import com.fr.design.data.datapane.TableDataTreePane;
import com.fr.design.data.datapane.TreeTableDataComboBox;
import com.fr.design.data.datapane.preview.PreviewLabel;
import com.fr.design.data.tabledata.wrapper.AbstractTableDataWrapper;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.data.tabledata.wrapper.TemplateTableDataWrapper;
import com.fr.design.dialog.BasicPane;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.editor.editor.Editor;
import com.fr.design.editor.editor.FormulaEditor;
import com.fr.design.editor.editor.OldColumnIndexEditor;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JPanel;

public class JTreeAutoBuildPane extends BasicPane
    implements com.fr.design.data.datapane.preview.PreviewLabel.Previewable, com.fr.design.data.datapane.EditOrNewLabel.Editable
{

    private TreeTableDataComboBox treeTableDataComboBox;
    private ValueEditorPane valuePane;
    private ValueEditorPane textPane;
    private JPanel centerPane;
    private JPanel selectTreeDataPanel;

    public JTreeAutoBuildPane()
    {
        initComponent();
    }

    public void initComponent()
    {
        setLayout(FRGUIPaneFactory.createM_BorderLayout());
        UILabel uilabel = new UILabel((new StringBuilder()).append(Inter.getLocText("Select_A_Tree_DataSource_To_Build")).append(": ").toString());
        treeTableDataComboBox = new TreeTableDataComboBox(DesignTableDataManager.getEditingTableDataSource());
        treeTableDataComboBox.setPreferredSize(new Dimension(180, 20));
        selectTreeDataPanel = FRGUIPaneFactory.createBoxFlowInnerContainer_S_Pane();
        selectTreeDataPanel.add(uilabel);
        treeTableDataComboBox.addItemListener(new ItemListener() {

            final JTreeAutoBuildPane this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                tdChange();
            }

            
            {
                this$0 = JTreeAutoBuildPane.this;
                super();
            }
        }
);
        selectTreeDataPanel.add(treeTableDataComboBox);
        treeTableDataComboBox.setPreferredSize(new Dimension(200, 25));
        PreviewLabel previewlabel = new PreviewLabel(this);
        previewlabel.setPreferredSize(new Dimension(25, 25));
        EditOrNewLabel editornewlabel = new EditOrNewLabel(this, this);
        editornewlabel.setPreferredSize(new Dimension(25, 25));
        selectTreeDataPanel.add(previewlabel);
        selectTreeDataPanel.add(editornewlabel);
        add(selectTreeDataPanel, "North");
        valuePane = ValueEditorPaneFactory.createValueEditorPane(new Editor[] {
            new OldColumnIndexEditor(Inter.getLocText("Columns"))
        });
        FormulaEditor formulaeditor = new FormulaEditor(Inter.getLocText("Parameter-Formula"));
        formulaeditor.setEnabled(true);
        textPane = ValueEditorPaneFactory.createValueEditorPane(new Editor[] {
            new OldColumnIndexEditor(Inter.getLocText("Columns")), formulaeditor
        });
        Component acomponent[][] = {
            {
                new UILabel((new StringBuilder()).append(Inter.getLocText("Actual_Value")).append(":").toString()), valuePane
            }, {
                new UILabel((new StringBuilder()).append(Inter.getLocText("Display_Value")).append(":").toString()), textPane
            }
        };
        double d = -2D;
        double ad[] = {
            d, d, d
        };
        double ad1[] = {
            d, d
        };
        centerPane = TableLayoutHelper.createTableLayoutPane(acomponent, ad, ad1);
        add(centerPane, "Center");
        tdChange();
    }

    private void tdChange()
    {
        TableDataWrapper tabledatawrapper = treeTableDataComboBox.getSelectedItem();
        if(tabledatawrapper == null)
            return;
        try
        {
            java.util.List list = tabledatawrapper.calculateColumnNameList();
            String as[] = new String[list.size()];
            list.toArray(as);
            valuePane.setEditors(new Editor[] {
                new OldColumnIndexEditor(as, Inter.getLocText("ColumnName"))
            }, Integer.valueOf(1));
            FormulaEditor formulaeditor1 = new FormulaEditor(Inter.getLocText("Parameter-Formula"));
            formulaeditor1.setEnabled(true);
            textPane.setEditors(new Editor[] {
                new OldColumnIndexEditor(as, Inter.getLocText("ColumnName")), formulaeditor1
            }, Integer.valueOf(1));
        }
        catch(Exception exception)
        {
            valuePane.setEditors(new Editor[] {
                new OldColumnIndexEditor(Integer.valueOf(100), Inter.getLocText("ColumnName"))
            }, Integer.valueOf(1));
            FormulaEditor formulaeditor = new FormulaEditor(Inter.getLocText("Parameter-Formula"));
            formulaeditor.setEnabled(true);
            textPane.setEditors(new Editor[] {
                new OldColumnIndexEditor(Integer.valueOf(100), Inter.getLocText("ColumnName")), formulaeditor
            }, Integer.valueOf(1));
        }
    }

    public void populate(TableDataDictionary tabledatadictionary)
    {
        if(tabledatadictionary == null)
        {
            treeTableDataComboBox.setSelectedItem("");
            textPane.populate(Integer.valueOf(1));
            valuePane.populate(Integer.valueOf(1));
            return;
        }
        String s = "";
        if(tabledatadictionary.getTableData() instanceof NameTableData)
            s = ((NameTableData)tabledatadictionary.getTableData()).getName();
        treeTableDataComboBox.setSelectedTableDataByName(s);
        tdChange();
        textPane.populate(Integer.valueOf(tabledatadictionary.getKeyColumnIndex() + 1));
        Object obj = null;
        if(tabledatadictionary.getFormula() != null)
            obj = tabledatadictionary.getFormula();
        else
            obj = Integer.valueOf(tabledatadictionary.getValueColumnIndex() + 1);
        valuePane.populate(obj);
    }

    public TableDataDictionary update()
    {
        TableDataDictionary tabledatadictionary = new TableDataDictionary();
        Object obj = valuePane.update();
        if(obj instanceof Integer)
            tabledatadictionary.setValueColumnIndex(((Integer)obj).intValue() - 1);
        else
            tabledatadictionary.setFormula((Formula)obj);
        TableDataWrapper tabledatawrapper = treeTableDataComboBox.getSelectedItem();
        if(tabledatawrapper != null)
        {
            tabledatadictionary.setTableData(new NameTableData(tabledatawrapper.getTableDataName()));
            tabledatadictionary.setKeyColumnIndex(((Integer)textPane.update()).intValue() - 1);
        }
        return tabledatadictionary;
    }

    protected String title4PopupWindow()
    {
        return "Auto Build Tree";
    }

    public void preview()
    {
        TableDataWrapper tabledatawrapper = treeTableDataComboBox.getSelectedItem();
        if(tabledatawrapper == null)
        {
            return;
        } else
        {
            tabledatawrapper.previewData();
            return;
        }
    }

    public void edit(JPanel jpanel)
    {
        RecursionTableData recursiontabledata = null;
        String s = "";
        TableDataTreePane tabledatatreepane = TableDataTreePane.getInstance(DesignModelAdapter.getCurrentModelAdapter());
        if(treeTableDataComboBox.getSelectedItem() == null)
        {
            recursiontabledata = new RecursionTableData();
            s = TableDataTreePane.createUnrepeatedName(tabledatatreepane.getDataTree(), "Tree");
        } else
        {
            recursiontabledata = treeTableDataComboBox.getSelcetedTableData();
            s = treeTableDataComboBox.getSelectedItem().getTableDataName();
        }
        TemplateTableDataWrapper templatetabledatawrapper = new TemplateTableDataWrapper(recursiontabledata, "");
        tabledatatreepane.dgEdit(templatetabledatawrapper.creatTableDataPane(), s);
        treeTableDataComboBox.refresh();
        treeTableDataComboBox.setSelectedTableDataByName(s);
        textPane.populate(Integer.valueOf(1));
        valuePane.populate(Integer.valueOf(1));
    }

}
