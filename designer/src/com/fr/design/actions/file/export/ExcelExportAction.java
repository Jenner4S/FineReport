package com.fr.design.actions.file.export;

import com.fr.base.BaseUtils;
import com.fr.design.mainframe.JWorkBook;
import com.fr.design.menu.KeySetUtils;
import com.fr.io.exporter.ExcelExporter;
import com.fr.io.exporter.Exporter;
import com.fr.io.exporter.LargeDataPageExcelExporter;
import com.fr.main.TemplateWorkBook;
import com.fr.report.core.ReportUtils;

public class ExcelExportAction extends AbstractExcelExportAction {
    /**
     * Constructor
     */
	public ExcelExportAction(JWorkBook jwb) {
		super(jwb);
        this.setMenuKeySet(KeySetUtils.SIMPLE_EXCEL_EXPORT);
        this.setName(getMenuKeySet().getMenuKeySetName());
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_file/excel.png"));
    }
	
    @Override
	protected Exporter getExporter() {
    	TemplateWorkBook tpl = this.getTemplateWorkBook();
    	if (hasLayerReport(tpl)) {
    		return new LargeDataPageExcelExporter(ReportUtils.getPaperSettingListFromWorkBook(tpl), false);
    	} else {
    		return new ExcelExporter(ReportUtils.getPaperSettingListFromWorkBook(tpl));
    	}
    }
}
