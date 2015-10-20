/**
 * 
 */
package com.fr.design.designer.creator.cardlayout;

import java.awt.Dimension;
import java.awt.event.MouseEvent;

import javax.swing.border.Border;

import com.fr.design.designer.beans.AdapterBus;
import com.fr.design.designer.beans.ComponentAdapter;
import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.designer.creator.XWBorderLayout;
import com.fr.design.designer.creator.XWidgetCreator;
import com.fr.design.mainframe.EditingMouseListener;
import com.fr.design.mainframe.FormDesigner;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WBorderLayout;
import com.fr.form.ui.container.cardlayout.WCardTitleLayout;

/**
 *
 *
 * @date: 2014-12-1-����7:26:21
 */
public class XWCardTitleLayout extends XWBorderLayout {
	
	private static final int CENTER = 1;

	/**
	 * ���캯��
	 */
	public XWCardTitleLayout() {
	
	}
	
	/**
	 * ���캯��
	 */
	public XWCardTitleLayout(WCardTitleLayout widget, Dimension initSize) {
		super(widget, initSize);
	}
	
    /**
     * ��WLayoutת��ΪXLayoutContainer
     */
	public void convert(){
        isRefreshing = true;
        WCardTitleLayout titleLayout = (WCardTitleLayout)this.toData();
        this.setVisible(titleLayout.isVisible());
        this.removeAll();
        String[] arrs = {WBorderLayout.NORTH, WBorderLayout.SOUTH, WBorderLayout.EAST, WBorderLayout.WEST, WBorderLayout.CENTER};
        for (int i = 0; i < arrs.length; i++) {
            Widget wgt = titleLayout.getLayoutWidget(arrs[i]);
            if (wgt != null) {
                XWidgetCreator comp = (XWidgetCreator) XCreatorUtils.createXCreator(wgt, calculatePreferredSize(wgt));
                this.add(comp, arrs[i]);
                comp.setBackupParent(this);
            }
        }
        isRefreshing = false;
	}
	
	/**
	 * ��ȡ��ǩ����
	 * 
	 * @return ��ǩ����
	 * 
	 *
	 * @date 2014-12-10-����1:50:04
	 * 
	 */
	public XWCardTagLayout getTagPart(){
		return (XWCardTagLayout) this.getComponent(CENTER);
	}
	
	/**
	 * ��ӱ�ǩ����
	 * 
	 * @param tagPart ��ǩ����
	 * 
	 *
	 * @date 2014-12-10-����1:49:40
	 * 
	 */
	public void addTagPart(XWCardTagLayout tagPart){
		this.add(tagPart, WBorderLayout.CENTER);
	}
	
	/**
	 * ����½���ť
	 * 
	 * @param addBtn �½���ť
	 * 
	 *
	 * @date 2014-12-10-����1:49:19
	 * 
	 */
	public void addNewButton(XCardAddButton addBtn){
		this.add(addBtn, WBorderLayout.EAST);
	}
	
    /**
     * �л��������״̬
     * 
     * @return designer �������
     */
    public void stopAddingState(FormDesigner designer){
    	designer.stopAddingState();
    	return;
    }
    
    /**
     * �ò������أ�����Ա߿���в���
     * @param border �߿�
     * 
     */
    public void setBorder(Border border) {
       return;
    }
    
    @Override
	/**
	 * �ò������أ�����ò���ʱѡ����Ӧ��tab��������
	 * @param editingMouseListener ����
	 * @param e ������¼�
	 * 
	 */
    public void respondClick(EditingMouseListener editingMouseListener,
    		MouseEvent e) {
		FormDesigner designer = editingMouseListener.getDesigner();
		SelectionModel selectionModel = editingMouseListener.getSelectionModel();

		XWCardMainBorderLayout mainLayout = (XWCardMainBorderLayout) this.getBackupParent();
		if(mainLayout != null){
			XWCardLayout cardLayout = mainLayout.getCardPart();
			selectionModel.setSelectedCreator(cardLayout);
		}
		
		if (editingMouseListener.stopEditing()) {
			if (this != designer.getRootComponent()) {
				ComponentAdapter adapter = AdapterBus.getComponentAdapter(designer, this);
				editingMouseListener.startEditing(this, adapter.getDesignerEditor(), adapter);
			}
		}
    }
}
