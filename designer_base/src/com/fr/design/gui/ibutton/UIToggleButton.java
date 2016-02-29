package com.fr.design.gui.ibutton;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;

import javax.swing.Icon;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fr.design.constants.UIConstants;
import com.fr.design.event.GlobalNameListener;
import com.fr.design.event.GlobalNameObserver;
import com.fr.stable.StringUtils;

/**
 * SelectedAble button label
 *
 * @author zhou
 * @since 2012-5-11下午4:28:24
 */
public class UIToggleButton extends UIButton implements GlobalNameObserver{
	private boolean isSelected;
	private boolean isEventBannded = false;
	private String toggleButtonName = "";
	private GlobalNameListener globalNameListener = null;

	public UIToggleButton() {
		this(StringUtils.EMPTY);
	}

	public UIToggleButton(Icon image) {
		this(StringUtils.EMPTY, image);
	}

	public UIToggleButton(String text) {
		this(text, null);
	}

	public UIToggleButton(String text, Icon image) {
		super(text, image);
		addMouseListener(getMouseListener());
	}

	public void setGlobalName(String name){
		toggleButtonName = name ;
	}

	/**
	 *
	 * @return
	 */
	public boolean isSelected() {
		return isSelected;
	}

	/**
	 * 能触发事件
	 *
	 * @param isSelected
	 */

	@Override
	public void setSelected(boolean isSelected) {
		if (this.isSelected != isSelected) {
			this.isSelected = isSelected;
			repaint();
		}
	}
    protected void initListener(){
        if(shouldResponseChangeListener()){
            this.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    if (uiObserverListener == null) {
                        return;
                    }
					if(globalNameListener!=null && shouldResponseNameListener()){
						globalNameListener.setGlobalName(toggleButtonName);
					}
                    uiObserverListener.doChange();
                }
            });
        }
    }

	public void setSelectedWithFireListener(boolean isSelected) {
		if (this.isSelected != isSelected) {
			this.isSelected = isSelected;
			fireSelectedChanged();
			repaint();
		}
	}

	protected MouseListener getMouseListener() {
		return new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (isEnabled() && !isEventBannded) {
					setSelectedWithFireListener(!isSelected()); 
				}
			}
		};
	}

	public void setEventBannded(boolean ban) {
		this.isEventBannded = ban;
	}

	protected void fireStateChanged() {

	}


	protected void fireSelectedChanged() {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ChangeListener.class) {
				((ChangeListener) listeners[i + 1]).stateChanged(new ChangeEvent(this));
			}
		}
	}

	@Override
	protected void paintBorder(Graphics g) {
		if (!isBorderPainted()) {
			return;
		}
		boolean isBorderPainted = isBorderPaintedOnlyWhenPressed && (getModel().isPressed() || isSelected);
		if (isBorderPainted || !isBorderPaintedOnlyWhenPressed) {
			if (ui instanceof UIButtonUI) {
				((UIButtonUI) ui).paintBorder(g, this);
			} else {
				super.paintBorder(g);
			}
		}
	}

	protected void paintOtherBorder(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(UIConstants.BS);
		Shape shape = new RoundRectangle2D.Float(0.5f, 0.5f, getWidth() - 1, getHeight() - 1, UIConstants.ARC, UIConstants.ARC);
		g2d.setColor(UIConstants.LINE_COLOR);
		g2d.draw(shape);
	}

	/**
	 * 组件是否需要响应添加的观察者事件
	 *
	 * @return 如果需要响应观察者事件则返回true，否则返回false
	 */
	public boolean shouldResponseChangeListener() {
		return true;
	}

	/**
	 *
	 * @param listener 观察者监听事件
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

}