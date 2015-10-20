package com.fr.design.gui.columnrow;

import java.awt.BorderLayout;

import com.fr.design.gui.ilable.UILabel;
import javax.swing.JPanel;

import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;


/**
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version ����ʱ�䣺2011-12-29 ����02:28:43
 * ��˵��: ����ѡ�����  ֻ�Ǻ�ColumnRowPane�Ĳ��ֲ�ͬ.
 */
public class ColumnRowVerticalPane extends ColumnRowPane {

	private static final long serialVersionUID = 2222494927594030309L;

	public ColumnRowVerticalPane( ) {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		
		JPanel pane = FRGUIPaneFactory.createYBoxEmptyBorderPane();
		this.add(pane, BorderLayout.NORTH);
		
		JPanel colPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		colPane.add(new UILabel(Inter.getLocText("Column") + ":"));
		pane.add(colPane);
		
		initColSpinner();
		colPane.add(columnSpinner);
		
		JPanel rowPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		pane.add(rowPane);
		
		rowPane.add(new UILabel(Inter.getLocText("Row") + ":"));
		initRowSpinner();
		rowPane.add(rowSpinner);
		
		this.addDocumentListener(d);
	}
}
