package com.fr.start;

import java.util.ArrayList;
import java.util.List;

import com.fr.base.FRContext;
import com.fr.design.actions.file.newReport.NewWorkBookAction;
import com.fr.design.actions.report.ReportFooterAction;
import com.fr.design.actions.report.ReportHeaderAction;
import com.fr.design.mainframe.toolbar.ToolBarMenuDockPlus;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.ShortCut;
import com.fr.general.ComparatorUtils;
import com.fr.stable.Constants;

/**
 * Created with IntelliJ IDEA.
 * User: richie
 * Date: 12/17/13
 * Time: 12:54 PM
 * ����Ӣ�İ��GridBI�����������
 */
public class Designer4BI extends Designer {
	
	/**
	 * ����BI�������
	 * 
	 * @param args ����
	 * 
	 */
    public static void main(String[] args) {
        new Designer4BI(args);
    }

    public Designer4BI(String[] args) {
        super(args);
    }

    protected void initLanguage() {
        //�������λ�ò�������������Ϊ��Ӱ�������л�������
        FRContext.setLanguage(Constants.LANGUAGE_ENGLISH);
    }

    protected SplashPane createSplashPane() {
        return new BISplashPane();
    }

    @Override
    /**
     * �����½��ļ��Ŀ�ݷ�ʽ���顣
     * @return ���ؿ�ݷ�ʽ������
     */
    public ShortCut[] createNewFileShortCuts() {
        ArrayList<ShortCut> shortCuts = new ArrayList<ShortCut>();
        shortCuts.add(new NewWorkBookAction());
        return shortCuts.toArray(new ShortCut[shortCuts.size()]);
    }

    /**
	 * ��ȡģ��-�˵�ѡ��
	 * 
	 * @param plus ��ǰ�Ĺ�������
	 * 
	 * @return �˵�����������
	 * 
	 */
    public MenuDef[] createTemplateShortCuts(ToolBarMenuDockPlus plus) {
        MenuDef[] menuDefs = plus.menus4Target();
        for (MenuDef m : menuDefs) {
            List<ShortCut> shortCuts = new ArrayList<ShortCut>();
            for (int i = 0, count = m.getShortCutCount(); i < count; i++) {
                ShortCut shortCut = m.getShortCut(i);
                if (!ComparatorUtils.equals(shortCut.getClass(), ReportHeaderAction.class)
                        && !ComparatorUtils.equals(shortCut.getClass(), ReportFooterAction.class)) {
                    shortCuts.add(shortCut);
                }
            }
            m.clearShortCuts();
            for (int i = 0, len = shortCuts.size(); i < len; i ++) {
                m.addShortCut(shortCuts.get(i));
            }

        }
        return menuDefs;
    }
}
