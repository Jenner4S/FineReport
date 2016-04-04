// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.webattr;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.ui.ToolBar;
import com.fr.form.ui.Widget;
import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;

// Referenced classes of package com.fr.design.webattr:
//            FToolBar, ToolBarButton, EditToolBar

public class ToolBarPane extends BasicBeanPane
{
    private class ToolBarHandler extends TransferHandler
    {

        private int action;
        final ToolBarPane this$0;

        public boolean canImport(javax.swing.TransferHandler.TransferSupport transfersupport)
        {
            if(!transfersupport.isDrop())
                return false;
            if(!transfersupport.isDataFlavorSupported(DataFlavor.stringFlavor))
                return false;
            boolean flag = (action & transfersupport.getSourceDropActions()) == action;
            if(flag)
            {
                transfersupport.setDropAction(action);
                return true;
            } else
            {
                return false;
            }
        }

        public boolean importData(javax.swing.TransferHandler.TransferSupport transfersupport)
        {
            if(!canImport(transfersupport))
                return false;
            WidgetOption widgetoption;
            try
            {
                widgetoption = (WidgetOption)transfersupport.getTransferable().getTransferData(DataFlavor.stringFlavor);
            }
            catch(UnsupportedFlavorException unsupportedflavorexception)
            {
                return false;
            }
            catch(IOException ioexception)
            {
                return false;
            }
            Widget widget = widgetoption.createWidget();
            ToolBarButton toolbarbutton = new ToolBarButton(widgetoption.optionIcon(), widget);
            toolbarbutton.setNameOption(widgetoption);
            add(toolbarbutton);
            validate();
            repaint();
            return true;
        }

        public ToolBarHandler(int i)
        {
            this$0 = ToolBarPane.this;
            super();
            action = i;
        }
    }


    private FToolBar ftoolbar;
    MouseListener listener = new MouseAdapter() {

        final ToolBarPane this$0;

        public void mouseClicked(MouseEvent mouseevent)
        {
            if(mouseevent.getClickCount() >= 2 && !SwingUtilities.isRightMouseButton(mouseevent))
            {
                final EditToolBar tb = new EditToolBar();
                tb.populate(getFToolBar());
                BasicDialog basicdialog = tb.showWindow(SwingUtilities.getWindowAncestor(ToolBarPane.this));
                basicdialog.addDialogActionListener(new DialogActionAdapter() {

                    final EditToolBar val$tb;
                    final _cls1 this$1;

                    public void doOk()
                    {
                        setFToolBar(tb.update());
                    }

                    
                    {
                        this$1 = _cls1.this;
                        tb = edittoolbar;
                        super();
                    }
                }
);
                basicdialog.setVisible(true);
            }
        }

            
            {
                this$0 = ToolBarPane.this;
                super();
            }
    }
;

    public ToolBarPane()
    {
        ftoolbar = new FToolBar();
        initComponent();
    }

    public void addAuthorityListener(MouseListener mouselistener)
    {
        java.util.List list = ftoolbar.getButtonlist();
        for(int i = 0; i < list.size(); i++)
            ((ToolBarButton)list.get(i)).addMouseListener(mouselistener);

    }

    public ToolBarPane(ToolBarButton toolbarbutton)
    {
        ftoolbar = new FToolBar();
        initComponent();
        add(toolbarbutton);
    }

    public void initComponent()
    {
        addMouseListener(listener);
        setLayout(FRGUIPaneFactory.createBoxFlowLayout());
        setTransferHandler(new ToolBarHandler(1));
        setBorder(BorderFactory.createTitledBorder(""));
    }

    public void removeDefaultMouseListener()
    {
        removeMouseListener(listener);
    }

    protected String title4PopupWindow()
    {
        return "Toolbar";
    }

    public void setSelectedButton(ToolBarButton toolbarbutton)
    {
        ftoolbar.addButton(toolbarbutton);
    }

    public Component add(Component component)
    {
        if(component instanceof ToolBarButton)
            ftoolbar.addButton((ToolBarButton)component);
        return super.add(component);
    }

    private Component addComp(Component component)
    {
        return super.add(component);
    }

    protected void removeButtonList()
    {
        ftoolbar.clearButton();
    }

    protected void setFToolBar(FToolBar ftoolbar1)
    {
        if(ftoolbar1 == null)
            ftoolbar1 = new FToolBar();
        ftoolbar = ftoolbar1;
        setToolBar(ftoolbar.getButtonlist());
    }

    public java.util.List getToolBarButtons()
    {
        return ftoolbar.getButtonlist();
    }

    protected FToolBar getFToolBar()
    {
        return ftoolbar;
    }

    protected boolean isEmpty()
    {
        return ftoolbar.getButtonlist().size() <= 0;
    }

    private void setToolBar(java.util.List list)
    {
        if(list == null || list.size() < 0)
            return;
        removeAll();
        for(int i = 0; i < list.size(); i++)
            addComp((Component)list.get(i));

        validate();
        repaint();
    }

    public void populateBean(ToolBar toolbar)
    {
        removeAll();
        getFToolBar().clearButton();
        for(int i = 0; i < toolbar.getWidgetSize(); i++)
        {
            Widget widget = toolbar.getWidget(i);
            WidgetOption widgetoption = WidgetOption.getToolBarButton(widget.getClass());
            if(widgetoption != null)
            {
                ToolBarButton toolbarbutton = new ToolBarButton(widgetoption.optionIcon(), widget);
                toolbarbutton.setNameOption(widgetoption);
                add(toolbarbutton);
                validate();
                repaint();
            }
        }

        getFToolBar().setBackground(toolbar.getBackground());
        getFToolBar().setDefault(toolbar.isDefault());
    }

    public ToolBar updateBean()
    {
        return ftoolbar.getToolBar();
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((ToolBar)obj);
    }
}
