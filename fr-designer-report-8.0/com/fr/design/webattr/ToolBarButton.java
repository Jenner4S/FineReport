// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.webattr;

import com.fr.base.BaseUtils;
import com.fr.base.IconManager;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.mainframe.DesignerContext;
import com.fr.form.ui.*;
import com.fr.stable.StringUtils;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.Icon;
import javax.swing.ImageIcon;

// Referenced classes of package com.fr.design.webattr:
//            ToolBarPane, EditToolBar

public class ToolBarButton extends UIButton
    implements MouseListener
{

    private Widget widget;
    private WidgetOption no;

    public ToolBarButton(Icon icon, Widget widget1)
    {
        this(null, icon, widget1);
    }

    public ToolBarButton(String s, Icon icon, Widget widget1)
    {
        super(s, icon);
        widget = widget1;
        if(widget1 instanceof Button)
        {
            Button button = (Button)widget1;
            String s1 = button.getIconName();
            if(StringUtils.isNotEmpty(s1))
            {
                java.awt.Image image = WidgetManager.getProviderInstance().getIconManager().getIconImage(s1);
                if(image != null)
                    setIcon(new ImageIcon(image));
            }
        }
        addMouseListener(this);
        setMargin(new Insets(0, 0, 0, 0));
    }

    public void changeAuthorityState(String s, boolean flag)
    {
        widget.changeOnlyVisibleAuthorityState(s, flag);
    }

    public boolean isDoneAuthorityEdited(String s)
    {
        return widget.isDoneVisibleAuthority(s);
    }

    public Widget getWidget()
    {
        return widget;
    }

    public void setWidget(Widget widget1)
    {
        widget = widget1;
    }

    public WidgetOption getNameOption()
    {
        return no;
    }

    public void setNameOption(WidgetOption widgetoption)
    {
        no = widgetoption;
    }

    protected void paintBorder(Graphics g)
    {
        setBorderType(2);
        super.paintBorder(g);
    }

    public void mouseClicked(MouseEvent mouseevent)
    {
        if(BaseUtils.isAuthorityEditing())
        {
            auhtorityMouseAction();
            return;
        }
        if(mouseevent.getClickCount() >= 2 && (getParent() instanceof ToolBarPane))
        {
            final ToolBarPane tb = (ToolBarPane)getParent();
            final EditToolBar etb = new EditToolBar();
            etb.populate(tb.getFToolBar(), this);
            BasicDialog basicdialog = etb.showWindow(DesignerContext.getDesignerFrame());
            basicdialog.addDialogActionListener(new DialogActionAdapter() {

                final ToolBarPane val$tb;
                final EditToolBar val$etb;
                final ToolBarButton this$0;

                public void doOk()
                {
                    tb.setFToolBar(etb.update());
                }

            
            {
                this$0 = ToolBarButton.this;
                tb = toolbarpane;
                etb = edittoolbar;
                super();
            }
            }
);
            basicdialog.setVisible(true);
        }
    }

    private void auhtorityMouseAction()
    {
        if((getParent() instanceof ToolBarPane) && isEnabled())
            setSelected(!isSelected());
    }

    public void mouseEntered(MouseEvent mouseevent)
    {
    }

    public void mouseExited(MouseEvent mouseevent)
    {
    }

    public void mousePressed(MouseEvent mouseevent)
    {
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
    }
}
