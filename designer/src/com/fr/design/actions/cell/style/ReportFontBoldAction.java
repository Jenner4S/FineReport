/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.cell.style;

import com.fr.base.BaseUtils;
import com.fr.base.Style;
import com.fr.base.core.StyleUtils;
import com.fr.design.actions.ToggleButtonUpdateAction;
import com.fr.design.gui.ibutton.UIToggleButton;
import com.fr.general.FRFont;
import com.fr.general.Inter;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.utils.gui.GUICoreUtils;

/**
 * Bold.
 */
public class ReportFontBoldAction extends AbstractStyleAction implements ToggleButtonUpdateAction {
	private UIToggleButton button;
	protected Style style;

	public ReportFontBoldAction(ElementCasePane t) {
		super(t);

		this.setName(Inter.getLocText("FRFont-bold"));
		this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/bold.png"));
	}

	/**
	 * ���ݰ�ť״̬��ȡ��ʽ
	 *
	 * @param style
	 * @param defStyle
	 * @return
	 */
	@Override
	public Style executeStyle(Style style, Style defStyle) {
		createToolBarComponent().setSelected(!createToolBarComponent().isSelected());
		if (createToolBarComponent().isSelected()) {
			setSelectedFont(style);
			createToolBarComponent().setSelected(false);
		} else {
			setUnselectedFont(style);
			createToolBarComponent().setSelected(true);
		}

		return this.style;
	}

	protected void setSelectedFont(Style style) {
		this.style = StyleUtils.boldReportFont(style);
	}

	protected void setUnselectedFont(Style style) {
		this.style = StyleUtils.unBoldReportFont(style);
	}

	/**
	 * Update Style.
	 */
	@Override
	public void updateStyle(Style style) {
		if (style == null) {
			return;
		}
		FRFont frFont = style.getFRFont();
		if (frFont == null) {
			return;
		}
		createToolBarComponent().setSelected(isStyle(frFont));
	}

	protected boolean isStyle(FRFont frFont) {
		return frFont.isBold();
	}

	/**
	 * Gets component on toolbar.
	 *
	 * @return the created components on toolbar.
	 */
	@Override
	public UIToggleButton createToolBarComponent() {
		if (button == null) {
			button = GUICoreUtils.createToolBarComponent(this);
			button.setEventBannded(true);
		}
		return button;
	}
}
