package com.bit.plugin.combox;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.fr.design.data.DataCreatorUI;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.present.dict.DictionaryPane;
import com.fr.design.widget.ui.CheckBoxDictPane;
import com.fr.design.widget.ui.CustomWritableRepeatEditorPane;
import com.fr.form.ui.ComboCheckBox;
import com.fr.general.Inter;

public class CellComboBoxUI extends CustomWritableRepeatEditorPane<MyCombox> {
	private CheckBoxDictPane checkBoxDictPane;
	private DictionaryPane dictPane;
    private UICheckBox supportTagCheckBox;
//	private UICheckBox muti;
	private UIComboBox mutiCheck;

	public CellComboBoxUI() {
		super.initComponents();
		dictPane = new DictionaryPane();
	}

	@Override
	protected JPanel setForthContentPane() {
		JPanel muticheckPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		muticheckPane.add(new UILabel("类型"/*Inter.getLocText("Widget-Date_Selector_Return_Type")*/ + ":"), BorderLayout.WEST);
		mutiCheck = new UIComboBox(new String[]{"多选  "/*Inter.getLocText("Widget-Array")*/, "单选  "/*Inter.getLocText("String")*/});
		muticheckPane.add(mutiCheck, BorderLayout.CENTER);	
		
		

		mutiCheck.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				checkVisible();
			}				
		});
		
		
		JPanel attrPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_S_Pane();
		attrPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		JPanel contenter = FRGUIPaneFactory.createBorderLayout_L_Pane();
        contenter.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		attrPane.add(muticheckPane);
        checkBoxDictPane = new CheckBoxDictPane();
		attrPane.add(contenter);
        //是否以标签形式显示
        JPanel tagPane = FRGUIPaneFactory.createMediumHGapFlowInnerContainer_M_Pane();
//        muti = new UICheckBox("是否多选", true);
        supportTagCheckBox = new UICheckBox(Inter.getLocText("Form-SupportTag"), true);
//        tagPane.add(muti);
        tagPane.add(supportTagCheckBox);
        contenter.add(tagPane, BorderLayout.NORTH);

        contenter.add(checkBoxDictPane, BorderLayout.WEST);
		return attrPane;
	}
	private void checkVisible(){
		supportTagCheckBox.setVisible(mutiCheck.getSelectedIndex() == 0);
		checkBoxDictPane.setVisible(mutiCheck.getSelectedIndex() == 0);
//		delimiterPane.setVisible(returnTypeComboBox.getSelectedIndex() == 1);
//		startPane.setVisible(returnTypeComboBox.getSelectedIndex() == 1);
//		endPane.setVisible(returnTypeComboBox.getSelectedIndex() == 1);
	}
	@Override
	protected void populateSubCustomWritableRepeatEditorBean(MyCombox e) {
		this.dictPane.populateBean(e.getDictionary());
		this.checkBoxDictPane.populate(e.createComboCheckBox());
        this.supportTagCheckBox.setSelected(e.isSupportTag());
        if(e.isMutiSelect()){
        	mutiCheck.setSelectedIndex(0);
        }else{
        	mutiCheck.setSelectedIndex(1);
        }
	}

	@Override
	protected MyCombox updateSubCustomWritableRepeatEditorBean() {
		MyCombox combo = new MyCombox();
        combo.setSupportTag(this.supportTagCheckBox.isSelected());
		combo.setDictionary(this.dictPane.updateBean());
		ComboCheckBox co = new ComboCheckBox();
		checkBoxDictPane.update(co);
		combo.setComboCheckBox(co);
		combo.setMutiSelect(mutiCheck.getSelectedIndex()==0);
		return combo;
	}

	@Override
	public DataCreatorUI dataUI() {
		return dictPane;
	}
	
	@Override
	protected String title4PopupWindow() {
		return "MyCombox";
	}

}