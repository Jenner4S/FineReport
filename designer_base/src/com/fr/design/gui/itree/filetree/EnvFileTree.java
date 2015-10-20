package com.fr.design.gui.itree.filetree;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.dnd.DnDConstants;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import com.fr.design.constants.UIConstants;
import com.fr.design.event.TemplateTreeDragSource;
import com.fr.design.gui.ilable.UILabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.fr.base.Env;
import com.fr.base.FRContext;
import com.fr.design.gui.itree.refreshabletree.ExpandMutableTreeNode;
import com.fr.design.gui.itree.refreshabletree.RefreshableJTree;
import com.fr.file.filetree.FileNode;
import com.fr.file.filetree.FileNodeFilter;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.stable.CoreConstants;
import com.fr.stable.StableUtils;

/*
 * �ļ��ṹ��.
 */
public class EnvFileTree extends RefreshableJTree {

	protected FileNodeFilter filter;
	protected Env env;
	protected String treeRootPath = "";
	protected String[] subPathes;

	public EnvFileTree() {
		this(null, null);
	}

	public EnvFileTree(String[] subPathes, FileNodeFilter filter) {
		this("", subPathes, filter);
	}

	public EnvFileTree(String treeRootPath, String[] subPathes, FileNodeFilter filter) {
		super(new FileNode(treeRootPath, true));

		this.setTreeRootPath(treeRootPath);
		this.setFileNodeFilter(filter);
		this.setSubPathes(subPathes);

		this.init();
	}

	private void setTreeRootPath(String path) {
		if (path == null) {
			path = "";
		}

		this.treeRootPath = path;
	}

	public void setFileNodeFilter(FileNodeFilter filter) {
		this.filter = filter;
	}

	protected void init() {
		this.putClientProperty("JTree.lineStyle", "Angled");

		this.setCellRenderer(fileTreeCellRenderer);

		this.setRootVisible(false);
		this.setShowsRootHandles(true);
		this.setEditable(false);
        new TemplateTreeDragSource(this, DnDConstants.ACTION_COPY);
	}

	// CellRenderer
	private DefaultTreeCellRenderer fileTreeCellRenderer = new DefaultTreeCellRenderer() {

		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row,
				boolean hasFocus) {
			super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
			ExpandMutableTreeNode treeNode = (ExpandMutableTreeNode) value;
			Object userObj = treeNode.getUserObject();
			if (userObj instanceof FileNode) {
				FileNode node = (FileNode) userObj;
				String lock = node.getLock();
				String name = node.getName();
				if (lock != null && !node.getUserID().equals(lock)) {
					name = name + Inter.getLocText("Locked");
					this.setIcon(FileTreeIcon.getIcon(node));
				}else {
					this.setIcon(FileTreeIcon.getIcon(node, false));
				}
				this.setText(name);
			} else if (userObj == PENDING) {
				this.setIcon(null);
				this.setText(PENDING.toString());
			}
			// �����½�һ��Label��Ϊrender����ΪJTree�ڶ�̬ˢ�µ�ʱ�򣬽ڵ���render�����ĵĿ�Ȳ���䣬��ʹ��һ���ֱȽϳ���������ʾΪ"..."
			UILabel label = new UILabel();
			label.setText(getText());
			label.setIcon(getIcon());
			this.setSize(label.getPreferredSize());
			Dimension dim = label.getPreferredSize();
			dim.height += 2;
			this.setPreferredSize(dim);
			this.setBackgroundNonSelectionColor(UIConstants.NORMAL_BACKGROUND);
			this.setForeground(UIConstants.FONT_COLOR);
			this.setBackgroundSelectionColor(UIConstants.FLESH_BLUE);
			return this;
		}
	};

	/*
	 * �ڵ�ǰtree��ѡ��currentPath
	 */
	public void selectPath(String currentPath) {
		if (currentPath == null) {
			return;
		}

		DefaultTreeModel m_model = (DefaultTreeModel) this.getModel();
		ExpandMutableTreeNode treeNode = (ExpandMutableTreeNode) m_model.getRoot();
		for (int i = 0, len = treeNode.getChildCount(); i < len; i++) {
			// ȡ����ǰ��childTreeNode,��append��searchingPath����
			ExpandMutableTreeNode childTreeNode = (ExpandMutableTreeNode) treeNode.getChildAt(i);

			if (selectFilePath(childTreeNode, "", currentPath, m_model)) {
				break;
			}
		}

		TreePath selectedTreePath = this.getSelectionPath();
		if (selectedTreePath != null) {
			this.scrollPathToVisible(selectedTreePath);
		}
	}

	/*
	 * ��currentTreeNode����ѰfilePath
	 * 
	 * prefix + currentTreeNode.getName() = currentTreeNode����Ӧ��Path
	 * 
	 * ����currentTreeNode���Ƿ��ҵ���filePath
	 */
	private boolean selectFilePath(ExpandMutableTreeNode currentTreeNode, String prefix, String filePath, DefaultTreeModel m_model) {
		FileNode fileNode = (FileNode) currentTreeNode.getUserObject();
		String nodePath = fileNode.getName();
		String currentTreePath = prefix + nodePath;

		// ���equals,˵���ҵ���,����������ȥ��
		if (ComparatorUtils.equals(new File(currentTreePath), new File(filePath))) {
			this.setSelectionPath(new TreePath(m_model.getPathToRoot(currentTreeNode)));
			return true;
		} // �����ǰ·����currentFilePath��ParnetFile,��expandTreeNode,������������
		else if (EnvFileTree.isParentFile(currentTreePath, filePath)) {
			this.loadPendingChildTreeNode(currentTreeNode);

			prefix = currentTreePath + CoreConstants.SEPARATOR;
			for (int i = 0, len = currentTreeNode.getChildCount(); i < len; i++) {
				ExpandMutableTreeNode childTreeNode = (ExpandMutableTreeNode) currentTreeNode.getChildAt(i);

				if (selectFilePath(childTreeNode, prefix, filePath, m_model)) {
					return true;
				}
			}
			return false;
		}

		return false;
	}

	/*
	 * ��ǰTreeNode�����е�FileNode.
	 */
	private FileNode[] listFileNodes(ExpandMutableTreeNode currentTreeNode) {
		if (currentTreeNode == null) {
			return new FileNode[0];
		}

		Object object = currentTreeNode.getUserObject();

		if (object instanceof FileNode) {
			return this.listFileNodes(((FileNode) object).getEnvPath());
		}

		return new FileNode[0];
	}

	/*
	 * ��filePath���String,����·����������е�FileNode
	 */
	private FileNode[] listFileNodes(String filePath) {
		FileNode[] res_fns = null;

		try {
			res_fns = env == null ? new FileNode[0] : env.listFile(filePath);
		} catch (Exception e) {
			FRContext.getLogger().error(e.getMessage(), e);
		}

		if (res_fns == null) {
			res_fns = new FileNode[0];
		}

		// ��FileNodeFilter����һ��
		if (filter != null) {
			java.util.List<FileNode> t_list = new ArrayList<FileNode>();
			for (int i = 0; i < res_fns.length; i++) {
				if (filter.accept(res_fns[i])) {
					t_list.add(res_fns[i]);
				}
			}

			res_fns = t_list.toArray(new FileNode[t_list.size()]);
		}

		Arrays.sort(res_fns, new FileNodeComparator());

		return res_fns;
	}

	/*
	 * ��ȡ��ǰѡ�е�FilePath��String,���FilePath����Ҫƴ������
	 */
	public FileNode getSelectedFileNode() {
		TreePath selectedTreePath = this.getSelectionPath();
		if (selectedTreePath == null) {
			return null;
		}

		ExpandMutableTreeNode currentTreeNode = (ExpandMutableTreeNode) selectedTreePath.getLastPathComponent();
		Object userObject = currentTreeNode.getUserObject();

		if (userObject instanceof FileNode) {
			return (FileNode) userObject;
		}

		return null;
	}

	/*
	 * �ı�Env��,���ݹ��캯��ʱ���õ�RootPathes,���¼���
	 */
	public void refreshEnv(Env env) {
		this.env = env;

		DefaultTreeModel m_model = (DefaultTreeModel) this.getModel();
		ExpandMutableTreeNode rootTreeNode = (ExpandMutableTreeNode) m_model.getRoot();
		rootTreeNode.removeAllChildren();

		FileNode[] fns;

		// ���rootPaths��null�Ļ��г������ļ�
		if (subPathes == null) {
			fns = listFileNodes(this.treeRootPath);
		} else {
			// ���¼����µ�FileDirectoryNode
			fns = new FileNode[subPathes.length];
			for (int i = 0; i < subPathes.length; i++) {
				fns[i] = new FileNode(StableUtils.pathJoin(new String[]{this.treeRootPath, subPathes[i]}), true);
			}
		}


		ExpandMutableTreeNode[] sub_tree_node_array = fileNodeArray2TreeNodeArray(fns);

		for (int i = 0; i < sub_tree_node_array.length; i++) {
			ExpandMutableTreeNode node = sub_tree_node_array[i];
			rootTreeNode.add(node);
		}

		m_model.reload(rootTreeNode);
	}

	/*
	 * ���õ�ǰTree��rootPathes
	 */
	private void setSubPathes(String[] subPathes) {
		this.subPathes = subPathes;
	}

	/**
	 * currentTreeNode���������PENDING�Ľڵ�,����֮...
	 */
	private void loadPendingChildTreeNode(ExpandMutableTreeNode currentTreeNode) {
		if (currentTreeNode.isLeaf()) {
			return;
		}

		// �жϵ�һ�����ӽڵ�.UserObject�ǲ���PENDING,�����PENDING�Ļ�,��Ҫ���¼������TreeNode
		ExpandMutableTreeNode flag = (ExpandMutableTreeNode) currentTreeNode.getFirstChild();
		if (flag == null || flag.getUserObject() != PENDING) {
			return;
		}
		currentTreeNode.removeAllChildren(); // ɾ�����еĽڵ�.

		ExpandMutableTreeNode[] children = loadChildTreeNodes(currentTreeNode);
		for (ExpandMutableTreeNode c : children) {
			currentTreeNode.add(c);
		}
	}

	/*
	 * �ж�eTreeNode�Ƿ���ҪRefresh,����ǰ��ֹ,����true���ʾ��ǰ��ֹ,����ҪRefresh
	 */
	protected boolean interceptRefresh(ExpandMutableTreeNode eTreeNode) {
		Object userObject = eTreeNode.getUserObject();
		if (userObject instanceof FileNode && !((FileNode) userObject).isDirectory()) {
			return true;
		}

		return eTreeNode.getChildCount() == 1 && ((ExpandMutableTreeNode) eTreeNode.getFirstChild()).getUserObject() == PENDING;
	}

	/*
	 * �õ�treeNode���ӽڵ�ExpandMutableTreeNode������
	 */
	protected ExpandMutableTreeNode[] loadChildTreeNodes(ExpandMutableTreeNode treeNode) {
		FileNode[] fn_array = listFileNodes(treeNode);

		return fileNodeArray2TreeNodeArray(fn_array);
	}

	/*
	 * ��FileNode[]ת��ExpandMutableTreeNode[]
	 */
	private ExpandMutableTreeNode[] fileNodeArray2TreeNodeArray(FileNode[] fn_array) {
		ExpandMutableTreeNode[] res = new ExpandMutableTreeNode[fn_array.length];
		for (int i = 0; i < res.length; i++) {
			FileNode fn = fn_array[i];
			res[i] = new ExpandMutableTreeNode(fn);
			if (fn.isDirectory()) {
				res[i].add(new ExpandMutableTreeNode());
			}
		}

		return res;
	}

	/*
	 * �Ƿ��Ǹ��ӹ�ϵ���ļ�.
	 */
	private static boolean isParentFile(String parentFilePath, String childFilePath) {
		File parentFile = new File(parentFilePath);
		File childFile = new File(childFilePath);

		while (true) {
			if (ComparatorUtils.equals(parentFile, childFile)) {
				return true;
			}

			childFile = childFile.getParentFile();
			if (childFile == null) {
				break;
			}
		}

		return false;
	}
}
