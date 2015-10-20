/**
 * 
 */
package com.fr.design.report.share;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fr.data.TableDataSource;
import com.fr.data.impl.EmbeddedTableData;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.controlpane.JControlPane;
import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.gui.controlpane.ShortCut4JControlPane;
import com.fr.design.mainframe.JTemplate;
import com.fr.general.Inter;
import com.fr.general.NameObject;

/**
 * @author neil
 *
 * @date: 2015-3-9-����9:10:20
 */
public class ConfusionManagerPane extends JControlPane {

	/**
	 * ��ȡ��ǰ���ı༭����
	 * 
	 * @return ���ݻ����༭����
	 * 
	 */
	public NameableCreator[] createNameableCreators() {
		NameableCreator local = new NameObjectCreator(Inter.getLocText("FR-Engine_DS-TableData"),
				"/com/fr/design/images/data/dock/serverdatatable.png", ConfusionInfo.class,
				ConfusionTableDataPane.class);

		return new NameableCreator[] { local };
	}

	@Override
	protected String title4PopupWindow() {
		return Inter.getLocText("FR-Designer_Data-confusion");
	}

	/**
	 * ��ȡģ�����������ݼ�, ��չʾ����ҳ��, ���û���������ݼ�, �򷵻�false
	 * 
	 * @return �Ƿ�����������ݼ�
	 * 
	 */
	public boolean populateTabledataManager() {
		List<NameObject> nameList = initNameObjectList();
		if (nameList.isEmpty()) {
			return false;
		}

		this.populate(nameList.toArray(new NameObject[nameList.size()]));
		return true;
	}

	private List<NameObject> initNameObjectList() {
		JTemplate<?, ?> jt = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
		TableDataSource workbook = jt.getTarget();
		Iterator<String> it = workbook.getTableDataNameIterator();
		List<NameObject> nameList = new ArrayList<NameObject>();
		while (it.hasNext()) {
			String tabledataName = it.next();
			EmbeddedTableData tabledata = (EmbeddedTableData) workbook.getTableData(tabledataName);
			ConfusionInfo info = new ConfusionInfo(tabledata, tabledataName);
			nameList.add(new NameObject(tabledataName, info));
		}

		return nameList;
	}

	@Override
	protected ShortCut4JControlPane[] createShortcuts() {
		return new ShortCut4JControlPane[] { moveUpItemShortCut(), moveDownItemShortCut(), sortItemShortCut() };
	}

}