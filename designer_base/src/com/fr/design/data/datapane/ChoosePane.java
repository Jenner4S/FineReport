package com.fr.design.data.datapane;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.base.TableData;
import com.fr.data.core.DataCoreUtils;
import com.fr.data.core.db.DBUtils;
import com.fr.data.core.db.TableProcedure;
import com.fr.data.core.db.dialect.DialectFactory;
import com.fr.data.impl.DBTableData;
import com.fr.dav.LocalEnv;
import com.fr.design.DesignerEnvManager;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.data.DesignTableDataManager;
import com.fr.design.data.datapane.RefreshLabel.Refreshable;
import com.fr.design.data.datapane.preview.PreviewLabel;
import com.fr.design.data.datapane.preview.PreviewLabel.Previewable;
import com.fr.design.data.datapane.preview.PreviewTablePane;
import com.fr.design.data.tabledata.Prepare4DataSourceChange;
import com.fr.design.gui.icombobox.*;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itree.refreshabletree.ExpandMutableTreeNode;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.file.DatasourceManager;
import com.fr.file.DatasourceManagerProvider;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

/**
 * @author zhou
 * @since 2012-7-11����4:49:39
 */
public class ChoosePane extends BasicBeanPane<DataBaseItems> implements Refreshable, Previewable, Prepare4DataSourceChange {
    private static final double COLUMN_SIZE = 24;

    /**
     * ���ݿ�
     */
    protected StringUIComboBox dsNameComboBox;

    /**
     * ģʽ
     */
    protected StringUIComboBox schemaBox;

    /**
     * ����
     */
    protected FRTreeComboBox tableNameComboBox;

    private SwingWorker populateWorker;


    private PopupMenuListener popupMenuListener = new PopupMenuListener() {

        public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
            new Thread() {
                @Override
                public void run() {
                    calculateTableDataNames();
                }
            }.start();
        }

        public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
        }

        public void popupMenuCanceled(PopupMenuEvent e) {
        }
    };


    private PopupMenuListener listener = new PopupMenuListener() {
        public void popupMenuCanceled(PopupMenuEvent e) {
        }

        public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
        }

        public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
            executePopulateWorker();
        }
    };

    public ChoosePane() {
        this(null);
    }

    public ChoosePane(Previewable parent) {
        this(parent, -1);
    }

    public ChoosePane(Previewable parent, int labelSize) {
        this.initBasicComponet();
        this.initComponentsLayout(new PreviewLabel(parent == null ? this : parent), labelSize);
    }

    private void initBasicComponet() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());

        dsNameComboBox = new StringUIComboBox();
        initDsNameComboBox();

        schemaBox = new StringUIComboBox();
        schemaBox.setEditor(new ComboBoxEditor());

        tableNameComboBox = new FRTreeComboBox(new JTree(new DefaultMutableTreeNode()), tableNameTreeRenderer, false);
        tableNameComboBox.setEditable(true);
        tableNameComboBox.setRenderer(listCellRenderer);
        registerDSChangeListener();
        initBoxListener();
    }

    private void initBoxListener() {
        addDSBoxListener();
        schemaBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent evt) {
                tableNameComboBox.setSelectedItem("");
            }
        });
        schemaBox.addPopupMenuListener(listener);
        addFocusListener();
        this.tableNameComboBox.addPopupMenuListener(popupMenuListener);
    }

    protected void addDSBoxListener() {
        dsNameComboBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                schemaBox.setSelectedIndex(-1);
                tableNameComboBox.setSelectedItem("");
                JTree tree = tableNameComboBox.getTree();
                if (tree == null) {
                    return;
                }
                DefaultMutableTreeNode rootTreeNode = (DefaultMutableTreeNode) tree.getModel().getRoot();
                rootTreeNode.removeAllChildren();
                rootTreeNode.add(new ExpandMutableTreeNode("Loading..."));
                ((DefaultTreeModel) tree.getModel()).reload();

            }
        });
    }

    protected void addFocusListener() {
        schemaBox.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (schemaBox.getSelectedIndex() == -1) {
                    schemaBox.updateUI();
                    schemaBox.showPopup();
                }
            }
        });
    }

    protected void initDsNameComboBox() {
        dsNameComboBox.setRefreshingModel(true);
        DatasourceManagerProvider datasourceManager = DatasourceManager.getProviderInstance();
        @SuppressWarnings("unchecked")
        Iterator<String> datasourceNameIterator = datasourceManager.getConnectionNameIterator();
        List<String> dsList = new ArrayList<String>();
        while (datasourceNameIterator.hasNext()) {
            dsList.add((String) datasourceNameIterator.next());
        }
        FilterableComboBoxModel dsNameComboBoxModel = new FilterableComboBoxModel(dsList);
        dsNameComboBox.setModel(dsNameComboBoxModel);
        dsNameComboBox.setRefreshingModel(false);
    }

    protected void initComponentsLayout(PreviewLabel previewLabel, int labelSize) {
        UILabel l1 = new UILabel(Inter.getLocText("FR-Designer_Database") + ":");
        UILabel l2 = new UILabel(Inter.getLocText("FR-Designer_Model") + ":");
        UILabel l3 = new UILabel(Inter.getLocText("FR-Designer_Table") + ":");

        if (labelSize > 0) {
            Dimension pSize = new Dimension(labelSize, 25);
            l1.setPreferredSize(pSize);
            l2.setPreferredSize(pSize);
            l3.setPreferredSize(pSize);
        }

        Component[][] coms = new Component[][]{{l1, dsNameComboBox, l2, schemaBox,
                l3, this.tableNameComboBox, new RefreshLabel(this), previewLabel}};

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        JPanel northDSPane = TableLayoutHelper.createTableLayoutPane(coms, new double[]{p}, new double[]{p, f, p, f, p, f, COLUMN_SIZE, COLUMN_SIZE});

        this.add(northDSPane, BorderLayout.CENTER);

    }

    @Override
    public void populateBean(final DataBaseItems ob) {
        this.dsNameComboBox.setSelectedItem(ob.getDatabaseName());
        schemaBox.setSelectedItem(ob.getSchemaName());
        this.tableNameComboBox.setSelectedItem(ob.getTableName());
    }


    private void executePopulateWorker() {
        if (populateWorker != null) {
            populateWorker.cancel(true);
        }
        populateWorker = new SwingWorker<com.fr.data.impl.Connection, Void>() {

            protected com.fr.data.impl.Connection doInBackground() throws Exception {
                schemaBox.addItem(Inter.getLocText("FR-Designer_Loading") + "...");
                schemaBox.setSelectedItem(null);
                return getConnection();
            }

            public void done() {
                try {
                    com.fr.data.impl.Connection selectedDatabase = get();
                    String selectedItem = schemaBox.getSelectedItem();
                    schemaBox.setRefreshingModel(true);
                    schemaBox.removeAllItems();
                    schemaBox.updateUI();
                    if (selectedDatabase == null) {
                        return;
                    }
                    String[] schema = DataCoreUtils.getDatabaseSchema(selectedDatabase);
                    schema = schema == null ? new String[]{""} : schema;
                    for (String aa : schema) {
                        schemaBox.addItem(aa);
                    }
                    int index = schemaBox.getSelectedIndex();
                    if (selectedItem != null) {
                        schemaBox.setSelectedItem(selectedItem);
                    } else if (index < schema.length) {
                        schemaBox.setSelectedIndex(index);
                    }
                } catch (Exception e) {
                    FRLogger.getLogger().error(e.getMessage());
                }
                schemaBox.setRefreshingModel(false);
                schemaBox.removePopupMenuListener(listener);
                schemaBox.setPopupVisible(true);
                schemaBox.addPopupMenuListener(listener);
            }
        };
        populateWorker.execute();
    }

    @Override
    public DataBaseItems updateBean() {
        return new DataBaseItems(this.getDSName(), this.schemaBox.getSelectedItem(), getTableName());
    }

    /**
     * ����ѡ�е�box
     */
    public void resetComponets() {
        GUICoreUtils.setSelectedItemQuietly(dsNameComboBox, -1);
        GUICoreUtils.setSelectedItemQuietly(schemaBox, -1);
        GUICoreUtils.setSelectedItemQuietly(tableNameComboBox, -1);
    }

    protected com.fr.data.impl.Connection getConnection() {
        String selectedDSName = this.getDSName();
        if (StringUtils.isEmpty(selectedDSName)) {
            return null; // peter:ѡ���˵�ǰ���㳤�ȵĽڵ�,ֱ�ӷ���.
        }
        DatasourceManagerProvider datasourceManager = DatasourceManager.getProviderInstance();
        @SuppressWarnings("unchecked")
        Iterator<String> datasourceNameIterator = datasourceManager.getConnectionNameIterator();
        while (datasourceNameIterator.hasNext()) {
            String datasourceName = datasourceNameIterator.next();
            if (ComparatorUtils.equals(selectedDSName, datasourceName)) {
                return datasourceManager.getConnection(datasourceName);
            }
        }
        return null;
    }

    /**
     * ˢ��û����á�����Ҫˢ��Ҳ�������ˢ�¡�
     */
    public void refresh() {
        DBUtils.refreshDatabase();
        String schema = StringUtils.isEmpty(schemaBox.getSelectedItem()) ? null : schemaBox.getSelectedItem();
        DataCoreUtils.refreshTables(getConnection(), TableProcedure.TABLE, schema);
        JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), Inter.getLocText("FR-Designer_Refresh_Successfully") + "!", Inter.getLocText("FR-Designer_Refresh_Database"),
                JOptionPane.INFORMATION_MESSAGE);
    }

    TreeCellRenderer tableNameTreeRenderer = new DefaultTreeCellRenderer() {
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            if (value instanceof DefaultMutableTreeNode) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                Object userObj = node.getUserObject();
                if (userObj instanceof String) {
                    this.setIcon(BaseUtils.readIcon("com/fr/design/images/m_insert/expandCell.gif"));
                } else if (userObj instanceof TableProcedure) {
                    this.setText(((TableProcedure) userObj).getName());
                }
            }
            return this;
        }
    };

    public static UIComboBoxRenderer listCellRenderer = new UIComboBoxRenderer() {
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof TreePath) {
                DefaultMutableTreeNode dmt = (DefaultMutableTreeNode) ((TreePath) value).getLastPathComponent();
                if (dmt.getUserObject() instanceof TableProcedure) {
                    this.setText(((TableProcedure) dmt.getUserObject()).getName());
                } else {
                    this.setText(null);
                }
            }
            return this;
        }
    };


    /**
     *  �����Ŀ�����¼�
     * @param aListener   �¼�������
     */
    public void addItemListener(ItemListener aListener) {
        this.tableNameComboBox.addItemListener(aListener);
    }

    /**
     *  ɾ����Ŀ�����¼�
     * @param aListener   �¼�������
     */
    public void removeItemListener(ItemListener aListener) {
        this.tableNameComboBox.removeItemListener(aListener);
    }

    @Override
    protected String title4PopupWindow() {
        return "choosepane";
    }

    protected void calculateTableDataNames() {
        JTree tree = tableNameComboBox.getTree();
        if (tree == null) {
            return;
        }
        DefaultMutableTreeNode rootTreeNode = (DefaultMutableTreeNode) tree.getModel().getRoot();
        rootTreeNode.removeAllChildren();

        String selectedDSName = this.getDSName();
        com.fr.data.impl.Connection selectedDatabase = this.getConnection();
        if (selectedDatabase == null) {
            return;
        }
        try {
            String schema = StringUtils.isEmpty(this.schemaBox.getSelectedItem()) ? null : this.schemaBox.getSelectedItem();
            TableProcedure[] sqlTableArray = DataCoreUtils.getTables(selectedDatabase, TableProcedure.TABLE, schema, DesignerEnvManager.getEnvManager().isOracleSystemSpace());
            if (sqlTableArray.length > 0) {
                ExpandMutableTreeNode tableTreeNode = new ExpandMutableTreeNode(selectedDSName + "-" + Inter.getLocText("FR-Designer_SQL-Table"));
                rootTreeNode.add(tableTreeNode);
                for (int i = 0; i < sqlTableArray.length; i++) {
                    ExpandMutableTreeNode tableChildTreeNode = new ExpandMutableTreeNode(sqlTableArray[i]);
                    tableTreeNode.add(tableChildTreeNode);
                }
            }
            TableProcedure[] sqlViewArray = DataCoreUtils.getTables(selectedDatabase, TableProcedure.VIEW, schema, DesignerEnvManager.getEnvManager().isOracleSystemSpace());
            if (sqlViewArray.length > 0) {
                ExpandMutableTreeNode viewTreeNode = new ExpandMutableTreeNode(selectedDSName + "-" + Inter.getLocText("FR-Designer_SQL-View"));
                rootTreeNode.add(viewTreeNode);
                for (int i = 0; i < sqlViewArray.length; i++) {
                    ExpandMutableTreeNode viewChildTreeNode = new ExpandMutableTreeNode(sqlViewArray[i]);
                    viewTreeNode.add(viewChildTreeNode);
                }
            }
            ((DefaultTreeModel) tree.getModel()).reload();
            /**
             * daniel չ������tree
             */
            TreeNode root = (TreeNode) tree.getModel().getRoot();
            TreePath parent = new TreePath(root);
            TreeNode node = (TreeNode) parent.getLastPathComponent();
            for (Enumeration e = node.children(); e.hasMoreElements(); ) {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                tree.expandPath(path);
            }
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage(), e);
        }
    }

    /**
     * ����ѡ�е����ݼ�����
     * @return    ���ݼ�����
     */
    public TableData createSelectTableData() {
        DataBaseItems paras = this.updateBean();
        boolean connect = false;
        com.fr.data.impl.Connection database = DBUtils.checkDBConnection(paras.getDatabaseName());
        if (database == null) {
			failedToFindTable();
            return TableData.EMPTY_TABLEDATA;
        }
        try {
            connect = FRContext.getCurrentEnv().testConnection(database);
        } catch (Exception e1) {
            connect = false;
        }
        if (!connect) {
            DesignerFrame designerFrame = DesignerContext.getDesignerFrame();
            JOptionPane.showMessageDialog(designerFrame, Inter.getLocText("Datasource-Connection_failed"),
					Inter.getLocText("FR-Designer_Failed"), JOptionPane.INFORMATION_MESSAGE);
			failedToFindTable();
            return null;
        }
        // ��ʾTable����.

        TableData tableData = null;
        if (FRContext.getCurrentEnv() instanceof LocalEnv) {
            TableData tableDataLocal = new DBTableData(database, DataCoreUtils.createSelectSQL(paras.getSchemaName(), paras.getTableName(),
                    DialectFactory.getDialectByName(paras.getDatabaseName())));
            tableData = tableDataLocal;
        } else {
            try {
                TableData tableDataLocal = new DBTableData(database, DataCoreUtils.createSelectSQL(paras.getSchemaName(), paras.getTableName()));
                tableData = FRContext.getCurrentEnv().previewTableData(tableDataLocal, java.util.Collections.EMPTY_MAP,
                        DesignerEnvManager.getEnvManager().getMaxNumberOrPreviewRow());
            } catch (Exception e) {
				failedToFindTable();
                FRContext.getLogger().error(e.getMessage(), e);
            }
        }

        return tableData;
    }

    protected String getDSName() {
        return this.dsNameComboBox.getSelectedItem();
    }

	protected void failedToFindTable() {
	}

    protected String getTableName() {
        String tableName = "";
        Object obj = this.tableNameComboBox.getSelectedItem();
        if (obj == null) {
            obj = this.tableNameComboBox.getEditor().getItem();
        }
        if (obj instanceof TreePath) {
            Object tp = ((ExpandMutableTreeNode) ((TreePath) obj).getLastPathComponent()).getUserObject();
            if (tp instanceof TableProcedure) {
                tableName = ((TableProcedure) tp).getName();
            }
        } else if (obj instanceof String) {
            tableName = (String) obj;
        }
        return tableName;
    }

    /**
     * �õ���ǰ��ColumnName[]
     * @return   ���ص�ǰ��ColumnName[]
     */
    public String[] currentColumnNames() {
        String[] colNames = null;

        DataBaseItems paras = this.updateBean();
        String selectedDSName = paras.getDatabaseName();
        if (StringUtils.isBlank(selectedDSName)) {
            return colNames = new String[0]; // peter:ѡ���˵�ǰ���㳤�ȵĽڵ�,ֱ�ӷ���.
        }

        String selectedTableObject = paras.getTableName();
        if (StringUtils.isEmpty(selectedTableObject)) {
            return colNames = new String[0];
        }
        java.sql.Connection conn = null;
        try {
            // daniel:���Ӳ���
            colNames = FRContext.getCurrentEnv().getColumns(selectedDSName, paras.getSchemaName(), selectedTableObject);
        } catch (Exception e2) {
            FRContext.getLogger().error(e2.getMessage(), e2);
        } finally {
            DBUtils.closeConnection(conn);
        }

        if (colNames == null) {
            colNames = new String[0];
        }
        return colNames;
    }

    /**
     *  Ԥ��key value��Ӧ������
     * @param key   ��
     * @param value    ֵ
     */
    public void preview(int key, int value) {
        PreviewTablePane.previewTableData(createSelectTableData(), key, value);
    }

    /**
     * Ĭ��Ԥ��
     */
    public void preview() {
        preview(-1, -1);
    }

    /**
     * �������ݼ���.
     */
    public void setTableNameComboBoxPopSize(int width, int height) {
        tableNameComboBox.setPopSize(width, height);
    }

    /**
     *ע��listener,��Ӧ���ݼ��ı�
     */
    public void registerDSChangeListener() {
        DesignTableDataManager.addDsChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                initDsNameComboBox();
            }
        });
    }


    protected class StringUIComboBox extends UIComboBox {
        private boolean refreshingModel = false;

        @Override
        protected void fireItemStateChanged(ItemEvent e) {
            if (!isRefreshingModel()) {
                super.fireItemStateChanged(e);
            }
        }

        @Override
        public String getSelectedItem() {
            Object ob = super.getSelectedItem();
            if (ob instanceof String) {
                return (String) ob;
            } else
                return StringUtils.EMPTY;
        }

        public boolean isRefreshingModel() {
            return refreshingModel;
        }

        public void setRefreshingModel(boolean refreshingModel) {
            this.refreshingModel = refreshingModel;
        }

        public void setSelectedItem(Object ob) {
            this.getModel().setSelectedItem(ob);
            if (ob != null && StringUtils.isEmpty(ob.toString())) {
                super.setSelectedIndex(-1);
            } else {
                super.setSelectedItem(ob);
            }
        }
    }

    private class ComboBoxEditor extends UIComboBoxEditor {
        private Object item;

        public void setItem(Object item) {
            this.item = item;
            textField.setText((item == null) ? "" : item.toString());
        }

        public Object getItem() {
            return this.item;
        }
    }

}
