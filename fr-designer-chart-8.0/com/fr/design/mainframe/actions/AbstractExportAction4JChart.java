// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.actions;

import com.fr.base.FRContext;
import com.fr.design.gui.iprogressbar.FRProgressBar;
import com.fr.design.mainframe.*;
import com.fr.design.mainframe.exporter.Exporter4Chart;
import com.fr.file.FILE;
import com.fr.file.FILEChooserPane;
import com.fr.file.filter.ChooseFileFilter;
import com.fr.form.ui.ChartBook;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import java.awt.event.ActionEvent;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

// Referenced classes of package com.fr.design.mainframe.actions:
//            JChartAction

public abstract class AbstractExportAction4JChart extends JChartAction
{

    private FRProgressBar progressbar;

    protected AbstractExportAction4JChart(JChart jchart)
    {
        super(jchart);
    }

    protected ChartBook getChartBook()
    {
        return (ChartBook)((JChart)getEditingComponent()).getTarget();
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        JChart jchart = (JChart)getEditingComponent();
        FILE file = jchart.getEditingFILE();
        DesignerFrame designerframe = DesignerContext.getDesignerFrame();
        HashMap hashmap = new HashMap();
        ChartBook chartbook = getChartBook();
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
            (progressbar = new FRProgressBar(createExportWork(file1, chartbook), designerframe, Inter.getLocText("Exporting"), "", 0, 100)).start();
        }
    }

    private SwingWorker createExportWork(FILE file, final ChartBook chartBook)
    {
        final String filePath = file.getPath();
        final String fileGetName = file.getName();
        SwingWorker swingworker = new SwingWorker() {

            final String val$filePath;
            final ChartBook val$chartBook;
            final String val$fileGetName;
            final AbstractExportAction4JChart this$0;

            protected Void doInBackground()
                throws Exception
            {
                Thread.sleep(100L);
                try
                {
                    FileOutputStream fileoutputstream = new FileOutputStream(filePath);
                    setProgress(10);
                    dealExporter(fileoutputstream, chartBook);
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
                this$0 = AbstractExportAction4JChart.this;
                filePath = s;
                chartBook = chartbook;
                fileGetName = s1;
                super();
            }
        }
;
        return swingworker;
    }

    private void dealExporter(FileOutputStream fileoutputstream, ChartBook chartbook)
        throws Exception
    {
        Exporter4Chart exporter4chart = getExporter();
        exporter4chart.export(fileoutputstream, (JChart)getEditingComponent());
    }

    protected abstract ChooseFileFilter getChooseFileFilter();

    protected abstract String getDefaultExtension();

    protected abstract Exporter4Chart getExporter();


}
