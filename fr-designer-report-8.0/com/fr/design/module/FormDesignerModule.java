// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.module;

import com.fr.base.FRContext;
import com.fr.base.io.IOFile;
import com.fr.base.io.XMLEncryptUtils;
import com.fr.design.DesignerEnvManager;
import com.fr.design.form.parameter.FormParaDesigner;
import com.fr.design.mainframe.*;
import com.fr.design.mainframe.actions.NewFormAction;
import com.fr.design.parameter.FormParameterReader;
import com.fr.design.parameter.ParameterPropertyPane;
import com.fr.design.widget.ui.btn.FormSubmitButtonDetailPane;
import com.fr.file.FILE;
import com.fr.form.main.Form;
import com.fr.general.*;
import com.fr.stable.bridge.StableFactory;
import java.util.HashMap;

// Referenced classes of package com.fr.design.module:
//            DesignModule, DesignModuleFactory

public class FormDesignerModule extends DesignModule
{

    public FormDesignerModule()
    {
    }

    public void start()
    {
        super.start();
        ModuleContext.startModule("com.fr.design.module.ChartDesignerModule");
        StableFactory.registerMarkedObject("DesignToolbarProvider", WidgetToolBarPane.getInstance());
        DesignModuleFactory.registerNewFormActionClass(com/fr/design/mainframe/actions/NewFormAction);
        DesignModuleFactory.registerFormParaDesignerClass(com/fr/design/form/parameter/FormParaDesigner);
        DesignModuleFactory.registerParaPropertyPaneClass(com/fr/design/parameter/ParameterPropertyPane);
        DesignModuleFactory.registerFormHierarchyPaneClass(com/fr/design/mainframe/FormHierarchyTreePane);
        DesignModuleFactory.registerWidgetPropertyPaneClass(com/fr/design/mainframe/WidgetPropertyPane);
        DesignModuleFactory.registerButtonDetailPaneClass(com/fr/design/widget/ui/btn/FormSubmitButtonDetailPane);
        DesignModuleFactory.registerParameterReader(new FormParameterReader());
        registerData4Designer();
    }

    private void registerData4Designer()
    {
        StableFactory.registerMarkedClass("JForm", com/fr/design/mainframe/JForm);
    }

    public App[] apps4TemplateOpener()
    {
        return (new App[] {
            new AbstractAppProvider() {

                final FormDesignerModule this$0;

                public String[] defaultExtentions()
                {
                    return (new String[] {
                        "frm", "form"
                    });
                }

                public JTemplate openTemplate(FILE file)
                {
                    HashMap hashmap = new HashMap();
                    hashmap.put("0", com/fr/form/main/Form);
                    hashmap.put("1", com/fr/file/FILE);
                    return (JTemplate)StableFactory.getMarkedInstanceObjectFromClass("JForm", new Object[] {
                        asIOFile(file), file
                    }, hashmap, com/fr/design/mainframe/BaseJForm);
                }

                public Form asIOFile(FILE file)
                {
                    if(XMLEncryptUtils.isCptEncoded() && !XMLEncryptUtils.checkVaild(DesignerEnvManager.getEnvManager().getEncryptionKey()) && !(new DecodeDialog(file)).isPwdRight())
                    {
                        FRContext.getLogger().error(Inter.getLocText("FR-Engine_ECP_error_pwd"));
                        return new Form();
                    }
                    Form form = new Form();
                    FRContext.getLogger().info(Inter.getLocText(new String[] {
                        "LOG-Is_Being_Openned", "LOG-Please_Wait"
                    }, new String[] {
                        (new StringBuilder()).append("\"").append(file.getName()).append("\"").append(",").toString(), "..."
                    }));
                    try
                    {
                        form.readStream(file.asInputStream());
                    }
                    catch(Exception exception)
                    {
                        FRContext.getLogger().error((new StringBuilder()).append("Failed to generate frm from ").append(file).toString(), exception);
                        return null;
                    }
                    return form;
                }

                public volatile IOFile asIOFile(FILE file)
                {
                    return asIOFile(file);
                }

            
            {
                this$0 = FormDesignerModule.this;
                super();
            }
            }

        });
    }

    public String getInterNationalName()
    {
        return Inter.getLocText("FR-Designer_formDesignerModule");
    }
}
