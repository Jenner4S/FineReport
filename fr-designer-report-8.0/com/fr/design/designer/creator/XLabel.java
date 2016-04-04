// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator;

import com.fr.base.*;
import com.fr.design.border.UIRoundedBorder;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.widget.editors.*;
import com.fr.design.mainframe.widget.renderer.FontCellRenderer;
import com.fr.design.mainframe.widget.renderer.LabelHorizontalAlignmentRenderer;
import com.fr.form.ui.*;
import com.fr.general.Background;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.beans.IntrospectionException;
import javax.swing.JComponent;

// Referenced classes of package com.fr.design.designer.creator:
//            XWidgetCreator, CRPropertyDescriptor

public class XLabel extends XWidgetCreator
{

    private int cornerSize;

    public XLabel(Label label, Dimension dimension)
    {
        super(label, dimension);
        cornerSize = 15;
    }

    public Label toData()
    {
        return (Label)data;
    }

    public CRPropertyDescriptor[] supportedDescriptor()
        throws IntrospectionException
    {
        return (CRPropertyDescriptor[])(CRPropertyDescriptor[])ArrayUtils.addAll(super.supportedDescriptor(), new CRPropertyDescriptor[] {
            (new CRPropertyDescriptor("widgetValue", data.getClass())).setI18NName(Inter.getLocText(new String[] {
                "FR-Designer_Widget", "Value"
            })).setEditorClass(com/fr/design/mainframe/widget/editors/WidgetValueEditor), (new CRPropertyDescriptor("wrap", data.getClass())).setI18NName(Inter.getLocText("FR-Designer_StyleAlignment-Wrap_Text")).putKeyValue("category", "Advanced"), (new CRPropertyDescriptor("verticalCenter", data.getClass())).setI18NName(Inter.getLocText("FR-Designer_PageSetup-Vertically")).putKeyValue("category", "Advanced"), (new CRPropertyDescriptor("textalign", data.getClass())).setI18NName(Inter.getLocText("FR-Designer_Alignment-Style")).setEditorClass(com/fr/design/mainframe/widget/editors/ItemCellEditor).setRendererClass(com/fr/design/mainframe/widget/renderer/LabelHorizontalAlignmentRenderer).putKeyValue("category", "Advanced"), (new CRPropertyDescriptor("font", data.getClass())).setI18NName(Inter.getLocText("FR-Designer_Font")).setEditorClass(com/fr/design/mainframe/widget/editors/FontEditor).setRendererClass(com/fr/design/mainframe/widget/renderer/FontCellRenderer).putKeyValue("category", "Advanced")
        });
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Label label = (Label)data;
        Dimension dimension = getSize();
        if(toData().getBackground() != null)
            toData().getBackground().paint(g, new java.awt.geom.Rectangle2D.Double(0.0D, 0.0D, dimension.getWidth(), dimension.getHeight()));
        if(label.getWidgetValue() != null)
        {
            Graphics2D graphics2d = (Graphics2D)g.create();
            BaseUtils.drawStringStyleInRotation(graphics2d, getWidth(), getHeight(), label.getWidgetValue().toString(), Style.getInstance(label.getFont()).deriveHorizontalAlignment(label.getTextalign()).deriveVerticalAlignment(label.isVerticalCenter() ? 0 : 1).deriveTextStyle(label.isWrap() ? 0 : 1), ScreenResolution.getScreenResolution());
        }
    }

    protected JComponent initEditor()
    {
        if(editor == null)
            editor = new UILabel();
        return editor;
    }

    protected void initXCreatorProperties()
    {
        super.initXCreatorProperties();
        if(toData().getBorder() != 0)
            setBorder(new UIRoundedBorder(toData().getBorder(), toData().getColor(), toData().isCorner() ? cornerSize : 0));
        else
            setBorder(DEFALUTBORDER);
    }

    protected String getIconName()
    {
        return "label_16.png";
    }

    public volatile Widget toData()
    {
        return toData();
    }
}
