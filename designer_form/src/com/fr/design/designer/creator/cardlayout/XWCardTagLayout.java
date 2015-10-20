/**
 * 
 */
package com.fr.design.designer.creator.cardlayout;

import java.awt.Dimension;
import java.awt.event.ContainerEvent;
import java.awt.event.MouseEvent;

import javax.swing.border.Border;

import com.fr.design.designer.beans.AdapterBus;
import com.fr.design.designer.beans.ComponentAdapter;
import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.designer.creator.XWHorizontalBoxLayout;
import com.fr.design.designer.creator.XWidgetCreator;
import com.fr.design.mainframe.EditingMouseListener;
import com.fr.design.mainframe.FormDesigner;
import com.fr.form.ui.CardSwitchButton;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WCardLayout;
import com.fr.form.ui.container.cardlayout.WCardTagLayout;
import com.fr.form.ui.container.cardlayout.WTabFitLayout;

/**
 *
 *
 * @date: 2014-11-25-����3:11:14
 */
public class XWCardTagLayout extends XWHorizontalBoxLayout {
	
	private static final int MIN_SIZE = 1;
	
	private String tagName = "Tab";
	
	//����һ��tabNameIndex��ֹtabFitLayout����
	private int tabFitIndex = 0;
	private CardSwitchButton currentCard;

	public CardSwitchButton getCurrentCard() {
		return currentCard;
	}

	public void setCurrentCard(CardSwitchButton currentCard) {
		this.currentCard = currentCard;
	}

	public int getTabFitIndex() {
		return tabFitIndex;
	}

	public void setTabFitIndex(int tabFitIndex) {
		this.tabFitIndex = tabFitIndex;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	private XWCardLayout cardLayout;
	
	public XWCardTagLayout(WCardTagLayout widget, Dimension initSize){
		super(widget, initSize);
	}
	
	/**
	 * ���캯��
	 */
	public XWCardTagLayout(WCardTagLayout widget, Dimension initSize, XWCardLayout cardLayout) {
		super(widget, initSize);
		
		this.cardLayout = cardLayout;
	}

	/**
	 * �������ļ����¼�
	 * 
	 * @param e �¼�
	 * 
	 *
	 * @date 2014-11-25-����6:20:10
	 * 
	 */
	public void componentAdded(ContainerEvent e) {
		super.componentAdded(e);
		
		if(this.cardLayout == null){
			initCardLayout();
		}
		
		int index = this.cardLayout.toData().getWidgetCount();
		//�¼�һ��card
		String widgetName = tagName+getTabNameIndex();
		WTabFitLayout fitLayout = new WTabFitLayout(widgetName,tabFitIndex,currentCard);
		fitLayout.setTabNameIndex(getTabNameIndex());
		XWTabFitLayout tabFitLayout = new XWTabFitLayout(fitLayout, new Dimension());
		tabFitLayout.setBackupParent(cardLayout);
		cardLayout.add(tabFitLayout, widgetName);
		this.cardLayout.toData().setShowIndex(index);
		cardLayout.showCard();
	}
	
	private void initCardLayout(){
		XWCardTitleLayout titleLayout = (XWCardTitleLayout)this.getBackupParent();
		XWCardMainBorderLayout borderLayout = (XWCardMainBorderLayout)titleLayout.getBackupParent();
		
		this.cardLayout = borderLayout.getCardPart();
	}
	
    /**
     * ��WLayoutת��ΪXLayoutContainer
     */
    public void convert() {
        isRefreshing = true;
        WCardTagLayout layout = (WCardTagLayout)this.toData();
        this.removeAll();
        for (int i = 0; i < layout.getWidgetCount(); i++) {
            Widget wgt = layout.getWidget(i);
            if (wgt != null) {
                XWidgetCreator comp = (XWidgetCreator) XCreatorUtils.createXCreator(wgt, calculatePreferredSize(wgt));
                this.add(comp, i);
                comp.setBackupParent(this);
            }
        }
        isRefreshing = false;
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
    
    //����ʱȥtabFitLayout����������Index+1����ֹ����
    private int getTabNameIndex(){
    	int tabNameIndex = 0;
    	WCardLayout layout = this.cardLayout.toData();
    	int size = layout.getWidgetCount();
    	if(size < MIN_SIZE){
    		return tabNameIndex;
    	}
		for(int i=0;i<size;i++){
			WTabFitLayout fitLayout = (WTabFitLayout) layout.getWidget(i);
			int tempIndex = fitLayout.getTabNameIndex();
			tabNameIndex = Math.max(tempIndex, tabNameIndex);
		}
		return ++tabNameIndex;
    }
    
	/**
	 * ����tab���
	 * 
	 * void
	 */
	public void adjustComponentWidth(){
		int btnNum = this.getComponentCount();
		int tagLayoutWidth = this.getWidth();
		int allBtnWidth = btnNum * CardSwitchButton.DEF_WIDTH + btnNum;
		int size = CardSwitchButton.DEF_WIDTH;
		if(tagLayoutWidth - allBtnWidth < CardSwitchButton.DEF_WIDTH){
			size = (tagLayoutWidth - CardSwitchButton.DEF_WIDTH - btnNum)/btnNum;
		}
		WCardTagLayout layout = (WCardTagLayout)this.toData();
		for(int i=0;i<btnNum;i++){
			CardSwitchButton button = layout.getSwitchButton(i);
			layout.setWidthAtWidget(button, size);
		}
	}
	
	
	/**
	 * �ò�����Ҫ���أ�����Ա߿���в���
	 * @param �߿�
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

		XWCardTitleLayout titleLayout = (XWCardTitleLayout) this.getBackupParent();
		if(titleLayout != null){
			XWCardMainBorderLayout mainLayout = (XWCardMainBorderLayout)titleLayout.getBackupParent();
			if(mainLayout != null){
				XWCardLayout cardLayout = mainLayout.getCardPart();
				selectionModel.setSelectedCreator(cardLayout);
			}
		}
		
		if (editingMouseListener.stopEditing()) {
			if (this != designer.getRootComponent()) {
				ComponentAdapter adapter = AdapterBus.getComponentAdapter(designer, this);
				editingMouseListener.startEditing(this, adapter.getDesignerEditor(), adapter);
			}
		}
	}
}
