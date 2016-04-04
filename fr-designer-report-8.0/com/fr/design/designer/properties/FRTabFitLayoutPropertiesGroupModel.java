// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.properties;

import com.fr.design.designer.creator.cardlayout.*;
import com.fr.design.mainframe.widget.editors.*;
import com.fr.form.ui.CardSwitchButton;
import com.fr.form.ui.container.cardlayout.*;
import com.fr.general.Inter;
import javax.swing.table.*;

// Referenced classes of package com.fr.design.designer.properties:
//            FRFitLayoutPropertiesGroupModel

public class FRTabFitLayoutPropertiesGroupModel extends FRFitLayoutPropertiesGroupModel
{

    private PropertyCellEditor titleEditor;
    private PropertyCellEditor gapEditor;
    private DefaultTableCellRenderer renderer;
    private WTabFitLayout layout;
    private XWTabFitLayout xfl;
    private static int PROPERTY_NAME_COLUMN = 0;
    private static int PROPERTY_VALUE_COLUMN = 1;

    public FRTabFitLayoutPropertiesGroupModel(XWTabFitLayout xwtabfitlayout)
    {
        super(xwtabfitlayout);
        xfl = xwtabfitlayout;
        layout = (WTabFitLayout)xwtabfitlayout.toData();
        renderer = new DefaultTableCellRenderer();
        titleEditor = new PropertyCellEditor(new StringEditor());
        gapEditor = new PropertyCellEditor(new IntegerPropertyEditor());
    }

    public String getGroupName()
    {
        return Inter.getLocText("FR-Designer_Current_tab");
    }

    public int getRowCount()
    {
        return 2;
    }

    public TableCellRenderer getRenderer(int i)
    {
        return renderer;
    }

    public TableCellEditor getEditor(int i)
    {
        switch(i)
        {
        case 0: // '\0'
            return gapEditor;
        }
        return titleEditor;
    }

    public Object getValue(int i, int j)
    {
        if(j == PROPERTY_NAME_COLUMN)
        {
            switch(i)
            {
            case 0: // '\0'
                return Inter.getLocText("FR-Designer_Component_Interval");
            }
            return Inter.getLocText("FR-Engine_Tab_Layout_Title");
        }
        switch(i)
        {
        case 0: // '\0'
            return Integer.valueOf(layout.getCompInterval());
        }
        return getTitle();
    }

    public boolean setValue(Object obj, int i, int j)
    {
        if(j == PROPERTY_VALUE_COLUMN)
        {
            if(i == 0)
            {
                int k = Integer.parseInt(String.valueOf(obj));
                setLayoutGap(k);
                return true;
            } else
            {
                layout.getCurrentCard().setText(String.valueOf(obj));
                return true;
            }
        } else
        {
            return false;
        }
    }

    private void setLayoutGap(int i)
    {
        if(xfl.canAddInterval(i))
        {
            int j = layout.getCompInterval();
            if(i != j)
            {
                xfl.moveContainerMargin();
                xfl.moveCompInterval(xfl.getAcualInterval());
                layout.setCompInterval(i);
                xfl.addCompInterval(xfl.getAcualInterval());
            }
        }
    }

    private String getTitle()
    {
        if(layout.getCurrentCard() == null)
            layout.setCurrentCard(getRelateSwitchButton());
        return layout.getCurrentCard().getText();
    }

    private CardSwitchButton getRelateSwitchButton()
    {
        int i = layout.getIndex();
        XWCardLayout xwcardlayout = (XWCardLayout)xfl.getBackupParent();
        XWCardMainBorderLayout xwcardmainborderlayout = (XWCardMainBorderLayout)xwcardlayout.getBackupParent();
        WCardMainBorderLayout wcardmainborderlayout = xwcardmainborderlayout.toData();
        WCardTitleLayout wcardtitlelayout = wcardmainborderlayout.getTitlePart();
        if(wcardtitlelayout == null)
        {
            return null;
        } else
        {
            WCardTagLayout wcardtaglayout = wcardtitlelayout.getTagPart();
            return wcardtaglayout != null ? wcardtaglayout.getSwitchButton(i) : null;
        }
    }

    public boolean isEditable(int i)
    {
        return true;
    }

}
