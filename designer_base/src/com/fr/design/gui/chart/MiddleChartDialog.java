package com.fr.design.gui.chart;

import java.awt.Dialog;
import java.awt.Frame;

import com.fr.base.chart.BaseChartCollection;
import com.fr.design.dialog.BasicDialog;

/**
 *  * �����, ���ڶ๤�̼�Э��, ��װһ�� ͼ���½��ĶԻ���, ������Ա�ȷ��: �ȵ���ֻҪһ��ͼ�����͵ĶԻ���.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2013-7-10 ����10:07:56
 */
public abstract class MiddleChartDialog extends BasicDialog{

	public MiddleChartDialog(Dialog parent) {
		super(parent);
	}
	
    public MiddleChartDialog(Frame owner) {
        super(owner);
    }
    
    public abstract BaseChartCollection getChartCollection();
    
    public abstract void populate(BaseChartCollection cc);

}
