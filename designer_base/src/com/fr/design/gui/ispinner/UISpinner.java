package com.fr.design.gui.ispinner;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import com.fr.design.constants.UIConstants;
import com.fr.design.event.GlobalNameListener;
import com.fr.design.event.GlobalNameObserver;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.itextfield.UINumberField;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.gui.itextfield.UITextFieldUI;
import com.fr.stable.Constants;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.design.utils.gui.GUIPaintUtils;

public class UISpinner extends JPanel implements UIObserver, GlobalNameObserver {

	protected double value;
	private static final int SIZE = 20;
	private static final int LEN = 13;
	private UINumberField textField;
	private UIButton preButton;
	private UIButton nextButton;
	private double minValue;
	private double maxValue;
	private double dierta;
	private String spinnerName = "";
	private UIObserverListener uiObserverListener;
	private GlobalNameListener globalNameListener = null;


	public UISpinner(double minValue, double maxValue, double dierta) {
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.dierta = dierta;
		initComponents();
		iniListener();
	}

	public UISpinner(double minValue, double maxValue, double dierta, double defaultValue) {
		this(minValue, maxValue, dierta);
		textField.setValue(defaultValue);
	}

	private void iniListener() {
		if (shouldResponseChangeListener()) {
			this.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					if (uiObserverListener == null) {
						return;
					}
					uiObserverListener.doChange();
				}
			});
		}
	}

	/**
	 *  给组件分别加上FocusListener
	 * @param focusListener    监听事件
	 */
	public void addUISpinnerFocusListenner(FocusListener focusListener) {
		this.addFocusListener(focusListener);
		this.textField.addFocusListener(focusListener);
		this.preButton.addFocusListener(focusListener);
		this.nextButton.addFocusListener(focusListener);

	}

	public double getValue() {
		return value;
	}

	public void setGlobalName(String name) {
		spinnerName = name;
	}

	public UINumberField getTextField() {
		return textField;
	}

	public void setValue(double value) {
		if (globalNameListener != null && shouldResponseNameListener()) {
			globalNameListener.setGlobalName(spinnerName);
		}
		value = value < minValue ? minValue : value;
		value = value > maxValue ? maxValue : value;
		if (value == this.value) {
			return;
		}
		this.value = value;

		textField.getDocument().removeDocumentListener(docListener);
		textField.setValue(value);
		textField.getDocument().addDocumentListener(docListener);
		fireStateChanged();
	}

	public void setTextFieldValue(double value) {
		if (globalNameListener != null && shouldResponseNameListener()) {
			globalNameListener.setGlobalName(spinnerName);
		}
		value = value < minValue ? minValue : value;
		value = value > maxValue ? maxValue : value;

		if (value == this.value) {
			return;
		}
		this.value = value;
		fireStateChanged();
	}


	public void setEnabled(boolean flag) {
		super.setEnabled(flag);
		this.textField.setEnabled(flag);
		this.preButton.setEnabled(flag);
		this.nextButton.setEnabled(flag);
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension dim = super.getPreferredSize();
		dim.height = SIZE;
		return dim;
	}

	/**
	 *增加 a <code>ChangeListener</code> to the listener list.
	 * @param l    监听事件
	 */
	public void addChangeListener(ChangeListener l) {
		this.listenerList.add(ChangeListener.class, l);
	}

	/**
	 *移除 a <code>ChangeListener</code> from the listener list.
	 * @param l    监听事件
	 */
	public void removeChangeListener(ChangeListener l) {
		this.listenerList.remove(ChangeListener.class, l);
	}

	// august: Process the listeners last to first
	protected void fireStateChanged() {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ChangeListener.class) {
				((ChangeListener) listeners[i + 1]).stateChanged(new ChangeEvent(this));
			}
		}
	}


	private void initComponents() {
		textField = initNumberField();
		textField.setMaxValue(maxValue);
		textField.setMinValue(minValue);
		setValue(value);
		textField.setUI(new SpinnerTextFieldUI(textField));
		preButton = new UIButton(UIConstants.ARROW_UP_ICON){
            public boolean shouldResponseChangeListener() {
                return false;
            }
        };
		preButton.setRoundBorder(true, Constants.LEFT);
		nextButton = new UIButton(UIConstants.ARROW_DOWN_ICON) {
            public boolean shouldResponseChangeListener() {
                return false;
            }
        };
		nextButton.setRoundBorder(true, Constants.LEFT);
		setLayout(new BorderLayout());
		add(textField, BorderLayout.CENTER);
		JPanel arrowPane = new JPanel();
		arrowPane.setPreferredSize(new Dimension(LEN, SIZE));
		arrowPane.setLayout(new GridLayout(2, 1));
		preButton.setBounds(0, 1, 13, 10);
		nextButton.setBounds(0, 10, 13, 10);
		arrowPane.add(preButton);
		arrowPane.add(nextButton);
		add(arrowPane, BorderLayout.EAST);
        componentInitListeners();
	}

    private void componentInitListeners(){
        preButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setValue(value + dierta);
            }
        });
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setValue(value - dierta);
            }
        });
        addMouseWheelListener(new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (isEnabled() && e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
                    setValue(value - e.getWheelRotation());
                }
            }
        });
        textField.getDocument().addDocumentListener(docListener);
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                textField.setValue(value);
            }
        });
    }

	protected UINumberField initNumberField() {
		return new UINumberField(2) {
			public boolean shouldResponseChangeListener() {
				return false;
			}
		};
	}

	private DocumentListener docListener = new DocumentListener() {
		@Override
		public void removeUpdate(DocumentEvent e) {
			setTextFieldValue(textField.getValue());
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			setTextFieldValue(textField.getValue());
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			setTextFieldValue(textField.getValue());
		}
	};

	/**
	 *  给组件登记一个观察者监听事件
	 * @param listener 观察者监听事件
	 */
	public void registerChangeListener(UIObserverListener listener) {
		uiObserverListener = listener;
	}

    /**
     * 组件是否需要响应添加的观察者事件
     *
     * @return 如果需要响应观察者事件则返回true，否则返回false
     */
	public boolean shouldResponseChangeListener() {
		return true;
	}

    /**
     * 给组件登记一个全局名字观察者监听事件
     *
     * @param listener 观察者监听事件
     */
	public void registerNameListener(GlobalNameListener listener) {
		globalNameListener = listener;
	}

    /**
     * 组件是否需要响应添加的观察者事件
     *
     * @return 如果需要响应观察者事件则返回true，否则返回false
     */
	public boolean shouldResponseNameListener() {
		return true;
	}

	private class SpinnerTextFieldUI extends UITextFieldUI {

		public SpinnerTextFieldUI(UITextField textField) {
			super(textField);
		}

		@Override
		public void paintBorder(Graphics2D g2d, int width, int height,
								boolean isRound, int rectDirection) {
			// do nothing
		}

		protected void paintBackground(Graphics g) {
			JTextComponent editor = getComponent();
			int width = editor.getWidth();
			int height = editor.getHeight();
			Shape oldClip = g.getClip();
			Shape roundShape = new RoundRectangle2D.Double(0, 0, width, height, UIConstants.ARC, UIConstants.ARC);
			Graphics2D g2d = (Graphics2D) g;
			g2d.clearRect(0, 0, width, height);
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.clip(roundShape);
			g2d.setColor(Color.WHITE);
			g2d.fillRoundRect(1, 1, width - 2, height - 2, UIConstants.ARC, UIConstants.ARC);
			if (isRollOver && isEnabled()) {
				Shape shape = new RoundRectangle2D.Double(1, 1, width - 3, height - 3, UIConstants.ARC, UIConstants.ARC);
				GUIPaintUtils.paintBorderShadow(g2d, 3, shape, UIConstants.HOVER_BLUE, Color.white);
			} else {
				g2d.setColor(UIConstants.LINE_COLOR);
				g2d.drawRoundRect(1, 1, width - 2, height - 2, UIConstants.ARC, UIConstants.ARC);
				g2d.clearRect(width - 2, 0, 2, height);
				g2d.setClip(oldClip);
				g2d.drawLine(width - 2, 1, width, 1);
				g2d.drawLine(width - 2, height - 1, width, height - 1);
			}
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		}

	}

    /**
     *   程序入口  测试
     * @param args       参数
     */
	public static void main(String... args) {
		LayoutManager layoutManager = null;
		JFrame jf = new JFrame("test");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel content = (JPanel) jf.getContentPane();
		content.setLayout(layoutManager);

		UISpinner bb = new UISpinner(0, 9, 1);
		bb.setValue(4);
		bb.setBounds(20, 20, bb.getPreferredSize().width, bb.getPreferredSize().height);
		content.add(bb);
		GUICoreUtils.centerWindow(jf);
		jf.setSize(400, 400);
		jf.setVisible(true);
	}
}