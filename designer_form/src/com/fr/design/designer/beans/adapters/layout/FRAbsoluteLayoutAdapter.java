package com.fr.design.designer.beans.adapters.layout;

import java.awt.Rectangle;

import com.fr.design.designer.beans.ConstraintsGroupModel;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWAbsoluteLayout;
import com.fr.design.designer.properties.BoundsGroupModel;
import com.fr.form.ui.container.WAbsoluteLayout;
import com.fr.design.utils.ComponentUtils;
import com.fr.design.utils.gui.LayoutUtils;

public class FRAbsoluteLayoutAdapter extends AbstractLayoutAdapter {

    public FRAbsoluteLayoutAdapter(XLayoutContainer container) {
        super(container);
    }
    
    /**
     * �Ƿ�����ָ��λ��������
     * @param creator ���
     * @param x ����x
     * @param y ����y
     * @return ���򷵻�true
     */
    @Override
	public boolean accept(XCreator creator, int x, int y) {
		return x >= 0 && y >= 0 && creator.getHeight() <= container.getHeight()
				&& creator.getWidth() <= container.getWidth();
	}

    @Override
	protected void addComp(XCreator creator, int x, int y) {
		if (XCreatorUtils.getParentXLayoutContainer(creator) != null) {
			Rectangle r = ComponentUtils.getRelativeBounds(container);
			Rectangle creatorRectangle = ComponentUtils.getRelativeBounds(creator);
			x = creatorRectangle.x - r.x;
			y = creatorRectangle.y - r.y;
		} else {
			int w = creator.getWidth() / 2;
			int h = creator.getHeight() / 2;
			x = x - w;
			y = y - h;
		}

		fix(creator, x, y);
		container.add(creator);
		LayoutUtils.layoutRootContainer(container);
	}
    
    /**
     * �����ק�������С
     * @param creator ���
     */
    @Override
	public void fix(XCreator creator) {
    	WAbsoluteLayout wabs = (WAbsoluteLayout)container.toData();
    	fix(creator,creator.getX(),creator.getY());
    	wabs.setBounds(creator.toData(),creator.getBounds());
    }
    
    /**
     * ���������С�����ʳߴ�λ��
     * @param creator ���
     * @param x ����x
     * @param y ����y
     */
    public void fix(XCreator creator ,int x, int y) {
    	if (x < 0) {
			x = 0;
		} else if (x + creator.getWidth() > container.getWidth()) {
			x = container.getWidth() - creator.getWidth();
		}

		if (y < 0) {
			y = 0;
		} else if (y + creator.getHeight() > container.getHeight()) {
			y = container.getHeight() - creator.getHeight();
		}

		creator.setLocation(x, y);
    }

    @Override
    public ConstraintsGroupModel getLayoutConstraints(XCreator creator) {
        return new BoundsGroupModel((XWAbsoluteLayout)container, creator);
    }
}
