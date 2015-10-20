package com.fr.design.present;

import java.util.ArrayList;
import java.util.List;

import com.fr.base.FRContext;
import com.fr.general.NameObject;
import com.fr.design.condition.HighLightConditionAttributesPane;
import com.fr.design.gui.controlpane.JControlPane;
import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.general.Inter;
import com.fr.report.cell.cellattr.highlight.DefaultHighlight;
import com.fr.report.cell.cellattr.highlight.Highlight;
import com.fr.report.cell.cellattr.highlight.HighlightGroup;
import com.fr.stable.Nameable;

public class ConditionAttributesGroupPane extends JControlPane {

	@Override
	public NameableCreator[] createNameableCreators() {
		return new NameableCreator[] { new NameObjectCreator(Inter.getLocText("Condition_Attributes"), DefaultHighlight.class, HighLightConditionAttributesPane.class) };
	}

	@Override
	public String title4PopupWindow() {
		return Inter.getLocText("Condition_Attributes");
	}

	/**
	 * Populate
	 */
	public void populate(HighlightGroup highlightGroup) {
		// marks����������ǰ�棬�����Ƿ��и������鶼���Բ���
		if (highlightGroup == null || highlightGroup.size() <= 0) {
			return;
		}
		List<NameObject> nameObjectList = new ArrayList<NameObject>();

		for (int i = 0; i < highlightGroup.size(); i++) {
			nameObjectList.add(new NameObject(highlightGroup.getHighlight(i).getName(), highlightGroup.getHighlight(i)));
		}

		this.populate(nameObjectList.toArray(new NameObject[nameObjectList.size()]));
	}

	/**
	 * Update.
	 */
	public HighlightGroup updateHighlightGroup() {
		Nameable[] res = this.update();
		Highlight[] res_array = new Highlight[res.length];
		for (int i = 0; i < res.length; i++) {
			// carl:update��������һ�������ڿ����ʱ����Ҫclone
			Highlight highlight = (Highlight)((NameObject)res[i]).getObject();
			highlight.setName(((NameObject)res[i]).getName());
			try {
				highlight = (Highlight)highlight.clone();
			} catch (CloneNotSupportedException e) {
				FRContext.getLogger().error(e.getMessage(), e);
			}
			res_array[i] = highlight;
		}

		return new HighlightGroup(res_array);
	}
}
