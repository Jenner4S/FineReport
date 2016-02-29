package com.fr.design.actions.server;

import com.fr.base.BaseUtils;
import com.fr.base.ConfigManager;
import com.fr.base.Env;
import com.fr.base.FRContext;
import com.fr.design.actions.UpdateAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.design.menu.MenuKeySet;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.event.ActionEvent;


/**
 * StyleList Action
 */
public class StyleListAction extends UpdateAction {
	public StyleListAction() {
        this.setMenuKeySet(PREDEFINED_STYLES);
        this.setName(getMenuKeySet().getMenuKeySetName()+ "...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
		this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_web/style.png"));
	}

    /**
     * 动作
     * @param evt 事件
     */
	public void actionPerformed(ActionEvent evt) {
		DesignerFrame designerFrame = DesignerContext.getDesignerFrame();
		final StyleManagerPane styleListPane = new StyleManagerPane();
		BasicDialog styleListDialog = styleListPane.showWindow(designerFrame);
		styleListDialog.addDialogActionListener(new DialogActionAdapter() {
			@Override
			public void doOk() {
				styleListPane.update(ConfigManager.getProviderInstance());
				//marks:保存数据
				Env currentEnv = FRContext.getCurrentEnv();
				try {
					currentEnv.writeResource(ConfigManager.getProviderInstance());
				} catch (Exception e) {
					FRContext.getLogger().error(e.getMessage(), e);
				}	
			}                
        });

        styleListPane.populate(ConfigManager.getProviderInstance());
		styleListDialog.setVisible(true);
	
	}
	
	@Override
	public void update() {
		this.setEnabled(true);
	}

	public static final MenuKeySet PREDEFINED_STYLES = new MenuKeySet() {
		@Override
		public char getMnemonic() {
			return 'K';
		}

		@Override
		public String getMenuName() {
			return Inter.getLocText("ServerM-Predefined_Styles");
		}

		@Override
		public KeyStroke getKeyStroke() {
			return null;
		}
	};
}