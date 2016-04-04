// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.treeview;

import com.fr.design.constants.UIConstants;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.gui.ilable.UILabel;
import com.fr.form.ui.Widget;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

public class ComponentTreeCellRenderer extends DefaultTreeCellRenderer
{

    public ComponentTreeCellRenderer()
    {
    }

    public Component getTreeCellRendererComponent(JTree jtree, Object obj, boolean flag, boolean flag1, boolean flag2, int i, boolean flag3)
    {
        super.getTreeCellRendererComponent(jtree, obj, flag, flag1, flag2, i, flag3);
        if(obj instanceof XCreator)
        {
            setText(((XCreator)obj).toData().getWidgetName());
            Icon icon = XCreatorUtils.getCreatorIcon((XCreator)obj);
            if(icon != null)
                setIcon(icon);
        }
        UILabel uilabel = new UILabel();
        uilabel.setText(getText());
        uilabel.setIcon(getIcon());
        Dimension dimension = uilabel.getPreferredSize();
        dimension.height += 2;
        setSize(dimension);
        setPreferredSize(dimension);
        setBackgroundNonSelectionColor(UIConstants.NORMAL_BACKGROUND);
        return this;
    }

    public Icon getClosedIcon()
    {
        return getIcon();
    }

    public Icon getLeafIcon()
    {
        return getIcon();
    }

    public Icon getOpenIcon()
    {
        return getIcon();
    }
}
