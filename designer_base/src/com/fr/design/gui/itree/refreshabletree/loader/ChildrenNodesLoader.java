package com.fr.design.gui.itree.refreshabletree.loader;

import com.fr.design.gui.itree.refreshabletree.ExpandMutableTreeNode;


/**
 * �������ϵ��ӽڵ�
 * 
 * @editor zhou
 * @since 2012-3-28����9:57:40
 */
public interface ChildrenNodesLoader {

	/**
	 * �����ӽڵ�
	 * 
	 * @return
	 */
	ExpandMutableTreeNode[] load();

	public static ChildrenNodesLoader NULL = new ChildrenNodesLoader() {

		@Override
		public ExpandMutableTreeNode[] load() {
			return new ExpandMutableTreeNode[0];
		}
	};

}
