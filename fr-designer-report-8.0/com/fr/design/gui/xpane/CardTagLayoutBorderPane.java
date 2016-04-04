// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.gui.xpane;

import com.fr.base.Utils;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.frpane.UINumberDragPane;
import com.fr.design.gui.ibutton.UIColorButton;
import com.fr.design.gui.ibutton.UIToggleButton;
import com.fr.design.gui.icombobox.LineComboBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.style.*;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.JForm;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.form.ui.LayoutBorderStyle;
import com.fr.form.ui.WidgetTitle;
import com.fr.general.FRFont;
import com.fr.general.Inter;
import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

// Referenced classes of package com.fr.design.gui.xpane:
//            LayoutBorderPane, CardTagLayoutBorderPreviewPane

public class CardTagLayoutBorderPane extends LayoutBorderPane
{

    public CardTagLayoutBorderPane()
    {
        initComponents();
    }

    protected UIScrollPane initRightBottomPane()
    {
        setFontSizeComboBox(new UIComboBox(FRFontPane.FONT_SIZES));
        setFontNameComboBox(new UIComboBox(Utils.getAvailableFontFamilyNames4Report()));
        JPanel jpanel = new JPanel(new BorderLayout(10, 0));
        jpanel.add(getFontSizeComboBox(), "Center");
        jpanel.add(getFontNameComboBox(), "East");
        setTitleBackgroundPane(new BackgroundNoImagePane());
        double d = -1D;
        double d1 = -2D;
        double ad[] = {
            d1, d1, d1, d1, d1, d1
        };
        double ad1[] = {
            d1, d
        };
        JPanel jpanel1 = TableLayoutHelper.createCommonTableLayoutPane(new JComponent[][] {
            new JComponent[] {
                new UILabel(Inter.getLocText("FR-Designer-Widget-Style_Title_Format")), jpanel
            }, new JComponent[] {
                new UILabel(""), initFontButtonPane()
            }, new JComponent[] {
                new UILabel(Inter.getLocText("FR-Designer-Widget-Style_Title_Background")), getTitleBackgroundPane()
            }
        }, ad, ad1, 10D);
        jpanel1.setBorder(BorderFactory.createEmptyBorder(15, 12, 10, 12));
        setTitlePane(new UIScrollPane(jpanel1));
        getTitlePane().setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("FR-Designer-Widget-Style_Title"), null));
        getTitlePane().setVisible(false);
        return getTitlePane();
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
        setLayoutBorderPreviewPane(new CardTagLayoutBorderPreviewPane(getBorderStyle()));
        jpanel2.add(getLayoutBorderPreviewPane(), "Center");
        JPanel jpanel3 = FRGUIPaneFactory.createBorderLayout_S_Pane();
        jpanel.add(jpanel3, "East");
        jpanel3.add(initRightBottomPane(), "Center");
        JTemplate jtemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
        if(!jtemplate.isJWorkBook() && ((JForm)jtemplate).isSelectRootPane())
            jpanel3.add(initBodyRightTopPane(), "North");
        else
            jpanel3.add(initRightTopPane(), "North");
    }

    public LayoutBorderStyle update()
    {
        LayoutBorderStyle layoutborderstyle = new LayoutBorderStyle();
        layoutborderstyle.setType(getBorderTypeCombo().getSelectedIndex());
        layoutborderstyle.setBorderStyle(getBorderStyleCombo().getSelectedIndex());
        layoutborderstyle.setBorder(getCurrentLineCombo().getSelectedLineStyle());
        layoutborderstyle.setColor(getCurrentLineColorPane().getColor());
        layoutborderstyle.setBackground(getBackgroundPane().update());
        layoutborderstyle.setAlpha((float)(getNumberDragPane().updateBean().doubleValue() / getMaxNumber()));
        WidgetTitle widgettitle = layoutborderstyle.getTitle() != null ? layoutborderstyle.getTitle() : new WidgetTitle();
        widgettitle.setTextObject("title");
        FRFont frfont = widgettitle.getFrFont();
        frfont = frfont.applySize(((Integer)getFontSizeComboBox().getSelectedItem()).intValue());
        frfont = frfont.applyName(getFontNameComboBox().getSelectedItem().toString());
        frfont = frfont.applyForeground(getColorSelectPane().getColor());
        frfont = updateItalicBold(frfont);
        int i = getUnderline().isSelected() ? getUnderlineCombo().getSelectedLineStyle() : 0;
        frfont = frfont.applyUnderline(i);
        widgettitle.setFrFont(frfont);
        widgettitle.setBackground(getTitleBackgroundPane().update());
        layoutborderstyle.setTitle(widgettitle);
        return layoutborderstyle;
    }

    protected void populateTitle()
    {
        WidgetTitle widgettitle = getBorderStyle() != null ? getBorderStyle().getTitle() : new WidgetTitle();
        widgettitle = widgettitle != null ? widgettitle : new WidgetTitle();
        populateFont(widgettitle);
        getUnderline().addMouseListener(new MouseAdapter() {

            final CardTagLayoutBorderPane this$0;

            public void mouseClicked(MouseEvent mouseevent)
            {
                paintPreviewPane();
            }

            
            {
                this$0 = CardTagLayoutBorderPane.this;
                super();
            }
        }
);
        getUnderlineCombo().addItemListener(new ItemListener() {

            final CardTagLayoutBorderPane this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                paintPreviewPane();
            }

            
            {
                this$0 = CardTagLayoutBorderPane.this;
                super();
            }
        }
);
        getTitleBackgroundPane().populateBean(widgettitle.getBackground());
        getTitleBackgroundPane().addChangeListener(new ChangeListener() {

            final CardTagLayoutBorderPane this$0;

            public void stateChanged(ChangeEvent changeevent)
            {
                paintPreviewPane();
            }

            
            {
                this$0 = CardTagLayoutBorderPane.this;
                super();
            }
        }
);
        paintPreviewPane();
    }
}
