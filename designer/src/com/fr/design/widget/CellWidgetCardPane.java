package com.fr.design.widget;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.frpane.UITabbedPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.widget.ui.BasicWidgetPropertySettingPane;
import com.fr.design.dialog.BasicPane;
import com.fr.form.event.Listener;
import com.fr.form.ui.NoneWidget;
import com.fr.form.ui.Widget;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

/*
 * carl :����Ū����
 */
public class CellWidgetCardPane extends BasicPane {
	//	��ǰ�ı༭�����Զ������
	private BasicBeanPane<? extends Widget> currentEditorDefinePane;

	private JTabbedPane tabbedPane;

	private BasicWidgetPropertySettingPane widgetPropertyPane;
	private JPanel attriPane;
	private JPanel cardPane;
	private CardLayout card;

	private JPanel presPane;
	private JPanel cardPaneForPresent;
	private CardLayout cardForPresent;

	private JPanel cardPaneForTreeSetting;

	private JPanel formPane;
	private WidgetEventPane eventTabPane;

	public CellWidgetCardPane(ElementCasePane pane) {
		this.initComponents(pane);
	}

	private void initComponents(ElementCasePane pane) {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		tabbedPane = new UITabbedPane();
		this.add(tabbedPane, BorderLayout.CENTER);
		attriPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		formPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		eventTabPane = new WidgetEventPane(pane);
		formPane.add(eventTabPane, BorderLayout.CENTER);
		tabbedPane.add(Inter.getLocText("Attribute"), attriPane);
		tabbedPane.add(Inter.getLocText("Form-Editing_Listeners"), formPane);

		presPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		cardPaneForPresent = FRGUIPaneFactory.createCardLayout_S_Pane();
		presPane.add(cardPaneForPresent, BorderLayout.CENTER);
		cardForPresent = new CardLayout();
		cardPaneForPresent.setLayout(cardForPresent);

		cardPaneForTreeSetting = FRGUIPaneFactory.createBorderLayout_L_Pane();

		widgetPropertyPane = new BasicWidgetPropertySettingPane();
		attriPane.add(widgetPropertyPane, BorderLayout.NORTH);
		cardPane = FRGUIPaneFactory.createCardLayout_S_Pane();
		attriPane.add(cardPane, BorderLayout.CENTER);
		card = (CardLayout) cardPane.getLayout();
		this.setPreferredSize(new Dimension(600, 450));
	}

	@Override
	protected String title4PopupWindow() {
		return "Widget";
	}

	public void populate(Widget cellWidget) {
		currentEditorDefinePane = null;

		if (cellWidget instanceof NoneWidget) {
			this.tabbedPane.setEnabled(false);
		} else {
			this.tabbedPane.setEnabled(true);
		}

		attriPane.remove(widgetPropertyPane);
		widgetPropertyPane = new BasicWidgetPropertySettingPane();
		attriPane.add(widgetPropertyPane, BorderLayout.NORTH);

		WidgetDefinePaneFactory.RN rn = WidgetDefinePaneFactory.createWidgetDefinePane(cellWidget);
		BasicBeanPane<? extends Widget> definePane = rn.getDefinePane();
		cardPane.add(definePane, rn.getCardName());
		if (rn.getDictionaryPane() != null) {
			cardPaneForPresent.removeAll();
			cardPaneForPresent.add(rn.getDictionaryPane(), rn.getCardName());
			cardForPresent.show(cardPaneForPresent, rn.getCardName());
			addPresPane(true);
		} else {
			addPresPane(false);
		}
		if (rn.getTreeSettingPane() != null) {
			cardPaneForTreeSetting.removeAll();
			cardPaneForTreeSetting.add(rn.getTreeSettingPane());
			addTreeSettingPane(true);
		} else {
			addTreeSettingPane(false);
		}
		card.show(cardPane, rn.getCardName());
		currentEditorDefinePane = definePane;
		eventTabPane.populate(cellWidget);
		widgetPropertyPane.populate(cellWidget);
		tabbedPane.setSelectedIndex(0);
	}

	public Widget update() {
		if (currentEditorDefinePane == null) {
			return null;
		}
		Widget widget = currentEditorDefinePane.updateBean();
		if (widget == null)
		{
			return null;
		}
		widgetPropertyPane.update(widget);

		Listener[] listener = eventTabPane == null ? new Listener[0] : eventTabPane.updateListeners();
		widget.clearListeners();
		for (Listener l : listener) {
			widget.addListener(l);
		}

		return widget;
	}


	@Override
	/**
	 *���
	 */
	public void checkValid() throws Exception {
		currentEditorDefinePane.checkValid();
	}

	//:jackie  ���ѡ���������̬������̬������tab���
	private void addPresPane(boolean add) {
		if (add) {
			tabbedPane.add(this.presPane, 1);
			tabbedPane.setTitleAt(1, Inter.getLocText("DS-Dictionary"));
		} else {
			tabbedPane.remove(presPane);
		}
	}

	private void addTreeSettingPane(boolean add) {
		if (add) {
			tabbedPane.add(this.cardPaneForTreeSetting, 1);
			tabbedPane.setTitleAt(1, Inter.getLocText("Create_Tree"));
		} else {
			tabbedPane.remove(this.cardPaneForTreeSetting);
		}
	}
}
