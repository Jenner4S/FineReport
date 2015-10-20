package com.fr.design.style.color;

import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fr.base.background.ColorBackground;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.style.AbstractSelectBox;

/**
 * Color select pane.
 */
public class NewColorSelectBox extends AbstractSelectBox<Color> implements UIObserver {
    private static final long serialVersionUID = 2782150678943960557L;
    
    private Color color;
    private NewColorSelectPane colorPane = new NewColorSelectPane();
    private UIObserverListener uiObserverListener;

    public NewColorSelectBox(int preferredWidth) {
    	initBox(preferredWidth);
        iniListener();
    }

    private void iniListener(){
        if(shouldResponseChangeListener()){
            this.addSelectChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    if(uiObserverListener == null){
                        return;
                    }
                    uiObserverListener.doChange();
                }
            });
        }
    }

    /**
     * ��ʼ���������
     * @param preferredWidth ����С
     * @return ���
     */
    public JPanel initWindowPane(double preferredWidth) {
    	// ������ʱ������������壬��ˢ�����ʹ����ɫ
    	colorPane = new NewColorSelectPane();
    	colorPane.addChangeListener(new ChangeListener() {
    		public void stateChanged(ChangeEvent e) {
    			hidePopupMenu();
    			color = ((NewColorSelectPane)e.getSource()).getColor();
    			fireDisplayComponent(ColorBackground.getInstance(color));
    		}
    	});
    	return colorPane;
    }

    /**
     *
     * @return
     */
    public Color getSelectObject() {
        return this.color;
    }

    /**
     *
     * @param color
     */
    public void setSelectObject(Color color) {
    	this.color = color;
    	colorPane.setColor(color);
    	fireDisplayComponent(ColorBackground.getInstance(color));
    }

    @Override
    /**
     * ������
     * @param listener ����
     */
    public void registerChangeListener(UIObserverListener listener) {
        uiObserverListener = listener;
    }

    @Override
    /**
     * �Ƿ���Ӧ����
     * @return ͬ��
     */
    public boolean shouldResponseChangeListener() {
        return true;
    }
}
