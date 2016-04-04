// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.actions.file.export;

import com.fr.base.FRContext;
import com.fr.design.actions.JWorkBookAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.iprogressbar.FRProgressBar;
import com.fr.design.mainframe.*;
import com.fr.design.parameter.ParameterInputPane;
import com.fr.file.FILE;
import com.fr.file.FILEChooserPane;
import com.fr.file.filter.ChooseFileFilter;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.io.exporter.*;
import com.fr.main.TemplateWorkBook;
import com.fr.main.impl.WorkBook;
import com.fr.main.workbook.ResultWorkBook;
import com.fr.page.PageSetChainProvider;
import com.fr.page.PageSetProvider;
import com.fr.report.ReportHelper;
import com.fr.report.core.ReportUtils;
import com.fr.report.worksheet.WorkSheet;
import com.fr.stable.ActorFactory;
import java.awt.event.ActionEvent;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

public abstract class AbstractExportAction extends JWorkBookAction
{

    private FRProgressBar progressbar;

    protected AbstractExportAction(JWorkBook jworkbook)
    {
        super(jworkbook);
    }

    protected WorkBook getTemplateWorkBook()
    {
        return (WorkBook)((JWorkBook)getEditingComponent()).getTarget();
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        JWorkBook jworkbook = (JWorkBook)getEditingComponent();
        FILE file = jworkbook.getEditingFILE();
        DesignerFrame designerframe = DesignerContext.getDesignerFrame();
        final HashMap parameterMap = new HashMap();
        WorkBook workbook = getTemplateWorkBook();
        com.fr.base.Parameter aparameter[] = workbook.getParameters();
        if(aparameter != null && aparameter.length > 0)
        {
            final ParameterInputPane pPane = new ParameterInputPane(aparameter);
            pPane.showSmallWindow(designerframe, new DialogActionAdapter() {

                final Map val$parameterMap;
                final ParameterInputPane val$pPane;
                final AbstractExportAction this$0;

                public void doOk()
                {
                    parameterMap.putAll(pPane.update());
                }

            
            {
                this$0 = AbstractExportAction.this;
                parameterMap = map;
                pPane = parameterinputpane;
                super();
            }
            }
).setVisible(true);
        }
        FILEChooserPane filechooserpane = FILEChooserPane.getInstance(false, true);
        filechooserpane.setFILEFilter(getChooseFileFilter());
        String s = file.getName();
        if(s.indexOf('.') != -1)
            s = s.substring(0, file.getName().lastIndexOf('.'));
        filechooserpane.setFileNameTextField(s, (new StringBuilder()).append(".").append(getDefaultExtension()).toString());
        int i = filechooserpane.showSaveDialog(designerframe, (new StringBuilder()).append(".").append(getDefaultExtension()).toString());
        if(i == 1 || i == 3)
        {
            filechooserpane = null;
            return;
        }
        if(i == 2 || i == 0)
        {
            FILE file1 = filechooserpane.getSelectedFILE();
            try
            {
                file1.mkfile();
            }
            catch(Exception exception)
            {
                FRLogger.getLogger().error("Error In Make New File");
            }
            filechooserpane = null;
            FRContext.getLogger().info((new StringBuilder()).append("\"").append(file1.getName()).append("\"").append(Inter.getLocText("Prepare_Export")).append("!").toString());
            (progressbar = new FRProgressBar(createExportWork(file1, workbook, parameterMap), designerframe, Inter.getLocText("Exporting"), "", 0, 100)).start();
        }
    }

    private SwingWorker createExportWork(FILE file, final TemplateWorkBook tpl, final Map parameterMap)
    {
        final String filePath = file.getPath();
        final String fileGetName = file.getName();
        SwingWorker swingworker = new SwingWorker() {

            final String val$filePath;
            final TemplateWorkBook val$tpl;
            final Map val$parameterMap;
            final String val$fileGetName;
            final AbstractExportAction this$0;

            protected Void doInBackground()
                throws Exception
            {
                Thread.sleep(100L);
                try
                {
                    FileOutputStream fileoutputstream = new FileOutputStream(filePath);
                    setProgress(10);
                    dealExporter(fileoutputstream, tpl, parameterMap);
                    setProgress(80);
                    fileoutputstream.close();
                    setProgress(100);
                    FRContext.getLogger().info((new StringBuilder()).append("\"").append(fileGetName).append("\"").append(Inter.getLocText("Finish_Export")).append("!").toString());
                    JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), (new StringBuilder()).append(Inter.getLocText("Exported_successfully")).append("\n").append(filePath).toString());
                }
                catch(Exception exception)
                {
                    setProgress(100);
                    FRContext.getLogger().errorWithServerLevel(exception.getMessage(), exception);
                    JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), (new StringBuilder()).append(Inter.getLocText("Export_failed")).append("\n").append(filePath).toString());
                }
                return null;
            }

            public void done()
            {
                progressbar.close();
            }

            protected volatile Object doInBackground()
                throws Exception
            {
                return doInBackground();
            }

            
            {
                this$0 = AbstractExportAction.this;
                filePath = s;
                tpl = templateworkbook;
                parameterMap = map;
                fileGetName = s1;
                super();
            }
        }
;
        return swingworker;
    }

    private void dealExporter(FileOutputStream fileoutputstream, TemplateWorkBook templateworkbook, Map map)
        throws Exception
    {
        Exporter exporter = getExporter();
        if(exporter instanceof AppExporter)
        {
            AppExporter appexporter = (AppExporter)exporter;
            if((exporter instanceof ExcelExporter) || (exporter instanceof CSVExporter) || (exporter instanceof PDFExporter) || (exporter instanceof WordExporter))
            {
                ReportHelper.clearFormulaResult(templateworkbook);
                appexporter.export(fileoutputstream, templateworkbook.execute(map, ActorFactory.getActor("page")));
            } else
            {
                ReportHelper.clearFormulaResult(templateworkbook);
                PageSetProvider pagesetprovider = templateworkbook.execute(map, ActorFactory.getActor("page")).generateReportPageSet(ReportUtils.getPaperSettingListFromWorkBook(templateworkbook)).traverse4Export();
                appexporter.export(fileoutputstream, pagesetprovider);
                pagesetprovider.release();
            }
        } else
        if(exporter instanceof EmbeddedTableDataExporter)
            ((EmbeddedTableDataExporter)exporter).export(fileoutputstream, (WorkBook)templateworkbook, map);
    }

    protected boolean hasLayerReport(TemplateWorkBook templateworkbook)
    {
        if(templateworkbook == null)
            return false;
        for(int i = 0; i < templateworkbook.getReportCount(); i++)
        {
            com.fr.report.report.Report report = templateworkbook.getReport(i);
            if((report instanceof WorkSheet) && ((WorkSheet)report).getLayerReportAttr() != null)
                return true;
        }

        return false;
    }

    protected abstract ChooseFileFilter getChooseFileFilter();

    protected abstract String getDefaultExtension();

    protected abstract Exporter getExporter();


}
