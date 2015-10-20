package com.fr.design.webattr;

import com.fr.base.BaseUtils;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.gui.ibutton.UIColorButton;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
import com.fr.report.web.ToolBarManager;
import com.fr.report.web.WebWrite;
import com.fr.stable.Constants;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.web.attr.ReportWebAttr;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WriteWebSettingPane extends WebSettingPane<WebWrite> {
	private UICheckBox colorBox;
	private UIColorButton colorButton;
    private UIRadioButton topRadioButton;
    private UIRadioButton bottomRadioButton;
    private UIRadioButton centerRadioButton;
    private UIRadioButton leftRadioButton;
    private UILabel rptShowLocationLabel;
    private UILabel sheetShowLocationLabel;
	private UICheckBox unloadCheck;
	private UICheckBox showWidgets;

	public WriteWebSettingPane() {
		super();
	}

	@Override
	protected JPanel createOtherSetPane() {
		colorBox = new UICheckBox(Inter.getLocText(new String[]{"Face_Write", "Current", "Edit", "Row",
				"Background", "Set"}) + ":");
		colorBox.setSelected(true);
		colorButton = new UIColorButton(BaseUtils.readIcon("/com/fr/design/images/gui/color/background.png"));
		colorBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				colorButton.setEnabled(colorBox.isSelected());
			}

		});
		JPanel backgroundPane = GUICoreUtils.createFlowPane(new Component[] { colorBox, colorButton }, FlowLayout.LEFT);

        //sheet��ǩҳ��ʾλ��
        topRadioButton = new UIRadioButton(Inter.getLocText("Top"));
        bottomRadioButton = new UIRadioButton(Inter.getLocText("Bottom"));
        sheetShowLocationLabel = new UILabel("sheet" + Inter.getLocText(new String[]{"Label","Page_Number","Display position"}) + ":",UILabel.LEFT);
        ButtonGroup buttonGroup = new ButtonGroup();
        bottomRadioButton.setSelected(true);
        buttonGroup.add(topRadioButton);
        buttonGroup.add(bottomRadioButton);
        JPanel sheetPane = GUICoreUtils.createFlowPane(new Component[] {sheetShowLocationLabel, topRadioButton, bottomRadioButton}, FlowLayout.LEFT);

        //Sean: ������ʾλ��since 706
        rptShowLocationLabel = new UILabel(Inter.getLocText("Report_Show_Location") + ":", UILabel.LEFT);
        centerRadioButton = new UIRadioButton(Inter.getLocText(new String[]{"Center", "Display"}));
        leftRadioButton = new UIRadioButton(Inter.getLocText(new String[]{"Left", "Display"}));
        ButtonGroup rptShowButtonGroup = new ButtonGroup();
        leftRadioButton.setSelected(true);
        rptShowButtonGroup.add(centerRadioButton);
        rptShowButtonGroup.add(leftRadioButton);
        JPanel showLocPane = GUICoreUtils.createFlowPane(new Component[] {rptShowLocationLabel, centerRadioButton, leftRadioButton}, FlowLayout.LEFT);

        unloadCheck = new UICheckBox(Inter.getLocText(new String[]{"Event-unloadcheck", "Tooltips"}));
		unloadCheck.setSelected(true);

		showWidgets=new UICheckBox(Inter.getLocText(new String[]{"Event-showWidgets"}));
		showWidgets.setSelected(false);
		JPanel unloadCheckPane = GUICoreUtils.createFlowPane(new Component[] {unloadCheck,showWidgets}, FlowLayout.LEFT);
        JPanel northPane = FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(2);
        northPane.add(sheetPane);
        northPane.add(showLocPane);
        northPane.add(backgroundPane);
        northPane.add(unloadCheckPane);
        return northPane;
	}

	@Override
	protected void checkEnabled(boolean isSelected) {
		super.checkEnabled(isSelected);
		colorBox.setEnabled(isSelected);
		colorButton.setEnabled(isSelected);
        topRadioButton.setEnabled(isSelected);
        leftRadioButton.setEnabled(isSelected);
        centerRadioButton.setEnabled(isSelected);
        rptShowLocationLabel.setEnabled(isSelected);
        sheetShowLocationLabel.setEnabled(isSelected);
        bottomRadioButton.setEnabled(isSelected);
		unloadCheck.setEnabled(isSelected);
		showWidgets.setEnabled(isSelected);
	}
	protected void setDefault(){
		super.setDefault();
		colorBox.setSelected(false);
        bottomRadioButton.setSelected(true);
        leftRadioButton.setSelected(true);
		unloadCheck.setSelected(true);
		showWidgets.setSelected(false);
	}
	@Override
	protected void populateSubWebSettingrBean(WebWrite webWrite) {
		if (webWrite == null) {
			webWrite = new WebWrite();
		}
		if (webWrite.getSelectedColor() != null) {
			colorBox.setSelected(true);
			colorButton.setColor(webWrite.getSelectedColor());
		} else {
			colorBox.setSelected(false);
		}

        if (webWrite.getSheetPosition() == Constants.TOP) {
            topRadioButton.setSelected(true);
        } else if(webWrite.getSheetPosition() == Constants.BOTTOM){
            bottomRadioButton.setSelected(true);
        }

        if(webWrite.isViewAtLeft()){
            leftRadioButton.setSelected(true);
        } else{
            centerRadioButton.setSelected(true);
        }
		unloadCheck.setSelected(webWrite.isUnloadCheck());
		showWidgets.setSelected(webWrite.isShowWidgets());

	}

	@Override
	protected WebWrite updateSubWebSettingBean() {
		WebWrite webWrite = new WebWrite();
		if (colorBox.isSelected()) {
			webWrite.setSelectedColor(colorButton.getColor());
		} else {
			webWrite.setSelectedColor(null);
		}
        if(topRadioButton.isSelected()){
        webWrite.setSheetPosition(Constants.TOP);
        }else if(bottomRadioButton.isSelected()){
            webWrite.setSheetPosition(Constants.BOTTOM);
        }
        webWrite.setViewAtLeft(leftRadioButton.isSelected());
		webWrite.setUnloadCheck(unloadCheck.isSelected());
		webWrite.setShowWidgets(showWidgets.isSelected());
		return webWrite;
	}

	@Override
	protected WidgetOption[] getToolBarInstance() {
		List<WidgetOption> defaultOptions = Arrays.asList(ReportWebWidgetConstants.getWriteToolBarInstance());
		List<WidgetOption> extraOptions = Arrays.asList(ExtraDesignClassManager.getInstance().getWebWidgetOptions());
		List<WidgetOption> options = new ArrayList<WidgetOption>();
		options.addAll(defaultOptions);
		options.addAll(extraOptions);
		return options.toArray(new WidgetOption[options.size()]);
	}

	@Override
	protected ToolBarManager getDefaultToolBarManager() {
		return ToolBarManager.createDefaultWriteToolBar();
	}

	@Override
	protected WebWrite getWebContent(ReportWebAttr reportWebAttr) {
		return reportWebAttr == null ? null:reportWebAttr.getWebWrite();
	}

	@Override
	protected String[] getEventNames() {
		return new WebWrite().supportedEvents();
	}

	@Override
	protected void setWebContent(ReportWebAttr reportWebAttr,WebWrite webContent) {
		reportWebAttr.setWebWrite(webContent);
	}

}
