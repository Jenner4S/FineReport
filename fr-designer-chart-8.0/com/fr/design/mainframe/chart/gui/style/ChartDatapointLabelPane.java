// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style;

import com.fr.base.*;
import com.fr.chart.base.*;
import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.frpane.UIBubbleFloatPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.style.FormatPane;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.general.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.mainframe.chart.gui.style:
//            ChartTextAttrPane

public class ChartDatapointLabelPane extends BasicPane
{

    protected static final int SPACE = 4;
    protected static final int NEWLIEN = 3;
    private static Map nameValueMap = new HashMap();
    private static Map valueNameMap = new HashMap();
    protected ChartTextAttrPane textFontPane;
    protected UICheckBox isLabelShow;
    protected UIComboBox positionBox;
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
    protected ChartStylePane parent;
    protected UICheckBox isGuid;
    protected UIComboBox divideComoBox;
    protected JPanel labelPane;

    public ChartDatapointLabelPane()
    {
    }

    public ChartDatapointLabelPane(String as[], Integer ainteger[], Plot plot, ChartStylePane chartstylepane)
    {
        parent = chartstylepane;
        isLabelShow = new UICheckBox(Inter.getLocText("FR-Chart-Chart_Label"));
        if(as != null && as.length > 0 && ainteger != null && ainteger.length > 0)
        {
            nameValueMap.clear();
            valueNameMap.clear();
            positionBox = new UIComboBox(as);
            positionBox.setSelectedItem(ainteger[0]);
            for(int i = 0; i < as.length; i++)
            {
                nameValueMap.put(as[i], ainteger[i]);
                valueNameMap.put(ainteger[i], as[i]);
            }

        }
        boolean flag = plot.isSupportLeadLine();
        if(plot.isSupportCategoryFilter())
            isCategory = new UICheckBox(Inter.getLocText("Chart-Category_Name"));
        isSeries = new UICheckBox(Inter.getLocText("Chart-Series_Name"));
        isValue = new UICheckBox(Inter.getLocText("Chart-Use_Value"));
        isValue.setSelected(true);
        valueFormatButton = new UIButton(Inter.getLocText("Chart-Use_Format"));
        if(plot.isSupportValuePercent())
        {
            isValuePercent = new UICheckBox(Inter.getLocText("Chart-Value_Percent"));
            valuePercentFormatButton = new UIButton(Inter.getLocText("Chart-Use_Format"));
            if(plot.isShowAllDataPointLabel())
                isValuePercent.setText(Inter.getLocText("Chart-Value_Conversion"));
        }
        if(plot.isSupportDelimiter())
            divideComoBox = new UIComboBox(ChartConstants.DELIMITERS);
        textFontPane = new ChartTextAttrPane();
        if(flag)
            isGuid = new UICheckBox(Inter.getLocText("ChartF-Show_GuidLine"));
        if(plot.isShowAllDataPointLabel())
        {
            isSeries.setSelected(true);
            isGuid.setSelected(true);
        }
        initFormatListener();
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d, d1
        };
        double ad1[] = {
            d, d, d, d, d, d, d, d
        };
        double ad2[] = {
            d, d, d, d, d, d, d
        };
        Component acomponent[][] = new Component[8][3];
        JPanel jpanel = null;
        if(positionBox != null)
        {
            JPanel jpanel1 = new JPanel(new BorderLayout(6, 6));
            jpanel1.add(new BoldFontTextLabel(Inter.getLocText("Chart-Layout_Position")), "West");
            jpanel1.add(positionBox, "Center");
            acomponent[0] = (new Component[] {
                jpanel1, null
            });
            if(flag)
                positionBox.addItemListener(new ItemListener() {

                    final ChartDatapointLabelPane this$0;

                    public void itemStateChanged(ItemEvent itemevent)
                    {
                        checkLeadLineWhenPositionChange();
                    }

            
            {
                this$0 = ChartDatapointLabelPane.this;
                super();
            }
                }
);
        }
        if(flag)
        {
            acomponent[1] = (new Component[] {
                isSeries, null
            });
            acomponent[2] = (new Component[] {
                null, null
            });
        } else
        {
            acomponent[1] = (new Component[] {
                isCategory, null
            });
            acomponent[2] = (new Component[] {
                isSeries, null
            });
        }
        acomponent[3] = (new Component[] {
            isValue, valueFormatButton
        });
        acomponent[4] = (new Component[] {
            isValuePercent, valuePercentFormatButton
        });
        JPanel jpanel2 = new JPanel(new BorderLayout(6, 6));
        if(plot.isSupportDelimiter())
        {
            jpanel2.add(new BoldFontTextLabel(Inter.getLocText("FR-Chart-Delimiter_Symbol")), "West");
            jpanel2.add(divideComoBox, "Center");
        }
        if(flag)
        {
            acomponent[5] = (new Component[] {
                isGuid, null
            });
            acomponent[6] = (new Component[] {
                jpanel2, null
            });
            acomponent[7] = (new Component[] {
                textFontPane, null
            });
            labelPane = TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
        } else
        {
            acomponent[5] = (new Component[] {
                jpanel2, null
            });
            acomponent[6] = (new Component[] {
                textFontPane, null
            });
            labelPane = TableLayoutHelper.createTableLayoutPane(acomponent, ad2, ad);
        }
        double ad3[] = {
            d, d
        };
        double ad4[] = {
            46D, d1
        };
        jpanel = TableLayoutHelper.createTableLayoutPane(new Component[][] {
            new Component[] {
                isLabelShow, null
            }, new Component[] {
                null, labelPane
            }
        }, ad3, ad4);
        setLayout(new BorderLayout());
        add(jpanel, "Center");
        isLabelShow.addActionListener(new ActionListener() {

            final ChartDatapointLabelPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                checkBoxUse();
            }

            
            {
                this$0 = ChartDatapointLabelPane.this;
                super();
            }
        }
);
    }

    private void getLabelPositionPane()
    {
    }

    private void getLabelContentPane()
    {
    }

    private void getShowGuidPane()
    {
    }

    private void getDelimPane()
    {
    }

    private void getLabelFontPane()
    {
    }

    private void checkLeadLineWhenPositionChange()
    {
        if(isGuid != null && positionBox != null)
        {
            isGuid.setSelected(positionBox.getSelectedIndex() != 0);
            isGuid.setEnabled(positionBox.getSelectedIndex() != 0);
        }
    }

    private void initValueFormat()
    {
        if(valueFormatButton != null)
            valueFormatButton.addMouseListener(new MouseAdapter() {

                final ChartDatapointLabelPane this$0;

                public void mouseReleased(MouseEvent mouseevent)
                {
                    if(!valueFormatButton.isEnabled())
                        return;
                    if(valueFormatPane == null)
                        valueFormatPane = new FormatPane();
                    Point point = valueFormatButton.getLocationOnScreen();
                    Point point1 = new Point(point.x + valueFormatButton.getWidth(), point.y + valueFormatButton.getHeight());
                    UIBubbleFloatPane uibubblefloatpane = new UIBubbleFloatPane(2, point1, valueFormatPane, 258, 209) {

                        final _cls3 this$1;

                        public void updateContentPane()
                        {
                            valueFormat = valueFormatPane.update();
                            parent.attributeChanged();
                        }

                    
                    {
                        this$1 = _cls3.this;
                        super(i, point, basicbeanpane, j, k);
                    }
                    }
;
                    uibubblefloatpane.show(ChartDatapointLabelPane.this, Style.getInstance(valueFormat));
                    super.mouseReleased(mouseevent);
                }

            
            {
                this$0 = ChartDatapointLabelPane.this;
                super();
            }
            }
);
    }

    private void initPercentFormat()
    {
        if(valuePercentFormatButton != null)
            valuePercentFormatButton.addMouseListener(new MouseAdapter() {

                final ChartDatapointLabelPane this$0;

                public void mouseReleased(MouseEvent mouseevent)
                {
                    if(!valuePercentFormatButton.isEnabled())
                        return;
                    if(percentFormatPane == null)
                        percentFormatPane = new FormatPane();
                    Point point = valuePercentFormatButton.getLocationOnScreen();
                    Point point1 = new Point(point.x + valuePercentFormatButton.getWidth(), point.y + valuePercentFormatButton.getHeight());
                    UIBubbleFloatPane uibubblefloatpane = new UIBubbleFloatPane(2, point1, percentFormatPane, 258, 209) {

                        final _cls4 this$1;

                        public void updateContentPane()
                        {
                            percentFormat = percentFormatPane.update();
                            parent.attributeChanged();
                        }

                    
                    {
                        this$1 = _cls4.this;
                        super(i, point, basicbeanpane, j, k);
                    }
                    }
;
                    uibubblefloatpane.show(ChartDatapointLabelPane.this, Style.getInstance(percentFormat));
                    super.mouseReleased(mouseevent);
                    percentFormatPane.justUsePercentFormat();
                }

            
            {
                this$0 = ChartDatapointLabelPane.this;
                super();
            }
            }
);
    }

    protected void initFormatListener()
    {
        initValueFormat();
        initPercentFormat();
    }

    protected String title4PopupWindow()
    {
        return null;
    }

    public void populate(AttrContents attrcontents)
    {
        isLabelShow.setSelected(true);
        String s = attrcontents.getSeriesLabel();
        if(s != null)
        {
            int i = 0;
            do
            {
                if(i >= ChartConstants.DELIMITERS.length)
                    break;
                String s1 = ChartConstants.DELIMITERS[i];
                if(divideComoBox != null && s.contains(s1))
                {
                    divideComoBox.setSelectedItem(s1);
                    break;
                }
                i++;
            } while(true);
            if(divideComoBox != null && s.contains("${BR}"))
                divideComoBox.setSelectedItem(ChartConstants.DELIMITERS[3]);
            if(isCategory != null)
                isCategory.setSelected(s.contains("${CATEGORY}"));
            if(isSeries != null)
                isSeries.setSelected(s.contains("${SERIES}"));
            if(isValue != null)
                isValue.setSelected(s.contains("${VALUE}"));
            if(isValuePercent != null)
                isValuePercent.setSelected(s.contains("${PERCENT}"));
        } else
        {
            noSelected();
        }
        int j = attrcontents.getPosition();
        if(positionBox != null && valueNameMap.containsKey(Integer.valueOf(j)))
            positionBox.setSelectedItem(valueNameMap.get(Integer.valueOf(j)));
        if(isGuid != null)
        {
            isGuid.setSelected(attrcontents.isShowGuidLine());
            if(positionBox != null)
                isGuid.setEnabled(positionBox.getSelectedIndex() != 0);
        }
        valueFormat = attrcontents.getFormat();
        percentFormat = attrcontents.getPercentFormat();
    }

    private void noSelected()
    {
        if(isCategory != null)
            isCategory.setSelected(false);
        if(isSeries != null)
            isSeries.setSelected(false);
        if(isValue != null)
            isValue.setSelected(false);
        if(isValuePercent != null)
            isValuePercent.setSelected(false);
    }

    public void checkBoxUse()
    {
        labelPane.setVisible(isLabelShow.isSelected());
        checkLeadLineWhenPositionChange();
    }

    public void populate(DataSeriesCondition dataseriescondition)
    {
        if(dataseriescondition == null)
            isLabelShow.setSelected(false);
        else
        if(dataseriescondition instanceof AttrContents)
        {
            AttrContents attrcontents = (AttrContents)dataseriescondition;
            populate(attrcontents);
        }
        if(textFontPane != null)
            if(dataseriescondition != null)
            {
                textFontPane.populate(((AttrContents)dataseriescondition).getTextAttr());
            } else
            {
                FRFont frfont = FRContext.getDefaultValues().getFRFont();
                textFontPane.populate(frfont != null ? frfont : FRFont.getInstance());
            }
        labelPane.setVisible(isLabelShow.isSelected());
    }

    public AttrContents update()
    {
        if(!isLabelShow.isSelected())
            return null;
        AttrContents attrcontents = new AttrContents();
        String s = "";
        String s1 = " ";
        if(divideComoBox != null)
            s1 = Utils.objectToString(divideComoBox.getSelectedItem());
        if(s1.contains(ChartConstants.DELIMITERS[3]))
            s1 = "${BR}";
        else
        if(s1.contains(ChartConstants.DELIMITERS[4]))
            s1 = " ";
        if(isCategory != null && isCategory.isSelected())
            s = (new StringBuilder()).append(s).append("${CATEGORY}").append(s1).toString();
        if(isSeries != null && isSeries.isSelected())
            s = (new StringBuilder()).append(s).append("${SERIES}").append(s1).toString();
        if(isValue != null && isValue.isSelected())
            s = (new StringBuilder()).append(s).append("${VALUE}").append(s1).toString();
        if(isValuePercent != null && isValuePercent.isSelected())
            s = (new StringBuilder()).append(s).append("${PERCENT}").append(s1).toString();
        if(s.contains(s1))
            s = s.substring(0, s.lastIndexOf(s1));
        if(positionBox != null && positionBox.getSelectedItem() != null && nameValueMap.containsKey(positionBox.getSelectedItem()))
            attrcontents.setPosition(((Integer)nameValueMap.get(positionBox.getSelectedItem())).intValue());
        attrcontents.setSeriesLabel(s);
        if(isGuid != null)
            attrcontents.setShowGuidLine(isGuid.isSelected());
        if(valueFormat != null)
            attrcontents.setFormat(valueFormat);
        if(percentFormat != null)
            attrcontents.setPercentFormat(percentFormat);
        if(textFontPane != null)
            attrcontents.setTextAttr(textFontPane.update());
        updatePercentFormatpane();
        return attrcontents;
    }

    protected void updatePercentFormatpane()
    {
        if(isValuePercent != null && isValuePercent.isSelected())
        {
            if(percentFormatPane == null)
                percentFormatPane = new FormatPane();
            if(percentFormat == null)
            {
                CoreDecimalFormat coredecimalformat = new CoreDecimalFormat(new DecimalFormat(), "#.##%");
                percentFormatPane.populateBean(coredecimalformat);
                percentFormat = coredecimalformat;
            }
        }
    }


}
