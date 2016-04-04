// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.insert.cell;

import com.fr.base.BaseUtils;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.menu.MenuKeySet;
import com.fr.general.Inter;
import java.awt.Image;
import javax.swing.KeyStroke;

// Referenced classes of package com.fr.design.actions.insert.cell:
//            AbstractCellAction

public class ImageCellAction extends AbstractCellAction
{

    public static final MenuKeySet INSERT_IMAGE = new MenuKeySet() {

        public char getMnemonic()
        {
            return 'I';
        }

        public String getMenuName()
        {
            return Inter.getLocText("M_Insert-Image");
        }

        public KeyStroke getKeyStroke()
        {
            return null;
        }

    }
;

    public ImageCellAction(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
        setMenuKeySet(INSERT_IMAGE);
        setName((new StringBuilder()).append(getMenuKeySet().getMenuKeySetName()).append("...").toString());
        setMnemonic(getMenuKeySet().getMnemonic());
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_insert/image.png"));
    }

    public Class getCellValueClass()
    {
        return java/awt/Image;
    }

}
