package com.fr.design.gui.frpane;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.gui.icombobox.UIComboBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

/**
 * 
 * @author zhou
 * @since 2012-5-31����12:25:21
 */
public abstract class UIComboBoxPane<T> extends BasicBeanPane<T> {

	private static final long serialVersionUID = 1L;
	
	protected abstract List<FurtherBasicBeanPane<? extends T>> initPaneList();

	protected UIComboBox jcb;
	protected JPanel cardPane;

	protected List<FurtherBasicBeanPane<? extends T>> cards;
	private String[] cardNames;
	
	public UIComboBoxPane() {
		cards = initPaneList();
		initComponents();
	}

	protected void initComponents() {
		cardNames = new String[cards.size()];

		jcb = createComboBox();
		cardPane = new JPanel(new CardLayout()) {
			@Override
			public Dimension getPreferredSize() {
				return cards.get(jcb.getSelectedIndex()).getPreferredSize();
			}
		};
		for (int i = 0; i < this.cards.size(); i++) {
			String name = this.cards.get(i).title4PopupWindow();// Name�Ӹ��Ե�pane�����ȡ
			cardNames[i] = name;
			cardPane.add(this.cards.get(i), cardNames[i]);
            addComboBoxItem(cards, i);
		}

		jcb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				comboBoxItemStateChanged();
				CardLayout cl = (CardLayout)cardPane.getLayout();
				cl.show(cardPane, cardNames[jcb.getSelectedIndex()]);
			}
		});

		initLayout();

		jcb.setSelectedIndex(0);
	}

	protected UIComboBox createComboBox() {
		return new UIComboBox();
	}

	protected void addComboBoxItem(List<FurtherBasicBeanPane<? extends T>> cards, int index) {
		jcb.addItem(cards.get(index).title4PopupWindow());
	}
	
	/**
	 * ����ֻ֧�����е�ĳ��ѡ��, 
	 */
	public void justSupportOneSelect(boolean surpport) {
		if(!surpport) {
			jcb.setSelectedIndex(0);
		}
		jcb.setEnabled(surpport);
	}

	/**
	 * august�������Ҫ�Ĳ����б仯������֮
	 */
	protected void initLayout() {
		this.setLayout(new BorderLayout(0,6));
		JPanel northPane = new JPanel(new BorderLayout());
		northPane.add(jcb, BorderLayout.CENTER);
		this.add(northPane, BorderLayout.NORTH);
		this.add(cardPane, BorderLayout.CENTER);

	}
	
	protected void comboBoxItemStateChanged() {
		
	}
	
	public void reset() {
		jcb.setSelectedIndex(0);
		for (FurtherBasicBeanPane<?> pane : cards) {
			pane.reset();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void populateBean(T ob) {
		for (int i = 0; i < this.cards.size(); i++) {
			FurtherBasicBeanPane pane = cards.get(i);
			if (pane.accept(ob)) {
				pane.populateBean(ob);
				jcb.setSelectedIndex(i);
				return;
			}
		}
	}

	@Override
	public T updateBean() {
		return cards.get(jcb.getSelectedIndex()).updateBean();
	}

	public int getSelectedIndex() {
		return jcb.getSelectedIndex();
	}
	
	public void setSelectedIndex(int index) {
		jcb.setSelectedIndex(index);
	}
	
	public void addTabChangeListener(ItemListener l){
		jcb.addItemListener(l);
	}
	
	public UIComboBox getUIComboBox(){
		return this.jcb;
	}
	
	public List<FurtherBasicBeanPane<? extends T>> getCards(){
		return this.cards;
	}

}
