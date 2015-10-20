package com.fr.design.mainframe.cell.settingpane;

import com.fr.base.present.Present;
import com.fr.design.present.PresentPane;
import com.fr.general.Inter;
import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.elementcase.TemplateElementCase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * @author zhou
 * @since 2012-5-11����5:24:35
 */
public class CellPresentPane extends AbstractCellAttrPane {
	private PresentPane presentPane;

    /**
     * ��ʼ�����
     * @return   ���
     */
	public JPanel createContentPane() {
		presentPane = new PresentPane();
		JPanel content = new JPanel(new BorderLayout());
		content.add(presentPane, BorderLayout.CENTER);
		presentPane.addTabChangeListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				adjustValues();
			}
		});
		return content;
	}

	@Override
	public String getIconPath() {
		return "com/fr/design/images/data/source/dataDictionary.png";
	}

	@Override
	public void updateBean(TemplateCellElement cellElement) {
		cellElement.setPresent(presentPane.updateBean());
	}

    /**
     * ����
     */
	public void updateBeans() {
        Present present = presentPane.updateBean();
        TemplateElementCase elementCase = elementCasePane.getEditingElementCase();
        int cellRectangleCount = cs.getCellRectangleCount();
        for (int rect = 0; rect < cellRectangleCount; rect++) {
            Rectangle cellRectangle = cs.getCellRectangle(rect);
            // ��Ҫ���к��е�������Ԫ�ء�
            for (int j = 0; j < cellRectangle.height; j++) {
                for (int i = 0; i < cellRectangle.width; i++) {
                    int column = i + cellRectangle.x;
                    int row = j + cellRectangle.y;
                    TemplateCellElement cellElement = elementCase.getTemplateCellElement(column, row);
                    if (cellElement == null) {
                        cellElement = new DefaultTemplateCellElement(column, row);
                        elementCase.addCellElement(cellElement);
                    }
                    cellElement.setPresent(present);
                }
            }
        }
	}

	@Override
	protected void populateBean() {
        //ѡ�е����е�Ԫ������̬�����Ա�Ż������ݣ������ǳ�ʼֵ
        //��Ҫ�ǽ��37664
		Present present = getSelectCellPresent();
        presentPane.populateBean(present);
	}

    private Present getSelectCellPresent(){
        TemplateElementCase elementCase = elementCasePane.getEditingElementCase();
        //��סctrlѡ�ж��cell��
        int cellRectangleCount = cs.getCellRectangleCount();
        
        for (int rect = 0; rect < cellRectangleCount; rect++) {
            Rectangle cellRectangle = cs.getCellRectangle(rect);
            for (int j = 0; j < cellRectangle.height; j++) {
                for (int i = 0; i < cellRectangle.width; i++) {
                    int column = i + cellRectangle.x;
                    int row = j + cellRectangle.y;
                    TemplateCellElement cellElement = elementCase.getTemplateCellElement(column, row);
                    if(cellElement == null || cellElement.getPresent() == null){
                       return null;
                    }
                }
            }
        }
        return cellElement.getPresent();
    }

    /**
     * �Ի������
     * @return    ����
     */
	public String title4PopupWindow() {
		return Inter.getLocText("FR-Chart-Style_Present");
	}

	public void setSelectedByIds(int level, String... id) {
		presentPane.setSelectedByName(id[level]);
	}


}
