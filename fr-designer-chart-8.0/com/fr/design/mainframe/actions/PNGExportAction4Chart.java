// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.actions;

import com.fr.base.BaseUtils;
import com.fr.design.mainframe.JChart;
import com.fr.design.mainframe.exporter.Exporter4Chart;
import com.fr.design.mainframe.exporter.ImageExporter4Chart;
import com.fr.design.menu.MenuKeySet;
import com.fr.file.filter.ChooseFileFilter;
import com.fr.general.Inter;
import javax.swing.KeyStroke;

// Referenced classes of package com.fr.design.mainframe.actions:
//            AbstractExportAction4JChart

public class PNGExportAction4Chart extends AbstractExportAction4JChart
{

    private MenuKeySet menuSet;

    public PNGExportAction4Chart(JChart jchart)
    {
        super(jchart);
        menuSet = new MenuKeySet() {

            final PNGExportAction4Chart this$0;

            public char getMnemonic()
            {
                return 'M';
            }

            public String getMenuName()
            {
                return Inter.getLocText("FR-Chart-Format_Image");
            }

            public KeyStroke getKeyStroke()
            {
                return KeyStroke.getKeyStroke(77, 2);
            }

            
            {
                this$0 = PNGExportAction4Chart.this;
                super();
            }
        }
;
        setMenuKeySet(menuSet);
        setName(getMenuKeySet().getMenuKeySetName());
        setMnemonic(getMenuKeySet().getMnemonic());
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/exportimg.png"));
    }

    protected ChooseFileFilter getChooseFileFilter()
    {
        return new ChooseFileFilter(new String[] {
            "png"
        }, Inter.getLocText("Image"));
    }

    protected String getDefaultExtension()
    {
        return "png";
    }

    protected Exporter4Chart getExporter()
    {
        return new ImageExporter4Chart();
    }
}
