/**
 * 
 */
package com.fr.design.designer.creator.cardlayout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import javax.swing.Icon;

import com.fr.base.BaseUtils;
import com.fr.base.background.ColorBackground;
import com.fr.design.designer.beans.AdapterBus;
import com.fr.design.designer.beans.ComponentAdapter;
import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.designer.creator.XButton;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.EditingMouseListener;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.FormHierarchyTreePane;
import com.fr.design.mainframe.JForm;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.form.ui.CardSwitchButton;
import com.fr.form.ui.LayoutBorderStyle;
import com.fr.form.ui.WidgetTitle;
import com.fr.form.ui.container.cardlayout.WTabFitLayout;
import com.fr.general.Background;
import com.fr.general.FRFont;

/**
 *
 *
 * @date: 2014-11-27-����10:28:14
 */
public class XCardSwitchButton extends XButton {

	private XWCardLayout cardLayout;
	private XWCardTagLayout tagLayout;
	
	private static final int LEFT_GAP = 16;
	public static final Color NORMAL_GRAL = new Color(236,236,236);
	public static final Color CHOOSED_GRAL = new Color(222,222,222);
	
	private static final int MIN_SIZE = 1;
	
	// ɾ����ťʶ������ƫ����
	private static final int RIGHT_OFFSET = 15;
	private static final int TOP_OFFSET = 25;
	
	// tab��ť���������Ϊ��ť�ڲ��Ĳ��ֿ�������������ҪС���Ӹ�������
	private static final int FONT_SIZE_ADJUST = 2;
	
	
	
	private static Icon MOUSE_COLSE = BaseUtils.readIcon("/com/fr/design/images/buttonicon/close_icon.png");
	private Icon closeIcon = MOUSE_COLSE;
	
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

	public XCardSwitchButton(CardSwitchButton widget, Dimension initSize) {
		super(widget, initSize);
	}

	public XCardSwitchButton(CardSwitchButton widget, Dimension initSize,
			XWCardLayout cardLayout, XWCardTagLayout tagLayout) {
		super(widget, initSize);
		this.cardLayout = cardLayout;
		this.tagLayout = tagLayout;
	}

	/**
	 * ��Ӧ����¼�
	 * 
	 * @param editingMouseListener
	 *            �¼�������
	 * @param e
	 *            ����¼�
	 * 
	 */
	public void respondClick(EditingMouseListener editingMouseListener,
			MouseEvent e) {
		FormDesigner designer = editingMouseListener.getDesigner();
		SelectionModel selectionModel = editingMouseListener.getSelectionModel();

		//�ر����´򿪣���ص�layoutδ�浽xml�У���ʼ��
		if(cardLayout == null){
			initRalateLayout(this);
		}
		
		//��ȡ��ǰtab��index
		XCardSwitchButton button = this;
		CardSwitchButton currentButton = (CardSwitchButton) button.toData();
		int index = currentButton.getIndex();
		
		//���ɾ��ͼ��ʱ
		if (isSeletectedClose(e,designer)) {
			//��ɾ�������һ��tabʱ��ɾ������tab����
			if(tagLayout.getComponentCount() <= MIN_SIZE){
				deleteTabLayout(selectionModel, designer);
				return;
			}
			deleteCard(button,index);
			this.tagLayout.adjustComponentWidth();
			designer.fireTargetModified();
			LayoutUtils.layoutRootContainer(designer.getRootComponent());
			FormHierarchyTreePane.getInstance().refreshRoot();
			return;
		}
		
		//����ǰtab��ť��Ϊѡ��״̬
		changeButtonState(index);
		
		// �л�����ǰtab��ť��Ӧ��tabFitLayout
		XWTabFitLayout tabFitLayout = (XWTabFitLayout) cardLayout.getComponent(index);
		selectionModel.setSelectedCreator(tabFitLayout);
		
		if (editingMouseListener.stopEditing()) {
			ComponentAdapter adapter = AdapterBus.getComponentAdapter(designer,
					this);
			editingMouseListener.startEditing(this,
					adapter.getDesignerEditor(), adapter);
		}
		
	}
	
	//ɾ��card��ͬʱ�޸�����switchbutton��tabfit��index
	private void deleteCard(XCardSwitchButton button,int index){
		tagLayout.remove(button);
		// �������tab�ڲ��������������ʾ������ʽ�Ĳ���
		XWTabFitLayout tabLayout = (XWTabFitLayout)cardLayout.getComponent(index);
		tabLayout.removeAll();
		cardLayout.remove(index);
		for (int i = 0; i < tagLayout.getComponentCount(); i++) {
			XCardSwitchButton temp = (XCardSwitchButton) tagLayout.getComponent(i);
			CardSwitchButton tempButton = (CardSwitchButton) temp.toData();
			XWTabFitLayout fit = (XWTabFitLayout) cardLayout.getComponent(i);
			WTabFitLayout layout = (WTabFitLayout) fit.toData();
			int currentIndex = tempButton.getIndex();
			int tabFitIndex = layout.getIndex();
			if (currentIndex > index) {
				tempButton.setIndex(--currentIndex);
			}
			if (tabFitIndex > index) {
				layout.setIndex(--tabFitIndex);
			}
		}
	}
	
	
	//SwitchButton��Ӧ��XWCardLayout��XWCardTagLayout��δ�浽xml��,���´�ʱ���ݸ��Ӳ��ϵ��ȡ
	private void initRalateLayout(XCardSwitchButton button){
		this.tagLayout = (XWCardTagLayout)this.getBackupParent();
		XWCardTitleLayout titleLayout = (XWCardTitleLayout) this.tagLayout.getBackupParent();
		XWCardMainBorderLayout borderLayout = (XWCardMainBorderLayout)titleLayout.getBackupParent();
		this.cardLayout = borderLayout.getCardPart();
	}
	
	//�Ƿ�������رհ�ť����
	private boolean isSeletectedClose(MouseEvent e,FormDesigner designer){
		
		int diff = designer.getArea().getHorScrollBar().getValue();
		
		// mouse position
		int ex = e.getX() + diff;
		int ey = e.getY();
		
		//��ȡtab���ֵ�λ��,��������tab��ť��λ��
		XLayoutContainer mainLayout = cardLayout.getBackupParent();
		Point point = mainLayout.getLocation();
		double mainX = point.getX();
		double mainY = point.getY();
		
		// ��������������Ӱ��
		JForm jform = (JForm)HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
		if(jform.getFormDesign().getParaComponent() != null){
			ey -= jform.getFormDesign().getParaHeight();
		}
		
		//����tab���ֵ����λ��
		ex -= mainX;
		ey -= mainY;
		
		// button position
		XCardSwitchButton button = this;
		Point position = button.getLocation();
		int width = button.getWidth();
		int height = button.getHeight();
		
		// �����밴ť�Ҳ�ɾ��ͼ������
		double recX = position.getX() + (width - RIGHT_OFFSET);
		double recY = position.getY() + (height - TOP_OFFSET);
		
		return (recX < ex && ex < recX + RIGHT_OFFSET &&  ey < recY);
	}
	
	//����ǰswitchButton��Ϊѡ��״̬
	private void changeButtonState(int index){
		for(int i=0;i<this.tagLayout.getComponentCount();i++){
			XCardSwitchButton temp = (XCardSwitchButton) tagLayout.getComponent(i);
			CardSwitchButton tempButton = (CardSwitchButton) temp.toData();
			tempButton.setShowButton(tempButton.getIndex()==index);
		}
	}
	
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        drawBackgorund();
        drawTitle();
		Dimension panelSize = this.getContentLabel().getSize();
		this.getContentBackground().paint(g, new Rectangle2D.Double(0, 0, panelSize.getWidth(), panelSize.getHeight()));
		drawCloseIcon(g2d);
    }
	
    //��ɾ��ͼ��
	private void drawCloseIcon(Graphics2D g2d){
		closeIcon.paintIcon(this, g2d,this.getWidth()-LEFT_GAP,0);
	}
	
	//������
	private void drawBackgorund(){
        CardSwitchButton button = (CardSwitchButton)this.toData();
        ColorBackground background;
        if(button.isShowButton()){
        	this.rebuid();
        	background = ColorBackground.getInstance(CHOOSED_GRAL);
        	this.setContentBackground(background);
        }else{
        	this.rebuid();
        	background = ColorBackground.getInstance(NORMAL_GRAL);
        	this.setContentBackground(background);
        }
	}
	
	//������
	private void drawTitle() {
		CardSwitchButton button = (CardSwitchButton) this.toData();
		this.setButtonText(button.getText());
		if (this.cardLayout == null) {
			initRalateLayout(this);
		}

		LayoutBorderStyle style = this.cardLayout.toData().getBorderStyle();

		// ���ⲿ��
		WidgetTitle title = style.getTitle();
		FRFont font = title.getFrFont();
		FRFont newFont = FRFont.getInstance(font.getName(),font.getStyle(),font.getSize() + FONT_SIZE_ADJUST);
		UILabel label = this.getContentLabel();
		label.setFont(newFont);
		label.setForeground(font.getForeground());
		Background background = title.getBackground();
		if (background != null) {
			if(button.isShowButton()){
	        	background = ColorBackground.getInstance(CHOOSED_GRAL);
	        	this.setContentBackground(background);
			}else{
				this.setContentBackground(background);
			}
		}
	}
	
	//ɾ��tab����
	private void deleteTabLayout(SelectionModel selectionModel,FormDesigner designer){
		XLayoutContainer mainLayout = this.cardLayout.getBackupParent();
		if(mainLayout != null){
			selectionModel.setSelectedCreator(mainLayout);
			selectionModel.deleteSelection();
		}
		LayoutUtils.layoutRootContainer(designer.getRootComponent());
		FormHierarchyTreePane.getInstance().refreshRoot();
		selectionModel.setSelectedCreator(designer.getRootComponent());
	}
	
}
