package com.fr.design.mainframe.cell.settingpane.style;

import com.fr.base.ConfigManager;
import com.fr.base.NameStyle;
import com.fr.base.ScreenResolution;
import com.fr.base.Style;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.DesignerBean;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;

public class PredefinedStylePane extends FurtherBasicBeanPane<NameStyle> implements DesignerBean {

    private DefaultListModel defaultListModel;
    private JList styleList;
    private ChangeListener changeListener;

    public PredefinedStylePane() {
        defaultListModel = new DefaultListModel();
        styleList = new JList(defaultListModel);
        DefaultListCellRenderer render = new DefaultListCellRenderer() {
            private Style nameStyle;

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value instanceof Style) {
                    this.nameStyle = (Style) value;
                    this.setText(" ");
                }
                this.setPreferredSize(new Dimension(210, 22));
                return this;
            }

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);

                if (nameStyle == null) {
                    return;
                }
                String text = "abcedfgh";
                if (nameStyle instanceof NameStyle) {
                    text = ((NameStyle) nameStyle).getName();
                }
                Style.paintBackground((Graphics2D) g, nameStyle, getWidth() - 1, getHeight() - 1);
                Style.paintContent((Graphics2D) g, text, nameStyle, getWidth() - 1, getHeight() - 1, ScreenResolution.getScreenResolution());
                Style.paintBorder((Graphics2D) g, nameStyle, getWidth() - 1, getHeight() - 1);
            }

        };
        styleList.setCellRenderer(render);
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.add(styleList, BorderLayout.CENTER);

        styleList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int clickedNumber = e.getClickCount();
                if (clickedNumber == 1) {
                    if (changeListener != null) {
                        changeListener.stateChanged(new ChangeEvent(styleList));
                    }
                }
                // ��������������2��Ϊ����˫���������༭����
                // if (clickedNumber >= 2) {
                // }
            }
        });

        DesignerContext.setDesignerBean("predefinedStyle", this);
    }

    /**
     * ��Ӹı����
     *
     * @param changeListener �����¼�
     */
    public void addChangeListener(ChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    /**
     * ����
     */
    public void reset() {

    }

    @Override
    public void populateBean(NameStyle ob) {
        refreshBeanElement();
        for (int i = 0; i < defaultListModel.getSize(); i++) {
            if (ComparatorUtils.equals(ob, defaultListModel.get(i))) {
                styleList.setSelectedIndex(i);
                break;
            }
        }
    }

    @Override
    public NameStyle updateBean() {
        return (NameStyle) styleList.getSelectedValue();
    }

    /**
     * ��ȡ������
     *
     * @return ����
     */
    public String title4PopupWindow() {
        return Inter.getLocText(new String[]{"PageSetup-Predefined", "Style"});
    }

    /**
     * �Ƿ���Խ��ɶ���
     *
     * @param ob �������
     * @return �Ƿ���Խ��ɶ���
     */
    public boolean accept(Object ob) {
        return ob instanceof NameStyle;
    }

    /**
     * ˢ���������
     */
    public void refreshBeanElement() {
        defaultListModel.removeAllElements();
        if (ConfigManager.getProviderInstance().hasStyle()) {
            Iterator iterato = ConfigManager.getProviderInstance().getStyleNameIterator();
            while (iterato.hasNext()) {
                String name = (String) iterato.next();
                NameStyle nameStyle = NameStyle.getInstance(name);
                defaultListModel.addElement(nameStyle);
            }
        }
        styleList.setModel(defaultListModel);
        GUICoreUtils.repaint(this);

    }

}
