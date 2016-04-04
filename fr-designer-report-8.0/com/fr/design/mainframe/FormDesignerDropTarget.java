// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.base.FRContext;
import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.designer.creator.XCreator;
import com.fr.form.data.DataBinding;
import com.fr.form.ui.*;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.io.IOException;

// Referenced classes of package com.fr.design.mainframe:
//            FormDesigner

public class FormDesignerDropTarget extends DropTargetAdapter
{

    private FormDesigner designer;

    public FormDesignerDropTarget(FormDesigner formdesigner)
    {
        designer = formdesigner;
        new DropTarget(formdesigner, this);
    }

    public void dragEnter(DropTargetDragEvent droptargetdragevent)
    {
        droptargetdragevent.acceptDrag(droptargetdragevent.getDropAction());
    }

    public void dragOver(DropTargetDragEvent droptargetdragevent)
    {
        Point point = droptargetdragevent.getLocation();
        DropTargetContext droptargetcontext = droptargetdragevent.getDropTargetContext();
        FormDesigner formdesigner = (FormDesigner)droptargetcontext.getComponent();
        formdesigner.doMousePress(point.getX(), point.getY());
        droptargetdragevent.acceptDrag(droptargetdragevent.getDropAction());
    }

    public void drop(DropTargetDropEvent droptargetdropevent)
    {
        Transferable transferable;
        java.awt.datatransfer.DataFlavor adataflavor[];
        transferable = droptargetdropevent.getTransferable();
        adataflavor = transferable.getTransferDataFlavors();
        Object obj;
        XCreator xcreator;
        Widget widget;
        obj = transferable.getTransferData(adataflavor[0]);
        xcreator = designer.getComponentAt(droptargetdropevent.getLocation());
        widget = xcreator.toData();
        if(obj instanceof String)
        {
            String s = (String)obj;
            if(widget instanceof IframeEditor)
                ((IframeEditor)widget).setSrc(s);
            xcreator.rebuid();
            designer.getSelectionModel().setSelectedCreator(xcreator);
        }
        if(!(obj instanceof String[][]) || ((String[][])(String[][])obj).length < 1)
            return;
        if(!(widget instanceof DataControl))
            return;
        try
        {
            WidgetValue widgetvalue = ((DataControl)widget).getWidgetValue();
            WidgetValue widgetvalue1 = new WidgetValue(new DataBinding((String[][])(String[][])obj));
            if(!ComparatorUtils.equals(widgetvalue, widgetvalue1))
            {
                ((DataControl)widget).setWidgetValue(widgetvalue1);
                designer.fireTargetModified();
            }
            xcreator.rebuid();
            designer.getSelectionModel().setSelectedCreator(xcreator);
        }
        catch(UnsupportedFlavorException unsupportedflavorexception)
        {
            FRContext.getLogger().error(unsupportedflavorexception.getMessage(), unsupportedflavorexception);
        }
        catch(IOException ioexception)
        {
            FRContext.getLogger().error(ioexception.getMessage(), ioexception);
        }
        return;
    }

    private void setSrcForIframeEditor()
    {
    }
}
