package com.fr.design.gui.ilist;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.data.core.DataCoreUtils;
import com.fr.data.core.db.TableProcedure;
import com.fr.data.core.db.dialect.DialectFactory;
import com.fr.data.core.db.dialect.OracleDialect;
import com.fr.data.impl.Connection;
import com.fr.design.DesignerEnvManager;
import com.fr.design.constants.UIConstants;
import com.fr.design.mainframe.dnd.SerializableTransferable;
import com.fr.file.DatasourceManager;
import com.fr.file.DatasourceManagerProvider;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.dnd.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.concurrent.CancellationException;

/**
 * �������ͼ���ߴ洢������ɵ�һ�������б�
 *
 * @author zhou
 * @since 2012-3-28����10:07:34
 */
public class TableViewList extends UIList {

    /**
     *
     */
    private static final long serialVersionUID = 2297780743855004708L;
    private SwingWorker refreshList;
    private Object object = null;


    public TableViewList() {
        super();
        this.setBackground(UIConstants.NORMAL_BACKGROUND);
        this.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        this.setCellRenderer(new TableListCellRenderer());
        new TableProcessorTreeDragSource(this, DnDConstants.ACTION_COPY);
        this.addFocusListener(new FocusAdapter() {

            @Override
            public void focusLost(FocusEvent e) {
                TableViewList.this.clearSelection();
            }
        });
        this.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                object = getSelectedValue();
            }

        });

    }

    /**
     * august��databaseName�����ݿ����֣�searchFilter������Ĺ�������,typesFilter����ͼ����
     * �洢�����е�һ�߻��߼���
     *
     * @param databaseName
     * @param searchFilter
     * @param typesFilter
     */
    public void populate(final String databaseName, final String searchFilter, final String... typesFilter) {
        DefaultListModel defaultListModel = new DefaultListModel();
        defaultListModel.addElement(UIConstants.PENDING);
        final DefaultListModel failed = new DefaultListModel();
        failed.addElement(UIConstants.CONNECTION_FAILED);
        this.setModel(defaultListModel);
        if (refreshList != null) {
            refreshList.cancel(true);
        }
        refreshList = new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                Connection datasource = DatasourceManager.getProviderInstance().getConnection(databaseName);
                boolean status = false;
                int count = 3;
                //�ܹ���3�����ӵĻ���
                while (!status && count > 0) {
                    status = FRContext.getCurrentEnv().testConnection(datasource);
                    count--;
                }
                if (!status) {
                    throw new Exception(Inter.getLocText("Datasource-Connection_failed"));
                }
                TableViewList.this.setModel(processDataInAnotherThread(databaseName, searchFilter, typesFilter));
                return null;
            }

            public void done() {
                try {
                    get();
                } catch (Exception e) {
                    if (!(e instanceof InterruptedException) && !(e instanceof CancellationException)) {
                        TableViewList.this.setModel(failed);
                        FRContext.getLogger().error(e.getMessage(), e);
                    }
                }
            }
        };
        if (databaseName != null) {
            refreshList.execute();
        }
    }

    /**
     * august��databaseName�����ݿ����֣�searchFilter������Ĺ�������,typesFilter����ͼ����
     * �洢�����е�һ�߻��߼���
     *
     * @param databaseName
     * @param searchFilter
     * @param typesFilter
     */
    private DefaultListModel processDataInAnotherThread(String databaseName, String searchFilter, String... typesFilter) throws Exception {
        DefaultListModel defaultListModel = new DefaultListModel();
        DatasourceManagerProvider datasourceManager = DatasourceManager.getProviderInstance();
        Connection datasource = datasourceManager.getConnection(databaseName);
        if (datasource == null) {
            return defaultListModel;
        }
        String[] schemas = DataCoreUtils.getDatabaseSchema(datasource);

        searchFilter = searchFilter.toLowerCase();


        boolean isOracle = FRContext.getCurrentEnv().isOracle(datasource);
        boolean isOracleSystemSpace = DesignerEnvManager.getEnvManager().isOracleSystemSpace();
        // oracleb����ѡ��ʾ���б���ֻ��ʾ�û��µ�(�����洢���̺�table��)
        if (isOracle && !isOracleSystemSpace) {
        	java.sql.Connection connection = datasource.createConnection();
        	OracleDialect orcDialect = (OracleDialect)DialectFactory.generateDialect(connection);
        	schemas = new String[]{orcDialect.getOracleCurrentUserSchema(connection)};
        }
        
        if (typesFilter.length == 1 && ComparatorUtils.equals(typesFilter[0], TableProcedure.PROCEDURE)) {
            return processStoreProcedure(defaultListModel, schemas, datasource, isOracle, searchFilter);
        } else {
            return processTableAndView(defaultListModel, schemas, datasource, searchFilter, isOracle, typesFilter);
        }
    }

    private DefaultListModel processStoreProcedure(DefaultListModel defaultListModel, String[] schemas, Connection datasource, boolean isOracle, String searchFilter) throws Exception {
        boolean isBlank = StringUtils.isBlank(searchFilter);
        @SuppressWarnings("unchecked")
        boolean isOracleSysSpace = DesignerEnvManager.getEnvManager().isOracleSystemSpace();
        List<TableProcedure[]> sqlTablees = DataCoreUtils.getProcedures(datasource, schemas, isOracle, isOracleSysSpace);
        for (TableProcedure[] sqlTables : sqlTablees) {
            if (sqlTables == null) {
                continue;
            }
            for (TableProcedure sqlTable : sqlTables) {
                String name = sqlTable.toString().toLowerCase();
                if (isBlank || name.indexOf(searchFilter) != -1) {
                    defaultListModel.addElement(sqlTable);
                }
            }
        }
        return defaultListModel;
    }

    private DefaultListModel processTableAndView(DefaultListModel defaultListModel, String[] schemas, Connection datasource, String searchFilter, boolean isOracle, String... typesFilter)
            throws Exception {
        boolean isBlank = StringUtils.isBlank(searchFilter);
        boolean isOracleSystemSpace = DesignerEnvManager.getEnvManager().isOracleSystemSpace();
        if (!isOracle) {
            String schema = null;
            for (String type : typesFilter) {
            	//��oracle���ݿ⣬Ĭ�϶�����ʾ���б�ģ�����Ϊtrue
                TableProcedure[] sqlTables = DataCoreUtils.getTables(datasource, type, schema, true);
                for (int i = 0; i < sqlTables.length; i++) {
                    if (isBlank || sqlTables[i].getName().toLowerCase().indexOf(searchFilter) != -1) {
                        defaultListModel.addElement(sqlTables[i]);
                    }
                }
            }
        } else {
            for (String type : typesFilter) {
            	for (String schema : schemas) {
        			TableProcedure[] sqlTables = DataCoreUtils.getTables(datasource, type, schema, isOracleSystemSpace);
        			// oracle�ı�������ģʽ
        			for (int i = 0; i < sqlTables.length; i++) {
        				TableProcedure ta = sqlTables[i];
        				String name = ta.getSchema() + '.' + ta.getName();
        				if (isBlank || name.toLowerCase().indexOf(searchFilter) != -1) {
        					defaultListModel.addElement(sqlTables[i]);
        				}
        			}
        		}
            }
        }
        return defaultListModel;
    }

    /**
     * ��ʾ��
     *
     * @editor zhou
     * @since 2012-3-28����10:11:58
     */
    private class TableListCellRenderer extends DefaultListCellRenderer {
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            TableProcedure tableProcedure = null;
            if (value instanceof TableProcedure) {
                tableProcedure = (TableProcedure) value;
                this.setText(tableProcedure.toString());
            } else if (value == UIConstants.PENDING) {
                this.setText(UIConstants.PENDING.toString());
            } else {
                return this;
            }
            String type = tableProcedure == null ? null : tableProcedure.getType();
            Icon icon = null;
            if (ComparatorUtils.equals(type, TableProcedure.TABLE)) {
                icon = BaseUtils.readIcon("/com/fr/design/images/data/tables.png");
            } else if (ComparatorUtils.equals(type, TableProcedure.VIEW)) {
                icon = BaseUtils.readIcon("/com/fr/design/images/data/views.png");
            } else {
                icon = BaseUtils.readIcon("/com/fr/design/images/data/store_procedure.png");
            }
            this.setIcon(icon);

            return this;
        }
    }

    /**
     * ��ק
     *
     * @editor zhou
     * @since 2012-3-28����10:11:36
     */
    private class TableProcessorTreeDragSource extends DragSourceAdapter implements DragGestureListener {
        private DragSource source;

        public TableProcessorTreeDragSource(JList jList, int actions) {
            source = new DragSource();
            source.createDefaultDragGestureRecognizer(jList, actions, this);
        }


        /**
         * Drag Gesture Handler
         */
        public void dragGestureRecognized(DragGestureEvent dge) {
            setSelectedValue(object, false);
            Component comp = dge.getComponent();
            if (!(comp instanceof TableViewList)) {
                return;
            }

            TableViewList list = (TableViewList) comp;
            TableProcedure tableProcedure = null;
            Object obj = list.getSelectedValue();
            if (obj instanceof TableProcedure) {
                tableProcedure = (TableProcedure) obj;
            }
            if (tableProcedure != null) {
                source.startDrag(dge, DragSource.DefaultLinkDrop, new SerializableTransferable(tableProcedure), this);
            }
        }
    }

}
