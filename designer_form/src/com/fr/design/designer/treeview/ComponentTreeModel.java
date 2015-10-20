package com.fr.design.designer.treeview;

import java.awt.Component;
import java.lang.reflect.Method;
import java.util.ArrayList;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.fr.base.FRContext;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWidgetCreator;
import com.fr.form.ui.Widget;

public class ComponentTreeModel implements TreeModel {

    private ArrayList<TreeModelListener> listeners = new ArrayList<TreeModelListener>();
    private Component root;
    private FormDesigner designer;

    public ComponentTreeModel(FormDesigner designer, Component root) {
        this.designer = designer;
        this.root = root;
    }

    @Override
    public Object getRoot() {
        return root;
    }

    @Override
    public Object getChild(Object parent, int index) {
        if (parent != null && parent instanceof XLayoutContainer) {
        	XLayoutContainer xlayout = (XLayoutContainer) parent;
        	XCreator creator = xlayout.getXCreator(index);
        	return creator.getXCreator();
        }
        return null;
    }
    /**
     * ���ø��ڵ�
     * 
     * @param root ���ڵ�
     */
    public void setRoot(Component root){
    	this.root=root;
    }

    @Override
    public int getChildCount(Object parent) {
        if (parent != null && parent instanceof XLayoutContainer) {
        	XLayoutContainer xlayout = (XLayoutContainer) parent;
        	return xlayout.getXCreatorCount();
        }
        return 0;
    }

    /**
     * �Ƿ�ΪҶ�ӽڵ�
     * @param node ����
     * @return ���򷵻�true
     */
    @Override
    public boolean isLeaf(Object node) {
        if (node != null && node instanceof XCreator) {
            return ((XCreator) node).isComponentTreeLeaf();
        }
        return true;
    }

    /**
     * ���ڵ�ֵ�ı�
     * @param path ���ṹ·��
     * @param newValue ��ֵ
     */
    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
        Object lastObject = path.getLastPathComponent();
        if (lastObject != newValue) {
            if (newValue instanceof String) {
                // Change varaible name
                XCreator component = (XCreator) lastObject;
                Widget wgt = ((XWidgetCreator)component).toData();
                wgt.setWidgetName((String)newValue);
				designer.getEditListenerTable().fireCreatorModified(component, DesignerEvent.CREATOR_EDITED);
            }
            TreeModelEvent event = new TreeModelEvent(this, path.getPath());
            fireEvent("treeNodesChanged", event);
        }
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        if (parent != null && parent instanceof XLayoutContainer) {
        	return ((XLayoutContainer)parent).getIndexOfChild(child);
        }
        return -1;
    }
    
    /**
     * ������¼�
     * @param l �¼�
     */
    @Override
    public void addTreeModelListener(TreeModelListener l) {
        if (!listeners.contains(l)) {
            listeners.add(l);
        }
    }

    /**
     * ɾ�����¼�
     * @param l �¼�
     */
    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        if (listeners.contains(l)) {
            listeners.remove(l);
        }
    }

    protected void fireEvent(String eventName, TreeModelEvent evt) {
        try {
            Method m = TreeModelListener.class.getMethod(eventName, TreeModelEvent.class);

            for (TreeModelListener listener : listeners) {
                m.invoke(listener, evt);
            }
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage(), e);
        }
    }
}
