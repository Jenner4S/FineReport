package com.fr.design.mainframe.cell.settingpane;

import com.fr.design.mainframe.AbstractAttrPane;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.FloatSelection;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.elementcase.TemplateElementCase;

/**
 * ��Ԫ�����Ա��ÿ��tab��Ӧ��pane���Ѿ�ʵ���˹��������ܣ����ҳ��װ���£����Զ�����
 * ���㣺���಻��Ҫд���캯���ˣ����е����������createContentPane()�������ɵ�pane����,������Ҫ����border��
 *
 * @author zhou
 * @since 2012-5-11����4:02:18
 */
public abstract class AbstractCellAttrPane extends AbstractAttrPane {
	protected TemplateCellElement cellElement;
	protected ElementCasePane elementCasePane;
	protected CellSelection cs;

	protected abstract void populateBean();

	public abstract void updateBeans();

	public abstract void updateBean(TemplateCellElement cellElement);

	public void populateBean(TemplateCellElement cellElement, ElementCasePane epane) {
		if (epane == null || cellElement == null) {
			return;
		}
		removeAttributeChangeListener();
		this.cellElement = cellElement;
		elementCasePane = epane;
		if (elementCasePane.getSelection() instanceof FloatSelection) {
			return;
		}
		//august����߱��뱣��һ��CellSelection�ľ�����Ϊ��Ԫ�����Ե�ĳЩ��������ʹ��CellSelection�����仯����ôelementCasePane.getSelection()��ȡ�ľͲ���׼ȷ����
		cs = (CellSelection) elementCasePane.getSelection();
		populateBean();
	}

	/**
	 * �ֳ������������������ڣ�����������Ǹ��Ի�����ô���Դ�cellElement����update�� �����ظ�ʹ����� Ϊ�˶Ի�����׼��
	 */
	public void updateBean() {
		updateBean(this.cellElement);
		TemplateElementCase elementCase = elementCasePane.getEditingElementCase();
		elementCase.addCellElement(cellElement);
	}
}
