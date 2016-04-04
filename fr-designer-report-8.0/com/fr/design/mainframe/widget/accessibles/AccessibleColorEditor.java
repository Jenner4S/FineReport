// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.accessibles;

import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.widget.editors.ColorTextField;
import com.fr.design.mainframe.widget.editors.ITextComponent;
import com.fr.design.mainframe.widget.wrappers.ColorWrapper;
import com.fr.design.style.color.*;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JToggleButton;

// Referenced classes of package com.fr.design.mainframe.widget.accessibles:
//            BaseAccessibleEditor, ColorPalette, ColorIcon

public class AccessibleColorEditor extends BaseAccessibleEditor
    implements ColorSelectable
{

    private ColorPalette palette;
    private Color defaultColor;
    private Color choosedColor;

    public AccessibleColorEditor()
    {
        super(new ColorWrapper(), new ColorWrapper(), true);
    }

    public void setDefaultColor(Color color)
    {
        defaultColor = color;
    }

    protected ITextComponent createTextField()
    {
        return new ColorTextField();
    }

    protected void showEditorPane()
    {
        if(palette == null)
        {
            palette = new ColorPalette();
            palette.addDefaultAction(new ActionListener() {

                final AccessibleColorEditor this$0;

                public void actionPerformed(ActionEvent actionevent)
                {
                    setResult(defaultColor);
                    palette.setVisible(false);
                }

            
            {
                this$0 = AccessibleColorEditor.this;
                super();
            }
            }
);
            palette.addCustomAction(new ActionListener() {

                final AccessibleColorEditor this$0;

                public void actionPerformed(ActionEvent actionevent)
                {
                    chooseCustomColor();
                    palette.setVisible(false);
                }

            
            {
                this$0 = AccessibleColorEditor.this;
                super();
            }
            }
);
            palette.addColorAction(new ActionListener() {

                final AccessibleColorEditor this$0;

                public void actionPerformed(ActionEvent actionevent)
                {
                    Color color = ((ColorIcon)((JToggleButton)actionevent.getSource()).getIcon()).getColor();
                    setResult(color);
                    palette.setVisible(false);
                }

            
            {
                this$0 = AccessibleColorEditor.this;
                super();
            }
            }
);
        }
        palette.setChoosedColor((Color)getValue());
        palette.show(this, 0, getHeight());
    }

    protected boolean isComboButton()
    {
        return true;
    }

    private void chooseCustomColor()
        throws HeadlessException
    {
        ColorSelectDetailPane colorselectdetailpane = new ColorSelectDetailPane(Color.WHITE);
        ColorSelectDialog.showDialog(DesignerContext.getDesignerFrame(), colorselectdetailpane, Color.WHITE, this);
        Color color = getColor();
        setResult(color);
    }

    private void setResult(Color color)
    {
        if(color != null)
        {
            setValue(color);
            fireStateChanged();
        }
    }

    public void setColor(Color color)
    {
        choosedColor = color;
    }

    public Color getColor()
    {
        return choosedColor;
    }

    public void colorSetted(ColorCell colorcell)
    {
    }




}
