// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.actions;

import com.fr.base.BaseUtils;
import com.fr.design.mainframe.JChart;
import com.fr.design.mainframe.exporter.Exporter4Chart;
import com.fr.design.mainframe.exporter.PdfExporter4Chart;
import com.fr.design.menu.MenuKeySet;
import com.fr.file.filter.ChooseFileFilter;
import com.fr.general.Inter;
import javax.swing.KeyStroke;

// Referenced classes of package com.fr.design.mainframe.actions:
//            AbstractExportAction4JChart

public class PDFExportAction4Chart extends AbstractExportAction4JChart
{

    private MenuKeySet pdf;

    public PDFExportAction4Chart(JChart jchart)
    {
        super(jchart);
        pdf = new MenuKeySet() {

            final PDFExportAction4Chart this$0;

            public char getMnemonic()
            {
                return 'P';
            }

            public String getMenuName()
            {
                return Inter.getLocText("FR-Chart-Format_PDF");
            }

            public KeyStroke getKeyStroke()
            {
                return null;
            }

            
            {
                this$0 = PDFExportAction4Chart.this;
                super();
            }
        }
;
        setMenuKeySet(pdf);
        setName(getMenuKeySet().getMenuKeySetName());
        setMnemonic(getMenuKeySet().getMnemonic());
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_file/pdf.png"));
    }

    protected ChooseFileFilter getChooseFileFilter()
    {
        return new ChooseFileFilter(new String[] {
            "pdf"
        }, Inter.getLocText("Export-PDF"));
    }

    protected String getDefaultExtension()
    {
        return "pdf";
    }

    protected Exporter4Chart getExporter()
    {
        return new PdfExporter4Chart();
    }
}
