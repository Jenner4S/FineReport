// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.cell.settingpane.style;

import com.fr.base.*;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.DesignerBean;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PredefinedStylePane extends FurtherBasicBeanPane
    implements DesignerBean
{

    private DefaultListModel defaultListModel;
    private JList styleList;
    private ChangeListener changeListener;

    public PredefinedStylePane()
    {
        defaultListModel = new DefaultListModel();
        styleList = new JList(defaultListModel);
        DefaultListCellRenderer defaultlistcellrenderer = new DefaultListCellRenderer() {

            private Style nameStyle;
            final PredefinedStylePane this$0;

            public Component getListCellRendererComponent(JList jlist, Object obj, int i, boolean flag, boolean flag1)
            {
                super.getListCellRendererComponent(jlist, obj, i, flag, flag1);
                if(obj instanceof Style)
                {
                    nameStyle = (Style)obj;
                    setText(" ");
                }
                setPreferredSize(new Dimension(210, 22));
                return this;
            }

            public void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                if(nameStyle == null)
                    return;
                String s = "abcedfgh";
                if(nameStyle instanceof NameStyle)
                    s = ((NameStyle)nameStyle).getName();
                Style.paintBackground((Graphics2D)g, nameStyle, getWidth() - 1, getHeight() - 1);
                Style.paintContent((Graphics2D)g, s, nameStyle, getWidth() - 1, getHeight() - 1, ScreenResolution.getScreenResolution());
                Style.paintBorder((Graphics2D)g, nameStyle, getWidth() - 1, getHeight() - 1);
            }

            
            {
                this$0 = PredefinedStylePane.this;
                super();
            }
        }
;
        styleList.setCellRenderer(defaultlistcellrenderer);
        setLayout(FRGUIPaneFactory.createBorderLayout());
        add(styleList, "Center");
        styleList.addMouseListener(new MouseAdapter() {

            final PredefinedStylePane this$0;

            public void mouseClicked(MouseEvent mouseevent)
            {
                int i = mouseevent.getClickCount();
                if(i == 1 && changeListener != null)
                    changeListener.stateChanged(new ChangeEvent(styleList));
            }

            
            {
                this$0 = PredefinedStylePane.this;
                super();
            }
        }
);
        DesignerContext.setDesignerBean("predefinedStyle", this);
    }

    public void addChangeListener(ChangeListener changelistener)
    {
        changeListener = changelistener;
    }

    public void reset()
    {
    }

    public void populateBean(NameStyle namestyle)
    {
        refreshBeanElement();
        int i = 0;
        do
        {
            if(i >= defaultListModel.getSize())
                break;
            if(ComparatorUtils.equals(namestyle, defaultListModel.get(i)))
            {
                styleList.setSelectedIndex(i);
                break;
            }
            i++;
        } while(true);
    }

    public NameStyle updateBean()
    {
        return (NameStyle)styleList.getSelectedValue();
    }

    public String title4PopupWindow()
    {
        return Inter.getLocText(new String[] {
            "PageSetup-Predefined", "Style"
        });
    }

    public boolean accept(Object obj)
    {
        return obj instanceof NameStyle;
    }

    public void refreshBeanElement()
    {
        defaultListModel.removeAllElements();
        if(ConfigManager.getProviderInstance().hasStyle())
        {
            NameStyle namestyle;
            for(Iterator iterator = ConfigManager.getProviderInstance().getStyleNameIterator(); iterator.hasNext(); defaultListModel.addElement(namestyle))
            {
                String s = (String)iterator.next();
                namestyle = NameStyle.getInstance(s);
            }

        }
        styleList.setModel(defaultListModel);
        GUICoreUtils.repaint(this);
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((NameStyle)obj);
    }


}
