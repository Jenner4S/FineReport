package com.fr.design.gui.frpane;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import com.fr.design.data.DataCreatorUI;
import com.fr.design.gui.ilable.UILabel;

import javax.swing.*;

import com.fr.general.NameObject;
import com.fr.data.impl.TableDataDictionary;
import com.fr.data.impl.TreeAttr;
import com.fr.data.impl.TreeNodeAttr;
import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.itree.refreshabletree.TreeDataCardPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.dialog.BasicPane;
import com.fr.form.ui.TreeComboBoxEditor;
import com.fr.form.ui.TreeEditor;
import com.fr.general.Inter;

public class TreeSettingPane extends BasicPane implements DataCreatorUI {
	private JTreeControlPane controlPane;
	private JTreeAutoBuildPane autoBuildPane;
	private UIComboBox buildBox;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1762889323082827111L;

	private String[] buildWay = new String[] { Inter.getLocText("Layer-Build"),
			Inter.getLocText("Auto-Build") };

	public TreeSettingPane(boolean isEditor) {
		this.initComponents(isEditor);
	}

	private void initComponents(boolean isEditor) {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		JPanel buildWayPanel= FRGUIPaneFactory.createMediumHGapFlowInnerContainer_M_Pane();
		UILabel buildWayLabel = new UILabel(Inter.getLocText("Build-Way") + " ��");
		buildWayPanel.add(buildWayLabel);
		buildBox = new UIComboBox(buildWay);
		buildBox.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				cardChanged(buildBox.getSelectedIndex());
			}
		});
		buildWayPanel.add(buildBox);
		
		controlPane = new JTreeControlPane(new NameableCreator[] { treeNode },
				new TreeDataCardPane(), isEditor);
		autoBuildPane = new JTreeAutoBuildPane();
		this.add(buildWayPanel, BorderLayout.NORTH);
		cardChanged(0);
	}
	
	private void cardChanged(int index) {
		this.remove(controlPane);
		this.remove(autoBuildPane);
		this.add(index == 0 ? controlPane : autoBuildPane, BorderLayout.CENTER);
		validate();
		repaint();
		revalidate();
	}

	@Override
	protected String title4PopupWindow() {
		return Inter.getLocText("Create_Tree");
	}

    @Override
    public JComponent toSwingComponent() {
        return this;
    }

    NameableCreator treeNode = new NameObjectCreator(
			Inter.getLocText("Gradation"),
			"/com/fr/design/images/data/source/jdbcTableData.png",
			TreeNodeAttr.class);

	/**
	 *
	 * @param treeEditor
	 */
	public void populate(TreeEditor treeEditor) {
		boolean isAutoBuild = treeEditor.isAutoBuild();
		TreeAttr treeAttr = treeEditor.getTreeAttr();
		if (treeAttr != null) {
			NameObject no = new NameObject("name", treeEditor);
			controlPane.populate(no);
		}
		if (isAutoBuild)  {
			buildBox.setSelectedIndex(1);
			TableDataDictionary dictionary = treeEditor.getDictionary();
			autoBuildPane.populate(dictionary);
		} else {
			buildBox.setSelectedIndex(0);
		}
	}

	/**
	 * ��ͼ����update
	 * @return
	 */
	public TreeEditor updateTreeEditor() {
//		NameObject no = this.controlPane.update();
//		if (no != null) {
//			return ((TreeEditor) no.getObject());
//		}
//
//		return null;
		TreeEditor te = new TreeEditor();
		if (buildBox.getSelectedIndex() == 1) {
			TableDataDictionary dictionary = this.autoBuildPane.update();
			te.setAutoBuild(true);
			te.setDictionary(dictionary);
			te.setNodeOrDict(dictionary);
		} else {
			te.setAutoBuild(false);
			NameObject no = this.controlPane.update();
			if (no != null) {
				TreeEditor editor = (TreeEditor) no.getObject();
				te.setAllowBlank(editor.isAllowBlank());
				te.setEnabled(editor.isEnabled());
				te.setDirectEdit(editor.isDirectEdit());
				te.setErrorMessage(editor.getErrorMessage());
				te.setWidgetName(editor.getWidgetName());
				te.setVisible(editor.isVisible());
				te.setWaterMark(editor.getWaterMark());
				te.setRemoveRepeat(editor.isRemoveRepeat());
				te.setTreeAttr(editor.getTreeAttr());
				te.setTreeNodeAttr(editor.getTreeNodeAttr());
				te.setNodeOrDict(editor.getTreeNodeAttr());
			}
		}
		return te;
	}

	/**
	 * ���ڵ����Ե�update
	 * @return
	 */
	public Object updateTreeNodeAttrs() {
		if(buildBox.getSelectedIndex() == 0) {
			NameObject no = controlPane.update();
			if (no != null) {
				return no.getObject();
			}
		} else {
			return autoBuildPane.update();
		}
		return null;
	}

	/**
	 * ��������update
	 * @return
	 */
	public TreeComboBoxEditor updateTreeComboBox() {
		TreeComboBoxEditor tcb = new TreeComboBoxEditor();
		if (buildBox.getSelectedIndex() == 1) {
			TableDataDictionary dictionary = this.autoBuildPane.update();
			tcb.setAutoBuild(true);
			tcb.setDictionary(dictionary);
			tcb.setNodeOrDict(dictionary);
		} else {
			tcb.setAutoBuild(false);
			NameObject no = this.controlPane.update();
			if (no != null) {
				if (no.getObject() instanceof TreeComboBoxEditor) {
					return (TreeComboBoxEditor) no.getObject();
				}

				TreeEditor editor = (TreeEditor) no.getObject();
				tcb.setAllowBlank(editor.isAllowBlank());
				tcb.setEnabled(editor.isEnabled());
				tcb.setDirectEdit(editor.isDirectEdit());
				tcb.setErrorMessage(editor.getErrorMessage());
				tcb.setWidgetName(editor.getWidgetName());
				tcb.setVisible(editor.isVisible());
				tcb.setWaterMark(editor.getWaterMark());
				tcb.setRemoveRepeat(editor.isRemoveRepeat());
				tcb.setTreeAttr(editor.getTreeAttr());
				tcb.setTreeNodeAttr(editor.getTreeNodeAttr());
				tcb.setNodeOrDict(editor.getTreeNodeAttr());
			}
		}
		return tcb;
	}

	/**
	 *
	 * @param nodeOrDict
	 */
	public void populate(Object nodeOrDict) {
		if(nodeOrDict instanceof TreeNodeAttr[]) {
			buildBox.setSelectedIndex(0);
			NameObject no = new NameObject("name", nodeOrDict);
			controlPane.populate(no);
		} else if(nodeOrDict instanceof TableDataDictionary) {
			buildBox.setSelectedIndex(1);
			autoBuildPane.populate((TableDataDictionary)nodeOrDict);
		}
	}
}
