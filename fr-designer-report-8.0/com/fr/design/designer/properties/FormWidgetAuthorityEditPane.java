// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.properties;

import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.*;
import com.fr.design.mainframe.toolbar.AuthorityEditToolBarComponent;
import com.fr.design.roleAuthority.*;
import com.fr.form.ui.Widget;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.*;

public class FormWidgetAuthorityEditPane extends AuthorityEditPane
{

    private FormDesigner designer;
    private Widget widgets[];
    private UICheckBox widgetVisible;
    private UICheckBox widgetAvailable;
    private ItemListener visibleItemListener;
    private ItemListener usableItemListener;

    public FormWidgetAuthorityEditPane(FormDesigner formdesigner)
    {
        super(formdesigner);
        widgets = null;
        widgetVisible = new UICheckBox(Inter.getLocText("FR-Designer_Visible"));
        widgetAvailable = new UICheckBox(Inter.getLocText("FR-Designer_Enabled"));
        visibleItemListener = new ItemListener() {

            final FormWidgetAuthorityEditPane this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                String s = ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName();
                if(s == null)
                    return;
                if(widgets != null && widgets.length > 0)
                {
                    for(int i = 0; i < widgets.length; i++)
                        widgets[i].changeVisibleAuthorityState(s, widgetVisible.isSelected());

                }
                doAfterAuthority();
            }

            
            {
                this$0 = FormWidgetAuthorityEditPane.this;
                super();
            }
        }
;
        usableItemListener = new ItemListener() {

            final FormWidgetAuthorityEditPane this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                String s = ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName();
                if(ComparatorUtils.equals(s, Inter.getLocText("FR-Engine_Role")))
                    return;
                if(s == null)
                    return;
                if(widgets != null && widgets.length > 0)
                {
                    for(int i = 0; i < widgets.length; i++)
                        widgets[i].changeUsableAuthorityState(s, widgetAvailable.isSelected());

                }
                doAfterAuthority();
            }

            
            {
                this$0 = FormWidgetAuthorityEditPane.this;
                super();
            }
        }
;
        designer = formdesigner;
        widgetAvailable.addItemListener(usableItemListener);
        widgetVisible.addItemListener(visibleItemListener);
    }

    private void doAfterAuthority()
    {
        designer.repaint();
        HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().fireTargetModified();
        RolesAlreadyEditedPane.getInstance().refreshDockingView();
        RolesAlreadyEditedPane.getInstance().setReportAndFSSelectedRoles();
        RolesAlreadyEditedPane.getInstance().repaint();
        checkCheckBoxes();
    }

    public void populateType()
    {
        type.setText(Inter.getLocText("Widget-Form_Widget_Config"));
    }

    public void populateName()
    {
        String s = "";
        if(widgets == null || widgets.length <= 0)
            return;
        for(int i = 0; i < widgets.length; i++)
            s = (new StringBuilder()).append(s).append(",").append(widgets[i].getClass().getSimpleName()).toString();

        name.setText(s.substring(1));
    }

    public JPanel populateCheckPane()
    {
        checkPane.add(populateWidgetCheckPane(), "West");
        return checkPane;
    }

    private JPanel populateWidgetCheckPane()
    {
        double d = -1D;
        double d1 = -2D;
        Component acomponent[][] = {
            {
                new UILabel(Inter.getLocText("FR-Designer_Widget"), 2), widgetVisible, widgetAvailable
            }
        };
        double ad[] = {
            d1
        };
        double ad1[] = {
            d1, d1, d
        };
        int ai[][] = {
            {
                1, 1, 1
            }
        };
        return TableLayoutHelper.createGapTableLayoutPane(acomponent, ad, ad1, ai, 6D, 6D);
    }

    public void populateDetials()
    {
        HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().setAuthorityMode(true);
        signelSelection();
        refreshCreator();
        populateType();
        populateName();
        checkPane.removeAll();
        populateCheckPane();
        checkPane.setBorder(BorderFactory.createEmptyBorder(-3, 0, 0, 0));
        checkCheckBoxes();
    }

    private void checkCheckBoxes()
    {
        String s = ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName();
        widgetVisible.removeItemListener(visibleItemListener);
        widgetAvailable.removeItemListener(usableItemListener);
        populateWidgetButton(s);
        widgetVisible.addItemListener(visibleItemListener);
        widgetAvailable.addItemListener(usableItemListener);
    }

    private void populateWidgetButton(String s)
    {
        if(widgets == null || widgets.length == 0)
            return;
        Widget widget = widgets[0];
        if(widget.isVisible())
            widgetVisible.setSelected(!widget.isDoneVisibleAuthority(s));
        else
            widgetVisible.setSelected(widget.isVisibleAuthority(s));
        if(widget.isEnabled())
            widgetAvailable.setSelected(!widget.isDoneUsableAuthority(s));
        else
            widgetAvailable.setSelected(widget.isUsableAuthority(s));
    }

    private void signelSelection()
    {
        if(HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().isJWorkBook())
        {
            JComponent jcomponent = DesignerContext.getDesignerFrame().getToolbarComponent();
            if(jcomponent instanceof AuthorityEditToolBarComponent)
                ((AuthorityEditToolBarComponent)jcomponent).removeSelection();
            HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().removeTemplateSelection();
        }
    }

    private void refreshCreator()
    {
        int i = designer.getSelectionModel().getSelection().size();
        widgets = i != 0 ? designer.getSelectionModel().getSelection().getSelectedWidgets() : null;
    }




}
