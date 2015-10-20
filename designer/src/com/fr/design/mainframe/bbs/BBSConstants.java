/**
 * 
 */
package com.fr.design.mainframe.bbs;

import com.fr.stable.StringUtils;

import java.util.Properties;

/**
 * @author neil
 *
 * @date: 2015-3-10-����9:50:13
 */
public class BBSConstants {

	//��ȡ��ǰ��½�û�δ��ȡ��Ϣ����
	public static final String GET_MESSAGE_URL = loadAttribute("GET_MESSAGE_URL", "http://bbs.finereport.com?op=bbs&cmd=checkMessageCount");
	//Ĭ�ϴ򿪵���̳����
	public static final String DEFAULT_URL = loadAttribute("DEFAULT_URL", "http://bbs.finereport.com/home.php?mod=space&do=pm");
	//Ĭ��ģ������url
	public static final String SHARE_URL = loadAttribute("SHARE_URL", "http://bbs.finereport.com/");
	//�ռ����������Ϣurl
	public static final String COLLECT_URL = loadAttribute("COLLECT_URL", "http://bbs.finereport.com/");
	//������֤������
	public static final String VERIFY_URL = loadAttribute("VERIFY_URL", "http://bbs.finereport.com/");
    //��ȡ��̳������Ϣ, �ж��Ƿ���Ҫ����
    public static final String UPDATE_INFO_URL = loadAttribute("UPDATE_INFO_URL", "http://bbs.finereport.com/");
    //��̳�ֻ���
    public static final String BBS_MOBILE_MOD = loadAttribute("BBS_MOBILE_MOD", "http://bbs.finereport.com/forum.php?mobile=1");
    //�ж��Ƿ���µĹؼ���
    public static final String UPDATE_KEY = loadAttribute("UPDATE_KEY", "newIsPopup");
   

	private static final String GUEST_KEY = "USER";
	private static final String LINK_KEY = "LINK";
	private static final int GUEST_NUM = 5;
	
	//�û�����Ϣ����
	public static final String[] ALL_GUEST = loadAllGuestsInfo(GUEST_KEY);
	//�û���̳������Ϣ
	public static final String[] ALL_LINK = loadAllGuestsInfo(LINK_KEY);
	
	private static Properties PROP = null;
	
	//���������û�����Ϣ, �û���, ��̳����
	private static String[] loadAllGuestsInfo(String key){
		String[] allGuests = new String[GUEST_NUM];
		for (int i = 0; i < GUEST_NUM; i++) {
			allGuests[i] = loadAttribute(key + i, StringUtils.EMPTY);
		}
		
		return allGuests;
	}
	//���Ҫ����, ֱ�Ӹ�bbs.properties������
	private static String loadAttribute(String key, String defaultValue) {
		if (PROP == null) {
			PROP = new Properties();
			try {
				PROP.load(BBSConstants.class.getResourceAsStream("/com/fr/design/mainframe/bbs/bbs.properties"));
			} catch (Exception e) {
			}
		}

		String p = PROP.getProperty(key);
		if (StringUtils.isEmpty(p)) {
			p = defaultValue;
		}
		return p;
	}
	
}
