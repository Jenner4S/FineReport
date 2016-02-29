/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.file.export;

import com.fr.base.BaseUtils;
import com.fr.design.mainframe.JWorkBook;
import com.fr.design.menu.KeySetUtils;
import com.fr.file.filter.ChooseFileFilter;
import com.fr.general.Inter;
import com.fr.io.exporter.Exporter;
import com.fr.io.exporter.PDFExporter;

/**
 * Export pdf
 */
public class PDFExportAction extends AbstractExportAction {
    /**
     * Constructor
     */
	public PDFExportAction(JWorkBook jwb) {
		super(jwb);
        this.setMenuKeySet(KeySetUtils.PDF_EXPORT);
        this.setName(getMenuKeySet().getMenuKeySetName()+"...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_file/pdf.png"));
    }

    @Override
	protected Exporter getExporter() {
        return new PDFExporter();
    }

    @Override
	protected ChooseFileFilter getChooseFileFilter() {
        return new ChooseFileFilter(new String[]{"pdf"}, Inter.getLocText("Export-PDF"));
    }

    @Override
	protected String getDefaultExtension() {
        return "pdf";
    }

}