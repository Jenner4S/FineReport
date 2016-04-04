// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.gui.xtable;

import com.fr.base.FRContext;
import com.fr.design.designer.beans.events.CreatorEventListenerTable;
import com.fr.design.designer.creator.CRPropertyDescriptor;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.widget.editors.ExtendedPropertyEditor;
import com.fr.design.mainframe.widget.editors.PropertyCellEditor;
import com.fr.general.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.util.ArrayList;

// Referenced classes of package com.fr.design.gui.xtable:
//            AbstractPropertyGroupModel, TableUtils

public class PropertyGroupModel extends AbstractPropertyGroupModel
{

    private FormDesigner designer;
    private static ArrayList PROPERTIES;

    public PropertyGroupModel(String s, XCreator xcreator, CRPropertyDescriptor acrpropertydescriptor[], FormDesigner formdesigner)
    {
        super(s, xcreator, acrpropertydescriptor);
        designer = formdesigner;
    }

    public Object getValue(int i, int j)
    {
        if(j == 0)
            return properties[i].getDisplayName();
        try
        {
            Method method = properties[i].getReadMethod();
            return method.invoke(dealCreatorData(), new Object[0]);
        }
        catch(Exception exception)
        {
            FRContext.getLogger().error(exception.getMessage(), exception);
        }
        return null;
    }

    public boolean setValue(Object obj, int i, int j)
    {
        if(j == 0)
            return false;
        try
        {
            Method method = properties[i].getWriteMethod();
            method.invoke(dealCreatorData(), new Object[] {
                obj
            });
            if(ComparatorUtils.equals("widgetName", properties[i].getName()))
                creator.resetCreatorName(obj.toString());
            properties[i].firePropertyChanged();
            return true;
        }
        catch(Exception exception)
        {
            FRContext.getLogger().error(exception.getMessage(), exception);
        }
        return false;
    }

    public boolean isEditable(int i)
    {
        return properties[i].getWriteMethod() != null;
    }

    public String getGroupName()
    {
        return Inter.getLocText(groupName);
    }

    public int compareTo(AbstractPropertyGroupModel abstractpropertygroupmodel)
    {
        int i = PROPERTIES.indexOf(groupName);
        int j = PROPERTIES.indexOf(abstractpropertygroupmodel.getGroupName());
        if(i < j)
            return -1;
        return i != j ? 1 : 0;
    }

    private Object dealCreatorData()
    {
        return creator.getPropertyDescriptorCreator().toData();
    }

    protected void initEditor(final int row)
        throws Exception
    {
        ExtendedPropertyEditor extendedpropertyeditor = (ExtendedPropertyEditor)properties[row].createPropertyEditor(dealCreatorData());
        if(extendedpropertyeditor == null)
        {
            Class class1 = properties[row].getPropertyType();
            extendedpropertyeditor = (ExtendedPropertyEditor)TableUtils.getPropertyEditorClass(class1).newInstance();
        }
        if(extendedpropertyeditor != null)
        {
            final ExtendedPropertyEditor extendEditor = extendedpropertyeditor;
            editors[row] = new PropertyCellEditor(extendedpropertyeditor);
            extendEditor.addPropertyChangeListener(new PropertyChangeListener() {

                final ExtendedPropertyEditor val$extendEditor;
                final int val$row;
                final PropertyGroupModel this$0;

                public void propertyChange(PropertyChangeEvent propertychangeevent)
                {
                    if(ComparatorUtils.equals(extendEditor.getValue(), getValue(row, 1)))
                        return;
                    if(extendEditor.refreshInTime())
                    {
                        editors[row].stopCellEditing();
                    } else
                    {
                        setValue(extendEditor.getValue(), row, 1);
                        if("widgetName".equals(properties[row].getName()))
                            designer.getEditListenerTable().fireCreatorModified(creator, 8);
                        else
                            designer.fireTargetModified();
                        designer.refreshDesignerUI();
                    }
                }

            
            {
                this$0 = PropertyGroupModel.this;
                extendEditor = extendedpropertyeditor;
                row = i;
                super();
            }
            }
);
        }
    }

    public volatile int compareTo(Object obj)
    {
        return compareTo((AbstractPropertyGroupModel)obj);
    }

    static 
    {
        PROPERTIES = new ArrayList();
        PROPERTIES.add("Properties");
        PROPERTIES.add("Others");
    }

}
