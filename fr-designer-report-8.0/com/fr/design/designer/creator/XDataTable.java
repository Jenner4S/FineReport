// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator;

import com.fr.design.designer.beans.events.DesignerEditor;
import com.fr.design.mainframe.widget.editors.DataTableConfigPane;
import com.fr.design.mainframe.widget.editors.WidgetValueEditor;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.form.data.DataTableConfig;
import com.fr.form.ui.DataTable;
import com.fr.form.ui.WidgetValue;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import com.fr.stable.core.PropertyChangeAdapter;
import java.awt.Dimension;
import java.awt.Graphics;
import java.beans.IntrospectionException;
import javax.swing.JComponent;

// Referenced classes of package com.fr.design.designer.creator:
//            XWidgetCreator, CRPropertyDescriptor

public class XDataTable extends XWidgetCreator
{

    private DesignerEditor designerEditor;

    public XDataTable(DataTable datatable, Dimension dimension)
    {
        super(datatable, dimension);
    }

    protected String getIconName()
    {
        return "text_field_16.png";
    }

    public CRPropertyDescriptor[] supportedDescriptor()
        throws IntrospectionException
    {
        return (CRPropertyDescriptor[])(CRPropertyDescriptor[])ArrayUtils.addAll(super.supportedDescriptor(), new CRPropertyDescriptor[] {
            (new CRPropertyDescriptor("widgetValue", data.getClass())).setI18NName(Inter.getLocText(new String[] {
                "Widget", "Value"
            })).setEditorClass(com/fr/design/mainframe/widget/editors/WidgetValueEditor).setPropertyChangeListener(new PropertyChangeAdapter() {

                final XDataTable this$0;

                public void propertyChange()
                {
                    if(((DataTable)toData()).getWidgetValue() != null && (((DataTable)toData()).getWidgetValue().getValue() instanceof DataTableConfig))
                        ((DataTableConfigPane)designerEditor.getEditorTarget()).populate((DataTableConfig)((DataTable)toData()).getWidgetValue().getValue());
                }

            
            {
                this$0 = XDataTable.this;
                super();
            }
            }
)
        });
    }

    public void paint(Graphics g)
    {
        super.paint(g);
        designerEditor.paintEditor(g, getSize());
    }

    protected void initXCreatorProperties()
    {
        super.initXCreatorProperties();
        ((DataTableConfigPane)designerEditor.getEditorTarget()).setSize(getSize());
        LayoutUtils.layoutContainer(designerEditor.getEditorTarget());
        if(((DataTable)toData()).getWidgetValue() != null && (((DataTable)toData()).getWidgetValue().getValue() instanceof DataTableConfig))
            ((DataTableConfigPane)designerEditor.getEditorTarget()).populate((DataTableConfig)((DataTable)toData()).getWidgetValue().getValue());
    }

    public Dimension initEditorSize()
    {
        return new Dimension(250, 100);
    }

    public DesignerEditor getDesignerEditor()
    {
        return designerEditor;
    }

    protected JComponent initEditor()
    {
        if(designerEditor == null)
        {
            final DataTableConfigPane configPane = new DataTableConfigPane();
            designerEditor = new DesignerEditor(configPane);
            configPane.addpropertyChangeListener(designerEditor);
            designerEditor.addStopEditingListener(new PropertyChangeAdapter() {

                final DataTableConfigPane val$configPane;
                final XDataTable this$0;

                public void propertyChange()
                {
                    ((DataTable)toData()).setWidgetValue(new WidgetValue(configPane.update()));
                }

            
            {
                this$0 = XDataTable.this;
                configPane = datatableconfigpane;
                super();
            }
            }
);
        }
        return null;
    }

}
