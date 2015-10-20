package com.fr.design.chart;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import com.fr.base.chart.BaseChartCollection;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.gui.chart.MiddleChartDialog;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.dialog.BasicDialog;
import com.fr.general.Inter;
import com.fr.design.utils.gui.GUICoreUtils;

/**
 * ��װһ�� ͼ���½��ĶԻ���, ������Ա�ȷ��: �ȵ���ֻҪһ��ͼ�����͵ĶԻ���.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2013-1-7 ����07:29:15
 */
public class ChartDialog extends MiddleChartDialog {
	
	private BaseChartCollection cc;
	
	private UIButton ok;
	private UIButton cancel;

    public ChartDialog(Frame owner) {
        super(owner);
        initComponent();
    }

    public ChartDialog(Dialog owner) {
        super(owner);
        initComponent();
    }
	
    private void initComponent() {
    	this.setLayout(new BorderLayout());
    	final ChartTypePane chartTypePane = new ChartTypePane();
    	setTitle(Inter.getLocText("M-Popup_ChartType"));

        this.applyClosingAction();
        this.applyEscapeAction();
    	this.setBasicDialogSize(BasicDialog.DEFAULT);
    	this.add(chartTypePane, BorderLayout.CENTER);
    	
    	JPanel buttonPane = new JPanel();
    	buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
    	
    	this.add(buttonPane, BorderLayout.SOUTH);
    	
    	ok = new UIButton(Inter.getLocText("OK"));
    	cancel = new UIButton(Inter.getLocText("Cancel"));
    	
    	buttonPane.add(ok);
    	buttonPane.add(cancel);
    	
    	ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chartTypePane.update((ChartCollection)cc);
				doOK();
			}
		});
    	
    	cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doCancel();
			}
		});
    	
    	GUICoreUtils.setWindowCenter(getOwner(), this);
    }

    /**
     * ������
     */
	public void checkValid() throws Exception {
		
	}
	
	/**
	 * �����½���ͼ�� ChartCollection
	 */
    public void populate(BaseChartCollection cc) {
        if (cc == null) {
            return;
        }
        this.cc = cc;
    }

    /**
     * ���ص�ǰ���ڱ༭��ͼ��ChartCollection
     */
    public BaseChartCollection getChartCollection() {
        return this.cc;
    }
}
