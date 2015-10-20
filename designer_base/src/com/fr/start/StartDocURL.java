package com.fr.start;

import java.awt.Desktop;
import java.net.URI;

import com.fr.base.FRContext;
import com.fr.stable.StableUtils;


/**
 * ��windows��ʼ�˵����exe�ļ�����html��ʽ�İ����ĵ�,
 * ��Ҫ��Ϊ��install4j����.exe�ļ��õ�.
 * Ϊʲô��ֱ����install4j��������*.htm�ļ��أ�
 * ��Ҫ����install4j��������*.htmֻ�е�һ�λ��¿�һ����������Ժ�Ļ�ֱ�Ӹı�
 * ��һ���¿�������������ݣ�������������Ŀǰ���ĵ��Ƕ���ֿ��ģ��û������ͬʱ
 * ������ĵ�����û�а취�ˣ����Ծ�д�����.class�ļ�.
 */
public class StartDocURL {
	//
	public static void main(String[] args) {
		//p:������·����URL����
		if(args == null || args.length < 1) {
    		FRContext.getLogger().error(
			"Can not find the install home, please check it.");
    		return;			
		}

    	try {
    		//p: �ж��Ƿ���httpЭ��.
    		if(args[0].toLowerCase().trim().startsWith("http")) {
				//p:���������
				Desktop.getDesktop().browse(new URI(args[0]));
			} else {
	    		String iHome = StableUtils.getInstallHome();
	        	if (iHome == null) {
	        		FRContext.getLogger().error(
	        				"Can not find the install home, please check it.");
	        		return;
	        	}

				//p:���������,�������ļ�
	    		Desktop.getDesktop().open(new java.io.File(iHome + args[0]));
			}    		
    	} catch (Exception e) {
    		FRContext.getLogger().error(e.getMessage(), e);
    	}
	}
}