package com.fr.design.gui.xtable;

import com.fr.base.FRContext;
import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.creator.CRPropertyDescriptor;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.widget.editors.ExtendedPropertyEditor;
import com.fr.design.mainframe.widget.editors.PropertyCellEditor;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.report.stable.FormConstants;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class PropertyGroupModel extends AbstractPropertyGroupModel {
	
	private FormDesigner designer;

    public PropertyGroupModel(String name, XCreator creator, CRPropertyDescriptor[] propArray,
			FormDesigner designer) {
		super(name, creator, propArray);
		this.designer = designer;
	}

	@Override
    public Object getValue(int row, int column) {
        if (column == 0) {
            return properties[row].getDisplayName();
        }
        try {
            Method m = properties[row].getReadMethod();
            return m.invoke(dealCreatorData());
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public boolean setValue(Object value, int row, int column) {
        if (column == 0) {
            return false;
        }

        try {
            Method m = properties[row].getWriteMethod();
            m.invoke(dealCreatorData(), value);
        	//��������Ϊ�ؼ���ʱ������������
            if(ComparatorUtils.equals(FormConstants.NAME, properties[row].getName())){
            	creator.resetCreatorName(value.toString());
            }
            properties[row].firePropertyChanged();
            return true;
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * �����Ƿ�ɱ༭
     * @param row ��
     * @return ��row�пɱ༭����true�����򷵻�false
     */
    @Override
    public boolean isEditable(int row) {
        return properties[row].getWriteMethod() != null;
    }

    /**
     * �����������ķ���,��ͨ���Է�Ϊ�������Ժ�����,�¼����Ը����¼����Ʋ�ͬ���з���
     * @return ������������
     */
    @Override
    public String getGroupName() {
        return Inter.getLocText(groupName);
    }

    /**
     * �Ƚ�
     * @param gm ������
     * @return ���رȽϽ��
     */
    @Override
    public int compareTo(AbstractPropertyGroupModel gm) {
        int firstIndex = PROPERTIES.indexOf(groupName);
        int lastIndex = PROPERTIES.indexOf(gm.getGroupName());
        if (firstIndex < lastIndex) {
            return -1;
        } else if (firstIndex == lastIndex) {
            return 0;
        } else {
            return 1;
        }
    }
    private static ArrayList<String> PROPERTIES = new ArrayList<String>();

    static {
        PROPERTIES.add("Properties");
        PROPERTIES.add("Others");
    }
    
    /**
     * �ؼ����Ը�ֵ��ȡֵʱ�����scale��title���´���
     * @return
     */
    private Object dealCreatorData() {
    	return creator.getPropertyDescriptorCreator().toData();
    }
    
	@Override
	protected void initEditor(final int row) throws Exception {
		ExtendedPropertyEditor editor = (ExtendedPropertyEditor) properties[row].createPropertyEditor(dealCreatorData());
		if (editor == null) {
			Class propType = properties[row].getPropertyType();
			editor = TableUtils.getPropertyEditorClass(propType).newInstance();
		}
		if (editor != null) {
			final ExtendedPropertyEditor extendEditor = editor;
			editors[row] = new PropertyCellEditor(editor);
			extendEditor.addPropertyChangeListener(new PropertyChangeListener() {

				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					if(ComparatorUtils.equals(extendEditor.getValue(),getValue(row,1))) {
						return;
					}
					if (extendEditor.refreshInTime()) {
						editors[row].stopCellEditing();
					} else {
						setValue(extendEditor.getValue(), row, 1);
						if ("widgetName".equals(properties[row].getName())) {
							designer.getEditListenerTable().fireCreatorModified(creator, DesignerEvent.CREATOR_RENAMED);
						} else {
							designer.fireTargetModified();
						}
						designer.refreshDesignerUI();
					}
				}
			});
		}
	}
}
