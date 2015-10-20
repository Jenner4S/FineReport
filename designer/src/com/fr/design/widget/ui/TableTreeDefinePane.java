package com.fr.design.widget.ui;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import com.fr.design.gui.ilable.UILabel;
import javax.swing.JPanel;

import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.present.dict.DictionaryPane;
import com.fr.form.ui.TableTree;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;

public class TableTreeDefinePane extends FieldEditorDefinePane<TableTree> {
	private UITextField urlTextFiled;
	// richer:用来描述tabletree的列名
	private DictionaryPane dictionaryPane;

	public TableTreeDefinePane() {
		this.iniComoponents();
	}

	private void iniComoponents() {
		super.initComponents();
		dictionaryPane = new DictionaryPane();
	}

	@Override
	protected JPanel setFirstContentPane() {
		JPanel contentPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		contentPane.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));
		JPanel dataPane = FRGUIPaneFactory.createMediumHGapFlowInnerContainer_M_Pane();
		dataPane.add(new UILabel(Inter.getLocText("Widget-Data_URL") + ":"));
		urlTextFiled = new UITextField(30);
		dataPane.add(urlTextFiled);
		contentPane.add(dataPane, BorderLayout.NORTH);
		JPanel ColumnPane = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("ColumnName"));
		contentPane.add(ColumnPane, BorderLayout.CENTER);
		return contentPane;
	}
	
	@Override
	protected String title4PopupWindow() {
		return "tabletree";
	}

	@Override
	protected void populateSubFieldEditorBean(TableTree e) {
		urlTextFiled.setText(e.getDataUrl());
		dictionaryPane.populateBean(e.getDictionary());
	}

	@Override
	protected TableTree updateSubFieldEditorBean() {
		TableTree tableTree = new TableTree();
		if (!StringUtils.isEmpty(urlTextFiled.getText())) {
			tableTree.setDataUrl(urlTextFiled.getText());
		}
		tableTree.setDictionary(dictionaryPane.updateBean());
		return tableTree;
	}

	public DictionaryPane getDictionaryPane() {
		return this.dictionaryPane;
	}

}
