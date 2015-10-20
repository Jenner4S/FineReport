package com.fr.design.webattr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JPanel;

import com.fr.design.ExtraDesignClassManager;
import com.fr.design.gui.core.WidgetOption;
import com.fr.report.web.ToolBarManager;
import com.fr.report.web.WebView;
import com.fr.web.attr.ReportWebAttr;

public class ViewWebSettingPane extends WebSettingPane<WebView> {
	public ViewWebSettingPane() {
		super();
	}

	@Override
	protected JPanel createOtherSetPane() {
		return null;
	}

	@Override
	protected void populateSubWebSettingrBean(WebView ob) {

	}

	@Override
	protected WebView updateSubWebSettingBean() {
		return new WebView();
	}

	@Override
	protected WidgetOption[] getToolBarInstance() {
		List<WidgetOption> defaultOptions = Arrays.asList(ReportWebWidgetConstants.getViewToolBarInstance());
		List<WidgetOption> extraOptions = Arrays.asList(ExtraDesignClassManager.getInstance().getWebWidgetOptions());
		List<WidgetOption> options = new ArrayList<WidgetOption>();
		options.addAll(defaultOptions);
		options.addAll(extraOptions);
		return options.toArray(new WidgetOption[options.size()]);
	}

	@Override
	protected ToolBarManager getDefaultToolBarManager() {
		return ToolBarManager.createDefaultViewToolBar();
	}

	@Override
	protected WebView getWebContent(ReportWebAttr reportWebAttr) {
		return reportWebAttr == null ? null : reportWebAttr.getWebView();
	}

	@Override
	protected String[] getEventNames() {
		return  new WebView().supportedEvents();
	}

	@Override
	protected void setWebContent(ReportWebAttr reportWebAttr,WebView webContent) {
		reportWebAttr.setWebView(webContent);
	}

}
