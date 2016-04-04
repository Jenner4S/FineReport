// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.cell.style;

import com.fr.base.BaseUtils;
import com.fr.base.Style;
import com.fr.base.core.StyleUtils;
import com.fr.design.actions.core.ActionUtils;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.style.color.UIToolbarColorButton;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import java.awt.Color;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

// Referenced classes of package com.fr.design.actions.cell.style:
//            AbstractStyleAction

public class ReportFontForegroundAction extends AbstractStyleAction
    implements ChangeListener
{

    public ReportFontForegroundAction(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
        setName(Inter.getLocText("Foreground"));
    }

    public void stateChanged(ChangeEvent changeevent)
    {
        actionPerformedUndoable();
    }

    public Style executeStyle(Style style, Style style1)
    {
        Object obj = getValue(com/fr/design/style/color/UIToolbarColorButton.getName());
        if(obj != null && (obj instanceof UIToolbarColorButton))
        {
            Color color = ((UIToolbarColorButton)obj).getColor();
            if(style.getFRFont() != null && ComparatorUtils.equals(color, style.getFRFont()))
                return style;
            style = StyleUtils.setReportFontForeground(style, color);
        }
        return style;
    }

    public JComponent createToolBarComponent()
    {
        Object obj = getValue(com/fr/design/style/color/UIToolbarColorButton.getName());
        if(obj == null || !(obj instanceof UIToolbarColorButton))
        {
            UIToolbarColorButton uitoolbarcolorbutton = new UIToolbarColorButton(BaseUtils.readIcon("/com/fr/design/images/gui/color/foreground.png"));
            putValue(com/fr/design/style/color/UIToolbarColorButton.getName(), uitoolbarcolorbutton);
            uitoolbarcolorbutton.set4Toolbar();
            uitoolbarcolorbutton.setEnabled(isEnabled());
            uitoolbarcolorbutton.addColorChangeListener(this);
            uitoolbarcolorbutton.setToolTipText(ActionUtils.createButtonToolTipText(this));
            return uitoolbarcolorbutton;
        } else
        {
            return (JComponent)obj;
        }
    }
}
