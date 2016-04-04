// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.design.gui.frpane.UITitlePanel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itabpane.TitleChangeListener;
import com.fr.design.mainframe.cell.CellElementEditPane;
import com.fr.general.Inter;
import com.fr.grid.selection.*;
import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.report.cell.Elem;
import com.fr.report.elementcase.TemplateElementCase;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.*;

// Referenced classes of package com.fr.design.mainframe:
//            DockingView, ElementCasePane, EastRegionContainerPane

public class CellElementPropertyPane extends DockingView
{

    private static CellElementPropertyPane singleton;
    private CellElementEditPane cellElementEditPane;
    private JPanel titlePane;
    private UILabel title;
    private TitleChangeListener titleListener;

    public static synchronized CellElementPropertyPane getInstance()
    {
        if(singleton == null)
            singleton = new CellElementPropertyPane();
        return singleton;
    }

    private CellElementPropertyPane()
    {
        titleListener = new TitleChangeListener() {

            final CellElementPropertyPane this$0;

            public void fireTitleChange(String s)
            {
                title.setText((new StringBuilder()).append(Inter.getLocText("CellElement-Property_Table")).append('-').append(s).toString());
            }

            
            {
                this$0 = CellElementPropertyPane.this;
                super();
            }
        }
;
        setLayout(new BorderLayout());
        setBorder(null);
        cellElementEditPane = new CellElementEditPane();
        cellElementEditPane.addTitleChangeListner(titleListener);
        titlePane = new JPanel(new BorderLayout());
        title = new UILabel((new StringBuilder()).append(getViewTitle()).append('-').append(Inter.getLocText("ExpandD-Expand_Attribute")).toString()) {

            private static final long serialVersionUID = 1L;
            final CellElementPropertyPane this$0;

            public Dimension getPreferredSize()
            {
                return new Dimension(super.getPreferredSize().width, 19);
            }

            
            {
                this$0 = CellElementPropertyPane.this;
                super(s);
            }
        }
;
        title.setHorizontalAlignment(0);
        title.setVerticalAlignment(0);
        titlePane.add(title, "Center");
        titlePane.setBorder(BorderFactory.createEmptyBorder(0, 0, 1, 0));
        add(titlePane, "North");
        add(cellElementEditPane, "Center");
    }

    public void refreshDockingView()
    {
        singleton = new CellElementPropertyPane();
    }

    private Elem getSelectedElement(Selection selection, TemplateElementCase templateelementcase)
    {
        Object obj = null;
        if(selection instanceof CellSelection)
        {
            CellSelection cellselection = (CellSelection)selection;
            obj = templateelementcase.getCellElement(cellselection.getColumn(), cellselection.getRow());
        } else
        if(selection instanceof FloatSelection)
        {
            FloatSelection floatselection = (FloatSelection)selection;
            obj = templateelementcase.getFloatElement(floatselection.getSelectedFloatName());
        }
        if(obj == null)
            obj = new DefaultTemplateCellElement(0, 0);
        return ((Elem) (obj));
    }

    public void populate(ElementCasePane elementcasepane)
    {
        TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        if(templateelementcase == null)
        {
            return;
        } else
        {
            cellElementEditPane.populate(elementcasepane);
            return;
        }
    }

    public String getViewTitle()
    {
        return Inter.getLocText("CellElement-Property_Table");
    }

    public Icon getViewIcon()
    {
        return BaseUtils.readIcon("/com/fr/design/images/m_report/qb.png");
    }

    public DockingView.Location preferredLocation()
    {
        return DockingView.Location.WEST_BELOW;
    }

    public UITitlePanel createTitlePanel()
    {
        return new UITitlePanel(this);
    }

    public transient void GoToPane(String as[])
    {
        cellElementEditPane.setSelectedIndex(as);
        EastRegionContainerPane.getInstance().setWindow2PreferWidth();
    }

}
