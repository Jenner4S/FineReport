// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.design.constants.UIConstants;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
import com.fr.main.impl.WorkBook;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.mainframe:
//            AuthorityPropertyPane, SheetAuthorityEditPane, AuthorityEditPane

public class AuthoritySheetEditedPane extends AuthorityPropertyPane
{

    private static final int TITLE_HEIGHT = 19;
    private AuthorityEditPane authorityEditPane;

    public AuthoritySheetEditedPane(WorkBook workbook, int i)
    {
        super(HistoryTemplateListPane.getInstance().getCurrentEditingTemplate());
        authorityEditPane = null;
        setLayout(new BorderLayout());
        setBorder(null);
        UILabel uilabel = new UILabel(Inter.getLocText(new String[] {
            "DashBoard-Potence", "Edit"
        })) {

            final AuthoritySheetEditedPane this$0;

            public Dimension getPreferredSize()
            {
                return new Dimension(super.getPreferredSize().width, 19);
            }

            
            {
                this$0 = AuthoritySheetEditedPane.this;
                super(s);
            }
        }
;
        uilabel.setHorizontalAlignment(0);
        uilabel.setVerticalAlignment(0);
        JPanel jpanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        jpanel.add(uilabel, "Center");
        jpanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UIConstants.LINE_COLOR));
        add(jpanel, "North");
        authorityEditPane = new SheetAuthorityEditPane(workbook, i);
        add(authorityEditPane, "Center");
    }

    public void populate()
    {
        authorityEditPane.populateDetials();
    }
}
