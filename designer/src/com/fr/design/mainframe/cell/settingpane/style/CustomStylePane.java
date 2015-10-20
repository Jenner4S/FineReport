package com.fr.design.mainframe.cell.settingpane.style;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ChangeListener;

import com.fr.base.CellBorderStyle;
import com.fr.base.NameStyle;
import com.fr.base.Style;
import com.fr.design.actions.utils.ReportActionUtils;
import com.fr.design.gui.ibutton.FiveButtonLayout;
import com.fr.design.gui.style.AbstractBasicStylePane;
import com.fr.design.gui.style.AlignmentPane;
import com.fr.design.gui.style.BackgroundPane;
import com.fr.design.gui.style.BorderPane;
import com.fr.design.gui.style.FRFontPane;
import com.fr.design.gui.style.FormatPane;
import com.fr.design.style.BorderUtils;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.MultiTabPane;
import com.fr.general.Inter;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.stable.Constants;


/**
 * �������ӵ�ԭ��ͼ���¸��ӵĻ�����������ԸҲ
 *
 * @author zhou
 * @since 2012-5-24����10:36:10
 */
public class CustomStylePane extends MultiTabPane<Style> {
	private static final int LENGTH_FOUR = 4;
	private static final int THREE_INDEX = 3;
	private String[] NameArray;
	private ElementCasePane reportPane;
	private BackgroundPane backgroundPane = null;


	public CustomStylePane() {
		super();
		tabPane.setOneLineTab(false);
		tabPane.setLayout(new FiveButtonLayout());
	}

	/**
	 * @return
	 */
	public String title4PopupWindow() {
		return Inter.getLocText(new String[]{"Custom", "Style"});
	}

	/**
	 * for ScrollBar
	 *
	 * @param l
	 */
	public void addTabChangeListener(ChangeListener l) {
		if (tabPane != null) {
			tabPane.addChangeListener(l);
		}
		if (backgroundPane != null) {
			backgroundPane.addChangeListener(l);
		}
	}

	/**
	 *
	 */
	public void reset() {
		populateBean(null);
	}

	@Override
	/**
	 *
	 */
	public void populateBean(Style ob) {
		for (int i = 0; i < paneList.size(); i++) {
			((AbstractBasicStylePane) paneList.get(i)).populateBean(ob);
		}
	}

	@Override
	/**
	 *
	 */
	public Style updateBean() {
		return updateStyle(ReportActionUtils.getCurrentStyle(reportPane));
	}

	/**
	 * @param style
	 * @return
	 */
	public Style updateStyle(Style style) {
		return ((AbstractBasicStylePane) paneList.get(tabPane.getSelectedIndex())).update(style);//ֻ���µ�ǰѡ��������ʽ
	}


	public boolean isBorderPaneSelected() {
		return tabPane.getSelectedIndex() == THREE_INDEX ;
	}

	/**
	 * @param ePane
	 */
	public void dealWithBorder(ElementCasePane ePane) {
		this.reportPane = ePane;
		Object[] fourObjectArray = BorderUtils.createCellBorderObject(reportPane);

		if (fourObjectArray != null && fourObjectArray.length % LENGTH_FOUR == 0) {
			if (fourObjectArray.length == LENGTH_FOUR) {
				((BorderPane) paneList.get(THREE_INDEX)).populateBean((CellBorderStyle) fourObjectArray[0], ((Boolean) fourObjectArray[1]).booleanValue(), ((Integer) fourObjectArray[2]).intValue(),
						(Color) fourObjectArray[THREE_INDEX]);
			} else {
				((BorderPane) paneList.get(THREE_INDEX)).populateBean(new CellBorderStyle(), Boolean.TRUE, Constants.LINE_NONE,
						(Color) fourObjectArray[THREE_INDEX]);
			}
		}

	}

	/**
	 *
	 */
	public void updateBorder() {
		BorderUtils.update(reportPane, ((BorderPane) paneList.get(THREE_INDEX)).update());
	}

	/**
	 * @param ob
	 * @return
	 */
	public boolean accept(Object ob) {
		return ob instanceof Style && !(ob instanceof NameStyle);
	}

	@Override
	protected List<BasicPane> initPaneList() {
		paneList = new ArrayList<BasicPane>();
		paneList.add(new FormatPane());
		paneList.add(new AlignmentPane());
		paneList.add(new FRFontPane());
		paneList.add(new BorderPane());
		paneList.add(backgroundPane = new BackgroundPane());
		return paneList;
	}

	@Override
	/**
	 *
	 */
	public void updateBean(Style ob) {

	}


}
