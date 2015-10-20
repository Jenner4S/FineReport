
package com.fr.design.gui.ibutton;

import com.fr.base.BaseUtils;
import com.fr.base.CellBorderStyle;
import com.fr.base.GraphHelper;
import com.fr.design.constants.UIConstants;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.stable.Constants;
import com.fr.stable.StringUtils;
import com.fr.design.utils.gui.GUICoreUtils;

import javax.swing.*;
import javax.swing.plaf.ButtonUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

public class UIButton extends JButton implements UIObserver {

	public static final int OTHER_BORDER = 1;
	public static final int NORMAL_BORDER = 2;
	private static final int HEIGH = 20;
	private boolean isExtraPainted = true;
	private boolean isRoundBorder = true;
	private int rectDirection = Constants.NULL;
	private Stroke borderStroke = UIConstants.BS;
	private Color borderColor = UIConstants.LINE_COLOR;

	private boolean isPressedPainted = true;
	private boolean isNormalPainted = true;
	protected boolean isBorderPaintedOnlyWhenPressed = false;

	private int borderType = NORMAL_BORDER;
	private CellBorderStyle border = null;

	protected UIObserverListener uiObserverListener;

	public UIButton() {
		this(StringUtils.EMPTY);
	}

	public UIButton(String string) {
		super(string);
		init();
	}


	public UIButton(Icon icon) {
		super(icon);
		init();
	}

	public UIButton(Action action) {
		super(action);
		init();
	}

	public UIButton(String text, Icon icon) {
		super(text, icon);
		init();
	}

    /**
     * �Ƿ���й�Ȩ�ޱ༭
     * @param role ��ɫ
     * @return ��
     */
	public boolean isDoneAuthorityEdited(String role) {
		return false;
	}

	public UIButton(Icon normal, Icon rollOver, Icon pressed) {
		super(normal);
		setBorderPainted(false);
		setRolloverIcon(rollOver);
		setPressedIcon(pressed);
		setExtraPainted(false);
		setBackground(null);
		setOpaque(false);
		initListener();
	}

	protected void initListener() {
		if (shouldResponseChangeListener()) {
			this.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (uiObserverListener == null) {
						return;
					}
					uiObserverListener.doChange();
				}
			});
		}
	}


	//ȷ���������ı߿����ͣ�����������Border����
	//����������border���ͣ���ҪsetOtherType��������������ɫ�ȡ������������ͣ�����û�����ã���Ĭ�ϵ��������ͱ߿�
	public void setBorderType(int borderType) {
		this.borderType = borderType;
	}


	public void setBorderStyle(CellBorderStyle border) {
		this.border = border;
	}

	public void set4ToolbarButton() {
		setNormalPainted(false);
		Dimension dim = getPreferredSize();
		dim.height = HEIGH;
		setBackground(null);
		setOpaque(false);
		setSize(dim);
		setBorderPaintedOnlyWhenPressed(true);
	}

	public void set4LargeToolbarButton() {
		setNormalPainted(false);
		setBackground(null);
		setOpaque(false);
		setSize(new Dimension(40, 40));
		setBorderPaintedOnlyWhenPressed(true);
	}

    public void set4ChartLargeToolButton(){
        setNormalPainted(false);
      	setBackground(null);
      	setOpaque(false);
      	setSize(new Dimension(34, 44));
      	setBorderPaintedOnlyWhenPressed(true);
    }


	private void init() {
		setBackground(null);
		setRolloverEnabled(true);
		initListener();
	}

	@Override
	public ButtonUI getUI() {
		return new UIButtonUI();
	}

    /**
     * ���½���
     */
	public void updateUI() {
		setUI(getUI());
	}

	public CellBorderStyle getBorderStyle() {
		return this.border;
	}

	@Override
	public Insets getInsets() {
		if (getIcon() != null) {
			return new Insets(0, 3, 0, 3);
		}
		return new Insets(0, 10, 0, 10);
	}

	//@Override
	public Dimension getPreferredSize() {
		return new Dimension(super.getPreferredSize().width, 20);
	}


	public int getBorderType() {
		return borderType;
	}

	public void setOtherBorder(Stroke s, Color c) {
		borderStroke = s;
		borderColor = c;
	}


	@Override
	protected void paintBorder(Graphics g) {

		if (!isBorderPainted()) {
			return;
		}
		if (borderType == OTHER_BORDER) {
			paintOtherBorder(g);
		} else {
			boolean isPress = (isBorderPaintedOnlyWhenPressed && getModel().isPressed());
			if (isPress || !isBorderPaintedOnlyWhenPressed) {
				if (ui instanceof UIButtonUI) {
					((UIButtonUI) ui).paintBorder(g, this);

				} else {
					super.paintBorder(g);

				}
			}
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Dimension size = this.getSize();
		Graphics2D g2d = (Graphics2D) g;
		Stroke oldStroke = g2d.getStroke();
		if (border != null) {
			g2d.setColor(border.getTopColor());
			GraphHelper.drawLine(g2d, 3, 4, size.getWidth() - 4, 4, border.getTopStyle());
			g2d.setColor(border.getLeftColor());
			GraphHelper.drawLine(g2d, 3, 4, 3, size.getHeight() - 4, border.getLeftStyle());
			g2d.setColor(border.getBottomColor());
			GraphHelper.drawLine(g2d, 3, size.getHeight() - 4, size.getWidth() - 4, size.getHeight() - 4, border.getBottomStyle());
			g2d.setColor(border.getRightColor());
			GraphHelper.drawLine(g2d, size.getWidth() - 4, 4, size.getWidth() - 4, size.getHeight() - 4, border.getRightStyle());
		} else {
			GraphHelper.drawLine(g2d, 2, 4, size.getWidth() - 4, 4, Constants.LINE_NONE);
			GraphHelper.drawLine(g2d, 2, 4, 2, size.getHeight() - 4, Constants.LINE_NONE);
			GraphHelper.drawLine(g2d, 2, size.getHeight() - 4, size.getWidth() - 4, size.getHeight() - 4, Constants.LINE_NONE);
			GraphHelper.drawLine(g2d, size.getWidth() - 4, 4, size.getWidth() - 4, size.getHeight() - 4, Constants.LINE_NONE);
		}
		g2d.setStroke(oldStroke);
	}


	protected void paintOtherBorder(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(borderStroke);
		Shape shape = new RoundRectangle2D.Float(0.5f, 0.5f, getWidth() - 1, getHeight() - 1, UIConstants.ARC, UIConstants.ARC);
		g2d.setColor(borderColor);
		g2d.draw(shape);
	}

	public void setExtraPainted(boolean extra) {
		this.isExtraPainted = extra;
	}

    /**
     * �Ƿ���⻭
     * @return ���򷵻�TRUE
     */
	public boolean isExtraPainted() {
		return this.isExtraPainted;
	}

	/**
	 * @return
	 */
	public int getRectDirection() {
		return rectDirection;
	}

    /**
     * �Ƿ�Բ�߿�
     * @return ���򷵻�true
     */
	public boolean isRoundBorder() {
		return isRoundBorder;
	}

	/**
	 * @param isRoundBorder
	 */
	public void setRoundBorder(boolean isRoundBorder) {
		setRoundBorder(isRoundBorder, Constants.NULL);
	}

	/**
	 * @param isRound
	 * @param rectDirection
	 */
	public void setRoundBorder(boolean isRound, int rectDirection) {
		this.isRoundBorder = isRound;
		this.rectDirection = rectDirection;
	}

    /**
     * �Ƿ�ѹ��
     * @return ���򷵻�TRUE
     */
	public boolean isPressedPainted() {
		return isPressedPainted;
	}

	/**
	 * @param isPressedPainted
	 */
	public void setPressedPainted(boolean isPressedPainted) {
		this.isPressedPainted = isPressedPainted;
	}

    /**
     * �Ƿ�������
     * @return ���򷵻�TRUE
     */
	public boolean isNormalPainted() {
		return isNormalPainted;
	}

	/**
	 * @param isNormalPressed
	 */
	public void setNormalPainted(boolean isNormalPressed) {
		this.isNormalPainted = isNormalPressed;
		if (!isNormalPressed) {
			setBackground(null);
			setOpaque(false);
		}
	}

	/**
	 * @param value
	 */
	public void setBorderPaintedOnlyWhenPressed(boolean value) {
		this.isBorderPaintedOnlyWhenPressed = value;
	}

    /**
     * ������
     * @param args ��ڲ���
     */
	public static void main(String... args) {
		JFrame jf = new JFrame("test");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel content = (JPanel) jf.getContentPane();
		content.setLayout(null);

		UIButton bb = new UIButton(BaseUtils.readIcon("/com/fr/design/images/buttonicon/add.png"));
		bb.setEnabled(false);
		bb.setBorderType(OTHER_BORDER);
		//  bb.setBounds(20, 20,content.getSize().width, bb.getPreferredSize().height);
		bb.setPreferredSize(new Dimension(100, 30));
		bb.setBounds(0, 0, bb.getPreferredSize().width, bb.getPreferredSize().height);
		content.add(bb);
		GUICoreUtils.centerWindow(jf);
		jf.setSize(400, 400);
		jf.setVisible(true);
	}

	/**
	 * ������Ǽ�һ���۲��߼����¼�
	 *
	 * @param listener �۲��߼����¼�
	 */
	public void registerChangeListener(UIObserverListener listener) {
		this.uiObserverListener = listener;
	}

	/**
	 * ����Ƿ���Ҫ��Ӧ��ӵĹ۲����¼�
	 *
	 * @return �����Ҫ��Ӧ�۲����¼��򷵻�true�����򷵻�false
	 */
	public boolean shouldResponseChangeListener() {
		return true;
	}
}
