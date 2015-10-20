/**
 * 
 */
package com.fr.design.mainframe.bbs;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;



import com.fr.base.BaseUtils;
import com.fr.design.DesignerEnvManager;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.imenu.UIMenuItem;
import com.fr.design.gui.imenu.UIPopupMenu;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;

/**
 * @author neil
 *
 * @date: 2015-3-5-����11:52:58
 */
public class ExitLabel extends UILabel{
	
	private static final int MENU_HEIGHT = 20;
	
	private UserInfoPane infoPane;

	/**
	 * ���캯��
	 */
	public ExitLabel(UserInfoPane infoPane) {
		this.infoPane = infoPane;
		this.setIcon(BaseUtils.readIcon("/com/fr/design/mainframe/bbs/images/switch.png"));
		this.addMouseListener(mouseListener);
	}
	
	private MouseAdapter mouseListener = new MouseAdapter() {
		
		@Override
		public void mouseClicked(MouseEvent e) {
			UIPopupMenu menu = new UIPopupMenu();
			menu.setOnlyText(true);
			menu.setPopupSize(infoPane.getWidth(),infoPane.getHeight());
			
			UIMenuItem closeOther = new UIMenuItem(Inter.getLocText("FR-Designer-BBSLogin_Switch-Account"));
			closeOther.addMouseListener(new MouseAdapter() {
				
				public void mousePressed(MouseEvent e) {
					clearLoingInformation();
					updateInfoPane();
				};
				
			});
			menu.add(closeOther);
			
			GUICoreUtils.showPopupMenu(menu, ExitLabel.this, 0, MENU_HEIGHT);
		}
		
		private void clearLoingInformation(){
			DesignerEnvManager.getEnvManager().setBBSName(StringUtils.EMPTY);
			DesignerEnvManager.getEnvManager().setBBSPassword(StringUtils.EMPTY);
		}
		
		private void updateInfoPane(){
			infoPane.markUnSignIn();
			ExitLabel.this.setVisible(false);
			BBSLoginDialog loginDialog = infoPane.getUserInfoLabel().getBbsLoginDialog(); 
			if(loginDialog == null){
				loginDialog = new BBSLoginDialog(DesignerContext.getDesignerFrame(), infoPane.getUserInfoLabel());
				infoPane.getUserInfoLabel().setBbsLoginDialog(loginDialog);
				loginDialog.showWindow();
			}
			loginDialog.getTipLabel().setVisible(false);
			loginDialog.setModal(true);
			loginDialog.clearLoginInformation();
			loginDialog.setVisible(true);
		}
	};
}
