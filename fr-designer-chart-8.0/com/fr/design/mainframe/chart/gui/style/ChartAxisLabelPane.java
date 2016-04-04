// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui.style;

import com.fr.base.Formula;
import com.fr.base.Utils;
import com.fr.chart.base.ChartBaseUtils;
import com.fr.chart.base.TextAttr;
import com.fr.chart.chartattr.Axis;
import com.fr.design.dialog.BasicPane;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.gui.style.NumberDragBar;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JPanel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

// Referenced classes of package com.fr.design.mainframe.chart.gui.style:
//            ChartTextAttrPane

public class ChartAxisLabelPane extends BasicPane
    implements UIObserver
{

    private static final int LABEL_INTERVAL = 0;
    private static final int LABEL_WRAP = 1;
    private static final long serialVersionUID = 0x5b9d7eb6871709d7L;
    private static final int NUM90 = 90;
    private UICheckBox isLabelShow;
    private UIComboBox showWay;
    private UITextField customLabelSamleTime;
    private UIComboBox labelOrientationChoose;
    private NumberDragBar orientationBar;
    private UIBasicSpinner orientationSpinner;
    private ChartTextAttrPane textAttrPane;
    private UICheckBox auto;
    private UICheckBox custom;
    private UIComboBox labelSampleChoose;
    private JPanel labelPane;
    private JPanel showWayPane;
    private UIObserverListener observerListener;
    private ActionListener autoActionListener;
    private ActionListener customActionListener;

    public ChartAxisLabelPane()
    {
        autoActionListener = new ActionListener() {

            final ChartAxisLabelPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                auto.removeActionListener(autoActionListener);
                custom.removeActionListener(customActionListener);
                auto.setSelected(true);
                custom.setSelected(false);
                customLabelSamleTime.setEnabled(false);
                auto.addActionListener(autoActionListener);
                custom.addActionListener(customActionListener);
            }

            
            {
                this$0 = ChartAxisLabelPane.this;
                super();
            }
        }
;
        customActionListener = new ActionListener() {

            final ChartAxisLabelPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                auto.removeActionListener(autoActionListener);
                custom.removeActionListener(customActionListener);
                auto.setSelected(false);
                custom.setSelected(true);
                customLabelSamleTime.setEnabled(true);
                customLabelSamleTime.setText("1");
                auto.addActionListener(autoActionListener);
                custom.addActionListener(customActionListener);
            }

            
            {
                this$0 = ChartAxisLabelPane.this;
                super();
            }
        }
;
        initComponents();
    }

    private void initComponents()
    {
        isLabelShow = new UICheckBox(Inter.getLocText("FR-Utils_Label"));
        auto = new UICheckBox(Inter.getLocText(new String[] {
            "FR-App-All_Auto", "FR-Chart-Axis_labelInterval"
        }));
        custom = new UICheckBox(Inter.getLocText(new String[] {
            "FR-App-All_Custom", "FR-Chart-Axis_labelInterval"
        }));
        showWay = new UIComboBox(new String[] {
            Inter.getLocText("FR-Chart-Axis_labelInterval"), Inter.getLocText("FR-Chart-Axis_labelWrap")
        });
        customLabelSamleTime = new UITextField();
        String as[] = {
            Inter.getLocText("FR-Chart_All_Normal"), Inter.getLocText("FR-Chart-Text_Vertical"), Inter.getLocText("FR-Chart-Text_Rotation")
        };
        labelOrientationChoose = new UIComboBox(as);
        orientationBar = new NumberDragBar(-90, 90);
        orientationSpinner = new UIBasicSpinner(new SpinnerNumberModel(0, -90, 90, 1));
        String as1[] = {
            Inter.getLocText("FR-App-All_Auto"), Inter.getLocText("FR-App-All_Custom")
        };
        labelSampleChoose = new UIComboBox(as1);
        customLabelSamleTime = new UITextField();
        checkCustomSampleField();
        checkOrientationField();
        textAttrPane = new ChartTextAttrPane();
        setLayout(new BorderLayout());
        initListener();
    }

    private void initListener()
    {
        showWay.addItemListener(new ItemListener() {

            final ChartAxisLabelPane this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                checkShowWay();
            }

            
            {
                this$0 = ChartAxisLabelPane.this;
                super();
            }
        }
);
        orientationSpinner.addChangeListener(new ChangeListener() {

            final ChartAxisLabelPane this$0;

            public void stateChanged(ChangeEvent changeevent)
            {
                orientationBar.setValue(((Integer)orientationSpinner.getValue()).intValue());
            }

            
            {
                this$0 = ChartAxisLabelPane.this;
                super();
            }
        }
);
        orientationBar.addChangeListener(new ChangeListener() {

            final ChartAxisLabelPane this$0;

            public void stateChanged(ChangeEvent changeevent)
            {
                orientationSpinner.setValue(Integer.valueOf(orientationBar.getValue()));
            }

            
            {
                this$0 = ChartAxisLabelPane.this;
                super();
            }
        }
);
        labelSampleChoose.addActionListener(new ActionListener() {

            final ChartAxisLabelPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                checkCustomSampleField();
            }

            
            {
                this$0 = ChartAxisLabelPane.this;
                super();
            }
        }
);
        auto.addActionListener(autoActionListener);
        custom.addActionListener(customActionListener);
        labelOrientationChoose.addActionListener(new ActionListener() {

            final ChartAxisLabelPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                checkOrientationField();
            }

            
            {
                this$0 = ChartAxisLabelPane.this;
                super();
            }
        }
);
        isLabelShow.addActionListener(new ActionListener() {

            final ChartAxisLabelPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                checkLabelUse();
            }

            
            {
                this$0 = ChartAxisLabelPane.this;
                super();
            }
        }
);
    }

    private JPanel getWrapShowWayPane()
    {
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            46D, d, d, d1
        };
        double ad1[] = {
            d, d, d, d
        };
        Component acomponent[][] = {
            {
                null, new UILabel(Inter.getLocText("FR-Chart-Axis_labelShowway")), showWay, null
            }, {
                null, new UILabel(Inter.getLocText("StyleAlignment-Text_Rotation")), labelOrientationChoose, null
            }, {
                null, orientationSpinner, orientationBar, null
            }, {
                null, textAttrPane, null, null
            }
        };
        return TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
    }

    private JPanel getIntervalShowWayPane()
    {
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d1
        };
        double ad1[] = {
            d, d, d
        };
        Component acomponent[][] = {
            {
                getCombox()
            }, {
                getTowChoose()
            }, {
                getOther()
            }
        };
        return TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
    }

    private JPanel getTowChoose()
    {
        auto.registerChangeListener(observerListener);
        custom.registerChangeListener(observerListener);
        customLabelSamleTime.registerChangeListener(observerListener);
        auto.setSelected(true);
        custom.setSelected(false);
        customLabelSamleTime.setVisible(true);
        customLabelSamleTime.setEnabled(false);
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            46D, d1
        };
        double ad1[] = {
            d, d
        };
        JPanel jpanel = TableLayoutHelper.createTableLayoutPane(new Component[][] {
            new Component[] {
                custom, customLabelSamleTime, null
            }
        }, new double[] {
            d
        }, new double[] {
            d, d, d1
        });
        Component acomponent[][] = {
            {
                null, auto
            }, {
                null, jpanel
            }
        };
        return TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
    }

    private JPanel getCombox()
    {
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            46D, d, d, d1
        };
        double ad1[] = {
            d
        };
        Component acomponent[][] = {
            {
                null, new UILabel(Inter.getLocText("FR-Chart-Axis_labelShowway")), showWay, null
            }
        };
        return TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
    }

    private JPanel getOther()
    {
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            46D, d, d, d1
        };
        double ad1[] = {
            d, d, d
        };
        Component acomponent[][] = {
            {
                null, new UILabel(Inter.getLocText("StyleAlignment-Text_Rotation")), labelOrientationChoose, null
            }, {
                null, orientationSpinner, orientationBar, null
            }, {
                null, textAttrPane, null, null
            }
        };
        return TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
    }

    private void checkShowWay()
    {
        if(showWayPane != null)
            remove(showWayPane);
        showWay.registerChangeListener(observerListener);
        orientationSpinner.registerChangeListener(observerListener);
        labelOrientationChoose.registerChangeListener(observerListener);
        if(showWay.getSelectedIndex() == 0)
            labelPane = getIntervalShowWayPane();
        else
            labelPane = getWrapShowWayPane();
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d, d
        };
        double ad1[] = {
            d1
        };
        showWayPane = TableLayoutHelper.createTableLayoutPane(new Component[][] {
            new Component[] {
                isLabelShow
            }, new Component[] {
                labelPane
            }
        }, ad, ad1);
        add(showWayPane, "Center");
        validate();
    }

    private void checkOrientationField()
    {
        if(labelOrientationChoose.getSelectedIndex() != 2)
        {
            orientationBar.setVisible(false);
            orientationSpinner.setVisible(false);
        } else
        {
            orientationBar.setVisible(true);
            orientationSpinner.setVisible(true);
        }
    }

    private void checkPaneOnlyInterval()
    {
        if(showWayPane != null)
            remove(showWayPane);
        showWayPane = getPanel();
        add(showWayPane, "Center");
        validate();
    }

    private JPanel getPanel()
    {
        labelSampleChoose.registerChangeListener(observerListener);
        customLabelSamleTime.registerChangeListener(observerListener);
        orientationSpinner.registerChangeListener(observerListener);
        labelOrientationChoose.registerChangeListener(observerListener);
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            46D, d, d, d1
        };
        double ad1[] = {
            d, d, d, d
        };
        Component acomponent[][] = {
            {
                null, new UILabel(Inter.getLocText("ChartF-Label_Interval")), labelSampleChoose, customLabelSamleTime
            }, {
                null, new UILabel(Inter.getLocText("StyleAlignment-Text_Rotation")), labelOrientationChoose, null
            }, {
                null, orientationSpinner, orientationBar, null
            }, {
                null, textAttrPane, null, null
            }
        };
        labelPane = TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
        double ad2[] = {
            d, d
        };
        double ad3[] = {
            d1
        };
        return TableLayoutHelper.createTableLayoutPane(new Component[][] {
            new Component[] {
                isLabelShow
            }, new Component[] {
                labelPane
            }
        }, ad2, ad3);
    }

    private void checkCustomSampleField()
    {
        if(labelSampleChoose.getSelectedIndex() == 0)
        {
            customLabelSamleTime.setVisible(false);
        } else
        {
            customLabelSamleTime.setVisible(true);
            customLabelSamleTime.setText("1");
        }
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("ChartF-Tick_Label");
    }

    private int getLabelTextOrientationIndex(TextAttr textattr)
    {
        if(textattr.getAlignText() == 1)
            return 1;
        return textattr.getRotation() == 0 ? 0 : 2;
    }

    private int getLabelSampleNumber(Formula formula)
    {
        Number number = ChartBaseUtils.formula2Number(formula);
        if(number != null)
        {
            int i = number.intValue();
            return i >= 1 ? 1 : 0;
        } else
        {
            return 0;
        }
    }

    private boolean isWrapShow(Formula formula)
    {
        Number number = ChartBaseUtils.formula2Number(formula);
        return number == null;
    }

    private void checkLabelUse()
    {
        isLabelShow.setEnabled(true);
        labelPane.setVisible(isLabelShow.isSelected());
    }

    public Dimension getPreferredSize()
    {
        if(isLabelShow.isSelected())
            return super.getPreferredSize();
        else
            return isLabelShow.getPreferredSize();
    }

    public void populate(Axis axis)
    {
        if(axis.isSupportAxisLabelWrap())
        {
            checkShowWay();
            if(isWrapShow(axis.getLabelNumber()))
            {
                showWay.setSelectedIndex(1);
            } else
            {
                showWay.setSelectedIndex(0);
                boolean flag = getLabelSampleNumber(axis.getLabelNumber()) == 0;
                auto.setSelected(flag);
                custom.setSelected(!flag);
                customLabelSamleTime.setText(flag ? "" : String.valueOf(ChartBaseUtils.formula2Number(axis.getLabelNumber())));
                customLabelSamleTime.setEnabled(!flag);
            }
        } else
        {
            checkPaneOnlyInterval();
            labelSampleChoose.setSelectedIndex(getLabelSampleNumber(axis.getLabelNumber()));
            if(labelSampleChoose.getSelectedIndex() == 1)
                customLabelSamleTime.setText(String.valueOf(ChartBaseUtils.formula2Number(axis.getLabelNumber())));
        }
        isLabelShow.setSelected(axis.isShowAxisLabel());
        textAttrPane.populate(axis.getTextAttr());
        labelOrientationChoose.setSelectedIndex(getLabelTextOrientationIndex(axis.getTextAttr()));
        orientationBar.setValue(axis.getTextAttr().getRotation());
        orientationSpinner.setValue(Integer.valueOf(axis.getTextAttr().getRotation()));
        checkLabelUse();
        initSelfListener(this);
    }

    public void update(Axis axis)
    {
        axis.setShowAxisLabel(isLabelShow.isSelected());
        if(isLabelShow.isSelected())
        {
            if(axis.isSupportAxisLabelWrap())
                update4Wrap(axis);
            else
                update4Normal(axis);
            TextAttr textattr = axis.getTextAttr();
            textAttrPane.update(textattr);
            if(labelOrientationChoose.getSelectedIndex() == 0)
            {
                textattr.setRotation(0);
                textattr.setAlignText(0);
            } else
            if(labelOrientationChoose.getSelectedIndex() == 1)
            {
                textattr.setRotation(0);
                textattr.setAlignText(1);
            } else
            {
                textattr.setAlignText(0);
                textattr.setRotation(Utils.objectToNumber(orientationSpinner.getValue(), false).intValue());
            }
        }
    }

    private void update4Normal(Axis axis)
    {
        if(labelSampleChoose.getSelectedIndex() == 0)
            axis.setLabelIntervalNumber(new Formula("0"));
        else
            axis.setLabelIntervalNumber(new Formula(customLabelSamleTime.getText()));
    }

    private void update4Wrap(Axis axis)
    {
        if(showWay.getSelectedIndex() == 1)
            axis.setLabelIntervalNumber(new Formula());
        else
        if(showWay.getSelectedIndex() == 0)
            if(auto.isSelected())
                axis.setLabelIntervalNumber(new Formula("0"));
            else
            if(custom.isSelected())
                axis.setLabelIntervalNumber(new Formula(customLabelSamleTime.getText()));
    }

    public void registerChangeListener(UIObserverListener uiobserverlistener)
    {
        observerListener = uiobserverlistener;
    }

    private void initSelfListener(Container container)
    {
        for(int i = 0; i < container.getComponentCount(); i++)
        {
            Component component = container.getComponent(i);
            if(component instanceof Container)
                initSelfListener((Container)component);
            if(component instanceof UIObserver)
                ((UIObserver)component).registerChangeListener(observerListener);
        }

    }

    public boolean shouldResponseChangeListener()
    {
        return true;
    }











}
