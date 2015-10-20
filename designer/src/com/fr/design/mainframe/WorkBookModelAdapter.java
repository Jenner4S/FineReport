package com.fr.design.mainframe;

import com.fr.base.Parameter;
import com.fr.base.TableData;
import com.fr.data.TableDataSource;
import com.fr.design.DesignModelAdapter;
import com.fr.design.bridge.DesignToolbarProvider;
import com.fr.form.ui.Widget;
import com.fr.main.impl.WorkBook;
import com.fr.main.parameter.ReportParameterAttr;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.report.cell.FloatElement;
import com.fr.report.elementcase.ElementCase;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.report.report.Report;
import com.fr.script.Calculator;
import com.fr.stable.ParameterProvider;
import com.fr.stable.StringUtils;
import com.fr.stable.bridge.StableFactory;
import com.fr.stable.js.WidgetName;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author zhou
 * @since 2012-7-26����2:03:12
 */
public class WorkBookModelAdapter extends DesignModelAdapter<WorkBook, JWorkBook> {

	public WorkBookModelAdapter(JWorkBook jworkbook) {
		super(jworkbook);
	}

	@Override
	public Parameter[] getParameters() {
		return this.getBook().getParameters();
	}

	// �������
	@Override
	public Parameter[] getReportParameters() {
		ReportParameterAttr rpa = this.getBook().getReportParameterAttr();
		return rpa == null ? new Parameter[0] : rpa.getParameters();
	}

	// ����Դ����
	@Override
	public Parameter[] getTableDataParameters() {
		TableDataSource source = this.getBook();
		Calculator c = Calculator.createCalculator();
		c.setAttribute(TableDataSource.class, source);
		java.util.List<ParameterProvider> list = new java.util.ArrayList<ParameterProvider>();
		java.util.Iterator<String> nameIt = this.getBook().getTableDataNameIterator();
		while (nameIt.hasNext()) {
			TableData td = source.getTableData(nameIt.next());
			if (td.getParameters(c) != null) {
				list.addAll(java.util.Arrays.asList(td.getParameters(c)));
			}
		}
		return list.toArray(new Parameter[list.size()]);
	}
	
	/**
	 * ������TableData���һЩ����
	 * @param oldName ������
	 * @param newName ������
	 * @return �����Ƿ�ˢ��.
	 */
	public boolean renameTableData(String oldName, String newName) {
		if (super.renameTableData(oldName, newName)) {
			if (this.getBook().getTableData(oldName) == null) {
				jTemplate.refreshParameterPane4TableData(oldName,newName);
			}
			return true;
		}
		return false;
	}

	/**
	 * �����ı�.
	 */
	public void envChanged() {
		DesignToolbarProvider provider = StableFactory.getMarkedObject(DesignToolbarProvider.STRING_MARKED, DesignToolbarProvider.class);
		if (provider != null) {
			provider.refreshToolbar();
		}
		jTemplate.refreshAllNameWidgets();
	}

	/**
	 * �����ı�.
	 */
	public void parameterChanged() {
		jTemplate.updateReportParameterAttr();
		jTemplate.populateReportParameterAttr();
	}
	
	/**
	 * �ؼ��ı�.
	 */
	public void widgetConfigChanged() {
		DesignToolbarProvider provider = StableFactory.getMarkedObject(DesignToolbarProvider.STRING_MARKED, DesignToolbarProvider.class);
		if (provider != null) {
			provider.refreshToolbar();
		}
		jTemplate.refreshAllNameWidgets();
	}

	/**
	 * ���ؿؼ�������
	 * @return widgetName �ؼ��б�.
	 */
	public List<WidgetName> getWidgetsName() {
		List<WidgetName> list = new ArrayList<WidgetName>();
		WorkBook wb = this.getBook();
		for (int i = 0, len = wb.getReportCount(); i < len; i++) {
			Report report = wb.getReport(i);
			Iterator it = report.iteratorOfElementCase();
			while (it.hasNext()) {
				ElementCase ec = (ElementCase)it.next();
				Iterator cs = ec.cellIterator();
				while (cs.hasNext()) {
					CellElement ce = (CellElement)cs.next();
					if (ce instanceof DefaultTemplateCellElement) {
						Widget widget = ((DefaultTemplateCellElement)ce).getWidget();
						if (widget != null && StringUtils.isNotEmpty(widget.getWidgetName())) {
							list.add(new WidgetName(widget.getWidgetName()));
						}
					}
				}
			}
		}
		return list;
	}

	/**
	 * ��������Ԫ�ص���������
	 * @return ��������.
	 */
	public String[] getFloatNames() {
		TemplateElementCase elementCase = jTemplate.getEditingElementCase();

		List<String> nameList = new ArrayList<String>();
		Iterator<FloatElement> it = elementCase.floatIterator();
		while (it.hasNext()) {
			nameList.add(it.next().getName());
		}
		return nameList.toArray(new String[nameList.size()]);
	}


    public Widget[] getLinkableWidgets() {
        return new Widget[0];
    }
}
