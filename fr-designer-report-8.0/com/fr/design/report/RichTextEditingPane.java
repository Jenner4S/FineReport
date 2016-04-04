// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.report;

import com.fr.base.FRContext;
import com.fr.base.Formula;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.formula.FormulaFactory;
import com.fr.design.formula.UIFormula;
import com.fr.design.gui.frpane.UITextPane;
import com.fr.design.mainframe.DesignerContext;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.report.cell.cellattr.core.RichTextConverter;
import java.awt.event.*;
import javax.swing.text.*;

public class RichTextEditingPane extends UITextPane
{

    private static final int WRAPPER_LEN = 3;
    private static final int PREFIX_LEN = 2;
    private boolean updating;
    private MouseListener doubleClickFormulaListener;

    public RichTextEditingPane()
    {
        updating = false;
        doubleClickFormulaListener = new MouseAdapter() {

            final RichTextEditingPane this$0;

            private int findFormulaStart(int i, StyledDocument styleddocument)
                throws BadLocationException
            {
                for(int j = i - 1; j >= 0; j--)
                {
                    String s = styleddocument.getText(j, 1);
                    if(ComparatorUtils.equals(s, "}"))
                        return -1;
                    if(ComparatorUtils.equals(s, "{") && j - 1 >= 0 && ComparatorUtils.equals(styleddocument.getText(j - 1, 1), "$"))
                        return j - 1;
                }

                return -1;
            }

            private int findFormulaEnd(int i, StyledDocument styleddocument)
                throws BadLocationException
            {
                int j = styleddocument.getLength();
                for(int k = i; k < j; k++)
                {
                    String s = styleddocument.getText(k, 1);
                    if(ComparatorUtils.equals(s, "{"))
                        return -1;
                    if(ComparatorUtils.equals(s, "}"))
                        return k + 1;
                }

                return -1;
            }

            private void popUpFormulaEditPane(final String formulaContent, final int formulaStart, final AttributeSet attrs)
            {
                final UIFormula formulaPane = FormulaFactory.createFormulaPane();
                formulaPane.populate(new Formula(formulaContent));
                formulaPane.showLargeWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {

                    final UIFormula val$formulaPane;
                    final int val$formulaStart;
                    final String val$formulaContent;
                    final AttributeSet val$attrs;
                    final _cls1 this$1;

                    public void doOk()
                    {
                        StyledDocument styleddocument = (StyledDocument)getDocument();
                        Formula formula = formulaPane.update();
                        String s = RichTextConverter.asFormula(formula.getContent());
                        try
                        {
                            styleddocument.remove(formulaStart, formulaContent.length() + 3);
                            styleddocument.insertString(formulaStart, s, attrs);
                        }
                        catch(BadLocationException badlocationexception)
                        {
                            FRContext.getLogger().error(badlocationexception.getMessage());
                        }
                    }

                    
                    {
                        this$1 = _cls1.this;
                        formulaPane = uiformula;
                        formulaStart = i;
                        formulaContent = s;
                        attrs = attributeset;
                        super();
                    }
                }
).setVisible(true);
            }

            public void mouseReleased(MouseEvent mouseevent)
            {
                int i;
                StyledDocument styleddocument;
                if(mouseevent.getClickCount() != 2)
                    break MISSING_BLOCK_LABEL_137;
                i = getSelectionStart();
                if(i <= 0)
                    return;
                styleddocument = (StyledDocument)getDocument();
                int j;
                j = findFormulaStart(i, styleddocument);
                if(j == -1)
                    return;
                int k;
                k = findFormulaEnd(i, styleddocument);
                if(k == -1)
                    return;
                try
                {
                    select(j, k);
                    Element element = styleddocument.getCharacterElement(j);
                    AttributeSet attributeset = element.getAttributes();
                    String s = styleddocument.getText(j + 2, k - j - 3);
                    popUpFormulaEditPane(s, j, attributeset);
                }
                catch(BadLocationException badlocationexception)
                {
                    FRContext.getLogger().error(badlocationexception.getMessage());
                }
            }

            
            {
                this$0 = RichTextEditingPane.this;
                super();
            }
        }
;
        addMouseListener(doubleClickFormulaListener);
    }

    public boolean isUpdating()
    {
        return updating;
    }

    public void setUpdating(boolean flag)
    {
        updating = flag;
    }

    public void startUpdating()
    {
        updating = true;
    }

    public void finishUpdating()
    {
        updating = false;
    }
}
