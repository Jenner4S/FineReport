/**
 * 
 */
package com.fr.design.designer.creator;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ContainerEvent;

import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.adapters.layout.FRTitleLayoutAdapter;
import com.fr.design.form.layout.FRTitleLayout;
import com.fr.form.ui.Label;
import com.fr.form.ui.Widget;
import com.fr.form.ui.WidgetTitle;
import com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget;
import com.fr.form.ui.container.WTitleLayout;
import com.fr.general.ComparatorUtils;

/**
 * һЩ�ؼ� ��ͼ������飬�б������ã��ұ���ĸ߶�����Ȳ���
 * @author jim
 * @date 2014-9-25
 */
public class XWTitleLayout extends DedicateLayoutContainer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5274572473978467325L;
	
	public XWTitleLayout() {
		super(new WTitleLayout("titlePane"), new Dimension());
	}

	/**
	 * �������캯��
	 * 
	 * @param widget �ؼ�widget
	 * @param initSize �ߴ��С
	 */
	public XWTitleLayout(WTitleLayout widget, Dimension initSize) {
		super(widget, initSize);
	}

	/**
	 * ��ʼ��������Ӧ�Ĳ��� ������ֻװһ����Ҫ����ԭ���߶ȵĿؼ���������Ϊabsolute
	 */
	@Override
	protected void initLayoutManager() {
		this.setLayout(new FRTitleLayout());
	}

	/**
	 * ��������Ⱦ��
	 */
	@Override
	public LayoutAdapter getLayoutAdapter() {
		return new FRTitleLayoutAdapter(this);
	}
	
	/**
	 * ����������Ӧ��wlayout
	 * 
	 * @return ͬ��
	 */
	@Override
	public WTitleLayout toData() {
		return (WTitleLayout) data;
	}
	
	/**
	 * �������������
	 * @param name ����
	 */
	public void resetCreatorName(String name) {
		super.resetCreatorName(name);
		// �б���Ļ������������ҲҪ������
		if (getXCreatorCount() > 1) {
        	getTitleCreator().toData().setWidgetName(WidgetTitle.TITLE_NAME_INDEX + name);
        }
	}
	
	/**
	 * ����Ĭ�����name
	 * 
	 * @return ������
	 */
	@Override
	public String createDefaultName() {
		return "titlePanel";
	}
	
	/**
	 * ���ر������
	 * @return �������
	 */
	public XCreator getTitleCreator() {
		for (int i=0; i < getXCreatorCount(); i++) {
			XCreator creator = getXCreator(i);
			if (!creator.hasTitleStyle()) {
				return creator;
			}
		}
		return null;
	}
	
	/**
	 * ��WLayoutת��ΪXLayoutContainer
	 */
	@Override
	public void convert() {
		isRefreshing = true;
		WTitleLayout layout = this.toData();
		this.removeAll();
		for (int i = 0, num = layout.getWidgetCount(); i < num; i++) {
			BoundsWidget bw = (BoundsWidget) layout.getWidget(i);
			if (bw != null) {
				Rectangle bounds = bw.getBounds();
				XWidgetCreator comp = (XWidgetCreator) XCreatorUtils.createXCreator(bw.getWidget());
				String constraint = bw.getWidget().acceptType(Label.class) ? WTitleLayout.TITLE : WTitleLayout.BODY;
				this.add(comp, constraint);
				comp.setBounds(bounds);
			}
		}
		isRefreshing = false;
	}

	/**
	 * �������
	 * 
	 * @param e �����¼�
	 */
	@Override
	public void componentAdded(ContainerEvent e) {
		if (isRefreshing) {
			return;
		}
		WTitleLayout layout = this.toData();
		XWidgetCreator creator = (XWidgetCreator) e.getChild();
		FRTitleLayout lay = (FRTitleLayout) getLayout();
        Object constraints = lay.getConstraints(creator);
        if (ComparatorUtils.equals(WTitleLayout.TITLE,constraints)) {
            layout.addTitle(creator.toData(), creator.getBounds());
        } else if (ComparatorUtils.equals(WTitleLayout.BODY,constraints)) {
            layout.addBody(creator.toData(), creator.getBounds());
        } 
	}

	/**
	 * ���ɾ��
	 * 
	 * @param e
	 *            �����¼�
	 */
	@Override
	public void componentRemoved(ContainerEvent e) {
		if (isRefreshing) {
			return;
		}
		XWidgetCreator xwc = ((XWidgetCreator) e.getChild());
		Widget wgt = xwc.toData();
		WTitleLayout wlayout = this.toData();
		wlayout.removeWidget(wgt);
	}

}
