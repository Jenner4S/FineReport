// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.condition;

import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.widget.WidgetPane;
import com.fr.form.ui.*;
import com.fr.general.Inter;
import com.fr.report.cell.cellattr.highlight.HighlightAction;
import com.fr.report.cell.cellattr.highlight.WidgetHighlightAction;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;

// Referenced classes of package com.fr.design.condition:
//            ConditionAttrSingleConditionPane, ConditionAttributesPane

public class WidgetHighlightPane extends ConditionAttrSingleConditionPane
{

    private static final int DIALOG_WIDTH = 700;
    private static final int DIALOG_HEIGHT = 400;
    private Widget widget;
    private UIComboBox box;
    private UICheckBox useWidget;
    private UIButton widgetButton;

    public WidgetHighlightPane(final ConditionAttributesPane conditionAttributesPane)
    {
        super(conditionAttributesPane);
        widgetButton = new UIButton(Inter.getLocText("FR-Designer_Edit"));
        widgetButton.addActionListener(new ActionListener() {

            final ConditionAttributesPane val$conditionAttributesPane;
            final WidgetHighlightPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                final WidgetPane widgetPane = new WidgetPane();
                widgetPane.populate(widget);
                BasicDialog basicdialog = widgetPane.showWindow(SwingUtilities.getWindowAncestor(conditionAttributesPane));
                basicdialog.addDialogActionListener(new DialogActionAdapter() {

                    final WidgetPane val$widgetPane;
                    final _cls1 this$1;

                    public void doOk()
                    {
                        widget = widgetPane.update();
                        setComboBox();
                    }

                    
                    {
                        this$1 = _cls1.this;
                        widgetPane = widgetpane;
                        super();
                    }
                }
);
                basicdialog.setVisible(true);
            }

            
            {
                this$0 = WidgetHighlightPane.this;
                conditionAttributesPane = conditionattributespane;
                super();
            }
        }
);
        UILabel uilabel = new UILabel((new StringBuilder()).append(Inter.getLocText("FR-Designer_Widget")).append(":").toString());
        add(uilabel);
        String as[] = {
            "", Inter.getLocText("FR-Designer_Text"), Inter.getLocText("FR-Designer_Form-TextArea"), Inter.getLocText("FR-Designer_Number"), Inter.getLocText("FR-Designer_Form-Password"), Inter.getLocText("FR-Designer_Form-Button"), Inter.getLocText("FR-Designer_Form-CheckBox"), Inter.getLocText("FR-Designer_Form-RadioGroup"), Inter.getLocText("FR-Designer_Form-CheckBoxGroup"), Inter.getLocText("FR-Designer_ComboBox"), 
            Inter.getLocText("FR-Designer_Form-ComboCheckBox"), Inter.getLocText("FR-Designer_Date"), Inter.getLocText("FR-Designer_File"), Inter.getLocText("FR-Designer_Form-List"), Inter.getLocText("FR-Designer_Form-Iframe"), Inter.getLocText("FR-Designer_Tree-ComboBox"), Inter.getLocText("Form-View_Tree")
        };
        box = new UIComboBox(as);
        add(box);
        box.setEnabled(false);
        add(widgetButton);
        widgetButton.setEnabled(false);
        useWidget = new UICheckBox(Inter.getLocText(new String[] {
            "Use", "Widget"
        }));
        add(useWidget);
        useWidget.addActionListener(new ActionListener() {

            final WidgetHighlightPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                if(!useWidget.isSelected())
                {
                    box.setSelectedIndex(0);
                    widgetButton.setEnabled(false);
                } else
                {
                    setComboBox();
                    widgetButton.setEnabled(true);
                }
            }

            
            {
                this$0 = WidgetHighlightPane.this;
                super();
            }
        }
);
    }

    public String nameForPopupMenuItem()
    {
        return Inter.getLocText("FR-Designer_Widget");
    }

    protected String title4PopupWindow()
    {
        return nameForPopupMenuItem();
    }

    public void setComboBox()
    {
        Widget widget1 = widget;
        if(widget1 instanceof ComboCheckBox)
            box.setSelectedItem(Inter.getLocText("FR-Designer_Form-ComboCheckBox"));
        else
        if(widget1 instanceof ComboBox)
            box.setSelectedItem(Inter.getLocText("FR-Designer_ComboBox"));
        else
        if(widget1 instanceof NumberEditor)
            box.setSelectedItem(Inter.getLocText("FR-Designer_Number"));
        else
        if(widget1 instanceof IframeEditor)
            box.setSelectedItem(Inter.getLocText("FR-Designer_Form-Iframe"));
        else
        if(widget1 instanceof FreeButton)
            box.setSelectedItem(Inter.getLocText("FR-Designer_Form-Button"));
        else
        if(widget1 instanceof DateEditor)
            box.setSelectedItem(Inter.getLocText("FR-Designer_Date"));
        else
        if(widget1 instanceof CheckBox)
            box.setSelectedItem(Inter.getLocText("FR-Designer_Form-CheckBox"));
        else
        if(widget1 instanceof RadioGroup)
            box.setSelectedItem(Inter.getLocText("FR-Designer_Form-RadioGroup"));
        else
        if(widget1 instanceof CheckBoxGroup)
            box.setSelectedItem(Inter.getLocText("FR-Designer_Form-CheckBoxGroup"));
        else
        if(widget1 instanceof MultiFileEditor)
            box.setSelectedItem(Inter.getLocText("FR-Designer_File"));
        else
        if(widget1 instanceof ListEditor)
            box.setSelectedItem(Inter.getLocText("FR-Designer_Form-List"));
        else
        if(widget1 instanceof TreeComboBoxEditor)
            box.setSelectedItem(Inter.getLocText("FR-Designer_Tree-ComboBox"));
        else
        if(widget1 instanceof TreeEditor)
            box.setSelectedItem(Inter.getLocText("Form-View_Tree"));
        else
        if(widget1 instanceof Password)
            box.setSelectedItem(Inter.getLocText("FR-Designer_Form-Password"));
        else
        if(widget1 instanceof TextArea)
            box.setSelectedItem(Inter.getLocText("FR-Designer_Form-TextArea"));
        else
        if(widget1 instanceof TextEditor)
            box.setSelectedItem(Inter.getLocText("FR-Designer_Text"));
    }

    public void populate(HighlightAction highlightaction)
    {
        widget = ((WidgetHighlightAction)highlightaction).getWidget();
        if(widget == null)
        {
            useWidget.setSelected(false);
            box.setSelectedIndex(0);
            widgetButton.setEnabled(false);
        } else
        {
            useWidget.setSelected(true);
            setComboBox();
            widgetButton.setEnabled(true);
        }
    }

    public HighlightAction update()
    {
        if(useWidget.isSelected())
            return new WidgetHighlightAction(widget);
        else
            return new WidgetHighlightAction();
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
