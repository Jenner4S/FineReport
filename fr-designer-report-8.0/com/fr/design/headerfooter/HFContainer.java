// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.headerfooter;

import com.fr.base.headerfooter.HFElement;
import com.fr.base.headerfooter.NewLineHFElement;
import com.fr.general.Inter;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.Scrollable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

// Referenced classes of package com.fr.design.headerfooter:
//            HFComponent

public class HFContainer extends JPanel
    implements Scrollable
{

    private java.util.List hfComponentList;
    private static final int HOR_GAP = 1;
    private static final int VER_GAP = 2;
    private ChangeListener contentChangeListener;
    private ActionListener moveLeftActionListener;
    private ActionListener moveRightActionListener;
    private ActionListener deleteActionListener;

    public HFContainer()
    {
        hfComponentList = new ArrayList();
        contentChangeListener = null;
        moveLeftActionListener = new ActionListener() {

            final HFContainer this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                Object obj = actionevent.getSource();
                if(obj instanceof HFComponent)
                    moveLeftHFComponent((HFComponent)obj);
            }

            
            {
                this$0 = HFContainer.this;
                super();
            }
        }
;
        moveRightActionListener = new ActionListener() {

            final HFContainer this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                Object obj = actionevent.getSource();
                if(obj instanceof HFComponent)
                    moveRightHFComponent((HFComponent)obj);
            }

            
            {
                this$0 = HFContainer.this;
                super();
            }
        }
;
        deleteActionListener = new ActionListener() {

            final HFContainer this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                Object obj = actionevent.getSource();
                if(obj instanceof HFComponent)
                {
                    int i = JOptionPane.showConfirmDialog(SwingUtilities.getWindowAncestor(HFContainer.this), (new StringBuilder()).append(Inter.getLocText("HF-Are_you_sure_to_delete_it")).append("?").toString(), Inter.getLocText("Delete"), 2, 3);
                    if(i == 0)
                        removeHFComponent((HFComponent)obj);
                }
            }

            
            {
                this$0 = HFContainer.this;
                super();
            }
        }
;
        setEnabled(true);
    }

    public void setEnabled(boolean flag)
    {
        super.setEnabled(flag);
        if(flag)
            setBackground(UIManager.getColor("TextArea.background"));
        else
            setBackground(SystemColor.control);
    }

    private void refreshLayout()
    {
        removeAll();
        setLayout(null);
        int i = 2;
        int j = 2;
        for(int k = 0; k < hfComponentList.size(); k++)
        {
            HFComponent hfcomponent = (HFComponent)hfComponentList.get(k);
            add(hfcomponent);
            hfcomponent.setLocation(i, j);
            hfcomponent.setSize(hfcomponent.getPreferredSize().width, hfcomponent.getPreferredSize().height);
            if(hfcomponent.getHFElement().getClass().equals(com/fr/base/headerfooter/NewLineHFElement))
            {
                i = 2;
                j += 2 + hfcomponent.getPreferredSize().height;
            } else
            {
                i += 1 + hfcomponent.getWidth();
            }
        }

        doLayout();
        revalidate();
        repaint();
        contentChanged();
    }

    public ChangeListener getContentChangeListener()
    {
        return contentChangeListener;
    }

    public void setContentChangeListener(ChangeListener changelistener)
    {
        contentChangeListener = changelistener;
    }

    private void contentChanged()
    {
        if(contentChangeListener != null)
        {
            ChangeEvent changeevent = new ChangeEvent(this);
            contentChangeListener.stateChanged(changeevent);
        }
    }

    public void populate(java.util.List list)
    {
        hfComponentList.clear();
        for(int i = 0; i < list.size(); i++)
            addHFComponent(new HFComponent((HFElement)list.get(i)));

        refreshLayout();
    }

    public java.util.List update()
    {
        ArrayList arraylist = new ArrayList();
        for(int i = 0; i < hfComponentList.size(); i++)
            arraylist.add(((HFComponent)hfComponentList.get(i)).getHFElement());

        return arraylist;
    }

    public void addHFComponent(HFComponent hfcomponent)
    {
        addHFComponent(-1, hfcomponent);
    }

    public void addHFComponent(int i, HFComponent hfcomponent)
    {
        if(i <= -1 || i > hfComponentList.size())
            hfComponentList.add(hfcomponent);
        else
            hfComponentList.add(i, hfcomponent);
        hfcomponent.setMoveLeftActionListener(moveLeftActionListener);
        hfcomponent.setMoveRightActionListener(moveRightActionListener);
        hfcomponent.setDeleteActionListener(deleteActionListener);
        hfcomponent.setContentChangeListener(getContentChangeListener());
        refreshLayout();
    }

    public void removeHFComponent(HFComponent hfcomponent)
    {
        if(hfComponentList.contains(hfcomponent))
        {
            hfComponentList.remove(hfcomponent);
            refreshLayout();
        }
    }

    public void moveLeftHFComponent(HFComponent hfcomponent)
    {
        int i = hfComponentList.indexOf(hfcomponent);
        if(i > 0)
            Collections.swap(hfComponentList, i - 1, i);
        refreshLayout();
    }

    public void moveRightHFComponent(HFComponent hfcomponent)
    {
        int i = hfComponentList.indexOf(hfcomponent);
        if(i < hfComponentList.size() - 1)
            Collections.swap(hfComponentList, i, i + 1);
        refreshLayout();
    }

    public Dimension getPreferredSize()
    {
        if(hfComponentList.size() <= 0)
            return super.getPreferredSize();
        int i = 0;
        int j = 0;
        int k = 0;
        int l = 0;
        int i1 = 0;
        for(int j1 = 0; j1 < hfComponentList.size(); j1++)
        {
            HFComponent hfcomponent = (HFComponent)hfComponentList.get(j1);
            if(j1 == 0)
            {
                i = hfcomponent.getPreferredSize().width;
                j = hfcomponent.getPreferredSize().height;
            }
            i1++;
            if(hfcomponent.getHFElement().getClass().equals(com/fr/base/headerfooter/NewLineHFElement))
            {
                l = Math.max(l, i1);
                k++;
                i1 = 0;
            }
        }

        l = Math.max(l, i1);
        k++;
        return new Dimension((i + 1) * l + 3, (j + 2) * k);
    }

    public Dimension getPreferredScrollableViewportSize()
    {
        return getPreferredSize();
    }

    public int getScrollableUnitIncrement(Rectangle rectangle, int i, int j)
    {
        switch(i)
        {
        case 1: // '\001'
            return rectangle.height / 10;

        case 0: // '\0'
            return rectangle.width / 10;
        }
        throw new IllegalArgumentException((new StringBuilder()).append("Invalid orientation: ").append(i).toString());
    }

    public int getScrollableBlockIncrement(Rectangle rectangle, int i, int j)
    {
        switch(i)
        {
        case 1: // '\001'
            return rectangle.height;

        case 0: // '\0'
            return rectangle.width;
        }
        throw new IllegalArgumentException((new StringBuilder()).append("Invalid orientation: ").append(i).toString());
    }

    public boolean getScrollableTracksViewportWidth()
    {
        if(getParent() instanceof JViewport)
            return getParent().getWidth() > getPreferredSize().width;
        else
            return false;
    }

    public boolean getScrollableTracksViewportHeight()
    {
        if(getParent() instanceof JViewport)
            return getParent().getHeight() > getPreferredSize().height;
        else
            return false;
    }
}
