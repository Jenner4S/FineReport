package com.fr.design.mainframe.chart.gui.style;

import com.fr.base.BaseUtils;
import com.fr.base.Formula;
import com.fr.base.Style;
import com.fr.base.Utils;
import com.fr.chart.chartattr.Axis;
import com.fr.chart.chartattr.Title;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.dialog.BasicPane;
import com.fr.general.FRFont;
import com.fr.general.Inter;
import com.fr.stable.Constants;
import com.fr.stable.StableUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChartAxisTitlePane extends BasicPane{
	private static final long serialVersionUID = -5292443330303230680L;
	
	private UICheckBox isAxisTitleVisable;
	private TinyFormulaPane axisTitleContentPane;
	private ChartTextAttrPane axisTitleAttrPane;
	private UIButtonGroup<Integer> titleAlignmentPane;
	private JPanel titlePane;
	
	public ChartAxisTitlePane(){
		initComponents();
	}
	
	private void initComponents(){
		isAxisTitleVisable = new UICheckBox(Inter.getLocText("Axis_Title"));
		axisTitleContentPane = new TinyFormulaPane();
		axisTitleAttrPane = new ChartTextAttrPane();
		axisTitleAttrPane.populate(FRFont.getInstance("Microsoft YaHei", Font.PLAIN, 9));
		Icon[] alignmentIconArray = {BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/h_left_normal.png"),
				BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/h_center_normal.png"),
				BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/h_right_normal.png")};
		Integer[] alignment = new Integer[] { Constants.LEFT, Constants.CENTER, Constants.RIGHT };
		titleAlignmentPane = new UIButtonGroup<Integer>(alignmentIconArray, alignment);
		titleAlignmentPane.setSelectedItem(Constants.CENTER);
		
		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
		double[] columnSize = { LayoutConstants.CHART_ATTR_TOMARGIN, f };
		double[] rowSize = { p, p, p, p};
        Component[][] components = new Component[][]{
                new Component[]{null,axisTitleContentPane},
                new Component[]{null,axisTitleAttrPane},
                new Component[]{null,new UILabel(Inter.getLocText("Alignment-Style"))},
                new Component[]{null,titleAlignmentPane,},
        };
        titlePane = TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
        
		isAxisTitleVisable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkTitleUse();
			}
		});
		
		double[] row = {p,p};
		double[] col = {f};
		this.setLayout(new BorderLayout());
		JPanel panel = TableLayoutHelper.createTableLayoutPane(new Component[][]{
										new Component[]{isAxisTitleVisable}, new Component[]{titlePane}}, row, col);
		this.add(panel, BorderLayout.CENTER);
	}
	
	private void checkTitleUse() {
		isAxisTitleVisable.setEnabled(true);
        titlePane.setVisible(isAxisTitleVisable.isSelected());
    }
	
	@Override
	protected String title4PopupWindow() {
		return "";
	}
	
	@Override
	public Dimension getPreferredSize(){
		if(this.isAxisTitleVisable.isSelected()){
			return super.getPreferredSize();
		}else{
			return this.isAxisTitleVisable.getPreferredSize();
		}
	}
	
	public void update(Axis axis) {
		if(isAxisTitleVisable.isSelected()) {
			if(axis.getTitle() == null) {
				Title newTitle = new Title(Inter.getLocText(new String[]{"ChartF-Axis", "ChartF-Title"}));
				
				axis.setTitle(newTitle);
				axisTitleContentPane.populateBean(Utils.objectToString(newTitle.getTextObject()));
				axisTitleAttrPane.populate(FRFont.getInstance("Microsoft YaHei", Font.PLAIN, 9));
			}
			Title title = axis.getTitle();
			title.setTitleVisble(true);
			if(axis.getPosition() == Constants.LEFT || axis.getPosition() == Constants.RIGHT) {
				title.getTextAttr().setAlignText(Style.VERTICALTEXT);
			}
			if(StableUtils.canBeFormula(axisTitleContentPane.updateBean())) {
				title.setTextObject(new Formula(axisTitleContentPane.updateBean()));
			} else {
				title.setTextObject(axisTitleContentPane.updateBean());
			}
			axisTitleAttrPane.update(title.getTextAttr());
			title.setPosition(titleAlignmentPane.getSelectedItem());
		} else {
			if(axis.getTitle() != null) {
				axis.getTitle().setTitleVisble(false);
			}
		}
		makeTitleAlignText(axis);
	}
	
	private void makeTitleAlignText(Axis axis) {
		if(axis.getPosition() == Constants.LEFT || axis.getPosition() == Constants.RIGHT) {
			Title title = axis.getTitle();
			if(title != null) {
				title.getTextAttr().setAlignText(Style.VERTICALTEXT);
			}
		}
	}
	
	public void populate(Axis axis){
		isAxisTitleVisable.setSelected(axis.getTitle() != null && axis.getTitle().isTitleVisble());
		if(isAxisTitleVisable.isSelected()) {
			axisTitleContentPane.populateBean(Utils.objectToString(axis.getTitle().getTextObject()));
			axisTitleAttrPane.populate(axis.getTitle().getTextAttr());
			titleAlignmentPane.setSelectedItem(axis.getTitle().getPosition());
		}
		
		checkTitleUse();
	}

}
