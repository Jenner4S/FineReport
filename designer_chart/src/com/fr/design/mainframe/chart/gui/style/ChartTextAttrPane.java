package com.fr.design.mainframe.chart.gui.style;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JPanel;

import com.fr.base.BaseUtils;
import com.fr.base.Utils;
import com.fr.chart.base.TextAttr;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.gui.ibutton.UIColorButton;
import com.fr.design.gui.ibutton.UIToggleButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.dialog.BasicPane;
import com.fr.general.FRFont;
import com.fr.design.utils.gui.GUICoreUtils;

public class ChartTextAttrPane extends BasicPane {
    private static final long serialVersionUID = 6731679928019436869L;
    protected UIComboBox fontNameComboBox;
    protected UIComboBox fontSizeComboBox;

    protected UIToggleButton bold;
    protected UIToggleButton italic;
    protected UIColorButton fontColor;

    public static Integer[] Font_Sizes = {new Integer(6), new Integer(8), new Integer(9), new Integer(10), new Integer(11), new Integer(12), new Integer(14), new Integer(16),
            new Integer(18), new Integer(20), new Integer(22), new Integer(24), new Integer(26), new Integer(28), new Integer(36), new Integer(48), new Integer(72)};

    public ChartTextAttrPane() {
        initComponents();
    }

    /**
     * ����
     * @return ����
     */
    public String title4PopupWindow() {
        // TODO Auto-generated method stub
        return null;
    }

    public void populate(TextAttr textAttr) {
        if (textAttr == null) {
            return;
        }
        FRFont frFont = textAttr.getFRFont();
        populate(frFont);
    }

    public void update(TextAttr textAttr) {
        if (textAttr == null) {
            textAttr = new TextAttr();
        }
        FRFont frFont = textAttr.getFRFont();
        frFont = updateFRFont();
        textAttr.setFRFont(frFont);
    }

    public TextAttr update() {
        TextAttr textAttr = new TextAttr();
        FRFont frFont = textAttr.getFRFont();
        frFont = updateFRFont();
        textAttr.setFRFont(frFont);
        return textAttr;
    }

    public void populate(FRFont frFont) {
        if (frFont == null) {
            return;
        }
        fontNameComboBox.setSelectedItem(frFont.getFamily());
        bold.setSelected(frFont.isBold());
        italic.setSelected(frFont.isItalic());
        if(fontSizeComboBox != null) {
        	fontSizeComboBox.setSelectedItem(frFont.getSize());
        }
        if (fontColor != null) {
            fontColor.setColor(frFont.getForeground());
        }
    }

    /**
     * ������
     * @return ������
     */
    public FRFont updateFRFont() {
        int style = Font.PLAIN;
        if (bold.isSelected() && !italic.isSelected()) {
            style = Font.BOLD;
        } else if (!bold.isSelected() && italic.isSelected()) {
            style = Font.ITALIC;
        } else if (bold.isSelected() && italic.isSelected()) {
            style = 3;
        }
        return FRFont.getInstance(Utils.objectToString(fontNameComboBox.getSelectedItem()), style,
                Float.valueOf(Utils.objectToString(fontSizeComboBox.getSelectedItem())), fontColor.getColor());
    }

    public void setEnabled(boolean enabled) {
        this.fontNameComboBox.setEnabled(enabled);
        this.fontSizeComboBox.setEnabled(enabled);
        this.fontColor.setEnabled(enabled);
        this.bold.setEnabled(enabled);
        this.italic.setEnabled(enabled);
    }

    protected void initComponents() {
        fontNameComboBox = new UIComboBox(Utils.getAvailableFontFamilyNames4Report());
        fontSizeComboBox = new UIComboBox(Font_Sizes);
        fontColor = new UIColorButton();
        bold = new UIToggleButton(BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/bold.png"));
        italic = new UIToggleButton(BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/italic.png"));

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;

        Component[] components1 = new Component[]{
                fontColor, italic, bold
        };
        JPanel buttonPane = new JPanel(new BorderLayout());
        buttonPane.add(fontSizeComboBox, BorderLayout.CENTER);
        buttonPane.add(GUICoreUtils.createFlowPane(components1, FlowLayout.LEFT, LayoutConstants.HGAP_LARGE), BorderLayout.EAST);


        double[] columnSize = {f};
        double[] rowSize = {p, p};
        Component[][] components = new Component[][]{
                new Component[]{fontNameComboBox},
                new Component[]{buttonPane}
        };

        JPanel panel = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        this.setLayout(new BorderLayout());
        this.add(panel, BorderLayout.CENTER);

        populate(FRFont.getInstance());
    }
}
