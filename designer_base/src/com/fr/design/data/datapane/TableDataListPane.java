package com.fr.design.data.datapane;

import com.fr.base.TableData;
import com.fr.data.TableDataSource;
import com.fr.data.impl.storeproc.StoreProcedure;
import com.fr.design.data.DesignTableDataManager;
import com.fr.design.gui.controlpane.JControlPane;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.gui.ilist.ListModelElement;
import com.fr.file.DatasourceManagerProvider;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.general.NameObject;
import com.fr.stable.Nameable;
import com.fr.stable.StringUtils;
import com.fr.stable.core.PropertyChangeAdapter;

import javax.swing.*;
import java.util.*;

/**
 * TableDataList Pane.
 */
public class TableDataListPane extends JControlPane {
    private Map<String, String> dsNameChangedMap = new HashMap<String, String>();
    private boolean isNamePermitted = true;

    public TableDataListPane() {
        super();
        dsNameChangedMap.clear();
        this.addEditingListner(new PropertyChangeAdapter() {
            @Override
            public void propertyChange() {
                isNamePermitted = true;
                TableDataSource source = DesignTableDataManager.getEditingTableDataSource();
                String[] allDSNames = DesignTableDataManager.getAllDSNames(source);
                String[] allListNames = nameableList.getAllNames();
                allListNames[nameableList.getSelectedIndex()] = StringUtils.EMPTY;
                String tempName = getEditingName();
                Object editingType = getEditingType();
                if (StringUtils.isEmpty(tempName)) {
                    String[] warning = new String[]{"NOT_NULL_Des", "Please_Rename"};
                    String[] sign = new String[]{",", "!"};
                    isNamePermitted = false;
                    nameableList.stopEditing();
                    JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(TableDataListPane.this), Inter.getLocText(warning, sign));
                    setWarnigText(editingIndex);
                    return;
                }

                if (!ComparatorUtils.equals(tempName, selectedName)
                        && isNameRepeted(new List[]{Arrays.asList(allDSNames), Arrays.asList(allListNames)}, tempName)) {
                    String[] waning = new String[]{"already_exists", "TableData", "Please_Rename"};
                    String[] sign = new String[]{"", tempName + ",", "!"};
                    isNamePermitted = false;
                    nameableList.stopEditing();
                    JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(TableDataListPane.this), Inter.getLocText(waning, sign));
                    setWarnigText(editingIndex);
                } else if (editingType instanceof StoreProcedure && isIncludeUnderline(tempName)) {
                    String[] datasource_underline = new String[]{"Datasource-Stored_Procedure", "Name", "can_not_include_underline"};
                    String[] sign = new String[]{"", "", "!"};
                    isNamePermitted = false;
                    nameableList.stopEditing();
                    JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(TableDataListPane.this), Inter.getLocText(datasource_underline, sign));
                    setWarnigText(editingIndex);
                }
                if (nameableList.getSelectedValue() instanceof ListModelElement) {
                    Nameable selected = ((ListModelElement) nameableList.getSelectedValue()).wrapper;
                    if (!ComparatorUtils.equals(tempName, selected.getName())) {
                        rename(selected.getName(), tempName);

                    }
                }
            }
        });
    }

    protected void rename(String oldName, String newName) {
        dsNameChangedMap.put(oldName, newName);
    }

    /**
     * 名字是否允许
     *
     * @return 是/否
     */
    public boolean isNamePermitted() {
        return isNamePermitted;
    }


    /**
     * 检查按钮可用状态 Check button enabled.
     */
    public void checkButtonEnabled() {
        super.checkButtonEnabled();
        isNamePermitted = !isContainsRename();
    }

    private boolean isIncludeUnderline(String name) {
        return ComparatorUtils.equals(name.indexOf(StoreProcedure.SPLIT), -1) ? false : true;
    }

    /**
     * 创建服务器数据集所需要的NameableCreator数组
     *
     * @return 数组
     */
    public NameableCreator[] createNameableCreators() {
        return TableDataCreatorProducer.getInstance().createServerTableDataCreator();
    }


    // 只能保证当前模板不重名了

    /**
     * 新建不重名的面板
     *
     * @param prefix 前缀字符
     * @return 生成的名字
     */
    @Override
    public String createUnrepeatedName(String prefix) {
        TableDataSource source = DesignTableDataManager.getEditingTableDataSource();
        if (source == null) {
            return super.createUnrepeatedName(prefix);
        }
        String[] allDsNames = DesignTableDataManager.getAllDSNames(source);
        DefaultListModel model = this.getModel();
        Nameable[] all = new Nameable[model.getSize()];
        for (int i = 0; i < model.size(); i++) {
            all[i] = ((ListModelElement) model.get(i)).wrapper;
        }
        // richer:生成的名字从1开始. kunsnat: 添加属性从0开始.
        int count = all.length + 1;
        while (true) {
            String name_test = prefix + count;
            boolean repeated = false;
            for (int i = 0, len = model.size(); i < len; i++) {
                Nameable nameable = all[i];
                if (ComparatorUtils.equals(nameable.getName(), name_test)) {
                    repeated = true;
                    break;
                }
            }
            for (String dsname : allDsNames) {
                if (ComparatorUtils.equals(dsname, name_test)) {
                    repeated = true;
                    break;
                }
            }

            if (!repeated) {
                return name_test;
            }

            count++;
        }
    }

    @Override
    protected String title4PopupWindow() {
        return "TableData";
    }

    /**
     * Populate.
     */
    public void populate(TableDataSource tds) {
        List<NameObject> nameObjectList = new ArrayList<NameObject>();

        Iterator tableDataNameIterator = tds.getTableDataNameIterator();
        while (tableDataNameIterator.hasNext()) {
            String tableDataName = (String) tableDataNameIterator.next();
            TableData tableData = tds.getTableData(tableDataName);

            if (tableData != null) {
                nameObjectList.add(new NameObject(tableDataName, tableData));
            }
        }

        populate(nameObjectList.toArray(new NameObject[nameObjectList.size()]));
    }

    /**
     * Populate.
     */
    public void populate(DatasourceManagerProvider datasourceManager) {
        Iterator<String> nameIt = datasourceManager.getTableDataNameIterator();
        Iterator<String> procedurenameIt = datasourceManager.getProcedureNameIterator();
        List<NameObject> nameObjectList = new ArrayList<NameObject>();
        while (nameIt.hasNext()) {
            String name = nameIt.next();
            nameObjectList.add(new NameObject(name, datasourceManager.getTableData(name)));
        }
        while (procedurenameIt.hasNext()) {
            String name = procedurenameIt.next();
            nameObjectList.add(new NameObject(name, datasourceManager.getProcedureByName(name)));
        }

        populate(nameObjectList.toArray(new NameObject[nameObjectList.size()]));
    }

    public void update(DatasourceManagerProvider datasourceManager) {
        datasourceManager.clearAllTableData();
        datasourceManager.clearAllProcedure();
        Nameable[] tableDataArray = this.update();
        for (int i = 0; i < tableDataArray.length; i++) {
            NameObject nameObject = (NameObject) tableDataArray[i];
            datasourceManager.putTableData(nameObject.getName(), (TableData) nameObject.getObject());
        }
    }

    public void update(TableDataSource tds) {
        tds.clearAllTableData();

        Nameable[] tableDataArray = this.update();
        for (int i = 0; i < tableDataArray.length; i++) {
            NameObject nameObject = (NameObject) tableDataArray[i];
            tds.putTableData(nameObject.getName(), (TableData) nameObject.getObject());
        }
    }

    /**
     * 判断数据集是否重名
     */
    public void checkValid() throws Exception {
        List<String> exsitTableDataNameList = new ArrayList<String>();
        // _denny: 判断是否有重复的数据集名
        checkRepeatedDSName(exsitTableDataNameList);

        Nameable[] tableDataArray = this.update();
        for (int i = 0; i < tableDataArray.length; i++) {
            NameObject nameObject = (NameObject) tableDataArray[i];

            if (exsitTableDataNameList.contains(nameObject.getName())) {
                String[] waring = new String[]{"TableData", "Error_TableDataNameRepeat"};
                String[] sign = new String[]{": " + nameObject.getName()};
                throw new Exception(Inter.getLocText(waring, sign));
            }

            exsitTableDataNameList.add(nameObject.getName());
        }
    }

    protected void checkRepeatedDSName(List<String> exsitTableDataNameList) {
    }

    /**
     * 在JJControlPane的左侧Tree里面选中某一Item
     *
     * @param name 被选择的Item名称
     */
    public void selectName(String name) {
        this.setSelectedName(name);
    }

    public Map<String, String> getDsNameChangedMap() {
        return this.dsNameChangedMap;
    }
}