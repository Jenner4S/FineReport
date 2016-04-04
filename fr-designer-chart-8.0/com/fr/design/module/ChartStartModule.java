// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.module;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.base.io.IOFile;
import com.fr.base.io.XMLEncryptUtils;
import com.fr.chart.base.ChartInternationalNameContentBean;
import com.fr.chart.charttypes.ChartTypeManager;
import com.fr.chart.module.ChartModule;
import com.fr.design.DesignerEnvManager;
import com.fr.design.chart.gui.ChartWidgetOption;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.mainframe.*;
import com.fr.file.FILE;
import com.fr.form.ui.ChartBook;
import com.fr.form.ui.ChartEditor;
import com.fr.general.*;

// Referenced classes of package com.fr.design.module:
//            ChartDesignerModule

public class ChartStartModule extends ChartDesignerModule
{

    public ChartStartModule()
    {
    }

    protected void dealBeforeRegister()
    {
        ModuleContext.startModule(com/fr/chart/module/ChartModule.getName());
    }

    protected void registerFloatEditor()
    {
    }

    protected WidgetOption[] options4Show()
    {
        ChartInternationalNameContentBean achartinternationalnamecontentbean[] = ChartTypeManager.getInstance().getAllChartBaseNames();
        ChartWidgetOption achartwidgetoption[] = new ChartWidgetOption[achartinternationalnamecontentbean.length];
        for(int i = 0; i < achartinternationalnamecontentbean.length; i++)
        {
            com.fr.chart.chartattr.Chart achart[] = ChartTypeManager.getInstance().getChartTypes(achartinternationalnamecontentbean[i].getPlotID());
            achartwidgetoption[i] = new ChartWidgetOption(Inter.getLocText(achartinternationalnamecontentbean[i].getName()), BaseUtils.readIcon((new StringBuilder()).append("com/fr/design/images/form/toolbar/").append(achartinternationalnamecontentbean[i].getName()).append(".png").toString()), com/fr/form/ui/ChartEditor, achart[0]);
        }

        return achartwidgetoption;
    }

    public App[] apps4TemplateOpener()
    {
        return (new App[] {
            new AbstractAppProvider() {

                final ChartStartModule this$0;

                public String[] defaultExtentions()
                {
                    return (new String[] {
                        "crt"
                    });
                }

                public JTemplate openTemplate(FILE file)
                {
                    return new JChart(asIOFile(file), file);
                }

                public ChartBook asIOFile(FILE file)
                {
                    if(XMLEncryptUtils.isCptEncoded() && !XMLEncryptUtils.checkVaild(DesignerEnvManager.getEnvManager().getEncryptionKey()) && !(new DecodeDialog(file)).isPwdRight())
                    {
                        FRContext.getLogger().error(Inter.getLocText("FR-Chart-Password_Error"));
                        return new ChartBook();
                    }
                    ChartBook chartbook = new ChartBook();
                    FRContext.getLogger().info(Inter.getLocText(new String[] {
                        "LOG-Is_Being_Openned", "LOG-Please_Wait"
                    }, new String[] {
                        (new StringBuilder()).append("\"").append(file.getName()).append("\"").append(",").toString(), "..."
                    }));
                    try
                    {
                        chartbook.readStream(file.asInputStream());
                    }
                    catch(Exception exception)
                    {
                        FRContext.getLogger().error((new StringBuilder()).append("Failed to generate frm from ").append(file).toString(), exception);
                        return null;
                    }
                    return chartbook;
                }

                public volatile IOFile asIOFile(FILE file)
                {
                    return asIOFile(file);
                }

            
            {
                this$0 = ChartStartModule.this;
                super();
            }
            }

        });
    }
}
