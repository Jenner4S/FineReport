package com.fr.design.webattr;

import com.fr.base.BaseUtils;
import com.fr.base.ConfigManager;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIColorButton;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.form.event.Listener;
import com.fr.general.Inter;
import com.fr.report.web.Location;
import com.fr.report.web.ToolBarManager;
import com.fr.report.web.WebContent;
import com.fr.report.web.WebWrite;
import com.fr.stable.Constants;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.web.attr.ReportWebAttr;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class WriteToolBarPane extends AbstractEditToolBarPane {
	private EventPane eventPane;
	private UICheckBox colorBox;
	private UIColorButton colorButton;
	private DragToolBarPane dragToolbarPane;
    private UIRadioButton topRadioButton = new UIRadioButton(Inter.getLocText("FR-Designer_Top"));
    private UIRadioButton bottomRadioButton = new UIRadioButton(Inter.getLocText("FR-Designer_Bottom"));
    private UILabel sheetShowLocationLabel = new UILabel("sheet" + Inter.getLocText(new String[]{"Label","Page_Number","Display position"}) + ":");
    private UIRadioButton centerRadioButton = new UIRadioButton(Inter.getLocText(new String[]{"Center", "Display"}));
    private UIRadioButton leftRadioButton = new UIRadioButton(Inter.getLocText(new String[]{"Left", "Display"}));
    private UILabel rptShowLocationLabel = new UILabel(Inter.getLocText("FR-Designer_Report_Show_Location") + ":", UILabel.LEFT);
    private UICheckBox isUseToolBarCheckBox = new UICheckBox(Inter.getLocText("FR-Designer_Use_ToolBar"));
	private UIButton editToolBarButton = new UIButton(Inter.getLocText("FR-Designer_Edit"));
	private UILabel showListenersLabel = new UILabel(Inter.getLocText("Form-Editing_Listeners") + ":");
	private UICheckBox unloadCheck;
	private UICheckBox showWidgets;

	public WriteToolBarPane() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		JPanel allPanel = FRGUIPaneFactory.createBorderLayout_L_Pane();
		this.add(allPanel, BorderLayout.CENTER);
		JPanel northPane = FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(2);
		allPanel.add(northPane, BorderLayout.NORTH);

        //sheet��ǩҳ��ʾλ��
        ButtonGroup sheetButtonGroup = new ButtonGroup();
        bottomRadioButton.setSelected(true);
        sheetButtonGroup.add(topRadioButton);
        sheetButtonGroup.add(bottomRadioButton);
        northPane.add(GUICoreUtils.createFlowPane(new Component[] {sheetShowLocationLabel, topRadioButton, bottomRadioButton}, FlowLayout.LEFT));

        //Sean:������ʾλ��
        ButtonGroup rptButtonGroup = new ButtonGroup();
        leftRadioButton.setSelected(true);
        rptButtonGroup.add(leftRadioButton);
        rptButtonGroup.add(centerRadioButton);
        northPane.add(GUICoreUtils.createFlowPane(new Component[] {rptShowLocationLabel, centerRadioButton, leftRadioButton}, FlowLayout.LEFT));

		colorBox = new UICheckBox(Inter.getLocText(new String[]{"Face_Write", "Current", "Edit", "Row", "Background", "Set"}) + ":");
		colorBox.setSelected(true);
		colorBox.addActionListener(colorListener);
		colorButton = new UIColorButton(BaseUtils.readIcon("/com/fr/design/images/gui/color/background.png"));
		northPane.add(GUICoreUtils.createFlowPane(new Component[] {colorBox, colorButton}, FlowLayout.LEFT));

		unloadCheck = new UICheckBox(Inter.getLocText(new String[]{"Event-unloadcheck", "Tooltips"}));
		unloadCheck.setSelected(true);

		showWidgets=new UICheckBox(Inter.getLocText(new String[]{"Event-showWidgets"}));
		showWidgets.setSelected(false);
		northPane.add(GUICoreUtils.createFlowPane(new Component[] {unloadCheck,showWidgets}, FlowLayout.LEFT));

		editToolBarButton.addActionListener(editBtnListener);
		isUseToolBarCheckBox.setSelected(true);
		isUseToolBarCheckBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				editToolBarButton.setEnabled(isUseToolBarCheckBox.isSelected());
			}
		});
		northPane.add(GUICoreUtils.createFlowPane(new Component[] {isUseToolBarCheckBox, editToolBarButton}, FlowLayout.LEFT));
        northPane.add(new UILabel());
        northPane.add(GUICoreUtils.createFlowPane(showListenersLabel, FlowLayout.LEFT));
		eventPane = new EventPane(new WebWrite().supportedEvents());
		JPanel center = FRGUIPaneFactory.createBorderLayout_S_Pane();
		center.add(eventPane, BorderLayout.CENTER);
		allPanel.add(center, BorderLayout.CENTER);
		//wei : Ĭ��ûconfig.xml������£�����Ĭ�Ϲ�����
		ToolBarManager toolBarManager = ToolBarManager.createDefaultWriteToolBar();
		toolBarManager.setToolBarLocation(Location.createTopEmbedLocation());
		this.toolBarManagers = new ToolBarManager[] {toolBarManager};
	}
	
	private ActionListener editBtnListener = new ActionListener() {
		
		public void actionPerformed(ActionEvent e) {
			final DragToolBarPane dragToolbarPane = new DragToolBarPane();
			dragToolbarPane.setDefaultToolBar(ToolBarManager.createDefaultWriteToolBar(), ReportWebWidgetConstants.getWriteToolBarInstance());
			dragToolbarPane.populateBean(WriteToolBarPane.this.toolBarManagers);
			BasicDialog toobarDialog = dragToolbarPane.showWindow(SwingUtilities.getWindowAncestor(WriteToolBarPane.this));
			toobarDialog.addDialogActionListener(new DialogActionAdapter() {
				@Override
				public void doOk() {
					WriteToolBarPane.this.toolBarManagers = dragToolbarPane.updateBean();
				}
			});
			toobarDialog.setVisible(true);
		}
	};
	
	private ActionListener colorListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			colorButton.setEnabled(colorBox.isSelected());
		}
		
	};
	
	@Override
	protected WidgetOption[] getToolBarInstance() {
		return ReportWebWidgetConstants.getWriteToolBarInstance();
	}
	
	@Override
	protected String title4PopupWindow() {
		return Inter.getLocText("WEB-Write_Setting");
	}

	@Override
	public void populateBean(WebContent webContent) {
		if (webContent == null) {
			webContent = new WebWrite();
		}
        WebWrite webWrite = (WebWrite)webContent;
		if (webWrite.getSelectedColor() != null) {
			colorBox.setSelected(true);
			colorButton.setColor(webWrite.getSelectedColor());
		} else {
			colorBox.setSelected(false);
		}

        if(webWrite.getSheetPosition() == Constants.TOP ){
            topRadioButton.setSelected(true);
        }else if(webWrite.getSheetPosition() == Constants.BOTTOM){
            bottomRadioButton.setSelected(true);
        }

        if (webWrite.isViewAtLeft()) {
            leftRadioButton.setSelected(true);
        } else {
            centerRadioButton.setSelected(true);
        }
        unloadCheck.setSelected(webWrite.isUnloadCheck());
		showWidgets.setSelected(webWrite.isShowWidgets());

		if (webWrite.isUseToolBar()) {
			this.toolBarManagers = webWrite.getToolBarManagers();
			this.isUseToolBarCheckBox.setSelected(true);
			editToolBarButton.setEnabled(true);
		} else {
			this.isUseToolBarCheckBox.setSelected(false);
			editToolBarButton.setEnabled(false);
		}
		
		if (webWrite.getListenerSize() != 0) {
			List<Listener> list = new ArrayList<Listener>();
			for (int i = 0; i < webWrite.getListenerSize(); i++) {
				list.add(webWrite.getListener(i));
			}
			eventPane.populate(list);
		}
	}

	@Override
	public WebWrite updateBean() {
		WebWrite webWrite = new WebWrite();
		if (this.isUseToolBarCheckBox.isSelected()) {
			webWrite.setToolBarManagers(this.toolBarManagers);
		} else {
			webWrite.setToolBarManagers(new ToolBarManager[0]);
		}
		
		if (colorBox.isSelected()) {
			webWrite.setSelectedColor(colorButton.getColor());
		} else {
			webWrite.setSelectedColor(null);
		}

		webWrite.setUnloadCheck(unloadCheck.isSelected());

		webWrite.setShowWidgets(showWidgets.isSelected());

        webWrite.setViewAtLeft(leftRadioButton.isSelected());

        if(topRadioButton.isSelected()){
            webWrite.setSheetPosition(Constants.TOP);
        }else if(bottomRadioButton.isSelected()){
            webWrite.setSheetPosition(Constants.BOTTOM);
        }

		for (int i = 0; i < eventPane.update().size(); i++) {
			webWrite.addListener(eventPane.update().get(i));
		}
		return webWrite;
	}
	
	@Override
	public void setEnabled(boolean isEnabled) {
		super.setEnabled(isEnabled);
		this.eventPane.setEnabled(isEnabled);
        this.topRadioButton.setEnabled(isEnabled);
        this.bottomRadioButton.setEnabled(isEnabled);
        this.centerRadioButton.setEnabled(isEnabled);
        this.leftRadioButton.setEnabled(isEnabled);
		this.isUseToolBarCheckBox.setEnabled(isEnabled );
		this.editToolBarButton.setEnabled(isEnabled && isUseToolBarCheckBox.isSelected());
		colorBox.setEnabled(isEnabled);
		colorButton.setEnabled(isEnabled && colorBox.isSelected());
		this.showListenersLabel.setEnabled(isEnabled);
		unloadCheck.setEnabled(isEnabled);
		showWidgets.setEnabled(isEnabled);
	}

    /**
     * �༭����������
     */
	@Override
	public void editServerToolBarPane() {
		final WriteToolBarPane serverPageToolBarPane = new WriteToolBarPane();
		ReportWebAttr reportWebAttr = ((ReportWebAttr)ConfigManager.getProviderInstance().getGlobalAttribute(ReportWebAttr.class));
		if (reportWebAttr != null) {
			serverPageToolBarPane.populateBean(reportWebAttr.getWebWrite());
		}
		BasicDialog serverPageDialog = serverPageToolBarPane.showWindow(SwingUtilities.getWindowAncestor(WriteToolBarPane.this));
		serverPageDialog.addDialogActionListener(new DialogActionAdapter() {
			
			@Override
			public void doOk() {
				ReportWebAttr reportWebAttr = ((ReportWebAttr)ConfigManager.getProviderInstance().getGlobalAttribute(ReportWebAttr.class));
				if (reportWebAttr == null) {
					reportWebAttr = new ReportWebAttr();
					ConfigManager.getProviderInstance().putGlobalAttribute(ReportWebAttr.class, reportWebAttr);
				}
				reportWebAttr.setWebWrite(serverPageToolBarPane.updateBean());
			}
		});
		serverPageDialog.setVisible(true);
	}
}
