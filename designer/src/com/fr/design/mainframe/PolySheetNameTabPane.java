package com.fr.design.mainframe;

import java.awt.Graphics2D;

import com.fr.design.menu.MenuDef;


/**
 * ��ȡ
 * 
 * @date 2015-2-5-����10:19:17
 * 
 */
public class PolySheetNameTabPane extends SheetNameTabPane{

	/**
	 * ���캯��
	 */
	public PolySheetNameTabPane(ReportComponentComposite reportCompositeX) {
		super(reportCompositeX);
	}
	
    protected void paintAddButton(Graphics2D g2d){
    	ADD_POLY_SHEET.paintIcon(this, g2d, iconLocation, 3);
    }
    
    
    protected void firstInsertActionPerformed(){
		new PolyReportInsertAction().actionPerformed(null);
    }

    
    protected void addInsertGridShortCut(MenuDef def){
    }
}
