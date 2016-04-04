// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.accessibles;

import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.Border;

// Referenced classes of package com.fr.design.mainframe.widget.accessibles:
//            ColorIcon

public class ColorPalette extends JPopupMenu
{

    private Border BLACK_BORDER;
    private Object colors[] = {
        new Object[] {
            Inter.getLocText("Black"), new Color(0, 0, 0)
        }, new Object[] {
            Inter.getLocText("Crimson"), new Color(128, 0, 0)
        }, new Object[] {
            Inter.getLocText("Red"), new Color(255, 0, 0)
        }, new Object[] {
            Inter.getLocText("Pink"), new Color(255, 0, 255)
        }, new Object[] {
            Inter.getLocText("Rose_Red"), new Color(255, 153, 204)
        }, new Object[] {
            Inter.getLocText("Brown"), new Color(153, 51, 0)
        }, new Object[] {
            Inter.getLocText("Orange"), new Color(255, 102, 0)
        }, new Object[] {
            Inter.getLocText("Light_Orange"), new Color(255, 153, 0)
        }, new Object[] {
            Inter.getLocText("Golden"), new Color(255, 204, 0)
        }, new Object[] {
            Inter.getLocText("Brown_Orange"), new Color(255, 204, 153)
        }, new Object[] {
            "", new Color(51, 51, 0)
        }, new Object[] {
            "", new Color(128, 128, 0)
        }, new Object[] {
            "", new Color(153, 204, 0)
        }, new Object[] {
            "", new Color(255, 255, 0)
        }, new Object[] {
            "", new Color(255, 255, 153)
        }, new Object[] {
            "", new Color(0, 51, 0)
        }, new Object[] {
            "", new Color(0, 128, 0)
        }, new Object[] {
            "", new Color(51, 153, 102)
        }, new Object[] {
            "", new Color(172, 168, 153)
        }, new Object[] {
            "", new Color(204, 255, 204)
        }, new Object[] {
            "", new Color(0, 51, 102)
        }, new Object[] {
            "", new Color(0, 128, 128)
        }, new Object[] {
            "", new Color(51, 204, 204)
        }, new Object[] {
            "", new Color(0, 255, 255)
        }, new Object[] {
            "", new Color(204, 255, 255)
        }, new Object[] {
            "", new Color(0, 0, 128)
        }, new Object[] {
            "", new Color(0, 0, 255)
        }, new Object[] {
            "", new Color(51, 102, 255)
        }, new Object[] {
            "", new Color(0, 204, 255)
        }, new Object[] {
            "", new Color(153, 204, 255)
        }, new Object[] {
            "", new Color(51, 51, 153)
        }, new Object[] {
            "", new Color(102, 102, 153)
        }, new Object[] {
            "", new Color(128, 0, 128)
        }, new Object[] {
            "", new Color(153, 51, 102)
        }, new Object[] {
            "", new Color(204, 153, 255)
        }, new Object[] {
            "", new Color(51, 51, 51)
        }, new Object[] {
            "", new Color(128, 128, 128)
        }, new Object[] {
            "", new Color(153, 153, 153)
        }, new Object[] {
            "", new Color(192, 192, 192)
        }, new Object[] {
            "", new Color(255, 255, 255)
        }
    };
    private UIButton btnDefault;
    private UIButton btnCustom;
    private UIButton btn;
    private JToggleButton btnColors[];
    private ButtonGroup group;

    public ColorPalette()
    {
        BLACK_BORDER = BorderFactory.createLineBorder(new Color(127, 157, 185));
        setBorder(BorderFactory.createCompoundBorder(BLACK_BORDER, BorderFactory.createEmptyBorder(4, 4, 4, 4)));
        setLayout(FRGUIPaneFactory.createBorderLayout());
        JToolBar jtoolbar = getTopBar();
        add(jtoolbar, "North");
        JToolBar jtoolbar1 = getPaletteBar();
        add(jtoolbar1, "Center");
        JToolBar jtoolbar2 = getBottomBar();
        add(jtoolbar2, "South");
    }

    public void addDefaultAction(ActionListener actionlistener)
    {
        btnDefault.addActionListener(actionlistener);
    }

    public void addCustomAction(ActionListener actionlistener)
    {
        btnCustom.addActionListener(actionlistener);
    }

    public void addColorAction(ActionListener actionlistener)
    {
        JToggleButton ajtogglebutton[] = btnColors;
        int i = ajtogglebutton.length;
        for(int j = 0; j < i; j++)
        {
            JToggleButton jtogglebutton = ajtogglebutton[j];
            jtogglebutton.addActionListener(actionlistener);
        }

    }

    private JToolBar getBottomBar()
    {
        JToolBar jtoolbar = new JToolBar();
        jtoolbar.setOpaque(false);
        jtoolbar.setLayout(new GridLayout(1, 1));
        jtoolbar.setBorderPainted(false);
        jtoolbar.setFloatable(false);
        btnCustom = getBtn((new StringBuilder()).append(Inter.getLocText("Custom")).append("...").toString());
        jtoolbar.add(btnCustom);
        return jtoolbar;
    }

    private UIButton getBtn(String s)
    {
        if(btn == null)
        {
            btn = new UIButton();
            btn.setBorder(BLACK_BORDER);
            btn.setFocusPainted(false);
            btn.setText(s);
        }
        return btn;
    }

    private JToolBar getPaletteBar()
    {
        JToolBar jtoolbar = new JToolBar();
        jtoolbar.setOpaque(false);
        GridLayout gridlayout = new GridLayout(5, 8, 1, 1);
        jtoolbar.setLayout(gridlayout);
        jtoolbar.setBorderPainted(false);
        jtoolbar.setFloatable(false);
        group = new ButtonGroup();
        btnColors = new JToggleButton[colors.length];
        for(int i = 0; i < colors.length; i++)
        {
            btnColors[i] = new JToggleButton();
            Object aobj[] = (Object[])(Object[])colors[i];
            String s = (String)aobj[0];
            Color color = (Color)aobj[1];
            btnColors[i].setIcon(new ColorIcon(s, color));
            btnColors[i].setToolTipText(s);
            btnColors[i].setFocusPainted(false);
            group.add(btnColors[i]);
            jtoolbar.add(btnColors[i]);
        }

        return jtoolbar;
    }

    private JToolBar getTopBar()
    {
        JToolBar jtoolbar = new JToolBar();
        jtoolbar.setBorderPainted(false);
        jtoolbar.setOpaque(false);
        jtoolbar.setFloatable(false);
        jtoolbar.setLayout(new GridLayout(1, 1));
        btnDefault = getBtn(Inter.getLocText("Form-Restore_Default_Value"));
        jtoolbar.add(btnDefault);
        return jtoolbar;
    }

    public void setChoosedColor(Color color)
    {
        group.clearSelection();
        if(color != null)
        {
            for(int i = 0; i < colors.length; i++)
            {
                Color color1 = (Color)((Object[])(Object[])colors[i])[1];
                if(color1.equals(color))
                {
                    btnColors[i].setSelected(true);
                    return;
                }
            }

        }
    }
}
