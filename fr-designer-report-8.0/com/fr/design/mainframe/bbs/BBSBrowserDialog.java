// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.bbs;

import com.fr.design.dialog.UIDialog;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import java.awt.*;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

// Referenced classes of package com.fr.design.mainframe.bbs:
//            DisplayThread

public class BBSBrowserDialog extends UIDialog
{

    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private DisplayThread displayThread;
    private Canvas canvas;
    private String url;

    public BBSBrowserDialog(Frame frame)
    {
        super(frame);
        displayThread = new DisplayThread();
        displayThread.start();
        canvas = new Canvas();
        setLayout(new BorderLayout());
        add(canvas, "Center");
        setSize(600, 400);
        GUICoreUtils.centerWindow(this);
    }

    public String getTitle()
    {
        return Inter.getLocText("FR-Designer_Forum");
    }

    public void addNotify()
    {
        super.addNotify();
        Display display = displayThread.getDisplay();
        display.syncExec(new Runnable() {

            final BBSBrowserDialog this$0;

            public void run()
            {
                Shell shell = SWT_AWT.new_Shell(displayThread.getDisplay(), canvas);
                shell.setLayout(new FillLayout());
                Browser browser = new Browser(shell, 0);
                browser.setLayoutData("Center");
                browser.setUrl(url);
            }

            
            {
                this$0 = BBSBrowserDialog.this;
                super();
            }
        }
);
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String s)
    {
        url = s;
    }

    public void checkValid()
        throws Exception
    {
    }

    public void showWindow(String s)
    {
        setUrl(s);
        setVisible(true);
    }



}
