package com.fr.design.gui.itree.filetree;

import java.io.File;

import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.fr.general.ComparatorUtils;

/**
 * �ļ��ṹ��.
 */
public abstract class AbstractFileTree extends JTree implements TreeExpansionListener {
    /**
     * �г����е�FileNode.
     */
    public abstract FileDirectoryNode[] listFileNodes(DefaultMutableTreeNode currentTreeNode);

    /**
     * չ��TreeNode.
     */
    public boolean expandTreeNode(DefaultMutableTreeNode currentTreeNode) {
        if (currentTreeNode.isLeaf()) {
            return false;
        }

        //�жϵ�һ�����ӽڵ�.
        DefaultMutableTreeNode flag = (DefaultMutableTreeNode) currentTreeNode.getFirstChild();
        if (flag == null) {     // No flag
            return false;
        }
        Object firstChildObj = flag.getUserObject();
        if (!(firstChildObj instanceof Boolean)) {
            return false;      // Already expanded
        }
        currentTreeNode.removeAllChildren();  //ɾ�����еĽڵ�.

        // �г����е����ļ���.
        FileDirectoryNode[] fileNodes = listFileNodes(currentTreeNode);
        // �������Ϊ��,(��ʱ isDirectory��true �����ò������ļ� ���Բ�Ӧ��չ���ļ���)
        // �����ļ�����û���ļ�.
        if ( fileNodes == null) {
        	return false;
        }
        for (int i = 0; i < fileNodes.length; i++) {
        	FileDirectoryNode tmpNameNode = fileNodes[i];

            DefaultMutableTreeNode node = new DefaultMutableTreeNode(tmpNameNode);
            currentTreeNode.add(node);

            // �����Ŀ¼,��Ҫ����ӽڵ�.
            if (tmpNameNode.isDirectory()) {
            	// �����Ŀ¼�����в������ļ� �Ͳ�����ӽڵ�.
            	// �����ļ�����û�����ļ��� Ҳ������ӽڵ�.kt
            	FileDirectoryNode[] childFileNode = listFileNodes(node);
            	if (childFileNode != null ) {
            		node.add(new DefaultMutableTreeNode(Boolean.TRUE));
            	}
            }
        }

        // ���¼���parentTreeNode.
        ((DefaultTreeModel) this.getModel()).reload(currentTreeNode);
//        this.expandPath(GUICoreUtils.getTreePath(currentTreeNode));

        return true;
    }

    protected DefaultMutableTreeNode getMutableTreeNode(TreePath path) {
        return (DefaultMutableTreeNode) (path.getLastPathComponent());
    }

    public void treeExpanded(TreeExpansionEvent event) {
        DefaultMutableTreeNode currentTreeNode = getMutableTreeNode(event.getPath());
        // �жϵ�ǰ�ļ��ڵ��������ļ�Ϊnullʱ  ��չ���ڵ�.
        FileDirectoryNode[] fileNodes = listFileNodes(currentTreeNode);
        if(fileNodes != null) {
        	this.expandTreeNode(currentTreeNode);
        }
    }

    public void treeCollapsed(TreeExpansionEvent event) {
    }

    /**
     * �Ƿ��Ǹ��ӹ�ϵ���ļ�.
     */
    public static boolean isParentFile(File parentFile, File childFile) {
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

