// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator.cardlayout;

import com.fr.design.designer.beans.AdapterBus;
import com.fr.design.designer.beans.ComponentAdapter;
import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.designer.creator.*;
import com.fr.design.mainframe.EditingMouseListener;
import com.fr.design.mainframe.FormDesigner;
import com.fr.form.ui.container.cardlayout.WCardTitleLayout;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import javax.swing.border.Border;

// Referenced classes of package com.fr.design.designer.creator.cardlayout:
//            XWCardTagLayout, XWCardMainBorderLayout, XCardAddButton

public class XWCardTitleLayout extends XWBorderLayout
{

    private static final int CENTER = 1;

    public XWCardTitleLayout()
    {
    }

    public XWCardTitleLayout(WCardTitleLayout wcardtitlelayout, Dimension dimension)
    {
        super(wcardtitlelayout, dimension);
    }

    public void convert()
    {
        isRefreshing = true;
        WCardTitleLayout wcardtitlelayout = (WCardTitleLayout)toData();
        setVisible(wcardtitlelayout.isVisible());
        removeAll();
        String as[] = {
            "North", "South", "East", "West", "Center"
        };
        for(int i = 0; i < as.length; i++)
        {
            com.fr.form.ui.Widget widget = wcardtitlelayout.getLayoutWidget(as[i]);
            if(widget != null)
            {
                XWidgetCreator xwidgetcreator = (XWidgetCreator)XCreatorUtils.createXCreator(widget, calculatePreferredSize(widget));
                add(xwidgetcreator, as[i]);
                xwidgetcreator.setBackupParent(this);
            }
        }

        isRefreshing = false;
    }

    public XWCardTagLayout getTagPart()
    {
        return (XWCardTagLayout)getComponent(1);
    }

    public void addTagPart(XWCardTagLayout xwcardtaglayout)
    {
        add(xwcardtaglayout, "Center");
    }

    public void addNewButton(XCardAddButton xcardaddbutton)
    {
        add(xcardaddbutton, "East");
    }

    public void stopAddingState(FormDesigner formdesigner)
    {
        formdesigner.stopAddingState();
    }

    public void setBorder(Border border)
    {
    }

    public void respondClick(EditingMouseListener editingmouselistener, MouseEvent mouseevent)
    {
        FormDesigner formdesigner = editingmouselistener.getDesigner();
        SelectionModel selectionmodel = editingmouselistener.getSelectionModel();
        XWCardMainBorderLayout xwcardmainborderlayout = (XWCardMainBorderLayout)getBackupParent();
        if(xwcardmainborderlayout != null)
        {
            XWCardLayout xwcardlayout = xwcardmainborderlayout.getCardPart();
            selectionmodel.setSelectedCreator(xwcardlayout);
        }
        if(editingmouselistener.stopEditing() && this != formdesigner.getRootComponent())
        {
            ComponentAdapter componentadapter = AdapterBus.getComponentAdapter(formdesigner, this);
            editingmouselistener.startEditing(this, componentadapter.getDesignerEditor(), componentadapter);
        }
    }
}
