package com.fr.plugin.chart.designer.component;

import com.fr.base.Style;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.frpane.UIBubbleFloatPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ibutton.UIToggleButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.itextarea.UITextArea;
import com.fr.design.gui.style.FormatPane;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import com.fr.plugin.chart.base.AttrTooltipContent;
import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;
import com.fr.plugin.chart.designer.style.VanChartStylePane;
import com.fr.stable.Constants;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.Format;

/**
 * 内容界面 。数据点提示和标签界面都用到
 */
public class VanChartTooltipContentPane extends BasicPane {

    private static final long serialVersionUID = 8825929000117843641L;

    protected UIButtonGroup<Integer> content;
    protected UICheckBox isCategory;
    protected UICheckBox isSeries;
    protected UICheckBox isValue;
    protected UIButton valueFormatButton;
    protected UICheckBox isValuePercent;
    protected UIButton valuePercentFormatButton;

    protected FormatPane valueFormatPane;
    protected FormatPane percentFormatPane;
    protected Format valueFormat;
    protected Format percentFormat;

    protected JPanel centerPane;
    protected UITextArea customFormatter;
    protected UIToggleButton useHtml;

    protected VanChartStylePane parent;
    private JPanel showOnPane;

    private boolean isDirty;

    public VanChartTooltipContentPane(VanChartStylePane parent, JPanel showOnPane){
        this.parent = parent;
        this.showOnPane = showOnPane;
        this.isDirty = true;

        this.setLayout(new BorderLayout());
        this.add(createLabelContentPane(),BorderLayout.CENTER) ;
    }

    protected JPanel createLabelContentPane() {
        content = new UIButtonGroup<Integer>(new String[]{Inter.getLocText("Plugin-ChartF_Common"),
                Inter.getLocText("Plugin-ChartF_Custom")});
        isCategory = new UICheckBox(Inter.getLocText("Chart-Category_Name"));
        isSeries = new UICheckBox(Inter.getLocText("Chart-Series_Name"));
        isValue = new UICheckBox(Inter.getLocText("Chart-Use_Value"));
        isValue.setSelected(true);
        valueFormatButton = new UIButton(Inter.getLocText("Chart-Use_Format"));
        isValuePercent = new UICheckBox(Inter.getLocText("Chart-Value_Percent"));
        valuePercentFormatButton = new UIButton(Inter.getLocText("Chart-Use_Format"));
        initFormatListener();

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {f, p};
        double[] rowSize = getRowSize(p);

        final JPanel commonPanel = TableLayoutHelper.createTableLayoutPane(getPaneComponents(), rowSize, columnSize);

        customFormatter = new UITextArea("formatter");
        useHtml = new UIToggleButton(Inter.getLocText("Plugin-ChartF_Html"));
        final JPanel customPanel = new JPanel(new BorderLayout(0, 4));
        customPanel.add(customFormatter, BorderLayout.CENTER);
        customPanel.add(useHtml, BorderLayout.SOUTH);

        centerPane = new JPanel(new CardLayout()){
            @Override
            public Dimension getPreferredSize() {
                if(content.getSelectedIndex() == 0){
                    return commonPanel.getPreferredSize();
                } else {
                    return customPanel.getPreferredSize();
                }
            }
        };
        centerPane.add(commonPanel,Inter.getLocText("Plugin-ChartF_Common"));
        centerPane.add(customPanel, Inter.getLocText("Plugin-ChartF_Custom"));

        initContentListener();
        initCommonChangeListener();

        JPanel contentPane = new JPanel(new BorderLayout(0, 4));
        contentPane.add(content, BorderLayout.NORTH);
        contentPane.add(centerPane, BorderLayout.CENTER);

        return createTableLayoutPaneWithTitle(Inter.getLocText("Plugin-ChartF_Content"), contentPane);
    }

    protected JPanel createTableLayoutPaneWithTitle(String title, Component component) {
        return TableLayout4VanChartHelper.createTableLayoutPaneWithTitle(title, component);
    }

    protected double[] getRowSize(double p){
        return new double[]{p,p,p,p};
    }

    protected Component[][] getPaneComponents(){
        return new Component[][]{
                new Component[]{isCategory,null},
                new Component[]{isSeries,null},
                new Component[]{isValue,valueFormatButton},
                new Component[]{isValuePercent,valuePercentFormatButton},
        };
    }

    private void initContentListener() {
        content.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               checkCardPane();
            }
        });
    }

    private void initCommonChangeListener() {
        isCategory.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                isDirty = true;
            }
        });
        isSeries.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                isDirty = true;
            }
        });
        isValue.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                isDirty = true;
            }
        });
        isValuePercent.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                isDirty = true;
            }
        });
    }

    protected void initFormatListener() {
        initValueFormat();
        initPercentFormat();
    }

    private void initValueFormat() {
        if(valueFormatButton != null) {
            valueFormatButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    if (!valueFormatButton.isEnabled()) {
                        return;
                    }

                    if(valueFormatPane == null) {
                        valueFormatPane =  new FormatPane();
                    }
                    Point comPoint = valueFormatButton.getLocationOnScreen();
                    Point arrowPoint = new Point(comPoint.x + valueFormatButton.getWidth(), comPoint.y + valueFormatButton.getHeight());
                    UIBubbleFloatPane<Style> pane = new UIBubbleFloatPane(Constants.LEFT, arrowPoint, valueFormatPane, 258, 209) {

                        @Override
                        public void updateContentPane() {
                            valueFormat = valueFormatPane.update();
                            if(parent != null){//条件属性没有parent
                                parent.attributeChanged();
                            }
                        }
                    };
                    pane.show(showOnPane, Style.getInstance(valueFormat));
                    super.mouseReleased(e);
                }
            });
        }
    }

    private void initPercentFormat() {
        if(valuePercentFormatButton != null) {
            valuePercentFormatButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    if (!valuePercentFormatButton.isEnabled()) {
                        return;
                    }

                    if(percentFormatPane == null) {
                        percentFormatPane =  new FormatPane();
                    }
                    Point comPoint = valuePercentFormatButton.getLocationOnScreen();
                    Point arrowPoint = new Point(comPoint.x + valuePercentFormatButton.getWidth(), comPoint.y + valuePercentFormatButton.getHeight());
                    UIBubbleFloatPane<Style> pane = new UIBubbleFloatPane(Constants.LEFT, arrowPoint, percentFormatPane, 258, 209) {
                        @Override
                        public void updateContentPane() {
                            percentFormat = percentFormatPane.update();
                            if(parent != null){
                                parent.attributeChanged();
                            }
                        }
                    };
                    pane.show(showOnPane, Style.getInstance(percentFormat));
                    super.mouseReleased(e);
                    percentFormatPane.justUsePercentFormat();
                }
            });
        }
    }

    private void checkCardPane() {
        CardLayout cardLayout = (CardLayout) centerPane.getLayout();
        if (content.getSelectedIndex() == 1) {
            cardLayout.show(centerPane,Inter.getLocText("Plugin-ChartF_Custom"));
            if(this.isDirty){
                customFormatter.setText(update().getFormatterTextFromCommon());
                this.isDirty = false;
            }
        } else {
            cardLayout.show(centerPane, Inter.getLocText("Plugin-ChartF_Common"));
        }
    }

    @Override
    protected String title4PopupWindow() {
        return "";
    }


    public void populate(AttrTooltipContent attrTooltipContent){
        if(attrTooltipContent == null){
            return;
        }
        content.setSelectedIndex(attrTooltipContent.isCommon() ? 0 : 1);
        isCategory.setSelected(attrTooltipContent.isCategoryName());
        isSeries.setSelected(attrTooltipContent.isSeriesName());
        isValue.setSelected(attrTooltipContent.isValue());
        isValuePercent.setSelected(attrTooltipContent.isPercentValue());
        customFormatter.setText(attrTooltipContent.getCustomText());
        useHtml.setSelected(attrTooltipContent.isUseHtml());
        valueFormat = attrTooltipContent.getFormat();
        percentFormat = attrTooltipContent.getPercentFormat();
        if(!attrTooltipContent.isCommon()){
            this.isDirty = false;
        }
        checkCardPane();
    }

    public AttrTooltipContent update() {
        AttrTooltipContent attrTooltipContent = new AttrTooltipContent();
        attrTooltipContent.setCommon(content.getSelectedIndex() == 0);
        attrTooltipContent.setCategoryName(isCategory.isSelected());
        attrTooltipContent.setSeriesName(isSeries.isSelected());
        attrTooltipContent.setValue(isValue.isSelected());
        attrTooltipContent.setPercentValue(isValuePercent.isSelected());
        attrTooltipContent.setCustomText(customFormatter.getText());
        attrTooltipContent.setUseHtml(useHtml.isSelected());
        attrTooltipContent.setFormat(valueFormat);
        attrTooltipContent.setPercentFormat(percentFormat);

        return attrTooltipContent;
    }
}
