// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.cell.style;

import com.fr.base.BaseUtils;
import com.fr.base.Style;
import com.fr.base.core.StyleUtils;
import com.fr.design.actions.ToggleButtonUpdateAction;
import com.fr.design.gui.ibutton.UIToggleButton;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.FRFont;
import com.fr.general.Inter;
import javax.swing.JComponent;

// Referenced classes of package com.fr.design.actions.cell.style:
//            AbstractStyleAction

public class ReportFontBoldAction extends AbstractStyleAction
    implements ToggleButtonUpdateAction
{

    private UIToggleButton button;
    protected Style style;

    public ReportFontBoldAction(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
        setName(Inter.getLocText("FRFont-bold"));
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/bold.png"));
    }

    public Style executeStyle(Style style1, Style style2)
    {
        createToolBarComponent().setSelected(!createToolBarComponent().isSelected());
        if(createToolBarComponent().isSelected())
        {
            setSelectedFont(style1);
            createToolBarComponent().setSelected(false);
        } else
        {
            setUnselectedFont(style1);
            createToolBarComponent().setSelected(true);
        }
        return style;
    }

    protected void setSelectedFont(Style style1)
    {
        style = StyleUtils.boldReportFont(style1);
    }

    protected void setUnselectedFont(Style style1)
    {
        style = StyleUtils.unBoldReportFont(style1);
    }

    public void updateStyle(Style style1)
    {
        if(style1 == null)
            return;
        FRFont frfont = style1.getFRFont();
        if(frfont == null)
        {
            return;
        } else
        {
            createToolBarComponent().setSelected(isStyle(frfont));
            return;
        }
    }

    protected boolean isStyle(FRFont frfont)
    {
        return frfont.isBold();
    }

    public UIToggleButton createToolBarComponent()
    {
        if(button == null)
        {
            button = GUICoreUtils.createToolBarComponent(this);
            button.setEventBannded(true);
        }
        return button;
    }

    public volatile JComponent createToolBarComponent()
    {
        return createToolBarComponent();
    }
}
