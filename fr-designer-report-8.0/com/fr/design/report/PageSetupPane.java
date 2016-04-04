// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.report;

import com.fr.base.*;
import com.fr.design.DesignerEnvManager;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.frpane.UITabbedPane;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.icombobox.UIComboBoxRenderer;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.page.PaperSettingProvider;
import com.fr.page.ReportSettingsProvider;
import com.fr.report.core.ReportUtils;
import com.fr.report.report.Report;
import com.fr.report.stable.ReportConstants;
import com.fr.report.stable.ReportSettings;
import com.fr.stable.unit.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.Document;

// Referenced classes of package com.fr.design.report:
//            UnitFieldPane

public class PageSetupPane extends BasicPane
{
    private class OtherPane extends BasicPane
    {

        private UIRadioButton topBottomRadioButton;
        private UIRadioButton leftRightRadioButton;
        private UICheckBox horizonalCenterCheckBox;
        private UICheckBox verticalCenterCheckBox;
        private UISpinner firstPageNumberSpinner;
        private UIRadioButton isShrinkToFit4None;
        private UIRadioButton isShrinkToFit4Height;
        private UIRadioButton isShrinkToFit4Width;
        final PageSetupPane this$0;

        protected String title4PopupWindow()
        {
            return "other";
        }

        private JPanel createIconRadioPane(Icon icon, UIRadioButton uiradiobutton)
        {
            JPanel jpanel = FRGUIPaneFactory.createCenterFlowInnerContainer_S_Pane();
            jpanel.add(new UILabel(icon));
            jpanel.add(uiradiobutton);
            return jpanel;
        }

        public void populate(Report report)
        {
            Object obj = null;
            obj = ReportUtils.getReportSettings(report);
            if(obj == null)
                obj = new ReportSettings();
            if(((ReportSettingsProvider) (obj)).getPageOrder() == 1)
                leftRightRadioButton.setSelected(true);
            else
                topBottomRadioButton.setSelected(true);
            horizonalCenterCheckBox.setSelected(((ReportSettingsProvider) (obj)).isHorizontalCenterOnPage());
            verticalCenterCheckBox.setSelected(((ReportSettingsProvider) (obj)).isVerticalCenterOnPage());
            firstPageNumberSpinner.setValue((new Integer(((ReportSettingsProvider) (obj)).getFirstPageNumber())).intValue());
            if(((ReportSettingsProvider) (obj)).getShrinkToFitMode() == 2)
                isShrinkToFit4Width.setSelected(true);
            else
            if(((ReportSettingsProvider) (obj)).getShrinkToFitMode() == 1)
                isShrinkToFit4Height.setSelected(true);
            else
                isShrinkToFit4None.setSelected(true);
        }

        public void update(Report report)
        {
            ReportSettingsProvider reportsettingsprovider = null;
            if(report.getReportSettings() == null)
                report.setReportSettings(new ReportSettings());
            reportsettingsprovider = report.getReportSettings();
            if(leftRightRadioButton.isSelected())
                reportsettingsprovider.setPageOrder(1);
            else
                reportsettingsprovider.setPageOrder(0);
            reportsettingsprovider.setHorizontalCenterOnPage(horizonalCenterCheckBox.isSelected());
            reportsettingsprovider.setVerticalCenterOnPage(verticalCenterCheckBox.isSelected());
            reportsettingsprovider.setFirstPageNumber((int)firstPageNumberSpinner.getValue());
            if(isShrinkToFit4Width.isSelected())
                reportsettingsprovider.setShrinkToFitMode(2);
            else
            if(isShrinkToFit4Height.isSelected())
                reportsettingsprovider.setShrinkToFitMode(1);
            else
                reportsettingsprovider.setShrinkToFitMode(0);
        }

        public OtherPane()
        {
            this$0 = PageSetupPane.this;
            super();
            setLayout(FRGUIPaneFactory.createBorderLayout());
            JPanel jpanel = FRGUIPaneFactory.createY_AXISBoxInnerContainer_L_Pane();
            add(jpanel, "North");
            JPanel jpanel1 = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("PageSetup-Page_Order"));
            JPanel jpanel2 = FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(2);
            jpanel1.add(jpanel2);
            jpanel.add(jpanel1);
            Icon icon = BaseUtils.readIcon("/com/fr/base/images/dialog/pagesetup/down.png");
            topBottomRadioButton = new UIRadioButton(Inter.getLocText("PageSetup-Top_to_bottom"));
            jpanel2.add(FRGUIPaneFactory.createIconRadio_S_Pane(icon, topBottomRadioButton));
            topBottomRadioButton.setMnemonic('B');
            Icon icon1 = BaseUtils.readIcon("/com/fr/base/images/dialog/pagesetup/over.png");
            leftRightRadioButton = new UIRadioButton(Inter.getLocText("PageSetup-Left_to_right"));
            jpanel2.add(FRGUIPaneFactory.createIconRadio_S_Pane(icon1, leftRightRadioButton));
            leftRightRadioButton.setMnemonic('R');
            ButtonGroup buttongroup = new ButtonGroup();
            buttongroup.add(topBottomRadioButton);
            buttongroup.add(leftRightRadioButton);
            topBottomRadioButton.setSelected(true);
            JPanel jpanel3 = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("PageSetup-Placement_Center_on_Page"));
            JPanel jpanel4 = FRGUIPaneFactory.createNormalFlowInnerContainer_M_Pane();
            jpanel3.add(jpanel4);
            jpanel.add(jpanel3);
            horizonalCenterCheckBox = new UICheckBox(Inter.getLocText("PageSetup-Horizontally"));
            horizonalCenterCheckBox.setMnemonic('H');
            verticalCenterCheckBox = new UICheckBox(Inter.getLocText("PageSetup-Vertically"));
            verticalCenterCheckBox.setMnemonic('V');
            jpanel4.add(GUICoreUtils.createFlowPane(horizonalCenterCheckBox, 1));
            jpanel4.add(GUICoreUtils.createFlowPane(verticalCenterCheckBox, 1));
            JPanel jpanel5 = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
            jpanel.add(jpanel5);
            jpanel5.add(new UILabel((new StringBuilder()).append(Inter.getLocText("PageSetup-First_Page_Number")).append(": ").toString()));
            firstPageNumberSpinner = new UISpinner(1.0D, 2147483647D, 1.0D, 1.0D);
            jpanel5.add(firstPageNumberSpinner);
            jpanel.add(Box.createVerticalStrut(4));
            JPanel jpanel6 = FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(1);
            jpanel.add(jpanel6);
            jpanel6.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
            JPanel jpanel7 = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
            jpanel7.add(new UILabel(Inter.getLocText("PageSetup-Shrink_to_fit_content")));
            isShrinkToFit4None = new UIRadioButton(Inter.getLocText("No"));
            isShrinkToFit4Height = new UIRadioButton(Inter.getLocText("Utils-Row_Height"));
            isShrinkToFit4Width = new UIRadioButton(Inter.getLocText("Utils-Column_Width"));
            ButtonGroup buttongroup1 = new ButtonGroup();
            jpanel7.add(isShrinkToFit4None);
            jpanel7.add(isShrinkToFit4Height);
            jpanel7.add(isShrinkToFit4Width);
            buttongroup1.add(isShrinkToFit4None);
            buttongroup1.add(isShrinkToFit4Height);
            buttongroup1.add(isShrinkToFit4Width);
            jpanel6.add(jpanel7);
        }
    }

    private class PagePane extends BasicPane
    {
        class ShowPagePane extends JPanel
        {

            private static final int NUM_3 = 3;
            private static final double NUM_POINT_3 = 0.29999999999999999D;
            private static final int NUM_NEG_5 = -5;
            private static final int NUM_NEG_15 = -15;
            private static final int NUM_NEG_10 = -10;
            private static final int NUM_NEG_13 = -13;
            private static final int NUM_NEG_7 = -7;
            private static final int NUM_NEG_8 = -8;
            private double paper_width;
            private double paper_height;
            private int paper_orientation;
            private int pane_width;
            private int pane_height;
            private double length_scale;
            private Image img;
            final PagePane this$1;

            public void populate(double d, double d1, int i, boolean flag)
            {
                if(d <= 0.0D || d1 <= 0.0D)
                {
                    return;
                } else
                {
                    paper_width = Math.min(d, 2000D);
                    paper_height = Math.min(d1, 2000D);
                    paper_orientation = i;
                    length_scale = flag ? 0.29999999999999999D : 3D;
                    return;
                }
            }

            public void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                if(paper_width > 0.0D && paper_height > 0.0D)
                {
                    double d2 = (length_scale * paper_width) / (double)(pane_width - 50);
                    double d3 = (length_scale * paper_height) / (double)(pane_height - 30);
                    double d;
                    double d1;
                    if(d2 > 1.0D || d3 > 1.0D)
                    {
                        double d4 = d2 <= d3 ? d3 : d2;
                        d = (length_scale * paper_width) / d4;
                        d1 = (length_scale * paper_height) / d4;
                    } else
                    {
                        d = paper_width * length_scale;
                        d1 = paper_height * length_scale;
                    }
                    Graphics2D graphics2d = (Graphics2D)g;
                    FontMetrics fontmetrics = graphics2d.getFontMetrics();
                    String s = (new StringBuilder()).append("").append(paper_width).toString();
                    if(s.indexOf(".") > 0)
                        s = s.substring(0, s.indexOf(".") + 2);
                    int i = fontmetrics.stringWidth(s);
                    d = Math.max(d, i + 26);
                    String s1 = (new StringBuilder()).append("").append(paper_height).toString();
                    if(s1.indexOf(".") > 0)
                        s1 = s1.substring(0, s1.indexOf(".") + 2);
                    int j = fontmetrics.stringWidth(s1);
                    d1 = Math.max(d1, j + 26);
                    double d5 = ((double)pane_width - d) / 2D;
                    double d6 = ((double)pane_height - d1) / 2D;
                    graphics2d.translate(d5, d6);
                    graphics2d = getG2d(d, d1, graphics2d, s, s1, i, j);
                    if(paper_orientation == 0)
                    {
                        graphics2d.drawImage(img, (int)((d - (double)img.getWidth(null)) / 2D), (int)((d1 - (double)img.getHeight(null)) / 2D), null);
                    } else
                    {
                        graphics2d.rotate(-Math.toRadians(90D));
                        graphics2d.drawImage(img, -(int)((d1 + (double)img.getWidth(null)) / 2D), (int)((d - (double)img.getHeight(null)) / 2D), null);
                        graphics2d.rotate(Math.toRadians(90D));
                    }
                    graphics2d.translate(-d5, -d6);
                }
            }

            private Graphics2D getG2d(double d, double d1, Graphics2D graphics2d, String s, String s1, 
                    int i, int j)
            {
                graphics2d.setColor(Color.WHITE);
                graphics2d.fill3DRect(0, 0, (int)d, (int)d1, true);
                graphics2d.setColor(Color.BLACK);
                graphics2d.drawLine(0, -5, 0, -15);
                graphics2d.drawLine((int)d - 1, -5, (int)d - 1, -15);
                graphics2d.drawString(s, (int)((d - (double)i) / 2D), -5);
                graphics2d.drawLine(2, -10, (int)((d - (double)i) / 2D) + -5, -10);
                graphics2d.drawLine((int)((d + (double)i) / 2D) - -5, -10, (int)d - 3, -10);
                graphics2d.drawLine(2, -10, 7, -13);
                graphics2d.drawLine(2, -10, 7, -7);
                graphics2d.drawLine((int)d - 3, -10, (int)d + -8, -13);
                graphics2d.drawLine((int)d - 3, -10, (int)d + -8, -7);
                graphics2d.drawLine(-15, 0, -5, 0);
                graphics2d.drawLine(-15, (int)d1 - 1, -5, (int)d1 - 1);
                graphics2d.rotate(-Math.toRadians(90D));
                graphics2d.drawString(s1, -(int)((d1 + (double)j) / 2D), -5);
                graphics2d.rotate(Math.toRadians(90D));
                graphics2d.drawLine(-10, 2, -10, (int)((d1 - (double)j) / 2D) + -5);
                graphics2d.drawLine(-10, (int)((d1 + (double)j) / 2D) - -5, -10, (int)d1 - 3);
                graphics2d.drawLine(-10, 2, -13, 7);
                graphics2d.drawLine(-10, 2, -7, 7);
                graphics2d.drawLine(-10, (int)d1 - 3, -13, (int)d1 + -8);
                graphics2d.drawLine(-10, (int)d1 - 3, -7, (int)d1 + -8);
                return graphics2d;
            }

            public ShowPagePane()
            {
                this$1 = PagePane.this;
                super();
                pane_width = 300;
                pane_height = 100;
                length_scale = 0.29999999999999999D;
                setSize(pane_width, pane_height);
                setPreferredSize(new Dimension(pane_width, pane_height));
                setBackground(new Color(128, 128, 128));
                setOpaque(false);
                img = BaseUtils.readImage("/com/fr/base/images/dialog/pagesetup/a.png");
            }
        }


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
        private ShowPagePane showPagePane;
        private UnitFieldPane.UnitLabel unitLabel;
        private int unitType;
        private Report report;
        private ActionListener previewListener;
        private ChangeListener previewListener2;
        private ActionListener switchInchListener;
        private UIComboBoxRenderer paperSizeCellRenderere;
        private UIComboBoxRenderer paperSizeMobileCellRenderere;
        private ItemListener paperSizeItemListener;
        private ItemListener paperSizeItemMobileListener;
        DocumentListener customTextListener;
        private FocusAdapter fa;
        final PageSetupPane this$0;

        protected String title4PopupWindow()
        {
            return "page";
        }

        public void populate(Report report1, int i)
        {
            report = report1;
            ReportSettingsProvider reportsettingsprovider = report1.getReportSettings();
            PaperSettingProvider papersettingprovider = reportsettingsprovider.getPaperSetting();
            if(papersettingprovider.getOrientation() == 1)
                landscapeRadioButton.setSelected(true);
            else
                portraitRadioButton.setSelected(true);
            switchInch.removeActionListener(switchInchListener);
            if(i == 2)
                switchInch.setSelectedIndex(1);
            else
                switchInch.setSelectedIndex(0);
            switchInch.addActionListener(switchInchListener);
            unitSet(i);
            PaperSize papersize = papersettingprovider.getPaperSize();
            if(papersize == null)
                papersize = PaperSize.PAPERSIZE_A4;
            boolean flag = true;
            int j = 0;
            do
            {
                if(j >= ReportConstants.PaperSizeNameSizeArray.length)
                    break;
                Object aobj[] = ReportConstants.PaperSizeNameSizeArray[j];
                if(ComparatorUtils.equals(papersize, aobj[1]))
                {
                    predefinedComboBox.setSelectedIndex(j);
                    predefinedRadioButton.setSelected(true);
                    flag = false;
                    break;
                }
                j++;
            } while(true);
            j = 0;
            do
            {
                if(j >= PageSetupPane.MOBILE_NAME_SIZE_ARRAY.length)
                    break;
                Object aobj1[] = PageSetupPane.MOBILE_NAME_SIZE_ARRAY[j];
                if(ComparatorUtils.equals(papersize, aobj1[1]))
                {
                    mobileComboBox.setSelectedIndex(j);
                    mobileRadioButton.setSelected(true);
                    flag = false;
                    break;
                }
                j++;
            } while(true);
            setAndPopulate(flag, i);
            populateMargin();
        }

        private void unitSet(int i)
        {
            unitType = i;
            unitLabel.setUnitType(i);
            marginTopUnitFieldPane.setUnitType(i);
            marginLeftUnitFieldPane.setUnitType(i);
            marginBottomUnitFieldPane.setUnitType(i);
            marginRightUnitFieldPane.setUnitType(i);
            headerUnitFieldPane.setUnitType(i);
            footerUnitFieldPane.setUnitType(i);
        }

        private void setAndPopulate(boolean flag, int i)
        {
            ReportSettingsProvider reportsettingsprovider = report.getReportSettings();
            PaperSettingProvider papersettingprovider = reportsettingsprovider.getPaperSetting();
            PaperSize papersize = papersettingprovider.getPaperSize();
            if(flag)
            {
                customRadioButton.setSelected(true);
                if(i == 1)
                {
                    paperWidthSpinner.setValue(new Float(papersize.getWidth().toCMValue4Scale2()));
                    paperHeightSpinner.setValue(new Float(papersize.getHeight().toCMValue4Scale2()));
                } else
                if(i == 2)
                {
                    paperWidthSpinner.setValue(new Float(papersize.getWidth().toINCHValue4Scale3()));
                    paperHeightSpinner.setValue(new Float(papersize.getHeight().toINCHValue4Scale3()));
                } else
                {
                    paperWidthSpinner.setValue(new Float(papersize.getWidth().toMMValue4Scale2()));
                    paperHeightSpinner.setValue(new Float(papersize.getHeight().toMMValue4Scale2()));
                }
            }
            if(i == 1)
                showPagePane.populate(papersize.getWidth().toCMValue4Scale2(), papersize.getHeight().toCMValue4Scale2(), papersettingprovider.getOrientation(), false);
            else
            if(i == 2)
                showPagePane.populate(papersize.getWidth().toINCHValue4Scale3(), papersize.getHeight().toINCHValue4Scale3(), papersettingprovider.getOrientation(), false);
            else
                showPagePane.populate(papersize.getWidth().toMMValue4Scale2(), papersize.getHeight().toMMValue4Scale2(), papersettingprovider.getOrientation(), true);
        }

        private void populateMargin()
        {
            ReportSettingsProvider reportsettingsprovider = report.getReportSettings();
            PaperSettingProvider papersettingprovider = reportsettingsprovider.getPaperSetting();
            Margin margin = papersettingprovider.getMargin();
            marginTopUnitFieldPane.setUnitValue(margin.getTop());
            marginLeftUnitFieldPane.setUnitValue(margin.getLeft());
            marginBottomUnitFieldPane.setUnitValue(margin.getBottom());
            marginRightUnitFieldPane.setUnitValue(margin.getRight());
            headerUnitFieldPane.setUnitValue(reportsettingsprovider.getHeaderHeight());
            footerUnitFieldPane.setUnitValue(reportsettingsprovider.getFooterHeight());
        }

        private void updatePaperSizeByType(PaperSettingProvider papersettingprovider)
        {
            if(predefinedRadioButton.isSelected())
                try
                {
                    papersettingprovider.setPaperSize((PaperSize)((PaperSize)predefinedComboBox.getSelectedItem()).clone());
                }
                catch(CloneNotSupportedException clonenotsupportedexception) { }
            else
            if(mobileRadioButton.isSelected())
                try
                {
                    papersettingprovider.setPaperSize((PaperSize)((PaperSize)mobileComboBox.getSelectedItem()).clone());
                }
                catch(CloneNotSupportedException clonenotsupportedexception1) { }
            else
            if(customRadioButton.isSelected())
                if(unitType == 1)
                    papersettingprovider.setPaperSize(new PaperSize(new CM(((Number)paperWidthSpinner.getValue()).floatValue()), new CM(((Number)paperHeightSpinner.getValue()).floatValue())));
                else
                if(unitType == 2)
                    papersettingprovider.setPaperSize(new PaperSize(new INCH(((Number)paperWidthSpinner.getValue()).floatValue()), new INCH(((Number)paperHeightSpinner.getValue()).floatValue())));
                else
                    papersettingprovider.setPaperSize(new PaperSize(new MM(((Number)paperWidthSpinner.getValue()).floatValue()), new MM(((Number)paperHeightSpinner.getValue()).floatValue())));
        }

        public void update(Report report1)
        {
            Object obj = report1.getReportSettings();
            if(obj == null)
            {
                obj = new ReportSettings();
                report1.setReportSettings(((ReportSettingsProvider) (obj)));
            }
            obj = report1.getReportSettings();
            PaperSettingProvider papersettingprovider = ((ReportSettingsProvider) (obj)).getPaperSetting();
            if(landscapeRadioButton.isSelected())
                papersettingprovider.setOrientation(1);
            else
                papersettingprovider.setOrientation(0);
            updatePaperSizeByType(papersettingprovider);
            Margin margin = papersettingprovider.getMargin();
            margin.setTop(marginTopUnitFieldPane.getUnitValue());
            margin.setLeft(marginLeftUnitFieldPane.getUnitValue());
            margin.setBottom(marginBottomUnitFieldPane.getUnitValue());
            margin.setRight(marginRightUnitFieldPane.getUnitValue());
            ((ReportSettingsProvider) (obj)).setHeaderHeight(headerUnitFieldPane.getUnitValue());
            ((ReportSettingsProvider) (obj)).setFooterHeight(footerUnitFieldPane.getUnitValue());
        }

        private void showPagePaneByType(PaperSize papersize, int i)
        {
            if(unitType == 1)
                showPagePane.populate(papersize.getWidth().toCMValue4Scale2(), papersize.getHeight().toCMValue4Scale2(), i, false);
            else
            if(unitType == 2)
                showPagePane.populate(papersize.getWidth().toINCHValue4Scale3(), papersize.getHeight().toINCHValue4Scale3(), i, false);
            else
                showPagePane.populate(papersize.getWidth().toMMValue4Scale2(), papersize.getHeight().toMMValue4Scale2(), i, true);
        }

        private void previewShowPagePane()
        {
            int i;
            if(landscapeRadioButton.isSelected())
                i = 1;
            else
                i = 0;
            if(predefinedRadioButton.isSelected())
            {
                PaperSize papersize = (PaperSize)predefinedComboBox.getSelectedItem();
                showPagePaneByType(papersize, i);
            } else
            if(mobileRadioButton.isSelected())
            {
                PaperSize papersize1 = (PaperSize)mobileComboBox.getSelectedItem();
                showPagePaneByType(papersize1, i);
            } else
            if(customRadioButton.isSelected())
                showPagePane.populate(((Number)paperWidthSpinner.getValue()).doubleValue(), ((Number)paperHeightSpinner.getValue()).doubleValue(), i, unitType == 0);
            showPagePane.repaint();
        }

        private JPanel createIconRadioPane(Icon icon, UIRadioButton uiradiobutton)
        {
            JPanel jpanel = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
            jpanel.add(new UILabel(icon));
            jpanel.add(uiradiobutton);
            return jpanel;
        }

        private void adjustCellRenderByType(StringBuffer stringbuffer, PaperSize papersize)
        {
            stringbuffer.append(" [");
            if(unitType == 1)
            {
                stringbuffer.append(Utils.convertNumberStringToString(new Float(papersize.getWidth().toCMValue4Scale2())));
                stringbuffer.append('x');
                stringbuffer.append(Utils.convertNumberStringToString(new Float(papersize.getHeight().toCMValue4Scale2())));
                stringbuffer.append(' ');
                stringbuffer.append(Inter.getLocText("Unit_CM"));
            } else
            if(unitType == 2)
            {
                stringbuffer.append(Utils.convertNumberStringToString(new Float(papersize.getWidth().toINCHValue4Scale3())));
                stringbuffer.append('x');
                stringbuffer.append(Utils.convertNumberStringToString(new Float(papersize.getHeight().toINCHValue4Scale3())));
                stringbuffer.append(' ');
                stringbuffer.append(Inter.getLocText("PageSetup-inches"));
            } else
            {
                stringbuffer.append(Utils.convertNumberStringToString(new Float(papersize.getWidth().toMMValue4Scale2())));
                stringbuffer.append('x');
                stringbuffer.append(Utils.convertNumberStringToString(new Float(papersize.getHeight().toMMValue4Scale2())));
                stringbuffer.append(' ');
                stringbuffer.append(Inter.getLocText("PageSetup-mm"));
            }
            stringbuffer.append(']');
        }

        private void adjustSpinnerValueByType(PaperSize papersize)
        {
            if(unitType == 1)
            {
                paperWidthSpinner.setValue(new Float(papersize.getWidth().toCMValue4Scale2()));
                paperHeightSpinner.setValue(new Float(papersize.getHeight().toCMValue4Scale2()));
            } else
            if(unitType == 2)
            {
                paperWidthSpinner.setValue(new Float(papersize.getWidth().toINCHValue4Scale3()));
                paperHeightSpinner.setValue(new Float(papersize.getHeight().toINCHValue4Scale3()));
            } else
            {
                paperWidthSpinner.setValue(new Float(papersize.getWidth().toMMValue4Scale2()));
                paperHeightSpinner.setValue(new Float(papersize.getHeight().toMMValue4Scale2()));
            }
        }



















        public PagePane()
        {
            this$0 = PageSetupPane.this;
            super();
            previewListener = new ActionListener() {

                final PagePane this$1;

                public void actionPerformed(ActionEvent actionevent)
                {
                    previewShowPagePane();
                }

                
                {
                    this$1 = PagePane.this;
                    super();
                }
            }
;
            previewListener2 = new ChangeListener() {

                final PagePane this$1;

                public void stateChanged(ChangeEvent changeevent)
                {
                    previewShowPagePane();
                }

                
                {
                    this$1 = PagePane.this;
                    super();
                }
            }
;
            switchInchListener = new ActionListener() {

                final PagePane this$1;

                public void actionPerformed(ActionEvent actionevent)
                {
                    if(actionevent.getSource() == switchInch)
                    {
                        int l = switchInch.getSelectedIndex();
                        switch(l)
                        {
                        default:
                            break;

                        case 0: // '\0'
                            if(unitType != 0)
                            {
                                unitType = 0;
                                DesignerEnvManager.getEnvManager().setPageLengthUnit((short)0);
                                paperWidthSpinner.setValue(Integer.valueOf(Math.round((new INCH(((Number)paperWidthSpinner.getValue()).floatValue())).toMMValue4Scale2())));
                                paperHeightSpinner.setValue(Integer.valueOf(Math.round((new INCH(((Number)paperHeightSpinner.getValue()).floatValue())).toMMValue4Scale2())));
                                populate(report, 0);
                            }
                            customRadioButton.setSelected(true);
                            break;

                        case 1: // '\001'
                            unitType = 2;
                            DesignerEnvManager.getEnvManager().setPageLengthUnit((short)2);
                            paperWidthSpinner.setValue(Float.valueOf((new MM(((Number)paperWidthSpinner.getValue()).floatValue())).toINCHValue4Scale3()));
                            paperHeightSpinner.setValue(Float.valueOf((new MM(((Number)paperHeightSpinner.getValue()).floatValue())).toINCHValue4Scale3()));
                            populate(report, 2);
                            customRadioButton.setSelected(true);
                            break;
                        }
                    }
                }

                
                {
                    this$1 = PagePane.this;
                    super();
                }
            }
;
            paperSizeCellRenderere = new UIComboBoxRenderer() {

                final PagePane this$1;

                public Component getListCellRendererComponent(JList jlist, Object obj, int l, boolean flag, boolean flag1)
                {
                    super.getListCellRendererComponent(jlist, obj, l, flag, flag1);
                    if(obj instanceof PaperSize)
                    {
                        PaperSize papersize = (PaperSize)obj;
                        int i1 = 0;
                        do
                        {
                            if(i1 >= ReportConstants.PaperSizeNameSizeArray.length)
                                break;
                            Object aobj2[] = ReportConstants.PaperSizeNameSizeArray[i1];
                            if(ComparatorUtils.equals(papersize, aobj2[1]))
                            {
                                StringBuffer stringbuffer1 = new StringBuffer(aobj2[0].toString());
                                adjustCellRenderByType(stringbuffer1, papersize);
                                setText(stringbuffer1.toString());
                                break;
                            }
                            i1++;
                        } while(true);
                    }
                    return this;
                }

                
                {
                    this$1 = PagePane.this;
                    super();
                }
            }
;
            paperSizeMobileCellRenderere = new UIComboBoxRenderer() {

                final PagePane this$1;

                public Component getListCellRendererComponent(JList jlist, Object obj, int l, boolean flag, boolean flag1)
                {
                    super.getListCellRendererComponent(jlist, obj, l, flag, flag1);
                    if(obj instanceof PaperSize)
                    {
                        PaperSize papersize = (PaperSize)obj;
                        int i1 = 0;
                        do
                        {
                            if(i1 >= PageSetupPane.MOBILE_NAME_SIZE_ARRAY.length)
                                break;
                            Object aobj2[] = PageSetupPane.MOBILE_NAME_SIZE_ARRAY[i1];
                            if(ComparatorUtils.equals(papersize, aobj2[1]))
                            {
                                StringBuffer stringbuffer1 = new StringBuffer(aobj2[0].toString());
                                adjustCellRenderByType(stringbuffer1, papersize);
                                setText(stringbuffer1.toString());
                                break;
                            }
                            i1++;
                        } while(true);
                    }
                    return this;
                }

                
                {
                    this$1 = PagePane.this;
                    super();
                }
            }
;
            paperSizeItemListener = new ItemListener() {

                final PagePane this$1;

                public void itemStateChanged(ItemEvent itemevent)
                {
                    PaperSize papersize = (PaperSize)predefinedComboBox.getSelectedItem();
                    adjustSpinnerValueByType(papersize);
                    predefinedRadioButton.setSelected(true);
                    previewShowPagePane();
                }

                
                {
                    this$1 = PagePane.this;
                    super();
                }
            }
;
            paperSizeItemMobileListener = new ItemListener() {

                final PagePane this$1;

                public void itemStateChanged(ItemEvent itemevent)
                {
                    PaperSize papersize = (PaperSize)mobileComboBox.getSelectedItem();
                    adjustSpinnerValueByType(papersize);
                    mobileRadioButton.setSelected(true);
                    previewShowPagePane();
                }

                
                {
                    this$1 = PagePane.this;
                    super();
                }
            }
;
            customTextListener = new DocumentListener() {

                final PagePane this$1;

                public void insertUpdate(DocumentEvent documentevent)
                {
                    fireTextEvent();
                }

                public void removeUpdate(DocumentEvent documentevent)
                {
                    fireTextEvent();
                }

                public void changedUpdate(DocumentEvent documentevent)
                {
                    fireTextEvent();
                }

                private void fireTextEvent()
                {
                    customRadioButton.setSelected(((Number)paperWidthSpinner.getValue()).doubleValue() > 0.0D && ((Number)paperHeightSpinner.getValue()).doubleValue() > 0.0D);
                }

                
                {
                    this$1 = PagePane.this;
                    super();
                }
            }
;
            fa = new FocusAdapter() {

                private static final int NUM_11 = 11;
                final PagePane this$1;

                public void focusLost(FocusEvent focusevent)
                {
                    String s = "0";
                    boolean flag = ComparatorUtils.equals(s, marginTopUnitFieldPane.getTextField().getText());
                    boolean flag1 = ComparatorUtils.equals(s, marginBottomUnitFieldPane.getTextField().getText());
                    boolean flag2 = ComparatorUtils.equals(s, marginLeftUnitFieldPane.getTextField().getText());
                    boolean flag3 = ComparatorUtils.equals(s, marginRightUnitFieldPane.getTextField().getText());
                    boolean flag4 = flag || flag1 || flag2;
                    boolean flag5 = flag4 || flag3;
                    boolean flag6 = !flag && !flag1 && !flag2;
                    if(flag5 && zeroMarginWarn.getText().endsWith("<br></html></body>"))
                        zeroMarginWarn.setText((new StringBuilder()).append("<html><body>").append(Inter.getLocText("zeroMarginWarn")).append("</body></html>").toString());
                    else
                    if(flag6 && !flag3)
                    {
                        StringBuffer stringbuffer1 = new StringBuffer();
                        for(int l = 0; l < 11; l++)
                            stringbuffer1.append("&nbsp");

                        zeroMarginWarn.setText((new StringBuilder()).append("<html><body>").append(stringbuffer1).append("<br><br></html></body>").toString());
                    }
                }

                
                {
                    this$1 = PagePane.this;
                    super();
                }
            }
;
            setLayout(FRGUIPaneFactory.createBorderLayout());
            JPanel jpanel = FRGUIPaneFactory.createY_AXISBoxInnerContainer_L_Pane();
            add(jpanel, "North");
            JPanel jpanel1 = FRGUIPaneFactory.createX_AXISBoxInnerContainer_S_Pane();
            jpanel1.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            JPanel jpanel2 = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("PageSetup-Orientation"));
            JPanel jpanel3 = FRGUIPaneFactory.createY_AXISBoxInnerContainer_M_Pane();
            jpanel2.add(jpanel3);
            jpanel1.add(jpanel2);
            JPanel jpanel4 = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
            portraitRadioButton = new UIRadioButton(Inter.getLocText("PageSetup-Portrait"));
            portraitRadioButton.setMnemonic('t');
            jpanel4.add(portraitRadioButton);
            jpanel3.add(jpanel4);
            portraitRadioButton.addActionListener(previewListener);
            JPanel jpanel5 = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
            landscapeRadioButton = new UIRadioButton(Inter.getLocText("PageSetup-Landscape"));
            jpanel3.add(jpanel5);
            landscapeRadioButton.setMnemonic('L');
            jpanel5.add(landscapeRadioButton);
            landscapeRadioButton.addActionListener(previewListener);
            ButtonGroup buttongroup = new ButtonGroup();
            buttongroup.add(portraitRadioButton);
            buttongroup.add(landscapeRadioButton);
            portraitRadioButton.setSelected(true);
            JPanel jpanel6 = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("Preview"));
            showPagePane = new ShowPagePane();
            jpanel6.add(showPagePane);
            jpanel.add(jpanel6);
            jpanel.add(jpanel1);
            JPanel jpanel7 = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("PageSetup-Paper_Size"));
            JPanel jpanel8 = FRGUIPaneFactory.createY_AXISBoxInnerContainer_M_Pane();
            jpanel7.add(jpanel8);
            jpanel.add(jpanel7);
            predefinedRadioButton = new UIRadioButton((new StringBuilder()).append(Inter.getLocText("PageSetup-Predefined")).append(":").toString());
            predefinedRadioButton.setMnemonic('P');
            predefinedRadioButton.addActionListener(previewListener);
            mobileRadioButton = new UIRadioButton((new StringBuilder()).append(Inter.getLocText("FR-Designer_MobilePhone")).append("  :").toString());
            mobileRadioButton.setMnemonic('M');
            mobileRadioButton.addActionListener(previewListener);
            customRadioButton = new UIRadioButton((new StringBuilder()).append(Inter.getLocText("Custom")).append(":").toString());
            customRadioButton.setMnemonic('C');
            customRadioButton.addActionListener(previewListener);
            predefinedComboBox = new UIComboBox();
            mobileComboBox = new UIComboBox();
            paperWidthSpinner = new UIBasicSpinner(new SpinnerNumberModel(0.0D, 0.0D, 1.7976931348623157E+308D, 1.0D));
            ((javax.swing.JSpinner.DefaultEditor)paperWidthSpinner.getEditor()).getTextField().setColumns(7);
            paperHeightSpinner = new UIBasicSpinner(new SpinnerNumberModel(0.0D, 0.0D, 1.7976931348623157E+308D, 1.0D));
            ((javax.swing.JSpinner.DefaultEditor)paperHeightSpinner.getEditor()).getTextField().setColumns(7);
            unitLabel = new UnitFieldPane.UnitLabel(0, paperHeightSpinner.getPreferredSize().height);
            String as[] = {
                Inter.getLocText("Unit_MM"), Inter.getLocText("Unit_INCH")
            };
            switchInch = new UIComboBox(as);
            switchInch.setEditable(false);
            switchInch.setSize(paperHeightSpinner.getPreferredSize().width, paperHeightSpinner.getPreferredSize().height);
            switchInch.setSelectedIndex(unitType);
            switchInch.addActionListener(switchInchListener);
            predefinedComboBox.setRenderer(paperSizeCellRenderere);
            predefinedComboBox.addItemListener(paperSizeItemListener);
            mobileComboBox.setRenderer(paperSizeMobileCellRenderere);
            mobileComboBox.addItemListener(paperSizeItemMobileListener);
            ((javax.swing.JSpinner.DefaultEditor)paperWidthSpinner.getEditor()).getTextField().getDocument().addDocumentListener(customTextListener);
            ((javax.swing.JSpinner.DefaultEditor)paperHeightSpinner.getEditor()).getTextField().getDocument().addDocumentListener(customTextListener);
            paperWidthSpinner.addChangeListener(previewListener2);
            paperHeightSpinner.addChangeListener(previewListener2);
            for(int i = 0; i < ReportConstants.PaperSizeNameSizeArray.length; i++)
            {
                Object aobj[] = ReportConstants.PaperSizeNameSizeArray[i];
                predefinedComboBox.addItem(aobj[1]);
            }

            for(int j = 0; j < PageSetupPane.MOBILE_NAME_SIZE_ARRAY.length; j++)
            {
                Object aobj1[] = PageSetupPane.MOBILE_NAME_SIZE_ARRAY[j];
                mobileComboBox.addItem(aobj1[1]);
            }

            JPanel jpanel9 = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
            jpanel8.add(jpanel9);
            jpanel9.add(predefinedRadioButton);
            jpanel9.add(predefinedComboBox);
            JPanel jpanel10 = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
            jpanel8.add(jpanel10);
            jpanel10.add(mobileRadioButton);
            jpanel10.add(mobileComboBox);
            ButtonGroup buttongroup1 = new ButtonGroup();
            buttongroup1.add(predefinedRadioButton);
            buttongroup1.add(mobileRadioButton);
            buttongroup1.add(customRadioButton);
            JPanel jpanel11 = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
            jpanel8.add(jpanel11);
            jpanel11.add(customRadioButton);
            jpanel11.add(paperWidthSpinner);
            jpanel11.add(new UILabel("x"));
            jpanel11.add(paperHeightSpinner);
            jpanel11.add(switchInch);
            JPanel jpanel12 = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("PageSetup-Margin"));
            JPanel jpanel13 = FRGUIPaneFactory.createX_AXISBoxInnerContainer_M_Pane();
            jpanel12.add(jpanel13);
            jpanel1.add(jpanel12);
            StringBuffer stringbuffer = new StringBuffer();
            for(int k = 0; k < 11; k++)
                stringbuffer.append("&nbsp");

            zeroMarginWarn = new UILabel((new StringBuilder()).append("<html><body>").append(stringbuffer).append("<br><br></html></body>").toString());
            zeroMarginWarn.setForeground(Color.RED);
            JPanel jpanel14 = FRGUIPaneFactory.createY_AXISBoxInnerContainer_M_Pane();
            jpanel13.add(jpanel14);
            JPanel jpanel15 = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
            jpanel14.add(jpanel15);
            jpanel15.add(new UILabel((new StringBuilder()).append(Inter.getLocText("Top")).append(":").toString()));
            marginTopUnitFieldPane = new UnitFieldPane(0);
            jpanel15.add(marginTopUnitFieldPane);
            JPanel jpanel16 = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
            jpanel14.add(jpanel16);
            jpanel16.add(new UILabel((new StringBuilder()).append(Inter.getLocText("Bottom")).append(":").toString()));
            marginBottomUnitFieldPane = new UnitFieldPane(0);
            jpanel16.add(marginBottomUnitFieldPane);
            JPanel jpanel17 = FRGUIPaneFactory.createY_AXISBoxInnerContainer_M_Pane();
            jpanel13.add(jpanel17);
            JPanel jpanel18 = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
            jpanel17.add(jpanel18);
            jpanel18.add(new UILabel((new StringBuilder()).append(Inter.getLocText("Left")).append(":").toString()));
            marginLeftUnitFieldPane = new UnitFieldPane(0);
            jpanel18.add(marginLeftUnitFieldPane);
            JPanel jpanel19 = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
            jpanel17.add(jpanel19);
            jpanel19.add(new UILabel((new StringBuilder()).append(Inter.getLocText("Right")).append(":").toString()));
            marginRightUnitFieldPane = new UnitFieldPane(0);
            jpanel19.add(marginRightUnitFieldPane);
            marginTopUnitFieldPane.getTextField().addFocusListener(fa);
            marginBottomUnitFieldPane.getTextField().addFocusListener(fa);
            marginLeftUnitFieldPane.getTextField().addFocusListener(fa);
            marginRightUnitFieldPane.getTextField().addFocusListener(fa);
            jpanel13.add(zeroMarginWarn);
            JPanel jpanel20 = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("Height"));
            JPanel jpanel21 = FRGUIPaneFactory.createNormalFlowInnerContainer_M_Pane();
            jpanel.add(jpanel20);
            jpanel20.add(jpanel21);
            JPanel jpanel22 = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
            jpanel21.add(jpanel22);
            jpanel22.add(new UILabel((new StringBuilder()).append(Inter.getLocText("PageSetup-Header")).append(":").toString()));
            headerUnitFieldPane = new UnitFieldPane(0);
            jpanel22.add(headerUnitFieldPane);
            JPanel jpanel23 = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
            jpanel21.add(jpanel23);
            jpanel23.add(new UILabel((new StringBuilder()).append(Inter.getLocText("PageSetup-Footer")).append(":").toString()));
            footerUnitFieldPane = new UnitFieldPane(0);
            jpanel23.add(footerUnitFieldPane);
            JPanel jpanel24 = FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(1);
            jpanel.add(jpanel24);
            jpanel24.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
        }
    }


    private PagePane pagePane;
    private OtherPane otherPane;
    private UILabel zeroMarginWarn;
    public static final PaperSize MOBILE_SMAIL_SIZE;
    public static final PaperSize MOBILE_LARGE_SIZE;
    public static final Object MOBILE_NAME_SIZE_ARRAY[][];

    public PageSetupPane()
    {
        initComponents();
    }

    private void initComponents()
    {
        setLayout(FRGUIPaneFactory.createBorderLayout());
        UITabbedPane uitabbedpane = new UITabbedPane();
        add(uitabbedpane, "Center");
        pagePane = new PagePane();
        otherPane = new OtherPane();
        uitabbedpane.addTab(Inter.getLocText("PageSetup-Page"), pagePane);
        uitabbedpane.addTab(Inter.getLocText("Other"), otherPane);
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("Page_Setup");
    }

    public void populate(Report report, int i)
    {
        if(report == null)
        {
            return;
        } else
        {
            pagePane.populate(report, i);
            otherPane.populate(report);
            return;
        }
    }

    public void update(Report report)
    {
        if(report == null)
        {
            return;
        } else
        {
            pagePane.update(report);
            otherPane.update(report);
            return;
        }
    }

    static 
    {
        MOBILE_SMAIL_SIZE = new PaperSize(new MM(142.8F), new MM(254F));
        MOBILE_LARGE_SIZE = new PaperSize(new MM(190.5F), new MM(338.7F));
        MOBILE_NAME_SIZE_ARRAY = (new Object[][] {
            new Object[] {
                Inter.getLocText("FR-Designer_PaperSize-Mobile-Large"), MOBILE_LARGE_SIZE
            }, new Object[] {
                Inter.getLocText("FR-Designer_PaperSize-Mobile-Small"), MOBILE_SMAIL_SIZE
            }
        });
    }


}
