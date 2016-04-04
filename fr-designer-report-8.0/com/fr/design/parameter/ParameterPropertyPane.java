// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.parameter;

import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.FormHierarchyTreePane;
import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.*;

// Referenced classes of package com.fr.design.parameter:
//            ParameterToolBarPane, ParaDefinitePane

public class ParameterPropertyPane extends JPanel
{

    private ParameterToolBarPane toolbarPane;
    private ParaDefinitePane paraPane;
    public static ParameterPropertyPane THIS;
    private boolean isEditing;

    public static final ParameterPropertyPane getInstance()
    {
        if(THIS == null)
            THIS = new ParameterPropertyPane();
        return THIS;
    }

    public static final ParameterPropertyPane getInstance(FormDesigner formdesigner)
    {
        if(THIS == null)
            THIS = new ParameterPropertyPane();
        THIS.setEditor(formdesigner);
        return THIS;
    }

    public void repaintContainer()
    {
        validate();
        repaint();
        revalidate();
    }

    public ParameterPropertyPane()
    {
        isEditing = false;
        toolbarPane = new ParameterToolBarPane();
        initParameterListener();
        setLayout(new BorderLayout(0, 6));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(toolbarPane, "North");
    }

    private void setEditor(FormDesigner formdesigner)
    {
        remove(FormHierarchyTreePane.getInstance());
        add(FormHierarchyTreePane.getInstance(formdesigner), "Center");
    }

    private void initParameterListener()
    {
        toolbarPane.setParaMouseListener(new MouseAdapter() {

            final ParameterPropertyPane this$0;

            public void mouseClicked(MouseEvent mouseevent)
            {
                if(paraPane == null)
                {
                    return;
                } else
                {
                    final UIButton parameterSelectedLabel = (UIButton)mouseevent.getSource();
                    SwingUtilities.invokeLater(new Runnable() {

                        final UIButton val$parameterSelectedLabel;
                        final _cls1 this$1;

                        public void run()
                        {
                            if(paraPane.isWithQueryButton())
                                paraPane.addingParameter2Editor(toolbarPane.getTargetParameter(parameterSelectedLabel));
                            else
                                paraPane.addingParameter2EditorWithQueryButton(toolbarPane.getTargetParameter(parameterSelectedLabel));
                        }

                    
                    {
                        this$1 = _cls1.this;
                        parameterSelectedLabel = uibutton;
                        super();
                    }
                    }
);
                    return;
                }
            }

            
            {
                this$0 = ParameterPropertyPane.this;
                super();
            }
        }
);
        toolbarPane.addActionListener(new ActionListener() {

            final ParameterPropertyPane this$0;

            public void actionPerformed(ActionEvent actionevent)
            {
                if(paraPane == null)
                {
                    return;
                } else
                {
                    paraPane.addingAllParameter2Editor();
                    return;
                }
            }

            
            {
                this$0 = ParameterPropertyPane.this;
                super();
            }
        }
);
    }

    public ParameterToolBarPane getParameterToolbarPane()
    {
        return toolbarPane;
    }

    public void populateBean(ParaDefinitePane paradefinitepane)
    {
        isEditing = false;
        paraPane = paradefinitepane;
        isEditing = true;
    }


}
