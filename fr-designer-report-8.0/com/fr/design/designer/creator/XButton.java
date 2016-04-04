// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator;

import com.fr.base.ScreenResolution;
import com.fr.base.background.GradientBackground;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.widget.editors.*;
import com.fr.design.mainframe.widget.renderer.FontCellRenderer;
import com.fr.design.mainframe.widget.renderer.IconCellRenderer;
import com.fr.form.parameter.FormSubmitButton;
import com.fr.form.ui.*;
import com.fr.form.ui.container.WAbsoluteLayout;
import com.fr.general.*;
import com.fr.stable.ArrayUtils;
import com.fr.stable.core.PropertyChangeAdapter;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.beans.IntrospectionException;
import javax.swing.BorderFactory;
import javax.swing.JComponent;

// Referenced classes of package com.fr.design.designer.creator:
//            XWidgetCreator, CRPropertyDescriptor, XWAbsoluteLayout, XCreatorUtils

public class XButton extends XWidgetCreator
{

    public static final Background DEFAULTBG = new GradientBackground(new Color(247, 247, 247), new Color(210, 210, 210), 1);
    public static final Font DEFAULTFT = new Font("Song_TypeFace", 0, 12);
    public static final Color DEFAULTFOREGROUNDCOLOR;
    private Background bg;
    private UILabel contentLabel;

    public XButton(Button button, Dimension dimension)
    {
        this(new FreeButton(button), dimension);
    }

    public XButton(FreeButton freebutton, Dimension dimension)
    {
        super(freebutton, dimension);
    }

    public XButton(FormSubmitButton formsubmitbutton, Dimension dimension)
    {
        super(formsubmitbutton, dimension);
    }

    public Background getContentBackground()
    {
        return bg;
    }

    public void setContentBackground(Background background)
    {
        bg = background;
    }

    public UILabel getContentLabel()
    {
        return contentLabel;
    }

    public void setContentLabel(UILabel uilabel)
    {
        contentLabel = uilabel;
    }

    public CRPropertyDescriptor[] supportedDescriptor()
        throws IntrospectionException
    {
        CRPropertyDescriptor acrpropertydescriptor[] = ((FreeButton)data).isCustomStyle() ? getisCustomStyle() : getisnotCustomStyle();
        return (CRPropertyDescriptor[])(CRPropertyDescriptor[])ArrayUtils.addAll(super.supportedDescriptor(), acrpropertydescriptor);
    }

    protected CRPropertyDescriptor creatNonListenerStyle(int i)
        throws IntrospectionException
    {
        CRPropertyDescriptor acrpropertydescriptor[] = {
            (new CRPropertyDescriptor("text", data.getClass())).setI18NName(Inter.getLocText(new String[] {
                "Form-Button", "Name"
            })), (new CRPropertyDescriptor("customStyle", data.getClass())).setI18NName(Inter.getLocText(new String[] {
                "Form-Button", "Style"
            })).setEditorClass(com/fr/design/mainframe/widget/editors/ButtonTypeEditor).putKeyValue("category", "Advanced"), (new CRPropertyDescriptor("initialBackground", data.getClass())).setEditorClass(com/fr/design/mainframe/widget/editors/ImgBackgroundEditor).setI18NName(Inter.getLocText("FR-Designer_Background-Initial")).putKeyValue("category", "Advanced"), (new CRPropertyDescriptor("overBackground", data.getClass())).setEditorClass(com/fr/design/mainframe/widget/editors/ImgBackgroundEditor).setI18NName(Inter.getLocText("FR-Designer_Background-Over")).putKeyValue("category", "Advanced"), (new CRPropertyDescriptor("clickBackground", data.getClass())).setEditorClass(com/fr/design/mainframe/widget/editors/ImgBackgroundEditor).setI18NName(Inter.getLocText("FR-Designer_Background-Click")).putKeyValue("category", "Advanced"), (new CRPropertyDescriptor("font", data.getClass())).setI18NName(Inter.getLocText("FR-Designer_FRFont")).setEditorClass(com/fr/design/mainframe/widget/editors/FontEditor).setRendererClass(com/fr/design/mainframe/widget/renderer/FontCellRenderer).putKeyValue("category", "Advanced"), (new CRPropertyDescriptor("iconName", data.getClass())).setI18NName(Inter.getLocText("FR-Designer_Icon")).setEditorClass(com/fr/design/mainframe/widget/editors/IconEditor).setRendererClass(com/fr/design/mainframe/widget/renderer/IconCellRenderer).putKeyValue("category", "Advanced"), (new CRPropertyDescriptor("hotkeys", data.getClass())).setI18NName(Inter.getLocText("FR-Designer_Button-Hotkeys")).putKeyValue("category", "Advanced").setEditorClass(com/fr/design/mainframe/widget/editors/ShortCutTextEditor)
        };
        return acrpropertydescriptor[i];
    }

    protected CRPropertyDescriptor[] getisCustomStyle()
        throws IntrospectionException
    {
        return (new CRPropertyDescriptor[] {
            creatNonListenerStyle(0).setPropertyChangeListener(new PropertyChangeAdapter() {

                final XButton this$0;

                public void propertyChange()
                {
                    setButtonText(((FreeButton)data).getText());
                }

            
            {
                this$0 = XButton.this;
                super();
            }
            }
), creatNonListenerStyle(1).setPropertyChangeListener(new PropertyChangeAdapter() {

                final XButton this$0;

                public void propertyChange()
                {
                    checkButonType();
                }

            
            {
                this$0 = XButton.this;
                super();
            }
            }
), creatNonListenerStyle(2).setPropertyChangeListener(new PropertyChangeAdapter() {

                final XButton this$0;

                public void propertyChange()
                {
                    bg = ((FreeButton)data).getInitialBackground();
                }

            
            {
                this$0 = XButton.this;
                super();
            }
            }
), creatNonListenerStyle(3), creatNonListenerStyle(4), creatNonListenerStyle(5).setPropertyChangeListener(new PropertyChangeAdapter() {

                final XButton this$0;

                public void propertyChange()
                {
                    FreeButton freebutton = (FreeButton)data;
                    if(freebutton.getFont() != null)
                    {
                        contentLabel.setFont(freebutton.getFont().applyResolutionNP(ScreenResolution.getScreenResolution()));
                        contentLabel.setForeground(freebutton.getFont().getForeground());
                    }
                }

            
            {
                this$0 = XButton.this;
                super();
            }
            }
), creatNonListenerStyle(6), creatNonListenerStyle(7)
        });
    }

    protected CRPropertyDescriptor[] getisnotCustomStyle()
        throws IntrospectionException
    {
        return (new CRPropertyDescriptor[] {
            (new CRPropertyDescriptor("text", data.getClass())).setI18NName(Inter.getLocText(new String[] {
                "Form-Button", "Name"
            })).setPropertyChangeListener(new PropertyChangeAdapter() {

                final XButton this$0;

                public void propertyChange()
                {
                    setButtonText(((FreeButton)data).getText());
                }

            
            {
                this$0 = XButton.this;
                super();
            }
            }
), (new CRPropertyDescriptor("customStyle", data.getClass())).setI18NName(Inter.getLocText(new String[] {
                "Form-Button", "Style"
            })).setEditorClass(com/fr/design/mainframe/widget/editors/ButtonTypeEditor).putKeyValue("category", "Advanced").setPropertyChangeListener(new PropertyChangeAdapter() {

                final XButton this$0;

                public void propertyChange()
                {
                    checkButonType();
                }

            
            {
                this$0 = XButton.this;
                super();
            }
            }
), (new CRPropertyDescriptor("iconName", data.getClass())).setI18NName(Inter.getLocText("FR-Designer_Icon")).setEditorClass(com/fr/design/mainframe/widget/editors/IconEditor).setRendererClass(com/fr/design/mainframe/widget/renderer/IconCellRenderer).putKeyValue("category", "Advanced"), (new CRPropertyDescriptor("hotkeys", data.getClass())).setI18NName(Inter.getLocText("FR-Designer_Button-Hotkeys")).putKeyValue("category", "Advanced").setEditorClass(com/fr/design/mainframe/widget/editors/ShortCutTextEditor)
        });
    }

    protected JComponent initEditor()
    {
        if(editor == null)
        {
            editor = new UILabel();
            contentLabel = new UILabel();
        }
        return editor;
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        AlphaComposite alphacomposite = data.isVisible() ? (AlphaComposite)((Graphics2D)g).getComposite() : AlphaComposite.getInstance(3, 0.4F);
        ((Graphics2D)g).setComposite(alphacomposite);
        Dimension dimension = contentLabel.getSize();
        if(bg != null)
            bg.paint(g, new java.awt.geom.Rectangle2D.Double(0.0D, 0.0D, dimension.getWidth(), dimension.getHeight()));
    }

    public void setButtonText(String s)
    {
        contentLabel.setText(s);
    }

    private void checkButonType()
    {
        UILabel uilabel = contentLabel;
        FreeButton freebutton = (FreeButton)data;
        if(!freebutton.isCustomStyle())
        {
            uilabel.setBorder(BorderFactory.createLineBorder(new Color(148, 148, 148)));
            bg = DEFAULTBG;
            contentLabel.setFont(DEFAULTFT);
            contentLabel.setForeground(DEFAULTFOREGROUNDCOLOR);
            editor.setLayout(new BorderLayout());
            editor.add(uilabel, "Center");
        } else
        {
            uilabel.setBorder(null);
            editor.setLayout(new BorderLayout());
            editor.add(uilabel, "Center");
            if(freebutton.getFont() != null)
            {
                contentLabel.setFont(freebutton.getFont().applyResolutionNP(ScreenResolution.getScreenResolution()));
                contentLabel.setForeground(freebutton.getFont().getForeground());
            }
            uilabel.setBounds(0, 0, freebutton.getButtonWidth() != 0 ? freebutton.getButtonWidth() : getWidth(), freebutton.getButtonHeight() != 0 ? freebutton.getButtonHeight() : getHeight());
            bg = freebutton.getInitialBackground();
        }
    }

    protected void initXCreatorProperties()
    {
        super.initXCreatorProperties();
        checkButonType();
        UILabel uilabel = contentLabel;
        FreeButton freebutton = (FreeButton)data;
        uilabel.setText(freebutton.getText());
        if(freebutton.isCustomStyle() && freebutton.getFont() != null)
        {
            uilabel.setFont(freebutton.getFont().applyResolutionNP(ScreenResolution.getScreenResolution()));
            uilabel.setForeground(freebutton.getFont().getForeground());
        }
        uilabel.setVerticalAlignment(0);
        uilabel.setHorizontalAlignment(0);
        if(freebutton.getButtonHeight() > 0 && freebutton.getButtonWidth() > 0)
        {
            setSize(freebutton.getButtonWidth(), freebutton.getButtonHeight());
            uilabel.setSize(freebutton.getButtonWidth(), freebutton.getButtonHeight());
            XLayoutContainer xlayoutcontainer;
            if((xlayoutcontainer = XCreatorUtils.getParentXLayoutContainer(this)) instanceof XWAbsoluteLayout)
                ((XWAbsoluteLayout)xlayoutcontainer).toData().setBounds(toData(), getBounds());
        }
        uilabel.setEnabled(freebutton.isEnabled());
    }

    public Dimension initEditorSize()
    {
        FreeButton freebutton = (FreeButton)data;
        if(checkbutton(freebutton))
            return new Dimension(freebutton.getButtonWidth(), freebutton.getButtonHeight());
        else
            return super.initEditorSize();
    }

    private boolean checkbutton(FreeButton freebutton)
    {
        return freebutton.isCustomStyle() && freebutton.getButtonHeight() > 0 && freebutton.getButtonWidth() > 0;
    }

    protected String getIconName()
    {
        return "button_16.png";
    }

    static 
    {
        DEFAULTFOREGROUNDCOLOR = Color.BLACK;
    }



}
