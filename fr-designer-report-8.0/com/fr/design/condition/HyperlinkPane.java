// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.condition;

import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.frpane.HyperlinkGroupPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.mainframe.*;
import com.fr.general.Inter;
import com.fr.js.*;
import com.fr.report.cell.cellattr.highlight.HighlightAction;
import com.fr.report.cell.cellattr.highlight.HyperlinkHighlightAction;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;

// Referenced classes of package com.fr.design.condition:
//            ConditionAttrSingleConditionPane, ConditionAttributesPane

public class HyperlinkPane extends ConditionAttrSingleConditionPane
{

    private NameJavaScriptGroup jsGroup;
    protected UITextField typeField;
    protected UICheckBox useHyperlink;
    protected UIButton hyperlinkButton;
    protected HyperlinkGroupPane pane;
    protected BasicDialog dialog;
    ActionListener l;

    public HyperlinkPane(final ConditionAttributesPane conditionAttributesPane)
    {
        super(conditionAttributesPane);
        l = new ActionListener() {

            final HyperlinkPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                if(useHyperlink.isSelected())
                {
                    hyperlinkButton.setEnabled(true);
                    setText();
                } else
                {
                    hyperlinkButton.setEnabled(false);
                    typeField.setText("");
                }
            }

            
            {
                this$0 = HyperlinkPane.this;
                super();
            }
        }
;
        hyperlinkButton = new UIButton(Inter.getLocText("FR-Designer_Edit"));
        hyperlinkButton.addActionListener(new ActionListener() {

            final ConditionAttributesPane val$conditionAttributesPane;
            final HyperlinkPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                NameJavaScriptGroup namejavascriptgroup = jsGroup;
                pane = DesignerContext.getDesignerFrame().getSelectedJTemplate().getHyperLinkPane();
                pane.populate(namejavascriptgroup);
                dialog = pane.showWindow(SwingUtilities.getWindowAncestor(conditionAttributesPane));
                dialog.addDialogActionListener(new DialogActionAdapter() {

                    final _cls1 this$1;

                    public void doOk()
                    {
                        jsGroup = pane.updateJSGroup();
                        setText();
                    }

                    
                    {
                        this$1 = _cls1.this;
                        super();
                    }
                }
);
                dialog.setVisible(true);
            }

            
            {
                this$0 = HyperlinkPane.this;
                conditionAttributesPane = conditionattributespane;
                super();
            }
        }
);
        hyperlinkButton.setEnabled(false);
        UILabel uilabel = new UILabel((new StringBuilder()).append(Inter.getLocText(new String[] {
            "Hyperlink", "Type"
        })).append(":").toString());
        typeField = new UITextField(12);
        typeField.setEditable(false);
        add(uilabel);
        add(typeField);
        add(hyperlinkButton);
        useHyperlink = new UICheckBox(Inter.getLocText(new String[] {
            "Use", "Links"
        }));
        useHyperlink.addActionListener(l);
        add(useHyperlink);
    }

    public String nameForPopupMenuItem()
    {
        return Inter.getLocText("FR-Designer_Hyperlink");
    }

    protected String title4PopupWindow()
    {
        return nameForPopupMenuItem();
    }

    public void setText()
    {
        if(jsGroup == null)
            typeField.setText("");
        else
        if(jsGroup.size() > 1)
            typeField.setText((new StringBuilder()).append(Inter.getLocText("FR-Designer_HyperLink_Must_Alone_Reset")).append("!").toString());
        else
        if(jsGroup.size() == 1)
        {
            JavaScript javascript = jsGroup.getNameHyperlink(0).getJavaScript();
            if(javascript instanceof JavaScriptImpl)
                typeField.setText("JavaScript");
            else
            if(javascript instanceof ReportletHyperlink)
                typeField.setText(Inter.getLocText(new String[] {
                    "Report", "Links"
                }));
            else
            if(javascript instanceof WebHyperlink)
                typeField.setText(Inter.getLocText("Hyperlink-Web_link"));
            else
            if(javascript instanceof EmailJavaScript)
                typeField.setText(Inter.getLocText(new String[] {
                    "Email", "Links"
                }));
        }
    }

    public void populate(HighlightAction highlightaction)
    {
        jsGroup = ((HyperlinkHighlightAction)highlightaction).getHperlink();
        if(jsGroup == null || jsGroup.size() == 0)
        {
            useHyperlink.setSelected(false);
            hyperlinkButton.setEnabled(false);
        } else
        {
            useHyperlink.setSelected(true);
            hyperlinkButton.setEnabled(true);
            setText();
        }
    }

    public HighlightAction update()
    {
        if(useHyperlink.isSelected())
            return new HyperlinkHighlightAction(jsGroup);
        else
            return new HyperlinkHighlightAction();
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
