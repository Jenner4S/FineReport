package com.fr.design.designer.beans.adapters.layout;

import com.fr.design.beans.GroupModel;
import com.fr.design.designer.beans.HoverPainter;
import com.fr.design.designer.beans.painters.FRParameterLayoutPainter;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWParameterLayout;
import com.fr.design.form.parameter.RootDesignGroupModel;
import com.fr.form.ui.container.WParameterLayout;

/**
 * ����������ļ�����
 */
public class FRParameterLayoutAdapter extends FRAbsoluteLayoutAdapter {

    private HoverPainter painter;

	public FRParameterLayoutAdapter(XLayoutContainer container) {
		super(container);
        painter = new FRParameterLayoutPainter(container);
	}

    public HoverPainter getPainter() {
        return painter;
    }

    public GroupModel getLayoutProperties() {
        return new RootDesignGroupModel((XWParameterLayout)container);
    }

    /**
     * ��˵��
     * @param creator    ���
     */
	public void fix(XCreator creator) {
		super.fix(creator);
		
		WParameterLayout wabs = (WParameterLayout)container.toData();
		wabs.refreshTagList();
    }
	
}
