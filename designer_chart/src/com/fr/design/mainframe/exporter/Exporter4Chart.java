package com.fr.design.mainframe.exporter;
import com.fr.design.mainframe.JChart;

import java.io.OutputStream;

/**
 * ͼ�������crt�ļ��ĵ��������������ļ��Ľӿ�
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-10-21
 * Time: ����7:14
 */
public interface Exporter4Chart {

    /**
   	 * �����crt������Ŀ���ļ�
   	 *
   	 * @param out      �����
   	 * @param chart    chart�ļ�
   	 * @throws Exception ����ʧ�����׳����쳣
   	 */
   	public void export(OutputStream out, JChart chart) throws Exception;

}
