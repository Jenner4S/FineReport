// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator;

import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.widget.editors.ParameterEditor;
import com.fr.design.mainframe.widget.renderer.ParameterRenderer;
import com.fr.form.ui.IframeEditor;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.core.PropertyChangeAdapter;
import java.awt.Color;
import java.awt.Dimension;
import java.beans.IntrospectionException;
import javax.swing.JComponent;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.designer.creator:
//            XWidgetCreator, CRPropertyDescriptor

public class XIframeEditor extends XWidgetCreator
{

    public XIframeEditor(IframeEditor iframeeditor, Dimension dimension)
    {
        super(iframeeditor, dimension);
    }

    public CRPropertyDescriptor[] supportedDescriptor()
        throws IntrospectionException
    {
        return (CRPropertyDescriptor[])(CRPropertyDescriptor[])ArrayUtils.addAll(super.supportedDescriptor(), new CRPropertyDescriptor[] {
            (new CRPropertyDescriptor("src", data.getClass())).setI18NName(Inter.getLocText("Form-Url")).setPropertyChangeListener(new PropertyChangeAdapter() {

                final XIframeEditor this$0;

                public void propertyChange()
                {
                    initFieldText();
                }

            
            {
                this$0 = XIframeEditor.this;
                super();
            }
            }
), (new CRPropertyDescriptor("overflowx", data.getClass())).setI18NName(Inter.getLocText("Preference-Horizontal_Scroll_Bar_Visible")).putKeyValue("category", "Advanced"), (new CRPropertyDescriptor("overflowy", data.getClass())).setI18NName(Inter.getLocText("Preference-Vertical_Scroll_Bar_Visible")).putKeyValue("category", "Advanced"), (new CRPropertyDescriptor("parameters", data.getClass())).setI18NName(Inter.getLocText("Parameters")).setEditorClass(com/fr/design/mainframe/widget/editors/ParameterEditor).setRendererClass(com/fr/design/mainframe/widget/renderer/ParameterRenderer).putKeyValue("category", "Advanced")
        });
    }

    protected JComponent initEditor()
    {
        if(editor == null)
        {
            editor = FRGUIPaneFactory.createBorderLayout_S_Pane();
            UITextField uitextfield = new UITextField();
            editor.add(uitextfield, "North");
            JPanel jpanel = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
            jpanel.setBackground(Color.white);
            editor.add(jpanel, "Center");
        }
        return editor;
    }

    private void initFieldText()
    {
        IframeEditor iframeeditor = (IframeEditor)data;
        String s = iframeeditor.getSrc();
        ((UITextField)editor.getComponent(0)).setText(StringUtils.isNotEmpty(s) ? s : "http://ip:port/address?");
    }

    protected void initXCreatorProperties()
    {
        super.initXCreatorProperties();
        initFieldText();
    }

    public Dimension initEditorSize()
    {
        return new Dimension(160, 80);
    }

    public boolean canEnterIntoParaPane()
    {
        return false;
    }

    protected String getIconName()
    {
        return "iframe_16.png";
    }

}
