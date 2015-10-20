package com.fr.design.style.color;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.JPanel;

import com.fr.base.chart.BaseChartCollection;
import com.fr.design.gui.chart.MiddleChartDialog;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;

/**
 * ��ɫѡ����������ɫ�Ի���
 * @author focus
 *
 */
public class ColorSelectDialog extends MiddleChartDialog{

	private Color color;
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	private ColorSelectDetailPane pane;
	
	private UIButton ok;
	private UIButton cancel;
	
	// �����ɫѡ�������
	private ColorSelectable seletePane;
	
	// ��ɫѡ�����
	ColorTracker okListener;

	/**
	 * construct
	 * @param owner ������
	 */
    public ColorSelectDialog(Frame owner) {
        super(owner);
        initComponent();
    }

    /**
     * construct 
     * @param owner ������
     */
    public ColorSelectDialog(Dialog owner) {
        super(owner);
        initComponent();
    }
    
    /**
     * construct
     * @param owner ������
     * @param pane ��ɫѡ����������ɫ���
     * @param initialColor ��ʼ��ɫ
     * @param okListener ��ɫѡ�����
     * @param seletePane �����ɫѡ�����
     */
    public ColorSelectDialog(Frame owner,ColorSelectDetailPane pane,Color initialColor,ActionListener okListener,ColorSelectable seletePane){
    	super(owner);
    	this.pane = pane;
    	this.color = initialColor;
    	this.okListener = (ColorTracker) okListener;
    	this.seletePane = seletePane;
    	initComponent();
    }
	
    private void initComponent() {
    	this.setLayout(new BorderLayout());
    	this.add(pane,BorderLayout.NORTH);
    	this.setBasicDialogSize(545,500);

    	
    	JPanel buttonPane = new JPanel();
    	buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
    	
    	this.add(buttonPane, BorderLayout.SOUTH);
    	
    	ok = new UIButton(Inter.getLocText("OK"));
    	cancel = new UIButton(Inter.getLocText("Cancel"));
    	
    	buttonPane.add(ok);
    	buttonPane.add(cancel);
    	
    	ok.setActionCommand("OK");
    	ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				seletePane.setColor(okListener.getColor());
				doOK();
			}
		});
    	
    	ok.addActionListener(okListener);
    	
    	cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doCancel();
			}
		});
    	
    	GUICoreUtils.setWindowCenter(getOwner(), this);
    }

	@Override
	public BaseChartCollection getChartCollection() {
		return null;
	}

	@Override
	public void populate(BaseChartCollection cc) {
		
	}

	/**
	 * ��ʾ��ɫѡ����������ɫ�Ի���
	 * @param owner ������
	 * @param pane ������ɫѡ�������
	 * @param initialColor ��ʼ��ɫ
	 * @param selectePane �����ɫѡ�������
	 * void
	 */
	public static void showDialog(Frame owner,ColorSelectDetailPane pane,Color initialColor,ColorSelectable selectePane){
		ColorTracker okListener = new ColorTracker(pane);
		ColorSelectDialog dialog = new ColorSelectDialog(owner,pane,initialColor,okListener,selectePane);
		dialog.setModal(true);
		dialog.show();
	}

	/**
	 * ������
	 */
	@Override
	public void checkValid() throws Exception {
		
	}

}

/**
 * ��ɫѡ��������
 * @author focus
 *
 */
class ColorTracker implements ActionListener, Serializable {
	ColorSelectDetailPane chooser;
    Color color;
    
    public void setColor(Color color){
    	this.color = color;
    }

    public ColorTracker(ColorSelectDetailPane c) {
        chooser = c;
    }

    public void actionPerformed(ActionEvent e) {
        color = chooser.getSelectedPanel().getSelectionModel().getSelectedColor();
    }

    public Color getColor() {
        return color;
    }
}
