package com.fr.design.present;

import com.fr.base.present.DictPresent;
import com.fr.base.present.FormulaPresent;
import com.fr.base.present.Present;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.fun.PresentKindProvider;
import com.fr.design.gui.frpane.UIComboBoxPane;
import com.fr.design.gui.icombobox.DictionaryComboBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.general.Inter;
import com.fr.report.cell.cellattr.BarcodePresent;
import com.fr.report.cell.cellattr.CurrencyLinePresent;

import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhou
 * @since 2012-5-31����11:22:28
 */
public class PresentPane extends UIComboBoxPane<Present> {
	private DictPresentPane dictPresentPane;
	private List<String> keys;
	private List<String> displays;


	@Override
	protected String title4PopupWindow() {
		return Inter.getLocText("FR-Designer_Present");
	}

	public void setSelectedByName(String radioName) {
		jcb.setSelectedItem(radioName);
	}

    /**
     * ����tab�ı�����¼�
     * @param l   �����¼�
     */
	public void addTabChangeListener(ItemListener l) {
		super.addTabChangeListener(l);
		dictPresentPane.addTabChangeListener(l);
	}

	@Override
	public void populateBean(Present ob) {
		if(ob == null) {
			dictPresentPane.reset();
		}
		super.populateBean(ob);
	}

	@Override
	protected List<FurtherBasicBeanPane<? extends Present>> initPaneList() {
		if (keys == null) {
			keys = new ArrayList<>();
		}
		if (displays == null) {
			displays = new ArrayList<>();
		}
		List<FurtherBasicBeanPane<? extends Present>> paneList = new ArrayList<>();
		FurtherBasicBeanPane<Present> none = new NonePresentPane();
		paneList.add(none);
		keys.add("NOPRESENT");
		displays.add(none.title4PopupWindow());

		paneList.add(dictPresentPane = new DictPresentPane());
		keys.add(DictPresent.class.getName());
		displays.add(dictPresentPane.title4PopupWindow());

		FurtherBasicBeanPane<BarcodePresent> bar = new BarCodePane();
		paneList.add(bar);
		keys.add(BarcodePresent.class.getName());
		displays.add(bar.title4PopupWindow());

		FurtherBasicBeanPane<FormulaPresent> formula = new FormulaPresentPane();
		paneList.add(formula);
		keys.add(FormulaPresent.class.getName());
		displays.add(formula.title4PopupWindow());

		FurtherBasicBeanPane<CurrencyLinePresent> currency = new CurrencyLinePane();
		paneList.add(currency);
		keys.add(CurrencyLinePresent.class.getName());
		displays.add(currency.title4PopupWindow());

		PresentKindProvider[] providers = ExtraDesignClassManager.getInstance().getPresentKindProviders();
		for (PresentKindProvider provider : providers) {
			FurtherBasicBeanPane<? extends Present> extra = provider.appearanceForPresent();
			paneList.add(extra);
			keys.add(provider.kindOfPresent().getName());
			displays.add(extra.title4PopupWindow());
		}
		return paneList;
	}

	@Override
	protected UIComboBox createComboBox() {
		return new DictionaryComboBox<>(keys.toArray(new String[keys.size()]), displays.toArray(new String[displays.size()]));
	}

	@Override
	protected void addComboBoxItem(List<FurtherBasicBeanPane<? extends Present>> cards, int index) {
		// ��ʼ����ʱ���Ѿ����ˣ��������ﲻ�ü���
	}
}
