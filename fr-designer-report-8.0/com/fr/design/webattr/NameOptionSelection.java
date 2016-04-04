// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.webattr;

import com.fr.design.gui.core.WidgetOption;
import java.awt.datatransfer.*;
import java.io.IOException;
import java.io.Serializable;

public class NameOptionSelection
    implements Transferable, Serializable
{

    private static final int STRING = 0;
    private static final DataFlavor flavors[];
    private WidgetOption data;

    public NameOptionSelection(WidgetOption widgetoption)
    {
        data = widgetoption;
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
        if(dataflavor.equals(flavors[0]))
            return data;
        else
            throw new UnsupportedFlavorException(dataflavor);
    }

    static 
    {
        flavors = (new DataFlavor[] {
            DataFlavor.stringFlavor
        });
    }
}
