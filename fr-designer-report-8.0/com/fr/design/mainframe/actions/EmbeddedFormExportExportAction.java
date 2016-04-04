// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.actions;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.design.actions.JTemplateAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.iprogressbar.FRProgressBar;
import com.fr.design.mainframe.*;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.menu.MenuKeySet;
import com.fr.design.parameter.ParameterInputPane;
import com.fr.file.FILE;
import com.fr.file.FILEChooserPane;
import com.fr.file.filter.ChooseFileFilter;
import com.fr.form.main.Form;
import com.fr.form.main.FormEmbeddedTableDataExporter;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import java.awt.event.ActionEvent;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

public class EmbeddedFormExportExportAction extends JTemplateAction
{

    private FRProgressBar progressbar;

    public EmbeddedFormExportExportAction(JForm jform)
    {
        super(jform);
        setMenuKeySet(KeySetUtils.EMBEDDED_EXPORT);
        setName((new StringBuilder()).append(getMenuKeySet().getMenuKeySetName()).append("...").toString());
        setMnemonic(getMenuKeySet().getMnemonic());
        setSmallIcon(BaseUtils.readIcon("/com/fr/base/images/oem/logo.png"));
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        JTemplate jtemplate = getEditingComponent();
        FILE file = jtemplate.getEditingFILE();
        DesignerFrame designerframe = DesignerContext.getDesignerFrame();
        HashMap hashmap = new HashMap();
        Form form = (Form)((JForm)getEditingComponent()).getTarget();
        inputParameter(hashmap, form, designerframe);
        FILEChooserPane filechooserpane = FILEChooserPane.getInstance(false, true);
        filechooserpane.setFILEFilter(getChooseFileFilter());
        String s = file.getName();
        filechooserpane.setFileNameTextField(s, ".frm");
        int i = filechooserpane.showSaveDialog(designerframe, ".frm");
        if(isCancel(i))
        {
            filechooserpane = null;
            return;
        }
        if(isOk(i))
            startExport(hashmap, form, designerframe, filechooserpane);
    }

    private void startExport(Map map, Form form, DesignerFrame designerframe, FILEChooserPane filechooserpane)
    {
        FILE file = filechooserpane.getSelectedFILE();
        try
        {
            file.mkfile();
        }
        catch(Exception exception)
        {
            FRLogger.getLogger().error("Error In Make New File");
        }
        filechooserpane = null;
        FRContext.getLogger().info((new StringBuilder()).append("\"").append(file.getName()).append("\"").append(Inter.getLocText("Prepare_Export")).append("!").toString());
        (progressbar = new FRProgressBar(createExportWork(file, form, map), designerframe, Inter.getLocText("Exporting"), "", 0, 100)).start();
    }

    private boolean isOk(int i)
    {
        return i == 2 || i == 0;
    }

    private boolean isCancel(int i)
    {
        return i == 1 || i == 3;
    }

    private void inputParameter(final Map parameterMap, Form form, DesignerFrame designerframe)
    {
        com.fr.base.Parameter aparameter[] = form.getParameters();
        if(ArrayUtils.isNotEmpty(aparameter))
        {
            final ParameterInputPane pPane = new ParameterInputPane(aparameter);
            pPane.showSmallWindow(designerframe, new DialogActionAdapter() {

                final Map val$parameterMap;
                final ParameterInputPane val$pPane;
                final EmbeddedFormExportExportAction this$0;

                public void doOk()
                {
                    parameterMap.putAll(pPane.update());
                }

            
            {
                this$0 = EmbeddedFormExportExportAction.this;
                parameterMap = map;
                pPane = parameterinputpane;
                super();
            }
            }
).setVisible(true);
        }
    }

    protected ChooseFileFilter getChooseFileFilter()
    {
        return new ChooseFileFilter(new String[] {
            "frm"
        }, Inter.getLocText("FR-Designer_Form_EmbeddedTD"));
    }

    private SwingWorker createExportWork(FILE file, final Form tpl, final Map parameterMap)
    {
        final String filePath = file.getPath();
        final String fileGetName = file.getName();
        SwingWorker swingworker = new SwingWorker() {

            final String val$filePath;
            final Form val$tpl;
            final Map val$parameterMap;
            final String val$fileGetName;
            final EmbeddedFormExportExportAction this$0;

            protected Void doInBackground()
                throws Exception
            {
                Thread.sleep(100L);
                try
                {
                    FileOutputStream fileoutputstream = new FileOutputStream(filePath);
                    setProgress(10);
                    FormEmbeddedTableDataExporter formembeddedtabledataexporter = new FormEmbeddedTableDataExporter();
                    formembeddedtabledataexporter.export(fileoutputstream, tpl, parameterMap);
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
                this$0 = EmbeddedFormExportExportAction.this;
                filePath = s;
                tpl = form;
                parameterMap = map;
                fileGetName = s1;
                super();
            }
        }
;
        return swingworker;
    }

}
