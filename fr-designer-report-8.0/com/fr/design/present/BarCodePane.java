// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.present;

import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.border.UIRoundedBorder;
import com.fr.design.constants.UIConstants;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.icombobox.UIComboBoxRenderer;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.report.cell.cellattr.BarcodeAttr;
import com.fr.report.cell.cellattr.BarcodePresent;
import com.fr.report.cell.painter.barcode.BarcodeImpl;
import com.fr.report.cell.painter.barcode.core.BarCodeUtils;
import com.fr.stable.pinyin.ChineseHelper;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.font.*;
import java.awt.geom.AffineTransform;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class BarCodePane extends FurtherBasicBeanPane
{
    private static class BarCodePreviewPane extends JPanel
    {

        private Object obj;

        public void setObject(Object obj1)
        {
            obj = obj1;
            GUICoreUtils.repaint(this);
        }

        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            if(obj == null)
                return;
            if(obj instanceof BarcodeImpl)
            {
                BarcodeImpl barcodeimpl = (BarcodeImpl)obj;
                Dimension dimension = getSize();
                barcodeimpl.draw((Graphics2D)g, (int)(dimension.getWidth() - (double)barcodeimpl.getWidth()) / 2, (int)(dimension.getHeight() - (double)barcodeimpl.getHeight()) / 2);
            } else
            {
                Graphics2D graphics2d = (Graphics2D)g;
                graphics2d.setPaint(Color.RED);
                HashMap hashmap = new HashMap();
                hashmap.put(TextAttribute.SIZE, new Float(14D));
                AttributedString attributedstring = new AttributedString(obj.toString(), hashmap);
                AttributedCharacterIterator attributedcharacteriterator = attributedstring.getIterator();
                int i = attributedcharacteriterator.getBeginIndex();
                int j = attributedcharacteriterator.getEndIndex();
                AffineTransform affinetransform = null;
                LineBreakMeasurer linebreakmeasurer = new LineBreakMeasurer(attributedcharacteriterator, new FontRenderContext(affinetransform, false, false));
                Dimension dimension1 = getSize();
                float f = dimension1.width;
                float f1 = 0.0F;
                linebreakmeasurer.setPosition(i);
                while(linebreakmeasurer.getPosition() < j) 
                {
                    TextLayout textlayout = linebreakmeasurer.nextLayout(f);
                    f1 += textlayout.getAscent();
                    float f2;
                    if(textlayout.isLeftToRight())
                        f2 = 0.0F;
                    else
                        f2 = f - textlayout.getAdvance();
                    textlayout.draw(graphics2d, f2, f1);
                    f1 += textlayout.getDescent() + textlayout.getLeading();
                }
            }
        }

        public BarCodePreviewPane()
        {
        }
    }


    private final int num16 = 16;
    private BarCodePreviewPane barCodePreviewPane;
    private UIComboBox typeComboBox;
    private UIBasicSpinner barWidthSpinner;
    private UIBasicSpinner barHeightSpinner;
    private UIBasicSpinner RCodesizespinner;
    private UICheckBox drawingTextCheckBox;
    private UIComboBox RCodeVersionComboBox;
    private UIComboBox RCodeErrorCorrectComboBox;
    private UILabel typeSetLabel;
    private String testText;

    public BarCodePane()
    {
        testText = "12345";
        initComponents();
        addlistener();
    }

    private void initComponents()
    {
        barCodePreviewPane = new BarCodePreviewPane();
        barWidthSpinner = new UIBasicSpinner(new SpinnerNumberModel(1.0D, 1.0D, 100D, 0.10000000000000001D));
        barHeightSpinner = new UIBasicSpinner(new SpinnerNumberModel(30, 1, 100, 1));
        barWidthSpinner.setPreferredSize(new Dimension(45, 20));
        barHeightSpinner.setPreferredSize(new Dimension(45, 20));
        JPanel jpanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        TitledBorder titledborder = new TitledBorder(new UIRoundedBorder(UIConstants.LINE_COLOR, 1, 5), Inter.getLocText("StyleFormat-Sample"), 4, 2, getFont(), UIConstants.LINE_COLOR);
        jpanel.setBorder(titledborder);
        jpanel.add(barCodePreviewPane, "Center");
        setTypeComboBox();
        setSome();
        RCodesizespinner = new UIBasicSpinner(new SpinnerNumberModel(2, 1, 6, 1));
        RCodeVersionComboBox = new UIComboBox();
        RCodeErrorCorrectComboBox = new UIComboBox();
        typeSetLabel = new UILabel((new StringBuilder()).append(Inter.getLocText("Type_Set")).append(":").toString(), 4);
        initVersionComboBox();
        initErrorCorrectComboBox();
        drawingTextCheckBox = new UICheckBox(Inter.getLocText("BarCodeD-Drawing_Text"));
        drawingTextCheckBox.setSelected(true);
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d, d1
        };
        double ad1[] = {
            d, d, d, d, d, d, d, d
        };
        barCodePreviewPane.setPreferredSize(new Dimension(0, 145));
        final JPanel centerPane = new JPanel(new CardLayout());
        Component acomponent[][] = {
            {
                typeSetLabel, typeComboBox
            }, {
                jpanel, null
            }, {
                centerPane, null
            }
        };
        JPanel jpanel1 = TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
        centerPane.add(getNormalPane(), "normal");
        centerPane.add(getSpecialPane(), "special");
        typeComboBox.addItemListener(new ItemListener() {

            final JPanel val$centerPane;
            final BarCodePane this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                CardLayout cardlayout = (CardLayout)centerPane.getLayout();
                cardlayout.show(centerPane, typeComboBox.getSelectedIndex() != 16 ? "normal" : "special");
                setTestText(BarCodeUtils.getTestTextByBarCode(typeComboBox.getSelectedIndex()));
                repaintPreviewBarCode();
            }

            
            {
                this$0 = BarCodePane.this;
                centerPane = jpanel;
                super();
            }
        }
);
        setLayout(new BorderLayout());
        add(jpanel1, "Center");
    }

    private void setTypeComboBox()
    {
        typeComboBox = new UIComboBox(BarCodeUtils.getAllSupportedBarCodeTypeArray());
        typeComboBox.setRenderer(new UIComboBoxRenderer() {

            final BarCodePane this$0;

            public Component getListCellRendererComponent(JList jlist, Object obj, int i, boolean flag, boolean flag1)
            {
                super.getListCellRendererComponent(jlist, obj, i, flag, flag1);
                if(obj instanceof Integer)
                    setText((new StringBuilder()).append(" ").append(BarCodeUtils.getBarCodeTypeName(((Integer)obj).intValue())).toString());
                return this;
            }

            
            {
                this$0 = BarCodePane.this;
                super();
            }
        }
);
    }

    private void setSome()
    {
        JFormattedTextField jformattedtextfield = ((javax.swing.JSpinner.DefaultEditor)barHeightSpinner.getEditor()).getTextField();
        jformattedtextfield.setColumns(2);
        JFormattedTextField jformattedtextfield1 = ((javax.swing.JSpinner.DefaultEditor)barWidthSpinner.getEditor()).getTextField();
        jformattedtextfield1.setColumns(2);
    }

    private JPanel getNormalPane()
    {
        double d = -2D;
        double ad[] = {
            d, d, d, d, d, d, d, d
        };
        JPanel jpanel = new JPanel(new FlowLayout(0, 2, 0));
        jpanel.add(barWidthSpinner);
        JPanel jpanel1 = new JPanel(new FlowLayout(0, 2, 0));
        jpanel1.add(barHeightSpinner);
        UILabel uilabel = new UILabel((new StringBuilder()).append(Inter.getLocText("Tree-Width")).append(":").toString(), 4);
        uilabel.setPreferredSize(typeSetLabel.getPreferredSize());
        Component acomponent[][] = {
            {
                uilabel, jpanel
            }, {
                new UILabel((new StringBuilder()).append(Inter.getLocText("Height")).append(":").toString(), 4), jpanel1
            }, {
                new UILabel((new StringBuilder()).append(Inter.getLocText("Text")).append(":").toString(), 4), drawingTextCheckBox
            }
        };
        double ad1[] = {
            d, d
        };
        JPanel jpanel2 = TableLayoutHelper.createTableLayoutPane(acomponent, ad, ad1);
        return jpanel2;
    }

    private JPanel getSpecialPane()
    {
        double d = -2D;
        double d1 = -1D;
        double ad[] = {
            d, d
        };
        double ad1[] = {
            d, d, d, d, d, d, d, d
        };
        UILabel uilabel = new UILabel((new StringBuilder()).append(Inter.getLocText("RCodeVersion")).append(":").toString(), 4);
        uilabel.setPreferredSize(typeSetLabel.getPreferredSize());
        Component acomponent[][] = {
            {
                uilabel, RCodeVersionComboBox
            }, {
                new UILabel((new StringBuilder()).append(Inter.getLocText("RCodeErrorCorrect")).append(":").toString(), 4), RCodeErrorCorrectComboBox
            }, {
                new UILabel((new StringBuilder()).append(Inter.getLocText("RCodeDrawPix")).append(":").toString(), 4), RCodesizespinner
            }
        };
        JPanel jpanel = TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
        return jpanel;
    }

    private void addlistener()
    {
        RCodesizespinner.addChangeListener(new ChangeListener() {

            final BarCodePane this$0;

            public void stateChanged(ChangeEvent changeevent)
            {
                repaintPreviewBarCode();
            }

            
            {
                this$0 = BarCodePane.this;
                super();
            }
        }
);
        RCodeVersionComboBox.addItemListener(new ItemListener() {

            final BarCodePane this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                repaintPreviewBarCode();
            }

            
            {
                this$0 = BarCodePane.this;
                super();
            }
        }
);
        RCodeErrorCorrectComboBox.addItemListener(new ItemListener() {

            final BarCodePane this$0;

            public void itemStateChanged(ItemEvent itemevent)
            {
                repaintPreviewBarCode();
            }

            
            {
                this$0 = BarCodePane.this;
                super();
            }
        }
);
        barWidthSpinner.addChangeListener(new ChangeListener() {

            final BarCodePane this$0;

            public void stateChanged(ChangeEvent changeevent)
            {
                repaintPreviewBarCode();
            }

            
            {
                this$0 = BarCodePane.this;
                super();
            }
        }
);
        barHeightSpinner.addChangeListener(new ChangeListener() {

            final BarCodePane this$0;

            public void stateChanged(ChangeEvent changeevent)
            {
                repaintPreviewBarCode();
            }

            
            {
                this$0 = BarCodePane.this;
                super();
            }
        }
);
        drawingTextCheckBox.addChangeListener(new ChangeListener() {

            final BarCodePane this$0;

            public void stateChanged(ChangeEvent changeevent)
            {
                repaintPreviewBarCode();
            }

            
            {
                this$0 = BarCodePane.this;
                super();
            }
        }
);
        repaintPreviewBarCode();
    }

    public String title4PopupWindow()
    {
        return Inter.getLocText("Highlight-Barcode");
    }

    private void initVersionComboBox()
    {
        String as[] = {
            Inter.getLocText(new String[] {
                "Auto", "Choose"
            }), "1", "2", "3", "4", "5", "6", "7", "8", "9", 
            "10", "11", "12", "13", "14", "15", "16"
        };
        initcombobox(RCodeVersionComboBox, as, 0);
    }

    private void initErrorCorrectComboBox()
    {
        String as[] = {
            (new StringBuilder()).append("L").append(Inter.getLocText("Level")).append("7%").toString(), (new StringBuilder()).append("M").append(Inter.getLocText("Level")).append("15%").toString(), (new StringBuilder()).append("Q").append(Inter.getLocText("Level")).append("25%").toString(), (new StringBuilder()).append("H").append(Inter.getLocText("Level")).append("30%").toString()
        };
        initcombobox(RCodeErrorCorrectComboBox, as, 1);
    }

    private void initcombobox(UIComboBox uicombobox, String as[], int i)
    {
        uicombobox.removeAllItems();
        for(int j = 0; j < as.length; j++)
            uicombobox.addItem(as[j]);

        uicombobox.setSelectedIndex(i);
    }

    private void repaintPreviewBarCode()
    {
        try
        {
            if(ChineseHelper.containChinese(getTestText()) && typeComboBox.getSelectedIndex() != 16)
                throw new Exception("Illegal Character.");
            barCodePreviewPane.setObject(BarCodeUtils.getBarcodeImpl(updateBean().getBarcode(), getTestText()));
        }
        catch(Exception exception)
        {
            barCodePreviewPane.setObject((new StringBuilder()).append(Inter.getLocText("Error")).append(": ").append(exception.getMessage()).toString());
        }
    }

    public void reset()
    {
        populateBean(new BarcodePresent());
    }

    public void populateBean(BarcodePresent barcodepresent)
    {
        BarcodeAttr barcodeattr = barcodepresent.getBarcode();
        if(barcodeattr == null)
            barcodeattr = new BarcodeAttr();
        setTestText(BarCodeUtils.getTestTextByBarCode(barcodeattr.getType()));
        typeComboBox.setSelectedIndex(barcodeattr.getType());
        barWidthSpinner.setValue(new Double(barcodeattr.getBarWidth()));
        barHeightSpinner.setValue(new Integer(barcodeattr.getBarHeight()));
        drawingTextCheckBox.setSelected(barcodeattr.isDrawingText());
        RCodesizespinner.setValue(new Integer(barcodeattr.getRcodeDrawPix()));
        repaintPreviewBarCode();
    }

    public BarcodePresent updateBean()
    {
        BarcodeAttr barcodeattr = new BarcodeAttr();
        if(typeComboBox.getSelectedIndex() == 16)
        {
            barcodeattr.setRCodeVersion(RCodeVersionComboBox.getSelectedIndex());
            barcodeattr.setRCodeErrorCorrect(RCodeErrorCorrectComboBox.getSelectedIndex());
            barcodeattr.setRcodeDrawPix(((Integer)RCodesizespinner.getValue()).intValue());
        }
        barcodeattr.setType(typeComboBox.getSelectedIndex());
        barcodeattr.setBarWidth(((Double)barWidthSpinner.getValue()).doubleValue());
        barcodeattr.setBarHeight(((Integer)barHeightSpinner.getValue()).intValue());
        barcodeattr.setDrawingText(drawingTextCheckBox.isSelected());
        return new BarcodePresent(barcodeattr);
    }

    public void setTestText(String s)
    {
        testText = s;
    }

    public String getTestText()
    {
        return testText;
    }

    public boolean accept(Object obj)
    {
        return obj instanceof BarcodePresent;
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((BarcodePresent)obj);
    }


}
