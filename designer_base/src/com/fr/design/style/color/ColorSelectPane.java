/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.style.color;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fr.design.border.UIRoundedBorder;
import com.fr.design.constants.UIConstants;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.DesignerContext;
import com.fr.general.Inter;
import com.fr.design.utils.gui.GUICoreUtils;

/**
 * The pane used to select color
 */
public class ColorSelectPane extends TransparentPane implements ColorSelectable {
    private static final long serialVersionUID = -8634152305687249392L;

	private Color color = null; //color
    //color setting action.
    private ArrayList<ChangeListener> colorChangeListenerList = new ArrayList<ChangeListener>();
    
    ColorSelectDetailPane pane;
    
    /**
     * Constructor.
     */
    public ColorSelectPane() {
    	super(true);
    	initialCompents(true);
    }
    
    private void initialCompents(boolean isSupportTransparent) {

		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		this.setBorder(new UIRoundedBorder(UIConstants.LINE_COLOR, 1, 5));
		if (isSupportTransparent) {
			UIButton transpanrentButton = new UIButton(Inter.getLocText("FR-Designer_ChartF-Transparency"));
			this.add(transpanrentButton, BorderLayout.NORTH);
			transpanrentButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					doTransparent();
				}
			});
		}

		// center
		JPanel centerPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_S_Pane();
		this.add(centerPane, BorderLayout.CENTER);
		
		// ���ʹ��
		UsedColorPane pane = new UsedColorPane(1, 8, ColorSelectConfigManager.getInstance().getColors(),this);
		centerPane.add(pane.getPane());
		
		JPanel menuColorPane1 = new JPanel();
		centerPane.add(menuColorPane1);

		menuColorPane1.setLayout(new GridLayout(5, 8, 1, 1));
		menuColorPane1.setBorder(BorderFactory.createEmptyBorder(8, 8, 0, 8));
        Color[] colorArray = this.getColorArray();
		for (int i = 0; i < colorArray.length; i++) {
			Color color = colorArray[i] == null ? UsedColorPane.DEFAULT_COLOR : colorArray[i];
			menuColorPane1.add(new ColorCell(color, this));
		}

		centerPane.add(Box.createVerticalStrut(1));

		UIButton customButton = new UIButton(Inter.getLocText("FR-Designer-Basic_More_Color"));
		
		customButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				customButtonPressed();
			}
		});
		customButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		JPanel centerPane1 = FRGUIPaneFactory.createBorderLayout_S_Pane();
		centerPane1.setBorder(BorderFactory.createEmptyBorder(2, 8, 0, 8));
		centerPane1.add(customButton, BorderLayout.NORTH);
		centerPane.add(centerPane1);
	}

    protected Color[] getColorArray(){
        return ColorFactory.MenuColors;
    }


    /**
     * Add change listener.
     * ���Ӽ���
     * @param ����
     */
    public void addChangeListener(ChangeListener changeListener) {
        this.colorChangeListenerList.add(changeListener);
    }
    
    @Override
    protected String title4PopupWindow() {
    	return "Color";
    }

    /**
     * Return the color.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Set the color.
     *
     * @param color the new color.
     */
    public void setColor(Color color) {
        this.color = color;

        //fire color change.
        if (!colorChangeListenerList.isEmpty()) {
            ChangeEvent evt = new ChangeEvent(this);

            for (int i = 0; i < colorChangeListenerList.size(); i++) {
                this.colorChangeListenerList.get(i).stateChanged(evt);
            }
        }

        ColorSelectConfigManager.getInstance().addToColorQueue(color);
        this.repaint();
    }

    /**
     * ѡ����ɫ
     * @param ��ɫ��Ԫ��
     * 
     */
	@Override
	public void colorSetted(ColorCell colorCell) {
		colorCell.repaint();
	}

	/**
	 * ��ʼ���������
	 * @param centerPane �������
	 * 
	 */
	@Override
	public void initCenterPaneChildren(JPanel centerPane) {
		GUICoreUtils.initCenterPaneChildren(centerPane, this);
	}

	/**
	 * ͸��
	 */
	@Override
	public void doTransparent() {
		this.setColor(null);
	}

	/**
	 * ������ɫ
	 */
	@Override
	public void customButtonPressed() {
		pane = new ColorSelectDetailPane(Color.WHITE);
		ColorSelectDialog.showDialog(DesignerContext.getDesignerFrame(), pane, Color.WHITE, this);
	}
}
