/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.server;

import com.fr.base.*;
import com.fr.dav.LocalEnv;
import com.fr.design.DesignModelAdapter;
import com.fr.design.actions.UpdateAction;
import com.fr.design.data.DesignTableDataManager;
import com.fr.design.data.datapane.TableDataTreePane;
import com.fr.design.data.tabledata.ResponseDataSourceChange;
import com.fr.design.data.tabledata.tabledatapane.TableDataManagerPane;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.design.menu.MenuKeySet;
import com.fr.file.DatasourceManager;
import com.fr.file.DatasourceManagerProvider;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * Global TableData.
 */
public class GlobalTableDataAction extends UpdateAction implements ResponseDataSourceChange {
    //private static TableDataManagerPane globalTableDataPane = new TableDataManagerPane();

    public GlobalTableDataAction() {
        this.setMenuKeySet(SERVER_TABLEDATA);
        this.setName(getMenuKeySet().getMenuKeySetName());
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/data/dock/serverdatabase.png"));
    }

    public static final MenuKeySet SERVER_TABLEDATA = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'S';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("DS-Server_TableData");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };

    /**
     * ����
     * @param evt �¼�
     */
    public void actionPerformed(ActionEvent evt) {
        final DesignerFrame designerFrame = DesignerContext.getDesignerFrame();
        final DatasourceManagerProvider datasourceManager = DatasourceManager.getProviderInstance();
        final DatasourceManager backupManager = datasourceManager.getBackUpManager();
        final TableDataManagerPane globalTableDataPane = new TableDataManagerPane() {
            public void complete() {
                populate(datasourceManager);
            }

            protected void renameConnection(String oldName, String newName) {
                datasourceManager.getConnectionLocalModifyTable().rename(oldName, newName);
            }
        };
        final BasicDialog globalTableDataDialog = globalTableDataPane.showLargeWindow(designerFrame, null);

        globalTableDataDialog.addDialogActionListener(new DialogActionAdapter() {

            @Override
            public void doOk() {
                if (!globalTableDataPane.isNamePermitted()) {
                    globalTableDataDialog.setDoOKSucceed(false);
                    return;
                }

                DesignTableDataManager.clearGlobalDs();
                globalTableDataPane.update(datasourceManager);
                if (!doWithDatasourceManager(datasourceManager, backupManager, globalTableDataPane, globalTableDataDialog)) {
                    //�������ʧ�ܣ��򲻹رնԻ���Ҳ��дxml�ļ������ҽ��Ի���λ�������������Ǹ�����ҳ��
                    return;
                }

                writeFile(datasourceManager);
                // ˢ�¹������ݼ�
                TableDataTreePane.getInstance(DesignModelAdapter.getCurrentModelAdapter());
                fireDSChanged(globalTableDataPane.getDsChangedNameMap());
            }

            public void doCancel() {
                datasourceManager.synchronizedWithServer();
            }
        });
        globalTableDataDialog.setVisible(true);
    }

    private void writeFile(DatasourceManagerProvider datasourceManager) {

        Env currentEnv = FRContext.getCurrentEnv();
        try {
            boolean isSuccess = currentEnv.writeResource(datasourceManager);
            if (!isSuccess) {
                throw new RuntimeException(Inter.getLocText("FR-Designer_Already_exist"));
            }
        } catch (Exception e) {
            throw new RuntimeException(Inter.getLocText("FR-Designer_Already_exist"));
        }
    }


    /**
     * �Ƿ�����������datasourceManager
     *
     * @param datasourceManager
     * @param databaseManagerPane
     * @return
     */
    private boolean doWithDatasourceManager(DatasourceManagerProvider datasourceManager, DatasourceManager backupManager,
                                            TableDataManagerPane tableDataManagerPane, BasicDialog databaseListDialog) {
        HashMap<String, TableData> modifyDetails = datasourceManager.getTableDataModifyDetails();
        modifyDetails.clear();
        Env currentEnv = FRContext.getCurrentEnv();
        ModifiedTable localModifiedTable = datasourceManager.checkTableDataModifyTable(backupManager, currentEnv.getUserID());
        boolean isFailed = false;
        if (currentEnv.isSupportLocalFileOperate() && !((LocalEnv) currentEnv).isNoRemoteUser()) {
            //����Ǳ��أ�������Զ���û�ʱ������Լ����޸ı�
            datasourceManager.updateSelfTableDataTotalModifiedTable(localModifiedTable, ModifiedTable.LOCAL_MODIFIER);
        } else {
            if (!currentEnv.isSupportLocalFileOperate()) {
                //�����Զ�̣���ȥȡ�����������µ��޸ı�,�����û�г�ͻ
                ModifiedTable currentServerModifyTable = currentEnv.getDataSourceModifiedTables(DatasourceManager.TABLEDATA);
                if (localModifiedTable.checkModifiedTableConflictWithServer(currentServerModifyTable, currentEnv.getUserID())) {
                    //�г�ͻ��������ʾ
                    String title = Inter.getLocText(new String[]{"Select", "Single", "Setting"});
                    int returnVal = JOptionPane.showConfirmDialog(DesignerContext.getDesignerFrame(), localModifiedTable.getWaringMessage(), title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (returnVal == JOptionPane.YES_OPTION) {
                        //����ǣ�������Ӧˢ��ȥ��ͻ
                        datasourceManager.synchronizedWithServer(backupManager, DatasourceManager.TABLEDATA);
                        //Ҫ������������ͻ�ģ������ϸ���޸ı��Ƚ����޸�
                        datasourceManager.doWithTableDataConfilct(localModifiedTable);
                        localModifiedTable.removeConfilct();
                        modifyDetails.clear();
                        //�������
                        tableDataManagerPane.populate(datasourceManager);
                    } else {
                        //����ʧ�ܣ�����ͣ��ҳ��
                        isFailed = true;
                    }
                }
            }
        }
        //���������������ܸ���
        int index = datasourceManager.isTableDataMapContainsRename();
        if (index != -1) {
            isFailed = true;
            tableDataManagerPane.setSelectedIndex(index);
        }
        databaseListDialog.setDoOKSucceed(!isFailed);
        //����޸ĳɹ�����ȥԶ�̶������޸��޸ı�
        if (!isFailed && !currentEnv.isSupportLocalFileOperate()) {
            currentEnv.writeDataSourceModifiedTables(localModifiedTable, DatasourceManager.TABLEDATA);
            localModifiedTable.clear();
            modifyDetails.clear();
        }
        return !isFailed;
    }


    public void update() {
        this.setEnabled(true);
    }

    /**
     * ��Ӧ���ݼ��ı�
     */
    public void fireDSChanged() {
        fireDSChanged(new HashMap<String, String>());
    }

    /**
     * ��Ӧ���ݼ��ı�
     *
     * @param map ��Ӧ��
     */
    public void fireDSChanged(Map<String, String> map) {
        DesignTableDataManager.fireDSChanged(map);
    }
}
