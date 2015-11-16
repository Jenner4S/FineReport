package com.fr.design.widget.ui;

import com.fr.base.FRContext;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.frpane.TreeSettingPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.present.dict.DictionaryPane;
import com.fr.design.widget.DataModify;
import com.fr.form.ui.NameWidget;
import com.fr.general.FRFont;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

public class UserEditorDefinePane extends AbstractDataModify<NameWidget> {
	private NameWidget nWidget;
	public UserEditorDefinePane() {
		this.initComponents();
	}
	
	private void initComponents() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		
		UILabel infoLabel = new UILabel();
        FRFont frFont = FRContext.getDefaultValues().getFRFont();
        infoLabel.setFont(new Font(frFont.getFamily(), Font.BOLD, 24));
	    infoLabel.setText(Inter.getLocText(
	    		"Widget-User_Defined_Editor") + ".");
	    infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    
		this.add(infoLabel, BorderLayout.CENTER);
	}
	
	@Override
	protected String title4PopupWindow() {
		return "name";
	}
	
	@Override
	public void populateBean(NameWidget cellWidget) {
		nWidget = cellWidget;
	}
	
	@Override
	public NameWidget updateBean() {
		return nWidget;
	}
}