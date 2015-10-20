package com.fr.design.webattr;

import com.fr.base.ConfigManager;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.design.webattr.ReportSelectToolBarPane.EditToolBarPane;
import com.fr.form.event.Listener;
import com.fr.general.Inter;
import com.fr.report.web.Location;
import com.fr.report.web.ToolBarManager;
import com.fr.report.web.WebView;
import com.fr.web.attr.ReportWebAttr;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ViewToolBarPane extends EditToolBarPane<WebView> {
	private EventPane eventPane;
	
	private UICheckBox isUseToolBarCheckBox = new UICheckBox(Inter.getLocText("FR-Designer_Use_ToolBar"));
	private UIButton editToolBarButton = new UIButton(Inter.getLocText("FR-Designer_Edit"));
	private UILabel showListenersLabel = new UILabel(Inter.getLocText("Form-Editing_Listeners") + ":");
	
	private ToolBarManager[] toolBarManagers = null;
	
	public ViewToolBarPane() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		JPanel allPanel = FRGUIPaneFactory.createBorderLayout_L_Pane();

		this.add(allPanel, BorderLayout.CENTER);
        JPanel northPane = FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(1);
        allPanel.add(northPane, BorderLayout.NORTH);
		editToolBarButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final DragToolBarPane dragToolbarPane = new DragToolBarPane();
				dragToolbarPane.setDefaultToolBar(ToolBarManager.createDefaultViewToolBar(), ReportWebWidgetConstants.getViewToolBarInstance());
				dragToolbarPane.populateBean(ViewToolBarPane.this.toolBarManagers);
				
				BasicDialog toobarDialog = dragToolbarPane.showWindow(SwingUtilities.getWindowAncestor(ViewToolBarPane.this));
				
				toobarDialog.addDialogActionListener(new DialogActionAdapter() {
					@Override
					public void doOk() {
						ViewToolBarPane.this.toolBarManagers = dragToolbarPane.updateBean();
					}
				});
				toobarDialog.setVisible(true);
			}
		});
		isUseToolBarCheckBox.setSelected(true);
		isUseToolBarCheckBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editToolBarButton.setEnabled(isUseToolBarCheckBox.isSelected());
			}
		});
        northPane.add(GUICoreUtils.createFlowPane(new Component[] {isUseToolBarCheckBox, editToolBarButton}, FlowLayout.LEFT));
        northPane.add(GUICoreUtils.createFlowPane(showListenersLabel, FlowLayout.LEFT));
		eventPane = new EventPane(new WebView().supportedEvents());
		JPanel center = FRGUIPaneFactory.createBorderLayout_S_Pane();
		center.add(eventPane, BorderLayout.CENTER);
		allPanel.add(center, BorderLayout.CENTER);
		//wei : Ĭ��ûconfig.xml������£�����Ĭ�Ϲ�����
		ToolBarManager toolBarManager = ToolBarManager.createDefaultViewToolBar();
		toolBarManager.setToolBarLocation(Location.createTopEmbedLocation());
		this.toolBarManagers = new ToolBarManager[] {toolBarManager};
	}
	
	@Override
	public void setEnabled(boolean isEnabled) {
		super.setEnabled(isEnabled);
		
		this.eventPane.setEnabled(isEnabled);
		
		this.isUseToolBarCheckBox.setEnabled(isEnabled);
		this.editToolBarButton.setEnabled(isEnabled && isUseToolBarCheckBox.isSelected());
		this.showListenersLabel.setEnabled(isEnabled);
	}
	
	@Override
	protected String title4PopupWindow() {
		return Inter.getLocText("M-Data_Analysis_Settings");
	}

	@Override
	public void populateBean(WebView webView) {
		if (webView == null) {
			webView = new WebView();
		}

		if (webView.isUseToolBar()) {
			this.toolBarManagers = webView.getToolBarManagers();
			this.isUseToolBarCheckBox.setSelected(true);
		} else {
			this.isUseToolBarCheckBox.setSelected(false);
			editToolBarButton.setEnabled(false);
		}
		
		if (webView.getListenerSize() != 0) {
			List<Listener> list = new ArrayList<Listener>();
			for (int i = 0; i < webView.getListenerSize(); i++) {
				list.add(webView.getListener(i));
			}
			eventPane.populate(list);
		}
	}

	@Override
	public WebView updateBean() {
		WebView webView = new WebView();
		if (this.isUseToolBarCheckBox.isSelected()) {
			webView.setToolBarManagers(this.toolBarManagers);
		} else {
			webView.setToolBarManagers(new ToolBarManager[0]);
		}
		for (int i = 0; i < eventPane.update().size(); i++) {
			Listener listener = eventPane.update().get(i);
			webView.addListener(listener);
		}
		return webView;
	}

    /**
     * �༭����������������
     */
	@Override
	public void editServerToolBarPane() {
		final ViewToolBarPane serverPageToolBarPane = new ViewToolBarPane();
		ReportWebAttr reportWebAttr = ((ReportWebAttr)ConfigManager.getProviderInstance().getGlobalAttribute(ReportWebAttr.class));
		if (reportWebAttr != null) {
			serverPageToolBarPane.populateBean(reportWebAttr.getWebView());
		}
		BasicDialog serverPageDialog = serverPageToolBarPane.showWindow(SwingUtilities.getWindowAncestor(ViewToolBarPane.this));
		serverPageDialog.addDialogActionListener(new DialogActionAdapter() {
			
			@Override
			public void doOk() {
				ReportWebAttr reportWebAttr = ((ReportWebAttr)ConfigManager.getProviderInstance().getGlobalAttribute(ReportWebAttr.class));
				if (reportWebAttr == null) {
					reportWebAttr = new ReportWebAttr();
					ConfigManager.getProviderInstance().putGlobalAttribute(ReportWebAttr.class, reportWebAttr);
				}
				reportWebAttr.setWebView(serverPageToolBarPane.updateBean());
			}
		});
		serverPageDialog.setVisible(true);
		
	}
}