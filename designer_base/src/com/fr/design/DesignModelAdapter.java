package com.fr.design;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fr.base.Parameter;
import com.fr.base.io.IOFile;
import com.fr.data.TableDataSource;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.mainframe.JTemplateProvider;
import com.fr.form.ui.Widget;
import com.fr.general.ComparatorUtils;
import com.fr.stable.js.WidgetName;

/**
 * ��ǰ�������ģʽ
 *
 * @author zhou
 * @since 2012-7-26����11:24:54
 */
public abstract class DesignModelAdapter<T extends IOFile, S extends JTemplateProvider> {

    private static DesignModelAdapter<?, ?> currentModelAdapter;
    protected S jTemplate;

    public DesignModelAdapter(S jTemplate) {
        this.jTemplate = jTemplate;
        setCurrentModelAdapter(this);
    }

    public T getBook() {
        return (T) ((JTemplate) jTemplate).getTarget();
    }

    public static void setCurrentModelAdapter(DesignModelAdapter<?, ?> model) {
        currentModelAdapter = model;
    }

    public static DesignModelAdapter<?, ?> getCurrentModelAdapter() {
        return currentModelAdapter;
    }

    /**
     * ��ӦĿ��ı��¼�.
     */
    public void fireTargetModified() {
        ((JTemplate) this.jTemplate).fireTargetModified();
    }

    public String[] getFloatNames() {
        return new String[0];
    }

    public Parameter[] getParameters() {
        return new Parameter[0];
    }

    // �������
    public Parameter[] getReportParameters() {
        return new Parameter[0];
    }

    /**
     * ����Դ����
     *
     * @return
     */
    public Parameter[] getTableDataParameters() {
        return new Parameter[0];
    }

    /**
     * ���������ݼ�
     *
     * @param oldName            ������
     * @param newName            ������
     * @param isNeedFireModified �Ƿ���Ҫ��������
     * @return �������ɹ�����True
     */
    public boolean renameTableData(String oldName, String newName, boolean isNeedFireModified) {
        if (!ComparatorUtils.equals(oldName, newName)) {
            TableDataSource tds = getBook();
            boolean b;
            b = tds.renameTableData(oldName, newName);
            if (!b) {
                return b;
            }
            if (isNeedFireModified) {
                fireTargetModified();
            }
        }
        return true;
    }

    /**
     * ������TableData���һЩ����
     *
     * @param oldName ������
     * @param newName ������.
     * @return �����Ƿ�����һ��.
     */
    public boolean renameTableData(String oldName, String newName) {
        return renameTableData(oldName, newName, true);
    }

    /**
     * ������tabledata
     *
     * @param map ������
     */
    public void renameTableData(Map<String, String> map) {
        if (map.isEmpty()) {
            return;
        }
        Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            renameTableData(key, map.get(key));
        }
    }

    public abstract Widget[] getLinkableWidgets() ;

    public abstract List<WidgetName> getWidgetsName();

    /**
     * �����ı�.
     */
    public abstract void envChanged();

    /**
     * �����ı�.
     */
    public abstract void parameterChanged();

    /**
     * �ؼ����øı�.
     */
    public abstract void widgetConfigChanged();
}
