package com.fr.design.report;


import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.MultilineLabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.dialog.BasicPane;
import com.fr.general.Inter;
import com.fr.io.attr.WordExportAttr;

public class WordExportPane extends BasicPane {
	private UICheckBox isExportAsTable;
	private JPanel southPane;

	public WordExportPane() {
		this.initComponents();
	}

	protected void initComponents() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());

		JPanel outnorthPane =FRGUIPaneFactory.createTitledBorderPane("Word" + Inter.getLocText("ReportD-Excel_Export"));
		this.add(outnorthPane);
		
		JPanel northPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_M_Pane();
		outnorthPane.add(northPane);
		
		JPanel checkBoxPane =FRGUIPaneFactory.createNormalFlowInnerContainer_M_Pane();
		isExportAsTable = new UICheckBox(Inter.getLocText("is_need_word_adjust"), false);
		checkBoxPane.add(isExportAsTable);
		
		southPane = FRGUIPaneFactory.createNormalFlowInnerContainer_M_Pane();
		JPanel innerAlertBorderPane = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("Attention"));
		JPanel alertPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_M_Pane();
		
        MultilineLabel wordLineLabel = new MultilineLabel();
        wordLineLabel.setPreferredSize(new Dimension(250, 100));
        wordLineLabel.setText(Inter.getLocText("alert_word"));
        wordLineLabel.setForeground(Color.RED);
		alertPane.add(wordLineLabel);
		
		southPane.add(innerAlertBorderPane);
		innerAlertBorderPane.add(alertPane);
		
		northPane.add(checkBoxPane);
		northPane.add(southPane);
	}
	
	@Override
	protected String title4PopupWindow() {
		return "WordExport";
	}

	public void populate(WordExportAttr wordExportAttr) {
		if(wordExportAttr == null){
			return;
		}
		
		if(wordExportAttr.isExportAsTable()){
			isExportAsTable.setSelected(true);
//			southPane.setVisible(true);
		}
	}

	public WordExportAttr update() {
		WordExportAttr wordExportAttr = new WordExportAttr();
		wordExportAttr.setExportAsTable(isExportAsTable.isSelected());
		return wordExportAttr;
	}
}