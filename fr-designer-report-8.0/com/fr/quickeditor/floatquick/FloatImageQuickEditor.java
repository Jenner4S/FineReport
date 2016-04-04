// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.quickeditor.floatquick;

import com.fr.base.BaseUtils;
import com.fr.base.Style;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.report.SelectImagePane;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.quickeditor.FloatQuickEditor;
import com.fr.report.cell.FloatElement;
import com.fr.report.cell.cellattr.CellImage;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;

public class FloatImageQuickEditor extends FloatQuickEditor
{

    public FloatImageQuickEditor()
    {
        UIButton uibutton = new UIButton(Inter.getLocText("Edit"), BaseUtils.readIcon("/com/fr/design/images/m_insert/image.png"));
        uibutton.addActionListener(new ActionListener() {

            final FloatImageQuickEditor this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                showEditingDialog();
            }

            
            {
                this$0 = FloatImageQuickEditor.this;
                super();
            }
        }
);
        uibutton.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        uibutton.setMargin(null);
        uibutton.setOpaque(false);
        Component acomponent[][] = {
            {
                uibutton
            }
        };
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d1
        };
        double ad1[] = {
            d
        };
        javax.swing.JPanel jpanel = TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(jpanel, "Center");
    }

    private void showEditingDialog()
    {
        final SelectImagePane imageEditorPane = new SelectImagePane();
        imageEditorPane.populate(floatElement);
        final Object oldValue = floatElement.getValue();
        final Style oldStyle = floatElement.getStyle();
        imageEditorPane.showWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {

            final SelectImagePane val$imageEditorPane;
            final Object val$oldValue;
            final Style val$oldStyle;
            final FloatImageQuickEditor this$0;

            public void doOk()
            {
                CellImage cellimage = imageEditorPane.update();
                if(!ComparatorUtils.equals(cellimage.getImage(), oldValue) || !ComparatorUtils.equals(cellimage.getStyle(), oldStyle))
                {
                    FloatImageQuickEditor.this.imageEditorPane.setValue(cellimage.getImage());
                    FloatImageQuickEditor.this.imageEditorPane.setStyle(cellimage.getStyle());
                    fireTargetModified();
                }
            }

            
            {
                this$0 = FloatImageQuickEditor.this;
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
