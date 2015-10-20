package com.fr.design.data.tabledata.wrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CancellationException;

import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.base.TableData;
import com.fr.data.impl.storeproc.ProcedureDataModel;
import com.fr.data.impl.storeproc.StoreProcedure;
import com.fr.design.data.DesignTableDataManager;
import com.fr.design.data.datapane.preview.PreviewTablePane;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.iprogressbar.AutoProgressBar;
import com.fr.design.gui.itree.refreshabletree.ExpandMutableTreeNode;
import com.fr.design.mainframe.DesignerContext;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;

/**
 * <code>StoreProcedureDataWrappe</code> ���洢���̵�һ���������ݼ�,����������<br>
 * Oracle���ݿ�϶�������SQL SERVER�Ͳ�һ������,�󲿷�����¶�û��.
 * <p/>
 * <code>StoreProcedureNameWrappe</code> ���洢���̱���ķ������ݼ�����������<br>
 * Oracle���ݿ�����������ʵû��Ҫ��������Oracle���з������ݼ��ĵ�һ����SQL SERVER�϶������������
 * <code>StoreProcedureNameWrappe</code>
 * ʵ�ʾ��ǣ���ǰ�������½����ݼ�ʱ������ߵ��б����϶�һ���洢���̵��ұߵ�SQL��壬
 * �õ���SQL���ִ�к󷵻ص����ݼ�������Ϊ������ܵõ�������ݼ�������Ĭ���õ�һ��
 *
 * @author zhou
 * @since 2012-4-12����10:29:15
 */
public final class StoreProcedureDataWrapper implements TableDataWrapper {
    public static final int PREVIEW_ALL = 0;
    public static final int PREVIEW_ONE = 1;
    public static AutoProgressBar loadingBar;

    private ProcedureDataModel procedureDataModel;
    private String dsName;
    private String storeprocedureName;
    private StoreProcedure storeProcedure;
    private List<String> columnNameList;
    private AutoProgressBar connectionBar;
    private ProcedureDataModel[] dataModels;
    private SwingWorker worker;
    private BasicDialog dialog;
    private int previewModel;

    public StoreProcedureDataWrapper(StoreProcedure storeProcedure, String storeprocedureName, String dsName) {
        this(storeProcedure, storeprocedureName, dsName, true);
    }


    /**
     * @param dsName             �洢����һ���������ݼ�������
     * @param storeProcedure     �洢����
     * @param storeprocedureName �洢���̵�����(ĳЩ����¿���Ϊ��)
     */
    public StoreProcedureDataWrapper(StoreProcedure storeProcedure, String storeprocedureName, String dsName, boolean needLoad) {
        this.dsName = dsName;
        this.storeProcedure = storeProcedure;
        this.storeProcedure.setCalculating(false);
        this.storeprocedureName = storeprocedureName;
        if (needLoad) {
            setWorker();
        }
        dialog = PreviewTablePane.getInstance().getDialog();
        dialog.addDialogActionListener(new DialogActionAdapter() {
            public void doOk() {
                getWorker().cancel(true);
            }

            public void doCancel() {
                getWorker().cancel(true);
            }
        });
        loadingBar = new AutoProgressBar(dialog, Inter.getLocText("FR-Designer_Loading_Data"), "", 0, 100) {
            public void doMonitorCanceled() {
                getDialog().setVisible(false);
                getWorker().cancel(true);
            }
        };
    }

    /**
     * ���ݼ�ִ�н�����ص������ֶ�
     *
     * @return ���ݼ�ִ�н�����ص������ֶ�
	 * 
	 *
	 * @date 2014-12-3-����7:43:17
	 * 
	 */
    public List<String> calculateColumnNameList() {
        if (columnNameList != null) {
            return columnNameList;
        }
        if (!createStore(false)) {
            JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), Inter.getLocText("FR-Engine_No-tableData"));
            return new ArrayList<String>();
        }
        columnNameList = Arrays.asList(procedureDataModel.getColumnName());
        return columnNameList;
    }

    /**
     * �����ӽڵ�
     *
     * @return �ڵ�����
	 * 
	 *
	 * @date 2014-12-3-����7:06:47
	 * 
	 */
    public ExpandMutableTreeNode[] load() {
        List<String> namelist;
        if (storeProcedure.isCalculating()) {
            namelist = Arrays.asList(new String[0]);
        } else {
            namelist = calculateColumnNameList();
        }
        ExpandMutableTreeNode[] res = new ExpandMutableTreeNode[namelist.size()];
        for (int i = 0; i < res.length; i++) {
            res[i] = new ExpandMutableTreeNode(namelist.get(i));
        }

        return res;
    }

    private boolean createStore(boolean needLoadingBar) {
        try {
            dataModels = DesignTableDataManager.createLazyDataModel(storeProcedure, needLoadingBar);
            if (dataModels == null || dataModels.length == 0) {
                return false;
            }
            for (int i = 0; i < dataModels.length; i++) {
                if (ComparatorUtils.equals(this.dsName, storeprocedureName + "_" + dataModels[i].getName())) {
                    procedureDataModel = dataModels[i];
                    break;
                }
            }
            return true;
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage());
        }
        return false;
    }

    @Override
    public Icon getIcon() {
        return BaseUtils.readIcon("/com/fr/design/images/data/store_procedure.png");
    }

    /**
	 * Ԥ������
	 * 
	 * @param previewModel Ԥ��ģʽ, ȫ������һ��
	 * 
	 *
	 * @date 2014-12-3-����7:05:50
	 * 
	 */
    public void previewData(final int previewModel) {
        this.previewModel = previewModel;
        new SwingWorker() {

            protected Object doInBackground() throws Exception {
                loadingBar.close();
                PreviewTablePane.resetPreviewTable();
                dialog.setVisible(true);
                return null;
            }
        }.execute();
        connectionBar = new AutoProgressBar(dialog, Inter.getLocText("Utils-Now_create_connection"), "", 0, 100) {
            public void doMonitorCanceled() {
                connectionBar.close();
                worker.cancel(true);
            }
        };
        worker.execute();
    }

    private void setWorker() {
        worker = new SwingWorker<Void, Void>() {
            protected Void doInBackground() throws Exception {
                connectionBar.start();
                boolean status = FRContext.getCurrentEnv().testConnection(((StoreProcedure) getTableData()).getDatabaseConnection());
                if (!status) {
                    connectionBar.close();
                    // bug 61345 Ԥ��ʧ��ʱ���رմ���
                    dialog.setVisible(false);
                    throw new Exception(Inter.getLocText("Datasource-Connection_failed"));
                }
                connectionBar.close();
                storeProcedure.resetDataModelList();
                createStore(true);
                return null;
            }

            public void done() {
                try {
                    get();
                    loadingBar.close();
                    dialog.setVisible(false);
                    switch (previewModel) {
                        case StoreProcedureDataWrapper.PREVIEW_ALL:
                            PreviewTablePane.previewStoreDataWithAllDs(dataModels);
                            break;
                        case StoreProcedureDataWrapper.PREVIEW_ONE:
                            previewData();
                            break;
                    }
                } catch (Exception e) {
                    if (!(e instanceof CancellationException)) {
                        FRContext.getLogger().error(e.getMessage(), e);
                        JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), e.getMessage());
                    }
                    loadingBar.close();
                }
            }
        };
    }

    private BasicDialog getDialog() {
        return this.dialog;
    }

    private SwingWorker getWorker() {
        return this.worker;
    }

    // august:���ֻ��Ԥ�����ص�һ�����ݼ�

    /**
     * Ԥ�����ص�һ�����ݼ�
     * 
	 *
	 * @date 2014-12-3-����7:42:53
	 * 
	 */
    public void previewData() {
        previewData(-1, -1);
    }

    // august:���ֻ��Ԥ�����ص�һ�����ݼ�

    /**
     * Ԥ�����ص�һ�����ݼ���������ʾֵ��ʵ��ֵ�ı�ǽ��
     * 
	 * @param keyIndex ʵ��ֵ
	 * @param valueIndex ��ʾֵ
	 * 
	 *
	 * @date 2014-12-3-����7:42:27
	 * 
	 */
    public void previewData(final int keyIndex, final int valueIndex) {
        PreviewTablePane.previewStoreData(procedureDataModel, keyIndex, valueIndex);
    }


    /**
     * Ԥ�����ص��������ݼ���ֻ���ڱ༭�洢����ʱ���õ�
     */
    public void previewAllTable() {
        if (procedureDataModel == null) {
            if (!createStore(true)) {
                return;
            }
        }
        PreviewTablePane.previewStoreDataWithAllDs(dataModels);
    }

    @Override
    public String getTableDataName() {
        return dsName;
    }

    public TableData getTableData() {
        return storeProcedure;
    }

    public String getStoreprocedureName() {
        return storeprocedureName;
    }

    /**
     * �Ƿ��쳣
     *
     * @return �Ƿ��쳣
     */
    public boolean isUnusual() {
        return false;
    }

    public boolean equals(Object obj) {
        return obj instanceof StoreProcedureDataWrapper
                && ComparatorUtils.equals(this.dsName, ((StoreProcedureDataWrapper) obj).getTableDataName())
                && ComparatorUtils.equals(this.storeProcedure, ((StoreProcedureDataWrapper) obj).getTableData())
                && ComparatorUtils.equals(this.storeprocedureName, ((StoreProcedureDataWrapper) obj).getStoreprocedureName());

    }

}
