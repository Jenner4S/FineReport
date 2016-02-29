/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.file.export;

import com.fr.base.FRContext;
import com.fr.base.Parameter;
import com.fr.page.PageSetProvider;
import com.fr.design.actions.JWorkBookAction;
import com.fr.design.gui.iprogressbar.FRProgressBar;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.design.mainframe.JWorkBook;
import com.fr.design.parameter.ParameterInputPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.file.FILE;
import com.fr.file.FILEChooserPane;
import com.fr.file.filter.ChooseFileFilter;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.io.exporter.*;
import com.fr.main.TemplateWorkBook;
import com.fr.main.impl.WorkBook;
import com.fr.report.ReportHelper;
import com.fr.report.core.ReportUtils;
import com.fr.report.report.Report;
import com.fr.report.worksheet.WorkSheet;
import com.fr.stable.ActorConstants;
import com.fr.stable.ActorFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.FileOutputStream;
import java.util.Map;

/**
 * Abstract export action.
 */
public abstract class AbstractExportAction extends JWorkBookAction {
    protected AbstractExportAction(JWorkBook jwb) {
        super(jwb);
    }

    private FRProgressBar progressbar;

    protected WorkBook getTemplateWorkBook() {
        return this.getEditingComponent().getTarget();
    }

    /**
     * 执行方法
     */
    public void actionPerformed(ActionEvent e) {
        JWorkBook jwb = this.getEditingComponent();
        FILE editingFILE = jwb.getEditingFILE();
        DesignerFrame designerFrame = DesignerContext.getDesignerFrame();

        // 弹出参数
        final java.util.Map parameterMap = new java.util.HashMap();
        final TemplateWorkBook tpl = getTemplateWorkBook();
        Parameter[] parameters = tpl.getParameters();
        if (parameters != null && parameters.length > 0) {// 检查Parameter.
            final ParameterInputPane pPane = new ParameterInputPane(
                    parameters);
            pPane.showSmallWindow(designerFrame, new DialogActionAdapter() {

                @Override
                public void doOk() {
                    parameterMap.putAll(pPane.update());
                }
            }).setVisible(true);
        }

        // Choose a file name....
        FILEChooserPane fileChooserPane = FILEChooserPane.getInstance(false, true);
        fileChooserPane.setFILEFilter(this.getChooseFileFilter());

        // 打开文件后输出文件名修改，eg：w.cpt.doc / w.svg.doc，去掉中间的后缀名~~ w.doc
        String filenName = editingFILE.getName();
        if (filenName.indexOf('.') != -1) {
            filenName = filenName.substring(0, editingFILE.getName().lastIndexOf('.'));
        }
        fileChooserPane.setFileNameTextField(filenName, "." + this.getDefaultExtension());
        int saveValue = fileChooserPane.showSaveDialog(designerFrame, "." + this.getDefaultExtension());
        if (saveValue == FILEChooserPane.CANCEL_OPTION || saveValue == FILEChooserPane.JOPTIONPANE_CANCEL_OPTION) {
            fileChooserPane = null;
            return;
        } else if (saveValue == FILEChooserPane.JOPTIONPANE_OK_OPTION || saveValue == FILEChooserPane.OK_OPTION) {
            FILE file = fileChooserPane.getSelectedFILE();
            try {
                file.mkfile();
            } catch (Exception e1) {
                FRLogger.getLogger().error("Error In Make New File");
            }
            fileChooserPane = null;
            FRContext.getLogger().info("\"" + file.getName() + "\"" + Inter.getLocText("Prepare_Export") + "!");

            (progressbar = new FRProgressBar(createExportWork(file, tpl, parameterMap), designerFrame,
                    Inter.getLocText("Exporting"), "", 0, 100)).start();
        }
    }

    private SwingWorker createExportWork(FILE file, final TemplateWorkBook tpl, final Map parameterMap) {
        final String filePath = file.getPath();
        final String fileGetName = file.getName();

        SwingWorker exportWorker = new SwingWorker<Void, Void>() {
            protected Void doInBackground() throws Exception {
                Thread.sleep(100); //bug 10516
                try {
                    final FileOutputStream fileOutputStream = new FileOutputStream(filePath);

                    this.setProgress(10);
                    dealExporter(fileOutputStream, tpl, parameterMap);
                    this.setProgress(80);
                    fileOutputStream.close();
                    this.setProgress(100);

                    FRContext.getLogger().info("\"" + fileGetName + "\"" + Inter.getLocText("Finish_Export") + "!");
                    JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(),
                            Inter.getLocText("Exported_successfully") + "\n" + filePath);
                } catch (Exception exp) {
                    this.setProgress(100);
                    FRContext.getLogger().errorWithServerLevel(exp.getMessage(), exp);
                    JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), Inter.getLocText("Export_failed") + "\n" + filePath);
                }
                return null;
            }

            public void done() {
                progressbar.close();
            }
        };
        return exportWorker;
    }

    private void dealExporter(FileOutputStream fileOutputStream, final TemplateWorkBook tpl, final Map parameterMap) throws Exception {
        final Exporter exporter = AbstractExportAction.this.getExporter();
        if (exporter instanceof AppExporter) {
            AppExporter appExporter = (AppExporter) exporter;
            if (exporter instanceof ExcelExporter || exporter instanceof CSVExporter
                    || exporter instanceof PDFExporter || exporter instanceof WordExporter) {
                ReportHelper.clearFormulaResult(tpl);// 清空rpt中的公式计算结果

                appExporter.export(fileOutputStream, tpl.execute(parameterMap, ActorFactory.getActor(ActorConstants.TYPE_PAGE)
                ));
            } else {
                ReportHelper.clearFormulaResult(tpl);// 清空currentReport中的公式计算结果

                PageSetProvider pageSet = tpl.execute(parameterMap, ActorFactory.getActor(ActorConstants.TYPE_PAGE)).generateReportPageSet(
                        ReportUtils.getPaperSettingListFromWorkBook(tpl)).traverse4Export();
                appExporter.export(fileOutputStream, pageSet);
                pageSet.release();
            }
        } else if (exporter instanceof EmbeddedTableDataExporter) {
            ((EmbeddedTableDataExporter) exporter).export(fileOutputStream, (WorkBook) tpl, parameterMap);
        }
    }

    /*
     * 这边判断是否有层式报表，有层式需要使用大数据量导出
     */
    protected boolean hasLayerReport(TemplateWorkBook tpl) {
        if (tpl == null) {
            return false;
        }
        for (int i = 0; i < tpl.getReportCount(); i++) {
            Report r = tpl.getReport(i);
            if (r instanceof WorkSheet) {
                if (((WorkSheet) r).getLayerReportAttr() != null) {
                    return true;
                }
            }
        }

        return false;
    }

    protected abstract ChooseFileFilter getChooseFileFilter();

    protected abstract String getDefaultExtension();

    protected abstract Exporter getExporter();
}