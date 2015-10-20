package com.fr.design.gui.ibutton;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.text.View;

import com.fr.design.constants.UIConstants;
import com.fr.design.roleAuthority.ReportAndFSManagePane;
import sun.swing.SwingUtilities2;

import com.fr.stable.StringUtils;
import com.fr.design.utils.gui.GUIPaintUtils;

public class UIButtonUI extends BasicButtonUI {

    private Rectangle viewRec = new Rectangle();
    private Rectangle textRec = new Rectangle();
    private Rectangle iconRec = new Rectangle();

    @Override
    public void paint(Graphics g, JComponent c) {
        UIButton b = (UIButton) c;
        Graphics2D g2d = (Graphics2D) g;
        int w = b.getWidth();
        int h = b.getHeight();

        String text = initRecAndGetText(b, SwingUtilities2.getFontMetrics(b, g), b.getWidth(), b.getHeight());
        String selectedRoles = ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName();
        clearTextShiftOffset();

        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (b.isExtraPainted()) {
            if (isPressed(b) && b.isPressedPainted()) {
                GUIPaintUtils.fillPressed(g2d, 0, 0, w, h, b.isRoundBorder(), b.getRectDirection(), b.isDoneAuthorityEdited(selectedRoles));
            } else if (isRollOver(b)) {
                GUIPaintUtils.fillRollOver(g2d, 0, 0, w, h, b.isRoundBorder(), b.getRectDirection(), b.isDoneAuthorityEdited(selectedRoles), b.isPressedPainted());
            } else if (b.isNormalPainted()) {
                GUIPaintUtils.fillNormal(g2d, 0, 0, w, h, b.isRoundBorder(), b.getRectDirection(), b.isDoneAuthorityEdited(selectedRoles), b.isPressedPainted());
            }
        }
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        paintContent(g, b, text);
    }

    protected boolean isRollOver(AbstractButton b) {
        ButtonModel model = b.getModel();
        return model.isRollover() && !b.isSelected();
    }

    protected boolean isPressed(AbstractButton b) {
        ButtonModel model = b.getModel();
        return (model.isArmed() && model.isPressed()) || b.isSelected();
    }

    private void paintContent(Graphics g, AbstractButton b, String text) {
        if (b.getIcon() != null) {
            paintIcon(g, b);
        }
        if (!StringUtils.isEmpty(text)) {
            paintText(g, b, text);
        }
    }

    private void paintText(Graphics g, AbstractButton b, String text) {
        View v = (View) b.getClientProperty(BasicHTML.propertyKey);
        if (v != null) {
            v.paint(g, textRec);
            return;
        }
        FontMetrics fm = SwingUtilities2.getFontMetrics(b, g);
        int mnemonicIndex = b.getDisplayedMnemonicIndex();
        if (b.isEnabled()) {
            g.setColor(UIConstants.FONT_COLOR);
        } else {
            g.setColor(UIConstants.LINE_COLOR);
        }

        SwingUtilities2.drawStringUnderlineCharAt(b, g, text, mnemonicIndex, textRec.x + getTextShiftOffset(), textRec.y + fm.getAscent() + getTextShiftOffset());
    }

    private String initRecAndGetText(AbstractButton b, FontMetrics fm, int width, int height) {
        Insets i = b.getInsets();
        viewRec.x = i.left;
        viewRec.y = i.top;
        viewRec.width = width - (i.right + viewRec.x);
        viewRec.height = height - (i.bottom + viewRec.y);
        textRec.x = textRec.y = textRec.width = textRec.height = 0;
        iconRec.x = iconRec.y = iconRec.width = iconRec.height = 0;
        // layout the text and icon
        return SwingUtilities.layoutCompoundLabel(
                b, fm, b.getText(), b.getIcon(),
                b.getVerticalAlignment(), b.getHorizontalAlignment(),
                b.getVerticalTextPosition(), b.getHorizontalTextPosition(),
                viewRec, iconRec, textRec,
                b.getText() == null ? 0 : b.getIconTextGap());
    }

    protected void paintBorder(Graphics g, UIButton b) {
        String selectedRoles = ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName();
        GUIPaintUtils.drawBorder((Graphics2D) g, 0, 0, b.getWidth(), b.getHeight(), b.isRoundBorder(), b.getRectDirection(), b.isDoneAuthorityEdited(selectedRoles));

    }

    protected void paintIcon(Graphics g, JComponent c) {
        AbstractButton b = (AbstractButton) c;
        ButtonModel model = b.getModel();
        Icon icon = b.getIcon();
        Icon tmpIcon = null;
        if (icon == null) {
            return;
        }
        Icon selectedIcon = null;
		/* the fallback icon should be based on the selected state */
        if (model.isSelected()) {
            selectedIcon = (Icon) b.getSelectedIcon();
            if (selectedIcon != null) {
                icon = selectedIcon;
            }
        }
        if (!model.isEnabled()) {
            if (model.isSelected()) {
                tmpIcon = (Icon) b.getDisabledSelectedIcon();
                if (tmpIcon == null) {
                    tmpIcon = selectedIcon;
                }
            }
            if (tmpIcon == null) {
                tmpIcon = (Icon) b.getDisabledIcon();
            }
        } else if (model.isPressed() && model.isArmed()) {
            tmpIcon = (Icon) b.getPressedIcon();
            if (tmpIcon != null) {
                // revert back to 0 offset
                clearTextShiftOffset();
            }
        } else if (b.isRolloverEnabled() && model.isRollover()) {
            if (model.isSelected()) {
                tmpIcon = (Icon) b.getRolloverSelectedIcon();
                if (tmpIcon == null) {
                    tmpIcon = selectedIcon;
                }
            }
            if (tmpIcon == null) {
                tmpIcon = (Icon) b.getRolloverIcon();
            }
        }
        if (tmpIcon != null) {
            icon = tmpIcon;
        }
        paintModelIcon(model, icon, g, c);
    }

    private void paintModelIcon(ButtonModel model, Icon icon, Graphics g, JComponent c) {
        if (model.isPressed() && model.isArmed()) {
            icon.paintIcon(c, g, iconRec.x + getTextShiftOffset(),
                    iconRec.y + getTextShiftOffset());
        } else {
            icon.paintIcon(c, g, iconRec.x, iconRec.y);
        }
    }
}
