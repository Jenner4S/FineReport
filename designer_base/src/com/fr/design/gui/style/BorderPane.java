package com.fr.design.gui.style;

/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;

import com.fr.design.gui.ilable.UILabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fr.base.BaseUtils;
import com.fr.base.CellBorderStyle;
import com.fr.base.Style;
import com.fr.design.gui.ibutton.UIToggleButton;
import com.fr.design.gui.icombobox.LineComboBox;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import com.fr.stable.Constants;
import com.fr.stable.CoreConstants;
import com.fr.design.style.color.NewColorSelectBox;

/**
 * @author zhou
 * @since 2012-5-28����6:22:04
 */
public class BorderPane extends AbstractBasicStylePane {
	private boolean insideMode = false;

	private UIToggleButton topToggleButton;
	private UIToggleButton horizontalToggleButton;
	private UIToggleButton bottomToggleButton;
	private UIToggleButton leftToggleButton;
	private UIToggleButton verticalToggleButton;
	private UIToggleButton rightToggleButton;

	private UIToggleButton innerToggleButton;
	private UIToggleButton outerToggleButton;

	private LineComboBox currentLineCombo;
	private NewColorSelectBox currentLineColorPane;

	public BorderPane() {
		this.initComponents();
	}

	protected void initComponents() {
		topToggleButton = new UIToggleButton(BaseUtils.readIcon("/com/fr/base/images/dialog/border/top.png"));
		leftToggleButton = new UIToggleButton(BaseUtils.readIcon("/com/fr/base/images/dialog/border/left.png"));
		bottomToggleButton = new UIToggleButton(BaseUtils.readIcon("/com/fr/base/images/dialog/border/bottom.png"));
		rightToggleButton = new UIToggleButton(BaseUtils.readIcon("/com/fr/base/images/dialog/border/right.png"));
		horizontalToggleButton = new UIToggleButton(BaseUtils.readIcon("/com/fr/base/images/dialog/border/horizontal.png"));
		verticalToggleButton = new UIToggleButton(BaseUtils.readIcon("/com/fr/base/images/dialog/border/vertical.png"));
		this.currentLineCombo = new LineComboBox(CoreConstants.UNDERLINE_STYLE_ARRAY);
		this.currentLineColorPane = new NewColorSelectBox(100);

		this.setLayout(new BorderLayout(0, 6));

		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;

		double[] columnSize1 = {p, f};
		double[] rowSize1 = {p, p};


		Component[][] components1 = new Component[][]{
				new Component[]{new UILabel(Inter.getLocText("Style") + ":"), currentLineCombo},
				new Component[]{new UILabel(Inter.getLocText("Color") + ":"), currentLineColorPane},
		};

		JPanel northPane = TableLayoutHelper.createTableLayoutPane(components1, rowSize1, columnSize1);


		double[] columnSize2 = {p, f};
		double[] rowSize2 = {p, p};
		JPanel externalPane = new JPanel(new GridLayout(0, 4));
		externalPane.add(topToggleButton);
		externalPane.add(leftToggleButton);
		externalPane.add(bottomToggleButton);
		externalPane.add(rightToggleButton);
		JPanel insidePane = new JPanel(new GridLayout(0, 2));
		insidePane.add(horizontalToggleButton);
		insidePane.add(verticalToggleButton);


		Component[][] components2 = new Component[][]{
				new Component[]{outerToggleButton = new UIToggleButton(BaseUtils.readIcon("com/fr/design/images/m_format/out.png")), innerToggleButton = new UIToggleButton(BaseUtils.readIcon("com/fr/design/images/m_format/in.png"))},
				new Component[]{externalPane, insidePane,}
		};
		JPanel centerPane = TableLayoutHelper.createTableLayoutPane(components2, rowSize2, columnSize2);

		this.setLayout(new BorderLayout(0, 6));
		this.add(northPane, BorderLayout.NORTH);
		this.add(centerPane, BorderLayout.CENTER);


		this.currentLineColorPane.setSelectObject(Color.BLACK);

		outerToggleButton.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				boolean value = outerToggleButton.isSelected();
				topToggleButton.setSelected(value);
				bottomToggleButton.setSelected(value);
				leftToggleButton.setSelected(value);
				rightToggleButton.setSelected(value);
			}
		});

		innerToggleButton.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				boolean value = innerToggleButton.isSelected();
				horizontalToggleButton.setSelected(value);
				verticalToggleButton.setSelected(value);
			}
		});

	}

	@Override
	public String title4PopupWindow() {
		return Inter.getLocText("Border");
	}

	public void populate(Style style) {
		if (style == null) {
			style = Style.DEFAULT_STYLE;
		}

		CellBorderStyle cellBorderStyle = new CellBorderStyle();
		cellBorderStyle.setTopStyle(style.getBorderTop());
		cellBorderStyle.setTopColor(style.getBorderTopColor());
		cellBorderStyle.setLeftStyle(style.getBorderLeft());
		cellBorderStyle.setLeftColor(style.getBorderLeftColor());
		cellBorderStyle.setBottomStyle(style.getBorderBottom());
		cellBorderStyle.setBottomColor(style.getBorderBottomColor());
		cellBorderStyle.setRightStyle(style.getBorderRight());
		cellBorderStyle.setRightColor(style.getBorderRightColor());

		this.populateBean(cellBorderStyle, false, style.getBorderTop(), style.getBorderTopColor());
	}

	public void populateBean(CellBorderStyle cellBorderStyle, boolean insideMode, int currentStyle, Color currentColor) {
		this.insideMode = insideMode;

		this.currentLineCombo.setSelectedLineStyle(cellBorderStyle.getTopStyle() == Constants.LINE_NONE ? Constants.LINE_THIN : cellBorderStyle.getTopStyle());
		this.currentLineColorPane.setSelectObject(cellBorderStyle.getTopColor());

		this.topToggleButton.setSelected(cellBorderStyle.getTopStyle() != Constants.LINE_NONE);
		this.bottomToggleButton.setSelected(cellBorderStyle.getBottomStyle() != Constants.LINE_NONE);
		this.leftToggleButton.setSelected(cellBorderStyle.getLeftStyle() != Constants.LINE_NONE);
		this.rightToggleButton.setSelected(cellBorderStyle.getRightStyle() != Constants.LINE_NONE);

		this.horizontalToggleButton.setSelected(cellBorderStyle.getHorizontalStyle() != Constants.LINE_NONE);
		this.verticalToggleButton.setSelected(cellBorderStyle.getVerticalStyle() != Constants.LINE_NONE);

		this.innerToggleButton.setSelected(cellBorderStyle.getInnerBorder() != Constants.LINE_NONE);
		this.outerToggleButton.setSelected(cellBorderStyle.getOuterBorderStyle() != Constants.LINE_NONE);

        this.innerToggleButton.setEnabled(this.insideMode);
		this.horizontalToggleButton.setEnabled(this.insideMode);
		this.verticalToggleButton.setEnabled(this.insideMode);

	}

	public Style update(Style style) {
		if (style == null) {
			style = Style.DEFAULT_STYLE;
		}

		CellBorderStyle cellBorderStyle = this.update();

		style = style.deriveBorder(cellBorderStyle.getTopStyle(), cellBorderStyle.getTopColor(), cellBorderStyle.getBottomStyle(), cellBorderStyle.getBottomColor(),
				cellBorderStyle.getLeftStyle(), cellBorderStyle.getLeftColor(), cellBorderStyle.getRightStyle(), cellBorderStyle.getRightColor());

		return style;
	}

	public CellBorderStyle update() {
		int lineStyle = currentLineCombo.getSelectedLineStyle();
		Color lineColor = currentLineColorPane.getSelectObject();
		CellBorderStyle cellBorderStyle = new CellBorderStyle();
		cellBorderStyle.setTopColor(lineColor);
		cellBorderStyle.setTopStyle(topToggleButton.isSelected() ? lineStyle : Constants.LINE_NONE);
		cellBorderStyle.setBottomColor(lineColor);
		cellBorderStyle.setBottomStyle(bottomToggleButton.isSelected() ? lineStyle : Constants.LINE_NONE);
		cellBorderStyle.setLeftColor(lineColor);
		cellBorderStyle.setLeftStyle(leftToggleButton.isSelected() ? lineStyle : Constants.LINE_NONE);
		cellBorderStyle.setRightColor(lineColor);
		cellBorderStyle.setRightStyle(rightToggleButton.isSelected() ? lineStyle : Constants.LINE_NONE);
		cellBorderStyle.setVerticalColor(lineColor);
		cellBorderStyle.setVerticalStyle(verticalToggleButton.isSelected() ? lineStyle : Constants.LINE_NONE);
		cellBorderStyle.setHorizontalColor(lineColor);
		cellBorderStyle.setHorizontalStyle(horizontalToggleButton.isSelected() ? lineStyle : Constants.LINE_NONE);
		return cellBorderStyle;
	}
}
