/**
 * 
 */
package com.fr.design.mainframe.bbs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.fr.base.FRContext;
import com.fr.design.DesignerEnvManager;
import com.fr.design.dialog.BasicPane;
import com.fr.design.mainframe.DesignerContext;
import com.fr.general.DateUtils;
import com.fr.general.FRLogger;
import com.fr.general.GeneralUtils;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;


/**
 * @author neil
 *
 * @date: 2015-3-5-����11:19:50
 */
public class UserInfoPane extends BasicPane{
	
	//Ĭ��δ��¼��ɫ
	private static final Color UN_LOGIN_BACKGROUND = new Color(210, 210, 210);
	private static final Color LOGIN_BACKGROUND = new Color(184, 220, 242);
	private static final int WIDTH = 104;
	private static final int HEIGHT = 24;
	
	// ��¼�򵯳����ʱ��
	private static final int LOGIN_DIFF_DAY = 7;
	// �ȴ����ʻ�����س�ʼ���������֮���ٵ�����¼��
	private static final int WAIT_TIME = 10000;
	
	private UserInfoLabel userInfoLabel;
	private ExitLabel switchAccountLabel;


	public UserInfoLabel getUserInfoLabel() {
		return userInfoLabel;
	}

	public void setUserInfoLabel(UserInfoLabel userInfoLabel) {
		this.userInfoLabel = userInfoLabel;
	}

	public ExitLabel getSwitchAccountLabel() {
		return switchAccountLabel;
	}

	public void setSwitchAccountLabel(ExitLabel switchAccountLabel) {
		this.switchAccountLabel = switchAccountLabel;
	}
	
	/**
	 * ���캯��
	 */
	public UserInfoPane() {
		this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
		this.setLayout(new BorderLayout());
		
		this.userInfoLabel = new UserInfoLabel(this);
		this.switchAccountLabel = new ExitLabel(this);
		
		this.markUnSignIn();
		autoLogin();
		autoPushLoginDialog();
		
		this.add(userInfoLabel, BorderLayout.CENTER);
		this.add(switchAccountLabel, BorderLayout.EAST);
	}
	
	// ��̨�Զ���¼
	private void autoLogin(){
		Thread bbsAutoLoginThread = new Thread(new Runnable() {

			@Override
			public void run() {
				String username = DesignerEnvManager.getEnvManager().getBBSName();
				String password = DesignerEnvManager.getEnvManager().getBBSPassword();
				if(!BBSLoginDialog.login(username, password)){
					markUnSignIn();
				}else{
					markSignIn(username);
				}
			}
		});
		bbsAutoLoginThread.start();
	}
	
	// ����xml������ϴε���ʱ��͵�ǰʱ���ʱ���
	private int getDiffFromLastLogin(){
		String lastBBSTime = DesignerEnvManager.getEnvManager().getLastShowBBSTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date lastBBSDate = null;
		try {
			if(lastBBSTime != null){
				synchronized (this) {
					lastBBSDate = sdf.parse(lastBBSTime);
				}
				Calendar calender = Calendar.getInstance();
				calender.setTime(lastBBSDate);
				int dayOld = calender.get(Calendar.DAY_OF_YEAR);
				calender.setTime(new Date());
				int dayNew = calender.get(Calendar.DAY_OF_YEAR);
				return dayNew - dayOld;
			}
		} catch (ParseException e) {
			FRLogger.getLogger().error(e.getMessage());
		}
		return 1;
	}
	
	private void autoPushLoginDialog(){
		Thread showBBSThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					// �ȹ��ʻ��ȼ�������������̵߳�����¼��
					Thread.sleep(WAIT_TIME);
					if(StringUtils.isEmpty(GeneralUtils.readBuildNO())){
						return;
					}

					//���쵯һ��, ���xml�к͵�ǰʱ�����С��7��, �Ͳ�����
					if(getDiffFromLastLogin() < LOGIN_DIFF_DAY){
						return;
					}

				} catch (InterruptedException e) {
					FRContext.getLogger().error(e.getMessage());
				}
				
				String userName = DesignerEnvManager.getEnvManager().getBBSName();
				if(StringUtils.isNotEmpty(userName)){
					return;
				}
				
				BBSLoginDialog bbsLoginDialog = userInfoLabel.getBbsLoginDialog();
				if(bbsLoginDialog == null){
					bbsLoginDialog = new BBSLoginDialog(DesignerContext.getDesignerFrame(),userInfoLabel);
					userInfoLabel.setBbsLoginDialog(bbsLoginDialog);
				}
				
				bbsLoginDialog.showWindow();
				DesignerEnvManager.getEnvManager().setLastShowBBSTime(DateUtils.DATEFORMAT2.format(new Date()));
			}
			
		});
		showBBSThread.start();
	}
	
	/**
	 * ��־δ��¼״̬, �������Ϊ��ɫ
	 * 
	 */
	public void markUnSignIn(){
		this.userInfoLabel.setText(Inter.getLocText("FR-Base_UnSignIn"));
		this.switchAccountLabel.setVisible(false);
		this.userInfoLabel.setOpaque(true);
		this.userInfoLabel.setBackground(UN_LOGIN_BACKGROUND);
		this.userInfoLabel.resetUserName();
		
	}
	
	/**
	 * ��־��½״̬, �������Ϊ��ɫ
	 * @param userName �û���
	 * 
	 */
	public void markSignIn(String userName){
		this.userInfoLabel.setText(userName);
		this.userInfoLabel.setUserName(userName);
		this.switchAccountLabel.setVisible(true);
		this.userInfoLabel.setOpaque(true);
		this.userInfoLabel.setBackground(LOGIN_BACKGROUND);
		this.switchAccountLabel.setOpaque(true);
		this.switchAccountLabel.setBackground(LOGIN_BACKGROUND);
	}
	
	@Override
	protected String title4PopupWindow() {
		return StringUtils.EMPTY;
	}

	
}
