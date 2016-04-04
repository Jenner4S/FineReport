// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.present;

import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.border.UIRoundedBorder;
import com.fr.design.constants.UIConstants;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.report.cell.cellattr.CurrencyLineAttr;
import com.fr.report.cell.cellattr.CurrencyLinePresent;
import com.fr.report.cell.painter.barcode.BarcodeException;
import com.fr.report.core.CurrencyLineImpl;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;
import javax.swing.text.Document;

public class CurrencyLinePane extends FurtherBasicBeanPane
{
    private class CurrencyLinePreviewPane extends JPanel
    {

        private String text;
        CurrencyLineAttr currencyLineAttr;
        final CurrencyLinePane this$0;

        public void setObject(String s, CurrencyLineAttr currencylineattr)
        {
            text = s;
            currencyLineAttr = currencylineattr;
            GUICoreUtils.repaint(this);
        }

        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            if(text == null)
                return;
            Dimension dimension = getSize();
            try
            {
                CurrencyLineImpl currencylineimpl = new CurrencyLineImpl(text, currencyLineAttr);
                currencylineimpl.draw((Graphics2D)g, (int)dimension.getWidth(), (int)dimension.getHeight());
            }
            catch(BarcodeException barcodeexception)
            {
                Color color = g.getColor();
                g.setColor(Color.red);
                g.drawString(barcodeexception.getMessage(), (int)(dimension.getWidth() / 8D), (int)(dimension.getHeight() / 8D));
                g.setColor(color);
            }
        }

        public CurrencyLinePreviewPane()
        {
            this$0 = CurrencyLinePane.this;
            super();
        }
    }


    private static final int VS_NUM = 4;
    private static final int VG_NUM = 6;
    private UIBasicSpinner intPartSpinner;
    private UIBasicSpinner deciPartSpinner;
    private UITextField textField;
    private CurrencyLinePreviewPane CurrencyLinePreviewPane;
    private int intPart;
    private int deciPart;
    private static final int POSITION = 8;
    ChangeListener listener2;
    DocumentListener listener;

    public CurrencyLinePane()
    {
        intPart = 9;
        deciPart = 3;
        listener2 = new ChangeListener() {

            final CurrencyLinePane this$0;

            public void stateChanged(ChangeEvent changeevent)
            {
                CurrencyLinePreviewPane.setObject(textField.getText(), update());
            }

            
            {
                this$0 = CurrencyLinePane.this;
                super();
            }
        }
;
        listener = new DocumentListener() {

            final CurrencyLinePane this$0;

            public void insertUpdate(DocumentEvent documentevent)
            {
                CurrencyLinePreviewPane.setObject(textField.getText(), update());
            }

            public void removeUpdate(DocumentEvent documentevent)
            {
                CurrencyLinePreviewPane.setObject(textField.getText(), update());
            }

            public void changedUpdate(DocumentEvent documentevent)
            {
                CurrencyLinePreviewPane.setObject(textField.getText(), update());
            }

            
            {
                this$0 = CurrencyLinePane.this;
                super();
            }
        }
;
        initComponents();
    }

    protected void initComponents()
    {
        intPartSpinner = new UIBasicSpinner(new SpinnerNumberModel(9, 1, 20, 1));
        intPartSpinner.setPreferredSize(new Dimension(45, 20));
        deciPartSpinner = new UIBasicSpinner(new SpinnerNumberModel(2, 1, 10, 1));
        deciPartSpinner.setPreferredSize(new Dimension(45, 20));
        textField = new UITextField(10);
        CurrencyLinePreviewPane = new CurrencyLinePreviewPane();
        CurrencyLinePreviewPane.setPreferredSize(new Dimension(0, 145));
        JPanel jpanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        TitledBorder titledborder = new TitledBorder(new UIRoundedBorder(UIConstants.LINE_COLOR, 1, 5), Inter.getLocText("StyleFormat-Sample"), 4, 2, getFont(), UIConstants.LINE_COLOR);
        jpanel.setBorder(titledborder);
        jpanel.add(CurrencyLinePreviewPane, "Center");
        textField.requestFocus();
        double d = 4D;
        double d1 = 6D;
        double d2 = -2D;
        double d3 = -1D;
        double ad[] = {
            d2, d3
        };
        double ad1[] = {
            d2, d2, d2, d2
        };
        Component acomponent[][] = {
            {
                new UILabel((new StringBuilder()).append(Inter.getLocText("Data")).append(":").toString(), 4), textField
            }, {
                jpanel, null
            }, {
                new UILabel((new StringBuilder()).append(Inter.getLocText("IntPart")).append(":").toString(), 4), groupPane(intPartSpinner)
            }, {
                new UILabel((new StringBuilder()).append(Inter.getLocText("DeciPart")).append(":").toString(), 4), groupPane(deciPartSpinner)
            }
        };
        JPanel jpanel1 = TableLayoutHelper.createTableLayoutPane(acomponent, ad1, ad);
        setLayout(new BorderLayout());
        add(jpanel1, "Center");
        textField.getDocument().addDocumentListener(listener);
        intPartSpinner.addChangeListener(listener2);
        deciPartSpinner.addChangeListener(listener2);
        textField.setText("123456.78");
    }

    public String title4PopupWindow()
    {
        return Inter.getLocText("Currency_Line");
    }

    public CurrencyLineAttr update()
    {
        CurrencyLineAttr currencylineattr = new CurrencyLineAttr();
        currencylineattr.setintPart(((Integer)intPartSpinner.getValue()).intValue());
        currencylineattr.setdeciPart(((Integer)deciPartSpinner.getValue()).intValue());
        return currencylineattr;
    }

    public void setintPart(int i)
    {
        intPart = i;
    }

    public void setdeciPart(int i)
    {
        deciPart = i;
    }

    protected static JPanel groupPane(JComponent jcomponent)
    {
        JPanel jpanel = new JPanel();
        jpanel.setBorder(null);
        jpanel.setLayout(new FlowLayout(0));
        jpanel.add(jcomponent);
        return jpanel;
    }

    public boolean accept(Object obj)
    {
        return obj instanceof CurrencyLinePresent;
    }

    public void reset()
    {
        intPartSpinner.setValue(Integer.valueOf(9));
        deciPartSpinner.setValue(Integer.valueOf(3));
    }

    public void populateBean(CurrencyLinePresent currencylinepresent)
    {
        CurrencyLineAttr currencylineattr = currencylinepresent.getCurrencyLineAttr();
        if(currencylineattr == null)
            currencylineattr = new CurrencyLineAttr();
        intPartSpinner.setValue(new Integer(currencylineattr.getintPart()));
        deciPartSpinner.setValue(new Integer(currencylineattr.getdeciPart()));
    }

    public CurrencyLinePresent updateBean()
    {
        CurrencyLineAttr currencylineattr = new CurrencyLineAttr();
        currencylineattr.setintPart(((Integer)intPartSpinner.getValue()).intValue());
        currencylineattr.setdeciPart(((Integer)deciPartSpinner.getValue()).intValue());
        return new CurrencyLinePresent(currencylineattr);
    }

    public volatile Object updateBean()
    {
        return updateBean();
    }

    public volatile void populateBean(Object obj)
    {
        populateBean((CurrencyLinePresent)obj);
    }


}
