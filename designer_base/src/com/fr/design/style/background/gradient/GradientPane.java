package com.fr.design.style.background.gradient;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;

import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.ilable.UILabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fr.base.background.GradientBackground;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Background;
import com.fr.general.Inter;
import com.fr.design.style.background.BackgroundPane.BackgroundSettingPane;

/**
 * 渐变色的面板，不是很pp，面板应用显得繁琐，有写可以写成控件类型，比如色彩选择的。。，可以做得花哨点
 * @author ben
 */
public class GradientPane extends BackgroundSettingPane {
	private static final long serialVersionUID = -6854603990673031897L;

	private UIRadioButton left2right, top2bottom;
	private GradientBar gradientBar;
	private ChangeListener changeListener = null;

	public GradientPane() {

		// bug 5452 简化GradientPane
		JPanel jpanel = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText(new String[]{"Gradient-Color", "Set"}));
		jpanel.setPreferredSize(new Dimension(450, 320));
		jpanel.setLayout(new BorderLayout());

		// neil:增加渐变色拖动条
		JPanel gradientPanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
		JPanel blankJp = new JPanel();
		gradientBar = new GradientBar(4, 254);
		blankJp.add(gradientBar);
		UILabel jl = new UILabel(Inter.getLocText("Drag_to_select_gradient"));
		jl.setHorizontalAlignment(SwingConstants.CENTER);
		gradientPanel.add(jl, BorderLayout.NORTH);
		gradientPanel.add(blankJp, BorderLayout.SOUTH);
		jpanel.add(gradientPanel, BorderLayout.NORTH);

		JPanel centerPane = FRGUIPaneFactory.createNormalFlowInnerContainer_M_Pane();
		JPanel innercenterPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		centerPane.add(new UILabel("           "));
		centerPane.add(innercenterPane);
		innercenterPane.add(new UILabel(Inter.getLocText("Gradient-Direction") + ":"));

		left2right = new UIRadioButton(Inter.getLocText("PageSetup-Landscape"));
		innercenterPane.add(left2right);
		left2right.setSelected(true);
		left2right.addActionListener(reviewListener);

		top2bottom = new UIRadioButton(Inter.getLocText("PageSetup-Portrait"));
		innercenterPane.add(top2bottom);
		top2bottom.addActionListener(reviewListener);

		ButtonGroup toggle = new ButtonGroup();
		toggle.add(left2right);
		toggle.add(top2bottom);
		jpanel.add(centerPane, BorderLayout.CENTER);

		this.add(jpanel);
	}


	public void populate(Background background) {
		if (!(background instanceof GradientBackground)) {
			return;
		}
		GradientBackground bg = (GradientBackground) background;
		this.gradientBar.getSelectColorPointBtnP1().setColorInner(bg.getStartColor());
		this.gradientBar.getSelectColorPointBtnP2().setColorInner(bg.getEndColor());
		if (bg.getDirection() == GradientBackground.LEFT2RIGHT) {
			left2right.setSelected(true);
		} else {
			top2bottom.setSelected(true);
		}
		if (bg.isUseCell()) {
			return;
		}
		double startValue = (double) bg.getBeginPlace();
		double endValue = (double) bg.getFinishPlace();
		gradientBar.setStartValue(startValue);
		gradientBar.setEndValue(endValue);
		this.gradientBar.repaint();
	}

	public GradientBackground update() {
		GradientBackground gb = new GradientBackground(
				gradientBar.getSelectColorPointBtnP1().getColorInner(),
				gradientBar.getSelectColorPointBtnP2().getColorInner());
		if (left2right.isSelected()) {
			gb.setDirection(GradientBackground.LEFT2RIGHT);
		} else {
			gb.setDirection(GradientBackground.TOP2BOTTOM);
		}
		if (gradientBar.isOriginalPlace()) {
			gb.setUseCell(true);
		} else {
			gb.setUseCell(false);
			gb.setBeginPlace((float) gradientBar.getStartValue());
			gb.setFinishPlace((float) gradientBar.getEndValue());
		}
		return gb;
	}


	ActionListener reviewListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			fireChagneListener();
		}
	};

	public void addChangeListener(ChangeListener changeListener) {
		this.changeListener = changeListener;
		gradientBar.addChangeListener(changeListener);
	}

	public void fireChagneListener() {
		if (this.changeListener != null) {
			ChangeEvent evt = new ChangeEvent(this);
			this.changeListener.stateChanged(evt);
		}
	}
}