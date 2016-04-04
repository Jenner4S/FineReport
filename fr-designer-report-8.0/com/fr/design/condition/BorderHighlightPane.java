// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.condition;

import com.fr.base.CellBorderStyle;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.style.BorderPane;
import com.fr.general.Inter;
import com.fr.report.cell.cellattr.highlight.BorderHighlightAction;
import com.fr.report.cell.cellattr.highlight.HighlightAction;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;

// Referenced classes of package com.fr.design.condition:
//            ConditionAttrSingleConditionPane, ConditionAttributesPane

public class BorderHighlightPane extends ConditionAttrSingleConditionPane
{

    private CellBorderStyle border;
    private UIButton borderButton;

    public BorderHighlightPane(final ConditionAttributesPane conditionAttributesPane)
    {
        super(conditionAttributesPane);
        borderButton = new UIButton(Inter.getLocText("FR-Designer_Edit"));
        borderButton.setPreferredSize(new Dimension(53, 23));
        borderButton.addActionListener(new ActionListener() {

            final ConditionAttributesPane val$conditionAttributesPane;
            final BorderHighlightPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                final BorderPane borderPane = new BorderPane();
                int i = border != null ? border.getTopStyle() : 0;
                Color color = border != null ? border.getTopColor() : Color.black;
                borderPane.populate(border, false, i, color);
                BasicDialog basicdialog = borderPane.showWindow(SwingUtilities.getWindowAncestor(conditionAttributesPane));
                basicdialog.addDialogActionListener(new DialogActionAdapter() {

                    final BorderPane val$borderPane;
                    final _cls1 this$1;

                    public void doOk()
                    {
                        border = borderPane.update();
                        borderButton.setBorderStyle(border);
                        borderButton.repaint();
                    }

                    
                    {
                        this$1 = _cls1.this;
                        borderPane = borderpane;
                        super();
                    }
                }
);
                basicdialog.setVisible(true);
            }

            
            {
                this$0 = BorderHighlightPane.this;
                conditionAttributesPane = conditionattributespane;
                super();
            }
        }
);
        UILabel uilabel = new UILabel((new StringBuilder()).append(Inter.getLocText("FR-Designer_Border")).append(":").toString());
        add(uilabel);
        add(borderButton);
    }

    public String nameForPopupMenuItem()
    {
        return Inter.getLocText("FR-Designer_Border");
    }

    public void populate(HighlightAction highlightaction)
    {
        border = ((BorderHighlightAction)highlightaction).getCellBorder();
        borderButton.setBorderStyle(border);
        borderButton.repaint();
    }

    public HighlightAction update()
    {
        return new BorderHighlightAction(border);
    }

    public volatile Object update()
    {
        return update();
    }

    public volatile void populate(Object obj)
    {
        populate((HighlightAction)obj);
    }



}
