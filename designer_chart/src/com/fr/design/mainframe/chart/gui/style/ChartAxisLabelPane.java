package com.fr.design.mainframe.chart.gui.style;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JPanel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fr.base.Formula;
import com.fr.base.Style;
import com.fr.base.Utils;
import com.fr.chart.base.ChartBaseUtils;
import com.fr.chart.base.TextAttr;
import com.fr.chart.chartattr.Axis;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.gui.style.NumberDragBar;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.dialog.BasicPane;
import com.fr.general.Inter;

public class ChartAxisLabelPane extends BasicPane implements UIObserver{
    private static final int LABEL_INTERVAL = 0;
    private static final int LABEL_WRAP =1;

    private static final long serialVersionUID = 6601571951210596823L;
    private static final int NUM90 = 90;
    private UICheckBox isLabelShow;
    private UIComboBox showWay;//��ʾ��ʽ
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
    private ActionListener autoActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            auto.removeActionListener(autoActionListener);
            custom.removeActionListener(customActionListener);
            auto.setSelected(true);
            custom.setSelected(false);
            customLabelSamleTime.setEnabled(false);
            auto.addActionListener(autoActionListener);
            custom.addActionListener(customActionListener);
        }
    };

    private ActionListener customActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            auto.removeActionListener(autoActionListener);
            custom.removeActionListener(customActionListener);
            auto.setSelected(false);
            custom.setSelected(true);
            customLabelSamleTime.setEnabled(true);
            customLabelSamleTime.setText("1");
            auto.addActionListener(autoActionListener);
            custom.addActionListener(customActionListener);
        }
    };

    public ChartAxisLabelPane() {
        initComponents();
    }

    private void initComponents() {
        isLabelShow = new UICheckBox(Inter.getLocText("FR-Utils_Label"));
        auto = new UICheckBox(Inter.getLocText(new String[]{"FR-App-All_Auto", "FR-Chart-Axis_labelInterval"}));
        custom = new UICheckBox(Inter.getLocText(new String[]{"FR-App-All_Custom", "FR-Chart-Axis_labelInterval"}));
        showWay = new UIComboBox(new String[]{Inter.getLocText("FR-Chart-Axis_labelInterval"),Inter.getLocText("FR-Chart-Axis_labelWrap")});
        customLabelSamleTime = new UITextField();

        String[] nameObjects = {Inter.getLocText("FR-Chart_All_Normal"), Inter.getLocText("FR-Chart-Text_Vertical"), Inter.getLocText("FR-Chart-Text_Rotation")};
        labelOrientationChoose = new UIComboBox(nameObjects);
        orientationBar = new NumberDragBar(-NUM90, NUM90);
        orientationSpinner = new UIBasicSpinner(new SpinnerNumberModel(0, -NUM90, NUM90, 1));

        String[] sampleType = {Inter.getLocText("FR-App-All_Auto"), Inter.getLocText("FR-App-All_Custom")};
        labelSampleChoose = new UIComboBox(sampleType);
        customLabelSamleTime = new UITextField();
        checkCustomSampleField();

        checkOrientationField();

        textAttrPane = new ChartTextAttrPane();
        this.setLayout(new BorderLayout());

        initListener();
    }

    private void initListener(){

        showWay.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                checkShowWay();
            }
        });

        orientationSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                orientationBar.setValue((Integer) orientationSpinner.getValue());

            }
        });
        orientationBar.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                orientationSpinner.setValue((Integer) orientationBar.getValue());

            }
        });


        labelSampleChoose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkCustomSampleField();
            }
        });

        auto.addActionListener(autoActionListener);

        custom.addActionListener(customActionListener);
        labelOrientationChoose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkOrientationField();
            }
        });
        isLabelShow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                checkLabelUse();
            }
        });
    }

    private JPanel getWrapShowWayPane() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {LayoutConstants.CHART_ATTR_TOMARGIN, p, p, f};
        double[] rowSize = {p, p, p, p};

        Component[][] components = new Component[][]{
                new Component[]{null, new UILabel(Inter.getLocText("FR-Chart-Axis_labelShowway")), showWay, null},
                new Component[]{null, new UILabel(Inter.getLocText("StyleAlignment-Text_Rotation")), labelOrientationChoose, null},
                new Component[]{null, orientationSpinner, orientationBar, null},
                new Component[]{null, textAttrPane, null, null},
        };
        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
    }

    private JPanel getIntervalShowWayPane() {

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {f};
        double[] rowSize = {p, p, p};

        Component[][] components = new Component[][]{
                new Component[]{getCombox()},
                new Component[]{getTowChoose()},
                new Component[]{getOther()}
        };
        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
    }

    private JPanel getTowChoose() {
        auto.registerChangeListener(this.observerListener);
        custom.registerChangeListener(this.observerListener);
        customLabelSamleTime.registerChangeListener(this.observerListener);
        auto.setSelected(true);
        custom.setSelected(false);
        customLabelSamleTime.setVisible(true);
        customLabelSamleTime.setEnabled(false);
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {LayoutConstants.CHART_ATTR_TOMARGIN, f};
        double[] rowSize = {p, p};

        JPanel customPane = TableLayoutHelper.createTableLayoutPane(new Component[][]{
                new Component[]{custom, customLabelSamleTime, null},
        }, new double[]{p}, new double[]{p, p, f});

        Component[][] components = new Component[][]{
                new Component[]{null, auto,},
                new Component[]{null, customPane}
        };
        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
    }

    private JPanel getCombox() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {LayoutConstants.CHART_ATTR_TOMARGIN, p, p, f};
        double[] rowSize = {p};

        Component[][] components = new Component[][]{
                new Component[]{null, new UILabel(Inter.getLocText("FR-Chart-Axis_labelShowway")), showWay, null}
        };
        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
    }

    private JPanel getOther() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {LayoutConstants.CHART_ATTR_TOMARGIN, p, p, f};
        double[] rowSize = {p, p, p};

        Component[][] components = new Component[][]{
                new Component[]{null, new UILabel(Inter.getLocText("StyleAlignment-Text_Rotation")), labelOrientationChoose, null},
                new Component[]{null, orientationSpinner, orientationBar, null},
                new Component[]{null, textAttrPane, null, null},
        };
        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
    }

    private void checkShowWay() {
        if (showWayPane != null) {
            this.remove(showWayPane);
        }
        showWay.registerChangeListener(this.observerListener);
        orientationSpinner.registerChangeListener(this.observerListener);
        labelOrientationChoose.registerChangeListener(this.observerListener);
        if (showWay.getSelectedIndex() == LABEL_INTERVAL) {
            labelPane = getIntervalShowWayPane();
        } else {
            labelPane = getWrapShowWayPane();
        }
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] row = {p, p};
        double[] col = {f};
        showWayPane = TableLayoutHelper.createTableLayoutPane(new Component[][]{
                new Component[]{isLabelShow}, new Component[]{labelPane}}, row, col);
        this.add(showWayPane, BorderLayout.CENTER);
        this.validate();
    }



    private void checkOrientationField() {
        if (labelOrientationChoose.getSelectedIndex() != 2) {
            orientationBar.setVisible(false);
            orientationSpinner.setVisible(false);
        } else {
            orientationBar.setVisible(true);
            orientationSpinner.setVisible(true);
        }
    }

    private void checkPaneOnlyInterval(){
        if (showWayPane != null) {
            this.remove(showWayPane);
        }
        showWayPane = getPanel();
        this.add(showWayPane, BorderLayout.CENTER);
        this.validate();
    }


    private JPanel getPanel() {
        labelSampleChoose.registerChangeListener(this.observerListener);
        customLabelSamleTime.registerChangeListener(this.observerListener);
        orientationSpinner.registerChangeListener(this.observerListener);
        labelOrientationChoose.registerChangeListener(this.observerListener);
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {LayoutConstants.CHART_ATTR_TOMARGIN, p, p, f};
        double[] rowSize = {p, p, p, p};

        Component[][] components = new Component[][]{
                new Component[]{null,new UILabel(Inter.getLocText("ChartF-Label_Interval")), labelSampleChoose, customLabelSamleTime},
                new Component[]{null,new UILabel(Inter.getLocText("StyleAlignment-Text_Rotation")), labelOrientationChoose, null},
                new Component[]{null,orientationSpinner, orientationBar, null},
                new Component[]{null,textAttrPane, null, null},
        };
        labelPane = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);

        double[] row = {p,p};
        double[] col = {f};
        return TableLayoutHelper.createTableLayoutPane(new Component[][]{
        			new Component[]{isLabelShow}, new Component[]{labelPane}}, row, col);
    }

    private void checkCustomSampleField() {
        if (labelSampleChoose.getSelectedIndex() == 0) {
            customLabelSamleTime.setVisible(false);
        } else {
            customLabelSamleTime.setVisible(true);
            customLabelSamleTime.setText("1");
        }
    }


    /**
     * �����ǩ
     */
    protected String title4PopupWindow() {
        return Inter.getLocText("ChartF-Tick_Label");
    }

    private int getLabelTextOrientationIndex(TextAttr textAttr) {
        if (textAttr.getAlignText() == Style.VERTICALTEXT) {
            return 1;
        } else if (textAttr.getRotation() != 0) {
            return 2;
        } else {
            return 0;
        }
    }

    private int getLabelSampleNumber(Formula formula) {
        Number number = ChartBaseUtils.formula2Number(formula);
        if (number != null) {
            int num = number.intValue();
            return num < 1 ? 0 : 1;
        }
        return 0;
    }

    private boolean isWrapShow(Formula formula){
        Number number = ChartBaseUtils.formula2Number(formula);
        return number == null;
    }

    /**
     * ��� ��ǩ�����Ƿ����.
     */
    private void checkLabelUse() {
        isLabelShow.setEnabled(true);
        labelPane.setVisible(isLabelShow.isSelected());
    }

    @Override
    public Dimension getPreferredSize() {
        if (isLabelShow.isSelected()) {
            return super.getPreferredSize();
        } else {
            return this.isLabelShow.getPreferredSize();
        }
    }

    /**
     * ���½�������.
     */
    public void populate(Axis axis) {
        if(axis.isSupportAxisLabelWrap()){
            checkShowWay();
            if (isWrapShow(axis.getLabelNumber())) {
                showWay.setSelectedIndex(LABEL_WRAP);
            } else {
                showWay.setSelectedIndex(LABEL_INTERVAL);
                boolean isAuto = getLabelSampleNumber(axis.getLabelNumber()) == 0;
                auto.setSelected(isAuto);
                custom.setSelected(!isAuto);
                customLabelSamleTime.setText(isAuto ? "" : String.valueOf(ChartBaseUtils.formula2Number(axis.getLabelNumber())));
                customLabelSamleTime.setEnabled(!isAuto);
            }
        }else{
            checkPaneOnlyInterval();
            labelSampleChoose.setSelectedIndex(getLabelSampleNumber(axis.getLabelNumber()));
            if (labelSampleChoose.getSelectedIndex() == 1) {
                customLabelSamleTime.setText(String.valueOf(ChartBaseUtils.formula2Number(axis.getLabelNumber())));
            }
        }
        isLabelShow.setSelected(axis.isShowAxisLabel());
        textAttrPane.populate(axis.getTextAttr());
        labelOrientationChoose.setSelectedIndex(getLabelTextOrientationIndex(axis.getTextAttr()));
        orientationBar.setValue(axis.getTextAttr().getRotation());
        orientationSpinner.setValue(axis.getTextAttr().getRotation());
        checkLabelUse();
        this.initSelfListener(this);
    }


    /**
     * �����������.
     */
    public void update(Axis axis) {
        axis.setShowAxisLabel(isLabelShow.isSelected());
        if (isLabelShow.isSelected()) {
            if(axis.isSupportAxisLabelWrap()){
                this.update4Wrap(axis);
             }else{
                update4Normal(axis);
            }

            TextAttr textAttr = axis.getTextAttr();
            textAttrPane.update(textAttr);
            if (labelOrientationChoose.getSelectedIndex() == 0) {
                textAttr.setRotation(0);
                textAttr.setAlignText(Style.HORIZONTALTEXT);
            } else if (labelOrientationChoose.getSelectedIndex() == 1) {
                textAttr.setRotation(0);
                textAttr.setAlignText(Style.VERTICALTEXT);
            } else {
                textAttr.setAlignText(Style.HORIZONTALTEXT);
                textAttr.setRotation(Utils.objectToNumber(orientationSpinner.getValue(), false).intValue());
            }
        }
    }

    private void update4Normal(Axis axis){
        if (labelSampleChoose.getSelectedIndex() == 0) {
            axis.setLabelIntervalNumber(new Formula("0"));
        } else {
            axis.setLabelIntervalNumber(new Formula(customLabelSamleTime.getText()));
        }
    }

    private void update4Wrap(Axis axis){
        if (showWay.getSelectedIndex() == LABEL_WRAP) {
             axis.setLabelIntervalNumber(new Formula());
         } else if (showWay.getSelectedIndex() == LABEL_INTERVAL) {
             if (auto.isSelected()) {
                 axis.setLabelIntervalNumber(new Formula("0"));
             } else if (custom.isSelected()) {
                 axis.setLabelIntervalNumber(new Formula(customLabelSamleTime.getText()));
             }
         }
    }

    /**
     * ע��ʱ�������
     * @param listener �۲��߼����¼�
     */
    public void registerChangeListener(UIObserverListener listener) {
        this.observerListener = listener;
    }

    private void initSelfListener(Container parentComponent) {
        for (int i = 0; i < parentComponent.getComponentCount(); i++) {
            Component tmpComp = parentComponent.getComponent(i);
            if (tmpComp instanceof Container) {
                initSelfListener((Container) tmpComp);
            }
            if (tmpComp instanceof UIObserver) {
                ((UIObserver) tmpComp).registerChangeListener(observerListener);
            }
        }
    }

    /**
     * �Ƿ���Ӧ�¼�����
     * @return ���򷵻�true
     */
    public boolean shouldResponseChangeListener() {
        return true;
    }
}
