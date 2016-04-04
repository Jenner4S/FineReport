// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.mainframe.widget.accessibles;

import com.fr.base.Env;
import com.fr.base.FRContext;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.widget.editors.ITextComponent;
import com.fr.design.mainframe.widget.renderer.IconCellRenderer;
import com.fr.design.mainframe.widget.wrappers.IconWrapper;
import com.fr.design.web.CustomIconPane;
import com.fr.form.ui.WidgetManager;
import com.fr.general.FRLogger;

// Referenced classes of package com.fr.design.mainframe.widget.accessibles:
//            UneditableAccessibleEditor, RendererField

public class AccessibleIconEditor extends UneditableAccessibleEditor
{

    private CustomIconPane customIconPane;

    public AccessibleIconEditor()
    {
        super(new IconWrapper());
    }

    protected ITextComponent createTextField()
    {
        return new RendererField(new IconCellRenderer());
    }

    protected void showEditorPane()
    {
        if(customIconPane == null)
            customIconPane = new CustomIconPane();
        BasicDialog basicdialog = customIconPane.showWindow(DesignerContext.getDesignerFrame());
        basicdialog.addDialogActionListener(new DialogActionAdapter() {

            final AccessibleIconEditor this$0;

            public void doOk()
            {
                setValue(customIconPane.update());
                fireStateChanged();
                Env env = FRContext.getCurrentEnv();
                try
                {
                    env.writeResource(WidgetManager.getProviderInstance());
                }
                catch(Exception exception)
                {
                    FRContext.getLogger().error(exception.getMessage(), exception);
                }
            }

            
            {
                this$0 = AccessibleIconEditor.this;
                super();
            }
        }
);
        customIconPane.populate((String)getValue());
        basicdialog.setVisible(true);
    }

}
