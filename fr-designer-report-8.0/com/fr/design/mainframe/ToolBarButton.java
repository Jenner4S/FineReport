// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.form.ui.Widget;
import com.fr.general.ComparatorUtils;
import java.awt.Dimension;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.Serializable;
import javax.swing.ButtonModel;

// Referenced classes of package com.fr.design.mainframe:
//            WidgetToolBarPane, FormDesigner

public class ToolBarButton extends UIButton
    implements MouseListener, MouseMotionListener, Serializable
{
    public class DragAndDropTransferable
        implements Transferable
    {

        private Widget widget;
        DataFlavor flavors[] = {
            new DataFlavor(com/fr/form/ui/Widget, "Widget")
        };
        final ToolBarButton this$0;

        public DataFlavor[] getTransferDataFlavors()
        {
            return flavors;
        }

        public boolean isDataFlavorSupported(DataFlavor dataflavor)
        {
            DataFlavor adataflavor[] = flavors;
            int i = adataflavor.length;
            for(int j = 0; j < i; j++)
            {
                DataFlavor dataflavor1 = adataflavor[j];
                if(ComparatorUtils.equals(dataflavor1, dataflavor))
                    return true;
            }

            return false;
        }

        public Object getTransferData(DataFlavor dataflavor)
            throws UnsupportedFlavorException, IOException
        {
            return widget;
        }

        public DragAndDropTransferable(Widget widget1)
        {
            this$0 = ToolBarButton.this;
            super();
            widget = widget1;
        }
    }

    public class DragAndDropDragGestureListener extends DragSourceAdapter
        implements DragGestureListener
    {

        private DragSource source;
        final ToolBarButton this$0;

        public void dragGestureRecognized(DragGestureEvent draggestureevent)
        {
            ToolBarButton toolbarbutton = (ToolBarButton)draggestureevent.getComponent();
            if(toolbarbutton != null)
            {
                Widget widget = toolbarbutton.getNameOption().createWidget();
                DragAndDropTransferable draganddroptransferable = new DragAndDropTransferable(widget);
                draggestureevent.startDrag(DragSource.DefaultCopyDrop, draganddroptransferable, this);
            }
        }

        public void dragEnter(DragSourceDragEvent dragsourcedragevent)
        {
        }

        public DragAndDropDragGestureListener(ToolBarButton toolbarbutton1, int i)
        {
            this$0 = ToolBarButton.this;
            super();
            source = new DragSource();
            source.createDefaultDragGestureRecognizer(toolbarbutton1, i, this);
        }
    }


    private WidgetOption no;
    private MouseEvent lastPressEvent;

    public ToolBarButton(WidgetOption widgetoption)
    {
        super(widgetoption.optionIcon());
        no = widgetoption;
        addMouseListener(this);
        addMouseMotionListener(this);
        setToolTipText(widgetoption.optionName());
        setBorder(null);
        setOpaque(false);
        setRequestFocusEnabled(false);
        set4ToolbarButton();
        new DragAndDropDragGestureListener(this, 3);
    }

    public Dimension getPreferredSize()
    {
        return new Dimension(super.getPreferredSize().width, 20);
    }

    public WidgetOption getNameOption()
    {
        return no;
    }

    public void setNameOption(WidgetOption widgetoption)
    {
        no = widgetoption;
    }

    public void mouseClicked(MouseEvent mouseevent)
    {
        if(mouseevent.getClickCount() < 2);
    }

    public void mousePressed(MouseEvent mouseevent)
    {
        lastPressEvent = mouseevent;
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
    }

    public void mouseDragged(MouseEvent mouseevent)
    {
        if(BaseUtils.isAuthorityEditing())
            return;
        if(lastPressEvent == null)
            return;
        getModel().setPressed(false);
        getModel().setSelected(false);
        getModel().setRollover(false);
        Object obj = mouseevent.getSource();
        Widget widget = null;
        if(obj instanceof ToolBarButton)
        {
            ToolBarButton toolbarbutton = (ToolBarButton)mouseevent.getSource();
            if(toolbarbutton == null)
                return;
            widget = toolbarbutton.getNameOption().createWidget();
        }
        if(widget != null)
        {
            WidgetToolBarPane.getTarget().startDraggingBean(XCreatorUtils.createXCreator(widget));
            lastPressEvent = null;
            setBorder(null);
        }
    }

    public void mouseMoved(MouseEvent mouseevent)
    {
    }

    public void mouseEntered(MouseEvent mouseevent)
    {
    }

    public void mouseExited(MouseEvent mouseevent)
    {
    }
}
