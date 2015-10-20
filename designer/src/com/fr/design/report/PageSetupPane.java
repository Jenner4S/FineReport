/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.report;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.Icon;

import com.fr.page.PaperSettingProvider;
import com.fr.page.ReportSettingsProvider;
import com.fr.design.gui.frpane.UITabbedPane;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.ilable.UILabel;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.fr.base.BaseUtils;
import com.fr.base.Margin;
import com.fr.base.PaperSize;
import com.fr.base.Utils;
import com.fr.design.DesignerEnvManager;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.icombobox.UIComboBoxRenderer;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.dialog.BasicPane;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.report.core.ReportUtils;
import com.fr.report.report.Report;
import com.fr.report.stable.ReportConstants;
import com.fr.report.stable.ReportSettings;
import com.fr.stable.Constants;
import com.fr.stable.CoreConstants;
import com.fr.stable.unit.CM;
import com.fr.stable.unit.INCH;
import com.fr.stable.unit.MM;
import com.fr.design.utils.gui.GUICoreUtils;

/**
 * @author richer
 * @since 6.5.5 ������2011-6-14 ҳ���������
 */
public class PageSetupPane extends BasicPane {
    private PagePane pagePane;
    private OtherPane otherPane;
    private UILabel zeroMarginWarn;
    
    public final static PaperSize MOBILE_SMAIL_SIZE = new PaperSize(new MM(142.8f), new MM(254));
    public final static PaperSize MOBILE_LARGE_SIZE = new PaperSize(new MM(190.5f), new MM(338.7f));
    
    public static final Object[][] MOBILE_NAME_SIZE_ARRAY = {
    	{Inter.getLocText("FR-Designer_PaperSize-Mobile-Large"),MOBILE_LARGE_SIZE},
    	{Inter.getLocText("FR-Designer_PaperSize-Mobile-Small"),MOBILE_SMAIL_SIZE}
    };
    
    public PageSetupPane() {
        this.initComponents();
    }

    private void initComponents() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        UITabbedPane centerTabbedPane = new UITabbedPane();
        this.add(centerTabbedPane, BorderLayout.CENTER);

        pagePane = new PagePane();
        otherPane = new OtherPane();
        centerTabbedPane.addTab(Inter.getLocText("PageSetup-Page"), pagePane);
        centerTabbedPane.addTab(Inter.getLocText("Other"), otherPane);
    }

    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("Page_Setup");
    }

    /**
     * Populate.
     *
     * @param report the populate rpt.
     */
    public void populate(Report report, int unitType) {
        if (report == null) {
            return;
        }

        this.pagePane.populate(report, unitType);
        this.otherPane.populate(report);
    }

    /**
     * Update.
     *
     * @param report the updated rpt.
     */
    public void update(Report report) {
        if (report == null) {
            return;
        }

        this.pagePane.update(report);
        this.otherPane.update(report);
    }

    private class PagePane extends BasicPane {

        private UIRadioButton portraitRadioButton;
        private UIRadioButton landscapeRadioButton;

        private UIRadioButton predefinedRadioButton;
        private UIRadioButton mobileRadioButton;
        private UIRadioButton customRadioButton;
        
        private UIComboBox predefinedComboBox;
        private UIComboBox mobileComboBox;
        
        private UIBasicSpinner paperWidthSpinner;
        private UIBasicSpinner paperHeightSpinner;
        private UIComboBox switchInch;

        private UnitFieldPane marginTopUnitFieldPane;
        private UnitFieldPane marginLeftUnitFieldPane;
        private UnitFieldPane marginBottomUnitFieldPane;
        private UnitFieldPane marginRightUnitFieldPane;

        private UnitFieldPane headerUnitFieldPane;
        private UnitFieldPane footerUnitFieldPane;

        // private JSpinner resolutionSpinner;

        private ShowPagePane showPagePane;

        private UnitFieldPane.UnitLabel unitLabel;

        private int unitType;
        private Report report;

        public PagePane() {
            this.setLayout(FRGUIPaneFactory.createBorderLayout());

            JPanel defaultPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_L_Pane();
            this.add(defaultPane, BorderLayout.NORTH);
            JPanel twoPane = FRGUIPaneFactory.createX_AXISBoxInnerContainer_S_Pane();
            twoPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            JPanel orientationPane = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("PageSetup-Orientation"));
            JPanel innerorientationPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_M_Pane();
            orientationPane.add(innerorientationPane);
            twoPane.add(orientationPane);

            JPanel portraitpanel = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
            portraitRadioButton = new UIRadioButton(Inter.getLocText("PageSetup-Portrait"));
            portraitRadioButton.setMnemonic('t');
            portraitpanel.add(portraitRadioButton);
            innerorientationPane.add(portraitpanel);
            portraitRadioButton.addActionListener(previewListener);

            JPanel landscapepanel = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
            landscapeRadioButton = new UIRadioButton(Inter.getLocText("PageSetup-Landscape"));
            innerorientationPane.add(landscapepanel);
            landscapeRadioButton.setMnemonic('L');
            landscapepanel.add(landscapeRadioButton);
            landscapeRadioButton.addActionListener(previewListener);

            ButtonGroup layoutButtonGroup = new ButtonGroup();
            layoutButtonGroup.add(portraitRadioButton);
            layoutButtonGroup.add(landscapeRadioButton);

            portraitRadioButton.setSelected(true);

            JPanel spp = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("Preview"));
            showPagePane = new ShowPagePane();
            spp.add(showPagePane);
            defaultPane.add(spp);
            defaultPane.add(twoPane);

            // paper size
            JPanel paperSizePane = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("PageSetup-Paper_Size"));
            JPanel innerpaperSizePane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_M_Pane();
            paperSizePane.add(innerpaperSizePane);
            defaultPane.add(paperSizePane);


            predefinedRadioButton = new UIRadioButton(Inter.getLocText("PageSetup-Predefined") + ":");
            predefinedRadioButton.setMnemonic('P');
            predefinedRadioButton.addActionListener(previewListener);
            
            mobileRadioButton = new UIRadioButton(Inter.getLocText("FR-Designer_MobilePhone") + "  :");
            mobileRadioButton.setMnemonic('M');
            mobileRadioButton.addActionListener(previewListener);
            
            
            
            customRadioButton = new UIRadioButton(Inter.getLocText("Custom") + ":");
            customRadioButton.setMnemonic('C');
            customRadioButton.addActionListener(previewListener);
            
            predefinedComboBox = new UIComboBox();
            mobileComboBox = new UIComboBox();
            
            paperWidthSpinner = new UIBasicSpinner(new SpinnerNumberModel(0.0, 0.0, Double.MAX_VALUE, 1.0));
            ((JSpinner.DefaultEditor) paperWidthSpinner.getEditor()).getTextField().setColumns(7);
            paperHeightSpinner = new UIBasicSpinner(new SpinnerNumberModel(0.0, 0.0, Double.MAX_VALUE, 1.0));
            ((JSpinner.DefaultEditor) paperHeightSpinner.getEditor()).getTextField().setColumns(7);
            unitLabel = new UnitFieldPane.UnitLabel(Constants.UNIT_MM, paperHeightSpinner.getPreferredSize().height);

            String[] inch = {Inter.getLocText("Unit_MM"), Inter.getLocText("Unit_INCH")};
            switchInch = new UIComboBox(inch);
            switchInch.setEditable(false);
            switchInch.setSize(paperHeightSpinner.getPreferredSize().width, paperHeightSpinner.getPreferredSize().height);
            switchInch.setSelectedIndex(unitType);
            switchInch.addActionListener(switchInchListener);

            predefinedComboBox.setRenderer(paperSizeCellRenderere);
            predefinedComboBox.addItemListener(paperSizeItemListener);
            
            mobileComboBox.setRenderer(paperSizeMobileCellRenderere);
            mobileComboBox.addItemListener(paperSizeItemMobileListener);

            ((JSpinner.DefaultEditor) paperWidthSpinner.getEditor()).getTextField().getDocument().addDocumentListener(customTextListener);
            ((JSpinner.DefaultEditor) paperHeightSpinner.getEditor()).getTextField().getDocument().addDocumentListener(customTextListener);

            paperWidthSpinner.addChangeListener(previewListener2);
            paperHeightSpinner.addChangeListener(previewListener2);

            for (int i = 0; i < ReportConstants.PaperSizeNameSizeArray.length; i++) {
                Object[] tmpPaperSizeNameArray = ReportConstants.PaperSizeNameSizeArray[i];
                predefinedComboBox.addItem(tmpPaperSizeNameArray[1]);
            }
            
            for(int i=0; i<MOBILE_NAME_SIZE_ARRAY.length;i++){
                Object[] tmpPaperSizeNameArray = MOBILE_NAME_SIZE_ARRAY[i];
                mobileComboBox.addItem(tmpPaperSizeNameArray[1]);
            }


            // tow radio buttons.
            JPanel radioButtonPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
            innerpaperSizePane.add(radioButtonPane);
            
            radioButtonPane.add(predefinedRadioButton);
            radioButtonPane.add(predefinedComboBox);
            
            // tow radio buttons.
            JPanel mobileButtonPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
            innerpaperSizePane.add(mobileButtonPane);
            
            mobileButtonPane.add(mobileRadioButton);
            mobileButtonPane.add(mobileComboBox);

            ButtonGroup paperSizeRadioButtonGroup = new ButtonGroup();
            paperSizeRadioButtonGroup.add(predefinedRadioButton);
            paperSizeRadioButtonGroup.add(mobileRadioButton);
            paperSizeRadioButtonGroup.add(customRadioButton);

            // size and textfields.
            JPanel paperSizeRightPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
            innerpaperSizePane.add(paperSizeRightPane);

            paperSizeRightPane.add(customRadioButton);
            paperSizeRightPane.add(paperWidthSpinner);
            paperSizeRightPane.add(new UILabel("x"));
            paperSizeRightPane.add(paperHeightSpinner);
            paperSizeRightPane.add(switchInch);


            // peter:���ñ߾�..
            JPanel outermarginPane = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("PageSetup-Margin"));
            JPanel marginPane = FRGUIPaneFactory.createX_AXISBoxInnerContainer_M_Pane();
            outermarginPane.add(marginPane);
            twoPane.add(outermarginPane);

            StringBuffer temp = new StringBuffer();
            for (int i = 0; i < 11; i++) {
                temp.append("&nbsp");
            }
            zeroMarginWarn = new UILabel("<html><body>" + temp + "<br><br></html></body>");
            zeroMarginWarn.setForeground(Color.RED);

            // left
            JPanel marginLeftPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_M_Pane();
            marginPane.add(marginLeftPane);

            JPanel marginLeftTextPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
            marginLeftPane.add(marginLeftTextPane);
            marginLeftTextPane.add(new UILabel(Inter.getLocText("Top") + ":"));
            marginTopUnitFieldPane = new UnitFieldPane(Constants.UNIT_MM);
            marginLeftTextPane.add(marginTopUnitFieldPane);
            JPanel marginLeftUnitPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
            marginLeftPane.add(marginLeftUnitPane);
            marginLeftUnitPane.add(new UILabel(Inter.getLocText("Bottom") + ":"));
            marginBottomUnitFieldPane = new UnitFieldPane(Constants.UNIT_MM);
            marginLeftUnitPane.add(marginBottomUnitFieldPane);

            // right
            JPanel marginRightPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_M_Pane();
            marginPane.add(marginRightPane);

            // peter:���һ����ֱ�����µ��ַ�panel.
            JPanel marginRightTextPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
            marginRightPane.add(marginRightTextPane);
            marginRightTextPane.add(new UILabel(Inter.getLocText("Left") + ":"));
            marginLeftUnitFieldPane = new UnitFieldPane(Constants.UNIT_MM);
            marginRightTextPane.add(marginLeftUnitFieldPane);

            JPanel marginRightUnitPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
            marginRightPane.add(marginRightUnitPane);
            marginRightUnitPane.add(new UILabel(Inter.getLocText("Right") + ":"));
            marginRightUnitFieldPane = new UnitFieldPane(Constants.UNIT_MM);
            marginRightUnitPane.add(marginRightUnitFieldPane);

            marginTopUnitFieldPane.getTextField().addFocusListener(fa);
            marginBottomUnitFieldPane.getTextField().addFocusListener(fa);
            marginLeftUnitFieldPane.getTextField().addFocusListener(fa);
            marginRightUnitFieldPane.getTextField().addFocusListener(fa);

            marginPane.add(zeroMarginWarn);

            // header and footer
            JPanel outhfHeightPane = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("Height"));
            JPanel hfHeightPane = FRGUIPaneFactory.createNormalFlowInnerContainer_M_Pane();
            defaultPane.add(outhfHeightPane);
            outhfHeightPane.add(hfHeightPane);

            // header height.
            JPanel headerHeightPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
            hfHeightPane.add(headerHeightPane);
            headerHeightPane.add(new UILabel(Inter.getLocText("PageSetup-Header") + ":"));

            headerUnitFieldPane = new UnitFieldPane(Constants.UNIT_MM);
            headerHeightPane.add(headerUnitFieldPane);

            // footer height.
            JPanel footerHeightPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
            hfHeightPane.add(footerHeightPane);
            footerHeightPane.add(new UILabel(Inter.getLocText("PageSetup-Footer") + ":"));

            footerUnitFieldPane = new UnitFieldPane(Constants.UNIT_MM);
            footerHeightPane.add(footerUnitFieldPane);

            // print gridlines.
            JPanel printOptionPane = FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(1);
            defaultPane.add(printOptionPane);
            printOptionPane.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
        }

        @Override
        protected String title4PopupWindow() {
            return "page";
        }

        private ActionListener previewListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                previewShowPagePane();
            }
        };

        private ChangeListener previewListener2 = new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                previewShowPagePane();
            }
        };

        //js:ҳ�����ã�����л���λ��Ӣ�� -- ����
        private ActionListener switchInchListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == switchInch) {
                    int index = switchInch.getSelectedIndex();
                    switch (index) {
                        case 0:
                            if (unitType != Constants.UNIT_MM) {
                                unitType = Constants.UNIT_MM;
                                DesignerEnvManager.getEnvManager().setPageLengthUnit(Constants.UNIT_MM);
                                paperWidthSpinner.setValue(Math.round(new INCH(((Number) paperWidthSpinner.getValue()).floatValue()).toMMValue4Scale2()));
                                paperHeightSpinner.setValue(Math.round(new INCH(((Number) paperHeightSpinner.getValue()).floatValue()).toMMValue4Scale2()));
                                populate(report, Constants.UNIT_MM);
                            }
                            customRadioButton.setSelected(true);
                            break;
                        case 1:
                            unitType = Constants.UNIT_INCH;
                            DesignerEnvManager.getEnvManager().setPageLengthUnit(Constants.UNIT_INCH);
                            paperWidthSpinner.setValue(new MM(((Number) paperWidthSpinner.getValue()).floatValue()).toINCHValue4Scale3());
                            paperHeightSpinner.setValue(new MM(((Number) paperHeightSpinner.getValue()).floatValue()).toINCHValue4Scale3());
                            populate(report, Constants.UNIT_INCH);
                            customRadioButton.setSelected(true);
                            break;
                    }
                }
            }
        };

        /**
         * Populate
         *
         * @param reportSettings rpt settings.
         */
        public void populate(Report report, int unitType) {
            this.report = report;
            ReportSettingsProvider reportSettings = report.getReportSettings();
            PaperSettingProvider psetting = reportSettings.getPaperSetting();
            // orientation
            if (psetting.getOrientation() == ReportConstants.LANDSCAPE) {
                this.landscapeRadioButton.setSelected(true);
            } else {
                this.portraitRadioButton.setSelected(true);
            }
            //js:setSelectedIndex�ᴥ��switchInchListener�������ɾ��
            switchInch.removeActionListener(switchInchListener);
            if (unitType == Constants.UNIT_INCH) {
                switchInch.setSelectedIndex(1);
            } else {
                switchInch.setSelectedIndex(Constants.UNIT_MM);
            }
            switchInch.addActionListener(switchInchListener);
            unitSet(unitType);
            // paper size.
            PaperSize paperSize = psetting.getPaperSize();
            if (paperSize == null) {
                paperSize = PaperSize.PAPERSIZE_A4;
            }
            boolean isCustomed = true;
            for (int i = 0; i < ReportConstants.PaperSizeNameSizeArray.length; i++) {
                Object[] tmpPaperSizeNameArray = ReportConstants.PaperSizeNameSizeArray[i];
                
                // peter:��ǰѡ�����Ԥ�ȶ���õ�.
                if (ComparatorUtils.equals(paperSize, tmpPaperSizeNameArray[1])) {
                    this.predefinedComboBox.setSelectedIndex(i);
                    this.predefinedRadioButton.setSelected(true);
                    isCustomed = false;
                    break;
                }
            }
            for(int i=0; i<MOBILE_NAME_SIZE_ARRAY.length; i++){
            	Object[] mobileNameSizeArray = MOBILE_NAME_SIZE_ARRAY[i];
            	if(ComparatorUtils.equals(paperSize, mobileNameSizeArray[1])){
            		this.mobileComboBox.setSelectedIndex(i);
            		this.mobileRadioButton.setSelected(true);
            		isCustomed = false;
            		break;
            	}
            }
            setAndPopulate(isCustomed, unitType);
            populateMargin();
        }
        
        private void unitSet(int unitType) {
            this.unitType = unitType;
            unitLabel.setUnitType(unitType);
            marginTopUnitFieldPane.setUnitType(unitType);
            marginLeftUnitFieldPane.setUnitType(unitType);
            marginBottomUnitFieldPane.setUnitType(unitType);
            marginRightUnitFieldPane.setUnitType(unitType);

            headerUnitFieldPane.setUnitType(unitType);
            footerUnitFieldPane.setUnitType(unitType);
        }


        private void setAndPopulate(boolean isCustomed, int unitType) {
        	ReportSettingsProvider reportSettings = report.getReportSettings();
            PaperSettingProvider psetting = reportSettings.getPaperSetting();
            PaperSize paperSize = psetting.getPaperSize();
            if (isCustomed) {
                this.customRadioButton.setSelected(true);

                if (unitType == Constants.UNIT_CM) {
                    paperWidthSpinner.setValue(new Float(paperSize.getWidth().toCMValue4Scale2()));
                    paperHeightSpinner.setValue(new Float(paperSize.getHeight().toCMValue4Scale2()));
                } else if (unitType == Constants.UNIT_INCH) {
                    paperWidthSpinner.setValue(new Float(paperSize.getWidth().toINCHValue4Scale3()));
                    paperHeightSpinner.setValue(new Float(paperSize.getHeight().toINCHValue4Scale3()));
                } else {
                    paperWidthSpinner.setValue(new Float(paperSize.getWidth().toMMValue4Scale2()));
                    paperHeightSpinner.setValue(new Float(paperSize.getHeight().toMMValue4Scale2()));
                }
            }
            // Ԥ���ĳ�ʼ��
            if (unitType == Constants.UNIT_CM) {
                showPagePane.populate(paperSize.getWidth().toCMValue4Scale2(), paperSize.getHeight().toCMValue4Scale2(),
                        psetting.getOrientation(), false);
            } else if (unitType == Constants.UNIT_INCH) {
                showPagePane.populate(paperSize.getWidth().toINCHValue4Scale3(), paperSize.getHeight().toINCHValue4Scale3(),
                        psetting.getOrientation(), false);
            } else {
                showPagePane.populate(paperSize.getWidth().toMMValue4Scale2(), paperSize.getHeight().toMMValue4Scale2(),
                        psetting.getOrientation(), true);
            }
        }


        private void populateMargin() {
        	ReportSettingsProvider reportSettings = report.getReportSettings();
            PaperSettingProvider psetting = reportSettings.getPaperSetting();
            // margins
            Margin margin = psetting.getMargin();
            this.marginTopUnitFieldPane.setUnitValue(margin.getTop());
            this.marginLeftUnitFieldPane.setUnitValue(margin.getLeft());
            this.marginBottomUnitFieldPane.setUnitValue(margin.getBottom());
            this.marginRightUnitFieldPane.setUnitValue(margin.getRight());

            // populate ui
            headerUnitFieldPane.setUnitValue(reportSettings.getHeaderHeight());
            footerUnitFieldPane.setUnitValue(reportSettings.getFooterHeight());
        }
        
        private void updatePaperSizeByType(PaperSettingProvider psetting){
            // paper size.
            if (this.predefinedRadioButton.isSelected()) {
                // peter:����ط���Ҫcloneһ�£���ֹ�û��������޸����PaperSize.
                try {
                    psetting.setPaperSize((PaperSize) ((PaperSize) predefinedComboBox.getSelectedItem()).clone());
                } catch (CloneNotSupportedException cloneNotSupportedException) {
                    // do nothing
                }
            }else if(this.mobileRadioButton.isSelected()){
                try {
                    psetting.setPaperSize((PaperSize) ((PaperSize) mobileComboBox.getSelectedItem()).clone());
                } catch (CloneNotSupportedException cloneNotSupportedException) {
                    // do nothing
                }
            	
            } else if (this.customRadioButton.isSelected()) {
                if (unitType == Constants.UNIT_CM) {
                    psetting.setPaperSize(new PaperSize(new CM(((Number) this.paperWidthSpinner.getValue()).floatValue()), new CM(
                            ((Number) this.paperHeightSpinner.getValue()).floatValue())));
                } else if (unitType == Constants.UNIT_INCH) {
                    psetting.setPaperSize(new PaperSize(new INCH(((Number) this.paperWidthSpinner.getValue()).floatValue()), new INCH(
                            ((Number) this.paperHeightSpinner.getValue()).floatValue())));
                } else {
                    psetting.setPaperSize(new PaperSize(new MM(((Number) this.paperWidthSpinner.getValue()).floatValue()), new MM(
                            ((Number) this.paperHeightSpinner.getValue()).floatValue())));
                }
            }
        }

        /**
         * update
         */
        public void update(Report report) {
        	ReportSettingsProvider reportSettings = report.getReportSettings();
            // samuel:���ж��Ƿ�reportSettings�Ƿ�Ϊ�գ����Ϊ�գ����½�
            if (reportSettings == null) {
                reportSettings = new ReportSettings();
                report.setReportSettings(reportSettings);
            }
            reportSettings = report.getReportSettings();
            PaperSettingProvider psetting = reportSettings.getPaperSetting();

            // orientation
            if (this.landscapeRadioButton.isSelected()) {
                psetting.setOrientation(ReportConstants.LANDSCAPE);
            } else {
                psetting.setOrientation(ReportConstants.PORTRAIT);
            }

            //paper size
            updatePaperSizeByType(psetting);
            
            // margins
            Margin margin = psetting.getMargin();
            margin.setTop(this.marginTopUnitFieldPane.getUnitValue());
            margin.setLeft(this.marginLeftUnitFieldPane.getUnitValue());
            margin.setBottom(this.marginBottomUnitFieldPane.getUnitValue());
            margin.setRight(this.marginRightUnitFieldPane.getUnitValue());

            // update ui.
            reportSettings.setHeaderHeight(this.headerUnitFieldPane.getUnitValue());
            reportSettings.setFooterHeight(this.footerUnitFieldPane.getUnitValue());
        }

        private void showPagePaneByType(PaperSize ps,int ori){
            if (unitType == Constants.UNIT_CM) {
                showPagePane.populate(ps.getWidth().toCMValue4Scale2(), ps.getHeight().toCMValue4Scale2(), ori, false);
            } else if (unitType == Constants.UNIT_INCH) {
                showPagePane.populate(ps.getWidth().toINCHValue4Scale3(), ps.getHeight().toINCHValue4Scale3(), ori, false);
            } else {
                showPagePane.populate(ps.getWidth().toMMValue4Scale2(), ps.getHeight().toMMValue4Scale2(), ori, true);
            }
        }

        private void previewShowPagePane() {
            int ori;
            if (this.landscapeRadioButton.isSelected()) {
                ori = ReportConstants.LANDSCAPE;
            } else {
                ori = ReportConstants.PORTRAIT;
            }
            PaperSize ps;
            if (this.predefinedRadioButton.isSelected()) {
                ps = (PaperSize) predefinedComboBox.getSelectedItem();
                showPagePaneByType(ps,ori);
            } else if(this.mobileRadioButton.isSelected()){
                ps = (PaperSize) mobileComboBox.getSelectedItem();
                showPagePaneByType(ps, ori);
            }else if (this.customRadioButton.isSelected()) {
                showPagePane.populate(((Number) this.paperWidthSpinner.getValue()).doubleValue(),
                        ((Number) this.paperHeightSpinner.getValue()).doubleValue(), ori, unitType == Constants.UNIT_MM);
            }

            showPagePane.repaint();
        }

        /**
         * Create icon radio pane for Portrait and Landscape.
         */
        private JPanel createIconRadioPane(Icon icon, UIRadioButton radioButton) {
            JPanel iconRadioPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
//			iconRadioPane.setLayout(FRGUIPaneFactory.createCenterFlowLayout());

            iconRadioPane.add(new UILabel(icon));
            iconRadioPane.add(radioButton);

            return iconRadioPane;
        }

        /**
         * Paper size cell renderer.
         */
        private UIComboBoxRenderer paperSizeCellRenderere = new UIComboBoxRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value instanceof PaperSize) {
                    PaperSize paperSize = (PaperSize) value;
                    for (int i = 0; i < ReportConstants.PaperSizeNameSizeArray.length; i++) {
                        Object[] tmpPaperSizeNameArray = ReportConstants.PaperSizeNameSizeArray[i];

                        if (ComparatorUtils.equals(paperSize, tmpPaperSizeNameArray[1])) {
                            StringBuffer sbuf = new StringBuffer(tmpPaperSizeNameArray[0].toString());

                            adjustCellRenderByType(sbuf, paperSize);

                            this.setText(sbuf.toString());
                            break;
                        }
                    }
                }

                return this;
            }
        };
        
        private void adjustCellRenderByType(StringBuffer sbuf,PaperSize paperSize){
        	sbuf.append(" [");
            if (unitType == Constants.UNIT_CM) {
                sbuf.append(Utils.convertNumberStringToString(new Float(paperSize.getWidth().toCMValue4Scale2())));
                sbuf.append('x');
                sbuf.append(Utils.convertNumberStringToString(new Float(paperSize.getHeight().toCMValue4Scale2())));
                sbuf.append(' ');
                sbuf.append(Inter.getLocText("Unit_CM"));
            } else if (unitType == Constants.UNIT_INCH) {
                sbuf.append(Utils.convertNumberStringToString(new Float(paperSize.getWidth().toINCHValue4Scale3())));
                sbuf.append('x');
                sbuf.append(Utils.convertNumberStringToString(new Float(paperSize.getHeight().toINCHValue4Scale3())));
                sbuf.append(' ');
                sbuf.append(Inter.getLocText("PageSetup-inches"));
            } else {
                sbuf.append(Utils.convertNumberStringToString(new Float(paperSize.getWidth().toMMValue4Scale2())));
                sbuf.append('x');
                sbuf.append(Utils.convertNumberStringToString(new Float(paperSize.getHeight().toMMValue4Scale2())));
                sbuf.append(' ');
                sbuf.append(Inter.getLocText("PageSetup-mm"));
            }
            sbuf.append(']');
        }
        
        
        /**
         * Paper size cell renderer.
         */
        private UIComboBoxRenderer paperSizeMobileCellRenderere = new UIComboBoxRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value instanceof PaperSize) {
                    PaperSize paperSize = (PaperSize) value;
                    for (int i = 0; i < MOBILE_NAME_SIZE_ARRAY.length; i++) {
                        Object[] tmpPaperSizeNameArray = MOBILE_NAME_SIZE_ARRAY[i];

                        if (ComparatorUtils.equals(paperSize, tmpPaperSizeNameArray[1])) {
                            StringBuffer sbuf = new StringBuffer(tmpPaperSizeNameArray[0].toString());
                            adjustCellRenderByType(sbuf,paperSize);
                            this.setText(sbuf.toString());
                            break;
                        }
                    }
                }

                return this;
            }
        };
        

        /**
         * Paper size item listener.
         */
        private ItemListener paperSizeItemListener = new ItemListener() {
            public void itemStateChanged(ItemEvent evt) {
                PaperSize paperSize = (PaperSize) predefinedComboBox.getSelectedItem();
                adjustSpinnerValueByType(paperSize);
                predefinedRadioButton.setSelected(true);
                previewShowPagePane();
            }
        };
        
        private void adjustSpinnerValueByType(PaperSize paperSize){
            if (unitType == Constants.UNIT_CM) {
                paperWidthSpinner.setValue(new Float(paperSize.getWidth().toCMValue4Scale2()));
                paperHeightSpinner.setValue(new Float(paperSize.getHeight().toCMValue4Scale2()));
            } else if (unitType == Constants.UNIT_INCH) {
                paperWidthSpinner.setValue(new Float(paperSize.getWidth().toINCHValue4Scale3()));
                paperHeightSpinner.setValue(new Float(paperSize.getHeight().toINCHValue4Scale3()));
            } else {
                paperWidthSpinner.setValue(new Float(paperSize.getWidth().toMMValue4Scale2()));
                paperHeightSpinner.setValue(new Float(paperSize.getHeight().toMMValue4Scale2()));
            }
        }
        
        /**
         * Paper size item listener.
         */
        private ItemListener paperSizeItemMobileListener = new ItemListener() {
            public void itemStateChanged(ItemEvent evt) {
                PaperSize paperSize = (PaperSize) mobileComboBox.getSelectedItem();
                adjustSpinnerValueByType(paperSize);
                mobileRadioButton.setSelected(true);
                previewShowPagePane();
            }
        };

        // text listener.
        DocumentListener customTextListener = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                fireTextEvent();
            }

            public void removeUpdate(DocumentEvent e) {
                fireTextEvent();
            }

            public void changedUpdate(DocumentEvent e) {
                fireTextEvent();
            }

            private void fireTextEvent() {
                customRadioButton.setSelected(((Number) paperWidthSpinner.getValue()).doubleValue() > 0
                        && ((Number) paperHeightSpinner.getValue()).doubleValue() > 0);
            }
        };

        class ShowPagePane extends JPanel {
            private final static int NUM_3 = 3;
            private final static double NUM_POINT_3 =0.3;
            private final static int NUM_NEG_5 = -5;
            private final static int NUM_NEG_15 = -15;
            private final static int NUM_NEG_10 = -10;
            private final static int NUM_NEG_13 = -13;
            private final static int NUM_NEG_7 = -7;
            private final static int NUM_NEG_8 = -8;
            private double paper_width;
            private double paper_height;
            private int paper_orientation;

            private int pane_width = 300;
            private int pane_height = 100;

            private double length_scale = 0.3;
            private Image img;

            public ShowPagePane() {
                this.setSize(pane_width, pane_height);
                this.setPreferredSize(new Dimension(pane_width, pane_height));
                this.setBackground(new Color(128, 128, 128));// background
                // color.
                this.setOpaque(false);
                img = BaseUtils.readImage("/com/fr/base/images/dialog/pagesetup/a.png");
            }

            public void populate(double paper_width, double paper_height, int paper_orientation, boolean useLocale) {
                if (paper_width <= 0 || paper_height <= 0)  {
                    return;
                }
                // ���2000�����⻭��ʱ�򳬱�
                this.paper_width = Math.min(paper_width, 2000);
                this.paper_height = Math.min(paper_height, 2000);
                this.paper_orientation = paper_orientation;
                length_scale = !useLocale ? NUM_3 : NUM_POINT_3;
            }

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);

                double paint_width;
                double paint_height;
                if (paper_width > 0 && paper_height > 0) {
                    double width_scale = length_scale * paper_width / (pane_width - 50);
                    double height_scale = length_scale * paper_height / (pane_height - 30);

                    if (width_scale > 1 || height_scale > 1) {
                        double max_scale = width_scale > height_scale ? width_scale : height_scale;
                        paint_width = length_scale * paper_width / max_scale;
                        paint_height = length_scale * paper_height / max_scale;
                    } else {
                        paint_width = paper_width * length_scale;
                        paint_height = paper_height * length_scale;
                    }
                    Graphics2D g2d = (Graphics2D) g;
                    FontMetrics fm = g2d.getFontMetrics();
                    // ����ĳ���
                    String w_str = "" + paper_width;
                    if (w_str.indexOf(CoreConstants.DOT) > 0) {
                        w_str = w_str.substring(0, w_str.indexOf(CoreConstants.DOT) + 2);
                    }
                    int w_length = fm.stringWidth(w_str);
                    paint_width = Math.max(paint_width, w_length + 26);
                    // ����ĳ���
                    String h_str = "" + paper_height;
                    if (h_str.indexOf(".") > 0) {
                        h_str = h_str.substring(0, h_str.indexOf(".") + 2);
                    }
                    int h_length = fm.stringWidth(h_str);
                    paint_height = Math.max(paint_height, h_length + 26);
                    double startX = (pane_width - paint_width) / 2;
                    double startY = (pane_height - paint_height) / 2;
                    g2d.translate(startX, startY);
                    g2d = getG2d(paint_width, paint_height,g2d,w_str,h_str,w_length,h_length);
                    if (paper_orientation == ReportConstants.PORTRAIT) {
                        g2d.drawImage(img, (int) ((paint_width - img.getWidth(null)) / 2),
                                (int) ((paint_height - img.getHeight(null)) / 2), null);
                    } else {
                        g2d.rotate(-Math.toRadians(90));
                        g2d.drawImage(img, -(int) ((paint_height + img.getWidth(null)) / 2),
                                (int) ((paint_width - img.getHeight(null)) / 2), null);
                        g2d.rotate(Math.toRadians(90));
                    }
                    g2d.translate(-startX, -startY);
                }
            }

            private Graphics2D getG2d(double paint_width, double paint_height,Graphics2D g2d,String w_str,String h_str,int w_length,int h_length) {
                g2d.setColor(Color.WHITE);
                g2d.fill3DRect(0, 0, (int) paint_width, (int) paint_height, true);
                g2d.setColor(Color.BLACK);
                // �������������
                g2d.drawLine(0, NUM_NEG_5, 0, NUM_NEG_15);
                g2d.drawLine((int) paint_width - 1, NUM_NEG_5, (int) paint_width - 1, NUM_NEG_15);

                g2d.drawString(w_str, (int) ((paint_width - w_length) / 2), NUM_NEG_5);
                // �������������ͷ
                g2d.drawLine(2, NUM_NEG_10, (int) ((paint_width - w_length) / 2) + NUM_NEG_5, NUM_NEG_10);
                g2d.drawLine((int) ((paint_width + w_length) / 2) -NUM_NEG_5, NUM_NEG_10, (int) paint_width -NUM_3, NUM_NEG_10);
                g2d.drawLine(2, NUM_NEG_10, -NUM_NEG_7, NUM_NEG_13);
                g2d.drawLine(2, NUM_NEG_10, -NUM_NEG_7, NUM_NEG_7);
                g2d.drawLine((int) paint_width - NUM_3,NUM_NEG_10, (int) paint_width +NUM_NEG_8, NUM_NEG_13);
                g2d.drawLine((int) paint_width - NUM_3, NUM_NEG_10, (int) paint_width+NUM_NEG_8, NUM_NEG_7);
                // �������������
                g2d.drawLine(NUM_NEG_15, 0, NUM_NEG_5, 0);
                g2d.drawLine(NUM_NEG_15, (int) paint_height - 1, NUM_NEG_5, (int) paint_height - 1);

                g2d.rotate(-Math.toRadians((NUM_NEG_10*NUM_NEG_8-NUM_NEG_10)));
                g2d.drawString(h_str, -(int) ((paint_height + h_length) / 2), NUM_NEG_5);
                g2d.rotate(Math.toRadians((NUM_NEG_10*NUM_NEG_8-NUM_NEG_10)));
                // ����ļ�ͷ
                g2d.drawLine(NUM_NEG_10, 2, NUM_NEG_10, (int) ((paint_height - h_length) / 2) +NUM_NEG_5);
                g2d.drawLine(NUM_NEG_10, (int) ((paint_height + h_length) / 2) -NUM_NEG_5, NUM_NEG_10, (int) paint_height - NUM_3);
                g2d.drawLine(NUM_NEG_10, 2, NUM_NEG_13, -NUM_NEG_7);
                g2d.drawLine(NUM_NEG_10, 2, NUM_NEG_7, -NUM_NEG_7);
                g2d.drawLine(NUM_NEG_10, (int) paint_height - NUM_3, NUM_NEG_13, (int) paint_height +NUM_NEG_8);
                g2d.drawLine(NUM_NEG_10, (int) paint_height - NUM_3, NUM_NEG_7, (int) paint_height+NUM_NEG_8);
                return g2d;
            }

        }

        private FocusAdapter fa = new FocusAdapter() {
        	private final static int NUM_11 = 11;
        	
            @Override
            public void focusLost(FocusEvent e) {
                String zeroValue = "0";
                boolean topValue =ComparatorUtils.equals(zeroValue,marginTopUnitFieldPane.getTextField().getText());
                boolean bottomValue =ComparatorUtils.equals(zeroValue,marginBottomUnitFieldPane.getTextField().getText());
                boolean leftValue = ComparatorUtils.equals(zeroValue,marginLeftUnitFieldPane.getTextField().getText());
                boolean rightValue = ComparatorUtils.equals(zeroValue,marginRightUnitFieldPane.getTextField().getText());
                boolean isOr = topValue || bottomValue || leftValue;
                boolean or = isOr || rightValue;
                boolean isAnd = !topValue && !bottomValue && !leftValue ;
                if (or && (zeroMarginWarn.getText()).endsWith("<br></html></body>")) {
                    zeroMarginWarn.setText("<html><body>"+Inter.getLocText("zeroMarginWarn")+"</body></html>");
                } else if (isAnd && !rightValue) {
                    StringBuffer temp = new StringBuffer();
                    for (int i = 0; i < NUM_11; i++) {
                        temp.append("&nbsp");
                    }
                    zeroMarginWarn.setText("<html><body>" + temp + "<br><br></html></body>");
                }
            }
        };

    }

    private class OtherPane extends BasicPane {

        private UIRadioButton topBottomRadioButton;
        private UIRadioButton leftRightRadioButton;

        private UICheckBox horizonalCenterCheckBox;
        private UICheckBox verticalCenterCheckBox;

        private UISpinner firstPageNumberSpinner;

        private UIRadioButton isShrinkToFit4None;
        private UIRadioButton isShrinkToFit4Height;
        private UIRadioButton isShrinkToFit4Width;

        public OtherPane() {
            this.setLayout(FRGUIPaneFactory.createBorderLayout());

            JPanel defaultPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_L_Pane();
            this.add(defaultPane, BorderLayout.NORTH);

            // page order
            JPanel outpageOrderPane = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("PageSetup-Page_Order"));
            JPanel pageOrderPane = FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(2);
            outpageOrderPane.add(pageOrderPane);
            defaultPane.add(outpageOrderPane);


            Icon topBottomIcon = BaseUtils.readIcon("/com/fr/base/images/dialog/pagesetup/down.png");
            topBottomRadioButton = new UIRadioButton(Inter.getLocText("PageSetup-Top_to_bottom"));
            pageOrderPane.add(FRGUIPaneFactory.createIconRadio_S_Pane(topBottomIcon, topBottomRadioButton));
            topBottomRadioButton.setMnemonic('B');

            Icon leftRightIcon = BaseUtils.readIcon("/com/fr/base/images/dialog/pagesetup/over.png");
            leftRightRadioButton = new UIRadioButton(Inter.getLocText("PageSetup-Left_to_right"));
            pageOrderPane.add(FRGUIPaneFactory.createIconRadio_S_Pane(leftRightIcon, leftRightRadioButton));
            leftRightRadioButton.setMnemonic('R');

            ButtonGroup pageOrderButtonGroup = new ButtonGroup();
            pageOrderButtonGroup.add(topBottomRadioButton);
            pageOrderButtonGroup.add(leftRightRadioButton);

            topBottomRadioButton.setSelected(true);

            // center on page
            JPanel outcenterOnPagePane = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("PageSetup-Placement_Center_on_Page"));
            JPanel centerOnPagePane = FRGUIPaneFactory.createNormalFlowInnerContainer_M_Pane();
            outcenterOnPagePane.add(centerOnPagePane);
            defaultPane.add(outcenterOnPagePane);


            this.horizonalCenterCheckBox = new UICheckBox(Inter.getLocText("PageSetup-Horizontally"));
            this.horizonalCenterCheckBox.setMnemonic('H');
            this.verticalCenterCheckBox = new UICheckBox(Inter.getLocText("PageSetup-Vertically"));
            this.verticalCenterCheckBox.setMnemonic('V');

            centerOnPagePane.add(GUICoreUtils.createFlowPane(horizonalCenterCheckBox, FlowLayout.CENTER));
            centerOnPagePane.add(GUICoreUtils.createFlowPane(verticalCenterCheckBox, FlowLayout.CENTER));

            // first page number.
            JPanel firstPaneNumberPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
            defaultPane.add(firstPaneNumberPane);

            firstPaneNumberPane.add(new UILabel(Inter.getLocText("PageSetup-First_Page_Number") + ": "));
            // marks: ����ط�����Ϊ�����������
            firstPageNumberSpinner = new UISpinner(1, Integer.MAX_VALUE, 1, 1);
            firstPaneNumberPane.add(firstPageNumberSpinner);

            defaultPane.add(Box.createVerticalStrut(4));

            // print gridlines.
            JPanel printOptionPane = FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(1);
            defaultPane.add(printOptionPane);
            printOptionPane.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));

            JPanel autoShrinkPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
            autoShrinkPane.add(new UILabel(Inter.getLocText("PageSetup-Shrink_to_fit_content")));
            isShrinkToFit4None = new UIRadioButton(Inter.getLocText("No"));
            isShrinkToFit4Height = new UIRadioButton(Inter.getLocText("Utils-Row_Height"));
            isShrinkToFit4Width = new UIRadioButton(Inter.getLocText("Utils-Column_Width"));

            ButtonGroup bp = new ButtonGroup();
            autoShrinkPane.add(isShrinkToFit4None);
            autoShrinkPane.add(isShrinkToFit4Height);
            autoShrinkPane.add(isShrinkToFit4Width);
            bp.add(isShrinkToFit4None);
            bp.add(isShrinkToFit4Height);
            bp.add(isShrinkToFit4Width);

            printOptionPane.add(autoShrinkPane);
        }

        @Override
        protected String title4PopupWindow() {
            return "other";
        }

        /**
         * Create icon radio pane for Portrait and Landscape.
         */
        private JPanel createIconRadioPane(Icon icon, UIRadioButton radioButton) {
            JPanel iconRadioPane = FRGUIPaneFactory.createCenterFlowInnerContainer_S_Pane();

            iconRadioPane.add(new UILabel(icon));
            iconRadioPane.add(radioButton);

            return iconRadioPane;
        }

        /**
         * Populate
         */
        public void populate(Report report) {
        	ReportSettingsProvider reportSettings = null;
            reportSettings = ReportUtils.getReportSettings(report);

            if (reportSettings == null) {
                reportSettings = new ReportSettings();
            }

            // page order.
            if (reportSettings.getPageOrder() == Constants.LEFT_TO_RIGHT) {
                this.leftRightRadioButton.setSelected(true);
            } else {
                this.topBottomRadioButton.setSelected(true);
            }

            // placement
            this.horizonalCenterCheckBox.setSelected(reportSettings.isHorizontalCenterOnPage());
            this.verticalCenterCheckBox.setSelected(reportSettings.isVerticalCenterOnPage());

            this.firstPageNumberSpinner.setValue(new Integer(reportSettings.getFirstPageNumber()));


            // Options
            if (reportSettings.getShrinkToFitMode() == ReportConstants.AUTO_SHRINK_TO_FIT_WIDTH) {
                this.isShrinkToFit4Width.setSelected(true);
            } else if (reportSettings.getShrinkToFitMode() == ReportConstants.AUTO_SHRINK_TO_FIT_HEIGHT) {
                this.isShrinkToFit4Height.setSelected(true);
            } else {
                this.isShrinkToFit4None.setSelected(true);
            }
        }

        /**
         * void update
         */
        public void update(Report report) {
        	ReportSettingsProvider reportSettings = null;
            if (report.getReportSettings() == null) {
                report.setReportSettings(new ReportSettings());
            }
            reportSettings = report.getReportSettings();

            // page order.
            if (this.leftRightRadioButton.isSelected()) {
                reportSettings.setPageOrder(Constants.LEFT_TO_RIGHT);
            } else {
                reportSettings.setPageOrder(Constants.TOP_TO_BOTTOM);
            }

            // placement
            reportSettings.setHorizontalCenterOnPage(this.horizonalCenterCheckBox.isSelected());
            reportSettings.setVerticalCenterOnPage(this.verticalCenterCheckBox.isSelected());

            reportSettings.setFirstPageNumber((int) this.firstPageNumberSpinner.getValue());

            if (this.isShrinkToFit4Width.isSelected()) {
                reportSettings.setShrinkToFitMode(ReportConstants.AUTO_SHRINK_TO_FIT_WIDTH);
            } else if (this.isShrinkToFit4Height.isSelected()) {
                reportSettings.setShrinkToFitMode(ReportConstants.AUTO_SHRINK_TO_FIT_HEIGHT);
            } else {
                reportSettings.setShrinkToFitMode(ReportConstants.AUTO_SHRINK_TO_FIT_NONE);
            }
        }

    }
}
