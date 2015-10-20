package com.fr.design.actions.utils;

import com.fr.base.Style;
import com.fr.design.actions.cell.style.StyleActionInterface;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.FloatSelection;
import com.fr.grid.selection.Selection;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.report.cell.FloatElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.elementcase.ElementCase;
import com.fr.report.elementcase.TemplateElementCase;

import java.awt.*;

public class ReportActionUtils {
	private ReportActionUtils() {
	}


	public static interface IterAction {
		public void dealWith(CellElement editCellElement);
	}

	public static boolean executeAction(
			final StyleActionInterface styleActionInterface, ElementCasePane reportPane) {

		Selection sel = reportPane.getSelection();
		if (sel instanceof FloatSelection) {
			ElementCase report = reportPane.getEditingElementCase();
			FloatElement selectedFloatElement =
					report.getFloatElement(((FloatSelection) sel).getSelectedFloatName());

			Style selectedStyle = selectedFloatElement.getStyle();
			selectedFloatElement.setStyle(
					styleActionInterface.executeStyle(selectedStyle, selectedStyle));
		} else {
			TemplateElementCase report = reportPane.getEditingElementCase();
			TemplateCellElement editCellElement = report.getTemplateCellElement(((CellSelection) sel).getColumn(), ((CellSelection) sel).getRow());
			if (editCellElement == null) {
				editCellElement = new DefaultTemplateCellElement(((CellSelection) sel).getColumn(), ((CellSelection) sel).getRow());
				report.addCellElement(editCellElement);
			}

			final Style selectedStyle = editCellElement.getStyle();

			actionIterateWithCellSelection((CellSelection) sel, report, new IterAction() {
				@Override
				public void dealWith(CellElement editCellElement) {
					Style style2Mod = editCellElement.getStyle();
					editCellElement.setStyle(
							styleActionInterface.executeStyle(style2Mod, selectedStyle));

				}
			});
		}

		return true;
	}

	public static void actionIterateWithCellSelection(
			CellSelection gridSelection, TemplateElementCase report, IterAction action) {
		//��Ҫ���к��е�������Ԫ�ء�
		// �����ѭ�����Ա�֤���һ���޸ı�׼��Ԫ��originalStyle����
		int cellRectangleCount = gridSelection.getCellRectangleCount();
		for (int rec = 0; rec < cellRectangleCount; rec++) {
			Rectangle cellRectangle = gridSelection.getCellRectangle(rec);
			// �����ѭ�����Ա�֤���һ���޸ı�׼��Ԫ��originalStyle����
			for (int j = cellRectangle.height - 1; j >= 0; j--) {
				for (int i = cellRectangle.width - 1; i >= 0; i--) {
					int column = i + cellRectangle.x;
					int row = j + cellRectangle.y;

					TemplateCellElement editCellElement = report.getTemplateCellElement(column, row);
					if (editCellElement == null) {
						editCellElement = new DefaultTemplateCellElement(column, row);
						report.addCellElement(editCellElement);
					} else {
						// ���ںϲ��ĸ���,���ǲ���μ����Style.
						if (editCellElement.getColumn() != column
								|| editCellElement.getRow() != row) {
							continue;
						}
					}

					action.dealWith(editCellElement);
				}
			}
		}
	}

	/**
	 * peter:��ֻ���ķ���Ȼ���õ�ǰ��Style.
	 *
	 * @param reportPane the current rpt pane.
	 * @return current style.
	 */
	public static Style getCurrentStyle(ElementCasePane reportPane) {
		//got simple cell element from row and column
		ElementCase report = reportPane.getEditingElementCase();

		Selection sel = reportPane.getSelection();
		if (sel instanceof FloatSelection) {
			FloatElement selectedFloatElement = report.getFloatElement(((FloatSelection) sel).getSelectedFloatName());
			return selectedFloatElement.getStyle();
		}

		//��Ҫ���к��е�������Ԫ�ء�
		//vivian: ��excel�����ı�style.��״̬�ĸı��ɵ�һ����״̬�����������ֻѡ���˵�һ��ֵ��ô�͸�Ϊ�෴��ֵ��
		// �����ѡ���˶��ֵ�����ȱ�ɺ͵�һ��ֵ��һ��

		CellElement editCellElement = report.getCellElement(((CellSelection) sel).getColumn(), ((CellSelection) sel).getRow());
		if (editCellElement == null) {
			return Style.DEFAULT_STYLE;
		}

		//peter:ֱ�ӷ��ص�ǰ�༭Ԫ�ص�Style
		return editCellElement.getStyle();
	}
}
