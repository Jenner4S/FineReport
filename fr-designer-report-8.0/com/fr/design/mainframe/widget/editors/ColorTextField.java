// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.editors;

import com.fr.design.gui.itextfield.UITextField;
import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.JComponent;

// Referenced classes of package com.fr.design.mainframe.widget.editors:
//            ITextComponent

public class ColorTextField extends JComponent
    implements ITextComponent
{
    class ColorTextFieldLayout
        implements LayoutManager
    {

        final ColorTextField this$0;

        public void addLayoutComponent(String s, Component component)
        {
        }

        public void removeLayoutComponent(Component component)
        {
        }

        public Dimension preferredLayoutSize(Container container)
        {
            byte byte0 = 22;
            int i = ColorTextField.LEFT + ColorTextField.BOX + ColorTextField.ICON_TEXT_PAD + 60;
            return new Dimension(i, byte0);
        }

        public Dimension minimumLayoutSize(Container container)
        {
            byte byte0 = 22;
            int i = ColorTextField.LEFT + ColorTextField.BOX + ColorTextField.ICON_TEXT_PAD;
            return new Dimension(i, byte0);
        }

        public void layoutContainer(Container container)
        {
            int i = 0;
            int j = container.getWidth();
            int k = container.getHeight();
            if(getColor() != null)
            {
                i += ColorTextField.LEFT + ColorTextField.BOX + ColorTextField.ICON_TEXT_PAD;
                j -= i;
            }
            textField.setBounds(i, 0, j, k);
        }

        public ColorTextFieldLayout()
        {
            this$0 = ColorTextField.this;
            super();
        }
    }


    private static int BOX = 12;
    private static int LEFT = 4;
    private static int ICON_TEXT_PAD = 4;
    private Color color;
    private UITextField textField;

    public ColorTextField()
    {
        setLayout(new ColorTextFieldLayout());
        textField = new UITextField();
        textField.setBorder(null);
        textField.setOpaque(false);
        add(textField);
    }

    public void paintComponent(Graphics g)
    {
        int i = getWidth();
        int j = getHeight();
        g.setColor(Color.white);
        g.fillRect(0, 0, i, j);
        int k = LEFT;
        int l = (j - BOX) / 2;
        if(getColor() != null)
        {
            g.setColor(getColor());
            g.fillRect(k, l, BOX, BOX);
            g.setColor(Color.black);
            g.drawRect(k, l, BOX, BOX);
            k += BOX + ICON_TEXT_PAD;
        }
    }

    public Color getColor()
    {
        return color;
    }

    public void setColor(Color color1)
    {
        color = color1;
    }

    public void setText(String s)
    {
        textField.setText(s);
    }

    public String getText()
    {
        return textField.getText();
    }

    public void setEditable(boolean flag)
    {
        textField.setEditable(flag);
        textField.setBackground(Color.white);
    }

    public void requestFocus()
    {
        textField.requestFocus();
    }

    public void addActionListener(ActionListener actionlistener)
    {
        textField.addActionListener(actionlistener);
    }

    public void selectAll()
    {
        textField.selectAll();
    }

    public void setValue(Object obj)
    {
        color = (Color)obj;
    }





}
