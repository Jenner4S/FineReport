package com.fr.design.gui.itree.filetree;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.fr.base.BaseUtils;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import com.fr.stable.project.ProjectConstants;


/**
 * File Tree.
 */
public class JFileTree extends AbstractFileTree {
    protected FileFilter fileFilter ;

    public JFileTree() {
        this(null);
    }

    public JFileTree(FileFilter filter) {
        this.init(filter);
    }

    private void init(FileFilter filter) {
        this.fileFilter = filter;

        DefaultTreeModel m_model = new DefaultTreeModel(new DefaultMutableTreeNode(Inter.getLocText("My_Computer")));
        this.setModel(m_model);
        
        this.putClientProperty("JTree.lineStyle", "Angled");

        this.addTreeExpansionListener(this);
        this.setCellRenderer(fileTreeCellRenderer);

        this.setRootVisible(false);
        this.setShowsRootHandles(true);
        this.setEditable(false);
    }

    public void setRootFile(File rootFile) {
    	setRootFiles(new File[] { rootFile });
    }

    public void setRootFiles(File[] rootFiles) {
        if (ArrayUtils.getLength(rootFiles) == 0) {
            return;
        }

        DefaultTreeModel m_model = (DefaultTreeModel) this.getModel();
        DefaultMutableTreeNode rootTreeNode = (DefaultMutableTreeNode) m_model.getRoot();
        rootTreeNode.removeAllChildren();

        for (int k = 0; k < rootFiles.length; k++) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(new RootFile(rootFiles[k]));
            rootTreeNode.add(node);

            if (rootFiles[k].isDirectory()) {
                node.add(new DefaultMutableTreeNode(Boolean.TRUE));
            }
        }
        // richer:����LocalEnv�����Ͳ������е����
        m_model.reload(rootTreeNode);

        if (rootFiles.length == 1) {
            File expandFile = rootFiles[0];
            this.selectFile(expandFile);
        }
    }

    public FileFilter getFileFilter() {
        return fileFilter;
    }

    public void setFileFilter(FileFilter fileFilter) {
        this.fileFilter = fileFilter;
    }

    public File getSelectedFile() {
        TreePath selectedTreePath = this.getSelectionPath();
        if(selectedTreePath == null) {
            return null;
        }

        DefaultMutableTreeNode currentTreeNode = this.getMutableTreeNode(selectedTreePath);
        StringBuffer fBuf = new StringBuffer();
        while (true) {
        	// ����Ѿ����˸��ڵ�,ֱ���˳�.
            if (currentTreeNode == null) {
                break;
            }

            Object object = currentTreeNode.getUserObject();
            if (object instanceof RootFile) {
            	// ��ǰ�ļ�.
            	RootFile rootFileNode = (RootFile) object;
                return new File(rootFileNode.getFile() + fBuf.toString());
            }

            FileDirectoryNode nameNode = (FileDirectoryNode) object;
            fBuf.insert(0, nameNode.getName());
            fBuf.insert(0, "/");

            // ��㷵��
            currentTreeNode = (DefaultMutableTreeNode) currentTreeNode.getParent();
        }

        return null;
    }

    /**
     * ͨ���ļ���Ѱ��չ��·��
     * @param currentFile ��ǰ�ļ�
     */
    public void selectFile(File currentFile) {
        if (currentFile == null) {
            return;
        }
        DefaultTreeModel m_model = (DefaultTreeModel) this.getModel();
        DefaultMutableTreeNode rootTreeNode = (DefaultMutableTreeNode) m_model.getRoot();
        int rootChildCount = rootTreeNode.getChildCount();
        for (int i = 0; i < rootChildCount; i++) {
            DefaultMutableTreeNode rootChildTreeNode = (DefaultMutableTreeNode) rootTreeNode.getChildAt(i);
            RootFile rootLocalFile = (RootFile) rootChildTreeNode.getUserObject();
            File rootFile = rootLocalFile.getFile();
            // �Ǹ��ӹ�ϵ,��ʼ�Һ���.
            if (AbstractFileTree.isParentFile(rootFile, currentFile)) {
                Stack nameStack = new Stack(); // �����е����ּ���Stack.
                while (true) {
                    if (ComparatorUtils.equals(rootFile, currentFile)) {
                        break;
                    }
                    nameStack.push(currentFile.getName());
                    currentFile = currentFile.getParentFile();
                    if (currentFile == null) {
                        break;
                    }
                }
                DefaultMutableTreeNode curChildTreeNode = rootChildTreeNode;
                while (!nameStack.isEmpty()) {
                    String name = (String) nameStack.pop();
                    this.expandTreeNode(curChildTreeNode);
                    for (int j = 0; j < curChildTreeNode.getChildCount(); j++) {
                        DefaultMutableTreeNode tmpChildTreeNode =
                                (DefaultMutableTreeNode) curChildTreeNode.getChildAt(j);
                        FileDirectoryNode tmpNameNode = (FileDirectoryNode) tmpChildTreeNode.getUserObject();
                        if (ComparatorUtils.equals(name, tmpNameNode.getName())) {
                            curChildTreeNode = tmpChildTreeNode;
                            // ѡ��ǰ�Ľڵ�.
                            this.setSelectionPath(new TreePath(m_model.getPathToRoot(curChildTreeNode)));
                            break;
                        }
                    }
                }
                break;
            }
        }
        TreePath selectedTreePath = this.getSelectionPath();
        if (selectedTreePath != null) {
            this.scrollPathToVisible(selectedTreePath);
        }
    }

    /**
     * �г���ǰ���е�File
     * @param currentTreeNode ��ǰ�ļ��ڵ�
     * @return ��ǰ�ڵ��µ�����File
     */
    public FileDirectoryNode[] listFileNodes(DefaultMutableTreeNode currentTreeNode) {
        StringBuffer fBuf = new StringBuffer();
        while (true) {
        	// ����Ѿ����˸��ڵ�,ֱ���˳�.
            if (currentTreeNode == null) {
                break;
            }
            Object object = currentTreeNode.getUserObject();
            if (object instanceof RootFile) {
            	RootFile rootFileNode = (RootFile) object;
                // ��ǰ�ļ�. (rootFileNode + fBuf.toString = Path����local��ַ)
                File currentFile = new File(rootFileNode.getFile() + fBuf.toString());
                // �г���ǰ�ļ����������ļ�,Ҫ�ж����Ƿ���ϵͳ�������ļ� �ܷ��. �򲻿��Ļ���ʾΪnull
                File[] files = currentFile.listFiles();
                // ����ļ��б�Ϊnull ����ΪFile[0] = []����null
                if (files == null ) {
                	return new FileDirectoryNode[0];
                }
                List fileNodeList = new ArrayList();
                for (int k = 0; k < files.length; k++) {
                    File tmpFile = files[k];
                    // �ļ�����Ϊ���صĻ�  �������б�
                    if (tmpFile.isHidden()) {
                        continue;
                    }
                    // ����ֻ��ʾ�ļ��� ������ ���ּ�
                    if (fileFilter.accept(tmpFile)) {
                    	// newNode ���� isDirectory���� ����ֻ��ʾ�ļ�������
                    	FileDirectoryNode newNode = FileDirectoryNode.createFileDirectoryNode(tmpFile);
                        fileNodeList.add(newNode);
                    }
                }
                // �ڵ�����б�
                FileDirectoryNode[] fileNodes = new FileDirectoryNode[fileNodeList.size()];
                fileNodeList.toArray(fileNodes);
                // ���ļ��н�������
                Arrays.sort(fileNodes, new FileNodeComparator());
                return fileNodes;
            }
            // ���ֽ�����㷴��Ļؼ�. ��:  Doload ==> C:\java\Doload ,���ص��ļ��е�path,��Ϊ�п�����String. ���Լ���instanceof
            if (object instanceof FileDirectoryNode) {
            	FileDirectoryNode nameNode = (FileDirectoryNode)object;
            	fBuf.insert(0, nameNode.getName());
            	fBuf.insert(0, "/");
            }
            // ��㷵��
            currentTreeNode = (DefaultMutableTreeNode) currentTreeNode.getParent();
        }
        return new FileDirectoryNode[0];
    }
    
    /**
     *  cellRenderer: tree����ʾ�ļ�������ͼ��
     */
	private DefaultTreeCellRenderer fileTreeCellRenderer = new DefaultTreeCellRenderer() {
		
		public Component getTreeCellRendererComponent(JTree tree, Object value,
				boolean selected, boolean expanded, boolean leaf, int row,
				boolean hasFocus) {
			super.getTreeCellRendererComponent(tree, value, selected, expanded,
					leaf, row, hasFocus);
			
			DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) value;
			StringBuffer fBuf = new StringBuffer();
			while(true) {
				if (treeNode == null) {
					break;
				}
				Object userObj = treeNode.getUserObject();
				if (userObj instanceof RootFile) {
					RootFile rootFileNode = (RootFile) userObj;
					// ��ǰ�ļ���ȫ��·��. (rootFileNode + fBuf.toString = Path����local��ַ)
					File currentFile = new File(rootFileNode.getFile() + fBuf.toString());
					FileSystemView view = FileSystemView.getFileSystemView();
					// File��ȫ��·��.
					// �õ�����treeͼ��
					Icon tmpIcon = view.getSystemIcon(currentFile);
                    if (currentFile.isDirectory() && fBuf.length() > 0) {
                        tmpIcon=BaseUtils.readIcon("/com/fr/design/images/gui/folder.png");
                    }
                    this.setIcon(tmpIcon);
					this.setName(null);
					Font oldFont = this.getFont();
					if(ComparatorUtils.equals(currentFile.getName(), ProjectConstants.WEBINF_NAME)){
						this.setForeground(Color.blue);
						this.setFont(new Font(oldFont.getName(),1,oldFont.getSize()));
					}else{
						this.setFont(new Font(oldFont.getName(),0,oldFont.getSize()));
					}
				}
				// ���ֽ�����㷴��Ļؼ�. ��:  Doload ==> C:\java\Doload 
				if (userObj instanceof FileDirectoryNode ) {
					FileDirectoryNode nameNode = (FileDirectoryNode)userObj;
					fBuf.insert(0, nameNode.getName());
					fBuf.insert(0, "/");
				}
				// ������� ���˷���
				treeNode = (DefaultMutableTreeNode) treeNode.getParent();
			}
			return this;
		}
	};
    
    /**
     *  ���ļ��н������� ���ļ��� Ȼ����������ļ�
     * @author kunsnat
     */
    public class FileNodeComparator implements Comparator {
        /**
         * This method should return > 0 if v1 is greater than v2, 0 if
         * v1 is equal to v2, or < 0 if v1 is less than v2.
         * It must handle null values for the comparison values.
         * ��������
         *
         * @param v1 comparison value.ֵ1
         * @param v2 comparison value.ֵ2
         * @return < 0, 0, or > 0 for v1<v2, v1==v2, or v1>v2 .ֵ1����ֵ2���ش���0����ȷ���0��С�ںʹ����෴
         */
        public int compare(Object v1, Object v2) {
            FileDirectoryNode nameNode1 = (FileDirectoryNode) v1;
            FileDirectoryNode nameNode2 = (FileDirectoryNode) v2;

            if (nameNode1.isDirectory()) {
                if (nameNode2.isDirectory()) {
                    return nameNode1.getName().toLowerCase().compareTo(nameNode2.getName().toLowerCase());
                } else {
                    return -1;
                }
            } else {
                if (nameNode2.isDirectory()) {
                    return 1;
                } else {
                    return nameNode1.getName().toLowerCase().compareTo(nameNode2.getName().toLowerCase());
                }
            }
        }
    }
}
