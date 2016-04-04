// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.design.designer.TargetComponent;
import com.fr.design.mainframe.toolbar.ToolBarMenuDockPlus;
import com.fr.design.menu.*;
import com.fr.form.ui.ChartBook;
import java.awt.Dimension;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.event.ChangeListener;

// Referenced classes of package com.fr.design.mainframe:
//            ChartDesignerUI, ChartToolBarPane, ChartArea, AuthorityEditPane

public class ChartDesigner extends TargetComponent
    implements MouseListener
{

    private ChartArea chartArea;
    private boolean hasCalGap;
    private ChartDesignerUI designerUI;
    private ArrayList changeListeners;
    private ChartToolBarPane chartToolBarPane;

    public ChartDesigner(ChartBook chartbook)
    {
        super(chartbook);
        hasCalGap = false;
        changeListeners = new ArrayList();
        addMouseListener(this);
        designerUI = new ChartDesignerUI();
        chartToolBarPane = new ChartToolBarPane(this) {

            final ChartDesigner this$0;

            public Dimension getPreferredSize()
            {
                Dimension dimension = super.getPreferredSize();
                return new Dimension(dimension.width, 42);
            }

            
            {
                this$0 = ChartDesigner.this;
                super(chartdesigner1);
            }
        }
;
        addMouseMotionListener(new MouseMotionAdapter() {

            final ChartDesigner this$0;

            public void mouseMoved(MouseEvent mouseevent)
            {
                if(designerUI != null)
                {
                    designerUI.mouseMoved(mouseevent);
                    repaint();
                }
            }

            
            {
                this$0 = ChartDesigner.this;
                super();
            }
        }
);
        updateUI();
    }

    public void updateUI()
    {
        setUI(designerUI);
    }

    public void setParent(ChartArea chartarea)
    {
        chartArea = chartarea;
    }

    public void copy()
    {
    }

    public boolean paste()
    {
        return false;
    }

    public boolean cut()
    {
        return false;
    }

    public void stopEditing()
    {
    }

    public AuthorityEditPane createAuthorityEditPane()
    {
        return null;
    }

    public ToolBarMenuDockPlus getToolBarMenuDockPlus()
    {
        return null;
    }

    public int getMenuState()
    {
        return 0;
    }

    public JPanel getEastUpPane()
    {
        return null;
    }

    public JPanel getEastDownPane()
    {
        return null;
    }

    public void cancelFormat()
    {
    }

    public ToolBarDef[] toolbars4Target()
    {
        return new ToolBarDef[0];
    }

    public MenuDef[] menus4Target()
    {
        return new MenuDef[0];
    }

    public ShortCut[] shortcut4TemplateMenu()
    {
        return new ShortCut[0];
    }

    public ShortCut[] shortCuts4Authority()
    {
        return new ShortCut[0];
    }

    public JComponent[] toolBarButton4Form()
    {
        return new JComponent[0];
    }

    public ChartArea getArea()
    {
        return chartArea;
    }

    public void mouseClicked(MouseEvent mouseevent)
    {
        designerUI.mouseClicked(mouseevent);
        chartToolBarPane.populate();
    }

    public void mousePressed(MouseEvent mouseevent)
    {
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
    }

    public void mouseEntered(MouseEvent mouseevent)
    {
    }

    public void mouseExited(MouseEvent mouseevent)
    {
    }

    private void registerChangeListener(ChangeListener changelistener)
    {
        if(changelistener == null)
        {
            return;
        } else
        {
            changeListeners.add(changelistener);
            return;
        }
    }

    public ChartToolBarPane getChartToolBarPane()
    {
        return chartToolBarPane;
    }

    public void populate()
    {
        chartToolBarPane.populate();
    }

    public void clearToolBarStyleChoose()
    {
        chartToolBarPane.clearStyleChoose();
    }

}
