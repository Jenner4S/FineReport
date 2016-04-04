// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator;

import com.fr.stable.StringUtils;
import com.fr.stable.core.PropertyChangeAdapter;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;

public final class CRPropertyDescriptor extends PropertyDescriptor
{

    private PropertyChangeAdapter l;

    public CRPropertyDescriptor(String s, Class class1)
        throws IntrospectionException
    {
        super(s, class1);
    }

    public CRPropertyDescriptor(String s, Class class1, String s1, String s2)
        throws IntrospectionException
    {
        super(s, class1, s1, s2);
    }

    public CRPropertyDescriptor putKeyValue(String s, Object obj)
    {
        if(StringUtils.isNotEmpty(s))
            setValue(s, obj);
        return this;
    }

    public CRPropertyDescriptor setPropertyChangeListener(PropertyChangeAdapter propertychangeadapter)
    {
        l = propertychangeadapter;
        return this;
    }

    public void firePropertyChanged()
    {
        if(l != null)
            l.propertyChange();
    }

    public CRPropertyDescriptor setEditorClass(Class class1)
    {
        setPropertyEditorClass(class1);
        return this;
    }

    public CRPropertyDescriptor setRendererClass(Class class1)
    {
        putKeyValue("renderer", class1);
        return this;
    }

    public CRPropertyDescriptor setI18NName(String s)
    {
        setDisplayName(s);
        return this;
    }
}
