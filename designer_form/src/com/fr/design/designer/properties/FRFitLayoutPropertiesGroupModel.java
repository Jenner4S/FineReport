/**
 * 
 */
package com.fr.design.designer.properties;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import com.fr.design.beans.GroupModel;
import com.fr.design.mainframe.widget.editors.FitLayoutDirectionEditor;
import com.fr.design.mainframe.widget.editors.IntegerPropertyEditor;
import com.fr.design.mainframe.widget.editors.PropertyCellEditor;
import com.fr.design.designer.creator.XWFitLayout;
import com.fr.form.ui.container.WFitLayout;
import com.fr.general.Inter;

/**
 * ����Ӧ������������Ա�
 * ��ҪΪ���������������ϲ�ˮƽ����ֱ�������ԭ�����ţ�Ϊweb�˱��ֵ�ǰ��ƵĿ�߱�����
 * 
 * @author jim
 * @date 2014-6-24
 */
public class FRFitLayoutPropertiesGroupModel implements GroupModel {
	
	private PropertyCellEditor editor;
	private DefaultTableCellRenderer renderer;
	private FitLayoutDirectionEditor stateEditor;
	private FitStateRenderer stateRenderer;
	private WFitLayout layout;
	private XWFitLayout xfl;
	
	public FRFitLayoutPropertiesGroupModel(XWFitLayout xfl){
		this.xfl = xfl;
		this.layout = xfl.toData();
		renderer = new DefaultTableCellRenderer();
		editor = new PropertyCellEditor(new IntegerPropertyEditor());
	    stateEditor = new FitLayoutDirectionEditor();
	    stateRenderer = new FitStateRenderer();
	}

	/** 
	 * ���ֹ������Լ�������
	 */
	@Override
	public String getGroupName() {
		return Inter.getLocText("FR-Designer-Layout_Adaptive_Layout");
	}

	@Override
	public int getRowCount() {
		return 2;
	}

	@Override
	public TableCellRenderer getRenderer(int row) {
		switch (row) {
	        case 0:
	            return renderer;
	        default:
	            return stateRenderer;
		}
	}

	@Override
	public TableCellEditor getEditor(int row) {
		switch (row) {
	        case 0:
	            return editor;
	        default:
	            return stateEditor;
		}
	}

	@Override
	public Object getValue(int row, int column) {
		if (column == 0) {
            switch (row) {
                case 0:
                    return Inter.getLocText("FR-Designer_Component_Interval");
                default :
                    return Inter.getLocText("FR-Designer_Component_Scale");
            }
        } else {
            switch (row) {
                case 0:
                    return layout.getCompInterval();
                default :
                	return  layout.getCompState();
            }
        }
	}

	@Override
	public boolean setValue(Object value, int row, int column) {
		int state = (Integer) value;
		if (column == 0 || state < 0) {
			return false;
		} else {
			if (row ==0 && xfl.canAddInterval(state)) {
				// ����������Ҫͬ������������������ˢ�º���ʾ����ӦЧ��
				setLayoutGap(state);
				return true;
			}else if (row == 1) {
				layout.setCompState(state);
				return true;
			} 
			return false;
		}
	}
	
	private void setLayoutGap(int value) {
		int  interval = layout.getCompInterval();
    	if (value != interval) {
    		xfl.moveContainerMargin();
    		xfl.moveCompInterval(xfl.getAcualInterval());
    		layout.setCompInterval(value);
    		xfl.addCompInterval(xfl.getAcualInterval());
    	}
	}

	/**
	 * �Ƿ�ɱ༭
	 * @param row ��
	 * @return ��
	 */
	@Override
	public boolean isEditable(int row) {
		return true;
	}

}
