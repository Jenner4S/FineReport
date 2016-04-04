// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.actions;

import com.fr.base.BaseUtils;
import com.fr.base.ExcelUtils;
import com.fr.design.mainframe.JChart;
import com.fr.design.mainframe.exporter.ExcelExporter4Chart;
import com.fr.design.mainframe.exporter.Exporter4Chart;
import com.fr.design.menu.MenuKeySet;
import com.fr.file.filter.ChooseFileFilter;
import com.fr.general.Inter;
import javax.swing.KeyStroke;

// Referenced classes of package com.fr.design.mainframe.actions:
//            AbstractExportAction4JChart

public class ExcelExportAction4Chart extends AbstractExportAction4JChart
{

    private MenuKeySet excel;

    public ExcelExportAction4Chart(JChart jchart)
    {
        super(jchart);
        excel = new MenuKeySet() {

            final ExcelExportAction4Chart this$0;

            public char getMnemonic()
            {
                return 'E';
            }

            public String getMenuName()
            {
                return Inter.getLocText("FR-Chart-Format_Excel");
            }

            public KeyStroke getKeyStroke()
            {
                return KeyStroke.getKeyStroke(69, 2);
            }

            
            {
                this$0 = ExcelExportAction4Chart.this;
                super();
            }
        }
;
        setMenuKeySet(excel);
        setName(getMenuKeySet().getMenuKeySetName());
        setMnemonic(getMenuKeySet().getMnemonic());
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_file/excel.png"));
    }

    protected ChooseFileFilter getChooseFileFilter()
    {
        return new ChooseFileFilter(new String[] {
            "xls", "xlsx"
        }, Inter.getLocText("Export-Excel"));
    }

    protected String getDefaultExtension()
    {
        return ExcelUtils.checkPOIJarExist() ? "xlsx" : "xls";
    }

    protected Exporter4Chart getExporter()
    {
        return new ExcelExporter4Chart();
    }
}
