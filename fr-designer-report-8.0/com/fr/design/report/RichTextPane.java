// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.report;

import com.fr.base.*;
import com.fr.design.cell.editor.RichTextToolBar;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.*;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.cellattr.CellGUIAttr;
import com.fr.report.cell.cellattr.core.*;
import com.fr.report.elementcase.ElementCase;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import java.math.BigDecimal;
import java.util.Iterator;
import javax.swing.JPanel;
import javax.swing.text.*;

// Referenced classes of package com.fr.design.report:
//            RichTextEditingPane

public class RichTextPane extends BasicPane
{

    public static final FRFont DEFAUL_FONT = FRFont.getInstance().applySize(13F);
    private RichTextEditingPane textPane;
    private RichTextToolBar toolBar;

    public RichTextPane()
    {
        initComponents();
    }

    protected void initComponents()
    {
        setLayout(FRGUIPaneFactory.createM_BorderLayout());
        JPanel jpanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        textPane = new RichTextEditingPane();
        textPane.setFont(DEFAUL_FONT);
        toolBar = new RichTextToolBar(textPane);
        jpanel.add(toolBar);
        JPanel jpanel1 = FRGUIPaneFactory.createBorderLayout_S_Pane();
        jpanel1.add(new UIScrollPane(textPane));
        add(jpanel1, "Center");
        add(jpanel, "North");
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("FR-Designer_RichTextEditor");
    }

    public void populate(ElementCase elementcase, CellElement cellelement)
    {
        Object obj = cellelement.getValue();
        if(obj == null)
            return;
        if(obj instanceof Formula)
            obj = RichTextConverter.asFormula(String.valueOf(obj));
        if(obj instanceof Number)
            obj = StableUtils.convertNumberStringToString((Number)obj, false);
        if(!(obj instanceof RichText))
        {
            if(!(obj instanceof String))
                return;
            CellGUIAttr cellguiattr = cellelement.getCellGUIAttr();
            Style style = Style.DEFAULT_STYLE;
            FRFont frfont = cellelement.getStyle().getFRFont();
            double d = 96D;
            double d1 = 72D;
            int i = roundUp(((double)frfont.getSize() * d) / d1);
            frfont = FRFont.getInstance(frfont.getFontName(), frfont.getStyle(), i);
            style = style.deriveFRFont(frfont);
            boolean flag = cellguiattr != null && cellguiattr.isShowAsHTML();
            obj = RichTextConverter.converHtmlToRichText(flag, obj.toString(), style);
        }
        populateDocContent((RichText)obj);
    }

    private int roundUp(double d)
    {
        String s = Double.toString(d);
        s = (new BigDecimal(s)).setScale(0, 4).toString();
        return Integer.valueOf(s).intValue();
    }

    public RichText update()
    {
        RichText richtext = new RichText();
        DefaultStyledDocument defaultstyleddocument = (DefaultStyledDocument)textPane.getDocument();
        Style style = Style.DEFAULT_STYLE;
        updateRichText(defaultstyleddocument, style, richtext);
        return richtext;
    }

    private void updateRichText(DefaultStyledDocument defaultstyleddocument, Style style, RichText richtext)
    {
        int i;
        RichChar richchar;
        int j;
        i = defaultstyleddocument.getLength();
        richchar = new RichChar("", style);
        j = 0;
_L3:
        if(j >= i) goto _L2; else goto _L1
_L1:
        Element element = defaultstyleddocument.getCharacterElement(j);
        AttributeSet attributeset = element.getAttributes();
        FRFont frfont = evalFont(attributeset);
        style = style.deriveFRFont(frfont);
        String s;
        s = defaultstyleddocument.getText(j, 1);
        String s1 = parseFormula(s, defaultstyleddocument, j, i);
        if(StringUtils.isNotEmpty(s1))
        {
            richchar = new RichChar(s1, style);
            richtext.addContent(richchar);
            j += s1.length() - 1;
            continue; /* Loop/switch isn't completed */
        }
        if(richchar.styleEquals(style))
        {
            richchar.appendText(s);
            continue; /* Loop/switch isn't completed */
        }
        try
        {
            richchar = new RichChar(s, style);
            richtext.addContent(richchar);
        }
        catch(Exception exception)
        {
            FRContext.getLogger().error(exception.getMessage());
        }
        j++;
          goto _L3
_L2:
    }

    private String parseFormula(String s, DefaultStyledDocument defaultstyleddocument, int i, int j)
    {
        String s1 = "";
        if(!ComparatorUtils.equals(s, "$"))
            return s1;
        try
        {
            String s2 = defaultstyleddocument.getText(i, j - i);
            int k = s2.indexOf("}");
            if(k != -1)
                return s2.substring(0, k + 1);
        }
        catch(Exception exception) { }
        return s1;
    }

    private void populateDocContent(RichText richtext)
    {
        DefaultStyledDocument defaultstyleddocument = (DefaultStyledDocument)textPane.getDocument();
        Iterator iterator = richtext.charIterator();
        SimpleAttributeSet simpleattributeset = new SimpleAttributeSet();
        while(iterator.hasNext()) 
        {
            RichChar richchar = (RichChar)iterator.next();
            Style style = richchar.getStyle();
            String s = richchar.getText();
            populateRichTextStye(style, simpleattributeset);
            try
            {
                toolBar.removeInputListener();
                defaultstyleddocument.insertString(defaultstyleddocument.getLength(), s, simpleattributeset);
                toolBar.addInputListener();
            }
            catch(BadLocationException badlocationexception)
            {
                FRContext.getLogger().error(badlocationexception.getMessage());
            }
        }
    }

    private void populateRichTextStye(Style style, SimpleAttributeSet simpleattributeset)
    {
        if(style == null)
        {
            return;
        } else
        {
            FRFont frfont = style.getFRFont();
            StyleConstants.setFontFamily(simpleattributeset, frfont.getFamily());
            StyleConstants.setFontSize(simpleattributeset, frfont.getSize());
            StyleConstants.setBold(simpleattributeset, frfont.isBold());
            StyleConstants.setItalic(simpleattributeset, frfont.isItalic());
            StyleConstants.setUnderline(simpleattributeset, frfont.getUnderline() != 0);
            StyleConstants.setSubscript(simpleattributeset, frfont.isSubscript());
            StyleConstants.setSuperscript(simpleattributeset, frfont.isSuperscript());
            StyleConstants.setForeground(simpleattributeset, frfont.getForeground());
            toolBar.populateToolBar(simpleattributeset);
            return;
        }
    }

    private FRFont evalFont(AttributeSet attributeset)
    {
        String s = StyleConstants.getFontFamily(attributeset);
        int i = StyleConstants.getFontSize(attributeset);
        java.awt.Color color = StyleConstants.getForeground(attributeset);
        boolean flag = StyleConstants.isSubscript(attributeset);
        boolean flag1 = StyleConstants.isSuperscript(attributeset);
        int j = evalFontStyle(attributeset);
        int k = evalUnderLine(attributeset);
        return FRFont.getInstance(s, j, i, color, k, false, false, flag1, flag);
    }

    private int evalUnderLine(AttributeSet attributeset)
    {
        int i = 0;
        boolean flag = StyleConstants.isUnderline(attributeset);
        if(flag)
            i++;
        return i;
    }

    private int evalFontStyle(AttributeSet attributeset)
    {
        int i = 0;
        boolean flag = StyleConstants.isBold(attributeset);
        boolean flag1 = StyleConstants.isItalic(attributeset);
        if(flag)
            i++;
        if(flag1)
            i += 2;
        return i;
    }

}
