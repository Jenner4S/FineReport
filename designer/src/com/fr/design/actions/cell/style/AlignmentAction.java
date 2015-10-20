package com.fr.design.actions.cell.style;

import javax.swing.Icon;

import com.fr.base.BaseUtils;
import com.fr.base.Style;
import com.fr.base.chart.BaseChartCollection;
import com.fr.design.constants.UIConstants;
import com.fr.design.actions.ButtonGroupAction;
import com.fr.design.actions.utils.ReportActionUtils;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.general.Inter;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.selection.FloatSelection;
import com.fr.grid.selection.Selection;
import com.fr.report.cell.FloatElement;
import com.fr.report.elementcase.TemplateElementCase;


public class AlignmentAction extends ButtonGroupAction implements StyleActionInterface {

	public AlignmentAction(ElementCasePane t, Icon[] iconArray,
						   Integer[] valueArray) {
		super(t, iconArray, valueArray);
	}


	/**
	 * executeStyle
	 *
	 * @param style
	 * @param selectedStyle
	 * @return style
	 */
	public Style executeStyle(Style style, Style selectedStyle) {
		return style.deriveHorizontalAlignment(getSelectedValue());
	}

	/**
	 * ����Style
	 *
	 * @param style style
	 */
	public void updateStyle(Style style) {
		setSelectedIndex(BaseUtils.getAlignment4Horizontal(style));
	}

	/**
	 * executeActionReturnUndoRecordNeeded
	 *
	 * @return
	 */
	public boolean executeActionReturnUndoRecordNeeded() {
		ElementCasePane reportPane = this.getEditingComponent();
		if (reportPane == null) {
			return false;
		}

		return ReportActionUtils.executeAction(this, reportPane);
	}

	/**
	 * update
	 */
	public void update() {
		super.update();

		//peter:�����ǰû��ReportFrame,����Ҫ����.
		if (!this.isEnabled()) {
			return;
		}

		//got simple cell element from row and column
		ElementCasePane reportPane = this.getEditingComponent();
		if (reportPane == null) {
			this.setEnabled(false);
			return;
		}
		Selection cs = reportPane.getSelection();
		TemplateElementCase tplEC = reportPane.getEditingElementCase();

		if (tplEC != null && cs instanceof FloatSelection) {
			FloatElement selectedFloat = tplEC.getFloatElement(((FloatSelection) cs).getSelectedFloatName());
			Object value = selectedFloat.getValue();
			if (value instanceof BaseChartCollection) {
				this.setEnabled(false);
				return;
			}
		}
		this.updateStyle(ReportActionUtils.getCurrentStyle(reportPane));
	}

	/**
	 * ������������������ʾ
	 *
	 * @return
	 */
	public UIButtonGroup<Integer> createToolBarComponent() {
		UIButtonGroup<Integer> group = super.createToolBarComponent();
		if (group != null) {
			group.setForToolBarButtonGroup(true);
			group.setAllToolTips(new String[]{Inter.getLocText("StyleAlignment-Left"), Inter.getLocText("Center"), Inter.getLocText("StyleAlignment-Right")});
		}
		for (int i = 0; i < 3; i++) {
			group.getButton(i).setRoundBorder(true, UIConstants.ARC);
			group.getButton(i).setBorderPainted(true);
		}
		return group;
	}

}
