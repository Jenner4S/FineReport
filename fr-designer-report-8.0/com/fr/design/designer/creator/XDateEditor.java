// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator;

import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.widget.editors.*;
import com.fr.design.mainframe.widget.renderer.DateCellRenderer;
import com.fr.form.ui.*;
import com.fr.general.DateUtils;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.core.PropertyChangeAdapter;
import java.awt.Color;
import java.awt.Dimension;
import java.beans.IntrospectionException;
import java.util.Date;
import javax.swing.JComponent;

// Referenced classes of package com.fr.design.designer.creator:
//            XDirectWriteEditor, CRPropertyDescriptor, XWScaleLayout, XWidgetCreator, 
//            XLayoutContainer

public class XDateEditor extends XDirectWriteEditor
{

    private UITextField textField;
    private XWidgetCreator.LimpidButton btn;

    public XDateEditor(DateEditor dateeditor, Dimension dimension)
    {
        super(dateeditor, dimension);
    }

    public CRPropertyDescriptor[] supportedDescriptor()
        throws IntrospectionException
    {
        return (CRPropertyDescriptor[])(CRPropertyDescriptor[])ArrayUtils.addAll(super.supportedDescriptor(), new CRPropertyDescriptor[] {
            (new CRPropertyDescriptor("widgetValue", data.getClass())).setI18NName(Inter.getLocText(new String[] {
                "Widget", "Value"
            })).setEditorClass(com/fr/design/mainframe/widget/editors/WidgetValueEditor).setPropertyChangeListener(new PropertyChangeAdapter() {

                final XDateEditor this$0;

                public void propertyChange()
                {
                    initFieldText();
                }

            
            {
                this$0 = XDateEditor.this;
                super();
            }
            }
), (new CRPropertyDescriptor("formatText", data.getClass())).setI18NName(Inter.getLocText("FR-Engine_Format")).setEditorClass(formatClass()).setRendererClass(com/fr/design/mainframe/widget/renderer/DateCellRenderer).putKeyValue("category", "Advanced"), (new CRPropertyDescriptor("startDate", data.getClass())).setI18NName(Inter.getLocText("FR-Designer_Start-Date")).putKeyValue("category", "Advanced").setEditorClass(com/fr/design/mainframe/widget/editors/DateRangeEditor), (new CRPropertyDescriptor("endDate", data.getClass())).setI18NName(Inter.getLocText("FR-Designer_End-Date")).putKeyValue("category", "Advanced").setEditorClass(com/fr/design/mainframe/widget/editors/DateRangeEditor), (new CRPropertyDescriptor("returnDate", data.getClass())).setI18NName(Inter.getLocText("FR-Designer_Return-Date")).putKeyValue("category", "Return-Value"), (new CRPropertyDescriptor("waterMark", data.getClass())).setI18NName(Inter.getLocText("FR-Designer_WaterMark")).putKeyValue("category", "Advanced")
        });
    }

    protected Class formatClass()
    {
        return com/fr/design/mainframe/widget/editors/DateFormatEditor;
    }

    private void initFieldText()
    {
        DateEditor dateeditor = (DateEditor)data;
        if(dateeditor.getWidgetValue() != null)
        {
            WidgetValue widgetvalue = dateeditor.getWidgetValue();
            String s = widgetvalue.toString();
            Object obj = widgetvalue.getValue();
            String s1 = dateeditor.getFormatText();
            if(obj instanceof Date)
                s = DateUtils.getDate2Str(s1, (Date)obj);
            if(StringUtils.isEmpty(s))
            {
                s = DateUtils.getDate2Str(s1, new Date());
                dateeditor.setWidgetValue(new WidgetValue(new Date()));
            }
            textField.setText(s);
        }
    }

    protected void initXCreatorProperties()
    {
        super.initXCreatorProperties();
        initFieldText();
    }

    protected JComponent initEditor()
    {
        if(editor == null)
        {
            editor = FRGUIPaneFactory.createBorderLayout_S_Pane();
            editor.add(textField = new UITextField(5), "Center");
            btn = new XWidgetCreator.LimpidButton(this, "", getIconPath(), toData().isVisible() ? 1.0F : 0.4F);
            btn.setPreferredSize(new Dimension(21, 21));
            editor.add(btn, "East");
            textField.setOpaque(false);
            editor.setBackground(Color.WHITE);
        }
        return editor;
    }

    protected String getIconName()
    {
        return "date_16.png";
    }

    protected void makeVisible(boolean flag)
    {
        btn.makeVisible(flag);
    }

    protected XLayoutContainer getCreatorWrapper(String s)
    {
        return new XWScaleLayout();
    }

    protected void addToWrapper(XLayoutContainer xlayoutcontainer, int i, int j)
    {
        setSize(i, j);
        xlayoutcontainer.add(this);
    }

    public boolean shouldScaleCreator()
    {
        return true;
    }

}
