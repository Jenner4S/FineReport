package com.fr.design.widget.ui;

import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.ui.WriteAbleRepeatEditor;

import javax.swing.*;
import java.awt.*;

public abstract class WritableRepeatEditorPane<E extends WriteAbleRepeatEditor> extends DirectWriteEditorDefinePane<E> {
 //产品说先把这个注释掉
//	private UICheckBox removeRepeatCheckBox;

	public WritableRepeatEditorPane() {
		this.initComponents();
	}


	@Override
	protected JPanel setSecondContentPane() {
		JPanel contentPane = FRGUIPaneFactory.createBorderLayout_L_Pane();
		contentPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		JPanel contenter = FRGUIPaneFactory.createMediumHGapFlowInnerContainer_M_Pane();
		contentPane.add(contenter,BorderLayout.NORTH);
//		removeRepeatCheckBox = new UICheckBox(Inter.getLocText("Form-Remove_Repeat_Data"), false);
//		contenter.add(removeRepeatCheckBox);
		JPanel otherContentPane = this.setThirdContentPane();
		if (otherContentPane != null) {
			contentPane.add(otherContentPane,BorderLayout.CENTER);
		}
		return contentPane;
	}

	protected abstract JPanel setThirdContentPane();

	@Override
	protected void populateSubDirectWriteEditorBean(E e) {
		//this.removeRepeatCheckBox.setSelected(e.isRemoveRepeat());

		populateSubWritableRepeatEditorBean(e);
	}

	protected abstract void populateSubWritableRepeatEditorBean(E e);

	@Override
	protected E updateSubDirectWriteEditorBean() {
		E e = updateSubWritableRepeatEditorBean();

	//	e.setRemoveRepeat(this.removeRepeatCheckBox.isSelected());

		return e;
	}

	protected abstract E updateSubWritableRepeatEditorBean();
}
