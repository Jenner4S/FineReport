package com.fr.design.data;

import com.fr.base.Env;
import com.fr.base.FRContext;
import com.fr.base.StoreProcedureParameter;
import com.fr.base.TableData;
import com.fr.data.TableDataSource;
import com.fr.data.core.DataCoreXmlUtils;
import com.fr.design.data.datapane.preview.PreviewTablePane;
import com.fr.data.impl.EmbeddedTableData;
import com.fr.data.impl.storeproc.ProcedureDataModel;
import com.fr.data.impl.storeproc.StoreProcedure;
import com.fr.data.impl.storeproc.StoreProcedureConstants;
import com.fr.design.data.tabledata.wrapper.*;
import com.fr.design.DesignModelAdapter;
import com.fr.design.gui.iprogressbar.AutoProgressBar;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.parameter.ParameterInputPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.file.DatasourceManager;
import com.fr.file.DatasourceManagerProvider;
import com.fr.general.ComparatorUtils;
import com.fr.general.data.DataModel;
import com.fr.general.data.TableDataException;
import com.fr.script.Calculator;
import com.fr.stable.ArrayUtils;
import com.fr.stable.ParameterProvider;
import com.fr.stable.StringUtils;
import com.fr.stable.xml.XMLPrintWriter;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.io.ByteArrayOutputStream;
import java.text.Collator;
import java.util.*;
import java.util.Map.Entry;

/**
 * ���������������ݼ�����:
 * 1.����ÿ��TableData,��������Ӧ��TableDataWrapper.TableDataWrapper������TableData�������л���
 * 2.TableDataWrapper�ǲ�֧�ֶ�TableData���޸Ĳ�ѯ��� ���������޸�ɾ������
 * ���TableData�仯�ˣ���ôTableDataWrapper Ҳ����������,Ȼ��������.������֤��������ȷ��,����ȡ������������С�
 * 3.����ģ�����ݼ����ؼ���Ӧ�ñ�֤����ģ�����ݼ�֮�䲻ͬ������Ĭ���и�����ģ�����Ĳ�����
 * 4.���˾�����ȫû�б�Ҫ��������һ��SQL����Ӧһ�������е������������̫�����ˡ�����ÿ�αȽϹؼ��ʶ�����
 * <p/>
 * !!!Notice: ����Ԥ�����ݼ��Ĳ��������������漰�����ݼ��Ľ����������Ҫ���������(��Ϊ�������ݼ��Ļ���,����Ҫ�ظ����㣩
 *
 * @author zhou
 */
public abstract class DesignTableDataManager {
    /**
     * ��ʵglobalDsCacheû�о��Եı�Ҫ��ֻ��Ϊ�˲������㡣���û��������ôÿ����շ��������ݼ����ߴ洢���̵�ʱ�򣬻�Ҫȥ������һ�£�
     * ����������ܱȽϸ��� �� �Ӽ��ٴ��븴�ӶȵĽǶȿ������Ǻ��б�Ҫ��
     */
    private static java.util.Map<String, TableDataWrapper> globalDsCache = new java.util.HashMap<String, TableDataWrapper>();
    private static java.util.Map<String, String> dsNameChangedMap = new HashMap<String, String>();
    private static List<ChangeListener> dsListeners = new ArrayList<ChangeListener>();

    public static String NO_PARAMETER = "no_paramater_pane";

    //���ڼ�¼�Ƿ�Ҫ����������
    private static ThreadLocal<String> threadLocal = new ThreadLocal<String>();


    /**
     * ���ȫ�� ���ݼ�����.
     */
    public static void clearGlobalDs() {
        globalDsCache.clear();
    }

    /**
     * ��Ӧ���ݼ��ı�.
     */
    private static void fireDsChanged() {
        for (int i = 0; i < dsListeners.size(); i++) {
        //��ǿforѭ���õ�iteratorʵ�ֵ�, ����м��ĸ�listener�޸Ļ�ɾ����(��ChartEditPane.dsChangeListener),
        // ����dsListeners��arraylist, ��ʱ��ConcurrentModifyException
//        for (ChangeListener l : dsListeners) {
            ChangeEvent e = null;
            dsListeners.get(i).stateChanged(e);
        }
    }

    /**
     * ��Ӧ���ݼ��ı�
     *
     * @param dsNameChangedMap �ı����ֵ����ݼ�
     */
    public static void fireDSChanged(Map<String, String> dsNameChangedMap) {
        if (!dsNameChangedMap.isEmpty()) {
            setDsNameChangedMap(dsNameChangedMap);
        }
        fireDsChanged();
        dsNameChangedMap.clear();
    }

    private static void setDsNameChangedMap(Map<String, String> map) {
        Iterator iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            dsNameChangedMap.put(key, map.get(key));
        }
    }

    /**
     * ���ݿ��Ƿ�ı�
     *
     * @param oldDsName ������
     * @return ���򷵻�true
     */
    public static boolean isDsNameChanged(String oldDsName) {
        return dsNameChangedMap.containsKey(oldDsName);
    }

    public static String getChangedDsNameByOldDsName(String oldDsName) {
        if (isDsNameChanged(oldDsName)) {
            return dsNameChangedMap.get(oldDsName);
        } else {
            return StringUtils.EMPTY;
        }
    }

    /**
     * ���ģ�����ݼ��ı� �����¼�.
     *
     * @param l ChangeListener������
     */
    public static void addDsChangeListener(ChangeListener l) {
        dsListeners.add(l);
    }

    /**
     * ��ȡ����Դsource��dsName�������ֶ�
     *
     * @param source ����Դ
     * @param dsName ���ݼ�����
     * @return
     */
    public static String[] getSelectedColumnNames(TableDataSource source, String dsName) {
        java.util.Map<String, TableDataWrapper> resMap = getAllEditingDataSet(source);
        java.util.Map<String, TableDataWrapper> dsMap = getAllDataSetIncludingProcedure(resMap);
        TableDataWrapper tabledataWrapper = dsMap.get(dsName);
        return tabledataWrapper == null ? new String[0] : tabledataWrapper.calculateColumnNameList().toArray(new String[0]);
    }

    /**
     * august:���ص�ǰ���ڱ༭�ľ��б�������Դ��ģ��(���������ۺϱ���) ���� : ͼ��ģ��
     *
     * @return TableDataSource
     *         attention:����������й�ϵ�ľ�̬������������л�ģ��tab���仯�ģ���Ӧ������ִ�и÷�������ˢ�����
     */
    public static TableDataSource getEditingTableDataSource() {
        return DesignModelAdapter.getCurrentModelAdapter() == null ? null : DesignModelAdapter.getCurrentModelAdapter().getBook();
    }

    /**
     * ���ص�ǰģ��(source)���ݼ������������ݼ��ʹ洢���������е����ݼ�����
     *
     * @param source
     * @return
     */
    public static String[] getAllDSNames(TableDataSource source) {
        Iterator<Entry<String, TableDataWrapper>> entryIt = getAllEditingDataSet(source).entrySet().iterator();
        List<String> list = new ArrayList<String>();
        while (entryIt.hasNext()) {
            Entry<String, TableDataWrapper> entry = entryIt.next();
            list.add(entry.getKey());
        }
        return list.toArray(new String[list.size()]);
    }

    /**
     * �����ݹ������ã����ص�ǰģ�����ݼ������������ݼ����洢���̱�������˳���
     */
    public static java.util.Map<String, TableDataWrapper> getAllEditingDataSet(TableDataSource source) {
        java.util.Map<String, TableDataWrapper> resMap = new java.util.LinkedHashMap<String, TableDataWrapper>();
        // ģ�����ݼ�
        addTemplateData(resMap, source);

        // ���������ݼ�
        addServerData(resMap);
        // �洢����
        addStoreProcedureData(resMap);

        return resMap;
    }

    public static java.util.Map<String, TableDataWrapper> getAllDataSetIncludingProcedure(java.util.Map<String, TableDataWrapper> resMap) {
        java.util.LinkedHashMap<String, TableDataWrapper> dsMap = new java.util.LinkedHashMap<String, TableDataWrapper>();
        Iterator<Entry<String, TableDataWrapper>> entryIt = resMap.entrySet().iterator();
        while (entryIt.hasNext()) {
            String key = entryIt.next().getKey();
            TableDataWrapper tableDataWrapper = resMap.get(key);
            if (tableDataWrapper.getTableData() instanceof StoreProcedure) {
                StoreProcedure storeProcedure = (StoreProcedure) tableDataWrapper.getTableData();
                boolean hasSchemaOrResult = false;
                StoreProcedureParameter[] parameters = StoreProcedure.getSortPara(storeProcedure.getParameters());
                String name = tableDataWrapper.getTableDataName();
                ArrayList<String> resultNames = storeProcedure.getResultNames();
                TableDataWrapper tdw = new StoreProcedureNameWrapper(name + "_Table", storeProcedure);

                for (StoreProcedureParameter parameter : parameters) {
                    if (parameter.getSchema() != StoreProcedureConstants.IN) {
                        String parameterName = name + "_" + parameter.getName();
                        TableDataWrapper newTwd = new StoreProcedureDataWrapper(storeProcedure, name, parameterName, false);
                        dsMap.put(parameterName, newTwd);
                        hasSchemaOrResult = true;
                    }
                }

                if (!resultNames.isEmpty()) {
                    hasSchemaOrResult = true;
                    for (int i = 0; i < resultNames.size(); i++) {
                        String parameterName = name + "_" + resultNames.get(i);
                        TableDataWrapper newTwd = new StoreProcedureDataWrapper(storeProcedure, name, parameterName, false);
                        dsMap.put(parameterName, newTwd);
                    }
                }

                if (!hasSchemaOrResult) {
                    dsMap.put(name + "_Table", tdw);
                }
            } else {
                dsMap.put(key, tableDataWrapper);
            }
        }
        return dsMap;
    }

    /**
     * �����ݹ������ã����ص�ǰ���������ݼ����洢�������е����ݼ�������˳���
     */
    public static java.util.Map<String, TableDataWrapper> getGlobalDataSet() {
        java.util.Map<String, TableDataWrapper> resMap = new java.util.LinkedHashMap<String, TableDataWrapper>();

        // ���������ݼ�
        addServerData(resMap);
        // �洢����
        addStoreProcedureData(resMap);

        return resMap;
    }

    /**
     * ���ݹ������ã����ص�ǰģ�����ݼ������������ݼ����洢�������е����ݼ�������˳���
     */
    public static List<Map<String, TableDataWrapper>> getEditingDataSet(TableDataSource source) {
        Map<String, TableDataWrapper> templateDataMap = new LinkedHashMap<String, TableDataWrapper>();
        Map<String, TableDataWrapper> serverDataMap = new LinkedHashMap<String, TableDataWrapper>();
        Map<String, TableDataWrapper> storeProcedureMap = new LinkedHashMap<String, TableDataWrapper>();

        addTemplateData(templateDataMap, source);
        addServerData(serverDataMap);
        addStoreProcedureData(storeProcedureMap);

        List<Map<String, TableDataWrapper>> list = new ArrayList<Map<String, TableDataWrapper>>();
        list.add(templateDataMap);
        list.add(serverDataMap);
        list.add(storeProcedureMap);
        return list;
    }

    private static void addTemplateData(java.util.Map<String, TableDataWrapper> resMap, TableDataSource source) {
        if (source != null) {
            String[] namearray = TableDataFactory.getSortOfChineseNameOfTemplateData(source);
            for (String tabledataname : namearray) {
                TableData td = source.getTableData(tabledataname);
                TableDataWrapper tdw = new TemplateTableDataWrapper(td, tabledataname);
                resMap.put(tabledataname, tdw);
            }
        }
    }

    private static void addServerData(java.util.Map<String, TableDataWrapper> resMap) {
        DatasourceManagerProvider mgr = DatasourceManager.getProviderInstance();
        String[] namearray = TableDataFactory.getSortOfChineseNameOfServerData(mgr);
        for (String name : namearray) {
            if (globalDsCache.containsKey(name)) {
                resMap.put(name, globalDsCache.get(name));
            } else {
                TableDataWrapper tdw = new ServerTableDataWrapper(mgr.getTableData(name), name);
                resMap.put(name, tdw);
                globalDsCache.put(name, tdw);
            }
        }
    }



    private static void addStoreProcedureData(java.util.Map<String, TableDataWrapper> resMap) {
        DatasourceManagerProvider mgr = DatasourceManager.getProviderInstance();
        String[] namearray = new String[0];
        @SuppressWarnings("unchecked")
        java.util.Iterator<String> nameIt = mgr.getProcedureNameIterator();
        while (nameIt.hasNext()) {
            namearray = (String[]) ArrayUtils.add(namearray, nameIt.next());
        }
        Arrays.sort(namearray, Collator.getInstance(java.util.Locale.CHINA));

        for (String name : namearray) {
            StoreProcedure storeProcedure = mgr.getProcedure(name);
            if (globalDsCache.containsKey(name)) {
                resMap.put(name, globalDsCache.get(name));
            } else {
                TableDataWrapper tdw = new StoreProcedureNameWrapper(name, storeProcedure);
                resMap.put(name, tdw);
                globalDsCache.put(name, tdw);
            }
        }
    }


    /**
     * Ԥ����Ҫ���������ݼ�
     *
     * @param tabledata      ���ݼ�
     * @param rowCount       ��ҪԤ��������
     * @param needLoadingBar �Ƿ���Ҫ���ؽ�����
     * @return ���ݼ�
     * @throws Exception �쳣
     */
    public static EmbeddedTableData previewTableDataNeedInputParameters(TableData tabledata, int rowCount, boolean needLoadingBar) throws Exception {
        return previewTableData(tabledata, rowCount, true, needLoadingBar);
    }

    /**
     * Ԥ������Ҫ���������ݼ�
     *
     * @param tabledata      ���ݼ�
     * @param rowCount       ��ҪԤ��������
     * @param needLoadingBar �Ƿ���Ҫ���ؽ�����
     * @return ���ݼ�
     * @throws Exception �쳣
     */
    public static EmbeddedTableData previewTableDataNotNeedInputParameters(TableData tabledata, int rowCount, boolean needLoadingBar) throws Exception {
        return previewTableData(tabledata, rowCount, false, needLoadingBar);
    }

    /**
     * ��ȡԤ�����EmbeddedTableData�����ǵ�Env
     *
     * @param tabledata
     * @param rowCount
     * @param isMustInputParameters �Ƿ�����������ֵ�����ܲ�����û��Ĭ��ֵ����һ��Ԥ��ʱ���ֵΪtrue����Ϊһ��Ԥ����Ҫ����ͬ�Ĳ����Ľ���ġ�
     *                              ����ȡ���ݼ����ֶ�����ʱ����û��Ҫ
     * @return
     */
    private static EmbeddedTableData previewTableData(TableData tabledata, int rowCount, boolean isMustInputParameters, boolean needLoadingBar) throws Exception {
        final AutoProgressBar loadingBar = PreviewTablePane.getInstance().getProgressBar();
        Env currentEnv = FRContext.getCurrentEnv();
        EmbeddedTableData embeddedTableData = null;
        ParameterProvider[] parameters = currentEnv.getTableDataParameters(tabledata);
        boolean isNullParameter = parameters == null || parameters.length == 0;
        ParameterProvider[] tableDataParameter = tabledata.getParameters(Calculator.createCalculator());
        boolean isOriginalNUllParameter = tableDataParameter == null || tableDataParameter.length == 0;
        if (isNullParameter && !isOriginalNUllParameter) {
            parameters = tableDataParameter;
        }
        boolean hasValue = true;
        for (ParameterProvider parameter : parameters) {
            if (parameter.getValue() == null || ComparatorUtils.equals(StringUtils.EMPTY, parameter.getValue())) {
                hasValue = false;
                break;
            }
        }
        final Map<String, Object> parameterMap = new HashMap<String, Object>();
        if (!hasValue || isMustInputParameters) {
            if (parameters != null && parameters.length > 0) {
                final ParameterInputPane pPane = new ParameterInputPane(parameters);
                pPane.showSmallWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {

                    public void doOk() {
                        parameterMap.putAll(pPane.update());
                    }
                }).setVisible(true);
            }
        } else {
            for (int i = 0; i < parameters.length; i++) {
                parameterMap.put(parameters[i].getName(), parameters[i].getValue());
            }
        }
        if (loadingBar != null && needLoadingBar) {
            loadingBar.start();
        }
        try {
            embeddedTableData = currentEnv.previewTableData(tabledata, parameterMap, rowCount);
        } catch (TableDataException e) {
            throw new TableDataException(e.getMessage(), e);
        } finally {
            new Timer().schedule(new TimerTask() {
                public void run() {
                    loadingBar.close();
                }
            }, 100);
        }
        return embeddedTableData;
    }

    /**
     * ����<code>TableData</code>��������,ע��<code>TableData</code>
     * ��û�п��ǲ����ġ����ڼ򵥵Ĳ�ѯ������ɵ�<code>TableData</code>, ����
     * <code>EmbeddedTableData</code>. ����˵�������ֵ�-���ݿ�����ɵ�<code>TableData</code>��
     * ʹ�ø÷�����û�����ݼ�����Ĺ��ܵ�
     *
     * @return List<String>
     */
    public static List<String> getColumnNamesByTableData(TableData tableData) {
        List<String> columnNames = new ArrayList<String>();
        if (tableData != null) {
            DataModel rs = tableData.createDataModel(Calculator.createCalculator());
            int value;
            try {
                value = rs.getColumnCount();
                for (int i = 0; i < value; i++) {
                    columnNames.add(rs.getColumnName(i));
                }
                rs.release();
            } catch (Exception e) {
                FRContext.getLogger().error(e.getMessage());
            }

        }

        return columnNames;
    }

    /**
     * �÷�����Ҫ������StoreProcedure����ġ�dataModelList�л������á��Ļ���
     * �����ø÷����������һ���Ѿ������˵Ĵ洢�����ظ�����.�ͷ�ҳԤ��ʱ�������һ�����������ж���������ݼ��Ĵ洢������˵���б�Ҫ
     *
     * @param needLoadingBar �Ƿ���Ҫ������
     * @param storeProcedure �洢����
     * @return ����
     */
    public static ProcedureDataModel[] createLazyDataModel(StoreProcedure storeProcedure, boolean needLoadingBar) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XMLPrintWriter writer = XMLPrintWriter.create(out);
        // ��storeProcedureд��xml�ļ���out
        DataCoreXmlUtils.writeXMLStoreProcedure(writer, storeProcedure, null);
        Env currentEnv = FRContext.getCurrentEnv();
        if (storeProcedure.getDataModelSize() > 0 && !storeProcedure.isFirstExpand()) {
            return storeProcedure.creatLazyDataModel();
        }
        ParameterProvider[] inParameters = currentEnv.getStoreProcedureParameters(storeProcedure);
        final Map<String, Object> parameterMap = new HashMap<String, Object>();
        if (inParameters.length > 0 && !ComparatorUtils.equals(threadLocal.get(), NO_PARAMETER)) {// ���Parameter.
            final ParameterInputPane pPane = new ParameterInputPane(inParameters);
            pPane.showSmallWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {
                public void doOk() {
                    parameterMap.putAll(pPane.update());
                }
            }).setVisible(true);

        }
        storeProcedure.setFirstExpand(false);
        if (needLoadingBar) {
            StoreProcedureDataWrapper.loadingBar.start();
        }
        return FRContext.getCurrentEnv().previewProcedureDataModel(storeProcedure, parameterMap, 0);
    }

    public static void setThreadLocal(String value) {
        threadLocal.set(value);
    }


}
