// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.edit;

import com.fr.base.BaseUtils;
import com.fr.base.Style;
import com.fr.design.actions.ElementCaseAction;
import com.fr.design.actions.utils.ReportActionUtils;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.frpane.HyperlinkGroupPane;
import com.fr.design.mainframe.*;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.menu.MenuKeySet;
import com.fr.general.FRFont;
import com.fr.grid.selection.*;
import com.fr.js.NameJavaScriptGroup;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.FloatElement;
import com.fr.report.elementcase.TemplateElementCase;
import java.awt.Color;
import javax.swing.SwingUtilities;

public class HyperlinkAction extends ElementCaseAction
{

    private boolean b;

    public HyperlinkAction(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
        setMenuKeySet(KeySetUtils.HYPER_LINK);
        setName((new StringBuilder()).append(getMenuKeySet().getMenuKeySetName()).append("...").toString());
        setMnemonic(getMenuKeySet().getMnemonic());
        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_insert/hyperLink.png"));
    }

    public boolean executeActionReturnUndoRecordNeeded()
    {
        b = true;
        ElementCasePane elementcasepane = (ElementCasePane)getEditingComponent();
        if(elementcasepane == null)
        {
            return false;
        } else
        {
            final TemplateElementCase report = elementcasepane.getEditingElementCase();
            NameJavaScriptGroup namejavascriptgroup = getNameJSGroup(elementcasepane, report);
            final HyperlinkGroupPane pane = DesignerContext.getDesignerFrame().getSelectedJTemplate().getHyperLinkPane();
            pane.populate(namejavascriptgroup);
            final Selection sel = elementcasepane.getSelection();
            BasicDialog basicdialog = pane.showWindow(SwingUtilities.getWindowAncestor(elementcasepane));
            basicdialog.addDialogActionListener(new DialogActionAdapter() {

                final HyperlinkGroupPane val$pane;
                final Selection val$sel;
                final TemplateElementCase val$report;
                final HyperlinkAction this$0;

                public void doOk()
                {
                    super.doOk();
                    final NameJavaScriptGroup updateNameHyperlinks = pane.updateJSGroup();
                    if(sel instanceof FloatSelection)
                    {
                        FloatElement floatelement = report.getFloatElement(((FloatSelection)sel).getSelectedFloatName());
                        floatelement.setNameHyperlinkGroup(updateNameHyperlinks);
                    } else
                    {
                        ReportActionUtils.actionIterateWithCellSelection((CellSelection)sel, report, new com.fr.design.actions.utils.ReportActionUtils.IterAction() {

                            final NameJavaScriptGroup val$updateNameHyperlinks;
                            final _cls1 this$1;

                            public void dealWith(CellElement cellelement)
                            {
                                Style style = cellelement.getStyle();
                                FRFont frfont = style.getFRFont();
                                if(updateNameHyperlinks.size() > 0)
                                {
                                    frfont = frfont.applyForeground(Color.blue);
                                    frfont = frfont.applyUnderline(1);
                                } else
                                {
                                    frfont = frfont.applyForeground(Color.black);
                                    frfont = frfont.applyUnderline(0);
                                }
                                cellelement.setStyle(style.deriveFRFont(frfont));
                                cellelement.setNameHyperlinkGroup(updateNameHyperlinks);
                            }

                    
                    {
                        this$1 = _cls1.this;
                        updateNameHyperlinks = namejavascriptgroup;
                        super();
                    }
                        }
);
                    }
                }

                public void doCancel()
                {
                    b = false;
                }

            
            {
                this$0 = HyperlinkAction.this;
                pane = hyperlinkgrouppane;
                sel = selection;
                report = templateelementcase;
                super();
            }
            }
);
            basicdialog.setVisible(true);
            return b;
        }
    }

    private NameJavaScriptGroup getNameJSGroup(ElementCasePane elementcasepane, TemplateElementCase templateelementcase)
    {
        NameJavaScriptGroup namejavascriptgroup = null;
        Selection selection = elementcasepane.getSelection();
        if(selection instanceof FloatSelection)
        {
            FloatElement floatelement = templateelementcase.getFloatElement(((FloatSelection)selection).getSelectedFloatName());
            namejavascriptgroup = floatelement.getNameHyperlinkGroup();
        } else
        {
            CellElement cellelement = templateelementcase.getCellElement(((CellSelection)selection).getColumn(), ((CellSelection)selection).getRow());
            if(cellelement != null)
                namejavascriptgroup = cellelement.getNameHyperlinkGroup();
        }
        return namejavascriptgroup;
    }

}
