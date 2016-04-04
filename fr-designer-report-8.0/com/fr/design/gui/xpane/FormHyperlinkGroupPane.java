// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.gui.xpane;

import com.fr.design.form.javascript.FormEmailPane;
import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.gui.frpane.HyperlinkGroupPane;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.js.EmailJavaScript;

public class FormHyperlinkGroupPane extends HyperlinkGroupPane
{

    public FormHyperlinkGroupPane()
    {
    }

    public NameableCreator[] createNameableCreators()
    {
        NameableCreator anameablecreator[] = super.createNameableCreators();
        int i = 0;
        do
        {
            if(i >= anameablecreator.length)
                break;
            if(ComparatorUtils.equals(anameablecreator[i].menuName(), Inter.getLocText("FR-Designer_Email")))
            {
                anameablecreator[i] = new NameObjectCreator(Inter.getLocText("FR-Designer_Email"), com/fr/js/EmailJavaScript, com/fr/design/form/javascript/FormEmailPane);
                break;
            }
            i++;
        } while(true);
        return anameablecreator;
    }
}
