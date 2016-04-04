// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.cell.settingpane;

import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.mainframe.JTemplate;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.grid.selection.CellSelection;
import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.cell.cellattr.*;
import com.fr.report.elementcase.TemplateElementCase;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

// Referenced classes of package com.fr.design.mainframe.cell.settingpane:
//            AbstractCellAttrPane

public class CellOtherSetPane extends AbstractCellAttrPane
{

    private UIButtonGroup autoshrik;
    private UICheckBox previewCellContent;
    private UICheckBox printAndExportContent;
    private UICheckBox printAndExportBackground;
    private UIComboBox showContent;
    private UITextField tooltipTextField;
    private UITextField fileNameTextField;
    private UICheckBox pageBeforeRowCheckBox;
    private UICheckBox pageAfterRowCheckBox;
    private UICheckBox pageBeforeColumnCheckBox;
    private UICheckBox pageAfterColumnCheckBox;
    private UICheckBox canBreakOnPaginateCheckBox;
    private UICheckBox repeatCheckBox;
    private UIButtonGroup insertRowPolicy;
    private ValueEditorPane valueEditor;
    private JPanel southContentPane;
    private JPanel defaultValuePane;

    public CellOtherSetPane()
    {
    }

    public JPanel createContentPane()
    {
        JPanel jpanel = createNormal();
        createOthers();
        double d = -1D;
        double d1 = -2D;
        double ad[] = {
            d1, d1, d1, d1, d1, d1, d1
        };
        double ad1[] = {
            d1, d
        };
        UILabel uilabel = new UILabel((new StringBuilder()).append(Inter.getLocText("Auto_Adjust_Size")).append(":").toString(), 4);
        uilabel.setVerticalAlignment(1);
        Component acomponent[][] = {
            {
                uilabel, autoshrik
            }, {
                new UILabel((new StringBuilder()).append(Inter.getLocText("Preview")).append(":").toString(), 4), previewCellContent
            }, {
                new UILabel((new StringBuilder()).append(Inter.getLocText("CellWrite-Print_Export")).append(":").toString(), 4), printAndExportContent
            }, {
                null, printAndExportBackground
            }, {
                new UILabel((new StringBuilder()).append(Inter.getLocText("Show_Content")).append(":").toString(), 4), showContent
            }, {
                null, jpanel
            }, {
                new UILabel((new StringBuilder()).append(Inter.getLocText("CellWrite-ToolTip")).append(":").toString(), 4), tooltipTextField
            }
        };
        JPanel jpanel1 = TableLayoutHelper.createTableLayoutPane(acomponent, ad, ad1);
        double ad2[] = {
            d1, d1, d1, d1, d1, d1
        };
        double ad3[] = {
            d1, d
        };
        Component acomponent1[][] = {
            {
                new JSeparator(0), null
            }, {
                new UILabel(Inter.getLocText("Pagination")), null
            }, {
                pageBeforeRowCheckBox, pageAfterRowCheckBox
            }, {
                pageBeforeColumnCheckBox, pageAfterColumnCheckBox
            }, {
                canBreakOnPaginateCheckBox, null
            }, {
                repeatCheckBox, null
            }
        };
        JPanel jpanel2 = TableLayoutHelper.createTableLayoutPane(acomponent1, ad2, ad3);
        double ad4[] = {
            d1, d1, d1, d1
        };
        double ad5[] = {
            d
        };
        Component acomponent2[][] = {
            {
                new JSeparator(0)
            }, {
                new UILabel(Inter.getLocText("CellWrite-InsertRow_Policy"), 2)
            }, {
                insertRowPolicy
            }, {
                defaultValuePane
            }
        };
        southContentPane = TableLayoutHelper.createTableLayoutPane(acomponent2, ad4, ad5);
        JPanel jpanel3 = new JPanel(new BorderLayout(0, 10));
        jpanel3.add(jpanel1, "North");
        jpanel3.add(jpanel2, "Center");
        JPanel jpanel4 = new JPanel(new BorderLayout(0, 10));
        jpanel4.add(jpanel3, "North");
        jpanel4.add(southContentPane, "Center");
        initAllNames();
        return jpanel4;
    }

    private JPanel createNormal()
    {
        String as[] = {
            Inter.getLocText("No"), Inter.getLocText("Utils-Row_Height"), Inter.getLocText("Utils-Column_Width"), Inter.getLocText("Default")
        };
        autoshrik = new UIButtonGroup(as);
        autoshrik.setTwoLine();
        autoshrik.setLayout(new GridLayout(2, 2, 1, 1));
        previewCellContent = new UICheckBox(Inter.getLocText("CellWrite-Preview_Cell_Content"));
        printAndExportContent = new UICheckBox(Inter.getLocText("CellWrite-Print_Content"));
        printAndExportBackground = new UICheckBox(Inter.getLocText("CellWrite-Print_Background"));
        showContent = new UIComboBox(new String[] {
            Inter.getLocText("Default"), Inter.getLocText("CellWrite-Show_As_Image"), Inter.getLocText("CellWrite-Show_As_HTML"), Inter.getLocText("ShowAsDownload")
        });
        final CardLayout fileNameLayout = new CardLayout();
        final JPanel fileNamePane = new JPanel(fileNameLayout);
        JPanel jpanel = new JPanel(new BorderLayout(4, 0));
        jpanel.add(new UILabel(Inter.getLocText("FileNameForDownload")), "West");
        fileNameTextField = new UITextField();
        tooltipTextField = new UITextField();
        tooltipTextField.getUI();
        fileNamePane.add(new JPanel(), "none");
        fileNamePane.add(jpanel, "content");
        jpanel.add(fileNameTextField, "Center");
        showContent.addItemListener(new ItemListener() {

            final CardLayout val$fileNameLayout;
            final JPanel val$fileNamePane;
            final CellOtherSetPane this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                fileNameLayout.show(fileNamePane, showContent.getSelectedIndex() != 3 ? "none" : "content");
            }

            
            {
                this$0 = CellOtherSetPane.this;
                fileNameLayout = cardlayout;
                fileNamePane = jpanel;
                super();
            }
        }
);
        tooltipTextField = new UITextField();
        tooltipTextField.getUI();
        return fileNamePane;
    }

    private void createOthers()
    {
        pageBeforeRowCheckBox = new UICheckBox(Inter.getLocText("CellWrite-Page_Before_Row"));
        pageAfterRowCheckBox = new UICheckBox(Inter.getLocText("CellWrite-Page_After_Row"));
        pageBeforeColumnCheckBox = new UICheckBox(Inter.getLocText("CellWrite-Page_Before_Column"));
        pageAfterColumnCheckBox = new UICheckBox(Inter.getLocText("CellWrite-Page_After_Column"));
        canBreakOnPaginateCheckBox = new UICheckBox(Inter.getLocText("CellPage-Can_Break_On_Paginate"));
        repeatCheckBox = new UICheckBox(Inter.getLocText("CellWrite-Repeat_Content_When_Paging"));
        insertRowPolicy = new UIButtonGroup(new String[] {
            Inter.getLocText("CellWrite-InsertRow_NULL"), Inter.getLocText("CellWrite-InsertRow_DEFAULT"), Inter.getLocText("CellWrite-InsertRow_COPY")
        });
        defaultValuePane = new JPanel(new BorderLayout(4, 0));
        valueEditor = ValueEditorPaneFactory.createBasicValueEditorPane();
        defaultValuePane.add(valueEditor, "Center");
        defaultValuePane.setVisible(false);
        insertRowPolicy.addChangeListener(new ChangeListener() {

            final CellOtherSetPane this$0;

            public void stateChanged(ChangeEvent changeevent)
            {
                defaultValuePane.setVisible(insertRowPolicy.getSelectedIndex() == 1);
            }

            
            {
                this$0 = CellOtherSetPane.this;
                super();
            }
        }
);
    }

    private void initAllNames()
    {
        autoshrik.setGlobalName(Inter.getLocText("Auto_Adjust_Size"));
        previewCellContent.setGlobalName(Inter.getLocText("Preview"));
        printAndExportContent.setGlobalName(Inter.getLocText("CellWrite-Preview_Cell_Content"));
        printAndExportBackground.setGlobalName(Inter.getLocText("CellWrite-Print_Background"));
        showContent.setGlobalName(Inter.getLocText("Show_Content"));
        fileNameTextField.setGlobalName(Inter.getLocText("Show_Content"));
        tooltipTextField.setGlobalName(Inter.getLocText("CellWrite-ToolTip"));
        pageBeforeRowCheckBox.setGlobalName(Inter.getLocText("CellWrite-Page_Before_Row"));
        pageAfterRowCheckBox.setGlobalName(Inter.getLocText("CellWrite-Page_After_Row"));
        pageBeforeColumnCheckBox.setGlobalName(Inter.getLocText("CellWrite-Page_Before_Column"));
        pageAfterColumnCheckBox.setGlobalName(Inter.getLocText("CellWrite-Page_After_Column"));
        canBreakOnPaginateCheckBox.setGlobalName(Inter.getLocText("CellPage-Can_Break_On_Paginate"));
        repeatCheckBox.setGlobalName(Inter.getLocText("CellWrite-Repeat_Content_When_Paging"));
        insertRowPolicy.setGlobalName(Inter.getLocText("CellWrite-InsertRow_Policy"));
        valueEditor.setGlobalName(Inter.getLocText("CellWrite-InsertRow_Policy"));
    }

    public String getIconPath()
    {
        return "com/fr/design/images/m_format/cellstyle/otherset.png";
    }

    protected void populateBean()
    {
        CellGUIAttr cellguiattr = cellElement.getCellGUIAttr();
        if(cellguiattr == null)
            cellguiattr = CellGUIAttr.DEFAULT_CELLGUIATTR;
        autoshrik.setSelectedIndex(cellguiattr.getAdjustMode());
        previewCellContent.setSelected(cellguiattr.isPreviewContent());
        printAndExportContent.setSelected(cellguiattr.isPrintContent());
        printAndExportBackground.setSelected(cellguiattr.isPrintBackground());
        if(cellguiattr.isShowAsImage())
            showContent.setSelectedItem(Inter.getLocText("CellWrite-Show_As_Image"));
        else
        if(cellguiattr.isShowAsHTML())
            showContent.setSelectedItem(Inter.getLocText("CellWrite-Show_As_HTML"));
        else
        if(cellguiattr.isShowAsDownload())
        {
            showContent.setSelectedItem(Inter.getLocText("ShowAsDownload"));
            fileNameTextField.setText(cellguiattr.getFileName());
        } else
        {
            showContent.setSelectedItem(Inter.getLocText("Default"));
        }
        tooltipTextField.setText(cellguiattr.getTooltipText());
        CellPageAttr cellpageattr = cellElement.getCellPageAttr();
        if(cellpageattr == null)
            cellpageattr = new CellPageAttr();
        pageBeforeRowCheckBox.setSelected(cellpageattr.isPageBeforeRow());
        pageBeforeColumnCheckBox.setSelected(cellpageattr.isPageBeforeColumn());
        pageAfterRowCheckBox.setSelected(cellpageattr.isPageAfterRow());
        pageAfterColumnCheckBox.setSelected(cellpageattr.isPageAfterColumn());
        canBreakOnPaginateCheckBox.setSelected(cellpageattr.isCanBreakOnPaginate());
        repeatCheckBox.setSelected(cellpageattr.isRepeat());
        CellInsertPolicyAttr cellinsertpolicyattr = cellElement.getCellInsertPolicyAttr();
        if(cellinsertpolicyattr == null)
            cellinsertpolicyattr = new CellInsertPolicyAttr();
        if(ComparatorUtils.equals(CellInsertPolicyAttr.INSERT_POLICY_COPY, cellinsertpolicyattr.getInsertPolicy()))
            insertRowPolicy.setSelectedIndex(2);
        else
        if(ComparatorUtils.equals(CellInsertPolicyAttr.INSERT_POLICY_DEFAULT, cellinsertpolicyattr.getInsertPolicy()))
        {
            insertRowPolicy.setSelectedIndex(1);
            Object obj = cellinsertpolicyattr.getDefaultInsertValue();
            valueEditor.populate(obj);
        } else
        {
            insertRowPolicy.setSelectedIndex(0);
        }
        defaultValuePane.setVisible(insertRowPolicy.getSelectedIndex() == 1);
        southContentPane.setVisible(true);
        JTemplate jtemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
        if(!jtemplate.isJWorkBook())
            southContentPane.setVisible(false);
    }

    public void updateBean(TemplateCellElement templatecellelement)
    {
        String s = null;
        CellGUIAttr cellguiattr = null;
        CellGUIAttr cellguiattr1 = templatecellelement.getCellGUIAttr();
        if(cellguiattr1 == null)
            cellguiattr1 = new CellGUIAttr();
        if(ComparatorUtils.equals(getGlobalName(), Inter.getLocText("Auto_Adjust_Size")))
            cellguiattr1.setAdjustMode(autoshrik.getSelectedIndex());
        if(ComparatorUtils.equals(getGlobalName(), Inter.getLocText("Preview")))
            cellguiattr1.setPreviewContent(previewCellContent.isSelected());
        if(ComparatorUtils.equals(getGlobalName(), Inter.getLocText("CellWrite-Preview_Cell_Content")))
            cellguiattr1.setPrintContent(printAndExportContent.isSelected());
        if(ComparatorUtils.equals(getGlobalName(), Inter.getLocText("CellWrite-Print_Background")))
            cellguiattr1.setPrintBackground(printAndExportBackground.isSelected());
        if(ComparatorUtils.equals(getGlobalName(), Inter.getLocText("Show_Content")))
        {
            cellguiattr1.setShowAsDefault(showContent.getSelectedIndex() == 0);
            cellguiattr1.setShowAsImage(showContent.getSelectedIndex() == 1);
            cellguiattr1.setShowAsHTML(showContent.getSelectedIndex() == 2);
            cellguiattr1.setShowAsDownload(showContent.getSelectedIndex() == 3);
            if(fileNameTextField.getText() == null || fileNameTextField.getText().trim().length() <= 0)
                cellguiattr1.setFileName(s);
            else
            if(showContent.getSelectedIndex() == 3)
                cellguiattr1.setFileName(fileNameTextField.getText());
        }
        if(ComparatorUtils.equals(getGlobalName(), Inter.getLocText("CellWrite-ToolTip")))
            if(tooltipTextField.getText() == null || tooltipTextField.getText().trim().length() <= 0)
                cellguiattr1.setTooltipText(s);
            else
                cellguiattr1.setTooltipText(tooltipTextField.getText());
        if(ComparatorUtils.equals(cellguiattr1, CellGUIAttr.DEFAULT_CELLGUIATTR))
            templatecellelement.setCellGUIAttr(cellguiattr);
        else
            templatecellelement.setCellGUIAttr(cellguiattr1);
        updatePageAttr(templatecellelement);
    }

    private void updatePageAttr(TemplateCellElement templatecellelement)
    {
        CellPageAttr cellpageattr = templatecellelement.getCellPageAttr();
        if(cellpageattr == null)
            cellpageattr = new CellPageAttr();
        if(ComparatorUtils.equals(getGlobalName(), Inter.getLocText("CellWrite-Page_Before_Row")))
            cellpageattr.setPageBeforeRow(pageBeforeRowCheckBox.isSelected());
        if(ComparatorUtils.equals(getGlobalName(), Inter.getLocText("CellWrite-Page_After_Row")))
            cellpageattr.setPageAfterRow(pageAfterRowCheckBox.isSelected());
        if(ComparatorUtils.equals(getGlobalName(), Inter.getLocText("CellWrite-Page_Before_Column")))
            cellpageattr.setPageBeforeColumn(pageBeforeColumnCheckBox.isSelected());
        if(ComparatorUtils.equals(getGlobalName(), Inter.getLocText("CellWrite-Page_After_Column")))
            cellpageattr.setPageAfterColumn(pageAfterColumnCheckBox.isSelected());
        if(ComparatorUtils.equals(getGlobalName(), Inter.getLocText("CellPage-Can_Break_On_Paginate")))
            cellpageattr.setCanBreakOnPaginate(canBreakOnPaginateCheckBox.isSelected());
        if(ComparatorUtils.equals(getGlobalName(), Inter.getLocText("CellWrite-Repeat_Content_When_Paging")))
            cellpageattr.setRepeat(repeatCheckBox.isSelected());
        templatecellelement.setCellPageAttr(cellpageattr);
        if(ComparatorUtils.equals(getGlobalName(), Inter.getLocText("CellWrite-InsertRow_Policy")))
        {
            CellInsertPolicyAttr cellinsertpolicyattr = new CellInsertPolicyAttr();
            if(insertRowPolicy.getSelectedIndex() == 2)
                cellinsertpolicyattr.setInsertPolicy(CellInsertPolicyAttr.INSERT_POLICY_COPY);
            else
            if(insertRowPolicy.getSelectedIndex() == 1)
            {
                cellinsertpolicyattr.setInsertPolicy(CellInsertPolicyAttr.INSERT_POLICY_DEFAULT);
                Object obj = valueEditor.update();
                cellinsertpolicyattr.setDefaultInsertValue(obj);
            } else
            {
                cellinsertpolicyattr.setInsertPolicy(CellInsertPolicyAttr.INSERT_POLICY_NULL);
            }
            templatecellelement.setCellInsertPolicyAttr(cellinsertpolicyattr);
        }
    }

    public void updateBeans()
    {
        TemplateElementCase templateelementcase = elementCasePane.getEditingElementCase();
        int i = cs.getCellRectangleCount();
        for(int j = 0; j < i; j++)
        {
            Rectangle rectangle = cs.getCellRectangle(j);
            for(int k = 0; k < rectangle.height; k++)
            {
                for(int l = 0; l < rectangle.width; l++)
                {
                    int i1 = l + rectangle.x;
                    int j1 = k + rectangle.y;
                    Object obj = templateelementcase.getTemplateCellElement(i1, j1);
                    if(obj == null)
                    {
                        obj = new DefaultTemplateCellElement(i1, j1);
                        templateelementcase.addCellElement(((TemplateCellElement) (obj)));
                    }
                    updateBean(((TemplateCellElement) (obj)));
                }

            }

        }

    }

    public String title4PopupWindow()
    {
        return Inter.getLocText("Datasource-Other_Attributes");
    }



}
