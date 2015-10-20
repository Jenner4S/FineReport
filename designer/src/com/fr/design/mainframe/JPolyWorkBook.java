/**
 * 
 */
package com.fr.design.mainframe;

import com.fr.main.impl.WorkBook;
import com.fr.report.poly.PolyWorkSheet;

/**
 * �ۺϱ���Book, ��WorkBook�������ڲ��ܷ���WorkSheet.
 * 
 * @author neil
 *
 * @date: 2015-2-5-����8:58:39
 */
public class JPolyWorkBook extends JWorkBook {
	
	private static final String DEFAULT_NAME = "Poly";

	/**
	 * ���캯��
	 */
	public JPolyWorkBook() {
        super(new WorkBook(new PolyWorkSheet()), DEFAULT_NAME);
        populateReportParameterAttr();
	}
	
    
    /**
	 * ����sheet����tab���
	 * 
	 * @param reportCompositeX ��ǰ�������
	 * 
	 * @return sheet����tab���
	 * 
	 * @date 2015-2-5-����11:42:12
	 * 
	 */
    public SheetNameTabPane createSheetNameTabPane(ReportComponentComposite reportCompositeX){
    	return new PolySheetNameTabPane(reportCompositeX);
    }
}
