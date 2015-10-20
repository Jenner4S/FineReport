/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.creator.cardlayout;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ContainerEvent;
import java.beans.IntrospectionException;

import javax.swing.border.Border;

import com.fr.base.background.ColorBackground;
import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.adapters.layout.FRCardLayoutAdapter;
import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.designer.creator.CRPropertyDescriptor;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWidgetCreator;
import com.fr.design.form.layout.FRCardLayout;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.widget.editors.CardTagWLayoutBorderStyleEditor;
import com.fr.design.mainframe.widget.renderer.LayoutBorderStyleRenderer;
import com.fr.form.ui.CardAddButton;
import com.fr.form.ui.CardSwitchButton;
import com.fr.form.ui.LayoutBorderStyle;
import com.fr.form.ui.Widget;
import com.fr.form.ui.WidgetTitle;
import com.fr.form.ui.container.WBorderLayout;
import com.fr.form.ui.container.WCardLayout;
import com.fr.form.ui.container.cardlayout.WCardMainBorderLayout;
import com.fr.form.ui.container.cardlayout.WCardTagLayout;
import com.fr.form.ui.container.cardlayout.WCardTitleLayout;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.stable.Constants;
import com.fr.stable.core.PropertyChangeAdapter;

/**
 * @author richer
 * @since 6.5.3
 */
public class XWCardLayout extends XLayoutContainer {
	
	private CardLayout cardLayout;
	private boolean initFlag = true;
	private static final int NORTH = 0;
	//Ĭ����ɫ���ⱳ��
	private static final Color TITLE_COLOR = new Color(51, 132, 240);

    public XWCardLayout(WCardLayout widget, Dimension initSize) {
        super(widget, initSize);
    }

    @Override
    protected String getIconName() {
        return "card_layout_16.png";
    }
    
    /**
	 * ��ȡĬ������
	 * 
	 * @return Ĭ����
	 * 
	 *
	 * @date 2014-11-25-����6:22:40
	 * 
	 */
	public String createDefaultName() {
    	return "tabpane";
    }

    /**
	 * ��ȡ��ǰ��װ�Ĳ��ֶ���
	 * 
	 * @return ����
	 * 
	 *
	 * @date 2014-11-25-����6:22:17
	 * 
	 */
    public WCardLayout toData() {
        return (WCardLayout) data;
    }

    @Override
	protected void initLayoutManager() {
    	cardLayout = new FRCardLayout(toData().getHgap(), toData().getVgap());
        this.setLayout(cardLayout);
    }

    /**
	 * ��WLayoutת��ΪXLayoutContainer
	 * 
	 *
	 * @date 2014-11-25-����6:21:48
	 * 
	 */
    public void convert() {
        isRefreshing = true;
        WCardLayout layout = this.toData();
        this.removeAll();
        for (int i = 0; i < layout.getWidgetCount(); i++) {
            Widget w = layout.getWidget(i);
            XWidgetCreator creator = (XWidgetCreator) XCreatorUtils.createXCreator(layout.getWidget(i));
            this.add(creator, w.getWidgetName(), i);
            creator.setBackupParent(this);
        }
        isRefreshing = false;
    }
    
    /**
	 * չʾ��ǰѡ�е�card
	 * 
	 *
	 * @date 2014-11-25-����6:21:23
	 * 
	 */
	public void showCard() {
		WCardLayout layout = this.toData();
		if (layout.getWidgetCount() > 0) {
			cardLayout.show(this, layout.getShowIndex2Name());
		}
	}
	
	/**
	 * ��ȡ��ǰXCreator��һ����װ������
	 * 
	 * @param widgetName ��ǰ�����
	 * 
	 * @return ��װ�ĸ�����
	 * 
	 *
	 * @date 2014-11-25-����4:47:23
	 * 
	 */
	protected XLayoutContainer getCreatorWrapper(String widgetName) {
		initStyle();
		Dimension dimension = new Dimension();
		//���ṹ��һ��borderlayout, ��ǩ������north, card����Ϊcenter
		WCardMainBorderLayout border = new WCardMainBorderLayout();
		XWCardMainBorderLayout xMainBorder = new XWCardMainBorderLayout(border, dimension);
		this.setBackupParent(xMainBorder);
		
		XWCardTitleLayout titlePart = this.initTitlePart(widgetName, xMainBorder);
		xMainBorder.addTitlePart(titlePart);

		return xMainBorder;
	}
	
	//������
	private XWCardTitleLayout initTitlePart(String widgetName, XWCardMainBorderLayout xMainBorder){
		Dimension dimension = new Dimension();
		//���ⲿ�ֱ���Ҳ��һ��borderlayout����, tag���ַ�center, ��Ӱ�ť��east
		WCardTitleLayout titleLayout = new WCardTitleLayout(this.toData().getWidgetName());
		XWCardTitleLayout xTitle = new XWCardTitleLayout(titleLayout,dimension);
		xTitle.setBackupParent(xMainBorder);
		
		//�л�card�İ�ť����
		XWCardTagLayout tagPart = initTagPart(widgetName, xTitle);
		//��Ӱ�ť
		XCardAddButton addBtn = initAddButton(widgetName, xTitle, tagPart);
		
		//���˳�����Ҫ����addBtn��tagPart�������õ�
		xTitle.addNewButton(addBtn);
		xTitle.addTagPart(tagPart);
		
		return xTitle;
	}
	
	//���tab��ť
	private XCardAddButton initAddButton(String widgetName, XWCardTitleLayout xTitle, XWCardTagLayout tagPart){
		Dimension dimension = new Dimension();
		CardAddButton addButton = new CardAddButton(widgetName);
		XCardAddButton xAddBtn = new XCardAddButton(addButton, dimension, tagPart, this);
		xAddBtn.setBackupParent(xTitle);
		
		return xAddBtn;
	}
	
	//���е�tab���ڵ�����
	private XWCardTagLayout initTagPart(String widgetName, XWCardTitleLayout xTitle){
		Dimension dimension = new Dimension();
		//���ñ����tab��ʽ����
		WCardTagLayout tagLayout = new WCardTagLayout();
		XWCardTagLayout xTag = new XWCardTagLayout(tagLayout, dimension, this);
		xTag.setBackupParent(xTitle);
		
		XCardSwitchButton xFirstBtn = initFirstButton(widgetName, xTag);
		xTag.add(xFirstBtn);
		
		return xTag;
	}
	
	//��һ��tab
	private XCardSwitchButton initFirstButton(String widgetName, XWCardTagLayout xTag){
		CardSwitchButton firstBtn = new CardSwitchButton(widgetName);
		firstBtn.setText(Inter.getLocText("FR-Designer_Title") + 0);
		firstBtn.setInitialBackground(ColorBackground.getInstance(Color.WHITE));
		xTag.setCurrentCard(firstBtn);
		XCardSwitchButton xFirstBtn = new XCardSwitchButton(firstBtn, new Dimension(CardSwitchButton.DEF_WIDTH, -1),this,xTag);
		firstBtn.setCustomStyle(true);
		xFirstBtn.setBackupParent(xTag);
		
		return xFirstBtn;
	}
	
	/**
	 * ���ø�����������
	 * 
	 * @param parentPanel ��ǰ������
	 * @param widgetName ��ǰ�ؼ���
	 * 
	 *
	 * @date 2014-11-27-����9:47:00
	 * 
	 */
	protected void setWrapperName(XLayoutContainer parentPanel, String widgetName) {
		//�����setһ��, �Ժ���Ҫ���ص�
		parentPanel.toData().setWidgetName("border_card_" + widgetName);
	}
	
	/**
	 * ����ǰ������ӵ���������
	 * 
	 * @param parentPanel ���������
	 * 
	 *
	 * @date 2014-11-25-����4:57:55
	 * 
	 */
	protected void addToWrapper(XLayoutContainer parentPanel, int width, int minHeight){			
		parentPanel.add(this, WBorderLayout.CENTER);
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
        if (isRefreshing) {
            return;
        }
        XWidgetCreator creator = (XWidgetCreator) e.getChild();
        creator.setDirections(null);
        WCardLayout layout = this.toData();
        Widget w = creator.toData();
        layout.addWidget(w);
    }

	@Override
	public LayoutAdapter getLayoutAdapter() {
		return new FRCardLayoutAdapter(this);
	}
	
	/**
	 * �Ƿ�֧�ֱ�����ʽ
	 * @return Ĭ��false
	 */
	public boolean hasTitleStyle() {
		return true;
	}
	
	
	/**
	*  �õ�������
	 * @return ������
	* @throws IntrospectionException
	*/
	public CRPropertyDescriptor[] supportedDescriptor() throws IntrospectionException {
	return  new CRPropertyDescriptor[] {
                new CRPropertyDescriptor("widgetName", this.data.getClass()).setI18NName(Inter
                       .getLocText("FR-Designer_Form-Widget_Name")).setPropertyChangeListener(new PropertyChangeAdapter(){
                    	   
                    	   @Override
                    	   public void propertyChange(){
                    		   WCardLayout cardLayout = toData();
                    		   changeRalateSwitchCardname(cardLayout.getWidgetName());
                    	   }
                       }),
               new CRPropertyDescriptor("borderStyle", this.data.getClass()).setEditorClass(
            		   CardTagWLayoutBorderStyleEditor.class).setRendererClass(LayoutBorderStyleRenderer.class).setI18NName(
                       Inter.getLocText("FR-Engine_Style")).putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Advanced")
                       .setPropertyChangeListener(new PropertyChangeAdapter() {

                            @Override
                            public void propertyChange() {
                           	initStyle();
                           }
                        }),
                    };
	}
	
	//��ʼ����ʽ
    protected void initStyle() {
    	LayoutBorderStyle style = toData().getBorderStyle();
    	initBorderTitleStyle(style);
    	initBorderStyle();
    	clearOrShowTitleLayout(ComparatorUtils.equals(style.getType(), LayoutBorderStyle.TITLE));
    }
    
    private void initBorderTitleStyle(LayoutBorderStyle style){
    	//��ʼ��Ĭ�ϱ�����ʽ
    	if(!initFlag){
    		return;
    	}
    	
		style.setType(LayoutBorderStyle.TITLE);
		style.setBorder(Constants.LINE_THIN);
    	WidgetTitle widgetTitle = style.getTitle();
    	widgetTitle.setBackground(ColorBackground.getInstance(TITLE_COLOR));
		initFlag = false;
    }

    //���ػ���ʾ���ⲿ��
    protected void clearOrShowTitleLayout(boolean isTitleStyle) {
    	XWCardMainBorderLayout mainLayout = (XWCardMainBorderLayout)this.getBackupParent();
    	if(mainLayout != null){
        	XWCardTitleLayout titleLayout = (XWCardTitleLayout) mainLayout.getComponent(NORTH);
        	if(titleLayout != null){
        		WCardTitleLayout layout = (WCardTitleLayout) titleLayout.toData();
            	titleLayout.setVisible(isTitleStyle);
            	layout.setVisible(isTitleStyle);
        	}
    	}
    }
    
    //�޸����SwtchButton���󶨵�cardLayout�ؼ���
	private void changeRalateSwitchCardname(String cardLayoutName) {
		XWCardMainBorderLayout borderLayout = (XWCardMainBorderLayout) this.getBackupParent();
		WCardMainBorderLayout border = borderLayout.toData();
		WCardTitleLayout titleLayout = border.getTitlePart();
		WCardTagLayout tagLayout = titleLayout.getTagPart();
		for (int i = 0, len = tagLayout.getWidgetCount(); i < len; i++) {
			CardSwitchButton button = tagLayout.getSwitchButton(i);
			button.setCardLayoutName(cardLayoutName);
		}
	}
	
    /**
     * ɾ��������
     * 
     * @param creator ��ǰ���
     * @param designer �������
     * 
     */
	public void deleteRelatedComponent(XCreator creator,FormDesigner designer){
		XWCardMainBorderLayout mainLayout = (XWCardMainBorderLayout) creator.getBackupParent();
		SelectionModel selectionModel = designer.getSelectionModel();
		selectionModel.setSelectedCreator(mainLayout);
		selectionModel.deleteSelection();
		return;
	}
	@Override
	public void setBorder(Border border) {
		super.setBorder(border);
		XWCardMainBorderLayout mainLayout = (XWCardMainBorderLayout) this.getBackupParent();
		if(mainLayout != null){
			mainLayout.setBorder(border);
		}
	}
}
