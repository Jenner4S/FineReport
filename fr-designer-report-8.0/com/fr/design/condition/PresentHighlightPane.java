// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.condition;

import com.fr.base.present.Present;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.present.PresentPane;
import com.fr.general.Inter;
import com.fr.report.cell.cellattr.NormalPresent;
import com.fr.report.cell.cellattr.PresentConstants;
import com.fr.report.cell.cellattr.highlight.HighlightAction;
import com.fr.report.cell.cellattr.highlight.PresentHighlightAction;
import java.awt.event.*;
import javax.swing.SwingUtilities;

// Referenced classes of package com.fr.design.condition:
//            ConditionAttrSingleConditionPane, ConditionAttributesPane

public class PresentHighlightPane extends ConditionAttrSingleConditionPane
{

    private UIComboBox presentComboBox;
    private Present present;
    private UIButton editButton;
    private ValueEditorPane valueEditor;

    public PresentHighlightPane(final ConditionAttributesPane conditionAttributesPane)
    {
        super(conditionAttributesPane);
        add(new UILabel((new StringBuilder()).append(Inter.getLocText("FR-Designer_Present")).append(":").toString()));
        String as[] = {
            PresentConstants.NORMAL, Inter.getLocText(new String[] {
                "Other", "Present"
            })
        };
        presentComboBox = new UIComboBox(as);
        add(presentComboBox);
        editButton = new UIButton(Inter.getLocText("FR-Designer_Edit"));
        editButton.addActionListener(new ActionListener() {

            final ConditionAttributesPane val$conditionAttributesPane;
            final PresentHighlightPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                if(presentComboBox.getSelectedIndex() != 0)
                {
                    final PresentPane presentPane = new PresentPane();
                    presentPane.populateBean(present);
                    BasicDialog basicdialog = presentPane.showWindow(SwingUtilities.getWindowAncestor(conditionAttributesPane));
                    basicdialog.addDialogActionListener(new DialogActionAdapter() {

                        final PresentPane val$presentPane;
                        final _cls1 this$1;

                        public void doOk()
                        {
                            present = (Present)presentPane.updateBean();
                        }

                    
                    {
                        this$1 = _cls1.this;
                        presentPane = presentpane;
                        super();
                    }
                    }
);
                    basicdialog.setVisible(true);
                }
            }

            
            {
                this$0 = PresentHighlightPane.this;
                conditionAttributesPane = conditionattributespane;
                super();
            }
        }
);
        valueEditor = ValueEditorPaneFactory.createBasicValueEditorPane();
        add(valueEditor);
        presentComboBox.addItemListener(new ItemListener() {

            final PresentHighlightPane this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                if(presentComboBox.getSelectedIndex() == 1)
                {
                    if(valueEditor.getParent() == PresentHighlightPane.this)
                        remove(valueEditor);
                    add(editButton);
                    validate();
                    repaint();
                } else
                {
                    if(editButton.getParent() == PresentHighlightPane.this)
                        remove(editButton);
                    add(valueEditor);
                    validate();
                    repaint();
                }
            }

            
            {
                this$0 = PresentHighlightPane.this;
                super();
            }
        }
);
    }

    public String nameForPopupMenuItem()
    {
        return Inter.getLocText("FR-Designer_Present");
    }

    protected String title4PopupWindow()
    {
        return nameForPopupMenuItem();
    }

    public void populate(HighlightAction highlightaction)
    {
        present = ((PresentHighlightAction)highlightaction).getPresent();
        if(!(present instanceof NormalPresent))
        {
            presentComboBox.setSelectedIndex(1);
        } else
        {
            presentComboBox.setSelectedIndex(0);
            valueEditor.populate(((NormalPresent)present).getNormalPresent());
        }
    }

    public HighlightAction update()
    {
        if(presentComboBox.getSelectedIndex() == 0 && (valueEditor.update() instanceof String))
            present = new NormalPresent(valueEditor.update());
        return new PresentHighlightAction(present);
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
