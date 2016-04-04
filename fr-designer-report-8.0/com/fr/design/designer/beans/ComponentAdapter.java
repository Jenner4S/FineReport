// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans;

import com.fr.design.designer.beans.events.DesignerEditor;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JPopupMenu;

public interface ComponentAdapter
{

    public abstract void paintComponentMascot(Graphics g);

    public abstract JPopupMenu getContextPopupMenu(MouseEvent mouseevent);

    public abstract ArrayList getXCreatorPropertyModel();

    public abstract DesignerEditor getDesignerEditor();

    public abstract void initialize();
}
