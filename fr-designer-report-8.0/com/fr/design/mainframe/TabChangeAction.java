// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.form.FormElementCaseContainerProvider;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

// Referenced classes of package com.fr.design.mainframe:
//            BaseJForm

public class TabChangeAction extends AbstractAction
{

    private final int index;
    private BaseJForm jform;
    private FormElementCaseContainerProvider ecContainer;

    public TabChangeAction(int i, BaseJForm basejform)
    {
        this(i, basejform, null);
    }

    public TabChangeAction(int i, BaseJForm basejform, FormElementCaseContainerProvider formelementcasecontainerprovider)
    {
        index = i;
        jform = basejform;
        ecContainer = formelementcasecontainerprovider;
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        jform.tabChanged(index, ecContainer);
    }
}
