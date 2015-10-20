package com.fr.design.designer.creator.cardlayout;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import javax.swing.Icon;

import com.fr.base.BaseUtils;
import com.fr.design.designer.beans.AdapterBus;
import com.fr.design.designer.beans.ComponentAdapter;
import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.designer.creator.XButton;
import com.fr.design.mainframe.EditingMouseListener;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.FormHierarchyTreePane;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.form.ui.CardAddButton;
import com.fr.form.ui.CardSwitchButton;
import com.fr.form.ui.container.cardlayout.WCardTagLayout;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;

public class XCardAddButton extends XButton{
	
	private XWCardTagLayout tagLayout;
	private XWCardLayout cardLayout;
	private static final int LEFT_GAP = 5;
	private static final int UP_GAP = 10;
	
	private static final int START_INDEX = 3;
	private static final int INDEX = 0;
	
	
	private static Icon ADD_ICON = BaseUtils.readIcon("/com/fr/design/form/images/add.png");
	private Icon addIcon = ADD_ICON;
	
	/**
	 * card�������card��ť
	 */

	public XWCardTagLayout getTagLayout() {
		return tagLayout;
	}

	public void setTagLayout(XWCardTagLayout tagLayout) {
		this.tagLayout = tagLayout;
	}

	public XWCardLayout getCardLayout() {
		return cardLayout;
	}

	public void setCardLayout(XWCardLayout cardLayout) {
		this.cardLayout = cardLayout;
	}

	/**
	 * ���캯��
	 * @param widget ��ť
	 * @param initSize ��С
	 */
	public XCardAddButton(CardAddButton widget, Dimension initSize) {
		super(widget, initSize);
	}
	
	/**
	 * ���캯��
	 * @param widget ��ť
	 * @param initSize ��С
	 */
	public XCardAddButton(CardAddButton widget, Dimension initSize,XWCardTagLayout fit,XWCardLayout cardLayout) {
		super(widget, initSize);
		this.tagLayout = fit;
		this.cardLayout = cardLayout;
	}
	
	/**
	 * ��Ӧ����¼�
	 * @param editingMouseListener �¼�������
	 * @param e ����¼�
	 * 
	 */
    public void respondClick(EditingMouseListener editingMouseListener,MouseEvent e){
		FormDesigner designer = editingMouseListener.getDesigner();
		designer.fireTargetModified();
    	
    	// addbutton��Ӧ��XWCardLayout��XWCardTagLayout��δ���뵽xml�У����´�֮���ȸ��ݸ��Ӳ��ȡ
    	if(cardLayout == null && tagLayout ==null ){
    		initRalateLayout();
    	}
    	int index = cardLayout.toData().getWidgetCount();
    	
    	//����µ�tab������ԭ������Ϊδѡ��״̬
    	setTabUnselectd();
    	addTab(index);
    	this.tagLayout.adjustComponentWidth();
    	
		if (editingMouseListener.stopEditing()) {
			ComponentAdapter adapter = AdapterBus.getComponentAdapter(designer, this);
			editingMouseListener.startEditing(this, adapter.getDesignerEditor(), adapter);
		}
		
		FormHierarchyTreePane.getInstance().refreshRoot();
		//�������л���������tab��Ӧ��tabfitLayout��
		showNewTab(editingMouseListener,index);
		
		LayoutUtils.layoutRootContainer(designer.getRootComponent());
    }
    
    private void initRalateLayout(){
    	XWCardTitleLayout titleLayout = (XWCardTitleLayout)this.getBackupParent();
		this.tagLayout = titleLayout.getTagPart();
		
		XWCardMainBorderLayout borderLayout = (XWCardMainBorderLayout)titleLayout.getBackupParent();
		this.cardLayout = borderLayout.getCardPart();
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        drawAddIcon(g2d);
    }
    
	private void drawAddIcon(Graphics2D g2d){
		addIcon.paintIcon(this, g2d,LEFT_GAP,UP_GAP);
	}
	
	//��ԭ����tabҳ����Ϊδѡ��״̬
	private void setTabUnselectd(){
		for(int i=0;i<this.tagLayout.getComponentCount();i++){
			WCardTagLayout layout = (WCardTagLayout) this.tagLayout.toData();
			CardSwitchButton button = layout.getSwitchButton(i);
			button.setShowButton(false);
		}
	}
	
	//����tab
	private void addTab(int index){
		Dimension dimension = new Dimension();
		XCardSwitchButton button = (XCardSwitchButton) this.tagLayout.getComponent(INDEX);
		dimension.width = button.getWidth();
    	
		String cardLayoutName = cardLayout.toData().getWidgetName();
    	CardSwitchButton titleButton = new CardSwitchButton(index,cardLayoutName);
    	//���ñ���
    	titleButton.setText(getTabTitleName());
    	XCardSwitchButton showButton = new XCardSwitchButton(titleButton,dimension,cardLayout,tagLayout);
    	titleButton.setCustomStyle(true);
    	titleButton.setShowButton(true);
    	
    	this.tagLayout.setCurrentCard(titleButton);
    	this.tagLayout.setTabFitIndex(index);
    	this.tagLayout.add(showButton);
	}
	
	//�л����㵽����tabҳ
	private void showNewTab(EditingMouseListener editingMouseListener,int index){
		SelectionModel selectionModel = editingMouseListener.getSelectionModel();
		XWTabFitLayout tabFitLayout = (XWTabFitLayout) cardLayout.getComponent(index);
		selectionModel.setSelectedCreator(tabFitLayout);
	}
	
    //����ʱȥtabFitLayout����������Index+1����ֹ����
    private String getTabTitleName(){
    	WCardTagLayout layout = (WCardTagLayout) this.tagLayout.toData();
    	int size = layout.getWidgetCount();
    	String prefix = Inter.getLocText("FR-Designer_Title");
    	String newTextName = prefix + size;

		for (int i = 0; i < size; i++) {
			CardSwitchButton button = layout.getSwitchButton(i);
			String _text = button.getText();
			if (ComparatorUtils.equals(_text, newTextName)) {
				int lastSize = Integer.parseInt(newTextName.replaceAll(prefix, ""));
				newTextName = prefix + (lastSize + 1);
				i = 0;
			}
		}
		return newTextName;
    }
}
