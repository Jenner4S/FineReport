// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.bbs;

import com.fr.design.gui.ilable.ActionLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import com.fr.start.BBSGuestPaneProvider;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.mainframe.bbs:
//            BBSConstants

public class BBSGuestPane extends JPanel
    implements BBSGuestPaneProvider
{

    public BBSGuestPane()
    {
        setLayout(FRGUIPaneFactory.createLabelFlowLayout());
        initTableContent();
    }

    private void initTableContent()
    {
        JPanel jpanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        JPanel jpanel1 = initInfoPane();
        jpanel.add(jpanel1, "North");
        JPanel jpanel2 = initUserPane();
        jpanel.add(jpanel2, "Center");
        add(jpanel);
    }

    private JPanel initUserPane()
    {
        double d = -2D;
        double ad[] = {
            d, d, d, d, d
        };
        double ad1[] = {
            d
        };
        java.awt.Component acomponent[][] = new java.awt.Component[ad.length][ad1.length];
        for(int i = 0; i < acomponent.length; i++)
        {
            String s = BBSConstants.ALL_GUEST[i];
            String s1 = BBSConstants.ALL_LINK[i];
            acomponent[i][0] = getURLActionLabel(s, s1);
        }

        return TableLayoutHelper.createTableLayoutPane(acomponent, ad, ad1);
    }

    private JPanel initInfoPane()
    {
        JPanel jpanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        UILabel uilabel = new UILabel(Inter.getLocText("FR-Designer_Thank_guest"));
        UILabel uilabel1 = new UILabel(" ");
        jpanel.add(uilabel, "North");
        jpanel.add(uilabel1, "Center");
        return jpanel;
    }

    private ActionLabel getURLActionLabel(String s, final String url)
    {
        ActionLabel actionlabel = new ActionLabel(s);
        actionlabel.addActionListener(new ActionListener() {

            final String val$url;
            final BBSGuestPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                try
                {
                    Desktop.getDesktop().browse(new URI(url));
                }
                catch(Exception exception) { }
            }

            
            {
                this$0 = BBSGuestPane.this;
                url = s;
                super();
            }
        }
);
        return actionlabel;
    }
}
