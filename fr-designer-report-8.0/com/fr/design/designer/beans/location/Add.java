// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.beans.location;

import com.fr.base.BaseUtils;
import com.fr.design.mainframe.FormDesigner;
import java.awt.*;

// Referenced classes of package com.fr.design.designer.beans.location:
//            Outer

public class Add extends Outer
{

    private Cursor addCursor;

    public Add()
    {
        addCursor = Toolkit.getDefaultToolkit().createCustomCursor(BaseUtils.readImage("/com/fr/design/images/form/designer/cursor/add.png"), new Point(0, 0), "add");
    }

    public void updateCursor(FormDesigner formdesigner)
    {
        formdesigner.setCursor(addCursor);
    }
}
