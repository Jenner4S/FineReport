// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.poly;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.base.chart.BaseChartGetter;
import com.fr.base.chart.BaseChartNameID;
import com.fr.design.constants.UIConstants;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.itooltip.MultiLineToolTip;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.dnd.SerializableTransferable;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.report.poly.PolyECBlock;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.dnd.*;
import java.io.Serializable;
import javax.swing.*;

public class PolyComponetsBar extends JToolBar
{
    private class SerIcon extends UIButton
        implements DragGestureListener, DragSourceListener
    {

        private DragSource dragSource;
        private Serializable serializable;
        final PolyComponetsBar this$0;

        public Dimension getPreferredSize()
        {
            return new Dimension(getIcon().getIconWidth(), getIcon().getIconHeight());
        }

        public JToolTip createToolTip()
        {
            MultiLineToolTip multilinetooltip = new MultiLineToolTip();
            multilinetooltip.setComponent(this);
            multilinetooltip.setOpaque(false);
            return multilinetooltip;
        }

        public void dragGestureRecognized(DragGestureEvent draggestureevent)
        {
            SerializableTransferable serializabletransferable = new SerializableTransferable(serializable);
            dragSource.startDrag(draggestureevent, DragSource.DefaultCopyDrop, serializabletransferable, this);
            getModel().setArmed(false);
            getModel().setRollover(false);
            repaint();
        }

        public void dragEnter(DragSourceDragEvent dragsourcedragevent)
        {
        }

        public void dragOver(DragSourceDragEvent dragsourcedragevent)
        {
        }

        public void dropActionChanged(DragSourceDragEvent dragsourcedragevent)
        {
        }

        public void dragExit(DragSourceEvent dragsourceevent)
        {
        }

        public void dragDropEnd(DragSourceDropEvent dragsourcedropevent)
        {
        }

        public SerIcon(Serializable serializable1, String s, String s1)
        {
            this$0 = PolyComponetsBar.this;
            super(BaseUtils.readIcon((new StringBuilder()).append("com/fr/design/images/poly/toolbar/").append(s1).append(".png").toString()));
            serializable = serializable1;
            setToolTipText(s);
            set4ToolbarButton();
            dragSource = new DragSource();
            dragSource.createDefaultDragGestureRecognizer(this, 3, this);
        }
    }


    private static Color FOLDER_PANE_BACKGROUND = new Color(214, 223, 247);
    private BaseChartNameID typeName[];
    private SerIcon serIcons[];

    public PolyComponetsBar()
    {
        typeName = BaseChartGetter.getStaticAllChartBaseNames();
        setOrientation(1);
        setBorder(BorderFactory.createEmptyBorder(4, 4, 0, 4));
        setFloatable(false);
        setBackground(UIConstants.NORMAL_BACKGROUND);
        setLayout(FRGUIPaneFactory.create1ColumnGridLayout());
        serIcons = new SerIcon[typeName.length + 1];
        serIcons[0] = new SerIcon(com/fr/report/poly/PolyECBlock, Inter.getLocText("Poly-Report_Block"), "Poly-Report_Block");
        add(serIcons[0]);
        int i = 0;
        for(int j = typeName.length; i < j; i++)
        {
            com.fr.base.chart.BaseChart abasechart[] = BaseChartGetter.getStaticChartTypes(typeName[i].getPlotID());
            serIcons[i + 1] = new SerIcon(abasechart[0], Inter.getLocText(typeName[i].getName()), typeName[i].getName());
            add(serIcons[i + 1]);
        }

    }

    public void checkEnable()
    {
        SerIcon asericon[] = serIcons;
        int i = asericon.length;
        for(int j = 0; j < i; j++)
        {
            SerIcon sericon = asericon[j];
            sericon.setEnabled(!BaseUtils.isAuthorityEditing());
        }

    }

    public static transient void main(String args[])
    {
        try
        {
            UIManager.setLookAndFeel(new WindowsLookAndFeel());
        }
        catch(UnsupportedLookAndFeelException unsupportedlookandfeelexception)
        {
            FRContext.getLogger().error(unsupportedlookandfeelexception.getMessage(), unsupportedlookandfeelexception);
        }
        JFrame jframe = new JFrame();
        JPanel jpanel = (JPanel)jframe.getContentPane();
        jpanel.setLayout(FRGUIPaneFactory.createBorderLayout());
        PolyComponetsBar polycomponetsbar = new PolyComponetsBar();
        jpanel.add(polycomponetsbar, "Center");
        jframe.setSize(400, 300);
        jframe.setVisible(true);
    }

}
