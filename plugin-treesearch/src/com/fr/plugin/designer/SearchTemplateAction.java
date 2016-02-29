package com.fr.plugin.designer;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.design.actions.UpdateAction;
import com.fr.design.file.TemplateTreePane;
import com.fr.design.gui.itree.filetree.TemplateFileTree;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.stable.StableUtils;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * Created by vito on 2015/9/22
 */
public class SearchTemplateAction extends UpdateAction{
    static final int DEFAULTNUM = 0;


    private ArrayList<DefaultMutableTreeNode> selectedNodes = new ArrayList<DefaultMutableTreeNode>();
    public SearchTemplateAction(){
        //国际化文件配置在search.properties中
        this.setName(Inter.getLocText("FR-Designer_Template-Tree-Search"));
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/plugin/designer/resource/search.png"));
    }

    /**
     * 搜索动作响应
     * @param e 动作事件
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        final int SEARCH = 0;
        final int NEXT = 1;

        String[] s = {Inter.getLocText("Plugins-TreeSearch_Search"), Inter.getLocText("Plugins-TreeSearch_Next")};
        JEditorPane input = new JEditorPane();
        Object[] con = {Inter.getLocText("Plugins-TreeSearch_Please_enter_a_keyword_search"),input};
        int option;
        TemplateFileTree tree = TemplateTreePane.getInstance().getTemplateFileTree();
        File parFile = new File("");
        DefaultMutableTreeNode pNode = new DefaultMutableTreeNode(parFile);
        new JTree(pNode);
        File workSpace ;
        try {
            workSpace = new File(StableUtils.pathJoin(FRContext.getCurrentEnv().getPath(), "reportlets"));
            if(workSpace.isDirectory()) {
                preLoadDataTree(pNode, workSpace);
            }
            int searchFlag = 0;
            do{
                option = JOptionPane.showOptionDialog(null,con, Inter.getLocText("Plugins-TreeSearch_File_search"), JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE,null,s,s[0]);
                if(option == SEARCH){
                    String inputValue = input.getText();
                    selectedNodes = Search(pNode, inputValue);
                    if(selectedNodes != null) {
                        searchFlag = 1;
                        File finFile = (File)selectedNodes.get(DEFAULTNUM).getUserObject();
                        tree.setSelectedTemplatePath(finFile.getPath().replace(workSpace.getAbsolutePath() + File.separator, ""));//该函数是从WebReport\WEB-INF\reportlets\之后开始计算路径的
                        if(selectedNodes.size() == 1){
                            return;
                        }
                    }else {
                        JOptionPane.showMessageDialog(null, Inter.getLocText("Plugins-TreeSearch_No_Documents_Found"), Inter.getLocText("Plugins-TreeSearch_Search_Result"), JOptionPane.INFORMATION_MESSAGE);
                    }
                }else if( option == NEXT){
                    searchFlag = updateSearchFlag(tree, workSpace, searchFlag);
                }
            }while (option!=JOptionPane.CLOSED_OPTION);
        } catch (Exception e1) {
            FRLogger.getLogger().error("Error in SearchTree:"+e1.toString());
        }

    }

    private void preLoadDataTree(DefaultMutableTreeNode pNode, File workSpace) {
        File[] files = workSpace.listFiles();
        if(files!=null) {
            for (File file : files) {
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(file);
                pNode.add(node);
                if (file.isDirectory()){
                    loadDataTree(node, file);
                }
            }
        }
    }

    private int updateSearchFlag(TemplateFileTree tree, File workSpace, int searchFlag) {
        if(searchFlag < selectedNodes.size()) {
            File finFile = (File) selectedNodes.get(searchFlag).getUserObject();
            tree.setSelectedTemplatePath(finFile.getPath().replace(workSpace.getAbsolutePath() + File.separator, ""));
            searchFlag++;
        }else {
            JOptionPane.showMessageDialog(null, Inter.getLocText("Plugins-TreeSearch_Already_The_Last"));
            searchFlag = 0;
        }
        return searchFlag;
    }

    private void searchTree(){

    }
    /**
     * 递归载入目录树
     * @param dataTree 目录树节点
     * @param file 目录所在文件夹
     */
    public void loadDataTree(DefaultMutableTreeNode dataTree,File file){
            File[] files = file.listFiles();
            DefaultMutableTreeNode node ;
        if(files != null){
            for(File f:files){
                node = new DefaultMutableTreeNode(f);
                dataTree.add(node);
                if(f.isDirectory()){
                    loadDataTree(node,f);
                }
            }
        }

    }

    /**
     * 搜索方法，忽略大小写
     * @param root
     *       该树的根节点
     * @param searchText
     * 目标字符串
     * @return 返回结果list
     */
    public ArrayList<DefaultMutableTreeNode> Search(DefaultMutableTreeNode root,String searchText){
        Enumeration<DefaultMutableTreeNode> e = root.postorderEnumeration();//先序遍历
        ArrayList<DefaultMutableTreeNode> result = new ArrayList<DefaultMutableTreeNode>();
        boolean resultFlag = false;
        while(e.hasMoreElements()){
            DefaultMutableTreeNode dn = e.nextElement();
            File cunFile = (File)dn.getUserObject();
            if(!(ComparatorUtils.equals(searchText,""))
                    && !(ComparatorUtils.equals(cunFile.getName(), "reportlets"))
                    && cunFile.getName().toUpperCase().contains(searchText.toUpperCase())){
                result.add(dn);
                resultFlag = true;
            }
        }
        if(resultFlag) {
            if(result.size()>1){
                JOptionPane.showMessageDialog(null, result.size() +Inter.getLocText("Plugins-TreeSearch_Found_Templates"),Inter.getLocText("Plugins-TreeSearch_Search_Result"), JOptionPane.INFORMATION_MESSAGE);
            }
            return result;
        }
        else return null;
    }

    /**
     * 查询有多个结果时，显示下一个结果
     */
    public void findNext(){
        Iterator<DefaultMutableTreeNode> it = selectedNodes.iterator();
        if(it.hasNext()){
            TemplateFileTree tree = TemplateTreePane.getInstance().getTemplateFileTree();
            File finFile = (File)it.next().getUserObject();
            tree.setSelectedTemplatePath(finFile.getPath().replace(new File(StableUtils.pathJoin(FRContext.getCurrentEnv().getPath(),"reportlets")).getAbsolutePath() + File.separator,""));
        }
    }
}