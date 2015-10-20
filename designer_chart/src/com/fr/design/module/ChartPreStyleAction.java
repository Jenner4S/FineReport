package com.fr.design.module;

import com.fr.base.BaseUtils;
import com.fr.base.ChartPreStyleManagerProvider;
import com.fr.base.ChartPreStyleServerManager;
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
 * ͼ��Ԥ������ʽAction.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2013-8-20 ����04:38:48
 */
public class ChartPreStyleAction extends UpdateAction {

	public ChartPreStyleAction() {
        this.setMenuKeySet(CHART_DEFAULT_STYLE);
        this.setName(getMenuKeySet().getMenuKeySetName()+ "...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
		this.setSmallIcon(BaseUtils.readIcon("com/fr/design/images/chart/ChartType.png"));
	}

    /**
     * ����
     * @param e �¼�
     */
	public void actionPerformed(ActionEvent e) {
		DesignerFrame designerFrame = DesignerContext.getDesignerFrame();
		final ChartPreStyleManagerPane pane = new ChartPreStyleManagerPane();
		BasicDialog dialog = pane.showWindow(designerFrame);
		dialog.addDialogActionListener(new DialogActionAdapter() {
			@Override
			public void doOk() {
				pane.updateBean();
				ChartPreStyleManagerProvider manager = ChartPreStyleServerManager.getProviderInstance();
				manager.setStyleEditing(null);
			}                
			
			@Override
			public void doCancel() {
				ChartPreStyleManagerProvider manager = ChartPreStyleServerManager.getProviderInstance();
				manager.setStyleEditing(null);
			}
        });

		pane.populateBean();
		dialog.setVisible(true);
		
	}

	public static final MenuKeySet CHART_DEFAULT_STYLE = new MenuKeySet() {
		@Override
		public char getMnemonic() {
			return 'C';
		}

		@Override
		public String getMenuName() {
			return Inter.getLocText("FR-Menu-Server_Chart_PreStyle");
		}

		@Override
		public KeyStroke getKeyStroke() {
			return null;
		}
	};
}
