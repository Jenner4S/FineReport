// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.cell.editor;

import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextarea.UITextArea;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.grid.Grid;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.cell.painter.BiasTextPainter;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

// Referenced classes of package com.fr.design.cell.editor:
//            AbstractCellEditor

public class BiasTextPainterCellEditor extends AbstractCellEditor
{
    public static class BiasTextPainterPane extends BasicPane
    {

        private UITextArea formulaTextArea;
        private boolean isBackSlash;
        private UIRadioButton choice1;
        private UIRadioButton choice2;
        ActionListener leftUp;
        ActionListener leftDown;

        protected String title4PopupWindow()
        {
            return Inter.getLocText("BiasD-Slope_Line");
        }

        public void populate(BiasTextPainter biastextpainter)
        {
            if(biastextpainter == null)
                return;
            isBackSlash = biastextpainter.isBackSlash();
            if(isBackSlash)
                choice2.setSelected(true);
            else
                choice1.setSelected(true);
            formulaTextArea.setText(biastextpainter.getText());
        }

        public BiasTextPainter update()
        {
            BiasTextPainter biastextpainter = new BiasTextPainter(formulaTextArea.getText());
            biastextpainter.setIsBackSlash(isBackSlash);
            return biastextpainter;
        }




        public BiasTextPainterPane()
        {
            leftUp = new ActionListener() {

                final BiasTextPainterPane this$0;

                public void actionPerformed(ActionEvent actionevent)
                {
                    if(ComparatorUtils.equals(actionevent.getSource(), choice1))
                        isBackSlash = false;
                }

                
                {
                    this$0 = BiasTextPainterPane.this;
                    super();
                }
            }
;
            leftDown = new ActionListener() {

                final BiasTextPainterPane this$0;

                public void actionPerformed(ActionEvent actionevent)
                {
                    if(ComparatorUtils.equals(actionevent.getSource(), choice2))
                        isBackSlash = true;
                }

                
                {
                    this$0 = BiasTextPainterPane.this;
                    super();
                }
            }
;
            BiasTextPainterPane biastextpainterpane = this;
            JPanel jpanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
            biastextpainterpane.add(jpanel, "Center");
            JPanel jpanel1 = FRGUIPaneFactory.createBorderLayout_S_Pane();
            jpanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
            jpanel.add(jpanel1, "Center");
            UILabel uilabel = new UILabel(Inter.getLocText(new String[] {
                "BiasD-Input_Slope_Line_text_separated_by_'|'", "Example"
            }, new String[] {
                " ", ": Season|Product"
            }));
            jpanel1.add(uilabel, "North");
            uilabel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 0));
            jpanel1.add(Box.createVerticalStrut(2), "South");
            formulaTextArea = new UITextArea();
            JScrollPane jscrollpane = new JScrollPane(formulaTextArea);
            jscrollpane.setBorder(null);
            jpanel1.add(jscrollpane, "Center");
            JPanel jpanel2 = FRGUIPaneFactory.createX_AXISBoxInnerContainer_S_Pane();
            choice1 = new UIRadioButton(Inter.getLocText("BiasD-From-upper_left_to_lower_right"));
            choice2 = new UIRadioButton(Inter.getLocText("BiasD-From-lower_left_to_upper_right"));
            choice1.addActionListener(leftUp);
            choice2.addActionListener(leftDown);
            ButtonGroup buttongroup = new ButtonGroup();
            buttongroup.add(choice1);
            buttongroup.add(choice2);
            jpanel2.add(choice1);
            jpanel2.add(choice2);
            jpanel.add(jpanel2, "South");
        }
    }


    private BiasTextPainterPane biasTextPainterPane;

    public BiasTextPainterCellEditor(ElementCasePane elementcasepane)
    {
        super(elementcasepane);
        biasTextPainterPane = null;
    }

    public Object getCellEditorValue()
        throws Exception
    {
        if(biasTextPainterPane == null)
            return null;
        else
            return biasTextPainterPane.update();
    }

    public Component getCellEditorComponent(Grid grid, TemplateCellElement templatecellelement, int i)
    {
        Object obj = null;
        if(templatecellelement != null)
            obj = templatecellelement.getValue();
        if(obj == null || !(obj instanceof BiasTextPainter))
            obj = new BiasTextPainter("");
        BiasTextPainter biastextpainter = (BiasTextPainter)obj;
        java.awt.Window window = SwingUtilities.getWindowAncestor(grid);
        biasTextPainterPane = new BiasTextPainterPane();
        biasTextPainterPane.populate(biastextpainter);
        return biasTextPainterPane.showSmallWindow(window, new DialogActionAdapter() {

            final BiasTextPainterCellEditor this$0;

            public void doOk()
            {
                fireEditingStopped();
            }

            public void doCancel()
            {
                fireEditingCanceled();
            }

            
            {
                this$0 = BiasTextPainterCellEditor.this;
                super();
            }
        }
);
    }
}
