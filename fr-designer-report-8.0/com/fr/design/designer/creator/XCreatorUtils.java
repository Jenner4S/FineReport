// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 

package com.fr.design.designer.creator;

import com.fr.base.FRContext;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.designer.creator.cardlayout.XCardAddButton;
import com.fr.design.designer.creator.cardlayout.XCardSwitchButton;
import com.fr.design.designer.creator.cardlayout.XWCardLayout;
import com.fr.design.designer.creator.cardlayout.XWCardMainBorderLayout;
import com.fr.design.designer.creator.cardlayout.XWCardTagLayout;
import com.fr.design.designer.creator.cardlayout.XWCardTitleLayout;
import com.fr.design.designer.creator.cardlayout.XWTabFitLayout;
import com.fr.design.module.DesignModuleFactory;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.form.parameter.FormSubmitButton;
import com.fr.form.ui.*;
import com.fr.form.ui.container.*;
import com.fr.form.ui.container.cardlayout.*;
import com.fr.general.FRLogger;
import com.fr.general.IOUtils;
import com.fr.stable.StringUtils;
import java.awt.Container;
import java.awt.Dimension;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Icon;

// Referenced classes of package com.fr.design.designer.creator:
//            XChartEditor, NullCreator, XCreator, XLayoutContainer, 
//            XNameWidget, XTextEditor, XTextArea, XNumberEditor, 
//            XButton, XCheckBox, XCheckBoxGroup, XComboBox, 
//            XComboCheckBox, XDateEditor, XFileUploader, XTableEditor, 
//            XIframeEditor, XLabel, XListEditor, XTableTree, 
//            XMultiFileUploader, XPassword, XRadio, XRadioGroup, 
//            XTreeEditor, XTreeComboBoxEditor, XEditorHolder, XDataTable, 
//            XElementCase, XWAbsoluteLayout, XWParameterLayout, XWHorizontalBoxLayout, 
//            XWBorderLayout, XWVerticalBoxLayout, XWHorizontalSplitLayout, XWVerticalSplitLayout, 
//            XWGridLayout, XWFitLayout, XWScaleLayout, XWTitleLayout

public class XCreatorUtils
{

    public static Map objectMap;
    public static Map xLayoutMap;

    public XCreatorUtils()
    {
    }

    private static void putExtraEditor()
    {
        if(DesignModuleFactory.getChartEditorClass() != null)
            objectMap.put(DesignModuleFactory.getChartEditorClass(), com/fr/design/designer/creator/XChartEditor);
    }

    private static Class searchXCreatorClass(Class class1)
    {
        Class class2 = (Class)objectMap.get(class1);
        if(class2 == null)
            class2 = (Class)xLayoutMap.get(class1);
        return class2;
    }

    public static XCreator createXCreator(Widget widget)
    {
        return createXCreator(widget, new Dimension());
    }

    public static XCreator createXCreator(Widget widget, Dimension dimension)
    {
        Object obj;
        if(widget == null)
        {
            obj = com/fr/design/designer/creator/NullCreator;
        } else
        {
            Class class1 = widget.getClass();
            obj = searchXCreatorClass(class1);
            if(obj == null)
            {
                FRContext.getLogger().error((new StringBuilder()).append(widget).append("'s").append(" xcreator doesn't exsit!").toString());
                obj = com/fr/design/designer/creator/NullCreator;
            }
        }
        Object obj1 = null;
        Constructor aconstructor[] = ((Class) (obj)).getConstructors();
        Constructor aconstructor1[] = aconstructor;
        int i = aconstructor1.length;
        int j = 0;
        do
        {
            if(j >= i)
                break;
            Constructor constructor = aconstructor1[j];
            try
            {
                obj1 = (XCreator)constructor.newInstance(new Object[] {
                    widget, dimension
                });
                break;
            }
            catch(Exception exception)
            {
                j++;
            }
        } while(true);
        if(obj1 == null)
        {
            FRContext.getLogger().error("Error to create xcreator!");
            obj1 = new NullCreator(widget, dimension);
        }
        return ((XCreator) (obj1));
    }

    public static void refreshAllNameWidgets(XLayoutContainer xlayoutcontainer)
    {
        _refreshNameWidget(xlayoutcontainer);
        LayoutUtils.layoutRootContainer(xlayoutcontainer);
    }

    private static void _refreshNameWidget(XLayoutContainer xlayoutcontainer)
    {
        int i = 0;
        for(int j = xlayoutcontainer.getXCreatorCount(); i < j; i++)
        {
            XCreator xcreator = xlayoutcontainer.getXCreator(i);
            if(xcreator instanceof XLayoutContainer)
            {
                _refreshNameWidget((XLayoutContainer)xcreator);
                continue;
            }
            if(xcreator instanceof XNameWidget)
                ((XNameWidget)xcreator).rebuild();
        }

    }

    public static XLayoutContainer getParentXLayoutContainer(XCreator xcreator)
    {
        for(Container container = xcreator.getParent(); container != null; container = container.getParent())
        {
            XCreator xcreator1 = (XCreator)container;
            if(xcreator1.isDedicateContainer())
                return (XLayoutContainer)container.getParent();
            if(container instanceof XLayoutContainer)
                return (XLayoutContainer)container;
        }

        return null;
    }

    public static XLayoutContainer getHotspotContainer(XCreator xcreator)
    {
        if(xcreator.isDedicateContainer())
            return (XLayoutContainer)xcreator.getParent();
        if(xcreator instanceof XLayoutContainer)
            return (XLayoutContainer)xcreator;
        else
            return getParentXLayoutContainer(xcreator);
    }

    public static Icon getCreatorIcon(XCreator xcreator)
    {
        String s = xcreator.getIconPath();
        if(StringUtils.isEmpty(s))
            return null;
        else
            return IOUtils.readIcon(s);
    }

    static 
    {
        objectMap = new HashMap();
        xLayoutMap = new HashMap();
        objectMap.put(com/fr/form/ui/TextEditor, com/fr/design/designer/creator/XTextEditor);
        objectMap.put(com/fr/form/ui/TextArea, com/fr/design/designer/creator/XTextArea);
        objectMap.put(com/fr/form/ui/NumberEditor, com/fr/design/designer/creator/XNumberEditor);
        objectMap.put(com/fr/form/ui/FreeButton, com/fr/design/designer/creator/XButton);
        objectMap.put(com/fr/form/ui/CheckBox, com/fr/design/designer/creator/XCheckBox);
        objectMap.put(com/fr/form/ui/CheckBoxGroup, com/fr/design/designer/creator/XCheckBoxGroup);
        objectMap.put(com/fr/form/ui/ComboBox, com/fr/design/designer/creator/XComboBox);
        objectMap.put(com/fr/form/ui/ComboCheckBox, com/fr/design/designer/creator/XComboCheckBox);
        objectMap.put(com/fr/form/ui/DateEditor, com/fr/design/designer/creator/XDateEditor);
        objectMap.put(com/fr/form/ui/FileEditor, com/fr/design/designer/creator/XFileUploader);
        objectMap.put(com/fr/form/ui/Table, com/fr/design/designer/creator/XTableEditor);
        objectMap.put(com/fr/form/ui/IframeEditor, com/fr/design/designer/creator/XIframeEditor);
        objectMap.put(com/fr/form/ui/FreeButton, com/fr/design/designer/creator/XButton);
        objectMap.put(com/fr/form/parameter/FormSubmitButton, com/fr/design/designer/creator/XButton);
        objectMap.put(com/fr/form/ui/Button, com/fr/design/designer/creator/XButton);
        objectMap.put(com/fr/form/ui/Label, com/fr/design/designer/creator/XLabel);
        objectMap.put(com/fr/form/ui/ListEditor, com/fr/design/designer/creator/XListEditor);
        objectMap.put(com/fr/form/ui/TableTree, com/fr/design/designer/creator/XTableTree);
        objectMap.put(com/fr/form/ui/MultiFileEditor, com/fr/design/designer/creator/XMultiFileUploader);
        objectMap.put(com/fr/form/ui/Password, com/fr/design/designer/creator/XPassword);
        objectMap.put(com/fr/form/ui/Radio, com/fr/design/designer/creator/XRadio);
        objectMap.put(com/fr/form/ui/RadioGroup, com/fr/design/designer/creator/XRadioGroup);
        objectMap.put(com/fr/form/ui/TreeEditor, com/fr/design/designer/creator/XTreeEditor);
        objectMap.put(com/fr/form/ui/TreeComboBoxEditor, com/fr/design/designer/creator/XTreeComboBoxEditor);
        objectMap.put(com/fr/form/ui/EditorHolder, com/fr/design/designer/creator/XEditorHolder);
        objectMap.put(com/fr/form/ui/DataTable, com/fr/design/designer/creator/XDataTable);
        objectMap.put(com/fr/form/ui/ElementCaseEditor, com/fr/design/designer/creator/XElementCase);
        objectMap.put(com/fr/form/ui/NameWidget, com/fr/design/designer/creator/XNameWidget);
        objectMap.put(com/fr/form/ui/CardSwitchButton, com/fr/design/designer/creator/cardlayout/XCardSwitchButton);
        objectMap.put(com/fr/form/ui/CardAddButton, com/fr/design/designer/creator/cardlayout/XCardAddButton);
        putExtraEditor();
        xLayoutMap.put(com/fr/form/ui/container/WAbsoluteLayout, com/fr/design/designer/creator/XWAbsoluteLayout);
        xLayoutMap.put(com/fr/form/ui/container/WParameterLayout, com/fr/design/designer/creator/XWParameterLayout);
        xLayoutMap.put(com/fr/form/ui/container/WAbsoluteLayout, com/fr/design/designer/creator/XWAbsoluteLayout);
        xLayoutMap.put(com/fr/form/ui/container/WHorizontalBoxLayout, com/fr/design/designer/creator/XWHorizontalBoxLayout);
        xLayoutMap.put(com/fr/form/ui/container/WBorderLayout, com/fr/design/designer/creator/XWBorderLayout);
        xLayoutMap.put(com/fr/form/ui/container/WCardLayout, com/fr/design/designer/creator/cardlayout/XWCardLayout);
        xLayoutMap.put(com/fr/form/ui/container/WVerticalBoxLayout, com/fr/design/designer/creator/XWVerticalBoxLayout);
        xLayoutMap.put(com/fr/form/ui/container/WHorizontalSplitLayout, com/fr/design/designer/creator/XWHorizontalSplitLayout);
        xLayoutMap.put(com/fr/form/ui/container/WVerticalSplitLayout, com/fr/design/designer/creator/XWVerticalSplitLayout);
        xLayoutMap.put(com/fr/form/ui/container/WGridLayout, com/fr/design/designer/creator/XWGridLayout);
        xLayoutMap.put(com/fr/form/ui/container/WFitLayout, com/fr/design/designer/creator/XWFitLayout);
        xLayoutMap.put(com/fr/form/ui/container/WScaleLayout, com/fr/design/designer/creator/XWScaleLayout);
        xLayoutMap.put(com/fr/form/ui/container/WTitleLayout, com/fr/design/designer/creator/XWTitleLayout);
        xLayoutMap.put(com/fr/form/ui/container/cardlayout/WCardTagLayout, com/fr/design/designer/creator/cardlayout/XWCardTagLayout);
        xLayoutMap.put(com/fr/form/ui/container/cardlayout/WCardTitleLayout, com/fr/design/designer/creator/cardlayout/XWCardTitleLayout);
        xLayoutMap.put(com/fr/form/ui/container/cardlayout/WTabFitLayout, com/fr/design/designer/creator/cardlayout/XWTabFitLayout);
        xLayoutMap.put(com/fr/form/ui/container/cardlayout/WCardMainBorderLayout, com/fr/design/designer/creator/cardlayout/XWCardMainBorderLayout);
        objectMap.putAll(ExtraDesignClassManager.getInstance().getParameterWidgetOptionsMap());
        objectMap.putAll(ExtraDesignClassManager.getInstance().getFormWidgetOptionsMap());
    }
}
