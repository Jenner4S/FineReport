// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.design.DesignModelAdapter;
import com.fr.design.event.TargetModifiedEvent;
import com.fr.design.event.TargetModifiedListener;
import com.fr.design.gui.imenu.UIMenuItem;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.actions.ExcelExportAction4Chart;
import com.fr.design.mainframe.actions.PDFExportAction4Chart;
import com.fr.design.mainframe.actions.PNGExportAction4Chart;
import com.fr.design.mainframe.form.FormECCompositeProvider;
import com.fr.design.mainframe.toolbar.ToolBarMenuDockPlus;
import com.fr.design.menu.ShortCut;
import com.fr.design.menu.ToolBarDef;
import com.fr.file.*;
import com.fr.form.ui.ChartBook;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.json.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import javax.swing.*;

// Referenced classes of package com.fr.design.mainframe:
//            JTemplate, ChartDesigner, ChartArea, ChartUndoState, 
//            DesignerContext, DesignerFrame, ChartDesignerPropertyPane, EastRegionContainerPane, 
//            AuthorityEditPane, BaseUndoState

public class JChart extends JTemplate
{

    public static final String XML_TAG = "JChart";
    private static final String CHART_CARD = "FORM";
    private static final String ELEMENTCASE_CARD = "ELEMENTCASE";
    private static final String CARDNAME[] = {
        "FORM", "ELEMENTCASE"
    };
    private static final int TOOLBARPANEDIMHEIGHT_FORM = 60;
    ChartDesigner chartDesigner;
    private JPanel tabCenterPane;
    private CardLayout cardLayout;
    private JComponent editingComponent;
    private FormECCompositeProvider reportComposite;

    public JChart()
    {
        super(new ChartBook(), "Chart");
    }

    public JChart(ChartBook chartbook, FILE file)
    {
        super(chartbook, file);
    }

    protected JPanel createCenterPane()
    {
        tabCenterPane = FRGUIPaneFactory.createCardLayout_S_Pane();
        JPanel jpanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        jpanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, new Color(85, 85, 85)));
        chartDesigner = new ChartDesigner((ChartBook)getTarget());
        ChartArea chartarea = new ChartArea(chartDesigner);
        jpanel.add(chartarea, "Center");
        tabCenterPane.add(jpanel, "FORM", 0);
        chartDesigner.addTargetModifiedListener(new TargetModifiedListener() {

            final JChart this$0;

            public void targetModified(TargetModifiedEvent targetmodifiedevent)
            {
                fireTargetModified();
            }

            
            {
                this$0 = JChart.this;
                super();
            }
        }
);
        add(tabCenterPane, "Center");
        return tabCenterPane;
    }

    public void removeTemplateSelection()
    {
    }

    public void refreshContainer()
    {
    }

    public void removeParameterPaneSelection()
    {
    }

    protected DesignModelAdapter createDesignModel()
    {
        return null;
    }

    public UIMenuItem[] createMenuItem4Preview()
    {
        return new UIMenuItem[0];
    }

    protected ChartUndoState createUndoState()
    {
        return new ChartUndoState(this, chartDesigner.getArea());
    }

    protected void applyUndoState(ChartUndoState chartundostate)
    {
        try
        {
            setTarget((ChartBook)chartundostate.getChartBook().clone());
            chartDesigner.setTarget(getTarget());
            chartDesigner.populate();
        }
        catch(CloneNotSupportedException clonenotsupportedexception)
        {
            throw new RuntimeException(clonenotsupportedexception);
        }
    }

    public String suffix()
    {
        return ".crt";
    }

    public void copy()
    {
    }

    public boolean paste()
    {
        return false;
    }

    public boolean cut()
    {
        return false;
    }

    public AuthorityEditPane createAuthorityEditPane()
    {
        return null;
    }

    public ToolBarMenuDockPlus getToolBarMenuDockPlus()
    {
        return null;
    }

    public JPanel getEastUpPane()
    {
        return null;
    }

    public JPanel getEastDownPane()
    {
        return null;
    }

    public ToolBarDef[] toolbars4Target()
    {
        return new ToolBarDef[0];
    }

    public JPanel[] toolbarPanes4Form()
    {
        return new JPanel[0];
    }

    public ShortCut[] shortcut4TemplateMenu()
    {
        return new ShortCut[0];
    }

    public ShortCut[] shortCuts4Authority()
    {
        return new ShortCut[0];
    }

    public JComponent[] toolBarButton4Form()
    {
        return new JComponent[0];
    }

    public JComponent toolBar4Authority()
    {
        return chartDesigner.getChartToolBarPane();
    }

    public int getToolBarHeight()
    {
        return 0;
    }

    public boolean isJWorkBook()
    {
        return false;
    }

    public boolean isChartBook()
    {
        return true;
    }

    public void setAuthorityMode(boolean flag)
    {
    }

    public void refreshToolArea()
    {
        DesignerContext.getDesignerFrame().resetToolkitByPlus(this);
        chartDesigner.populate();
        ChartDesignerPropertyPane.getInstance().populateChartPropertyPane(((ChartBook)getTarget()).getChartCollection(), chartDesigner);
        EastRegionContainerPane.getInstance().replaceUpPane(ChartDesignerPropertyPane.getInstance());
    }

    public ShortCut[] shortcut4ExportMenu()
    {
        return (new ShortCut[] {
            new PNGExportAction4Chart(this), new ExcelExportAction4Chart(this), new PDFExportAction4Chart(this)
        });
    }

    public Icon getIcon()
    {
        return BaseUtils.readIcon("/com/fr/design/images/chart.png");
    }

    public ChartDesigner getChartDesigner()
    {
        return chartDesigner;
    }

    public void copyJS()
    {
        JSONObject jsonobject = ((ChartBook)getTarget()).createExportConfig();
        String s = "";
        if(jsonobject != null)
            try
            {
                if(jsonobject.has("charts"))
                {
                    JSONArray jsonarray = jsonobject.getJSONArray("charts");
                    s = jsonarray.toString(2);
                } else
                {
                    s = jsonobject.toString(2);
                }
                JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), Inter.getLocText("FR-Chart-CopyJS_Message"), (new StringBuilder()).append(Inter.getLocText("FR-Chart-Action_Copy")).append("JS").toString(), 1);
            }
            catch(JSONException jsonexception)
            {
                FRContext.getLogger().error(jsonexception.getMessage());
                JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), (new StringBuilder()).append(Inter.getLocText("FR-Chart-CopyJS_Failed")).append("!").toString(), Inter.getLocText("Error"), 0);
            }
        else
            JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), (new StringBuilder()).append(Inter.getLocText("FR-Chart-CopyJS_Failed")).append("!").toString(), Inter.getLocText("Error"), 0);
        StringSelection stringselection = new StringSelection(s);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringselection, null);
    }

    public void styleChange()
    {
        chartDesigner.clearToolBarStyleChoose();
    }

    protected FILEChooserPane getFILEChooserPane(boolean flag)
    {
        return new FILEChooserPane4Chart(true, flag);
    }

    protected volatile void applyUndoState(BaseUndoState baseundostate)
    {
        applyUndoState((ChartUndoState)baseundostate);
    }

    protected volatile BaseUndoState createUndoState()
    {
        return createUndoState();
    }

    protected volatile JComponent createCenterPane()
    {
        return createCenterPane();
    }

}
