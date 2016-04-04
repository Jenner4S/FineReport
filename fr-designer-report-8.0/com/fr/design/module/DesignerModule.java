// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.module;

import com.fr.base.*;
import com.fr.base.io.IOFile;
import com.fr.base.io.XMLEncryptUtils;
import com.fr.base.process.ProcessOperator;
import com.fr.design.DesignerEnvManager;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.actions.core.ActionUtils;
import com.fr.design.actions.insert.cell.*;
import com.fr.design.actions.insert.flot.*;
import com.fr.design.actions.server.StyleListAction;
import com.fr.design.fun.Feedback;
import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.hyperlink.ReportletHyperlinkPane;
import com.fr.design.hyperlink.WebHyperlinkPane;
import com.fr.design.javascript.*;
import com.fr.design.mainframe.*;
import com.fr.design.mainframe.bbs.BBSGuestPane;
import com.fr.design.mainframe.form.FormElementCaseDesigner;
import com.fr.design.mainframe.form.FormReportComponentComposite;
import com.fr.design.mainframe.loghandler.DesignerLogImpl;
import com.fr.design.parameter.WorkBookParameterReader;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.file.FILE;
import com.fr.general.*;
import com.fr.general.xml.GeneralXMLTools;
import com.fr.io.importer.Excel2007ReportImporter;
import com.fr.io.importer.ExcelReportImporter;
import com.fr.js.*;
import com.fr.main.impl.WorkBook;
import com.fr.plugin.ExtraClassManager;
import com.fr.quickeditor.ChartQuickEditor;
import com.fr.quickeditor.cellquick.*;
import com.fr.quickeditor.floatquick.FloatImageQuickEditor;
import com.fr.quickeditor.floatquick.FloatStringQuickEditor;
import com.fr.report.cell.CellElementValueConverter;
import com.fr.report.cell.cellattr.core.RichText;
import com.fr.report.cell.cellattr.core.SubReport;
import com.fr.report.cell.cellattr.core.group.DSColumn;
import com.fr.report.cell.painter.BiasTextPainter;
import com.fr.stable.ParameterProvider;
import com.fr.stable.bridge.StableFactory;
import com.fr.stable.script.CalculatorProviderContext;
import com.fr.stable.script.ValueConverter;
import com.fr.stable.xml.ObjectTokenizer;
import com.fr.stable.xml.ObjectXMLWriterFinder;
import com.fr.xml.ReportXMLUtils;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JDialog;
import javax.swing.JPanel;

// Referenced classes of package com.fr.design.module:
//            DesignModule, DesignModuleFactory

public class DesignerModule extends DesignModule
{
    private static abstract class AbstractWorkBookApp
        implements App
    {

        public int currentAPILevel()
        {
            return 1;
        }

        public JTemplate openTemplate(FILE file)
        {
            return new JWorkBook((WorkBook)asIOFile(file), file);
        }

        private AbstractWorkBookApp()
        {
        }

    }


    public DesignerModule()
    {
    }

    public void start()
    {
        super.start();
        justStartModules4Engine();
        justStartModules4Designer();
        CalculatorProviderContext.setValueConverter(valueConverter());
        GeneralXMLTools.Object_Tokenizer = startXMLReadObjectTokenizer();
        GeneralXMLTools.Object_XML_Writer_Finder = startObjectXMLWriterFinder();
        addAdapterForPlate();
        generateInsertActionClasses();
        registerHyperlink();
        registerCellEditor();
        registerFloatEditor();
        registerData4Form();
        registerOtherPane();
        InformationCollector.getInstance().collectStartTime();
        ExtraDesignClassManager.getInstance().getFeedback().didFeedback();
        ExtraClassManager.getInstance().addLogProvider(DesignerLogImpl.getInstance());
    }

    private void registerOtherPane()
    {
        StableFactory.registerMarkedClass("BBSGuestPane", com/fr/design/mainframe/bbs/BBSGuestPane);
    }

    private void registerCellEditor()
    {
        ActionUtils.registerCellEditor(java/lang/String, CellStringQuickEditor.getInstance());
        ActionUtils.registerCellEditor(java/lang/Number, CellStringQuickEditor.getInstance());
        ActionUtils.registerCellEditor(com/fr/base/Formula, CellStringQuickEditor.getInstance());
        ActionUtils.registerCellEditor(com/fr/report/cell/cellattr/core/SubReport, CellSubReportEditor.getInstance());
        ActionUtils.registerCellEditor(com/fr/report/cell/cellattr/core/RichText, CellRichTextEditor.getInstance());
        ActionUtils.registerCellEditor(com/fr/report/cell/cellattr/core/group/DSColumn, CellDScolumnEditor.getInstance());
        ActionUtils.registerCellEditor(java/awt/Image, CellImageQuickEditor.getInstance());
        ActionUtils.registerCellEditor(com/fr/report/cell/painter/BiasTextPainter, new CellBiasTextPainterEditor());
        ActionUtils.registerCellEditor(java/awt/image/BufferedImage, CellImageQuickEditor.getInstance());
        if(ModuleContext.isModuleStarted("com.fr.chart.module.ChartModule"))
            ActionUtils.registerChartCellEditorInEditor(ChartQuickEditor.getInstance());
    }

    public String getInterNationalName()
    {
        return Inter.getLocText("FR-Module_Designer");
    }

    private void registerFloatEditor()
    {
        FloatStringQuickEditor floatstringquickeditor = new FloatStringQuickEditor();
        ActionUtils.registerFloatEditor(java/lang/String, floatstringquickeditor);
        ActionUtils.registerFloatEditor(com/fr/base/Formula, floatstringquickeditor);
        FloatImageQuickEditor floatimagequickeditor = new FloatImageQuickEditor();
        ActionUtils.registerFloatEditor(java/awt/Image, floatimagequickeditor);
        ActionUtils.registerFloatEditor(java/awt/image/BufferedImage, floatimagequickeditor);
        if(ModuleContext.isModuleStarted("com.fr.chart.module.ChartModule"))
            ActionUtils.registerChartFloatEditorInEditor(ChartQuickEditor.getInstance());
    }

    private void justStartModules4Engine()
    {
        ModuleContext.startModule("com.fr.report.module.EngineModule");
    }

    private void justStartModules4Designer()
    {
        ModuleContext.startModule("com.fr.design.module.ChartDesignerModule");
        ModuleContext.startModule("com.fr.design.module.FormDesignerModule");
    }

    public ValueConverter valueConverter()
    {
        return new CellElementValueConverter();
    }

    public ObjectTokenizer startXMLReadObjectTokenizer()
    {
        return new com.fr.xml.ReportXMLUtils.ReportObjectTokenizer();
    }

    public ObjectXMLWriterFinder startObjectXMLWriterFinder()
    {
        return new com.fr.xml.ReportXMLUtils.ReportObjectXMLWriterFinder();
    }

    private void addAdapterForPlate()
    {
        ProcessTransitionAdapter.setProcessTransitionAdapter(new ProcessTransitionAdapter() {

            final DesignerModule this$0;

            protected String[] getTransitionNamesByBook(String s)
            {
                return ((ProcessOperator)StableFactory.getMarkedObject("ProcessOperator", com/fr/base/process/ProcessOperator, ProcessOperator.EMPTY)).getTransitionNamesByBook(s);
            }

            protected String[] getParaNames(String s)
            {
                return ((ProcessOperator)StableFactory.getMarkedObject("ProcessOperator", com/fr/base/process/ProcessOperator, ProcessOperator.EMPTY)).getParaNames(s);
            }

            protected ParameterProvider[] getParas(String s)
            {
                return ((ProcessOperator)StableFactory.getMarkedObject("ProcessOperator", com/fr/base/process/ProcessOperator, ProcessOperator.EMPTY)).getParas(s);
            }

            protected MultiFieldParameter[] getAllMultiFieldParas(String s)
            {
                return ((ProcessOperator)StableFactory.getMarkedObject("ProcessOperator", com/fr/base/process/ProcessOperator, ProcessOperator.EMPTY)).getAllMultiFieldParas(s);
            }

            
            {
                this$0 = DesignerModule.this;
                super();
            }
        }
);
    }

    public App[] apps4TemplateOpener()
    {
        return (new App[] {
            getCptApp(), getXlsApp(), getXlsxApp()
        });
    }

    private AbstractWorkBookApp getXlsxApp()
    {
        return new AbstractWorkBookApp() {

            final DesignerModule this$0;

            public String[] defaultExtentions()
            {
                return (new String[] {
                    "xlsx"
                });
            }

            public WorkBook asIOFile(FILE file)
            {
                WorkBook workbook = null;
                try
                {
                    workbook = (new Excel2007ReportImporter()).generateWorkBookByStream(file.asInputStream());
                }
                catch(Exception exception)
                {
                    FRContext.getLogger().error((new StringBuilder()).append("Failed to generate xlsx from ").append(file).toString(), exception);
                }
                return workbook;
            }

            public volatile IOFile asIOFile(FILE file)
            {
                return asIOFile(file);
            }

            
            {
                this$0 = DesignerModule.this;
                super();
            }
        }
;
    }

    private AbstractWorkBookApp getXlsApp()
    {
        return new AbstractWorkBookApp() {

            final DesignerModule this$0;

            public String[] defaultExtentions()
            {
                return (new String[] {
                    "xls"
                });
            }

            public WorkBook asIOFile(FILE file)
            {
                WorkBook workbook = null;
                try
                {
                    workbook = (new ExcelReportImporter()).generateWorkBookByStream(file.asInputStream());
                }
                catch(Exception exception)
                {
                    FRContext.getLogger().error((new StringBuilder()).append("Failed to generate xls from ").append(file).toString(), exception);
                }
                return workbook;
            }

            public volatile IOFile asIOFile(FILE file)
            {
                return asIOFile(file);
            }

            
            {
                this$0 = DesignerModule.this;
                super();
            }
        }
;
    }

    private AbstractWorkBookApp getCptApp()
    {
        return new AbstractWorkBookApp() {

            final DesignerModule this$0;

            public String[] defaultExtentions()
            {
                return (new String[] {
                    "cpt"
                });
            }

            public WorkBook asIOFile(FILE file)
            {
                if(XMLEncryptUtils.isCptEncoded() && !XMLEncryptUtils.checkVaild(DesignerEnvManager.getEnvManager().getEncryptionKey()) && !(new DecodeDialog(file)).isPwdRight())
                {
                    FRContext.getLogger().error(Inter.getLocText("ECP-error_pwd"));
                    return new WorkBook();
                }
                WorkBook workbook = new WorkBook();
                FRContext.getLogger().info(Inter.getLocText(new String[] {
                    "LOG-Is_Being_Openned", "LOG-Please_Wait"
                }, new String[] {
                    (new StringBuilder()).append("\"").append(file.getName()).append("\"").append(",").toString(), "..."
                }));
                TempNameStyle tempnamestyle = TempNameStyle.getInstance();
                tempnamestyle.clear();
                String s = "";
                try
                {
                    s = IOUtils.inputStream2String(file.asInputStream());
                    workbook.readStream(file.asInputStream());
                }
                catch(Exception exception)
                {
                    String s1 = "";
                    s1 = ComparatorUtils.equals("invalid user.", s) ? Inter.getLocText("FR-Designer_No-Privilege") : Inter.getLocText("NS-exception_readError");
                    FRContext.getLogger().error((new StringBuilder()).append(s1).append(file).toString(), exception);
                }
                DesignerModule.checkNameStyle(tempnamestyle);
                return workbook;
            }

            public volatile IOFile asIOFile(FILE file)
            {
                return asIOFile(file);
            }

            
            {
                this$0 = DesignerModule.this;
                super();
            }
        }
;
    }

    private static void checkNameStyle(TempNameStyle tempnamestyle)
    {
        Iterator iterator = tempnamestyle.getIterator();
        ArrayList arraylist = new ArrayList();
        for(; iterator.hasNext(); arraylist.add((String)iterator.next()));
        if(!arraylist.isEmpty())
            showConfirmDialog(arraylist);
    }

    private static void showConfirmDialog(ArrayList arraylist)
    {
        JDialog jdialog = new JDialog();
        jdialog.setAlwaysOnTop(true);
        jdialog.setSize(450, 150);
        jdialog.setResizable(false);
        jdialog.setIconImage(BaseUtils.readImage("/com/fr/base/images/oem/logo.png"));
        String s = arraylist.toString().replaceAll("\\[", "").replaceAll("\\]", "");
        UILabel uilabel = new UILabel(Inter.getLocText(new String[] {
            "Current_custom_global", "Has_been_gone"
        }, new String[] {
            s
        }));
        uilabel.setHorizontalAlignment(0);
        jdialog.add(uilabel, "Center");
        JPanel jpanel = new JPanel();
        UIButton uibutton = new UIButton(Inter.getLocText("FR-Designer_Yes"));
        uibutton.addActionListener(new ActionListener(arraylist, jdialog) {

            final ArrayList val$namelist;
            final JDialog val$jd;

            public void actionPerformed(ActionEvent actionevent)
            {
                try
                {
                    for(int i = 0; i < namelist.size(); i++)
                    {
                        ConfigManager.getProviderInstance().putStyle((String)namelist.get(i), Style.DEFAULT_STYLE);
                        FRContext.getCurrentEnv().writeResource(ConfigManager.getProviderInstance());
                    }

                }
                catch(Exception exception)
                {
                    FRLogger.getLogger().error(exception.getMessage());
                }
                jd.dispose();
                (new StyleListAction()).actionPerformed(actionevent);
            }

            
            {
                namelist = arraylist;
                jd = jdialog;
                super();
            }
        }
);
        UIButton uibutton1 = new UIButton(Inter.getLocText("FR-Designer_No"));
        uibutton1.addActionListener(new ActionListener(jdialog) {

            final JDialog val$jd;

            public void actionPerformed(ActionEvent actionevent)
            {
                jd.dispose();
            }

            
            {
                jd = jdialog;
                super();
            }
        }
);
        jpanel.add(uibutton);
        jpanel.add(uibutton1);
        jdialog.setTitle(Inter.getLocText("FR-Custom_styles_lost"));
        jdialog.add(jpanel, "South");
        GUICoreUtils.centerWindow(jdialog);
        jdialog.setVisible(true);
    }

    private void generateInsertActionClasses()
    {
        if(ModuleContext.isModuleStarted("com.fr.chart.module.ChartModule"))
        {
            ActionUtils.registerCellInsertActionClass(new Class[] {
                com/fr/design/actions/insert/cell/DSColumnCellAction, com/fr/design/actions/insert/cell/GeneralCellAction, com/fr/design/actions/insert/cell/RichTextCellAction, com/fr/design/actions/insert/cell/FormulaCellAction, com/fr/design/actions/insert/cell/ChartCellAction, com/fr/design/actions/insert/cell/ImageCellAction, com/fr/design/actions/insert/cell/BiasCellAction, com/fr/design/actions/insert/cell/SubReportCellAction
            });
            ActionUtils.registerFloatInsertActionClass(new Class[] {
                com/fr/design/actions/insert/flot/TextBoxFloatAction, com/fr/design/actions/insert/flot/FormulaFloatAction, com/fr/design/actions/insert/flot/ChartFloatAction, com/fr/design/actions/insert/flot/ImageFloatAction
            });
        } else
        {
            ActionUtils.registerCellInsertActionClass(new Class[] {
                com/fr/design/actions/insert/cell/DSColumnCellAction, com/fr/design/actions/insert/cell/GeneralCellAction, com/fr/design/actions/insert/cell/FormulaCellAction, com/fr/design/actions/insert/cell/ImageCellAction, com/fr/design/actions/insert/cell/BiasCellAction, com/fr/design/actions/insert/cell/SubReportCellAction
            });
            ActionUtils.registerFloatInsertActionClass(new Class[] {
                com/fr/design/actions/insert/flot/TextBoxFloatAction, com/fr/design/actions/insert/flot/FormulaFloatAction, com/fr/design/actions/insert/flot/ImageFloatAction
            });
        }
    }

    private void registerHyperlink()
    {
        if(ModuleContext.isModuleStarted("com.fr.chart.module.ChartModule"))
            DesignModuleFactory.registerCreators4Hyperlink(new NameableCreator[] {
                new NameObjectCreator(Inter.getLocText("FR-Hyperlink_Reportlet"), com/fr/js/ReportletHyperlink, com/fr/design/hyperlink/ReportletHyperlinkPane$CHART_NO_RENAME), new NameObjectCreator(Inter.getLocText("FR-Designer_Email"), com/fr/js/EmailJavaScript, com/fr/design/javascript/EmailPane), new NameObjectCreator(Inter.getLocText("Hyperlink-Web_link"), com/fr/js/WebHyperlink, com/fr/design/hyperlink/WebHyperlinkPane$CHART_NO_RENAME), new NameObjectCreator(Inter.getLocText("JavaScript-Dynamic_Parameters"), com/fr/js/ParameterJavaScript, com/fr/design/javascript/ParameterJavaScriptPane$CHART_NO_RENAME), new NameObjectCreator("JavaScript", com/fr/js/JavaScriptImpl, com/fr/design/javascript/JavaScriptImplPane$CHART_NO_RENAME)
            });
        else
            DesignModuleFactory.registerCreators4Hyperlink(new NameableCreator[] {
                new NameObjectCreator(Inter.getLocText("FR-Hyperlink_Reportlet"), com/fr/js/ReportletHyperlink, com/fr/design/hyperlink/ReportletHyperlinkPane), new NameObjectCreator(Inter.getLocText("FR-Designer_Email"), com/fr/js/EmailJavaScript, com/fr/design/javascript/EmailPane), new NameObjectCreator(Inter.getLocText("Hyperlink-Web_link"), com/fr/js/WebHyperlink, com/fr/design/hyperlink/WebHyperlinkPane), new NameObjectCreator(Inter.getLocText("JavaScript-Dynamic_Parameters"), com/fr/js/ParameterJavaScript, com/fr/design/javascript/ParameterJavaScriptPane), new NameObjectCreator("JavaScript", com/fr/js/JavaScriptImpl, com/fr/design/javascript/JavaScriptImplPane)
            });
    }

    private void registerData4Form()
    {
        StableFactory.registerMarkedClass("FormElementCaseDesigner", com/fr/design/mainframe/form/FormElementCaseDesigner);
        StableFactory.registerMarkedClass("FormReportComponentComposite", com/fr/design/mainframe/form/FormReportComponentComposite);
        DesignModuleFactory.registerParameterReader(new WorkBookParameterReader());
    }

}
