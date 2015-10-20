package com.fr.design.mainframe.loghandler;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

import com.fr.general.Inter;

public class LogHandlerBarUI extends ComponentUI implements MouseListener, FocusListener {
	private static final Color DEFAULT_FOREGROUND = new Color(0, 0, 0);
	private static final int TEXT_LEADING_GAP = 14;

	private boolean armed;
	private int textLeadingGap = TEXT_LEADING_GAP;

	// ����ɫ��ʼɫ
	private Color lightColor;
	// ����ɫ����ɫ
	private Color darkColor;

	// ��UIʵ����Ӧ��CaptionButton
	protected LogHandlerBar button;

	public LogHandlerBarUI() {
	}

    /**
     * ����UI
     * @param c ���
     * @return ���UI
     */
	public static ComponentUI createUI(JComponent c) {
		return new LogHandlerBarUI();
	}

    /**
     *�������
     * @param c ���
     */
	public void installUI(JComponent c) {
		button = (LogHandlerBar) c;
		button.setForeground(DEFAULT_FOREGROUND);
		button.setFocusable(true);

		button.addMouseListener(this);
		button.addFocusListener(this);
	}

    /**
     * ����������
     * @param c ���
     */
	public void uninstallUI(JComponent c) {
		button.removeMouseListener(this);
		button.removeFocusListener(this);
	}

	protected void paintBackground(Graphics g) {
		int w = button.getWidth();
		int h = button.getHeight();
		Graphics2D g2d = (Graphics2D) g;
		GradientPaint gp = new GradientPaint(1, 1, darkColor, 1, h - 1, darkColor);
		g2d.setPaint(gp);
		g2d.fillRect(0, 0, w, h);
	}

	public void paint(Graphics g, JComponent c) {
		super.paint(g, c);
		if (armed) {
			button.setWithSerious(false);
		}
		if (button.IsWithSerious()) {
			lightColor = new Color(255, 242, 218);
			darkColor = new Color(255, 219, 108);
		} else {
			lightColor = new Color(226, 230, 234);
			darkColor = new Color(183, 188, 195);
		}
		paintBackground(g);
		paintCaptionText(g);

	}

	protected void paintCaptionText(Graphics g) {
		FontMetrics fm = g.getFontMetrics();
		Color color = button.getForeground();
		g.setColor(color);
		int y = ((button.getHeight() - fm.getHeight()) / 2) + fm.getAscent();
		if (button.getText() != null) {
			g.drawString(button.getText(), textLeadingGap, y);
		}

		g.drawString(Inter.getLocText("NNormal") + '(' + button.getInfo() + ')', button.getWidth() - 310, y);
		g.drawString(Inter.getLocText("Alert") + '(' + button.getError() + ')', button.getWidth() - 250, y);
		g.drawString(Inter.getLocText("Seriously") + '(' + button.getServer() + ')', button.getWidth() - 190, y);
	}

    /**
     * �����
     * @param e ����¼�
     */
	public void mousePressed(MouseEvent e) {
		armed = true;
		button.requestFocus();
	}

    /**
     * ��갴�²��ͷ�
     * @param e ����¼�
     */
	public void mouseEntered(MouseEvent e) {

	}

    /**
     *�¼�����
     * @param e ����¼�
     */
	public void mouseExited(MouseEvent e) {
		button.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

    /**
     *ʧȥ���̽���
     * @param e �����¼�
     */
	public void focusLost(FocusEvent e) {
		armed = false;
	}
    /**
     *�����
     * @param e ����¼�
     */
	public void mouseClicked(MouseEvent e) {
	}
    /**
     *����ͷ�
     * @param e ����¼�
     */
	public void mouseReleased(MouseEvent e) {
	}
    /**
     *��ȡ���̽���
     * @param e �����¼�
     */
	public void focusGained(FocusEvent e) {
	}
}
