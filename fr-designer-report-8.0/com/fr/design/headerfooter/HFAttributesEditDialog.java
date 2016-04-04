// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.headerfooter;

import com.fr.base.Formula;
import com.fr.base.headerfooter.DateHFElement;
import com.fr.base.headerfooter.FormulaHFElement;
import com.fr.base.headerfooter.HFElement;
import com.fr.base.headerfooter.ImageHFElement;
import com.fr.base.headerfooter.NewLineHFElement;
import com.fr.base.headerfooter.NumberOfPageHFElement;
import com.fr.base.headerfooter.PageNumberHFElement;
import com.fr.base.headerfooter.TextHFElement;
import com.fr.base.headerfooter.TimeHFElement;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.formula.FormulaFactory;
import com.fr.design.formula.UIFormula;
import com.fr.design.gui.frpane.UITabbedPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextarea.UITextArea;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.style.FRFontPane;
import com.fr.design.style.FormatPane;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

// Referenced classes of package com.fr.design.headerfooter:
//            ImagePane, MoveActionListener

public class HFAttributesEditDialog extends BasicPane
{
    private class NewLinePane extends JPanel
    {

        final HFAttributesEditDialog this$0;

        public NewLinePane()
        {
            this$0 = HFAttributesEditDialog.this;
            super();
            setLayout(FRGUIPaneFactory.createBorderLayout());
            UILabel uilabel = new UILabel(Inter.getLocText("HF-NewLine_Des"));
            uilabel.setHorizontalAlignment(0);
            add(uilabel, "Center");
        }
    }

    private class TextPane extends JPanel
    {

        private UITextArea textArea;
        final HFAttributesEditDialog this$0;

        public void populate(String s)
        {
            textArea.setText(s);
        }

        public String update()
        {
            return textArea.getText();
        }

        public TextPane()
        {
            this$0 = HFAttributesEditDialog.this;
            super();
            setLayout(FRGUIPaneFactory.createBorderLayout());
            textArea = new UITextArea();
            add(new JScrollPane(textArea));
        }
    }


    private HFElement hfElement;
    private UITabbedPane tabbedPane;
    private TextPane textPane;
    private JPanel formulaPane;
    private UITextField formulaContentField;
    private FRFontPane frFontPane;
    private FormatPane formatPane;
    private ImagePane imagePane;
    private NewLinePane newLinePane;
    private UIButton moveLeftButton;
    private UIButton moveRightButton;
    private UIButton deleteButton;
    private java.util.List moveActionListeners;

    public HFAttributesEditDialog()
    {
        hfElement = null;
        moveLeftButton = null;
        moveRightButton = null;
        deleteButton = null;
        moveActionListeners = new ArrayList();
        setLayout(new BorderLayout(0, 4));
        JPanel jpanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        add(jpanel);
        JPanel jpanel1 = FRGUIPaneFactory.createBorderLayout_S_Pane();
        jpanel.add(jpanel1, "North");
        JPanel jpanel2 = FRGUIPaneFactory.createMediumHGapFlowInnerContainer_M_Pane();
        jpanel1.add(jpanel2, "West");
        moveLeftButton = new UIButton(Inter.getLocText("HF-Move_Left"));
        moveLeftButton.setMnemonic('L');
        moveLeftButton.addActionListener(new ActionListener() {

            final HFAttributesEditDialog this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                doMoveLeft();
            }

            
            {
                this$0 = HFAttributesEditDialog.this;
                super();
            }
        }
);
        jpanel2.add(moveLeftButton);
        moveRightButton = new UIButton(Inter.getLocText("HF-Move_Right"));
        moveRightButton.setMnemonic('R');
        moveRightButton.addActionListener(new ActionListener() {

            final HFAttributesEditDialog this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                doMoveRight();
            }

            
            {
                this$0 = HFAttributesEditDialog.this;
                super();
            }
        }
);
        jpanel2.add(moveRightButton);
        deleteButton = new UIButton(Inter.getLocText("HF-Delete_it"));
        deleteButton.setMnemonic('D');
        deleteButton.addActionListener(new ActionListener() {

            final HFAttributesEditDialog this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                doDelete();
            }

            
            {
                this$0 = HFAttributesEditDialog.this;
                super();
            }
        }
);
        jpanel2.add(deleteButton);
        tabbedPane = new UITabbedPane();
        jpanel.add(tabbedPane, "Center");
        textPane = new TextPane();
        formulaPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        formulaContentField = new UITextField();
        formulaPane.add(new UILabel((new StringBuilder()).append(Inter.getLocText("Value")).append(":").toString()));
        formulaPane.add(Box.createHorizontalStrut(2));
        UILabel uilabel = new UILabel("=");
        uilabel.setFont(new Font("Dialog", 1, 12));
        formulaPane.add(uilabel);
        formulaContentField = new UITextField(20);
        formulaPane.add(formulaContentField);
        UIButton uibutton = new UIButton("...");
        formulaPane.add(uibutton);
        uibutton.setToolTipText((new StringBuilder()).append(Inter.getLocText("Formula")).append("...").toString());
        uibutton.setPreferredSize(new Dimension(25, formulaContentField.getPreferredSize().height));
        uibutton.addActionListener(new ActionListener() {

            final HFAttributesEditDialog this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                Formula formula = new Formula();
                String s = formulaContentField.getText();
                if(s == null || s.length() <= 0)
                    formula.setContent("");
                else
                    formula.setContent(s);
                final UIFormula formulaPane = FormulaFactory.createFormulaPane();
                formulaPane.populate(formula);
                formulaPane.showLargeWindow(SwingUtilities.getWindowAncestor((Component)formulaPane), new DialogActionAdapter() {

                    final UIFormula val$formulaPane;
                    final _cls4 this$1;

                    public void doOk()
                    {
                        Formula formula1 = formulaPane.update();
                        if(formula1.getContent().length() <= 1)
                            formulaContentField.setText("");
                        else
                            formulaContentField.setText(formula1.getContent().substring(1));
                    }

                    
                    {
                        this$1 = _cls4.this;
                        formulaPane = uiformula;
                        super();
                    }
                }
).setVisible(true);
            }

            
            {
                this$0 = HFAttributesEditDialog.this;
                super();
            }
        }
);
        frFontPane = new FRFontPane();
        formatPane = new FormatPane();
        imagePane = new ImagePane();
        newLinePane = new NewLinePane();
    }

    protected String title4PopupWindow()
    {
        return Inter.getLocText("HF-Header_and_Footer");
    }

    public void populate(HFElement hfelement)
    {
        populate(hfelement, false);
    }

    public void populate(HFElement hfelement, boolean flag)
    {
        hfElement = hfelement;
        tabbedPane.removeAll();
        if(hfElement != null)
            if(ComparatorUtils.equals(hfElement.getClass(), com/fr/base/headerfooter/TextHFElement))
            {
                addTextPaneToTab();
                addReportFontPaneToTab();
                textPane.populate(((TextHFElement)hfelement).getText());
                frFontPane.populate(((TextHFElement)hfelement).getFRFont());
            } else
            if(ComparatorUtils.equals(hfElement.getClass(), com/fr/base/headerfooter/FormulaHFElement))
            {
                addFormulaPaneToTab();
                addReportFontPaneToTab();
                formulaContentField.setText(((FormulaHFElement)hfelement).getFormulaContent());
                frFontPane.populate(((FormulaHFElement)hfelement).getFRFont());
            } else
            if(ComparatorUtils.equals(hfElement.getClass(), com/fr/base/headerfooter/PageNumberHFElement))
            {
                addReportFontPaneToTab();
                frFontPane.populate(((PageNumberHFElement)hfelement).getFRFont());
            } else
            if(ComparatorUtils.equals(hfElement.getClass(), com/fr/base/headerfooter/NumberOfPageHFElement))
            {
                addReportFontPaneToTab();
                frFontPane.populate(((NumberOfPageHFElement)hfelement).getFRFont());
            } else
            if(ComparatorUtils.equals(hfElement.getClass(), com/fr/base/headerfooter/DateHFElement))
            {
                addReportFontPaneToTab();
                addFormatPaneToTab();
                frFontPane.populate(((DateHFElement)hfelement).getFRFont());
                formatPane.populate(((DateHFElement)hfelement).getFormat());
            } else
            if(ComparatorUtils.equals(hfElement.getClass(), com/fr/base/headerfooter/TimeHFElement))
            {
                addReportFontPaneToTab();
                addFormatPaneToTab();
                frFontPane.populate(((TimeHFElement)hfelement).getFRFont());
                formatPane.populate(((TimeHFElement)hfelement).getFormat());
            } else
            if(ComparatorUtils.equals(hfElement.getClass(), com/fr/base/headerfooter/ImageHFElement))
            {
                addImagePaneToTab();
                imagePane.populate(((ImageHFElement)hfelement).getImage());
            } else
            if(ComparatorUtils.equals(hfElement.getClass(), com/fr/base/headerfooter/NewLineHFElement))
                addNewLinePaneToTab();
        tabbedPane.revalidate();
        if(flag)
        {
            moveLeftButton.setEnabled(false);
            moveRightButton.setEnabled(false);
            deleteButton.setEnabled(false);
        } else
        {
            moveLeftButton.setEnabled(true);
            moveRightButton.setEnabled(true);
            deleteButton.setEnabled(true);
        }
    }

    private void addTextPaneToTab()
    {
        tabbedPane.addTab(Inter.getLocText("Text"), textPane);
    }

    private void addFormulaPaneToTab()
    {
        tabbedPane.addTab(Inter.getLocText("Formula"), formulaPane);
    }

    private void addReportFontPaneToTab()
    {
        tabbedPane.addTab(Inter.getLocText("FRFont"), frFontPane);
    }

    private void addFormatPaneToTab()
    {
        tabbedPane.addTab(Inter.getLocText("Format"), formatPane);
    }

    private void addImagePaneToTab()
    {
        tabbedPane.addTab(Inter.getLocText("Image"), imagePane);
    }

    private void addNewLinePaneToTab()
    {
        tabbedPane.addTab(Inter.getLocText("HF-New_Line"), newLinePane);
    }

    public void update()
    {
        if(hfElement == null)
            return;
        if(ComparatorUtils.equals(hfElement.getClass(), com/fr/base/headerfooter/TextHFElement))
        {
            ((TextHFElement)hfElement).setText(textPane.update());
            ((TextHFElement)hfElement).setFRFont(frFontPane.update());
        } else
        if(ComparatorUtils.equals(hfElement.getClass(), com/fr/base/headerfooter/FormulaHFElement))
        {
            ((FormulaHFElement)hfElement).setFormulaContent(formulaContentField.getText());
            ((FormulaHFElement)hfElement).setFRFont(frFontPane.update());
        } else
        if(ComparatorUtils.equals(hfElement.getClass(), com/fr/base/headerfooter/PageNumberHFElement))
            ((PageNumberHFElement)hfElement).setFRFont(frFontPane.update());
        else
        if(ComparatorUtils.equals(hfElement.getClass(), com/fr/base/headerfooter/NumberOfPageHFElement))
            ((NumberOfPageHFElement)hfElement).setFRFont(frFontPane.update());
        else
        if(ComparatorUtils.equals(hfElement.getClass(), com/fr/base/headerfooter/DateHFElement))
        {
            ((DateHFElement)hfElement).setFRFont(frFontPane.update());
            ((DateHFElement)hfElement).setFormat(formatPane.update());
        } else
        if(ComparatorUtils.equals(hfElement.getClass(), com/fr/base/headerfooter/TimeHFElement))
        {
            ((TimeHFElement)hfElement).setFRFont(frFontPane.update());
            ((TimeHFElement)hfElement).setFormat(formatPane.update());
        } else
        if(ComparatorUtils.equals(hfElement.getClass(), com/fr/base/headerfooter/ImageHFElement))
            ((ImageHFElement)hfElement).setImage(imagePane.update());
        else
        if(!ComparatorUtils.equals(hfElement.getClass(), com/fr/base/headerfooter/NewLineHFElement));
    }

    public void doOK()
    {
        update();
        dialogExit();
    }

    public void addMoveActionListener(MoveActionListener moveactionlistener)
    {
        moveActionListeners.add(moveactionlistener);
    }

    public void doMoveLeft()
    {
        int i = 0;
        for(int j = moveActionListeners.size(); i < j; i++)
        {
            MoveActionListener moveactionlistener = (MoveActionListener)moveActionListeners.get(i);
            if(moveactionlistener != null)
                moveactionlistener.doMoveLeft();
        }

    }

    public void doMoveRight()
    {
        int i = 0;
        for(int j = moveActionListeners.size(); i < j; i++)
        {
            MoveActionListener moveactionlistener = (MoveActionListener)moveActionListeners.get(i);
            if(moveactionlistener != null)
                moveactionlistener.doMoveRight();
        }

    }

    public void doDelete()
    {
        int i = 0;
        for(int j = moveActionListeners.size(); i < j; i++)
        {
            MoveActionListener moveactionlistener = (MoveActionListener)moveActionListeners.get(i);
            if(moveactionlistener != null)
                moveactionlistener.doDelete();
        }

    }

    public void dialogExit()
    {
        setVisible(false);
    }

}
