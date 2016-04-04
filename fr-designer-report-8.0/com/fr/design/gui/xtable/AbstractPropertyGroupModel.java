// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.gui.xtable;

import com.fr.base.FRContext;
import com.fr.design.beans.GroupModel;
import com.fr.design.designer.creator.CRPropertyDescriptor;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.mainframe.widget.editors.PropertyCellEditor;
import com.fr.design.mainframe.widget.renderer.PropertyCellRenderer;
import com.fr.general.FRLogger;
import javax.swing.table.*;

// Referenced classes of package com.fr.design.gui.xtable:
//            TableUtils

public abstract class AbstractPropertyGroupModel
    implements GroupModel, Comparable
{

    protected String groupName;
    protected XCreator creator;
    protected CRPropertyDescriptor properties[];
    protected TableCellRenderer renderers[];
    protected PropertyCellEditor editors[];
    public static final String RENDERER = "renderer";

    public AbstractPropertyGroupModel(String s, XCreator xcreator, CRPropertyDescriptor acrpropertydescriptor[])
    {
        groupName = s;
        creator = xcreator;
        properties = acrpropertydescriptor;
        renderers = new TableCellRenderer[properties.length];
        editors = new PropertyCellEditor[properties.length];
    }

    public String getGroupName()
    {
        return groupName;
    }

    public int getRowCount()
    {
        return properties.length;
    }

    public TableCellRenderer getRenderer(int i)
    {
        if(renderers[i] == null)
            try
            {
                initRenderer(i);
            }
            catch(Exception exception)
            {
                FRContext.getLogger().error(exception.getMessage(), exception);
            }
        return renderers[i];
    }

    public TableCellEditor getEditor(int i)
    {
        if(editors[i] == null)
            try
            {
                initEditor(i);
            }
            catch(Exception exception)
            {
                FRContext.getLogger().error(exception.getMessage(), exception);
            }
        return editors[i];
    }

    private void initRenderer(int i)
        throws Exception
    {
        Class class1 = (Class)properties[i].getValue("renderer");
        if(class1 != null)
        {
            renderers[i] = (TableCellRenderer)class1.newInstance();
        } else
        {
            Class class2 = properties[i].getPropertyType();
            Class class3 = TableUtils.getTableCellRendererClass(class2);
            if(class3 != null)
            {
                renderers[i] = (TableCellRenderer)class3.newInstance();
            } else
            {
                Class class4 = properties[i].getPropertyEditorClass();
                if(class4 == null)
                    class4 = TableUtils.getPropertyEditorClass(class2);
                if(class4 == null)
                {
                    renderers[i] = new DefaultTableCellRenderer();
                } else
                {
                    com.fr.design.mainframe.widget.editors.ExtendedPropertyEditor extendedpropertyeditor = ((PropertyCellEditor)getEditor(i)).getCellEditor();
                    renderers[i] = new PropertyCellRenderer(extendedpropertyeditor);
                }
            }
        }
    }

    protected abstract void initEditor(int i)
        throws Exception;
}
