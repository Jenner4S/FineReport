package com.fr.design.designer.beans.adapters.layout;

import java.awt.CardLayout;
import java.awt.LayoutManager;

import com.fr.design.beans.GroupModel;
import com.fr.design.designer.beans.ConstraintsGroupModel;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.cardlayout.XWCardLayout;
import com.fr.design.designer.properties.CardLayoutConstraints;
import com.fr.design.designer.properties.CardLayoutPropertiesGroupModel;
import com.fr.design.utils.ComponentUtils;
import com.fr.design.utils.gui.LayoutUtils;

public class FRCardLayoutAdapter extends AbstractLayoutAdapter {

    public FRCardLayoutAdapter(XLayoutContainer container) {
        super(container);
    }

    /**
	 * ��ǰ�����Ƿ�������creator
	 * 
	 * @param creator ��������
	 * @param x ����x
	 * @param y ����y
	 * 
	 * @return �Ƿ����
	 * 
	 *
	 * @date 2014-12-30-����5:13:28
	 * 
	 */
    public boolean accept(XCreator creator, int x, int y) {
        return true;
    }

    /**
	 * ��ָ�������ӵ���ǰ����
	 * 
	 * @param creator ��������
	 * @param x x����
	 * @param y y����
	 * 
	 *
	 * @date 2014-12-30-����5:17:46
	 * 
	 */
    public void addComp(XCreator creator, int x, int y) {
        container.add(creator, creator.toData().getWidgetName());
        LayoutUtils.layoutRootContainer(container);
    }

    /**
	 * ��ָ�������ӵ���ǰ����
	 * 
	 * @param dragged ��������
	 * 
	 *
	 * @date 2014-12-30-����5:17:46
	 * 
	 */
    public void addNextComponent(XCreator dragged) {
    	addComp(dragged, -1, -1);
    }

    /**
	 * ��ָ�������ӵ�Ŀ�����ǰ��
	 * 
	 * @param target Ŀ�����
	 * @param added ��������
	 * 
	 *
	 * @date 2014-12-30-����5:17:46
	 * 
	 */
    public void addBefore(XCreator target, XCreator added) {
        int index = ComponentUtils.indexOfComponent(container, target);

        if (index == -1) {
            container.add(added, added.toData().getWidgetName(), 0);
        } else {
            container.add(added, added.toData().getWidgetName(), index);
        }

        LayoutUtils.layoutRootContainer(container);
    }

    /**
	 * ��ָ�������ӵ�Ŀ���������
	 * 
	 * @param target Ŀ�����
	 * @param added ��������
	 * 
	 *
	 * @date 2014-12-30-����5:17:46
	 * 
	 */
    public void addAfter(XCreator target, XCreator added) {
        int index = ComponentUtils.indexOfComponent(container, target);

        if (index == -1) {
            container.add(added, added.toData().getWidgetName());
        } else {
            index++;

            if (index >= container.getComponentCount()) {
                container.add(added, added.toData().getWidgetName());
            } else {
                container.add(added, added.toData().getWidgetName(), index);
            }
        }

        LayoutUtils.layoutRootContainer(container);
    }
    
    /**
	 * չʾ���
	 * 
	 * @param child ��Ҫչʾ�����
	 * 
	 *
	 * @date 2014-12-30-����5:17:13
	 * 
	 */
    public void showComponent(XCreator child) {
        LayoutManager layout = container.getLayout();
        CardLayout cardLayout = (CardLayout) layout;
        cardLayout.show(container, child.toData().getWidgetName());
    }

    @Override
    public ConstraintsGroupModel getLayoutConstraints(XCreator creator) {
		return new CardLayoutConstraints((XWCardLayout) container, creator);
    }
    @Override
    public GroupModel getLayoutProperties() {
        return new CardLayoutPropertiesGroupModel((XWCardLayout) container);
    }
}
