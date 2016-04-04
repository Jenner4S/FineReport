// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style.axis;

import com.fr.chart.chartattr.*;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.style.FormatPane;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.style.*;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.*;

// Referenced classes of package com.fr.design.mainframe.chart.gui.style.axis:
//            ChartAxisUsePane

public class ChartCategoryPane extends ChartAxisUsePane
{
    private class ContentPane extends JPanel
    {

        private static final long serialVersionUID = 0x61f49b42a6b2eab0L;
        final ChartCategoryPane this$0;

        private void initComponents()
        {
            axisTitlePane = new ChartAxisTitlePane();
            axisTitleNoFormulaPane = new ChartAxisTitleNoFormulaPane();
            axisValuePane = new ChartAxisValueTypePane();
            axisLineStylePane = new ChartAxisLineStylePane();
            formatPane = new FormatPane();
            axisLabelPane = new ChartAxisLabelPane();
            axisReversed = new UICheckBox(Inter.getLocText("AxisReversed"));
            setLayout(new BorderLayout());
            add(isSupportLineStyle() ? ((Component) (getPaneWithLineStyle())) : ((Component) (getPaneWithOutLineStyle())), "Center");
        }

        public ContentPane()
        {
            this$0 = ChartCategoryPane.this;
            super();
            initComponents();
        }
    }


    private static final long serialVersionUID = 0x1d15c96bd4b37cb9L;
    protected ChartAxisTitlePane axisTitlePane;
    protected ChartAxisTitleNoFormulaPane axisTitleNoFormulaPane;
    private ChartAxisValueTypePane axisValuePane;
    private ChartAxisLineStylePane axisLineStylePane;
    private FormatPane formatPane;
    private ChartAxisLabelPane axisLabelPane;
    private UICheckBox axisReversed;

    public ChartCategoryPane()
    {
    }

    private JPanel getPaneWithOutLineStyle()
    {
        double d = -2D;
        double d1 = -1D;
        JPanel jpanel = TableLayoutHelper.createTableLayoutPane4Chart(new String[] {
            "Axis", "Type"
        }, new Component[][] {
            new Component[] {
                axisValuePane
            }
        }, new double[] {
            d
        }, new double[] {
            d1
        });
        double ad[] = {
            d1
        };
        double ad1[] = {
            d, d, d, d, d, d, d, d, d, d
        };
        Component acomponent[][] = {
            {
                jpanel
            }, {
                axisValuePane
            }, {
                new JSeparator()
            }, {
                getAxisTitlePane()
            }, {
                new JSeparator()
            }, {
                axisReversed
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
            }
        };
        return TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
    }

    private JPanel getPaneWithLineStyle()
    {
        double d = -2D;
        double d1 = -1D;
        JPanel jpanel = TableLayoutHelper.createTableLayoutPane4Chart(new String[] {
            "Axis", "Type"
        }, new Component[][] {
            new Component[] {
                axisValuePane
            }
        }, new double[] {
            d
        }, new double[] {
            d1
        });
        double ad[] = {
            d1
        };
        double ad1[] = {
            d, d, d, d, d, d, d, d, d, d, 
            d, d
        };
        Component acomponent[][] = {
            {
                jpanel
            }, {
                axisValuePane
            }, {
                new JSeparator()
            }, {
                getAxisTitlePane()
            }, {
                new JSeparator()
            }, {
                axisLineStylePane
            }, {
                new JSeparator()
            }, {
                axisReversed
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
            }
        };
        return TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
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

    public String title4PopupWindow()
    {
        return Inter.getLocText("ChartF-Category_Axis");
    }

    protected JPanel createContentPane()
    {
        return new ContentPane();
    }

    protected void layoutContentPane()
    {
        leftcontentPane = createContentPane();
        leftcontentPane.setBorder(BorderFactory.createMatteBorder(5, 10, 5, 0, original));
        add(leftcontentPane);
    }

    public void updateBean(Axis axis, Plot plot)
    {
        updateBean(axis);
        if(plot.isSupportAxisReverse())
            if(axis.hasAxisReversed())
            {
                if((plot instanceof Bar2DPlot) && ((Bar2DPlot)plot).isHorizontal())
                    plot.getyAxis().setPosition(1);
                else
                    plot.getyAxis().setPosition(4);
            } else
            if((plot instanceof Bar2DPlot) && ((Bar2DPlot)plot).isHorizontal())
                plot.getyAxis().setPosition(3);
            else
                plot.getyAxis().setPosition(2);
    }

    public void updateBean(Axis axis)
    {
        if(axis == null)
            axis = new CategoryAxis();
        axisLineStylePane.update(axis);
        updateAxisTitle(axis);
        if(axis instanceof CategoryAxis)
        {
            CategoryAxis categoryaxis = (CategoryAxis)axis;
            axisValuePane.updateBean(categoryaxis);
            formatPane.setComboBoxModel(categoryaxis.isDate());
        }
        axis.setAxisReversed(axisReversed.isSelected());
        axisLabelPane.update(axis);
        axis.setFormat(formatPane.update());
    }

    public void populateBean(Axis axis)
    {
        if(axis == null)
            return;
        axisLineStylePane.populate(axis);
        populateAxisTitle(axis);
        axisLabelPane.populate(axis);
        axisReversed.setSelected(axis.hasAxisReversed());
        if(axis instanceof CategoryAxis)
        {
            CategoryAxis categoryaxis = (CategoryAxis)axis;
            axisValuePane.populateBean(categoryaxis);
            formatPane.setComboBoxModel(categoryaxis.isDate());
            formatPane.populateBean(categoryaxis.getFormat());
        }
        GUICoreUtils.repaint(this);
    }

    public void populateBean(Axis axis, Plot plot)
    {
        if(plot.isSupportAxisReverse())
        {
            populateBean(axis);
        } else
        {
            relayoutWithPlot(plot);
            populateBean(axis);
        }
    }

    private void relayoutWithPlot(Plot plot)
    {
        removeAll();
        JPanel jpanel = isSupportLineStyle() ? getPaneWithOutAxisRevert() : getPaneWithOutAxisRevertAndLineStyle();
        if(jpanel != null)
            reloaPane(jpanel);
    }

    private JPanel getPaneWithOutAxisRevertAndLineStyle()
    {
        double d = -1D;
        double d1 = -2D;
        JPanel jpanel = TableLayoutHelper.createTableLayoutPane4Chart(new String[] {
            "Axis", "Type"
        }, new Component[][] {
            new Component[] {
                axisValuePane
            }
        }, new double[] {
            d1
        }, new double[] {
            d
        });
        double ad[] = {
            d
        };
        double ad1[] = {
            d1, d1, d1, d1, d1, d1, d1, d1
        };
        Component acomponent[][] = {
            {
                jpanel
            }, {
                axisValuePane
            }, {
                new JSeparator()
            }, {
                getAxisTitlePane()
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
                    d1
                }, new double[] {
                    d
                })
            }, {
                new JSeparator()
            }, {
                axisLabelPane
            }
        };
        return TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
    }

    private JPanel getPaneWithOutAxisRevert()
    {
        double d = -1D;
        double d1 = -2D;
        JPanel jpanel = TableLayoutHelper.createTableLayoutPane4Chart(new String[] {
            "Axis", "Type"
        }, new Component[][] {
            new Component[] {
                axisValuePane
            }
        }, new double[] {
            d1
        }, new double[] {
            d
        });
        double ad[] = {
            d
        };
        double ad1[] = {
            d1, d1, d1, d1, d1, d1, d1, d1, d1, d1
        };
        Component acomponent[][] = {
            {
                jpanel
            }, {
                axisValuePane
            }, {
                new JSeparator()
            }, {
                getAxisTitlePane()
            }, {
                new JSeparator()
            }, {
                axisLineStylePane
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
                    d1
                }, new double[] {
                    d
                })
            }, {
                new JSeparator()
            }, {
                axisLabelPane
            }
        };
        return TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
    }

    public ChartAxisValueTypePane getAxisValueTypePane()
    {
        return axisValuePane;
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
