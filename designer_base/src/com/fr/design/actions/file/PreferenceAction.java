package com.fr.design.actions.file;

import java.awt.event.ActionEvent;

import com.fr.design.DesignerEnvManager;
import com.fr.design.actions.UpdateAction;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;


public class PreferenceAction extends UpdateAction {
	public PreferenceAction() {
        this.setMenuKeySet(KeySetUtils.PREFERENCE);
        this.setName(getMenuKeySet().getMenuKeySetName()+"...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
	}

    /**
     * 动作
     * @param e 事件
     */
	public void actionPerformed(ActionEvent e) {
		final DesignerFrame designerFrame = DesignerContext.getDesignerFrame();
		
		final PreferencePane preferencePane = new PreferencePane();
		preferencePane.populate(DesignerEnvManager.getEnvManager());
		
		BasicDialog basicDialog = preferencePane.showWindow(designerFrame);
		basicDialog.addDialogActionListener(new DialogActionAdapter() {
			public void doOk() {
				preferencePane.update(DesignerEnvManager.getEnvManager());
				DesignerEnvManager.loadLogSetting();
				DesignerEnvManager.getEnvManager().saveXMLFile();
				JTemplate jt = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
				jt.refreshToolArea();
			}
		});	
		
		basicDialog.setVisible(true);
	}
}