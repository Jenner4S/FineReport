// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.condition;

import com.fr.base.background.ColorBackground;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.style.background.BackgroundPane;
import com.fr.design.style.background.BackgroundPreviewLabel;
import com.fr.general.Inter;
import com.fr.report.cell.cellattr.highlight.BackgroundHighlightAction;
import com.fr.report.cell.cellattr.highlight.HighlightAction;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;

// Referenced classes of package com.fr.design.condition:
//            ConditionAttrSingleConditionPane, ConditionAttributesPane

public class BackPane extends ConditionAttrSingleConditionPane
{

    private UILabel backgroundLabel;
    private BackgroundPreviewLabel backgroundPreviewPane;
    private UIComboBox backScopeComboBox;

    public BackPane(final ConditionAttributesPane conditionAttributesPane)
    {
        super(conditionAttributesPane);
        backgroundLabel = new UILabel((new StringBuilder()).append(Inter.getLocText("FR-Designer_Background")).append(":").toString());
        backgroundPreviewPane = new BackgroundPreviewLabel();
        backgroundPreviewPane.setPreferredSize(new Dimension(80, 20));
        UIButton uibutton = new UIButton(Inter.getLocText("FR-Designer_Edit"));
        uibutton.addActionListener(new ActionListener() {

            final ConditionAttributesPane val$conditionAttributesPane;
            final BackPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                final BackgroundPane backgroundPane = new BackgroundPane();
                backgroundPane.populate(backgroundPreviewPane.getBackgroundObject());
                backgroundPane.showWindow(SwingUtilities.getWindowAncestor(conditionAttributesPane), new DialogActionAdapter() {

                    final BackgroundPane val$backgroundPane;
                    final _cls1 this$1;

                    public void doOk()
                    {
                        backgroundPreviewPane.setBackgroundObject(backgroundPane.update());
                        backgroundPreviewPane.repaint();
                    }

                    
                    {
                        this$1 = _cls1.this;
                        backgroundPane = backgroundpane;
                        super();
                    }
                }
).setVisible(true);
            }

            
            {
                this$0 = BackPane.this;
                conditionAttributesPane = conditionattributespane;
                super();
            }
        }
);
        backScopeComboBox = new UIComboBox(new String[] {
            Inter.getLocText("Utils-Current_Cell"), Inter.getLocText("Utils-Current_Row"), Inter.getLocText("Utils-Current_Column")
        });
        add(backgroundLabel);
        add(backgroundPreviewPane);
        add(uibutton);
        add(backScopeComboBox);
        backgroundPreviewPane.setBackgroundObject(ColorBackground.getInstance(Color.white));
    }

    public String nameForPopupMenuItem()
    {
        return Inter.getLocText("FR-Designer_Background");
    }

    public void populate(HighlightAction highlightaction)
    {
        backgroundPreviewPane.setBackgroundObject(((BackgroundHighlightAction)highlightaction).getBackground());
        backScopeComboBox.setSelectedIndex(((BackgroundHighlightAction)highlightaction).getScope());
    }

    public HighlightAction update()
    {
        return new BackgroundHighlightAction(backgroundPreviewPane.getBackgroundObject(), backScopeComboBox.getSelectedIndex());
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
