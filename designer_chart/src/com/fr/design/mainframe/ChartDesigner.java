/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.mainframe;
import com.fr.form.ui.ChartBook;
import com.fr.design.designer.TargetComponent;
import com.fr.design.mainframe.toolbar.ToolBarMenuDockPlus;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.ShortCut;
import com.fr.design.menu.ToolBarDef;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-10-13
 * Time: ����4:30
 */
public class ChartDesigner extends TargetComponent<ChartBook>  implements MouseListener{

    private ChartArea chartArea;//�ϲ�����
    private boolean hasCalGap = false;
    private ChartDesignerUI designerUI;
    private ArrayList<ChangeListener> changeListeners = new ArrayList<ChangeListener>();
    private ChartToolBarPane chartToolBarPane;

    public ChartDesigner(ChartBook chartBook) {
        super(chartBook);
        this.addMouseListener(this);
        designerUI = new ChartDesignerUI();
        chartToolBarPane = new ChartToolBarPane(this){
            public Dimension getPreferredSize() {
                Dimension size = super.getPreferredSize();
                return new Dimension(size.width, ChartToolBarPane.TOTAL_HEIGHT);
            }
        };
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if(designerUI!=null){
                    designerUI.mouseMoved(e);
                    ChartDesigner.this.repaint();
                }
            }
        });
        updateUI();// ��ʼ��������ƹ��ߵ�UIʵ��
    }

    /**
     * ������UI��ΪDesignerUI��������Ⱦ
     */
    @Override
    public void updateUI() {
        setUI(designerUI);
    }

    /**
     * �����ϲ�����
     * @param chartArea ͼ������
     */
    public void setParent(ChartArea chartArea) {
        this.chartArea = chartArea;
    }

    /**
     * ����
     */
    public void copy() {

    }

    /**
     * ���
     * @return �ɹ�����true
     */
    public boolean paste() {
        return false;
    }

    /**
     * ����
     * @return �ɹ�����TRUE
     */
    public boolean cut() {
        return false;
    }

    /**
     * ֹͣ�༭
     */
    public void stopEditing() {

    }

    /**
     * Ȩ�ޱ༭���
     * @return ���
     */
    public AuthorityEditPane createAuthorityEditPane() {
        return null;
    }

    /**
     * ������
     * @return ������
     */
    public ToolBarMenuDockPlus getToolBarMenuDockPlus() {
        return null;
    }

    /**
     * �˵�״̬
     * @return ״̬
     */
    public int getMenuState() {
        return 0;
    }

    /**
     * �������
     * @return ���
     */
    public JPanel getEastUpPane() {
        return null;
    }

    /**
     * �������
     * @return ���
     */
    public JPanel getEastDownPane() {
        return null;
    }

    /**
     * ȡ����ʽ
     */
    public void cancelFormat() {

    }

    /**
     * ͼ��������ù�������
     * @return ͼ��������ù�������
     */
    public ToolBarDef[] toolbars4Target() {
        return new ToolBarDef[0];
    }

    /**
     * �˵�
     * @return �˵�
     */
    public MenuDef[] menus4Target() {
        return new MenuDef[0];
    }

    /**
     * �˵���
     * @return �˵���
     */
    public ShortCut[] shortcut4TemplateMenu() {
        return new ShortCut[0];
    }

    /**
     * Ȩ�ޱ༭�ò˵���
     * @return �˵���
     */
    public ShortCut[] shortCuts4Authority() {
        return new ShortCut[0];
    }

    /**
     * ���ù�������ť
     * @return ���ù�������ť
     */
    public JComponent[] toolBarButton4Form() {
        return new JComponent[0];
    }


    /**
     * ���ر�����
     * @return ������
     */
    public ChartArea getArea() {
        return chartArea;
    }


    /**
     * �����
     * @param e �¼�
     */
    public void mouseClicked(MouseEvent e) {
        designerUI.mouseClicked(e);
        this.chartToolBarPane.populate();
    }

    /**
     * ��갴��
     * @param e �¼�
     */
    public void mousePressed(MouseEvent e) {

    }

    /**
     * ����ͷ�
     * @param e �¼�
     */
    public void mouseReleased(MouseEvent e) {

    }

    /**
     * ������
     * @param e �¼�
     */
    public void mouseEntered(MouseEvent e) {

    }

    /**
     * ����˳�
     * @param e �¼�
     */
    public void mouseExited(MouseEvent e) {

    }


    private void registerChangeListener(ChangeListener changeListener){
        if(changeListener == null){
            return;
        }
        this.changeListeners.add(changeListener);
    }

    public ChartToolBarPane getChartToolBarPane(){
        return this.chartToolBarPane;
    }

    public void populate(){
        this.chartToolBarPane.populate();
    }

    /**
     * �������������ȫ�ַ��ť��ѡ��
     */
    public void clearToolBarStyleChoose(){
        chartToolBarPane.clearStyleChoose();
    }
}
