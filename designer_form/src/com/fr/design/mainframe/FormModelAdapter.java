package com.fr.design.mainframe;

import java.util.ArrayList;
import java.util.List;

import com.fr.design.DesignModelAdapter;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.form.main.Form;
import com.fr.form.main.WidgetGatherAdapter;
import com.fr.form.ui.ChartEditorProvider;
import com.fr.form.ui.DataControl;
import com.fr.form.ui.ElementCaseEditor;
import com.fr.form.ui.MultiFileEditor;
import com.fr.form.ui.Widget;
import com.fr.general.ComparatorUtils;
import com.fr.stable.js.WidgetName;

public class FormModelAdapter extends DesignModelAdapter<Form, BaseJForm> {

	public FormModelAdapter(BaseJForm jForm) {
		super(jForm);
	}

    /**
     * �����ı�.
     */
	public void envChanged() {
		WidgetToolBarPane.refresh();
		jTemplate.refreshAllNameWidgets();
	}

    /**
     * �����ı�.
     */
	public void parameterChanged() {
	//ʵʱ���²���
      jTemplate.populateParameter();
	}

    /**
     * �ؼ����øı�.
     */
	public void widgetConfigChanged() {
		WidgetToolBarPane.refresh();
		jTemplate.refreshAllNameWidgets();
	}

    /**
     * ������TableData���һЩ����
     *
     * @param oldName ������
     * @param newName ������.
     * @return �����Ƿ�����һ��.
     */
	public boolean renameTableData(String oldName, String newName) {
		if (super.renameTableData(oldName, newName)) {
			jTemplate.refreshSelectedWidget();
			return true;
		}
		return false;
	}

	@Override
	public List<WidgetName> getWidgetsName() {
		final List<WidgetName> list = new ArrayList<WidgetName>();
		Form.traversalFormWidget(this.getBook().getContainer(), new WidgetGatherAdapter() {

			@Override
			public void dealWith(Widget widget) {
				if (widget instanceof DataControl || widget instanceof MultiFileEditor) {
					list.add(new WidgetName(widget.getWidgetName()));
				}
			}
		});
		return list;
	}
	
	/**
	 * ��ȡ���Ա������Ķ���,�������е����пؼ�
	 */
	public Widget[] getLinkableWidgets() {
		final ArrayList<Widget> linkAbleList = new ArrayList<Widget>();
        final JForm currentJForm = ((JForm) HistoryTemplateListPane.getInstance().getCurrentEditingTemplate());
		Form.traversalWidget(currentJForm.getRootLayout(), new WidgetGatherAdapter() {

			@Override
			public boolean dealWithAllCards() {
				return true;
			}
			
			public void dealWith(Widget widget) {
                boolean isSupportAsHypelink = widget.acceptType(ElementCaseEditor.class) || widget.acceptType(ChartEditorProvider.class);
                //���Գ����Ķ��󲻰�������; Ŀǰֻ��ͼ��ͱ�������
                // bug66182 ɾ��������!ComparatorUtils.equals(editingECName, widget.getWidgetName())  �õ�ǰ���������ѡ���Լ�
                 if (isSupportAsHypelink){
			    	linkAbleList.add( widget);
                }
			}
		}, Widget.class);
		
		return linkAbleList.toArray(new Widget[linkAbleList.size()]);
	}
}
