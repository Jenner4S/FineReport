package com.fr.design.style.color;

import com.fr.base.background.ColorBackground;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.style.AbstractSelectBox;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * Color select pane.
 */
public class ColorSelectBox extends AbstractSelectBox<Color> implements UIObserver {
    private static final long serialVersionUID = 2782150678943960557L;

    private Color color;
    private ColorSelectPane colorPane;
    private UIObserverListener uiObserverListener;

    public ColorSelectBox(int preferredWidth) {
        colorPane = getColorSelectPane();
        initBox(preferredWidth);
        iniListener();
    }

    private void iniListener() {
        if (shouldResponseChangeListener()) {
            this.addSelectChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    if (uiObserverListener == null) {
                        return;
                    }
                    uiObserverListener.doChange();
                }
            });
        }
    }

    protected ColorSelectPane getColorSelectPane(){
        return new ColorSelectPane() {
            public void setVisible(boolean b) {
                super.setVisible(b);
            }
        };
    }

    /**
     * ��ʼ������������
     * @param preferredWidth ���
     * @return �������
     */
    public JPanel initWindowPane(double preferredWidth) {
    	// ������ʱ������������壬ˢ�����ʹ����ɫ
    	colorPane = getColorSelectPane();
        colorPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                hidePopupMenu();
                color = ((ColorSelectPane) e.getSource()).getColor();
                fireDisplayComponent(ColorBackground.getInstance(color));
            }
        });
        return colorPane;
    }

    /**
     * ��ȡ��ǰѡ�е���ɫ
     * @return ��ǰѡ�е���ɫ
     */
    public Color getSelectObject() {
        return this.color;
    }

    /**
     *����ѡ�е���ɫ
     * @param color ��ɫ
     */
    public void setSelectObject(Color color) {
        this.color = color;
        colorPane.setColor(color);

        fireDisplayComponent(ColorBackground.getInstance(color));
    }

    /**
     * ע�������
     * @param listener ������
     */
    public void registerChangeListener(UIObserverListener listener) {
        uiObserverListener = listener;
    }

    /**
     * �Ƿ���Ӧ�¼�
     * @return ��Ҫ��Ӧ
     */
    public boolean shouldResponseChangeListener() {
        return true;
    }
}
