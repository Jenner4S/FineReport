package com.fr.design.data.tabledata.wrapper;

import com.fr.base.FRContext;
import com.fr.base.TableData;
import com.fr.data.TableDataSource;
import com.fr.data.impl.*;
import com.fr.data.impl.storeproc.StoreProcedure;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.data.datapane.TableDataNameObjectCreator;
import com.fr.design.data.tabledata.tabledatapane.*;
import com.fr.design.fun.TableDataCreatorProvider;
import com.fr.file.DatasourceManagerProvider;
import com.fr.general.ComparatorUtils;
import com.fr.stable.ArrayUtils;
import com.fr.stable.StringUtils;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * ����TableDataȡ��Ӧ��һЩ����
 *
 * @author zhou
 * @since 2012-3-28����10:03:52
 */
public abstract class TableDataFactory {
    /**
     * ��˳���,����������
     */
    private static Map<String, TableDataNameObjectCreator> map = new java.util.LinkedHashMap<String, TableDataNameObjectCreator>();

    /**
     * ͬһ���͵�ֻ�ܼ�һ��,�ͼ����ϲ����,��ΪҪ������������е� FileTableData���ӽ�������ôFileTableData������Ͳ���ȷ��
     */
    static {
        map.put(DBTableData.class.getName(), new TableDataNameObjectCreator(null, "/com/fr/design/images/data/database.png", DBTableData.class, DBTableDataPane.class));
        map.put(ClassTableData.class.getName(), new TableDataNameObjectCreator(null, "/com/fr/design/images/data/source/classTableData.png", ClassTableData.class, ClassTableDataPane.class));
        map.put(EmbeddedTableData.class.getName(), new TableDataNameObjectCreator(null, "/com/fr/design/images/data/dataTable.png", EmbeddedTableData.class, EmbeddedTableDataPane.class));
        map.put(DecoratedTableData.class.getName(), new TableDataNameObjectCreator(null, "/com/fr/design/images/data/multi.png", DecoratedTableData.class, DecoratedTableDataPane.class));
        map.put(StoreProcedure.class.getName(), new TableDataNameObjectCreator(null, "/com/fr/design/images/data/store_procedure.png", StoreProcedure.class, ProcedureDataPane.class));
        map.put(MultiTDTableData.class.getName(), new TableDataNameObjectCreator(null, "/com/fr/design/images/data/multi.png", MultiTDTableData.class, MultiTDTableDataPane.class));
        map.put(FileTableData.class.getName(), new TableDataNameObjectCreator(null, "/com/fr/design/images/data/file.png", FileTableData.class, FileTableDataPane.class));
        map.put(RecursionTableData.class.getName(), new TableDataNameObjectCreator(null, "/com/fr/design/images/data/tree.png", RecursionTableData.class, TreeTableDataPane.class));
        map.put(MultiFieldTableData.class.getName(), new TableDataNameObjectCreator(null, "/com/fr/design/images/data/database.png", MultiFieldTableData.class, null));
    }

    /**
     * ע�����
     *
     * @param clazz ���ݼ���
     * @param creator ���
     */
    public static void register(Class<? extends TableData> clazz, TableDataNameObjectCreator creator) {
        map.put(clazz.getName(), creator);
    }

    private static TableDataNameObjectCreator getTableDataNameObjectCreator(TableData tabledata) {
        TableDataCreatorProvider creatorProvider = ExtraDesignClassManager.getInstance().getTableDataCreatorProvider();
        if (creatorProvider != null) {
            map.putAll(creatorProvider.registerMap());
        }
        TableDataNameObjectCreator tableDataNameObjectCreator = map.get(tabledata.getClass().getName());
        if (tableDataNameObjectCreator == null) {
            tableDataNameObjectCreator = map.get(tabledata.getClass().getSuperclass().getName());
            if (tableDataNameObjectCreator == null) {
                tableDataNameObjectCreator = map.get(tabledata.getClass().getSuperclass().getSuperclass().getName());// �������ɣ������ټ�
                // ����ѭ����
            }
        }
        return tableDataNameObjectCreator;
    }

    /**
     * ��ȡ���ݼ�����Ӧ�ı༭���
     *
     * @param tabledata ���ݼ�
     * @param name      ����
     * @return �������ݼ���Ӧ��pane
     */
    public static AbstractTableDataPane<?> creatTableDataPane(TableData tabledata, String name) {
        AbstractTableDataPane datapane = null;
        TableDataNameObjectCreator tableDataNameObjectCreator = getTableDataNameObjectCreator(tabledata);
        Class<? extends AbstractTableDataPane<?>> creatorClass = (Class<AbstractTableDataPane<?>>) tableDataNameObjectCreator.getUpdatePane();
        if (tableDataNameObjectCreator != null && creatorClass != null) {
            try {
                if (ComparatorUtils.equals(creatorClass, MultiTDTableDataPane.class) || ComparatorUtils.equals(creatorClass, TreeTableDataPane.class)) {
                    Constructor constructor = creatorClass.getDeclaredConstructor(new Class[]{String.class});
                    constructor.setAccessible(true);
                    datapane = (AbstractTableDataPane) constructor.newInstance(name);
                } else {
                    datapane = creatorClass.newInstance();
                }
                datapane.populateBean(tabledata); // August:����tabledata�Ǹո��½��Ļ���ԭ���ģ�һ��populate��ȥ�����������Ǵ���д�Ĳ���
            } catch (Exception e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }
        }
        return datapane;
    }

    /**
     * �������ݼ����
     *
     * @param tabledata ���ݼ�
     * @return �������ݼ���Ӧ��pane
     */
    public static AbstractTableDataPane<?> creatTableDataPane(TableData tabledata) {
        return creatTableDataPane(tabledata, StringUtils.EMPTY);
    }

    /**
     * ��ȡ���ݼ�����Ӧ��ͼ��·��
     *
     * @param tabledata
     * @return
     */
    public static String getIconPath(TableData tabledata) {
        TableDataNameObjectCreator tableDataNameObjectCreator = getTableDataNameObjectCreator(tabledata);
        if (tableDataNameObjectCreator != null && tableDataNameObjectCreator.getIconPath() != null) {
            return tableDataNameObjectCreator.getIconPath();
        }
        return "/com/fr/design/images/data/database.png";
    }

    /**
     * ��ȡ�Ѿ��ź�˳�������
     * �����ݿ��ѯ���ٳ������������ݼ����ٹ������ݼ������ļ����ݼ�����Щ�ڲ����ڲ��������֣�0-9��������ĸ��a-z����Ȼ���֣�ƴ������������
     *
     * @param source
     * @return
     */
    @SuppressWarnings("unchecked")
    public static String[] getSortOfChineseNameOfTemplateData(TableDataSource source) {
        clearAll();
        java.util.Iterator<String> nameIt = source.getTableDataNameIterator();
        while (nameIt.hasNext()) {
            String name = nameIt.next();
            TableData td = source.getTableData(name);
            addName(name, td);
        }
        return getSortedNameArray();
    }

    @SuppressWarnings("unchecked")
    public static String[] getSortOfChineseNameOfServerData(DatasourceManagerProvider mgr) {
        clearAll();
        java.util.Iterator<String> nameIt = mgr.getTableDataNameIterator();
        while (nameIt.hasNext()) {
            String name = nameIt.next();
            TableData td = mgr.getTableData(name);
            addName(name, td);
        }
        return getSortedNameArray();
    }

    private static String[] getSortedNameArray() {
        Iterator<Entry<String, TableDataNameObjectCreator>> entryIt = map.entrySet().iterator();
        List<String> namelist = new ArrayList<String>();
        while (entryIt.hasNext()) {
            Entry<String, TableDataNameObjectCreator> entry = entryIt.next();
            TableDataNameObjectCreator tableDataNameObjectCreator = entry.getValue();
            namelist.addAll(tableDataNameObjectCreator.getNames());
        }
        return namelist.toArray(ArrayUtils.EMPTY_STRING_ARRAY);
    }

    private static void addName(String name, TableData tabledata) {
        if (tabledata == null) {
            return;
        }

        TableDataNameObjectCreator tableDataNameObjectCreator = getTableDataNameObjectCreator(tabledata);
        if (tableDataNameObjectCreator == null) {
            return;
        }
        tableDataNameObjectCreator.addNames(name);
    }

    private static void clearAll() {
        Iterator<Entry<String, TableDataNameObjectCreator>> entryIt = map.entrySet().iterator();
        while (entryIt.hasNext()) {
            entryIt.next().getValue().clear();
        }
    }
}
