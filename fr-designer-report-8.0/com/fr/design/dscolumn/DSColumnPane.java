// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.dscolumn;

import com.fr.base.FRContext;
import com.fr.data.TableDataSource;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.frpane.UITabbedPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.report.cell.*;
import java.awt.Component;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

// Referenced classes of package com.fr.design.dscolumn:
//            DSColumnBasicPane, DSColumnConditionsPane, DSColumnAdvancedPane

public class DSColumnPane extends BasicPane
{

    private TableDataSource tplEC;
    private UITabbedPane tabbedPane;
    private DSColumnBasicPane basicPane;
    private DSColumnConditionsPane conditionPane;
    private DSColumnAdvancedPane advancedPane;
    private TemplateCellElement cellElement;
    protected Component lastSelectedComponent;
    public static final int SETTING_ALL = 2;
    public static final int SETTING_DSRELATED = 1;
    public ChangeListener appliedWizardTabChangeListener = new ChangeListener() {

        final DSColumnPane this$0;

        public void stateChanged(ChangeEvent changeevent)
        {
            try
            {
                if(lastSelectedComponent == null)
                    lastSelectedComponent = basicPane;
                Component component = tabbedPane.getSelectedComponent();
                if(lastSelectedComponent == basicPane)
                {
                    basicPane.update(cellElement);
                    refrushOtherTabs();
                }
                lastSelectedComponent = component;
            }
            catch(Exception exception)
            {
                FRContext.getLogger().error(exception.getMessage(), exception);
            }
        }

            
            {
                this$0 = DSColumnPane.this;
                super();
            }
    }
;
    PropertyChangeListener myPropertyChangeListener = new PropertyChangeListener() {

        final DSColumnPane this$0;

        public void propertyChange(PropertyChangeEvent propertychangeevent)
        {
            refrushOtherTabs();
        }

            
            {
                this$0 = DSColumnPane.this;
                super();
            }
    }
;

    public DSColumnPane()
    {
        basicPane = null;
        conditionPane = null;
        advancedPane = null;
        initComponents(2);
    }

    public DSColumnPane(int i)
    {
        basicPane = null;
        conditionPane = null;
        advancedPane = null;
        initComponents(i);
    }

    protected void initComponents(int i)
    {
        DSColumnPane dscolumnpane = this;
        dscolumnpane.setLayout(FRGUIPaneFactory.createBorderLayout());
        tabbedPane = new UITabbedPane();
        tabbedPane.addChangeListener(appliedWizardTabChangeListener);
        dscolumnpane.add(tabbedPane, "Center");
        basicPane = new DSColumnBasicPane(i);
        basicPane.addPropertyChangeListener("cellElement", myPropertyChangeListener);
        tabbedPane.addTab(Inter.getLocText("Basic"), basicPane);
        conditionPane = new DSColumnConditionsPane(i);
        tabbedPane.addTab(Inter.getLocText("Filter"), conditionPane);
        advancedPane = new DSColumnAdvancedPane(i);
        tabbedPane.addTab(Inter.getLocText("Advanced"), advancedPane);
        setPreferredSize(new Dimension(610, 400));
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("ExpandD-Data_Column");
    }

    public void populate(TableDataSource tabledatasource, TemplateCellElement templatecellelement)
        throws Exception
    {
        tplEC = tabledatasource;
        if(tabledatasource == null || templatecellelement == null)
        {
            cellElement = new DefaultTemplateCellElement();
            return;
        }
        try
        {
            cellElement = (TemplateCellElement)templatecellelement.clone();
        }
        catch(CloneNotSupportedException clonenotsupportedexception) { }
        basicPane.populate(tabledatasource, cellElement);
        conditionPane.populate(tabledatasource, cellElement);
        advancedPane.populate(cellElement);
    }

    public CellElement update()
    {
        basicPane.update(cellElement);
        conditionPane.update(cellElement);
        advancedPane.update(cellElement);
        return cellElement;
    }

    public void refrushOtherTabs()
    {
        if(conditionPane == null || advancedPane == null)
        {
            return;
        } else
        {
            conditionPane.populate(tplEC, cellElement);
            advancedPane.populate(cellElement);
            return;
        }
    }

    public void putElementcase(ElementCasePane elementcasepane)
    {
        basicPane.putElementcase(elementcasepane);
    }

    public void putCellElement(TemplateCellElement templatecellelement)
    {
        basicPane.putCellElement(templatecellelement);
    }



}
