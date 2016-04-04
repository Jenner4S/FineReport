// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.cell;

import com.fr.base.*;
import com.fr.design.actions.ElementCaseAction;
import com.fr.design.actions.UpdateAction;
import com.fr.design.gui.imenu.UIMenu;
import com.fr.design.mainframe.CellElementPropertyPane;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.menu.*;
import com.fr.design.style.StylePane;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;
import com.fr.stable.pinyin.PinyinHelper;
import java.awt.event.ActionEvent;
import java.util.Iterator;

public class GlobalStyleMenuDef extends MenuDef
{
    public static class GlobalStyleSelection extends ElementCaseAction
    {

        private NameStyle nameStyle;

        public NameStyle getNameStyle()
        {
            return nameStyle;
        }

        public void setNameStyle(NameStyle namestyle)
        {
            nameStyle = namestyle;
        }

        public boolean executeActionReturnUndoRecordNeeded()
        {
            StylePane stylepane = new StylePane();
            if(StringUtils.isEmpty(getName()))
                stylepane.setGlobalStyle(getNameStyle());
            else
                stylepane.setGlobalStyle(NameStyle.getInstance(getName()));
            stylepane.updateGlobalStyle((ElementCasePane)getEditingComponent());
            return true;
        }

        public GlobalStyleSelection(ElementCasePane elementcasepane, NameStyle namestyle)
        {
            super(elementcasepane);
            setName("");
            setNameStyle(namestyle);
        }
    }

    public static class CustomStyleAction extends UpdateAction
    {

        public void actionPerformed(ActionEvent actionevent)
        {
            CellElementPropertyPane.getInstance().GoToPane(new String[] {
                Inter.getLocText("FR-Engine_Style"), Inter.getLocText("FR-Engine_Custom")
            });
        }

        public CustomStyleAction(String s)
        {
            setName(s);
            setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_format/cell.png"));
        }
    }


    private static final int MAX_LENTH = 12;
    private ElementCasePane ePane;

    public GlobalStyleMenuDef(ElementCasePane elementcasepane)
    {
        ePane = elementcasepane;
        setMenuKeySet(KeySetUtils.GLOBAL_STYLE);
        setName(getMenuKeySet().getMenuKeySetName());
        setMnemonic(getMenuKeySet().getMnemonic());
        setIconPath("/com/fr/design/images/m_web/style.png");
    }

    public void updateMenu()
    {
        UIMenu uimenu = createJMenu();
        uimenu.removeAll();
        ConfigManagerProvider configmanagerprovider = ConfigManager.getProviderInstance();
        com.fr.design.actions.UpdateAction.UseMenuItem usemenuitem;
        for(Iterator iterator = configmanagerprovider.getStyleNameIterator(); iterator.hasNext(); uimenu.add(usemenuitem))
        {
            String s = (String)iterator.next();
            NameStyle namestyle = NameStyle.getInstance(s);
            usemenuitem = (new GlobalStyleSelection(ePane, namestyle)).createUseMenuItem();
            usemenuitem.setNameStyle(namestyle);
        }

        uimenu.addSeparator();
        uimenu.add(new CustomStyleAction(Inter.getLocText("FR-Engine_Custom")));
    }

    public static String judgeChina(String s)
    {
        Integer integer = Integer.valueOf(0);
        StringBuffer stringbuffer = new StringBuffer();
        for(int i = 0; i < s.length(); i++)
        {
            String s1 = s.substring(i, i + 1);
            boolean flag = PinyinHelper.isChinese(s1.charAt(0));
            if(integer.intValue() == 12)
            {
                stringbuffer.append("..");
                break;
            }
            if(integer.intValue() == 11 && flag)
                continue;
            if(flag)
                integer = Integer.valueOf(integer.intValue() + 2);
            else
                integer = Integer.valueOf(integer.intValue() + 1);
            stringbuffer.append(s1);
            if(integer.intValue() <= 12)
                continue;
            stringbuffer.append("..");
            break;
        }

        return stringbuffer.toString();
    }
}
