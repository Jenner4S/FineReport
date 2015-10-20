package com.fr.design.module;

import com.fr.design.ExtraDesignClassManager;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.general.ModuleContext;
import com.fr.module.TopModule;
import com.fr.stable.ArrayUtils;
import com.fr.stable.bridge.StableFactory;
import com.fr.stable.plugin.ExtraDesignClassManagerProvider;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-11-24
 * Time   : ����2:52
 * ���������ģ��ĸ���
 */
public abstract class DesignModule extends TopModule {
	public void start() {
		super.start();
		DesignerFrame.App<?>[] apps = apps4TemplateOpener();
		for (DesignerFrame.App<?> app : apps) {
			DesignerFrame.registApp(app);
		}
        ModuleContext.registerStartedModule(DesignModule.class.getName(), this);
		StableFactory.registerMarkedClass(ExtraDesignClassManagerProvider.XML_TAG, ExtraDesignClassManager.class);
	}

    public boolean isStarted() {
        return ModuleContext.isModuleStarted(DesignModule.class.getName());
    }

	/**
	 * ����������ܴ򿪵�ģ�����͵�һ�������б�
	 *
	 * @return ���Դ򿪵�ģ�����͵�����
	 */
	public abstract DesignerFrame.App<?>[] apps4TemplateOpener();

	/**
	 * ���ʻ��ļ�·��
	 * @return ���ʻ��ļ�·��
	 */
	public String[] getLocaleFile() {
		return ArrayUtils.EMPTY_STRING_ARRAY;
	}


}
