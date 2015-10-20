/**
 * 
 */
package com.fr.design.designer.beans.adapters.layout;


import java.awt.Rectangle;

import com.fr.design.beans.GroupModel;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWidgetCreator;
import com.fr.design.designer.creator.cardlayout.XWCardLayout;
import com.fr.design.designer.creator.cardlayout.XWTabFitLayout;
import com.fr.design.designer.properties.FRTabFitLayoutPropertiesGroupModel;
import com.fr.design.utils.ComponentUtils;
import com.fr.form.ui.LayoutBorderStyle;
import com.fr.form.ui.container.cardlayout.WCardMainBorderLayout;
import com.fr.general.ComparatorUtils;

/**
 * tab����tabFit������
 * 
 * @author focus
 * @date 2014-6-24
 */
public class FRTabFitLayoutAdapter extends FRFitLayoutAdapter {
	//�������߶ȶ�tab�����ڲ������y���������ƫ��
	private static int TAB_HEIGHT = 40;
	
	/**
	 * ���캯��
	 * @param container XWTabFitLayout����
	 */
	public FRTabFitLayoutAdapter(XLayoutContainer container) {
		super(container);
	}
	
	/**
	 * ���ز����������ԣ�����һЩ����������layoutˢ��ʱ����
	 */
	@Override
    public GroupModel getLayoutProperties() {
		XWTabFitLayout xfl = (XWTabFitLayout) container;
        return new FRTabFitLayoutPropertiesGroupModel(xfl); 	 	
    }
	
    /**
     * �����ComponentAdapter��������ʱ��������ֲ��ֹ�������Ϊ�գ���̶����øò��ֹ�������
     * addComp�������������ľ�����ӡ��ڸ÷����ڣ����ֹ����������ṩ����Ĺ��ܡ�
     * @param creator ����ӵ������
     * @param x ��ӵ�λ��x����λ���������container��
     * @param y ��ӵ�λ��y����λ���������container��
     * @return �Ƿ���ӳɹ����ɹ�����true������false
     */
    @Override
  	public boolean addBean(XCreator creator, int x, int y) {
    	// ����accept�жϺ�container�ᱻ�ı䣬�ȱ���
    	XLayoutContainer backUpContainer = container;
  	  	Rectangle rect = ComponentUtils.getRelativeBounds(container);
  	  	
  	  	int posX = x - rect.x;
  	  	int posY = y - rect.y;
  		if (!accept(creator, posX, posY)) {
  			return false;
  		}
  		// posX��posY����������������������λ�ã�����tab���ֵı�Ե������Ҫ���������ӵ�
  		// ��������Ӧ�����У���ʱ������λ�þ���tab�������ڵ�λ��
  		if(this.intersectsEdge(posX, posY, backUpContainer)){
  			if(!ComparatorUtils.equals(backUpContainer.getOuterLayout(), backUpContainer.getBackupParent())){
  				XWTabFitLayout tabLayout = (XWTabFitLayout)backUpContainer;
  				y = adjustY(y,tabLayout);
  			}
  			addComp(creator, x, y);
  	  		((XWidgetCreator) creator).recalculateChildrenSize();
  	  		return true;
  		}
  		// ������ڱ�Ե������Ϊ������Ӧ���֣����������λ�þ��������������λ��
  		addComp(creator, posX, posY);
  		((XWidgetCreator) creator).recalculateChildrenSize();
  		return true;
  	}
    
    // tab���ֵ��������ܵ�tab�߶ȵ�Ӱ�죬�жϵ��ϱ߽�ȡ��������XWTabFitLayout���ϱ߽磬
    // ʵ�ʼ����ʱ�����������������CardMainBorerLayout����Ҫ��tab�߶ȼ���
    private int adjustY(int y,XWTabFitLayout tabLayout){
		XWCardLayout cardLayout = (XWCardLayout) tabLayout.getBackupParent();
		LayoutBorderStyle style = cardLayout.toData().getBorderStyle();
		if(ComparatorUtils.equals(style.getType(), LayoutBorderStyle.TITLE)){
			y -= WCardMainBorderLayout.TAB_HEIGHT;
		}
		return y;
    }
}