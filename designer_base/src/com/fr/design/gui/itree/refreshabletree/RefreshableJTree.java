package com.fr.design.gui.itree.refreshabletree;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.*;

import com.fr.general.NameObject;
import com.fr.design.constants.UIConstants;
import com.fr.design.gui.itooltip.UIToolTip;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;

import java.awt.*;
import java.awt.event.MouseEvent;

public abstract class RefreshableJTree extends JTree {
    private static final int WIDTH_BETWEEN_NODES = 20; //tree���ӽڵ�֮�������������صĲ�
    private Icon icon;

    public static final Object PENDING = new Object() {

        @Override
        public String toString() {
            return Inter.getLocText("Loading") + "...";
        }
    };

    public RefreshableJTree() {
        this(null);
    }

    public RefreshableJTree(Object rootObj) {
        super(new DefaultTreeModel(new ExpandMutableTreeNode(rootObj)));
        DefaultTreeModel model = (DefaultTreeModel) getModel();
        ExpandMutableTreeNode root = (ExpandMutableTreeNode) model.getRoot();
        root.setExpanded(true);
        this.setRootVisible(false);
        this.setBackground(UIConstants.NORMAL_BACKGROUND);
        this.addTreeExpansionListener(expansion);
        this.addTreeWillExpandListener(willExpand);
    }

    private TreeExpansionListener expansion = new TreeExpansionListener() {

        @Override
        public void treeCollapsed(TreeExpansionEvent event) {
            TreePath treePath = event.getPath();
            if (treePath == null) {
                return;
            }

            ExpandMutableTreeNode treeNode = ((ExpandMutableTreeNode) treePath.getLastPathComponent());
            treeNode.setExpanded(false);
        }

        @Override
        public void treeExpanded(TreeExpansionEvent event) {
            TreePath treePath = event.getPath();
            if (treePath == null) {
                return;
            }

            ExpandMutableTreeNode treeNode = ((ExpandMutableTreeNode) treePath.getLastPathComponent());
            treeNode.setExpanded(true);
        }
    };
    private TreeWillExpandListener willExpand = new TreeWillExpandListener() {

        @Override
        public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
        }

        @Override
        public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
            TreePath treePath = event.getPath();
            if (treePath == null) {
                return;
            }

            final ExpandMutableTreeNode treeNode = ((ExpandMutableTreeNode) treePath.getLastPathComponent());

            if (treeNode.getChildCount() == 1 && ((ExpandMutableTreeNode) treeNode.getFirstChild()).getUserObject() == PENDING) {
                //������ļ�����ֻ��һ��Ĭ�����ӵ����ڼ��ص��ӽڵ㣬����ʾ���ڼ��أ����ǿվ�����
                new SwingWorker<Long, Void>() {

                    @Override
                    protected Long doInBackground() throws Exception {
                        long startTime = System.currentTimeMillis();
                        ExpandMutableTreeNode[] nodes = RefreshableJTree.this.loadChildTreeNodes(treeNode);
                        for (int i = 0; i < nodes.length; i++) {
                            treeNode.add(nodes[i]);
                        }
                        DefaultTreeModel treeModel = (DefaultTreeModel) RefreshableJTree.this.getModel();
                        // ��Ҫ��ʱ��������treeUI����Ⱦ���ˣ����԰�����ŵ������߳�����
                        if (treeNode.getChildCount() >= 1 && ((ExpandMutableTreeNode) treeNode.getFirstChild()).getUserObject() == PENDING) {
                            treeNode.remove(0);
                        }
                        treeModel.nodeStructureChanged(treeNode);
                        long usedTime = System.currentTimeMillis() - startTime;
                        return usedTime;
                    }

                    @Override
                    protected void done() {
                        RefreshableJTree.this.updateUI();
                        // �ָ�Tree�Ŀ�����
                        RefreshableJTree.this.setEnabled(true);
                    }

                }.execute();
            }
        }
    };

    /**
     * @return
     */
    public boolean isTemplateShowing() {
        return ((ExpandMutableTreeNode) this.getModel().getRoot()).getChildCount() == 0 ? false : true;
    }

    /*
      * ˢ��
      */
    public void refresh() {
        refresh((ExpandMutableTreeNode) this.getModel().getRoot(), StringUtils.EMPTY);
    }

    public void refreshChildByName(String childName) {
        refresh((ExpandMutableTreeNode) this.getModel().getRoot(), childName);
    }

    /*
      * ˢ��expandRoot�ڵ��������Ѵ򿪵Ľڵ��UserObject,����isExpandedΪtrue��TreeNode
      */
    private void refresh(ExpandMutableTreeNode expandRoot, String childName) {
        if (expandRoot == null) {
            return;
        }
        refreshTreeNode(expandRoot, childName);

        // model.reload, then do expand treenode that isExpanded is true
        ((DefaultTreeModel) this.getModel()).reload(expandRoot);

        // չ������isExpandedΪtrue��TreeNode
        expandRoot.expandCurrentTreeNode(this);
    }

    /*
      * ˢ��eTreeNode�������е�����ɹ�ȡ���ķ�Ҷ�ӽڵ����Ҷ����UserObject
      */
    protected void refreshTreeNode(ExpandMutableTreeNode eTreeNode, String childName) {
        // ���eTreeNode��δȡ��״̬,����expand
        if (interceptRefresh(eTreeNode)) {
            return;
        }

        // ˢ�µ�ǰeTreeNode������ӽڵ��UserObject������
        ExpandMutableTreeNode[] new_nodes = loadChildTreeNodes(eTreeNode);

        /*
           * �����µ�ǰeTreeNode�µ�ChildTreeNode��childTreeNodeList �Ƴ�����ChildTreeNode
           * ����childUserObjects��childTreeNodeList�ıȶ�,���¹���eTreeNode
           */
        java.util.List<DefaultMutableTreeNode> childTreeNodeList = new java.util.ArrayList<DefaultMutableTreeNode>();
        for (int i = 0, len = eTreeNode.getChildCount(); i < len; i++) {
            if (eTreeNode.getChildAt(i) instanceof ExpandMutableTreeNode) {
                childTreeNodeList.add((ExpandMutableTreeNode) eTreeNode.getChildAt(i));
            } else {
                childTreeNodeList.add((DefaultMutableTreeNode) eTreeNode.getChildAt(i));
            }
        }

        eTreeNode.removeAllChildren();

        for (int ci = 0; ci < new_nodes.length; ci++) {
            Object cUserObject = new_nodes[ci].getUserObject();

            for (int ni = 0, nlen = childTreeNodeList.size(); ni < nlen; ni++) {
                ExpandMutableTreeNode cTreeNode = (ExpandMutableTreeNode) childTreeNodeList.get(ni);
                if (ComparatorUtils.equals(cTreeNode.getUserObject(), cUserObject)) {
                    new_nodes[ci].setExpanded(cTreeNode.isExpanded());
                    break;
                }
            }

            eTreeNode.add(new_nodes[ci]);
        }
    }

    /*
      * �ж�eTreeNode�Ƿ���ҪRefresh,����ǰ��ֹ,����true���ʾ��ǰ��ֹ,����ҪRefresh
      */
    protected abstract boolean interceptRefresh(ExpandMutableTreeNode eTreeNode);

    /*
      * �õ�treeNode���ӽڵ�ExpandMutableTreeNode������
      */
    protected abstract ExpandMutableTreeNode[] loadChildTreeNodes(ExpandMutableTreeNode treeNode);

    public NameObject getSelectedNameObject() {
        TreePath selectedTreePath = this.getSelectionPath();
        if (selectedTreePath == null) {
            return null;
        }

        ExpandMutableTreeNode selectedTreeNode = (ExpandMutableTreeNode) selectedTreePath.getLastPathComponent();
        Object selectedUserObject = selectedTreeNode.getUserObject();
        if (selectedUserObject instanceof NameObject) {
            return (NameObject) selectedUserObject;
        }

        selectedTreeNode = (ExpandMutableTreeNode) selectedTreeNode.getParent();
        selectedUserObject = selectedTreeNode.getUserObject();
        if (selectedUserObject instanceof NameObject) {
            return (NameObject) selectedUserObject;
        }
        return null;
    }

    public String getToolTipText(MouseEvent event) {
        String tip = null;
        icon = new ImageIcon();

        if (event != null) {
            Point p = event.getPoint();
            int selRow = getRowForLocation(p.x, p.y);
            TreeCellRenderer r = getCellRenderer();

            if (selRow != -1 && r != null) {
                int i = 0;              //tree�ڵ�ļ���
                TreePath path = getPathForRow(selRow);
                Object lastPath = path.getLastPathComponent();
                if (lastPath instanceof TreeNode) {
                    TreeNode treeNode = (TreeNode) lastPath;
                    while (treeNode.getParent() instanceof TreeNode) {
                        i++;
                        treeNode = treeNode.getParent();
                    }
                }
                Component rComponent = r.getTreeCellRendererComponent
                        (this, lastPath, isRowSelected(selRow),
                                isExpanded(selRow), getModel().isLeaf(lastPath), selRow,
                                true);

                if (rComponent instanceof JComponent && rComponent.getPreferredSize().getWidth() + i * WIDTH_BETWEEN_NODES > getVisibleRect().getWidth()) {
                    tip = ((DefaultTreeCellRenderer) r).getText();
                    icon = ((DefaultTreeCellRenderer) r).getIcon();
                }
            }
        }
        if (tip == null) {
            tip = getToolTipText();
        }
        return tip;
    }

    public Point getToolTipLocation(MouseEvent event) {
        if (event != null) {
            Point p = event.getPoint();
            int selRow = getRowForLocation(p.x, p.y);
            TreeCellRenderer r = getCellRenderer();
            if (selRow != -1 && r != null) {
                TreePath path = getPathForRow(selRow);
                Rectangle pathBounds = getPathBounds(path);
                return new Point(pathBounds.x - 2, pathBounds.y - 1);
            }
        }
        return null;
    }

    public JToolTip createToolTip() {
        UIToolTip tip = new UIToolTip(icon);
        tip.setComponent(this);
        tip.setOpaque(false);
        return tip;
    }
}
