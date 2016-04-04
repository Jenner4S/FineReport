// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator;

import com.fr.design.designer.beans.events.DesignerEditor;
import com.fr.design.gui.chart.BaseChartPropertyPane;
import com.fr.design.gui.chart.MiddleChartComponent;
import com.fr.design.mainframe.BaseJForm;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.widget.editors.WLayoutBorderStyleEditor;
import com.fr.design.mainframe.widget.renderer.LayoutBorderStyleRenderer;
import com.fr.design.module.DesignModuleFactory;
import com.fr.form.ui.BaseChartEditor;
import com.fr.form.ui.Widget;
import com.fr.general.Inter;
import com.fr.stable.core.PropertyChangeAdapter;
import java.awt.*;
import java.beans.IntrospectionException;
import javax.swing.*;

// Referenced classes of package com.fr.design.designer.creator:
//            XBorderStyleWidgetCreator, CRPropertyDescriptor

public class XChartEditor extends XBorderStyleWidgetCreator
{

    private static final long serialVersionUID = 0x9eb977f70aba9dcfL;
    private DesignerEditor designerEditor;
    private boolean isRefreshing;

    public XChartEditor(BaseChartEditor basecharteditor)
    {
        this(basecharteditor, new Dimension(250, 150));
    }

    public XChartEditor(BaseChartEditor basecharteditor, Dimension dimension)
    {
        super((Widget)basecharteditor, dimension);
        isRefreshing = false;
    }

    public String getIconPath()
    {
        return super.getIconPath();
    }

    protected String getIconName()
    {
        return "Chart.png";
    }

    public String createDefaultName()
    {
        return "chart";
    }

    public boolean hasTitleStyle()
    {
        return true;
    }

    public CRPropertyDescriptor[] supportedDescriptor()
        throws IntrospectionException
    {
        return (new CRPropertyDescriptor[] {
            (new CRPropertyDescriptor("widgetName", data.getClass())).setI18NName(Inter.getLocText("Form-Widget_Name")), (new CRPropertyDescriptor("borderStyle", data.getClass())).setEditorClass(com/fr/design/mainframe/widget/editors/WLayoutBorderStyleEditor).setRendererClass(com/fr/design/mainframe/widget/renderer/LayoutBorderStyleRenderer).setI18NName(Inter.getLocText("Chart-Style_Name")).putKeyValue("category", "Advanced").setPropertyChangeListener(new PropertyChangeAdapter() {

                final XChartEditor this$0;

                public void propertyChange()
                {
                    initStyle();
                }

            
            {
                this$0 = XChartEditor.this;
                super();
            }
            }
)
        });
    }

    public boolean canEnterIntoParaPane()
    {
        return false;
    }

    public DesignerEditor getDesignerEditor()
    {
        return designerEditor;
    }

    protected void initXCreatorProperties()
    {
        super.initXCreatorProperties();
        initBorderStyle();
        com.fr.base.chart.BaseChartCollection basechartcollection = ((BaseChartEditor)data).getChartCollection();
        isRefreshing = true;
        ((MiddleChartComponent)designerEditor.getEditorTarget()).populate(basechartcollection);
        isRefreshing = false;
    }

    public JComponent createToolPane(final BaseJForm jform, final FormDesigner formDesigner)
    {
        getDesignerEditorTarget().addStopEditingListener(new PropertyChangeAdapter() {

            final BaseJForm val$jform;
            final FormDesigner val$formDesigner;
            final XChartEditor this$0;

            public void propertyChange()
            {
                JComponent jcomponent = jform.getEditingPane();
                if(jcomponent instanceof BaseChartPropertyPane)
                {
                    ((BaseChartPropertyPane)jcomponent).setSupportCellData(true);
                    ((BaseChartPropertyPane)jcomponent).populateChartPropertyPane(getDesignerEditorTarget().update(), formDesigner);
                }
            }

            
            {
                this$0 = XChartEditor.this;
                jform = basejform;
                formDesigner = formdesigner;
                super();
            }
        }
);
        final BaseChartPropertyPane propertyPane = DesignModuleFactory.getChartWidgetPropertyPane(formDesigner);
        SwingUtilities.invokeLater(new Runnable() {

            final BaseChartPropertyPane val$propertyPane;
            final FormDesigner val$formDesigner;
            final XChartEditor this$0;

            public void run()
            {
                if(getDesignerEditor().getEditorTarget() != null)
                {
                    propertyPane.setSupportCellData(true);
                    propertyPane.populateChartPropertyPane(getDesignerEditorTarget().update(), formDesigner);
                }
            }

            
            {
                this$0 = XChartEditor.this;
                propertyPane = basechartpropertypane;
                formDesigner = formdesigner;
                super();
            }
        }
);
        return propertyPane;
    }

    private MiddleChartComponent getDesignerEditorTarget()
    {
        MiddleChartComponent middlechartcomponent = null;
        if(getDesignerEditor().getEditorTarget() instanceof MiddleChartComponent)
            middlechartcomponent = (MiddleChartComponent)getDesignerEditor().getEditorTarget();
        return middlechartcomponent;
    }

    public void paint(Graphics g)
    {
        super.paint(g);
        designerEditor.paintEditor(g, getSize());
    }

    public Dimension initEditorSize()
    {
        return new Dimension(250, 100);
    }

    protected JComponent initEditor()
    {
        if(designerEditor == null)
        {
            final MiddleChartComponent chartComponent = DesignModuleFactory.getChartComponent(((BaseChartEditor)data).getChartCollection());
            if(chartComponent != null)
            {
                MiddleChartComponent middlechartcomponent = chartComponent;
                middlechartcomponent.setBorder(BorderFactory.createLineBorder(Color.lightGray));
                designerEditor = new DesignerEditor(middlechartcomponent);
                chartComponent.addStopEditingListener(designerEditor);
                designerEditor.addPropertyChangeListener(new PropertyChangeAdapter() {

                    final MiddleChartComponent val$chartComponent;
                    final XChartEditor this$0;

                    public void propertyChange()
                    {
                        if(!isRefreshing)
                            ((BaseChartEditor)data).resetChangeChartCollection(chartComponent.update());
                    }

            
            {
                this$0 = XChartEditor.this;
                chartComponent = middlechartcomponent;
                super();
            }
                }
);
            }
        }
        return null;
    }


}
