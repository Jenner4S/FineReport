package com.fr.design.actions.server;

import com.fr.base.BaseUtils;
import com.fr.base.Env;
import com.fr.base.FRContext;
import com.fr.design.DesignModelAdapter;
import com.fr.design.actions.UpdateAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.design.menu.MenuKeySet;
import com.fr.design.webattr.WidgetManagerPane;
import com.fr.form.ui.WidgetManager;
import com.fr.form.ui.WidgetManagerProvider;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class WidgetManagerAction extends UpdateAction {
    public WidgetManagerAction() {
        this.setMenuKeySet(WIDGET_MANAGER);
        this.setName(getMenuKeySet().getMenuKeySetName()+ "...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_format/modified.png"));
    }

    /**
     * 动作
     * @param e 事件
     */
    public void actionPerformed(ActionEvent e) {
        final DesignerFrame designerFrame = DesignerContext.getDesignerFrame();
        final WidgetManagerProvider widgetManager = WidgetManager.getProviderInstance();
        final WidgetManagerPane widgetManagerPane = new WidgetManagerPane() {
            @Override
			public void complete() {
                populate(widgetManager);
            }
        };

        BasicDialog widgetConfigDialog = widgetManagerPane.showLargeWindow(designerFrame,new DialogActionAdapter() {
            @Override
			public void doOk() {
                widgetManagerPane.update(widgetManager);

                Env currentEnv = FRContext.getCurrentEnv();
                try {
                    currentEnv.writeResource(widgetManager);
                    //marks: 由于这个面板还改变权限相关的操作，所以这个时候还要操作权限配置
//					currentEnv.writeResource(FRContext.getPrivilegeManager());
                } catch (Exception ex) {
                    FRContext.getLogger().error(ex.getMessage(), ex);
                }
                DesignModelAdapter model = DesignModelAdapter.getCurrentModelAdapter();
                if (model != null) {
					model.widgetConfigChanged();
				}
                designerFrame.getSelectedJTemplate().refreshToolArea();
            }
        });

        widgetConfigDialog.setVisible(true);
    }

    @Override
	public void update() {
        this.setEnabled(true);
    }

    public static final MenuKeySet WIDGET_MANAGER = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'W';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("ServerM-Widget_Manager");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };
}