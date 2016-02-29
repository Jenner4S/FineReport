/*
 * Copyright(c) 2001-2010, FineReport  Inc, All Rights Reserved.
 */
package com.fr.grid;

import java.awt.Dimension;

import com.fr.base.GraphHelper;
import com.fr.design.mainframe.ElementCasePane;

/**
 * GridRow used to paint and edit grid row.
 * 
 * @editor zhou
 * @since 2012-3-22下午6:12:03
 */
public class GridRow extends GridHeader<Integer> {
	@Override
	protected void initByConstructor() {
		GridRowMouseHandler gridRowMouseHandler = new GridRowMouseHandler(this);
		this.addMouseListener(gridRowMouseHandler);
		this.addMouseMotionListener(gridRowMouseHandler);
		this.updateUI();
	}

	@Override
	public Integer getDisplay(int index) {
		return new Integer(index + 1);
	}

	@Override
	public void updateUI() {
		this.setUI(new GridRowUI());
	}

	/**
	 * Gets the preferred size.
	 */
	@Override
	public Dimension getPreferredSize() {
		ElementCasePane reportPane = this.getElementCasePane();

		if (!(reportPane.isRowHeaderVisible())) {
			return new Dimension(0, 0);
		}

		int maxCharNumber = this.caculateMaxCharNumber(reportPane);
		return new Dimension(maxCharNumber * GraphHelper.getFontMetrics(this.getFont()).charWidth('M'), super.getPreferredSize().height);
	}

	/**
	 * Calculates max char number.
	 */
	private int caculateMaxCharNumber(ElementCasePane reportPane) {
		int maxCharNumber = 5;
		maxCharNumber = Math.max(maxCharNumber, ("" + (reportPane.getGrid().getVerticalValue() + reportPane.getGrid().getVerticalExtent())).length() + 1);

		return maxCharNumber;
	}
}