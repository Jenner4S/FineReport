/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.mainframe;

import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UINumberField;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.scrollruler.*;
import com.fr.general.FRScreen;
import com.fr.general.Inter;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-10-13
 * Time: ����5:08
 */
public class ChartArea extends JComponent implements ScrollRulerComponent {

    private static final int TOPGAP = 8;
    private static final int MIN_WIDTH = 36;
    private static final int MIN_HEIGHT = 21;
    private static final double SLIDER_FLOAT = 120.0;
    private static final double SLIDER_MIN = 60.0;
    private static final double DEFAULT_SLIDER = 100.0;
    private static final int ROTATIONS = 50;
    private int designerwidth = 810;
    private int designerheight = 500;
    private int customWidth = 810;
    private int customHeight = 500;
    private ChartDesigner designer;
    private int horizontalValue = 0;
    private int verticalValue = 0;
    private int verticalMax = 0;
    private int horicalMax = 0;
    private FormScrollBar verScrollBar;
    private FormScrollBar horScrollBar;
    //��ʾ������ͼ������С�Ŀؼ�
    private UINumberField widthPane;
    private UINumberField heightPane;
    private boolean isValid = true;
    private double START_VALUE = DEFAULT_SLIDER;

    public ChartArea(ChartDesigner designer) {
        this(designer, true);
    }

    public ChartArea(ChartDesigner designer, boolean useScrollBar) {
        this.designer = designer;
        this.designer.setParent(this);
        this.customWidth = designer.getTarget().getWidth();
        this.customHeight = designer.getTarget().getHeight();
        this.designerwidth = this.customWidth;
        this.designerheight = this.customHeight;
        isValid = useScrollBar;
        verScrollBar = new FormScrollBar(Adjustable.VERTICAL, this);
        horScrollBar = new FormScrollBar(Adjustable.HORIZONTAL, this);
        if (useScrollBar) {
            this.setLayout(new FormRulerLayout());
            designer.setBorder(new LineBorder(new Color(198, 198, 198)));
            this.add(FormRulerLayout.CENTER, designer);
            addFormSize();
            this.add(FormRulerLayout.VERTICAL, verScrollBar);
            this.add(FormRulerLayout.HIRIZONTAL, horScrollBar);
            enableEvents(AWTEvent.MOUSE_WHEEL_EVENT_MASK);
        } else {
            // �����������ֻҪ��ߺ�����pane
            this.setLayout(new RulerLayout());
            this.add(RulerLayout.CENTER, designer);
            addFormRuler();
        }
        this.setFocusTraversalKeysEnabled(false);
    }

    /**
     * ���ӱ���ҳ���С���ƽ��棬�����ֶ��޸ĺͻ����϶�
     */
    private void addFormSize() {
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double[] rowSize = {f};
        double[] columnSize = {p, f, p, p, p, p, p,f,p};
        UILabel tipsPane = new UILabel("chart");
        tipsPane.setPreferredSize(new Dimension(200, 0));
        widthPane = new UINumberField();
        widthPane.setPreferredSize(new Dimension(60, 0));
        heightPane = new UINumberField();
        heightPane.setPreferredSize(new Dimension(60, 0));
        JPanel panel = new JPanel(){
              public Dimension getPreferredSize(){
                  return new Dimension(200,0);
              }
        };
        JPanel resizePane = TableLayoutHelper.createCommonTableLayoutPane(new JComponent[][]{
                        {tipsPane, new UILabel(), widthPane, new UILabel(Inter.getLocText("Indent-Pixel")), new UILabel("x"),
                                heightPane, new UILabel(Inter.getLocText("Indent-Pixel")),new UILabel(),panel}},
                rowSize, columnSize, 8
        );
        this.add(FormRulerLayout.BOTTOM, resizePane);
        setWidgetsConfig();
        // �ȳ�ʼ�����鼰��Ӧ�¼���Ȼ���ȡ�ֱ��ʵ�����������ʾ��С
        initCalculateSize();
    }

    private void setWidgetsConfig() {
        widthPane.setHorizontalAlignment(widthPane.CENTER);
        heightPane.setHorizontalAlignment(heightPane.CENTER);
        widthPane.setMaxDecimalLength(0);
        heightPane.setMaxDecimalLength(0);
        //�ؼ���ʼֵ���Ǹ��ڵ������ʼ�Ŀ�͸�
        widthPane.setValue(designerwidth);
        heightPane.setValue(designerheight);
        addWidthPaneListener();
        addHeightPaneListener();
    }

    private void initCalculateSize() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension scrnsize = toolkit.getScreenSize();
        double value = FRScreen.getByDimension(scrnsize).getValue();
        if (value != DEFAULT_SLIDER) {
            reCalculateRoot(value, true);
        }
    }

    //���ÿ�ȵĿؼ�����Ӧ�¼�
    private void addWidthPaneListener() {
        widthPane.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        reCalculateWidth((int) ((UINumberField) evt.getSource()).getValue());
                    }
                }
        );
        widthPane.addFocusListener(
                new FocusAdapter() {
                    public void focusLost(FocusEvent e) {
                        // ʧȥ����ʱ��������Ϊ�������
                        reCalculateWidth((int) ((UINumberField) e.getSource()).getValue());
                    }
                }
        );
    }

    private void addHeightPaneListener() {
        heightPane.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        reCalculateHeight((int) ((UINumberField) evt.getSource()).getValue());
                    }
                }
        );
        heightPane.addFocusListener(
                new FocusAdapter() {
                    public void focusLost(FocusEvent e) {
                        // ʧȥ����ʱ��������Ϊ�������
                        reCalculateHeight((int) ((UINumberField) e.getSource()).getValue());
                    }
                }
        );
    }

    private void reCalculateWidth(int width) {
        int dW = width - designerwidth;
        if (dW == 0) {
            return;
        }
        // ͼ������������СΪʵ�ʵĸߺ͵�ǰ�Ŀ�Ȼ�󰴴˵����ڲ������
        designer.setSize(width, designerheight);
        designerwidth = width;
        customWidth = width;
        designer.getTarget().setWidth(width);
        ChartArea.this.validate();
        designer.fireTargetModified();
    }

    private void reCalculateHeight(int height) {
        int dW = height - designerwidth;
        if (dW == 0) {
            return;
        }
        // ͼ������������СΪʵ�ʵĸߺ͵�ǰ�Ŀ�Ȼ�󰴴˵����ڲ������
        designer.setSize(designerwidth, height);
        designerheight = height;
        customHeight = height;
        this.designer.getTarget().setHeight(height);
        ChartArea.this.validate();
        designer.fireTargetModified();
    }

    /**
     * ���ս����С�İٷֱ�ֵ����root��С
     *
     * @param needCalculateParaHeight �Ƿ���Ҫ������������߶�
     * @param value
     */
    private void reCalculateRoot(double value, boolean needCalculateParaHeight) {
        if (value == START_VALUE) {
            return;
        }
        double percent = (value - START_VALUE) / START_VALUE;
        Dimension d = new Dimension(designerwidth, designerheight);
        // ��������Ӧ���ִ�С��ͬ���������������border��С����ʱˢ����formArea
        ChartArea.this.validate();
        START_VALUE = value;
    }

    /**
     * ���ӿ̶���
     */
    public void addFormRuler() {
        BaseRuler vRuler = new VerticalRuler(this);
        BaseRuler hRuler = new HorizontalRuler(this);
        this.add(RulerLayout.VRULER, vRuler);
        this.add(RulerLayout.HRULER, hRuler);
    }

    /**
     * �������¼�
     * ���ڱ���ƽ���Ҫ�� ������С���ڽ���ʱ���������ſ����϶������Բ�֧�ֹ����������¹�
     */
    @Override
    protected void processMouseWheelEvent(java.awt.event.MouseWheelEvent evt) {
        int id = evt.getID();
        switch (id) {
            case MouseEvent.MOUSE_WHEEL: {
                int rotations = evt.getWheelRotation();
                int value = this.verScrollBar.getValue() + rotations * ROTATIONS;
                value = Math.min(value, verticalMax);
                value = Math.max(0, value);
                doLayout(); //��dolayout����Ϊÿ�ι�����Ҫ���� Max�Ĵ�С
                this.verScrollBar.setValue(value);
                break;
            }
        }
    }

    /**
     * ��������
     */
    public void doLayout() {
        layout();
        if (isValid) {
            setScrollBarProperties(customWidth - designer.getWidth(), horScrollBar);
            //���������ֵ��ʱ��Ӧ�����ϲ������ĸ߶�
            setScrollBarProperties(customHeight - designer.getHeight(), verScrollBar);
        }
    }

    /**
     * ���ù�����������
     */
    private void setScrollBarProperties(int value, FormScrollBar bar) {
        if (value <= 0) {
            // �����й�����ʱ���ֶ���С������ȵ������ڣ����ù�����ֵ��max
            setScrollBarMax(0, bar);
            bar.setMaximum(0);
            bar.setValue(0);
            bar.setEnabled(false);
        } else {
            //���������ק������valueһֱΪ��ǰvalue
            int oldValue = verticalValue;
            setScrollBarMax(value, bar);
            bar.setEnabled(true);
            bar.setMaximum(value);
            bar.setValue(value);
            bar.setValue(oldValue);
        }
    }

    private boolean isScrollNotVisible(FormScrollBar bar) {
        if (bar.getOrientation() == Adjustable.VERTICAL) {
            return verticalMax == 0;
        } else {
            return horicalMax == 0;
        }
    }

    private void setScrollBarMax(int max, FormScrollBar bar) {
        if (bar.getOrientation() == Adjustable.VERTICAL) {
            verticalMax = max;
        } else {
            horicalMax = max;
        }
    }

    /**
     * ����designer����С�߶�
     *
     * @return int
     */
    public int getMinHeight() {
        return MIN_HEIGHT;
    }

    /**
     * ����designer����С���
     *
     * @return int
     */
    public int getMinWidth() {
        return MIN_WIDTH;
    }

    /**
     * getRulerLengthUnit
     *
     * @return short
     */
    public short getRulerLengthUnit() {
        return -1;
    }

    /**
     * ����ˮƽ��������value
     *
     * @return int
     */
    public int getHorizontalValue() {
        return horizontalValue;
    }

    /**
     * ����ˮƽ��������value
     *
     * @param newValue
     */
    public void setHorizontalValue(int newValue) {
        this.horizontalValue = newValue;
    }

    /**
     * ������ֱ��������value
     *
     * @return
     */
    public int getVerticalValue() {
        return verticalValue;
    }

    /**
     * ��ֱ��������ֵ
     *
     * @param newValue
     */
    public void setVerticalValue(int newValue) {
        this.verticalValue = newValue;
    }

    /**
     * ���ص�ǰdesigner�ĸ߶�
     *
     * @return height
     */
    public int getDesignerHeight() {
        return designer.getHeight();
    }

    /**
     * ���ص�ǰdesigner�Ŀ��
     *
     * @return
     */
    public int getDesignerWidth() {
        return designer.getWidth();
    }

    /**
     * ���ؿ�ȿؼ���value
     *
     * @return ���
     */
    public double getWidthPaneValue() {
        return widthPane.getValue();
    }

    /**
     * ���ÿ��ֵ
     *
     * @param value ֵ
     */
    public void setWidthPaneValue(int value) {
        widthPane.setValue(value);
    }

    /**
     * ���ø߶�ֵ
     *
     * @param value ֵ
     */
    public void setHeightPaneValue(int value) {
        heightPane.setValue(value);
    }

    /**
     * ���ظ߶ȿؼ���value
     *
     * @return �߶�
     */
    public double getHeightPaneValue() {
        return heightPane.getValue();
    }

    /**
     * ���ؽ��������С
     *
     * @return Dimension
     */
    public Dimension getAreaSize() {
        return new Dimension(horScrollBar.getMaximum(), verScrollBar.getMaximum());
    }

    /**
     * setAreaSize
     *
     * @param totalSize
     * @param horizontalValue
     * @param verticalValue
     */
    public void setAreaSize(Dimension totalSize, int horizontalValue, int verticalValue, double width, double height, double slide) {
        horScrollBar.setMaximum((int) totalSize.getWidth());
        verScrollBar.setMaximum((int) totalSize.getHeight());
        horScrollBar.setValue(horizontalValue);
        verScrollBar.setValue(verticalValue);
        // ������refresh�ײ���������Ҫ����֮ǰ�Ŀ�ߺͰٷֱ�����������size
        if (width != widthPane.getValue()) {
            widthPane.setValue(width);
            reCalculateWidth((int) width);
        }
        if (height != heightPane.getValue()) {
            heightPane.setValue(height);
            reCalculateHeight((int) height);
        }
        // undoʱ������refreshRoot����Ҫ�ٴΰ��հٷֱȵ�����
        START_VALUE = DEFAULT_SLIDER;
        reCalculateRoot(slide, true);
    }

    public int getCustomWidth(){
        return this.customWidth;
    }

    public int getCustomHeight(){
       return this.customHeight;
    }

    /**
     * �����������ֵ��max
     *
     * @param oldmax      ֮ǰ���ֵ
     * @param max         ��ǰ���ֵ
     * @param newValue    ��ǰvalue
     * @param oldValue    ֮ǰvalue
     * @param visi        designer�Ĵ�С
     * @param orientation ����������
     * @return ������ֵ��max
     */
    @Override
    public Point calculateScroll(int oldmax, int max, int newValue, int oldValue, int visi, int orientation) {
        int scrollMax = orientation == 1 ? verticalMax : horicalMax;
        //��ֹ����������Ͷ˻����Լ�������ƶ�(���������Χ����ʱ��newValueҪ�ڷ�Χ֮��)
        if (oldmax == scrollMax + visi && newValue > scrollMax) {
            return new Point(oldValue, oldmax);
        }
        return new Point(newValue, max);
    }

    private class FormRulerLayout extends RulerLayout {
        public FormRulerLayout() {
            super();
        }

        /**
         * ���õ�layout����ǰ����Ҫ���
         */
        public void layoutContainer(Container target) {
            synchronized (target.getTreeLock()) {
                Insets insets = target.getInsets();
                int top = insets.top;
                int left = insets.left;
                int bottom = target.getHeight() - insets.bottom;
                int right = target.getWidth() - insets.right;
                Dimension resize = resizePane.getPreferredSize();
                Dimension hbarPreferredSize = null;
                Dimension vbarPreferredSize = null;

                resizePane.setBounds(left, bottom - resize.height, right, resize.height);
                if (horScrollBar != null) {
                    hbarPreferredSize = horScrollBar.getPreferredSize();
                    vbarPreferredSize = verScrollBar.getPreferredSize();
                    horScrollBar.setBounds(left, bottom - hbarPreferredSize.height - resize.height, right - BARSIZE, hbarPreferredSize.height);
                    verScrollBar.setBounds(right - vbarPreferredSize.width, top, vbarPreferredSize.width, bottom - BARSIZE - resize.height);
                }
                ChartDesigner dg = ((ChartDesigner) designer);
                Rectangle rec = new Rectangle(left + (right - designerwidth) / 2, TOPGAP, right, bottom);
                //�Ƿ�Ϊͼ��
                if (isValid) {
                    int maxHeight = bottom - hbarPreferredSize.height - resize.height - TOPGAP * 2;
                    int maxWidth = right - vbarPreferredSize.width;
                    designerwidth  = designerwidth> maxWidth ? maxWidth : designerwidth;
                    designerheight = designerheight > maxHeight ? maxHeight : designerheight;
                    int designerLeft = left + (verScrollBar.getX() - designerwidth) / 2;
                    rec = new Rectangle(designerLeft, TOPGAP, designerwidth, designerheight);
                }
                // designer����������ƽ����е���岿�֣�Ŀǰֻ������Ӧ���ֺͲ������档
                designer.setBounds(rec);
            }
        }

    }

}
