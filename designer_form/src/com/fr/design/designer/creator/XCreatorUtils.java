/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.creator;

import com.fr.base.FRContext;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.designer.creator.cardlayout.*;
import com.fr.design.module.DesignModuleFactory;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.form.parameter.FormSubmitButton;
import com.fr.form.ui.*;
import com.fr.form.ui.Button;
import com.fr.form.ui.Label;
import com.fr.form.ui.TextArea;
import com.fr.form.ui.container.*;
import com.fr.form.ui.container.cardlayout.WCardMainBorderLayout;
import com.fr.form.ui.container.cardlayout.WCardTagLayout;
import com.fr.form.ui.container.cardlayout.WCardTitleLayout;
import com.fr.form.ui.container.cardlayout.WTabFitLayout;
import com.fr.general.IOUtils;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Constructor;

/**
 * XCreator����ش���
 *
 * @author richer
 * @since 6.5.3
 */
public class XCreatorUtils {

    public static java.util.Map<Class<? extends Widget>, Class<?>> objectMap = new java.util.HashMap<Class<? extends Widget>, Class<?>>();
    public static java.util.Map<Class<? extends Widget>, Class<?>> xLayoutMap = new java.util.HashMap<Class<? extends Widget>, Class<?>>();

    static {
        objectMap.put(TextEditor.class, XTextEditor.class);
        objectMap.put(TextArea.class, XTextArea.class);
        objectMap.put(NumberEditor.class, XNumberEditor.class);
        objectMap.put(FreeButton.class, XButton.class);
        objectMap.put(CheckBox.class, XCheckBox.class);
        objectMap.put(CheckBoxGroup.class, XCheckBoxGroup.class);
        objectMap.put(ComboBox.class, XComboBox.class);
        objectMap.put(ComboCheckBox.class, XComboCheckBox.class);
        objectMap.put(DateEditor.class, XDateEditor.class);
        objectMap.put(FileEditor.class, XFileUploader.class);
        objectMap.put(Table.class, XTableEditor.class);
        objectMap.put(IframeEditor.class, XIframeEditor.class);
        objectMap.put(FreeButton.class, XButton.class);
        objectMap.put(FormSubmitButton.class, XButton.class);
        objectMap.put(Button.class, XButton.class);
        objectMap.put(Label.class, XLabel.class);
        objectMap.put(ListEditor.class, XListEditor.class);
        objectMap.put(TableTree.class, XTableTree.class);
        objectMap.put(MultiFileEditor.class, XMultiFileUploader.class);
        objectMap.put(Password.class, XPassword.class);
        objectMap.put(Radio.class, XRadio.class);
        objectMap.put(RadioGroup.class, XRadioGroup.class);
        objectMap.put(TreeEditor.class, XTreeEditor.class);
        objectMap.put(TreeComboBoxEditor.class, XTreeComboBoxEditor.class);
        objectMap.put(EditorHolder.class, XEditorHolder.class);
        objectMap.put(DataTable.class, XDataTable.class);
        objectMap.put(ElementCaseEditor.class, XElementCase.class);
        objectMap.put(NameWidget.class, XNameWidget.class);
        objectMap.put(CardSwitchButton.class, XCardSwitchButton.class);
        objectMap.put(CardAddButton.class, XCardAddButton.class);
        putExtraEditor();

        xLayoutMap.put(WAbsoluteLayout.class, XWAbsoluteLayout.class);
        xLayoutMap.put(WParameterLayout.class, XWParameterLayout.class);
        xLayoutMap.put(WAbsoluteLayout.class, XWAbsoluteLayout.class);
        xLayoutMap.put(WHorizontalBoxLayout.class, XWHorizontalBoxLayout.class);
        xLayoutMap.put(WBorderLayout.class, XWBorderLayout.class);
        xLayoutMap.put(WCardLayout.class, XWCardLayout.class);
        xLayoutMap.put(WVerticalBoxLayout.class, XWVerticalBoxLayout.class);
        xLayoutMap.put(WHorizontalSplitLayout.class, XWHorizontalSplitLayout.class);
        xLayoutMap.put(WVerticalSplitLayout.class, XWVerticalSplitLayout.class);
        xLayoutMap.put(WGridLayout.class, XWGridLayout.class);

        xLayoutMap.put(WFitLayout.class, XWFitLayout.class);
        xLayoutMap.put(WScaleLayout.class, XWScaleLayout.class);
        xLayoutMap.put(WTitleLayout.class, XWTitleLayout.class);
        xLayoutMap.put(WCardTagLayout.class, XWCardTagLayout.class);
        xLayoutMap.put(WCardTitleLayout.class, XWCardTitleLayout.class);
        xLayoutMap.put(WTabFitLayout.class, XWTabFitLayout.class);
        xLayoutMap.put(WCardMainBorderLayout.class, XWCardMainBorderLayout.class);

        objectMap.putAll(ExtraDesignClassManager.getInstance().getParameterWidgetOptionsMap());
        objectMap.putAll(ExtraDesignClassManager.getInstance().getFormWidgetOptionsMap());
    }

    private static void putExtraEditor() {
        if (DesignModuleFactory.getChartEditorClass() != null) {
            objectMap.put(DesignModuleFactory.getChartEditorClass(), XChartEditor.class);
        }
    }

    private static Class<? extends XCreator> searchXCreatorClass(Class<? extends Widget> clazz) {
        Class<? extends XCreator> xClazz = (Class<? extends XCreator>) objectMap.get(clazz);
        if (xClazz == null) {
            xClazz = (Class<? extends XCreator>) xLayoutMap.get(clazz);
        }
        return xClazz;
    }

    /**
     *����creator
     * @param widget �ؼ�
     * @return ���ؿؼ���creator
     */
    public static XCreator createXCreator(Widget widget) {
        return createXCreator(widget, new Dimension());
    }

    /**
     * ����ʼ��С��Widgetת��ΪXCreator��ȻXCreatorҲ��Ҫ�Ѵ�С��ֵ��
     * @param widget �ؼ�
     * @param d ��С
     * @return ���ؿؼ���xcreator
     */
    public static XCreator createXCreator(Widget widget, Dimension d) {
        XCreator creator = null;
        Class<? extends Widget> widgetClass = widget.getClass();
        Class<? extends XCreator> clazz = XCreatorUtils.searchXCreatorClass(widgetClass);
        if (clazz == null) {
            FRContext.getLogger().error(widget + "'s" + " xcreator doesn't exsit!");
            clazz = NullCreator.class;
            widgetClass = Widget.class;
        }
        Constructor<? extends XCreator> c;
        try {
            // ģ�����Ե�� XChartEditor�ýӿڹ���
            if (clazz.getName().endsWith("XChartEditor")) {
                c = clazz.getConstructor(BaseChartEditor.class, Dimension.class);
            } else {
                c = clazz.getConstructor(widgetClass, Dimension.class);
            }
            creator = c.newInstance(widget, d);
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage(), e);
        }
        return creator;
    }

    /**
     *ˢ���������ֿؼ�
     * @param container ��������
     */
    public static void refreshAllNameWidgets(XLayoutContainer container) {
        _refreshNameWidget(container);
        LayoutUtils.layoutRootContainer(container);
    }

    private static void _refreshNameWidget(XLayoutContainer container) {
        for (int i = 0, len = container.getXCreatorCount(); i < len; i++) {
            XCreator creator = container.getXCreator(i);
            if (creator instanceof XLayoutContainer) {
                _refreshNameWidget((XLayoutContainer) creator);
            } else if (creator instanceof XNameWidget) {
                ((XNameWidget) creator).rebuild();
            }
        }
    }

    /**
	 * ��ȡ����������ڵĶ�������,������Ŀ�걾��
	 * 
	 * @param creator ���
	 * @return ���ظ�����
	 */
	public static XLayoutContainer getParentXLayoutContainer(XCreator creator) {
		Container c = creator.getParent();
		while (c != null) {
			XCreator crea = (XCreator) c;
			if (crea.isDedicateContainer()) {
				return (XLayoutContainer) c.getParent();
			} 
			if (c instanceof XLayoutContainer) {
				return (XLayoutContainer) c;
			}
			c = c.getParent();
		}

		return null;
	}

	/**
	 * ��ȡ����������ڵĶ�������,������Ŀ�걾��
	 * 
	 * @param creator ���
	 * @return ���ض�������
	 */
	public static XLayoutContainer getHotspotContainer(XCreator creator) {
		if (creator.isDedicateContainer()) {
			return (XLayoutContainer) creator.getParent();
		} 
		if (creator instanceof XLayoutContainer) {
			return (XLayoutContainer) creator;
		}
		return getParentXLayoutContainer(creator);
	}

	/**
	 * ���������ͼ��
	 * 
	 * @param creator  ���
	 * @return  ���icon
	 */
    public static Icon getCreatorIcon(XCreator creator) {
        String iconPath = creator.getIconPath();
        if (StringUtils.isEmpty(iconPath)) {
            return null;
        }
        return IOUtils.readIcon(iconPath);
    }
}
