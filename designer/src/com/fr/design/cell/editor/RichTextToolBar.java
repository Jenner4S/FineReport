/**
 * 
 */
package com.fr.design.cell.editor;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.base.Formula;
import com.fr.base.Utils;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.formula.FormulaFactory;
import com.fr.design.formula.UIFormula;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIToggleButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.style.FRFontPane;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.report.RichTextEditingPane;
import com.fr.design.report.RichTextPane;
import com.fr.design.style.color.UIToolbarColorButton;
import com.fr.general.FRFont;
import com.fr.general.Inter;
import com.fr.report.cell.cellattr.core.RichTextConverter;
import com.fr.stable.Constants;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;

/**
 *
 *
 * @date: 2014-12-5-����1:10:31
 */
public class RichTextToolBar extends BasicPane{

    private static final Dimension BUTTON_SIZE = new Dimension(24, 20);

    private UIComboBox fontNameComboBox;
    private UIComboBox fontSizeComboBox;
    private UIToggleButton bold;
    private UIToggleButton italic;
    private UIToggleButton underline;

    private UIToolbarColorButton colorSelectPane;
    private UIToggleButton superPane;
    private UIToggleButton subPane;
    private UIToggleButton formulaPane;
    
    //�ⲿ��������
    private RichTextEditingPane textPane;

    public RichTextToolBar() {
        this.initComponents();
    }
    
    public RichTextToolBar(RichTextEditingPane textPane) {
    	this.textPane = textPane;
    	
    	this.initComponents();
    }

    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("FR-Designer_font");
    }

    protected void initComponents() {
    	//��ʼ�����������а�ť��ʽ
        initAllButton();
        //��ӵ�������
        addToToolBar();
    }
    
    private void initAllButton(){
        fontNameComboBox = new UIComboBox(Utils.getAvailableFontFamilyNames4Report());
        fontNameComboBox.setPreferredSize(new Dimension(144, 20));
        fontSizeComboBox = new UIComboBox(FRFontPane.FONT_SIZES);
		colorSelectPane = new UIToolbarColorButton(BaseUtils.readIcon("/com/fr/design/images/gui/color/foreground.png"));
		colorSelectPane.set4Toolbar();
        
        bold = new UIToggleButton(BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/bold.png"));
        italic = new UIToggleButton(BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/italic.png"));
        underline = new UIToggleButton(BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/underline.png"));
        superPane = new UIToggleButton(BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/sup.png"));
        subPane = new UIToggleButton(BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/sub.png"));
        formulaPane = new UIToggleButton(BaseUtils.readIcon("/com/fr/design/images/m_insert/formula.png"));

        //����
        initAllNames();
        //������ʾ
        setToolTips();
        //��ʽ
        setAllButtonStyle();
    	//�󶨼�����
        bindListener();
    }
    
    private void setAllButtonStyle(){
    	setButtonStyle(bold);
    	setButtonStyle(italic);
    	setButtonStyle(underline);
    	setButtonStyle(subPane);
    	setButtonStyle(superPane);
    	setButtonStyle(formulaPane);
    }
    
    private void setButtonStyle(UIButton button){
    	button.setNormalPainted(false);
		button.setBackground(null);
		button.setOpaque(false);
		button.setPreferredSize(BUTTON_SIZE);
		button.setBorderPaintedOnlyWhenPressed(true);
    }
    
    private void addToToolBar(){
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
    	
        this.add(fontNameComboBox);
        this.add(fontSizeComboBox);
        this.add(bold);
        this.add(italic);
        this.add(underline);
        this.add(colorSelectPane);
        this.add(superPane);
        this.add(subPane);
        this.add(formulaPane);
    }
    
    private void bindListener(){
        FRFont defaultFont = RichTextPane.DEFAUL_FONT;
        fontNameComboBox.addItemListener(fontNameItemListener);
        fontNameComboBox.setSelectedItem(defaultFont.getFontName());
        fontSizeComboBox.addItemListener(fontSizeItemListener);
        fontSizeComboBox.setSelectedItem(scaleDown(defaultFont.getSize()));
        
        bold.addActionListener(blodChangeAction);
        italic.addActionListener(itaChangeAction);
        underline.addActionListener(underlineChangeAction);
        subPane.addActionListener(subChangeAction);
        superPane.addActionListener(superChangeAction);
        colorSelectPane.addColorChangeListener(colorChangeAction);
        formulaPane.addActionListener(formulaActionListener);
        
        //ѡ�����ֵļ�����
        textPane.addCaretListener(textCareListener);
        textPane.addMouseListener(setMouseCurrentStyle);
        textPane.getDocument().addDocumentListener(inputListener);
    }

    private void initAllNames() {
        fontNameComboBox.setGlobalName(Inter.getLocText("FR-Designer_Font-Family"));
        fontSizeComboBox.setGlobalName(Inter.getLocText("FR-Designer_Font-Size"));
        italic.setGlobalName(Inter.getLocText("FR-Designer_italic"));
        bold.setGlobalName(Inter.getLocText("FR-Designer_bold"));
        underline.setGlobalName(Inter.getLocText("FR-Designer_Underline"));
        superPane.setGlobalName(Inter.getLocText("FR-Designer_Superscript"));
        subPane.setGlobalName(Inter.getLocText("FR-Designer_Subscript"));
    }

    private void setToolTips() {
        colorSelectPane.setToolTipText(Inter.getLocText("FR-Designer_Foreground"));
        italic.setToolTipText(Inter.getLocText("FR-Designer_italic"));
        bold.setToolTipText(Inter.getLocText("FR-Designer_bold"));
        underline.setToolTipText(Inter.getLocText("FR-Designer_Underline"));
        superPane.setToolTipText(Inter.getLocText("FR-Designer_Superscript"));
        subPane.setToolTipText(Inter.getLocText("FR-Designer_Subscript"));
        formulaPane.setToolTipText(Inter.getLocText("FR-Designer_Formula"));
    }
    
    /**
	 * �Ƴ��������
	 * ����populateʱ, �����ַ���, ��ʱ����Ҫ�������
	 * 
	 *
	 * @date 2015-1-5-����5:13:04
	 * 
	 */
    public void removeInputListener(){
    	this.textPane.getDocument().removeDocumentListener(inputListener);
    }
    
    /**
	 * ������������¼�
	 * 
	 *
	 * @date 2015-1-5-����5:13:26
	 * 
	 */
    public void addInputListener(){
    	this.textPane.getDocument().addDocumentListener(inputListener);
    }
    
    private ActionListener blodChangeAction = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			boolean isBold = RichTextToolBar.this.bold.isSelected();
			// ����setCharacterAttributes���������ı���ѡ���ı�������
			MutableAttributeSet attr = new SimpleAttributeSet();
			StyleConstants.setBold(attr, !isBold);
			setCharacterAttributes(RichTextToolBar.this.textPane, attr, false);
		}
	};
	
	private ActionListener itaChangeAction = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			boolean isIta = RichTextToolBar.this.italic.isSelected();
			// ����setCharacterAttributes���������ı���ѡ���ı�������
			MutableAttributeSet attr = new SimpleAttributeSet();
			StyleConstants.setItalic(attr, !isIta);
			setCharacterAttributes(RichTextToolBar.this.textPane, attr, false);
		}
	};
	
	private ActionListener underlineChangeAction = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			boolean isUnder = RichTextToolBar.this.underline.isSelected();
			// ����setCharacterAttributes���������ı���ѡ���ı�������
			MutableAttributeSet attr = new SimpleAttributeSet();
			StyleConstants.setUnderline(attr, !isUnder);
			setCharacterAttributes(RichTextToolBar.this.textPane, attr, false);
		}
	};
	private ActionListener subChangeAction = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			boolean isSub = RichTextToolBar.this.subPane.isSelected();
			// ����setCharacterAttributes���������ı���ѡ���ı�������
			MutableAttributeSet attr = new SimpleAttributeSet();
			StyleConstants.setSubscript(attr, !isSub);
			setCharacterAttributes(RichTextToolBar.this.textPane, attr, false);
		}
	};
	private ActionListener superChangeAction = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			boolean isSuper = RichTextToolBar.this.superPane.isSelected();
			// ����setCharacterAttributes���������ı���ѡ���ı�������
			MutableAttributeSet attr = new SimpleAttributeSet();
			StyleConstants.setSuperscript(attr, !isSuper);
			setCharacterAttributes(RichTextToolBar.this.textPane, attr, false);
		}
	};
	
	private ChangeListener colorChangeAction = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
        	Color color = RichTextToolBar.this.colorSelectPane.getColor();
        	color = color == null ? Color.BLACK : color;
        	// ����setCharacterAttributes���������ı���ѡ���ı�������
			MutableAttributeSet attr = new SimpleAttributeSet();
			StyleConstants.setForeground(attr, color);
			setCharacterAttributes(RichTextToolBar.this.textPane, attr, false);
        }
    };
	
	// �����ı���ѡ���ı�����ʽ
	private void setCharacterAttributes(JEditorPane editor, AttributeSet attr,
			boolean replace) {
		//ע�ⲻҪʧ��
		textPane.requestFocus();
		
		// ȡ��ѡ���ı�����ʼλ�úͽ���λ��
		int start = editor.getSelectionStart();
		int end = editor.getSelectionEnd();

		// ���ѡ���ı�������ѡ���ı�����ʽ
		if (start != end) {
			StyledDocument doc = (StyledDocument) textPane.getDocument();
			// ����ѡ�ı�����Ϊ�µ���ʽ��replaceΪfalse��ʾ������ԭ�е���ʽ
			doc.setCharacterAttributes(start, end - start, attr, replace);
		}
	}
	
	private ItemListener fontSizeItemListener = new ItemListener() {
    	@Override
    	public void itemStateChanged(ItemEvent e) {
    		int fontSize = (Integer) RichTextToolBar.this.fontSizeComboBox.getSelectedItem();
    		fontSize = scaleUp(fontSize);
    		// ����setCharacterAttributes���������ı���ѡ���ı�������
    		MutableAttributeSet attr = new SimpleAttributeSet();
    		StyleConstants.setFontSize(attr, fontSize);
    		setCharacterAttributes(RichTextToolBar.this.textPane, attr, false);
    	}
    };
	
	private ItemListener fontNameItemListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
        	String fontName = (String) RichTextToolBar.this.fontNameComboBox.getSelectedItem();
			// ����setCharacterAttributes���������ı���ѡ���ı�������
			MutableAttributeSet attr = new SimpleAttributeSet();
			StyleConstants.setFontFamily(attr, fontName);
			setCharacterAttributes(RichTextToolBar.this.textPane, attr, false);
        }
    };
	
	private ActionListener formulaActionListener = new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
			final UIFormula formulaPane = FormulaFactory.createFormulaPane();
			formulaPane.populate(new Formula());
			formulaPane.showLargeWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {
				@Override
				public void doOk() {
					StyledDocument doc = (StyledDocument) textPane.getDocument();
					Formula fm = formulaPane.update();
					String content = RichTextConverter.asFormula(fm.getContent());
					int start = textPane.getSelectionStart();
                    AttributeSet attrs = start > 0 ? doc.getCharacterElement(start - 1).getAttributes() : new SimpleAttributeSet();
					try {
						doc.insertString(start, content, attrs);
					} catch (BadLocationException e) {
						FRContext.getLogger().error(e.getMessage());
					}
				}
			}).setVisible(true);
		}
	};
	
	private int roundUp(double num){
		String numStr = Double.toString(num);
		numStr = new BigDecimal(numStr).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
		return Integer.valueOf(numStr);
	}

	private CaretListener textCareListener = new CaretListener() {
		
		//����ѡ�в��ֵ�������ʽ, ����̬��ʾ�������ϰ�ť��״̬
		private void setSelectedCharStyle(int start, int end, StyledDocument doc){
			boolean isBold = true;
			boolean isItalic = true;
			boolean isUnderline = true;
			boolean isSubscript = true;
			boolean isSuperscript = true;
			String fontName_1st = null;
			int fontSize_1st = 0;
			Color fontColor_1st = null;
			
			for (int i = start; i < end; i++) {
				Element ele = doc.getCharacterElement(i);
				AttributeSet attrs = ele.getAttributes();
				
				//����
				isBold = isBold && StyleConstants.isBold(attrs);
				//б��
				isItalic = isItalic && StyleConstants.isItalic(attrs);
				//�»���
				isUnderline = isUnderline && StyleConstants.isUnderline(attrs);
				//�±�
				isSubscript = isSubscript && StyleConstants.isSubscript(attrs);
				//�ϱ�
				isSuperscript = isSuperscript && StyleConstants.isSuperscript(attrs);
				
				if(i == start){
					fontName_1st = (String) attrs.getAttribute(StyleConstants.FontFamily);  
					fontSize_1st = (Integer) attrs.getAttribute(StyleConstants.FontSize);  
					fontColor_1st = (Color) attrs.getAttribute(StyleConstants.Foreground);
					fontColor_1st = fontColor_1st == null ? Color.BLACK : fontColor_1st;
				}
			}

			setButtonSelected(isBold, isItalic, isUnderline, isSubscript, isSuperscript, 
					fontName_1st, fontSize_1st, fontColor_1st);
		}
		
		//��̬��ʾ�������ϰ�ť��״̬
		private void setButtonSelected(boolean isBold, boolean isItalic, boolean isUnderline, 
				boolean isSubscript, boolean isSuperscript, String fontName_1st, 
				int fontSize_1st, Color fontColor_1st){
			bold.setSelected(isBold);
			italic.setSelected(isItalic);
			underline.setSelected(isUnderline);
			subPane.setSelected(isSubscript);
			superPane.setSelected(isSuperscript);
			//Ϊʲô��������, ��С, ��ɫ, ����Ҫȥ�ж��Ƿ�ȫ��ͬ��
			//��Ϊ���ȫ��ͬ, ������Ϊ��һ���ַ�����ʽ, �����ȫ��ͬ, ��ôĬ��Ҳ���óɵ�һ���ַ�����ʽ.
			fontNameComboBox.setSelectedItem(fontName_1st);
			fontSizeComboBox.removeItemListener(fontSizeItemListener);
			fontSizeComboBox.setSelectedItem(scaleDown(fontSize_1st));
			fontSizeComboBox.addItemListener(fontSizeItemListener);
			selectColorPane(fontColor_1st);
		}
		
		private void selectColorPane(Color color){
			colorSelectPane.removeColorChangeListener(colorChangeAction);
			colorSelectPane.setColor(color);
			colorSelectPane.addColorChangeListener(colorChangeAction);
		}
	
		@Override
		public void caretUpdate(CaretEvent e) {
			StyledDocument doc = (StyledDocument) textPane.getDocument();
			
			// ȡ��ѡ���ı�����ʼλ�úͽ���λ��
			int start = textPane.getSelectionStart();
			int end = textPane.getSelectionEnd();
			
			//���û��ѡ���ַ�
			if(end == start){
				return;
			}
			
			setSelectedCharStyle(start, end, doc);
		}
	};
	
	//���õ�ǰ���λ��ʽ
	private MouseListener setMouseCurrentStyle = new MouseAdapter() {
		
		@Override
		public void mouseClicked(MouseEvent e) {
			StyledDocument doc = (StyledDocument) textPane.getDocument();
			
			// ȡ��ѡ���ı�����ʼλ�úͽ���λ��
			int start = textPane.getSelectionStart();
			int end = textPane.getSelectionEnd();
			
			if(start != end){
				return;
			}
			
			setToLastCharStyle(end, doc);
		}
		
		//���Ĭ�ϲ�ѡ�ַ�, ��ô����Ϊ���һ���ַ�����ʽ
		private void setToLastCharStyle(int end, StyledDocument doc){
			if(textPane.isUpdating()){
				return;
			}
			
			//ȡǰһ���ַ�����ʽ
			Element ele = doc.getCharacterElement(end - 1);
			AttributeSet attrs = ele.getAttributes();
			populateToolBar(attrs);
		}
	};
	
	/**
	 * ����ʽ�и��¹������ϵİ�ť״̬
	 * 
	 * @param attrs ��ʽ
	 * 
	 *
	 * @date 2015-1-5-����5:12:33
	 * 
	 */
	public void populateToolBar(AttributeSet attrs){
		int size = scaleDown(StyleConstants.getFontSize(attrs));
		fontNameComboBox.setSelectedItem(StyleConstants.getFontFamily(attrs));
		fontSizeComboBox.setSelectedItem(size);
		
		bold.setSelected(StyleConstants.isBold(attrs));
		italic.setSelected(StyleConstants.isItalic(attrs));
		underline.setSelected(StyleConstants.isUnderline(attrs));
		subPane.setSelected(StyleConstants.isSubscript(attrs));
		superPane.setSelected(StyleConstants.isSuperscript(attrs));
		Color foreGround = StyleConstants.getForeground(attrs);
		foreGround = foreGround == null ? Color.BLACK : foreGround;
		colorSelectPane.setColor(foreGround);
		colorSelectPane.repaint();
	}
	
	//ptתΪpx =*4/3
	private int scaleUp(int fontSize){
		return scale(fontSize, true);
	}
	
	//pxתpt = *3/4
	private int scaleDown(int fontSize){
		return scale(fontSize, false);
	}
	
	private int scale(int fontSize, boolean isUp){
		double dpi96 = Constants.FR_PAINT_RESOLUTION;
		double dpi72 = Constants.DEFAULT_FONT_PAINT_RESOLUTION;
		double scale = isUp ? (dpi96 / dpi72) : (dpi72 / dpi96);
		
		return roundUp(fontSize * scale);
	}
	
	private DocumentListener inputListener = new DocumentListener() {
		
		@Override
		public void removeUpdate(DocumentEvent e) {
		}
		
		@Override
		public void insertUpdate(DocumentEvent e) {
			//��־���ڸ�������
			textPane.startUpdating();
			final MutableAttributeSet attr = updateStyleFromToolBar();
			final int start = textPane.getSelectionStart();
			int end = textPane.getSelectionEnd();
			
			if (start != end) {
				textPane.finishUpdating();
				return;
			}

			//�ŵ�SwingWorker��, ����Ϊ��documentListener�ﲻ�ܶ�̬�ı�doc����
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					changeContentStyle(start, attr);
				}
			});
		}
		
		//����Style����ʾpopulate��ť
		private void changeContentStyle(int start, MutableAttributeSet attr){
			changeContentStyle(start, attr, 1);
		}
		
		private void changeContentStyle(int start, MutableAttributeSet attr, int contentLength){
			// ����ѡ�ı�����Ϊ�µ���ʽ��replaceΪfalse��ʾ������ԭ�е���ʽ
			StyledDocument doc = (StyledDocument) textPane.getDocument();
			doc.setCharacterAttributes(start, contentLength, attr, false);
			textPane.finishUpdating();
		}
		
		//�������ϵ����ø�ֵ��������ַ�
		private MutableAttributeSet updateStyleFromToolBar(){
			final boolean isBold = bold.isSelected();
			final boolean isItalic = italic.isSelected();
			final boolean isSub = subPane.isSelected();
			final boolean isSuper = superPane.isSelected();
			final boolean isUnderLine = underline.isSelected();
			final String fontName = (String) fontNameComboBox.getSelectedItem();
			final int fontSize = scaleUp((Integer) fontSizeComboBox.getSelectedItem());
			final Color foreGround = colorSelectPane.getColor() == null ? Color.BLACK : colorSelectPane.getColor();
			
			MutableAttributeSet attr = new SimpleAttributeSet();
			StyleConstants.setBold(attr, isBold);
			StyleConstants.setItalic(attr, isItalic);
			StyleConstants.setSubscript(attr, isSub);
			StyleConstants.setSuperscript(attr, isSuper);
			StyleConstants.setUnderline(attr, isUnderLine);
			StyleConstants.setForeground(attr, foreGround);
			StyleConstants.setFontFamily(attr, fontName);
			StyleConstants.setFontSize(attr, fontSize);
			
			return attr;
		}

		private static final int NOT_INITED = -1;
		private static final int UPDATING = -2;
		//��¼��һ������ɹ�����㶨λ, ��Ϊ�п����ı������м�����
		private int inputStart = NOT_INITED;
		private static final int JDK_6 = 6;
		
		@Override
		public void changedUpdate(DocumentEvent e) {
			//�����Ҫע��, jdk1.6��1.7�������뷨�Ĵ����߼���һ��, jdk6ʱֱ�������뷨������һ�������
			//����һ����insert��ȥֱ�Ӵ���inserupdate�¼�, ��jdk7��ֱ�Ӱ����е�������.
			//inserupdate�Ǳ߰󶨵���һ����������¼�, ���һ�����ķ���
			if(StableUtils.getMajorJavaVersion() > JDK_6){
				if(isUpdating()){
					return;
				}
				StyledDocument doc = (StyledDocument) textPane.getDocument();
				final String content;
				initFlag(doc);
				
				final int start = textPane.getSelectionStart();
				final int inputLen = start - inputStart;
				//�����������
				try {
					content = doc.getText(inputStart, inputLen);
				} catch (BadLocationException e1) {
					return;
				}
				
				//�������뷨, Ĭ�������ַ��ᱻ���뷨�Ŀ��ȡס, jtextpane�õ���һ���ո�, ��ʱ��������
				if(StringUtils.isBlank(content) || inputLen <= 0){
					return;
				}
				//����һ�������������ֵ���ʽ
				setContentStyle(inputLen);
			}
		}
		
		private void setContentStyle(final int inputLen){
			//������Start, ����Ҫ����������ʽ
			final int _start = inputStart;
			final MutableAttributeSet attr = updateStyleFromToolBar();

			//�ŵ�SwingWorker��, ����Ϊ��documentListener�ﲻ�ܶ�̬�ı�doc����
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					//��ֹ������ѭ��change�¼�
					startUpdating();
					//Start-1 ����Ϊ�������뷨���ÿո�ռ1λ
					changeContentStyle(_start, attr, inputLen);
					resetFlag();
				}
			});
		}
		
		private boolean isUpdating(){
			return inputStart == UPDATING;
		}
		
		private void startUpdating(){
			inputStart = UPDATING;
		}
		
		//��ʼ���״̬, ���ڼ�¼�������뷨����ַ�ͬʱ���������
		private void initFlag(StyledDocument doc){
			if(inputStart != NOT_INITED){
				return;
			}
			inputStart = textPane.getSelectionStart() - 1;
		}
		
		//���ñ��״̬
		private void resetFlag(){
			inputStart = NOT_INITED;
		}
	};

}
