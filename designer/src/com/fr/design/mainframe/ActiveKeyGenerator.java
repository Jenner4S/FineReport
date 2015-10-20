package com.fr.design.mainframe;
import com.fr.base.BaseUtils;
import com.fr.design.DesignerEnvManager;
import com.fr.design.mainframe.bbs.BBSConstants;
import com.fr.general.http.HttpClient;
import com.fr.stable.StringUtils;
import com.fr.stable.core.UUID;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * 	�޶���"-"�ָ�õ����鳤��=4;
 *	���ٰ���10�����ϲ�ͬ���ַ�
 *	ÿ7λ�ַ�, Ҫ����Ա�7����
 *	�޶�ͬһ���ַ�, �����Գ���10������
 *	�޶����ܳ���7���ַ���ȫ����ĸ�����
 * 
 * @author neil
 *
 * @date: 2015-4-8-����8:49:05
 */
public class ActiveKeyGenerator {
	
	//û�������, ������֤����, �´�������ȥ��֤
	public static final int AUTH_ERROR = -1;
	//��֤�ɹ�
	public static final int AUTH_SUCCESS = 0;
	//��֤ʧ��
	public static final int AUTH_FAILED = 1;

	private static final int CONNECT_LEN = 4;
	private static final int KEY_LEN = 5;
	private static final int MIN_NUM_COUNT = 10;
	private static final int MAGIC_NUM = 7;
	private static final String SPLIT_CHAR = "-";
	private static final int MAX_TRY_COUNT = 100;
	
	/**
	 * ���ɼ�����, ���ڴ��ϵļ����������µ�
	 * 
	 * @return 8.0�µļ�����
	 * 
	 */
	public static String generateActiveKey(){
		for (int i = 0; i < MAX_TRY_COUNT; i++) {
			String key = UUID.randomUUID().toString();
			
			char[] keyChar = key.toCharArray();
			int len = keyChar.length;
			int[] numArray = new int[len];
			
			if(invalidEachCharCount(len, keyChar, numArray)){
				continue;
			}
			
			if(isCharAllNum(len, numArray, keyChar)){
				continue;
			}
			
			String activeKey = new String(keyChar);
			//�ܳ�����key, �Լ���ȥ��֤��
			if(!localVerify(activeKey)){
				continue;
			}
			return activeKey;
		}
		
		return StringUtils.EMPTY;
	}
	
	/**
	 * ��֤key�Ϸ���, �������غ�������֤
	 * 
	 * @param key ������
	 * @param timeout ��֤��ʱʱ��
	 * 
	 * @return �Ƿ�Ϸ�
	 * 
	 */
	public static boolean verify(String key, int timeout){
		return localVerify(key) && onLineVerify(key, timeout);
	}
	
	/**
	 * ����У�鼤����(��ʱҲ����֤ͨ��, ���ǻ����´�����ʱ������֤)
	 * 
	 * @param key ������
	 * 
	 * @return �Ƿ���֤ͨ��
	 * 
	 */
	public static boolean onLineVerify(String key){
		return onLineVerify(key, -1);
	}
	
	//׼����֤��HttpClient
	private static HttpClient prepareVerifyConnect(DesignerEnvManager envManager, int timeout, String key){
		HashMap<String, String> para = new HashMap<String, String>();
		para.put("uuid", envManager.getUUID());
		para.put("key", key);
		para.put("username", envManager.getBBSName());
		HttpClient hc = new HttpClient(BBSConstants.VERIFY_URL, para);
		if (timeout != -1) {
			hc.setTimeout(timeout);
		}
		
		return hc;
	}
	
	/**
	 * ����У�鼤����(��ʱҲ����֤ͨ��, ���ǻ����´�����ʱ������֤)
	 * 
	 * @param key ������
	 * @param timeout ��ʱʱ��
	 * 
	 * @return �Ƿ���֤ͨ��
	 * 
	 */
	public static boolean onLineVerify(String key, int timeout){
		DesignerEnvManager envManager = DesignerEnvManager.getEnvManager();
		HttpClient hc = prepareVerifyConnect(envManager, timeout, key);

        //�ȹرյ�������֤, ����������ס, �������ܲ�������������, ȷ���������ڿ���.
		if (true || !hc.isServerAlive()) {
			//�����Ϸ������Ļ�, ����ͨ��, �´������������������֤
			return true;
		}
		
		boolean res = Boolean.valueOf(hc.getResponseText());
		if (res) {
			//������֤ͨ����, �Ͱ���֤ͨ����״̬������, �´ξͲ���������֤��.
			envManager.setActiveKeyStatus(AUTH_SUCCESS);
		} else {
			//���û��֤ͨ��, ��յ���ǰactivekey.����������, �������뼤����
			envManager.setActivationKey(StringUtils.EMPTY);
		}
		
		return res;
	}
	
	/**
	 * ����У�鼤����
	 * 
	 * @param key ������
	 * 
	 * @return �Ƿ���֤ͨ��
	 * 
	 */
	public static boolean localVerify(String key){
		if(StringUtils.isEmpty(key) || invalidSplitLength(key)){
			return false;
		}
		char[] keyChar = key.toCharArray();
		int len = keyChar.length;
		int[] numArray = new int[len];
		
		if(invalidEachCharCount(len, keyChar, numArray)){
			return false;
		}
		
		int count = len / MAGIC_NUM;
		if (count != KEY_LEN) {
			return false;
		}
		//�������
		return validRemain(count, numArray);
	}
	
	//�Ƿ�ȫ��ĸ
	private static boolean isCharAllNum(int len, int[] numArray, char[] keyChar){
		int count = len / MAGIC_NUM;
		for (int j = 0; j < count; j++) {
			int temp = 0;
			for (int k = 0; k < MAGIC_NUM; k++) {
				temp = temp + numArray[k + j * MAGIC_NUM];
			}
			if(temp == 0){
				return true;
			}
			
			updateRemain(temp, numArray, j, keyChar);
		}
		
		return false;
	}
	
	//�Ƿ���Ϲ涨��split��ʽ
	private static boolean invalidSplitLength(String key){
		return key.split(SPLIT_CHAR).length != CONNECT_LEN;
	}
	
	//��ȡchar��Ӧ��intֵ
	private static int getCharIntValue(char charStr){
		if (!BaseUtils.isNum(charStr)) {
			return 0;
		}
		return Character.getNumericValue(charStr);
	}
	
	//У������
	private static boolean validRemain(int count, int[] numArray){
		for (int j = 0; j < count; j++) {
			int temp = 0;
			for (int k = 0; k < MAGIC_NUM; k++) {
				temp = temp + numArray[k + j * MAGIC_NUM];
			}
			if (temp == 0){
				return false;
			}
			
			if(temp % MAGIC_NUM != 0){
				return false;
			}
		}
		
		return true;
	}
	
	//�ж��Ƿ���ڲ��Ϸ�(����)��ĳһ���ַ�
	private static boolean invalidEachCharCount(int len, char[] keyChar, int[] numArray){
		HashMap<Character, Integer> hs = new HashMap<Character, Integer>();
		for (int j = 0; j < len; j++) {
			int count = hs.containsKey(keyChar[j]) ? hs.get(keyChar[j]) + 1 : 1;
			hs.put(keyChar[j], count);
			numArray[j] = getCharIntValue(keyChar[j]);
		}
		
		if (hs.size() <= MIN_NUM_COUNT) {
			return true;
		}
		
		Iterator<Entry<Character, Integer>> it = hs.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Character, Integer> entry = it.next();
			if(entry.getValue() > MIN_NUM_COUNT){
				return true;
			}
		}
		
		return false;
	}
	
	//��������
	private static void updateRemain(int temp, int[] numArray, int j, char[] keyChar){
		//����
		int remain = temp % MAGIC_NUM;
		int lastMagicIndex = MAGIC_NUM - 1 + j * MAGIC_NUM;
		int newNum = numArray[lastMagicIndex] - remain;
		while(newNum <= 0){
			newNum += MAGIC_NUM;
		}
		
		keyChar[lastMagicIndex] = (char)(newNum + '0');
	}

    public static void main(String[] args) {
        String a = "671b3b43-cfb4-40e7-9ca8-fe71ba33b8e3";

        String key = generateActiveKey();

        boolean verify = localVerify(key);
        System.out.println(verify);
    }
}
