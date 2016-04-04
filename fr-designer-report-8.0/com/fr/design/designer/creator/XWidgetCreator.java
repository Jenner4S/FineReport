// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.design.mainframe.FormDesigner;
import com.fr.form.ui.Widget;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.stable.core.PropertyChangeAdapter;
import java.awt.*;
import java.beans.IntrospectionException;
import javax.swing.*;

// Referenced classes of package com.fr.design.designer.creator:
//            XCreator, CRPropertyDescriptor

public abstract class XWidgetCreator extends XCreator
{
    public class LimpidButton extends JButton
    {

        private String name;
        private String imagePath;
        private float opacity;
        final XWidgetCreator this$0;

        public void draw()
        {
            try
            {
                ImageIcon imageicon = (ImageIcon)BaseUtils.readIcon(imagePath);
                Image image = imageicon.getImage();
                MediaTracker mediatracker = new MediaTracker(this);
                byte byte0 = 21;
                byte byte1 = 21;
                mediatracker.addImage(image, 0);
                mediatracker.waitForAll();
                GraphicsConfiguration graphicsconfiguration = (new JFrame()).getGraphicsConfiguration();
                java.awt.image.BufferedImage bufferedimage = graphicsconfiguration.createCompatibleImage(byte0, byte1, 3);
                Graphics2D graphics2d = (Graphics2D)bufferedimage.getGraphics();
                AlphaComposite alphacomposite = AlphaComposite.getInstance(3, 1.0F);
                graphics2d.setComposite(alphacomposite);
                graphics2d.drawImage(image, 0, 0, this);
                graphics2d.setColor(Color.black);
                graphics2d.drawString(name, 25, 20);
                graphics2d.dispose();
                AlphaComposite alphacomposite1 = AlphaComposite.getInstance(3, opacity);
                java.awt.image.BufferedImage bufferedimage1 = graphicsconfiguration.createCompatibleImage(byte0, byte1, 3);
                graphics2d = (Graphics2D)bufferedimage1.getGraphics();
                graphics2d.setComposite(alphacomposite1);
                graphics2d.drawImage(image, 2, 2, this);
                graphics2d.setColor(Color.black);
                graphics2d.drawString(name, 25, 20);
                graphics2d.dispose();
                setIgnoreRepaint(true);
                setFocusable(false);
                setBorder(null);
                setContentAreaFilled(false);
                setIcon(new ImageIcon(bufferedimage1));
                setRolloverIcon(new ImageIcon(bufferedimage1));
                setPressedIcon(new ImageIcon(bufferedimage));
            }
            catch(Exception exception)
            {
                FRContext.getLogger().error(exception.getMessage());
            }
        }

        public void makeVisible(boolean flag)
        {
            opacity = flag ? 1.0F : 0.4F;
            draw();
        }

        public LimpidButton(String s, String s1, float f)
        {
            this$0 = XWidgetCreator.this;
            super();
            opacity = 0.4F;
            name = s;
            imagePath = s1;
            opacity = f;
            draw();
        }
    }


    protected static final float FULL_OPACITY = 1F;
    protected static final float HALF_OPACITY = 0.4F;

    public XWidgetCreator(Widget widget, Dimension dimension)
    {
        super(widget, dimension);
        setOpaque(false);
    }

    public CRPropertyDescriptor[] supportedDescriptor()
        throws IntrospectionException
    {
        return (new CRPropertyDescriptor[] {
            (new CRPropertyDescriptor("widgetName", data.getClass())).setI18NName(Inter.getLocText("Form-Widget_Name")), (new CRPropertyDescriptor("enabled", data.getClass())).setI18NName(Inter.getLocText("FR-Designer_Enabled")).setPropertyChangeListener(new PropertyChangeAdapter() {

                final XWidgetCreator this$0;

                public void propertyChange()
                {
                    setEnabled(toData().isEnabled());
                }

            
            {
                this$0 = XWidgetCreator.this;
                super();
            }
            }
), (new CRPropertyDescriptor("visible", data.getClass())).setI18NName(Inter.getLocText("FR-Designer_Widget-Visible")).setPropertyChangeListener(new PropertyChangeAdapter() {

                final XWidgetCreator this$0;

                public void propertyChange()
                {
                    makeVisible(toData().isVisible());
                }

            
            {
                this$0 = XWidgetCreator.this;
                super();
            }
            }
)
        });
    }

    public Widget toData()
    {
        return data;
    }

    protected void initXCreatorProperties()
    {
        setEnabled(toData().isEnabled());
    }

    public void recalculateChildrenSize()
    {
    }

    protected void makeVisible(boolean flag)
    {
    }

    public void ChangeCreatorName(FormDesigner formdesigner, XCreator xcreator)
    {
        String s = xcreator.toData().getWidgetName();
        String s1 = JOptionPane.showInputDialog(formdesigner, Inter.getLocText("Form-Change_Widget_Name_Discription"), s);
        if(s1 != null)
            formdesigner.renameCreator(xcreator, s1);
    }
}
