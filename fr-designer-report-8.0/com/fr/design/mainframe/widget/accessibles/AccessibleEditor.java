// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.accessibles;

import com.fr.design.Exception.ValidationException;
import java.awt.Component;
import javax.swing.event.ChangeListener;

public interface AccessibleEditor
{

    public abstract void validateValue()
        throws ValidationException;

    public abstract Object getValue();

    public abstract void setValue(Object obj);

    public abstract Component getEditor();

    public abstract void addChangeListener(ChangeListener changelistener);

    public abstract void removeChangeListener(ChangeListener changelistener);
}
