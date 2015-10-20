/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 * ͼ�������ļ�
 */

package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.file.FILEChooserPane;
import com.fr.file.FILEChooserPane4Chart;
import com.fr.form.ui.ChartBook;
import com.fr.design.DesignModelAdapter;
import com.fr.design.event.TargetModifiedEvent;
import com.fr.design.event.TargetModifiedListener;
import com.fr.design.gui.imenu.UIMenuItem;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.actions.ExcelExportAction4Chart;
import com.fr.design.mainframe.actions.PDFExportAction4Chart;
import com.fr.design.mainframe.actions.PNGExportAction4Chart;
import com.fr.design.mainframe.form.FormECCompositeProvider;
import com.fr.design.mainframe.toolbar.ToolBarMenuDockPlus;
import com.fr.design.menu.ShortCut;
import com.fr.design.menu.ToolBarDef;
import com.fr.file.FILE;
import com.fr.general.Inter;
import com.fr.json.JSONArray;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;

/**
 * ͼ��crt�ļ�
 * <p/>
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-10-13
 * Time: ����2:28
 */
public class JChart extends JTemplate<ChartBook, ChartUndoState> {
    public static final String XML_TAG = "JChart";
    private static final String CHART_CARD = "FORM";
    private static final String ELEMENTCASE_CARD = "ELEMENTCASE";

    private static final String[] CARDNAME = new String[]{CHART_CARD, ELEMENTCASE_CARD};
    private static final int TOOLBARPANEDIMHEIGHT_FORM = 60;
    //ͼ�������
    ChartDesigner chartDesigner;

    //�м�༭����, carllayout����
    private JPanel tabCenterPane;
    private CardLayout cardLayout;
    //��ǰ�༭���������
    private JComponent editingComponent;
    private FormECCompositeProvider reportComposite;

    public JChart() {
        super(new ChartBook(), "Chart");
    }

    public JChart(ChartBook chartFile, FILE file) {
        super(chartFile, file);
    }

    @Override
    protected JPanel createCenterPane() {
        tabCenterPane = FRGUIPaneFactory.createCardLayout_S_Pane();
        JPanel centerPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        centerPane.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, new Color(85, 85, 85)));
        chartDesigner = new ChartDesigner(this.getTarget());
        ChartArea area = new ChartArea(chartDesigner);
        centerPane.add(area, BorderLayout.CENTER);
        tabCenterPane.add(centerPane, CHART_CARD, 0);
        chartDesigner.addTargetModifiedListener(new TargetModifiedListener() {
                    public void targetModified(TargetModifiedEvent e) {
                        JChart.this.fireTargetModified();// ���ñ���*, ����ˢ�½���, ˢ�¹�������ť
                    }
                });

        this.add(tabCenterPane, BorderLayout.CENTER);
        return tabCenterPane;
    }

    /**
     * �Ƴ�ѡ��
     */
    public void removeTemplateSelection() {

    }

    /**
     * ˢ������
     */
    public void refreshContainer() {

    }

    /**
     * �Ƴ��������ѡ��
     */
    public void removeParameterPaneSelection() {

    }

    /**
     * �������ģʽ
     *
     * @return ����ģʽ
     */
    protected DesignModelAdapter<ChartBook, ?> createDesignModel() {
        return null;
    }

    /**
     * ����Ԥ���ò˵�
     *
     * @return �˵�
     */
    public UIMenuItem[] createMenuItem4Preview() {
        return new UIMenuItem[0];
    }

    /**
     * ��������״̬
     *
     * @return ״̬
     */
    protected ChartUndoState createUndoState() {
        return new ChartUndoState(this,chartDesigner.getArea());
    }

    /**
     * Ӧ�ó���״̬
     *
     * @param chartUndoState ����״̬
     */
    protected void applyUndoState(ChartUndoState chartUndoState) {
        try {
            this.setTarget((ChartBook)chartUndoState.getChartBook().clone());
            chartDesigner.setTarget(this.getTarget());
            chartDesigner.populate();
        }catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * ��׺
     *
     * @return ��׺
     */
    public String suffix() {
        return ".crt";
    }

    /**
     * ����
     */
    public void copy() {

    }

    /**
     * ���
     *
     * @return �Ƿ������ɹ�
     */
    public boolean paste() {
        return false;
    }

    /**
     * �Ƿ����
     *
     * @return ���гɹ�
     */
    public boolean cut() {
        return false;
    }

    /**
     * ����Ȩ�ޱ༭���
     *
     * @return ���
     */
    public AuthorityEditPane createAuthorityEditPane() {
        return null;
    }

    /**
     * ������
     *
     * @return ������
     */
    public ToolBarMenuDockPlus getToolBarMenuDockPlus() {
        return null;
    }

    /**
     * �������
     *
     * @return ���
     */
    public JPanel getEastUpPane() {
        return null;
    }

    /**
     * �������
     *
     * @return ���
     */
    public JPanel getEastDownPane() {
        return null;
    }

    /**
     * �������˵�
     *
     * @return �˵�
     */
    public ToolBarDef[] toolbars4Target() {
        return new ToolBarDef[0];
    }

    /**
     * �����
     *
     * @return ���
     */
    public JPanel[] toolbarPanes4Form() {
        return new JPanel[0];
    }

    /**
     * ģ��˵�
     *
     * @return ģ��˵�
     */
    public ShortCut[] shortcut4TemplateMenu() {
        return new ShortCut[0];
    }

    /**
     * Ȩ�ޱ༭�˵�
     *
     * @return �˵�
     */
    public ShortCut[] shortCuts4Authority() {
        return new ShortCut[0];
    }

    /**
     * ��������
     *
     * @return ��
     */
    public JComponent[] toolBarButton4Form() {
        return new JComponent[0];
    }

    /**
     * Ȩ�ޱ༭������,����ͼ�����������������������������ȫ�ر༭
     *
     * @return ������
     */
    public JComponent toolBar4Authority() {
        return chartDesigner.getChartToolBarPane();
    }

    /**
     * �������߶�
     *
     * @return �������߶�
     */
    public int getToolBarHeight() {
        return 0;
    }

    /**
     * �Ƿ��Ǳ���
     *
     * @return ����
     */
    public boolean isJWorkBook() {
        return false;
    }

    /**
     * �Ƿ���ͼ��
     *
     * @return ���򷵻�true
     */
    public boolean isChartBook() {
        return true;
    }

    /**
     * ����Ȩ�ޱ༭ģʽ
     *
     * @param isUpMode û��Ȩ�ޱ༭
     */
    public void setAuthorityMode(boolean isUpMode) {

    }

    /**
     * ˢ�¹�������
     */
    public void refreshToolArea() {
        DesignerContext.getDesignerFrame().resetToolkitByPlus(JChart.this);
        chartDesigner.populate();
        ChartDesignerPropertyPane.getInstance().populateChartPropertyPane(getTarget().getChartCollection(), chartDesigner);
        EastRegionContainerPane.getInstance().replaceUpPane(ChartDesignerPropertyPane.getInstance());
    }

    /**
     * �����˵����Ӳ˵� ��Ŀǰ����ͼ�������
     *
     * @return �Ӳ˵�
     */
    public ShortCut[] shortcut4ExportMenu() {
        return new ShortCut[]{new PNGExportAction4Chart(this), new ExcelExportAction4Chart(this), new PDFExportAction4Chart(this)};
    }

    public Icon getIcon() {
        return BaseUtils.readIcon("/com/fr/design/images/chart.png");
    }

    public ChartDesigner getChartDesigner(){
        return chartDesigner;
    }

    /**
     * ����JS����
     */
    public void copyJS(){
        JSONObject jsonObject =this.getTarget().createExportConfig();
        String jsonString = StringUtils.EMPTY;
        if(jsonObject != null){
            try{
                if(jsonObject.has("charts")){
                    JSONArray charts = jsonObject.getJSONArray("charts");
                    jsonString = charts.toString(2);
                }else{
                    jsonString = jsonObject.toString(2);
                }
                JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), Inter.getLocText("FR-Chart-CopyJS_Message"), Inter.getLocText("FR-Chart-Action_Copy")+"JS", JOptionPane.INFORMATION_MESSAGE);
            }catch (JSONException ex){
                FRContext.getLogger().error(ex.getMessage());
                JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), Inter.getLocText("FR-Chart-CopyJS_Failed")+"!", Inter.getLocText("Error"), JOptionPane.ERROR_MESSAGE);
            }
        }else{
            JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), Inter.getLocText("FR-Chart-CopyJS_Failed")+"!", Inter.getLocText("Error"), JOptionPane.ERROR_MESSAGE);
        }
        StringSelection stringSelection = new StringSelection(jsonString);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
    };

    /**
     * ϵ�з��Ķ�
     */
    public void styleChange(){
          chartDesigner.clearToolBarStyleChoose();
    }

    protected FILEChooserPane getFILEChooserPane(boolean isShowLoc){
        return new FILEChooserPane4Chart(true, isShowLoc);
    }

}
