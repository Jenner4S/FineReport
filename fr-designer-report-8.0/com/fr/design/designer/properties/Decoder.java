// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.properties;

import com.fr.design.Exception.ValidationException;

public interface Decoder
{

    public abstract Object decode(String s);

    public abstract void validate(String s)
        throws ValidationException;
}
