package com.fr.file;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.AbstractAction;
import javax.swing.AbstractListModel;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicButtonUI;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.dav.LocalEnv;
import com.fr.design.DesignerEnvManager;
import com.fr.design.actions.UpdateAction;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.UIDialog;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.DefaultCompletionFilter;
import com.fr.design.gui.itextfield.UIAutoCompletionField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.file.filetree.FileNode;
import com.fr.file.filter.ChooseFileFilter;
import com.fr.file.filter.FILEFilter;
import com.fr.general.ComparatorUtils;
import com.fr.general.GeneralContext;
import com.fr.general.Inter;
import com.fr.stable.CoreConstants;
import com.fr.stable.ProductConstants;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.project.ProjectConstants;

/*
 * FileChooserPaneҪ������ʾĳButton,����ʾ��ǰ·��
 * �߾�Ҫ����
 * postfix��û�д���
 */
public class FILEChooserPane extends BasicPane {
    /**
     * Return value if OK is chosen.
     */
    public static final int OK_OPTION = 0;
    /**
     * Return value if CANCEL is chosen.
     */
    public static final int CANCEL_OPTION = 1;

    public static final int JOPTIONPANE_OK_OPTION = 2;

    public static final int JOPTIONPANE_CANCEL_OPTION = 3;

    private static final FILEChooserPane INSTANCE = new FILEChooserPane();

    public FILE currentDirectory; // ��ǰ·��,��subFileList����ʾ���·�������е��ļ�

    private List<FILEFilter> filterList = new ArrayList<FILEFilter>();
    private FILEFilter filter;

    private LocationButtonPane locationBtnPane; // ��ʾlocation��Panel
    private UIButton createFolderButton;

    private PlaceListModel model;
    private JList placesList; // File.listRoots() + Env + Favourite
    private JList subFileList; // ��ǰѡ��Ŀ¼�µ��ļ��м��ļ�

    private JScrollPane scrollPane;
    private UIAutoCompletionField fileNameTextField; // �ļ������ı���
    private UIComboBox postfixComboBox; // �ļ���׺���������б��

    private UIButton okButton;
    private UIButton cancelButton;


    protected int type;
    protected boolean showEnv;
    protected boolean showLoc;
    protected boolean showWebReport = false;

    private UIDialog dialog;

    private int returnValue = CANCEL_OPTION;

    protected String suffix;

    /**
     * @return
     */
    public static FILEChooserPane getInstance() {
        return getInstance(true, true);
    }

    /**
     * @param showEnv
     * @return
     */
    public static FILEChooserPane getInstance(boolean showEnv) {
        return getInstance(showEnv, true);
    }

    /**
     * @param showEnv
     * @param showLoc
     * @return
     */
    public static FILEChooserPane getInstance(boolean showEnv, boolean showLoc) {
        INSTANCE.showEnv = showEnv;
        INSTANCE.showLoc = showLoc;
        INSTANCE.showWebReport = false;
        INSTANCE.setModelOfPlaceList();
        INSTANCE.removeAllFilter();
        return INSTANCE;
    }

    /**
     * @param showEnv
     * @param filter
     * @return
     */
    public static FILEChooserPane getInstance(boolean showEnv, FILEFilter filter) {
        INSTANCE.showEnv = showEnv;
        INSTANCE.setModelOfPlaceList();
        INSTANCE.removeAllFilter();
        INSTANCE.addChooseFILEFilter(filter, 0);
        return INSTANCE;
    }

    /**
     * @param showEnv
     * @param showLoc
     * @param filter
     * @return
     */
    public static FILEChooserPane getInstance(boolean showEnv, boolean showLoc, FILEFilter filter) {
        INSTANCE.showEnv = showEnv;
        INSTANCE.showLoc = showLoc;
        INSTANCE.showWebReport = false;
        INSTANCE.setModelOfPlaceList();
        INSTANCE.removeAllFilter();
        INSTANCE.addChooseFILEFilter(filter, 0);
        return INSTANCE;
    }

    /**
     * @param showEnv
     * @param showLoc
     * @param showWebReport
     * @param filter
     * @return
     */
    public static FILEChooserPane getInstance(boolean showEnv, boolean showLoc, boolean showWebReport, FILEFilter filter) {
        INSTANCE.showEnv = showEnv;
        INSTANCE.showLoc = showLoc;
        INSTANCE.showWebReport = showWebReport;
        INSTANCE.setModelOfPlaceList();
        INSTANCE.removeAllFilter();
        INSTANCE.addChooseFILEFilterToFist(filter, 0);
        return INSTANCE;
    }

    private FILEChooserPane() {
        this.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        this.setLayout(FRGUIPaneFactory.createBorderLayout());

        // kel:support Esc key.
        InputMap inputMapAncestor = this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        ActionMap actionMap = this.getActionMap();

        // transfer focus to CurrentEditor
        inputMapAncestor.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "dialogExit");
        actionMap.put("dialogExit", new AbstractAction() {
            public void actionPerformed(ActionEvent evt) {
                returnValue = CANCEL_OPTION;
                dialogExit();
            }
        });

        JPanel locationPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        // locationPane.setLayout(FRGUIPaneFactory.createBorderLayout());
        locationPane.add(locationBtnPane = new LocationButtonPane(), BorderLayout.CENTER);

        createFolderButton = createFolderButton();
        locationPane.add(createFolderButton, BorderLayout.EAST);

        JPanel centerLeftPanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        // centerLeftPanel.setLayout(FRGUIPaneFactory.createBorderLayout());
        // Richie:placesList includes C,D,E,F and DeskTop etc.
        placesList = new JList();
        centerLeftPanel.add(placesList, BorderLayout.CENTER);
        centerLeftPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        centerLeftPanel.setPreferredSize(new Dimension(120, 1));
        placesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        placesList.setCellRenderer(placelistRenderer);
        placesList.addListSelectionListener(placeListener);
        placesList.addMouseListener(placeMouseListener);

        // centerRightPane
        JPanel centerRightPane = FRGUIPaneFactory.createBorderLayout_S_Pane();

        JPanel subFilePanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        // subFilePanel.setLayout(FRGUIPaneFactory.createBorderLayout()); //
        // TODO alex_GUI
        // �Ժ�rightPanelҪ��JTable
        subFileList = new JList(new DefaultListModel());
        subFileList.setCellRenderer(listRenderer);
        subFileList.addMouseListener(subFileListMouseListener);
        subFileList.addKeyListener(subFileListKeyListener);
        scrollPane = new JScrollPane(subFileList);
        subFilePanel.add(scrollPane, BorderLayout.CENTER);
        centerRightPane.add(subFilePanel, BorderLayout.CENTER);

        // ��createTableLayoutPane������fileNamePane
        Component[][] coms = new Component[][]{
                new Component[]{GUICoreUtils.createBorderPane(new UILabel(Inter.getLocText("Utils-File_name") + ":"), BorderLayout.WEST),
                        fileNameTextField = new UIAutoCompletionField(), okButton = new UIButton(Inter.getLocText("Utils-Design-File_Open"))
                },
                new Component[]{GUICoreUtils.createBorderPane(new UILabel(Inter.getLocText("Utils-File_type") + ":"), BorderLayout.WEST),
                        postfixComboBox = new UIComboBox(), cancelButton = new UIButton(Inter.getLocText("Utils-Design-Action_Cancel"))
                }};

        JPanel fileNamePane = TableLayoutHelper.createGapTableLayoutPane(coms, new double[]{TableLayout.PREFERRED, TableLayout.PREFERRED,
                TableLayout.PREFERRED}, new double[]{TableLayout.PREFERRED, TableLayout.FILL, TableLayout.PREFERRED}, 24, 4);
        centerRightPane.add(fileNamePane, BorderLayout.SOUTH);

        Component[][] outComponents = new Component[][]{
                new Component[]{GUICoreUtils.createBorderPane(new UILabel(Inter.getLocText("FR-App-File_Lookup_range") + ":"), BorderLayout.WEST), locationPane},
                new Component[]{centerLeftPanel, centerRightPane}};
        JPanel contentPane = TableLayoutHelper.createTableLayoutPane(outComponents, new double[]{TableLayout.PREFERRED, TableLayout.FILL},
                new double[]{TableLayout.PREFERRED, TableLayout.FILL});
        this.add(contentPane, BorderLayout.CENTER);
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                doOK();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                returnValue = CANCEL_OPTION;
                doCancel();
            }
        });
        fileNameTextField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    returnValue = CANCEL_OPTION;
                    doOK();
                }
            }
        });
        postfixComboBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                Object ss = postfixComboBox.getSelectedItem();
                if (ss instanceof FILEFilter) {
                    setFILEFilter((FILEFilter) ss);
                } else {
					setFILEFilter(null);
				}
            }
        });
    }
    
    private void doCancel(){
    	this.locationBtnPane.setPopDir(null);
    	dialogExit();
    }

    /**
     * @param showEnv
     * @param showLoc
     */
    public FILEChooserPane(boolean showEnv, boolean showLoc) {
        this();
        this.showEnv = showEnv;
        this.showLoc = showLoc;
        this.setModelOfPlaceList();
    }


    /**
     * ����ѡ�е�FILE
     *
     * @return
     */
    public FILE getSelectedFILE() {
        String fileName = fileNameTextField.getText().trim();
        if (!fileName.endsWith(suffix) && fileName.indexOf(CoreConstants.DOT) == -1) {
            fileName += this.suffix;
        }
        if (currentDirectory == null) {
            return null;
        }
        if (currentDirectory instanceof FileFILE) {
            return new FileFILE((FileFILE) currentDirectory, fileName);
        } else {
            return new FileNodeFILE((FileNodeFILE) currentDirectory, fileName, false);
        }
    }

    protected String getEnvProjectName(){
        return Inter.getLocText("Utils-Report-Env_Directory");
    }

    /**
     * �����ļ�������
     *
     * @param filter ������
     */
    public void addChooseFILEFilter(FILEFilter filter) {
        addChooseFILEFilter(filter, filterList.size());
    }


    /**
     * ��ָ��index���ӹ�����
     *
     * @param filter ������
     * @param index ���
     */
    public void addChooseFILEFilter(FILEFilter filter, int index) {
        if (filterList.contains(filter)) {
            return;
        }
        this.filterList.add(index, filter);
    }

    // August:����ķ����ڰ���ʱֱ��return�ˣ�Ӧ�ðѸ�filter���ڵ�һ��
    // ��Ȼ�ڶ�δ�����FILEchooserpaneʱ��filter�����

    /**
     * �����Ѿ����ڣ���֮ɾȥ֮����ָ����λ������
     *
     * @param filter ����
     * @param index ���
     */
    public void addChooseFILEFilterToFist(FILEFilter filter, int index) {
        if (filterList.contains(filter)) {
            filterList.remove(filter);
            filterList.add(index, filter);
            return;
        }
        this.filterList.add(index, filter);
    }

    /**
     * ɾ���ļ�������
     *
     * @param filter ����
     */
    public void removeFILEFilter(FILEFilter filter) {
        if (filterList.contains(filter)) {
            this.filterList.remove(filter);
        }
    }

    /**
     * ɾ��ȫ���Ĺ�����
     */
    public void removeAllFilter() {
        this.filterList.clear();
    }


    /**
     * ����filter,ˢ���Ҳ�subFileList�е�items
     *
     * @param filter ����
     */
    public void setFILEFilter(FILEFilter filter) {
        this.filter = filter;

        refreshSubFileListModel();
    }

    /**
     * richer:Ĭ�ϵĻ���ʹ��.cpt��Ϊ��׺��
     *
     * @param text �ı�
     * @param suffix ��׺
     */
    public void setFileNameTextField(String text, String suffix) {
        if (StringUtils.isEmpty(suffix)) {
            suffix = ".cpt";
        }
        this.suffix = suffix;

        if (!text.endsWith(suffix)) {
            text = text + suffix;
        }
        fileNameTextField.removeDocumentListener();
        fileNameTextField.setText(text);
        fileNameTextField.addDocumentListener();
        if (currentDirectory != null) {
            return;
        }
        FILE[] res_array = currentDirectory.listFiles();
        String[] name_array = new String[res_array.length];
        for (int i = 0; i < res_array.length; i++) {
            name_array[i] = res_array[i].getName();
        }
        fileNameTextField.setFilter(new DefaultCompletionFilter(name_array));
    }

    /**
     * @return
     */
    public String getFileNameTextField() {
        return this.fileNameTextField.getText();
    }


    /**
     * �򿪶Ի���
     * @param parent ����
     * @return ����
     */
    public int showOpenDialog(Component parent) {
        return showOpenDialog(parent, ".cpt");
    }

    /**
     * �򿪶Ի���
     * @param parent ����
     * @param suffix ��׺
     * @return ����
     */
    public int showOpenDialog(Component parent, String suffix) {
        return showDialog(parent, JFileChooser.OPEN_DIALOG, suffix);
    }

    /**
     * �򿪶Ի���
     * @param parent ����
     * @return ����
     */
    public int showSaveDialog(Component parent) {
        return showSaveDialog(parent, ".cpt");
    }

    /**
     * �򿪶Ի���
     * @param parent ����
     * @param suffix ��׺
     * @return ����
     */
    public int showSaveDialog(Component parent, String suffix) {
        return showDialog(parent, JFileChooser.SAVE_DIALOG, suffix);
    }

    /**
     * august:�ؼ����¼�������������ӵ� ��ô��ÿ��showDialog������Ҫ�ظ����һ���¼���������
     *
     * @param parent ����
     * @param type ����
     * @param suffix ��׺
     * @return ����
     */
    public int showDialog(Component parent, int type, String suffix) {
        this.type = type;
        this.suffix = suffix;


        dialog = showWindow(SwingUtilities.getWindowAncestor(parent), false);
        JPanel contentPane = (JPanel) dialog.getContentPane();
        contentPane.setLayout(FRGUIPaneFactory.createM_BorderLayout());
        contentPane.add(this, BorderLayout.CENTER);
        okButton.setText(dialogName());
        // kel:�򿪽����ʱ�����ı����ý��㣬֧��enter�򿪻򱣴档
        dialog.addWindowListener(new WindowAdapter() {
            public void windowActivated(WindowEvent e) {
                fileNameTextField.requestFocusInWindow();
            }

            public void windowClosing(WindowEvent e) {
                returnValue = CANCEL_OPTION;
                dialogExit();
            }
        });

        // neil:Ĭ�ϴ�pane����ʾ����֧�ֵĸ�ʽ
        // daniel ��templateFileTree��ȡ
        if (!showWebReport) {
            fileType();
        }
        chooseType();
        // richer:���ļ����ͱ�ѡ��ʱ,��ʾ���ļ��ͽ�����ʾ��ѡ��������
        // ����Ǳ���Ի���,����Ĭ������
        if (type == JFileChooser.SAVE_DIALOG) {
            this.getFileNameTextField();
        } else {
            fileNameTextField.removeDocumentListener();
            fileNameTextField.setText("");
            fileNameTextField.addDocumentListener();
        }
        dialog.setVisible(true);
        return returnValue;
    }

    protected void fileType() {
        String appName = ProductConstants.APP_NAME;
        JTemplate editing = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
        if(ComparatorUtils.equals(suffix,".crt")){
            this.addChooseFILEFilter(new ChooseFileFilter("crt", appName + Inter.getLocText(new String[]{"Utils-The-Chart", "FR-App-All_File"})));
            return;
        }
        if(editing == null || !editing.isChartBook()){
            String[] fileSuffix_local = LocalEnv.FILE_TYPE;
            String[] fileSuffix = {"cpt", "frm", "form", "cht", "chart"};
            if (type == JFileChooser.OPEN_DIALOG) {
                if (FRContext.getCurrentEnv().isSupportLocalFileOperate()) { //��������
                    this.addChooseFILEFilter(new ChooseFileFilter(fileSuffix_local, appName + Inter.getLocText(new String[]{"FR-App-Report_Template", "FR-App-All_File"})));
                } else {
                    this.addChooseFILEFilter(new ChooseFileFilter(fileSuffix, appName + Inter.getLocText(new String[]{"FR-App-Report_Template", "FR-App-All_File"})));
                }
            }

            // ben:filefilter���ó�ֵΪcpt����
            this.addChooseFILEFilter(new ChooseFileFilter("cpt", appName + Inter.getLocText(new String[]{"FR-App-Report_Template", "FR-App-All_File"})));

            // richer:form�ļ� daniel �ĳ�������
            this.addChooseFILEFilter(new ChooseFileFilter("frm", appName + Inter.getLocText(new String[]{"FR-App-Template_Form", "FR-App-All_File"})));
            this.addChooseFILEFilter(new ChooseFileFilter("form", appName + Inter.getLocText(new String[]{"FR-App-Template_Form", "FR-App-All_File"})));
        }else{
            String[] fileSuffix_local =  {"xls","xlsx"};
            if (type == JFileChooser.OPEN_DIALOG) {
                this.addChooseFILEFilter(new ChooseFileFilter(fileSuffix_local,Inter.getLocText("Import-Excel_Source")));
            }
        }

        // ��� xls �ļ����͹��� kt
        if (FRContext.getCurrentEnv().isSupportLocalFileOperate()) {  //��������
            this.addChooseFILEFilter(new ChooseFileFilter("xls", Inter.getLocText("Import-Excel_Source")));
            this.addChooseFILEFilter(new ChooseFileFilter("xlsx", Inter.getLocText("Import-Excel2007_Source")));
        }
        if(ComparatorUtils.equals(suffix,".png")){
            this.addChooseFILEFilter(new ChooseFileFilter("png",Inter.getLocText("FR-App-Export_png")));
        }
        if (type == JFileChooser.SAVE_DIALOG) {
            this.addChooseFILEFilter(new ChooseFileFilter("pdf", Inter.getLocText("FR-Import-Export_PDF")));
            this.addChooseFILEFilter(new ChooseFileFilter("svg", Inter.getLocText("FR-Import-Export_SVG")));
            this.addChooseFILEFilter(new ChooseFileFilter("csv", Inter.getLocText("FR-Import-Export_CSV")));
            this.addChooseFILEFilter(new ChooseFileFilter("doc", Inter.getLocText("FR-Import-Export_Word")));
            this.addChooseFILEFilter(new ChooseFileFilter("txt", Inter.getLocText("FR-Import-Export_Text")));
        }

    }

    private void chooseType() {
        DefaultComboBoxModel defaultComboBoxModel = (DefaultComboBoxModel) postfixComboBox.getModel();
        defaultComboBoxModel.removeAllElements();
        for (int i = 0; i < filterList.size(); i++) {
            defaultComboBoxModel.addElement(filterList.get(i));
        }
        if (FRContext.getCurrentEnv().isSupportLocalFileOperate()) {  //��������
            if (!showWebReport) {
                defaultComboBoxModel.addElement(Inter.getLocText("FR-Utils-App_AllFiles") + "(*.*)");
            }
        }
        // Ĭ��ѡȡ���ļ�����(.cpt)����
        if (!filterList.isEmpty()) {
            setFILEFilter(filterList.get(0));
            defaultComboBoxModel.setSelectedItem(filterList.get(0));
        }
        // richer:���ݲ�ͬ���ļ�������ʾ��ͬ�ĺ�׺��
        // daniel �ĳ������ֱ�֤����
        if (ComparatorUtils.equals(suffix, ".frm") || ComparatorUtils.equals(suffix, ".form")) {
//            postfixComboBox.setSelectedIndex(2);
            // ����Ĭ���õ���".frm"
            postfixComboBox.setSelectedIndex(suffixIndex("frm"));
        } else if (ComparatorUtils.equals(suffix, ".xls")) {
            postfixComboBox.setSelectedIndex(suffixIndex("xls"));
        } else if (ComparatorUtils.equals(suffix, ".xlsx")) {
            postfixComboBox.setSelectedIndex(suffixIndex("xlsx"));
        } else if (ComparatorUtils.equals(suffix, ".pdf")) {
            postfixComboBox.setSelectedIndex(suffixIndex("pdf"));
        } else if (ComparatorUtils.equals(suffix, ".svg")) {
            postfixComboBox.setSelectedIndex(suffixIndex("svg"));
        } else if (ComparatorUtils.equals(suffix, ".csv")) {
            postfixComboBox.setSelectedIndex(suffixIndex("csv"));
        } else if (ComparatorUtils.equals(suffix, ".doc")) {
            postfixComboBox.setSelectedIndex(suffixIndex("doc"));
        } else if (ComparatorUtils.equals(suffix, ".txt")) {
            postfixComboBox.setSelectedIndex(suffixIndex("txt"));
        }else if(ComparatorUtils.equals(suffix,".png")){
            postfixComboBox.setSelectedIndex(suffixIndex("png"));
        }
        //jerry 26216 ֻ����.cpt .frm���õĸ�ʽ�����Ҳ��ɱ༭
        if (type == JFileChooser.OPEN_DIALOG) {
            postfixComboBox.setEnabled(true);
        } else {
            postfixComboBox.setEnabled(false);
        }

        //ֻ��һ������ʱ��������
        if (filterList.size() == 1) {
            postfixComboBox.setEnabled(false);
        }
    }

    private int suffixIndex(String extension){
        for(int i=0 ;i<filterList.size();i++){
            FILEFilter fileFilter = filterList.get(i);
            if(fileFilter.containsExtension(extension)){
                return i;
            }
        }
        return 0;
    }


    private void doOK() {
        // ���û��д�ļ���,������� or save
        if (fileNameTextField.getText().length() == 0) {
            return;
        }
        if (type == JFileChooser.OPEN_DIALOG) {
            if (this.subFileList.getSelectedValue() == null) {
                FILE file = this.getSelectedFILE();
                if (file.exists()) {
                    returnValue = OK_OPTION;
                    saveDictionary();
                    dialogExit();
                } else {
                    JOptionPane.showMessageDialog(this, Inter.getLocText("FR-App-Template_Report_Not_Exist"));
                    return;
                }
            }

            // alex:NPE�ж�,��Ϊ����û��ѡ����Ŀ¼,ֱ�����ı����������ļ���
            FILE selectedSubFile = (FILE) this.subFileList.getSelectedValue();
            if (selectedSubFile != null && selectedSubFile.isDirectory()) {
                setSelectedDirectory((FILE) this.subFileList.getSelectedValue());
            } else {
                returnValue = OK_OPTION;
                saveDictionary();
                dialogExit();
            }
        } else if (type == JFileChooser.SAVE_DIALOG) {
            saveDialog();

        }
    }


    private void saveDialog() {
        String filename = fileNameTextField.getText();
        if (!filename.endsWith(suffix)) {
            fileNameTextField.setText(filename + this.suffix);
        }
        returnValue = OK_OPTION;
        FILE selectedFile = this.getSelectedFILE();
        if (!FRContext.getCurrentEnv().hasFileFolderAllow(selectedFile.getPath())) {
            JOptionPane.showMessageDialog(FILEChooserPane.this, Inter.getLocText("FR-App-Privilege_No") + "!", Inter.getLocText("FR-App-File_Message"), JOptionPane.WARNING_MESSAGE);
            return ;
        }
        if (selectedFile.exists()) {
            int selVal = JOptionPane.showConfirmDialog(dialog, Inter.getLocText("FR-Utils-Would_you_like_to_cover_the_current_file") + " ?",
                    ProductConstants.PRODUCT_NAME, JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
            if (selVal == JOptionPane.YES_OPTION) {
                returnValue = JOPTIONPANE_OK_OPTION;
                saveDictionary();
                dialogExit();
            } else {
                returnValue = JOPTIONPANE_CANCEL_OPTION;
            }

        } else {
            dialogExit();
            saveDictionary();
        }
    }

    private void saveDictionary() {
        FILE selectedFile = this.getSelectedFILE();
        if (selectedFile != null) {
            DesignerEnvManager designerEnvManager = DesignerEnvManager.getEnvManager();
            String prefix = currentDirectory.prefix();
            String path = currentDirectory.getPath();
            designerEnvManager.setCurrentDirectoryPrefix(prefix);
            designerEnvManager.setDialogCurrentDirectory(path);
            designerEnvManager.saveXMLFile();
        }
    }

    private void dialogExit() {

        if (dialog == null) {
            return;
        }
        dialog.setVisible(false);
        dialog.dispose();
        dialog = null;
    }

    /**
     * @return
     */
    public int getReturnValue() {
        return this.returnValue;
    }

    /*
     * dialog������
     */
    private String dialogName() {
        return type == JFileChooser.OPEN_DIALOG ? Inter.getLocText("Utils-Design-File_Open") : Inter.getLocText("FR-App-Template_Save");
    }

    /*
     * ��subFileList��ѡ���ļ�
     */
    private void setSelectedFileName(String name) {
        if (name == null) {
            return;
        }

        ListModel model = subFileList.getModel();
        for (int i = 0, len = model.getSize(); i < len; i++) {
            if (ComparatorUtils.equals(name, ((FILE) model.getElementAt(i)).getName())) {
                subFileList.setSelectedIndex(i);
                // TODO alex_GUI ��ôScrollIntoView?
                break;
            }
        }
    }

    protected String title4PopupWindow() {
        return dialogName();
    }

    private class PlaceListModel extends AbstractListModel {
        private FileNodeFILE envFILE;
        private FileNodeFILE webReportFILE;
        private List<FileFILE> filesOfSystem = new ArrayList<FileFILE>();

        PlaceListModel() {
            if (FILEChooserPane.this.showEnv) {
                envFILE = new FileNodeFILE(new FileNode(ProjectConstants.REPORTLETS_NAME, true)){
                    public String getName() {
                        return getEnvProjectName();
                    }
                };
            }
            if (FILEChooserPane.this.showWebReport) {
                // webReportFILE = new FileFILE(new
                // File(FRContext.getCurrentEnv().getWebReportPath()));
                webReportFILE = new FileNodeFILE(FRContext.getCurrentEnv().getWebReportPath());
                // String webReportPath =
                // FRContext.getCurrentEnv().getWebReportPath();
                // String webReportParentPath = new
                // File(webReportPath).getParent();
                // webReportFILE = new FileNodeFILE(new FileNode("WebReport",
                // true),webReportParentPath);
            }
            if (FILEChooserPane.this.showLoc) {

                // ����
                File[] desktop = FileSystemView.getFileSystemView().getRoots();
                if (desktop != null) {
                    for (int i = 0; i < desktop.length; i++) {
                        if (desktop[i].exists()) {
                            filesOfSystem.add(new FileFILE(desktop[i]));
                        }
                    }
                }

                // C, D, E���̷�
                File[] roots = File.listRoots();
                if (roots != null) {
                    for (int i = 0; i < roots.length; i++) {
                        if (roots[i].exists()) {
                            filesOfSystem.add(new FileFILE(roots[i]));
                        }
                    }
                }
            }
        }

        @Override
        public FILE getElementAt(int index) {
            int n = FILEChooserPane.this.showEnv ? 1 : 0;
            int n2 = FILEChooserPane.this.showWebReport ? 1 : 0;

            if (index < n) {
                return envFILE;
            } else if (index < n + n2) {
                return webReportFILE;
            } else if (index < n + n2 + filesOfSystem.size()) {
                return filesOfSystem.get(index - n - n2);
            }
            throw new IndexOutOfBoundsException();
        }

        @Override
        public int getSize() {
            if (FILEChooserPane.this.showEnv && FILEChooserPane.this.showWebReport) {
                return 2 + filesOfSystem.size();
            } else if (FILEChooserPane.this.showEnv || FILEChooserPane.this.showWebReport) {
                return 1 + filesOfSystem.size();
            } else {
                return filesOfSystem.size();
            }
        }

        private void setCD(final FILE lastdirctory) {
            for (int i = 0; i < this.getSize(); i++) {
                FILE file = this.getElementAt(i);
                if (ComparatorUtils.equals(lastdirctory.prefix(), file.prefix())) {
                    setCurrentDirectory(lastdirctory);
                    return;
                }
            }
            setCurrentDirectory(this.getElementAt(0));
        }
    }

    protected void setModelOfPlaceList() {
        if (placesList == null) {
            return;
        }
        model = new PlaceListModel();
        placesList.setModel(model);
        String lastdirctorypath = DesignerEnvManager.getEnvManager().getDialogCurrentDirectory();
        String prefix = DesignerEnvManager.getEnvManager().getCurrentDirectoryPrefix();
        FILE lastdirctory = FILEFactory.createFolder(prefix + lastdirctorypath);
        model.setCD(lastdirctory);

        if (currentDirectory != null) {
            return;
        }
        // b:����Ӧ����currentDirectoryΪ��ʱ���Դ�envmanagerȡ�����FileSystemView����this.env��ɶ��ϵ
        if (StringUtils.isNotBlank(DesignerEnvManager.getEnvManager().getDialogCurrentDirectory())) {
            currentDirectory = FILEFactory.createFolder(DesignerEnvManager.getEnvManager().getDialogCurrentDirectory());
        } else {
            currentDirectory = FILEFactory.createFolder(FileSystemView.getFileSystemView().getHomeDirectory().getPath());
        }
        if (currentDirectory != null && currentDirectory.exists()) {
            this.setCurrentDirectory(currentDirectory);
        }
    }

    private ListCellRenderer placelistRenderer = new DefaultListCellRenderer() {

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof FILE) {
                FILE dir = (FILE) value;

                String name = dir.getName();
                if (name != null && !StringUtils.isBlank(name)) {
                    this.setText(name);
                } else {
                    this.setText(GeneralContext.getCurrentAppNameOfEnv());
                }
                Icon icon = dir.getIcon();
                if (icon != null) {
                    this.setIcon(icon);
                }
            }

            return this;
        }

    };
    /*
     * JList��CellRenderer
     */
    private ListCellRenderer listRenderer = new DefaultListCellRenderer() {

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof FILE) {
                FILE dir = (FILE) value;

                String name = dir.getName();
                if (name != null) {
                    this.setText(name);
                }
                Icon icon = dir.getIcon();
                if (icon != null) {
                    this.setIcon(icon);
                }
            }

            return this;
        }

    };
    // placeList listener
    ListSelectionListener placeListener = new ListSelectionListener() {
        public void valueChanged(ListSelectionEvent e) {
            Object selValue = placesList.getSelectedValue();
            if (selValue instanceof FILE) {
                setSelectedDirectory((FILE) selValue);
            }
        }
    };

    /**
     * placeList mouseListener
     */
    private MouseListener placeMouseListener = new MouseAdapter() {
        public void mousePressed(MouseEvent e) {
            Object selValue = placesList.getSelectedValue();
            if (selValue instanceof FILE) {
                setSelectedDirectory((FILE) selValue);
            }
        }
    };

    /**
     * right list.
     */
    private KeyListener subFileListKeyListener = new KeyAdapter() {
        public void keyReleased(KeyEvent e) {
            Object source = e.getSource();
            if (!(source instanceof JList)) {
                return;
            }

            JList list = (JList) source;

            Object selValue = list.getSelectedValue();
            if (selValue instanceof FILE) {
                setFileTextField((FILE) selValue);
                // if (((FILE)selValue).isDirectory()) {
                // fileNameTextField.setText("");
                // }
                // Richie:����Enter��ʱ����ļ��л��ߴ��ļ�
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    setSelectedDirectory((FILE) selValue);
                    if (!((FILE) selValue).isDirectory()) {
                        doOK();
                    }
                }
            }

        }
    };
    /*
     * �����JListʱ��listener
     */
    private MouseListener subFileListMouseListener = new MouseAdapter() {
        public void mousePressed(MouseEvent e) {
            Object source = e.getSource();
            if (!(source instanceof JList)) {
                return;
            }

            if (e.getClickCount() < 1) {
                return;
            }

            JList list = (JList) source;

            Object selValue = list.getSelectedValue();
            if (selValue instanceof FILE) {
                if (e.getClickCount() == 1) {
                    fileNameTextField.removeDocumentListener();
                    setFileTextField((FILE) selValue);
                    fileNameTextField.addDocumentListener();
                } else {
                    setSelectedDirectory((FILE) selValue);
                    if (!((FILE) selValue).isDirectory()) {
                        doOK();
                    }
                }
            }
        }
    };

    /*
     * ѡ���ļ�
     */
    private void setFileTextField(FILE file) {
        // clickedFILE = file;

        // String okButtonText;
        // if (file != null && file.isDirectory()
        // && this.fileNameTextField.getText().length() == 0) {
        // okButtonText = Inter.getLocText("Open");// + "(O)";
        // } else {
        // okButtonText = dialogName();
        // }
        // okButton.setText(okButtonText);

        if (file != null && !file.isDirectory()) {
            fileNameTextField.setText(file.getName());
            if (file instanceof FileFILE) {
                fileNameTextField.setText(((FileFILE) file).getTotalName());
            }
        }
    }

    /**
     * Sets the current directory. Passing in <code>null</code> sets the file
     * chooser to point to the user's default directory.
     *
     * @param dir the current directory to point to
     * @see #setSelectedDirectory
     */
    public void setCurrentDirectory(FILE dir) {
        if (dir == null && placesList != null) {
            dir = (FILE) placesList.getModel().getElementAt(0);
            placesList.setSelectedIndex(0);
        }

        if (placesList != null) {
            if (ComparatorUtils.equals(dir.prefix(), FILEFactory.ENV_PREFIX) || dir.prefix().endsWith(FILEFactory.WEBREPORT_PREFIX)) {
                placesList.setSelectedIndex(0);
            } else if (ComparatorUtils.equals(dir.prefix(), FILEFactory.FILE_PREFIX)) {
                PlaceListModel defaultListModel = (PlaceListModel) placesList.getModel();
                for (int i = 0; i < defaultListModel.getSize(); i++) {
                    if (defaultListModel.getElementAt(i) instanceof FileFILE) {
                        FileFILE popDir = (FileFILE) defaultListModel.getElementAt(i);
                        if (popDir != null && dir != null && dir.getPath().indexOf(popDir.getPath()) == 0) {
                            placesList.setSelectedIndex(i);
                            break;
                        }
                    }
                }
            }
        }

        setSelectedDirectory(dir);
    }

    /*
     * Set Selected Directory ���Ҳ��subFileList����ʾdir����������ļ����ļ���
     */
    private void setSelectedDirectory(FILE dir) {
        if (ComparatorUtils.equals(currentDirectory, dir) || dir == null || !dir.isDirectory()) {
            return;
        }

        currentDirectory = dir;

        this.locationBtnPane.populate(currentDirectory);
        this.createFolderButton.setEnabled(currentDirectory != null);

        refreshSubFileListModel();
    }

    /*
     * ˢ���Ҳ��SubFileList
     */
    private void refreshSubFileListModel() {
        if (currentDirectory != null) {
            ((DefaultListModel) subFileList.getModel()).removeAllElements();

            FILE[] res_array = currentDirectory.listFiles();
            ((DefaultListModel) subFileList.getModel()).removeAllElements();
            for (int i = 0; i < res_array.length; i++) {
                if (filter == null || filter.accept(res_array[i])) {
                    ((DefaultListModel) subFileList.getModel()).addElement(res_array[i]);
                }
            }
            String[] name_array = new String[res_array.length];
            for (int i = 0; i < res_array.length; i++) {
                name_array[i] = res_array[i].getName();
            }
            fileNameTextField.setFilter(new DefaultCompletionFilter(name_array));
            subFileList.validate();
            subFileList.repaint(10);
        }
    }

    /*
     * �����LocationButtonPane
     */
    private class LocationButtonPane extends JPanel {
        private FILE popDir;

        private BasicArrowButton leftArrowButton;
        private BasicArrowButton rightArrowButton;

        private List<UIButton> buttonList = new ArrayList<UIButton>();
        private int pathIndex = 0;
        private int maxPathIndex = 0;

        public LocationButtonPane() {
            this.setLayout(FRGUIPaneFactory.createBoxFlowLayout());

            leftArrowButton = new BasicArrowButton(BasicArrowButton.WEST) {
                public Dimension getPreferredSize() {
                    return new Dimension(21, 21);
                }
            };
            leftArrowButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (pathIndex > 0) {
                        pathIndex--;
                    }
                    LocationButtonPane.this.doLayout();
                    LocationButtonPane.this.repaint(10);
                }
            });

            rightArrowButton = new BasicArrowButton(BasicArrowButton.EAST) {
                public Dimension getPreferredSize() {
                    return new Dimension(21, 21);
                }
            };
            rightArrowButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (pathIndex < maxPathIndex) {
                        pathIndex++;
                    }
                    LocationButtonPane.this.doLayout();
                    LocationButtonPane.this.repaint(10);
                }
            });
        }

        public void highLightButton(FILE dir) {
            for (int i = 0; i < this.buttonList.size(); i++) {
                this.buttonList.get(i).setForeground(null);
                if (((SetDirectoryAction) this.buttonList.get(i).getAction()).getDir() != null
                        && this.buttonList.get(i).getAction() instanceof SetDirectoryAction
                        && (ComparatorUtils.equals(((SetDirectoryAction) this.buttonList.get(i).getAction()).getDir().getPath(), dir.getPath()))) {

                    this.buttonList.get(i).setForeground(Color.BLUE);
                }
            }
        }
        
        public void setPopDir(FILE file){
        	popDir = file; 
        }

        public void populate(FILE dir) {
            if (popDir != null && dir != null && popDir.toString().indexOf(dir.toString()) == 0) {
                highLightButton(dir);
                return;
            }

            popDir = dir;
            this.buttonList.clear();

            if (dir == null) {
                return;
            }

            String path = dir.getPath();
            // ȷ�����һ���ַ��Ƿָ���
            if (!path.endsWith("/") && !path.endsWith("\\") && !StringUtils.isBlank(path)) {
                path = path + "/";
            }
            String webAppName = GeneralContext.getCurrentAppNameOfEnv();
            if (StringUtils.isBlank(path)) {
                this.buttonList.add(createBlankButton(new SetDirectoryAction(webAppName + '/')));
            }

            Pattern seperatorPattern = Pattern.compile("[/\\\\]+"); // alex:֮������Pattern����Ӹ�+,����Ϊ��Щ·������������������ָ�������һ��
            Matcher matcher = seperatorPattern.matcher(path);
            int node_start = 0;
            while (matcher.find()) {
                int start = matcher.start();
                String btn_text = path.substring(node_start, start);
                String btn_path = path.substring(0, start);
                if (StringUtils.isBlank(btn_text)) {
                    btn_text = webAppName;
                }
                this.buttonList.add(createBlankButton((new SetDirectoryAction(btn_text + '/',
                        // alex:dir.prefix����btn_pathһ�����pathJoin,��Ϊbtn_path�Ƿ���/��ͷ��unix,linux
                        // OS������ܲ�һ��
                        FILEFactory.createFolder(dir.prefix() + StableUtils.pathJoin(new String[]{btn_path, "/"}))))));
                node_start = matcher.end();
            }
            maxPathIndex = calculateMaxPathIndex();

            pathIndex = maxPathIndex;
            this.doLayout();
            this.repaint(10);

            // ������ʾ��ǰĿ¼
            highLightButton(dir);
        }

        // doLayout
        public void doLayout() {
            this.removeAll();

            if (popDir == null) {
                return;
            }

            int pathWidth = 0;
            // ǰ׺
            // UILabel prefixLabel = new UILabel(popDir.prefix());
            // this.add(prefixLabel);
            // pathWidth += prefixLabel.getPreferredSize().width;

            // pathWidth
            for (int i = 0; i < buttonList.size(); i++) {
                pathWidth += buttonList.get(i).getPreferredSize().width;
            }

            // ��path��ť�ĳ��ȳ�����ʾ��Χʱ������
            if (this.getWidth() < pathWidth) {
                int tmpWidth = leftArrowButton.getPreferredSize().width + leftArrowButton.getPreferredSize().width;
                int oldTmpWidth = tmpWidth;
                for (int i = pathIndex; i < buttonList.size(); i++) {
                    tmpWidth += buttonList.get(i).getPreferredSize().width;
                    if (tmpWidth > this.getWidth()) {
                        break;
                    }


                    this.add(buttonList.get(i));
                    oldTmpWidth = tmpWidth;
                }
                UILabel blankLabel = new UILabel("");
                blankLabel.setPreferredSize(new Dimension(this.getWidth() - oldTmpWidth - 1, blankLabel.getPreferredSize().height));
                this.add(blankLabel);
                // leftArrowButton
                this.add(leftArrowButton, 1);
                // rightArrowButton
                this.add(rightArrowButton);
            } else {
                pathIndex = 0;
                for (int i = 0; i < buttonList.size(); i++) {
                    this.add(buttonList.get(i));
                }
            }

            super.doLayout();
        }

        private int calculateMaxPathIndex() {
            if (popDir == null) {
                return 0;
            }

            int pathWidth = 0;
            // ǰ׺
            UILabel prefixLabel = new UILabel(popDir.prefix());
            pathWidth += prefixLabel.getPreferredSize().width;

            for (int i = 0; i < buttonList.size(); i++) {
                pathWidth += buttonList.get(i).getPreferredSize().width;
            }

            if (this.getWidth() < pathWidth) {
                int index = 0;

                int tmpWidth = prefixLabel.getPreferredSize().width + leftArrowButton.getPreferredSize().width
                        + leftArrowButton.getPreferredSize().width;
                for (int i = buttonList.size() - 1; i >= 0; i--) {
                    tmpWidth += buttonList.get(i).getPreferredSize().width;
                    if (tmpWidth > this.getWidth()) {
                        break;
                    }
                    index = i;
                }

                return index;
            }
            // �������㹻ʱ�����������ҵİ�ť���Դ�0��ʼ��ʾ
            else {
                return 0;
            }
        }
    }

    private class SetDirectoryAction extends UpdateAction {
        private FILE dir;

        public SetDirectoryAction(String name) {
            this.setName(name);
            // this.dir = file;
        }

        public SetDirectoryAction(String name, FILE file) {
            this.setName(name);
            this.dir = file;
        }

        public void actionPerformed(ActionEvent evt) {
            if (dir != null) {
                setSelectedDirectory(dir);
            }
        }

        public FILE getDir() {
            return dir;
        }
    }


    private UIButton createBlankButton(SetDirectoryAction setDirectoryAction) {
        final UIButton blankButton = new UIButton(setDirectoryAction);
        blankButton.setMargin(new Insets(0, 0, 0, 0));
        blankButton.setUI(new BasicButtonUI());
        blankButton.setHorizontalTextPosition(SwingConstants.CENTER);
        blankButton.setBorderPainted(false);
        blankButton.setBorder(BorderFactory.createRaisedBevelBorder());
        blankButton.setBackground(FILEChooserPane.this.getBackground().darker());
        blankButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                blankButton.setBackground(FILEChooserPane.this.getBackground().brighter());
                blankButton.setBorderPainted(true);
                repaint();
            }

            public void mouseExited(MouseEvent e) {
                blankButton.setBackground(FILEChooserPane.this.getBackground().darker());
                blankButton.setBorderPainted(false);
                repaint();
            }

            public void mousePressed(MouseEvent e) {
                blankButton.setBackground(FILEChooserPane.this.getBackground().brighter());
                blankButton.setBorderPainted(false);
                repaint();
            }

            public void mouseReleased(MouseEvent e) {
                blankButton.setBackground(FILEChooserPane.this.getBackground().brighter());
                blankButton.setBorderPainted(true);
                repaint();
            }


        });
        return blankButton;
    }


    private ActionListener createFolderActionListener = new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
            if (currentDirectory == null) {
                return;
            }
            if (!FRContext.getCurrentEnv().hasFileFolderAllow(currentDirectory.getPath() + "/")) {
                JOptionPane.showMessageDialog(FILEChooserPane.this, Inter.getLocText("FR-App-Privilege_No") + "!", Inter.getLocText("FR-App-File_Message"), JOptionPane.WARNING_MESSAGE);
                return;
            }

            String res = JOptionPane.showInputDialog(Inter.getLocText("FR-Utils-Please_Input_a_New_Name"));
            if (res != null) {
                currentDirectory.createFolder(res);

                refreshSubFileListModel();

                setSelectedFileName(res);
                // ben:���ﴦ����Щ���ף�ȡ�ļ�ʱû�п���filefilter������Ч��һ����ȡ��ʱ��Ӧ����subfilelist��data
                FILE[] allFiles = currentDirectory.listFiles();
                int place = 0;
                for (int i = 0; i < allFiles.length; i++) {
                    if (ComparatorUtils.equals(allFiles[i].getName(), res) && allFiles[i].isDirectory()) {
                        place = i;
                        break;
                    }
                }
                scrollPane.validate();
                int total = scrollPane.getVerticalScrollBar().getMaximum();
                int value = total * place / subFileList.getModel().getSize();
                scrollPane.getVerticalScrollBar().setValue(value);
            }
        }
    };


    /*
     * �½��ļ��е�Button
     */
    private UIButton createFolderButton() {
        UIButton folderButton = new UIButton();
        folderButton.setIcon(BaseUtils.readIcon("/com/fr/design/images/file/newfolder.png"));
        folderButton.setEnabled(false);
        folderButton.setMargin(new Insets(0, 0, 0, 0));
        folderButton.setUI(new BasicButtonUI());
        folderButton.setToolTipText(Inter.getLocText("FR-Utils-New_Folder"));
        folderButton.addActionListener(createFolderActionListener);
        return folderButton;
    }
}
