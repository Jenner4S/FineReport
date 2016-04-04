// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.quickeditor.cellquick;

import com.fr.base.BaseUtils;
import com.fr.design.cell.editor.BiasTextPainterCellEditor;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.mainframe.DesignerContext;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.quickeditor.CellQuickEditor;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.cell.painter.BiasTextPainter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JComponent;

public class CellBiasTextPainterEditor extends CellQuickEditor
{

    public CellBiasTextPainterEditor()
    {
    }

    public JComponent createCenterBody()
    {
        UIButton uibutton = new UIButton(Inter.getLocText("Edit"), BaseUtils.readIcon("/com/fr/design/images/m_insert/bias.png"));
        uibutton.addActionListener(new ActionListener() {

            final CellBiasTextPainterEditor this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                showEditingDialog();
            }

            
            {
                this$0 = CellBiasTextPainterEditor.this;
                super();
            }
        }
);
        uibutton.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        uibutton.setMargin(null);
        uibutton.setOpaque(false);
        return uibutton;
    }

    private void showEditingDialog()
    {
        final BiasTextPainter oldbiasTextPainter = (BiasTextPainter)cellElement.getValue();
        final com.fr.design.cell.editor.BiasTextPainterCellEditor.BiasTextPainterPane biasTextPainterPane = new com.fr.design.cell.editor.BiasTextPainterCellEditor.BiasTextPainterPane();
        biasTextPainterPane.populate(oldbiasTextPainter);
        biasTextPainterPane.showSmallWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {

            final com.fr.design.cell.editor.BiasTextPainterCellEditor.BiasTextPainterPane val$biasTextPainterPane;
            final BiasTextPainter val$oldbiasTextPainter;
            final CellBiasTextPainterEditor this$0;

            public void doOk()
            {
                BiasTextPainter biastextpainter = biasTextPainterPane.update();
                if(!ComparatorUtils.equals(oldbiasTextPainter, biastextpainter))
                {
                    CellBiasTextPainterEditor.this.biasTextPainterPane.setValue(biastextpainter);
                    fireTargetModified();
                }
            }

            
            {
                this$0 = CellBiasTextPainterEditor.this;
                biasTextPainterPane = biastextpainterpane;
                oldbiasTextPainter = biastextpainter;
                super();
            }
        }
).setVisible(true);
    }

    protected void refreshDetails()
    {
    }



}
