// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.webattr;

import com.fr.design.ExtraDesignClassManager;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.core.WidgetOption;
import com.fr.report.web.ToolBarManager;
import com.fr.stable.ArrayUtils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;

// Referenced classes of package com.fr.design.webattr:
//            ReportSelectToolBarPane, DragToolBarPane

public abstract class AbstractEditToolBarPane extends ReportSelectToolBarPane.EditToolBarPane
{

    protected ToolBarManager toolBarManagers[];
    private AbstractEditToolBarPane abstractEditToolBarPane;
    protected ActionListener editBtnListener;

    public AbstractEditToolBarPane()
    {
        toolBarManagers = null;
        editBtnListener = new ActionListener() {

            final AbstractEditToolBarPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                final DragToolBarPane dragToolbarPane = new DragToolBarPane();
                dragToolbarPane.setDefaultToolBar(ToolBarManager.createDefaultToolBar(), getToolBarInstanceWithExtra());
                dragToolbarPane.populateBean(toolBarManagers);
                BasicDialog basicdialog = dragToolbarPane.showWindow(SwingUtilities.getWindowAncestor(AbstractEditToolBarPane.this));
                basicdialog.addDialogActionListener(new DialogActionAdapter() {

                    final DragToolBarPane val$dragToolbarPane;
                    final _cls1 this$1;

                    public void doOk()
                    {
                        toolBarManagers = dragToolbarPane.updateBean();
                    }

                    
                    {
                        this$1 = _cls1.this;
                        dragToolbarPane = dragtoolbarpane;
                        super();
                    }
                }
);
                basicdialog.setVisible(true);
            }

            
            {
                this$0 = AbstractEditToolBarPane.this;
                super();
            }
        }
;
    }

    protected WidgetOption[] getToolBarInstanceWithExtra()
    {
        WidgetOption awidgetoption[] = ExtraDesignClassManager.getInstance().getWebWidgetOptions();
        return (WidgetOption[])(WidgetOption[])ArrayUtils.addAll(getToolBarInstance(), awidgetoption);
    }

    protected abstract WidgetOption[] getToolBarInstance();
}
