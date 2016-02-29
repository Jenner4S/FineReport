package com.fr.grid;

import java.awt.Color;



public abstract class GridHeader<T> extends BaseGridComponent {
	public final static int SIZE_ADJUST = 4;
	
    //属性
    private Color separatorLineColor = new Color(172, 168, 153); //separator lines
    private Color selectedForeground = Color.black;
    private Color selectedBackground = new Color(253, 216, 153);

    public GridHeader() {
        //清除所有的Key Action.
        this.getInputMap().clear();
        this.getActionMap().clear();

        this.setFocusable(false);
        this.setOpaque(true);
        
        initByConstructor();
    }
    
    protected abstract void initByConstructor();
    
    protected abstract T getDisplay(int index) ;


    /**
     * Gets separator line color.
     *
     * @return the separator line color.
     */
    public Color getSeparatorLineColor() {
        return this.separatorLineColor;
    }

    /**
     * Sets row separator line color.
     *
     * @param separatorLineColor the new row color of separator line.
     */
    public void setSeparatorLineColor(Color separatorLineColor) {
        Color old = this.separatorLineColor;
        this.separatorLineColor = separatorLineColor;

        this.firePropertyChange("separatorLineColor", old, this.separatorLineColor);
        this.repaint();
    }

    /**
     * Gets selected foreground.
     *
     * @return the selected foreground.
     */
    public Color getSelectedForeground() {
        return this.selectedForeground;
    }

    /**
     * Sets row selected foreground.
     *
     * @param selectedForeground the new row selected foreground.
     */
    public void setSelectedForeground(Color selectedForeground) {
        Color old = this.selectedForeground;
        this.selectedForeground = selectedForeground;

        this.firePropertyChange("selectedForeground", old, this.selectedForeground);
        this.repaint();
    }

    /**
     * Gets selected background.
     *
     * @return the selected background.
     */
    public Color getSelectedBackground() {
        return this.selectedBackground;
    }

    /**
     * Sets row selected background.
     *
     * @param selectedBackground the new row selected background.
     */
    public void setSelectedBackground(Color selectedBackground) {
        Color old = this.selectedBackground;
        this.selectedBackground = selectedBackground;

        this.firePropertyChange("selectedBackground", old, this.selectedBackground);
        this.repaint();
    }
}