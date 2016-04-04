// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.quickeditor.cellquick;

import com.fr.base.BaseUtils;
import com.fr.base.Style;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.report.SelectImagePane;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.quickeditor.CellQuickEditor;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.cell.cellattr.CellImage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JComponent;

public class CellImageQuickEditor extends CellQuickEditor
{

    private static CellImageQuickEditor THIS;

    public static final CellImageQuickEditor getInstance()
    {
        if(THIS == null)
            THIS = new CellImageQuickEditor();
        return THIS;
    }

    private CellImageQuickEditor()
    {
    }

    public JComponent createCenterBody()
    {
        UIButton uibutton = new UIButton(Inter.getLocText("Edit"), BaseUtils.readIcon("/com/fr/design/images/m_insert/image.png"));
        uibutton.addActionListener(new ActionListener() {

            final CellImageQuickEditor this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                showEditingDialog();
            }

            
            {
                this$0 = CellImageQuickEditor.this;
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
        final SelectImagePane imageEditorPane = new SelectImagePane();
        imageEditorPane.populate(cellElement);
        final Object oldValue = cellElement.getValue();
        final Style oldStyle = cellElement.getStyle();
        imageEditorPane.showWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {

            final SelectImagePane val$imageEditorPane;
            final Object val$oldValue;
            final Style val$oldStyle;
            final CellImageQuickEditor this$0;

            public void doOk()
            {
                CellImage cellimage = imageEditorPane.update();
                if(!ComparatorUtils.equals(cellimage.getImage(), oldValue) || !ComparatorUtils.equals(cellimage.getStyle(), oldStyle))
                {
                    CellImageQuickEditor.this.imageEditorPane.setValue(cellimage.getImage());
                    CellImageQuickEditor.this.imageEditorPane.setStyle(cellimage.getStyle());
                    fireTargetModified();
                }
            }

            
            {
                this$0 = CellImageQuickEditor.this;
                imageEditorPane = selectimagepane;
                oldValue = obj;
                oldStyle = style;
                super();
            }
        }
).setVisible(true);
    }

    protected void refreshDetails()
    {
    }




}
