// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator;

import com.fr.base.BaseUtils;
import com.fr.design.designer.beans.AdapterBus;
import com.fr.design.designer.beans.ComponentAdapter;
import com.fr.design.designer.beans.events.DesignerEditor;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.xpane.ToolTipEditor;
import com.fr.design.mainframe.EditingMouseListener;
import com.fr.design.mainframe.FormDesigner;
import com.fr.form.ui.EditorHolder;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// Referenced classes of package com.fr.design.designer.creator:
//            XWidgetCreator

public class XEditorHolder extends XWidgetCreator
{

    private DesignerEditor designerEditor;
    private static Icon icon = BaseUtils.readIcon("/com/fr/design/images/form/designer/holder.png");

    public XEditorHolder(EditorHolder editorholder, Dimension dimension)
    {
        super(editorholder, dimension);
    }

    public void respondClick(EditingMouseListener editingmouselistener, MouseEvent mouseevent)
    {
        FormDesigner formdesigner = editingmouselistener.getDesigner();
        if(editingmouselistener.stopEditing())
        {
            ComponentAdapter componentadapter = AdapterBus.getComponentAdapter(formdesigner, this);
            editingmouselistener.startEditing(this, componentadapter.getDesignerEditor(), componentadapter);
            Rectangle rectangle = getBounds();
            int i = (rectangle.x + rectangle.width / 2) - editingmouselistener.getMinMoveSize();
            int j = rectangle.x + rectangle.width / 2 + editingmouselistener.getMinMoveSize();
            if(mouseevent.getX() > i && mouseevent.getX() < j)
                ToolTipEditor.getInstance().showToolTip(this, mouseevent.getXOnScreen(), mouseevent.getYOnScreen());
        }
    }

    protected String getIconName()
    {
        return "text_field_16.png";
    }

    public DesignerEditor getDesignerEditor()
    {
        if(designerEditor == null)
        {
            UILabel uilabel = new UILabel(icon);
            designerEditor = new DesignerEditor(uilabel);
            uilabel.addFocusListener(new FocusListener() {

                final XEditorHolder this$0;

                public void focusGained(FocusEvent focusevent)
                {
                }

                public void focusLost(FocusEvent focusevent)
                {
                    ToolTipEditor.getInstance().hideToolTip();
                }

            
            {
                this$0 = XEditorHolder.this;
                super();
            }
            }
);
            uilabel.setBorder(BorderFactory.createLineBorder(new Color(128, 152, 186)));
        }
        return designerEditor;
    }

    protected JComponent initEditor()
    {
        if(editor == null)
        {
            editor = new UILabel(icon);
            editor.setBorder(BorderFactory.createLineBorder(new Color(128, 152, 186)));
        }
        return editor;
    }

}
