package com.fr.design.designer.beans.adapters.component;

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.beans.IntrospectionException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;

import com.fr.base.FRContext;
import com.fr.design.actions.UpdateAction;
import com.fr.design.beans.GroupModel;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.gui.xtable.PropertyGroupModel;
import com.fr.design.designer.beans.ComponentAdapter;
import com.fr.design.designer.beans.actions.ChangeNameAction;
import com.fr.design.designer.beans.events.DesignerEditor;
import com.fr.design.designer.creator.CRPropertyDescriptor;
import com.fr.design.designer.creator.XButton;
import com.fr.design.designer.creator.XCreator;
import com.fr.form.ui.Button;
import com.fr.form.ui.Widget;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.stable.StringUtils;
import com.fr.stable.core.PropertyChangeAdapter;
import com.fr.design.utils.ComponentUtils;
import com.fr.design.utils.gui.LayoutUtils;

public class CompositeComponentAdapter implements ComponentAdapter {

	protected FormDesigner designer;
	protected DesignerEditor<? extends JComponent> editorComponent;
	protected XCreator xCreator;

	public CompositeComponentAdapter(FormDesigner designer, Component c) {
		this.designer = designer;
		this.xCreator = (XCreator) c;
	}
    /**
     * ʵ�������������������������г�ʼ��
     */
	public void initialize() {
		initButtonText();
		Dimension initialSize = xCreator.getPreferredSize();
		xCreator.setSize(initialSize);
		LayoutUtils.layoutContainer(xCreator);
	}

	private void initButtonText() {
		Widget widget = xCreator.toData();
		if (xCreator instanceof XButton && StringUtils.isEmpty(((Button) widget).getText())) {
			((Button) xCreator.toData()).setText(widget.getWidgetName());
			((XButton) xCreator).setButtonText(widget.getWidgetName());
		}
	}

	@Override
	public void paintComponentMascot(Graphics g) {
        //����Ӧ�������Ⱦ�е����⣬��ק�Ŀؼ����óɰ�͸��
        Graphics2D g2d = (Graphics2D) g;
        AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,.5f);
        g2d.setComposite(composite);
		xCreator.paint(g2d);
		g.setColor(XCreatorConstants.RESIZE_BOX_BORDER_COLOR);
		g.drawRect(0, 0, xCreator.getWidth() - 1, xCreator.getHeight() - 1);
	}

	@Override
	public JPopupMenu getContextPopupMenu(MouseEvent e) {
		JPopupMenu popupMenu = new JPopupMenu();
		if (changeVarNameAction == null) {
			changeVarNameAction = new ChangeNameAction(designer);
		}
        //�ײ㲼�ֻ���������Ӧ���ֶ�����ɾ��
        boolean isRootComponent = ComponentUtils.isRootComponent(xCreator) || designer.isRoot(xCreator);
		changeVarNameAction.setEnabled(!isRootComponent);
		popupMenu.add(changeVarNameAction);

		Action[] actions = designer.getActions();
		for (Action action : actions) {
            action.setEnabled(!designer.isRootRelatedAction(((UpdateAction)action).getName()) || !isRootComponent);
			popupMenu.add(action);
		}
		return popupMenu;
	}

	private ChangeNameAction changeVarNameAction;

	private ArrayList<PropertyGroupModel> createPropertyGroupModels(CRPropertyDescriptor[] properties) {
		HashMap<String, ArrayList<CRPropertyDescriptor>> maps = new HashMap<String, ArrayList<CRPropertyDescriptor>>();
		ArrayList<String> groupNames = new ArrayList<String>();
		for (CRPropertyDescriptor property : properties) {
			String groupName = (String) property.getValue(XCreatorConstants.PROPERTY_CATEGORY);
			if (StringUtils.isEmpty(groupName)) {
				groupName = XCreatorConstants.DEFAULT_GROUP_NAME;
			}
			ArrayList<CRPropertyDescriptor> groupProperties = maps.get(groupName);
			if (groupProperties == null) {
				groupProperties = new ArrayList<CRPropertyDescriptor>();
				maps.put(groupName, groupProperties);
				groupNames.add(groupName);
			}
			groupProperties.add(property);
		}
		ArrayList<PropertyGroupModel> groups = new ArrayList<PropertyGroupModel>();
		for (String groupName : groupNames) {
			ArrayList<CRPropertyDescriptor> groupProperties = maps.get(groupName);
			PropertyGroupModel groupModel = new PropertyGroupModel(groupName, xCreator, groupProperties
					.toArray(new CRPropertyDescriptor[0]), designer);
			groups.add(groupModel);
		}
		return groups;
	}

	@Override
	public ArrayList<GroupModel> getXCreatorPropertyModel() {
		ArrayList<GroupModel> groupModels = new ArrayList<GroupModel>();
		CRPropertyDescriptor[] properties;
		properties = getCalculateCreatorProperties();
		ArrayList<PropertyGroupModel> groups = createPropertyGroupModels(properties);
		Collections.sort(groups);
		groupModels.addAll(groups);
		return groupModels;
	}
	
	/**
	 * ����Ӧ�����з����ı�����õ�scaleLayout�ͱ���顢ͼ���֧�ֵı���ؼ��õ�titleLayoutʱ
	 * �ؼ�����ֻ��ʾ�����������ǿؼ����Ի���Ϊ�����
	 * @return
	 */
	private CRPropertyDescriptor[] getCalculateCreatorProperties() {
		try {
			return xCreator.getPropertyDescriptorCreator().supportedDescriptor();
		} catch (IntrospectionException ex) {
			FRContext.getLogger().error(ex.getMessage(), ex);
			return new CRPropertyDescriptor[0];
		}
	}

	@Override
	public DesignerEditor<? extends JComponent> getDesignerEditor() {
		if (editorComponent == null) {
			editorComponent = xCreator.getDesignerEditor();
			if (editorComponent != null) {
				editorComponent.addPropertyChangeListener(new PropertyChangeAdapter() {

					@Override
					public void propertyChange() {
						designer.fireTargetModified();
					}
				});
			}
		}
		if (editorComponent != null) {
			editorComponent.reset();
		}
		return editorComponent;
	}
}
