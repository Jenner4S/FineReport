package com.fr.design.present;

import com.fr.base.FRContext;
import com.fr.base.Style;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.widget.WidgetPane;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.form.ui.DateEditor;
import com.fr.form.ui.NoneWidget;
import com.fr.form.ui.Widget;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.Selection;
import com.fr.privilege.finegrain.WidgetPrivilegeControl;
import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.elementcase.TemplateElementCase;

import java.awt.*;
import java.text.Format;
import java.text.SimpleDateFormat;

public class CellWriteAttrPane extends BasicPane {

    private WidgetPane cellEditorDefPane;

    public CellWriteAttrPane(ElementCasePane elementCasePane) {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        cellEditorDefPane = new WidgetPane(elementCasePane);
        this.add(cellEditorDefPane, BorderLayout.CENTER);
    }

    /**
     * ����cellWriteAttrPane
     *
     * @param elementCasePane ������ڻ�ȡһЩElementCasePane��Ϣ
     */
    public static void showWidgetWindow(ElementCasePane elementCasePane) {
        final CellWriteAttrPane wp = new CellWriteAttrPane(elementCasePane);
        Selection selection = elementCasePane.getSelection();
        if (!(selection instanceof CellSelection)) {
            return;
        }

        CellSelection cs = (CellSelection) selection;

        // got simple cell element from column and row.
        TemplateElementCase report = elementCasePane.getEditingElementCase();
        final TemplateCellElement editCellElement = report.getTemplateCellElement(cs.getColumn(), cs.getRow());
        wp.populate(editCellElement);
        BasicDialog dialog = wp.showWindow(DesignerContext.getDesignerFrame());
        dialog.addDialogActionListener(new DialogActionAdapter() {

            @Override
            public void doOk() {
                wp.update(editCellElement);
                DesignerContext.getDesignerFrame().getSelectedJTemplate().fireTargetModified();
            }
        });
        DesignerContext.setReportWritePane(dialog);
        dialog.setVisible(true);
    }

    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("FR-Designer-Widget_Settings");
    }

    public void populate(TemplateCellElement cellElement) {
        if (cellElement == null) {// ����Ĭ�ϵ�CellElement.
            cellElement = new DefaultTemplateCellElement(0, 0, null);
        }

        Widget cellWidget = cellElement.getWidget();

        if (cellWidget != null && cellWidget instanceof DateEditor) {
            // p:���ڵĸ�ʽ��Ҫ���õ���Ԫ��������.
            DateEditor dateCellEditorDef = (DateEditor) cellWidget;

            // p:��Ҫ�������ı༭��,���ڸ�ʽ�����ŵ�CellElement��Style����
            // ����ط��ܷ����û�����alex�����.
            // p:���ڵĸ�ʽ��Ҫ���õ���Ԫ��������.
            Style style = cellElement.getStyle();
            if (style != null) {
                Format format = style.getFormat();
                if (format != null && format instanceof SimpleDateFormat) {
                    SimpleDateFormat simpleDateFormat = (SimpleDateFormat) format;
                    dateCellEditorDef.setFormatText(simpleDateFormat.toPattern());
                }
            }
        }
        // ������п�¡��ԭ����Ϊ�˱���ԭʼ��Widget�Ա���µ�Widget���Ƚ����ж��Ƿ����˸ı�
        if (cellWidget != null) {
            try {
                cellWidget = (Widget) cellWidget.clone();
            } catch (CloneNotSupportedException e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }
        }
        cellEditorDefPane.populate(cellWidget);
    }

    public void update(TemplateCellElement cellElement) {
        if (cellElement == null) {// ����Ĭ�ϵ�CellElement.
            return;
        }

        Widget cellWidget = this.cellEditorDefPane.update();
        // p:��Ҫ�������ı༭��,���ڸ�ʽ�����ŵ�CellElement��Style����
        if (cellWidget instanceof DateEditor) {
            // p:���ڵĸ�ʽ��Ҫ���õ���Ԫ��������.
            DateEditor dateCellEditorDef = (DateEditor) cellWidget;
            String formatText = dateCellEditorDef.getFormatText();
            if (formatText != null) {
                Style style = cellElement.getStyle();
                if (style != null) {
                    cellElement.setStyle(style.deriveFormat(new SimpleDateFormat(formatText)));
                }
            }
        }

        // p:�������cellEditorDef���õ�CellGUIAttr.
        if (cellWidget instanceof NoneWidget) {
            cellElement.setWidget(null);
        } else {
            if (cellElement.getWidget() != null) {
                cellWidget = upDateWidgetAuthority(cellElement, cellWidget);
            }
            cellElement.setWidget(cellWidget);
        }

    }


    private Widget upDateWidgetAuthority(TemplateCellElement cellElement, Widget newWidget) {
        try {
            Widget oldWidget = (Widget) cellElement.getWidget().clone();
            if (newWidget.getClass() == oldWidget.getClass()) {
                newWidget.setWidgetPrivilegeControl((WidgetPrivilegeControl) oldWidget.getWidgetPrivilegeControl().clone());
            }
        } catch (Exception e) {
            FRLogger.getLogger().error(e.getMessage());
        }
        return newWidget;
    }

    @Override
    /**
     *����Ƿ���Ч
     */
    public void checkValid() throws Exception {
        this.cellEditorDefPane.checkValid();
    }

}
