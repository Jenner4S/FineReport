/**
 * 
 */
package com.fr.design.designer.creator;

import com.fr.form.ui.container.WLayout;

import java.awt.*;
import java.beans.IntrospectionException;
import java.util.ArrayList;

/**
 * һЩ�ؼ�ר���������������������sclae����
 * @author jim
 * @date 2014-11-7
 */
public abstract class DedicateLayoutContainer extends XLayoutContainer {

	public DedicateLayoutContainer(WLayout widget, Dimension initSize) {
		super(widget, initSize);
	}
	
	/**
     *  �õ�������
     * @return ������
     * @throws IntrospectionException
     */
	public CRPropertyDescriptor[] supportedDescriptor() throws IntrospectionException {
		return new CRPropertyDescriptor[0];
	}
	
	/**
	 * ��������ͼ��
	 * 
	 * @return
	 */
	@Override
	public String getIconPath() {
        if(this.getXCreator(XWScaleLayout.INDEX) != null){
            return this.getXCreator(XWScaleLayout.INDEX).getIconPath();
        }
		return "/com/fr/web/images/form/resources/text_field_16.png";
	}

	
	/**
	 * �ؼ�������ʾ�����
	 * @param path �ؼ���list
	 */
	public void notShowInComponentTree(ArrayList<Component> path) {
		path.remove(path.size()-1);
	}
	
	/**
	 * �������������
	 * @param name ����
	 */
	public void resetCreatorName(String name) {
		super.resetCreatorName(name);
		XCreator child = getXCreator(XWScaleLayout.INDEX);
		child.toData().setWidgetName(name);
	}
	
	/**
	 * ���ض�Ӧ���Ա�������scale��title�����������
	 * @return ���
	 */
	public XCreator getPropertyDescriptorCreator() {
		return getXCreator(XWScaleLayout.INDEX);
	}
	
	/**
	 * �Ƿ���Ϊ�ؼ�����Ҷ�ӽڵ�
	 * @return ���򷵻�true
	 */
	public boolean isComponentTreeLeaf() {
		return true;
	}
	
	/**
	 *  �Ƿ�Ϊsclae��titleר������
	 * @return ���򷵻�true
	 */
	public boolean isDedicateContainer() {
		return true;
	}

}
