package com.fr.poly;

import java.awt.BorderLayout;

import javax.swing.Icon;

import com.fr.design.mainframe.DockingView;
import com.fr.general.Inter;
/**
 * �ۺϱ���ľۺϿ����Ա༭docking
 * @editor zhou
 * @since 2012-3-23����3:41:33
 */
public class PolyBlockProperPane extends DockingView {
	public static PolyBlockProperPane getInstance(PolyDesigner designer) {
		HOLDER.singleton.setEditingPolyDesigner(designer);
		return HOLDER.singleton;
	}

	private static class HOLDER {
		private static PolyBlockProperPane singleton = new PolyBlockProperPane();
	}

	private PolyDesigner designer;
	private PolyBlockProperTable polyBlockProperTable;

	private PolyBlockProperPane() {

		polyBlockProperTable = new PolyBlockProperTable();
		this.setLayout(new BorderLayout());
		this.add(polyBlockProperTable, BorderLayout.CENTER);
	}

	private void setEditingPolyDesigner(PolyDesigner designer) {
		this.designer = designer;
	}

	@Override
	public void refreshDockingView() {
		polyBlockProperTable.populate(designer);
	}

	@Override
	public String getViewTitle() {
		return Inter.getLocText("Form-Widget_Property_Table");
	}

	@Override
	public Icon getViewIcon() {
		return null;
	}

	@Override
	public Location preferredLocation() {
		return Location.WEST_BELOW;
	}

}
