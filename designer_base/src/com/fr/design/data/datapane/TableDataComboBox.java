package com.fr.design.data.datapane;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fr.design.constants.UIConstants;
import com.fr.design.data.DesignTableDataManager;
import com.fr.data.TableDataSource;
import com.fr.design.data.tabledata.Prepare4DataSourceChange;
import com.fr.design.data.tabledata.wrapper.TemplateTableDataWrapper;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.icombobox.UIComboBoxRenderer;
import com.fr.general.ComparatorUtils;
import com.fr.stable.StringUtils;

/**
 * �����������ݼ���������
 * 
 * @author zhou
 * @since 2012-4-20����10:34:30
 */
public class TableDataComboBox extends UIComboBox implements Prepare4DataSourceChange {
    protected java.util.Map<String, TableDataWrapper> resMap;
    private java.util.Map<String, TableDataWrapper> dsMap;
    private static final long serialVersionUID = 1L;
	private boolean refresModel = false;
    private String treeName; //�����ݼ����������

    public TableDataComboBox(TableDataSource source){
        this(source,StringUtils.EMPTY);
    }
	public TableDataComboBox(TableDataSource source, String treeName) {
		super();
        this.treeName = treeName;
		this.setRenderer(new UIComboBoxRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				JLabel renderer = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof TableDataWrapper) {
					TableDataWrapper tabledatawrappe = (TableDataWrapper)value;
					renderer.setIcon(tabledatawrappe.getIcon());
					renderer.setText(tabledatawrappe.getTableDataName());
					renderer.setToolTipText(tabledatawrappe.getTableDataName());
				} else {
					renderer.setIcon(null);
					renderer.setText(StringUtils.EMPTY);
				}
				return renderer;
			}
		});
		refresh(source);
        registerDSChangeListener();
	}

    /**
     * refresh ComboBox
     * @param source
     */
	public void refresh(TableDataSource source) {
		TableDataWrapper dataWrapper = getSelectedItem();
		refresModel = true;
		setResMap(source);
        setDsMap();
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		this.setModel(model);
		model.addElement(UIConstants.PENDING);
		Iterator<Entry<String, TableDataWrapper>> entryIt = dsMap.entrySet().iterator();
		while (entryIt.hasNext()) {
            TableDataWrapper tableDataWrapper = entryIt.next().getValue();
            if (!ComparatorUtils.equals(tableDataWrapper.getTableDataName(), treeName)) {
                model.addElement(tableDataWrapper);
            }
		}
        if (dataWrapper != null) {
            if (DesignTableDataManager.isDsNameChanged(dataWrapper.getTableDataName())) {
                this.setSelectedTableDataByName(DesignTableDataManager.getChangedDsNameByOldDsName(dataWrapper.getTableDataName()));
            } else {
                this.getModel().setSelectedItem(dataWrapper);
            }
        }
		refresModel = false;
	}

    protected void setResMap(TableDataSource source) {
        this.resMap = DesignTableDataManager.getAllEditingDataSet(source);
    }

    private void setDsMap() {
        dsMap = DesignTableDataManager.getAllDataSetIncludingProcedure(resMap);
    }



    /**
     * ��resMap�����TableData��Ϣ
     * @param name ���ݼ�����
     * @param templateTableDataWrappe ���ݼ�
     */
	public void putTableDataIntoMap(String name, TemplateTableDataWrapper templateTableDataWrappe) {
		if (dsMap.containsKey(name)) {
			return;
		}
		this.addItem(templateTableDataWrappe);
		dsMap.put(name, templateTableDataWrappe);
	}

	public void setSelectedTableDataByName(String name) {
		TableDataWrapper tableDataWrappe = dsMap.get(name) == null? dsMap.get(name + "_P_CURSOR") : dsMap.get(name);
		this.getModel().setSelectedItem(tableDataWrappe);
	}

	@Override
	public TableDataWrapper getSelectedItem() {
		if (dataModel.getSelectedItem() instanceof TableDataWrapper) {
			return (TableDataWrapper)dataModel.getSelectedItem();
		}
		return null;
	}

	//august:addElement������Ȼ��fireItemStateChanged������
	@Override
	protected void fireItemStateChanged(ItemEvent e) {
		if (!refresModel) {
			super.fireItemStateChanged(e);
		}
	}

    /**
     *ע��listener,��Ӧ���ݼ��ı�
     */
    public void registerDSChangeListener() {
        DesignTableDataManager.addDsChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                TableDataComboBox.this.refresh(DesignTableDataManager.getEditingTableDataSource());
            }
        });
    }
}
