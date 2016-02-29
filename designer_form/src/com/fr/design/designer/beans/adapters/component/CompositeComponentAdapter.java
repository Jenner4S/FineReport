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
     * 实例化组件的适配器后，在这儿进行初始化
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
        //自适应交叉点渲染有点问题，拖拽的控件设置成半透明
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
        //底层布局或者是自适应布局都不能删除
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
	 * 自适应布局中放置文本框等用的scaleLayout和报表块、图表块支持的标题控件用的titleLayout时
	 * 控件树处只显示父容器，但是控件属性还是为自身的
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