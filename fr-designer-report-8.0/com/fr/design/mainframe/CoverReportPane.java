// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.general.Inter;
import java.awt.*;
import javax.swing.Icon;
import javax.swing.JPanel;

public class CoverReportPane extends JPanel
{

    private UIButton editButton;
    private AlphaComposite composite;
    private LayoutManager coverLayout;

    public CoverReportPane()
    {
        composite = AlphaComposite.getInstance(3, 0.6F);
        coverLayout = new LayoutManager() {

            final CoverReportPane this$0;

            public void removeLayoutComponent(Component component)
            {
            }

            public Dimension preferredLayoutSize(Container container)
            {
                return container.getPreferredSize();
            }

            public Dimension minimumLayoutSize(Container container)
            {
                return null;
            }

            public void layoutContainer(Container container)
            {
                int i = container.getParent().getWidth();
                int j = container.getParent().getHeight();
                int k = editButton.getPreferredSize().width;
                int l = editButton.getPreferredSize().height;
                editButton.setBounds((i - k) / 2, (j - l) / 2, k, l);
            }

            public void addLayoutComponent(String s, Component component)
            {
            }

            
            {
                this$0 = CoverReportPane.this;
                super();
            }
        }
;
        setLayout(coverLayout);
        setBackground(null);
        setOpaque(false);
        editButton = new UIButton(Inter.getLocText("Edit"), BaseUtils.readIcon("/com/fr/design/images/control/edit.png")) {

            final CoverReportPane this$0;

            public Dimension getPreferredSize()
            {
                return new Dimension(60, 24);
            }

            
            {
                this$0 = CoverReportPane.this;
                super(s, icon);
            }
        }
;
        editButton.setBorderPainted(false);
        editButton.setExtraPainted(false);
        editButton.setBackground(new Color(176, 196, 222));
        add(editButton);
    }

    public void paint(Graphics g)
    {
        Graphics2D graphics2d = (Graphics2D)g;
        java.awt.Composite composite1 = graphics2d.getComposite();
        graphics2d.setComposite(composite);
        graphics2d.setColor(Color.white);
        graphics2d.fillRect(0, 0, getWidth(), getHeight());
        graphics2d.setComposite(composite1);
        super.paint(g);
    }

}
