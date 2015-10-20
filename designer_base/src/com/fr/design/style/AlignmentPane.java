/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.style;

import com.fr.base.BaseUtils;
import com.fr.base.Style;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.AlignmentComboBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.plugin.ExtraClassManager;
import com.fr.stable.CoreConstants;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Pane to edit cell alignment.
 */
public class AlignmentPane extends BasicPane {
    private static final int NUM_90 =90;
    private AlignmentComboBox horAlignmentComboBox;
    private AlignmentComboBox verAlignmentComboBox;
    private UIComboBox textComboBox;
    private static final String[] TEXT = {
            Inter.getLocText("StyleAlignment-Wrap_Text"),
            Inter.getLocText("StyleAlignment-Single_Line"),
            Inter.getLocText("StyleAlignment-Single_Line(Adjust_Font)"),
            Inter.getLocText("StyleAlignment-Multi_Line(Adjust_Font)")};
    private UIBasicSpinner rotationSpinner;
    private UICheckBox isVerticalTextCheckBox;
    private UIRadioButton leftToRightRB;
    private UIRadioButton rightToLeftRB;
    //    private UIComboBox imageComboBox;
    private static final String[] IMAGE = {
            Inter.getLocText("Default"),
            Inter.getLocText("Image-Extend")
    };
    private UIBasicSpinner leftIndentSpinner;
    private UIBasicSpinner rightIndentSpinner;
    
    //james:�м����
    private UIBasicSpinner spacingBeforeSpinner;
    private UIBasicSpinner spacingAfterSpinner;
    private UIBasicSpinner lineSpacingSpinner;

    private EventListenerList eventChangeList = new EventListenerList();
    private ActionListener actionListener1 = new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            if (isVerticalTextCheckBox.isSelected()) {
                rotationSpinner.setEnabled(false);
                leftToRightRB.setEnabled(true);
                rightToLeftRB.setEnabled(true);
            } else {
                rotationSpinner.setEnabled(true);
                leftToRightRB.setEnabled(false);
                rightToLeftRB.setEnabled(false);
            }
        }
    };

    public AlignmentPane() {
        this.initComponents();
    }

    protected void initComponents() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        JPanel contentPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_S_Pane();
        this.add(contentPane, BorderLayout.NORTH);
        contentPane.add(getAlignment());
        contentPane.add(getTextImagePane());
        //richer:�ı����ƺ�ͼƬ����
        JPanel textDirectionPanel = FRGUIPaneFactory.createY_AXISBoxInnerContainer_S_Pane();
        contentPane.add(textDirectionPanel);
        textDirectionPanel.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("StyleAlignment-Text_Rotation"), null));
        JPanel isVerticalTextPanel = FRGUIPaneFactory.createMediumHGapFlowInnerContainer_M_Pane();
        textDirectionPanel.add(isVerticalTextPanel);
        initVerticalPane(isVerticalTextPanel);
        //Rotation.
        JPanel rotationPane = FRGUIPaneFactory.createMediumHGapFlowInnerContainer_M_Pane();
        textDirectionPanel.add(rotationPane);

        rotationPane.add(new UILabel(" " + Inter.getLocText("StyleAlignment-Text_Rotation")));//james:����һ���ո����������˺ÿ�Щ

        rotationSpinner = new UIBasicSpinner(new SpinnerNumberModel(0, -NUM_90, NUM_90, 1));
        rotationSpinner.addChangeListener(changeListener);
        GUICoreUtils.setColumnForSpinner(rotationSpinner, 3);
        rotationPane.add(rotationSpinner);
        rotationPane.add(new UILabel(Inter.getLocText("StyleAlignment-Degrees")));
        rotationPane.add(new UILabel("(" + Inter.getLocText("StyleAlignment-between_-90_and_90") + ")"));
        contentPane.add(getIndentPane());

        //james:�м����
        JPanel spacingPane = new JPanel();
        contentPane.add(spacingPane);
        initSpacingPane(spacingPane);
    }

    private void initVerticalPane(JPanel isVerticalTextPanel){
        isVerticalTextCheckBox = new UICheckBox(Inter.getLocText("StyleAlignment-Vertical_Text"), false);
        isVerticalTextCheckBox.addActionListener(actionListener);
        isVerticalTextCheckBox.addActionListener(actionListener1);
        this.leftToRightRB = new UIRadioButton(Inter.getLocText("StyleAlignment-Left_To_Right"));
        this.leftToRightRB.addActionListener(actionListener);
        this.rightToLeftRB = new UIRadioButton(Inter.getLocText("StyleAlignment-Right_To_Left"));
        this.rightToLeftRB.addActionListener(actionListener);

        ButtonGroup directionBG = new ButtonGroup();
        directionBG.add(leftToRightRB);
        directionBG.add(rightToLeftRB);
        rightToLeftRB.setSelected(true);

        if (ExtraClassManager.getInstance().getVerticalTextProcessor() == null){
            return;
        }
        isVerticalTextPanel.add(isVerticalTextCheckBox);
        isVerticalTextPanel.add(leftToRightRB);
        isVerticalTextPanel.add(rightToLeftRB);
    }

    private void initSpacingPane (JPanel spacingPane) {
        spacingPane.setLayout(new GridLayout(1, 3));
        spacingPane.setBorder(BorderFactory.createTitledBorder(Inter.getLocText("Style-Spacing") + ":"));

        spacingBeforeSpinner = new UIBasicSpinner(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
        spacingBeforeSpinner.addChangeListener(changeListener);
        GUICoreUtils.setColumnForSpinner(spacingBeforeSpinner, 3);
        spacingPane.add(this.createCenterFlowPane(Inter.getLocText("Style-Spacing_Before"), spacingBeforeSpinner));

        spacingAfterSpinner = new UIBasicSpinner(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
        spacingAfterSpinner.addChangeListener(changeListener);
        GUICoreUtils.setColumnForSpinner(spacingAfterSpinner, 3);
        spacingPane.add(this.createCenterFlowPane(Inter.getLocText("Style-Spacing_After"), spacingAfterSpinner));

        lineSpacingSpinner = new UIBasicSpinner(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
        lineSpacingSpinner.addChangeListener(changeListener);
        GUICoreUtils.setColumnForSpinner(lineSpacingSpinner, 3);
        spacingPane.add(this.createCenterFlowPane(Inter.getLocText("Style-Line_Spacing"), lineSpacingSpinner));
    }


    private JPanel getIndentPane() {
        JPanel indentPane = FRGUIPaneFactory.createMediumHGapFlowInnerContainer_M_Pane();

        indentPane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("Sytle-Indentation"), null));
        Comparable maximum = null;
        leftIndentSpinner = new UIBasicSpinner(new SpinnerNumberModel(new Integer(0), new Integer(0), maximum, new Integer(1)));
        leftIndentSpinner.addChangeListener(changeListener);
        GUICoreUtils.setColumnForSpinner(leftIndentSpinner, 3);
        indentPane.add(this.createCenterFlowPane(Inter.getLocText(new String[]{"Style-Left_Indent", "Unit_PT"}, new String[] {"(", ")"}), leftIndentSpinner));

        rightIndentSpinner = new UIBasicSpinner(new SpinnerNumberModel(new Integer(0), new Integer(0), maximum, new Integer(1)));
        rightIndentSpinner.addChangeListener(changeListener);
        GUICoreUtils.setColumnForSpinner(rightIndentSpinner, 3);
        indentPane.add(this.createCenterFlowPane(Inter.getLocText(new String[] {"Style-Right_Indent", "Unit_PT"}, new String[] {"(", ")" }) , rightIndentSpinner));
        return indentPane;
    }

    private JPanel getTextImagePane() {
        JPanel textImagePane = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("StyleAlignment-Text_Style"));

        //textStyle.
        JPanel textStylePane = FRGUIPaneFactory.createMediumHGapFlowInnerContainer_M_Pane();
        //        textImagePane.add(textStylePane);

        textComboBox = new UIComboBox(TEXT);
        textComboBox.addActionListener(actionListener);
        textStylePane.add(new UILabel(Inter.getLocText("StyleAlignment-Text_Style")));
        textStylePane.add(textComboBox);
        textImagePane.add(textStylePane);//���ı��ź�����ÿ���
        return textImagePane;
    }

    private JPanel getAlignment() {
        //algnment.
        JPanel aligmentPane = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("Alignment-Style"));
        JPanel horAlignmentPane = FRGUIPaneFactory.createMediumHGapFlowInnerContainer_M_Pane();
        //        aligmentPane.add(horAlignmentPane);
        horAlignmentComboBox = new AlignmentComboBox(CoreConstants.HORIZONTAL_ALIGNMENT_ARRAY);
        horAlignmentComboBox.addActionListener(actionListener);
        horAlignmentPane.add(new UILabel(Inter.getLocText("StyleAlignment-Horizontal")));
        horAlignmentPane.add(horAlignmentComboBox);

        JPanel verAlignmentPane = FRGUIPaneFactory.createMediumHGapFlowInnerContainer_M_Pane();
        aligmentPane.add(verAlignmentPane);
        aligmentPane.add(horAlignmentPane);
        verAlignmentComboBox = new AlignmentComboBox(CoreConstants.VERTICAL_ALIGNMENT_ARRAY);
        verAlignmentComboBox.addActionListener(actionListener);
        verAlignmentPane.add(new UILabel(Inter.getLocText("StyleAlignment-Vertical")));
        verAlignmentPane.add(verAlignmentComboBox);
        return aligmentPane;
    }

    /**
     * ��ӱ仯�����¼�
     *
     * @param changeListener �仯����
     */
    public void addChangeListener(ChangeListener changeListener) {
        eventChangeList.add(ChangeListener.class, changeListener);
    }

    /**
     * ����״̬ת��
     *
     */
    public void fireStateChanged() {
        Object[] listeners = eventChangeList.getListenerList();
        ChangeEvent e = null;

        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                if (e == null) {
                    e = new ChangeEvent(this);
                }
                ((ChangeListener) listeners[i + 1]).stateChanged(e);
            }
        }
    }

    ActionListener actionListener = new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            fireStateChanged();
        }
    };
    ChangeListener changeListener = new ChangeListener() {

        public void stateChanged(ChangeEvent e) {
            fireStateChanged();
        }
    };

    private JPanel createCenterFlowPane(String name, JComponent comp) {
        JPanel centerFlowPane = FRGUIPaneFactory.createCenterFlowInnerContainer_S_Pane();

        centerFlowPane.add(new UILabel(name));
        centerFlowPane.add(comp);

        return centerFlowPane;
    }

    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("Style");
    }

    /**
     * Populate cellstyle border.
     *
     * @param style the new style.
     */
    public void populate(Style style) {
        this.horAlignmentComboBox.setSelectedAlignment(BaseUtils.getAlignment4Horizontal(style));
        this.verAlignmentComboBox.setSelectedAlignment(style.getVerticalAlignment());

        if (style.getTextStyle() == Style.TEXTSTYLE_SINGLELINE) {
            this.textComboBox.setSelectedIndex(1);
        } else if (style.getTextStyle() == Style.TEXTSTYLE_SINGLELINEADJUSTFONT) {
            this.textComboBox.setSelectedIndex(2);
        } else if (style.getTextStyle() == Style.TEXTSTYLE_MULTILINEADJUSTFONT) {
            this.textComboBox.setSelectedIndex(3);
        } else {
            this.textComboBox.setSelectedIndex(0);
        }

        //james:isVerticalText
        if (style.getVerticalText() == Style.VERTICALTEXT) {

        } else {
            this.isVerticalTextCheckBox.setSelected(false);
            this.leftToRightRB.setEnabled(false);
            this.rightToLeftRB.setEnabled(false);
            this.rotationSpinner.setEnabled(true);
            this.rotationSpinner.setValue(new Integer(style.getRotation()));
        }

        //alex:indent
        this.leftIndentSpinner.setValue(new Integer(style.getPaddingLeft()));
        this.rightIndentSpinner.setValue(new Integer(style.getPaddingRight()));
    
        //james:�м����
        this.spacingBeforeSpinner.setValue(new Integer(style.getSpacingBefore()));
        this.spacingAfterSpinner.setValue(new Integer(style.getSpacingAfter()));
        this.lineSpacingSpinner.setValue(new Integer(style.getLineSpacing()));
    }

    private void populateVertical(Style style){
        if (ExtraClassManager.getInstance().getVerticalTextProcessor() == null){
            return;
        }
        this.isVerticalTextCheckBox.setSelected(true);
        this.rotationSpinner.setEnabled(false);
        if (style.getTextDirection() == Style.LEFT_TO_RIGHT) {
            leftToRightRB.setEnabled(true);
            rightToLeftRB.setEnabled(true);
            this.leftToRightRB.setSelected(true);
            this.rightToLeftRB.setSelected(false);
        } else {
            leftToRightRB.setEnabled(true);
            rightToLeftRB.setEnabled(true);
            this.leftToRightRB.setSelected(false);
            this.rightToLeftRB.setSelected(true);
        }
    }

    /**
     * ����Ƿ�Ϸ�
     *
     */
    public void checkValid() throws Exception {
        int rotation = ((Integer) this.rotationSpinner.getValue()).intValue();

        if (rotation > NUM_90 || rotation < -NUM_90) {
            throw new Exception(Inter.getLocText("StyleAlignment-The_value_of_rotation_must_between_-90_and_90_degrees") + ".");
        }
    }

    /**
     * Update cellstyle border
     *
     * @param style the new style.
     */
    public Style update(Style style) {
        //peter:��Ҫ�жϴ��ݽ�����ֵ�Ƿ�Ϊnull.
        if (style == null) {
            return style;
        }

        style = style.deriveHorizontalAlignment(this.horAlignmentComboBox.getSelectedAlignment());
        style = style.deriveVerticalAlignment(this.verAlignmentComboBox.getSelectedAlignment());

        if (ComparatorUtils.equals(this.textComboBox.getSelectedItem(), TEXT[0])) {
            style = style.deriveTextStyle(Style.TEXTSTYLE_WRAPTEXT);
        } else if (ComparatorUtils.equals(this.textComboBox.getSelectedItem(), TEXT[1])) {
            style = style.deriveTextStyle(Style.TEXTSTYLE_SINGLELINE);
        } else if (ComparatorUtils.equals(this.textComboBox.getSelectedItem(), TEXT[2])) {
            style = style.deriveTextStyle(Style.TEXTSTYLE_SINGLELINEADJUSTFONT);
        } else {
            style = style.deriveTextStyle(Style.TEXTSTYLE_MULTILINEADJUSTFONT);
        }

//        //peter: image layout
//        if (ComparatorUtils.equals(this.imageComboBox.getSelectedItem(), IMAGE[0])) {
//            style = style.deriveImageLayout(Constants.IMAGE_DEFAULT);
//        } else if (ComparatorUtils.equals(this.imageComboBox.getSelectedItem(), IMAGE[1])) {
//            style = style.deriveImageLayout(Constants.IMAGE_EXTEND);
//        }

        //james isVerticalText
        if (this.isVerticalTextCheckBox.isSelected()) {
            style = style.deriveVerticalText(Style.VERTICALTEXT);
            style = style.deriveRotation(0);
            if (this.leftToRightRB.isSelected()) {
                style = style.deriveTextDirection(Style.LEFT_TO_RIGHT);
            } else {
                style = style.deriveTextDirection(Style.RIGHT_TO_LEFT);
            }
        } else {
            style = style.deriveVerticalText(Style.HORIZONTALTEXT);
            style = style.deriveRotation(((Integer) this.rotationSpinner.getValue()).intValue());
        }

        //alex:indent
        style = style.derivePaddingLeft(((Integer) this.leftIndentSpinner.getValue()).intValue());
        style = style.derivePaddingRight(((Integer) this.rightIndentSpinner.getValue()).intValue());

        style = style.deriveSpacingBefore(((Integer) this.spacingBeforeSpinner.getValue()).intValue());
        style = style.deriveSpacingAfter(((Integer) this.spacingAfterSpinner.getValue()).intValue());
        style = style.deriveLineSpacing(((Integer) this.lineSpacingSpinner.getValue()).intValue());
        
        return style;
    }

    public static class Alignment {

    }
}
