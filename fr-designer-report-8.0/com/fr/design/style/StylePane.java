// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.style;

import com.fr.base.*;
import com.fr.base.core.StyleUtils;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.frpane.UITabbedPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.style.background.BackgroundPane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.grid.selection.*;
import com.fr.report.cell.*;
import com.fr.report.elementcase.ElementCase;
import com.fr.report.elementcase.TemplateElementCase;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

// Referenced classes of package com.fr.design.style:
//            FormatPane, AlignmentPane, FRFontPane, BorderPane, 
//            BorderUtils

public class StylePane extends BasicBeanPane
    implements ChangeListener
{
    public static class PreivewArea extends JComponent
    {

        private String paintText;
        private Style style;

        public void setStyle(Style style1)
        {
            style = style1;
            repaint();
        }

        public void paintComponent(Graphics g)
        {
            Graphics2D graphics2d = (Graphics2D)g;
            int i = ScreenResolution.getScreenResolution();
            if(style == Style.DEFAULT_STYLE)
            {
                Style.paintContent(graphics2d, paintText, style, getWidth() - 3, getHeight() - 3, i);
                return;
            } else
            {
                Style.paintBackground(graphics2d, style, getWidth() - 3, getHeight() - 3);
                Style.paintContent(graphics2d, paintText, style, getWidth() - 3, getHeight() - 3, i);
                Style.paintBorder(graphics2d, style, getWidth() - 3, getHeight() - 3);
                return;
            }
        }

        public Dimension getMinimumSize()
        {
            return getPreferredSize();
        }

        public PreivewArea()
        {
            paintText = "Report";
            style = Style.DEFAULT_STYLE;
            setPreferredSize(new Dimension(40, 40));
        }
    }

    private static class NormalStyleG extends GStyle
    {

        Style styleOfReportPane;
        Style newStyle;

        protected Style getStyle2Apply(Style style)
        {
            return StyleUtils.applyCellStyle(styleOfReportPane, newStyle, style);
        }

        NormalStyleG(Style style, Style style1)
        {
            styleOfReportPane = style;
            newStyle = style1;
        }
    }

    private static class NameStyleG extends GStyle
    {

        NameStyle nameStyle;

        protected Style getStyle2Apply(Style style)
        {
            return nameStyle;
        }

        NameStyleG(NameStyle namestyle)
        {
            nameStyle = namestyle;
        }
    }

    private static abstract class GStyle
    {

        protected abstract Style getStyle2Apply(Style style);

        private GStyle()
        {
        }

    }

    class FRFontListSelectionListener
        implements ListSelectionListener
    {

        final StylePane this$0;

        public void valueChanged(ListSelectionEvent listselectionevent)
        {
            updatePreviewArea();
        }

        FRFontListSelectionListener()
        {
            this$0 = StylePane.this;
            super();
        }
    }


    private static final int BORDER_ARRAY_LENGTH = 4;
    private static final int ALIGNMENT_INDEX = 1;
    private static final int FONT_INDEX = 2;
    private static final int BORDER_INDEX = 3;
    private static final int BACKGROUND_INDEX = 4;
    private ElementCasePane reportPane;
    protected Style editing;
    private NameStyle globalStyle;
    private FormatPane formatPane;
    private AlignmentPane alignmentPane;
    private FRFontPane frFontPane;
    private BorderPane borderPane;
    private BackgroundPane backgroundPane;
    private PreivewArea previewArea;
    private JPanel previewPane;
    protected ChangeListener tabChangeActionListener;

    public StylePane()
    {
        formatPane = null;
        alignmentPane = null;
        frFontPane = null;
        borderPane = null;
        backgroundPane = null;
        tabChangeActionListener = new ChangeListener() {

            final StylePane this$0;

            public void stateChanged(ChangeEvent changeevent)
            {
                Object obj = changeevent.getSource();
                if(obj == null || !(obj instanceof JTabbedPane))
                    return;
                JTabbedPane jtabbedpane = (JTabbedPane)obj;
                int i = jtabbedpane.getSelectedIndex();
                if(jtabbedpane.getComponentAt(i).getClass() == javax/swing/JPanel)
                    if(i == 1)
                        jtabbedpane.setComponentAt(i, getAlignmentPane());
                    else
                    if(i == 2)
                        jtabbedpane.setComponentAt(i, getFRFontPane());
                    else
                    if(i == 3)
                        jtabbedpane.setComponentAt(i, getBorderPane());
                    else
                    if(i == 4)
                        jtabbedpane.setComponentAt(i, getBackgroundPane());
                updatePreviewArea();
            }

            
            {
                this$0 = StylePane.this;
                super();
            }
        }
;
        setLayout(FRGUIPaneFactory.createBorderLayout());
        previewPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        add(previewPane, "North");
        previewPane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("Preview"), null));
        previewArea = new PreivewArea();
        previewPane.add(previewArea, "Center");
        JPanel jpanel = FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(1);
        previewPane.add(jpanel, "East");
        UITabbedPane uitabbedpane = new UITabbedPane();
        add(uitabbedpane, "Center");
        uitabbedpane.addTab(Inter.getLocText("Format"), getFormatPane());
        uitabbedpane.addTab(Inter.getLocText("Alignment"), FRGUIPaneFactory.createY_AXISBoxInnerContainer_L_Pane());
        uitabbedpane.addTab(Inter.getLocText("Sytle-FRFont"), FRGUIPaneFactory.createY_AXISBoxInnerContainer_L_Pane());
        uitabbedpane.addTab(Inter.getLocText("Border"), FRGUIPaneFactory.createY_AXISBoxInnerContainer_L_Pane());
        uitabbedpane.addTab(Inter.getLocText("Background"), FRGUIPaneFactory.createY_AXISBoxInnerContainer_L_Pane());
        uitabbedpane.addChangeListener(tabChangeActionListener);
        setPreferredSize(new Dimension(450, 480));
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("Style");
    }

    public void stateChanged(ChangeEvent changeevent)
    {
        updatePreviewArea();
    }

    protected FormatPane getFormatPane()
    {
        if(formatPane == null)
        {
            formatPane = new FormatPane();
            if(editing != null)
                formatPane.populate(editing.getFormat());
        }
        return formatPane;
    }

    private AlignmentPane getAlignmentPane()
    {
        if(alignmentPane == null)
        {
            alignmentPane = new AlignmentPane();
            alignmentPane.addChangeListener(this);
            if(editing != null)
                alignmentPane.populate(editing);
        }
        return alignmentPane;
    }

    private FRFontPane getFRFontPane()
    {
        if(frFontPane == null)
        {
            frFontPane = new FRFontPane();
            frFontPane.addChangeListener(this);
            if(editing != null)
                frFontPane.populate(editing.getFRFont());
        }
        return frFontPane;
    }

    private BorderPane getBorderPane()
    {
        if(borderPane == null)
        {
            borderPane = new BorderPane();
            borderPane.addChangeListener(this);
            if(reportPane != null)
            {
                Object aobj[] = BorderUtils.createCellBorderObject(reportPane);
                if(aobj != null && aobj.length == 4)
                    borderPane.populate((CellBorderStyle)aobj[0], ((Boolean)aobj[1]).booleanValue(), ((Integer)aobj[2]).intValue(), (Color)aobj[3]);
            } else
            if(editing != null)
                borderPane.populate(editing);
        }
        return borderPane;
    }

    private BackgroundPane getBackgroundPane()
    {
        if(backgroundPane == null)
        {
            backgroundPane = new BackgroundPane();
            backgroundPane.addChangeListener(this);
            if(editing != null)
                backgroundPane.populate(editing.getBackground());
        }
        return backgroundPane;
    }

    public NameStyle getGlobalStyle()
    {
        return globalStyle;
    }

    public void setGlobalStyle(NameStyle namestyle)
    {
        globalStyle = namestyle;
    }

    public void populate(ElementCasePane elementcasepane)
    {
        reportPane = elementcasepane;
        populateBean(analyzeCurrentStyle(elementcasepane));
        updatePreviewArea();
    }

    private Style analyzeCurrentStyle(ElementCasePane elementcasepane)
    {
        Style style = null;
        Selection selection = elementcasepane.getSelection();
        if(selection instanceof FloatSelection)
        {
            TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
            FloatElement floatelement = templateelementcase.getFloatElement(((FloatSelection)selection).getSelectedFloatName());
            style = floatelement.getStyle();
        } else
        {
            CellSelection cellselection = (CellSelection)selection;
            TemplateElementCase templateelementcase1 = elementcasepane.getEditingElementCase();
            CellElement cellelement = templateelementcase1.getCellElement(cellselection.getColumn(), cellselection.getRow());
            if(cellelement != null && cellelement.getStyle() != null)
                try
                {
                    style = (Style)cellelement.getStyle().clone();
                }
                catch(CloneNotSupportedException clonenotsupportedexception)
                {
                    FRContext.getLogger().error(clonenotsupportedexception.getMessage(), clonenotsupportedexception);
                }
        }
        if(style == null)
            style = Style.DEFAULT_STYLE;
        return style;
    }

    public boolean updateGlobalStyle(ElementCasePane elementcasepane)
    {
        updatePreviewArea();
        NameStyle namestyle = getGlobalStyle();
        return applyStyle(elementcasepane, new NameStyleG(namestyle));
    }

    public boolean update(ElementCasePane elementcasepane)
    {
        Style style = analyzeCurrentStyle(elementcasepane);
        return applyStyle(elementcasepane, new NormalStyleG(style, updateBean()));
    }

    private boolean applyStyle(ElementCasePane elementcasepane, GStyle gstyle)
    {
        TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
        Selection selection = elementcasepane.getSelection();
        if(selection instanceof FloatSelection)
        {
            FloatElement floatelement = templateelementcase.getFloatElement(((FloatSelection)selection).getSelectedFloatName());
            floatelement.setStyle(gstyle.getStyle2Apply(floatelement.getStyle()));
        } else
        {
            CellSelection cellselection = (CellSelection)selection;
            for(int i = 0; i < cellselection.getRowSpan(); i++)
            {
                for(int j = 0; j < cellselection.getColumnSpan(); j++)
                {
                    int k = j + cellselection.getColumn();
                    int l = i + cellselection.getRow();
                    Object obj = templateelementcase.getTemplateCellElement(k, l);
                    if(obj == null)
                    {
                        obj = new DefaultTemplateCellElement(k, l);
                        templateelementcase.addCellElement(((TemplateCellElement) (obj)));
                    }
                    ((TemplateCellElement) (obj)).setStyle(gstyle.getStyle2Apply(((TemplateCellElement) (obj)).getStyle()));
                }

            }

        }
        if(borderPane != null)
            BorderUtils.update(elementcasepane, borderPane.update());
        elementcasepane.repaint();
        return true;
    }

    public void populateBean(Style style)
    {
        editing = style != null ? style : Style.getInstance();
        if(formatPane != null)
            formatPane.populate(editing.getFormat());
        if(alignmentPane != null)
            alignmentPane.populate(editing);
        if(frFontPane != null)
            frFontPane.populate(editing.getFRFont());
        if(borderPane != null)
            borderPane.populate(editing);
        if(backgroundPane != null)
            backgroundPane.populate(editing.getBackground());
        updatePreviewArea();
    }

    public Style updateBean()
    {
        try
        {
            if(alignmentPane != null)
                alignmentPane.checkValid();
        }
        catch(Exception exception)
        {
            JOptionPane.showMessageDialog(this, exception.getMessage());
            return editing;
        }
        Style style = editing;
        if(formatPane != null)
            style = style.deriveFormat(formatPane.update());
        if(alignmentPane != null)
            style = alignmentPane.update(style);
        if(frFontPane != null)
            style = style.deriveFRFont(frFontPane.update());
        if(borderPane != null)
            style = borderPane.update(style);
        if(backgroundPane != null)
            style = style.deriveBackground(backgroundPane.update());
        return style;
    }

    public void updatePreviewArea()
    {
        if(editing != null)
            previewArea.setStyle(updateBean());
    }

    public PreivewArea getPreviewArea()
    {
        return previewArea;
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((Style)obj);
    }




}
