// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.gui.xpane;

import com.fr.base.*;
import com.fr.base.background.GradientBackground;
import com.fr.design.border.UIRoundedBorder;
import com.fr.design.constants.UIConstants;
import com.fr.design.dialog.BasicPane;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.gui.frpane.UINumberDragPane;
import com.fr.design.gui.ibutton.*;
import com.fr.design.gui.icombobox.LineComboBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.gui.style.*;
import com.fr.design.layout.*;
import com.fr.design.mainframe.JForm;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.form.ui.LayoutBorderStyle;
import com.fr.form.ui.WidgetTitle;
import com.fr.general.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.*;
import javax.swing.plaf.basic.BasicToggleButtonUI;
import javax.swing.text.Document;

// Referenced classes of package com.fr.design.gui.xpane:
//            LayoutBorderPreviewPane

public class LayoutBorderPane extends BasicPane
{
    private class BorderButtonIcon
        implements Icon
    {

        private int display;
        final LayoutBorderPane this$0;

        public int getIconWidth()
        {
            return iconWidth;
        }

        public int getIconHeight()
        {
            return iconWidth;
        }

        public void paintIcon(Component component, Graphics g, int i, int j)
        {
            if(display == 1)
            {
                g.setColor(Color.black);
                g.drawRect(3, 3, getIconWidth() - 7, getIconHeight() - 7);
            } else
            if(display == 2)
            {
                g.setColor(Color.black);
                g.drawRoundRect(3, 3, getIconWidth() - 7, getIconHeight() - 7, 6, 6);
            }
        }

        private BorderButtonIcon(int i)
        {
            this$0 = LayoutBorderPane.this;
            super();
            display = i;
        }

    }

    private class BorderToggleButtonUI extends BasicToggleButtonUI
    {

        final LayoutBorderPane this$0;

        public void paint(Graphics g, JComponent jcomponent)
        {
            paintBackground(g, (AbstractButton)jcomponent);
            super.paint(g, jcomponent);
        }

        private void paintBackground(Graphics g, AbstractButton abstractbutton)
        {
            if(abstractbutton.isContentAreaFilled())
            {
                Dimension dimension = abstractbutton.getSize();
                GradientBackground gradientbackground = new GradientBackground(new Color(247, 247, 247), new Color(228, 228, 228), 1);
                gradientbackground.paint(g, new java.awt.geom.RoundRectangle2D.Double(2D, 2D, dimension.width - 4, dimension.height - 4, 5D, 5D));
            }
        }

        private void paintBorder(Graphics g, Color color, int i, int j)
        {
            Graphics2D graphics2d = (Graphics2D)g;
            graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2d.setColor(color);
            graphics2d.drawRoundRect(0, 0, i - 1, j - 1, 5, 5);
            graphics2d.setColor(Color.white);
            graphics2d.drawRoundRect(1, 1, i - 3, j - 3, 5, 5);
            graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        }

        protected void paintButtonPressed(Graphics g, AbstractButton abstractbutton)
        {
            Dimension dimension = abstractbutton.getSize();
            paintBorder(g, new Color(78, 143, 203), dimension.height, dimension.width);
        }

        private BorderToggleButtonUI()
        {
            this$0 = LayoutBorderPane.this;
            super();
        }

    }

    private class BorderButton extends JToggleButton
    {

        final LayoutBorderPane this$0;

        public Border getBorder()
        {
            ButtonModel buttonmodel = getModel();
            if(isSelected())
                return null;
            if(isRolloverEnabled() && buttonmodel.isRollover())
                return new UIRoundedBorder(new Color(148, 148, 148), 1, 5);
            else
                return super.getBorder();
        }

        public void updateUI()
        {
            setUI(new BorderToggleButtonUI());
        }

        private void addBorderActionListener(final int border)
        {
            addActionListener(new ActionListener() {

                final int val$border;
                final BorderButton this$1;

                public void actionPerformed(ActionEvent actionevent)
                {
                    if(border == 0)
                    {
                        borderStyle.reset();
                    } else
                    {
                        borderStyle.setColor(currentLineColorPane.getColor());
                        borderStyle.setBorder(currentLineCombo.getSelectedLineStyle());
                        borderStyle.setCorner(border != 1);
                    }
                    layoutBorderPreviewPane.repaint();
                }

                
                {
                    this$1 = BorderButton.this;
                    border = i;
                    super();
                }
            }
);
        }

        private BorderButton(int i)
        {
            this$0 = LayoutBorderPane.this;
            super();
            setIcon(new BorderButtonIcon(i));
            addBorderActionListener(i);
            setPreferredSize(new Dimension(32, 32));
            setBorder(new UIRoundedBorder(new Color(220, 220, 220), 1, 5));
            setRolloverEnabled(true);
        }

    }

    protected class VerButtonPane extends JPanel
    {

        JToggleButton noBorder;
        BorderButton normalBorder;
        BorderButton RoundedBorder;
        ButtonGroup group;
        final LayoutBorderPane this$0;

        public void populate(LayoutBorderStyle layoutborderstyle)
        {
            if(layoutborderstyle.getBorder() == 0)
                group.setSelected(noBorder.getModel(), true);
            else
            if(layoutborderstyle.isCorner())
                group.setSelected(RoundedBorder.getModel(), true);
            else
                group.setSelected(normalBorder.getModel(), true);
        }

        private VerButtonPane()
        {
            this$0 = LayoutBorderPane.this;
            super();
            setLayout(new FlowLayout(1));
            setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
            group = new ButtonGroup();
            noBorder = new BorderButton(0);
            normalBorder = new BorderButton(1);
            RoundedBorder = new BorderButton(2);
            group.add(noBorder);
            group.add(normalBorder);
            group.add(RoundedBorder);
        }
    }


    private static final int NO_BORDERS = 0;
    private static final int RIGHTANGLE_BORDERS = 1;
    private static final int ROUNDED_BORDERS = 2;
    private static final int MAX_WIDTH = 220;
    private LayoutBorderStyle borderStyle;
    private LayoutBorderPreviewPane layoutBorderPreviewPane;
    private UIComboBox borderTypeCombo;
    private UIComboBox borderStyleCombo;
    private LineComboBox currentLineCombo;
    private UIColorButton currentLineColorPane;
    private BackgroundSpecialPane backgroundPane;
    private UINumberDragPane numberDragPane;
    private TinyFormulaPane formulaPane;
    private UIComboBox fontNameComboBox;
    private UIComboBox fontSizeComboBox;
    private UIColorButton colorSelectPane;
    private UIToggleButton bold;
    private UIToggleButton italic;
    private UIToggleButton underline;
    private LineComboBox underlineCombo;
    private UIButtonGroup hAlignmentPane;
    private BackgroundNoImagePane titleBackgroundPane;
    private UIScrollPane titlePane;
    private int minNumber;
    private double maxNumber;
    private int iconWidth;
    public static final int BORDER_LINE_STYLE_ARRAY[] = {
        0, 1, 2, 5
    };
    public static final String BORDER_TYPE[] = {
        Inter.getLocText("FR-Designer-Widget-Style_Standard"), Inter.getLocText("FR-Designer-Widget-Style_Title")
    };
    public static final String BORDER_STYLE[] = {
        Inter.getLocText("FR-Designer-Widget-Style_Common"), Inter.getLocText("FR-Designer-Widget-Style_Shadow")
    };
    private static final Dimension BUTTON_SIZE = new Dimension(24, 20);

    public LayoutBorderStyle getBorderStyle()
    {
        return borderStyle;
    }

    public void setBorderStyle(LayoutBorderStyle layoutborderstyle)
    {
        borderStyle = layoutborderstyle;
    }

    public LayoutBorderPreviewPane getLayoutBorderPreviewPane()
    {
        return layoutBorderPreviewPane;
    }

    public void setLayoutBorderPreviewPane(LayoutBorderPreviewPane layoutborderpreviewpane)
    {
        layoutBorderPreviewPane = layoutborderpreviewpane;
    }

    public UIComboBox getBorderTypeCombo()
    {
        return borderTypeCombo;
    }

    public void setBorderTypeCombo(UIComboBox uicombobox)
    {
        borderTypeCombo = uicombobox;
    }

    public UIComboBox getBorderStyleCombo()
    {
        return borderStyleCombo;
    }

    public void setBorderStyleCombo(UIComboBox uicombobox)
    {
        borderStyleCombo = uicombobox;
    }

    public LineComboBox getCurrentLineCombo()
    {
        return currentLineCombo;
    }

    public void setCurrentLineCombo(LineComboBox linecombobox)
    {
        currentLineCombo = linecombobox;
    }

    public UIColorButton getCurrentLineColorPane()
    {
        return currentLineColorPane;
    }

    public void setCurrentLineColorPane(UIColorButton uicolorbutton)
    {
        currentLineColorPane = uicolorbutton;
    }

    public BackgroundSpecialPane getBackgroundPane()
    {
        return backgroundPane;
    }

    public void setBackgroundPane(BackgroundSpecialPane backgroundspecialpane)
    {
        backgroundPane = backgroundspecialpane;
    }

    public UINumberDragPane getNumberDragPane()
    {
        return numberDragPane;
    }

    public void setNumberDragPane(UINumberDragPane uinumberdragpane)
    {
        numberDragPane = uinumberdragpane;
    }

    public TinyFormulaPane getFormulaPane()
    {
        return formulaPane;
    }

    public void setFormulaPane(TinyFormulaPane tinyformulapane)
    {
        formulaPane = tinyformulapane;
    }

    public UIComboBox getFontNameComboBox()
    {
        return fontNameComboBox;
    }

    public void setFontNameComboBox(UIComboBox uicombobox)
    {
        fontNameComboBox = uicombobox;
    }

    public UIComboBox getFontSizeComboBox()
    {
        return fontSizeComboBox;
    }

    public void setFontSizeComboBox(UIComboBox uicombobox)
    {
        fontSizeComboBox = uicombobox;
    }

    public UIColorButton getColorSelectPane()
    {
        return colorSelectPane;
    }

    public void setColorSelectPane(UIColorButton uicolorbutton)
    {
        colorSelectPane = uicolorbutton;
    }

    public UIToggleButton getBold()
    {
        return bold;
    }

    public void setBold(UIToggleButton uitogglebutton)
    {
        bold = uitogglebutton;
    }

    public UIToggleButton getItalic()
    {
        return italic;
    }

    public void setItalic(UIToggleButton uitogglebutton)
    {
        italic = uitogglebutton;
    }

    public UIToggleButton getUnderline()
    {
        return underline;
    }

    public void setUnderline(UIToggleButton uitogglebutton)
    {
        underline = uitogglebutton;
    }

    public LineComboBox getUnderlineCombo()
    {
        return underlineCombo;
    }

    public void setUnderlineCombo(LineComboBox linecombobox)
    {
        underlineCombo = linecombobox;
    }

    public UIButtonGroup gethAlignmentPane()
    {
        return hAlignmentPane;
    }

    public void sethAlignmentPane(UIButtonGroup uibuttongroup)
    {
        hAlignmentPane = uibuttongroup;
    }

    public BackgroundNoImagePane getTitleBackgroundPane()
    {
        return titleBackgroundPane;
    }

    public void setTitleBackgroundPane(BackgroundNoImagePane backgroundnoimagepane)
    {
        titleBackgroundPane = backgroundnoimagepane;
    }

    public UIScrollPane getTitlePane()
    {
        return titlePane;
    }

    public void setTitlePane(UIScrollPane uiscrollpane)
    {
        titlePane = uiscrollpane;
    }

    public int getMinNumber()
    {
        return minNumber;
    }

    public void setMinNumber(int i)
    {
        minNumber = i;
    }

    public double getMaxNumber()
    {
        return maxNumber;
    }

    public void setMaxNumber(double d)
    {
        maxNumber = d;
    }

    public int getIconWidth()
    {
        return iconWidth;
    }

    public void setIconWidth(int i)
    {
        iconWidth = i;
    }

    public LayoutBorderPane()
    {
        borderStyle = new LayoutBorderStyle();
        minNumber = 0;
        maxNumber = 100D;
        iconWidth = 32;
        initComponents();
    }

    protected void initComponents()
    {
        setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel jpanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        add(jpanel, "Center");
        JPanel jpanel1 = FRGUIPaneFactory.createBorderLayout_S_Pane();
        jpanel.add(jpanel1, "Center");
        jpanel1.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("FR-Designer-Widget-Style_Preview"), null));
        JPanel jpanel2 = FRGUIPaneFactory.createBorderLayout_S_Pane();
        jpanel1.add(jpanel2, "Center");
        jpanel2.setBorder(BorderFactory.createEmptyBorder(10, 4, 10, 4));
        layoutBorderPreviewPane = new LayoutBorderPreviewPane(borderStyle);
        jpanel2.add(layoutBorderPreviewPane, "Center");
        JPanel jpanel3 = FRGUIPaneFactory.createBorderLayout_S_Pane();
        jpanel.add(jpanel3, "East");
        jpanel3.add(initRightBottomPane(), "Center");
        JTemplate jtemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
        if(!jtemplate.isJWorkBook() && ((JForm)jtemplate).isSelectRootPane())
            jpanel3.add(initBodyRightTopPane(), "North");
        else
            jpanel3.add(initRightTopPane(), "North");
    }

    protected UIScrollPane initRightTopPane()
    {
        borderTypeCombo = new UIComboBox(BORDER_TYPE);
        borderTypeCombo.addItemListener(new ItemListener() {

            final LayoutBorderPane this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                if(borderTypeCombo.getSelectedIndex() == 0)
                {
                    titlePane.setVisible(false);
                } else
                {
                    titlePane.setVisible(true);
                    currentLineCombo.setSelectedItem(Integer.valueOf(1));
                }
            }

            
            {
                this$0 = LayoutBorderPane.this;
                super();
            }
        }
);
        borderStyleCombo = new UIComboBox(BORDER_STYLE);
        currentLineCombo = new LineComboBox(BORDER_LINE_STYLE_ARRAY);
        currentLineColorPane = new UIColorButton(null);
        currentLineColorPane.setUI(getButtonUI(currentLineColorPane));
        currentLineColorPane.set4ToolbarButton();
        currentLineColorPane.setPreferredSize(new Dimension(20, 20));
        JPanel jpanel = new JPanel(new BorderLayout());
        jpanel.add(currentLineColorPane, "West");
        backgroundPane = new BackgroundSpecialPane();
        JPanel jpanel1 = FRGUIPaneFactory.createBorderLayout_S_Pane();
        numberDragPane = new UINumberDragPane(0.0D, 100D);
        jpanel1.add(numberDragPane, "Center");
        jpanel1.add(new UILabel(" %"), "East");
        double d = -2D;
        double ad[] = {
            d, d, d, d, d, d, d
        };
        double ad1[] = {
            d, 220D
        };
        JPanel jpanel2 = TableLayoutHelper.createCommonTableLayoutPane(new JComponent[][] {
            new JComponent[] {
                new UILabel(Inter.getLocText("FR-Designer-Widget-Style_Frame_Style")), borderTypeCombo
            }, new JComponent[] {
                new UILabel(Inter.getLocText("FR-Designer-Widget-Style_Render_Style")), borderStyleCombo
            }, new JComponent[] {
                new UILabel(Inter.getLocText("FR-Designer-Widget-Style_Border_Line")), currentLineCombo
            }, new JComponent[] {
                new UILabel(Inter.getLocText("FR-Designer-Widget-Style_Border_Color")), jpanel
            }, new JComponent[] {
                new UILabel(Inter.getLocText("FR-Designer-Widget-Style_Body_Background")), backgroundPane
            }, new JComponent[] {
                new UILabel(""), new UILabel(Inter.getLocText("FR-Designer-Widget-Style_Alpha"))
            }, new JComponent[] {
                new UILabel(""), jpanel1
            }
        }, ad, ad1, 10D);
        jpanel2.setBorder(BorderFactory.createEmptyBorder(15, 12, 10, 12));
        UIScrollPane uiscrollpane = new UIScrollPane(jpanel2);
        uiscrollpane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("FR-Designer-Widget-Style_Frame"), null));
        uiscrollpane.setPreferredSize(uiscrollpane.getPreferredSize());
        return uiscrollpane;
    }

    protected JPanel initBodyRightTopPane()
    {
        borderTypeCombo = new UIComboBox(BORDER_TYPE);
        borderStyleCombo = new UIComboBox(BORDER_STYLE);
        currentLineCombo = new LineComboBox(BORDER_LINE_STYLE_ARRAY);
        currentLineColorPane = new UIColorButton(null);
        backgroundPane = new BackgroundSpecialPane();
        JPanel jpanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        numberDragPane = new UINumberDragPane(0.0D, 100D);
        jpanel.add(numberDragPane, "Center");
        jpanel.add(new UILabel(" %"), "East");
        double d = -1D;
        double d1 = -2D;
        double ad[] = {
            d1, d1, d1
        };
        double ad1[] = {
            d1, d
        };
        JPanel jpanel1 = TableLayoutHelper.createCommonTableLayoutPane(new JComponent[][] {
            new JComponent[] {
                new UILabel(Inter.getLocText("FR-Designer-Widget-Style_Body_Background")), backgroundPane
            }, new JComponent[] {
                new UILabel(""), new UILabel(Inter.getLocText("FR-Designer-Widget-Style_Alpha"))
            }, new JComponent[] {
                new UILabel(""), jpanel
            }
        }, ad, ad1, 10D);
        jpanel1.setBorder(BorderFactory.createEmptyBorder(15, 12, 10, 6));
        JPanel jpanel2 = FRGUIPaneFactory.createBorderLayout_S_Pane();
        jpanel2.add(jpanel1, "Center");
        jpanel2.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("FR-Designer-Widget-Style_Frame"), null));
        return jpanel2;
    }

    protected UIScrollPane initRightBottomPane()
    {
        formulaPane = new TinyFormulaPane();
        fontSizeComboBox = new UIComboBox(FRFontPane.FONT_SIZES);
        fontNameComboBox = new UIComboBox(Utils.getAvailableFontFamilyNames4Report());
        fontNameComboBox.setPreferredSize(new Dimension(160, 30));
        JPanel jpanel = new JPanel(new BorderLayout(10, 0));
        jpanel.add(fontSizeComboBox, "Center");
        jpanel.add(fontNameComboBox, "East");
        Icon aicon[] = {
            BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/h_left_normal.png"), BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/h_center_normal.png"), BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/h_right_normal.png")
        };
        Integer ainteger[] = {
            Integer.valueOf(2), Integer.valueOf(0), Integer.valueOf(4)
        };
        hAlignmentPane = new UIButtonGroup(aicon, ainteger);
        hAlignmentPane.setAllToolTips(new String[] {
            Inter.getLocText("FR-Designer-StyleAlignment_Left"), Inter.getLocText("FR-Designer-StyleAlignment_Center"), Inter.getLocText("FR-Designer-StyleAlignment_Right")
        });
        JPanel jpanel1 = new JPanel(new FlowLayout(0, 0, 0));
        jpanel1.add(hAlignmentPane);
        titleBackgroundPane = new BackgroundNoImagePane();
        double d = -2D;
        double ad[] = {
            d, d, d, d, d, d
        };
        double ad1[] = {
            d, 220D
        };
        JPanel jpanel2 = TableLayoutHelper.createCommonTableLayoutPane(new JComponent[][] {
            new JComponent[] {
                new UILabel(Inter.getLocText("FR-Designer-Widget-Style_Title_Content")), formulaPane
            }, new JComponent[] {
                new UILabel(Inter.getLocText("FR-Designer-Widget-Style_Title_Format")), jpanel
            }, new JComponent[] {
                new UILabel(""), initFontButtonPane()
            }, new JComponent[] {
                new UILabel(Inter.getLocText("FR-Designer_Alignment-Style")), jpanel1
            }, new JComponent[] {
                new UILabel(Inter.getLocText("FR-Designer-Widget-Style_Title_Background")), titleBackgroundPane
            }
        }, ad, ad1, 10D);
        jpanel2.setBorder(BorderFactory.createEmptyBorder(15, 12, 10, 12));
        titlePane = new UIScrollPane(jpanel2);
        titlePane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("FR-Designer-Widget-Style_Title"), null));
        titlePane.setVisible(false);
        return titlePane;
    }

    protected JPanel initFontButtonPane()
    {
        colorSelectPane = new UIColorButton();
        bold = new UIToggleButton(BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/bold.png"));
        italic = new UIToggleButton(BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/italic.png"));
        underline = new UIToggleButton(BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/underline.png"));
        bold.setPreferredSize(BUTTON_SIZE);
        italic.setPreferredSize(BUTTON_SIZE);
        underline.setPreferredSize(BUTTON_SIZE);
        underline.addChangeListener(new ChangeListener() {

            final LayoutBorderPane this$0;

            public void stateChanged(ChangeEvent changeevent)
            {
                underlineCombo.setVisible(underline.isSelected());
            }

            
            {
                this$0 = LayoutBorderPane.this;
                super();
            }
        }
);
        underlineCombo = new LineComboBox(UIConstants.BORDER_LINE_STYLE_ARRAY);
        Component acomponent[] = {
            colorSelectPane, italic, bold, underline
        };
        JPanel jpanel = new JPanel(new BorderLayout());
        jpanel.add(GUICoreUtils.createFlowPane(acomponent, 0, 1));
        JPanel jpanel1 = FRGUIPaneFactory.createBorderLayout_S_Pane();
        jpanel1.add(jpanel, "West");
        jpanel1.add(underlineCombo, "Center");
        initAllNames();
        setToolTips();
        return jpanel1;
    }

    protected void initAllNames()
    {
        fontNameComboBox.setGlobalName(Inter.getLocText("FR-Designer-FRFont_Family"));
        fontSizeComboBox.setGlobalName(Inter.getLocText("FR-Designer-FRFont_Size"));
        colorSelectPane.setGlobalName(Inter.getLocText("FR-Designer-FRFont_Foreground"));
        italic.setGlobalName(Inter.getLocText("FR-Designer-FRFont_Italic"));
        bold.setGlobalName(Inter.getLocText("FR-Designer-FRFont_Bold"));
        underline.setGlobalName(Inter.getLocText("FR-Designer-FRFont_Underline"));
        underlineCombo.setGlobalName(Inter.getLocText("FR-Designer-FRFont_Line_Style"));
    }

    protected void setToolTips()
    {
        colorSelectPane.setToolTipText(Inter.getLocText("FR-Designer-FRFont_Foreground"));
        italic.setToolTipText(Inter.getLocText("FR-Designer-FRFont_Italic"));
        bold.setToolTipText(Inter.getLocText("FR-Designer-FRFont_Bold"));
        underline.setToolTipText(Inter.getLocText("FR-Designer-FRFont_Underline"));
    }

    public LayoutBorderStyle update()
    {
        LayoutBorderStyle layoutborderstyle = new LayoutBorderStyle();
        layoutborderstyle.setType(borderTypeCombo.getSelectedIndex());
        layoutborderstyle.setBorderStyle(borderStyleCombo.getSelectedIndex());
        layoutborderstyle.setBorder(currentLineCombo.getSelectedLineStyle());
        layoutborderstyle.setColor(currentLineColorPane.getColor());
        layoutborderstyle.setBackground(backgroundPane.update());
        layoutborderstyle.setAlpha((float)(numberDragPane.updateBean().doubleValue() / maxNumber));
        WidgetTitle widgettitle = layoutborderstyle.getTitle() != null ? layoutborderstyle.getTitle() : new WidgetTitle();
        widgettitle.setTextObject(formulaPane.updateBean());
        FRFont frfont = widgettitle.getFrFont();
        frfont = frfont.applySize(((Integer)fontSizeComboBox.getSelectedItem()).intValue());
        frfont = frfont.applyName(fontNameComboBox.getSelectedItem().toString());
        frfont = frfont.applyForeground(colorSelectPane.getColor());
        frfont = updateItalicBold(frfont);
        int i = underline.isSelected() ? underlineCombo.getSelectedLineStyle() : 0;
        frfont = frfont.applyUnderline(i);
        widgettitle.setFrFont(frfont);
        widgettitle.setPosition(((Integer)hAlignmentPane.getSelectedItem()).intValue());
        widgettitle.setBackground(titleBackgroundPane.update());
        layoutborderstyle.setTitle(widgettitle);
        return layoutborderstyle;
    }

    protected FRFont updateItalicBold(FRFont frfont)
    {
        int i = frfont.getStyle();
        boolean flag = i == 2 || i == 3;
        boolean flag1 = i == 1 || i == 3;
        if(italic.isSelected() && !flag)
            i += 2;
        else
        if(!italic.isSelected() && flag)
            i -= 2;
        frfont = frfont.applyStyle(i);
        if(bold.isSelected() && !flag1)
            i++;
        else
        if(!bold.isSelected() && flag1)
            i--;
        frfont = frfont.applyStyle(i);
        return frfont;
    }

    public void populate(LayoutBorderStyle layoutborderstyle)
    {
        if(borderStyle == null)
            borderStyle = new LayoutBorderStyle();
        borderStyle.setStyle(layoutborderstyle);
        populateBorder();
        populateTitle();
    }

    protected void populateBorder()
    {
        borderTypeCombo.setSelectedIndex(borderStyle.getType());
        borderTypeCombo.addItemListener(new ItemListener() {

            final LayoutBorderPane this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                paintPreviewPane();
            }

            
            {
                this$0 = LayoutBorderPane.this;
                super();
            }
        }
);
        borderStyleCombo.setSelectedIndex(borderStyle.getBorderStyle());
        borderStyleCombo.addItemListener(new ItemListener() {

            final LayoutBorderPane this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                paintPreviewPane();
            }

            
            {
                this$0 = LayoutBorderPane.this;
                super();
            }
        }
);
        currentLineCombo.setSelectedLineStyle(borderStyle.getBorder());
        currentLineCombo.addItemListener(new ItemListener() {

            final LayoutBorderPane this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                paintPreviewPane();
            }

            
            {
                this$0 = LayoutBorderPane.this;
                super();
            }
        }
);
        currentLineColorPane.setColor(borderStyle.getColor());
        currentLineColorPane.addColorChangeListener(new ChangeListener() {

            final LayoutBorderPane this$0;

            public void stateChanged(ChangeEvent changeevent)
            {
                paintPreviewPane();
            }

            
            {
                this$0 = LayoutBorderPane.this;
                super();
            }
        }
);
        backgroundPane.populateBean(borderStyle.getBackground());
        backgroundPane.addChangeListener(new ChangeListener() {

            final LayoutBorderPane this$0;

            public void stateChanged(ChangeEvent changeevent)
            {
                paintPreviewPane();
            }

            
            {
                this$0 = LayoutBorderPane.this;
                super();
            }
        }
);
        numberDragPane.populateBean(Double.valueOf((double)borderStyle.getAlpha() * maxNumber));
    }

    protected void populateTitle()
    {
        WidgetTitle widgettitle = borderStyle != null ? borderStyle.getTitle() : new WidgetTitle();
        widgettitle = widgettitle != null ? widgettitle : new WidgetTitle();
        populateFourmula(widgettitle);
        populateFont(widgettitle);
        underline.addMouseListener(new MouseAdapter() {

            final LayoutBorderPane this$0;

            public void mouseClicked(MouseEvent mouseevent)
            {
                paintPreviewPane();
            }

            
            {
                this$0 = LayoutBorderPane.this;
                super();
            }
        }
);
        underlineCombo.addItemListener(new ItemListener() {

            final LayoutBorderPane this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                paintPreviewPane();
            }

            
            {
                this$0 = LayoutBorderPane.this;
                super();
            }
        }
);
        hAlignmentPane.setSelectedItem(Integer.valueOf(widgettitle.getPosition()));
        hAlignmentPane.addChangeListener(new ChangeListener() {

            final LayoutBorderPane this$0;

            public void stateChanged(ChangeEvent changeevent)
            {
                paintPreviewPane();
            }

            
            {
                this$0 = LayoutBorderPane.this;
                super();
            }
        }
);
        titleBackgroundPane.populateBean(widgettitle.getBackground());
        titleBackgroundPane.addChangeListener(new ChangeListener() {

            final LayoutBorderPane this$0;

            public void stateChanged(ChangeEvent changeevent)
            {
                paintPreviewPane();
            }

            
            {
                this$0 = LayoutBorderPane.this;
                super();
            }
        }
);
        paintPreviewPane();
    }

    protected void populateFont(WidgetTitle widgettitle)
    {
        FRFont frfont = widgettitle.getFrFont();
        fontSizeComboBox.setSelectedItem(Integer.valueOf(frfont.getSize()));
        fontSizeComboBox.addItemListener(new ItemListener() {

            final LayoutBorderPane this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                paintPreviewPane();
            }

            
            {
                this$0 = LayoutBorderPane.this;
                super();
            }
        }
);
        fontNameComboBox.setSelectedItem(frfont.getFamily());
        fontNameComboBox.addItemListener(new ItemListener() {

            final LayoutBorderPane this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                paintPreviewPane();
            }

            
            {
                this$0 = LayoutBorderPane.this;
                super();
            }
        }
);
        colorSelectPane.setColor(frfont.getForeground());
        colorSelectPane.addColorChangeListener(new ChangeListener() {

            final LayoutBorderPane this$0;

            public void stateChanged(ChangeEvent changeevent)
            {
                paintPreviewPane();
            }

            
            {
                this$0 = LayoutBorderPane.this;
                super();
            }
        }
);
        colorSelectPane.repaint();
        bold.setSelected(frfont.isBold());
        bold.addMouseListener(new MouseAdapter() {

            final LayoutBorderPane this$0;

            public void mouseClicked(MouseEvent mouseevent)
            {
                paintPreviewPane();
            }

            
            {
                this$0 = LayoutBorderPane.this;
                super();
            }
        }
);
        italic.setSelected(frfont.isItalic());
        italic.addMouseListener(new MouseAdapter() {

            final LayoutBorderPane this$0;

            public void mouseClicked(MouseEvent mouseevent)
            {
                paintPreviewPane();
            }

            
            {
                this$0 = LayoutBorderPane.this;
                super();
            }
        }
);
        int i = frfont.getUnderline();
        if(i == 0)
        {
            underline.setSelected(false);
            underlineCombo.setVisible(false);
        } else
        {
            underline.setSelected(true);
            underlineCombo.setVisible(true);
            underlineCombo.setSelectedLineStyle(i);
        }
    }

    protected void paintPreviewPane()
    {
        layoutBorderPreviewPane.repaint(update());
    }

    protected UIButtonUI getButtonUI(final UIColorButton uiColorButton)
    {
        return new UIButtonUI() {

            final UIColorButton val$uiColorButton;
            final LayoutBorderPane this$0;

            public void paint(Graphics g, JComponent jcomponent)
            {
                UIButton uibutton = (UIButton)jcomponent;
                g.setColor(Color.black);
                GraphHelper.draw(g, new java.awt.geom.RoundRectangle2D.Double(1.0D, 1.0D, uibutton.getWidth() - 2, uibutton.getHeight() - 2, 0.0D, 0.0D), 1);
                if(uibutton.getModel().isEnabled())
                    g.setColor(uiColorButton.getColor());
                else
                    g.setColor(new Color(Utils.filterRGB(uiColorButton.getColor().getRGB(), 50)));
                g.fillRect(2, 2, uibutton.getWidth() - 3, uibutton.getHeight() - 3);
            }

            
            {
                this$0 = LayoutBorderPane.this;
                uiColorButton = uicolorbutton;
                super();
            }
        }
;
    }

    private void populateFourmula(WidgetTitle widgettitle)
    {
        formulaPane.populateBean(widgettitle.getTextObject().toString());
        formulaPane.getUITextField().getDocument().addDocumentListener(new DocumentListener() {

            final LayoutBorderPane this$0;

            public void insertUpdate(DocumentEvent documentevent)
            {
                paintPreviewPane();
            }

            public void removeUpdate(DocumentEvent documentevent)
            {
                paintPreviewPane();
            }

            public void changedUpdate(DocumentEvent documentevent)
            {
                paintPreviewPane();
            }

            
            {
                this$0 = LayoutBorderPane.this;
                super();
            }
        }
);
    }

    protected JPanel createVerButtonPane(JToggleButton jtogglebutton, String s)
    {
        JPanel jpanel = new JPanel();
        jpanel.setLayout(new VerticalFlowLayout(1, 2, 2));
        jpanel.add(jtogglebutton);
        jpanel.add(new UILabel(s));
        return jpanel;
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("FR-Designer-Widget_Style");
    }










}
