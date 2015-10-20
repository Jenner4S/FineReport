package com.fr.design.gui.frpane;

import com.fr.base.Formula;
import com.fr.design.data.DesignTableDataManager;
import com.fr.design.data.datapane.EditOrNewLabel;
import com.fr.design.data.datapane.TableDataTreePane;
import com.fr.design.data.datapane.TreeTableDataComboBox;
import com.fr.design.data.datapane.preview.PreviewLabel;
import com.fr.data.impl.NameTableData;
import com.fr.data.impl.RecursionTableData;
import com.fr.data.impl.TableDataDictionary;
import com.fr.design.data.tabledata.wrapper.AbstractTableDataWrapper;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.data.tabledata.wrapper.TemplateTableDataWrapper;
import com.fr.design.DesignModelAdapter;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.dialog.BasicPane;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.editor.editor.*;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

public class JTreeAutoBuildPane extends BasicPane implements PreviewLabel.Previewable, EditOrNewLabel.Editable {
    private TreeTableDataComboBox treeTableDataComboBox;
    private ValueEditorPane valuePane;
    private ValueEditorPane textPane;
    private JPanel centerPane;
    private JPanel selectTreeDataPanel;

    public JTreeAutoBuildPane() {
        this.initComponent();
    }

    /**
     * ��ʼ��
     */
    public void initComponent() {
        this.setLayout(FRGUIPaneFactory.createM_BorderLayout());
        UILabel selectTreeDataLabel = new UILabel(Inter.getLocText("Select_A_Tree_DataSource_To_Build") + ": ");
        treeTableDataComboBox = new TreeTableDataComboBox(DesignTableDataManager.getEditingTableDataSource());
        treeTableDataComboBox.setPreferredSize(new Dimension(180, 20));
        selectTreeDataPanel = FRGUIPaneFactory.createBoxFlowInnerContainer_S_Pane();
        selectTreeDataPanel.add(selectTreeDataLabel);
        treeTableDataComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                tdChange();
            }
        });
        selectTreeDataPanel.add(treeTableDataComboBox);
        treeTableDataComboBox.setPreferredSize(new Dimension(200, 25));
        treeTableDataComboBox.setSelectedIndex(-1);
        PreviewLabel pl = new PreviewLabel(this);
        pl.setPreferredSize(new Dimension(25, 25));
        EditOrNewLabel enl = new EditOrNewLabel(this, this);
        enl.setPreferredSize(new Dimension(25, 25));
        selectTreeDataPanel.add(pl);
        selectTreeDataPanel.add(enl);

        this.add(selectTreeDataPanel, BorderLayout.NORTH);

        valuePane = ValueEditorPaneFactory.createValueEditorPane(new Editor[]{new ColumnNameEditor(), new ColumnIndexEditor()});
        FormulaEditor formulaEditor = new FormulaEditor(Inter.getLocText("Parameter-Formula"));
        formulaEditor.setEnabled(true);
        textPane = ValueEditorPaneFactory.createValueEditorPane(new Editor[]{new ColumnNameEditor(), new ColumnIndexEditor(), formulaEditor});
        Component[][] coms = {
                {new UILabel(Inter.getLocText("Actual_Value") + ":"),
                        valuePane},
                {new UILabel(Inter.getLocText("Display_Value") + ":"),
                        textPane}};

        double p = TableLayout.PREFERRED;

        double[] rowSize = {p, p, p};
        double[] columnSize = {p, p};
        centerPane = TableLayoutHelper.createTableLayoutPane(coms, rowSize,
                columnSize);

        this.add(centerPane, BorderLayout.CENTER);
        tdChange();
    }

    private void tdChange() {
        TableDataWrapper tableDataWrappe = this.treeTableDataComboBox.getSelectedItem();
        if (tableDataWrappe == null) {
            return;
        }
        try {
            List<String> namelist = tableDataWrappe.calculateColumnNameList();
            String[] columnNames = new String[namelist.size()];
            namelist.toArray(columnNames);
            valuePane.setEditors(new Editor[]{new ColumnNameEditor(columnNames), new ColumnIndexEditor(columnNames.length)}, columnNames[0]);
            FormulaEditor formulaEditor = new FormulaEditor(Inter.getLocText("Parameter-Formula"));
            formulaEditor.setEnabled(true);
            textPane.setEditors(new Editor[]{new ColumnNameEditor(columnNames), new ColumnIndexEditor(columnNames.length), formulaEditor}, columnNames[0]);
        } catch (Exception e) {
            valuePane.setEditors(new Editor[]{new OldColumnIndexEditor(100, Inter.getLocText("ColumnName"))}, 1);
            FormulaEditor formulaEditor = new FormulaEditor(Inter.getLocText("Parameter-Formula"));
            formulaEditor.setEnabled(true);
            textPane.setEditors(new Editor[]{new OldColumnIndexEditor(100, Inter.getLocText("ColumnName")), formulaEditor}, 1);
        }
    }

    public void populate(TableDataDictionary tableDataDict) {
        if (tableDataDict == null) {
            this.treeTableDataComboBox.setSelectedItem("");
            this.textPane.populate(1);
            this.valuePane.populate(1);
            return;
        } else {
            String _name = "";
            if (tableDataDict.getTableData() instanceof NameTableData) {
                _name = ((NameTableData) tableDataDict.getTableData()).getName();
            }
            this.treeTableDataComboBox.setSelectedTableDataByName(_name);
            tdChange();
            // alex:��Ϊ��ʾ�������ϵ�index����1Ϊʼ��,����Ҫ��1
            if (!StringUtils.isEmpty(tableDataDict.getKeyColumnName())) {
                this.valuePane.populate(tableDataDict.getKeyColumnName());
            } else {
                this.valuePane.populate(tableDataDict.getKeyColumnIndex() + 1);
            }
            Object value = null;
            if ((tableDataDict).getFormula() != null) {
                value = (tableDataDict).getFormula();
            } else {
                if (!StringUtils.isEmpty(tableDataDict.getValueColumnName())) {
                    value = tableDataDict.getValueColumnName();
                } else {
                    value = tableDataDict.getValueColumnIndex() + 1;
                }
            }

            this.textPane.populate(value);
        }
    }

    public TableDataDictionary update() {
        TableDataDictionary tableDataDict = new TableDataDictionary();
        Object object = this.valuePane.update(StringUtils.EMPTY);
        // alex:��Ϊ��ʾ�������ϵ�index����1Ϊʼ��,����Ҫ��1
        // carl:������������Ҫ�䣬�뿼��6.2�ļ���

        if (object instanceof Object[]) {
            Object[] temp = (Object[]) object;
            tableDataDict.setKeyColumnIndex(((Integer) temp[0]).intValue() - 1);
            tableDataDict.setKeyColumnName((String) temp[1]);
        } else if (object instanceof Integer) {
            tableDataDict.setKeyColumnIndex((Integer) object - 1);
        } else if (object instanceof String) {
            tableDataDict.setKeyColumnName((String) object);
        }
        Object object_text = this.textPane.update(StringUtils.EMPTY);
        if (object_text instanceof Object[]) {
            Object[] temp = (Object[]) object_text;
            if (temp[0] instanceof Formula) {
                tableDataDict.setFormula((Formula) temp[0]);
            } else {
                tableDataDict.setValueColumnIndex(((Integer) temp[0]).intValue() - 1);
                tableDataDict.setValueColumnName((String) temp[1]);
            }
        } else if (object_text instanceof Integer) {
            tableDataDict.setValueColumnIndex((Integer) this.textPane.update() - 1);
        } else if (object_text instanceof String) {
            tableDataDict.setValueColumnName((String) object_text);
        } else {
            tableDataDict.setFormula(((Formula) object));
        }

        TableDataWrapper tableDataWrappe = this.treeTableDataComboBox.getSelectedItem();
        if (tableDataWrappe != null) {
            tableDataDict.setTableData(new NameTableData(tableDataWrappe.getTableDataName()));
        }

        return tableDataDict;
    }

    @Override
    protected String title4PopupWindow() {
        return "Auto Build Tree";
    }

    /**
     * Ԥ��
     */
    public void preview() {
        TableDataWrapper tableDataWrappe = treeTableDataComboBox.getSelectedItem();
        if (tableDataWrappe == null) {
            return;
        }
        tableDataWrappe.previewData();
    }

    /**
     * �༭
     * @param jPanel ���
     */
    public void edit(JPanel jPanel) {
        RecursionTableData rtd = null;
        String name = "";
        TableDataTreePane tdtp = TableDataTreePane.getInstance(DesignModelAdapter.getCurrentModelAdapter());
        if (treeTableDataComboBox.getSelectedItem() == null) {
            //�½�
            rtd = new RecursionTableData();
            name = TableDataTreePane.createUnrepeatedName(tdtp.getDataTree(), "Tree");
        } else {
            //�༭
            rtd = treeTableDataComboBox.getSelcetedTableData();
            name = treeTableDataComboBox.getSelectedItem().getTableDataName();
        }
        AbstractTableDataWrapper atdw = new TemplateTableDataWrapper(rtd, "");
        tdtp.dgEdit(atdw.creatTableDataPane(), name);
        treeTableDataComboBox.refresh();
        treeTableDataComboBox.setSelectedTableDataByName(name);
        textPane.populate(1);
        valuePane.populate(1);
    }

}