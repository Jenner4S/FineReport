package com.fr.design.widget.ui;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.itree.refreshabletree.TreeRootPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.gui.frpane.TreeSettingPane;
import com.fr.form.ui.TreeEditor;
import com.fr.general.Inter;


/*
 * richer:tree editor
 */
public class TreeEditorDefinePane extends FieldEditorDefinePane<TreeEditor> {
	protected TreeSettingPane treeSettingPane;
	protected TreeRootPane treeRootPane;

	private UICheckBox removeRepeatCheckBox;

	public TreeEditorDefinePane(){
		this.initComponents();			
	}

	@Override
	protected void populateSubFieldEditorBean(TreeEditor e) {
		this.treeSettingPane.populate(e);
		treeRootPane.populate(e.getTreeAttr());
	}

	@Override
	protected TreeEditor updateSubFieldEditorBean() {
		TreeEditor editor = treeSettingPane.updateTreeEditor();
		editor.setTreeAttr(treeRootPane.update());
		return editor;
	}

	@Override
	protected JPanel setFirstContentPane() {
		return this.setSecondContentPane();
	}

	protected JPanel setSecondContentPane() {
		JPanel contentPane = FRGUIPaneFactory.createBorderLayout_L_Pane();
		contentPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		JPanel contenter = FRGUIPaneFactory.createMediumHGapFlowInnerContainer_M_Pane();
		contentPane.add(contenter,BorderLayout.NORTH);
		removeRepeatCheckBox = new UICheckBox(Inter.getLocText("Form-Remove_Repeat_Data"), false);
		contenter.add(removeRepeatCheckBox);
		JPanel otherContentPane = this.setThirdContentPane();
		if (otherContentPane != null) {
			contentPane.add(otherContentPane,BorderLayout.CENTER);
		}
		return contentPane;
	}

	protected JPanel setThirdContentPane() {
		JPanel content = FRGUIPaneFactory.createBorderLayout_L_Pane();
		content.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		treeRootPane = new TreeRootPane();
		content.add(treeRootPane, BorderLayout.NORTH);
		treeSettingPane = new TreeSettingPane(true);
		return content;
	}
	
	@Override
	protected String title4PopupWindow() {
		return "tree";
	}

	/**
	 * get treeSettingPane
	 * @return
	 */
	public TreeSettingPane getTreeSettingPane() {
		return this.treeSettingPane;
	}
}