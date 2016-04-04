// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.webattr;

import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.report.web.Printer;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.webattr:
//            ServerPrinterPane

public class ReportServerPrinterPane extends JPanel
{

    private ServerPrinterPane serverPrinterPane;

    public ReportServerPrinterPane()
    {
        initComponents();
    }

    protected void initComponents()
    {
        setLayout(FRGUIPaneFactory.createBorderLayout());
        setBorder(BorderFactory.createEmptyBorder(6, 2, 4, 2));
        serverPrinterPane = new ServerPrinterPane();
        add(serverPrinterPane);
    }

    public void populate(Printer printer)
    {
        serverPrinterPane.populate(printer);
    }

    public Printer update()
    {
        return serverPrinterPane.update();
    }
}
