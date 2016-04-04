// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator;

import com.fr.design.ExtraDesignClassManager;
import com.fr.design.fun.FormElementCaseEditorProcessor;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.*;
import com.fr.design.mainframe.widget.editors.*;
import com.fr.design.mainframe.widget.renderer.LayoutBorderStyleRenderer;
import com.fr.design.mainframe.widget.renderer.PaddingMarginCellRenderer;
import com.fr.form.FormElementCaseContainerProvider;
import com.fr.form.FormElementCaseProvider;
import com.fr.form.ui.*;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import com.fr.stable.core.PropertyChangeAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.IntrospectionException;
import javax.swing.*;

// Referenced classes of package com.fr.design.designer.creator:
//            XBorderStyleWidgetCreator, CRPropertyDescriptor

public class XElementCase extends XBorderStyleWidgetCreator
    implements FormElementCaseContainerProvider
{

    private UILabel imageLable;
    private JPanel coverPanel;

    public XElementCase(ElementCaseEditor elementcaseeditor, Dimension dimension)
    {
        super(elementcaseeditor, dimension);
    }

    protected void initXCreatorProperties()
    {
        super.initXCreatorProperties();
        initBorderStyle();
    }

    public boolean hasTitleStyle()
    {
        return true;
    }

    public CRPropertyDescriptor[] supportedDescriptor()
        throws IntrospectionException
    {
        CRPropertyDescriptor acrpropertydescriptor[] = {
            (new CRPropertyDescriptor("widgetName", data.getClass())).setI18NName(Inter.getLocText("Form-Widget_Name")), (new CRPropertyDescriptor("borderStyle", data.getClass())).setEditorClass(com/fr/design/mainframe/widget/editors/WLayoutBorderStyleEditor).setRendererClass(com/fr/design/mainframe/widget/renderer/LayoutBorderStyleRenderer).setI18NName(Inter.getLocText("FR-Designer-Widget_Style")).putKeyValue("category", "Advanced").setPropertyChangeListener(new PropertyChangeAdapter() {

                final XElementCase this$0;

                public void propertyChange()
                {
                    initStyle();
                }

            
            {
                this$0 = XElementCase.this;
                super();
            }
            }
), (new CRPropertyDescriptor("margin", data.getClass())).setEditorClass(com/fr/design/mainframe/widget/editors/PaddingMarginEditor).setRendererClass(com/fr/design/mainframe/widget/renderer/PaddingMarginCellRenderer).setI18NName(Inter.getLocText("FR-Layout_Padding")).putKeyValue("category", "Advanced"), (new CRPropertyDescriptor("showToolBar", data.getClass())).setEditorClass(com/fr/design/mainframe/widget/editors/BooleanEditor).setI18NName(Inter.getLocText("Form-EC_toolbar")).putKeyValue("category", "Advanced")
        };
        FormElementCaseEditorProcessor formelementcaseeditorprocessor = ExtraDesignClassManager.getInstance().getPropertyTableEditor();
        if(formelementcaseeditorprocessor == null)
        {
            return acrpropertydescriptor;
        } else
        {
            java.beans.PropertyDescriptor apropertydescriptor[] = formelementcaseeditorprocessor.createPropertyDescriptor(data.getClass());
            return (CRPropertyDescriptor[])(CRPropertyDescriptor[])ArrayUtils.addAll(acrpropertydescriptor, apropertydescriptor);
        }
    }

    protected String getIconName()
    {
        return "text_field_16.png";
    }

    public String createDefaultName()
    {
        return "report";
    }

    protected JComponent initEditor()
    {
        if(editor == null)
        {
            setBorder(DEFALUTBORDER);
            editor = new JPanel();
            editor.setBackground(null);
            editor.setLayout(null);
            imageLable = initImageBackground();
            coverPanel = new CoverReportPane();
            coverPanel.setPreferredSize(imageLable.getPreferredSize());
            coverPanel.setBounds(imageLable.getBounds());
            editor.add(coverPanel);
            coverPanel.setVisible(false);
            editor.add(imageLable);
        }
        return editor;
    }

    private UILabel initImageBackground()
    {
        UILabel uilabel = new UILabel();
        BufferedImage bufferedimage = toData().getECImage();
        setLabelBackground(bufferedimage, uilabel);
        return uilabel;
    }

    private void setLabelBackground(Image image, UILabel uilabel)
    {
        ImageIcon imageicon = new ImageIcon(image);
        uilabel.setIcon(imageicon);
        uilabel.setOpaque(true);
        uilabel.setLayout(null);
        uilabel.setBounds(0, 0, imageicon.getIconWidth(), imageicon.getIconHeight());
    }

    public void displayCoverPane(boolean flag)
    {
        coverPanel.setVisible(flag);
        coverPanel.setPreferredSize(editor.getPreferredSize());
        coverPanel.setBounds(editor.getBounds());
        editor.repaint();
    }

    public JComponent getCoverPane()
    {
        return coverPanel;
    }

    public Dimension initEditorSize()
    {
        return new Dimension(250, 100);
    }

    public boolean isReport()
    {
        return true;
    }

    public boolean canEnterIntoParaPane()
    {
        return false;
    }

    public ElementCaseEditor toData()
    {
        return (ElementCaseEditor)data;
    }

    public FormElementCaseProvider getElementCase()
    {
        return toData().getElementCase();
    }

    public String getElementCaseContainerName()
    {
        return toData().getWidgetName();
    }

    public void setElementCase(FormElementCaseProvider formelementcaseprovider)
    {
        toData().setElementCase(formelementcaseprovider);
    }

    public void setBackground(BufferedImage bufferedimage)
    {
        toData().setECImage(bufferedimage);
        setEditorIcon(bufferedimage);
    }

    private void setEditorIcon(BufferedImage bufferedimage)
    {
        setLabelBackground(bufferedimage, imageLable);
    }

    public Dimension getSize()
    {
        return new Dimension(getWidth(), getHeight());
    }

    public void respondClick(EditingMouseListener editingmouselistener, MouseEvent mouseevent)
    {
        super.respondClick(editingmouselistener, mouseevent);
        switchTab(mouseevent, editingmouselistener);
    }

    private void switchTab(MouseEvent mouseevent, EditingMouseListener editingmouselistener)
    {
        FormDesigner formdesigner = editingmouselistener.getDesigner();
        if(mouseevent.getClickCount() == 2 || formdesigner.getCursor().getType() == 12)
        {
            FormElementCaseContainerProvider formelementcasecontainerprovider = (FormElementCaseContainerProvider)formdesigner.getComponentAt(mouseevent);
            formdesigner.switchTab(formelementcasecontainerprovider);
        }
    }

    public volatile AbstractBorderStyleWidget toData()
    {
        return toData();
    }

    public volatile Widget toData()
    {
        return toData();
    }
}
