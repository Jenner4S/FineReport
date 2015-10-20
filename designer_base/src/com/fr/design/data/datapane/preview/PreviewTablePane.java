/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.data.datapane.preview;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.base.TableData;
import com.fr.design.data.DesignTableDataManager;
import com.fr.data.impl.DBTableData;
import com.fr.data.impl.EmbeddedTableData;
import com.fr.data.impl.MultiTDTableData;
import com.fr.data.impl.storeproc.ProcedureDataModel;
import com.fr.design.DesignerEnvManager;
import com.fr.design.gui.frpane.UITabbedPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UINumberField;
import com.fr.design.gui.iprogressbar.AutoProgressBar;
import com.fr.design.gui.itable.SortableJTable;
import com.fr.design.gui.itable.TableSorter;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.BasicPane;
import com.fr.function.TIME;
import com.fr.general.FRFont;
import com.fr.general.Inter;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CancellationException;

/**
 * august�� PreviewTablePaneһ���ṩ5�����еľ�̬����������Ԥ����
 */
public class PreviewTablePane extends BasicPane {
    private TableData tableData;
    private ProcedureDataModel storeProcedureDataModel;
    private static UINumberField maxPreviewNumberField;
    private UINumberField currentRowsField;
    private JTable preveiwTable;
    private static AutoProgressBar progressBar;
    private AutoProgressBar connectionBar;
    private java.util.List<LoadedEventListener> listeners = new ArrayList<LoadedEventListener>();
    private BasicDialog dialog;
    private SwingWorker worker;

    private UILabel refreshLabel;
    private static PreviewTablePane THIS;
    private EmbeddedTableData previewTableData;

    public static final PreviewTablePane getInstance() {
        if (THIS == null) {
            THIS = new PreviewTablePane();
        }
        return THIS;
    }

    private PreviewTablePane() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());

        // elalke:Ԥ������
        JPanel previewNumberPanel = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        this.add(previewNumberPanel, BorderLayout.NORTH);

        JPanel currentPreviewPanel = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        previewNumberPanel.add(currentPreviewPanel);
        currentPreviewPanel.add(new UILabel(Inter.getLocText("Current_Preview_Rows") + ":"));

        currentRowsField = new UINumberField();
        currentPreviewPanel.add(currentRowsField);
        currentRowsField.setEditable(false);
        currentRowsField.setColumns(4);
        currentRowsField.setInteger(true);

        JPanel maxPanel = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        previewNumberPanel.add(maxPanel);
        maxPanel.add(new UILabel(Inter.getLocText("Datasource-Maximum_Number_of_Preview_Rows") + ":"));

        maxPreviewNumberField = new UINumberField();
        maxPanel.add(maxPreviewNumberField);
        maxPreviewNumberField.setColumns(4);
        maxPreviewNumberField.setInteger(true);

        DesignerEnvManager designerEnvManager = DesignerEnvManager.getEnvManager();
        maxPreviewNumberField.setValue(designerEnvManager.getMaxNumberOrPreviewRow());

        maxPreviewNumberField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                DesignerEnvManager designerEnvManager = DesignerEnvManager.getEnvManager();
                designerEnvManager.setMaxNumberOrPreviewRow((int) ((UINumberField) evt.getSource()).getValue());
            }
        });

        Icon refreshImage = BaseUtils.readIcon("/com/fr/design/images/control/refresh.png");
        refreshLabel = new UILabel(refreshImage);
        previewNumberPanel.add(refreshLabel);
        refreshLabel.addMouseListener(new MouseAdapter() {
            boolean mouseEntered = false;
            boolean buttonPressed = false;

            public void mouseEntered(MouseEvent e) { // ��������ʱ�����.
                mouseEntered = true;
                if (!buttonPressed) {
                    refreshLabel.setBackground(java.awt.Color.WHITE);
                    refreshLabel.setOpaque(true);
                    refreshLabel.setBorder(BorderFactory.createLineBorder(java.awt.Color.GRAY));
                }
            }

            public void mouseExited(MouseEvent e) {
                mouseEntered = false;
                refreshLabel.setOpaque(false);
                refreshLabel.setBorder(BorderFactory.createEmptyBorder());
            }

            public void mousePressed(MouseEvent e) {
                buttonPressed = true;
                refreshLabel.setBackground(java.awt.Color.lightGray);
            }

            public void mouseReleased(MouseEvent e) {
                buttonPressed = false;
                if (mouseEntered) {
                    refreshLabel.setBackground(java.awt.Color.WHITE);
                    try {
                        populate(tableData);
                        if (storeProcedureDataModel != null) {
                            populateStoreDataSQL();
                        }
                    } catch (Exception e1) {
                    }
                }
            }
        });

        preveiwTable = new SortableJTable(new TableSorter());
        preveiwTable.setRowSelectionAllowed(false);
        preveiwTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        this.add(new JScrollPane(preveiwTable), BorderLayout.CENTER);
        if (this.dialog == null) {
            this.dialog = this.showWindow(DesignerContext.getDesignerFrame());
        }
        progressBar = new AutoProgressBar(this, Inter.getLocText("Loading_Data"), "", 0, 100) {
            public void doMonitorCanceled() {
                if (getWorker() != null) {
                    getWorker().cancel(true);
                }
                getDialog().setVisible(false);
            }
        };
    }

    public AutoProgressBar getProgressBar() {
        return this.progressBar;
    }

    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("Preview");
    }

    private void addLoadedListener(LoadedEventListener l) {
        listeners.add(l);
    }

    private void fireLoadedListener() {
        for (LoadedEventListener l : listeners) {
            l.fireLoaded();
        }
    }

    /**
     * sets current row count.
     *
     * @param currentRows
     */
    private void setCurrentRows(int currentRows) {
        this.currentRowsField.setValue(currentRows);
    }

    private void resetPreviewTableColumnColor() {
        this.listeners.clear();

    }

    public BasicDialog getDialog() {
        return this.dialog;
    }

    public SwingWorker getWorker() {
        return this.worker;
    }

    // elake:ΪԤ�����columnIndex����cɫ.
    private void setPreviewTableColumnColor(final int columnIndex, final Color c) {
        addLoadedListener(new LoadedEventListener() {
            @Override
            public void fireLoaded() {
                TableColumn column = preveiwTable.getColumnModel().getColumn(columnIndex);
                DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
                    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                        JComponent comp = (JComponent) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                        comp.setBackground(c);
                        comp.setBorder(BorderFactory.createRaisedBevelBorder());
                        return comp;
                    }
                };
                column.setCellRenderer(cellRenderer);
            }
        });

    }

    /**
     * �������
     */
    public static void resetPreviewTable() {
        getInstance().preveiwTable = new SortableJTable(new TableSorter());
        getInstance().preveiwTable.setRowSelectionAllowed(false);
        getInstance().preveiwTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        getInstance().progressBar.close();
        getInstance().repaint();
    }

    private void setModel(TableModel tableModel) {
        TableSorter tableSorter = (TableSorter) preveiwTable.getModel();

        tableSorter.setTableModel(tableModel);

        preveiwTable.getParent().validate();
        preveiwTable.repaint();
    }


    /**
     * ֱ��Ԥ�����ݼ���û��ʵ��ֵ����ʾֵ
     *
     * @param tableData tableData
     */
    public static void previewTableData(TableData tableData) {
        previewTableData(tableData, -1, -1);
    }

    /**
     * Ԥ�����ݼ���keyIndexΪʵ��ֵ��valueIndexΪ��ʾֵ
     *
     * @param tableData  tableData
     * @param keyIndex
     * @param valueIndex
     */
    public static EmbeddedTableData previewTableData(TableData tableData, final int keyIndex, final int valueIndex) {
        PreviewTablePane previewTablePane = new PreviewTablePane();
        previewTablePane.setBorder(BorderFactory.createTitledBorder(Inter.getLocText("Data")));
        try {
            previewTablePane.populate(tableData);
            previewTablePane.resetPreviewTableColumnColor();

            if (keyIndex > -1) {
                previewTablePane.setPreviewTableColumnColor(keyIndex, Color.getHSBColor(0, 204, 204));
            }
            if (valueIndex > -1) {
                previewTablePane.setPreviewTableColumnColor(valueIndex, Color.lightGray);
            }

        } catch (Exception exp) {
            previewTablePane.setModel(new PreviewTableModel((int) previewTablePane.maxPreviewNumberField.getValue()));
            previewTablePane.showErrorMessage(exp);
        }
        if (!previewTablePane.dialog.isVisible()) {
            previewTablePane.dialog.setVisible(true);
        }

		return previewTablePane.previewTableData;
    }

    private void showErrorMessage(Exception exp) {
        String errMessage = exp.getLocalizedMessage();
        String columnErrMessage = errMessage.substring(0, errMessage.indexOf(">="));
        String tatolColumnErrMessage = errMessage.substring(errMessage.indexOf(">=") + 2);
        try {
            int choiceColumn = Integer.parseInt(columnErrMessage.trim());
            int tatalColumn = Integer.parseInt(tatolColumnErrMessage.trim());
            columnErrMessage = Inter.getLocText(new String[]{"Ser", String.valueOf(choiceColumn + 1), "Column_Does_Not_Exsit", ", ", "Total", String.valueOf(tatalColumn), "Column" + "!"});
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage(), e);
            return;
        }
        FRContext.getLogger().errorWithServerLevel(exp.getMessage(), exp);
        JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), columnErrMessage, Inter.getLocText("Error"), JOptionPane.ERROR_MESSAGE);
    }

    private void populate(TableData tableData) throws Exception {
        this.tableData = tableData;

        // p:ֱ��Ԥ��.
        if (tableData != null) {
            previewTableDataSQL();
        }
    }

    private void previewTableDataSQL() throws Exception {
        connectionBar = new AutoProgressBar(this, Inter.getLocText("Utils-Now_create_connection"), "", 0, 100) {
            public void doMonitorCanceled() {
                getWorker().cancel(true);
                getDialog().setVisible(false);
            }
        };
        setWorker();
        worker.execute();

    }


    private void setPreviewTableColumnValue(final Graphics g) {
        for (int i = 0; i < preveiwTable.getColumnModel().getColumnCount(); i++) {
            TableColumn column = preveiwTable.getColumnModel().getColumn(i);
            DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    JComponent comp = (JComponent) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    Font f = table.getFont();

                    //Ĭ����ϵͳ��֧�� �޷���ʾʱ  ��������� ����������Ϊ��.
                    Font defaultShowFont = FRFont.getInstance("", f.getStyle(), f.getSize());
                    if (value instanceof String) {
                        String str = (String) value;
                        for (int j = 0; j < str.length(); j++) {
                            char c = str.charAt(j);
                            if (!f.canDisplay(c)) {
                                table.setFont(defaultShowFont);
                            }
                        }
                    }
                    return comp;
                }
            };
            column.setCellRenderer(cellRenderer);
        }
    }


    private void setWorker() {

        worker = new SwingWorker<PreviewTableModel, Void>() {
            protected PreviewTableModel doInBackground() throws Exception {
                connectionBar.start();
                if (tableData instanceof DBTableData) {
                    boolean status = FRContext.getCurrentEnv().testConnection(((DBTableData) tableData).getDatabase());
                    if (!status) {
                        connectionBar.close();
                        throw new Exception(Inter.getLocText("Datasource-Connection_failed"));
                    }
                }
                connectionBar.close();
                PreviewTableModel previewModel = null;
                if (tableData instanceof MultiTDTableData) {
                    ((MultiTDTableData) tableData).setTableDataSource(DesignTableDataManager.getEditingTableDataSource());
                }
                previewTableData = DesignTableDataManager.previewTableDataNeedInputParameters(tableData, (int) maxPreviewNumberField.getValue(), true);
                // parameterInputDialog
                // update֮���parameters,ת��һ��parameterMap,����Ԥ��TableData
                previewModel = new PreviewTableModel(previewTableData.createDataModel(null), (int) maxPreviewNumberField.getValue());
                for (int i = 0; i < previewTableData.getColumnCount(); i++) {
                    Class<?> cls = previewTableData.getColumnClass(i);
                    if (cls == Date.class || cls == TIME.class || cls == Timestamp.class) {
                        previewModel.dateIndexs.add(i);
                    }
                }
                return previewModel;
            }

            public void done() {
                try {
                    PreviewTableModel model = get();
                    setModel(model);
                    setCurrentRows(model.getRowCount());
                    setPreviewTableColumnValue(getParent().getGraphics());
                    fireLoadedListener();
                } catch (Exception e) {
                    if (!(e instanceof CancellationException)) {
                        FRContext.getLogger().error(e.getMessage(), e);
                        JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), e.getMessage());
                    }
                    dialog.setVisible(false);
                } finally {
                    progressBar.close();
                }
            }
        };
    }

    /**
     * ֱ��Ԥ���洢���̵�һ���������ݼ���û��ʵ��ֵ����ʾֵ
     *
     * @param storeProcedureDataModel storeProcedureDataModel
     */
    public static void previewStoreData(ProcedureDataModel storeProcedureDataModel) {
        previewStoreData(storeProcedureDataModel, -1, -1);
    }

    /**
     * Ԥ���洢���̵�һ���������ݼ���keyIndexΪʵ��ֵ��valueIndexΪ��ʾֵ
     *
     * @param storeProcedureDataModel storeProcedureDataModel
     * @param keyIndex                ʵ��ֵ
     * @param valueIndex              ��ʾֵ
     */
    public static void previewStoreData(final ProcedureDataModel storeProcedureDataModel, final int keyIndex, final int valueIndex) {
        final PreviewTablePane previewTablePane = new PreviewTablePane();
        previewTablePane.storeProcedureDataModel = storeProcedureDataModel;
        previewTablePane.setBorder(BorderFactory.createTitledBorder(Inter.getLocText("Data")));

        try {
            previewTablePane.populateStoreDataSQL();
            previewTablePane.resetPreviewTableColumnColor();

            if (keyIndex > -1) {
                previewTablePane.setPreviewTableColumnColor(keyIndex, Color.getHSBColor(0, 204, 204));
            }
            if (valueIndex > -1) {
                previewTablePane.setPreviewTableColumnColor(valueIndex, Color.lightGray);
            }

        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage(), e);
        }
        previewTablePane.fireLoadedListener();
        previewTablePane.showWindow(DesignerContext.getDesignerFrame()).setVisible(true);
    }

    /**
     * ֱ��Ԥ���洢���̵����з������ݼ���û��ʵ��ֵ����ʾֵ
     *
     * @param storeProcedureDataModels storeProcedureDataModels
     */
    public static void previewStoreDataWithAllDs(ProcedureDataModel[] storeProcedureDataModels) {
        UITabbedPane tabPreviewpane = new UITabbedPane();
        int tableSize = storeProcedureDataModels.length;
        for (int i = 0; i < tableSize; i++) {
            PreviewTablePane previewTablePane = new PreviewTablePane();
            previewTablePane.storeProcedureDataModel = storeProcedureDataModels[i];
            previewTablePane.setBorder(BorderFactory.createTitledBorder(Inter.getLocText("Data")));
            try {
                previewTablePane.populateStoreDataSQL();
            } catch (Exception e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }
            tabPreviewpane.addTab(storeProcedureDataModels[i].getName(), previewTablePane);
        }

        BasicPane prieviewPane = new BasicPane() {

            @Override
            protected String title4PopupWindow() {
                return Inter.getLocText("Preview");
            }

        };
        prieviewPane.setLayout(FRGUIPaneFactory.createBorderLayout());
        prieviewPane.add(tabPreviewpane, BorderLayout.CENTER);
        prieviewPane.showWindow(DesignerContext.getDesignerFrame()).setVisible(true);
    }

    private void populateStoreDataSQL() throws Exception {
        PreviewTableModel previewModel;
        try {
            previewModel = new PreviewTableModel(storeProcedureDataModel, (int) maxPreviewNumberField.getValue());
        } catch (Exception e) {
            previewModel = new PreviewTableModel((int) maxPreviewNumberField.getValue());
        }
        setModel(previewModel);
        setCurrentRows(previewModel.getRowCount());
        fireLoadedListener();

    }
}
