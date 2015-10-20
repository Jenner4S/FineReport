package com.fr.design.designer.creator.cardlayout;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.beans.IntrospectionException;

import javax.swing.border.Border;

import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.adapters.layout.FRTabFitLayoutAdapter;
import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.designer.creator.CRPropertyDescriptor;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWFitLayout;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.FormHierarchyTreePane;
import com.fr.design.mainframe.widget.editors.PaddingMarginEditor;
import com.fr.design.mainframe.widget.renderer.PaddingMarginCellRenderer;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.form.ui.CardSwitchButton;
import com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget;
import com.fr.form.ui.container.cardlayout.WCardTagLayout;
import com.fr.form.ui.container.cardlayout.WTabFitLayout;
import com.fr.general.Inter;

/**
 * @author focus
 * @date 2014-6-23
 */
public class XWTabFitLayout extends XWFitLayout {
	
	private static final int MIN_SIZE = 1;
	// tab��������ק���µ�����������ʱ���������ק��ߴ��������ߣ��ᵼ�µ�����ʱ���Ҳ���ԭ�������
	// �����Ƚ���ק֮ǰ�Ŀ����������
	private Dimension referDim;
	

	public Dimension getReferDim() {
		return referDim;
	}

	public void setReferDim(Dimension referDim) {
		this.referDim = referDim;
	}

	public XWTabFitLayout(){
		this(new WTabFitLayout(), new Dimension());
	}
	
	public XWTabFitLayout(WTabFitLayout widget, Dimension initSize) {
		super(widget, initSize);
	}
	
	/**
	*  �õ�������
	 * @return ������
	* @throws IntrospectionException
	*/
	public CRPropertyDescriptor[] supportedDescriptor() throws IntrospectionException {
	return  new CRPropertyDescriptor[] {
				new CRPropertyDescriptor("widgetName", this.data.getClass()).setI18NName(Inter
					        .getLocText("FR-Designer_Form-Widget_Name")),
                new CRPropertyDescriptor("margin", this.data.getClass()).setEditorClass(PaddingMarginEditor.class)
                       .setRendererClass(PaddingMarginCellRenderer.class).setI18NName(Inter.getLocText("FR-Designer_Layout-Padding"))
                       .putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Advanced"),
                    };
	}
	
	@Override
	public LayoutAdapter getLayoutAdapter() {
		return new FRTabFitLayoutAdapter(this);
	}
	
	/**
	 * tab������ɾ��XWTabFitLayout��Ӧ��tab��ť
	 * 
	 * @param creator ��ǰ���
	 * @param designer �������
	 * 
	 */
	public void deleteRelatedComponent(XCreator creator,FormDesigner designer){
		//�������ҳ���ص�layout�Ͷ�Ӧ��tab��ť
    	XWTabFitLayout fitLayout = (XWTabFitLayout)creator;
    	WTabFitLayout fit = (WTabFitLayout) fitLayout.toData();
    	//����tabfitLayout��tab��ť��index
    	int index = fit.getIndex();
    	//����tabFitLayout��cardLayout
    	XWCardLayout cardLayout = (XWCardLayout) fitLayout.getBackupParent();
    	XWCardMainBorderLayout mainLayout = (XWCardMainBorderLayout) cardLayout.getBackupParent();
    	XWCardTitleLayout titleLayout = mainLayout.getTitlePart();
    	//����tab��ť��tagLayout
    	XWCardTagLayout tagLayout = titleLayout.getTagPart();
    	WCardTagLayout tag = (WCardTagLayout) tagLayout.toData();
    	
    	//ɾ������tab����
    	if(tag.getWidgetCount() <= MIN_SIZE){
    		deleteTabLayout(mainLayout,designer);
    		return;
    	}
    	
    	//��ɾ����Ӧ��tab��ť
    	for(int i=0;i<tagLayout.getComponentCount();i++){
    		CardSwitchButton button = tag.getSwitchButton(i);
    		if(button.getIndex()==index){
    			tagLayout.remove(i);
    			break;
    		}
    	}
    	//ˢ��tab��ť��tabFitLayout��index
    	refreshIndex(tag,cardLayout,index);
    	
    	LayoutUtils.layoutRootContainer(designer.getRootComponent());
	}
	
	
	private void deleteTabLayout(XLayoutContainer mainLayout,FormDesigner designer){
		SelectionModel selectionModel = designer.getSelectionModel();
		if(mainLayout != null){
			selectionModel.setSelectedCreator(mainLayout);
			selectionModel.deleteSelection();
		}
		LayoutUtils.layoutRootContainer(designer.getRootComponent());
		FormHierarchyTreePane.getInstance().refreshRoot();
		selectionModel.setSelectedCreator(designer.getRootComponent());
	}
	
	private void refreshIndex(WCardTagLayout tag,XWCardLayout cardLayout,int index){
    	for(int i=0;i<tag.getWidgetCount();i++){
    		CardSwitchButton button = tag.getSwitchButton(i);
    		XWTabFitLayout tempFit = (XWTabFitLayout) cardLayout.getComponent(i);
    		WTabFitLayout tempFitLayout = (WTabFitLayout) tempFit.toData();
    		int currentFitIndex = tempFitLayout.getIndex();
    		int buttonIndex = button.getIndex();
    		if(buttonIndex > index){
    			button.setIndex(--buttonIndex);
    		}
    		if(currentFitIndex > index){
    			tempFitLayout.setIndex(--currentFitIndex);
    		}
    	}
	}
	
	/**
	 * tab�������л�����Ӧ��tab��ť
	 * @param comp ��ǰ���
	 * void
	 */
    public void seleteRelatedComponent(XCreator comp){
    	XWTabFitLayout fitLayout = (XWTabFitLayout)comp;
    	WTabFitLayout fit = (WTabFitLayout) fitLayout.toData();
    	int index = fit.getIndex();
    	XWCardLayout cardLayout = (XWCardLayout) fitLayout.getBackupParent();
    	XWCardMainBorderLayout mainLayout = (XWCardMainBorderLayout) cardLayout.getBackupParent();
    	XWCardTitleLayout titleLayout = mainLayout.getTitlePart();
    	XWCardTagLayout tagLayout = titleLayout.getTagPart();
    	WCardTagLayout layout = (WCardTagLayout) tagLayout.toData();
    	for(int i=0;i<tagLayout.getComponentCount();i++){
    		CardSwitchButton button = layout.getSwitchButton(i);
    		button.setShowButton(button.getIndex()==index);
    	}
    }
    
    
    /**
	 * Ѱ�������Ϊ����Ӧ���ֵĸ�����
	 * 
	 * @return ��������
	 * 
	 *
	 * @date 2014-12-30-����3:15:28
	 * 
	 */
    public XLayoutContainer findNearestFit(){
    	XLayoutContainer parent = this.getBackupParent();
    	return parent == null ? null : parent.findNearestFit();
    } 
    
	/**
	 * �Ƕ�������Ӧ���ֵ�����
	 * @param percent �ٷֱ�
	 */
	public void adjustCompSize(double percent) {
		this.adjustCreatorsWhileSlide(percent);
	}
	
	/**
	 * �ò�����Ҫ���أ�����Ա߿���в���
	 * @param �߿�
	 * 
	 */
    public void setBorder(Border border) {
    	return;
    }
    
	/**
	 * ���հٷֱ������ڲ�������
	 * 
	 * @param percent ��ȱ仯�İٷֱ�
	 */
	public void adjustCreatorsWidth(double percent) {
		if (this.getComponentCount()==0) {
			// ��ʼ��û������ؼ�ʱ��ʵ�ʿ����Ȼ����
			this.toData().setContainerWidth(this.getWidth());
			return;
		}
		updateWidgetBackupBounds();
		int gap = toData().getCompInterval();
		if (gap >0 && hasCalGap) {
			moveCompInterval(getAcualInterval());
			updateCompsWidget();
		}
		layoutWidthResize(percent); 
		if (percent < 0 && this.getNeedAddWidth() > 0) {
			this.setSize(this.getWidth()+this.getNeedAddWidth(), this.getHeight());
			modifyEdgemostCreator(true);
		}
		addCompInterval(getAcualInterval());
		// �������Ž��������տ�����
		this.setReferDim(null);
		updateCompsWidget();
		this.toData().setContainerWidth(this.getWidth());
		updateWidgetBackupBounds();
		LayoutUtils.layoutContainer(this);
	}
	
	
	/**
	 * ���������߶��ֶ��޸�ʱ��
	 * ͬʱ���������ڵ������,��Сʱ��Ҫ�����е�����߶Ȳ�������С�߶�
	 * @param percent �߶ȱ仯�İٷֱ�
	 */
	public void adjustCreatorsHeight(double percent) {
		if (this.getComponentCount()==0) {
			//�����߶Ⱥ�wlayout�Ǳ߼�¼��
			this.toData().setContainerHeight(this.getHeight());
			return;
		}
		updateWidgetBackupBounds();
		int gap = toData().getCompInterval();
		if (gap >0 && hasCalGap) {
			moveCompInterval(getAcualInterval());
			updateCompsWidget();
		}
		layoutHeightResize(percent);
		if (percent < 0 && this.getNeedAddHeight() > 0) {
			this.setSize(this.getWidth(), this.getHeight()+this.getNeedAddHeight());
			modifyEdgemostCreator(false);
		}
		addCompInterval(getAcualInterval());
		updateCompsWidget();
		this.toData().setContainerHeight(this.getHeight());
		updateWidgetBackupBounds();
		LayoutUtils.layoutContainer(this);
	}
	
	public XLayoutContainer getOuterLayout(){
		XWCardLayout cardLayout = (XWCardLayout) this.getBackupParent();
		return cardLayout.getBackupParent();
	}
	
	// �����ڲ������widget
	private void updateCompsWidget(){
		for(int m=0;m<this.getComponentCount();m++){
			XCreator childCreator = this.getXCreator(m);
			BoundsWidget wgt = this.toData().getBoundsWidget(childCreator.toData());
			wgt.setBounds(this.getComponent(m).getBounds());
			wgt.setBackupBounds(this.getComponent(m).getBounds());
		}
	}
	
    /**
     * ȥ��ԭ�еļ��
     * @param gap ���
     */
    public void moveCompInterval(int gap) {
    	if (gap == 0) {
    		return;
    	}
    	int val = gap/2;
    	
    	// �Ƚ������С��tab���ֵĴ�С�Ĳ��տ��
    	double referWidth = getReferWidth();
    	double referHeight = getReferHeight();
    	
    	for (int i=0, len=this.getComponentCount(); i<len; i++) {
    		Component comp = this.getComponent(i);
    		Rectangle rec = comp.getBounds();
    		Rectangle bound = new Rectangle(rec);
    		if (rec.x > 0) {
    			bound.x -= val;
    			bound.width += val;
    		}
    		if (rec.width+rec.x < referWidth) {
    			bound.width  += val;
    		}
    		if (rec.y > 0) {
    			bound.y -= val;
    			bound.height += val;
    		}
    		if (rec.height+rec.y < referHeight) {
    			bound.height += val;
    		}
    		comp.setBounds(bound);
    	}
	
    	this.hasCalGap = false;
    }
    
    private double getReferWidth(){
    	if(referDim != null){
    		return referDim.getWidth();
    	}else{
    		return this.getWidth();
    	}
    }
    
    private double getReferHeight(){
    	if(referDim != null){
    		return referDim.getHeight();
    	}else{
    		return this.getHeight();
    	}
    }
    
    
    /**
     * �������0ʱ�����洦���ϼ��
     * ����ļ���������ʾ��ʵ�ʱ���Ĵ�С���ܼ��Ӱ��
     * ps:�ı䲼�ִ�С�������롢ɾ�������춼Ҫ���¿��Ǽ��
     * @param gap ���
     */
    public void addCompInterval(int gap) {
    	if (gap == 0) {
    		return;
    	}
    	int val = gap/2;
    	double referWidth = getReferWidth();
    	double referHeight = getReferHeight();
    	for (int i=0, len=this.getComponentCount(); i<len; i++) {
    		Component comp = this.getComponent(i);
    		Rectangle rec = comp.getBounds();
    		Rectangle bound = new Rectangle(rec);
    		if (rec.x > 0) {
    			bound.x += val;
    			bound.width -= val;
    		}
    		if (rec.width+rec.x < referWidth) {
    			bound.width  -= val;
    		}
    		if (rec.y > 0) {
    			bound.y += val;
    			bound.height -= val;
    		}
    		if (rec.height+rec.y < referHeight) {
    			bound.height -= val;
    		}
    		comp.setBounds(bound);
    	}
	
    	this.hasCalGap = true;
    }
}
