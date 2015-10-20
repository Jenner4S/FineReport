package com.fr.design.mainframe.backgroundpane;

import com.fr.base.GraphHelper;
import com.fr.base.background.PatternBackground;
import com.fr.design.constants.UIConstants;
import com.fr.design.border.UIRoundedBorder;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Background;
import com.fr.general.Inter;
import com.fr.design.style.color.ColorSelectBox;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;

/**
 * @author zhou
 * @since 2012-5-29����1:12:33
 */
public class PatternBackgroundPane extends BackgroundSettingPane {

	private int patternIndex = 0; // pattern index.
	private ColorSelectBox foregroundColorPane;
	private ColorSelectBox backgroundColorPane;
	private PatternButton[] patternButtonArray;

	public PatternBackgroundPane() {
		this.setLayout(new BorderLayout(0, 4));

		JPanel contentPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_S_Pane();
		this.add(contentPane, BorderLayout.NORTH);
		contentPane.setBorder(new UIRoundedBorder(UIConstants.LINE_COLOR, 1, 5));

		JPanel typePane2 = new JPanel();
		contentPane.add(typePane2);
		typePane2.setLayout(new GridLayout(0, 8, 1, 1));
		typePane2.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		ButtonGroup patternButtonGroup = new ButtonGroup();
		patternButtonArray = new PatternButton[PatternBackground.PATTERN_COUNT];
		for (int i = 0; i < PatternBackground.PATTERN_COUNT; i++) {
			patternButtonArray[i] = new PatternButton(i);
			patternButtonGroup.add(patternButtonArray[i]);
			typePane2.add(patternButtonArray[i]);
		}

		JPanel colorPane = new JPanel(new GridLayout(0, 2));
		foregroundColorPane = new ColorSelectBox(70);
		backgroundColorPane = new ColorSelectBox(70);
		foregroundColorPane.setSelectObject(Color.lightGray);
		backgroundColorPane.setSelectObject(Color.black);

		colorPane.add(this.createLabelColorPane(Inter.getLocText("Foreground") + ":", foregroundColorPane));
		colorPane.add(this.createLabelColorPane(Inter.getLocText("Background") + ":", backgroundColorPane));
		this.add(colorPane, BorderLayout.CENTER);
		foregroundColorPane.addSelectChangeListener(colorChangeListener);
		backgroundColorPane.addSelectChangeListener(colorChangeListener);
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension dim = super.getPreferredSize();
		dim.height = 190;
		return dim;
	}

	private JPanel createLabelColorPane(String text, JComponent colorPane) {
		JPanel labelColorPane = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		labelColorPane.add(new UILabel(text));
		labelColorPane.add(colorPane);

		return labelColorPane;
	}

	public void populateBean(Background background) {
		PatternBackground patternBackground = (PatternBackground) background;
		int patternIndex = patternBackground.getPatternIndex();

		if (patternIndex >= 0 && patternIndex < this.patternButtonArray.length) {
			this.patternButtonArray[patternIndex].setSelected(true);
			this.patternIndex = patternIndex;
		} else {
			this.patternIndex = 0;
		}

		foregroundColorPane.setSelectObject(patternBackground.getForeground());
		backgroundColorPane.setSelectObject(patternBackground.getBackground());
	}

	public Background updateBean() {
		return new PatternBackground(patternIndex, foregroundColorPane.getSelectObject(), backgroundColorPane.getSelectObject());
	}


	/**
	 * ������Ǽ�һ���۲��߼����¼�
	 *
	 * @param listener �۲��߼����¼�
	 */
	public void registerChangeListener(final UIObserverListener listener) {
		 foregroundColorPane.addSelectChangeListener(new ChangeListener() {
			 public void stateChanged(ChangeEvent e) {
				 listener.doChange();
			 }
		 });
		backgroundColorPane.addSelectChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				listener.doChange();
			}
		});
		for (int i = 0, count = patternButtonArray.length; i < count; i ++) {
			patternButtonArray[i].addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					listener.doChange();
				}
			});
		}
	}


	// Foreground or Background changed.
	ChangeListener colorChangeListener = new ChangeListener() {

		public void stateChanged(ChangeEvent e) {
			for (int i = 0; i < patternButtonArray.length; i++) {
				patternButtonArray[i].setPatternForeground(foregroundColorPane.getSelectObject());
				patternButtonArray[i].setPatternBackground(backgroundColorPane.getSelectObject());
			}

			PatternBackgroundPane.this.repaint();// repaint
		}
	};

	/**
	 * Pattern type button.
	 */
	class PatternButton extends JToggleButton implements ActionListener {

		public PatternButton(int pIndex) {
			this.pIndex = pIndex;
			this.addActionListener(this);

			this.setCursor(new Cursor(Cursor.HAND_CURSOR));
			this.setBorder(null);
			this.patternBackground = new PatternBackground(this.pIndex, Color.lightGray, Color.black);
		}

		public void paintComponent(Graphics g) {
			Graphics2D g2d = (Graphics2D) g;

			Dimension d = getSize();
			this.patternBackground.paint(g2d, new Rectangle2D.Double(0, 0, d.width - 1, d.height - 1));

			if (this.pIndex == patternIndex) {// it's selected.
				g2d.setPaint(UIConstants.LINE_COLOR);
				GraphHelper.draw(g2d, new Rectangle2D.Double(0, 0, d.width - 1, d.height - 1));
			}
		}

		public Dimension getPreferredSize() {
			return new Dimension(super.getPreferredSize().width, 20);
		}

		public void setPatternForeground(Color foreground) {
			this.patternBackground.setForeground(foreground);
		}

		public void setPatternBackground(Color background) {
			this.patternBackground.setBackground(background);
		}

		/**
		 * set Pattern index.
		 */
		public void actionPerformed(ActionEvent evt) {
			PatternBackgroundPane.this.patternIndex = pIndex;

			fireChagneListener();
			PatternBackgroundPane.this.repaint();// repaint
		}

		public void addChangeListener(ChangeListener changeListener) {
			this.changeListener = changeListener;
		}

		private void fireChagneListener() {
			if (this.changeListener != null) {
				ChangeEvent evt = new ChangeEvent(this);
				this.changeListener.stateChanged(evt);
			}
		}

		private int pIndex = 0;
		private PatternBackground patternBackground;
	}

	@Override
	public boolean accept(Background background) {
		return background instanceof PatternBackground;
	}

	@Override
	public String title4PopupWindow() {
		return Inter.getLocText("Background-Pattern");
	}
}
