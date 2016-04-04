// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.chart.gui;

import com.fr.base.ScreenResolution;
import com.fr.chart.base.GlyphUtils;
import com.fr.chart.base.TextAttr;
import com.fr.design.constants.UIConstants;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRFont;
import com.fr.stable.StringUtils;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Dimension2D;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

public class UIEditLabel extends JPanel
    implements UIObserver
{

    private static final int OFF_LEFT = 10;
    private UITextField currentEditingEditor;
    private UILabel showLabel;
    private boolean isEditingStopped;
    private String originalLabel;
    private DocumentListener documentListener = new DocumentListener() {

        final UIEditLabel this$0;

        public void insertUpdate(DocumentEvent documentevent)
        {
            fireChange();
        }

        public void removeUpdate(DocumentEvent documentevent)
        {
            fireChange();
        }

        public void changedUpdate(DocumentEvent documentevent)
        {
            fireChange();
        }

            
            {
                this$0 = UIEditLabel.this;
                super();
            }
    }
;
    private AWTEventListener awt = new AWTEventListener() {

        final UIEditLabel this$0;

        public void eventDispatched(AWTEvent awtevent)
        {
            if(!isShowing())
            {
                return;
            } else
            {
                doSomeInAll(awtevent);
                return;
            }
        }

            
            {
                this$0 = UIEditLabel.this;
                super();
            }
    }
;
    private UIObserverListener observerListener;

    private void fireChange()
    {
        setText(currentEditingEditor.getText());
        observerListener.doChange();
    }

    private void doSomeInAll(AWTEvent awtevent)
    {
        Rectangle rectangle = new Rectangle(getLocationOnScreen().x, getLocationOnScreen().y, getWidth(), getHeight());
        if(awtevent instanceof MouseEvent)
        {
            MouseEvent mouseevent = (MouseEvent)awtevent;
            if(mouseevent.getClickCount() > 0)
            {
                Point point = new Point((int)mouseevent.getLocationOnScreen().getX() - 20, (int)mouseevent.getLocationOnScreen().getY());
                if(!rectangle.contains(point) && !isEditingStopped)
                    stopEditing();
            }
        }
    }

    public UIEditLabel(String s, int i)
    {
        currentEditingEditor = null;
        isEditingStopped = true;
        originalLabel = "";
        initComponents();
        showLabel.setHorizontalAlignment(i);
        originalLabel = s;
        setText(s);
    }

    public UIEditLabel(int i)
    {
        currentEditingEditor = null;
        isEditingStopped = true;
        originalLabel = "";
        initComponents();
        showLabel.setHorizontalAlignment(i);
    }

    private void initComponents()
    {
        showLabel = new UILabel();
        currentEditingEditor = new UITextField();
        setLayout(new BorderLayout());
        add(showLabel, "Center");
        Toolkit.getDefaultToolkit().addAWTEventListener(awt, 16L);
        showLabel.addMouseListener(new MouseAdapter() {

            final UIEditLabel this$0;

            public void mousePressed(MouseEvent mouseevent)
            {
                doAfterMousePress();
                isEditingStopped = false;
                removeAll();
                currentEditingEditor.getDocument().removeDocumentListener(documentListener);
                currentEditingEditor.setText(getText());
                currentEditingEditor.getDocument().addDocumentListener(documentListener);
                add(currentEditingEditor, "Center");
                setBackground(UIConstants.FLESH_BLUE);
                revalidate();
            }

            public void mouseEntered(MouseEvent mouseevent)
            {
                showLabel.setToolTipText(getTooltip());
                setBackground(UIConstants.FLESH_BLUE);
            }

            public void mouseExited(MouseEvent mouseevent)
            {
                showLabel.setToolTipText(getTooltip());
                setBackground(UIConstants.NORMAL_BACKGROUND);
            }

            
            {
                this$0 = UIEditLabel.this;
                super();
            }
        }
);
        currentEditingEditor.addKeyListener(new KeyAdapter() {

            final UIEditLabel this$0;

            public void keyPressed(KeyEvent keyevent)
            {
                if(keyevent.getKeyCode() == 10)
                    stopEditing();
            }

            
            {
                this$0 = UIEditLabel.this;
                super();
            }
        }
);
        currentEditingEditor.getDocument().addDocumentListener(documentListener);
    }

    protected void doAfterMousePress()
    {
    }

    public void resetNomalrBackground()
    {
        setBackground(UIConstants.NORMAL_BACKGROUND);
    }

    private String getTooltip()
    {
        String s = showLabel.getText();
        double d = GlyphUtils.calculateTextDimensionWithNoRotation(s, new TextAttr(FRFont.getInstance()), ScreenResolution.getScreenResolution()).getWidth();
        if(d <= (double)showLabel.getWidth())
            return null;
        else
            return s;
    }

    private void stopEditing()
    {
        isEditingStopped = true;
        removeAll();
        String s = showLabel.getText();
        s = StringUtils.cutStringEndWith(s, ":");
        s = ComparatorUtils.equals(s, originalLabel) ? originalLabel : StringUtils.perfectEnd(s, (new StringBuilder()).append("(").append(originalLabel).append(")").toString());
        showLabel.setText(StringUtils.perfectEnd(s, ":"));
        add(showLabel, "Center");
        revalidate();
        repaint();
    }

    public String getText()
    {
        String s = showLabel.getText();
        s = StringUtils.cutStringEndWith(s, ":");
        return StringUtils.cutStringEndWith(s, (new StringBuilder()).append("(").append(originalLabel).append(")").toString());
    }

    public void setText(String s)
    {
        if(s == null || StringUtils.isEmpty(s))
        {
            showLabel.setText(originalLabel);
            return;
        }
        if(!ComparatorUtils.equals(s, originalLabel))
        {
            s = StringUtils.cutStringEndWith(s, ":");
            s = StringUtils.perfectEnd(s, (new StringBuilder()).append("(").append(originalLabel).append(")").toString());
        }
        s = StringUtils.perfectEnd(s, ":");
        showLabel.setText(s);
    }

    public void registerChangeListener(UIObserverListener uiobserverlistener)
    {
        observerListener = uiobserverlistener;
    }

    public boolean shouldResponseChangeListener()
    {
        return true;
    }








}
