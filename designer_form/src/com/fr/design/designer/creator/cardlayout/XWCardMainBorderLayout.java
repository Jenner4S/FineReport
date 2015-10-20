/**
 * 
 */
package com.fr.design.designer.creator.cardlayout;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;



import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XWBorderLayout;
import com.fr.form.ui.container.WBorderLayout;
import com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget;
import com.fr.form.ui.container.cardlayout.WCardMainBorderLayout;

/**
 * card����������
 * 
 *
 *
 * @date: 2014-12-9-����9:59:31
 */
public class XWCardMainBorderLayout extends XWBorderLayout{
	
	private static final int CENTER = 1;
	private static final int NORTH = 0;
	public static final Color DEFAULT_BORDER_COLOR = new Color(210,210,210);
	private static final int LAYOUT_INDEX = 0;
	private static final int TITLE_STYLE = 2;
	private static final int NORMAL_STYLE = 1;
	
	/**
	 * ���캯��
	 */
	public XWCardMainBorderLayout(WCardMainBorderLayout border, Dimension dimension) {
		super(border, dimension);
	}

	/**
	 * ��ȡ��ǰ�����Ŀؼ�
	 * 
	 * @return �ؼ�
	 * 
	 *
	 * @date 2014-12-10-����1:46:33
	 * 
	 */
	public WCardMainBorderLayout toData() {
		return (WCardMainBorderLayout) super.toData();
	}
	
	/**
	 * ��ӱ�������
	 * 
	 * @param title ��������
	 * 
	 *
	 * @date 2014-12-10-����1:50:56
	 * 
	 */
	public void addTitlePart(XWCardTitleLayout title){
		this.add(title, WBorderLayout.NORTH);
	}
	
	/**
	 * ���card����
	 * 
	 * @param card card����
	 * 
	 *
	 * @date 2014-12-10-����1:50:37
	 * 
	 */
	public void addCardPart(XWCardLayout card){
		this.add(card, WBorderLayout.CENTER);
	}

	public XWCardLayout getCardPart(){
		return this.getComponentCount() == TITLE_STYLE ? (XWCardLayout)this.getComponent(CENTER) : (XWCardLayout)this.getComponent(NORTH);
	}
	
	public XWCardTitleLayout getTitlePart(){
		return (XWCardTitleLayout)this.getComponent(NORTH);
	}
	
    /**
     * �ؼ�������Ҫ����xwcardmainLayout�������������xwcardLayout��
     * ������ʽ�£�this.getComponent(1)==xwcardLayout
     * ��׼��ʽ�£�this.getComponent(0)==xwcardLayout
     * @return �����xwcardLayout
     */
    @Override
    public XCreator getXCreator() {
    	switch(this.getComponentCount()){
    		case TITLE_STYLE:
    			return (XCreator)this.getComponent(TITLE_STYLE-1);
    		case NORMAL_STYLE:
    			return (XCreator)this.getComponent(NORMAL_STYLE-1);
    		default:
    			return this;
    	}
    }
	/**
	 * �ؼ�������ʾ�����
	 * @param path �ؼ���list
	 */
	public void notShowInComponentTree(ArrayList<Component> path) {
		path.remove(LAYOUT_INDEX);
	}
	
    @Override
    public ArrayList<XWTabFitLayout> getTargetChildrenList() {
    	ArrayList<XWTabFitLayout> tabLayoutList = new ArrayList<XWTabFitLayout>();
    	XWCardLayout cardLayout = this.getCardPart();
    	for(int i=0, size=cardLayout.getComponentCount(); i<size; i++){
    		XWTabFitLayout tabLayout = (XWTabFitLayout)cardLayout.getComponent(i);
    		tabLayoutList.add(tabLayout);
    	}
    	return tabLayoutList;
    }
    
    /**
     * ���µ���������Ŀ��
     * @param ���
     */
    public void recalculateChildWidth(int width){
		ArrayList<?> childrenList = this.getTargetChildrenList();
		int size = childrenList.size();
		if (size > 0) {
			for (int j = 0; j < size; j++) {
				XWTabFitLayout tabLayout = (XWTabFitLayout) childrenList
						.get(j);
				tabLayout.setBackupBound(tabLayout.getBounds());
				int refSize = tabLayout.getWidth();
				int offest = width - refSize;
				double percent = (double) offest / refSize;
				if (percent < 0 && !tabLayout.canReduce(percent)) {
					return;
				}
				tabLayout.setSize(tabLayout.getWidth() + offest,
						tabLayout.getHeight());
				for (int m = 0; m < tabLayout.getComponentCount(); m++) {
					XCreator childCreator = tabLayout.getXCreator(m);
					BoundsWidget wgt = tabLayout.toData()
							.getBoundsWidget(childCreator.toData());
					wgt.setBounds(tabLayout.getComponent(m).getBounds());
				}
				tabLayout.adjustCreatorsWidth(percent);
			}
		}
    }
    
    /**
     * ���µ���������ĸ߶�
     * @param height �߶�
     */
    public void recalculateChildHeight(int height){
		ArrayList<?> childrenList = this.getTargetChildrenList();
		int size = childrenList.size();
		if (size > 0) {
			for (int j = 0; j < size; j++) {
				XWTabFitLayout tabLayout = (XWTabFitLayout) childrenList
						.get(j);
				tabLayout.setBackupBound(tabLayout.getBounds());
				int refSize = tabLayout.getHeight();
				int offset = height - refSize - WCardMainBorderLayout.TAB_HEIGHT;
		    	if(offset < 0){
		    		// ����ʱ��Ҫ����ԭtab���ֿ��
		    		tabLayout.setReferDim(new Dimension(tabLayout.getWidth(),tabLayout.getHeight()));
		    	}
				double percent = (double) offset / refSize;
				if (percent < 0 && !tabLayout.canReduce(percent)) {
					return;
				}
				tabLayout.setSize(tabLayout.getWidth(),
						tabLayout.getHeight() + offset);
				for (int m = 0; m < tabLayout.getComponentCount(); m++) {
					XCreator childCreator = tabLayout.getXCreator(m);
					BoundsWidget wgt = tabLayout.toData()
							.getBoundsWidget(childCreator.toData());
					wgt.setBounds(tabLayout.getComponent(m).getBounds());
				}
				tabLayout.adjustCreatorsHeight(percent);
			}
		}
    
    }
}
