// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.present;

import com.fr.base.ConfigManagerProvider;
import com.fr.base.Style;
import com.fr.design.gui.controlpane.*;
import com.fr.design.gui.ilist.ListModelElement;
import com.fr.design.gui.ilist.ModNameActionListener;
import com.fr.design.mainframe.DesignerBean;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.style.StylePane;
import com.fr.general.Inter;
import com.fr.general.NameObject;
import com.fr.stable.Nameable;
import java.util.*;

public class StyleArrayPane extends JControlPane
{

    public StyleArrayPane()
    {
        addModNameActionListener(new ModNameActionListener() {

            final StyleArrayPane this$0;

            public void nameModed(int i, String s, String s1)
            {
                populateSelectedValue();
            }

            
            {
                this$0 = StyleArrayPane.this;
                super();
            }
        }
);
    }

    public NameableCreator[] createNameableCreators()
    {
        return (new NameableCreator[] {
            new AbstractNameableCreator(Inter.getLocText("FR-Engine_Style_Name"), com/fr/base/Style, com/fr/design/style/StylePane) {

                final StyleArrayPane this$0;

                public NameObject createNameable(UnrepeatedNameHelper unrepeatednamehelper)
                {
                    return new NameObject(unrepeatednamehelper.createUnrepeatedName("H"), Style.getInstance());
                }

                public void saveUpdatedBean(ListModelElement listmodelelement, Object obj)
                {
                    ((NameObject)listmodelelement.wrapper).setObject(obj);
                }

                public String createTooltip()
                {
                    return null;
                }

                public volatile Nameable createNameable(UnrepeatedNameHelper unrepeatednamehelper)
                {
                    return createNameable(unrepeatednamehelper);
                }

            
            {
                this$0 = StyleArrayPane.this;
                super(s, class1, class2);
            }
            }

        });
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("ServerM-Predefined_Styles");
    }

    public void populate(ConfigManagerProvider configmanagerprovider)
    {
        if(configmanagerprovider == null)
            return;
        ArrayList arraylist = new ArrayList();
        Iterator iterator = configmanagerprovider.getStyleNameIterator();
        do
        {
            if(!iterator.hasNext())
                break;
            String s = (String)iterator.next();
            Style style = configmanagerprovider.getStyle(s);
            if(style != null)
                arraylist.add(new NameObject(s, style));
        } while(true);
        NameObject anameobject[] = new NameObject[arraylist.size()];
        arraylist.toArray(anameobject);
        populate(((Nameable []) (anameobject)));
    }

    public void update(ConfigManagerProvider configmanagerprovider)
    {
        configmanagerprovider.clearAllStyle();
        Nameable anameable[] = update();
        for(int i = 0; i < anameable.length; i++)
            configmanagerprovider.putStyle(((NameObject)anameable[i]).getName(), (Style)((NameObject)anameable[i]).getObject());

        DesignerContext.getDesignerBean("predefinedStyle").refreshBeanElement();
    }

}
