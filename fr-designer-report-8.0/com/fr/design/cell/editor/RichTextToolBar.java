// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.cell.editor;

import com.fr.base.*;
import com.fr.design.dialog.*;
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
import com.fr.general.*;
import com.fr.report.cell.cellattr.core.RichTextConverter;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import javax.swing.JEditorPane;
import javax.swing.SwingUtilities;
import javax.swing.event.*;
import javax.swing.text.*;

public class RichTextToolBar extends BasicPane
{

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
    private RichTextEditingPane textPane;
    private ActionListener blodChangeAction = new ActionListener() {

        final RichTextToolBar this$0;

        public void actionPerformed(ActionEvent actionevent)
        {
            boolean flag = bold.isSelected();
            SimpleAttributeSet simpleattributeset = new SimpleAttributeSet();
            StyleConstants.setBold(simpleattributeset, !flag);
            setCharacterAttributes(textPane, simpleattributeset, false);
        }

            
            {
                this$0 = RichTextToolBar.this;
                super();
            }
    }
;
    private ActionListener itaChangeAction = new ActionListener() {

        final RichTextToolBar this$0;

        public void actionPerformed(ActionEvent actionevent)
        {
            boolean flag = italic.isSelected();
            SimpleAttributeSet simpleattributeset = new SimpleAttributeSet();
            StyleConstants.setItalic(simpleattributeset, !flag);
            setCharacterAttributes(textPane, simpleattributeset, false);
        }

            
            {
                this$0 = RichTextToolBar.this;
                super();
            }
    }
;
    private ActionListener underlineChangeAction = new ActionListener() {

        final RichTextToolBar this$0;

        public void actionPerformed(ActionEvent actionevent)
        {
            boolean flag = underline.isSelected();
            SimpleAttributeSet simpleattributeset = new SimpleAttributeSet();
            StyleConstants.setUnderline(simpleattributeset, !flag);
            setCharacterAttributes(textPane, simpleattributeset, false);
        }

            
            {
                this$0 = RichTextToolBar.this;
                super();
            }
    }
;
    private ActionListener subChangeAction = new ActionListener() {

        final RichTextToolBar this$0;

        public void actionPerformed(ActionEvent actionevent)
        {
            boolean flag = subPane.isSelected();
            SimpleAttributeSet simpleattributeset = new SimpleAttributeSet();
            StyleConstants.setSubscript(simpleattributeset, !flag);
            setCharacterAttributes(textPane, simpleattributeset, false);
        }

            
            {
                this$0 = RichTextToolBar.this;
                super();
            }
    }
;
    private ActionListener superChangeAction = new ActionListener() {

        final RichTextToolBar this$0;

        public void actionPerformed(ActionEvent actionevent)
        {
            boolean flag = superPane.isSelected();
            SimpleAttributeSet simpleattributeset = new SimpleAttributeSet();
            StyleConstants.setSuperscript(simpleattributeset, !flag);
            setCharacterAttributes(textPane, simpleattributeset, false);
        }

            
            {
                this$0 = RichTextToolBar.this;
                super();
            }
    }
;
    private ChangeListener colorChangeAction = new ChangeListener() {

        final RichTextToolBar this$0;

        public void stateChanged(ChangeEvent changeevent)
        {
            Color color = colorSelectPane.getColor();
            color = color != null ? color : Color.BLACK;
            SimpleAttributeSet simpleattributeset = new SimpleAttributeSet();
            StyleConstants.setForeground(simpleattributeset, color);
            setCharacterAttributes(textPane, simpleattributeset, false);
        }

            
            {
                this$0 = RichTextToolBar.this;
                super();
            }
    }
;
    private ItemListener fontSizeItemListener = new ItemListener() {

        final RichTextToolBar this$0;

        public void itemStateChanged(ItemEvent itemevent)
        {
            int i = ((Integer)fontSizeComboBox.getSelectedItem()).intValue();
            i = scaleUp(i);
            SimpleAttributeSet simpleattributeset = new SimpleAttributeSet();
            StyleConstants.setFontSize(simpleattributeset, i);
            setCharacterAttributes(textPane, simpleattributeset, false);
        }

            
            {
                this$0 = RichTextToolBar.this;
                super();
            }
    }
;
    private ItemListener fontNameItemListener = new ItemListener() {

        final RichTextToolBar this$0;

        public void itemStateChanged(ItemEvent itemevent)
        {
            String s = (String)fontNameComboBox.getSelectedItem();
            SimpleAttributeSet simpleattributeset = new SimpleAttributeSet();
            StyleConstants.setFontFamily(simpleattributeset, s);
            setCharacterAttributes(textPane, simpleattributeset, false);
        }

            
            {
                this$0 = RichTextToolBar.this;
                super();
            }
    }
;
    private ActionListener formulaActionListener = new ActionListener() {

        final RichTextToolBar this$0;

        public void actionPerformed(ActionEvent actionevent)
        {
            final UIFormula formulaPane = FormulaFactory.createFormulaPane();
            formulaPane.populate(new Formula());
            formulaPane.showLargeWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {

                final UIFormula val$formulaPane;
                final _cls9 this$1;

                public void doOk()
                {
                    StyledDocument styleddocument = (StyledDocument)textPane.getDocument();
                    Formula formula = formulaPane.update();
                    String s = RichTextConverter.asFormula(formula.getContent());
                    int i = textPane.getSelectionStart();
                    Object obj = i <= 0 ? ((Object) (new SimpleAttributeSet())) : ((Object) (styleddocument.getCharacterElement(i - 1).getAttributes()));
                    try
                    {
                        styleddocument.insertString(i, s, ((AttributeSet) (obj)));
                    }
                    catch(BadLocationException badlocationexception)
                    {
                        FRContext.getLogger().error(badlocationexception.getMessage());
                    }
                }

                    
                    {
                        this$1 = _cls9.this;
                        formulaPane = uiformula;
                        super();
                    }
            }
).setVisible(true);
        }

            
            {
                this$0 = RichTextToolBar.this;
                super();
            }
    }
;
    private CaretListener textCareListener = new CaretListener() {

        final RichTextToolBar this$0;

        private void setSelectedCharStyle(int i, int j, StyledDocument styleddocument)
        {
            boolean flag = true;
            boolean flag1 = true;
            boolean flag2 = true;
            boolean flag3 = true;
            boolean flag4 = true;
            String s = null;
            int k = 0;
            Color color = null;
            for(int l = i; l < j; l++)
            {
                Element element = styleddocument.getCharacterElement(l);
                AttributeSet attributeset = element.getAttributes();
                flag = flag && StyleConstants.isBold(attributeset);
                flag1 = flag1 && StyleConstants.isItalic(attributeset);
                flag2 = flag2 && StyleConstants.isUnderline(attributeset);
                flag3 = flag3 && StyleConstants.isSubscript(attributeset);
                flag4 = flag4 && StyleConstants.isSuperscript(attributeset);
                if(l == i)
                {
                    s = (String)attributeset.getAttribute(StyleConstants.FontFamily);
                    k = ((Integer)attributeset.getAttribute(StyleConstants.FontSize)).intValue();
                    color = (Color)attributeset.getAttribute(StyleConstants.Foreground);
                    color = color != null ? color : Color.BLACK;
                }
            }

            setButtonSelected(flag, flag1, flag2, flag3, flag4, s, k, color);
        }

        private void setButtonSelected(boolean flag, boolean flag1, boolean flag2, boolean flag3, boolean flag4, String s, int i, 
                Color color)
        {
            bold.setSelected(flag);
            italic.setSelected(flag1);
            underline.setSelected(flag2);
            subPane.setSelected(flag3);
            superPane.setSelected(flag4);
            fontNameComboBox.setSelectedItem(s);
            fontSizeComboBox.removeItemListener(fontSizeItemListener);
            fontSizeComboBox.setSelectedItem(Integer.valueOf(scaleDown(i)));
            fontSizeComboBox.addItemListener(fontSizeItemListener);
            selectColorPane(color);
        }

        private void selectColorPane(Color color)
        {
            colorSelectPane.removeColorChangeListener(colorChangeAction);
            colorSelectPane.setColor(color);
            colorSelectPane.addColorChangeListener(colorChangeAction);
        }

        public void caretUpdate(CaretEvent caretevent)
        {
            StyledDocument styleddocument = (StyledDocument)textPane.getDocument();
            int i = textPane.getSelectionStart();
            int j = textPane.getSelectionEnd();
            if(j == i)
            {
                return;
            } else
            {
                setSelectedCharStyle(i, j, styleddocument);
                return;
            }
        }

            
            {
                this$0 = RichTextToolBar.this;
                super();
            }
    }
;
    private MouseListener setMouseCurrentStyle = new MouseAdapter() {

        final RichTextToolBar this$0;

        public void mouseClicked(MouseEvent mouseevent)
        {
            StyledDocument styleddocument = (StyledDocument)textPane.getDocument();
            int i = textPane.getSelectionStart();
            int j = textPane.getSelectionEnd();
            if(i != j)
            {
                return;
            } else
            {
                setToLastCharStyle(j, styleddocument);
                return;
            }
        }

        private void setToLastCharStyle(int i, StyledDocument styleddocument)
        {
            if(textPane.isUpdating())
            {
                return;
            } else
            {
                Element element = styleddocument.getCharacterElement(i - 1);
                AttributeSet attributeset = element.getAttributes();
                populateToolBar(attributeset);
                return;
            }
        }

            
            {
                this$0 = RichTextToolBar.this;
                super();
            }
    }
;
    private DocumentListener inputListener = new DocumentListener() {

        private static final int NOT_INITED = -1;
        private static final int UPDATING = -2;
        private int inputStart;
        private static final int JDK_6 = 6;
        private static final int JDK_7 = 7;
        final RichTextToolBar this$0;

        public void removeUpdate(DocumentEvent documentevent)
        {
        }

        public void insertUpdate(DocumentEvent documentevent)
        {
            textPane.startUpdating();
            final MutableAttributeSet attr = updateStyleFromToolBar();
            final int start = textPane.getSelectionStart();
            int i = textPane.getSelectionEnd();
            if(start != i)
            {
                textPane.finishUpdating();
                return;
            } else
            {
                SwingUtilities.invokeLater(new Runnable() {

                    final int val$start;
                    final MutableAttributeSet val$attr;
                    final _cls12 this$1;

                    public void run()
                    {
                        changeContentStyle(start, attr);
                    }

                    
                    {
                        this$1 = _cls12.this;
                        start = i;
                        attr = mutableattributeset;
                        super();
                    }
                }
);
                return;
            }
        }

        private void changeContentStyle(int i, MutableAttributeSet mutableattributeset)
        {
            changeContentStyle(i, mutableattributeset, 1);
        }

        private void changeContentStyle(int i, MutableAttributeSet mutableattributeset, int j)
        {
            StyledDocument styleddocument = (StyledDocument)textPane.getDocument();
            styleddocument.setCharacterAttributes(i, j, mutableattributeset, false);
            textPane.finishUpdating();
        }

        private MutableAttributeSet updateStyleFromToolBar()
        {
            boolean flag = bold.isSelected();
            boolean flag1 = italic.isSelected();
            boolean flag2 = subPane.isSelected();
            boolean flag3 = superPane.isSelected();
            boolean flag4 = underline.isSelected();
            String s = (String)fontNameComboBox.getSelectedItem();
            int i = scaleUp(((Integer)fontSizeComboBox.getSelectedItem()).intValue());
            Color color = colorSelectPane.getColor() != null ? colorSelectPane.getColor() : Color.BLACK;
            SimpleAttributeSet simpleattributeset = new SimpleAttributeSet();
            StyleConstants.setBold(simpleattributeset, flag);
            StyleConstants.setItalic(simpleattributeset, flag1);
            StyleConstants.setSubscript(simpleattributeset, flag2);
            StyleConstants.setSuperscript(simpleattributeset, flag3);
            StyleConstants.setUnderline(simpleattributeset, flag4);
            StyleConstants.setForeground(simpleattributeset, color);
            StyleConstants.setFontFamily(simpleattributeset, s);
            StyleConstants.setFontSize(simpleattributeset, i);
            return simpleattributeset;
        }

        public void changedUpdate(DocumentEvent documentevent)
        {
            if(StableUtils.getMajorJavaVersion() == 7)
            {
                if(isUpdating())
                    return;
                StyledDocument styleddocument = (StyledDocument)textPane.getDocument();
                initFlag(styleddocument);
                int i = textPane.getSelectionStart();
                int j = i - inputStart;
                String s;
                try
                {
                    s = styleddocument.getText(inputStart, j);
                }
                catch(BadLocationException badlocationexception)
                {
                    return;
                }
                if(StringUtils.isBlank(s) || j <= 0)
                    return;
                setContentStyle(j);
            }
        }

        private void setContentStyle(final int inputLen)
        {
            final int _start = inputStart;
            final MutableAttributeSet attr = updateStyleFromToolBar();
            SwingUtilities.invokeLater(new Runnable() {

                final int val$_start;
                final MutableAttributeSet val$attr;
                final int val$inputLen;
                final _cls12 this$1;

                public void run()
                {
                    startUpdating();
                    changeContentStyle(_start, attr, inputLen);
                    resetFlag();
                }

                    
                    {
                        this$1 = _cls12.this;
                        _start = i;
                        attr = mutableattributeset;
                        inputLen = j;
                        super();
                    }
            }
);
        }

        private boolean isUpdating()
        {
            return inputStart == -2;
        }

        private void startUpdating()
        {
            inputStart = -2;
        }

        private void initFlag(StyledDocument styleddocument)
        {
            if(inputStart != -1)
            {
                return;
            } else
            {
                inputStart = textPane.getSelectionStart() - 1;
                return;
            }
        }

        private void resetFlag()
        {
            inputStart = -1;
        }





            
            {
                this$0 = RichTextToolBar.this;
                super();
                inputStart = -1;
            }
    }
;

    public RichTextToolBar()
    {
        initComponents();
    }

    public RichTextToolBar(RichTextEditingPane richtexteditingpane)
    {
        textPane = richtexteditingpane;
        initComponents();
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("FR-Designer_font");
    }

    protected void initComponents()
    {
        initAllButton();
        addToToolBar();
    }

    private void initAllButton()
    {
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
        initAllNames();
        setToolTips();
        setAllButtonStyle();
        bindListener();
    }

    private void setAllButtonStyle()
    {
        setButtonStyle(bold);
        setButtonStyle(italic);
        setButtonStyle(underline);
        setButtonStyle(subPane);
        setButtonStyle(superPane);
        setButtonStyle(formulaPane);
    }

    private void setButtonStyle(UIButton uibutton)
    {
        uibutton.setNormalPainted(false);
        uibutton.setBackground(null);
        uibutton.setOpaque(false);
        uibutton.setPreferredSize(BUTTON_SIZE);
        uibutton.setBorderPaintedOnlyWhenPressed(true);
    }

    private void addToToolBar()
    {
        setLayout(new FlowLayout(0));
        add(fontNameComboBox);
        add(fontSizeComboBox);
        add(bold);
        add(italic);
        add(underline);
        add(colorSelectPane);
        add(superPane);
        add(subPane);
        add(formulaPane);
    }

    private void bindListener()
    {
        FRFont frfont = RichTextPane.DEFAUL_FONT;
        fontNameComboBox.addItemListener(fontNameItemListener);
        fontNameComboBox.setSelectedItem(frfont.getFontName());
        fontSizeComboBox.addItemListener(fontSizeItemListener);
        fontSizeComboBox.setSelectedItem(Integer.valueOf(scaleDown(frfont.getSize())));
        bold.addActionListener(blodChangeAction);
        italic.addActionListener(itaChangeAction);
        underline.addActionListener(underlineChangeAction);
        subPane.addActionListener(subChangeAction);
        superPane.addActionListener(superChangeAction);
        colorSelectPane.addColorChangeListener(colorChangeAction);
        formulaPane.addActionListener(formulaActionListener);
        textPane.addCaretListener(textCareListener);
        textPane.addMouseListener(setMouseCurrentStyle);
        textPane.getDocument().addDocumentListener(inputListener);
    }

    private void initAllNames()
    {
        fontNameComboBox.setGlobalName(Inter.getLocText("FR-Designer_Font-Family"));
        fontSizeComboBox.setGlobalName(Inter.getLocText("FR-Designer_Font-Size"));
        italic.setGlobalName(Inter.getLocText("FR-Designer_italic"));
        bold.setGlobalName(Inter.getLocText("FR-Designer_bold"));
        underline.setGlobalName(Inter.getLocText("FR-Designer_Underline"));
        superPane.setGlobalName(Inter.getLocText("FR-Designer_Superscript"));
        subPane.setGlobalName(Inter.getLocText("FR-Designer_Subscript"));
    }

    private void setToolTips()
    {
        colorSelectPane.setToolTipText(Inter.getLocText("FR-Designer_Foreground"));
        italic.setToolTipText(Inter.getLocText("FR-Designer_italic"));
        bold.setToolTipText(Inter.getLocText("FR-Designer_bold"));
        underline.setToolTipText(Inter.getLocText("FR-Designer_Underline"));
        superPane.setToolTipText(Inter.getLocText("FR-Designer_Superscript"));
        subPane.setToolTipText(Inter.getLocText("FR-Designer_Subscript"));
        formulaPane.setToolTipText(Inter.getLocText("FR-Designer_Formula"));
    }

    public void removeInputListener()
    {
        textPane.getDocument().removeDocumentListener(inputListener);
    }

    public void addInputListener()
    {
        textPane.getDocument().addDocumentListener(inputListener);
    }

    private void setCharacterAttributes(JEditorPane jeditorpane, AttributeSet attributeset, boolean flag)
    {
        textPane.requestFocus();
        int i = jeditorpane.getSelectionStart();
        int j = jeditorpane.getSelectionEnd();
        if(i != j)
        {
            StyledDocument styleddocument = (StyledDocument)textPane.getDocument();
            styleddocument.setCharacterAttributes(i, j - i, attributeset, flag);
        }
    }

    private int roundUp(double d)
    {
        String s = Double.toString(d);
        s = (new BigDecimal(s)).setScale(0, 4).toString();
        return Integer.valueOf(s).intValue();
    }

    public void populateToolBar(AttributeSet attributeset)
    {
        int i = scaleDown(StyleConstants.getFontSize(attributeset));
        fontNameComboBox.setSelectedItem(StyleConstants.getFontFamily(attributeset));
        fontSizeComboBox.setSelectedItem(Integer.valueOf(i));
        bold.setSelected(StyleConstants.isBold(attributeset));
        italic.setSelected(StyleConstants.isItalic(attributeset));
        underline.setSelected(StyleConstants.isUnderline(attributeset));
        subPane.setSelected(StyleConstants.isSubscript(attributeset));
        superPane.setSelected(StyleConstants.isSuperscript(attributeset));
        Color color = StyleConstants.getForeground(attributeset);
        color = color != null ? color : Color.BLACK;
        colorSelectPane.setColor(color);
        colorSelectPane.repaint();
    }

    private int scaleUp(int i)
    {
        return scale(i, true);
    }

    private int scaleDown(int i)
    {
        return scale(i, false);
    }

    private int scale(int i, boolean flag)
    {
        double d = 96D;
        double d1 = 72D;
        double d2 = flag ? d / d1 : d1 / d;
        return roundUp((double)i * d2);
    }















}
