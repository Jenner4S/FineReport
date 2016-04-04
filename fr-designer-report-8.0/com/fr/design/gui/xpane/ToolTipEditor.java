// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.gui.xpane;

import com.fr.base.background.GradientBackground;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.border.UIRoundedBorder;
import com.fr.design.designer.beans.events.CreatorEventListenerTable;
import com.fr.design.designer.beans.events.DesignerEditor;
import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.designer.creator.*;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.form.ui.*;
import com.fr.general.Background;
import com.fr.stable.ArrayUtils;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.Border;

public class ToolTipEditor extends JWindow
{
    class EditorButton extends UILabel
    {

        final ToolTipEditor this$0;

        public Dimension getPreferredSize()
        {
            return new Dimension(22, 22);
        }

        public EditorButton(final WidgetOption option)
        {
            this$0 = ToolTipEditor.this;
            super(option.optionIcon());
            setToolTipText(option.optionName());
            addMouseListener(new MouseAdapter() {

                final ToolTipEditor val$this$0;
                final WidgetOption val$option;
                final EditorButton this$1;

                public void mouseClicked(MouseEvent mouseevent)
                {
                    setVisible(false);
                    transform(option.createWidget());
                }

                public void mouseEntered(MouseEvent mouseevent)
                {
                    setBorder(buttonBorder);
                }

                public void mouseExited(MouseEvent mouseevent)
                {
                    setBorder(null);
                }


// JavaClassFileOutputException: Invalid index accessing method local variables table of <init>
            }
);
        }
    }

    class EditorLayout
        implements LayoutManager
    {

        int top;
        int left;
        int right;
        int bottom;
        int hgap;
        int vgap;
        int maxLine;
        final ToolTipEditor this$0;

        public void addLayoutComponent(String s, Component component)
        {
        }

        public void layoutContainer(Container container)
        {
            synchronized(container.getTreeLock())
            {
                Insets insets = container.getInsets();
                int i = container.getComponentCount();
                for(int j = 0; j < i; j++)
                {
                    Component component = container.getComponent(j);
                    if(component.isVisible())
                    {
                        Dimension dimension = component.getPreferredSize();
                        component.setBounds(insets.left + left + (j % maxLine) * (hgap + dimension.width), top + insets.top + (j / maxLine) * (vgap + dimension.height), dimension.width, dimension.height);
                    }
                }

            }
        }

        public Dimension minimumLayoutSize(Container container)
        {
            return new Dimension(0, 0);
        }

        public Dimension preferredLayoutSize(Container container)
        {
            Insets insets = container.getInsets();
            int i = container.getComponentCount();
            return new Dimension(maxLine * Num + insets.left + insets.right + right + left, (((i + maxLine) - 1) / maxLine) * Num + insets.top + insets.bottom + top + bottom);
        }

        public void removeLayoutComponent(Component component)
        {
        }

        EditorLayout()
        {
            this$0 = ToolTipEditor.this;
            super();
            top = 4;
            left = 4;
            right = 4;
            bottom = 4;
            hgap = 2;
            vgap = 4;
            maxLine = 9;
        }
    }

    class EditorChoosePane extends JPanel
    {

        private Background background;
        final ToolTipEditor this$0;

        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            if(!isOpaque())
            {
                return;
            } else
            {
                Rectangle rectangle = getBounds();
                background.paint(g, new java.awt.geom.RoundRectangle2D.Double(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight(), 5D, 5D));
                return;
            }
        }

        protected void initComponents()
        {
            WidgetOption awidgetoption[] = WidgetOption.getFormWidgetIntance();
            awidgetoption = (WidgetOption[])(WidgetOption[])ArrayUtils.addAll(awidgetoption, ExtraDesignClassManager.getInstance().getParameterWidgetOptions());
            WidgetOption awidgetoption1[] = awidgetoption;
            int i = awidgetoption1.length;
            for(int j = 0; j < i; j++)
            {
                WidgetOption widgetoption = awidgetoption1[j];
                if(com/fr/form/ui/DataControl.isAssignableFrom(widgetoption.widgetClass()))
                    add(new EditorButton(widgetoption));
            }

            setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2), new UIRoundedBorder(new Color(139, 139, 140), 1, 5)));
        }

        public EditorChoosePane()
        {
            this$0 = ToolTipEditor.this;
            super();
            background = new GradientBackground(Color.white, new Color(234, 246, 254), 1);
            setLayout(new EditorLayout());
            initComponents();
        }
    }


    private static ToolTipEditor editor = new ToolTipEditor();
    private XEditorHolder holder;
    private Border buttonBorder;
    private int Num;

    public static ToolTipEditor getInstance()
    {
        return editor;
    }

    private ToolTipEditor()
    {
        buttonBorder = new UIRoundedBorder(new Color(149, 149, 149), 1, 5);
        Num = 24;
        initComponents();
    }

    private void setEditor(XEditorHolder xeditorholder)
    {
        holder = xeditorholder;
    }

    private void transform(Widget widget)
    {
        XLayoutContainer xlayoutcontainer = XCreatorUtils.getParentXLayoutContainer(holder);
        EditorHolder editorholder = (EditorHolder)holder.toData();
        widget.setWidgetName(editorholder.getWidgetName());
        widget.setEnabled(editorholder.isEnabled());
        widget.setVisible(editorholder.isVisible());
        WidgetValue.convertWidgetValue((DataControl)widget, editorholder.getWidgetValue().getValue());
        XCreator xcreator = null;
        xcreator = xlayoutcontainer.replace(widget, holder);
        Container container = ((UILabel)holder.getDesignerEditor().getEditorTarget()).getParent();
        if(container instanceof FormDesigner)
        {
            ((FormDesigner)container).getEditListenerTable().fireCreatorModified(xcreator, 5);
            ((FormDesigner)container).getSelectionModel().setSelectedCreator(xcreator);
        }
    }

    public void showToolTip(XEditorHolder xeditorholder, int i, int j)
    {
        setEditor(xeditorholder);
        if(!isVisible())
        {
            setVisible(true);
            if(i + getWidth() > Toolkit.getDefaultToolkit().getScreenSize().width)
                i -= getWidth();
            setLocation(i, j);
        }
    }

    public boolean isEditorVisible()
    {
        return isVisible();
    }

    public void hideToolTip()
    {
        setVisible(false);
    }

    public void resetBounds(XEditorHolder xeditorholder, Rectangle rectangle, Rectangle rectangle1)
    {
        if(isVisible() && holder == xeditorholder && !GUICoreUtils.isTheSameRect(rectangle, rectangle1))
            setVisible(false);
    }

    protected void initComponents()
    {
        EditorChoosePane editorchoosepane = new EditorChoosePane();
        getContentPane().add(editorchoosepane);
        setSize(editorchoosepane.getPreferredSize());
    }




}
