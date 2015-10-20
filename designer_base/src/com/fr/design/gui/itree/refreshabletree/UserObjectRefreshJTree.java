package com.fr.design.gui.itree.refreshabletree;

import com.fr.general.NameObject;
import com.fr.design.gui.itree.refreshabletree.loader.ChildrenLoaderFactory;
import com.fr.general.ComparatorUtils;
import com.fr.design.utils.gui.GUICoreUtils;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public abstract class UserObjectRefreshJTree<T extends UserObjectOP<?>> extends RefreshableJTree {
	private static final long serialVersionUID = 8824111110910126977L;
	private TreePath[] oldPaths;
	private boolean isFind;

	public UserObjectRefreshJTree() {
		super();
		this.putClientProperty("JTree.lineStyle", "Angled");
		this.setShowsRootHandles(true);
		this.setRootVisible(false);

		// �ı�ѡ��ģʽ��Ϊ�����϶����������
		this.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);

		// alex:ѡ�ж���ڵ��ʱ��,������������ĳ�ڵ�ʱ,������ѡ���Ƕ���ڵ�
		this.addMouseListener(treeMouseListener);
	}

	/*
	* �ж�eTreeNode�Ƿ���ҪRefresh,����ǰ��ֹ,����true���ʾ��ǰ��ֹ,����ҪRefresh
	*/
	@Override
	protected boolean interceptRefresh(ExpandMutableTreeNode eTreeNode) {
		if (eTreeNode.getUserObject() instanceof UserObjectOP) {
			return false;
		}
		return eTreeNode.getChildCount() == 0 || ((ExpandMutableTreeNode) eTreeNode.getFirstChild()).getUserObject() == PENDING;
	}

	/**
	 * Populate
	 */
	public void populate(T userObject) {
		DefaultTreeModel treeModel = (DefaultTreeModel) this.getModel();
		ExpandMutableTreeNode root = (ExpandMutableTreeNode) treeModel.getRoot();
		root.setUserObject(userObject);
		root.removeAllChildren();

		ExpandMutableTreeNode[] children = loadChildTreeNodes(root);
		for (int i = 0; i < children.length; i++) {
			root.add(children[i]);
		}
		treeModel.reload(root);
		root.expandCurrentTreeNode(this);
	}

	@Override
	protected ExpandMutableTreeNode[] loadChildTreeNodes(ExpandMutableTreeNode selectedTreeNode) {
		Object userObj = selectedTreeNode.getUserObject();
		return ChildrenLoaderFactory.createChildNodesLoader(userObj).load();
	}

	/**
	 * �Ƶ���Ŀ¼�µ�
	 */
	public void removeNameObject(NameObject no) {
		DefaultTreeModel treeModel = (DefaultTreeModel) this.getModel();
		ExpandMutableTreeNode root = (ExpandMutableTreeNode) treeModel.getRoot();

		for (int i = 0, len = root.getChildCount(); i < len; i++) {
			ExpandMutableTreeNode childTreeNode = (ExpandMutableTreeNode) root.getChildAt(i);

			if (ComparatorUtils.equals(childTreeNode.getUserObject(), no)) {
				root.remove(childTreeNode);
				treeModel.reload(root);
				break;
			}
		}
	}

	/*
	* ����NameObjectȡTreePath
	*/
	public TreePath getTreePathByNameObject(NameObject nameObject) {
		if (nameObject == null) {
			return null;
		}

		DefaultTreeModel treeModel = (DefaultTreeModel) this.getModel();

		// �½�һ������NameObject��newChildTreeNode,�ӵ�Root����
		ExpandMutableTreeNode root = (ExpandMutableTreeNode) treeModel.getRoot();

		for (int i = 0, len = root.getChildCount(); i < len; i++) {
			ExpandMutableTreeNode childTreeNode = (ExpandMutableTreeNode) root.getChildAt(i);
			if (ComparatorUtils.equals(nameObject, childTreeNode.getUserObject())) {
				return GUICoreUtils.getTreePath(childTreeNode);
			}
		}

		return null;
	}

	/**
	 * ��һ�������Ƿ��ظ�
	 */
	public boolean isNameRepeated(String name) {
		DefaultTreeModel treeModel = (DefaultTreeModel) this.getModel();
		ExpandMutableTreeNode root = (ExpandMutableTreeNode) treeModel.getRoot();

		for (int i = 0, len = root.getChildCount(); i < len; i++) {
			ExpandMutableTreeNode childTreeNode = (ExpandMutableTreeNode) root.getChildAt(i);
			Object userObject = childTreeNode.getUserObject();
			if (userObject instanceof NameObject && ((NameObject) userObject).getName().endsWith(name)) {
				return true;
			}

		}


		return false;
	}

	public void setSelectionPath(TreePath treePath) {
		if (isFind) {
			return;
		}
		super.setSelectionPath(treePath);
	}

	private MouseListener treeMouseListener = new MouseAdapter() {
		public void mousePressed(MouseEvent e) {
			if (SwingUtilities.isLeftMouseButton(e)) {
				TreePath path = getPathForLocation(e.getX(), e.getY());
				isFind = false;
				if (path != null && oldPaths != null) {
					for (int i = 0; i < oldPaths.length; i++) {
						if (ComparatorUtils.equals(path, oldPaths[i])) {
							isFind = true;
							break;
						}
					}
				}
				// marks:������ϴ�ѡ�е�paths�ϣ����ϴε�paths��Ϊ������·��,����������ڵĽڵ���Ϊѡ�еĽڵ�
				if (!(e.isShiftDown() || e.isControlDown())) {
					if (isFind) {
						setSelectionPaths(oldPaths);
					} else {
						setSelectionPath(path);
					}
				}
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (SwingUtilities.isLeftMouseButton(e)) {
				oldPaths = getSelectionPaths();
			}
		}
	};
}
