// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.wrappers;

import com.fr.base.TemplateUtils;
import com.fr.data.impl.TreeNodeWrapper;
import com.fr.design.Exception.ValidationException;
import com.fr.design.designer.properties.Decoder;
import com.fr.design.designer.properties.Encoder;
import com.fr.general.Inter;

public class TreeModelWrapper
    implements Encoder, Decoder
{

    public TreeModelWrapper()
    {
    }

    public String encode(Object obj)
    {
        if(obj == null)
            return "";
        if(obj instanceof com.fr.data.impl.TreeNodeAttr[])
            return TemplateUtils.render(Inter.getLocText("FR-Designer_Total_N_Grade"), new String[] {
                "N"
            }, new String[] {
                (new StringBuilder()).append(((com.fr.data.impl.TreeNodeAttr[])(com.fr.data.impl.TreeNodeAttr[])obj).length).append("").toString()
            });
        if(obj instanceof TreeNodeWrapper)
        {
            com.fr.data.impl.TreeNodeAttr atreenodeattr[] = ((TreeNodeWrapper)obj).getTreeNodeAttrs();
            return TemplateUtils.render(Inter.getLocText("FR-Designer_Total_N_Grade"), new String[] {
                "N"
            }, new String[] {
                (new StringBuilder()).append(atreenodeattr.length).append("").toString()
            });
        } else
        {
            return Inter.getLocText("FR-Designer_Auto-Build");
        }
    }

    public Object decode(String s)
    {
        return null;
    }

    public void validate(String s)
        throws ValidationException
    {
    }
}
