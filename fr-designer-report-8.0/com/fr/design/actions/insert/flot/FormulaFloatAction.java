// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.insert.flot;

import com.fr.base.BaseUtils;
import com.fr.base.Formula;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.menu.MenuKeySet;
import com.fr.general.Inter;
import com.fr.report.cell.FloatElement;
import java.awt.event.ActionEvent;
import javax.swing.KeyStroke;

// Referenced classes of package com.fr.design.actions.insert.flot:
//            AbstractShapeAction

public class FormulaFloatAction extends AbstractShapeAction
{

    public static final MenuKeySet FLOAT_INSERT_FORMULA = new MenuKeySet() {

        public char getMnemonic()
        {
            return 'F';
        }

        public String getMenuName()
        {
            return Inter.getLocText("HF-Insert_Formula");
        }

        public KeyStroke getKeyStroke()
        {
            return null;
        }

    }
;

    public FormulaFloatAction(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
        setMenuKeySet(FLOAT_INSERT_FORMULA);
        setName((new StringBuilder()).append(getMenuKeySet().getMenuKeySetName()).append("...").toString());
        setMnemonic(getMenuKeySet().getMnemonic());
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_insert/formula.png"));
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        ElementCasePane elementcasepane = (ElementCasePane)getEditingComponent();
        if(elementcasepane == null)
        {
            return;
        } else
        {
            FloatElement floatelement = new FloatElement(new Formula(""));
            startDraw(floatelement);
            return;
        }
    }

}
