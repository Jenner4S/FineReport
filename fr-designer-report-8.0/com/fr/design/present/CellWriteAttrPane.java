// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.present;

import com.fr.base.FRContext;
import com.fr.base.Style;
import com.fr.design.dialog.*;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.*;
import com.fr.design.widget.WidgetPane;
import com.fr.form.ui.*;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.Selection;
import com.fr.privilege.finegrain.WidgetPrivilegeControl;
import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.elementcase.TemplateElementCase;
import java.text.SimpleDateFormat;

public class CellWriteAttrPane extends BasicPane
{

    private WidgetPane cellEditorDefPane;

    public CellWriteAttrPane(ElementCasePane elementcasepane)
    {
        setLayout(FRGUIPaneFactory.createBorderLayout());
        cellEditorDefPane = new WidgetPane(elementcasepane);
        add(cellEditorDefPane, "Center");
    }

    public static void showWidgetWindow(ElementCasePane elementcasepane)
    {
        CellWriteAttrPane cellwriteattrpane = new CellWriteAttrPane(elementcasepane);
        Selection selection = elementcasepane.getSelection();
        if(!(selection instanceof CellSelection))
        {
            return;
        } else
        {
            CellSelection cellselection = (CellSelection)selection;
            TemplateElementCase templateelementcase = elementcasepane.getEditingElementCase();
            TemplateCellElement templatecellelement = templateelementcase.getTemplateCellElement(cellselection.getColumn(), cellselection.getRow());
            cellwriteattrpane.populate(templatecellelement);
            BasicDialog basicdialog = cellwriteattrpane.showWindow(DesignerContext.getDesignerFrame());
            basicdialog.addDialogActionListener(new DialogActionAdapter(cellwriteattrpane, templatecellelement) {

                final CellWriteAttrPane val$wp;
                final TemplateCellElement val$editCellElement;

                public void doOk()
                {
                    wp.update(editCellElement);
                    DesignerContext.getDesignerFrame().getSelectedJTemplate().fireTargetModified();
                }

            
            {
                wp = cellwriteattrpane;
                editCellElement = templatecellelement;
                super();
            }
            }
);
            DesignerContext.setReportWritePane(basicdialog);
            basicdialog.setVisible(true);
            return;
        }
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("FR-Designer-Widget_Settings");
    }

    public void populate(TemplateCellElement templatecellelement)
    {
        if(templatecellelement == null)
            templatecellelement = new DefaultTemplateCellElement(0, 0, null);
        Widget widget = templatecellelement.getWidget();
        if(widget != null && (widget instanceof DateEditor))
        {
            DateEditor dateeditor = (DateEditor)widget;
            Style style = templatecellelement.getStyle();
            if(style != null)
            {
                java.text.Format format = style.getFormat();
                if(format != null && (format instanceof SimpleDateFormat))
                {
                    SimpleDateFormat simpledateformat = (SimpleDateFormat)format;
                    dateeditor.setFormatText(simpledateformat.toPattern());
                }
            }
        }
        if(widget != null)
            try
            {
                widget = (Widget)widget.clone();
            }
            catch(CloneNotSupportedException clonenotsupportedexception)
            {
                FRContext.getLogger().error(clonenotsupportedexception.getMessage(), clonenotsupportedexception);
            }
        cellEditorDefPane.populate(widget);
    }

    public void update(TemplateCellElement templatecellelement)
    {
        if(templatecellelement == null)
            return;
        Widget widget = cellEditorDefPane.update();
        if(widget instanceof DateEditor)
        {
            DateEditor dateeditor = (DateEditor)widget;
            String s = dateeditor.getFormatText();
            if(s != null)
            {
                Style style = templatecellelement.getStyle();
                if(style != null)
                    templatecellelement.setStyle(style.deriveFormat(new SimpleDateFormat(s)));
            }
        }
        if(widget instanceof NoneWidget)
        {
            templatecellelement.setWidget(null);
        } else
        {
            if(templatecellelement.getWidget() != null)
                widget = upDateWidgetAuthority(templatecellelement, widget);
            templatecellelement.setWidget(widget);
        }
    }

    private Widget upDateWidgetAuthority(TemplateCellElement templatecellelement, Widget widget)
    {
        try
        {
            Widget widget1 = (Widget)templatecellelement.getWidget().clone();
            if(widget.getClass() == widget1.getClass())
                widget.setWidgetPrivilegeControl((WidgetPrivilegeControl)widget1.getWidgetPrivilegeControl().clone());
        }
        catch(Exception exception)
        {
            FRLogger.getLogger().error(exception.getMessage());
        }
        return widget;
    }

    public void checkValid()
        throws Exception
    {
        cellEditorDefPane.checkValid();
    }
}
