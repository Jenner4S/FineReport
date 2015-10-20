package com.fr.design.gui.frpane;

import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;

import javax.swing.*;
import javax.swing.plaf.TabbedPaneUI;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date: 13-3-28
 * Time: ����9:14
 */
public class UITabbedPane extends JTabbedPane{

    private boolean isClosable = false; //Tab�Ƿ�ɹر�
    private String classPath;  //panel���������
    private String tabName;  //Tab����
    private  int tabSize = 0;
    public UITabbedPane() {
        super();
    }

    public UITabbedPane(int tabPlacement) {
        super(tabPlacement);
    }

    public UITabbedPane(int tabPlacement, int tabLayoutPolicy) {
        super(tabPlacement, tabLayoutPolicy);
    }

    public UITabbedPane(boolean closable,String tabname,String classpath){
        super();
        setClosable(closable);
        setTabName(tabname);
        setClassPath(classpath);
    }

    /**
     * ���tab
     * @param s tab��
     * @param component ���
     */
    public void addTab(String s, Component component) {
        if(isClosable() && ComparatorUtils.equals(s, getTabName())){
            super.addTab(s + tabSize, component);
        }else{
            super.addTab(s, component);
        }
        tabSize++;
    }

    /**
     * ����tab�ɹر�/���
     * @param isClosable �Ƿ�ɹر�/���
     */
    public void setClosable(boolean isClosable){
        this.isClosable = isClosable;
    }

    /**
     * tab�ɹر�
     * @return �����Ƿ�tab�ɹر�
     */
    public boolean isClosable(){
        return this.isClosable;
    }

    public void setClassPath(String classpath){
        this.classPath = classpath;
    }

    public String getClassPath(){
        return this.classPath;
    }

    public void setTabName(String tabname){
        this.tabName = tabname;
    }

    public String getTabName(){
        return this.tabName;
    }

    public void setTabSize(int tabsize){
        tabSize = tabsize;
    }

    public int getTabSize(){
        return tabSize;
    }
    @Override
    /**
     * ��ȡUI����
     */
    public TabbedPaneUI getUI(){
        return new UITabbedPaneUI();
    }

    @Override
    /**
     * ����UI
     */
    public void updateUI() {
        setUI(getUI());
    }

    /**
     * ɾ��tab������ֱ�Ӹ�дremoveTabAt
     * @param i tab����
     */
    public void doRemoveTab(int i){
        int re = JOptionPane.showConfirmDialog(SwingUtilities.getWindowAncestor(UITabbedPane.this), Inter.getLocText("sure_to_delete")+ "?", Inter.getLocText("Remove")
                , JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (re == JOptionPane.OK_OPTION) {
            super.removeTabAt(i);
        }
    }
}
