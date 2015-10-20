package com.fr.design.actions.cell;

/**
 * richer:global style menu
 */

import com.fr.base.BaseUtils;
import com.fr.base.ConfigManager;
import com.fr.base.NameStyle;
import com.fr.design.actions.ElementCaseAction;
import com.fr.design.actions.UpdateAction;
import com.fr.design.gui.imenu.UIMenu;
import com.fr.design.mainframe.CellElementPropertyPane;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.menu.MenuDef;
import com.fr.design.style.StylePane;
import com.fr.general.Inter;
import com.fr.base.ConfigManagerProvider;
import com.fr.stable.StringUtils;
import com.fr.stable.pinyin.PinyinHelper;

import java.awt.event.ActionEvent;
import java.util.Iterator;

public class GlobalStyleMenuDef extends MenuDef {
    private static final int MAX_LENTH = 12;
    private ElementCasePane ePane;

    public GlobalStyleMenuDef(ElementCasePane ePane) {
        this.ePane = ePane;
        this.setMenuKeySet(KeySetUtils.GLOBAL_STYLE);
        this.setName(getMenuKeySet().getMenuKeySetName());
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setIconPath("/com/fr/design/images/m_web/style.png");
    }

    /**
     * ���²˵���
     */
    public void updateMenu() {
        UIMenu createdMenu = this.createJMenu();
        createdMenu.removeAll();
        ConfigManagerProvider configManager = ConfigManager.getProviderInstance();
        Iterator iterator = configManager.getStyleNameIterator();
        while (iterator.hasNext()) {
            String name = (String) iterator.next();
            NameStyle nameStyle = NameStyle.getInstance(name);

            UpdateAction.UseMenuItem useMenuItem = new GlobalStyleSelection(ePane, nameStyle).createUseMenuItem();
            useMenuItem.setNameStyle(nameStyle);
            createdMenu.add(useMenuItem);
        }
        createdMenu.addSeparator();
        createdMenu.add(new CustomStyleAction(Inter.getLocText("FR-Engine_Custom")));
    }

    /**
     * �����Զ�����ʽ���Ƶĳ���
     *
     * @param longName ����
     * @return ����֮�������
     */
    public static String judgeChina(String longName) {

        //neil:bug 1623 �����Զ�����ʽ���Ƶĳ��ȣ�ֻ��ʾǰ12���ַ���ÿ��Ӣ����1���ַ���ÿ��������2���ַ�
        Integer index = 0;
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < longName.length(); i++) {
            String bb = longName.substring(i, i + 1); //����һ��Pattern,ͬʱ����һ��������ʽ
            boolean cc = PinyinHelper.isChinese(bb.charAt(0));
            if (index == MAX_LENTH) {
                sBuffer.append("..");
                break;
            }
            if ((index == MAX_LENTH - 1 && cc)) {
                continue;
            }
            if (cc) {
                index = index + 2;
            } else {
                index = index + 1;
            }

            sBuffer.append(bb);
            if (index > MAX_LENTH) {
                sBuffer.append("..");
                break;
            }

        }

        return sBuffer.toString();

    }

    public static class CustomStyleAction extends UpdateAction {

        public CustomStyleAction(String name) {

            this.setName(name);
            this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_format/cell.png"));
        }

        /**
         * ����
         *
         * @param e �¼�
         */
        public void actionPerformed(ActionEvent e) {
            CellElementPropertyPane.getInstance().GoToPane(new String[]{Inter.getLocText("FR-Engine_Style"), Inter.getLocText("FR-Engine_Custom")});
        }

    }

    public static class GlobalStyleSelection extends ElementCaseAction {

        private NameStyle nameStyle;

        public GlobalStyleSelection(ElementCasePane t, NameStyle nameStyle) {
            super(t);
            setName(StringUtils.EMPTY);
            //�ظ�����һ�Σ�����Ҫicon
//        	this.setName(nameStyle == null ? "" : nameStyle.getName());
//        	this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_format/cell.png"));
            this.setNameStyle(nameStyle);
        }

        public NameStyle getNameStyle() {
            return this.nameStyle;
        }

        public void setNameStyle(NameStyle nameStyle) {
            this.nameStyle = nameStyle;
        }

        /**
         * ִ�ж�������
         * @return �Ƿ���true
         */
        public boolean executeActionReturnUndoRecordNeeded() {
            StylePane stylePane = new StylePane();
            if (StringUtils.isEmpty(this.getName())) {
                stylePane.setGlobalStyle(this.getNameStyle());
            } else {
                stylePane.setGlobalStyle(NameStyle.getInstance(this.getName()));
            }

            stylePane.updateGlobalStyle(getEditingComponent());
            return true;
        }
    }
}
