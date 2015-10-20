package com.fr.design.widget.ui.btn;

import java.awt.Component;

import javax.swing.BorderFactory;
import com.fr.design.gui.ilable.UILabel;
import javax.swing.JPanel;

import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.dialog.BasicPane;
import com.fr.design.editor.editor.ColumnRowEditor;
import com.fr.general.Inter;
import com.fr.report.web.button.write.AppendRowButton;

/**
 * Created by IntelliJ IDEA. Author : Richer Version: 6.5.6 Date : 11-11-16 Time
 * : ����9:34
 */
public class DefineAppendColumnRowPane extends BasicPane {
	private ColumnRowEditor crEditor;
	private com.fr.design.editor.editor.IntegerEditor jNumberEditor;
	private UILabel rowCountLable;

	public DefineAppendColumnRowPane() {
		this.initComponents();
	}

	private void initComponents() {
		double p = TableLayout.PREFERRED;
		double rowSize[] = { p, p };
		double columnSize[] = { p, p, p };

		crEditor = new ColumnRowEditor();
		jNumberEditor = new com.fr.design.editor.editor.IntegerEditor();

		rowCountLable = new UILabel(Inter.getLocText("Edit-Row_Count") + ":");
		JPanel lpane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		lpane.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
		lpane.add(new UILabel(Inter.getLocText("Append_Delete_Row_Message")));
		Component[][] components = { { new UILabel(Inter.getLocText(new String[]{"Specify", "Cell"}) + ":"), crEditor, lpane }, { rowCountLable, jNumberEditor } };
		JPanel contentPane = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
		contentPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		this.add(contentPane);
	}

	@Override
	protected String title4PopupWindow() {
		return "Button";
	}

	public void populate(AppendRowButton btn) {
		crEditor.setValue(btn.getFixCell());
		jNumberEditor.setValue(new Integer(btn.getCount()));
	}

	public void update(AppendRowButton btn) {
		btn.setFixCell(crEditor.getValue());
		btn.setCount(jNumberEditor.getValue().intValue());
	}
}
