package com.fr.design.data.tabledata.wrapper;

import com.fr.base.BaseUtils;
import com.fr.base.Env;
import com.fr.base.FRContext;
import com.fr.base.TableData;
import com.fr.design.data.DesignTableDataManager;
import com.fr.data.impl.storeproc.ProcedureDataModel;
import com.fr.data.impl.storeproc.StoreProcedure;
import com.fr.design.data.datapane.preview.PreviewTablePane;
import com.fr.design.gui.itree.refreshabletree.ExpandMutableTreeNode;
import com.fr.env.RemoteEnv;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
public final class StoreProcedureNameWrapper implements TableDataWrapper {
    private ProcedureDataModel procedureDataModel;
    private String name;
    private StoreProcedure storeProcedure;
    private List<String> columnNameList;

    /**
     * @param name           �洢���̱�������
     * @param storeProcedure �洢����
     */
    public StoreProcedureNameWrapper(String name, StoreProcedure storeProcedure) {
        this.name = name;
        this.storeProcedure = storeProcedure;
    }

    /**
     * �����ӽڵ�
     *
     * @return �ӽڵ�
     */
    public ExpandMutableTreeNode[] load() {
        List<String> namelist = calculateColumnNameList();
        ExpandMutableTreeNode[] res = new ExpandMutableTreeNode[namelist.size()];
        for (int i = 0; i < res.length; i++) {
            res[i] = new ExpandMutableTreeNode(namelist.get(i));
        }

        return res;
    }

    @Override
    public String getTableDataName() {
        return name;
    }

    public TableData getTableData() {
        return storeProcedure;
    }

    @Override
    public Icon getIcon() {
        return BaseUtils.readIcon("/com/fr/design/images/data/store_procedure.png");
    }

    private void createStore(boolean needLoadingBar) {
        try {
            procedureDataModel = DesignTableDataManager.createLazyDataModel(storeProcedure, needLoadingBar)[0];
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage());
        }
    }

    /**
     * ���ݼ�ִ�н�����ص������ֶ�
     * <p/>
     * TODO:Ҫ��Ҫ����Exception�أ����˸о����б�Ҫ
     *
     * @return �ֶ�
     */
    public List<String> calculateColumnNameList() {
        if (columnNameList != null) {
            return columnNameList;
        }
        columnNameList = new ArrayList<String>();
        Env env = FRContext.getCurrentEnv();
        if (env instanceof RemoteEnv) {
            try {
                createStore(false);
                columnNameList = Arrays.asList(procedureDataModel.getColumnName());
            } catch (Exception e) {
                FRContext.getLogger().errorWithServerLevel(e.getMessage(), e);
            }

        } else {
            if (procedureDataModel == null) {
                createStore(false);
            }
            if (procedureDataModel != null) {
                columnNameList = Arrays.asList(procedureDataModel.getColumnName());
            }
        }
        return columnNameList;
    }

    /**
     * Ԥ�����ݼ�
     */
    public void previewData() {
        if (procedureDataModel == null) {
            createStore(true);
        }
        PreviewTablePane.previewStoreData(procedureDataModel);

    }

    /**
     * Ԥ�����ݼ���������ʾֵ��ʵ��ֵ�ı�ǽ��
     *
     * @param keyIndex   ��ʾֵIndex
     * @param valueIndex ʵ��ֵindex
     */
    public void previewData(int keyIndex, int valueIndex) {
        if (procedureDataModel == null) {
            createStore(true);
        }
        PreviewTablePane.previewStoreData(procedureDataModel, keyIndex, valueIndex);
    }

    /**
     * �Ƿ��쳣
     *
     * @return �쳣����true
     */
    public boolean isUnusual() {
        return false;
    }

    public StoreProcedure getStoreProcedure() {
        return storeProcedure;
    }

}
