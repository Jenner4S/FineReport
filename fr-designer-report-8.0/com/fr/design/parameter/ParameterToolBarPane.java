// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.parameter;

import com.fr.base.Parameter;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.general.Inter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.BorderFactory;

public class ParameterToolBarPane extends BasicBeanPane
{
    private class FlowParameterPaneLayout
        implements LayoutManager
    {

        final ParameterToolBarPane this$0;

        public void addLayoutComponent(String s, Component component)
        {
        }

        public void removeLayoutComponent(Component component)
        {
        }

        public Dimension preferredLayoutSize(Container container)
        {
            int i = container.getWidth();
            layoutContainer(container);
            int j = parameterSelectedLabellist.size() != 0 ? breakid * 26 + 4 + 18 + 4 + addAll.getPreferredSize().height : 18;
            return new Dimension(i, j);
        }

        public Dimension minimumLayoutSize(Container container)
        {
            return new Dimension(0, 0);
        }

        public void layoutContainer(Container container)
        {
            int i = container.getWidth();
            int j = 0;
            int k = 22;
            label.setBounds(0, 0, i, 18);
            breakid = 1;
            for(Iterator iterator = parameterSelectedLabellist.iterator(); iterator.hasNext();)
            {
                UIButton uibutton = (UIButton)iterator.next();
                Dimension dimension = uibutton.getPreferredSize();
                if(j + dimension.width > i)
                {
                    breakid++;
                    j = 0;
                    k += dimension.height + 6;
                }
                uibutton.setBounds(j, k, dimension.width, dimension.height);
                j += dimension.width + 4;
            }

            addAll.setBounds(0, k + 6 + 20, i, addAll.getPreferredSize().height);
        }

        public FlowParameterPaneLayout()
        {
            this$0 = ParameterToolBarPane.this;
            super();
        }
    }


    private Parameter parameterList[];
    private ArrayList parameterSelectedLabellist;
    private MouseListener paraMouseListner;
    private UIButton addAll;
    private UILabel label;
    private int breakid;
    private static final int GAP_H = 4;
    private static final int GAP_V = 6;
    private static final int GAP_BV = 4;
    private static final int L_H = 18;

    public ParameterToolBarPane()
    {
        parameterSelectedLabellist = new ArrayList();
        setLayout(new FlowParameterPaneLayout());
        label = new UILabel() {

            private static final long serialVersionUID = 1L;
            final ParameterToolBarPane this$0;

            public Dimension getPreferredSize()
            {
                return new Dimension(super.getPreferredSize().width, 18);
            }

            
            {
                this$0 = ParameterToolBarPane.this;
                super();
            }
        }
;
        label.setText((new StringBuilder()).append(Inter.getLocText("Following_parameters_are_not_generated")).append(":").toString());
        label.setHorizontalAlignment(2);
        label.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));
        add(label);
        addAll = new UIButton(Inter.getLocText("Add-all"));
        add(addAll);
    }

    protected String title4PopupWindow()
    {
        return null;
    }

    public Parameter getTargetParameter(UIButton uibutton)
    {
        int i = parameterSelectedLabellist.indexOf(uibutton);
        if(i < 0 || i > parameterList.length - 1)
            return null;
        else
            return parameterList[i];
    }

    public void populateBean(Parameter aparameter[])
    {
        parameterSelectedLabellist.clear();
        removeAll();
        add(label);
        if(aparameter.length == 0)
        {
            setVisible(false);
            repaint();
            return;
        }
        setVisible(true);
        parameterList = aparameter;
        for(int i = 0; i < parameterList.length; i++)
        {
            UIButton uibutton = new UIButton(parameterList[i].getName());
            parameterSelectedLabellist.add(uibutton);
            add(uibutton);
        }

        UIButton uibutton1;
        for(Iterator iterator = parameterSelectedLabellist.iterator(); iterator.hasNext(); uibutton1.addMouseListener(paraMouseListner))
            uibutton1 = (UIButton)iterator.next();

        add(addAll);
        doLayout();
        repaint();
    }

    public Parameter[] updateBean()
    {
        return parameterList;
    }

    public void setParaMouseListener(MouseListener mouselistener)
    {
        paraMouseListner = mouselistener;
    }

    public void addActionListener(ActionListener actionlistener)
    {
        addAll.addActionListener(actionlistener);
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((Parameter[])obj);
    }






}
