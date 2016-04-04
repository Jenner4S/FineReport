// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe;

import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.event.UIObserverListener;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.toolbar.AuthorityEditToolBarComponent;
import com.fr.design.roleAuthority.*;
import com.fr.form.ui.Widget;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.grid.selection.*;
import com.fr.js.NameJavaScript;
import com.fr.js.NameJavaScriptGroup;
import com.fr.privilege.finegrain.CellPrivilegeControl;
import com.fr.privilege.finegrain.ColumnRowPrivilegeControl;
import com.fr.report.cell.*;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.report.worksheet.WorkSheet;
import com.fr.stable.ColumnRow;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import javax.swing.*;

// Referenced classes of package com.fr.design.mainframe:
//            AuthorityEditPane, JTemplate, ElementCasePane, DesignerContext, 
//            DesignerFrame

public class ElementCasePaneAuthorityEditPane extends AuthorityEditPane
{

    private static final int WIDGET_VISIBLE = 0;
    private static final int WIDGET_USABLE = 1;
    private static final int CELL = 2;
    private static final int HYPER_LINK = 3;
    private static final int FLOAT_SELECTION = 3;
    private static final int NEW_VALUE = 4;
    private static final int NEW_PANE_WIDTH = 120;
    private UICheckBox floatElementVisibleCheckBoxes;
    private UICheckBox cellElementVisibleCheckBoxes;
    private UICheckBox widgetVisible;
    private UICheckBox widgetAvailable;
    private UICheckBox gridColumnRowVisible;
    private UICheckBox newValue;
    private ValueEditorPane valueEditor;
    private UICheckBox hyperlinkCheckBoxes[];
    private ElementCasePane elementCasePane;
    private int selectionType;
    private CellSelection cellSelection;
    private FloatSelection floatSelection;
    private boolean isAllHasWidget;
    private boolean isAllHasHyperlink;
    private UIObserverListener observerListener;
    private ItemListener newValuelistener;
    private ItemListener columnRowAuthorityListener;
    private ItemListener floatElementAuthorityListener;
    private ItemListener cellRolesAuthorityListener;
    private ItemListener widgetVisibleRoleAuthorityListener;
    private ItemListener widgetUsableAuthorityListener;

    public ElementCasePaneAuthorityEditPane(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
        floatElementVisibleCheckBoxes = new UICheckBox(Inter.getLocText("FR-Designer_Visible"));
        cellElementVisibleCheckBoxes = new UICheckBox(Inter.getLocText("FR-Designer_Visible"));
        widgetVisible = new UICheckBox(Inter.getLocText("FR-Designer_Visible"));
        widgetAvailable = new UICheckBox(Inter.getLocText("FR-Designer_Enabled"));
        gridColumnRowVisible = new UICheckBox(Inter.getLocText("FR-Designer_Hide"));
        newValue = new UICheckBox(Inter.getLocText("FR-Designer_New_Value"));
        valueEditor = ValueEditorPaneFactory.createBasicValueEditorPane(120);
        hyperlinkCheckBoxes = null;
        elementCasePane = null;
        selectionType = 0;
        observerListener = new UIObserverListener() {

            final ElementCasePaneAuthorityEditPane this$0;

            public void doChange()
            {
                if(elementCasePane == null || cellSelection == null)
                    return;
                if(setAuthorityStyle(4))
                    elementCasePane.fireTargetModified();
            }

            
            {
                this$0 = ElementCasePaneAuthorityEditPane.this;
                super();
            }
        }
;
        newValuelistener = new ItemListener() {

            final ElementCasePaneAuthorityEditPane this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                if(elementCasePane == null || cellSelection == null)
                    return;
                if(setAuthorityStyle(4))
                {
                    valueEditor.setEnabled(newValue.isSelected());
                    doAfterAuthority();
                }
            }

            
            {
                this$0 = ElementCasePaneAuthorityEditPane.this;
                super();
            }
        }
;
        columnRowAuthorityListener = new ItemListener() {

            final ElementCasePaneAuthorityEditPane this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                if(elementCasePane == null)
                    return;
                boolean flag = false;
                if(selectionType == 1)
                    flag = setAuthorityColumn();
                else
                    flag = setAuthorityRow();
                if(flag)
                    doAfterAuthority();
            }

            
            {
                this$0 = ElementCasePaneAuthorityEditPane.this;
                super();
            }
        }
;
        floatElementAuthorityListener = new ItemListener() {

            final ElementCasePaneAuthorityEditPane this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                if(elementCasePane == null || floatSelection == null)
                    return;
                if(setLFloatAuthorityStyle())
                    doAfterAuthority();
            }

            
            {
                this$0 = ElementCasePaneAuthorityEditPane.this;
                super();
            }
        }
;
        cellRolesAuthorityListener = new ItemListener() {

            final ElementCasePaneAuthorityEditPane this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                if(elementCasePane == null || cellSelection == null)
                    return;
                if(setAuthorityStyle(2))
                    doAfterAuthority();
            }

            
            {
                this$0 = ElementCasePaneAuthorityEditPane.this;
                super();
            }
        }
;
        widgetVisibleRoleAuthorityListener = new ItemListener() {

            final ElementCasePaneAuthorityEditPane this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                if(elementCasePane == null || cellSelection == null)
                    return;
                if(setAuthorityStyle(0))
                    doAfterAuthority();
            }

            
            {
                this$0 = ElementCasePaneAuthorityEditPane.this;
                super();
            }
        }
;
        widgetUsableAuthorityListener = new ItemListener() {

            final ElementCasePaneAuthorityEditPane this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                if(elementCasePane == null || cellSelection == null)
                    return;
                if(setAuthorityStyle(1))
                    doAfterAuthority();
            }

            
            {
                this$0 = ElementCasePaneAuthorityEditPane.this;
                super();
            }
        }
;
        elementCasePane = elementcasepane;
        initCheckBoxesState();
        initListener();
    }

    private void doAfterAuthority()
    {
        elementCasePane.repaint();
        elementCasePane.fireTargetModified();
        RolesAlreadyEditedPane.getInstance().refreshDockingView();
        RolesAlreadyEditedPane.getInstance().setReportAndFSSelectedRoles();
        RolesAlreadyEditedPane.getInstance().repaint();
        checkCheckBoxes();
    }

    private boolean setAuthorityColumn()
    {
        String s = ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName();
        if(ComparatorUtils.equals(s, Inter.getLocText("FR-Designer_Role")))
            return false;
        if(s == null)
            return false;
        TemplateElementCase templateelementcase = elementCasePane.getEditingElementCase();
        boolean flag = !gridColumnRowVisible.isSelected();
        if(!flag)
        {
            for(int i = cellSelection.getColumn(); i < cellSelection.getColumn() + cellSelection.getColumnSpan(); i++)
                templateelementcase.addColumnPrivilegeControl(i, s);

        } else
        {
            for(int j = cellSelection.getColumn(); j < cellSelection.getColumn() + cellSelection.getColumnSpan(); j++)
                templateelementcase.removeColumnPrivilegeControl(j, s);

        }
        return true;
    }

    private boolean setAuthorityRow()
    {
        String s = ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName();
        if(ComparatorUtils.equals(s, Inter.getLocText("FR-Designer_Role")))
            return false;
        if(s == null)
            return false;
        TemplateElementCase templateelementcase = elementCasePane.getEditingElementCase();
        boolean flag = !gridColumnRowVisible.isSelected();
        if(!flag)
        {
            for(int i = cellSelection.getRow(); i < cellSelection.getRow() + cellSelection.getRowSpan(); i++)
                templateelementcase.addRowPrivilegeControl(i, s);

        } else
        {
            for(int j = cellSelection.getRow(); j < cellSelection.getRow() + cellSelection.getRowSpan(); j++)
                templateelementcase.removeRowPrivilegeControl(j, s);

        }
        return true;
    }

    private boolean setLFloatAuthorityStyle()
    {
        String s = ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName();
        if(ComparatorUtils.equals(s, Inter.getLocText("FR-Designer_Role")))
            return false;
        if(s == null)
        {
            return false;
        } else
        {
            String s1 = floatSelection.getSelectedFloatName();
            TemplateElementCase templateelementcase = elementCasePane.getEditingElementCase();
            FloatElement floatelement = templateelementcase.getFloatElement(s1);
            floatelement.changeAuthorityState(s, floatElementVisibleCheckBoxes.isSelected());
            return true;
        }
    }

    private boolean setAuthorityStyle(int i)
    {
        String s = ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName();
        if(ComparatorUtils.equals(s, Inter.getLocText("FR-Designer_Role")))
            return false;
        if(s == null)
            return false;
        TemplateElementCase templateelementcase = elementCasePane.getEditingElementCase();
        int j = cellSelection.getCellRectangleCount();
        for(int k = 0; k < j; k++)
        {
            Rectangle rectangle = cellSelection.getCellRectangle(k);
            for(int l = rectangle.height - 1; l >= 0; l--)
            {
                for(int i1 = rectangle.width - 1; i1 >= 0; i1--)
                {
                    int j1 = i1 + rectangle.x;
                    int k1 = l + rectangle.y;
                    Object obj = templateelementcase.getTemplateCellElement(j1, k1);
                    if(obj == null)
                    {
                        obj = new DefaultTemplateCellElement(j1, k1);
                        templateelementcase.addCellElement(((TemplateCellElement) (obj)));
                    } else
                    if(((TemplateCellElement) (obj)).getColumn() != j1 || ((TemplateCellElement) (obj)).getRow() != k1)
                        continue;
                    if(i == 2)
                    {
                        ((TemplateCellElement) (obj)).changeAuthorityState(s, cellElementVisibleCheckBoxes.isSelected());
                        continue;
                    }
                    if(i == 4)
                    {
                        ((TemplateCellElement) (obj)).changeNewValueAuthorityState(s, newValue.isSelected(), valueEditor.update());
                        continue;
                    }
                    if(i == 0)
                    {
                        Widget widget = ((TemplateCellElement) (obj)).getWidget();
                        widget.changeVisibleAuthorityState(s, widgetVisible.isSelected());
                    } else
                    {
                        Widget widget1 = ((TemplateCellElement) (obj)).getWidget();
                        widget1.changeUsableAuthorityState(s, widgetAvailable.isSelected());
                    }
                }

            }

        }

        return true;
    }

    public void initCheckBoxesState()
    {
        TemplateElementCase templateelementcase = elementCasePane.getEditingElementCase();
        if(cellSelection == null)
        {
            cellElementVisibleCheckBoxes.setSelected(true);
        } else
        {
            Rectangle rectangle = cellSelection.getCellRectangle(0);
            DefaultTemplateCellElement defaulttemplatecellelement = (DefaultTemplateCellElement)templateelementcase.getCellElement(rectangle.x, rectangle.y);
            if(defaulttemplatecellelement == null)
                defaulttemplatecellelement = new DefaultTemplateCellElement(rectangle.x, rectangle.y);
            boolean flag = defaulttemplatecellelement.isDoneAuthority(ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName());
            cellElementVisibleCheckBoxes.setSelected(!flag);
        }
        widgetAvailable.setEnabled(cellElementVisibleCheckBoxes.isSelected());
        widgetVisible.setEnabled(cellElementVisibleCheckBoxes.isSelected());
    }

    private void initListener()
    {
        cellElementVisibleCheckBoxes.addItemListener(cellRolesAuthorityListener);
        widgetVisible.addItemListener(widgetVisibleRoleAuthorityListener);
        widgetAvailable.addItemListener(widgetUsableAuthorityListener);
        floatElementVisibleCheckBoxes.addItemListener(floatElementAuthorityListener);
        gridColumnRowVisible.addItemListener(columnRowAuthorityListener);
        newValue.addItemListener(newValuelistener);
        valueEditor.registerChangeListener(observerListener);
    }

    private void removeListener()
    {
        cellElementVisibleCheckBoxes.removeItemListener(cellRolesAuthorityListener);
        widgetVisible.removeItemListener(widgetVisibleRoleAuthorityListener);
        widgetAvailable.removeItemListener(widgetUsableAuthorityListener);
        floatElementVisibleCheckBoxes.removeItemListener(floatElementAuthorityListener);
        gridColumnRowVisible.removeItemListener(columnRowAuthorityListener);
        newValue.removeItemListener(newValuelistener);
        valueEditor.registerChangeListener(null);
    }

    private void addHyperlinkListener()
    {
        cellElementVisibleCheckBoxes.addItemListener(new ItemListener() {

            final ElementCasePaneAuthorityEditPane this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                if(hyperlinkCheckBoxes != null)
                {
                    for(int j = 0; j < hyperlinkCheckBoxes.length; j++)
                    {
                        hyperlinkCheckBoxes[j].setEnabled(cellElementVisibleCheckBoxes.isSelected());
                        if(!cellElementVisibleCheckBoxes.isSelected())
                            hyperlinkCheckBoxes[j].setSelected(false);
                    }

                }
            }

            
            {
                this$0 = ElementCasePaneAuthorityEditPane.this;
                super();
            }
        }
);
        for(int i = 0; i < hyperlinkCheckBoxes.length; i++)
            hyperlinkCheckBoxes[i].addItemListener(cellRolesAuthorityListener);

    }

    public void populateType()
    {
        if(selectionType == 0)
            type.setText(Inter.getLocText("FR-Designer_Cell"));
        else
        if(selectionType == 2)
            type.setText(Inter.getLocText("FR-Designer_Row"));
        else
        if(selectionType == 1)
            type.setText(Inter.getLocText("FR-Designer_Column"));
        else
            type.setText(Inter.getLocText("M_Insert-Float"));
    }

    public void populateName()
    {
        if(selectionType == 0)
            name.setText(getCellSelectionName());
        else
        if(selectionType == 2 || selectionType == 1)
            name.setText(getCellColumnRowName());
        else
            name.setText(getFloatSelectionName());
    }

    private String getCellSelectionName()
    {
        String s = "";
        int i = cellSelection.getCellRectangleCount();
        for(int j = 0; j < i; j++)
        {
            s = (new StringBuilder()).append(s).append(",").toString();
            Rectangle rectangle = cellSelection.getCellRectangle(j);
            ColumnRow columnrow = ColumnRow.valueOf(rectangle.x, rectangle.y);
            s = (new StringBuilder()).append(s).append(columnrow.toString()).toString();
            if(rectangle.width * rectangle.height != 1)
            {
                ColumnRow columnrow1 = ColumnRow.valueOf((rectangle.width + rectangle.x) - 1, (rectangle.height + rectangle.y) - 1);
                s = (new StringBuilder()).append(s).append(":").append(columnrow1.toString()).toString();
            }
        }

        return s.substring(1);
    }

    private String getCellColumnRowName()
    {
        int i = cellSelection.getCellRectangleCount();
        String s = "";
        ColumnRow columnrow = ColumnRow.valueOf(cellSelection.getColumn(), cellSelection.getRow());
        if(cellSelection.getSelectedType() == 1 && i == 1)
        {
            if(cellSelection.getColumnSpan() == 1)
            {
                s = columnrow.toString().substring(0, 1);
            } else
            {
                ColumnRow columnrow1 = ColumnRow.valueOf((cellSelection.getColumn() + cellSelection.getColumnSpan()) - 1, (cellSelection.getRow() + cellSelection.getRowSpan()) - 1);
                s = (new StringBuilder()).append(columnrow.toString().substring(0, 1)).append("-").append(columnrow1.toString().substring(0, 1)).toString();
            }
        } else
        if(cellSelection.getSelectedType() == 2 && i == 1)
            if(cellSelection.getRowSpan() == 1)
            {
                s = columnrow.toString().substring(1);
            } else
            {
                ColumnRow columnrow2 = ColumnRow.valueOf((cellSelection.getColumn() + cellSelection.getColumnSpan()) - 1, (cellSelection.getRow() + cellSelection.getRowSpan()) - 1);
                s = (new StringBuilder()).append(columnrow.toString().substring(1)).append("-").append(columnrow2.toString().substring(1)).toString();
            }
        return s;
    }

    private String getFloatSelectionName()
    {
        return floatSelection.getSelectedFloatName();
    }

    private void mutilRect(CellSelection cellselection)
    {
        isAllHasWidget = true;
        isAllHasHyperlink = true;
        int i = cellselection.getCellRectangleCount();
        TemplateElementCase templateelementcase = elementCasePane.getEditingElementCase();
        for(int j = 0; j < i; j++)
        {
            Rectangle rectangle = cellselection.getCellRectangle(j);
            for(int k = 0; k < rectangle.height; k++)
            {
                for(int l = 0; l < rectangle.width; l++)
                {
                    int i1 = l + rectangle.x;
                    int j1 = k + rectangle.y;
                    DefaultTemplateCellElement defaulttemplatecellelement = (DefaultTemplateCellElement)templateelementcase.getCellElement(i1, j1);
                    if(defaulttemplatecellelement == null)
                        defaulttemplatecellelement = new DefaultTemplateCellElement(cellselection.getColumn(), cellselection.getRow());
                    if(defaulttemplatecellelement.getCellWidgetAttr() == null)
                        isAllHasWidget = false;
                    if(defaulttemplatecellelement.getNameHyperlinkGroup() == null)
                        isAllHasHyperlink = false;
                }

            }

        }

    }

    public JPanel populateCheckPane()
    {
        checkPane.removeAll();
        if(selectionType == 0)
            populateCellSelectionCheckPane(checkPane);
        else
        if(selectionType == 1 || selectionType == 2)
            populateColumnRowCheckPane(checkPane);
        else
        if(selectionType == 3)
            populateFloatSelectionCheckPane(checkPane);
        checkPane.setBorder(BorderFactory.createEmptyBorder(-3, 0, 0, 0));
        return checkPane;
    }

    private void populateColumnRowCheckPane(JPanel jpanel)
    {
        double d = -1D;
        double d1 = -2D;
        Component acomponent[][] = {
            {
                gridColumnRowVisible
            }
        };
        double ad[] = {
            d1
        };
        double ad1[] = {
            d
        };
        int ai[][] = {
            {
                1
            }
        };
        jpanel.add(TableLayoutHelper.createGapTableLayoutPane(acomponent, ad, ad1, ai, 6D, 6D), "West");
    }

    private void populateFloatSelectionCheckPane(JPanel jpanel)
    {
        jpanel.add(populateFloatElementCheckPane(), "West");
    }

    private void populateCellSelectionCheckPane(JPanel jpanel)
    {
        if(elementCasePane.isSelectedOneCell())
        {
            TemplateElementCase templateelementcase = elementCasePane.getEditingElementCase();
            DefaultTemplateCellElement defaulttemplatecellelement = (DefaultTemplateCellElement)templateelementcase.getCellElement(cellSelection.getColumn(), cellSelection.getRow());
            if(defaulttemplatecellelement == null)
                defaulttemplatecellelement = new DefaultTemplateCellElement(cellSelection.getColumn(), cellSelection.getRow());
            if(defaulttemplatecellelement.getCellWidgetAttr() != null)
                jpanel.add(populateWidgetCheckPane(), "West");
            else
                jpanel.add(populatCellCheckPane(), "West");
        } else
        {
            mutilRect(cellSelection);
            if(!isAllHasWidget && !isAllHasHyperlink)
                jpanel.add(populateMutilCellCheckPane(), "West");
            else
            if(isAllHasWidget)
                jpanel.add(populateMutilWidgetCheckPane(), "West");
        }
    }

    public void populateDetials()
    {
        HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().setAuthorityMode(false);
        TemplateElementCase templateelementcase = elementCasePane.getEditingElementCase();
        if(templateelementcase instanceof WorkSheet)
            ((WorkSheet)templateelementcase).setPaintSelection(true);
        signelSelection();
        Selection selection = elementCasePane.getSelection();
        if(selection instanceof CellSelection)
        {
            selectionType = 0;
            cellSelection = ((CellSelection)selection).clone();
            floatSelection = null;
            judgeChooseCR();
        } else
        if(selection instanceof FloatSelection)
        {
            selectionType = 3;
            cellSelection = null;
            floatSelection = new FloatSelection(((FloatSelection)selection).getSelectedFloatName());
        }
        populateType();
        populateName();
        populateCheckPane();
        checkCheckBoxes();
    }

    private void judgeChooseCR()
    {
        if(cellSelection.getSelectedType() == 1 && cellSelection.getCellRectangleCount() == 1)
            selectionType = 1;
        if(cellSelection.getSelectedType() == 2 && cellSelection.getCellRectangleCount() == 1)
            selectionType = 2;
    }

    private void signelSelection()
    {
        JTemplate jtemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
        if(jtemplate.isJWorkBook())
        {
            JComponent jcomponent = DesignerContext.getDesignerFrame().getToolbarComponent();
            if(jcomponent instanceof AuthorityEditToolBarComponent)
                ((AuthorityEditToolBarComponent)jcomponent).removeSelection();
            jtemplate.removeParameterPaneSelection();
        }
    }

    private JPanel populateFloatElementCheckPane()
    {
        double d = -1D;
        double d1 = -2D;
        Component acomponent[][] = {
            {
                new UILabel(Inter.getLocText("M_Insert-Float"), 2), floatElementVisibleCheckBoxes
            }
        };
        double ad[] = {
            d1
        };
        double ad1[] = {
            d1, d
        };
        int ai[][] = {
            {
                1, 1
            }
        };
        return TableLayoutHelper.createGapTableLayoutPane(acomponent, ad, ad1, ai, 6D, 6D);
    }

    private JPanel populateWidgetCheckPane()
    {
        double d = -1D;
        double d1 = -2D;
        Component acomponent[][] = {
            {
                new UILabel(Inter.getLocText("FR-Designer_Cell"), 2), cellElementVisibleCheckBoxes
            }, {
                null, newValue
            }, {
                null, valueEditor
            }, {
                new UILabel(Inter.getLocText("FR-Designer_Widget"), 2), widgetVisible
            }, {
                null, widgetAvailable
            }
        };
        double ad[] = {
            d1, d1, d1, d1, d1
        };
        double ad1[] = {
            d1, d
        };
        int ai[][] = {
            {
                1, 1
            }, {
                1, 1
            }, {
                1, 1
            }, {
                1, 1
            }, {
                1, 1
            }
        };
        return TableLayoutHelper.createGapTableLayoutPane(acomponent, ad, ad1, ai, 6D, 6D);
    }

    private JPanel populateMutilWidgetCheckPane()
    {
        double d = -1D;
        double d1 = -2D;
        Component acomponent[][] = {
            {
                new UILabel(Inter.getLocText("FR-Designer_Cell"), 2), cellElementVisibleCheckBoxes
            }, {
                new UILabel(Inter.getLocText("FR-Designer_Widget"), 2), widgetVisible
            }, {
                null, widgetAvailable
            }
        };
        double ad[] = {
            d1, d1, d1
        };
        double ad1[] = {
            d1, d
        };
        int ai[][] = {
            {
                1, 1
            }, {
                1, 1
            }, {
                1, 1
            }
        };
        return TableLayoutHelper.createGapTableLayoutPane(acomponent, ad, ad1, ai, 6D, 6D);
    }

    private JPanel populateMutilCellCheckPane()
    {
        double d = -1D;
        double d1 = -2D;
        Component acomponent[][] = {
            {
                cellElementVisibleCheckBoxes
            }
        };
        double ad[] = {
            d1
        };
        double ad1[] = {
            d
        };
        int ai[][] = {
            {
                1
            }
        };
        return TableLayoutHelper.createGapTableLayoutPane(acomponent, ad, ad1, ai, 6D, 6D);
    }

    private JPanel populatCellCheckPane()
    {
        double d = -1D;
        double d1 = -2D;
        Component acomponent[][] = {
            {
                cellElementVisibleCheckBoxes
            }, {
                newValue
            }, {
                valueEditor
            }
        };
        double ad[] = {
            d1, d1, d1
        };
        double ad1[] = {
            d
        };
        int ai[][] = {
            {
                1
            }, {
                1
            }, {
                1
            }
        };
        return TableLayoutHelper.createGapTableLayoutPane(acomponent, ad, ad1, ai, 6D, 6D);
    }

    public TemplateCellElement getFirstCell()
    {
        TemplateElementCase templateelementcase = elementCasePane.getEditingElementCase();
        Rectangle rectangle = cellSelection.getCellRectangle(0);
        TemplateCellElement templatecellelement = null;
        for(int i = rectangle.height - 1; i >= 0; i--)
        {
            for(int j = rectangle.width - 1; j >= 0; j--)
            {
                int k = j + rectangle.x;
                int l = i + rectangle.y;
                TemplateCellElement templatecellelement1 = templateelementcase.getTemplateCellElement(k, l);
                if(templatecellelement1 != null && templatecellelement1.getColumn() == k && templatecellelement1.getRow() == l)
                    templatecellelement = templatecellelement1;
            }

        }

        return templatecellelement;
    }

    private void checkCheckBoxes()
    {
        String s = ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName();
        removeListener();
        if(selectionType == 0)
            checkCellSelectionCkeckboxes(s);
        else
        if(selectionType == 1 || selectionType == 2)
            checkColumnRowCheckBoxes(s);
        else
        if(selectionType == 3)
            checkFloatSelectionCkeckboxes(s);
        initListener();
    }

    private void checkCellSelectionCkeckboxes(String s)
    {
        TemplateCellElement templatecellelement = getFirstCell();
        if(templatecellelement == null)
        {
            resetCellElementCheckBoxes();
            return;
        }
        cellElementVisibleCheckBoxes.setSelected(!templatecellelement.isDoneAuthority(s));
        newValue.setEnabled(!templatecellelement.isDoneAuthority(s));
        if(!templatecellelement.isDoneAuthority(s))
        {
            newValue.setSelected(templatecellelement.isDoneNewValueAuthority(s));
            if(newValue.isSelected())
            {
                valueEditor.setEnabled(true);
                valueEditor.populate(templatecellelement.getCellPrivilegeControl().getNewValueMap().get(s));
            } else
            {
                valueEditor.setEnabled(false);
            }
        } else
        {
            newValue.setSelected(false);
            valueEditor.setEnabled(false);
        }
        populateWidgetButton(templatecellelement.getWidget(), s, templatecellelement);
    }

    private void populateWidgetButton(Widget widget, String s, TemplateCellElement templatecellelement)
    {
        if(widget == null)
            return;
        if(widget.isVisible())
        {
            widgetVisible.setSelected(!widget.isDoneVisibleAuthority(s));
            widgetVisible.setEnabled(!templatecellelement.isDoneAuthority(s));
        } else
        {
            widgetVisible.setSelected(widget.isVisibleAuthority(s));
        }
        if(widget.isEnabled())
        {
            widgetAvailable.setSelected(!widget.isDoneUsableAuthority(s));
            widgetAvailable.setEnabled(!widget.isDoneVisibleAuthority(s));
        } else
        {
            widgetAvailable.setSelected(widget.isUsableAuthority(s));
        }
    }

    private void resetCellElementCheckBoxes()
    {
        cellElementVisibleCheckBoxes.setSelected(true);
        widgetVisible.setSelected(true);
        widgetVisible.setEnabled(true);
        widgetAvailable.setSelected(true);
        widgetAvailable.setEnabled(true);
        newValue.setSelected(false);
        valueEditor.setEnabled(false);
    }

    private void checkColumnRowCheckBoxes(String s)
    {
        if(cellSelection == null)
        {
            gridColumnRowVisible.setSelected(false);
            return;
        } else
        {
            TemplateElementCase templateelementcase = elementCasePane.getEditingElementCase();
            boolean flag = selectionType != 1 ? templateelementcase.getRowPrivilegeControl(cellSelection.getRow()).checkInvisible(s) : templateelementcase.getColumnPrivilegeControl(cellSelection.getColumn()).checkInvisible(s);
            gridColumnRowVisible.setSelected(flag);
            return;
        }
    }

    private void checkFloatSelectionCkeckboxes(String s)
    {
        String s1 = floatSelection.getSelectedFloatName();
        TemplateElementCase templateelementcase = elementCasePane.getEditingElementCase();
        FloatElement floatelement = templateelementcase.getFloatElement(s1);
        floatElementVisibleCheckBoxes.setSelected(!floatelement.isDoneAuthority(s));
    }

    private JPanel populateHyperlinkCheckPane(AbstractCellElement abstractcellelement)
    {
        NameJavaScriptGroup namejavascriptgroup = abstractcellelement.getNameHyperlinkGroup();
        hyperlinkCheckBoxes = new UICheckBox[namejavascriptgroup.size()];
        double d = -1D;
        double d1 = -2D;
        Component acomponent[][] = new Component[hyperlinkCheckBoxes.length + 1][];
        if(namejavascriptgroup.size() == 1)
        {
            acomponent[0] = (new Component[] {
                new UILabel(Inter.getLocText("FR-Designer_Cell"), 2), cellElementVisibleCheckBoxes
            });
            acomponent[1] = (new Component[] {
                new UILabel(Inter.getLocText("FR-Designer_Hyperlink"), 2), hyperlinkCheckBoxes[0] = new UICheckBox(Inter.getLocText("FR-Designer_Visible"))
            });
        } else
        {
            acomponent[0] = (new Component[] {
                new UILabel(Inter.getLocText("FR-Designer_Cell"), 2), cellElementVisibleCheckBoxes = new UICheckBox(Inter.getLocText("FR-Designer_Visible"))
            });
            acomponent[1] = (new Component[] {
                new UILabel(Inter.getLocText("FR-Designer_Hyperlink"), 2), hyperlinkCheckBoxes[0] = new UICheckBox((new StringBuilder()).append(namejavascriptgroup.getNameHyperlink(0).getName()).append(Inter.getLocText("FR-Designer_Visible")).toString())
            });
            for(int i = 1; i < hyperlinkCheckBoxes.length; i++)
                acomponent[i + 1] = (new Component[] {
                    null, hyperlinkCheckBoxes[i] = new UICheckBox((new StringBuilder()).append(namejavascriptgroup.getNameHyperlink(i).getName()).append(Inter.getLocText("FR-Designer_Visible")).toString())
                });

        }
        for(int j = 0; j < hyperlinkCheckBoxes.length; j++)
            hyperlinkCheckBoxes[j].setEnabled(cellElementVisibleCheckBoxes.isSelected());

        addHyperlinkListener();
        double ad[] = new double[hyperlinkCheckBoxes.length + 1];
        int ai[][] = new int[hyperlinkCheckBoxes.length + 1][];
        for(int k = 0; k < hyperlinkCheckBoxes.length + 1; k++)
        {
            ad[k] = d1;
            ai[k] = (new int[] {
                1, 1
            });
        }

        double ad1[] = {
            d1, d
        };
        return TableLayoutHelper.createGapTableLayoutPane(acomponent, ad, ad1, ai, 6D, 6D);
    }

    private JPanel populateMutilHyperlinkCheckPane()
    {
        hyperlinkCheckBoxes = new UICheckBox[1];
        double d = -1D;
        double d1 = -2D;
        Component acomponent[][] = new Component[2][];
        acomponent[0] = (new Component[] {
            new UILabel(Inter.getLocText("FR-Designer_Cell"), 2), cellElementVisibleCheckBoxes
        });
        acomponent[1] = (new Component[] {
            new UILabel(Inter.getLocText("FR-Designer_Hyperlink"), 2), hyperlinkCheckBoxes[0] = new UICheckBox(Inter.getLocText("FR-Designer_Visible"))
        });
        hyperlinkCheckBoxes[0].setEnabled(cellElementVisibleCheckBoxes.isSelected());
        addHyperlinkListener();
        double ad[] = {
            d1, d1
        };
        double ad1[] = {
            d1, d
        };
        int ai[][] = {
            {
                1, 1
            }, {
                1, 1
            }
        };
        return TableLayoutHelper.createGapTableLayoutPane(acomponent, ad, ad1, ai, 6D, 6D);
    }













}
