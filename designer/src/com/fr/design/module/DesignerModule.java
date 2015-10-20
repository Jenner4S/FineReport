package com.fr.design.module;

import com.fr.base.*;
import com.fr.base.io.XMLEncryptUtils;
import com.fr.base.process.ProcessOperator;
import com.fr.design.DesignerEnvManager;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.actions.core.ActionUtils;
import com.fr.design.actions.insert.cell.*;
import com.fr.design.actions.insert.flot.ChartFloatAction;
import com.fr.design.actions.insert.flot.FormulaFloatAction;
import com.fr.design.actions.insert.flot.ImageFloatAction;
import com.fr.design.actions.insert.flot.TextBoxFloatAction;
import com.fr.design.actions.server.StyleListAction;
import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.hyperlink.ReportletHyperlinkPane;
import com.fr.design.hyperlink.WebHyperlinkPane;
import com.fr.design.javascript.EmailPane;
import com.fr.design.javascript.JavaScriptImplPane;
import com.fr.design.javascript.ParameterJavaScriptPane;
import com.fr.design.javascript.ProcessTransitionAdapter;
import com.fr.design.mainframe.*;
import com.fr.design.mainframe.bbs.BBSGuestPane;
import com.fr.design.mainframe.form.FormECCompositeProvider;
import com.fr.design.mainframe.form.FormECDesignerProvider;
import com.fr.design.mainframe.form.FormElementCaseDesigner;
import com.fr.design.mainframe.form.FormReportComponentComposite;
import com.fr.design.parameter.WorkBookParameterReader;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.file.FILE;
import com.fr.general.*;
import com.fr.general.xml.GeneralXMLTools;
import com.fr.io.importer.Excel2007ReportImporter;
import com.fr.io.importer.ExcelReportImporter;
import com.fr.js.*;
import com.fr.main.impl.WorkBook;
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
import com.fr.stable.StringUtils;
import com.fr.stable.bridge.StableFactory;
import com.fr.stable.module.Module;
import com.fr.stable.script.CalculatorProviderContext;
import com.fr.stable.script.ValueConverter;
import com.fr.stable.xml.ObjectTokenizer;
import com.fr.stable.xml.ObjectXMLWriterFinder;
import com.fr.start.BBSGuestPaneProvider;
import com.fr.base.remote.RemoteDeziConstants;
import com.fr.xml.ReportXMLUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

public class DesignerModule extends DesignModule {

	/**
	 * ���������ģ��
	 */
	public void start() {
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
	}
	
	private void registerOtherPane(){
		StableFactory.registerMarkedClass(BBSGuestPaneProvider.XML_TAG, BBSGuestPane.class);
	}
	
	/**
	 * kunsnat:ע�ᵥԪ��ѡ��Editor
	 */
	
	private void registerCellEditor() {
		ActionUtils.registerCellEditor(String.class, CellStringQuickEditor.getInstance());
		ActionUtils.registerCellEditor(Number.class, CellStringQuickEditor.getInstance());
		ActionUtils.registerCellEditor(Formula.class, CellStringQuickEditor.getInstance());
		ActionUtils.registerCellEditor(SubReport.class, CellSubReportEditor.getInstance());
		ActionUtils.registerCellEditor(RichText.class, CellRichTextEditor.getInstance());
		ActionUtils.registerCellEditor(DSColumn.class, CellDScolumnEditor.getInstance());
		ActionUtils.registerCellEditor(Image.class, CellImageQuickEditor.getInstance());
		ActionUtils.registerCellEditor(BiasTextPainter.class, new CellBiasTextPainterEditor());
		ActionUtils.registerCellEditor(BufferedImage.class, CellImageQuickEditor.getInstance());
		
		if (ModuleContext.isModuleStarted(Module.CHART_MODULE)) {
			ActionUtils.registerChartCellEditorInEditor(ChartQuickEditor.getInstance());
		}
	}


    public String getInterNationalName(){
        return Inter.getLocText("FR-Module_Designer");
    }

	
	/**
	 * kunnat: ע������ѡ��Editor
	 */
	private void registerFloatEditor() {
		FloatStringQuickEditor floatStringQuickEditor = new FloatStringQuickEditor();
		ActionUtils.registerFloatEditor(String.class, floatStringQuickEditor);
		ActionUtils.registerFloatEditor(Formula.class, floatStringQuickEditor);
		
		FloatImageQuickEditor floatImageQuickEditor = new FloatImageQuickEditor();
		ActionUtils.registerFloatEditor(Image.class, floatImageQuickEditor);
		ActionUtils.registerFloatEditor(BufferedImage.class, floatImageQuickEditor);
		if (ModuleContext.isModuleStarted(Module.CHART_MODULE)) {
			ActionUtils.registerChartFloatEditorInEditor(ChartQuickEditor.getInstance());
		}
	}

	/**
	 * kunsnat: һЩģ����Ϣ ����������������,
	 * ���� ��ȡCC.XML, ���������֮��, ���ϻ��ȡXML, ��ҪChart_Module�е�ע����Ϣ
	 */
	private void justStartModules4Engine() {
		ModuleContext.startModule(ENGINE_MODULE);
	}
	
	private void justStartModules4Designer() {
		ModuleContext.startModule(CHART_DESIGNER_MODULE);
		ModuleContext.startModule(FORM_DESIGNER_MODULE);
	}

	/**
	 * CellElementValueConverter��������������������ֵ������ʽ/����/����Ԫ��ת���ɶ�Ӧ��ֵ��
	 *
	 * @return ���ش������ֵ��ת����
	 */
	public ValueConverter valueConverter() {
		return new CellElementValueConverter();
	}

	@Override
	/**
	 * ��Բ�ͬ�Ķ����ڶ�ȡObject�����xml��ʱ����Ҫʹ�ò�ͬ�Ķ���������
	 * @return ���ض���������
	 */
	public ObjectTokenizer startXMLReadObjectTokenizer() {
		return new ReportXMLUtils.ReportObjectTokenizer();
	}

	/**
	 * ��Բ�ͬ�Ķ�����д�����XMLʱ��Ҫʹ�ò�ͬ��XML������
	 *
	 * @return ����xml������
	 */
	@Override
	public ObjectXMLWriterFinder startObjectXMLWriterFinder() {
		return new ReportXMLUtils.ReportObjectXMLWriterFinder();
	}

	//wei:fs��ģ���п�������Ҫ��������������õĵط�����������
	private void addAdapterForPlate() {

		ProcessTransitionAdapter.setProcessTransitionAdapter(new ProcessTransitionAdapter() {

			@Override
			protected String[] getTransitionNamesByBook(String book) {
				return StableFactory.getMarkedObject(ProcessOperator.MARK_STRING, ProcessOperator.class, ProcessOperator.EMPTY).getTransitionNamesByBook(book);
			}

			@Override
			protected String[] getParaNames(String book) {
				return StableFactory.getMarkedObject(ProcessOperator.MARK_STRING, ProcessOperator.class, ProcessOperator.EMPTY).getParaNames(book);
			}

			@Override
			protected ParameterProvider[] getParas(String book) {
				return StableFactory.getMarkedObject(ProcessOperator.MARK_STRING, ProcessOperator.class, ProcessOperator.EMPTY).getParas(book);
			}

			protected MultiFieldParameter[] getAllMultiFieldParas(String book) {
				return StableFactory.getMarkedObject(ProcessOperator.MARK_STRING, ProcessOperator.class, ProcessOperator.EMPTY).getAllMultiFieldParas(book);
			}
		});
	}

	private static abstract class AbstractWorkBookApp implements DesignerFrame.App<WorkBook> {
		@Override
		public JTemplate<WorkBook, ?> openTemplate(FILE tplFile) {
			return new JWorkBook(asIOFile(tplFile), tplFile);
		}
	}

	@Override
	/**
	 * ����������ܴ򿪵�ģ�����͵�һ�������б�
	 * @return ���Դ򿪵�ģ�����͵�����
	 */
	public DesignerFrame.App[] apps4TemplateOpener() {
		return new DesignerFrame.App[]{new AbstractWorkBookApp() {
			public String[] defaultExtentions() {
				return new String[]{"cpt"};
			}
			public WorkBook asIOFile(FILE file) {
				if(XMLEncryptUtils.isCptEncoded() && 
						!XMLEncryptUtils.checkVaild(DesignerEnvManager.getEnvManager().getEncryptionKey())){
					if(!new DecodeDialog(file).isPwdRight()){
						FRContext.getLogger().error(Inter.getLocText("ECP-error_pwd"));
						return new WorkBook();
					}
				}
				
				WorkBook tpl = new WorkBook();
				// richer:�򿪱���֪ͨ
				FRContext.getLogger().info(Inter.getLocText(new String[]{"LOG-Is_Being_Openned", "LOG-Please_Wait"}, new String[]{"\"" + file.getName() + "\"" + ",", "..."}));
				TempNameStyle namestyle = TempNameStyle.getInstance();
				namestyle.clear();
				String checkStr = StringUtils.EMPTY;
				try {
					checkStr = IOUtils.inputStream2String(file.asInputStream());
					tpl.readStream(file.asInputStream());
				} catch (Exception exp) {
					String errorMessage = StringUtils.EMPTY;
					errorMessage = ComparatorUtils.equals(RemoteDeziConstants.INVALID_USER, checkStr) ? Inter.getLocText("FR-Designer_No-Privilege") 
							: Inter.getLocText("NS-exception_readError");
					FRContext.getLogger().error(errorMessage + file, exp);
				}
				checkNameStyle(namestyle);
				return tpl;
			}
		}, new AbstractWorkBookApp() {
			@Override
			public String[] defaultExtentions() {
				return new String[]{"xls"};
			}
			@Override
			public WorkBook asIOFile(FILE tplFile) {
				WorkBook workbook = null;
				try {
					workbook = new ExcelReportImporter().generateWorkBookByStream(tplFile.asInputStream());
				} catch (Exception exp) {
					FRContext.getLogger().error("Failed to generate xls from " + tplFile, exp);
				}
				return workbook;
			}
		}, new AbstractWorkBookApp() {
			@Override
			public String[] defaultExtentions() {
				return new String[]{"xlsx"};
			}
			@Override
			public WorkBook asIOFile(FILE tplFile) {
				WorkBook workbook = null;
				try {
					workbook = new Excel2007ReportImporter().generateWorkBookByStream(tplFile.asInputStream());
				} catch (Exception exp) {
					FRContext.getLogger().error("Failed to generate xlsx from " + tplFile, exp);
				}
				return workbook;
			}
		}};
	}

	private static void checkNameStyle(TempNameStyle namestyle) {
		Iterator it = namestyle.getIterator();
		ArrayList<String> al = new ArrayList<String>();
		while (it.hasNext()) {
			al.add((String) it.next());
		}
		if (!al.isEmpty()) {
			showConfirmDialog(al);
		}
	}

	private static void showConfirmDialog(final ArrayList<String> namelist) {

		final JDialog jd = new JDialog();
		// ģ̬һ�£���Ϊ���ܻ�����ʽ��ʧ
		// jd.setModal(true);
		jd.setAlwaysOnTop(true);
		jd.setSize(450, 150);
		jd.setResizable(false);
		jd.setIconImage(BaseUtils.readImage("/com/fr/base/images/oem/logo.png"));
		String message = namelist.toString().replaceAll("\\[", "").replaceAll("\\]", "");
		UILabel jl = new UILabel(Inter.getLocText(new String[]{"Current_custom_global", "Has_been_gone"}, new String[]{message}));
		jl.setHorizontalAlignment(SwingConstants.CENTER);
		jd.add(jl, BorderLayout.CENTER);
		JPanel jp = new JPanel();

		// ���ǡ���ť�����֮������һ��ȫ����ʽ����д��xml
		UIButton confirmButton = new UIButton(Inter.getLocText("FR-Designer_Yes"));
		confirmButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					for (int i = 0; i < namelist.size(); i++) {
						ConfigManager.getProviderInstance().putStyle(namelist.get(i), Style.DEFAULT_STYLE);
						FRContext.getCurrentEnv().writeResource(ConfigManager.getProviderInstance());
					}
				} catch (Exception ex) {
					FRLogger.getLogger().error(ex.getMessage());
				}
				jd.dispose();
				new StyleListAction().actionPerformed(e);// ����
			}
		});

		UIButton noButton = new UIButton(Inter.getLocText("FR-Designer_No"));
		noButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jd.dispose();
			}
		});

		jp.add(confirmButton);
		jp.add(noButton);
		jd.setTitle(Inter.getLocText("FR-Custom_styles_lost"));
		jd.add(jp, BorderLayout.SOUTH);
		GUICoreUtils.centerWindow(jd);
		jd.setVisible(true);
	}

	private void generateInsertActionClasses() {
		if (ModuleContext.isModuleStarted(CHART_MODULE)) {
			ActionUtils.registerCellInsertActionClass(new Class[]{
					DSColumnCellAction.class,
					GeneralCellAction.class,
					RichTextCellAction.class,
					FormulaCellAction.class,
					ChartCellAction.class,
					ImageCellAction.class,
					BiasCellAction.class,
					SubReportCellAction.class
			});
			ActionUtils.registerFloatInsertActionClass(new Class[]{
					TextBoxFloatAction.class,
					FormulaFloatAction.class,
					ChartFloatAction.class,
					ImageFloatAction.class
			});
		} else {
			ActionUtils.registerCellInsertActionClass(new Class[]{
					DSColumnCellAction.class,
					GeneralCellAction.class,
					FormulaCellAction.class,
					ImageCellAction.class,
					BiasCellAction.class,
					SubReportCellAction.class
			});
			ActionUtils.registerFloatInsertActionClass(new Class[]{
					TextBoxFloatAction.class,
					FormulaFloatAction.class,
					ImageFloatAction.class
			});
		}
	}

	private void registerHyperlink() {
		if (ModuleContext.isModuleStarted(Module.CHART_MODULE)) {
			DesignModuleFactory.registerCreators4Hyperlink(new NameableCreator[]{
					new NameObjectCreator(Inter.getLocText("FR-Hyperlink_Reportlet"), ReportletHyperlink.class, ReportletHyperlinkPane.CHART_NO_RENAME.class),
					new NameObjectCreator(Inter.getLocText("FR-Designer_Email"), EmailJavaScript.class, EmailPane.class),
					new NameObjectCreator(Inter.getLocText("Hyperlink-Web_link"), WebHyperlink.class, WebHyperlinkPane.CHART_NO_RENAME.class),
					new NameObjectCreator(Inter.getLocText("JavaScript-Dynamic_Parameters"), ParameterJavaScript.class, ParameterJavaScriptPane.CHART_NO_RENAME.class),
					new NameObjectCreator("JavaScript", JavaScriptImpl.class, JavaScriptImplPane.CHART_NO_RENAME.class)
			});
		} else {
			DesignModuleFactory.registerCreators4Hyperlink(new NameableCreator[]{
					new NameObjectCreator(Inter.getLocText("FR-Hyperlink_Reportlet"), ReportletHyperlink.class, ReportletHyperlinkPane.class),
					new NameObjectCreator(Inter.getLocText("FR-Designer_Email"), EmailJavaScript.class, EmailPane.class),
					new NameObjectCreator(Inter.getLocText("Hyperlink-Web_link"), WebHyperlink.class, WebHyperlinkPane.class),
					new NameObjectCreator(Inter.getLocText("JavaScript-Dynamic_Parameters"), ParameterJavaScript.class, ParameterJavaScriptPane.class),
					new NameObjectCreator("JavaScript", JavaScriptImpl.class, JavaScriptImplPane.class)
			});
		}
	}

	private void registerData4Form() {
		StableFactory.registerMarkedClass(FormECDesignerProvider.XML_TAG, FormElementCaseDesigner.class);
		StableFactory.registerMarkedClass(FormECCompositeProvider.XML_TAG, FormReportComponentComposite.class);
        DesignModuleFactory.registerParameterReader(new WorkBookParameterReader());
	}
}
