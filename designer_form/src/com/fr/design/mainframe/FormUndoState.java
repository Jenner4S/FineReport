package com.fr.design.mainframe;

import java.awt.Dimension;

import com.fr.form.main.Form;
import com.fr.form.ui.Widget;

public class FormUndoState extends BaseUndoState<BaseJForm> {
	private Form form;
	private Dimension designerSize;
	private int hValue;
	private int vValue;
	private Widget[] selectWidgets;
	private double widthValue;
	private double heightValue;
	private double slideValue;

	public FormUndoState(BaseJForm t, FormArea formArea) {
		super(t);

		try {
			this.form = (Form) ((Form) t.getTarget()).clone();
		} catch (CloneNotSupportedException ex) {
			throw new RuntimeException(ex);
		}

		this.selectWidgets = formArea.getFormEditor().getSelectionModel().getSelection().getSelectedWidgets();
		this.hValue = formArea.getHorizontalValue();
		this.vValue = formArea.getVerticalValue();
		this.designerSize = formArea.getAreaSize();
		this.widthValue = formArea.getWidthPaneValue();
		this.heightValue = formArea.getHeightPaneValue();
		this.slideValue = formArea.getSlideValue();
	}

	/**
	 * ����form
	 */
	public Form getForm() {
		return form;
	}

	/**
	 * ����ѡ�еĿؼ�
	 */
	public Widget[] getSelectWidgets() {
		return selectWidgets;
	}

	/**
	 * ����design�����С
	 */
	public Dimension getAreaSize() {
		return designerSize;
	}

	/**
	 * ���غ��������ֵ
	 */
	public int getHorizontalValue() {
		return hValue;
	}

	/**
	 * �������������ֵ
	 */
	public int getVerticalValue() {
		return vValue;
	}
	
	/**
	 * ��������ʵ�ʿ��
	 */
	public double getWidthValue() {
		return this.widthValue;
	}
	
	/**
	 * ��������ʵ�ʸ߶�
	 */
	public double getHeightValue() {
		return this.heightValue;
	}
	
	/**
	 * �����趨�İٷֱ�ֵ
	 */
	public double getSlideValue() {
		return this.slideValue;
	}

	@Override
	public void applyState() {
		this.getApplyTarget().applyUndoState4Form(this);
	}
}