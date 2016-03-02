package com.fr.design.mainframe.cell.settingpane;

import java.awt.*;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fr.base.Style;
import com.fr.design.mainframe.cell.settingpane.style.StylePane;
import com.fr.general.Inter;
import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.elementcase.TemplateElementCase;

/**
 * @author zhou
 * @since 2012-5-11下午3:59:39
 */
public class CellStylePane extends AbstractCellAttrPane {
	private StylePane stylePane;
//	private UIButtonGroup<Boolean> isCopy;
	
//	
//	private TemplateCellElement copyCellElement=null;
//
//	private ElementCasePane copyCasePane=null;
	
	
	@Override
	public JPanel createContentPane() {
		JPanel content = new JPanel(new BorderLayout());
		
		stylePane = new StylePane();
//		isCopy = new UIButtonGroup<Boolean>(new String[]{"复制", "不复制"}, new Boolean[]{true, false});
//		isCopy.setSelectedIndex(1);
//		content.add(isCopy, BorderLayout.NORTH);
//		isCopy.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                copySetting();
//            }
//			
//        });
//		label = new UILabel("    ");
//		content.add(label, BorderLayout.SOUTH);
		
		content.add(stylePane, BorderLayout.CENTER);
		stylePane.addPredefinedChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				attributeChanged();
			}
		});
//		stylePane.getAncestorListeners()[0].
		stylePane.addCustomTabChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				adjustValues();// 里面的Tab切换后要及时调整滚动条,因为一些界面可能不需要滚动条
			}
		});
		return content;
	}

	
	//新加方法
//	private void copySetting() {
//		// TODO Auto-generated method stub
//		copyCellElement = cellElement;
//		copyCasePane = elementCasePane;
////		isCopy.setSelectedIndex(1);
//	}
//	
	
	
	@Override
	public String getIconPath() {
		return "com/fr/design/images/m_format/cell.png";
	}

	@Override
	public void updateBean(TemplateCellElement cellElement) {
		cellElement.setStyle(stylePane.updateBean());
		
	}

	@Override
	public void updateBeans() {
		if (stylePane.getSelectedIndex() == 1) {
			Style s = stylePane.updateBean();
			TemplateElementCase elementCase = elementCasePane.getEditingElementCase();
			int cellRectangleCount = cs.getCellRectangleCount();
			for (int rect = 0; rect < cellRectangleCount; rect++) {
				Rectangle cellRectangle = cs.getCellRectangle(rect);
				for (int j = 0; j < cellRectangle.height; j++) {
					for (int i = 0; i < cellRectangle.width; i++) {
						int column = i + cellRectangle.x;
						int row = j + cellRectangle.y;
						TemplateCellElement cellElement = elementCase.getTemplateCellElement(column, row);
						if (cellElement == null) {
							cellElement = new DefaultTemplateCellElement(column, row);
							elementCase.addCellElement(cellElement);
						}
						cellElement.setStyle(s);
					}
				}
			}
		} else {
			TemplateElementCase elementCase = elementCasePane.getEditingElementCase();
			int cellRectangleCount = cs.getCellRectangleCount();
			for (int rect = 0; rect < cellRectangleCount; rect++) {
				Rectangle cellRectangle = cs.getCellRectangle(rect);
				for (int j = 0; j < cellRectangle.height; j++) {
					for (int i = 0; i < cellRectangle.width; i++) {
						int column = i + cellRectangle.x;
						int row = j + cellRectangle.y;
						TemplateCellElement cellElement = elementCase.getTemplateCellElement(column, row);
						if (cellElement == null) {
							cellElement = new DefaultTemplateCellElement(column, row);
							elementCase.addCellElement(cellElement);
						}
						Style style = cellElement.getStyle();
						if (style == null) {
							style = style.DEFAULT_STYLE;
						}
						style = stylePane.updateStyle(style);
						cellElement.setStyle(style);
					}
				}
			}
			stylePane.updateBorder();// border必须特别处理
		}
	}

	@Override
	protected void populateBean() {

//		if(isCopy.getSelectedItem()&&copyCasePane!=null&&copyCellElement!=null){
//			stylePane.populateBean(copyCellElement.getStyle());
//			stylePane.dealWithBorder(copyCasePane);
//			
//			return;
//		}
		stylePane.populateBean(cellElement.getStyle());
		stylePane.dealWithBorder(elementCasePane);

	}

	@Override
	public String title4PopupWindow() {
		return Inter.getLocText("Style");
	}

	public void setSelectedByIds(int level, String... id) {
		stylePane.setSelctedByName(id[level]);
	}

}