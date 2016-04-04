// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.properties;

import com.fr.design.beans.GroupModel;
import com.fr.design.designer.beans.*;
import com.fr.design.designer.beans.events.CreatorEventListenerTable;
import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.designer.creator.*;
import com.fr.design.designer.creator.cardlayout.XWCardLayout;
import com.fr.design.gui.itable.AbstractPropertyTable;
import com.fr.design.gui.itable.PropertyGroup;
import com.fr.design.mainframe.*;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

// Referenced classes of package com.fr.design.designer.properties:
//            MultiSelectionBoundsModel

public class WidgetPropertyTable extends AbstractPropertyTable
{

    private FormDesigner designer;

    public WidgetPropertyTable(FormDesigner formdesigner)
    {
        setDesigner(formdesigner);
    }

    public static ArrayList getCreatorPropertyGroup(FormDesigner formdesigner, XCreator xcreator)
    {
        ArrayList arraylist = new ArrayList();
        ComponentAdapter componentadapter = AdapterBus.getComponentAdapter(formdesigner, xcreator);
        ArrayList arraylist1 = componentadapter.getXCreatorPropertyModel();
        GroupModel groupmodel;
        for(Iterator iterator = arraylist1.iterator(); iterator.hasNext(); arraylist.add(new PropertyGroup(groupmodel)))
            groupmodel = (GroupModel)iterator.next();

        XLayoutContainer xlayoutcontainer = XCreatorUtils.getParentXLayoutContainer(xcreator);
        if(xcreator.acceptType(new Class[] {
    com/fr/design/designer/creator/XWFitLayout
}) || xcreator.acceptType(new Class[] {
    com/fr/design/designer/creator/XWParameterLayout
}))
            xlayoutcontainer = null;
        if(xlayoutcontainer != null && !(xcreator instanceof XWCardLayout))
        {
            LayoutAdapter layoutadapter = xlayoutcontainer.getLayoutAdapter();
            com.fr.design.designer.beans.ConstraintsGroupModel constraintsgroupmodel = layoutadapter.getLayoutConstraints(xcreator);
            if(constraintsgroupmodel != null)
                arraylist.add(new PropertyGroup(constraintsgroupmodel));
        }
        if(xcreator instanceof XLayoutContainer)
        {
            LayoutAdapter layoutadapter1 = ((XLayoutContainer)xcreator).getLayoutAdapter();
            if(layoutadapter1 != null)
            {
                GroupModel groupmodel1 = layoutadapter1.getLayoutProperties();
                if(groupmodel1 != null)
                    arraylist.add(new PropertyGroup(groupmodel1));
            }
        }
        return arraylist;
    }

    public void initPropertyGroups(Object obj)
    {
        int i = designer.getSelectionModel().getSelection().size();
        if(i == 0 || i == 1)
        {
            Object obj1 = i != 0 ? ((Object) (designer.getSelectionModel().getSelection().getSelectedCreator())) : ((Object) (designer.getRootComponent()));
            if(designer.isRoot(((XCreator) (obj1))))
                groups = designer.getDesignerMode().createRootDesignerPropertyGroup();
            else
                groups = getCreatorPropertyGroup(designer, ((XCreator) (obj1)));
        } else
        {
            groups = new ArrayList();
            MultiSelectionBoundsModel multiselectionboundsmodel = new MultiSelectionBoundsModel(designer);
            groups.add(new PropertyGroup(multiselectionboundsmodel));
        }
        com.fr.design.gui.itable.AbstractPropertyTable.BeanTableModel beantablemodel = new com.fr.design.gui.itable.AbstractPropertyTable.BeanTableModel(this);
        setModel(beantablemodel);
        setAutoResizeMode(3);
        TableColumn tablecolumn = getColumn(getColumnName(0));
        tablecolumn.setPreferredWidth(30);
        repaint();
    }

    private void setDesigner(FormDesigner formdesigner)
    {
        designer = formdesigner;
    }

    public String getToolTipText(MouseEvent mouseevent)
    {
        int i = rowAtPoint(mouseevent.getPoint());
        int j = columnAtPoint(mouseevent.getPoint());
        if(i != -1 && j == 0)
            return String.valueOf(getValueAt(i, j));
        else
            return null;
    }

    public void firePropertyEdit()
    {
        designer.getEditListenerTable().fireCreatorModified(5);
    }


}
