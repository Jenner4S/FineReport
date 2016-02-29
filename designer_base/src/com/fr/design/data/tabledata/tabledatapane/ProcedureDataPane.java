package com.fr.design.data.tabledata.tabledatapane;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingWorker;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.base.StoreProcedureParameter;
import com.fr.data.core.db.TableProcedure;
import com.fr.data.impl.Connection;
import com.fr.data.impl.NameDatabaseConnection;
import com.fr.data.impl.storeproc.StoreProcedure;
import com.fr.design.actions.UpdateAction;
import com.fr.design.border.UIRoundedBorder;
import com.fr.design.constants.UIConstants;
import com.fr.design.data.DesignTableDataManager;
import com.fr.design.data.datapane.connect.ConnectionTableProcedurePane;
import com.fr.design.data.datapane.connect.ConnectionTableProcedurePane.DoubleClickSelectedNodeOnTreeListener;
import com.fr.design.data.datapane.sqlpane.SQLEditPane;
import com.fr.design.data.tabledata.ResponseDataSourceChange;
import com.fr.design.data.tabledata.StoreProcedureWorkerListener;
import com.fr.design.data.tabledata.wrapper.StoreProcedureDataWrapper;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itableeditorpane.UITableEditAction;
import com.fr.design.gui.itableeditorpane.UITableEditorPane;
import com.fr.design.gui.itoolbar.UIToolbar;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.menu.SeparatorDef;
import com.fr.design.menu.ToolBarDef;
import com.fr.file.DatasourceManager;
import com.fr.general.Inter;
import com.fr.script.Calculator;
import com.fr.stable.ArrayUtils;
import com.fr.stable.StringUtils;

public class ProcedureDataPane extends AbstractTableDataPane<StoreProcedure> implements ResponseDataSourceChange {
    private static final String[] DRIVERS = {
            "oracle.jdbc.driver.OracleDriver",
            "com.microsoft.sqlserver.jdbc.SQLServerDriver",
            "com.ibm.db2.jcc.DB2Driver",
            "com.mysql.jdbc.Driver",
            "org.gjt.mm.mysql.Driver"
    }; // 需要隐藏面板的数据库的驱动
    private static final String PREVIEW_BUTTON = Inter.getLocText("FR-Designer_Preview");
    private ConnectionTableProcedurePane connectionTableProcedurePane;
    private JPanel cardpane;
    private CardLayout cardLayout;
    private String editorPaneType;
    private int paneIndex;
    private UITableEditorPane<StoreProcedureParameter> editorPane;
    private UITableEditorPane<StoreProcedureParameter> inAutoeditorPane;
    private UITableEditorPane<StoreProcedureParameter> autoEditorPane;
    private StoreProcedure storeprocedure;
    // 存储过程的内容
    private SQLEditPane storeProcedureContext;
    // 存储过程的警告提示
    private UILabel warningLabel;
    // 存储过程显示的名字
    private UILabel queryText;
    private UICheckBox isShareCheckBox;
    private MaxMemRowCountPanel maxPanel;
    private SwingWorker populateWorker;
    private StoreProcedureWorkerListener storeProcedureWorkerListener;
    private SwingWorker updateWorker;

    public ProcedureDataPane() {
        super();
        queryText = new UILabel(StringUtils.EMPTY);
        Box box = new Box(BoxLayout.Y_AXIS);
        JPanel northpane = new JPanel(new BorderLayout(4, 4));
        northpane.add(creatToolBar(), BorderLayout.CENTER);
        northpane.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));

        storeProcedureContext = new SQLEditPane(SQLEditPane.UNREQUEST_DROPTARGET);
        storeProcedureContext.setEditable(false);
        UIScrollPane storeProcedureContextPane = new UIScrollPane(storeProcedureContext);
        storeProcedureContextPane.setBorder(new UIRoundedBorder(UIConstants.LINE_COLOR, 1, UIConstants.ARC));
        storeProcedureContextPane.setPreferredSize(new Dimension(680, 600));

        JPanel namePane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        namePane.add(new UILabel(Inter.getLocText("FR-Designer_Datasource-Stored_Procedure") + ":"));
        namePane.add(queryText);
        inAutoeditorPane = new UITableEditorPane<StoreProcedureParameter>(new StoreProcedureTableModel());
        autoEditorPane = new UITableEditorPane<StoreProcedureParameter>(new AutoStoreProcedureTableModel());
        autoEditorPane.getbuttonPane().setVisible(false);

        cardLayout = new CardLayout();
        cardpane = FRGUIPaneFactory.createCardLayout_S_Pane();
        cardpane.setLayout(this.cardLayout);
        cardpane.add(inAutoeditorPane, "inAutoeditorPane");
        cardpane.add(autoEditorPane, "autoEditorPane");

        box.add(northpane);
        box.add(storeProcedureContextPane);
        box.add(namePane);
        box.add(cardpane);
        warningLabel = new UILabel("");
        JPanel sqlSplitPane = new JPanel(new BorderLayout(4, 4));
        sqlSplitPane.add(box, BorderLayout.CENTER);

        // 左边的Panel,上面是选择DatabaseConnection的ComboBox,下面DatabaseConnection对应的Table
        initconnectionTableProcedurePane();
        this.setLayout(new BorderLayout(4, 4));
        this.add(connectionTableProcedurePane, BorderLayout.WEST);
        this.add(sqlSplitPane, BorderLayout.CENTER);
    }

    private void initconnectionTableProcedurePane() {
        connectionTableProcedurePane = new ConnectionTableProcedurePane() {
            @Override
            protected JPanel createCheckBoxgroupPane() {
                return null;
            }
        };
        connectionTableProcedurePane.addDoubleClickListener(new DoubleClickSelectedNodeOnTreeListener() {

            @Override
            public void actionPerformed(TableProcedure target) {
                editorPane.stopEditing();
                queryText.setText(target.toString());
                refresh();
            }

        });
        connectionTableProcedurePane.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                editorPaneType = isAutoParameterDatabase() ? "autoEditorPane" : "inAutoeditorPane";
                paneIndex = isAutoParameterDatabase() ? 1 : 0;
                editorPane = (UITableEditorPane) cardpane.getComponent(paneIndex);
                cardLayout.show(cardpane, editorPaneType);
            }
        });

    }

    private JToolBar creatToolBar() {
        ToolBarDef toolBarDef = new ToolBarDef();
        toolBarDef.addShortCut(new PreviewAction());
        toolBarDef.addShortCut(new RefreshAction());
        toolBarDef.addShortCut(SeparatorDef.DEFAULT);
        isShareCheckBox = new UICheckBox(Inter.getLocText("FR-Designer_Is_Share_DBTableData"));
        maxPanel = new MaxMemRowCountPanel();
        maxPanel.setBorder(null);
        UIToolbar toolbar = ToolBarDef.createJToolBar();
        toolBarDef.updateToolBar(toolbar);
        toolbar.add(isShareCheckBox);
        toolbar.add(maxPanel);
        return toolbar;
    }

    private boolean isAutoParameterDatabase() {
        Connection connection = DatasourceManager.getProviderInstance().getConnection(connectionTableProcedurePane.getSelectedDatabaseConnnectonName());
        return connection == null ? false : ArrayUtils.contains(DRIVERS, connection.getDriver());
    }

    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("FR-Designer_Datasource-Stored_Procedure");
    }

    @Override
    public void populateBean(StoreProcedure e) {
        storeprocedure = e;
        this.queryText.setText(e.getQuery());
        this.connectionTableProcedurePane.setSelectedDatabaseConnection(e.getDatabaseConnection());
        isShareCheckBox.setSelected(e.isShare());
        maxPanel.setValue(e.getMaxMemRowCount());

        // refresh storeProcedureContext
        if (populateWorker != null) {
            populateWorker.cancel(true);
        }
        populateWorker = new SwingWorker<Void, Void>() {

            protected Void doInBackground() throws Exception {
                try {
                    storeProcedureContext.setText(StringUtils.EMPTY);
                    String connectionname = connectionTableProcedurePane.getSelectedDatabaseConnnectonName();
                    String procedureText = FRContext.getCurrentEnv().getProcedureText(connectionname, storeprocedure.getQuery());
                    storeProcedureContext.setText(procedureText);
                    warningLabel.setText(StringUtils.EMPTY);
                } catch (Exception ex) {
                    if (ex instanceof SQLException) {
                        warningLabel.setText(Inter.getLocText(new String[]{"Database", "Datasource-Connection_failed"}));
                    } else {
                        warningLabel.setText(ex.getMessage());
                    }
                }
                return null;
            }

            public void done() {
                editorPane.populate(storeprocedure.getParameters());
            }
        };

        populateWorker.execute();

    }

    /**
     * 增加存储过程监听器
     *
     * @param listener 监听器
     */
    public void addStoreProcedureWorkerListener(StoreProcedureWorkerListener listener) {

        this.storeProcedureWorkerListener = listener;

    }

    /**
     * 去除存储过程监听器
     */
    public void removeStoreProcedureWorkerListener() {
        this.storeProcedureWorkerListener = null;

    }
    
    private StoreProcedure updateBeanWithOutExecute() {
        String dbName = connectionTableProcedurePane.getSelectedDatabaseConnnectonName();

        if (StringUtils.isBlank(dbName)) {
            try {
                throw new Exception(Inter.getLocText("FR-Designer_Connect_SQL_Cannot_Null") + ".");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(ProcedureDataPane.this, Inter.getLocText("FR-Designer_Connect_SQL_Cannot_Null") + ".");
            }
        }

        StoreProcedure sp = new StoreProcedure();
        sp.setDatabaseConnection(new NameDatabaseConnection(dbName));
        List<StoreProcedureParameter> parametersList = editorPane.update();
        sp.setParameters(parametersList.toArray(new StoreProcedureParameter[parametersList.size()]));

        sp.setQuery(this.queryText.getText());
        sp.setShare(isShareCheckBox.isSelected());
        sp.setMaxMemRowCount(maxPanel.getValue());

        return sp;
    }
    
    @Override
        public StoreProcedure updateBean() {
     
            final StoreProcedure sp = updateBeanWithOutExecute();
            if (updateWorker != null) {
                updateWorker.cancel(true);
            }
            updateWorker = new SwingWorker<Void, Void>() {
    
                protected Void doInBackground() throws Exception {
                    DesignTableDataManager.setThreadLocal(DesignTableDataManager.NO_PARAMETER);
                    sp.setCalculating(true);
                    sp.creatDataModel(Calculator.createCalculator());
                    return null;
                }
    
                public void done() {
                    DesignTableDataManager.setThreadLocal(DesignTableDataManager.NO_PARAMETER);
                    sp.setCalculating(false);
                    doAfterProcudureDone();
                    fireDSChanged();
                }
            };
    
            updateWorker.execute();
            return sp;
        }

    private void doAfterProcudureDone() {
        if (storeProcedureWorkerListener != null) {
            storeProcedureWorkerListener.fireDoneAction();
        }
    }

    // 刷新参数,从数据库取值
    private void refresh() {
        String text = this.queryText.getText();
        if (text == null) {
            text = StringUtils.EMPTY;
        }
        text = text.trim();
        String[] tableName = text.split("\\.");
        String connectionname = this.connectionTableProcedurePane.getSelectedDatabaseConnnectonName();
        try {
            String procedureText = FRContext.getCurrentEnv().getProcedureText(this.connectionTableProcedurePane.getSelectedDatabaseConnnectonName(), text);

            // 获取参数默认值，例如：NAME in varchar2 default 'SCOTT'，默认值为SCOTT
            String parameterDefaultValue = "";
            if (StringUtils.isNotEmpty(procedureText)) {
            	int index_begin = procedureText.indexOf("BEGIN");
            	
            	//from sam: 默认值只会在begin之前声明, 不然会把所有的存储过程里带'xx'的都作为默认值
            	String defaulValueStr = index_begin == -1 ? procedureText : procedureText.substring(0, index_begin);
            	String[] strs = defaulValueStr.split("\'");
                parameterDefaultValue = strs.length > 1 ? strs[1] : parameterDefaultValue;
            }

            StoreProcedureParameter[] newparameters;
            newparameters = FRContext.getCurrentEnv().getStoreProcedureDeclarationParameters(connectionname, tableName[tableName.length - 1], parameterDefaultValue);


            editorPane.populate(newparameters);
            storeProcedureContext.setText(procedureText);
            warningLabel.setText("");
        } catch (SQLException sql) {
            warningLabel.setText(Inter.getLocText(new String[]{"Database", "Datasource-Connection_failed"}));
            storeProcedureContext.setText("");
            editorPane.populate(new StoreProcedureParameter[0]);
        } catch (Exception e) {
            warningLabel.setText(e.getMessage());
            storeProcedureContext.setText("");
            editorPane.populate(new StoreProcedureParameter[0]);
        }
        return;
    }

    /**
     * 响应数据集改变
     */
    public void fireDSChanged() {
        fireDSChanged(new HashMap<String, String>());
    }

    /**
     * 响应数据集改变
     *
     * @param map 改变的map
     */
    public void fireDSChanged(Map<String, String> map) {
        DesignTableDataManager.fireDSChanged(map);
    }

    private class PreviewAction extends UpdateAction {
        public PreviewAction() {
            this.setName(PREVIEW_BUTTON);
            this.setMnemonic('P');
            this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_file/preview.png"));
        }

        public void actionPerformed(ActionEvent evt) {
        	StoreProcedure sp = updateBeanWithOutExecute();
            StoreProcedureDataWrapper storeProcedureDataWrappe = new StoreProcedureDataWrapper(sp, StringUtils.EMPTY, queryText.getText());
            storeProcedureDataWrappe.previewData(StoreProcedureDataWrapper.PREVIEW_ALL);
        }
    }

    protected class RefreshAction extends UITableEditAction {
        public RefreshAction() {
            this.setName(Inter.getLocText("FR-Designer_Refresh"));
            this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/control/refresh.png"));
        }

        public void actionPerformed(ActionEvent e) {
            refresh();
        }

        @Override
        public void checkEnabled() {
        }
    }
}