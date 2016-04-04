// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.condition;

import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.style.FRFontPane;
import com.fr.design.style.FRFontPreviewArea;
import com.fr.general.FRFont;
import com.fr.general.Inter;
import com.fr.report.cell.cellattr.highlight.FRFontHighlightAction;
import com.fr.report.cell.cellattr.highlight.HighlightAction;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.SwingUtilities;

// Referenced classes of package com.fr.design.condition:
//            ConditionAttrSingleConditionPane, ConditionAttributesPane

public class FontPane extends ConditionAttrSingleConditionPane
{

    private UILabel fontLabel;
    private FRFontPreviewArea frFontPreviewPane;
    private UIComboBox fontScopeComboBox;

    public FontPane(final ConditionAttributesPane conditionAttributesPane)
    {
        super(conditionAttributesPane);
        fontLabel = new UILabel((new StringBuilder()).append(Inter.getLocText("FR-Designer_Sytle-FRFont")).append(":").toString());
        frFontPreviewPane = new FRFontPreviewArea();
        frFontPreviewPane.setBorder(BorderFactory.createTitledBorder(""));
        frFontPreviewPane.setPreferredSize(new Dimension(80, 20));
        UIButton uibutton = new UIButton(Inter.getLocText("FR-Designer_Edit"));
        uibutton.addActionListener(new ActionListener() {

            final ConditionAttributesPane val$conditionAttributesPane;
            final FontPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                final FRFontPane frFontPane = new FRFontPane();
                BasicDialog basicdialog = frFontPane.showWindow(SwingUtilities.getWindowAncestor(conditionAttributesPane));
                frFontPane.populate(frFontPreviewPane.getFontObject());
                basicdialog.addDialogActionListener(new DialogActionAdapter() {

                    final FRFontPane val$frFontPane;
                    final _cls1 this$1;

                    public void doOk()
                    {
                        frFontPreviewPane.setFontObject(frFontPane.update());
                        frFontPreviewPane.repaint();
                    }

                    
                    {
                        this$1 = _cls1.this;
                        frFontPane = frfontpane;
                        super();
                    }
                }
);
                basicdialog.setVisible(true);
            }

            
            {
                this$0 = FontPane.this;
                conditionAttributesPane = conditionattributespane;
                super();
            }
        }
);
        fontScopeComboBox = new UIComboBox(new String[] {
            Inter.getLocText("Utils-Current_Cell"), Inter.getLocText("Utils-Current_Row"), Inter.getLocText("Utils-Current_Column")
        });
        add(fontLabel);
        add(frFontPreviewPane);
        add(uibutton);
        add(fontScopeComboBox);
        frFontPreviewPane.setFontObject(FRFont.getInstance());
    }

    public String nameForPopupMenuItem()
    {
        return Inter.getLocText("FR-Designer_Sytle-FRFont");
    }

    public void populate(HighlightAction highlightaction)
    {
        frFontPreviewPane.setFontObject(((FRFontHighlightAction)highlightaction).getFRFont());
        fontScopeComboBox.setSelectedIndex(((FRFontHighlightAction)highlightaction).getScope());
    }

    public HighlightAction update()
    {
        return new FRFontHighlightAction(frFontPreviewPane.getFontObject(), fontScopeComboBox.getSelectedIndex());
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
