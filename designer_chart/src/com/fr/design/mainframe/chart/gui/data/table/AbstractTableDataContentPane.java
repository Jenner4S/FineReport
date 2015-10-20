package com.fr.design.mainframe.chart.gui.data.table;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.icombobox.UIComboBox;

public abstract class AbstractTableDataContentPane extends BasicBeanPane<ChartCollection>{

    private boolean isNeedSummaryCaculateMethod = true;
	
	public abstract void updateBean(ChartCollection ob);
	
	/**
	 * ���box�Ƿ� ��Ҫʹ�� ����: ����ͼ, �ڷ�����δѡ��ʱ, ����ѡ��ʹ�� ϵ���� ϵ��ֵ "ϵ����ʹ��"
     * @param hasUse �Ƿ�ʹ��
	 */
	public void checkBoxUse(boolean hasUse) {
		
	}
	
	/**
	 * ���ݼ����ʱ, ˢ��box
     * @param dataWrapper ��ά��
	 */
	public void onSelectTableData(TableDataWrapper dataWrapper) {
		List<String> columnNameList = dataWrapper.calculateColumnNameList();
		refreshBoxListWithSelectTableData(columnNameList);
	}

    /**
     * ������е�box����
     */
    public abstract void clearAllBoxList();

	protected abstract void refreshBoxListWithSelectTableData(List columnNameList);
	
	/**
	 * ˢ��Box��ѡ��.
	 */
	protected void refreshBoxItems(UIComboBox box, List list) {
		if(box == null) {
			return;
		}
		
		Object ob = box.getSelectedItem();
		box.removeAllItems();
		
		int length = list.size();
		for(int i = 0; i < length; i++) {
			box.addItem(list.get(i));
		}
		
		box.getModel().setSelectedItem(ob);
	}

    /**
     * ���box�����ж���
     * @param box ����
     */
    protected void clearBoxItems(UIComboBox box){
        if(box == null){
            return;
        }
        box.removeAllItems();
    }
	
	protected ItemListener tooltipListener = new ItemListener() {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if(e.getSource() instanceof UIComboBox) {
				UIComboBox box = (UIComboBox)e.getSource();
				if(box.getSelectedItem() != null) {
					box.setToolTipText(box.getSelectedItem().toString());
				} else {
					box.setToolTipText(null);
				}
			}
		}
    };
	
	/**
	 *  ������ǰ �Զ���������ֶ�
	 */
	protected void combineCustomEditValue(UIComboBox comBox, String value) {
		if(comBox != null) {
			comBox.setEditable(true);
			comBox.setSelectedItem(value);
			comBox.setEditable(false);
		}
	}

	@Override
	protected String title4PopupWindow() {
		return "";
	}
	
	@Override
	public void populateBean(ChartCollection collection) {
		if(collection == null) {
			return;
		}
	}

    /**
     * ����layout�������
     */
    public void redoLayoutPane(){

    }
	
	@Override
	public ChartCollection updateBean() {
		return null;
	}

    /**
     * �����Ƿ���Ҫ���ܷ�ʽ
     * @param isNeedSummaryCaculateMethod �Ƿ���Ҫ����
     */
    public void setNeedSummaryCaculateMethod(boolean isNeedSummaryCaculateMethod){
        this.isNeedSummaryCaculateMethod = isNeedSummaryCaculateMethod;
    }

    /**
     * �����Ƿ���Ҫ���ܷ�ʽ
     * @return �Ƿ���Ҫ���ܷ�ʽ
     */
    public boolean isNeedSummaryCaculateMethod(){
        return this.isNeedSummaryCaculateMethod;
    }
}
