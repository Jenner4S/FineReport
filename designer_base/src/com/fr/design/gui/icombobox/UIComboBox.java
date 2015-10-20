package com.fr.design.gui.icombobox;

import com.fr.design.event.GlobalNameListener;
import com.fr.design.event.GlobalNameObserver;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.utils.gui.GUICoreUtils;

import javax.swing.*;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

/**
 * august:�ǳ�beautiful��ComboBox����֧�ֱ༭״̬. ���ݹ���ʱ������ƶ���ȥ����ToolTips,�����к��������
 * ����֧�ֱ༭����ΪUIComboBox��TextField �Ļ��� �����ǿ�Renderer������ ,
 * ����ͨ��paintCurrentValueBackground()�����Ʊ���,
 * Ȼ��ͨ��paintCurrentValue(),ȥ����UIComboBox����ʾ��ֵ����������������ڸ��Ӷ������ˣ�������ʱ��֧��
 * ���⣬���������ò�Ҫ��ͼ��
 *
 * @author zhou
 * @since 2012-5-9����3:18:58
 */
public class UIComboBox extends JComboBox implements UIObserver, GlobalNameObserver {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static final int SIZE = 20;

    private static final int SIZE5 = 5;

    private UIObserverListener uiObserverListener;

    private String comboBoxName = "";

    private GlobalNameListener globalNameListener = null;

    public UIComboBox() {
        super();
        init();
    }

    public UIComboBox(ComboBoxModel model) {
        super(model);
        init();
    }

    public UIComboBox(Object[] items) {
        super(items);
        init();
    }

    public UIComboBox(Vector<?> items) {
        super(items);
        init();
    }

    private void init() {
        setOpaque(false);
        setUI(getUIComboBoxUI());
        setRenderer(new UIComboBoxRenderer());
        setEditor(new UIComboBoxEditor());
        initListener();
    }

    private void initListener() {
        if (shouldResponseChangeListener()) {
            this.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    fireSetGlobalName();
                }
            });
            this.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (uiObserverListener == null) {
                        return;
                    }
                    fireSetGlobalName();
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        uiObserverListener.doChange();
                    }
                }
            });
        }
    }

    private void fireSetGlobalName() {
        if (globalNameListener != null && shouldResponseNameListener()) {
            globalNameListener.setGlobalName(comboBoxName);
        }
    }


    protected ComboBoxUI getUIComboBoxUI() {
        return new UIComboBoxUI();
    }

    /**
     * ֻ��������ΪUIComboBoxRenderer������Ҫ�̳�UIComboBoxRenderer
     */
    @Override
    public void setRenderer(ListCellRenderer aRenderer) {
        if (aRenderer instanceof UIComboBoxRenderer) {
            super.setRenderer(aRenderer);
        } else {
            //throw new IllegalArgumentException("Must be UIComboBoxRenderer");
        }
    }

    protected ComboPopup createPopup() {
        return null;
    }

    public void setGlobalName(String name) {
        comboBoxName = name;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(super.getPreferredSize().width + SIZE5, SIZE);//��5��ԭ�����ڣ�render�ÿһ����ǰ���˿���һ��Ҫ�༸����
    }

    /**
     * �������¼�
     */
    public void mouseEnterEvent() {

    }

    /**
     * ����뿪�¼�
     */
    public void mouseExitEvent() {

    }

	/**
	 *
	 */
    public void updateUI() {
        setUI(getUIComboBoxUI());
    }


	/**
	 *
	 * @param listener �۲��߼����¼�
	 */
    public void registerChangeListener(UIObserverListener listener) {
        uiObserverListener = listener;
    }

    /**
     * @return
     */
    public boolean shouldResponseChangeListener() {
        return true;
    }

	/**
	 *
	 * @param listener �۲��߼����¼�
	 */
    public void registerNameListener(GlobalNameListener listener) {
        globalNameListener = listener;
    }

	/**
	 *
	 * @return
	 */
    public boolean shouldResponseNameListener() {
        return true;
    }


    /**
     * @param args
     */
    public static void main(String... args) {
		LayoutManager layoutManager = null;
        JFrame jf = new JFrame("test");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel content = (JPanel) jf.getContentPane();
        content.setLayout(layoutManager);
        UIComboBox bb = new UIComboBox(new String[]{"", "jerry", "kunsnat", "richer"});
        bb.setEditable(true);
        bb.setBounds(20, 20, bb.getPreferredSize().width, bb.getPreferredSize().height);
        content.add(bb);
        GUICoreUtils.centerWindow(jf);
        jf.setSize(400, 400);
        jf.setVisible(true);
    }


}
