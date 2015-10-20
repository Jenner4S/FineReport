package com.fr.design.present.dict;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import com.fr.design.constants.UIConstants;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.ilable.UILabel;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fr.base.Formula;
import com.fr.base.TableData;
import com.fr.design.data.DesignTableDataManager;
import com.fr.data.TableDataSource;
import com.fr.design.data.datapane.TableDataComboBox;
import com.fr.design.data.datapane.TableDataTreePane;
import com.fr.data.impl.DBTableData;
import com.fr.data.impl.DynamicSQLDict;
import com.fr.data.impl.NameTableData;
import com.fr.data.impl.TableDataDictionary;
import com.fr.design.data.datapane.preview.PreviewLabel;
import com.fr.design.data.datapane.preview.PreviewLabel.Previewable;
import com.fr.design.data.tabledata.wrapper.TemplateTableDataWrapper;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.DesignModelAdapter;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.editor.editor.ColumnIndexEditor;
import com.fr.design.editor.editor.ColumnNameEditor;
import com.fr.design.editor.editor.Editor;
import com.fr.design.editor.editor.FormulaEditor;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import com.fr.stable.StringUtils;
import com.fr.design.utils.gui.GUICoreUtils;

/**
 * �����ֵ�����ݲ�ѯ���
 *
 * @editor zhou
 * @since 2012-3-29����1:49:24
 */
public class TableDataDictPane extends FurtherBasicBeanPane<TableDataDictionary> implements Previewable, UIObserver {
    private static final int BEGIN = 1;
    private static final int END = 10;
    private static final long serialVersionUID = -5469742115988153206L;
    private static final int SELECTED_NO_TABLEDATA = -2;
    public TableDataComboBox tableDataNameComboBox;
    private ValueEditorPane keyColumnPane;
    private ValueEditorPane valueDictPane;
    private ItemListener itemListener;
    private UIObserverListener uiObserverListener;

    public TableDataDictPane() {
        initBasicComponets();
        initComponents();
        iniListener();
    }

    private void initBasicComponets() {
        tableDataNameComboBox = new TableDataComboBox(DesignTableDataManager.getEditingTableDataSource());
        tableDataNameComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    tdChange(e);
                }
            }
        });
        keyColumnPane = ValueEditorPaneFactory.createValueEditorPane(new Editor[]{new ColumnNameEditor(), new ColumnIndexEditor()});
        FormulaEditor formulaEditor = new FormulaEditor(Inter.getLocText("Parameter-Formula"));
        formulaEditor.setEnabled(true);
        valueDictPane = ValueEditorPaneFactory.createValueEditorPane(new Editor[]{new ColumnNameEditor(), new ColumnIndexEditor(), formulaEditor});
    }

    private void initComponents() {

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f};
        double[] rowSize = {p, p, p};

        JPanel firstLine = new JPanel(new BorderLayout(4, 0));
        firstLine.add(tableDataNameComboBox, BorderLayout.CENTER);
        firstLine.add(new PreviewLabel(this), BorderLayout.EAST);


        Component[][] components = new Component[][]{
                new Component[]{new UILabel("  " + Inter.getLocText("DS-TableData") + ":", UILabel.RIGHT), firstLine},
                new Component[]{new UILabel(Inter.getLocText("Actual_Value") + ":", UILabel.RIGHT), keyColumnPane},
                new Component[]{new UILabel(Inter.getLocText("Display_Value") + ":", UILabel.RIGHT), valueDictPane}
        };

        JPanel panel = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        this.setLayout(new BorderLayout());
        this.add(panel, BorderLayout.CENTER);
    }


    private void iniListener() {
        if (shouldResponseChangeListener()) {
            this.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (uiObserverListener == null) {
                        return;
                    }
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        uiObserverListener.doChange();
                    }
                }
            });
        }
    }

    /**
     * ����Listener
     *
     * @param aListener ���������Listenerָ���listener
     */
    public void addItemListener(ItemListener aListener) {
        this.itemListener = aListener;
    }

    /**
     * ��������
     * @return �����Ǵ�����ʾ�ı���
     */
    public String title4PopupWindow() {
        return Inter.getLocText("Dic-Data_Query");
    }

    private void tdChange(final ItemEvent e) {
        TableDataWrapper tableDataWrappe = this.tableDataNameComboBox.getSelectedItem();
        if (tableDataWrappe == null) {
            return;
        }

        List<String> namelist = tableDataWrappe.calculateColumnNameList();
        String[] columnNames = null;
        if (!namelist.isEmpty()) {
            columnNames = namelist.toArray(ArrayUtils.EMPTY_STRING_ARRAY);
        } else {
            columnNames = new String[]{""};
        }
        ColumnNameEditor columnNameEditor1 = new ColumnNameEditor(columnNames);
        columnNameEditor1.addItemListener(itemListener);
        ColumnIndexEditor columnIndexEditor1 = new ColumnIndexEditor(columnNames.length);
        columnIndexEditor1.addItemListener(itemListener);
        keyColumnPane.setEditors(new Editor[]{columnNameEditor1, columnIndexEditor1}, columnNames[0]);

        FormulaEditor formulaEditor = new FormulaEditor(Inter.getLocText("Parameter-Formula"));
        formulaEditor.setEnabled(true);
        formulaEditor.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent ee) {
                if (itemListener != null) {
                    itemListener.itemStateChanged(e);
                }
            }
        });
        ColumnNameEditor columnNameEditor2 = new ColumnNameEditor(columnNames);
        columnNameEditor2.addItemListener(itemListener);
        ColumnIndexEditor columnIndexEditor2 = new ColumnIndexEditor(columnNames.length);
        columnIndexEditor2.addItemListener(itemListener);
        valueDictPane.setEditors(new Editor[]{columnNameEditor2, columnIndexEditor2, formulaEditor}, columnNames[0]);
        if (itemListener != null) {
            itemListener.itemStateChanged(e);
        }
    }

    @Override
    /**
     *
     */
    public void populateBean(TableDataDictionary tableDataDict) {
        populate(tableDataDict, "");
    }

    private void populate(TableDataDictionary tableDataDict, String name) {
        if (tableDataDict == null || tableDataDict.getTableData() == TableData.EMPTY_TABLEDATA) {
            this.tableDataNameComboBox.setSelectedIndex(-1);
            this.keyColumnPane.populate(StringUtils.EMPTY);
            this.valueDictPane.populate(StringUtils.EMPTY);
            return;
        }
        if (tableDataDict.getTableData() instanceof DBTableData && !"".equals(name)) {
            this.tableDataNameComboBox.putTableDataIntoMap(name, new TemplateTableDataWrapper(
                    (DBTableData) tableDataDict.getTableData(), name));
            this.tableDataNameComboBox.setSelectedTableDataByName(name);
        } else if (tableDataDict.getTableData() instanceof NameTableData) {
            this.tableDataNameComboBox.setSelectedTableDataByName(((NameTableData) tableDataDict.getTableData())
                    .getName());
        }
        // alex:��Ϊ��ʾ�������ϵ�index����1Ϊʼ��,����Ҫ��1

        TableDataWrapper tableDataWrappe = this.tableDataNameComboBox.getSelectedItem();
        if (tableDataWrappe == null) {
            keyColumnPane.resetComponets();
            valueDictPane.resetComponets();
        } else {
            if (StringUtils.isNotEmpty(tableDataDict.getKeyColumnName())) {
                this.keyColumnPane.populate(tableDataDict.getKeyColumnName());
            } else {
                this.keyColumnPane.populate(tableDataDict.getKeyColumnIndex() + 1);
            }

            Object value = null;
            if (tableDataDict.getFormula() != null) {
                value = tableDataDict.getFormula();
            } else {
                if (StringUtils.isNotEmpty(tableDataDict.getValueColumnName())) {
                    value = tableDataDict.getValueColumnName();
                } else {
                    value = tableDataDict.getValueColumnIndex() + 1;
                }
            }

            this.valueDictPane.populate(value);
        }
    }

    /**
     * @param dsql
     */
    public void populateBean(DynamicSQLDict dsql) {
        DBTableData db = new DBTableData(dsql.getDatabaseConnection(), dsql.getSqlFormula());
        String name = "";
        TableDataSource dataSource = DesignTableDataManager.getEditingTableDataSource();
        if (dataSource != null) {
            for (int i = BEGIN; i < END; i++) {
                TableData td = dataSource.getTableData(Inter.getLocText("Dictionary-Dynamic_SQL") + i);
                if (td == null) {
                    name = Inter.getLocText("Dictionary-Dynamic_SQL") + i;
                    dataSource.putTableData(Inter.getLocText("Dictionary-Dynamic_SQL") + i, db);
                    break;
                } else {
                    if (ComparatorUtils.equals(td, db)) {
                        name = Inter.getLocText("Dictionary-Dynamic_SQL") + i;
                        break;
                    } else {
                        continue;
                    }
                }
            }
        }

        TableDataDictionary tdd = new TableDataDictionary(db, dsql.getKeyColumnIndex(), dsql.getValueColumnIndex());
        if (dsql.getFormula() != null) {
            tdd.setFormula(dsql.getFormula());
        }
        this.populate(tdd, name);
        TableDataTreePane.getInstance(DesignModelAdapter.getCurrentModelAdapter());
    }

    /**
     * @return
     */
    public TableDataDictionary updateBean() {
        TableDataDictionary tableDataDict = new TableDataDictionary();
        Object object = this.valueDictPane.update();
        // alex:��Ϊ��ʾ�������ϵ�index����1Ϊʼ��,����Ҫ��1
        // carl:������������Ҫ�䣬�뿼��6.2�ļ���
        if (object instanceof Integer) {
            int valuleColumnIndex = (Integer) object - 1;
            String valueColumnName = StringUtils.EMPTY;

            if (this.valueDictPane.getCurrentEditor() instanceof ColumnNameEditor) {
                valueColumnName = ((ColumnNameEditor) this.valueDictPane.getCurrentEditor()).getColumnName();
                valuleColumnIndex = -1;
            }
            tableDataDict.setValueColumnIndex(valuleColumnIndex);
            tableDataDict.setValueColumnName(valueColumnName);
        } else {
            tableDataDict.setFormula(((Formula) object));
        }
        TableDataWrapper tableDataWrappe = this.tableDataNameComboBox.getSelectedItem();
        if (tableDataWrappe != null) {
            tableDataDict.setTableData(new NameTableData(tableDataWrappe.getTableDataName()));
            int keyColumnIndex = (Integer) this.keyColumnPane.update() - 1;
            String keyColumnName = StringUtils.EMPTY;

            if (keyColumnPane.getCurrentEditor() instanceof ColumnNameEditor) {
                keyColumnName = ((ColumnNameEditor) this.keyColumnPane.getCurrentEditor()).getColumnName();
                keyColumnIndex = -1;
            }

            tableDataDict.setKeyColumnIndex(keyColumnIndex);
            tableDataDict.setKeyColumnName(keyColumnName);
        }

        return tableDataDict;
    }

    /**
     * Ԥ��
     */
    public void preview() {
        TableDataWrapper tableDataWrappe = tableDataNameComboBox.getSelectedItem();
        if (tableDataWrappe == null) {
            return;
        }
        Object object = this.valueDictPane.update();
        if (object instanceof Integer) {
            tableDataWrappe.previewData((Integer) this.keyColumnPane.update() - 1, (Integer) object - 1);
        }

    }

    /**
     * �ж�ob�Ƿ���TableDataDictionary����
     * @param ob �����жϵ�Object
     * @return �����TableDataDictionary���ͣ��򷵻�true
     */
    public boolean accept(Object ob) {
        return ob instanceof TableDataDictionary;
    }

    @Override
    /**
     *����
     */
    public void reset() {
        GUICoreUtils.setSelectedItemQuietly(tableDataNameComboBox, UIConstants.PENDING);
        keyColumnPane.clearComponentsData();
        valueDictPane.clearComponentsData();
    }


    /**
     *������Ǽ�һ���۲��߼����¼�
     * @param listener �۲��߼����¼�
     */
    public void registerChangeListener(UIObserverListener listener) {
        uiObserverListener = listener;
    }

    /**
     * �Ƿ�Ӧ����Ӧlistener�¼�
     * @return Ҫ����Ӧlistener�¼����򷵻�true
     */
    public boolean shouldResponseChangeListener() {
        return true;
    }
}
