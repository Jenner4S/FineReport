package com.fr.design.mainframe;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import com.fr.base.FRContext;
import com.fr.design.designer.EditingState;
import com.fr.design.event.TargetModifiedListener;
import com.fr.design.gui.icontainer.UIModeControlContainer;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
import com.fr.grid.Grid;
import com.fr.main.impl.WorkBook;
import com.fr.report.report.TemplateReport;

/**
 * 整个报表编辑区域 包括滚动条、中间的grid或者聚合块、下面的sheetTab
 *
 * @editor zhou
 * @since 2012-3-27下午12:12:05
 */
public class ReportComponentComposite extends JComponent {
    private JWorkBook parent;
    private UIModeControlContainer parentContainer = null;

    protected ReportComponentCardPane centerCardPane;
    private JPanel CellElementRegion;

    private java.util.List<EditingState> templateStateList = new ArrayList<EditingState>();

    private SheetNameTabPane sheetNameTab;

    private JPanel hbarContainer;


    /**
     * Constructor with workbook..
     *
     * @param workBook the current workbook.
     */
    public ReportComponentComposite(JWorkBook jwb) {
        this.parent = jwb;
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.add(centerCardPane = new ReportComponentCardPane(), BorderLayout.CENTER);
        sheetNameTab = jwb.createSheetNameTabPane(this);
        sheetNameTab.setSelectedIndex(0);
        CellElementRegion = FRGUIPaneFactory.createBorderLayout_S_Pane();
        this.add(CellElementRegion, BorderLayout.NORTH);
        this.add(createSouthControlPane(), BorderLayout.SOUTH);
    }
    
    protected void doBeforeChange(int oldIndex) {
        if (oldIndex >= 0) {
            templateStateList.set(oldIndex, centerCardPane.editingComponet.createEditingState());
        }
    }

    protected void doAfterChange(int newIndex) {
        WorkBook workbook = getEditingWorkBook();
        if (workbook == null) {
            FRContext.getLogger().error(Inter.getLocText("Read_failure") + "!");
            //AUGUST:加个报错,不然测试总是SB的认为打不开一个坏的excel文件就是BUG，也不知道去检查下源文件。
            return;
        }
        centerCardPane.populate(workbook.getTemplateReport(newIndex));
        if (parentContainer != null) {
            parentContainer.setDownPane(ReportComponentComposite.this);
        }

        if (templateStateList.size() > newIndex) {
            EditingState reportPaneEditState = templateStateList.get(newIndex);
            if (reportPaneEditState != null) {
                reportPaneEditState.revert();
            }
        } else {
            while (templateStateList.size() <= newIndex) {
                templateStateList.add(null);
            }
            centerCardPane.editingComponet.setSelection(centerCardPane.editingComponet.getDefaultSelectElement());
        }

        if (centerCardPane.editingComponet.elementCasePane == null) {
            return;
        }
        Grid grid = centerCardPane.editingComponet.elementCasePane.getGrid();

        if (!grid.hasFocus() && grid.isRequestFocusEnabled()) {
            grid.requestFocus();
        }
    }

    /**
	 * 移除选中状态
	 * 
	 * @date 2015-2-5-上午11:41:44
	 * 
	 */
    public void removeSelection() {
        if (centerCardPane.editingComponet instanceof WorkSheetDesigner) {
            ((WorkSheetDesigner) centerCardPane.editingComponet).removeSelection();
        } else {
            centerCardPane.populate(centerCardPane.editingComponet.getTemplateReport());
        }

    }

    public TemplateReport getEditingTemplateReport() {
        return centerCardPane.editingComponet.getTemplateReport();
    }

    public int getEditingIndex() {
        return sheetNameTab.getSelectedIndex();
    }


    public void setParentContainer(UIModeControlContainer parentContainer) {
        this.parentContainer = parentContainer;
    }


    public void setComponents() {
        CellElementRegion.removeAll();
        hbarContainer.removeAll();
        hbarContainer.add(centerCardPane.editingComponet.getHorizontalScrollBar());
        centerCardPane.editingComponet.getHorizontalScrollBar().setValue(centerCardPane.editingComponet.getHorizontalScrollBar().getValue());
        centerCardPane.editingComponet.getVerticalScrollBar().setValue(centerCardPane.editingComponet.getVerticalScrollBar().getValue());
        this.doLayout();
    }

    public int getSelectedIndex() {
        return sheetNameTab.getSelectedIndex();
    }

    protected ReportComponent getEditingReportComponent() {
        return this.centerCardPane.editingComponet;
    }

    protected WorkBook getEditingWorkBook() {
        return this.parent.getTarget();
    }

    /**
     * 添加目标改变的监听
     *
     * @param targetModifiedListener 对象修改监听器
     */
    public void addTargetModifiedListener(TargetModifiedListener targetModifiedListener) {
        this.centerCardPane.addTargetModifiedListener(targetModifiedListener);
    }

    private JComponent createSouthControlPane() {
        hbarContainer = FRGUIPaneFactory.createBorderLayout_S_Pane();
        hbarContainer.add(centerCardPane.editingComponet.getHorizontalScrollBar());
        JSplitPane splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sheetNameTab, hbarContainer);
        splitpane.setBorder(null);
        splitpane.setDividerSize(3);
        splitpane.setResizeWeight(0.6);
        return splitpane;
    }

    public void setSelectedIndex(int selectedIndex) {
        sheetNameTab.setSelectedIndex(selectedIndex);
        centerCardPane.populate(getEditingWorkBook().getTemplateReport(selectedIndex));
    }

    /**
     * 停止编辑
     */
    public void stopEditing() {
        centerCardPane.stopEditing();
    }

    public void setComposite() {
        DesignerContext.getDesignerFrame().resetToolkitByPlus(parent);
        parent.setComposite();
        this.validate();
        this.repaint(40);
    }

    /**
     * 模板更新
     */
    public void fireTargetModified() {
        parent.fireTargetModified();
    }
}