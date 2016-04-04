// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style.axis;

import com.fr.base.Formula;
import com.fr.base.Utils;
import com.fr.chart.base.ChartBaseUtils;
import com.fr.chart.base.ChartConstants;
import com.fr.chart.chartattr.*;
import com.fr.design.chart.ChartSwingUtils;
import com.fr.design.chart.axis.MinMaxValuePane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.gui.style.FormatPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.style.*;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

// Referenced classes of package com.fr.design.mainframe.chart.gui.style.axis:
//            ChartAxisUsePane

public class ChartValuePane extends ChartAxisUsePane
{

    private static final long serialVersionUID = 0x667b1520eb692ffcL;
    protected ChartAxisTitlePane axisTitlePane;
    protected ChartAxisTitleNoFormulaPane axisTitleNoFormulaPane;
    private ChartAxisLineStylePane axisLineStylePane;
    private UIComboBox unitCombox;
    private ChartAxisLabelPane axisLabelPane;
    private FormatPane formatPane;
    private UICheckBox axisReversed;
    protected MinMaxValuePane minValuePane;
    private UICheckBox logBox;
    private UITextField logBaseField;
    private JPanel dataPane;
    private JPanel zeroPane;

    public ChartValuePane()
    {
    }

    protected void layoutContentPane()
    {
        leftcontentPane = createContentPane();
        leftcontentPane.setBorder(BorderFactory.createMatteBorder(5, 10, 5, 0, original));
        add(leftcontentPane);
    }

    protected JPanel createContentPane()
    {
        axisTitlePane = new ChartAxisTitlePane();
        axisTitleNoFormulaPane = new ChartAxisTitleNoFormulaPane();
        axisLineStylePane = new ChartAxisLineStylePane();
        zeroPane = aliagnZero4Second();
        axisReversed = new UICheckBox(Inter.getLocText("AxisReversed"));
        unitCombox = new UIComboBox(ChartConstants.UNIT_I18N_VALUES);
        formatPane = new FormatPane();
        axisLabelPane = new ChartAxisLabelPane();
        dataPane = createDataDefinePane();
        JPanel jpanel = new JPanel();
        jpanel.setLayout(new BorderLayout());
        jpanel.add(getComponentPane(), "Center");
        return jpanel;
    }

    private JPanel getComponentPane()
    {
        return isSupportLineStyle() ? getPaneWithLineStyle() : getPaneWithOutLineStyle();
    }

    private JPanel getPaneWithLineStyle()
    {
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d1
        };
        double ad1[] = {
            d, d, d, d, d, d, d, d, d, d, 
            d, d, d, d, d, d
        };
        Component acomponent[][] = {
            {
                getAxisTitlePane()
            }, {
                new JSeparator()
            }, {
                axisLineStylePane
            }, {
                zeroPane
            }, {
                new JSeparator()
            }, {
                axisReversed
            }, {
                new JSeparator()
            }, {
                TableLayoutHelper.createTableLayoutPane4Chart(new String[] {
                    "ChartF-Units"
                }, new Component[][] {
                    new Component[] {
                        unitCombox
                    }
                }, new double[] {
                    d
                }, new double[] {
                    d1
                })
            }, {
                new JSeparator()
            }, {
                TableLayoutHelper.createTableLayoutPane4Chart(new String[] {
                    "Data_Type"
                }, new Component[][] {
                    new Component[] {
                        formatPane
                    }
                }, new double[] {
                    d
                }, new double[] {
                    d1
                })
            }, {
                new JSeparator()
            }, {
                axisLabelPane
            }, {
                new JSeparator()
            }, {
                new UILabel(Inter.getLocText(new String[] {
                    "Value", "Define"
                }))
            }, {
                dataPane
            }
        };
        return TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
    }

    private JPanel getPaneWithOutLineStyle()
    {
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d1
        };
        double ad1[] = {
            d, d, d, d, d, d, d, d, d, d, 
            d, d, d
        };
        Component acomponent[][] = {
            {
                getAxisTitlePane()
            }, {
                new JSeparator()
            }, {
                axisReversed
            }, {
                new JSeparator()
            }, {
                TableLayoutHelper.createTableLayoutPane4Chart(new String[] {
                    "ChartF-Units"
                }, new Component[][] {
                    new Component[] {
                        unitCombox
                    }
                }, new double[] {
                    d
                }, new double[] {
                    d1
                })
            }, {
                new JSeparator()
            }, {
                TableLayoutHelper.createTableLayoutPane4Chart(new String[] {
                    "Data_Type"
                }, new Component[][] {
                    new Component[] {
                        formatPane
                    }
                }, new double[] {
                    d
                }, new double[] {
                    d1
                })
            }, {
                new JSeparator()
            }, {
                axisLabelPane
            }, {
                new JSeparator()
            }, {
                new UILabel(Inter.getLocText(new String[] {
                    "Value", "Define"
                }))
            }, {
                dataPane
            }
        };
        return TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
    }

    private JPanel createDataDefinePane()
    {
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            46D, d, d1
        };
        double ad1[] = {
            d, d
        };
        Component acomponent[][] = {
            {
                null, initMinMaxValue(), null
            }, {
                null, addLogarithmicPane2ValuePane(), addLogText()
            }
        };
        return TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
    }

    protected JPanel initMinMaxValue()
    {
        if(minValuePane == null)
            minValuePane = new MinMaxValuePane();
        return minValuePane;
    }

    protected JPanel addLogarithmicPane2ValuePane()
    {
        JPanel jpanel = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
        jpanel.add(logBox = new UICheckBox((new StringBuilder()).append(Inter.getLocText("Logarithmic")).append(":").toString()));
        jpanel.add(new UILabel(Inter.getLocText("Chart_Log_Base")));
        logBaseField = new UITextField(4);
        logBaseField.setText("10");
        logBaseField.setPreferredSize(new Dimension(20, 20));
        logBox.addActionListener(new ActionListener() {

            final ChartValuePane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                checkLogUse();
            }

            
            {
                this$0 = ChartValuePane.this;
                super();
            }
        }
);
        ChartSwingUtils.addListener(logBox, logBaseField);
        return jpanel;
    }

    private JPanel addLogText()
    {
        JPanel jpanel = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
        if(logBaseField != null)
            jpanel.add(logBaseField);
        return jpanel;
    }

    protected JPanel getAxisTitlePane()
    {
        return axisTitlePane;
    }

    protected void updateAxisTitle(Axis axis)
    {
        axisTitlePane.update(axis);
    }

    protected void populateAxisTitle(Axis axis)
    {
        axisTitlePane.populate(axis);
    }

    protected JPanel aliagnZero4Second()
    {
        return null;
    }

    public String title4PopupWindow()
    {
        return Inter.getLocText("Chart_F_Radar_Axis");
    }

    public void updateBean(Axis axis, Plot plot)
    {
        updateBean(axis);
        if(plot.isSupportAxisReverse())
            if(axis.hasAxisReversed())
            {
                if((plot instanceof Bar2DPlot) && ((Bar2DPlot)plot).isHorizontal())
                    plot.getxAxis().setPosition(4);
                else
                    plot.getxAxis().setPosition(1);
            } else
            if((plot instanceof Bar2DPlot) && ((Bar2DPlot)plot).isHorizontal())
                plot.getxAxis().setPosition(2);
            else
                plot.getxAxis().setPosition(3);
    }

    public void updateBean(Axis axis)
    {
        if(axis instanceof NumberAxis)
        {
            NumberAxis numberaxis = (NumberAxis)axis;
            axisLineStylePane.update(axis);
            axis.setAxisReversed(axisReversed.isSelected());
            String s = Utils.objectToString(unitCombox.getSelectedItem());
            if(ComparatorUtils.equals(s, ChartConstants.UNIT_I18N_VALUES[0]))
                s = null;
            numberaxis.setShowUnit(ChartConstants.getUnitValueFromKey(s));
            if(numberaxis.isSurpportAxisTitle())
                updateAxisTitle(numberaxis);
            axisLabelPane.update(axis);
            axis.setFormat(formatPane.update());
            if(minValuePane != null)
                minValuePane.update(axis);
            updateLog(numberaxis);
        }
    }

    private void updateLog(NumberAxis numberaxis)
    {
        if(logBaseField != null && logBox.isSelected())
        {
            String s = logBaseField.getText();
            if(StringUtils.isEmpty(s))
            {
                numberaxis.setLog(false);
                numberaxis.setLogBase(null);
            } else
            {
                numberaxis.setLog(true);
                Formula formula = new Formula(s);
                Number number = ChartBaseUtils.formula2Number(formula);
                if(number != null && number.doubleValue() <= 1.0D)
                    numberaxis.setLogBase(new Formula("2"));
                else
                    numberaxis.setLogBase(formula);
            }
        } else
        {
            numberaxis.setLog(false);
        }
    }

    public void populateBean(Axis axis, Plot plot)
    {
        if(!plot.isSupportAxisReverse())
            relayoutNoAxisRevser();
        populateBean(axis);
    }

    private void relayoutNoAxisRevser()
    {
        removeAll();
        JPanel jpanel = isSupportLineStyle() ? getPaneWithOutAxisRevert() : getPaneWithOutAxisRevertAndLineStyle();
        if(jpanel != null)
            reloaPane(jpanel);
    }

    private JPanel getPaneWithOutAxisRevert()
    {
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d1
        };
        double ad1[] = {
            d, d, d, d, d, d, d, d, d, d, 
            d, d, d, d, d
        };
        Component acomponent[][] = {
            {
                getAxisTitlePane()
            }, {
                new JSeparator()
            }, {
                axisLineStylePane
            }, {
                zeroPane
            }, {
                new JSeparator()
            }, {
                TableLayoutHelper.createTableLayoutPane4Chart(new String[] {
                    "ChartF-Units"
                }, new Component[][] {
                    new Component[] {
                        unitCombox
                    }
                }, new double[] {
                    d
                }, new double[] {
                    d1
                })
            }, {
                new JSeparator()
            }, {
                TableLayoutHelper.createTableLayoutPane4Chart(new String[] {
                    "Data_Type"
                }, new Component[][] {
                    new Component[] {
                        formatPane
                    }
                }, new double[] {
                    d
                }, new double[] {
                    d1
                })
            }, {
                new JSeparator()
            }, {
                axisLabelPane
            }, {
                new JSeparator()
            }, {
                new UILabel(Inter.getLocText(new String[] {
                    "Value", "Define"
                }))
            }, {
                dataPane
            }
        };
        return TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
    }

    private JPanel getPaneWithOutAxisRevertAndLineStyle()
    {
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d1
        };
        double ad1[] = {
            d, d, d, d, d, d, d, d, d, d, 
            d, d
        };
        Component acomponent[][] = {
            {
                getAxisTitlePane()
            }, {
                new JSeparator()
            }, {
                TableLayoutHelper.createTableLayoutPane4Chart(new String[] {
                    "ChartF-Units"
                }, new Component[][] {
                    new Component[] {
                        unitCombox
                    }
                }, new double[] {
                    d
                }, new double[] {
                    d1
                })
            }, {
                new JSeparator()
            }, {
                TableLayoutHelper.createTableLayoutPane4Chart(new String[] {
                    "Data_Type"
                }, new Component[][] {
                    new Component[] {
                        formatPane
                    }
                }, new double[] {
                    d
                }, new double[] {
                    d1
                })
            }, {
                new JSeparator()
            }, {
                axisLabelPane
            }, {
                new JSeparator()
            }, {
                new UILabel(Inter.getLocText(new String[] {
                    "Value", "Define"
                }))
            }, {
                dataPane
            }
        };
        return TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
    }

    public void populateBean(Axis axis)
    {
        if(axis instanceof NumberAxis)
        {
            NumberAxis numberaxis = (NumberAxis)axis;
            axisLineStylePane.populate(axis);
            axisReversed.setSelected(axis.hasAxisReversed());
            String s = numberaxis.getShowUnit();
            if(StringUtils.isBlank(s))
                s = ChartConstants.UNIT_I18N_KEYS[0];
            unitCombox.setSelectedItem(ChartConstants.getUnitKey2Value(s));
            if(numberaxis.isSurpportAxisTitle())
                populateAxisTitle(axis);
            axisLabelPane.populate(axis);
            formatPane.populateBean(axis.getFormat());
            if(minValuePane != null)
                minValuePane.populate(axis);
            if(logBox != null && logBaseField != null)
            {
                logBox.setSelected(numberaxis.isLog());
                if(numberaxis.getLogBase() != null)
                    logBaseField.setText(numberaxis.getLogBase().toString());
            }
        }
        checkLogUse();
    }

    private void checkLogUse()
    {
        if(logBaseField != null && logBox != null)
            logBaseField.setEnabled(logBox.isSelected());
    }

    protected boolean isSupportLineStyle()
    {
        return true;
    }

    public volatile void updateBean(Object obj)
    {
        updateBean((Axis)obj);
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((Axis)obj);
    }

}
