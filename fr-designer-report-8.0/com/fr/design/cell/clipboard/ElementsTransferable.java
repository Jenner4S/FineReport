// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.cell.clipboard;

import com.fr.stable.StableUtils;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;

// Referenced classes of package com.fr.design.cell.clipboard:
//            CellElementsClip, FloatElementsClip

public class ElementsTransferable
    implements Transferable, ClipboardOwner
{

    public static final DataFlavor CellElementsClipDataFlavor;
    public static final DataFlavor FloatElementClipDataFlavor;
    public static final DataFlavor StringDataFlavor;
    private static final DataFlavor flavors[];
    private java.util.List dataList;

    private static DataFlavor createConstant(Class class1, String s)
    {
        try
        {
            return new DataFlavor(class1, s);
        }
        catch(Exception exception)
        {
            return null;
        }
    }

    public ElementsTransferable()
    {
        dataList = new ArrayList();
    }

    public void addObject(Object obj)
    {
        dataList.add(obj);
    }

    public Object getFirstObject()
    {
        if(dataList.size() <= 0)
            return null;
        else
            return dataList.get(0);
    }

    public DataFlavor[] getTransferDataFlavors()
    {
        return (DataFlavor[])flavors.clone();
    }

    public boolean isDataFlavorSupported(DataFlavor dataflavor)
    {
        for(int i = 0; i < flavors.length; i++)
            if(dataflavor.equals(flavors[i]))
                return true;

        return false;
    }

    public Object getTransferData(DataFlavor dataflavor)
        throws UnsupportedFlavorException, IOException
    {
        if(dataflavor == null)
            return null;
        for(int i = 0; i < dataList.size(); i++)
        {
            Object obj = dataList.get(i);
            if(obj != null && StableUtils.classInstanceOf(dataflavor.getRepresentationClass(), obj.getClass()))
                return obj;
        }

        return null;
    }

    public void lostOwnership(Clipboard clipboard, Transferable transferable)
    {
    }

    public static Object getElementNotStringTransderData(Transferable transferable)
        throws UnsupportedFlavorException, IOException
    {
        for(int i = 0; i < flavors.length - 1; i++)
        {
            Object obj = transferable.getTransferData(flavors[i]);
            if(obj != null)
                return obj;
        }

        return null;
    }

    static 
    {
        CellElementsClipDataFlavor = createConstant(com/fr/design/cell/clipboard/CellElementsClip, "CellElementsClip");
        FloatElementClipDataFlavor = createConstant(com/fr/design/cell/clipboard/FloatElementsClip, "FloatElementClip");
        StringDataFlavor = createConstant(java/lang/String, "String");
        flavors = (new DataFlavor[] {
            CellElementsClipDataFlavor, FloatElementClipDataFlavor, StringDataFlavor
        });
    }
}
